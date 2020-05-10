package com.Whodundid.scripts.scriptUtil.Exceptions;

//Last edited: Oct 11, 2018
//First Added: Oct 11, 2018
//Author: Hunter Bragg

public class ScriptException extends Exception {
	
	public ScriptException() {}
	public ScriptException(String message) {
		super(message);
	}
	
	public class ScriptAlreadyRunningException extends ScriptException {
		public ScriptAlreadyRunningException(String message) {
			super(message);
		}
	}
	
	public class ScriptArgumentException extends ScriptException {
		
		public ScriptArgumentException(String message) {
			super(message);
		}
	}
}
