package com.Whodundid.worldEditor.EditorGuiObjects;

import com.Whodundid.core.util.renderUtil.CursorHelper;
import com.Whodundid.worldEditor.EditorResources;
import com.Whodundid.worldEditor.EditorUtil.EditorGuiBase;
import com.Whodundid.worldEditor.EditorUtil.EditorTools;

//Last edited: Nov 25, 2018
//First Added: Nov 25, 2018
//Author: Hunter Bragg

public class EditorCursor extends EditorGuiObject {
	
	protected boolean cursorVisibility = true;
	
	public EditorCursor(EditorGuiBase guiIn) {
		super(guiIn);
		cursorVisibility = CursorHelper.isVisible;
	}
	
	@Override
	public void drawObject(int mX, int mY) {
		updateCursor();
		switch (guiInstance.editor.getSelectedTool()) {
		case PAN:
			if (editor.insideEditor) {
				if (editor.render3D) {
					mc.renderEngine.bindTexture(EditorResources.editorOrbit);
					drawModalRectWithCustomSizedTexture(mX - 8, mY - 8, 0, 0, 16, 16, 16, 16);
				} else {
					if (!guiInstance.isThereRCM()) {
						//if (guiInstance.leftClick || guiInstance.middleClick || guiInstance.rightClick) {
						//	mc.renderEngine.bindTexture(EditorResources.editorPanG);
						//	drawModalRectWithCustomSizedTexture(mX - 9, mY, 0, 0, 15, 13, 15, 13);
						//}
						//else { 
						//	mc.renderEngine.bindTexture(EditorResources.editorPanU);
						//	drawModalRectWithCustomSizedTexture(mX - 9, mY - 3, 0, 0, 16, 16, 16, 16);
						//}
					}
				}
			}
			break;
		default: break;
		}
	}
	
	public void updateCursor() {
		switch (editor.getSelectedTool()) {
		case PAN:
			CursorHelper.setCursorVisibility(!editor.insideEditor || guiInstance.isThereRCM());
			cursorVisibility = false;
			break;
		default: break;
		}
		if (!cursorVisibility) {
			if (editor.getSelectedTool().equals(EditorTools.PAN) && !(!editor.insideEditor || guiInstance.isThereRCM())) {
			} else {
				cursorVisibility = true;
				CursorHelper.setCursorVisibility(true);
			}
		}
	}
}
