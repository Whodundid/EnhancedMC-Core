/*package com.Whodundid.scripts.builtInScripts;

import java.text.DecimalFormat;
import com.Whodundid.main.util.miscUtil.ChatBuilder;
import com.Whodundid.scripts.ScriptManager;
import com.Whodundid.scripts.scriptBase.Script;
import com.Whodundid.scripts.scriptUtil.Exceptions.ScriptException.ScriptArgumentException;
import net.minecraft.client.Minecraft;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.world.biome.BiomeGenBase;

public class Script_BiomePaste extends Script {
	
	public Script_BiomePaste() {
		this.ScriptName = "BiomePaste";
	}

	public void startScript(String[] args) throws ScriptArgumentException {
		/*
		runner = new Thread() {
			@Override
			public void run() {
				try {
					BiomeGenBase[][] dimensions = Script_BiomeCopy.getDimensions();
					double area = Script_BiomeCopy.getArea();
					int length = Script_BiomeCopy.getLength();
					
					if (dimensions != null) {					
						int curX = (int) Math.floor(mc.thePlayer.posX);
						int curY = (int) Math.floor(mc.thePlayer.posY);
						int curZ = (int) Math.floor(mc.thePlayer.posZ);
						int posX = 0;
						int posZ = 0;
						int startPosX = curX;
						
						boolean pos1Set = false;
						boolean pos2Set = false;
						boolean biomeSet = false;
						
						refreshTime = System.currentTimeMillis();
						double i = 0;
						while (!globalKill.get() && !kill.get()) {
							if (i >= area) {
								mc.thePlayer.sendChatMessage("/warp debugZone");
								Minecraft.getMinecraft().thePlayer.addChatMessage(ChatBuilder.of(EnumChatFormatting.GOLD + "Script: " + EnumChatFormatting.WHITE + getScriptName() + EnumChatFormatting.RED + " finished.").build());
								break;
							}
							if (System.currentTimeMillis() - refreshTime >= 100) {
								if (!pos2Set) {
									if (!pos1Set) {
										mc.thePlayer.sendChatMessage("//pos1");
										pos1Set = true;
									} else {
										mc.thePlayer.sendChatMessage("//pos2");
										pos2Set = true;
									}
								} else {
									if (!biomeSet) {
										String correct = "";
										String biomeName = dimensions[posX][posZ].biomeName;
										correct = biomeName.replace(" ", "_");
										mc.thePlayer.sendChatMessage("//setbiome " + correct);
										biomeSet = true;
									} else {
										if (posX >= (length - 1)) {
											curZ += 1;
											posZ += 1;
											curX = startPosX;
											posX = 0;
										} else {
											curX += 1;
											posX += 1;
										}
										if (i != (area - 1)) {
											mc.thePlayer.sendChatMessage("/tp " + curX + " " + curY + " " + curZ);
										}
										double result = ((i + 1)/area)*100;									
										mc.thePlayer.addChatMessage(ChatBuilder.of(new DecimalFormat("#.#").format(result) + "% pasted").build());
										pos1Set = false;
										pos2Set = false;
										biomeSet = false;
										i++;
									}
								}
								refreshTime = System.currentTimeMillis();					
							}
						}
						mc.thePlayer.sendChatMessage("/back");
					} else {
						mc.thePlayer.addChatMessage(ChatBuilder.of("Nothing in biome copy clipboard, select a region first").build());
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