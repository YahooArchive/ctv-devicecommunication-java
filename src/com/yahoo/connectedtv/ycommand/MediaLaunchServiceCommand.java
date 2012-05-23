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
 * Wraps an incoming media Launch Command
 * <p>
 * Example Command:
 * 
 * <pre>
 * {@literal
 * PUBLISH|SERVICE|medialaunch|{"mimetype":"text/url","name":"Yahoo! Homepage", "url":"http://yahoo.com"}|END
 * }
 * </pre>
 * 
 * </p>
 * 
 * @author jecortez
 * 
 * @version $Revision: 1.0 $
 */
public class MediaLaunchServiceCommand extends AbstractServiceCommand {
	/**
	 * Field serialVersionUID.
	 * (value is 4422898638031538717)
	 */
	private static final long serialVersionUID = 4422898638031538717L;
	/**
	 * Field CATEGORY.
	 * (value is ""medialaunch"")
	 */
	public final static String CATEGORY = "medialaunch";

	/**
	 * Field mimetype.
	 */
	private String mimetype;
	/**
	 * Field url.
	 */
	private String url;
	/**
	 * Field title.
	 */
	private String title;

	/**
	 * Constructor for MediaLaunchServiceCommand.
	 * @param tokenizedCommand String[]
	 * @throws CommandParseException
	 */
	public MediaLaunchServiceCommand(String[] tokenizedCommand) throws CommandParseException {
		super(tokenizedCommand);
		this.subject = "medialaunch";
		this.jsonToMembers(this.parseJSONObject(tokenizedCommand[3]));
	}

	/**
	 * Method jsonToMembers.
	 * @param payload JSONObject
	 * @throws CommandParseException
	 */
	private void jsonToMembers(JSONObject payload) throws CommandParseException {
		if (payload == null) {
			throw new CommandParseException("no payload found");
		}
		try {
			if (payload.has("url")) {
				this.url = payload.getString("url");
			}
			if (payload.has("title")) {
				this.title = payload.getString("title");
			}
			if (payload.has("mimetype")) {
				this.mimetype = payload.getString("mimetype");
			}
		} catch (JSONException e) {
			e.printStackTrace();
			throw new CommandParseException("Error parsing JSON");
		}
	}

	/**
	 * Method membersToJSON.
	 * @return JSONObject
	 */
	private JSONObject membersToJSON() {
		JSONObject retval = new JSONObject();

		try {
			retval.put("url", this.url);
			retval.put("title", this.title);
			retval.put("mimetype", this.mimetype);
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
		StringBuffer sb = new StringBuffer();
		sb.append(this.method);
		sb.append("|SERVICE|medialaunch|");
		sb.append(this.membersToJSON());
		sb.append("|END");

		return sb.toString();
	}

	@Override
	public String getPayload() {
		return this.membersToJSON().toString();
	}

	/**
	 * Method getUrl.
	 * @return String
	 */
	public String getUrl() {
		return url;
	}

	/**
	 * Method setUrl.
	 * @param url String
	 */
	public void setUrl(String url) {
		this.url = url;
	}

	/**
	 * Method getTitle.
	 * @return String
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * Method setTitle.
	 * @param title String
	 */
	public void setTitle(String title) {
		this.title = title;
	}

	/**
	 * Method getMimetype.
	 * @return String
	 */
	public String getMimetype() {
		return mimetype;
	}

	/**
	 * Method setMimetype.
	 * @param mimetype String
	 */
	public void setMimetype(String mimetype) {
		this.mimetype = mimetype;
	}

}
