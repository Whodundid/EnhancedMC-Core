package com.Whodundid.slc.window;

import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;
import com.Whodundid.core.EnhancedMC;
import com.Whodundid.core.app.AppType;
import com.Whodundid.core.app.RegisteredApps;
import com.Whodundid.core.util.chatUtil.ChatBuilder;
import com.Whodundid.core.windowLibrary.windowObjects.actionObjects.WindowButton;
import com.Whodundid.core.windowLibrary.windowTypes.WindowParent;
import com.Whodundid.core.windowLibrary.windowTypes.interfaces.IActionObject;
import com.Whodundid.slc.SLCApp;
import com.Whodundid.slc.config.SLCLoadSkinConfigProfile;
import com.Whodundid.slc.config.SLCSaveSkinConfigProfile;
import com.Whodundid.slc.util.GlobalModes;
import com.Whodundid.slc.util.LayerTypes;
import com.Whodundid.slc.util.SLCResources;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.ResourceLocation;

public class SLCMainWindow extends WindowParent {
	
	SLCApp slc = (SLCApp) RegisteredApps.getApp(AppType.SLC);
	ResourceLocation playerSkin;
	String skinType;
	WindowButton headBtn, jackBtn, larmBtn, rarmBtn, llegBtn, rlegBtn, capeBtn;
	WindowButton optionsBtn, stateBtn, valueUpBtn, valueDownBtn, flipBtn;
	WindowButton resetProfileBtn, loadProfileBtn;
	String headState, chestState, larmState, rarmState, llegState, rlegState, capeState;
	String hRate, cRate, caRate, laRate, raRate, llRate, rlRate, state;
	int gss, gbs, gbd;
	int hSpeed, jSpeed, caSpeed, laSpeed, raSpeed, llSpeed, rlSpeed;
	int hBSpeed, jBSpeed, caBSpeed, laBSpeed, raBSpeed, llBSpeed, rlBSpeed;
	int hBD, jBD, caBD, laBD, raBD, llBD, rlBD;
	boolean oneClick, alreadyPressed;
	
	public SLCMainWindow() {
		super();
		aliases.add("slcmain");
		windowIcon = SLCResources.icon;
	}
	
	@Override
	public void initWindow() {
		setObjectName("SLC Menu");
		centerObjectWithSize(200, 256);
		playerSkin = mc.thePlayer.getLocationSkin();
		skinType = mc.thePlayer.getSkinType();
	}
	
	@Override
	public void initObjects() {
		defaultHeader(this);
		
		if (slc.getGlobalSwitchingStatus()) state = "All Switch";
		else if (slc.getGlobalBlinkingStatus()) state = "All Blink";
		else if (slc.globalOn) { state = "Individual"; }
		else { state = "Off"; }
		
		larmBtn = new WindowButton(this, midX + 47, midY - 35, 46, 20, slc.skinFrontFacing ? "L Arm" : "R Arm");
		rarmBtn = new WindowButton(this, midX - 93, midY - 35, 46, 20, slc.skinFrontFacing ? "R Arm" : "L Arm");
		llegBtn = new WindowButton(this, midX + 47, midY + 16, 46, 20, slc.skinFrontFacing ? "R Leg" : "L Leg");
		rlegBtn = new WindowButton(this, midX - 93, midY + 16, 46, 20, slc.skinFrontFacing ? "L Leg" : "R Leg");
		headBtn = new WindowButton(this, midX - 23, midY - 121, 46, 20, "Hat");
		jackBtn = new WindowButton(this, midX - 93, midY - 86, 46, 20, "Jacket");
		capeBtn = new WindowButton(this, midX + 47, midY - 86, 46, 20, "Cape");
		
		optionsBtn = new WindowButton(this, midX + 41, midY + 76, 55, 20, "Options");
		flipBtn = new WindowButton(this, midX - 22, midY + 76, 55, 20, "Flip All");
		stateBtn = new WindowButton(this, midX - 95, midY + 76, 65, 20, state);
		valueUpBtn = new WindowButton(this, midX - 95, midY - 123, 58, 20, "All + " + slc.currentChangeValue);
		valueDownBtn = new WindowButton(this, midX + 37, midY - 123, 58, 20, "All - " + slc.currentChangeValue);
		resetProfileBtn = new WindowButton(this, midX - 1, midY + 104, 45, 20, "Reset");
		loadProfileBtn = new WindowButton(this, midX + 51, midY + 104, 45, 20, "Load");
		
		addObject(larmBtn, rarmBtn, llegBtn, rlegBtn, headBtn, jackBtn, capeBtn);
		addObject(optionsBtn, flipBtn, stateBtn, valueUpBtn, valueDownBtn, resetProfileBtn, loadProfileBtn);
	}
	
	@Override
	public void drawObject(int mXIn, int mYIn) {
		bindTexture(skinType.equals("slim") ? SLCResources.SLSsettingsGuiAlex : SLCResources.SLSsettingsGuiSteve);
		drawTexturedModalRect(midX - width / 2, midY - height / 2, 0, 0, width, height);
		GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
		drawCenteredStringWithShadow(slc.getDefaultSkinFacing(), midX + 70, endY - 69, 0xffcc00);
		GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
		drawMouseEnteredBtn();
		drawSelectedProfile();
		drawLoadedProfile();
		drawSkin();
		drawDisabledParts();
		drawRates();
		super.drawObject(mXIn, mYIn);
	}
	
	@Override
	public void actionPerformed(IActionObject object, Object... args) {
		if (object.equals(optionsBtn)) { openOptionsGui(); }
		if (object.equals(stateBtn)) {
			if (slc.currentMode == GlobalModes.SW) slc.currentMode = GlobalModes.BL;
			else if (slc.currentMode == GlobalModes.BL) slc.currentMode = GlobalModes.IN;
			else if (slc.currentMode == GlobalModes.IN) slc.currentMode = GlobalModes.OFF;
			else if (slc.currentMode == GlobalModes.OFF) slc.currentMode = GlobalModes.SW;
			changeStateBtn();
		}
		if (object.equals(valueUpBtn)) {
			changePartRateValues(slc.currentChangeValue);
			if (slc.getGlobalSwitchingStatus()) {
				slc.currentMode = GlobalModes.SW;
				changeStateBtn();
			}
			else if (slc.getGlobalBlinkingStatus()) {
				slc.currentMode = GlobalModes.BL;
				changeStateBtn();
			}
		}
		if (object.equals(valueDownBtn)) {
			changePartRateValues(-slc.currentChangeValue);
			if (slc.getGlobalSwitchingStatus()) {
				slc.currentMode = GlobalModes.SW;
				changeStateBtn();
			}
			else if (slc.getGlobalBlinkingStatus()) {
				slc.currentMode = GlobalModes.BL;
				changeStateBtn();
			}
		}
		if (object.equals(headBtn)) { slc.currentPart = LayerTypes.H; openPartGUI(); }
		if (object.equals(jackBtn)) { slc.currentPart = LayerTypes.J; openPartGUI(); }
		if (object.equals(capeBtn)) { slc.currentPart = LayerTypes.CA; openPartGUI(); }
		if (object.equals(larmBtn)) { slc.currentPart = slc.skinFrontFacing ? LayerTypes.LA : LayerTypes.RA; openPartGUI(); }
		if (object.equals(rarmBtn)) { slc.currentPart = slc.skinFrontFacing ? LayerTypes.RA : LayerTypes.LA; openPartGUI(); }
		if (object.equals(llegBtn)) { slc.currentPart = slc.skinFrontFacing ? LayerTypes.LL : LayerTypes.RL; openPartGUI(); }
		if (object.equals(rlegBtn)) { slc.currentPart = slc.skinFrontFacing ? LayerTypes.RL : LayerTypes.LL; openPartGUI(); }
		if (object.equals(flipBtn)) { delayFlipBtnInputs(); }
		if (object.equals(resetProfileBtn)) {
			if (slc.currentLoadedProfile == slc.currentSkinProfile) {
				slc.getPartModifier().resetAllParts();
				switch (slc.resetMode) {
				case SW: slc.setGlobalSwitch(true); stateBtn.setString("All Switch"); break;
				case BL: slc.setGlobalBlink(true); stateBtn.setString("All Blink"); break;
				case IN: slc.setGlobalSwitch(false); slc.setGlobalBlink(false); stateBtn.setString("Individual"); break;
				case N: slc.setGlobalSwitch(false); slc.setGlobalBlink(false); stateBtn.setString("Off"); break;
				}
			}
			else { SLCLoadSkinConfigProfile.createNewConfig(slc, String.valueOf(slc.currentSkinProfile)); }		
			mc.thePlayer.addChatMessage(ChatBuilder.of("Reset skin profile: " + slc.currentSkinProfile).setColor(EnumChatFormatting.AQUA).build());
		}
		if (object.equals(loadProfileBtn)) { tryToLoadProfile(); }
	}
	
	private void changeStateBtn() {
		switch (slc.currentMode) {
		case SW: slc.globalOn = true; slc.setGlobalSwitch(true); stateBtn.setString("All Switch");  break;
		case BL: slc.setGlobalBlink(true); stateBtn.setString("All Blink"); break;
		case IN: slc.setGlobalSwitch(false); slc.setGlobalBlink(false); stateBtn.setString("Individual"); break;
		case OFF: slc.globalOn = false; stateBtn.setString("Off"); break;
		}
		slc.getConfig().saveMainConfig();
		SLCSaveSkinConfigProfile.updateProfile(slc, slc.currentLoadedProfile);
	}
	
	private void drawMouseEnteredBtn() {
		bindTexture(SLCResources.SLSsettingsGuiSteve);
		//rotate button
		if (mY >= midY + 57 && mY <= midY + 67 && mX >= midX + 24 && mX <= midX + 43) { drawTexturedModalRect(midX + 25, midY + 58, 201, 55, 18, 9); }
		//profile buttons
		if (mY >= midY + 104 && mY <= midY + 124) {
			if (mX >= midX - 96 && mX <= midX - 77) { drawTexturedModalRect(midX - 95, midY + 105, 201, 65, 18, 18); }
			if (mX >= midX - 74 && mX <= midX - 55) { drawTexturedModalRect(midX - 73, midY + 105, 201, 84, 18, 18); }
			if (mX >= midX - 52 && mX <= midX - 33) { drawTexturedModalRect(midX - 51, midY + 105, 201, 103, 18, 18); }
			if (mX >= midX - 30 && mX <= midX - 11) { drawTexturedModalRect(midX - 29, midY + 105, 201, 122, 18, 18); }
		}
	}
	
	@Override
	public void mousePressed(int mouseX, int mouseY, int mouseButton) {
		//Profile buttons
		if (mouseY >= (midY + 104) && mouseY <= (midY + 124)) {
			if (mouseX >= (midX - 96) && mouseX <= (midX - 77)) { createDoubleClickTimer(200, 1); }
			if (mouseX >= (midX - 74) && mouseX <= (midX - 55)) { createDoubleClickTimer(200, 2); }
			if (mouseX >= (midX - 52) && mouseX <= (midX - 33)) { createDoubleClickTimer(200, 3); }
			if (mouseX >= (midX - 30) && mouseX <= (midX - 11)) { createDoubleClickTimer(200, 4); }
		}
		//Skin buttons
		if (mouseY >= (midY - 71) && mouseY <= (midY - 40)) {
			if (mouseX >= (midX - 17) && mouseX <= (midX + 16)) {
				slc.getPartModifier().setPartEnabled(LayerTypes.H, !slc.getPart(LayerTypes.H).isEnabled());
				resetProfileBtn.playPressSound();
			}
		}
		if (mouseY >= (midY - 38) && mouseY <= (midY + 10)) {
			if (skinType.equals("slim")) {
				if (mouseX >= (midX + 18) && mouseX <= (midX + 29)) {
					slc.getPartModifier().setPartEnabled(slc.skinFrontFacing ? LayerTypes.LA : LayerTypes.RA, !slc.getPart(slc.skinFrontFacing ? LayerTypes.LA : LayerTypes.RA).isEnabled());
					resetProfileBtn.playPressSound();
				}
			}
			else {
				if (mouseX >= (midX + 18) && mouseX <= (midX + 34)) {
					slc.getPartModifier().setPartEnabled(slc.skinFrontFacing ? LayerTypes.LA : LayerTypes.RA, !slc.getPart(slc.skinFrontFacing ? LayerTypes.LA : LayerTypes.RA).isEnabled());
					resetProfileBtn.playPressSound();
				}
			}		
			if (mouseX >= (midX - 16) && mouseX <= (midX + 15)) {
				slc.getPartModifier().setPartEnabled(LayerTypes.J, !slc.getPart(LayerTypes.J).isEnabled());
				resetProfileBtn.playPressSound();
			}
			if (skinType.equals("slim")) {
				if (mouseX >= (midX - 30) && mouseX <= (midX - 19)) {
					slc.getPartModifier().setPartEnabled(slc.skinFrontFacing ? LayerTypes.RA : LayerTypes.LA, !slc.getPart(slc.skinFrontFacing ? LayerTypes.RA : LayerTypes.LA).isEnabled());
					resetProfileBtn.playPressSound();
				}
			}
			else {
				if (mouseX >= (midX - 34) && mouseX <= (midX - 19)) {
					slc.getPartModifier().setPartEnabled(slc.skinFrontFacing ? LayerTypes.RA : LayerTypes.LA, !slc.getPart(slc.skinFrontFacing ? LayerTypes.RA : LayerTypes.LA).isEnabled());
					resetProfileBtn.playPressSound();
				}
			}
		}
		if (mouseY >= (midY + 12) && mouseY <= (midY + 60)) {
			if (mouseX >= (midX - 24) && mouseX <= (midX - 2)) {
				slc.getPartModifier().setPartEnabled(slc.skinFrontFacing ? LayerTypes.RL : LayerTypes.LL, !slc.getPart(slc.skinFrontFacing ? LayerTypes.RL : LayerTypes.LL).isEnabled());
				resetProfileBtn.playPressSound();
			}
			if (mouseX >= (midX + 2) && mouseX <= (midX + 16)) {
				slc.getPartModifier().setPartEnabled(slc.skinFrontFacing ? LayerTypes.LL : LayerTypes.RL, !slc.getPart(slc.skinFrontFacing ? LayerTypes.LL : LayerTypes.RL).isEnabled());
				resetProfileBtn.playPressSound();
			}
		}
		//Rotate Skin Button
		if (mouseY >= midY + 57 && mouseY <= midY + 67) {
			if (mouseX >= midX + 24 && mouseX <= midX + 43) {
				slc.skinFrontFacing = !slc.skinFrontFacing;
				if (slc.skinFrontFacing) {
					llegBtn.setString("L Leg");
					rlegBtn.setString("R Leg");
					larmBtn.setString("L Arm");
					rarmBtn.setString("R Arm");
				}
				else {
					llegBtn.setString("R Leg");
					rlegBtn.setString("L Leg");
					larmBtn.setString("R Arm");
					rarmBtn.setString("L Arm");
				}
				slc.getConfig().saveMainConfig();
				resetProfileBtn.playPressSound();
			}
		}
		super.mousePressed(mouseX, mouseY, mouseButton);
	}
	
	private void createDoubleClickTimer(int time, int btnPressed) {
		if (oneClick) {
			if (btnPressed <= 4) { tryToLoadProfile(); }
			resetProfileBtn.playPressSound();
	        oneClick = false;
		}
		else {
			oneClick = true;
			if (btnPressed <= 4) { slc.currentSkinProfile = btnPressed; }
			resetProfileBtn.playPressSound();
			Timer t = new Timer("doubleclickTimer", false);
			t.schedule(new TimerTask() { @Override public void run() { oneClick = false; } }, time);
		}
	}
	
	private void tryToLoadProfile() {
		if (slc.currentLoadedProfile != slc.currentSkinProfile) {
			slc.currentLoadedProfile = slc.currentSkinProfile;
			slc.loadSelectedProfile();
			mc.thePlayer.addChatMessage(ChatBuilder.of("Loading skin profile: " + slc.currentSkinProfile).setColor(EnumChatFormatting.GREEN).build());
			changeStateBtn();
		}
	}
	
	private void delayFlipBtnInputs() {
		if (!alreadyPressed) {
			slc.toggleGlobalBlinkFlipped();
			alreadyPressed = true;
			Timer t = new Timer("doubleclickTimer", false);
			t.schedule(new TimerTask() { @Override public void run() { alreadyPressed = false; } }, 1500);
		}
	}
	
	private void changePartRateValues(int changeRate) {
		if (slc.getGlobalSwitchingStatus()) { testValues(null, changeRate + slc.getGlobalSwitchingSpeed()); return; }
		else if (slc.getGlobalBlinkingStatus()) { testValues(null, changeRate + slc.getGlobalBlinkingSpeed()); return; }
		for (LayerTypes t : LayerTypes.values()) {
			testValues(t, changeRate + (slc.getPart(LayerTypes.H).isSwitching() ? slc.getPartStats().getPartSwitchSpeed(LayerTypes.H) : slc.getPartStats().getPartBlinkSpeed(LayerTypes.H)));
		}
	}
	
	private void testValues(LayerTypes type, int changeValue) {
		changeValue = changeValue > slc.MaxRate ? slc.MaxRate : changeValue;
		changeValue = changeValue < slc.MinRate ? slc.MinRate : changeValue;
		if (slc.getGlobalSwitchingStatus()) { slc.setGlobalSwitchSpeed(changeValue); }
		else if (slc.getGlobalBlinkingStatus()) { slc.setGlobalBlinkDelay(changeValue); }
		else {
			if (slc.getPart(type).isSwitching()) { slc.getPartModifier().setPartSwitchSpeed(type, changeValue); }
			else if (slc.getPart(type).isBlinking()) { slc.getPartModifier().setPartBlinkSpeed(type, changeValue); }
		}
	}
	
	private void drawRates() {
		gss = slc.getGlobalSwitchingSpeed();
		gbs = slc.getGlobalBlinkingSpeed();
		gbd = slc.getGlobalBlinkDuration();
		
		if (slc.getGlobalSwitchingStatus()) {
			hSpeed = gss; jSpeed = gss; caSpeed = gss; laSpeed = gss; raSpeed = gss; llSpeed = gss; rlSpeed = gss;
		}
		else if (slc.getGlobalBlinkingStatus()) {
			hBSpeed = gbs; jBSpeed = gbs; caBSpeed = gbs; laBSpeed = gbs; raBSpeed = gbs; llBSpeed = gbs; rlBSpeed = gbs;
			hBD = gbd; jBD = gbd; caBD = gbd; laBD = gbd; raBD = gbd; llBD = gbd; rlBD = gbd;
		}
		else if (slc.globalOn) {
			hSpeed = slc.getPart(LayerTypes.H).getSwitchSpeed();
			hBSpeed = slc.getPart(LayerTypes.H).getBlinkSpeed();
			hBD = slc.getPart(LayerTypes.H).getBlinkDuration();
			jSpeed = slc.getPart(LayerTypes.J).getSwitchSpeed();
			jBSpeed = slc.getPart(LayerTypes.J).getBlinkSpeed();
			jBD = slc.getPart(LayerTypes.J).getBlinkDuration();
			caSpeed = slc.getPart(LayerTypes.CA).getSwitchSpeed();
			caBSpeed = slc.getPart(LayerTypes.CA).getBlinkSpeed();
			caBD = slc.getPart(LayerTypes.CA).getBlinkDuration();
			laSpeed = slc.getPart(LayerTypes.LA).getSwitchSpeed();
			laBSpeed = slc.getPart(LayerTypes.LA).getBlinkSpeed();
			laBD = slc.getPart(LayerTypes.LA).getBlinkDuration();
			raSpeed = slc.getPart(LayerTypes.RA).getSwitchSpeed();
			raBSpeed = slc.getPart(LayerTypes.RA).getBlinkSpeed();
			raBD = slc.getPart(LayerTypes.RA).getBlinkDuration();
			llSpeed = slc.getPart(LayerTypes.LL).getSwitchSpeed();
			llBSpeed = slc.getPart(LayerTypes.LL).getBlinkSpeed();
			llBD = slc.getPart(LayerTypes.LL).getBlinkDuration();
			rlSpeed = slc.getPart(LayerTypes.RL).getSwitchSpeed();
			rlBSpeed = slc.getPart(LayerTypes.RL).getBlinkSpeed();
			rlBD = slc.getPart(LayerTypes.RL).getBlinkDuration();
		}
		if (slc.getGlobalSwitchingStatus()) {
			drawString("SS: " + hSpeed, midX - 19 + getOffset(hSpeed), midY - 98, 0xFFFFFF);
			drawString("SS: " + jSpeed, midX - 90 + getOffset(jSpeed), midY - 63, 0xFFFFFF);
			drawString("SS: " + caSpeed, midX + 51 + getOffset(caSpeed), midY - 63, 0xFFFFFF);
			drawString("SS: " + laSpeed, midX + 51 + getOffset(laSpeed), midY - 12, 0xFFFFFF);
			drawString("SS: " + raSpeed, midX - 90 + getOffset(raSpeed), midY - 12, 0xFFFFFF);
			drawString("SS: " + llSpeed, midX + 51 + getOffset(llSpeed), midY + 39, 0xFFFFFF);
			drawString("SS: " + rlSpeed, midX - 90 + getOffset(rlSpeed), midY + 39, 0xFFFFFF);
		}
		else if (slc.getGlobalBlinkingStatus()) {
			drawString("BS: " + hBSpeed, midX - 19 + getOffset(hBSpeed), midY - 98, 0xFFFFFF);
			drawString("BD: " + hBD, midX - 19 + getOffset(hBD), midY - 89, 0xFFFFFF);
			drawString("BS: " + jBSpeed, midX - 90 + getOffset(jBSpeed), midY - 63, 0xFFFFFF);
			drawString("BD: " + jBD, midX - 90 + getOffset(jBD), midY - 54, 0xFFFFFF);
			drawString("BS: " + caBSpeed, midX + 51 + getOffset(caBSpeed), midY - 63, 0xFFFFFF);
			drawString("BD: " + caBD, midX + 51 + getOffset(caBD), midY - 54, 0xFFFFFF);
			drawString("BS: " + laBSpeed, midX + 51 + getOffset(laBSpeed), midY - 12, 0xFFFFFF);
			drawString("BD: " + laBD, midX + 51 + getOffset(laBD), midY - 3, 0xFFFFFF);
			drawString("BS: " + raBSpeed, midX - 90 + getOffset(raBSpeed), midY - 12, 0xFFFFFF);
			drawString("BD: " + raBD, midX - 90 + getOffset(raBD), midY - 3, 0xFFFFFF);
			drawString("BS: " + llBSpeed, midX + 51 + getOffset(llBSpeed), midY + 39, 0xFFFFFF);
			drawString("BD: " + llBD, midX + 51 + getOffset(llBD), midY + 48, 0xFFFFFF);
			drawString("BS: " + rlBSpeed, midX - 90 + getOffset(rlBSpeed), midY + 39, 0xFFFFFF);
			drawString("BD: " + rlBD, midX - 90 + getOffset(rlBD), midY + 48, 0xFFFFFF);
		}
		else if (slc.globalOn) {
			if (slc.getPart(LayerTypes.H).isSwitching()) {
				drawString("SS: " + hSpeed, midX - 19 + getOffset(hSpeed), midY - 98, 0xFFFFFF);
			}
			else if (slc.getPart(LayerTypes.H).isBlinking()){
				drawString("BS: " + hBSpeed, midX - 19 + getOffset(hBSpeed), midY - 98, 0xFFFFFF);
				drawString("BD: " + hBD, midX - 19 + getOffset(hBD), midY - 89, 0xFFFFFF);
			}
			if (slc.getPart(LayerTypes.J).isSwitching()) {
				drawString("SS: " + jSpeed, midX - 90 + getOffset(jSpeed), midY - 63, 0xFFFFFF);
			}
			else if (slc.getPart(LayerTypes.J).isBlinking()) {
				drawString("BS: " + jBSpeed, midX - 90 + getOffset(jBSpeed), midY - 63, 0xFFFFFF);
				drawString("BD: " + jBD, midX - 90 + getOffset(jBD), midY - 54, 0xFFFFFF);
			}
			if (slc.getPart(LayerTypes.CA).isSwitching()) {
				drawString("SS: " + caSpeed, midX + 51 + getOffset(caSpeed), midY - 63, 0xFFFFFF);
			}
			else if (slc.getPart(LayerTypes.CA).isBlinking()) {
				drawString("BS: " + caBSpeed, midX + 51 + getOffset(caBSpeed), midY - 63, 0xFFFFFF);
				drawString("BD: " + caBD, midX + 51 + getOffset(caBD), midY - 54, 0xFFFFFF);
			}
			if (slc.getPart(LayerTypes.LA).isSwitching()) {
				drawString("SS: " + laSpeed, midX + (slc.skinFrontFacing ? 51 : -91) + getOffset(laSpeed), midY - 12, 0xFFFFFF);
			}
			else if (slc.getPart(LayerTypes.LA).isBlinking()) {
				if (slc.skinFrontFacing) {
					drawString("BS: " + laBSpeed, midX + 51 + getOffset(laBSpeed), midY - 12, 0xFFFFFF);
					drawString("BD: " + laBD, midX + 51 + getOffset(laBD), midY - 3, 0xFFFFFF);
				}
				else {
					drawString("BS: " + laBSpeed, midX - 90 + getOffset(laBSpeed), midY - 12, 0xFFFFFF);
					drawString("BD: " + laBD, midX - 90 + getOffset(laBD), midY - 3, 0xFFFFFF);
				}
			}
			if (slc.getPart(LayerTypes.RA).isSwitching()) {
				drawString("SS: " + raSpeed, midX + (slc.skinFrontFacing ? -90 : 51) + getOffset(raSpeed), midY - 12, 0xFFFFFF);
			}
			else if (slc.getPart(LayerTypes.RA).isBlinking()) {
				if (slc.skinFrontFacing) {
					drawString("BS: " + raBSpeed, midX - 90 + getOffset(raBSpeed), midY - 12, 0xFFFFFF);
					drawString("BD: " + raBD, midX - 90 + getOffset(raBD), midY - 3, 0xFFFFFF);
				}
				else {
					drawString("BS: " + raBSpeed, midX + 51 + getOffset(raBSpeed), midY - 12, 0xFFFFFF);
					drawString("BD: " + raBD, midX + 51 + getOffset(raBD), midY - 3, 0xFFFFFF);
				}
			}
			if (slc.getPart(LayerTypes.LL).isSwitching()) {
				drawString("SS: " + llSpeed, midX + (slc.skinFrontFacing ? 51 : -91) + getOffset(llSpeed), midY + 39, 0xFFFFFF);
			}
			else if (slc.getPart(LayerTypes.LL).isBlinking()) {
				if (slc.skinFrontFacing) {
					drawString("BS: " + llBSpeed, midX + 51 + getOffset(llBSpeed), midY + 39, 0xFFFFFF);
					drawString("BD: " + llBD, midX + 51 + getOffset(llBD), midY + 48, 0xFFFFFF);
				} else {
					drawString("BS: " + llBSpeed, midX - 90 + getOffset(llBSpeed), midY + 39, 0xFFFFFF);
					drawString("BD: " + llBD, midX - 90 + getOffset(llBD), midY + 48, 0xFFFFFF);
				}				
			}
			if (slc.getPart(LayerTypes.RL).isSwitching()) {
				drawString("SS: " + rlSpeed, midX + (slc.skinFrontFacing ? -90 : 51) + getOffset(rlSpeed), midY + 39, 0xFFFFFF);
			}
			else if (slc.getPart(LayerTypes.RL).isBlinking()) {
				if (slc.skinFrontFacing) {
					drawString("BS: " + rlBSpeed, midX - 90 + getOffset(rlBSpeed), midY + 39, 0xFFFFFF);
					drawString("BD: " + rlBD, midX - 90 + getOffset(rlBD), midY + 48, 0xFFFFFF);
				}
				else {
					drawString("BS: " + rlBSpeed, midX + 51 + getOffset(rlBSpeed), midY + 39, 0xFFFFFF);
					drawString("BD: " + rlBD, midX + 51 + getOffset(rlBD), midY + 48, 0xFFFFFF);
				}
			}
		}
	}
	
	private void drawDisabledParts() {
		bindTexture(SLCResources.SLSsettingsGuiSteve);
		if (!slc.getPart(LayerTypes.H).isEnabled()) { drawTexturedModalRect(midX - 7, midY - 62, 201, 40, 14, 14); }
		if (!slc.getPart(LayerTypes.J).isEnabled()) { drawTexturedModalRect(midX - 7, midY - 20, 201, 40, 14, 14); }
		if (!slc.getPart(LayerTypes.LA).isEnabled()) { drawTexturedModalRect(midX + (slc.skinFrontFacing ? 19 : -33), midY - 20, 201, 40, 14, 14); }
		if (!slc.getPart(LayerTypes.RA).isEnabled()) { drawTexturedModalRect(midX + (slc.skinFrontFacing ? -33 : 19), midY - 20, 201, 40, 14, 14); }
		if (!slc.getPart(LayerTypes.LL).isEnabled()) { drawTexturedModalRect(midX + (slc.skinFrontFacing ? 2 : -16), midY + 30, 201, 40, 14, 14); }
		if (!slc.getPart(LayerTypes.RL).isEnabled()) { drawTexturedModalRect(midX + (slc.skinFrontFacing ? -16 : 2), midY + 30, 201, 40, 14, 14); }
	}
	
	private void drawSelectedProfile() {
		bindTexture(SLCResources.SLSsettingsGuiSteve);
		drawTexturedModalRect(midX - 96 + ((slc.currentSkinProfile - 1) * 22), midY + 104, 201, 0, 20, 20);	
	}
	
	private void drawLoadedProfile() {
		bindTexture(SLCResources.SLSsettingsGuiSteve);
		drawTexturedModalRect(midX - 95 + ((slc.currentLoadedProfile - 1) * 22), midY + 105, 201, 21, 18, 18);
	}
	
	private void drawSkin() {
		bindTexture(playerSkin);
		boolean isAlex = skinType.equals("slim");
		Set parts = mc.gameSettings.getModelParts();
		if (slc.skinFrontFacing) {
			//Head Front
			drawTexturedModalRect(midX - 16, midY - 71, 32, 32, 32, 32);
			//Chest Front
			drawTexturedModalRect(midX - 16, midY - 37, 80, 80, 32, 48);
			//Left Leg Front
			drawTexturedModalRect(midX - 17, midY + 13, 16, 80, 16, 48);
			//Right Leg Front
			drawTexturedModalRect(midX + 1, midY + 13, 80, 208, 16, 48);
			if (parts.contains(LayerTypes.H.getMCType())) { drawTexturedModalRect(midX - 16, midY - 71, 160, 32, 32, 32); }
			if (parts.contains(LayerTypes.J.getMCType())) { drawTexturedModalRect(midX - 16, midY - 37, 80, 144, 32, 48); }
			if (parts.contains(LayerTypes.RL.getMCType())) { drawTexturedModalRect(midX - 17, midY + 13, 16, 144, 16, 48); }
			if (parts.contains(LayerTypes.LL.getMCType())) { drawTexturedModalRect(midX + 1, midY + 13, 16, 208, 16, 48); }
			if (isAlex) {
				//Left Arm Front
				drawTexturedModalRect(midX + 18, midY - 37, 144, 208, 12, 48);
				//Right Arm Front
				drawTexturedModalRect(midX - 30, midY - 37, 176, 80, 12, 48);
				if (parts.contains(LayerTypes.LA.getMCType())) { drawTexturedModalRect(midX + 18, midY - 37, 208, 208, 12, 48); }
				if (parts.contains(LayerTypes.RA.getMCType())) { drawTexturedModalRect(midX - 30, midY - 37, 176, 144, 12, 48); }
			}
			else { 
				//Left Arm Front
				drawTexturedModalRect(midX + 18, midY - 37, 144, 208, 16, 48);
				//Right Arm Front
				drawTexturedModalRect(midX - 34, midY - 37, 176, 80, 16, 48);
				if (parts.contains(LayerTypes.LA.getMCType())) { drawTexturedModalRect(midX + 18, midY - 37, 208, 208, 16, 48); }
				if (parts.contains(LayerTypes.RA.getMCType())) { drawTexturedModalRect(midX - 34, midY - 37, 176, 144, 16, 48); }
			}
		}
		else {
			//Head Back
			drawTexturedModalRect(midX - 16, midY - 71, 96, 32, 32, 32);
			//Chest Back
			drawTexturedModalRect(midX - 16, midY - 37, 128, 80, 32, 48);
			//Left Leg Back
			drawTexturedModalRect(midX - 17, midY + 13, 112, 208, 16, 48);
			//Right Leg Back
			drawTexturedModalRect(midX + 1, midY + 13, 48, 80, 16, 48);
			if (parts.contains(LayerTypes.H.getMCType())) {	drawTexturedModalRect(midX - 16, midY - 71, 224, 32, 32, 32); }
			if (parts.contains(LayerTypes.J.getMCType())) { drawTexturedModalRect(midX - 16, midY - 37, 128, 144, 32, 48); }
			if (parts.contains(LayerTypes.LL.getMCType())) { drawTexturedModalRect(midX - 17, midY + 13, 48, 208, 16, 48); }
			if (parts.contains(LayerTypes.RL.getMCType())) { drawTexturedModalRect(midX + 1, midY + 13, 48, 144, 16, 48); }
			if (isAlex) {
				//Left Arm Back
				drawTexturedModalRect(midX - 30, midY - 37, 172, 208, 12, 48);
				//Right Arm Back
				drawTexturedModalRect(midX + 18, midY - 37, 204, 80, 12, 48);
				if (parts.contains(LayerTypes.LA.getMCType())) { drawTexturedModalRect(midX - 30, midY - 37, 236, 208, 12, 48); }
				if (parts.contains(LayerTypes.RA.getMCType())) { drawTexturedModalRect(midX + 18, midY - 37, 204, 144, 12, 48);	}
			}
			else {
				//Left Arm Back
				drawTexturedModalRect(midX - 34, midY - 37, 176, 208, 16, 48);
				//Right Arm Back
				drawTexturedModalRect(midX + 18, midY - 37, 208, 80, 16, 48);
				if (parts.contains(LayerTypes.LA.getMCType())) { drawTexturedModalRect(midX - 34, midY - 37, 240, 208, 16, 48); }
				if (parts.contains(LayerTypes.RA.getMCType())) { drawTexturedModalRect(midX + 18, midY - 37, 208, 144, 16, 48); }
			}
		}
	}
	
	private int getOffset(int value) { return (11 - 3 * Integer.toString(value).length()); }
	private void openPartGUI() { EnhancedMC.displayWindow(new SLCPartWindow(), this); }
	private void openOptionsGui() { EnhancedMC.displayWindow(new SLCGlobalOptionsWindow(), this); }
	
}
