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

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

/**
 * Parses commands and converts them into sub-classes of Command
 * 
 * @author jecortez
 * 
 * @version $Revision: 1.0 $
 */
public class CommandParser {
	/**
	 * Parses message string and returns the correct Command object
	 * @param command String
	 * @return subclassed object of Command
	 * @throws CommandParseException
	 */
	public static AbstractCommand parse(String command) throws CommandParseException {
		// Utilities.log("PARSING: " + command);
		String[] tokenizedCommand = command.split(CommandSchema.DELIMITER_REGEX);
		AbstractCommand newCommand = null;
		if (tokenizedCommand.length > 2) {
			Class<? extends AbstractCommand> cClass = CommandSchema.getInstance().matchCommand(tokenizedCommand);
			if (cClass != null) {
				@SuppressWarnings("rawtypes")
				Class[] argsClass = new Class[1];
				argsClass[0] = tokenizedCommand.getClass();
				try {
					// Get the constructor of the class matching the
					// arguments passed
					Constructor<? extends AbstractCommand> con = cClass.getConstructor(argsClass);
					Object[] args = new Object[1];
					args[0] = tokenizedCommand;

					newCommand = con.newInstance(args);
				} catch (IllegalArgumentException e) {
					throw new CommandParseException(e.getClass().getSimpleName() + ":" + e.getMessage());
				} catch (InstantiationException e) {
					throw new CommandParseException(e.getClass().getSimpleName() + ":" + e.getMessage());
				} catch (IllegalAccessException e) {
					throw new CommandParseException(e.getClass().getSimpleName() + ":" + e.getMessage());
				} catch (InvocationTargetException e) {
					e.printStackTrace();
					throw new CommandParseException(e.getClass().getSimpleName() + ":" + e.getMessage());
				} catch (SecurityException e) {
					throw new CommandParseException(e.getClass().getSimpleName() + ":" + e.getMessage());
				} catch (NoSuchMethodException e) {
					throw new CommandParseException(e.getClass().getSimpleName() + ":" + e.getMessage());
				}
			}
		}
		if (newCommand != null) {
			Utilities.log("CREATED NEW: " + newCommand.getClass().getCanonicalName());
		} else {
			throw new CommandParseException("UNKNWON COMMAND: " + command);
		}
		return newCommand;
	}

}
