package com.Whodundid.slc.util;

public class SkinPart {
	
	private LayerTypes type;
	private boolean enabled;
	private boolean isSwitching;
	private boolean isBlinking;
	private boolean partState;
	private boolean stateFlipped;
	private boolean alreadyBlinked;
	private boolean offsetUsed;
	private int switchSpeed;
	private int blinkSpeed;
	private int blinkDuration;
	private int offset;
	
	public SkinPart(LayerTypes typeIn) {
		type = typeIn;
		switchSpeed = 1500;
		blinkSpeed = 3000;
		blinkDuration = 400;
		partState = true;
		enabled = true;
	}
	
	public LayerTypes getType() { return type; }
	
	public void setEnabled(boolean state) { enabled = state; }
	public void setSwitching() { isSwitching = true; isBlinking = false; }
	public void setBlinking() { isBlinking = true; isSwitching = false; }
	public void toggleSwitching() { isBlinking = false; isSwitching = !isSwitching; }
	public void toggleBlinking() { isSwitching = false; isBlinking = !isBlinking; }
	public void setSwitchSpeed(int speed) { switchSpeed = speed; }
	public void setBlinkSpeed(int speed) { blinkSpeed = speed; }
	public void setBlinkDuration(int speed) { blinkDuration = speed; }
	public void setOffset(int offsetIn) { offset = offsetIn; }
	public void setOffsetUsed(boolean value) { offsetUsed = value; }
	public void setNoMode() { isSwitching = false; isBlinking = false; }
	public void setAlreadyBlinked(boolean state) { alreadyBlinked = state; }
	public void setStateFlipped(boolean state) { stateFlipped = state; }
	public void togglePartState() { partState = !partState; }
	public void togglePartStateTo(boolean state) { partState = state; }
	
	public boolean isEnabled() { return enabled; }
	public boolean isSwitching() { return isSwitching; }
	public boolean isBlinking() { return isBlinking; }
	public boolean hasOffset() { return offset > 0; }
	public boolean hasOffsetBeenUsed() { return offsetUsed; }
	public boolean getState() { return partState; }
	public boolean getStateFlipped() { return stateFlipped; }
	public boolean hasAlreadyBlinked() { return alreadyBlinked; }
	public boolean compareType(LayerTypes typeIn) { return typeIn.getLayerType().equals(type.getLayerType()); }
	
	public int getSwitchSpeed() { return switchSpeed; }
	public int getBlinkSpeed() { return blinkSpeed; }
	public int getBlinkDuration() { return blinkDuration; }
	public int getOffset() { return offset; }
}
