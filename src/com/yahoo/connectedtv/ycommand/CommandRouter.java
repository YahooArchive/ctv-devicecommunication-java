/*******************************************************************************
 * Copyright (c) 2011, Yahoo! Inc.
 * All rights reserved.
 *
 * Redistribution and use of this software in source and binary forms, 
 * with or without modification, are permitted provided that the following 
 * conditions are met:
 *
 * * Redistributions of source code must retain the above
 *   copyright notice, this list of conditions and the
 *   following disclaimer.
 *
 * * Redistributions in binary form must reproduce the above
 *   copyright notice, this list of conditions and the
 *   following disclaimer in the documentation and/or other
 *   materials provided with the distribution.
 *
 * * Neither the name of Yahoo! Inc. nor the names of its
 *   contributors may be used to endorse or promote products
 *   derived from this software without specific prior
 *   written permission of Yahoo! Inc.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS 
 * IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED 
 * TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A 
 * PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT 
 * OWNER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, 
 * SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT 
 * LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, 
 * DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY 
 * THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT 
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE 
 * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 ******************************************************************************/
package com.yahoo.connectedtv.ycommand;

import java.io.IOException;
import java.util.*;

/**
 * Hub for all incoming and outgoing commands.
 *
 * @author jecortez
 * @version $Revision: 1.0 $
 */
public class CommandRouter implements IConnectionHandler {
	/**
	 * Field subscribers.
	 */
	private Map<Class<? extends AbstractCommand>, List<ICommandSubscriber>> subscribers;
	/**
	 * Field rmcSubscribers - Remote Method Call Subscribers
	 */
	private Map<String, List<ICommandSubscriber>> rmcSubscribers;
	/**
	 * Field conn.
	 */
	private Connection conn;

	/**
	 * Constructor for CommandRouter.
	 */
	public CommandRouter() {
		this.subscribers = new HashMap<Class<? extends AbstractCommand>, List<ICommandSubscriber>>();
		this.rmcSubscribers = new HashMap<String, List<ICommandSubscriber>>();
	}

	/**
	 * @param conn
	 */
	public void setConnection(Connection conn) {
		this.conn = conn;
	}

	/**
	 * Method onConnectionClosed.
	 *
	 * @param connection Connection
	 * @see com.yahoo.connectedtv.ycommand.IConnectionHandler#onConnectionClosed(Connection)
	 */
	public void onConnectionClosed(Connection connection) {
		Utilities.log("Connection broken " + connection.getAddr().toString() + " ------------");
		Collection<List<ICommandSubscriber>> commandSubscriberLists = subscribers.values();
		if (commandSubscriberLists != null) {
			for (List<ICommandSubscriber> csl : commandSubscriberLists) {
				for (ICommandSubscriber cs : csl) {
					cs.onConnectionLost(connection);
				}
				csl.clear();
			}
			subscribers.clear();
		}
	}

	/**
	 * Method onDataAvailable.
	 *
	 * @param command String
	 * @throws IOException
	 * @throws CommandParseException
	 * @see com.yahoo.connectedtv.ycommand.IConnectionHandler#onDataAvailable(String)
	 */
	public void onDataAvailable(String command) throws IOException, CommandParseException {
		AbstractCommand cmd = CommandParser.parse(command);
		if (cmd != null) {
			List<ICommandSubscriber> commandSubscriberList = subscribers.get(cmd.getClass());
			if (cmd instanceof AbstractMethodCommand) {
				Utilities.log(this.generateRmcSubscriberKey(((AbstractMethodCommand) cmd).widgetId, cmd.subject,
						((AbstractMethodCommand) cmd).callId));
				commandSubscriberList = this.rmcSubscribers.get(this.generateRmcSubscriberKey(
						((AbstractMethodCommand) cmd).widgetId, cmd.subject, ((AbstractMethodCommand) cmd).callId));
			} else {
				commandSubscriberList = subscribers.get(cmd.getClass());
			}
			if (commandSubscriberList != null) {
				for (ICommandSubscriber cs : commandSubscriberList) {
					cs.onCommandReceived(cmd);
				}
			} else {
				Utilities.log("No one cares about:" + cmd.getClass().getCanonicalName());
			}
		}
	}

	/**
	 * @param command
	 * @throws IOException
	 * @throws InterruptedException
	 */
	public void publishCommand(AbstractCommand command) throws InterruptedException {
		conn.write(command.toCommandString());
	}

	/**
	 * @param rawCommand
	 * @throws IOException
	 */
	public void publishCommand(String rawCommand) throws InterruptedException {
		conn.write(rawCommand);
	}

	/**
	 * @param commandType
	 * @param params      Object[]
	 * @throws IOException
	 */
	public void subscribeTo(Class<? extends AbstractCommand> commandType, Object... params) throws InterruptedException {
		String command = CommandSchema.getInstance().getSubscribeString(commandType);
		if (command != null && !command.equals("")) {
			this.publishCommand(String.format(command, params));
		} else {
			Utilities.log("No command to subscribe to:" + commandType.getCanonicalName());
		}
	}

	/**
	 * @param commandType
	 * @param params      Object[]
	 * @throws IOException
	 */
	private void unsubscribeFrom(Class<? extends AbstractCommand> commandType, Object... params) throws InterruptedException {
		String command = CommandSchema.getInstance().getUnsubscribeString(commandType);
		if (command != null && !command.equals("")) {
			this.publishCommand(String.format(command, params));
		} else {
			Utilities.log("Don't know how to subscribe to:" + commandType.getCanonicalName());
		}
	}

	/**
	 * @throws IOException
	 * @throws InterruptedException
	 */
	public void reestablishConnection() throws IOException, InterruptedException {
		this.conn.establish();
	}

	/**
	 * @param widgetID
	 * @param subject
	 * @param callId
	 * @return
	 */
	public String generateRmcSubscriberKey(String widgetID, String subject, String callId) {
		return widgetID + "-" + subject + "-" + callId;
	}

	/**
	 * @param subscriber
	 */
	public void registerRMCCommandSubscriber(String widgetID, String subject, String callId, ICommandSubscriber subscriber) {
		List<ICommandSubscriber> commandSubscriberList = this.rmcSubscribers.get(this.generateRmcSubscriberKey(
				widgetID, subject, callId));
		if (commandSubscriberList == null) {
			commandSubscriberList = new LinkedList<ICommandSubscriber>();
			Utilities.log(this.generateRmcSubscriberKey(widgetID, subject, callId));
			this.rmcSubscribers.put(this.generateRmcSubscriberKey(widgetID, subject, callId), commandSubscriberList);
		}
		if (!commandSubscriberList.contains(subscriber)) {
			commandSubscriberList.add(subscriber);
		} else {
			Utilities.log("Preventing subscriber from subscribing twice to an RMC");
		}

	}

	//allow unregistering with a key as well
	public void unregisterRMCCommandSubscriber(String key, ICommandSubscriber subscriber) {
		List<ICommandSubscriber> commandSubscriberList = this.rmcSubscribers.get(key);
		if (commandSubscriberList != null && commandSubscriberList.contains(subscriber)) {
			commandSubscriberList.remove(subscriber);

			if(commandSubscriberList.isEmpty()){
				rmcSubscribers.remove(key);
			}
		} else {
			Utilities.log("unsubscribing from rmc you are not subscribed to!");
		}
	}

	public void unregisterCommandSubscriber(String widgetID, String subject, String callId, ICommandSubscriber subscriber) {
		String key = this.generateRmcSubscriberKey(widgetID, subject, callId);
		unregisterRMCCommandSubscriber(key, subscriber);
	}

	/**
	 * @param subscriber
	 * @param commandType
	 */
	public void registerCommandSubscriber(ICommandSubscriber subscriber, Class<? extends AbstractCommand> commandType, Object... params) throws InterruptedException {
		Utilities.log("Router, Registering :" + commandType.getCanonicalName());
		List<ICommandSubscriber> commandSubscriberList = subscribers.get(commandType);
		if (commandSubscriberList == null) {
			commandSubscriberList = new LinkedList<ICommandSubscriber>();
			subscribers.put(commandType, commandSubscriberList);
		}
		if (!commandSubscriberList.contains(subscriber)) {
			commandSubscriberList.add(subscriber);
			this.subscribeTo(commandType, params);
		} else {
			Utilities.log("Preventing subscriber from subscribing twice to an event");
		}
	}

	public void unregisterCommandSubscriber(ICommandSubscriber subscriber, Class<? extends AbstractCommand> commandType, Object... params) throws InterruptedException {
		Utilities.log("Router, Unregistering :" + commandType.getCanonicalName());
		List<ICommandSubscriber> commandSubscriberList = subscribers.get(commandType);
		if (commandSubscriberList != null && commandSubscriberList.contains(subscriber)) {
			commandSubscriberList.remove(subscriber);

			if(commandSubscriberList.isEmpty()){
				subscribers.remove(commandType);
				this.unsubscribeFrom(commandType, params);
			}
		} else {
			Utilities.log("unsubscribing from event you are not subscribed to!");
		}
	}
}
