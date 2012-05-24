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
 * Wraps a Session granted protocol command
 * 
 * @author jecortez
 * 
 * @version $Revision: 1.0 $
 */
public class GrantedSessionCommand extends AbstractSessionCommand {
	/**
	 * Field serialVersionUID.
	 * (value is 5428563604893800002)
	 */
	private static final long serialVersionUID = 5428563604893800002L;
	/**
	 * Field instanceId.
	 */
	String instanceId;

	/**
	 * Constructor for GrantedSessionCommand.
	 * @param tokenizedCommand String[]
	 * @throws CommandParseException
	 */
	public GrantedSessionCommand(String[] tokenizedCommand) throws CommandParseException {
		super(tokenizedCommand);
		if (tokenizedCommand.length > 2) {
			this.instanceId = tokenizedCommand[2];
		} else {
			throw new CommandParseException("No instanceID");
		}
	}

	/**
	 * Get the unique ID assigned to the client device
	 * 
	
	 * @return unique id */
	public String getInstanceId() {
		return instanceId;
	}

	/**
	 * Method setInstanceId.
	 * @param instanceId String
	 */
	public void setInstanceId(String instanceId) {
		this.instanceId = instanceId;
	}

	/**
	 * Method toCommandString.
	 * @return String
	 */
	public String toCommandString() {
		return String.format("SESSION|GRANTED|%s|END", this.instanceId);
	}

	@Override
	public String getPayload() {
		return instanceId;
	}

}
