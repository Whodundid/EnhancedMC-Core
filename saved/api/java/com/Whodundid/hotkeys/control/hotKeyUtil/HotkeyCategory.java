package com.Whodundid.hotkeys.control.hotKeyUtil;

//Last edited: Feb 17, 2019
//First Added: Feb 17, 2019
//Author: Hunter Bragg

public class HotkeyCategory {
	
	protected String categoryName = "";
	
	public HotkeyCategory(String nameIn) {
		categoryName = nameIn;
	}
	
	public String getCategoryName() { return categoryName; }
}
