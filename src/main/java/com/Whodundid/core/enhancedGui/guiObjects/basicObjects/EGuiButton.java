package com.Whodundid.core.enhancedGui.guiObjects.basicObjects;

import com.Whodundid.core.coreSubMod.EMCResources;
import com.Whodundid.core.enhancedGui.types.EnhancedActionObject;
import com.Whodundid.core.enhancedGui.types.interfaces.IEnhancedActionObject;
import com.Whodundid.core.enhancedGui.types.interfaces.IEnhancedGuiObject;
import com.Whodundid.core.subMod.SubMod;
import com.Whodundid.core.util.renderUtil.EColors;
import com.Whodundid.core.util.storageUtil.EDimension;
import com.Whodundid.core.util.storageUtil.ModSetting;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.input.Mouse;

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
	protected ResourceLocation btnTexture = EMCResources.guiButtonBase;
	protected ResourceLocation btnSelTexture = EMCResources.guiButtonSel;
	
	protected EGuiButton(IEnhancedGuiObject parentIn) { super(parentIn); }
	public EGuiButton(IEnhancedGuiObject parentIn, int posX, int posY, int width, int height, ModSetting<Boolean> settingIn) {
		this(parentIn, posX, posY, width, height);
		setTrueFalseButton(true, settingIn);
	}
	public EGuiButton(IEnhancedGuiObject parentIn, int posX, int posY, int width, int height) { this(parentIn, posX, posY, width, height, ""); }
	public EGuiButton(IEnhancedGuiObject parentIn, int posX, int posY, int width, int height, String displayStringIn) {
		init(parentIn, posX, posY, width, height);
		displayLabel = new EGuiLabel(this, (drawCentered ? midX : startX + 3) + textOffset, startY + (height - 7) / 2, displayStringIn) {
			@Override
			public void drawObject(int mX, int mY, float ticks) {
				if (drawString) { super.drawObject(mX, mY, ticks); }
			}
		};
		displayLabel.setDrawCentered(drawCentered).setClickable(false);
		addObject(displayLabel);
	}

	@Override
	public void drawObject(int mX, int mY, float ticks) {
		if (drawBackground) { drawRect(startX, startY, endX, endY, backgroundColor); }
		boolean mouseHover = isMouseOver(mX, mY);
		boolean mouseCheck = !Mouse.isButtonDown(0) && mouseHover;
		int stringColor = isEnabled() ? (mouseCheck ? (color == 14737632 ? textHoverColor : color) : color) : color + 0xbbbbbb;
		displayLabel.setDisplayStringColor(stringColor);
		if (drawDefault) {
			GlStateManager.color(1.0f, 1.0f, 1.0f);
			if (mouseHover) {
				if (btnSelTexture != null) { mc.renderEngine.bindTexture(btnSelTexture); }
			} else {
				if (btnTexture != null) { mc.renderEngine.bindTexture(btnTexture); }
			}
			GlStateManager.enableBlend();
	        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
	        GlStateManager.blendFunc(770, 771);
	    	displayLabel.setDisplayStringColor(stringColor);
	    	if (usingBaseTexture || usingBaseSelTexture) {
	    		if (stretchBaseTextures) {
					drawModalRectWithCustomSizedTexture(startX, startY, 0, 0, width, height, width, height);
				} else {
					mc.renderEngine.bindTexture(EMCResources.guiButtons);
					int i = height > 20 ? 20 : height;
					int offset = mouseHover ? 20 : 0;
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
			}
	    	else if (btnTexture != null || btnSelTexture != null) {
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
		pressButton(button);
    }
	
	@Override
	public void keyPressed(char typedChar, int keyCode) {
		if (keyCode == 28) {
			if (getParent() != null) { performAction(null); }
		}
		super.keyPressed(typedChar, keyCode);
	}
	
	//------------------
	//EGuiButton methods
	//------------------
	
	protected void pressButton(int button) {
		if (enabled && checkDraw()) {
			pressedButton = button;
			if (runActionOnPress) { onPress(); }
			else if (button == 0) {
				playPressSound();
				performAction(null);
			}
		}
	}
	
	public EGuiButton updateTrueFalseDisplay(ModSetting<Boolean> setting) { return updateTrueFalseDisplay(setting.get()); }
	public EGuiButton updateTrueFalseDisplay(boolean val) {
		if (trueFalseButton) { setDisplayString(val ? "True" : "False").setDisplayStringColor(val ? 0x55ff55 : 0xff5555); }
		return this;
	}
	
	public EGuiButton toggleTrueFalse(ModSetting<Boolean> setting, SubMod m, boolean saveAll) {
		if (trueFalseButton) {
			boolean val = setting.get();
			setDisplayString(val ? "True" : "False").setDisplayStringColor(val ? 0x55ff55 : 0xff5555);
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
		
	public static void playPressSound() { mc.getSoundHandler().playSound(PositionedSoundRecord.create(EMCResources.buttonSound, 1.0F)); }
	
	public EGuiButton setTextures(ResourceLocation base, ResourceLocation sel) { setButtonTexture(base); setButtonSelTexture(sel); return this; }
	public EGuiButton setButtonTexture(ResourceLocation loc) { btnTexture = loc; usingBaseTexture = EMCResources.guiButtonBase.equals(loc); return this; }
	public EGuiButton setButtonSelTexture(ResourceLocation loc) { btnSelTexture = loc; usingBaseSelTexture = EMCResources.guiButtonSel.equals(loc); return this; }
	
	public EGuiButton setDisplayString(String stringIn) { displayLabel.setDisplayString(stringIn); return this; }
	public EGuiButton setDisplayStringColor(int colorIn) { color = colorIn; return this; }
	public EGuiButton setDisplayStringColor(EColors colorIn) { if (colorIn != null) { color = colorIn.c(); } return this; }
	public EGuiButton setDisplayStringHoverColor(int colorIn) { textHoverColor = colorIn; return this; }
	public EGuiButton setDisplayStringHoverColor(EColors colorIn) { if (colorIn != null) { textHoverColor = colorIn.c(); } return this; }
	public EGuiButton setDrawBackground(boolean val) { drawBackground = val; return this; }
	public EGuiButton setBackgroundColor(int colorIn) { backgroundColor = colorIn; return this; }
	public EGuiButton setBackgroundColor(EColors colorIn) { if (colorIn != null) { backgroundColor = colorIn.c(); } return this; }
	public EGuiButton setDrawDefault(boolean val) { drawDefault = val; return this; }
	public EGuiButton setTrueFalseButton(boolean val) { return setTrueFalseButton(val, false); }
	public EGuiButton setTrueFalseButton(boolean val, ModSetting<Boolean> settingIn) { return setTrueFalseButton(val, settingIn != null ? settingIn.get() : false); }
	public EGuiButton setTrueFalseButton(boolean val, boolean initial) { trueFalseButton = val; updateTrueFalseDisplay(initial); return this; }
	public EGuiButton setDrawString(boolean val) { drawString = val; return this; }
	
	public int getPressedButton() { return pressedButton; }
	public int getBackgroundColor() { return backgroundColor; }
	public int getDisplayStringColor() { return color; }
	public int getDisplayStringHoverColor() { return textHoverColor; }
	public String getDisplayString() { return displayLabel.getDisplayString(); }
	public EGuiLabel getDisplayLabel() { return displayLabel; }
}
