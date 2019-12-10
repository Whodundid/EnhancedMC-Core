package com.Whodundid.core.enhancedGui.objectExceptions;

import com.Whodundid.core.enhancedGui.types.interfaces.IEnhancedGuiObject;

public class ObjectInitException extends Exception {
	
	public ObjectInitException(IEnhancedGuiObject parentIn, IEnhancedGuiObject problemObject) {
		super(parentIn + " Failed to initialize " + problemObject + " object's!");
	}
}
