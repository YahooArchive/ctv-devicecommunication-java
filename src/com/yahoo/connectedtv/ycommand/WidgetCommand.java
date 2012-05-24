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


/**
 * Wraps an protocol widget communication command
 * <p>
 * Example Commands:
 * 
 * <pre>
 * {@literal
 * PUBLISH|WIDGET|com.yahoo.tv.news|{"article_title": "Foobar","article_id": "1234"}|END
 * }
 * </pre>
 * 
 * </p>
 * 
 * @author jecortez
 * 
 * @version $Revision: 1.0 $
 */
public class WidgetCommand extends AbstractCommand {
	/**
	 * Field serialVersionUID. (value is -3852542835125242998)
	 */
	private static final long serialVersionUID = -3852542835125242998L;

	/**
	 * Field widgetId.
	 */
	protected String widgetId;
	/**
	 * Field jsonPayload.
	 */
	protected Object payload;

	/**
	 * Constructor for WidgetCommand.
	 * 
	 * @param tokenizedCommand
	 *            String[]
	 * @throws CommandParseException
	 */
	public WidgetCommand(String[] tokenizedCommand) throws CommandParseException {
		super(tokenizedCommand);

		if (tokenizedCommand.length <= 2) {
			throw new CommandParseException("Wrong number of tokens");
		} else if (tokenizedCommand[1] == null || tokenizedCommand[1].equals("")) {
			throw new CommandParseException("type cannot be null or empty");
		} else if (tokenizedCommand[2] == null || tokenizedCommand[2].equals("")) {
			throw new CommandParseException("subject cannot be null or empty");
		} else if (tokenizedCommand[3] == null) {
			throw new CommandParseException("subject cannot be null");
		} else if (tokenizedCommand[2].equals(AbstractCommand.TYPE_WIDGET)) {
			throw new CommandParseException("invalid event");
		}

		this.type = AbstractCommand.TYPE_WIDGET;
		this.widgetId = tokenizedCommand[2];
		this.payload = tokenizedCommand[3];
	}

	/**
	 * Constructor for WidgetCommand.
	 * 
	 * @param widgetId
	 *            String
	 * @param json_payload
	 *            JSONObject
	 */
	public WidgetCommand(String widgetId, Object json_payload) {
		super("PUBLISH");
		this.type = AbstractCommand.TYPE_WIDGET;
		this.widgetId = widgetId;
		this.payload = json_payload;
	}

	/**
	 * Method toCommandString.
	 * 
	 * @return String
	 */
	@Override
	public String toCommandString() {
		StringBuffer sb = new StringBuffer();
		sb.append(this.method);
		sb.append("|WIDGET|");
		sb.append(widgetId);
		sb.append('|');
		sb.append(this.payload.toString());
		sb.append("|END");

		return sb.toString();
	}

	/**
	 * Method getWidgetId.
	 * 
	 * @return String
	 */
	public String getWidgetId() {
		return widgetId;
	}

	/**
	 * Method setWidgetId.
	 * 
	 * @param widgetId
	 *            String
	 */
	public void setWidgetId(String widgetId) {
		this.widgetId = widgetId;
	}

	/**
	 * Method getJsonPayload.
	 * 
	 * @return JSONObject
	 */
	public String getPayload() {
		return this.payload.toString();
	}

	/**
	 * Method setJson_payload.
	 * 
	 * @param json_payload
	 *            JSONObject
	 */
	public void setPayload(Object json_payload) {
		this.payload = json_payload;
	}
}
