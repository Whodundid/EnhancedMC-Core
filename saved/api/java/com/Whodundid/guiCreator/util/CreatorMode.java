package com.Whodundid.guiCreator.util;

public enum CreatorMode {
	EDIT_MODE("Edit"),
	TEST_MODE("Test");
	
	public String name = "NONE";
	
	private CreatorMode(String nameIn) {
		name = nameIn;
	}
	
	public String getName() { return name; }
}
