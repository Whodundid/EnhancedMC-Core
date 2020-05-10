/*package com.Whodundid.scripts.builtInScripts;

import com.Whodundid.main.util.miscUtil.NumberUtil;
import com.Whodundid.scripts.ScriptManager;
import com.Whodundid.scripts.scriptBase.Script;
import com.Whodundid.scripts.scriptUtil.Exceptions.ScriptException.ScriptArgumentException;
import net.minecraft.client.Minecraft;

public class Script_GrassBrush extends Script {
	
	int size = 5;

	public void startScript(String[] args) throws ScriptArgumentException {
		/*
		if (args != null) {
			if (args.length > 0) {
				if (NumberUtil.isInteger(args[0])) {
					size = Integer.parseInt(args[0]);
					if (size > 15) {
						throw new ScriptArgumentException("Given size: is greater than 15.");
					} else if (size < 1) {
						throw new ScriptArgumentException("Given size: " + args[0] + " cannot be less than 1.");
					}
				} else {
					throw new ScriptArgumentException("Given argument: " + args[0] + " is not an integer.");
				}
			}
		}
		
		runner = new Thread() {
			@Override
			public void run() {
				try {
					refreshTime = System.currentTimeMillis();
					int i = 0;
					while (i < 3 && !globalKill.get() || !kill.get()) {
						if (System.currentTimeMillis() - refreshTime > 100) {
							if (i == 2) {
								break;
							}
							if (i == 0) {
								Minecraft.getMinecraft().thePlayer.sendChatMessage("//br sphere 35%31:1,65%0 " + size);							
							} else if (i == 1) {
								Minecraft.getMinecraft().thePlayer.sendChatMessage("//mask >grass");
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