package com.Whodundid.scripts.scriptUtil;

import com.Whodundid.core.util.chatUtil.ChatBuilder;
import com.Whodundid.core.util.storageUtil.EArrayList;
import com.Whodundid.scripts.EScript;
import com.Whodundid.scripts.ScriptingApp;
import com.Whodundid.scripts.scriptUtil.Exceptions.ScriptException;
import java.util.Iterator;
import net.minecraft.client.Minecraft;

//Last edited: Jun 17, 2019
//First Added: Jun 17, 2019
//Author: Hunter Bragg

public class ScriptRunner {
	
	private ScriptingApp man;
	private EArrayList<EScript> scripts = new EArrayList();
	private EArrayList<EScript> scriptsToBeAdded = new EArrayList();
	
	private class RunnerThread extends Thread {
		
		private boolean running = false;
		private long timeAlive = 0l;
		
		@Override
		public void run() {
			if (!running) {
				System.out.println("Script runner thread started!");
				running = true;
				
				while (!man.getGlobalKill() && running) {
					//check for any new scripts and add them
					if (scriptsToBeAdded.isNotEmpty()) {
						scripts.addAll(scriptsToBeAdded);
						scriptsToBeAdded.clear();
					}
					
					Iterator<EScript> it = scripts.iterator();
					
					while (it.hasNext()) {
						EScript s = it.next();
						ScriptReader reader = s.getReader();
						
						switch (s.getState()) {
						case dying: //check for dying scripts
							it.remove();
							s.setState(ScriptStates.dead);
							Minecraft.getMinecraft().thePlayer.addChatMessage(ChatBuilder.of("Script: " + s.getScriptName() + " killed.").build());
							continue;
						case sleeping://check for sleeping scripts
							if (System.currentTimeMillis() - s.getSleepStart() >= s.getSleepDuration()) {
								//remove sleeping script
								s.setState(ScriptStates.idle);
							}
							break;
						case idle: //run next script line
							if (reader.containsScript()) {
								try {
									if (!man.getGlobalKill() && running) { reader.runNextLine(); }
								} catch (ScriptException e) {
									e.printStackTrace();
									//remove the script with error
									s.setState(ScriptStates.dying);
								}
							}
							break;
						default: break;
						}
					}
				}
				System.out.println("Script runner thread stopped!");
			}
			else { System.out.println("Already running!"); }
		}
		
		public boolean isRunning() { return running; }
		public long getRunningTime() { return timeAlive; }
		
		public void kill() { if (running) { running = false; } }
	};
	
	RunnerThread runnerThread = null;
	
	public ScriptRunner(ScriptingApp manIn) {
		man = manIn;
		runnerThread = new RunnerThread();
		runnerThread.start();
	}
	
	public void tryStartScript(EScript scriptIn, EArrayList<String> args) {
		if (man.isEnabled()) {
			if (scriptIn != null) { startScript(scriptIn, args); }
		}
	}
	
	private void startScript(EScript scriptIn, EArrayList<String> args) {
		int id = scripts.size();
		scriptIn.setID(id);
		scriptIn.setArgs(args);
		scriptIn.setState(ScriptStates.idle);
		scriptIn.setStartTime(System.currentTimeMillis());
		scriptsToBeAdded.add(scriptIn);
		Minecraft.getMinecraft().thePlayer.addChatMessage(ChatBuilder.of("Script: " + scriptIn.getScriptName() + " started.").build());
	}
	
	public boolean isScriptRunning(Class scriptIn) {
		if (scriptIn == null) { return false; }
		if (scripts.size() == 0) { return false; }
		
		for (EScript s : scripts) {
			if (s.getClass().equals(scriptIn)) { return true; }
		}
		
		return false;
	}
	
	public boolean isScriptRunning(String scriptName) {
		if (scriptName == null) { return false; }
		if (scripts.size() == 0) { return false; }
		
		for (EScript s : scripts) {
			if (s.getScriptName().equals(scriptName)) { return true; }
		}
		return false;
	}
	
	public boolean killScript(Class scriptIn) {
		if (scriptIn == null) { return false; }
		if (scripts.size() == 0) { return false; }
		
		for (EScript s : scripts) {
			if (s.getClass().equals(scriptIn)) {
				if (s.isRunning()) { s.setState(ScriptStates.dying); }
				return true;
			}
		}
		return false;
	}
	
	public EArrayList<EScript> getScripts() {
		return scripts;
	}
	
	public RunnerThread getRunnerThread() { return runnerThread; }
	public boolean isRunning() { return runnerThread.isRunning(); }
	public long getRunningTime() { return runnerThread.getRunningTime(); }
	
	public void startRunner() {
		try {
			if (runnerThread != null && !runnerThread.isRunning()) { runnerThread = new RunnerThread(); runnerThread.start(); }
			else { System.out.println("Already running!"); }
		} catch (Exception e) { e.printStackTrace(); }
	}
	public void stopRunner() {
		try {
			if (runnerThread != null) {
				runnerThread.kill();
				try {
					runnerThread.join();
					Iterator<EScript> it = scripts.iterator();
					while (it.hasNext()) {
						EScript s = it.next();
						s.setState(ScriptStates.dead);
						Minecraft.getMinecraft().thePlayer.addChatMessage(ChatBuilder.of("Script: " + s.getScriptName() + " killed.").build());
						it.remove();
					}
				} catch (InterruptedException e) { e.printStackTrace(); }
			}
			else { System.out.println("Thread not running!"); }
		} catch (Exception e) { e.printStackTrace(); }
	}
	public void setRunning(boolean val) {
		if (val) { startRunner(); }
		else { stopRunner(); }
	}
}
