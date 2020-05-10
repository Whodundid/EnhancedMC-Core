package com.Whodundid.hotkeys.control.hotKeyUtil;

//Last edited: Oct 9, 2018
//First Added: Oct 9, 2018
//Author: Hunter Bragg

public class HotKeyAlreadyExistsException extends Exception {
	
	public HotKeyAlreadyExistsException(String missingArgs) {
		super(missingArgs);
	}
}
