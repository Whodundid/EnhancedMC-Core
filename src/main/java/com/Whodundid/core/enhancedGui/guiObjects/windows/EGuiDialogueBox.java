package com.Whodundid.core.enhancedGui.guiObjects.windows;

import com.Whodundid.core.enhancedGui.guiObjects.actionObjects.EGuiButton;
import com.Whodundid.core.enhancedGui.types.WindowParent;
import com.Whodundid.core.enhancedGui.types.interfaces.IEnhancedGuiObject;
import com.Whodundid.core.util.EUtil;
import com.Whodundid.core.util.storageUtil.EArrayList;

//Author: Hunter Bragg

public class EGuiDialogueBox extends WindowParent {
	
	public int messageColor = 0xffffff, titleColor = 0xffffff;
	public String message = "", title = "";
	protected EArrayList<String> wordWrappedLines;
	protected IEnhancedGuiObject defaultObject;
	protected EGuiButton yes, no, okButton;
	protected DialogueBoxTypes type;
	
	public enum DialogueBoxTypes { yesNo, ok, custom; }
	
	protected EGuiDialogueBox() { super(); }
	public EGuiDialogueBox(DialogueBoxTypes typeIn) {
		super();
		setDimensions(250, 75);
		type = typeIn;
		requestFocus();
		getTopParent().setFocusLockObject(this);
		setResizeable(true);
		setMinDims(101, 101);
	}
	
	@Override
	public void initObjects() {
		defaultHeader(this);
		getHeader().setTitle(title).setTitleColor(titleColor);
		
		if (type != null) {
			switch (type) {
			case yesNo:
				yes = new EGuiButton(this, midX - 100, midY + 10, 65, 20, "Yes");
				no = new EGuiButton(this, midX + 25, midY + 10, 65, 20, "No");
				addObject(yes.setZLevel(1), no.setZLevel(1));
				break;
			case ok:
				okButton = new EGuiButton(this, midX - 25, midY + 10, 50, 20, "Ok") {
					@Override
					public void onPress() {
						playPressSound();
						parent.close();
					}
				};
				okButton.setRunActionOnPress(true);
				addObject(okButton.setZLevel(1));
				break;
			default: break;
			}
		}
	}
	
	@Override
	public void drawObject(int mXIn, int mYIn, float ticks) {
		drawDefaultBackground();
		if (wordWrappedLines != null) {
			int lnWidth = wordWrappedLines.size() * 10;
			int totalWidth = midY + 5 - startY;
			int lnStartY = startY + (totalWidth - lnWidth) / 2;
			int i = 0;
			for (String s : wordWrappedLines) {
				drawCenteredStringWithShadow(s, midX, lnStartY + (i * 10), messageColor);
				i++;
			}
		}
		super.drawObject(mXIn, mYIn, ticks);
	}
	
	@Override
	public void keyPressed(char typedKey, int keyCode) {
		if (keyCode == 28) { //enter
			if (defaultObject != null && defaultObject instanceof EGuiButton) {
				((EGuiButton) defaultObject).performAction();
			}
		}
	}
	
	public EGuiDialogueBox setDefaultObject(IEnhancedGuiObject objIn) { defaultObject = objIn; return this; }
	public EGuiDialogueBox setTitle(String stringIn) { title = stringIn; return this; }
	public EGuiDialogueBox setTitleColor(int colorIn) { titleColor = colorIn; return this; }
	public EGuiDialogueBox setMessage(String stringIn) { message = stringIn; wordWrappedLines = EUtil.createWordWrapString(message, width - 20); return this; }
	public EGuiDialogueBox setMessageColor(int colorIn) { messageColor = colorIn; return this; }
	
	public IEnhancedGuiObject getPrimaryObject() { return defaultObject; }
}
