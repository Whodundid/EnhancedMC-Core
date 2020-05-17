package com.Whodundid.core.enhancedGui.guiObjects.actionObjects;

import com.Whodundid.core.app.AppConfigSetting;
import com.Whodundid.core.app.EMCApp;
import com.Whodundid.core.coreApp.EMCResources;
import com.Whodundid.core.enhancedGui.guiObjects.basicObjects.EGuiLabel;
import com.Whodundid.core.enhancedGui.types.EnhancedActionObject;
import com.Whodundid.core.enhancedGui.types.interfaces.IEnhancedGuiObject;
import com.Whodundid.core.util.EUtil;
import com.Whodundid.core.util.renderUtil.EColors;
import com.Whodundid.core.util.resourceUtil.EResource;
import com.Whodundid.core.util.storageUtil.EDimension;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.client.renderer.GlStateManager;
import org.lwjgl.input.Mouse;

//Author: Hunter Bragg

public class EGuiButton extends EnhancedActionObject {
	
	EGuiLabel displayLabel = new EGuiLabel(this, midX, midY, "");
	public static int defaultColor = 14737632;
	public int color = 14737632;
	public int textHoverColor = 0xffffa0;
	public int backgroundColor = 0xff000000;
	protected boolean usingBaseTextures = true;
	protected boolean stretchBaseTextures = false;
	protected boolean drawTextures = true;
	protected int pressedButton = -1;
	protected int textOffset = 0;
	protected boolean drawBackground = false;
	protected boolean trueFalseButton = false;
	protected boolean drawString = true;
	protected boolean drawCentered = true;
	protected EResource btnTexture = EMCResources.guiButtonBase;
	protected EResource btnSelTexture = null;
	
	protected EGuiButton(IEnhancedGuiObject parentIn) { super(parentIn); }
	public EGuiButton(IEnhancedGuiObject parentIn, int posX, int posY, int width, int height, AppConfigSetting<Boolean> settingIn) {
		this(parentIn, posX, posY, width, height);
		setTrueFalseButton(true, settingIn);
	}
	public EGuiButton(IEnhancedGuiObject parentIn, int posX, int posY, int width, int height) { this(parentIn, posX, posY, width, height, ""); }
	public EGuiButton(IEnhancedGuiObject parentIn, int posX, int posY, int width, int height, String displayStringIn) {
		setMaxDims(200, 20);
		init(parentIn, posX, posY, width, height);
		displayLabel = new EGuiLabel(this, (drawCentered ? midX : startX + 3) + textOffset, startY + (height - 7) / 2, displayStringIn) {
			@Override
			public void drawObject(int mX, int mY) {
				if (drawString) { super.drawObject(mX, mY); }
			}
		};
		displayLabel.setDrawCentered(drawCentered).setClickable(false);
		addObject(displayLabel);
	}

	@Override
	public void drawObject(int mX, int mY) {
		if (drawBackground) { drawRect(startX, startY, endX, endY, backgroundColor); }
		
		boolean mouseHover = isMouseOver(mX, mY);
		boolean mouseCheck = !Mouse.isButtonDown(0) && mouseHover;
		int stringColor = isEnabled() ? (mouseCheck ? (color == 14737632 ? textHoverColor : color) : color) : color + 0xbbbbbb;
		displayLabel.setDisplayStringColor(stringColor);
		
		//reset the color buffer to prepare for texture drawing
		GlStateManager.color(1.0f, 1.0f, 1.0f);
		
		//only draw textures if specified
		if (drawTextures) {
			
			//determine textures to draw
			if (usingBaseTextures) {
				if (mouseHover) { bindSel(); }
				else { bindBase(); }
			}
			else {
				if (btnTexture != null && btnSelTexture == null) { bindBase(); }
				else {
					if (mouseHover) { bindSel(); }
					else { bindBase(); }
				}
			}
			
			//prime the renderer
			GlStateManager.enableBlend();
	        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
	        GlStateManager.blendFunc(770, 771);
	    	
	        //draw the textures
	    	if (usingBaseTextures) {
	    		if (stretchBaseTextures) { drawTexture(startX, startY, width, height); }
	    		else { drawBaseTexture(mouseHover); }
			}
	    	else if (btnTexture != null) {
	    		drawTexture(startX, startY, width, height);
	    		if (btnSelTexture == null && mouseHover) {
	    			drawRect(startX + 1, startY + 1, endX - 1, endY - 1, 0x888B97D3);
	    			//drawRect(startX, startY, startX + 1, endY, EColors.orange); //left
	    			//drawRect(startX, startY, endX, startY + 1, EColors.orange); //top
	    			//drawRect(endX - 2, startY + 1, endX - 1, endY - 1, EColors.orange); //right
	    			//drawRect(startX + 1, endY - 2, endX - 2, endY - 1, EColors.orange); //bot
	    		}
	    	}
		}
		
		//draw disabled overlay
		if (!isEnabled()) { drawRect(startX, startY, endX, endY, 0x77000000); }
		
		super.drawObject(mX, mY);
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
		EUtil.ifNotNullDo(getWindowParent(), w -> w.bringToFront());
		pressButton(button);
    }
	
	@Override
	public void keyPressed(char typedChar, int keyCode) {
		if (keyCode == 28) {
			if (getParent() != null) { performAction(null, null); }
		}
		super.keyPressed(typedChar, keyCode);
	}
	
	//------------------
	//EGuiButton Methods
	//------------------
	
	protected void pressButton(int button) {
		if (enabled && checkDraw()) {
			pressedButton = button;
			if (runActionOnPress) { onPress(); }
			else if (button == 0) {
				playPressSound();
				performAction(null, null);
			}
		}
	}
	
	public EGuiButton updateTrueFalseDisplay() {
		if (trueFalseButton) {
			String cur = getDisplayString();
			boolean val = cur.equals("False");
			setDisplayString(val ? "True" : "False").setDisplayStringColor(val ? 0x55ff55 : 0xff5555);
		}
		return this;
	}
	
	public EGuiButton updateTrueFalseDisplay(AppConfigSetting<Boolean> setting) { return updateTrueFalseDisplay(setting.get()); }
	public EGuiButton updateTrueFalseDisplay(boolean val) {
		if (trueFalseButton) { setDisplayString(val ? "True" : "False").setDisplayStringColor(val ? 0x55ff55 : 0xff5555); }
		return this;
	}
	
	public EGuiButton toggleTrueFalse(AppConfigSetting<Boolean> setting, EMCApp m, boolean saveAll) {
		if (trueFalseButton) {
			boolean val = setting.get();
			setting.set(!val);
			setDisplayString(!val ? "True" : "False").setDisplayStringColor(!val ? 0x55ff55 : 0xff5555);
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
	
	//----------------------
	//Drawing Helper Methods
	//----------------------
	
	//texture binding methods
	private void bindBase() { if (btnTexture != null) { bindTexture(btnTexture); } }
	private void bindSel() { if (btnSelTexture != null ) { bindTexture(btnSelTexture); } }
	
	//draw method
	private void drawBaseTexture(boolean mouseHover) {
		bindTexture(EMCResources.mcWidgets);
		int i = height > 20 ? 20 : height;
		int offset = mouseHover ? 20 : 0;
		if (!isEnabled()) { offset = 0; }
		if (height < 20) {
			i = i >= 3 ? i - 2 : i;
			drawTexturedModalRect(startX, startY, 0, 66 + offset, width - 2, i);
    		drawTexturedModalRect(startX + width - 2, startY, 198, 66 + offset, 2, i);
    		drawTexturedModalRect(startX, startY + height - 2, 0, 84 + offset, width - 2, 2);
    		drawTexturedModalRect(startX + width - 2, startY + height - 2, 198, 84 + offset, 2, 2);
		} else {
			drawTexturedModalRect(startX, startY, 0, 66 + offset, width - 2, i);
    		drawTexturedModalRect(startX + width - 2, startY, 198, 66 + offset, 2, i);
		}
	}
	
	private void checkForBaseTextures() {
		usingBaseTextures = EMCResources.guiButtonBase.equals(btnTexture) && EMCResources.guiButtonSel.equals(btnSelTexture);
	}
	
	
	//------------------
	//EGuiButton Setters
	//------------------
	
	public EGuiButton setTextures(EResource base, EResource sel) { setButtonTexture(base); setButtonSelTexture(sel); return this; }
	public EGuiButton setButtonTexture(EResource loc) { btnTexture = loc; checkForBaseTextures(); return this; }
	public EGuiButton setButtonSelTexture(EResource loc) { btnSelTexture = loc; checkForBaseTextures(); return this; }
	public EGuiButton setDrawTextures(boolean val) { drawTextures = val; return this; }
	public EGuiButton setDisplayString(String stringIn) { displayLabel.setDisplayString(stringIn); return this; }
	public EGuiButton setDisplayStringColor(int colorIn) { color = colorIn; return this; }
	public EGuiButton setDisplayStringColor(EColors colorIn) { if (colorIn != null) { color = colorIn.c(); } return this; }
	public EGuiButton setDisplayStringHoverColor(int colorIn) { textHoverColor = colorIn; return this; }
	public EGuiButton setDisplayStringHoverColor(EColors colorIn) { if (colorIn != null) { textHoverColor = colorIn.c(); } return this; }
	public EGuiButton setDrawBackground(boolean val) { drawBackground = val; return this; }
	public EGuiButton setBackgroundColor(int colorIn) { backgroundColor = colorIn; return this; }
	public EGuiButton setBackgroundColor(EColors colorIn) { if (colorIn != null) { backgroundColor = colorIn.c(); } return this; }
	public EGuiButton setTrueFalseButton(boolean val) { return setTrueFalseButton(val, false); }
	public EGuiButton setTrueFalseButton(boolean val, AppConfigSetting<Boolean> settingIn) { return setTrueFalseButton(val, settingIn != null ? settingIn.get() : false); }
	public EGuiButton setTrueFalseButton(boolean val, boolean initial) { trueFalseButton = val; updateTrueFalseDisplay(initial); return this; }
	public EGuiButton setDrawString(boolean val) { drawString = val; return this; }
	public EGuiButton setDrawStretched(boolean val) { stretchBaseTextures = val; return this; }
	
	//------------------
	//EGuiButton Getters
	//------------------
	
	public int getPressedButton() { return pressedButton; }
	public int getBackgroundColor() { return backgroundColor; }
	public int getDisplayStringColor() { return color; }
	public int getDisplayStringHoverColor() { return textHoverColor; }
	public String getDisplayString() { return displayLabel.getDisplayString(); }
	public EGuiLabel getDisplayLabel() { return displayLabel; }
	
	//--------------
	//Static Methods
	//--------------
	
	public static void playPressSound() { mc.getSoundHandler().playSound(PositionedSoundRecord.create(EMCResources.buttonSound, 1.0F)); }
}
