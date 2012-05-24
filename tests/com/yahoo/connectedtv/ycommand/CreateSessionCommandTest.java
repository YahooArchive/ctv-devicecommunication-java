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

public class CreateSessionCommandTest {

	@Test
	public void testToCommandString() {
		CreateSessionCommand csc = new CreateSessionCommand("myappID", "myConsumerKey", "sshhhsecret", "Unit Test");
		assertEquals("SESSION|CREATE|app_id=myappID&consumer_key=myConsumerKey&secret=6e16a84ece0be3950fd984290004ae2358258cfb|Unit Test|END", csc.toCommandString());
	}

	@Test
	public void testGetAppKey() {
		CreateSessionCommand csc = new CreateSessionCommand("myappID", "myConsumerKey", "sshhhsecret", "Unit Test");
		assertEquals("app_id=myappID&consumer_key=myConsumerKey&secret=6e16a84ece0be3950fd984290004ae2358258cfb", csc.getAppKey());
	}

	@Test
	public void testGetName() {
		CreateSessionCommand csc = new CreateSessionCommand("myappID", "myConsumerKey", "sshhhsecret", "Unit Test");
		assertEquals("Unit Test", csc.getName());
	}

	@Test
	public void testSetName() {
		CreateSessionCommand csc = new CreateSessionCommand("myappID", "myConsumerKey", "sshhhsecret", "Unit Test");
		csc.setName("dumbo");
		assertEquals("dumbo", csc.getName());
	}

	@Test
	public void testGetAppId() {
		CreateSessionCommand csc = new CreateSessionCommand("myappID", "myConsumerKey", "sshhhsecret", "Unit Test");
		assertEquals("myappID", csc.getAppId());
	}

	@Test
	public void testSetAppId() {
		CreateSessionCommand csc = new CreateSessionCommand("myappID", "myConsumerKey", "sshhhsecret", "Unit Test");
		csc.setAppId("harry potter");
		assertEquals("harry potter", csc.getAppId());
	}

	@Test
	public void testGetConsumerKey() {
		CreateSessionCommand csc = new CreateSessionCommand("myappID", "myConsumerKey", "sshhhsecret", "Unit Test");
		assertEquals("myConsumerKey", csc.getConsumerKey());
	}

	@Test
	public void testSetConsumerKey() {
		CreateSessionCommand csc = new CreateSessionCommand("myappID", "myConsumerKey", "sshhhsecret", "Unit Test");
		csc.setConsumerKey("luna");
		assertEquals("luna", csc.getConsumerKey());
	}

	@Test
	public void testGetSecret() {
		CreateSessionCommand csc = new CreateSessionCommand("myappID", "myConsumerKey", "sshhhsecret", "Unit Test");
		assertEquals("sshhhsecret", csc.getSecret());
	}

	@Test
	public void testSetSecret() {
		CreateSessionCommand csc = new CreateSessionCommand("myappID", "myConsumerKey", "sshhhsecret", "Unit Test");
		csc.setSecret("donttell");
		assertEquals("donttell", csc.getSecret());
	}

}
