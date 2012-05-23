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

public class NavigationInputCommandTest {

	@Test
	public void testToCommandString() throws CommandParseException {
		String command = "press_enter";
		NavigationInputCommand nic = new NavigationInputCommand(command);
		assertEquals("PUBLISH|INPUT|navigation|press_enter|END", nic.toCommandString());

		String[] tokenizedCommand = { "PUBLISH", "INPUT", "navigation", command };
		NavigationInputCommand tnic = new NavigationInputCommand(tokenizedCommand);
		assertEquals("PUBLISH|INPUT|navigation|press_enter|END", tnic.toCommandString());

	}

	@Test
	public void testNavigationInputCommandStringString() {
		String command = "press_enter";
		NavigationInputCommand nic = new NavigationInputCommand(command);
		assertEquals(nic.getMethod(), "PUBLISH");
		assertEquals(nic.getType(), "INPUT");
		assertEquals(nic.getSubject(), "navigation");
		assertEquals(nic.getCommand(), command);
	}

	@Test
	public void testNavigationInputCommandStringArray() throws CommandParseException {
		String command = "press_enter";
		String[] tokenizedCommand = { "PUBLISH", "INPUT", "navigation", command };
		NavigationInputCommand nic = new NavigationInputCommand(tokenizedCommand);
		assertEquals(nic.getMethod(), "PUBLISH");
		assertEquals(nic.getType(), "INPUT");
		assertEquals(nic.getSubject(), "navigation");
		assertEquals(nic.getCommand(), command);
	}

	@Test
	public void testGetCommand() {
		String command = "press_enter";
		NavigationInputCommand nic = new NavigationInputCommand(command);
		assertEquals(nic.getCommand(), command);
	}

	@Test
	public void testSetCommand() {
		String command = "press_left";
		NavigationInputCommand nic = new NavigationInputCommand("press_enter");
		nic.setCommand(command);
		assertEquals(nic.getCommand(), command);
	}

}
