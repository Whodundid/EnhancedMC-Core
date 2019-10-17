package com.Whodundid.core.enhancedGui.guiObjects;

import com.Whodundid.core.enhancedGui.guiObjectUtil.TextAreaLine;
import com.Whodundid.core.util.storageUtil.EArrayList;

public class newTextArea extends EGuiScrollList {

	EArrayList<TextAreaLine> textDocument, drawnLines;
	TextAreaLine currentLine, longestLine;
}
