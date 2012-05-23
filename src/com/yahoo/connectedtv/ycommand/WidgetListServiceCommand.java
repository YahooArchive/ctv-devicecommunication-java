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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Wraps an widget list command.
 * <p>
 * Example Command:
 * 
 * <pre>
 * {@literal
 * PUBLISH|SERVICE|widgetlist|[{id: "com.yahoo.widgets.tv.flickr",name: "Flickr", iconURL: "http://l.yimg.com/g/images/logo_home.png.v2"}]|END
 * }
 * </pre>
 * 
 * </p>
 * 
 * @author jecortez
 * 
 * @version $Revision: 1.0 $
 */
public class WidgetListServiceCommand extends AbstractServiceCommand {
	/**
	 * Field serialVersionUID.
	 * (value is -1121055518342662807)
	 */
	private static final long serialVersionUID = -1121055518342662807L;

	/**
	 * Field CATEGORY.
	 * (value is ""widgetlist"")
	 */
	public final static String CATEGORY = "widgetlist";

	/**
	 * Field widgets.
	 */
	private List<Map<String, String>> widgets;

	/**
	 * Constructor for WidgetListServiceCommand.
	 * @param tokenizedCommand String[]
	 * @throws CommandParseException
	 */
	public WidgetListServiceCommand(String[] tokenizedCommand) throws CommandParseException {
		super(tokenizedCommand);

		if (tokenizedCommand.length > 2 && tokenizedCommand[2].equals(CATEGORY)) {
			this.subject = WidgetListServiceCommand.CATEGORY;
			widgets = new LinkedList<Map<String, String>>();
			try {
				this.jsonToMembers(new JSONArray((tokenizedCommand[3])));
			} catch (JSONException e) {
				e.printStackTrace();
				throw new CommandParseException("unable to parse JSON");
			}
		} else {
			throw new CommandParseException("not widgetlist event");
		}
	}

	/**
	 * Method jsonToMembers.
	 * @param payload JSONArray
	 * @throws CommandParseException
	 */
	private void jsonToMembers(JSONArray payload) throws CommandParseException {
		try {
			for (int i = 0; i < payload.length(); i++) {
				JSONObject entry = payload.getJSONObject(i);
				Map<String, String> widget = new HashMap<String, String>();
				if (entry.has("id")) {
					widget.put("widgetID", entry.getString("id"));
				}

				if (entry.has("name")) {
					widget.put("name", entry.getString("name"));
				}

				if (entry.has("iconURL")) {
					widget.put("iconURL", entry.getString("iconURL"));
				}
				widgets.add(widget);
			}
		} catch (JSONException e) {
			e.printStackTrace();
			throw new CommandParseException("Error parsing JSON");
		}
	}

	/**
	 * Method membersToJSON.
	 * @return JSONArray
	 */
	private JSONArray membersToJSON() {
		JSONArray retaval = new JSONArray();
		for(Map<String, String> item: this.widgets){
			try {
				JSONObject itemObj = new JSONObject();
				itemObj.put("id", item.get("widgetID"));
				itemObj.put("name", item.get("name"));
				itemObj.put("iconURL", item.get("iconURL"));
				retaval.put(itemObj);
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		return retaval;
	}

	/**
	 * Method toCommandString.
	 * @return String
	 */
	public String toCommandString() {
		return String.format("PUBLISH|SERVICE|widgetlist|%s|END", this.membersToJSON().toString());
	}

	@Override
	public String getPayload() {
		return this.membersToJSON().toString();
	}

	/**
	 * Method getWidgets.
	 * @return List<Map<String,String>>
	 */
	public List<Map<String, String>> getWidgets() {
		return widgets;
	}

	/**
	 * Method setWidgets.
	 * @param widgets List<Map<String,String>>
	 */
	public void setWidgets(List<Map<String, String>> widgets) {
		this.widgets = widgets;
	}

}
