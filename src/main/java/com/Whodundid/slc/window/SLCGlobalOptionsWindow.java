package com.Whodundid.slc.window;

import com.Whodundid.core.app.AppType;
import com.Whodundid.core.app.RegisteredApps;
import com.Whodundid.core.windowLibrary.windowObjects.actionObjects.WindowButton;
import com.Whodundid.core.windowLibrary.windowTypes.WindowParent;
import com.Whodundid.core.windowLibrary.windowTypes.interfaces.IActionObject;
import com.Whodundid.slc.SLCApp;
import com.Whodundid.slc.config.SLCSaveSkinConfigProfile;
import com.Whodundid.slc.util.PartModes;
import com.Whodundid.slc.util.SLCResources;

public class SLCGlobalOptionsWindow extends WindowParent {

	SLCApp slc = (SLCApp) RegisteredApps.getApp(AppType.SLC);
	WindowButton intervalBtn, loadProfileBtn, defaultProfileResetBtn, defaultFacingBtn, saveStateBtn, resetBtn, doneBtn;
	String isFront, resetMode, saveStates;
	int currentProfile;
	
	public SLCGlobalOptionsWindow() {
		super();
		aliases.add("slcoptions", "skinoptions");
		windowIcon = SLCResources.icon;
	}
	
	@Override
	public void initWindow() {
		centerObjectWithSize(200, 256);
		super.initWindow();
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
		
		doneBtn = new WindowButton(this, midX + 51, midY + 104, 45, 20, "Done");
		resetBtn = new WindowButton(this, midX - 1, midY + 104, 45, 20, "Reset");
		intervalBtn = new WindowButton(this, midX + 53, midY - 90, 40, 20, "" + slc.currentChangeValue);
		loadProfileBtn = new WindowButton(this, midX + 53, midY - 59, 40, 20, "" + slc.defaultLoadedProfile);
		defaultProfileResetBtn = new WindowButton(this, midX + 53, midY - 28, 40, 20, resetMode);
		defaultFacingBtn = new WindowButton(this, midX + 53, midY + 3, 40, 20, isFront);
		saveStateBtn = new WindowButton(this, midX + 53, midY + 67, 40, 20, saveStates);
		
		addObject(doneBtn, resetBtn, intervalBtn, loadProfileBtn, defaultProfileResetBtn, defaultFacingBtn, saveStateBtn);
	}
	
	@Override
	public void drawObject(int mXIn, int mYIn) {
		bindTexture(SLCResources.SLSguiTexture);
		drawTexturedModalRect(midX - width / 2, midY - height / 2, 0, 0, width, height);
		super.drawObject(mXIn, mYIn);
	}
	
	@Override
	public void actionPerformed(IActionObject object, Object... args) {
		if (object.equals(doneBtn)) { returnToMainGui(); }
		if (object.equals(intervalBtn)) { changeIntervalValue(); }
		if (object.equals(loadProfileBtn)) { changeProfile(); }
		if (object.equals(defaultFacingBtn)) { changeFacing(); }
		if (object.equals(defaultProfileResetBtn)) {
			switch (slc.resetMode) {
			case SW: slc.resetMode = PartModes.BL; defaultProfileResetBtn.setString("Blink"); break;
			case BL: slc.resetMode = PartModes.IN; defaultProfileResetBtn.setString("Indv."); break;
			case IN: slc.resetMode = PartModes.N; defaultProfileResetBtn.setString("Off"); break;
			default: slc.resetMode = PartModes.SW; defaultProfileResetBtn.setString("Switch"); break;
			}
			slc.getConfig().saveMainConfig();
		}
		if (object.equals(saveStateBtn)) {
			slc.savePartStates = !slc.savePartStates;
			saveStateBtn.setString((slc.savePartStates) ? "True" : "False");
			SLCSaveSkinConfigProfile.updateProfile(slc, slc.currentLoadedProfile);
		}
		if (object.equals(resetBtn)) {
			slc.resetMode = PartModes.N; defaultProfileResetBtn.setString("Indv.");
			slc.currentChangeValue = 100; intervalBtn.setString("" + slc.currentChangeValue);
			currentProfile = 1; slc.defaultLoadedProfile = 1; loadProfileBtn.setString("" + currentProfile);
			defaultFacingBtn.setString("Front"); isFront = "Front"; slc.rotateSkin(isFront);
			saveStateBtn.setString("True"); slc.savePartStates = true;
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
		intervalBtn.setString("" + slc.currentChangeValue);
		slc.getConfig().saveMainConfig();
	}
	
	private void changeProfile() {
		if (currentProfile == 4) { currentProfile = 1; }
		else if (currentProfile < 4) { currentProfile += 1; }
		loadProfileBtn.setString("" + currentProfile);
		slc.defaultLoadedProfile = currentProfile;
		slc.getConfig().saveMainConfig();
	}
	
	private void changeFacing() {
		isFront = isFront.equals("Front") ? "Back" : "Front";
		defaultFacingBtn.setString(isFront);
		slc.rotateSkin(isFront);
		slc.getConfig().saveMainConfig();
	}
	
	private void returnToMainGui() {
    	slc.currentPart = null;
    	fileUpAndClose();
	}
	
}