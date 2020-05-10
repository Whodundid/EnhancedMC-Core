package com.Whodundid.scripts;

import java.util.ArrayList;

public class AvailableScripts {
	
	static ArrayList<String> scriptList = new ArrayList<>();
	static ArrayList<EScript> unregisteredScripts = new ArrayList<>();
	
	public static void addNewScript(EScript scriptIn) {
		if (!unregisteredScripts.contains(scriptIn)) {
			
		}
	}
	
	protected static void registerScript(EScript scriptIn) {
		if (!scriptList.contains(scriptIn.getScriptName())) {
			scriptList.add(scriptIn.getScriptName());
		}
	}
	
	public static void registerAllScripts() {
		synchronized (scriptList) {
			scriptList.clear();
			
		}
	}
	
	public static void unregisterAllScripts() {
		synchronized (scriptList) {
			scriptList.clear();
		}
	}
}
