package com.Whodundid.slc.gui;

import com.Whodundid.core.app.AppType;
import com.Whodundid.core.app.RegisteredApps;
import com.Whodundid.core.enhancedGui.guiObjects.actionObjects.EGuiButton;
import com.Whodundid.core.enhancedGui.types.WindowParent;
import com.Whodundid.core.enhancedGui.types.interfaces.IEnhancedActionObject;
import com.Whodundid.slc.SLCApp;
import com.Whodundid.slc.SLCResources;
import com.Whodundid.slc.config.SLCSaveSkinConfigProfile;
import com.Whodundid.slc.util.PartModes;

public class SLCGlobalOptionsGui extends WindowParent {

	SLCApp slc = (SLCApp) RegisteredApps.getApp(AppType.SLC);
	EGuiButton intervalBtn, loadProfileBtn, defaultProfileResetBtn, defaultFacingBtn, saveStateBtn, resetBtn, doneBtn;
	String isFront, resetMode, saveStates;
	int currentProfile;
	
	public SLCGlobalOptionsGui() { super(); }
	public SLCGlobalOptionsGui(Object oldGuiIn) { super(oldGuiIn); }
	public SLCGlobalOptionsGui(int posX, int posY) { super(posX, posY); }
	public SLCGlobalOptionsGui(int posX, int posY, Object oldGuiIn) { super(posX, posY, oldGuiIn); }
	
	@Override
	public void initGui() {
		centerObjectWithSize(200, 256);
		super.initGui();
		setObjectName("SLC Options");
	}
	
	@Override
	public void initObjects() {
		defaultHeader(this);
		
		currentProfile = slc.defaultLoadedProfile;
		isFront = (slc.skinFrontFacing) ? "Front" : "Back";
		saveStates = (slc.savePartStates) ? "True" : "False";
		switch (slc.resetMode) {
		case SW: resetMode = "Switch"; break;
		case BL: resetMode = "Blink"; break;
		case IN: resetMode = "Indv."; break;
		default: resetMode = "Off"; break;
		}
		
		doneBtn = new EGuiButton(this, midX + 51, midY + 104, 45, 20, "Done");
		resetBtn = new EGuiButton(this, midX - 1, midY + 104, 45, 20, "Reset");
		intervalBtn = new EGuiButton(this, midX + 53, midY - 90, 40, 20, "" + slc.currentChangeValue);
		loadProfileBtn = new EGuiButton(this, midX + 53, midY - 59, 40, 20, "" + slc.defaultLoadedProfile);
		defaultProfileResetBtn = new EGuiButton(this, midX + 53, midY - 28, 40, 20, resetMode);
		defaultFacingBtn = new EGuiButton(this, midX + 53, midY + 3, 40, 20, isFront);
		saveStateBtn = new EGuiButton(this, midX + 53, midY + 67, 40, 20, saveStates);
		
		addObject(doneBtn, resetBtn, intervalBtn, loadProfileBtn, defaultProfileResetBtn, defaultFacingBtn, saveStateBtn);
	}
	
	@Override
	public void drawObject(int mXIn, int mYIn) {
		mc.renderEngine.bindTexture(SLCResources.SLSguiTexture);
		drawTexturedModalRect(midX - width / 2, midY - height / 2, 0, 0, width, height);
		super.drawObject(mXIn, mYIn);
	}
	
	@Override
	public void actionPerformed(IEnhancedActionObject object, Object... args) {
		if (object.equals(doneBtn)) { returnToMainGui(); }
		if (object.equals(intervalBtn)) { changeIntervalValue(); }
		if (object.equals(loadProfileBtn)) { changeProfile(); }
		if (object.equals(defaultFacingBtn)) { changeFacing(); }
		if (object.equals(defaultProfileResetBtn)) {
			switch (slc.resetMode) {
			case SW: slc.resetMode = PartModes.BL; defaultProfileResetBtn.setDisplayString("Blink"); break;
			case BL: slc.resetMode = PartModes.IN; defaultProfileResetBtn.setDisplayString("Indv."); break;
			case IN: slc.resetMode = PartModes.N; defaultProfileResetBtn.setDisplayString("Off"); break;
			default: slc.resetMode = PartModes.SW; defaultProfileResetBtn.setDisplayString("Switch"); break;
			}
			slc.getConfig().saveMainConfig();
		}
		if (object.equals(saveStateBtn)) {
			slc.savePartStates = !slc.savePartStates;
			saveStateBtn.setDisplayString((slc.savePartStates) ? "True" : "False");
			SLCSaveSkinConfigProfile.updateProfile(slc, slc.currentLoadedProfile);
		}
		if (object.equals(resetBtn)) {
			slc.resetMode = PartModes.N; defaultProfileResetBtn.setDisplayString("Indv.");
			slc.currentChangeValue = 100; intervalBtn.setDisplayString("" + slc.currentChangeValue);
			currentProfile = 1; slc.defaultLoadedProfile = 1; loadProfileBtn.setDisplayString("" + currentProfile);
			defaultFacingBtn.setDisplayString("Front"); isFront = "Front"; slc.rotateSkin(isFront);
			saveStateBtn.setDisplayString("True"); slc.savePartStates = true;
			slc.getConfig().saveMainConfig();
			SLCSaveSkinConfigProfile.updateProfile(slc, slc.currentLoadedProfile);
		}
	}
	
	private void changeIntervalValue() {
		int currentInterval = slc.currentChangeValue;
		switch (currentInterval) {
		case 25: slc.currentChangeValue = 50; break;
		case 50: slc.currentChangeValue = 100; break;
		case 100: slc.currentChangeValue = 250; break;
		case 250: slc.currentChangeValue = 500; break;
		case 500: slc.currentChangeValue = 1000; break;
		case 1000: slc.currentChangeValue = 25; break;
		default: slc.currentChangeValue = 25; break;
		}
		intervalBtn.setDisplayString("" + slc.currentChangeValue);
		slc.getConfig().saveMainConfig();
	}
	
	private void changeProfile() {
		if (currentProfile == 4) { currentProfile = 1; }
		else if (currentProfile < 4) { currentProfile += 1; }
		loadProfileBtn.setDisplayString("" + currentProfile);
		slc.defaultLoadedProfile = currentProfile;
		slc.getConfig().saveMainConfig();
	}
	
	private void changeFacing() {
		isFront = isFront.equals("Front") ? "Back" : "Front";
		defaultFacingBtn.setDisplayString(isFront);
		slc.rotateSkin(isFront);
		slc.getConfig().saveMainConfig();
	}
	
	private void returnToMainGui() {
    	slc.currentPart = null;
    	fileUpAndClose();
	}
}