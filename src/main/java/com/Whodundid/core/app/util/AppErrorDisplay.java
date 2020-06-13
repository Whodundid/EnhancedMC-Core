package com.Whodundid.core.app.util;

import com.Whodundid.core.app.EMCApp;
import com.Whodundid.core.app.window.AppErrorDialogueBox;
import com.Whodundid.core.app.window.windowUtil.AppErrorType;
import com.Whodundid.core.renderer.EnhancedMCRenderer;
import com.Whodundid.core.util.storageUtil.EArrayList;

//Author: Hunter Bragg

public class AppErrorDisplay {

	public static void displayError(AppErrorType type) { displayError(type, null, null, null); }
	public static void displayError(AppErrorType type, EMCApp modIn) { displayError(type, modIn, null, null); }
	public static void displayError(AppErrorType type, EMCApp modIn, Exception e) { displayError(type, modIn, e, null); }
	public static void displayError(AppErrorType type, EMCApp modIn, EArrayList<EMCApp> mods) { displayError(type, modIn, null, mods); }
	public static void displayError(AppErrorType type, EMCApp modIn, Exception e, EArrayList<EMCApp> mods) {
		EnhancedMCRenderer ren = EnhancedMCRenderer.getInstance();
		if (ren != null) {
			AppErrorDialogueBox errorBox = new AppErrorDialogueBox(ren, type, modIn);
			ren.addObject(null, errorBox);
			errorBox.createErrorMessage(e, mods);
		}
	}
}
