/*package com.Whodundid.scripts.builtInScripts;

import java.awt.Robot;
import java.awt.event.InputEvent;
import com.Whodundid.scripts.ScriptManager;
import com.Whodundid.scripts.scriptBase.Script;
import com.Whodundid.scripts.scriptUtil.Exceptions.ScriptException.ScriptArgumentException;

public class Script_DestroyAll extends Script {
	
	public void startScript(String[] args) throws ScriptArgumentException {
		/*
		runner = new Thread() {
			@Override
			public void run() {
				try {
					Robot boty = new Robot();
					refreshTime = System.currentTimeMillis();
					//long sideTime = System.currentTimeMillis();
					//long forwardTime = System.currentTimeMillis();
					//long forwardWaitTime = System.currentTimeMillis();
					boolean left = false;
					//boolean forwardWait = false;
					//int i = 0;
					//boty.keyPress(KeyEvent.VK_A);
					//boty.mousePress(InputEvent.BUTTON1_DOWN_MASK);
					while (!globalKill.get() || !kill.get()) {
						if (System.currentTimeMillis() - refreshTime >= 100) {
							
							if (left) {
								System.out.println("cow");
								left = false;
							} else {
								boty.mouseRelease(InputEvent.BUTTON2_DOWN_MASK);
								System.out.println("rabbit");
								left = true;
							}
							
							if (left) {
								boty.mousePress(InputEvent.BUTTON1_DOWN_MASK);
								//boty.keyPress(KeyEvent.VK_SPACE);
								left = false;
							} else {
								boty.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);
								//boty.keyRelease(KeyEvent.VK_SPACE);
								left = true;   
							}								
						}
						
						if (forwardWait) {
							if (System.currentTimeMillis() - forwardWaitTime >= 11950) {
								boty.keyPress(KeyEvent.VK_W);
								forwardTime = System.currentTimeMillis();
								forwardWait = false;
							}
						} else {
							if (System.currentTimeMillis() - forwardTime >= 50) {
								boty.keyRelease(KeyEvent.VK_W);
								forwardWaitTime = System.currentTimeMillis();
								forwardWait = true;
							}
						}
						
						
						if (System.currentTimeMillis() - sideTime >= 12000) {
							if (left) {
								boty.keyRelease(KeyEvent.VK_D);
								boty.keyPress(KeyEvent.VK_A);
								left = false;
								sideTime = System.currentTimeMillis();
							} else {
								boty.keyRelease(KeyEvent.VK_A);
								boty.keyPress(KeyEvent.VK_D);
								left = true;
								sideTime = System.currentTimeMillis();
							}
							refreshTime = System.currentTimeMillis();
						}							
					}
					
					//boty.keyRelease(KeyEvent.VK_W);
					//boty.keyRelease(KeyEvent.VK_D);
					//boty.keyRelease(KeyEvent.VK_A);
					boty.mouseRelease(InputEvent.BUTTON2_DOWN_MASK);
					
					ScriptManager.removeRunningScript(scriptID);
				} catch (Exception e) { e.printStackTrace(); }
			}
		};
		runner.start();
	}
}
*/