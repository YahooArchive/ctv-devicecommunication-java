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

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Various helper methods
 *
 * @author jecortez
 *
 * @version $Revision: 1.0 $
 */
public class Utilities {
	/**
	 * Field DEFAULT_LOGGER_NAMESPACE.
	 * (value is ""com.yahoo.connectedtv.ycommand.defaultlogger"")
	 */
	public final static String DEFAULT_LOGGER_NAMESPACE = "com.yahoo.connectedtv.ycommand.defaultlogger";
	/**
	 * Field logger.
	 */
	private static Logger logger = null;

	/**
	 * Method getLogger.
	 * @return Logger
	 */
	private static Logger getLogger() {
		if (Utilities.logger == null) {
			Utilities.logger = Logger.getLogger(DEFAULT_LOGGER_NAMESPACE);
		}
		return Utilities.logger;
	}

	/**
	 * Method setLogger.
	 * @param logger Logger
	 */
	public static void setLogger(Logger logger) {
		Utilities.logger = logger;
	}

	/**
	 * Method setLoggerEnabled.
	 * @param enable boolean
	 */
	public static void setLoggerEnabled(boolean enable) {
		if (enable) {
			getLogger().setLevel(Level.ALL);
		} else {
			getLogger().setLevel(Level.OFF);
		}
	}

	/**
	 * Method log.
	 * @param message Object
	 */
	public static void log(Object message) {
		if(message == null){
			getLogger().log(Level.INFO, "null");
		}else{
			getLogger().log(Level.INFO, message.toString());
		}

	}

	/**
	 * Method log.
	 * @param level Level
	 * @param message Object
	 */
	public static void log(Level level, Object message) {
		getLogger().log(level, message.toString());
	}

	public static String stringArrayToString(String[] items){
		StringBuffer sb = new StringBuffer("[");
		for (int i = 0; i < items.length; i++) {
			sb.append("'");
			sb.append(items[i]);
			sb.append("'");
			if (i != items.length-1)
				sb.append(",");
		}
		sb.append("]");
		return sb.toString();
	}
}
