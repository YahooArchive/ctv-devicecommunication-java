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

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

/**
 * Base Command class.
 * 
 * @author jecortez
 * 
 * @version $Revision: 1.0 $
 */
public abstract class AbstractCommand implements Serializable {
	/**
	 * Field serialVersionUID.
	 * (value is 1)
	 */
	private static final long serialVersionUID = 1L; // must be overridden
	/**
	 * Field METHOD_PUBLISH.
	 * (value is ""PUBLISH"")
	 */
	public final static String METHOD_PUBLISH = "PUBLISH";
	/**
	 * Field METHOD_SUBSCRIBE.
	 * (value is ""SUBSCRIBE"")
	 */
	public final static String METHOD_SUBSCRIBE = "SUBSCRIBE";
	/**
	 * Field METHOD_SUBSCRIBED.
	 * (value is ""SUBSCRIBED"")
	 */
	public final static String METHOD_SUBSCRIBED = "SUBSCRIBED";
	/**
	 * Field METHOD_UNSUBSCRIBE.
	 * (value is ""UNSUBSCRIBE"")
	 */
	public final static String METHOD_UNSUBSCRIBE = "UNSUBSCRIBE";
	/**
	 * Field METHOD_UNSUBSCRIBED.
	 * (value is ""UNSUBSCRIBED"")
	 */
	public final static String METHOD_UNSUBSCRIBED = "UNSUBSCRIBED";
	/**
	 * Field METHOD_CALL.
	 * (value is ""CALL"")
	 */
	public final static String METHOD_CALL = "CALL";
	/**
	 * Field METHOD_RETURN.
	 * (value is ""RETURN"")
	 */
	public final static String METHOD_RETURN = "RETURN";
	/**
	 * Field TYPE_INPUT.
	 * (value is ""INPUT"")
	 */
	public final static String TYPE_INPUT = "INPUT";
	/**
	 * Field TYPE_WIDGET.
	 * (value is ""WIDGET"")
	 */
	public final static String TYPE_WIDGET = "WIDGET";
	/**
	 * Field TYPE_SERVICE.
	 * (value is ""SERVICE"")
	 */
	public final static String TYPE_SERVICE = "SERVICE";

	/**
	 * Field DIRECTIVE_SESSION.
	 * (value is ""SESSION"")
	 */
	public final static String DIRECTIVE_SESSION = "SESSION";
	/**
	 * Field DIRECTIVE_ERROR.
	 * (value is ""ERROR"")
	 */
	public final static String DIRECTIVE_ERROR = "ERROR";

	/**
	 * Field method.
	 */
	protected String method;
	/**
	 * Field type.
	 */
	protected String type;
	/**
	 * Field subject.
	 */
	protected String subject;

	/**
	 * create a command from an already-tokenized command
	 * 
	 * @param tokenizedCommand
	 *            a tokenized command (typically from command Parser)
	
	 * @throws CommandParseException */
	public AbstractCommand(String[] tokenizedCommand) throws CommandParseException {
		if (tokenizedCommand != null 
				&& tokenizedCommand.length > 0
				&& (tokenizedCommand[0].equals(AbstractCommand.METHOD_PUBLISH)
						|| tokenizedCommand[0].equals(AbstractCommand.METHOD_SUBSCRIBE)
						|| tokenizedCommand[0].equals(AbstractCommand.METHOD_SUBSCRIBED)
						|| tokenizedCommand[0].equals(AbstractCommand.METHOD_UNSUBSCRIBE)
						|| tokenizedCommand[0].equals(AbstractCommand.METHOD_UNSUBSCRIBED)
						|| tokenizedCommand[0].equals(AbstractCommand.METHOD_CALL)
						|| tokenizedCommand[0].equals(AbstractCommand.METHOD_RETURN)
						|| tokenizedCommand[0].equals(AbstractCommand.DIRECTIVE_SESSION) || tokenizedCommand[0]
						.equals(AbstractCommand.DIRECTIVE_ERROR))) {
			this.method = tokenizedCommand[0];
		} else {
			throw new CommandParseException("unknown event");
		}

	}

	/**
	 * A bare-bones command
	 * 
	 * @param method
	 */
	public AbstractCommand(String method) {
		this.method = method;
	}

	/**
	 * Method toCommandString.
	 * @return String
	 */
	public abstract String toCommandString();

		/**
	 * Method toCommandString.
	 * @return String
	 */
	public abstract String getPayload();

	/**
	 * Method parseJSONObject.
	 * @param payload String
	 * @return JSONObject
	 */
	protected JSONObject parseJSONObject(String payload) {
		try {
			return new JSONObject(payload);
		} catch (JSONException je) {
			Utilities.log("could not parse json");
			Utilities.log(payload);
			return null;
		}
	}

	/**
	 * Method getMethod.
	 * @return String
	 */
	public String getMethod() {
		return method;
	}

	/**
	 * Method setMethod.
	 * @param method String
	 */
	public void setMethod(String method) {
		this.method = method;
	}

	/**
	 * Method getType.
	 * @return String
	 */
	public String getType() {
		return type;
	}

	/**
	 * Method setType.
	 * @param type String
	 */
	public void setType(String type) {
		this.type = type;
	}

	/**
	 * Method getSubject.
	 * @return String
	 */
	public String getSubject() {
		return subject;
	}

	/**
	 * Method setSubject.
	 * @param subject String
	 */
	public void setSubject(String subject) {
		this.subject = subject;
	}

	/**
	 * Method writeObject.
	 * @param out ObjectOutputStream
	 * @throws IOException
	 */
	protected void writeObject(ObjectOutputStream out) throws IOException {
		// perform the default serialization for all non-transient, non-static
		// fields
		out.defaultWriteObject();
	}

	/**
	 * Method readObject.
	 * @param in ObjectInputStream
	 * @throws IOException
	 * @throws ClassNotFoundException
	 */
	protected void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
		// always perform the default de-serialization first
		in.defaultReadObject();
	}
}
