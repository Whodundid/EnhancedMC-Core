package com.Whodundid.slc.window;

import java.util.Set;
import com.Whodundid.core.app.AppType;
import com.Whodundid.core.app.RegisteredApps;
import com.Whodundid.core.windowLibrary.windowObjects.actionObjects.WindowButton;
import com.Whodundid.core.windowLibrary.windowObjects.advancedObjects.header.WindowHeader;
import com.Whodundid.core.windowLibrary.windowTypes.WindowParent;
import com.Whodundid.core.windowLibrary.windowTypes.interfaces.IActionObject;
import com.Whodundid.slc.SLCApp;
import com.Whodundid.slc.util.LayerTypes;
import com.Whodundid.slc.util.SLCResources;
import com.Whodundid.slc.util.SkinPart;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.util.ResourceLocation;

public class SLCPartWindow extends WindowParent {
	
	SLCApp sls = (SLCApp) RegisteredApps.getApp(AppType.SLC);
	ResourceLocation playerSkin;
	ResourceLocation playerCape;
	GameSettings mcSettings;
	String skinType;
	WindowButton resetBtn, doneBtn;
	WindowButton fiveUpBtn, twoFiveUpBtn, hundredUpBtn, fiveHundredUpBtn;
	WindowButton fiveDownBtn, twoFiveDownBtn, hundredDownBtn, fiveHundredDownBtn;
	SkinPart part;
	String enabled, mode;
	Set modelParts;
	boolean SSpeedSel, BDelaySel, BDurationSel, OffsetSel;
	boolean ssIsSelectable, bdelayIsSelectable, bdurationIsSelectable;
	boolean isEnumEnabled;
	
	public SLCPartWindow() {
		super();
		windowIcon = SLCResources.icon;
	}
	
	@Override
	public void initWindow() {
		setDimensions(200, 256);
		
		mcSettings = mc.gameSettings;
		
		part = sls.getPart(sls.currentPart);
		enabled = (part.isEnabled()) ? "Enabled" : "Disabled";
		mode = (part.isSwitching()) ? "Switch" : "Blink";
		
		playerSkin = mc.thePlayer.getLocationSkin();
		playerCape = mc.thePlayer.getLocationCape();
		skinType = mc.thePlayer.getSkinType();
		
		modelParts = mcSettings.getModelParts();
		isEnumEnabled = modelParts.contains(part.getType().getMCType());
		SSpeedSel = true;
		BDelaySel = true;
		
		setObjectName("SLC Part Menu");
	}
	
	@Override
	public void initObjects() {
		setHeader(new WindowHeader(this));
		
		doneBtn = new WindowButton(this, midX + 56, midY + 104, 40, 20, "Done");
		resetBtn = new WindowButton(this, midX + 9, midY + 104, 40, 20, "Reset");
		fiveUpBtn = new WindowButton(this, midX + 70, midY + 76, 25, 20, "+5");
		twoFiveUpBtn = new WindowButton(this, midX + 39, midY + 76, 25, 20, "+25");
		hundredUpBtn = new WindowButton(this, midX + 2, midY + 76, 30, 20, "+100");
		hundredDownBtn = new WindowButton(this, midX - 36, midY + 76, 30, 20, "-100");
		twoFiveDownBtn = new WindowButton(this, midX - 67, midY + 76, 25, 20, "-25");
		fiveDownBtn = new WindowButton(this, midX - 94, midY + 76, 20, 20, "-5");
		
		addObject(doneBtn, resetBtn, fiveUpBtn, twoFiveUpBtn, hundredUpBtn, hundredDownBtn, twoFiveDownBtn, fiveDownBtn);
	}

	@Override
	public void drawObject(int mXIn, int mYIn) {
		bindTexture(SLCResources.SLSpartGui);
		drawTexturedModalRect(midX - width / 2, midY - height / 2, 0, 0, width, height);
		drawPartEnabled();
		drawMouseEntered();
		drawPartTitle();
		drawPartStats();
		drawCheckedBoxes();
		drawSelectedBoxes();
		drawPartVisual();
		super.drawObject(mXIn, mYIn);
	}
	
	@Override
	public void actionPerformed(IActionObject object, Object... args) {
		if (object.equals(doneBtn)) { returnToMainGui(); }
		if (object.equals(fiveUpBtn)) { addToValues(5); }
		if (object.equals(twoFiveUpBtn)) { addToValues(25); }
		if (object.equals(hundredUpBtn)) { addToValues(100); }
		if (object.equals(hundredDownBtn)) { addToValues(-100); }
		if (object.equals(twoFiveDownBtn)) { addToValues(-25); }
		if (object.equals(fiveDownBtn)) { addToValues(-5);}
		if (object.equals(resetBtn)) { sls.getPartModifier().resetPartValues(part.getType()); }
	}
	
	@Override
	public void keyPressed(char typedChar, int keyCode) {
		sls.currentPart = null;
		super.keyPressed(typedChar, keyCode);
	}
	
	@Override
	public void mousePressed(int mouseX, int mouseY, int mouseButton) {
		if (mouseX >= midX - 91 && mouseX <= midX) {
			if (mouseY >= midY - 94 && mouseY <= midY - 82) {
				part.toggleSwitching();
				if (!part.isSwitching() && !part.isBlinking()) { part.setNoMode(); }
				resetBtn.playPressSound();
			}
			if (mouseY >= midY - 65 && mouseY <= midY - 54) {
				part.toggleBlinking();
				if (!part.isSwitching() && !part.isBlinking()) { part.setNoMode(); }
				resetBtn.playPressSound();
			}
			if (mouseY >= midY - 34 && mouseY <= midY - 22) {
				if (ssIsSelectable) {
					SSpeedSel = !SSpeedSel;
					resetBtn.playPressSound();
				}
			}
			if (mouseY >= midY - 6 && mouseY <= midY + 6) {
				if (bdelayIsSelectable) {
					BDelaySel = !BDelaySel;
					resetBtn.playPressSound();
				}
			}
			if (mouseY >= midY + 22 && mouseY <= midY + 34) {
				if (bdurationIsSelectable) {
					BDurationSel = !BDurationSel;
					resetBtn.playPressSound();
				}
			}
			if (mouseY >= midY + 50 && mouseY <= midY + 62) {
				OffsetSel = !OffsetSel;
				resetBtn.playPressSound();
			}
			if (mouseY >= midY + 108 && mouseY <= midY + 120) {
				sls.getPartModifier().flipPartState(part.getType());
				resetBtn.playPressSound();
			}
		}
		if (mouseY >= midY - 127 && mouseY <= midY - 105) {
			if (mouseX >= midX + 48 && mouseX <= midX + 98) {
				mcSettings.setModelPartEnabled(part.getType().getMCType(), isEnumEnabled = !isEnumEnabled);
				resetBtn.playPressSound();
			}
		}		
		super.mousePressed(mouseX, mouseY, mouseButton);
	}
	
	private void drawMouseEntered() {
		bindTexture(SLCResources.SLSpartGui);
		if (mY >= midY - 127 && mY <= midY - 105 && mX >= midX + 48 && mX <= midX + 98) {
			if (isEnumEnabled) { drawTexturedModalRect(midX + 54, midY - 122, 201, 151, 12, 13); }
			else { drawTexturedModalRect(midX + 81, midY - 122, 201, 151, 12, 13); }
		}
		if (mX >= midX - 91 && mX <= midX) {
			if (mY >= midY - 94 && mY <= midY - 82) drawTexturedModalRect(midX - 91, midY - 94, 223, 15, 12, 12);
			if (mY >= midY - 65 && mY <= midY - 53) drawTexturedModalRect(midX - 91, midY - 65, 223, 15, 12, 12);
			if (mY >= midY - 34 && mY <= midY - 22) drawTexturedModalRect(midX - 91, midY - 34, 223, 15, 12, 12);
			if (mY >= midY - 6 && mY <= midY + 6) drawTexturedModalRect(midX - 91, midY - 6, 223, 15, 12, 12);
			if (mY >= midY + 22 && mY <= midY + 34) drawTexturedModalRect(midX - 91, midY + 22, 223, 15, 12, 12);
			if (mY >= midY + 50 && mY <= midY + 62) drawTexturedModalRect(midX - 91, midY + 50, 223, 15, 12, 12);
			if (mY >= midY + 108 && mY <= midY + 120) drawTexturedModalRect(midX - 91, midY + 108, 223, 15, 12, 12);
		}
	}
	
	private void addToValues(int value) {
		int changeSwitch = part.getSwitchSpeed() + value;
		int changeBlink = part.getBlinkSpeed() + value;
		int changeBlinkDur = part.getBlinkDuration() + value;
		int changeOffset = part.getOffset() + value;
		
		if (part.isSwitching()) {
			if (SSpeedSel) {
				if (changeSwitch < sls.MaxRate) {
					sls.getPartModifier().setPartSwitchSpeed(part.getType(), changeSwitch >= sls.MinRate ? changeSwitch : sls.MinRate);
				}
				else { sls.getPartModifier().setPartSwitchSpeed(part.getType(), sls.MaxRate); }
			}
		} else if (part.isBlinking()) {
			if (BDelaySel) {
				if (changeBlink < sls.MaxRate) {
					sls.getPartModifier().setPartBlinkSpeed(part.getType(), changeBlink >= sls.MinRate ? changeBlink : sls.MinRate);
				}
				else { sls.getPartModifier().setPartBlinkSpeed(part.getType(), sls.MaxRate); }
			}
			if (BDurationSel) {
				if (changeBlinkDur < sls.MaxRate) {
					sls.getPartModifier().setPartBlinkDuration(part.getType(), changeBlinkDur >= sls.MinBlinkDurRate ? changeBlinkDur : sls.MinBlinkDurRate);
				}
				else { sls.getPartModifier().setPartBlinkDuration(part.getType(), sls.MaxRate); }
			}
		}		
		if (OffsetSel) {
			if (changeOffset < sls.MaxRate) {
				sls.getPartModifier().setPartOffset(part.getType(), changeOffset >= 0 ? changeOffset : 0);
			}
			else { sls.getPartModifier().setPartOffset(part.getType(), sls.MaxRate); }
		}
	}
	
	private void drawCheckedBoxes() {
		bindTexture(SLCResources.SLSpartGui);
		if (part.isSwitching()) { drawTexturedModalRect(midX - 91, midY - 97, 201, 0, 14, 14); }
		if (part.isBlinking()) { drawTexturedModalRect(midX - 91, midY - 68, 201, 0, 14, 14); }
		if (part.getStateFlipped()) { drawTexturedModalRect(midX - 91, midY + 105, 201, 0, 14, 14); }
	}
	
	private void drawSelectedBoxes() {
		bindTexture(SLCResources.SLSpartGui);
		if (OffsetSel) { drawTexturedModalRect(midX - 89, midY + 52, 214, 15, 8, 8); }
		if (part.isSwitching()) {
			ssIsSelectable = true;
			bdelayIsSelectable = false;
			bdurationIsSelectable = false;
			drawTexturedModalRect(midX - 91, midY - 6, 201, 15, 12, 12);
			drawTexturedModalRect(midX - 91, midY + 22, 201, 15, 12, 12);			
			if (SSpeedSel) { drawTexturedModalRect(midX - 89, midY - 32, 214, 15, 8, 8); }
		}
		else if (part.isBlinking()) {
			ssIsSelectable = false;
			bdelayIsSelectable = true;
			bdurationIsSelectable = true;
			drawTexturedModalRect(midX - 91, midY - 34, 201, 15, 12, 12);
			if (BDelaySel) { drawTexturedModalRect(midX - 89, midY - 4, 214, 15, 8, 8); }
			if (BDurationSel) { drawTexturedModalRect(midX - 89, midY + 24, 214, 15, 8, 8); }
		}
		else {
			ssIsSelectable = false;
			bdelayIsSelectable = false;
			bdurationIsSelectable = false;
			drawTexturedModalRect(midX - 91, midY - 6, 201, 15, 12, 12);
			drawTexturedModalRect(midX - 91, midY + 22, 201, 15, 12, 12);
			drawTexturedModalRect(midX - 91, midY - 34, 201, 15, 12, 12);
		}
	}
	
	private void drawPartStats() {
		drawString("" + part.getSwitchSpeed() + " ms", midX + 50, midY - 31, 0xFFFFFF);
		drawString("" + part.getBlinkSpeed() + " ms", midX + 50, midY - 3, 0xFFFFFF);
		drawString("" + part.getBlinkDuration() + " ms", midX + 50, midY + 25, 0xFFFFFF);
		drawString("" + part.getOffset() + " ms", midX + 50, midY + 53, 0xFFFFFF);
	}
	
	private void drawPartTitle() {
		bindTexture(SLCResources.SLSpartGui);
		switch (part.getType()) {
		case H: 
			drawTexturedModalRect(midX - 35, midY - 121, 201, 87, 22, 14);
			break;
		case J:
			drawTexturedModalRect(midX - 45, midY - 121, 201, 117, 44, 14);
			break;
		case CA:
			drawTexturedModalRect(midX - 39, midY - 121, 201, 102, 31, 14);
			break;
		case LA:
			drawTexturedModalRect(midX - 67, midY - 121, 201, 28, 30, 14);
			drawTexturedModalRect(midX - 29, midY - 121, 201, 72, 47, 14);
			break;
		case RA:
			drawTexturedModalRect(midX - 69, midY - 121, 201, 43, 35, 14);
			drawTexturedModalRect(midX - 26, midY - 121, 201, 72, 47, 14);
			break;
		case LL:
			drawTexturedModalRect(midX - 55, midY - 121, 201, 28, 30, 14);
			drawTexturedModalRect(midX - 18, midY - 120, 201, 58, 22, 13);
			break;
		case RL:
			drawTexturedModalRect(midX - 58, midY - 121, 201, 43, 35, 14);
			drawTexturedModalRect(midX - 16, midY - 120, 201, 58, 22, 13);
			break;
		}
	}
	
	private void drawPartVisual() {
		bindTexture(playerSkin);
		boolean isAlex = skinType.equals("slim");
		Set parts = mc.gameSettings.getModelParts();
		switch (part.getType()) {
		case H:
			if (sls.skinFrontFacing) {
				drawTexturedModalRect(midX + 51, midY - 89, 32, 32, 32, 32); 
				if (parts.contains(LayerTypes.H.getMCType())) { drawTexturedModalRect(midX + 51, midY - 89, 160, 32, 32, 32); }
			}
			else {
				drawTexturedModalRect(midX + 51, midY - 89, 96, 32, 32, 32);
				if (parts.contains(LayerTypes.H.getMCType())) { drawTexturedModalRect(midX + 51, midY - 89, 224, 32, 32, 32); }
			}			
			break;
		case J:
			if (sls.skinFrontFacing) {
				drawTexturedModalRect(midX + 51, midY - 98, 80, 80, 32, 48);
				if (parts.contains(LayerTypes.J.getMCType())) { drawTexturedModalRect(midX + 51, midY - 98, 80, 144, 32, 48); }
			}
			else {
				drawTexturedModalRect(midX + 51, midY - 98, 128, 80, 32, 48);
				if (parts.contains(LayerTypes.J.getMCType())) {	drawTexturedModalRect(midX + 51, midY - 98, 128, 144, 32, 48); }
			}
			break;
		case CA:
			if (playerCape != null) {
				bindTexture(playerCape);
				drawModalRectWithCustomSizedTexture(midX + 51, midY - 98, 0, 0, 128, 128, 128, 128);
			}
			break;
		case RA:
			if (isAlex) {
				if (sls.skinFrontFacing) {
					drawTexturedModalRect(midX + 62, midY - 98, 176, 80, 12, 48);
					if (parts.contains(LayerTypes.RA.getMCType())) { drawTexturedModalRect(midX + 62, midY - 98, 176, 144, 12, 48); }
				}
				else {
					drawTexturedModalRect(midX + 62, midY - 98, 204, 80, 12, 48);
					if (parts.contains(LayerTypes.RA.getMCType())) { drawTexturedModalRect(midX + 62, midY - 98, 204, 144, 12, 48); }
				}
			} else {
				if (sls.skinFrontFacing) {
					drawTexturedModalRect(midX + 59, midY - 98, 176, 80, 16, 48);
					if (parts.contains(LayerTypes.RA.getMCType())) { drawTexturedModalRect(midX + 59, midY - 98, 176, 144, 16, 48); }
				}
				else {
					drawTexturedModalRect(midX + 59, midY - 98, 208, 80, 16, 48);
					if (parts.contains(LayerTypes.RA.getMCType())) { drawTexturedModalRect(midX + 59, midY - 98, 208, 144, 16, 48); }
				}
			}
			break;
		case LA:
			if (isAlex) {
				if (sls.skinFrontFacing) {
					drawTexturedModalRect(midX + 62, midY - 98, 144, 208, 12, 48);
					if (parts.contains(LayerTypes.LA.getMCType())) { drawTexturedModalRect(midX + 62, midY - 98, 208, 208, 12, 48); }
				}
				else {
					drawTexturedModalRect(midX + 62, midY - 98, 172, 208, 12, 48);
					if (parts.contains(LayerTypes.LA.getMCType())) { drawTexturedModalRect(midX + 62, midY - 98, 236, 208, 12, 48); }
				}
			} else {
				if (sls.skinFrontFacing) {
					drawTexturedModalRect(midX + 59, midY - 98, 144, 208, 16, 48);
					if (parts.contains(LayerTypes.LA.getMCType())) { drawTexturedModalRect(midX + 59, midY - 98, 208, 208, 16, 48); }
				}
				else {
					drawTexturedModalRect(midX + 59, midY - 98, 176, 208, 16, 48);
					if (parts.contains(LayerTypes.LA.getMCType())) { drawTexturedModalRect(midX + 59, midY - 98, 240, 208, 16, 48); }
				}
			}
			break;
		case LL:
			if (sls.skinFrontFacing) {
				drawTexturedModalRect(midX + 59, midY - 98, 80, 208, 16, 48);
				if (parts.contains(LayerTypes.LL.getMCType())) { drawTexturedModalRect(midX + 59, midY - 98, 16, 208, 16, 48); }
			}
			else {
				drawTexturedModalRect(midX + 59, midY - 98, 112, 208, 16, 48);
				if (parts.contains(LayerTypes.LL.getMCType())) { drawTexturedModalRect(midX + 59, midY - 98, 48, 208, 16, 48); }
			}
			break;
		case RL:
			if (sls.skinFrontFacing) {
				drawTexturedModalRect(midX + 59, midY - 98, 16, 80, 16, 48);
				if (parts.contains(LayerTypes.RL.getMCType())) { drawTexturedModalRect(midX + 59, midY - 98, 16, 144, 16, 48); }
			}
			else {
				drawTexturedModalRect(midX + 59, midY - 98, 48, 80, 16, 48);
				if (parts.contains(LayerTypes.RL.getMCType())) { drawTexturedModalRect(midX + 59, midY - 98, 48, 144, 16, 48); }
			}
			break;
		default: break;
		}
	}
	
	private void drawPartEnabled() {
		bindTexture(SLCResources.SLSpartGui);
		if (!isEnumEnabled) { drawTexturedModalRect(midX + 51, midY - 124, 201, 132, 45, 18); }
	}
	
	private void returnToMainGui() {
    	sls.currentPart = null;
    	fileUpAndClose();
	}
	
}