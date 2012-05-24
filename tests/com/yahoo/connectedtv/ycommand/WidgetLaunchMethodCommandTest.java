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

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class WidgetLaunchMethodCommandTest {

	@Test
	public void testToCommandString() throws JSONException {
		WidgetLaunchMethodCommand vlmc = new WidgetLaunchMethodCommand(
				"foobar",
				"com.yahoo.widgets.tv.yahoovideo",
				"{\"entries\":[{\"title\":\"Yahoo!\",\"entries\":[{\"bitrate\":300,\"url\":\"http://yahoo.com\"}]}],\"method\":\"launchvideo\"}");

		String[] cmd = vlmc.toCommandString().split("\\|");

		assertEquals("CALL", cmd[0]);
		assertEquals("widgetlaunch", cmd[1]);
		assertEquals("null", cmd[2]);
		JSONObject response = new JSONObject(cmd[3]);
		assertEquals("com.yahoo.widgets.tv.yahoovideo", response.getString("widget_id"));
		assertEquals(
				"{\"entries\":[{\"title\":\"Yahoo!\",\"entries\":[{\"bitrate\":300,\"url\":\"http://yahoo.com\"}]}],\"method\":\"launchvideo\"}",
				response.getString("payload"));
		assertEquals("foobar", cmd[4]);
	}

	// @Test
	// public void testVideolLaunchMethodCommandStringArray() {
	// fail("Not yet implemented");
	// }

	// @Test
	// public void testVideolLaunchMethodCommandStringString() {
	// WidgetLaunchMethodCommand vlmc = new WidgetLaunchMethodCommand("foobar",
	// "http://yahoo.com");
	// assertEquals("CALL", vlmc.getMethod());
	// assertEquals("widgetlaunch", vlmc.getSubject());
	// assertEquals(null, vlmc.getWidgetID());
	// assertEquals("foobar", vlmc.getCallId());
	// VideolLaunchMethodCommand.PlaylistEntry entry = vlmc.getUrls().get(0);
	// assertEquals("http://yahoo.com",
	// entry.urls.get(entry.urls.keySet().toArray()[0]));
	// }
	//
	// @Test
	// public void testVideolLaunchMethodCommandStringStringString() {
	// VideolLaunchMethodCommand vlmc = new VideolLaunchMethodCommand("foobar",
	// "Yahoo!", "http://yahoo.com");
	// assertEquals("CALL", vlmc.getMethod());
	// assertEquals("widgetlaunch", vlmc.getSubject());
	// assertEquals(null, vlmc.getWidgetID());
	// assertEquals("foobar", vlmc.getCallId());
	// VideolLaunchMethodCommand.PlaylistEntry entry = vlmc.getUrls().get(0);
	// assertEquals("http://yahoo.com",
	// entry.urls.get(entry.urls.keySet().toArray()[0]));
	// assertEquals("Yahoo!", entry.title);
	// }
	//
	// @Test
	// public void
	// testVideolLaunchMethodCommandStringStringListOfPlaylistEntry() {
	// VideolLaunchMethodCommand.PlaylistEntry entry = new
	// VideolLaunchMethodCommand.PlaylistEntry("Yahoo!",
	// "http://yahoo.com");
	// List<VideolLaunchMethodCommand.PlaylistEntry> entries = new
	// ArrayList<VideolLaunchMethodCommand.PlaylistEntry>();
	// entries.add(entry);
	// VideolLaunchMethodCommand vlmc = new VideolLaunchMethodCommand("foobar",
	// entries);
	// assertEquals("CALL", vlmc.getMethod());
	// assertEquals("widgetlaunch", vlmc.getSubject());
	// assertEquals(null, vlmc.getWidgetID());
	// assertEquals("foobar", vlmc.getCallId());
	//
	// VideolLaunchMethodCommand.PlaylistEntry newentry = vlmc.getUrls().get(0);
	// assertEquals("http://yahoo.com",
	// newentry.urls.get(newentry.urls.keySet().toArray()[0]));
	// assertEquals("Yahoo!", newentry.title);
	// }

}
