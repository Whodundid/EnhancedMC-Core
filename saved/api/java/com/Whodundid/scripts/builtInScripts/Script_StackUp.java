/*package com.Whodundid.scripts.builtInScripts;

import com.Whodundid.scripts.scriptBase.Script;
import com.Whodundid.scripts.scriptUtil.Exceptions.ScriptException.ScriptArgumentException;

//Last edited: Oct 9, 2018
//First Added: Oct 9, 2018
//Author: Hunter Bragg

public class Script_StackUp extends Script {

	public void startScript(String[] args) throws ScriptArgumentException {
		/*
		runner = new Thread() {
			@Override
			public void run() {
				try {
					refreshTime = System.currentTimeMillis();
					int i = 0;
					float previousPitch = 0f;
					while (!globalKill.get() || !kill.get()) {
						if (System.currentTimeMillis() - refreshTime > 100) {
							if (i == 0) {
								previousPitch = mc.thePlayer.rotationPitch;
								mc.thePlayer.rotationPitch = -90f;
								mc.thePlayer.sendChatMessage("//stack 100");
							} else {
								mc.thePlayer.rotationPitch = previousPitch;
								break;
							}
							i++;
							refreshTime = System.currentTimeMillis();
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