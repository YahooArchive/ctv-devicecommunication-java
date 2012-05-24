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
 * Wraps an protocol check status command. This checks the current authorization
 * status of a an instance id.
 * <p>
 * Example Command:
 * 
 * <pre>
 * {@literal
 * SESSION|STATUS|a44520388013d49fcdb16a6b7129bffb4e54ce99|END
 * 
 * SESSION|STATUS|allowed|END
 * }
 * </pre>
 * 
 * </p>
 * 
 * @author jecortez
 * 
 * @version $Revision: 1.0 $
 */
public class StatusSessionCommand extends AbstractSessionCommand {
	/**
	 * Field serialVersionUID.
	 * (value is 4880444037815074358)
	 */
	private static final long serialVersionUID = 4880444037815074358L;

	/**
	 * Field status.
	 */
	String status;

	/**
	 * Constructor for StatusSessionCommand.
	 * @param tokenizedCommand String[]
	 * @throws CommandParseException
	 */
	public StatusSessionCommand(String[] tokenizedCommand) throws CommandParseException {
		super(tokenizedCommand);
		if (tokenizedCommand.length > 2) {
			this.status = tokenizedCommand[2];
		}
	}

	/**
	 * Method toCommandString.
	 * @return String
	 */
	public String toCommandString() {
		return String.format("SESSION|STATUS|%s|END", this.status);
	}

	@Override
	public String getPayload() {
		return status;
	}

	/**
	 * Get the unique ID assigned to the client device
	 * 
	
	 * @return unique id */
	public String getStatus() {
		return status;
	}

	/**
	 * Method setStatus.
	 * @param status String
	 */
	public void setStatus(String status) {
		this.status = status;
	}

	/**
	 * Method isAuthorized.
	 * @return boolean
	 */
	public boolean isAuthorized() {
		return status.equals("allowed");
	}
}
