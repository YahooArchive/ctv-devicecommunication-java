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

import org.hamcrest.CoreMatchers;
import org.junit.Test;

import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

public class CommandParserTest {

	@Test
	public void testSessionGrantedParse() throws CommandParseException {
		String command = "SESSION|GRANTED|1234567|END";
		assertThat(CommandParser.parse(command), CoreMatchers.instanceOf(GrantedSessionCommand.class));
	}

	@Test
	public void testErrorParse() throws CommandParseException {
		String command = "ERROR|003|Invalid Signature|END";
		assertThat(CommandParser.parse(command), CoreMatchers.instanceOf(ErrorCommand.class));
	}

	@Test
	public void testKeyboardParse() throws CommandParseException {
		String command = "PUBLISH|INPUT|keyboard|{\"totalValue\": \"hi\", \"key\": \"\", \"keyCode\":13, \"cursorPosition\": 2, \"modifiers\": {\"control\": false, \"shift\": false, \"alt\": false}, \"layoutType\": 1}|END";
		assertThat(CommandParser.parse(command), CoreMatchers.instanceOf(KeyboardInputCommand.class));
	}

	@Test
	public void testMediaControlParse() throws CommandParseException {
		String command = "PUBLISH|INPUT|mediacontrol|{\"state\": \"5\", timeindex: \"123456\", duration:1234567, isControlOnScreen: true, humanDuration: \"00:01:42\"}|END";
		assertThat(CommandParser.parse(command), CoreMatchers.instanceOf(MediaControlInputCommand.class));
	}

	@Test
	public void testWidgetParse() throws CommandParseException {
		String command = "PUBLISH|WIDGET|com.yahoo.connectedtv.tests|{}|END";
		assertThat(CommandParser.parse(command), CoreMatchers.instanceOf(WidgetCommand.class));
	}

	@Test
	public void testSubscribedParse() throws CommandParseException {
		String command = "SUBSCRIBED|INPUT|keyboard|END";
		assertThat(CommandParser.parse(command), CoreMatchers.instanceOf(SubscribedCommand.class));
	}

	@Test
	public void testUnsubscribedParse() throws CommandParseException {
		String command = "UNSUBSCRIBED|INPUT|keyboard|END";
		assertThat(CommandParser.parse(command), CoreMatchers.instanceOf(UnsubscribedCommand.class));
	}

	@Test(expected = CommandParseException.class)
	public void testJunkDataParse() throws CommandParseException {
		String command = "0Ha1Th3r!|END><!@#$%^&*()..|/\\,.?\"'{}[]:~`|eNd|PUBLISH|SUBSCRIBE`";
		CommandParser.parse(command);
		assertTrue(false);// shouldn't have gotten here
	}

}
