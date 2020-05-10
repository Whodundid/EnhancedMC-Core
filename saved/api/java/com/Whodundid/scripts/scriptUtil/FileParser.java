package com.Whodundid.scripts.scriptUtil;

import com.Whodundid.core.util.storageUtil.EArrayList;
import com.Whodundid.scripts.EScript;
import com.Whodundid.scripts.scriptUtil.Exceptions.ScriptException;
import com.Whodundid.scripts.scriptUtil.Exceptions.ScriptNotFoundException;
import java.io.File;
import java.util.Scanner;

//Last edited: Jun 17, 2019
//First Added: Jun 17, 2019
//Author: Hunter Bragg

public class FileParser {
	
	private static File currentFile = null;
	
	/** Attempts to assemble a script object off of the given file. */
	public static EScript parseFile(File scriptIn) {
		if (scriptIn != null) {
			currentFile = scriptIn;
			Scanner fileReader = null;
			try {
				fileReader = new Scanner(scriptIn);
				EArrayList<String> lines = new EArrayList();
				
				//read in all lines
				while (fileReader.hasNextLine()) { lines.add(fileReader.nextLine()); }
				
				//check that read script actually has content
				if (lines.size() == 0) { throw new ScriptNotFoundException(scriptIn.getName()); }
				
				//search for scriptDef at line 1
				String name = findScriptName(lines.get(0));
				
				if (name == null) { throw new ScriptNotFoundException(scriptIn.getName()); }
				return new EScript(name, lines);
			}
			catch (Exception e) { e.printStackTrace(); }
			finally {
				if (fileReader != null) { fileReader.close(); }
			}
		}
		currentFile = null;
		return null;
	}
	
	/** Searches through the given line for the specific 'scriptDef name' template. */
	private static String findScriptName(String lineContents) {
		Scanner lineReader = new Scanner(lineContents);
		try {
			EArrayList<String> tokens = new EArrayList();
			while (lineReader.hasNext()) {
				tokens.add(lineReader.next());
				if (tokens.size() >= 2) { break; }
			}
			
			if (tokens.size() > 2) { throw new ScriptException("Too many script name arguments. Use only: 'scriptDef name'"); }
			else if (tokens.size() < 2) { throw new ScriptException("Not enough script name arguments. Use: 'scriptDef name'"); }
			
			String scriptDefCheck = tokens.get(0);
			String scriptNameCheck = tokens.get(1);
			
			if (scriptDefCheck == null) { throw new ScriptException("ScriptDefCheck somehow returned null!"); }
			if (scriptNameCheck == null) { throw new ScriptException("Script name somehow returned null!"); }
			
			if (!scriptDefCheck.equals("scriptDef")) { throw new ScriptNotFoundException(currentFile.getName()); }
			if (scriptNameCheck.length() == 0) { throw new ScriptException("No script name found directly after scriptDef!"); }
			
			return scriptNameCheck;
		}
		catch (ScriptException e) {}
		catch (Exception e) { e.printStackTrace(); }
		finally {
			if (lineReader != null) { lineReader.close(); }
		}
		
		return null;
	}
}
