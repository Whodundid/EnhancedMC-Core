/*package com.Whodundid.scripts.builtInScripts;

import java.awt.AWTException;
import java.awt.Robot;
import java.awt.event.InputEvent;
import org.lwjgl.input.Mouse;
import com.Whodundid.scripts.ScriptManager;
import com.Whodundid.scripts.scriptBase.Script;
import com.Whodundid.scripts.scriptUtil.Exceptions.ScriptException.ScriptArgumentException;

public class Script_SpamRightClick extends Script {

	Robot clicker;
	
	public Script_SpamRightClick() throws AWTException {
		this.ScriptName = "SpamRightClick";
		clicker = new Robot();
	}
	
	public void startScript(String[] args) throws ScriptArgumentException {
		/*
		runner = new Thread() {
			@Override
			public void run() {
				try {
					long startTime = System.currentTimeMillis();
					
					while (!globalKill.get()) {
						if (mc.inGameHasFocus) {
							if (System.currentTimeMillis() - startTime >= 150) {
							
								clicker.mousePress(InputEvent.BUTTON3_DOWN_MASK);
								clicker.mouseRelease(InputEvent.BUTTON3_DOWN_MASK);
								startTime = System.currentTimeMillis();
							}
						}
					}
					ScriptManager.removeRunningScript(scriptID);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		};
		runner.start();
	}
}
*/