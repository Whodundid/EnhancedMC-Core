package com.Whodundid.core.subMod;

import com.Whodundid.core.enhancedGui.EnhancedGui;
import com.Whodundid.core.util.miscUtil.ScreenLocation;
import com.Whodundid.core.util.storageUtil.StorageBox;

//Last edited: Dec 14, 2018
//First Added: Dec 14, 2018
//Author: Hunter Bragg

public interface IUseScreenLocation {
	
	public void setLocation(ScreenLocation locIn);
	public void setLocation(int xIn, int yIn);
	public StorageBox<Integer, Integer> getLocation();
	public ScreenLocation getScreenLocation();
	public EnhancedGui getScreenLocationGui();
}
