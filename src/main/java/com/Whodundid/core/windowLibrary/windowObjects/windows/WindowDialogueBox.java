package com.Whodundid.core.windowLibrary.windowObjects.windows;

import com.Whodundid.core.util.EUtil;
import com.Whodundid.core.util.renderUtil.EColors;
import com.Whodundid.core.util.storageUtil.EArrayList;
import com.Whodundid.core.windowLibrary.windowObjects.actionObjects.WindowButton;
import com.Whodundid.core.windowLibrary.windowTypes.WindowParent;
import com.Whodundid.core.windowLibrary.windowTypes.interfaces.IWindowObject;
import net.minecraft.util.MathHelper;

//Author: Hunter Bragg

public class WindowDialogueBox extends WindowParent {
	
	public int messageColor = 0xffffff, titleColor = 0xffffff;
	public String message = "", title = "";
	protected EArrayList<String> wordWrappedLines;
	protected IWindowObject defaultObject;
	protected WindowButton yes, no, okButton;
	protected DialogueBoxTypes type;
	
	public enum DialogueBoxTypes { yesNo, ok, custom; }
	
	protected WindowDialogueBox() { this(DialogueBoxTypes.custom); }
	public WindowDialogueBox(DialogueBoxTypes typeIn) {
		super();
		type = typeIn;
		setDimensions(250, 85);
		setResizeable(true);
		setMinDims(101, 85);
	}
	
	@Override
	public void initObjects() {
		defaultHeader(this);
		getHeader().setTitle(title);
		getHeader().setTitleColor(titleColor);
		
		if (type != null) {
			switch (type) {
			case yesNo:
				int bw = MathHelper.clamp_int((width - 10) / 3, 0, 140);
				int g = width / 30;
				
				yes = new WindowButton(this, midX - g - bw, endY - 30, bw, 20, "Yes");
				no = new WindowButton(this, midX + g, endY - 30, bw, 20, "No");
				
				no.setStringColor(EColors.yellow);
				yes.setStringColor(EColors.lgreen);
				
				addObject(yes, no);
				
				defaultObject = yes;
				break;
			case ok:
				okButton = new WindowButton(this, midX - 40, endY - 30, 80, 20, "Ok") {
					@Override
					public void onPress() {
						playPressSound();
						parent.close();
					}
				};
				okButton.setRunActionOnPress(true);
				addObject(okButton);
				
				defaultObject = okButton;
				break;
			default: break;
			}
		}
	}
	
	@Override
	public void drawObject(int mXIn, int mYIn) {
		drawDefaultBackground();
		
		if (wordWrappedLines != null) {
			int lnWidth = wordWrappedLines.size() * 10;
			int totalWidth = (endY - 25) - startY;
			int lnStartY = startY + (totalWidth - lnWidth) / 2 - 2;
			int i = 0;
			scissor(startX + 1, startY + 1, endX - 1, endY - 1);
			for (String s : wordWrappedLines) {
				drawStringCS(s, midX, lnStartY + (i * 10), messageColor);
				i++;
			}
			endScissor();
		}
		super.drawObject(mXIn, mYIn);
	}
	
	@Override
	public void keyPressed(char typedKey, int keyCode) {
		if (keyCode == 28) { //enter
			if (defaultObject != null && defaultObject instanceof WindowButton) {
				((WindowButton) defaultObject).performAction();
			}
		}
	}
	
	public WindowDialogueBox setDefaultObject(IWindowObject objIn) { defaultObject = objIn; return this; }
	public WindowDialogueBox setTitle(String stringIn) { title = stringIn; setObjectName(title); if (getHeader() != null) { header.setTitle(title); } return this; }
	public WindowDialogueBox setTitleColor(int colorIn) { titleColor = colorIn; if (getHeader() != null) { header.setTitleColor(titleColor); } return this; }
	public WindowDialogueBox setMessageColor(int colorIn) { messageColor = colorIn; return this; }
	public WindowDialogueBox setMessage(String stringIn) {
		message = stringIn;
		wordWrappedLines = EUtil.createWordWrapString(message, width - 20);
		return this;
	}
	
	public IWindowObject getPrimaryObject() { return defaultObject; }
	
}
