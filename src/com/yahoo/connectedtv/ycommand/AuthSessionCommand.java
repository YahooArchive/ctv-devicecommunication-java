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

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.logging.Level;

/**
 * Wraps an protocol authorize session command
 * <p>
 * Example Command:
 * 
 * <pre>
 * {@literal
 * SESSION|AUTH|a44520388013d49fcdb16a6b7129bffb4e54ce99|END
 * }
 * </pre>
 * 
 * </p>
 * 
 * @author jecortez
 * 
 * @version $Revision: 1.0 $
 */
public class AuthSessionCommand extends AbstractSessionCommand {
	/**
	 * Field serialVersionUID.
	 * (value is -8967152467731112563)
	 */
	private static final long serialVersionUID = -8967152467731112563L;
	/**
	 * Field userCode.
	 */
	private String userCode;
	/**
	 * Field cert.
	 */
	private String cert;

	/**
	 * @param code
	 *            code the user has inputed on screen
	
	 * @param certificate String
	 */
	public AuthSessionCommand(String code, String certificate) {
		super("SESSION");
		this.userCode = code;
		this.cert = certificate;
	}

	/**
	 * Method generateSignature.
	 * @return String
	 */
	private String generateSignature() {
		if (this.cert.equals("") || this.cert == null) {
			Utilities.log(Level.SEVERE, "NO CERTIFICATE FOUND!");
		}
		// generate a SHA1-HMAC signature
		String sig = "";
		try {
			SecretKeySpec key = new SecretKeySpec(this.userCode.getBytes("UTF-8"), "HmacSHA1");
			Mac mac = Mac.getInstance("HmacSHA1");
			mac.init(key);

			sig = this.byteArrayToHex(mac.doFinal(this.cert.getBytes("UTF-8")));
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (InvalidKeyException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}

		return sig;
	}

	/**
	 * Method byteArrayToHex.
	 * @param a byte[]
	 * @return String
	 */
	private String byteArrayToHex(byte[] a) {
		int hn, ln, cx;
		String hexDigitChars = "0123456789abcdef";
		StringBuffer buf = new StringBuffer(a.length * 2);
		for (cx = 0; cx < a.length; cx++) {
			hn = ((int) (a[cx]) & 0x00ff) / 16;
			ln = ((int) (a[cx]) & 0x000f);
			buf.append(hexDigitChars.charAt(hn));
			buf.append(hexDigitChars.charAt(ln));
		}
		return buf.toString();
	}

	/**
	 * Method toCommandString.
	 * @return String
	 */
	@Override
	public String toCommandString() {
		return String.format("SESSION|AUTH|%s|END", this.generateSignature());
	}

	@Override
	public String getPayload() {
		return null;  //To change body of implemented methods use File | Settings | File Templates.
	}

	/**
	 * Method getUserCode.
	 * @return String
	 */
	public String getUserCode() {
		return userCode;
	}

	/**
	 * Method setUserCode.
	 * @param userCode String
	 */
	public void setUserCode(String userCode) {
		this.userCode = userCode;
	}

	/**
	 * Method getSignature.
	 * @return String
	 */
	public String getSignature() {
		return this.generateSignature();
	}

	/**
	 * Method getCert.
	 * @return String
	 */
	public String getCert() {
		return cert;
	}

	/**
	 * Method setCert.
	 * @param cert String
	 */
	public void setCert(String cert) {
		this.cert = cert;
	}
}
