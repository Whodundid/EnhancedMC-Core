package com.Whodundid.scripts.scriptUtil.Exceptions;

import java.io.File;

//Last edited: Jun 17, 2019
//First Added: Jun 17, 2019
//Author: Hunter Bragg

public class ScriptNotFoundException extends ScriptException {
	public ScriptNotFoundException(File fileIn) {
		super("Parsed file: '" + fileIn.getName() + "' does not contain a scriptDef!");
	}
	public ScriptNotFoundException(String fileName) {
		super("Parsed file: '" + fileName + "' does not contain a scriptDef!");
	}
}