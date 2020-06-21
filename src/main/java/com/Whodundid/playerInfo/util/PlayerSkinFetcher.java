package com.Whodundid.playerInfo.util;

import com.Whodundid.core.terminal.window.ETerminal;
import com.Whodundid.core.util.EUtil;
import com.Whodundid.core.util.chatUtil.ChatBuilder;
import com.Whodundid.core.util.renderUtil.EColors;
import com.Whodundid.core.util.storageUtil.EArrayList;
import com.Whodundid.playerInfo.PlayerInfoApp;
import com.Whodundid.playerInfo.window.PlayerInfoWindow;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import org.apache.commons.codec.binary.Base64;
import net.minecraft.client.Minecraft;
import net.minecraft.util.EnumChatFormatting;

public class PlayerSkinFetcher {
	
	public static Minecraft mc = Minecraft.getMinecraft();
	private static boolean isAlexSkin = false;
	private static boolean hasCape = false;
	private static File lastSkin = null;
	private static File lastCape = null;
	private static String name = "";
	private static String lastUUID;
	
	public static void getSkinPic(PlayerInfoWindow windowIn, ETerminal termIn, String nameIn) {
		Thread getter = new Thread() {
			@Override
			public void run() {
				String uuid = "";
				
				try {
					URL uuidFetcher = new URL("https://api.mojang.com/users/profiles/minecraft/" + nameIn);
					InputStreamReader uuidReader = new InputStreamReader(uuidFetcher.openStream());
					BufferedReader getUuid = new BufferedReader(uuidReader);
					String testUUID = getUuid.readLine();
					if (testUUID != null) {
						int nameIndex = EUtil.findStartingIndex(testUUID, "\"name\":\"") + 8;
						name = EUtil.subStringToString(testUUID, nameIndex, "\"");
						//System.out.println("Name: " + name);
						int uuidIndex = EUtil.findStartingIndex(testUUID, "\"id\":\"") + 6;
						uuid = testUUID.substring(uuidIndex, testUUID.length() - 2);
					}
					getUuid.close();
					
					//System.out.println("the uuid: " + uuid);
				}
				catch (IOException e) {  e.printStackTrace(); }
				
				if (uuid != null && !uuid.isEmpty()) {
					try {
						//get the UUID of player name
						URL skinFetcher = new URL("https://sessionserver.mojang.com/session/minecraft/profile/" + uuid);
						InputStreamReader skinReader = new InputStreamReader(skinFetcher.openStream());
						BufferedReader reader = new BufferedReader(skinReader);
						String skin = "";
						EArrayList<String> lines = reader.lines().collect(EArrayList.toEArrayList());
						for (String s : lines) { skin += s; }
						skin = skin.trim().replace(" ", "");
						
						//locate the texture path index
						int skinIndex = EUtil.findStartingIndex(skin, "[{\"name\":\"textures\",\"value\":\"") + 29;
						
						//decode the texture from base 64
						byte[] skinBase64 = Base64.decodeBase64(skin.substring(skinIndex, skin.length() - 5));
						String nextPart = new String(skinBase64);
						nextPart = nextPart.trim().replace(" ", "").replace("\n", "");
						
						//check if the skin is an alex model or not
						isAlexSkin = nextPart.contains(":{\"model\":\"slim\"}");
						//System.out.println(nextPart);
						//System.out.println(isAlexSkin);
						
						//find indexes for textures
						int textureIndex = EUtil.findStartingIndex(nextPart, ":{\"SKIN\":{\"url\":\"") + 17;
						int capeIndex = EUtil.findStartingIndex(nextPart, "\"CAPE\":{\"url\":\"") + 15;
						
						//get skin texture path
						String texURL = nextPart.substring(textureIndex);
						
						//System.out.println("tex url half: " + texURL);
						String texUrl = texURL.substring(0, EUtil.findStartingIndex(texURL, "\""));
						
						//read skin texture from path
						URL skinImgFetcher = new URL(texUrl);
						InputStream skinStream = new BufferedInputStream(skinImgFetcher.openStream());
						ByteArrayOutputStream skinOut = new ByteArrayOutputStream();
						byte[] skinBuf = new byte[1024];
						int n = 0;
						while (-1 != (n = skinStream.read(skinBuf))) {
						   skinOut.write(skinBuf, 0, n);
						}
						skinOut.close();
						skinStream.close();
						byte[] skinResponse = skinOut.toByteArray();
						
						FileOutputStream fos = new FileOutputStream(nameIn + "_Skin.png");
						fos.write(skinResponse);
						fos.close();
						
						//System.out.println(capeIndex);
						
						//cape stuff
						if (hasCape = capeIndex >= 15) {
							//get cape texture path
							String capeURL = nextPart.substring(capeIndex);
							//System.out.println("cape url half: " + capeURL);
							String capeUrl = capeURL.substring(0, EUtil.findStartingIndex(capeURL, "\""));
							//System.out.println("cape url: " + capeUrl);
							
							//read cape texture from path
							URL capeImgFetcher = new URL(capeUrl);
							InputStream capeStream = new BufferedInputStream(capeImgFetcher.openStream());
							ByteArrayOutputStream capeOut = new ByteArrayOutputStream();
							byte[] capeBuf = new byte[1024];
							int i = 0;
							while (-1 != (i = capeStream.read(capeBuf))) {
							   capeOut.write(capeBuf, 0, i);
							}
							capeOut.close();
							capeStream.close();
							byte[] capeResponse = capeOut.toByteArray();
							
							FileOutputStream cos = new FileOutputStream(nameIn + "_Cape.png");
							cos.write(capeResponse);
							cos.close();
						}
						
						lastSkin = new File(nameIn + "_Skin.png");
						lastCape = new File(nameIn + "_Cape.png");
						//name = nameIn;
						lastUUID = uuid;
						
						onResponse(windowIn, true);
					}
					catch (StringIndexOutOfBoundsException e) {
						if (termIn != null) {
							termIn.writeln("No player with that name found.", EColors.cyan);
						}
						else {
							if (mc.thePlayer != null) {
								mc.thePlayer.addChatMessage(ChatBuilder.of(EnumChatFormatting.AQUA + "No player with that name found.").build());
							}
						}
						e.printStackTrace();
					}
					catch (IOException e) {
						if (termIn != null) {
							termIn.error(e.toString());
						}
						else {
							if (mc.thePlayer != null) {
								mc.thePlayer.addChatMessage(ChatBuilder.of(EnumChatFormatting.RED + "Player Skin Fetcher: " + e.toString()).build());
							}
						}
						e.printStackTrace();
					}
					catch (Exception e) {
						if (termIn != null) {
							termIn.error(e.toString());
						}
						if (mc.thePlayer != null) {
							mc.thePlayer.addChatMessage(ChatBuilder.of(EnumChatFormatting.RED + "Player Skin Fetcher: " + e.toString()).build());
						}
						onResponse(windowIn, false);
						e.printStackTrace();
					}
				}
				else {
					if (termIn != null) { termIn.writeln("No player with that name found.", EColors.cyan); }
					else { mc.thePlayer.addChatMessage(ChatBuilder.of(EnumChatFormatting.AQUA + "No player with that name found.").build()); }
				}
			}
		};
		getter.start();
	}
	
	private static void onResponse(PlayerInfoWindow windowIn, boolean pass) {
		if (pass) {
			try {
				if (lastSkin != null && lastSkin.exists()) {
					PlayerInfoApp.container = new SkinContainer(name, lastSkin, lastCape, isAlexSkin, hasCape, lastUUID);
					PlayerInfoApp.window = windowIn;
					PlayerInfoApp.received = true;
				}
			}
			catch (Exception e) { e.printStackTrace(); }
		}
	}
	
}