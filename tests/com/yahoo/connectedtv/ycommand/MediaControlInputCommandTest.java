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

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class MediaControlInputCommandTest {

	@Test
	public void testToCommandString() throws CommandParseException {

		String[] tokenizedCommand = {
				"PUBLISH",
				"INPUT",
				"mediacontrol",
				"{\"state\": 5, \"timeindex\": 123.456, \"duration\":1234.567, \"isControlOnScreen\": true, \"humanDuration\": \"00:01:42\"}",
				"END" };
		MediaControlInputCommand mic = new MediaControlInputCommand(tokenizedCommand);

//		assertEquals(
//				"PUBLISH|INPUT|mediacontrol|{\"duration\":1234.567,\"timeindex\":123.456,\"state\":5,\"humanDuration\":\"00:01:42\",\"isControlOnScreen\":true}|END",
//				mic.toCommandString());
		assertEquals(mic.getMethod(), "PUBLISH");
		assertEquals(mic.getType(), "INPUT");
		assertEquals(mic.getSubject(), "mediacontrol");
		assertEquals(mic.getState(), 5);
		assertEquals(mic.getTimeindex(), 123.456, 5);
		assertEquals(mic.getDuration(), 1234.567, 5);
		assertEquals(mic.isControlOnScreen(), true);

	}

	/*
	 * "mediacontrol." instead of mediacontrol
	 */

	@Test(expected = CommandParseException.class)
	public void testMediaControlInputCommandParserException() throws CommandParseException {
		String[] tokenizedCommand = {
				"PUBLISH",
				"INPUT",
				"mediacontrol.",
				"{\"state\": \"5\", timeindex: \"123456\", \"duration\":1234567, \"isControlOnScreen\": true, \"humanDuration\": \"00:01:42\"}",
				"END" };
		new MediaControlInputCommand(tokenizedCommand);
	}

	/**
	 * Junk chars in payload
	 * 
	 * @throws CommandParseException
	 */
	@Test(expected = CommandParseException.class)
	public void testJunkCharInPayload() throws CommandParseException {
		String[] tokenizedCommand = { "PUBLISH", "INPUT", "mediacontrol", "!@#$%^&*()..|/\\,.?\'{}[]:", "END" };
		new MediaControlInputCommand(tokenizedCommand);
	}

	/**
	 * payload empty
	 * 
	 * @throws CommandParseException
	 */
	@Test(expected = CommandParseException.class)
	public void testEmptyPayload() throws CommandParseException {
		String[] tokenizedCommand = { "PUBLISH", "INPUT", "mediacontrol", "", "END" };
		new MediaControlInputCommand(tokenizedCommand);
	}

}
