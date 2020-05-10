package com.Whodundid.hotkeys.control.hotKeyUtil;

//Last edited: Sep 30, 2018
//First Added: Sep 30, 2018
//Author: Hunter Bragg

public class MissingHotKeyArgumentException extends Exception {
	
	public MissingHotKeyArgumentException(String missingArgs) {
		super(missingArgs);
	}
}
