package com.Whodundid.scripts.scriptUtil;

import com.Whodundid.core.util.storageUtil.EArrayList;
import com.Whodundid.core.util.storageUtil.StorageBoxHolder;
import com.Whodundid.scripts.EScript;
import com.Whodundid.scripts.scriptUtil.Exceptions.ScriptException;
import java.util.Scanner;
import java.util.concurrent.atomic.AtomicBoolean;

//Last edited: Jun 17, 2019
//First Added: Jun 14, 2019
//Author: Hunter Bragg

public class ScriptReader {
	
	private EScript script = null;
	private int currentLine = 1;
	private AtomicBoolean currentlyRunning = new AtomicBoolean(false);
	
	public ScriptReader(EScript scriptIn) {
		script = scriptIn;
	}
	
	/** Runs the next line specified by currentLine of this ScriptReader's associated script.
	 *  If the end of the script is reached, this reader resets itself and unloads it's associated script.
	 *  
	 *  Will throw ScriptException if parsing a line with errors or if it is a line it cannot understand.
	 */
	public void runNextLine() throws ScriptException {
		System.out.println("Current Script Line: " + currentLine);
		if (currentLine >= script.getLength()) { script.setState(ScriptStates.dying); return; }
		script.setState(ScriptStates.running);
		String line = null;
		
		while (currentLine < script.getLength() && script.getLineAt(currentLine).isEmpty()) {
			currentLine++;
		}
		
		line = script.getLineAt(currentLine);
		
		findKeyWord(line);
		runKeyWord(null);
		
		currentLine++;
		script.setState(ScriptStates.idle);
	}
	
	/** Search through a line and find and return any scriptKeyWords */
	private StorageBoxHolder<ScriptKeyWord, String> findKeyWord(String lineIn) {
		StorageBoxHolder<ScriptKeyWord, String> keyWords = new StorageBoxHolder();
		Scanner lineReader = null;
		try {
			if (lineIn != null && !lineIn.isEmpty()) {
				if (lineIn.contains(".")) {
					String[] parts = lineIn.split("\\.");
					if (parts.length == 2) {
						String testKeyWord = parts[0];
						String target = parts[1];
						
						if (ScriptKeyWord.isStringKeyWord(testKeyWord)) {
							System.out.println("keyWord: " + ScriptKeyWord.getKeyWordFromString(testKeyWord));
							System.out.println("target: " + target);
							
							keyWords.add(ScriptKeyWord.getKeyWordFromString(testKeyWord), target);
						}
					}
				}
				else {
					
				}
			}
			
			
			
			
			
			
			
			
			
			
			/*EArrayList<ScriptKeyWord> found = null;
			if (lineIn != null) {
				found = new EArrayList();
				String[] tokensA = lineIn.split("[()]");
				EArrayList<String> tokens = new EArrayList();
				tokens.addAll(tokensA);
				
				//placeholder stuff
				for (String s : tokens) {
					if (s.contains(".")) {
						String[] parts = s.split(".");
						if (parts.length == 2) {
							String testKeyWord = parts[0];
							String target = parts[1];
							
							if (ScriptKeyWord.isStringKeyWord(testKeyWord)) {
								
							}
						}
					} else {
						if (ScriptKeyWord.isStringKeyWord(s)) {
							found.add(ScriptKeyWord.getKeyWordFromString(s));
						}
					}
				}
				
				System.out.println("TOKENS: " + tokens);
				System.out.println(found);
			}
			return found;*/
		}
		catch (Exception e) { e.printStackTrace(); }
		finally { if (lineReader != null) { lineReader.close(); } }
		return null;
	}
	
	private void runKeyWord(EArrayList<String> args) throws ScriptException {
		
	}
	
	/** Attempts to set the current script line related to this ScriptReader.
	 *  If there is no script associated with this ScriptReader, ScriptException is thrown.
	 *  If the given line number is out of range of the number of lines in this script, ScriptException is thrown.
	 */
	private ScriptReader setCurrentLine(int lineNumIn) throws ScriptException {
		if (script != null) { throw new ScriptException("Tried executing script command: 'setCurrentLine' when no script was defined!"); }
		if (lineNumIn < 0 || lineNumIn > script.getLength()) { throw new ScriptException("Line number: " + lineNumIn + " out of script range!"); }
		currentLine = lineNumIn;
		return this;
	}
	
	/** Returns this ScriptReader's associated script. */
	public EScript getScriptObj() { return script; }
	/** Returns the current line this script is on. */
	public int getCurrentLine() { return currentLine; }
	/** Returns if there is a script associated with this ScriptReader. */
	public boolean containsScript() { return script != null; }
}
