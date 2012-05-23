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
/**
 * 
 */
package com.yahoo.connectedtv.ycommand;

/**
 * Handles responses to SUBSCRIBE commands.
 * 
 * @author jecortez
 * @version $Revision: 1.0 $
 */
public class SubscribedCommand extends AbstractCommand {
	/**
	 * Field serialVersionUID.
	 * (value is -3295890644028390272)
	 */
	private static final long serialVersionUID = -3295890644028390272L;
	/**
	 * Field subject.
	 */
	private String subject;

	/**
	 * @param tokenizedCommand
	
	 * @throws CommandParseException */
	public SubscribedCommand(String[] tokenizedCommand) throws CommandParseException {
		super(tokenizedCommand);
		
		if (tokenizedCommand.length < 2) {
			throw new CommandParseException("Wrong number of tokens");
		} else if(tokenizedCommand[1] == null || tokenizedCommand[1].equals("")){
			throw new CommandParseException("type cannot be null or empty");
		} else if(tokenizedCommand[2] == null || tokenizedCommand[2].equals("")){
			throw new CommandParseException("subject cannot be null or empty");
		}
		
		this.type = tokenizedCommand[1];
		this.subject = tokenizedCommand[2];
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.yahoo.connectedtv.ycommand.AbstractCommand#toCommandString()
	 */
	@Override
	public String toCommandString() {
		return String.format("SUBSCRIBED|%s|%s|END", this.type, this.subject);
	}

	@Override
	public String getPayload() {
		return null;
	}
}
