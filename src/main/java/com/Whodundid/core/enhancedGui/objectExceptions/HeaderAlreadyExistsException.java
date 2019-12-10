package com.Whodundid.core.enhancedGui.objectExceptions;

import com.Whodundid.core.enhancedGui.guiObjects.advancedObjects.header.EGuiHeader;

//Last edited: Jan 1, 2019
//First Added: Jan 1, 2019
//Author: Hunter Bragg

public class HeaderAlreadyExistsException extends Exception {
	
	public HeaderAlreadyExistsException(EGuiHeader existingHeader) {
		super("Only one Header object can be attached to an object. Header: " + existingHeader + " already is attached to " + existingHeader.getParent() + ".");
	}
}
