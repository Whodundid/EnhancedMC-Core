package com.Whodundid.core.enhancedGui.guiObjects;

import com.Whodundid.core.EnhancedMC;
import com.Whodundid.core.enhancedGui.types.interfaces.IEnhancedActionObject;
import com.Whodundid.core.enhancedGui.types.interfaces.IEnhancedGuiObject;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.resources.I18n;

//Last edited: Jan 1, 2019
//First Added: Dec 31, 2018
//Author: Hunter Bragg

public class EGuiLinkConfirmationDialogueBox extends EGuiDialogueBox {
	
	String link;
	EGuiButton yes, copy, no;
	
	public EGuiLinkConfirmationDialogueBox(IEnhancedGuiObject parentIn, String linkIn) {
		ScaledResolution res = new ScaledResolution(mc);
		
		String prompt = I18n.format("chat.link.confirm", new Object[0]);
		String warning = I18n.format("chat.link.warning", new Object[0]);
		link = linkIn;
		
		int longestString = 0;
		longestString = EnhancedMC.getFontRenderer().getStringWidth(prompt);
		int linkLength = EnhancedMC.getFontRenderer().getStringWidth(linkIn);
		if (linkLength > longestString) { longestString = linkLength; }
		int copyLength = EnhancedMC.getFontRenderer().getStringWidth(I18n.format("chat.copy", new Object[0])) + 8;
		int buttonLength = copyLength + 232; //length of all buttons and gaps inbetween
		if (buttonLength > longestString) { longestString = buttonLength; }
		
		int x = res.getScaledWidth() / 2 - longestString / 2 - 15;
		int y = res.getScaledHeight() / 2 - 60;
		int w = longestString > 400 ? 400 : longestString + 30;
		
		EGuiLabel promptLabel = new EGuiLabel(this, res.getScaledWidth() / 2, y + 18, prompt).setDrawCentered(true).setDisplayStringColor(0xffbb00);
		EGuiLabel linkLabel = new EGuiLabel(this, res.getScaledWidth() / 2, y + 33, linkIn).setDrawCentered(true).enableWordWrap(true, longestString);
		EGuiLabel warningLabel = new EGuiLabel(this, res.getScaledWidth() / 2, y + 38 + linkLabel.getTextHeight(), warning).setDrawCentered(true).setDisplayStringColor(0xff5555);
		
		int h = 100 + linkLabel.getTextHeight() < 110 ? 110 : 100 + linkLabel.getTextHeight();
		
		init(parentIn, x, y, w, h);
		setHeader(new EGuiHeader(this));
		requestFocus();
		getTopParent().setFocusLockObject(this);
		setDisplayString("Opening Link");
		
		yes = new EGuiButton(this, midX - copyLength - 35, endY - 35, 75, 20, I18n.format("gui.yes", new Object[0])).setDisplayStringColor(0x55ff55);
		copy = new EGuiButton(this, midX - (copyLength  + 8) / 2, endY - 35, copyLength + 8, 20, I18n.format("chat.copy", new Object[0])).setDisplayStringColor(0xffffff);
		no = new EGuiButton(this, midX + copyLength - 39, endY - 35, 75, 20, I18n.format("gui.no", new Object[0])).setDisplayStringColor(0xff5555);
		
		addObject(promptLabel, linkLabel, warningLabel, yes, copy, no);
	}
	
	@Override public void initObjects() {}
	
	@Override
	public void drawObject(int mXIn, int mYIn, float ticks) {
		super.drawObject(mXIn, mYIn, ticks);
	}
	
	@Override
	public void actionPerformed(IEnhancedActionObject object) {
		if (object.equals(yes)) { openWebLink(link); close(); }
		if (object.equals(copy)) { ((GuiScreen) getTopParent()).setClipboardString(link); close(); }
		if (object.equals(no)) { close(); }
	}
}
