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
