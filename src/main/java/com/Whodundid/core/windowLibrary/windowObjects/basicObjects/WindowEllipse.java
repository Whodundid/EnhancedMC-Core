package com.Whodundid.core.windowLibrary.windowObjects.basicObjects;

import com.Whodundid.core.util.renderUtil.EColors;
import com.Whodundid.core.util.storageUtil.StorageBox;
import com.Whodundid.core.windowLibrary.windowTypes.interfaces.IWindowObject;

public class WindowEllipse extends WindowShape {
	
	int cX, cY;
	
	public WindowEllipse(IWindowObject parentIn, int x, int y, int radiusX, int radiusY) { this(parentIn, x, y, radiusX, radiusY, true, 0xffffffff); }
	public WindowEllipse(IWindowObject parentIn, int x, int y, int radiusX, int radiusY, EColors colorIn) { this(parentIn, x, y, radiusX, radiusY, true, colorIn.intVal); }
	public WindowEllipse(IWindowObject parentIn, int x, int y, int radiusX, int radiusY, int colorIn) { this(parentIn, x, y, radiusX, radiusY, true, colorIn); }
	public WindowEllipse(IWindowObject parentIn, int x, int y, int radiusX, int radiusY, boolean filledIn) { this(parentIn, x, y, radiusX, radiusY, filledIn, 0xffffffff); }
	public WindowEllipse(IWindowObject parentIn, int x, int y, int radiusX, int radiusY, boolean filledIn, EColors colorIn) { this(parentIn, x, y, radiusX, radiusY, filledIn, colorIn.intVal); }
	public WindowEllipse(IWindowObject parentIn, int x, int y, int radiusX, int radiusY, boolean filledIn, int colorIn) {
		init(parentIn, x, y, radiusX * 2, radiusY * 2);
		cX = x;
		cY = y;
		filled = filledIn;
		color = colorIn;
	}
	
	@Override
	public void drawObject(int mXIn, int mYIn) {
		
		if (filled) { drawFilledEllipse(startX + width / 2, startY + height / 2, width, height, 50, color); }
		else { drawEllipse(startX + width / 2, startY + height / 2, width, height, 50, color); }
		
		super.drawObject(mXIn, mYIn);
	}
	
	//-------------------
	//EGuiEllipse Getters
	//-------------------
	
	public StorageBox<Integer, Integer> getCenter() { return new StorageBox(cX, cY); }
	
	//-------------------
	//EGuiEllipse Setters
	//-------------------
	
	public WindowEllipse setCenter(StorageBox<Integer, Integer> in) { return setCenter(in.getObject(), in.getValue()); }
	public WindowEllipse setCenter(int x, int y) { cX = x; cY = y; return this; }

}
