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

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class KeyboardInputCommandTest {

	@Test
	public void testToCommandString() throws CommandParseException {

		int layoutType = 1;
		String totalValue = "";
		int cursorPosition = 0;
		String key = "";
		int keyCode = -1;
		boolean shiftModifier = false;
		boolean altModifier = false;
		boolean controlModifier = false;
		boolean isKeyboardOnScreen = false;

		KeyboardInputCommand kic = new KeyboardInputCommand(layoutType, totalValue, cursorPosition, key, keyCode,
				shiftModifier, altModifier, controlModifier);
		assertEquals("PUBLISH|INPUT|keyboard|{\"keyCode\":" + keyCode + ",\"cursorPosition\":" + cursorPosition
				+ ",\"modifiers\":{\"control\":" + controlModifier + ",\"alt\":" + altModifier + ",\"shift\":"
				+ shiftModifier + "},\"key\":\"\",\"totalValue\":\"\",\"layoutType\":" + layoutType + "}|END",
				kic.toCommandString());

		String[] tokenizedCommand = {
				"PUBLISH",
				"INPUT",
				"keyboard",
				"{\"totalValue\": \"foobar\", \"key\": \"o\", \"keyCode\":13, \"isKeyboardOnScreen\": true, \"cursorPosition\": 2, \"modifiers\": {\"control\": false, \"shift\": false, \"alt\": false}, \"layoutType\": 1}",
				"END" };
		KeyboardInputCommand tkic = new KeyboardInputCommand(tokenizedCommand);

		assertEquals("PUBLISH|INPUT|keyboard|{\"keyCode\":13,\"cursorPosition\":" + "2"
				+ ",\"modifiers\":{\"control\":" + controlModifier + ",\"alt\":" + altModifier + ",\"shift\":"
				+ shiftModifier + "},\"key\":\"o\",\"totalValue\":\"foobar\",\"layoutType\":" + layoutType + "}|END",
				tkic.toCommandString());

	}

	@Test
	public void testGetterSetter() throws CommandParseException {

		String[] tokenizedCommand = {
				"PUBLISH",
				"INPUT",
				"keyboard",
				"{\"totalValue\": \"foobar\", \"key\": \"o\", \"keyCode\":13, \"isKeyboardOnScreen\": true, \"cursorPosition\": 2, \"modifiers\": {\"control\": false, \"shift\": false, \"alt\": false}, \"layoutType\": 1}",
				"END" };
		KeyboardInputCommand kic = new KeyboardInputCommand(tokenizedCommand);
		kic.setAltModifier(false);
		kic.setControlModifier(true);
		kic.setCursorPosition(3);
		kic.setKey("1234");
		kic.setKeyboardOnScreen(true);
		kic.setKeyCode(34);
		kic.setLayoutType(3);
		kic.setShiftModifier(true);
		kic.setTotalValue("total");
		assertEquals(false, kic.isAltModifier());
		assertEquals(true, kic.isControlModifier());
		assertEquals(true, kic.isShiftModifier());
		assertEquals("1234", kic.getKey());
		assertEquals(34, kic.getKeyCode());
		assertEquals(3, kic.getCursorPosition());
		assertEquals(true, kic.isKeyboardOnScreen());
		assertEquals(3, kic.getLayoutType());
		assertEquals("total", kic.getTotalValue());

	}

	@Test(expected = CommandParseException.class)
	public void testKeyboardInputWrongToken() throws CommandParseException {
		String[] tokenizedCommand = { "PUBLISH", "INPUT", "keyboard.", "keyCode", };
		new KeyboardInputCommand(tokenizedCommand);
	}

	@Test(expected = CommandParseException.class)
	public void testPayloadEmptyString() throws CommandParseException {
		String[] tokenizedCommand = { "PUBLISH", "INPUT", "keyboard", "", "END" };
		new KeyboardInputCommand(tokenizedCommand);
	}

	@Test(expected = CommandParseException.class)
	public void testPayloadNull() throws CommandParseException {
		String[] tokenizedCommand = { "PUBLISH", "INPUT", "keyboard", null, "END" };
		new KeyboardInputCommand(tokenizedCommand);
	}

	@Test(expected = CommandParseException.class)
	public void testJunkCharInPayload() throws CommandParseException {
		String[] tokenizedCommand = { "PUBLISH", "INPUT", "keyboard", "!@#$%^&*()..|/\\,.?\'{}[]:", "END" };
		new KeyboardInputCommand(tokenizedCommand);
	}

}
