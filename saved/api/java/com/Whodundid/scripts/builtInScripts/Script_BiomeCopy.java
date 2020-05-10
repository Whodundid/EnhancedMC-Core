/*package com.Whodundid.scripts.builtInScripts;

import java.text.DecimalFormat;
import com.Whodundid.main.MainMod;
import com.Whodundid.main.util.miscUtil.ChatBuilder;
import com.Whodundid.main.util.miscUtil.WorldEditListener;
import com.Whodundid.main.util.storageUtil.Vector3D;
import com.Whodundid.scripts.EScript;
import com.Whodundid.scripts.ScriptManager;
import com.Whodundid.scripts.scriptUtil.Exceptions.ScriptException.ScriptArgumentException;
import net.minecraft.client.Minecraft;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.chunk.Chunk;

public class Script_BiomeCopy extends EScript {

	static BiomeGenBase[][] dimensions;
	static double area = 0;
	static int length = 0;
	
	public Script_BiomeCopy() {
		name = "BiomeCopy";
	}
	
	public void startScript(String[] args) throws ScriptArgumentException {
		/*
		 Thread runner = new Thread() {
			@Override
			public void run() {
				try {
					if (WorldEditListener.posSet()) {
						Vector3D pos1 = WorldEditListener.getPos1();
						Vector3D pos2 = WorldEditListener.getPos2();
						
						double curX = 0;
						double curZ = 0;
						int posX = 0;
						int posZ = 0;
						boolean lowerX = false;
									
						if (pos1.getX() > pos2.getX()) {
							curX = pos2.getX();
						} else {
							lowerX = true;
							curX = pos1.getX();
						}
						if (pos1.getZ() > pos2.getZ()) {
							curZ = pos2.getZ();
						} else {
							curZ = pos1.getZ();
						}
						
						dimensions = new BiomeGenBase[(int) (Math.abs(pos1.getX() - pos2.getX()) + 1)][(int) (Math.abs(pos1.getZ() - pos2.getZ()) + 1)];
						length = (int) Math.abs(pos1.getX() - pos2.getX()) + 1;
						area = (Math.abs(pos1.getX() - pos2.getX()) + 1) * (Math.abs(pos1.getZ() - pos2.getZ()) + 1);
						
						refreshTime = System.currentTimeMillis();
						double i = 0;
						while (!MainMod.getScriptMan().globalKill.get() && !kill.get()) {
							if (i == area) {
								break;
							}
							if (System.currentTimeMillis() - refreshTime >= 100) {
								try {
									dimensions[posX][posZ] = mc.theWorld.getBiomeGenForCoords(new BlockPos(curX, 1, curZ));
								} catch (Exception e) { e.printStackTrace(); }
								if (lowerX) {
									if (curX >= (pos2.getX())) {
										curX = pos1.getX();
										curZ += 1;
										posZ += 1;
										posX = 0;
									} else {
										curX += 1;
										posX += 1;
									}
								} else {
									if (curX >= (pos1.getX())) {
										curX = pos2.getX();
										curZ += 1;
										posZ += 1;
										posX = 0;
									} else {
										curX += 1;
										posX += 1;
									}
								}
								double result = ((i + 1)/area)*100;
								mc.thePlayer.addChatMessage(ChatBuilder.of(new DecimalFormat("#.#").format(result) + "% copied").build());
								refreshTime = System.currentTimeMillis();
								i++;
							}
						}
					}
					//ScriptManager.removeRunningScript(scriptID);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}		
		};
		//runner.start();
		
	}
	
	public static BiomeGenBase[][] getDimensions() { return dimensions; }
	public static double getArea() { return area; }
	public static int getLength() { return length; }
	
	public static void clearSavedBiome() {
		if (dimensions != null) {
			dimensions = null;
			Minecraft.getMinecraft().thePlayer.addChatMessage(ChatBuilder.of(EnumChatFormatting.RED + "Biome copy clipboard cleared").build());
		} else {
			Minecraft.getMinecraft().thePlayer.addChatMessage(ChatBuilder.of(EnumChatFormatting.WHITE + "Nothing in biome copy clipboard").build());
		}
	}
	
}
*/