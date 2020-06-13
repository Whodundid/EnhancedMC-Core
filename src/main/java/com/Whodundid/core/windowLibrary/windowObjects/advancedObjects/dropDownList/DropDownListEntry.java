package com.Whodundid.core.windowLibrary.windowObjects.advancedObjects.dropDownList;

import com.Whodundid.core.util.renderUtil.EColors;
import net.minecraft.client.Minecraft;

//Author: Hunter Bragg

public class DropDownListEntry {
	
	static Minecraft mc = Minecraft.getMinecraft();
	protected int entryID = -1;
	protected int color = 0xffffffff;
	protected WindowDropDownList parentList;
	protected String text = "";
	protected Object entryObject;
	protected boolean visible = true;
	protected boolean enabled = true;
	protected boolean globalAction = false;
	
	public DropDownListEntry(String textIn) { this(textIn, EColors.lgray.intVal, null, false); }
	public DropDownListEntry(String textIn, EColors colorIn, Object objectIn) { this(textIn, colorIn.intVal, objectIn, false); }
	public DropDownListEntry(String textIn, int colorIn, Object objectIn) { this(textIn, colorIn, objectIn, false); }
	public DropDownListEntry(String textIn, EColors colorIn, Object objectIn, boolean hasActionIn) { this(textIn, colorIn.intVal, objectIn, hasActionIn); }
	public DropDownListEntry(String textIn, int colorIn, Object objectIn, boolean hasActionIn) {
		text = textIn;
		color = colorIn;
		entryObject = objectIn;
		globalAction = hasActionIn;
	}
	
	public int getEntryID() { return entryID; }
	public WindowDropDownList getParentList() { return parentList; }
	public String getText() { return text; }
	public int getColor() { return color; }
	public Object getEntryObject() { return entryObject; }
	public boolean isVisible() { return visible; }
	public boolean isEnabled() { return enabled; }
	public boolean isThereGlobalAction() { return globalAction; }
	
	public DropDownListEntry setEntryID(int idIn) { entryID = idIn; return this; }
	public DropDownListEntry setParentList(WindowDropDownList parentIn) { parentList = parentIn; return this; }
	public DropDownListEntry setText(String textIn) { text = textIn; return this; }
	public DropDownListEntry setColor(EColors colorIn) { return setColor(colorIn.intVal); }
	public DropDownListEntry setColor(int colorIn) { setColor(colorIn); return this; }
	public DropDownListEntry setEntryObject(Object objectIn) { entryObject = objectIn; return this; }
	public DropDownListEntry setVisibility(boolean val) { visible = val; return this; }
	public DropDownListEntry setEnabled(boolean val) { enabled = val; return this; }
	public DropDownListEntry setGlobalActionPresent(boolean val) { globalAction = val; return this; }
	
	public void runEntryAction() {
		if (globalAction) { parentList.runGlobalAction(); }
	}
	
	@Override public String toString() { return text; }
	
}
