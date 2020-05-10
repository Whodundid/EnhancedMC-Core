/*package com.Whodundid.scripts.builtInScripts;


import com.Whodundid.main.global.subMod.RegisteredSubMods;
import com.Whodundid.main.global.subMod.SubModType;
import com.Whodundid.main.util.miscUtil.ChatBuilder;
import com.Whodundid.scripts.ScriptManager;
import com.Whodundid.scripts.scriptBase.Script;
import net.minecraft.client.Minecraft;
import net.minecraft.util.EnumChatFormatting;

public class Script_Rotate360 extends Script {
	
	static ScriptManager scriptMan = (ScriptManager) RegisteredSubMods.getMod(SubModType.SCRIPTS);
	static volatile boolean running = false;
	
	public Script_Rotate360() {
		ScriptName = "Rotate360";
	}
	
	public void startScript(String[] args) {
		if (!running && scriptMan.isEnabled()) {
			Thread runner = new Thread() {
				@Override
				public void run() {
					try {
						running = true;
						mc.thePlayer.addChatMessage(ChatBuilder.of(EnumChatFormatting.GOLD + "Script: " + EnumChatFormatting.WHITE + ScriptName + EnumChatFormatting.GREEN + " started.").build());
						refreshTime = System.currentTimeMillis();
						int i = 0;
						while (!scriptMan.getGlobalKill() && !kill.get()) {
							if (i >= 8) {
								break;
							}
							if (System.currentTimeMillis() - refreshTime >= 100) {
								if ((i % 2) == 1) {
									Minecraft.getMinecraft().thePlayer.sendChatMessage("//paste");
									i++;
								} else {
									Minecraft.getMinecraft().thePlayer.sendChatMessage("//rotate 90");
									i++;
								}
								refreshTime = System.currentTimeMillis();
							}
						}
					} catch (Exception e) { e.printStackTrace(); }
					mc.thePlayer.addChatMessage(ChatBuilder.of(EnumChatFormatting.GOLD + "Script: " + EnumChatFormatting.WHITE + ScriptName +
							   "_" + scriptID + EnumChatFormatting.RED + " stopped.").build());
					running = false;
				}
			};
			runner.start();
		} else {
			mc.thePlayer.addChatMessage(ChatBuilder.of("An instance of: " + ScriptName + " is currently running!").build());
		}
	}
}
*/