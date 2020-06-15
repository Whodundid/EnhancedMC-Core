package com.Whodundid.core.windowLibrary.windowObjects.actionObjects;

import com.Whodundid.core.EnhancedMC;
import com.Whodundid.core.app.EMCApp;
import com.Whodundid.core.app.config.AppConfigSetting;
import com.Whodundid.core.coreApp.EMCResources;
import com.Whodundid.core.util.EUtil;
import com.Whodundid.core.util.renderUtil.EColors;
import com.Whodundid.core.util.resourceUtil.EResource;
import com.Whodundid.core.util.storageUtil.EDimension;
import com.Whodundid.core.windowLibrary.windowObjects.basicObjects.WindowLabel;
import com.Whodundid.core.windowLibrary.windowTypes.ActionObject;
import com.Whodundid.core.windowLibrary.windowTypes.interfaces.IWindowObject;
import java.util.function.Consumer;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.client.renderer.GlStateManager;
import org.lwjgl.input.Mouse;

//Author: Hunter Bragg

public class WindowButton extends ActionObject {
	
	WindowLabel displayLabel = new WindowLabel(this, midX, midY, "");
	public static int defaultColor = 14737632;
	public int color = 14737632;
	public int disabledColor = EColors.lgray.intVal;
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
	protected boolean drawDisabledColor = false;
	protected boolean drawCentered = true;
	protected EResource btnTexture = EMCResources.guiButtonBase;
	protected EResource btnSelTexture = null;
	
	protected WindowButton(IWindowObject parentIn) { super(parentIn); }
	public WindowButton(IWindowObject parentIn, int posX, int posY, int width, int height, AppConfigSetting<Boolean> settingIn) {
		this(parentIn, posX, posY, width, height);
		setTrueFalseButton(true, settingIn);
	}
	
	public WindowButton(IWindowObject parentIn, int posX, int posY, int width, int height) { this(parentIn, posX, posY, width, height, ""); }
	public WindowButton(IWindowObject parentIn, int posX, int posY, int width, int height, String displayStringIn) {
		setMaxDims(200, 20);
		init(parentIn, posX, posY, width, height);
		displayLabel = new WindowLabel(this, (drawCentered ? midX : startX + 3) + textOffset, startY + (height - 7) / 2, displayStringIn) {
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
		
		boolean mouseHover = isClickable() && isMouseOver(mX, mY);
		boolean mouseCheck = !Mouse.isButtonDown(0) && mouseHover;
		int stringColor = isEnabled() ? (mouseCheck ? (color == 14737632 ? textHoverColor : color) : color) : (drawDisabledColor ? disabledColor : color + 0xbbbbbb);
		displayLabel.setColor(stringColor);
		
		//reset the color buffer to prepare for texture drawing
		GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
		
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
	    	
			//draw disabled overlay
			if (!isEnabled()) { drawRect(startX, startY, endX, endY, 0x77000000); }
		}
		
		super.drawObject(mX, mY);
	}
	
	@Override
	public WindowButton setDimensions(EDimension dimIn) {
		if (dimIn != null) { return setDimensions(dimIn.startX, dimIn.startY, dimIn.width, dimIn.height); }
		return this;
	}
	
	@Override
	public WindowButton setDimensions(int startXIn, int startYIn, int widthIn, int heightIn) {
		super.setDimensions(startXIn, startYIn, widthIn, heightIn);
		if (displayLabel != null) {
			displayLabel.setDimensions((drawCentered ? midX : startX + 3) + textOffset, startY + (height - 7) / 2, displayLabel.width, displayLabel.height);
		}
		return this;
	}
	
	@Override
	public void mousePressed(int mX, int mY, int button) {
		if (isClickable()) {
			EUtil.ifNotNullDo(getWindowParent(), w -> w.bringToFront());
			pressButton(button);
		}
    }
	
	@Override
	public void keyPressed(char typedChar, int keyCode) {
		if (keyCode == 28) {
			if (isEnabled() && checkDraw() && isClickable()) { performAction(); }
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
				performAction();
			}
		}
	}
	
	public WindowButton updateTrueFalseDisplay() {
		if (trueFalseButton) {
			String cur = getString();
			boolean val = cur.equals("False");
			setString(val ? "True" : "False").setStringColor(val ? 0x55ff55 : 0xff5555);
		}
		return this;
	}
	
	public WindowButton updateTrueFalseDisplay(AppConfigSetting<Boolean> setting) { return updateTrueFalseDisplay(setting.get()); }
	public WindowButton updateTrueFalseDisplay(boolean val) {
		if (trueFalseButton) { setString(val ? "True" : "False").setStringColor(val ? 0x55ff55 : 0xff5555); }
		return this;
	}
	
	public WindowButton toggleTrueFalse(AppConfigSetting<Boolean> setting, EMCApp m, boolean saveAll) { return toggleTrueFalse(setting, m, saveAll, true); }
	public WindowButton toggleTrueFalse(AppConfigSetting<Boolean> setting, EMCApp m, boolean saveAll, boolean reloadWindows) {
		if (trueFalseButton) {
			boolean val = setting.get();
			setting.set(!val);
			setString(!val ? "True" : "False").setStringColor(!val ? 0x55ff55 : 0xff5555);
			if (m != null) {
				if (saveAll) { m.getConfig().saveAllConfigs(); }
				else { m.getConfig().saveMainConfig(); }
			}
			if (reloadWindows) { EnhancedMC.reloadAllWindows(); }
		}
		return this;
	}
	
	public WindowButton setDrawStringCentered(boolean val) {
		drawCentered = val;
		displayLabel.setDrawCentered(val);
		//displayLabel.setDimensions((drawCentered ? midX : startX + 3) + textOffset, startY + (height - 7) / 2, displayLabel.width, displayLabel.height);
		return this;
	}
	
	public WindowButton setDisplayStringOffset(int offsetIn) {
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
	//EGuiButton Getters
	//------------------
	
	public int getPressedButton() { return pressedButton; }
	public int getBackgroundColor() { return backgroundColor; }
	public int getStringColor() { return color; }
	public int getStringHoverColor() { return textHoverColor; }
	public String getString() { return displayLabel.getString(); }
	public WindowLabel getDisplayLabel() { return displayLabel; }
	
	//------------------
	//EGuiButton Setters
	//------------------
	
	public WindowButton setTextures(EResource base, EResource sel) { setButtonTexture(base); setButtonSelTexture(sel); return this; }
	public WindowButton setButtonTexture(EResource loc) { btnTexture = loc; checkForBaseTextures(); return this; }
	public WindowButton setButtonSelTexture(EResource loc) { btnSelTexture = loc; checkForBaseTextures(); return this; }
	public WindowButton setString(String stringIn) { displayLabel.setString(stringIn); return this; }
	public WindowButton setStringColor(int colorIn) { color = colorIn; return this; }
	public WindowButton setStringColor(EColors colorIn) { if (colorIn != null) { color = colorIn.c(); } return this; }
	public WindowButton setStringDisabledColor(int colorIn) { disabledColor = colorIn; return this; }
	public WindowButton setStringDisabledColor(EColors colorIn) { if (colorIn != null) { disabledColor = colorIn.c(); } return this; }
	public WindowButton setStringHoverColor(int colorIn) { textHoverColor = colorIn; return this; }
	public WindowButton setStringHoverColor(EColors colorIn) { if (colorIn != null) { textHoverColor = colorIn.c(); } return this; }
	public WindowButton setBackgroundColor(int colorIn) { backgroundColor = colorIn; return this; }
	public WindowButton setBackgroundColor(EColors colorIn) { if (colorIn != null) { backgroundColor = colorIn.c(); } return this; }
	public WindowButton setTrueFalseButton(boolean val) { return setTrueFalseButton(val, false); }
	public WindowButton setTrueFalseButton(boolean val, AppConfigSetting<Boolean> settingIn) { return setTrueFalseButton(val, settingIn != null ? settingIn.get() : false); }
	public WindowButton setTrueFalseButton(boolean val, boolean initial) { trueFalseButton = val; updateTrueFalseDisplay(initial); return this; }
	public WindowButton setDrawTextures(boolean val) { drawTextures = val; return this; }
	public WindowButton setDrawString(boolean val) { drawString = val; return this; }
	public WindowButton setDrawDisabledColor(boolean val) { drawDisabledColor = val; return this; }
	public WindowButton setDrawStretched(boolean val) { stretchBaseTextures = val; return this; }
	public WindowButton setDrawBackground(boolean val) { drawBackground = val; return this; }
	
	//--------------
	//Static Methods
	//--------------
	
	public static void playPressSound() { mc.getSoundHandler().playSound(PositionedSoundRecord.create(EMCResources.buttonSound, 1.0F)); }
	
	public static void setTextures(EResource base, EResource sel, WindowButton button, WindowButton... additional) { setButtonVal(b -> b.setTextures(base, sel), button, additional); }
	public static void setButtonTexture(EResource base, WindowButton button, WindowButton... additional) { setButtonVal(b -> b.setButtonTexture(base), button, additional); }
	public static void setButtonSelTexture(EResource sel, WindowButton button, WindowButton... additional) { setButtonVal(b -> b.setButtonSelTexture(sel), button, additional); }
	public static void setString(String textIn, WindowButton button, WindowButton... additional) { setButtonVal(b -> b.setString(textIn), button, additional); }
	public static void setStringColor(EColors colorIn, WindowButton button, WindowButton... additional) { setButtonVal(b -> b.setStringColor(colorIn), button, additional); }
	public static void setStringColor(int colorIn, WindowButton button, WindowButton... additional) { setButtonVal(b -> b.setStringHoverColor(colorIn), button, additional); }
	public static void setStringHoverColor(EColors colorIn, WindowButton button, WindowButton... additional) { setButtonVal(b -> b.setStringHoverColor(colorIn), button, additional); }
	public static void setStringHoverColor(int colorIn, WindowButton button, WindowButton... additional) { setButtonVal(b -> b.setStringColor(colorIn), button, additional); }
	public static void setStringDisabledColor(EColors colorIn, WindowButton button, WindowButton... additional) { setButtonVal(b -> b.setStringDisabledColor(colorIn), button, additional); }
	public static void setStringDisabledColor(int colorIn, WindowButton button, WindowButton... additional) { setButtonVal(b -> b.setStringDisabledColor(colorIn), button, additional); }
	public static void setBackgroundColor(EColors colorIn, WindowButton button, WindowButton... additional) { setButtonVal(b -> b.setBackgroundColor(colorIn), button, additional); }
	public static void setBackgroundColor(int colorIn, WindowButton button, WindowButton... additional) { setButtonVal(b -> b.setBackgroundColor(colorIn), button, additional); }
	public static void setDrawString(boolean val, WindowButton button, WindowButton... additional) { setButtonVal(b -> b.setDrawString(val), button, additional); }
	public static void setDrawDisabledColor(boolean val, WindowButton button, WindowButton... additional) { setButtonVal(b -> b.setDrawDisabledColor(val), button, additional); }
	public static void setDrawStretched(boolean val, WindowButton button, WindowButton... additional) { setButtonVal(b -> b.setDrawStretched(val), button, additional); }
	public static void setDrawTextures(boolean val, WindowButton button, WindowButton... additional) { setButtonVal(b -> b.setDrawTextures(val), button, additional); }
	public static void setDrawBackground(boolean val, WindowButton button, WindowButton... additional) { setButtonVal(b -> b.setDrawBackground(val), button, additional); }
	
	private static void setButtonVal(Consumer<? super WindowButton> action, WindowButton button, WindowButton... additional) {
		EUtil.filterNullDo(action, EUtil.add(button, additional));
	}
	
}
