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

public class AuthSessionCommandTest {
	public static final String certificate = "-----BEGIN CERTIFICATE-----\n"
			+ "MIIDBzCCAhigAwIBAwIBADANBgkqhkiG9w0BAQQFADByMQswCQYDVQQGEwJVUzET\n"
			+ "MBEGA1UECBMKQ2FsaWZvcm5pYTESMBAGA1UEBxMJU3Vubnl2YWxlMQ4wDAYDVQQK\n"
			+ "EwVZYWhvbzEVMBMGA1UECxMMQ29ubmVjdGVkIFRWMRMwEQYDVQQDEwpUViBXaWRn\n"
			+ "ZXRzMB4XDTExMDQxODIzMDkzMloXDTEyMDQxNzIzMDkzMlowcjELMAkGA1UEBhMC\n"
			+ "VVMxEzARBgNVBAgTCkNhbGlmb3JuaWExEjAQBgNVBAcTCVN1bm55dmFsZTEOMAwG\n"
			+ "A1UEChMFWWFob28xFTATBgNVBAsTDENvbm5lY3RlZCBUVjETMBEGA1UEAxMKVFYg\n"
			+ "V2lkZ2V0czCB9jANBgkqhkiG9w0BAQEFAAOB5AAwgeACgdgDOIEIEJuyW5rRwI2H\n"
			+ "Bj4epGVYeaz0sWB0T5o745XmkRpJbiNpFtoe0ookjiUDrtnLTHxUz1Xpy24Cccbr\n"
			+ "KlwC95N8ZdR0W+DsBhC1r4zluwCHgAy1h/rwXAUfMdlqNud5XIAaZTZgdO8E3T3N\n"
			+ "/ImRopzy801PvVUIHZY7gvVTAQF2nC6ycmAzKPViLW2paFOB+dOSYPLBURmoMJyK\n"
			+ "E5YzcI+oO8JK/n59siAZj8jCNVa/bpd8Ktmv68T0xWRF8x6yWnxV2fMr2Qucwa6c\n"
			+ "hNQntbeehFpqmOcCAwEAATANBgkqhkiG9w0BAQQFAAOB2QABGdBWpdSVS3gfYXHS\n"
			+ "VfwZ8ErrU6kv/y5OL/1Ug+pnVOcJEwTO8iSAaxJckpD6+v3i5VroRtgVsv0RjZ6I\n"
			+ "7egWdFDGF2Q8Qz8xu4IprKhc5ItBle9hwHai1j5d4y9iOit4+MHJGrIWzhDh4o8h\n"
			+ "AW8c6RPLZ3Jdx7C54qqVcj1dUhgC/gD4SYShnAf2RY64QbNXAGsw9FzaEhpPGfMQ\n"
			+ "XtbrPdDE5QkYueUqwUx7L1NWZJ8d4XbZQZdRvo4ZA86k3qlLQd+lkpiWtSzHZrPV\n" + "IUk138PVETXYt18=\n"
			+ "-----END CERTIFICATE-----";

	@Test
	public void testAuthSessionCommand() {
		AuthSessionCommand asc = new AuthSessionCommand("fake", certificate);
		assertEquals(asc.getType(), "SESSION");
	}

	@Test
	public void testToCommandString() {
		AuthSessionCommand asc = new AuthSessionCommand("fake", certificate);
		assertEquals("SESSION|AUTH|a37336e0dea3bf2e2e8ecafa81dad0e8d69d18e2|END", asc.toCommandString());
	}

	@Test
	public void testGetUserCode() {
		AuthSessionCommand asc = new AuthSessionCommand("fake", certificate);
		assertEquals("fake", asc.getUserCode());
	}

	@Test
	public void testSetUserCode() {
		AuthSessionCommand asc = new AuthSessionCommand("fake", certificate);
		asc.setUserCode("noms");
		assertEquals("noms", asc.getUserCode());
	}

	@Test
	public void testGetCert() {
		AuthSessionCommand asc = new AuthSessionCommand("fake", certificate);
		assertEquals(certificate, asc.getCert());
	}

	@Test
	public void testSetCert() {
		AuthSessionCommand asc = new AuthSessionCommand("fake", certificate);
		asc.setCert("foobar");
		assertEquals("foobar", asc.getCert());
	}

}
