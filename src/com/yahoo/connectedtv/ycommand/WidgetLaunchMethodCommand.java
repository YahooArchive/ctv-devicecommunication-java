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

/**
 * Wraps an Widget Launch Command
 * <p>
 * Example Command:
 * 
 * <pre>
 * {@literal
 * CALL|widgetlaunch|null|{"widget_id":"com.yahoo.widgets.tv.settings"}|3534245|END
 * 
 * RETURN|widgetlaunch|com.yahoo.widgets.tv.settings|{status:"launched"}|3534245|END
 * }
 * </pre>
 * 
 * </p>
 * 
 * @author jecortez
 * 
 * @version $Revision: 1.0 $
 */
public class WidgetLaunchMethodCommand extends AbstractMethodCommand {
	/**
	 * Field serialVersionUID.
	 * (value is 4422898638031538717)
	 */
	private static final long serialVersionUID = 4422898638031538717L;

	/**
	 * Field CATEGORY.
	 * (value is ""widgetlaunch"")
	 */
	public final static String CATEGORY = "widgetlaunch";

	/**
	 * Field launchingWidgetId.
	 */
	private String launchingWidgetId;
	/**
	 * Field payload.
	 */
	protected String payload;

	// private boolean installWidget = false;
	// private String minVersion = "";

	/**
	 * Constructor for WidgetLaunchMethodCommand.
	 * @param tokenizedCommand String[]
	 * @throws CommandParseException
	 */
	public WidgetLaunchMethodCommand(String[] tokenizedCommand) throws CommandParseException {
		super(tokenizedCommand);
		System.out.println("not doing anything with incoming widget launch");
	}

	/**
	 * @param callId
	 *            unique id that will match responses to this call
	 * @param launchingWidgetId
	 *            widgetID of the widget you want to launch
	 * @param payload
	 *            payload to be sent to the widget upon launch
	 */
	public WidgetLaunchMethodCommand(String callId, String launchingWidgetId, String payload) {
		super("CALL", null, CATEGORY, callId);
		this.launchingWidgetId = launchingWidgetId;
		this.payload = payload;
	}

	/**
	 * @param callId
	 *            unique id that will match responses to this call
	 * @param launchingWidgetId
	 *            widgetID of the widget you want to launch
	 */
	public WidgetLaunchMethodCommand(String callId, String launchingWidgetId) {
		this(callId, launchingWidgetId, null);
	}

	/**
	 * Method membersToJSON.
	 * @return JSONObject
	 */
	protected JSONObject membersToJSON() {
		JSONObject retval = new JSONObject();

		try {
			retval.put("widget_id", this.launchingWidgetId);
			if (payload != null) {
				retval.put("payload", this.payload.toString());
			}
		} catch (JSONException e) {
			Utilities.log("ERROR CREATING JSON MESSAGE");
		}

		return retval;
	}

	/**
	 * Method toCommandString.
	 * @return String
	 */
	@Override
	public String toCommandString() {
		return super.toCommandString(this.membersToJSON().toString());
	}

	@Override
	public String getPayload() {
		return this.membersToJSON().toString();
	}

}
