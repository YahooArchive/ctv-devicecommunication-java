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
import org.junit.Before;
import org.junit.Test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.MalformedURLException;
import java.net.URL;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

public class IPCodeResolverTest {
	private String ip = "10.73.145.228";
	private int port = 8099;
	private String code;

	@Before
	public void setUp() throws Exception {
		try {
			String url = "http://bis.tv.widgets.yahoo.com/disc/getCode.php?format=json&ip=10.73.145.228&port=8099&appid=com%2Eyahoo%2Ewidgets%2Etv%2Esettings&appver=1%2E6%2E7&man=TV%20OEM%20Name&brand=TV%20Device%20Brand&class=TV%20Device%20Class&model=TV%20Device%20Model&deviceid=TV%20Device%20Id&hwversion=0%2E1&swversion=0%2E1&region=US&lang=en&yweversion=6%2E0%2E12&yweres=960x540&dockversion=1%2E6%2E10&fwversion=1%2E6%2E37&ts=1318276061&sig=407ac9c09891e7247c87ae2e5f04d3e3";
			URL urlConnection = new URL(url);

			HttpURLConnection conn = (HttpURLConnection) urlConnection.openConnection();
			conn.setConnectTimeout(1000);
			conn.setRequestMethod("GET");

			conn.connect();
			InputStream in = conn.getInputStream();
			BufferedReader reader = new BufferedReader(new InputStreamReader(in));
			String ipText = reader.readLine();
			conn.disconnect();

			JSONObject response = new JSONObject(ipText);
			this.code = response.getString("key");
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (JSONException je) {
			Utilities.log("could not parse json");
		}

		if (this.code == null || this.code.equals("")) {
			fail();
		}

		if (this.code.length() != 4) {
			fail();
		}
	}

	@Test
	public void testIPCodeResolver() {
		IPCodeResolver res = new IPCodeResolver(this.code);
		assertEquals(this.code, res.getCode());
	}

	@Test
	public void testGetAddress() {
		IPCodeResolver res = new IPCodeResolver(this.code);
		InetSocketAddress addr = res.getAddress();
		assertEquals(this.ip, addr.getAddress().getHostAddress());
		assertEquals(this.port, addr.getPort());
	}

}
