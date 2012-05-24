/*******************************************************************************
 * Copyright (c) 2012, Yahoo! Inc.
 * All rights reserved.
 *
 * Redistribution and use of this software in source and binary forms,
 * with or without modification, are permitted provided that the following
 * conditions are met:
 *
 *  * Redistributions of source code must retain the above
 *    copyright notice, this list of conditions and the
 *    following disclaimer.
 *
 *  * Redistributions in binary form must reproduce the above
 *    copyright notice, this list of conditions and the
 *    following disclaimer in the documentation and/or other
 *    materials provided with the distribution.
 *
 *  * Neither the name of Yahoo! Inc. nor the names of its
 *    contributors may be used to endorse or promote products
 *    derived from this software without specific prior
 *    written permission of Yahoo! Inc.
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

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Singleton, Defines Schema for commands. Also matches tokenized commands to
 * target classes. Normally used by library only.
 *
 * @author jecortez
 * @version $Revision: 1.0 $
 */
public class CommandSchema {
	/**
	 * Field instance.
	 */
	private static CommandSchema instance;

	/**
	 * Field DELIMITER. (value is ""|"")
	 */
	public final static String DELIMITER = "|";
	/**
	 * Field DELIMITER_ESCAPE. (value is ""PIPE"")
	 */
	public final static String DELIMITER_ESCAPE = "PIPE";
	/**
	 * Field DELIMITER_REGEX. (value is ""\\|"")
	 */
	public final static String DELIMITER_REGEX = "\\|";

	/**
	 * Field schemas.
	 */
	private List<SchemaItem> schemas;

	/**
	 * Field genericSchemas.
	 * These schemas are the catch-all and are not tied to any particular API. These will be matched last
	 */
	private List<SchemaItem> genericSchemas;

	/**
	 * Field subscribeSchemas.
	 */
	private Map<Class<? extends AbstractCommand>, String> subscribeSchemas;
	/**
	 * Field unsubscribeSchemas.
	 */
	private Map<Class<? extends AbstractCommand>, String> unsubscribeSchemas;

	/**
	 * Constructor for CommandSchema.
	 */
	private CommandSchema() {
		schemas = new LinkedList<SchemaItem>();
		schemas.add(new SchemaItem(new String[]{"SESSION", "GRANTED"}, GrantedSessionCommand.class));
		schemas.add(new SchemaItem(new String[]{"SESSION", "STATUS"}, StatusSessionCommand.class));

		schemas.add(new SchemaItem(new String[]{"ERROR"}, ErrorCommand.class));

		schemas.add(new SchemaItem(new String[]{"SUBSCRIBED"}, SubscribedCommand.class));
		schemas.add(new SchemaItem(new String[]{"UNSUBSCRIBED"}, UnsubscribedCommand.class));

		schemas.add(new SchemaItem(new String[]{"PUBLISH", "INPUT", "keyboard"}, KeyboardInputCommand.class));
		schemas.add(new SchemaItem(new String[]{"PUBLISH", "INPUT", "navigation"}, NavigationInputCommand.class));
		schemas.add(new SchemaItem(new String[]{"PUBLISH", "INPUT", "mediacontrol"}, MediaControlInputCommand.class));

		schemas.add(new SchemaItem(new String[]{"PUBLISH", "SERVICE", "widgetlist"}, WidgetListServiceCommand.class));
		schemas.add(new SchemaItem(new String[]{"PUBLISH", "SERVICE", "medialaunch"}, MediaLaunchServiceCommand.class));

		schemas.add(new SchemaItem(new String[]{"CALL", "widgetLaunch"}, WidgetLaunchMethodCommand.class));
		schemas.add(new SchemaItem(new String[]{"RETURN", "widgetLaunch"}, WidgetLaunchMethodCommand.class));

		// these are triggered last!
		genericSchemas = new LinkedList<SchemaItem>();
		genericSchemas.add(new SchemaItem(new String[]{"PUBLISH", "WIDGET"}, WidgetCommand.class));
		genericSchemas.add(new SchemaItem(new String[]{"CALL"}, WidgetMethodCommand.class));
		genericSchemas.add(new SchemaItem(new String[]{"RETURN"}, WidgetMethodCommand.class));

		subscribeSchemas = new HashMap<Class<? extends AbstractCommand>, String>();
		subscribeSchemas.put(KeyboardInputCommand.class, "SUBSCRIBE|INPUT|keyboard|END");
		subscribeSchemas.put(NavigationInputCommand.class, "SUBSCRIBE|INPUT|navigation|END");
		subscribeSchemas.put(MediaControlInputCommand.class, "SUBSCRIBE|INPUT|mediacontrol|END");
		subscribeSchemas.put(WidgetListServiceCommand.class, "SUBSCRIBE|SERVICE|widgetlist|END");
		subscribeSchemas.put(MediaLaunchServiceCommand.class, "SUBSCRIBE|SERVICE|medialaunch|END");

		subscribeSchemas.put(WidgetCommand.class, "SUBSCRIBE|WIDGET|%s|END");

		unsubscribeSchemas = new HashMap<Class<? extends AbstractCommand>, String>();
		unsubscribeSchemas.put(KeyboardInputCommand.class, "UNSUBSCRIBE|INPUT|keyboard|END");
		unsubscribeSchemas.put(NavigationInputCommand.class, "UNSUBSCRIBE|INPUT|navigation|END");
		unsubscribeSchemas.put(MediaControlInputCommand.class, "UNSUBSCRIBE|INPUT|mediacontrol|END");
		unsubscribeSchemas.put(WidgetListServiceCommand.class, "UNSUBSCRIBE|SERVICE|widgetlist|END");
		unsubscribeSchemas.put(MediaLaunchServiceCommand.class, "UNSUBSCRIBE|SERVICE|medialaunch|END");

		unsubscribeSchemas.put(WidgetCommand.class, "UNSUBSCRIBE|WIDGET|%s|END");
	}

	/**
	 * Defines an new schema and target for example:
	 * <p/>
	 * {@code new SchemaItem(new String[] "PUBLISH", "INPUT", "navigation"},
	 * NavigationInputCommand.class) }
	 *
	 * @author jecortez
	 */
	public static class SchemaItem {
		/**
		 * Field schema.
		 */
		public String[] schema;
		/**
		 * Field target.
		 */
		public Class<? extends AbstractCommand> target;

		/**
		 * Constructor for SchemaItem.
		 *
		 * @param schema String[]
		 * @param target Class<? extends AbstractCommand>
		 */
		public SchemaItem(String[] schema, Class<? extends AbstractCommand> target) {
			this.schema = schema;
			this.target = target;
		}

		public String toString() {
			StringBuffer sb = new StringBuffer("{schema:");
			sb.append(Utilities.stringArrayToString(this.schema));
			sb.append(", target:");
			sb.append(this.target.getCanonicalName());
			sb.append("}");
			return sb.toString();
		}
	}


	/**
	 * @return Singleton object
	 */
	public static CommandSchema getInstance() {
		if (instance == null) {
			instance = new CommandSchema();
		}
		return instance;
	}

	/**
	 * @param item Add custom item to the schema
	 */
	public synchronized void addSchema(SchemaItem item) {
		schemas.add(item);
	}

	/**
	 * Match a tokenized command to a target
	 *
	 * @param tokenizedCommand tokenized string command
	 * @return Class matched target
	 */
	public synchronized Class<? extends AbstractCommand> matchCommand(String[] tokenizedCommand) {
		Class<? extends AbstractCommand> matchedCommand = matchSchemaList(tokenizedCommand, this.schemas);
		if(matchedCommand == null){
			matchedCommand = matchSchemaList(tokenizedCommand, this.genericSchemas);
		}
		return matchedCommand;
	}

	private Class<? extends AbstractCommand> matchSchemaList(String[] tokenizedCommand, List<SchemaItem> schemaList) {
		Class<? extends AbstractCommand> matchedCommand = null;
		for (SchemaItem item : schemaList) {
			for (int i = 0; i <= item.schema.length; i++) {
				if (i == item.schema.length) {// got to the end, so must be a match
					matchedCommand = item.target;
					break;
				} else if (!item.schema[i].equals(tokenizedCommand[i])) { // doesn't match!
					break;
				}
			}
			if (matchedCommand != null) {
				break;
			}
		}
		return matchedCommand;
	}

	/**
	 * Get the string to subscribe to a certain class type
	 *
	 * @param commandClass Command type to subscribe to
	 * @return subscribe string or null, String may have String.format place
	 *         holders
	 */
	public String getSubscribeString(Class<? extends AbstractCommand> commandClass) {
		return this.subscribeSchemas.get(commandClass);
	}

	/**
	 * Add a new string to the unsubscribe string list
	 *
	 * @param commandClass
	 * @param unsubscribeString String may have String.format place holders
	 */
	public void addSubscribeString(Class<? extends AbstractCommand> commandClass, String unsubscribeString) {
		this.subscribeSchemas.put(commandClass, unsubscribeString);
	}

	/**
	 * Add a new string to the subscribe string list
	 *
	 * @param commandClass
	 * @param subscribeString String may have String.format place holders
	 */
	public void addUnsubscribeString(Class<? extends AbstractCommand> commandClass, String subscribeString) {
		this.unsubscribeSchemas.put(commandClass, subscribeString);
	}

	/**
	 * Get the string to unsubscribe to a certain class type
	 *
	 * @param commandClass Command type to unsubscribe from
	 * @return unsubscribe string or null, String may have String.format place
	 *         holders
	 */
	public String getUnsubscribeString(Class<? extends AbstractCommand> commandClass) {
		return this.unsubscribeSchemas.get(commandClass);
	}

	/**
	 * Escape a command
	 *
	 * @param payload
	 * @return escaped payload
	 */
	public static String unescapeString(String payload) {
		return payload.replaceAll(CommandSchema.DELIMITER_ESCAPE, CommandSchema.DELIMITER);
	}
}
