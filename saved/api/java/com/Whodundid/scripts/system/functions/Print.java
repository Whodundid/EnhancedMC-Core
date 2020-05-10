package com.Whodundid.scripts.system.functions;

import com.Whodundid.core.util.chatUtil.ChatBuilder;
import com.Whodundid.core.util.storageUtil.EArrayList;
import com.Whodundid.scripts.EScript;
import com.Whodundid.scripts.scriptUtil.Exceptions.ScriptException;
import com.Whodundid.scripts.scriptUtil.scriptObjects.scriptVariables.ScriptBool;
import com.Whodundid.scripts.scriptUtil.scriptObjects.scriptVariables.ScriptDouble;
import com.Whodundid.scripts.scriptUtil.scriptObjects.scriptVariables.ScriptInt;
import com.Whodundid.scripts.scriptUtil.scriptObjects.scriptVariables.ScriptString;
import com.Whodundid.scripts.scriptUtil.scriptObjects.scriptVariables.ScriptVarType;
import com.Whodundid.scripts.scriptUtil.scriptObjects.scriptVariables.ScriptVariable;
import java.util.InputMismatchException;
import java.util.Scanner;

//Last edited: Jun 18, 2019
//First Added: Jun 18, 2019
//Author: Hunter Bragg

public class Print implements ISystemFunction {
	
	private int unclosed = 0;

	@Override public String getName() { return "print"; }

	@Override
	public void runFunction(EScript script, EArrayList<String> args) throws ScriptException {
		if (args == null) { print(""); return; } //check if list is null
		if (args.size() == 0 || args.get(0) == null) { print(""); return; } //check for empty list or null first entry
		
		String line = args.get(0);
		Scanner lineReader = new Scanner(line);
		
		try {
			if (line.length() == 0) { print(""); return; } //check for empty line
			
			//check if there is only a single character in the line
			if (line.length() == 1) {
				if (script.checkForType(line) != ScriptVarType.UNDEFINED) {
					ScriptVariable var = script.getVariable(line);
					switch (var.getVariableType()) {
					case BOOL: print(Boolean.toString(((ScriptBool) var).val)); break;
					case DOUBLE: print(Double.toString(((ScriptDouble) var).val)); break;
					case INT: print(Integer.toString(((ScriptInt) var).val)); break;
					case STRING: print(((ScriptString) var).s); break;
					default: break;
					}
				}
				else { throwError(script, ": Unidentified symbol: '" + line + "'"); }
			}
			else if (line.length() > 1) {
				//test if variable
				if (script.checkForType(line) != ScriptVarType.UNDEFINED) {
					ScriptVariable var = script.getVariable(line);
					switch (var.getVariableType()) {
					case BOOL: print(Boolean.toString(((ScriptBool) var).val)); break;
					case DOUBLE: print(Double.toString(((ScriptDouble) var).val)); break;
					case INT: print(Integer.toString(((ScriptInt) var).val)); break;
					case STRING: print(((ScriptString) var).s); break;
					default: break;
					}
				}
				else {
					//test if ""
					for (int i = 0; i < line.length(); i++) {
						char c = line.charAt(i);
						if (c == '"') { unclosed++; }
					}
					if (unclosed % 2 == 1) { throwError(script, ": Unidentified symbol: '" + line + "'"); }
					
					char first = line.charAt(0);
					char last = line.charAt(line.length() - 1);
					if (first == '"' && last == '"') {
						if (line.length() == 2) { print(""); }
						else {
							String msg = line.substring(1, line.length() - 2);
							print(msg);
						}
					}
					else { throwError(script, ": Unidentified symbol: '" + line + "'"); }
				}
			}
		}
		catch (InputMismatchException e) { e.printStackTrace(); }
		finally {
			lineReader.close();
			unclosed = 0;
		}
	}
	
	private void print(String msg) {
		if (mc.thePlayer != null) {
			mc.thePlayer.addChatMessage(ChatBuilder.of(msg).build());
		}
	}
	
	private void throwError(EScript script, String msg) throws ScriptException {
		throw new ScriptException(script.getScriptName() + msg);
	}
}
