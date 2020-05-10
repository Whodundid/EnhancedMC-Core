package com.Whodundid.slc.util;

import java.util.Timer;
import java.util.TimerTask;

import com.Whodundid.core.util.storageUtil.StorageBoxHolder;
import com.Whodundid.slc.SLCApp;
import com.Whodundid.slc.config.SLCSaveSkinConfigProfile;

import net.minecraft.client.Minecraft;

public class PartModifier {
	
	SLCApp sls;
	private boolean delayTimer = false;
	
	public PartModifier(SLCApp slsIn) {
		sls = slsIn;
	}
	
	public void setPartMode(LayerTypes type, String mode) {
		if (mode.equals("switch")) {
			sls.getPart(type).setSwitching();
			SLCSaveSkinConfigProfile.updateProfile(sls, sls.currentSkinProfile);
		} else if (mode.equals("blink")) {
			sls.getPart(type).setBlinking();
			SLCSaveSkinConfigProfile.updateProfile(sls, sls.currentSkinProfile);
		} else if (mode.equals("none")) {
			sls.getPart(type).setNoMode();
			SLCSaveSkinConfigProfile.updateProfile(sls, sls.currentSkinProfile);
		}
	}	
	
	public void setAllPartsMode(String mode) {
		if (mode.equals("switch")) {
			for (LayerTypes t : LayerTypes.values()) { setPartMode(t, "switch"); }
		} else if (mode.equals("blink")) {
			for (LayerTypes t : LayerTypes.values()) { setPartMode(t, "blink"); }
		} else if (mode.equals("none")) {
			for (LayerTypes t : LayerTypes.values()) { setPartMode(t, "none"); }
		}
	}
	
    public void setPartEnabled(LayerTypes type, boolean state) {
    	sls.getPart(type).setEnabled(state);
    	resetCurrentPartRates();
    	SLCSaveSkinConfigProfile.updateProfile(sls, sls.currentSkinProfile); 
    }
	
	public void deselectAllParts() {
		for (LayerTypes t : LayerTypes.values()) { setPartEnabled(t, false); }
	}
	
	public void setPartSwitchSpeed(LayerTypes type, int speed) {
		sls.getPart(type).setSwitchSpeed(speed);
		resetCurrentPartRates();
		SLCSaveSkinConfigProfile.updateProfile(sls, sls.currentSkinProfile); 
	}
	
	public void setPartBlinkSpeed(LayerTypes type, int speed) {
		sls.getPart(type).setBlinkSpeed(speed);
		resetCurrentPartRates();
		SLCSaveSkinConfigProfile.updateProfile(sls, sls.currentSkinProfile); 
	}
	
	public void setPartBlinkDuration(LayerTypes type, int speed) {
		sls.getPart(type).setBlinkDuration(speed);
		resetCurrentPartRates();
		SLCSaveSkinConfigProfile.updateProfile(sls, sls.currentSkinProfile);
	}
	
	public void setPartOffset(LayerTypes type, int offset) {
		sls.getPart(type).setOffset(offset);
		resetCurrentPartRates();
		SLCSaveSkinConfigProfile.updateProfile(sls, sls.currentSkinProfile);
	}
	
    public void setAllPartsToState(boolean state) {
    	if (!delayTimer) {
    		startDelayTimer();
    		for (LayerTypes t : LayerTypes.values()) {
    			sls.getPart(t).togglePartStateTo(state);
    			Minecraft.getMinecraft().gameSettings.setModelPartEnabled(t.getMCType(), state);
    		}
    		SLCSaveSkinConfigProfile.updateProfile(sls, sls.currentSkinProfile);
    	}    	
    }
    
    public void flipPartState(LayerTypes type) {
    	SkinPart part = sls.getPart(type);
    	part.setStateFlipped(!part.getStateFlipped());
    	part.togglePartState();
    	if (part.isEnabled()) { sls.updateMCSkinLayers(part); }
    	SLCSaveSkinConfigProfile.updateProfile(sls, sls.currentSkinProfile);
    }

	public void selectAllParts(boolean state) {
		for (SkinPart p : sls.getParts()) { p.setEnabled(state); }
		SLCSaveSkinConfigProfile.updateProfile(sls, sls.currentSkinProfile);
	}
	
	public void resetCurrentPartRates() {
		StorageBoxHolder<LayerTypes, Long> rates = sls.getCurrentPartRates();
		for (LayerTypes t : LayerTypes.values()) {
			rates.put(t, 0l);
			sls.getPart(t).setOffsetUsed(false);
		}
	}
	
	public void resetPartValues(LayerTypes type) {
		SkinPart part = sls.getPart(type);
		part.setAlreadyBlinked(false);
		part.setBlinkDuration(sls.defaultBlinkDuration);
		part.setBlinkSpeed(sls.defaultBlinkDelay);
		part.setSwitchSpeed(sls.defaultSwitchSpeed);
		part.setOffset(0);
		part.setOffsetUsed(false);
		part.setStateFlipped(false);
		part.togglePartStateTo(false);
		part.setNoMode();
		SLCSaveSkinConfigProfile.updateProfile(sls, sls.currentSkinProfile);
	}
	
	public void resetAllParts() {
		for (LayerTypes t : LayerTypes.values()) { resetPartValues(t); }
	}
	
	private void startDelayTimer() {
		delayTimer = true;
		Timer delay = new Timer();
		delay.schedule(new TimerTask() { @Override public void run() { delayTimer = false; } }, sls.MinRate);
	}
}