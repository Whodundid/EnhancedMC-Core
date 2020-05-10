/*package com.Whodundid.scripts.builtInScripts;

import com.Whodundid.scripts.scriptBase.Script;
import com.Whodundid.scripts.scriptUtil.Exceptions.ScriptException.ScriptArgumentException;

public class Script_SchemPreviewer extends Script {
	
	String baseName = "";
	int frames = 0;
	int returnPoint = 0;
	
	public Script_SchemPreviewer() {
		this.ScriptName = "SchemPreviewer";
	}

	public void startScript(String[] args) throws ScriptArgumentException {
		/*
		if (args.length > 0 && args.length < 4) {
			try {
				baseName = args[0];
				frames = Integer.parseInt(args[1]);
				returnPoint = Integer.parseInt(args[2]);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		runner = new Thread() {
			@Override
			public void run() {
				try {					
					long startTime = System.currentTimeMillis();
					int currentFrame = 1;
					
					boolean forward = true;
					boolean load = true;
					
					while (!globalKill.get()) {
						if (System.currentTimeMillis() - startTime > 500) {
							if (forward) {
								if (load) {
									if (currentFrame < returnPoint) {
										mc.thePlayer.sendChatMessage("//schem load " + baseName + "_" + currentFrame);
										currentFrame++;
									} else if (currentFrame == returnPoint) {
										mc.thePlayer.sendChatMessage("//schem load " + baseName + "_" + currentFrame);
										currentFrame--;
										forward = false;
									}
									load = false;
								} else {
									mc.thePlayer.sendChatMessage("//paste");
									load = true;
								}
								
							} else {
								if (load) {
									if (currentFrame > 1) {
										mc.thePlayer.sendChatMessage("//schem load " + baseName + "_" + currentFrame);
										currentFrame--;
									} else if (currentFrame == 1) {
										mc.thePlayer.sendChatMessage("//schem load " + baseName + "_" + currentFrame);
										currentFrame++;
										forward = true;
									}
									load = false;
								} else {
									mc.thePlayer.sendChatMessage("//paste");
									load = true;
								}								
							}
							
							startTime = System.currentTimeMillis();
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