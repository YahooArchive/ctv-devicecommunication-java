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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Uses the Yahoo! backend service to fetch an IP and port. The user must go to
 * the Profile widget, then system settings, then "Pair Device" to view the
 * code. This code is then used to lookup the IP and port of the TV.
 * 
 * @author jecortez
 * @version $Revision: 1.0 $
 */
public class IPCodeResolver {
	/**
	 * Field DEFAULT_TIMEOUT.
	 * (value is 1000)
	 */
	public static final int DEFAULT_TIMEOUT = 1000;
	/**
	 * Field BASE_URL.
	 */
	public static String BASE_URL = "http://bis.tv.widgets.yahoo.com/disc";

	/**
	 * Field code.
	 */
	private String code;

	/**
	 * @param code
	 *            Code the user has viewed on the TV screen
	 */
	public IPCodeResolver(String code) {
		this.code = code;
	}

	/**
	 * Method fetchIPText.
	 * @param url String
	 * @param timeout int
	 * @return String
	 */
	private String fetchIPText(String url, int timeout) {
		String ipText = null;
		try {
			URL urlConnection = new URL(url);

			HttpURLConnection conn = (HttpURLConnection) urlConnection.openConnection();
			conn.setConnectTimeout(timeout);
			conn.setRequestMethod("GET");

			conn.connect();
			InputStream in = conn.getInputStream();
			BufferedReader reader = new BufferedReader(new InputStreamReader(in));

			StringBuffer ipTextBuffer = new StringBuffer();
			String line;
			while ((line = reader.readLine()) != null) {
				ipTextBuffer.append(line);
			}
			ipText = ipTextBuffer.toString();

			conn.disconnect();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return ipText;
	}

	/**
	 * Get the address of the TV, this does a synchronous HTTP fetch
	 * 
	
	 * @return Address of the TV */
	public InetSocketAddress getAddress() {
		return this.getAddress(DEFAULT_TIMEOUT);
	}

	/**
	 * Get the address of the TV, this does a synchronous HTTP fetch
	 * 
	 * @param timeout
	 *            in milliseconds for the HTTP fetch
	
	 * @return Address of the TV */
	public InetSocketAddress getAddress(int timeout) {
		String ipText = this.fetchIPText(BASE_URL + "/getIP.php?format=json&code=" + code, timeout);
		if (ipText != null) {
			String ip = null;
			int port = -1;
			try {
				JSONObject response = new JSONObject(ipText);
				ip = response.getString("ip");
				port = response.getInt("port");
			} catch (JSONException je) {
				Utilities.log("could not parse json");
				Utilities.log(ipText);
			}

			if (ip != null && port > 0) {
				return new InetSocketAddress(ip, port);
			}
		}
		return null;
	}

	/**
	 * Method getCode.
	 * @return String
	 */
	public String getCode() {
		return code;
	}

	/**
	 * Method setCode.
	 * @param code String
	 */
	public void setCode(String code) {
		this.code = code;
	}
}
