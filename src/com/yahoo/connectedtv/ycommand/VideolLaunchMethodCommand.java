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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Wraps an outgoing video Launch Command. This is a helper to generate a
 * WidgetLaunchCommand. Incoming responses to these calls will be of type
 * WidgetLaunchCommand.
 * <p>
 * Example Command:
 * 
 * <pre>
 * {@literal
 * CALL|widgetlaunch|null|{"payload":"{\"entries\":[{\"entries\":[{\"bitrate\":300,\"url\":\"http:\\\/\\\/209.191.64.78\\\/videos\\\/mp4-libx264-756k-480x852-libfaac-128.mp4\"}],\"title\":\"\"}],\"method\":\"launchvideo\"}","widget_id":"com.yahoo.widgets.tv.yahoovideo"}|foobar|END
 * }
 * </pre>
 * 
 * </p>
 * 
 * @author jecortez
 * 
 * @version $Revision: 1.0 $
 */
public class VideolLaunchMethodCommand extends WidgetLaunchMethodCommand {
	/**
	 * Field serialVersionUID.
	 * (value is 4422898638031538717)
	 */
	private static final long serialVersionUID = 4422898638031538717L;
	/**
	 * Field WIDGET_ID.
	 * (value is ""com.yahoo.widgets.tv.yahoovideo"")
	 */
	public final static String WIDGET_ID = "com.yahoo.widgets.tv.yahoovideo";

	/**
	 * Field urls.
	 */
	private List<PlaylistEntry> urls;

	/**
	 * @param callId
	 *            unique id that will match responses to this call
	 * @param url
	 *            video url
	 */
	public VideolLaunchMethodCommand(String callId, String url) {
		this(callId, "", url);
	}

	/**
	 * @param callId
	 *            unique id that will match responses to this call
	 * @param title
	 *            video titles
	 * @param url
	 *            video url
	 */
	public VideolLaunchMethodCommand(String callId, String title, String url) {
		super(callId, WIDGET_ID, null);
		this.urls = new ArrayList<PlaylistEntry>();
		this.urls.add(new PlaylistEntry(title, url));
	}

	/**
	 * @param callId
	 *            unique id that will match responses to this call
	
	 * @param entries List<PlaylistEntry>
	 */
	public VideolLaunchMethodCommand(String callId, List<PlaylistEntry> entries) {
		super(callId, WIDGET_ID, null);
		this.urls = entries;
	}

	/**
	 * Defines a single video entry. This mirrors how playlists work on the
	 * Yahoo! TV App platform. An entry has a title and a list of urls at
	 * different bitrates. If there is only 1 url, if is common proactice to set
	 * the bitrate to 300.
	 * @author jecortez
	 */
	public static class PlaylistEntry {
		/**
		 * Field urls.
		 */
		public Map<Integer, String> urls;
		/**
		 * Field title.
		 */
		public String title;

		/**
		 * Empty playlist with empty title
		 */
		public PlaylistEntry() {
			this.title = "";
			this.urls = new HashMap<Integer, String>();
		}

		/**
		 * Defaults bitrate to 300, and sets an empty title
		 * 
		 * @param url
		 *            video url.
		 */
		public PlaylistEntry(String url) {
			this();
			this.urls.put(300, url);
		}

		/**
		 * Defaults bitrate to 300
		 * 
		 * @param title
		 *            video title
		 * @param url
		 *            video url
		 */
		public PlaylistEntry(String title, String url) {
			this();
			this.title = title;
			this.urls.put(300, url);
		}

		/**
		 * Constructor for PlaylistEntry.
		 * @param jsonEntry JSONObject
		 * @throws JSONException
		 */
		public PlaylistEntry(JSONObject jsonEntry) throws JSONException {
			if (jsonEntry.has("title")) {
				this.title = jsonEntry.getString("title");
			} else {
				this.title = "";
			}
			if (jsonEntry.has("entries")) {
				JSONArray entries = jsonEntry.getJSONArray("entries");
				for (int i = 0; i < entries.length(); i++) {
					JSONObject entry = entries.getJSONObject(i);
					if (entry.has("bitrate") && entry.has("url")) {
						this.urls.put(Integer.valueOf(entry.getInt("bitrate")), entry.getString("url"));
					}
				}
			}
		}

		/**
		 * Method toJSON.
		 * @return JSONObject
		 * @throws JSONException
		 */
		public JSONObject toJSON() throws JSONException {
			JSONObject retval = new JSONObject();
			retval.put("title", this.title);

			JSONArray entries = new JSONArray();
			for (Integer bitrate : this.urls.keySet()) {
				JSONObject videoEntry = new JSONObject();
				videoEntry.put("bitrate", bitrate.intValue());
				videoEntry.put("url", this.urls.get(bitrate));
				entries.put(videoEntry);
			}
			retval.put("entries", entries);

			return retval;
		}
	}

	/**
	 * Method membersToJSON.
	 * @return JSONObject
	 */
	protected JSONObject membersToJSON() {
		JSONObject container = new JSONObject();

		JSONArray entries = new JSONArray();

		try {
			container.put("method", "launchvideo");

			for (PlaylistEntry entry : this.urls) {
				entries.put(entry.toJSON());
			}

			container.put("entries", entries);

		} catch (JSONException e) {
			Utilities.log("ERROR CREATING JSON MESSAGE");
		}

		this.payload = container.toString();

		return super.membersToJSON();
	}

	/**
	 * Method toCommandString.
	 * @return String
	 */
	@Override
	public String toCommandString() {
		return super.toCommandString(this.membersToJSON().toString());
	}

	/**
	 * Method getUrls.
	 * @return List<PlaylistEntry>
	 */
	public List<PlaylistEntry> getUrls() {
		return this.urls;
	}

	/**
	 * Method setUrls.
	 * @param urls List<PlaylistEntry>
	 */
	public void setUrls(List<PlaylistEntry> urls) {
		this.urls = urls;
	}

	/**
	 * Add a single url to the current playlist. Default bitrate to 300 and
	 * title to empty
	 * 
	 * @param url
	 *            video url
	 */
	public void addUrl(String url) {
		this.urls.add(new PlaylistEntry(url));
	}

	/**
	 * Add a single url to the current playlist. Default bitrate to 300
	 * 
	 * @param title
	 *            video title
	 * @param url
	 *            video url
	 */
	public void addUrl(String title, String url) {
		this.urls.add(new PlaylistEntry(title, url));
	}
}
