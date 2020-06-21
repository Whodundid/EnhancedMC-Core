package com.Whodundid.core.windowLibrary.windowObjects.windows;

import com.Whodundid.core.windowLibrary.windowObjects.actionObjects.WindowButton;
import com.Whodundid.core.windowLibrary.windowTypes.WindowParent;
import com.Whodundid.core.windowLibrary.windowTypes.interfaces.IActionObject;

public class CalculatorWindow extends WindowParent {

	WindowButton plus, minus, multiply, divide, pow, square, sqrt, equals;
	WindowButton one, two, three, four, five, six, seven, eight, nine, zero;
	WindowButton lParens, rParens;
	WindowButton backspace, clear;
	
	@Override
	public void initWindow() {
		setDimensions(150, 200);
	}
	
	@Override
	public void initObjects() {
		
	}
	
	@Override
	public void drawObject(int mXIn, int mYIn) {
		
	}
	
	@Override
	public void actionPerformed(IActionObject object, Object... args) {
		
	}
	
}
