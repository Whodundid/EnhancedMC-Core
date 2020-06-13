package com.Whodundid.core.windowLibrary.windowObjects.windows;

import com.Whodundid.core.EnhancedMC;
import com.Whodundid.core.util.renderUtil.EColors;
import com.Whodundid.core.windowLibrary.windowObjects.actionObjects.WindowButton;
import com.Whodundid.core.windowLibrary.windowObjects.basicObjects.WindowLabel;
import com.Whodundid.core.windowLibrary.windowTypes.interfaces.IActionObject;
import com.Whodundid.core.windowLibrary.windowTypes.interfaces.IWindowObject;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.resources.I18n;

//Author: Hunter Bragg

public class LinkConfirmationWindow extends WindowDialogueBox {
	
	String link;
	WindowButton yes, copy, no;
	
	public LinkConfirmationWindow(String linkIn) { this(EnhancedMC.getRenderer(), linkIn); }
	public LinkConfirmationWindow(IWindowObject parentIn, String linkIn) {
		super();
		link = linkIn;
	}
	
	@Override
	public void initObjects() {
		ScaledResolution res = new ScaledResolution(mc);
		
		String prompt = I18n.format("chat.link.confirm", new Object[0]);
		String warning = I18n.format("chat.link.warning", new Object[0]);
		
		int longestString = 0;
		longestString = mc.fontRendererObj.getStringWidth(prompt);
		int linkLength = mc.fontRendererObj.getStringWidth(link);
		if (linkLength > longestString) { longestString = linkLength; }
		int copyLength = mc.fontRendererObj.getStringWidth(I18n.format("chat.copy", new Object[0])) + 8;
		int buttonLength = copyLength + 232; //length of all buttons and gaps inbetween
		if (buttonLength > longestString) { longestString = buttonLength; }
		
		int x = res.getScaledWidth() / 2 - longestString / 2 - 15;
		int y = res.getScaledHeight() / 2 - 60;
		int w = longestString > 400 ? 400 : longestString + 30;
		
		WindowLabel promptLabel = new WindowLabel(this, res.getScaledWidth() / 2, y + 18, prompt).setDrawCentered(true).setColor(0xffbb00);
		WindowLabel linkLabel = new WindowLabel(this, res.getScaledWidth() / 2, y + 33, link).setDrawCentered(true).enableWordWrap(true, longestString);
		WindowLabel warningLabel = new WindowLabel(this, res.getScaledWidth() / 2, y + 38 + linkLabel.getTextHeight(), warning).setDrawCentered(true).setColor(0xff5555);
		
		int h = 100 + linkLabel.getTextHeight() < 110 ? 110 : 100 + linkLabel.getTextHeight();
		
		setDimensions(x, y, w, h);
		requestFocus();
		getTopParent().setFocusLockObject(this);
		setTitle("Opening Web Link");
		setTitleColor(EColors.lgray.intVal);
		
		yes = new WindowButton(this, midX - copyLength - 35, endY - 35, 75, 20, I18n.format("gui.yes", new Object[0])).setStringColor(0x55ff55);
		copy = new WindowButton(this, midX - (copyLength  + 8) / 2, endY - 35, copyLength + 8, 20, I18n.format("chat.copy", new Object[0])).setStringColor(0xffffff);
		no = new WindowButton(this, midX + copyLength - 39, endY - 35, 75, 20, I18n.format("gui.no", new Object[0])).setStringColor(0xff5555);
		
		addObject(promptLabel, linkLabel, warningLabel, yes, copy, no);
		
		super.initObjects();
	}
	
	@Override
	public void actionPerformed(IActionObject object, Object... args) {
		if (object.equals(yes)) { openWebLink(link); close(); }
		if (object.equals(copy)) { ((GuiScreen) getTopParent()).setClipboardString(link); close(); }
		if (object.equals(no)) { close(); }
	}
	
}
