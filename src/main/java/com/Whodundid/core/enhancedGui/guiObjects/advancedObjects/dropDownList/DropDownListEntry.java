package com.Whodundid.core.enhancedGui.guiObjects.advancedObjects.dropDownList;

import net.minecraft.client.Minecraft;

//Author: Hunter Bragg

public class DropDownListEntry<obj> {
	
	static Minecraft mc = Minecraft.getMinecraft();
	protected int entryID = -1;
	protected EGuiDropDownList parentList;
	protected String displayString = "";
	protected obj entryObject;
	protected boolean visible = true;
	protected boolean enabled = true;
	protected boolean globalAction = false;
	
	public DropDownListEntry(String displayStringIn) { this(displayStringIn, null, false); }
	public DropDownListEntry(String displayStringIn, obj objectIn) { this(displayStringIn, objectIn, false); }
	public DropDownListEntry(String displayStringIn, obj objectIn, boolean globalActionDefined) {
		displayString = displayStringIn;
		entryObject = objectIn;
		globalAction = globalActionDefined;
	}
	
	public int getEntryID() { return entryID; }
	public EGuiDropDownList getParentList() { return parentList; }
	public String getDisplayString() { return displayString; }
	public obj getEntryObject() { return entryObject; }
	public boolean isVisible() { return visible; }
	public boolean isEnabled() { return enabled; }
	public boolean isThereGlobalAction() { return globalAction; }
	
	public DropDownListEntry setEntryID(int idIn) { entryID = idIn; return this; }
	public DropDownListEntry setParentList(EGuiDropDownList parentIn) { parentList = parentIn; return this; }
	public DropDownListEntry setDisplayString(String displayStringIn) { displayString = displayStringIn; return this; }
	public DropDownListEntry setEntryObject(obj objectIn) { entryObject = objectIn; return this; }
	public DropDownListEntry setVisibility(boolean val) { visible = val; return this; }
	public DropDownListEntry setEnabled(boolean val) { enabled = val; return this; }
	public DropDownListEntry setGlobalActionPresent(boolean val) { globalAction = val; return this; }
	
	public void runEntryAction() {
		if (globalAction) { parentList.runGlobalAction(); }
	}
}
