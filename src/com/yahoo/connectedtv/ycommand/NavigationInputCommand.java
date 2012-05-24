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
 * Wraps an protocol navigation command command
 * <p>
 * Example Commands:
 * 
 * <pre>
 * {@literal
 * PUBLISH|INPUT|navigation|press_up|END
 * PUBLISH|INPUT|navigation|press_down|END
 * PUBLISH|INPUT|navigation|down_left|END
 * PUBLISH|INPUT|navigation|up_left|END
 * }
 * </pre>
 * 
 * </p>
 * 
 * @author jecortez
 * 
 * @version $Revision: 1.0 $
 */
public class NavigationInputCommand extends AbstractInputCommand {
	/**
	 * 
	 */
	private static final long serialVersionUID = 5761951643862994566L;
	/**
	 * Field CATEGORY.
	 * (value is ""navigation"")
	 */
	public final static String CATEGORY = "navigation";

	/**
	 * Field UP.
	 * (value is ""up"")
	 */
	public final static String UP = "up";
	/**
	 * Field DOWN.
	 * (value is ""down"")
	 */
	public final static String DOWN = "down";
	/**
	 * Field LEFT.
	 * (value is ""left"")
	 */
	public final static String LEFT = "left";
	/**
	 * Field RIGHT.
	 * (value is ""right"")
	 */
	public final static String RIGHT = "right";
	/**
	 * Field ENTER.
	 * (value is ""enter"")
	 */
	public final static String ENTER = "enter";
	/**
	 * Field YAHOO.
	 * (value is ""yahoo"")
	 */
	public final static String YAHOO = "yahoo";
	/**
	 * Field DOCK.
	 * (value is ""yahoo"")
	 */
	public final static String DOCK = "yahoo";
	/**
	 * Field BACK.
	 * (value is ""back"")
	 */
	public final static String BACK = "back";
	/**
	 * Field RED.
	 * (value is ""red"")
	 */
	public final static String RED = "red";
	/**
	 * Field GREEN.
	 * (value is ""green"")
	 */
	public final static String GREEN = "green";
	/**
	 * Field YELLOW.
	 * (value is ""yellow"")
	 */
	public final static String YELLOW = "yellow";
	/**
	 * Field BLUE.
	 * (value is ""blue"")
	 */
	public final static String BLUE = "blue";
	/**
	 * Field REWIND.
	 * (value is ""rewind"")
	 */
	public final static String REWIND = "rewind";
	/**
	 * Field PAUSE.
	 * (value is ""pause"")
	 */
	public final static String PAUSE = "pause";
	/**
	 * Field PLAY.
	 * (value is ""play"")
	 */
	public final static String PLAY = "play";
	/**
	 * Field STOP.
	 * (value is ""stop"")
	 */
	public final static String STOP = "stop";
	/**
	 * Field FASTFOWARD.
	 * (value is ""fastfoward"")
	 * @deprecated I can't spell or read the spec correctly, use FORWARD instead
	 */
	public final static String FASTFOWARD = "forward";

	public final static String FORWARD = "forward";
	/**
	 * Field EXIT.
	 * (value is ""exit"")
	 */
	public final static String EXIT = "exit";

	/**
	 * Field command.
	 */
	private String command;

	/**
	 * Constructor for NavigationInputCommand.
	 * @param command String
	 */
	public NavigationInputCommand(String command) {
		super();
		this.subject = NavigationInputCommand.CATEGORY;
		this.command = command;
	}

	/**
	 * Constructor for NavigationInputCommand.
	 * @param tokenizedCommand String[]
	 * @throws CommandParseException
	 */
	public NavigationInputCommand(String[] tokenizedCommand) throws CommandParseException {
		super(tokenizedCommand);

		if (tokenizedCommand.length > 2 && tokenizedCommand[2].equals(NavigationInputCommand.CATEGORY)) {
			this.subject = NavigationInputCommand.CATEGORY;
			this.command = tokenizedCommand[3];
		} else {
			throw new CommandParseException("not keyboard event");
		}
	}

	/**
	 * Method toCommandString.
	 * @return String
	 */
	public String toCommandString() {
		StringBuffer sb = new StringBuffer();
		sb.append(this.method);
		sb.append("|INPUT|navigation|");
		sb.append(this.command);
		sb.append("|END");

		return sb.toString();
	}

	@Override
	public String getPayload() {
		return this.command;
	}

	/**
	 * Method getCommand.
	 * @return String
	 */
	public String getCommand() {
		return command;
	}

	/**
	 * Method setCommand.
	 * @param command String
	 */
	public void setCommand(String command) {
		this.command = command;
	}

}
