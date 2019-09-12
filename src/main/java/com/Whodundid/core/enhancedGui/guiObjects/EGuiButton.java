package com.Whodundid.core.enhancedGui.guiObjects;

import com.Whodundid.core.enhancedGui.EnhancedActionObject;
import com.Whodundid.core.enhancedGui.interfaces.IEnhancedActionObject;
import com.Whodundid.core.enhancedGui.interfaces.IEnhancedGuiObject;
import com.Whodundid.core.subMod.SubMod;
import com.Whodundid.core.util.renderUtil.Resources;
import com.Whodundid.core.util.storageUtil.EDimension;
import com.Whodundid.core.util.storageUtil.ModSetting;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.input.Mouse;

//Last edited: Jan 2, 2019
//First Added: Sep 14, 2018
//Author: Hunter Bragg

public class EGuiButton extends EnhancedActionObject implements IEnhancedActionObject {
	
	EGuiLabel displayLabel = new EGuiLabel(this, midX, midY, "");
	public static int defaultColor = 14737632;
	public int color = 14737632;
	public int textHoverColor = 0xffffa0;
	public int backgroundColor = 0xff000000;
	protected boolean usingBaseTexture = true, usingBaseSelTexture = true;
	protected boolean stretchBaseTextures = false;
	protected int pressedButton = -1;
	protected int textOffset = 0;
	protected boolean drawBackground = false;
	protected boolean drawDefault = true;
	protected boolean trueFalseButton = false;
	protected boolean drawString = true;
	protected boolean drawCentered = true;
	protected ResourceLocation btnTexture = Resources.guiButtonBase;
	protected ResourceLocation btnSelTexture = Resources.guiButtonSel;
	
	protected EGuiButton(IEnhancedGuiObject parentIn) { super(parentIn); }
	public EGuiButton(IEnhancedGuiObject parentIn, int posX, int posY, int width, int height) { this(parentIn, posX, posY, width, height, ""); }
	public EGuiButton(IEnhancedGuiObject parentIn, int posX, int posY, int width, int height, String displayStringIn) {
		init(parentIn, posX, posY, width, height);
		displayLabel = new EGuiLabel(this, (drawCentered ? midX : startX + 3) + textOffset, startY + (height - 7) / 2, displayStringIn) {
			@Override
			public void drawObject(int mX, int mY, float ticks) {
				if (drawString) { super.drawObject(mX, mY, ticks); }
			}
		};
		displayLabel.setDrawCentered(drawCentered);
		addObject(displayLabel);
	}

	@Override
	public void drawObject(int mX, int mY, float ticks) {
		if (drawBackground) { drawRect(startX, startY, endX, endY, backgroundColor); }
		boolean mouseCheck = !Mouse.isButtonDown(0) && isMouseHover;
		//int stringColor = isEnabled() ? (isMouseHover ? (color == 14737632 ? textHoverColor : color) : color) : 0x979797;
		int stringColor = isEnabled() ? (mouseCheck ? (color == 14737632 ? textHoverColor : color) : color) : color + 0xbbbbbb;
		displayLabel.setDisplayStringColor(stringColor);
		if (drawDefault) {
			GlStateManager.color(1.0f, 1.0f, 1.0f);
			if (isMouseHover) {
				if (btnSelTexture != null) { mc.renderEngine.bindTexture(btnSelTexture); }
			} else {
				if (btnTexture != null) { mc.renderEngine.bindTexture(btnTexture); }
			}
			GlStateManager.enableBlend();
	        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
	        GlStateManager.blendFunc(770, 771);
	    	//if (!isEnabled()) { stringColor = 0xaaaaaa; }
	    	displayLabel.setDisplayStringColor(stringColor);
	    	if (usingBaseTexture || usingBaseSelTexture) {
	    		if (stretchBaseTextures) {
					drawModalRectWithCustomSizedTexture(startX, startY, 0, 0, width, height, width, height);
				} else {
					mc.renderEngine.bindTexture(Resources.guiButtons);
					int i = height > 20 ? 20 : height;
					int offset = isMouseHover ? 20 : 0;
					if (!isEnabled()) { offset = 0; }
					if (height < 20) {
						i = i >= 3 ? i - 2 : i;
						drawTexturedModalRect(startX, startY, 0, 0 + offset, width - 2, i);
	            		drawTexturedModalRect(startX + width - 2, startY, 198, 0 + offset, 2, i);
	            		drawTexturedModalRect(startX, startY + height - 2, 0, 18 + offset, width - 2, 2);
	            		drawTexturedModalRect(startX + width - 2, startY + height - 2, 198, 18 + offset, 2, 2);
					} else {
						drawTexturedModalRect(startX, startY, 0, 0 + offset, width - 2, i);
	            		drawTexturedModalRect(startX + width - 2, startY, 198, 0 + offset, 2, i);
					}
				}
			} else {
				drawModalRectWithCustomSizedTexture(startX, startY, 0, 0, width, height, width, height);
			}
		}
		if (!isEnabled()) { drawRect(startX, startY, endX, endY, 0x77000000); }
		super.drawObject(mX, mY, ticks);
	}
	
	@Override
	public EGuiButton setDimensions(EDimension dimIn) {
		if (dimIn != null) { return setDimensions(dimIn.startX, dimIn.startY, dimIn.width, dimIn.height); }
		return this;
	}
	
	@Override
	public EGuiButton setDimensions(int startXIn, int startYIn, int widthIn, int heightIn) {
		super.setDimensions(startXIn, startYIn, widthIn, heightIn);
		if (displayLabel != null) {
			displayLabel.setDimensions((drawCentered ? midX : startX + 3) + textOffset, startY + (height - 7) / 2, displayLabel.width, displayLabel.height);
		}
		return this;
	}
	
	@Override
	public void mousePressed(int mX, int mY, int button) {
		super.mousePressed(mX, mY, button);
		if (hasFocus()) { pressButton(button); }
    }
	
	@Override
	public void keyPressed(char typedChar, int keyCode) {
		if (keyCode == 28) {
			if (getParent() != null) { getParent().actionPerformed(this); }
		}
		super.keyPressed(typedChar, keyCode);
	}
	
	//------------------
	//EGuiButton methods
	//------------------
	
	protected void pressButton(int button) {
		if (enabled && checkDraw() && isMouseHover) {
			pressedButton = button;
			if (runActionOnPress) { performAction(); }
			else if (button == 0) {
				playPressSound();
				if (actionReciever != null) { actionReciever.actionPerformed(this); }
			}
		}
	}
	
	public EGuiButton updateTrueFalseDisplay(boolean val) {
		if (trueFalseButton) { setDisplayString(val ? "True" : "False").setDisplayStringColor(val ? 0x55ff55 : 0xff5555); }
		return this;
	}
	
	public EGuiButton updateTrueFalseDisplay(ModSetting setting) {
		if (trueFalseButton) { setDisplayString(setting.get() ? "True" : "False").setDisplayStringColor(setting.get() ? 0x55ff55 : 0xff5555); }
		return this;
	}
	
	public EGuiButton toggleTrueFalse(ModSetting setting, SubMod m, boolean saveAll) {
		if (trueFalseButton) {
			setting.set(!setting.get());
			setDisplayString(setting.get() ? "True" : "False").setDisplayStringColor(setting.get() ? 0x55ff55 : 0xff5555);
			if (m != null) {
				if (saveAll) { m.getConfig().saveAllConfigs(); }
				else { m.getConfig().saveMainConfig(); }
			}
		}
		return this;
	}
	
	public EGuiButton setDrawStringCentered(boolean val) {
		drawCentered = val;
		displayLabel.setDrawCentered(val);
		//displayLabel.setDimensions((drawCentered ? midX : startX + 3) + textOffset, startY + (height - 7) / 2, displayLabel.width, displayLabel.height);
		return this;
	}
	
	public EGuiButton setDisplayStringOffset(int offsetIn) {
		textOffset = offsetIn;
		displayLabel.setDimensions((drawCentered ? midX : startX + 3) + textOffset, startY + (height - 7) / 2, displayLabel.width, displayLabel.height);
		return this;
	}
		
	public static void playPressSound() { mc.getSoundHandler().playSound(PositionedSoundRecord.create(Resources.buttonSound, 1.0F)); }
	
	public int getPressedButton() { return pressedButton; }
	public EGuiButton setDisplayString(String stringIn) { displayLabel.setDisplayString(stringIn); return this; }
	public EGuiButton setDisplayStringColor(int colorIn) { color = colorIn; return this; }
	public EGuiButton setDisplayStringHoverColor(int colorIn) { textHoverColor = colorIn; return this; }
	public EGuiButton setTextures(ResourceLocation base, ResourceLocation sel) { setButtonTexture(base); setButtonSelTexture(sel); return this; }
	public EGuiButton setButtonTexture(ResourceLocation loc) { btnTexture = loc; if (loc != null) { usingBaseTexture = loc.equals(Resources.guiButtonBase); } return this; }
	public EGuiButton setButtonSelTexture(ResourceLocation loc) { btnSelTexture = loc; if (loc != null) { usingBaseSelTexture = loc.equals(Resources.guiButtonSel); } return this; }
	public EGuiButton setDrawBackground(boolean val) { drawBackground = val; return this; }
	public EGuiButton setBackgroundColor(int colorIn) { backgroundColor = colorIn; return this; }
	public EGuiButton setDrawDefault(boolean val) { drawDefault = val; return this; }
	public EGuiButton setTrueFalseButton(boolean val) { return setTrueFalseButton(val, false); }
	public EGuiButton setTrueFalseButton(boolean val, ModSetting settingIn) { return setTrueFalseButton(val, settingIn != null ? settingIn.get() : false); }
	public EGuiButton setTrueFalseButton(boolean val, boolean initial) { trueFalseButton = val; updateTrueFalseDisplay(initial); return this; }
	public EGuiButton setDrawString(boolean val) { drawString = val; return this; }
	public String getDisplayString() { return displayLabel.getDisplayString(); }
	public EGuiLabel getDisplayLabel() { return displayLabel; }
}
