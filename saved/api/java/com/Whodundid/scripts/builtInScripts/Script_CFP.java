/*package com.Whodundid.scripts.builtInScripts;

import com.Whodundid.main.util.miscUtil.ChatBuilder;
import com.Whodundid.scripts.ScriptManager;
import com.Whodundid.scripts.scriptBase.Script;
import com.Whodundid.scripts.scriptUtil.Exceptions.ScriptException.ScriptArgumentException;
import net.minecraft.util.EnumChatFormatting;

public class Script_CFP extends Script {
	
	private boolean minusA = false;
	
	@Override
	public void initScript() {
		ScriptName = "Script_CopyFlipPaste";
	}
	
	public void startScript(String[] args) throws ScriptArgumentException {
		/*
		mc.thePlayer.addChatMessage(ChatBuilder.of(EnumChatFormatting.GOLD + "Script: " + EnumChatFormatting.WHITE + this.ScriptName + EnumChatFormatting.GREEN + " started.").build());
		if (args.length > 0) {
			if (args[0].equals("-a")) {
				minusA = true;
			} else { throw new ScriptArgumentException("Unrecognized argument: " + args[0]); }
		}
		
		runner = new Thread() {
			@Override
			public void run() {
				try {
					int i = 0;
					refreshTime = System.currentTimeMillis();
					while (i < 2 && !globalKill.get() && !kill.get()) {
						if (System.currentTimeMillis() - refreshTime >= 100) {
							if (i == 0) {
								mc.thePlayer.sendChatMessage("//copy");
							} 
							else if (i == 1) {
								mc.thePlayer.sendChatMessage("//flip");
							}
							else {
								if (minusA) {
									mc.thePlayer.sendChatMessage("//paste -a");
								} else {
									mc.thePlayer.sendChatMessage("//paste");
								}
							}
							refreshTime = System.currentTimeMillis();
						}
						i++;
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