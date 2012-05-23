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

/**
 * Abstract class that all Method Call protocol commands inherit from
 * 
 * @author jecortez
 * 
 * @version $Revision: 1.0 $
 */
@SuppressWarnings("serial")
public abstract class AbstractMethodCommand extends AbstractCommand {
	/**
	 * Field callId.
	 */
	protected String callId;
	/**
	 * Field widgetId.
	 */
	protected String widgetId;

	/**
	 * Constructor for AbstractMethodCommand.
	 * 
	 * @param method
	 *            String
	 * @param widgetId
	 *            String
	 * @param category
	 *            String
	 * @param callId
	 *            String
	 */
	public AbstractMethodCommand(String method, String widgetId, String category, String callId) {
		super(method);
		assert (method.equals(AbstractCommand.METHOD_CALL) || method.equals(AbstractCommand.METHOD_RETURN));
		this.widgetId = widgetId;
		this.subject = category;
		this.callId = callId;
	}

	/**
	 * Constructor for AbstractMethodCommand.
	 * 
	 * @param tokenizedCommand
	 *            String[]
	 * @throws CommandParseException
	 */
	public AbstractMethodCommand(String[] tokenizedCommand) throws CommandParseException {
		super(tokenizedCommand);
		this.subject = tokenizedCommand[1];
		this.widgetId = tokenizedCommand[2];

		this.callId = tokenizedCommand[3];
	}

	/**
	 * Method toCommandString.
	 * 
	 * @param payload
	 *            String
	 * @return String
	 */
	public String toCommandString(String payload) {
		StringBuffer sb = new StringBuffer();
		sb.append(this.method);
		sb.append('|');
		sb.append(this.subject);
		sb.append('|');
		sb.append(this.widgetId == null ? "null" : this.widgetId);
		sb.append('|');
		sb.append((payload == null || payload.trim().equals("")) ? "null" : payload);
		sb.append('|');
		sb.append(this.callId);
		sb.append("|END");

		return sb.toString();
	}

	/**
	 * Method getCallId.
	 * 
	 * @return String
	 */
	public String getCallId() {
		return callId;
	}

	/**
	 * Method setCallId.
	 * 
	 * @param callId
	 *            String
	 */
	public void setCallId(String callId) {
		this.callId = callId;
	}

	/**
	 * Method getWidgetID.
	 * 
	 * @return String
	 */
	public String getWidgetID() {
		return widgetId;
	}

	/**
	 * Method setWidgetID.
	 * 
	 * @param widgetId
	 *            String
	 */
	public void setWidgetID(String widgetId) {
		this.widgetId = widgetId;
	}

	/**
	 * Method getName.
	 * 
	 * @return String
	 */
	public String getName() {
		return subject;
	}

	/**
	 * Method setName.
	 * 
	 * @param name
	 *            String
	 */
	public void setName(String name) {
		this.subject = name;
	}

}
