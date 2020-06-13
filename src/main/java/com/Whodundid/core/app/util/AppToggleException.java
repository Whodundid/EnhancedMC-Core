package com.Whodundid.core.app.util;

import com.Whodundid.core.app.EMCApp;
import com.Whodundid.core.app.window.windowUtil.AppErrorType;
import com.Whodundid.core.util.storageUtil.EArrayList;

//Author: Hunter Bragg

public class AppToggleException extends Exception {
	
	AppErrorType type;
	EArrayList<EMCApp> modList;
	
	public AppToggleException() { this(AppErrorType.ERROR, null); }
	public AppToggleException(AppErrorType typeIn) { this(typeIn, null); }
	public AppToggleException(AppErrorType typeIn, EArrayList<EMCApp> listIn) {
		super();
		type = typeIn;
		modList = listIn;
	}
	
	public AppErrorType getErrorType() { return type; }
	public EArrayList<EMCApp> getModList() { return modList; }
}
