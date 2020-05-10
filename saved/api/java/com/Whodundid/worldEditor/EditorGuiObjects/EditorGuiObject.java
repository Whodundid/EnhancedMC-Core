package com.Whodundid.worldEditor.EditorGuiObjects;

import com.Whodundid.core.enhancedGui.types.WindowParent;
import com.Whodundid.worldEditor.EditorApp;
import com.Whodundid.worldEditor.EditorUtil.EditorGuiBase;

//Last edited: Nov 25, 2018
//First Added: Nov 25, 2018
//Author: Hunter Bragg

public abstract class EditorGuiObject extends WindowParent {
	
	public EditorApp editor;
	public EditorGuiBase guiInstance;
	
	protected EditorGuiObject(EditorGuiBase baseIn) {
		guiInstance = baseIn;
		editor = baseIn.editor;
	}
	
	public EditorGuiBase getGuiInstance() { return guiInstance; }
}
