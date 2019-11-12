package com.Whodundid.core.subMod.util;

import com.Whodundid.core.renderer.EnhancedMCRenderer;
import com.Whodundid.core.subMod.SubMod;
import com.Whodundid.core.subMod.gui.SubModErrorDialogueBox;
import com.Whodundid.core.subMod.gui.SubModErrorType;
import com.Whodundid.core.util.storageUtil.EArrayList;

public class SubModErrorDisplay {

	public static void displayError(SubModErrorType type) { displayError(type, null, null, null); }
	public static void displayError(SubModErrorType type, SubMod modIn) { displayError(type, modIn, null, null); }
	public static void displayError(SubModErrorType type, SubMod modIn, Exception e) { displayError(type, modIn, e, null); }
	public static void displayError(SubModErrorType type, SubMod modIn, EArrayList<SubMod> mods) { displayError(type, modIn, null, mods); }
	public static void displayError(SubModErrorType type, SubMod modIn, Exception e, EArrayList<SubMod> mods) {
		EnhancedMCRenderer ren = EnhancedMCRenderer.getInstance();
		if (ren != null) {
			SubModErrorDialogueBox errorBox = new SubModErrorDialogueBox(ren, type, modIn);
			ren.addObject(errorBox);
			errorBox.createErrorMessage(e, mods);
		}
	}
}
