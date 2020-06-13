package com.Whodundid.core.windowLibrary.windowObjects.utilityObjects;

import com.Whodundid.core.util.renderUtil.EColors;
import com.Whodundid.core.windowLibrary.windowObjects.actionObjects.WindowButton;
import com.Whodundid.core.windowLibrary.windowObjects.advancedObjects.scrollList.WindowScrollList;
import com.Whodundid.core.windowLibrary.windowObjects.basicObjects.WindowLabel;

public class SettingCategoryContainer {

	WindowScrollList parentList;
	String name = "noname";
	int color = 0xffffff;
	
	public SettingCategoryContainer(WindowScrollList parentListIn, String categoryName, EColors categoryColor) { this(parentListIn, categoryName, categoryColor.intVal); }
	public SettingCategoryContainer(WindowScrollList parentListIn, String categoryName, int categoryColor) {
		parentList = parentListIn;
	}
	
	public SettingCategoryContainer addSetting() {
		
		
		return this;
	}
	
	public int getY() {
		return -1;
	}
	
	private class SettingContainer {
		
		WindowButton button;
		WindowLabel label;
		
		public SettingContainer() {
			
		}
	}
	
}
