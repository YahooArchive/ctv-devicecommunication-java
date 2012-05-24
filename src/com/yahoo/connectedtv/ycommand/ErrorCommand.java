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

/**
 * Wraps an protocol error command
 * 
 * @author jecortez
 * 
 * @version $Revision: 1.0 $
 */
public class ErrorCommand extends AbstractCommand {
	/**
	 * Field serialVersionUID.
	 * (value is -7812335699456900252)
	 */
	private static final long serialVersionUID = -7812335699456900252L;

	/**
	 * Field CODE_INVALID_APP_KEY.
	 * (value is 1)
	 */
	public final static int CODE_INVALID_APP_KEY = 1;
	/**
	 * Field CODE_INVALID_INSTANCE_KEY.
	 * (value is 2)
	 */
	public final static int CODE_INVALID_INSTANCE_KEY = 2;
	/**
	 * Field CODE_INVALID_SIGNATURE.
	 * (value is 3)
	 */
	public final static int CODE_INVALID_SIGNATURE = 3;
	/**
	 * Field CODE_BAD_MESSAGE.
	 * (value is 101)
	 */
	public final static int CODE_BAD_MESSAGE = 101;

	/**
	 * Field errorCode.
	 */
	private int errorCode;
	/**
	 * Field errorDescription.
	 */
	private String errorDescription;

	/**
	 * Constructor for ErrorCommand.
	 * @param tokenizedCommand String[]
	 * @throws CommandParseException
	 */
	public ErrorCommand(String[] tokenizedCommand) throws CommandParseException {
		super(tokenizedCommand);
		if (tokenizedCommand.length > 2 && tokenizedCommand[0].equals(AbstractCommand.DIRECTIVE_ERROR)) {
			this.type = AbstractCommand.DIRECTIVE_ERROR;
			this.errorCode = Integer.parseInt(tokenizedCommand[1]);
			this.errorDescription = tokenizedCommand[2];
		} else {
			throw new CommandParseException("invalid event");
		}
	}

	/**
	 * Method getErrorCode.
	 * @return int
	 */
	public int getErrorCode() {
		return errorCode;
	}

	/**
	 * Method getErrorDescription.
	 * @return String
	 */
	public String getErrorDescription() {
		return errorDescription;
	}

	/**
	 * Method toCommandString.
	 * @return String
	 */
	public String toCommandString() {
		String code = Integer.toString(this.errorCode);
		if (this.errorCode < 100) {
			code = "0" + code;
			if (this.errorCode < 10) {
				code = "0" + code;
			}
		}
		return String.format("ERROR|%s|%s|END", code, this.errorDescription);
	}

	@Override
	public String getPayload() {
		return null;  //To change body of implemented methods use File | Settings | File Templates.
	}
}
