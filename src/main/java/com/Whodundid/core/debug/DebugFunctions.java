package com.Whodundid.core.debug;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.concurrent.atomic.AtomicInteger;
import javax.imageio.ImageIO;

import org.apache.logging.log4j.Level;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import com.Whodundid.core.EnhancedMC;
import com.Whodundid.core.enhancedGui.types.EnhancedGui;
import com.Whodundid.core.enhancedGui.types.interfaces.IEnhancedGuiObject;
import com.Whodundid.core.subMod.RegisteredSubMods;
import com.Whodundid.core.subMod.SubMod;
import com.Whodundid.core.subMod.SubModType;
import com.Whodundid.core.util.mathUtil.HexMath;
import com.Whodundid.core.util.mathUtil.NumberUtil;
import com.Whodundid.core.util.playerUtil.Direction;
import com.Whodundid.core.util.playerUtil.PlayerFacing;
import com.Whodundid.core.util.playerUtil.PlayerMovement;
import com.Whodundid.core.util.storageUtil.StorageBox;
import com.Whodundid.enhancedChat.EnhancedChatMod;
import com.Whodundid.miniMap.MiniMapMod;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiDisconnected;
import net.minecraft.client.multiplayer.GuiConnecting;
import net.minecraft.client.multiplayer.ServerAddress;
import net.minecraft.client.multiplayer.ServerData;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.client.network.NetHandlerLoginClient;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.network.EnumConnectionState;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.handshake.client.C00Handshake;
import net.minecraft.network.login.client.C00PacketLoginStart;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.ModContainer;

//Last edited: 12-12-18
//First Added: 9-14-18
//Author: Hunter Bragg

public class DebugFunctions {

	static Minecraft mc = Minecraft.getMinecraft();

	public static void runDebugFunction(IDebugCommand function) {
		runDebugFunction(function.getDebugCommandID());
	}

	public static boolean runDebugFunction(int functionID) {
		try {
			switch (functionID) {
			case 0: debug_0(); return true;
			case 1: debug_1(); return true;
			case 2: debug_2(); return true;
			case 3: debug_3(); return true;
			}
		} catch (Throwable e) { e.printStackTrace(); }
		return false;
	}

	private static void debug_0() throws Throwable {
		EnhancedChatMod modd = (EnhancedChatMod) RegisteredSubMods.getMod(SubModType.ENHANCEDCHAT);
		modd.msgLengthLimit = 1500;
		//System.out.println("here " + (PlayerFacing.isFacingPlayer() ? PlayerFacing.getFacingPlayerName() : "no one"));
		//System.out.println(" " + mc.ingameGUI);
		
		/*
		if (RegisteredSubMods.isModRegEn(SubModType.MINIMAP)) {
			MiniMapMod mapMod = (MiniMapMod) RegisteredSubMods.getMod(SubModType.MINIMAP);
			mapMod.findPlayer = "";
			if (PlayerFacing.isFacingPlayer()) {
				mapMod.findPlayer = PlayerFacing.getFacingPlayerName();
			}
		}
		*/
		
		EnhancedMC.getRenderer().reInitObjects();
		
		/*
		Thread t = new Thread() {
			@Override
			public void run() {
				try {
					float start = mc.thePlayer.prevRotationYaw;
					float diff = PlayerFacing.getCompassFacingDir().getDegree() - start;
					for (int i = 0; i < 10; i++) {
						Thread.sleep(200 / 10);
						PlayerFacing.setFacingDir(PlayerFacing.getDegreeFacingDir() + diff / 10);
					}
					Thread.sleep(50);
					
					PlayerMovement.pressMovementKey(Direction.N);
					PlayerMovement.setSprinting();
					
					Thread.sleep(100);
					
					for (int i = 0; i < 10; i++) {
						Thread.sleep(120 / 10);
						if (i == 4) { PlayerMovement.setJumping(); }
						PlayerFacing.setFacingDir(PlayerFacing.getDegreeFacingDir() + (12.5f / 10));
					}
					
					Thread.sleep(80);
					
					for (int i = 0; i < 20; i++) {
						Thread.sleep(100 / 20);
						PlayerFacing.setFacingDir(PlayerFacing.getDegreeFacingDir() + (-60.5f / 20));
					}
					
					Thread.sleep(20);
					
					for (int i = 0; i < 100; i++) {
						Thread.sleep(100 / 25);
						PlayerFacing.setFacingDir(PlayerFacing.getDegreeFacingDir() + (48.0f / 100));
						if (i == 40) {
							PlayerMovement.setSprinting(false);
							PlayerMovement.setJumping(false);
						}
						if (i == 45) {
							PlayerMovement.unpressMovementKey(Direction.N);
							PlayerMovement.setSneaking();
						}
					}
					PlayerMovement.setSneaking(false);
				}
				catch (InterruptedException e) { e.printStackTrace(); }
			}
		};
		t.start();
		*/
		
		//System.out.println(RegisteredSubMods.getMod(SubModType.HOTKEYS).isEnabled());
		/*
		System.out.println();
		
		if (mc.currentScreen instanceof EnhancedGui) {
			EnhancedGui g = (EnhancedGui) mc.currentScreen;
			
			for (IEnhancedGuiObject o : g.getAllChildren()) {
				System.out.println(o.getClass().getTypeName());
			}
		}
		
		System.out.println();
		*/
		
		//System.out.println(RegisteredSubMods.getMod(SubModType.PARKOUR).getMainGui(false, null, null));
		//RegisteredSubMods.unregisterSubMod(RegisteredSubMods.getMod(SubModType.PARKOUR));
		//RegisteredSubMods.registerSubMod(new ParkourMod());
		//PlayerFacing.setFacingDir(PlayerFacing.getCompassFacingDir());
		//System.out.println(RegisteredSubMods.isModRegistered(SubModType.PARKOUR));
		//Display.setTitle("Alt Acc");
        
		//mc.getNetHandler().getNetworkManager().
		//mc.displayGuiScreen(null);
		//RegisteredSubMods.getMod(SubModType.CLEARVISUALS).getConfig().loadMainConfig();
		//EnhancedChatMod chatMod = (EnhancedChatMod) RegisteredSubMods.getMod(SubModType.ENHANCEDCHAT);
		//System.out.println(chatMod.getChatOrganizer().getFilterHistoryList("Lobby"));
		//chatMod.getChatOrganizer().loadKeyWordLists();
		//CommandNotFoundEvent event = new CommandNotFoundEvent("lulw");
		//net.minecraftforge.common.MinecraftForge.EVENT_BUS.post(event);
		//System.out.println("test");
		//ChatComponentTranslation ret = new ChatComponentTranslation("commands.generic.notFound");
        //ret.getChatStyle().setColor(EnumChatFormatting.RED);
		//if (mc.thePlayer != null) { mc.thePlayer.addChatMessage(ret); }
		// GlobalSettings.loadConfig();
		//EnhancedMC.getRenderManager().getRenderer().reInitObjects();
		// boolean state = Mouse.isGrabbed();
		// Mouse.setGrabbed(!state);
		// if (state) { mc.setIngameNotInFocus(); }
		// else { mc.setIngameFocus(); }
		// EScript s = FileParser.parseFile(new File("EnhancedMC/test.txt"));
		// ScriptManager scriptMan = (ScriptManager)
		// RegisteredSubMods.getMod(SubModType.SCRIPTS);
		// scriptMan.getScriptRunner().tryStartScript(s, null);
		// MainMod.getInGameGui().getImmediateChildren().clear();
		// MainMod.getInGameGui().reInitObjects();
		// ClientCommandHandler h = ClientCommandHandler.instance;
		// System.out.println(h.getCommands().keySet());
		// AutoCorrectManager ac = (AutoCorrectManager)
		// RegisteredSubMods.getMod(SubModType.AUTOCORRECT);
		// ac.init();
		// ac.loader.loadCommands();
		// ac.registerCommands();
		/*
		 * for (AutoCorrectCommand c : ac.getCommandList()) {
		 * System.out.println(c.getBaseCommand()); for (String a : c.getAliases()) {
		 * System.out.println("    -" + a); } } //final String OVERFLOW =
		 * Stream.empty().map(m -> m) + ""; //System.out.println(OVERFLOW);
		 * //HotKeyManager man = (HotKeyManager)
		 * RegisteredSubMods.getMod(SubModType.HOTKEYS); //man.loadHotKeys();
		 * //man.saveHotKeys(); //mc.displayGuiScreen(new HotKeyCreationStepByStep());
		 * //EnhancedChat chatMod = (EnhancedChat)
		 * RegisteredSubMods.getMod(SubModType.ENHANCEDCHAT);
		 * //chatMod.getChatOrganizer().printOutContents();
		 * //ConfigMaker.addSpawnPoint(); //EnhancedInGameGui guiMain =
		 * MainMod.getInGameGui(); //for (IEnhancedGuiObject o : guiMain.getObjects()) {
		 * // guiMain.removeObject(o); //}
		 * 
		 * //EGuiLabel label = new EGuiLabel(guiMain, 50, 50,
		 * "The quick brown fox jumped over the incredibly large boulder at an impressive 32.04 kph."
		 * ); //label.setDrawCentered(true).enableShadow(false).enableWordWrap(true,
		 * 190); //guiMain.addObject(label); //guiMain.addObject(new
		 * EGuiLinkConfirmationDialogueBox(guiMain, "https://www.google.com"));
		 * 
		 * //ClearVisuals visuals = (ClearVisuals)
		 * RegisteredSubMods.getMod(SubModType.CLEARVISUALS);
		 * //visuals.getConfig().loadConfig(); //DisplayMode m = new DisplayMode(1918,
		 * 1013); //Display.setDisplayMode(m); //Display.setLocation(-2, 0);
		 * //mc.displayGuiScreen(new TestGuiMainMenu()); //MiniMap map = (MiniMap)
		 * RegisteredSubMods.getMod(SubModType.MINIMAP); //map.zoomMiniMap(1);
		 * //System.out.println(mc.thePlayer.motionX + " " + mc.thePlayer.motionZ);
		 * //mc.thePlayer.motionX = 0; //mc.thePlayer.motionZ = 0;
		 * //mc.thePlayer.motionY = 0; //mc.thePlayer.motionX =
		 * mc.thePlayer.getLookVec().xCoord * 0.65; //mc.thePlayer.motionZ =
		 * mc.thePlayer.getLookVec().zCoord * 0.65; //mc.thePlayer.motionY =
		 * mc.thePlayer.getLookVec().yCoord * 0.15; //Mouse.setCursorPosition(50,
		 * Mouse.getY()); //System.out.println("eh"); /*ParkourAI parkour = (ParkourAI)
		 * RegisteredSubMods.getMod(SubModType.PARKOUR); if
		 * (PlayerFacing.getFacingBlockPos() != null) { if (parkour.getHelperBlock() ==
		 * null || !(PlayerFacing.getFacingBlockPos().equals(parkour.getHelperBlock().
		 * getHelperBlockLocation()))) { parkour.setHelperBlock(new
		 * HelperBlock(PlayerFacing.getFacingBlockPos())); } }
		 * parkour.setHelperBlock(null);
		 */
		//mc.displayGuiScreen(new ExperimentGui());
		//mc.displayGuiScreen(new ScreenChatOptions(null, mc.gameSettings).getClass().getConstructor(parameterTypes));
		// mc.displayGuiScreen(new CustomInGameMenu());
		// EArrayList<String> cat = new EArrayList<String>("baccon", "bigBoi");
		// cat.add("baccon", "bigBoi");
		// System.out.println(cat);
		// Ping mod = (Ping) RegisteredSubMods.getMod(SubModType.PING);
		// mod.getConfig().saveConfig(mod.getConfig().getConfigNames().get(0));
		// mod.getConfig().loadConfig(mod.getConfig().getConfigNames().get(0));
		// ChatWindows mod = (ChatWindows)
		// RegisteredSubMods.getMod(SubModType.CHATWINDOWS);
		// mod.openChatWindow(ChatType.ALL);
		// mc.displayGuiScreen(new PingGui());
	}

	private static void debug_1() throws Throwable {
		mc.thePlayer.rotationYaw = PlayerFacing.getCompassFacingDir().getDegree();
		
		
		Thread t = new Thread() {
			@Override
			public void run() {
				try {
					float start = mc.thePlayer.prevRotationYaw;
					float diff = PlayerFacing.getCompassFacingDir().getDegree() - start;
					for (int i = 0; i < 10; i++) {
						Thread.sleep(200 / 10);
						//PlayerFacing.setFacingDir(PlayerFacing.getDegreeFacingDir() + diff / 10);
					}
				}
				catch (InterruptedException e) { e.printStackTrace(); }
			}
		};
		t.start();
		
		/*
		 * for (int i = 0; i < mc.thePlayer.inventory.getSizeInventory(); i++) {
		 * System.out.print(i + ": "); if (mc.thePlayer.inventory.getStackInSlot(i) !=
		 * null) {
		 * System.out.println(mc.thePlayer.inventory.getStackInSlot(i).getDisplayName())
		 * ; } else { System.out.println("Nothing"); }
		 * 
		 * }
		 */
		/*
		try {
			DisplayMode m = new DisplayMode(1918, 1013);
			Display.setDisplayMode(m);
			Display.setLocation(-2, 0);
		} catch (Exception e) {
			e.printStackTrace();
		}
		*/
	}

	private static void debug_2() throws Throwable {
		net.minecraftforge.fml.client.FMLClientHandler.instance().setupServerList();
		ServerData hypixel = new ServerData("Hypixel", "localhost", false);
		ServerAddress serveraddress = ServerAddress.fromString(hypixel.serverIP);
        mc.setServerData(hypixel);
        (new Thread("Server Connector #" + new AtomicInteger(0).incrementAndGet()) {
            public void run() {
                InetAddress inetaddress = null;

                try {
                    inetaddress = InetAddress.getByName(serveraddress.getIP());
                    NetworkManager networkManager = NetworkManager.func_181124_a(inetaddress, serveraddress.getPort(), mc.gameSettings.func_181148_f());
                    networkManager.setNetHandler(new NetHandlerLoginClient(networkManager, mc, null));
                    networkManager.sendPacket(new C00Handshake(47, serveraddress.getIP(), serveraddress.getPort(), EnumConnectionState.LOGIN, true));
                    networkManager.sendPacket(new C00PacketLoginStart(mc.getSession().getProfile()));
                }
                catch (UnknownHostException unknownhostexception) {

                    EnhancedMC.EMCLogger.error("Couldn\'t connect to server", unknownhostexception);
                    mc.displayGuiScreen(new GuiDisconnected(null, "connect.failed", new ChatComponentTranslation("disconnect.genericReason", new Object[] {"Unknown host"})));
                }
                catch (Exception exception) {

                	EnhancedMC.EMCLogger.error("Couldn\'t connect to server", exception);
                    String s = exception.toString();

                    if (inetaddress != null) {
                        String s1 = inetaddress.toString() + ":" + serveraddress.getPort();
                        s = s.replaceAll(s1, "");
                    }

                    mc.displayGuiScreen(new GuiDisconnected(null, "connect.failed", new ChatComponentTranslation("disconnect.genericReason", new Object[] {s})));
                }
            }
        }).start();
        
		//Object[] info = mc.getNetHandler().getPlayerInfoMap().toArray();
		//for (Object o : info) {
		//	if (o instanceof NetworkPlayerInfo) {
		//		NetworkPlayerInfo i = ((NetworkPlayerInfo) o);
		//		System.out.println(i.getGameProfile().getName() + " " + i.getResponseTime());
		//	}
		//}
	}

	/** Used for editor things mainly */
	private static void debug_3() throws Throwable {
		int rad = 1024;
		BufferedImage img = new BufferedImage(rad, rad, BufferedImage.TYPE_INT_RGB);

		// Center Point (MIDDLE, MIDDLE)
		int centerX = img.getWidth() / 2;
		int centerY = img.getHeight() / 2;
		int radius = (img.getWidth() / 2) * (img.getWidth() / 2);

		// Red Source is (RIGHT, MIDDLE)
		int redX = img.getWidth();
		int redY = img.getHeight() / 2;
		int redRad = img.getWidth() * img.getWidth();

		// Green Source is (LEFT, MIDDLE)
		int greenX = 0;
		int greenY = img.getHeight() / 2;
		int greenRad = img.getWidth() * img.getWidth();

		// Blue Source is (MIDDLE, BOTTOM)
		int blueX = img.getWidth() / 2;
		int blueY = img.getHeight();
		int blueRad = img.getWidth() * img.getWidth();

		for (int i = 0; i < img.getWidth(); i++) {
			for (int j = 0; j < img.getHeight(); j++) {
				int a = i - centerX;
				int b = j - centerY;

				int distance = a * a + b * b;
				if (distance < radius) {
					int rdx = i - redX;
					int rdy = j - redY;
					int redDist = (rdx * rdx + rdy * rdy);
					int redVal = (int) (255 - ((redDist / (float) redRad) * 256));

					int gdx = i - greenX;
					int gdy = j - greenY;
					int greenDist = (gdx * gdx + gdy * gdy);
					int greenVal = (int) (255 - ((greenDist / (float) greenRad) * 256));

					int bdx = i - blueX;
					int bdy = j - blueY;
					int blueDist = (bdx * bdx + bdy * bdy);
					int blueVal = (int) (255 - ((blueDist / (float) blueRad) * 256));

					Color c = new Color(redVal, greenVal, blueVal);

					float hsbVals[] = Color.RGBtoHSB(c.getRed(), c.getGreen(), c.getBlue(), null);

					Color highlight = Color.getHSBColor(hsbVals[0], hsbVals[1], 1);

					img.setRGB(i, j, HexMath.RGBtoHEX(highlight));
				} else {
					img.setRGB(i, j, 0xFFFFFF);
				}
			}
		}

		try {
			ImageIO.write(img, "png", new File("wheel.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
