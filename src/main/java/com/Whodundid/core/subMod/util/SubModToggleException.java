package com.Whodundid.core.subMod.util;

import com.Whodundid.core.subMod.SubMod;
import com.Whodundid.core.subMod.gui.SubModErrorType;
import com.Whodundid.core.util.storageUtil.EArrayList;

//Author: Hunter Bragg

public class SubModToggleException extends Exception {
	
	SubModErrorType type;
	EArrayList<SubMod> modList;
	
	public SubModToggleException() { this(SubModErrorType.ERROR, null); }
	public SubModToggleException(SubModErrorType typeIn) { this(typeIn, null); }
	public SubModToggleException(SubModErrorType typeIn, EArrayList<SubMod> listIn) {
		super();
		type = typeIn;
		modList = listIn;
	}
	
	public SubModErrorType getErrorType() { return type; }
	public EArrayList<SubMod> getModList() { return modList; }
}
