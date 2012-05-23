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
 * Wraps an protocol media transport controls state change command
 * 
 * @author jecortez
 * 
 * @version $Revision: 1.0 $
 */
public class MediaControlInputCommand extends AbstractInputCommand {
	/**
	 * Field serialVersionUID.
	 * (value is 4302545704172360159)
	 */
	private static final long serialVersionUID = 4302545704172360159L;

	/**
	 * Field STATE_INIT.
	 * (value is -1)
	 */
	public static final int STATE_INIT = -1;
	/**
	 * Field STATE_PLAY.
	 * (value is 0)
	 */
	public static final int STATE_PLAY = 0;
	/**
	 * Field STATE_PAUSE.
	 * (value is 1)
	 */
	public static final int STATE_PAUSE = 1;
	/**
	 * Field STATE_FASTFORWARD.
	 * (value is 2)
	 */
	public static final int STATE_FASTFORWARD = 2;
	/**
	 * Field STATE_FORWARD.
	 * (value is 2)
	 */
	public static final int STATE_FORWARD = 2;
	/**
	 * Field STATE_FF.
	 * (value is 2)
	 */
	public static final int STATE_FF = 2;
	/**
	 * Field STATE_REWIND.
	 * (value is 3)
	 */
	public static final int STATE_REWIND = 3;
	/**
	 * Field STATE_STOP.
	 * (value is 4)
	 */
	public static final int STATE_STOP = 4;
	/**
	 * Field STATE_BUFFERING.
	 * (value is 5)
	 */
	public static final int STATE_BUFFERING = 5;
	/**
	 * Field STATE_BUFFEREMPTY.
	 * (value is 6)
	 */
	public static final int STATE_BUFFEREMPTY = 6;
	/**
	 * Field STATE_INFOLOADED.
	 * (value is 7)
	 */
	public static final int STATE_INFOLOADED = 7;
	/**
	 * Field STATE_EOF.
	 * (value is 8)
	 */
	public static final int STATE_EOF = 8;
	/**
	 * Field STATE_UNKNOWN.
	 * (value is 9)
	 */
	public static final int STATE_UNKNOWN = 9;
	/**
	 * Field STATE_ERROR.
	 * (value is 10)
	 */
	public static final int STATE_ERROR = 10;

	/**
	 * Field state.
	 */
	private int state = -1;
	/**
	 * Field timeindex.
	 */
	private double timeindex;
	/**
	 * Field duration.
	 */
	private double duration;
	/**
	 * Field isControlOnScreen.
	 */
	private boolean isControlOnScreen;
	/**
	 * Field humanDuration.
	 */
	private String humanDuration;
	
	private String title;

	/**
	 * Constructor for MediaControlInputCommand.
	 * @param tokenizedCommand String[]
	 * @throws CommandParseException
	 */
	public MediaControlInputCommand(String[] tokenizedCommand) throws CommandParseException {
		super(tokenizedCommand);
		this.subject = "mediacontrol";

		if (tokenizedCommand.length > 2 && tokenizedCommand[2].equals("mediacontrol")) {
			this.jsonToMembers(this.parseJSONObject(tokenizedCommand[3]));
		} else {
			throw new CommandParseException("not mediacontrol event");
		}
	}

	/**
	 * Constructor for MediaControlInputCommand.
	 * @param timeIndex long
	 */
	public MediaControlInputCommand(long timeIndex) {
		super();
		this.subject = "mediacontrol";
		this.timeindex = timeIndex;
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
			if (payload.has("state")) {
				this.state = payload.getInt("state");
			}
			if (payload.has("timeindex")) {
				this.timeindex = payload.getDouble("timeindex");
			}
			if (payload.has("duration")) {
				this.duration = payload.getDouble("duration");
			}
			if (payload.has("isControlOnScreen")) {
				this.isControlOnScreen = payload.getBoolean("isControlOnScreen");
			}
			if (payload.has("humanDuration")) {
				this.setHumanDuration(payload.getString("humanDuration"));
			}
			if (payload.has("title")) {
				this.title = payload.getString("title");
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
			retval.put("state", this.state);
			retval.put("title", this.state);
			retval.put("timeindex", this.timeindex);
			retval.put("duration", this.duration);
			retval.put("isControlOnScreen", this.isControlOnScreen);
			retval.put("humanDuration", this.humanDuration);
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
		sb.append("|INPUT|mediacontrol|");
		sb.append(this.membersToJSON());
		sb.append("|END");

		return sb.toString();
	}

	@Override
	public String getPayload() {
		return this.membersToJSON().toString();
	}

	/**
	 * Method getState.
	 * @return int
	 */
	public int getState() {
		return state;
	}

	/**
	 * Method setState.
	 * @param state int
	 */
	public void setState(int state) {
		this.state = state;
	}

	/**
	 * Method getTimeindex.
	 * @return double
	 */
	public double getTimeindex() {
		return timeindex;
	}

	/**
	 * Method setTimeindex.
	 * @param timeindex double
	 */
	public void setTimeindex(double timeindex) {
		this.timeindex = timeindex;
	}

	/**
	 * Method getDuration.
	 * @return double
	 */
	public double getDuration() {
		return duration;
	}

	/**
	 * Method setDuration.
	 * @param duration double
	 */
	public void setDuration(double duration) {
		this.duration = duration;
	}

	/**
	 * Method isControlOnScreen.
	 * @return boolean
	 */
	public boolean isControlOnScreen() {
		return isControlOnScreen;
	}

	/**
	 * Method setControlOnScreen.
	 * @param isControlOnScreen boolean
	 */
	public void setControlOnScreen(boolean isControlOnScreen) {
		this.isControlOnScreen = isControlOnScreen;
	}

	/**
	 * Method setHumanDuration.
	 * @param humanDuration String
	 */
	public void setHumanDuration(String humanDuration) {
		this.humanDuration = humanDuration;
	}

	/**
	 * Method getHumanDuration.
	 * @return String
	 */
	public String getHumanDuration() {
		return humanDuration;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}
}
