package com.Whodundid.scripts.builtInScripts;

public class biomeCopy {
	/*
	
	{
		ScriptName = "biomeCopy";
	}
	
	static BiomeGenBase[][] dimensions;
	static Minecraft mc = Minecraft.getMinecraft();
	static double area = 0;
	static int length = 0;
	static int width = 0;
	
	public static void clearSavedBiome() {
		if (dimensions != null) {
			dimensions = null;
			Minecraft.getMinecraft().thePlayer.addChatMessage(ChatBuilder.of(EnumChatFormatting.RED + "Biome copy clipboard cleared").build());
		} else {
			Minecraft.getMinecraft().thePlayer.addChatMessage(ChatBuilder.of(EnumChatFormatting.WHITE + "Nothing in biome copy clipboard").build());
		}
	}
	
	public static void boimeCopyScript() {
		if (!running) {
			CurrentScript.setCurrentlyRunningScript(biomeCopy.class);
			Thread Script = new Thread() {
				public void run() {
					if (EventListener.posSet()) {
						running = true;
						kill.set(false);
						Vector3D pos1 = EventListener.getPos1();
						Vector3D pos2 = EventListener.getPos2();
						
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
						width = (int) Math.abs(pos1.getZ() - pos2.getZ()) + 1;

						area = (Math.abs(pos1.getX() - pos2.getX()) + 1) * (Math.abs(pos1.getZ() - pos2.getZ()) + 1);
						
						long startTime = System.currentTimeMillis();
						boolean pause = false;
						double i = 0;
						while (!kill.get()) {
							if (i == area) {
								Minecraft.getMinecraft().thePlayer.addChatMessage(ChatBuilder.of(EnumChatFormatting.GOLD + "Script: " + EnumChatFormatting.WHITE + CurrentScript.getCurrentlyRunningScript().getSimpleName() + EnumChatFormatting.RED + " finished.").build());
								kill.set(true);
								break;
							}
							if (!pause) {
								if (System.currentTimeMillis() - startTime >= 100) {
									mc.thePlayer.sendChatMessage("/tp " + (int) curX + " " + (int) (pos1.getY() + 1) + " " + (int) curZ);
									startTime = System.currentTimeMillis();
									pause = true;
								}
							} else {
								if (System.currentTimeMillis() - startTime >= 100) {
									BlockPos blockpos = new BlockPos(mc.getRenderViewEntity().posX, mc.getRenderViewEntity().getEntityBoundingBox().minY, mc.getRenderViewEntity().posZ);
									Chunk chunk = mc.theWorld.getChunkFromBlockCoords(blockpos);
									try {
										dimensions[posX][posZ] = chunk.getBiome(blockpos, mc.theWorld.getWorldChunkManager());
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
									startTime = System.currentTimeMillis();
									i++;
									pause = false;
								}
							}
						}
						running = false;
					} else {
						mc.thePlayer.addChatMessage(ChatBuilder.of("World edit region not yet selected, select a region first").build());
					}
					CurrentScript.removeCurrentlyRunningScript();
				}
			};
			Script.start();
		} else {
			Minecraft.getMinecraft().thePlayer.addChatMessage(ChatBuilder.of(EnumChatFormatting.RED + "ALREADY RUNNING").build());
		}
	}
	
	public static void pasteBiome() {
		if (!running) {
			Thread Script = new Thread() {
				public void run() {
					CurrentScript.setCurrentlyRunningScript(biomeCopy.class);
					try {
						if (dimensions != null) {
							kill.set(false);
							
							int curX = (int) Math.floor(mc.thePlayer.posX);
							int curY = (int) Math.floor(mc.thePlayer.posY);
							int curZ = (int) Math.floor(mc.thePlayer.posZ);
							int posX = 0;
							int posZ = 0;
							int startPosX = curX;
							
							boolean pos1Set = false;
							boolean pos2Set = false;
							boolean biomeSet = false;
							
							long startTime = System.currentTimeMillis();
							double i = 0;
							while (!kill.get()) {
								if (i >= area) {
									kill.set(true);
									mc.thePlayer.sendChatMessage("/warp debugZone");
									Minecraft.getMinecraft().thePlayer.addChatMessage(ChatBuilder.of(EnumChatFormatting.GOLD + "Script: " + EnumChatFormatting.WHITE + CurrentScript.getCurrentlyRunningScript().getSimpleName() + EnumChatFormatting.RED + " finished.").build());
									break;
								}
								if (System.currentTimeMillis() - startTime >= 100) {
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
									startTime = System.currentTimeMillis();					
								}
							}
							mc.thePlayer.sendChatMessage("/back");
						} else {
							mc.thePlayer.addChatMessage(ChatBuilder.of("Nothing in biome copy clipboard, select a region first").build());
						}
					} catch (Exception e) { e.printStackTrace(); }
					CurrentScript.removeCurrentlyRunningScript();
				}
			};
			Script.start();
		} else {
			Minecraft.getMinecraft().thePlayer.addChatMessage(ChatBuilder.of(EnumChatFormatting.RED + "ALREADY RUNNING").build());
		}
	}
	
	*/
}