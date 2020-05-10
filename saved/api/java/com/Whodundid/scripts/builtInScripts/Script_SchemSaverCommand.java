/*package com.Whodundid.scripts.builtInScripts;

import com.Whodundid.scripts.ScriptManager;
import com.Whodundid.scripts.scriptBase.Script;
import com.Whodundid.scripts.scriptUtil.Exceptions.ScriptException.ScriptArgumentException;
import net.minecraft.client.Minecraft;

public class Script_SchemSaverCommand extends Script {
	
	String baseName = "";
	
	public Script_SchemSaverCommand() {
		this.ScriptName = "SchemSaver";
	}

	public void startScript(String[] args) throws ScriptArgumentException {
		/*
		if (args.length > 0) {
			if (args.length > 0) {
				baseName = args[0];
			} else {
				throw new ScriptArgumentException("Given argument: " + args[0] + " is not an integer.");
			}
		}
		
		runner = new Thread() {
			@Override
			public void run() {
				try {
					long startTime = System.currentTimeMillis();
					int i = 0;
					while (i < 2 && !globalKill.get()) {
						if (System.currentTimeMillis() - startTime > 100) {
							switch (i) {
							case 0: Minecraft.getMinecraft().thePlayer.sendChatMessage("//copy"); break;
							case 1: Minecraft.getMinecraft().thePlayer.sendChatMessage("//schem save " + baseName); globalKill.set(true); break;
							}
							i++;
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
