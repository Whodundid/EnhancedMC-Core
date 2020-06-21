package com.Whodundid.slc.util;

import com.Whodundid.slc.SLCApp;

public class PartStats {
	
	SLCApp slc;
	
	public PartStats(SLCApp slsIn) {
		slc = slsIn;
	}
	
	public String getPartMode(LayerTypes type) {
		if (isPartSwitching(type)) { return "switching"; }
		else if (isPartBlinking(type)) { return "blinking"; }
		else { return "unspecified"; }
	}
	
	public boolean getPartEnabled(LayerTypes type) { return slc.getPart(type).isEnabled(); }
	public boolean getPartState(LayerTypes type) { return slc.getPart(type).getState(); }
	public boolean isPartSwitching(LayerTypes type) { return slc.getPart(type).isSwitching(); }
	public boolean isPartBlinking(LayerTypes type) { return slc.getPart(type).isBlinking(); }
	public boolean doesPartHasOffset(LayerTypes type) { return slc.getPart(type).hasOffset(); }
	public boolean isPartStateFlipped(LayerTypes type) { return slc.getPart(type).getStateFlipped(); }
	
	public int getPartSwitchSpeed(LayerTypes type) { return slc.getPart(type).getSwitchSpeed(); }
	public int getPartBlinkSpeed(LayerTypes type) { return slc.getPart(type).getBlinkSpeed(); }
	public int getPartBlinkDuration(LayerTypes type) { return slc.getPart(type).getBlinkDuration(); }
	public int getPartOffset(LayerTypes type) { return slc.getPart(type).getOffset(); }
}