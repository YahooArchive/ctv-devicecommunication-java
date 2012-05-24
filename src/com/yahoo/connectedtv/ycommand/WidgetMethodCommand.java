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
 * Wraps an Widget Launch Command
 * <p>
 * Example Command:
 * <p/>
 * <pre>
 * {@literal
 * CALL|fetchVideos|com.yahoo.widgets.tv.video|{"foo":"bar"}|3534245|END
 *
 * RETURN|fetchVideos|com.yahoo.widgets.tv.settings|{status:"launched"}|3534245|END
 * }
 * </pre>
 * <p/>
 * </p>
 *
 * @author jecortez
 * @version $Revision: 1.0 $
 */
public class WidgetMethodCommand extends AbstractMethodCommand {
	/**
	 * Field serialVersionUID. (value is 4422898638031538717)
	 */
	private static final long serialVersionUID = 4422898638031538717L;

	/**
	 * Field payload.
	 */
	protected String payload;

	public WidgetMethodCommand(String[] tokenizedCommand) throws CommandParseException {
		super(tokenizedCommand);
		this.payload = tokenizedCommand[4];
	}

	/**
	 * 
	 *
	 * @param callId	   unique id that will match responses to this call
	 * @param payload	  payload to be sent to the widget upon launch
	 * @param type
	 * @param remoteMethod
	 * @param widgetId
	 */
	public WidgetMethodCommand(String type, String remoteMethod, String callId, String widgetId, String payload) {
		super(type, widgetId, remoteMethod, callId);
		this.payload = payload != null ? payload : "";
	}

	/**
	 * Method toCommandString.
	 *
	 * @return String
	 */
	@Override
	public String toCommandString() {
		return super.toCommandString(this.payload.toString());
	}

	public String getPayload() {
		return payload;
	}

	public void setPayload(String payload) {
		this.payload = payload;
	}

}
