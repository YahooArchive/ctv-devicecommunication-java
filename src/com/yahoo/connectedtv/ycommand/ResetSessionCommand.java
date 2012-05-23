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

/**
 * Wraps an protocol reset session command
 * <p>
 * Example Command:
 * 
 * <pre>
 * {@literal
 * SESSION|RESET|f477f88f-a4a5-4d03-9bdd-70599d683985|END
 * }
 * </pre>
 * 
 * </p>
 * 
 * @author jecortez
 * 
 * @version $Revision: 1.0 $
 */
public class ResetSessionCommand extends AbstractSessionCommand {
	/**
	 * Field serialVersionUID.
	 * (value is -4620256791049568967)
	 */
	private static final long serialVersionUID = -4620256791049568967L;
	/**
	 * Field instanceId.
	 */
	String instanceId;

	/**
	 * @param instanceId
	 *            The instanceId that was previously issued from a Session
	 *            Create
	 */
	public ResetSessionCommand(String instanceId) {
		super("SESSION");
		assert (instanceId != null);
		this.instanceId = instanceId;
	}

	/**
	 * Method toCommandString.
	 * @return String
	 */
	@Override
	public String toCommandString() {
		return String.format("SESSION|RESET|%s|END", this.instanceId);
	}

	@Override
	public String getPayload() {
		return instanceId;
	}
}
