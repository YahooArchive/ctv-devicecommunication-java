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

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Wraps an protocol keyboard state change command
 * <p>
 * Example Command:
 * 
 * <pre>
 * {@literal
 * PUBLISH|INPUT|keyboard|{"totalValue": "foobar", "key": "o", "keyCode":false, "isKeyboardOnScreen": true, "cursorPosition": 2, "modifiers": {"control": false, "shift": false, "alt": false}, "layoutType": 1}|END
 * }
 * </pre>
 * 
 * </p>
 * 
 * @author jecortez
 * 
 * @version $Revision: 1.0 $
 */
public class KeyboardInputCommand extends AbstractInputCommand {
	/**
	 * 
	 */
	private static final long serialVersionUID = 4574408368828955819L;

	/**
	 * Field LAYOUT_QUERTY.
	 * (value is 0)
	 */
	public final static int LAYOUT_QUERTY = 0;
	/**
	 * Field LAYOUT_PIN.
	 * (value is 1)
	 */
	public final static int LAYOUT_PIN = 1;

	/**
	 * Field KEY_BACKSPACE.
	 * (value is 8)
	 */
	public final static int KEY_BACKSPACE = 8;
	/**
	 * Field KEY_TAB.
	 * (value is 9)
	 */
	public final static int KEY_TAB = 9;
	/**
	 * Field KEY_RETURN.
	 * (value is 13)
	 */
	public final static int KEY_RETURN = 13;
	/**
	 * Field KEY_CAPS_LOCK.
	 * (value is 20)
	 */
	public final static int KEY_CAPS_LOCK = 20;
	/**
	 * Field KEY_ESCAPE.
	 * (value is 27)
	 */
	public final static int KEY_ESCAPE = 27;
	/**
	 * Field KEY_PAGE_UP.
	 * (value is 33)
	 */
	public final static int KEY_PAGE_UP = 33;
	/**
	 * Field KEY_PAGE_DOWN.
	 * (value is 34)
	 */
	public final static int KEY_PAGE_DOWN = 34;
	/**
	 * Field KEY_END.
	 * (value is 35)
	 */
	public final static int KEY_END = 35;
	/**
	 * Field KEY_HOME.
	 * (value is 36)
	 */
	public final static int KEY_HOME = 36;
	/**
	 * Field KEY_PRINTSCREEN.
	 * (value is 44)
	 */
	public final static int KEY_PRINTSCREEN = 44;
	/**
	 * Field KEY_INSERT.
	 * (value is 45)
	 */
	public final static int KEY_INSERT = 45;
	/**
	 * Field KEY_DELETE.
	 * (value is 46)
	 */
	public final static int KEY_DELETE = 46;
	/**
	 * Field KEY_F10.
	 * (value is 121)
	 */
	public final static int KEY_F10 = 121;
	/**
	 * Field KEY_NUM_LOCK.
	 * (value is 144)
	 */
	public final static int KEY_NUM_LOCK = 144;
	/**
	 * Field KEY_SCROLL_LOCK.
	 * (value is 145)
	 */
	public final static int KEY_SCROLL_LOCK = 145;

	/**
	 * Field layoutType.
	 */
	private int layoutType = LAYOUT_QUERTY;
	/**
	 * Field totalValue.
	 */
	private String totalValue = "";
	/**
	 * Field cursorPosition.
	 */
	private int cursorPosition = 0;
	/**
	 * Field key.
	 */
	private String key = "";
	/**
	 * Field keyCode.
	 */
	private int keyCode = -1;
	/**
	 * Field keyboardOnScreen.
	 */
	private boolean keyboardOnScreen = false;
	/**
	 * Field shiftModifier.
	 */
	private boolean shiftModifier = false;
	/**
	 * Field altModifier.
	 */
	private boolean altModifier = false;
	/**
	 * Field controlModifier.
	 */
	private boolean controlModifier = false;

	/**
	 * Constructor for KeyboardInputCommand.
	 * @param tokenizedCommand String[]
	 * @throws CommandParseException
	 */
	public KeyboardInputCommand(String[] tokenizedCommand) throws CommandParseException {
		super(tokenizedCommand);

		if (tokenizedCommand.length > 2 && tokenizedCommand[2].equals("keyboard")) {
			this.subject = "keyboard";
			if(tokenizedCommand[3] != null){
				this.jsonToMembers(this.parseJSONObject(tokenizedCommand[3]));
			} else {
				throw new CommandParseException("payload cannot be null");
			}
		} else {
			throw new CommandParseException("not keyboard event");
		}
	}

	/**
	 * @param layoutType
	 *            LAYOUT_QUERTY or LAYOUT_PIN
	 * @param totalValue
	 *            total value of the text field
	 * @param cursorPosition
	 *            current 0-based position of the cusor
	 * @param key
	 *            last key hit, empty if not printable
	 * @param keyCode
	 *            keycode of last key if key was not printable
	 * @param shiftModifier
	 *            is Shift activated
	 * @param altModifier
	 *            is alt activated
	 * @param controlModifier
	 *            is control activated
	 */
	public KeyboardInputCommand(int layoutType, String totalValue, int cursorPosition, String key, int keyCode,
			boolean shiftModifier, boolean altModifier, boolean controlModifier) {
		super();
		this.subject = "keyboard";

		this.layoutType = layoutType;
		this.totalValue = totalValue;
		this.cursorPosition = cursorPosition;
		this.key = key;
		this.keyCode = keyCode;
		this.shiftModifier = shiftModifier;
		this.altModifier = altModifier;
		this.controlModifier = controlModifier;
	}

	/**
	 * Method jsonToMembers.
	 * @param payload JSONObject
	 * @throws CommandParseException
	 */
	private void jsonToMembers(JSONObject payload) throws CommandParseException {
		if (payload == null) {
			throw new CommandParseException("no payload found");
		}
		try {
			if (payload.has("layoutType")) {
				this.layoutType = payload.getInt("layoutType");
			}

			if (payload.has("totalValue")) {
				this.totalValue = payload.getString("totalValue");
			}

			if (payload.has("cursorPosition")) {
				try {
					this.cursorPosition = payload.getInt("cursorPosition");
				} catch (JSONException je) {
					System.out.println("Unable to parse cursor position");
				}
			}

			if (payload.has("key")) {
				this.key = payload.getString("key");
			}

			if (payload.has("keyCode")) {
				this.keyCode = payload.getInt("keyCode");
			}

			if (payload.has("isKeyboardOnScreen")) {
				this.keyboardOnScreen = payload.getBoolean("isKeyboardOnScreen");
			}

			if (payload.has("modifiers")) {
				JSONObject modifiers = payload.getJSONObject("modifiers");
				this.altModifier = modifiers.getBoolean("alt");
				this.shiftModifier = modifiers.getBoolean("shift");
				this.controlModifier = modifiers.getBoolean("control");
			}
		} catch (JSONException e) {
			e.printStackTrace();
			throw new CommandParseException("Error parsing JSON");
		}
	}

	/**
	 * Method membersToJSON.
	 * @return JSONObject
	 */
	private JSONObject membersToJSON() {
		JSONObject retval = new JSONObject();

		try {
			retval.put("layoutType", this.layoutType);
			retval.put("totalValue", this.totalValue);
			retval.put("cursorPosition", this.cursorPosition);
			retval.put("key", this.key);
			retval.put("keyCode", this.keyCode);

			JSONObject modifiers = new JSONObject();
			modifiers.put("shift", this.shiftModifier);
			modifiers.put("alt", this.altModifier);
			modifiers.put("control", this.controlModifier);
			retval.put("modifiers", modifiers);
		} catch (JSONException e) {
			Utilities.log("ERROR CREATING JSON MESSAGE");
		}

		return retval;
	}

	/**
	 * Method toCommandString.
	 * @return String
	 */
	@Override
	public String toCommandString() {
		StringBuffer sb = new StringBuffer();
		sb.append(this.method);
		sb.append("|INPUT|keyboard|");
		sb.append(this.membersToJSON());
		sb.append("|END");

		return sb.toString();
	}

	@Override
	public String getPayload() {
		return this.membersToJSON().toString();
	}

	/**
	
	 * @return LAYOUT_PIN or LAYOUT_QWERTY */
	public int getLayoutType() {
		return layoutType;
	}

	/**
	 * @param layoutType
	 *            LAYOUT_PIN or LAYOUT_QWERTY
	 */
	public void setLayoutType(int layoutType) {
		this.layoutType = layoutType;
	}

	/**
	
	 * @return value of the text field */
	public String getTotalValue() {
		return totalValue;
	}

	/**
	 * @param totalValue
	 *            value of the text field
	 */
	public void setTotalValue(String totalValue) {
		this.totalValue = totalValue;
	}

	/**
	
	 * @return 0-based position of the cursor */
	public int getCursorPosition() {
		return cursorPosition;
	}

	/**
	 * @param cursorPosition
	 *            0-based position of the cursor
	 */
	public void setCursorPosition(int cursorPosition) {
		this.cursorPosition = cursorPosition;
	}

	/**
	
	 * @return Last character entered, empty string if not printable */
	public String getKey() {
		return key;
	}

	/**
	 * @param key
	 *            Last character entered, empty string if not printable
	 */
	public void setKey(String key) {
		this.key = key;
	}

	/**
	
	 * @return status of the shift modifier */
	public boolean isShiftModifier() {
		return shiftModifier;
	}

	/**
	 * @param shiftModifier
	 *            status of the shift modifier
	 */
	public void setShiftModifier(boolean shiftModifier) {
		this.shiftModifier = shiftModifier;
	}

	/**
	
	 * @return status of the alt modifier */
	public boolean isAltModifier() {
		return altModifier;
	}

	/**
	 * @param altModifier
	 *            status of the alt modifier
	 */
	public void setAltModifier(boolean altModifier) {
		this.altModifier = altModifier;
	}

	/**
	
	 * @return status of the control modifier */
	public boolean isControlModifier() {
		return controlModifier;
	}

	/**
	 * @param controlModifier
	 *            status of the controls modifier
	 */
	public void setControlModifier(boolean controlModifier) {
		this.controlModifier = controlModifier;
	}

	/**
	
	 * @return is keyboard currently on TV screen */
	public boolean isKeyboardOnScreen() {
		return keyboardOnScreen;
	}

	/**
	 * @param keyboardOnScreen
	 *            is keyboard currently on TV screen
	 */
	public void setKeyboardOnScreen(boolean keyboardOnScreen) {
		this.keyboardOnScreen = keyboardOnScreen;
	}

	/**
	
	 * @return if last key was not printable, the keyCode see KEY_* for
	 *         constants */
	public int getKeyCode() {
		return keyCode;
	}

	/**
	 * @param keyCode
	 *            if last key was not printable, the keyCode see KEY_* for
	 *            constants
	 */
	public void setKeyCode(int keyCode) {
		this.keyCode = keyCode;
	}

}
