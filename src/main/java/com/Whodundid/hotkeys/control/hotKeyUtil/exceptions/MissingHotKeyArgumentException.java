package com.Whodundid.hotkeys.control.hotKeyUtil.exceptions;

//Last edited: Sep 30, 2018
//First Added: Sep 30, 2018
//Author: Hunter Bragg

public class MissingHotkeyArgumentException extends Exception {
	
	public MissingHotkeyArgumentException(String missingArgs) {
		super(missingArgs);
	}
	
}
