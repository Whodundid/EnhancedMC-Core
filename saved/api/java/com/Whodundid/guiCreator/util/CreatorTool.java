package com.Whodundid.guiCreator.util;

public enum CreatorTool {
	//creator
	SELECT(ToolCategory.CREATOR, "Select"),
	MOVE(ToolCategory.CREATOR, "Move"),
	RESIZE(ToolCategory.CREATOR, "Resize"),
	EYEDROPPER(ToolCategory.CREATOR, "Eye Dropper"),
	
	//shape
	LINE(ToolCategory.SHAPE, "Line"),
	SQUARE(ToolCategory.SHAPE, "Rectangle"),
	CIRCLE(ToolCategory.SHAPE, "Ellipse"),
	
	//action
	BUTTON(ToolCategory.ACTION, "Button"),
	BUTTON3(ToolCategory.ACTION, "Button 3 State"),
	CHECKBOX(ToolCategory.ACTION, "Checkbox"),
	RADIOBUTTON(ToolCategory.ACTION, "Radio Button"),
	SLIDER(ToolCategory.ACTION, "Slider"),
	TEXTFIELD(ToolCategory.ACTION, "Text Field"),
	
	//basic
	HEADER(ToolCategory.BASIC, "Header"),
	CONTAINER(ToolCategory.BASIC, "Container"),
	IMAGEBOX(ToolCategory.BASIC, "Image Box"),
	LABEL(ToolCategory.BASIC, "Label"),
	
	//advanced
	SCROLLLIST(ToolCategory.ADVANCED, "Scroll List"),
	TEXTAREA(ToolCategory.ADVANCED, "Text Area"),
	DROPDOWNLIST(ToolCategory.ADVANCED, "Drop Down List"),
	PLAYERVIEWER(ToolCategory.ADVANCED, "Player Viewer"),
	PROGRESSBAR(ToolCategory.ADVANCED, "Progress Bar");
	
	public ToolCategory category = null;
	public String name = "NONE";
	
	private CreatorTool(ToolCategory catIn, String nameIn) {
		category = catIn;
		name = nameIn;
	}
	
	public String getName() { return name; }
	public ToolCategory getCategory() { return category; }
}
