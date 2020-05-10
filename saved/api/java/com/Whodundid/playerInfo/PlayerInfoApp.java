package com.Whodundid.playerInfo;

import net.minecraft.client.settings.KeyBinding;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.client.ClientCommandHandler;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent.KeyInputEvent;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import org.lwjgl.input.Keyboard;
import com.Whodundid.core.EnhancedMC;
import com.Whodundid.core.app.AppType;
import com.Whodundid.core.app.EMCApp;
import com.Whodundid.core.terminal.gui.ETerminal;
import com.Whodundid.core.util.EUtil;
import com.Whodundid.core.util.chatUtil.ChatBuilder;
import com.Whodundid.core.util.playerUtil.PlayerFacing;
import com.Whodundid.playerInfo.cmd.CMD_CheckNameHistory;
import com.Whodundid.playerInfo.cmd.CMD_CheckPlayerSkin;
import com.Whodundid.playerInfo.gui.SkinDisplayWindow;
import com.Whodundid.playerInfo.term.Term_NameHistory;
import com.Whodundid.playerInfo.term.Term_PlayerSkin;
import com.Whodundid.playerInfo.util.PlayerSkinFetcher;
import com.Whodundid.playerInfo.util.SkinContainer;

@Mod(modid = PlayerInfoApp.MODID, version = PlayerInfoApp.VERSION, name = PlayerInfoApp.NAME, dependencies = "required-after:enhancedmc")
public final class PlayerInfoApp extends EMCApp {

	public static final String MODID = "playerinfo";
	public static final String VERSION = "2.0";
	public static final String NAME = "EMC Player Info";
	
	private ArrayList<String> preSorted = new ArrayList<String>();
	private ArrayList<String> pastNames = new ArrayList<String>();
	private ArrayList<String> changeDates = new ArrayList<String>();
	private int startPos = 0;
	public String uuid = null;
	public String curName = "";
	public static final KeyBinding nameChecker = new KeyBinding("Fetch Name History", Keyboard.KEY_N, "EnhancedMC");
	
	public static long timeSinceLast = 0l;
	public static boolean received = false;
	public static SkinContainer container = null;

	//--------------------------
	//PlayerInfoApp Constructors
	//--------------------------
	
	public PlayerInfoApp() {
		super(AppType.PLAYERINFO);
		version = VERSION;
		author = "Whodundid";
		
		addDependency(AppType.CORE, "1.0");
		addGui(new SkinDisplayWindow("", ""));
		
		setAliases("playerinfo", "pinfo", "pi");
	}

	@Override
	public void onPostInit(FMLPostInitializationEvent event) {
		ClientRegistry.registerKeyBinding(nameChecker);
		ClientCommandHandler.instance.registerCommand(new CMD_CheckNameHistory(this));
		ClientCommandHandler.instance.registerCommand(new CMD_CheckPlayerSkin(this));
	}

	@Override
	public void keyEvent(KeyInputEvent e) {
		if (nameChecker.isPressed()) {
			if (PlayerFacing.isFacingPlayer()) {
				fetchNameHistory(null, true);
			}
		}
	}

	@Override
	public Object sendArgs(Object... args) {
		if (isEnabled()) {
			if (args.length == 2) {
				if (args[0] instanceof String && args[1] instanceof String) {
					String cmd = (String) args[0];
					String player = (String) args[1];

					switch (cmd) {
					case "PlayerInfo: fetch names": fetchNameHistory(player, false); break;
					case "PlayerInfo: fetch skin": fetchSkin(null, player); break;
					default: break;
					}
				}
			}
		}
		return null;
	}
	
	@Override
	public void renderLastWorldEvent(RenderWorldLastEvent e) {
		if (received) {
			
			if (container != null) {
				try {
					//create the skin window
					SkinDisplayWindow displayer = new SkinDisplayWindow(container.getUUID(), container.getName());
					displayer.setSkin(container.getSkinImage(), container.getCapeImage(), container.isAlex(), container.hasCape());
					
					//display skin window
					EnhancedMC.displayWindow(displayer);
				}
				catch (Exception q) { q.printStackTrace(); }
			}
			
			received = false;
		}
	}
	
	@Override
	public void terminalRegisterCommandEvent(ETerminal conIn, boolean runVisually) {
		EnhancedMC.getTerminalHandler().registerCommand(new Term_NameHistory(), conIn, runVisually);
		EnhancedMC.getTerminalHandler().registerCommand(new Term_PlayerSkin(), conIn, runVisually);
	}
	
	//---------------------
	//PlayerInfoApp Methods
	//---------------------

	public void fetchNameHistory(String playerName, boolean isFacing) { fetchNameHistory(null, playerName, isFacing); }
	public void fetchNameHistory(ETerminal termIn, String playerName, boolean isFacing) {
		if (isEnabled()) {
			Thread fetcher = new Thread() {
				@Override
				public void run() {
					try {
						String name = PlayerFacing.getFacingPlayerName();
						if (isFacing) { fetchHistory(termIn, name); }
						else { fetchHistory(termIn, playerName); }
						displayPlayerNameHistory(termIn, isFacing ? name : curName);
					}
					catch (Exception e) { e.printStackTrace(); }
				}
			};
			fetcher.start();
		}
	}
	
	public void fetchSkin(ETerminal termIn, String playerName) {
		if (isEnabled()) {
			PlayerSkinFetcher.getSkinPic(termIn, playerName);
		}
		else if (termIn != null) { termIn.error("PlayerInfo App is disabled! Enable it to use."); }
		else if (mc.thePlayer != null) { mc.thePlayer.addChatMessage(ChatBuilder.of(EnumChatFormatting.RED + "PlayerInfo App is disabled! Enable to use.").build()); }
	}
	
	//------------------------------
	//PlayerInfoApp Internal Methods
	//------------------------------

	private void displayPlayerNameHistory(ETerminal termIn, String originalName) {
		if (termIn != null) {
			if (!pastNames.isEmpty() && !changeDates.isEmpty()) {
				if (pastNames.size() > 1) {
					String result = "";
					result += EnumChatFormatting.GOLD + "" + originalName + "'s " + EnumChatFormatting.GREEN + "previous name history:\n";
					result += EnumChatFormatting.AQUA + "-----------------------------------------------------\n";
					for (int i = 0; i < pastNames.size(); i++) {
						result += EnumChatFormatting.GOLD + "" + pastNames.get(i) + " : " + EnumChatFormatting.GREEN + changeDates.get(i) + "\n";
					}
					result += EnumChatFormatting.AQUA + "-----------------------------------------------------\n";
					termIn.writeln(result);
				}
				else {
					termIn.writeln(EnumChatFormatting.GOLD + "" + pastNames.get(pastNames.size() - 1) + EnumChatFormatting.GREEN + " has not changed their name.");
				}
			}
		}
		else {
			mc.thePlayer.addChatMessage(ChatBuilder.of("").build());
			if (!pastNames.isEmpty() && !changeDates.isEmpty()) {
				if (pastNames.size() > 1) {
					String result = "";
					result += EnumChatFormatting.GOLD + "" + originalName + "'s " + EnumChatFormatting.GREEN + "previous name history:\n";
					result += EnumChatFormatting.AQUA + "-----------------------------------------------------\n";
					for (int i = 0; i < pastNames.size(); i++) {
						result += EnumChatFormatting.GOLD + "" + pastNames.get(i) + " : " + EnumChatFormatting.GREEN + changeDates.get(i) + "\n";
					}
					result += EnumChatFormatting.AQUA + "-----------------------------------------------------\n";
					mc.thePlayer.addChatMessage(ChatBuilder.of(result).build());
				} else {
					mc.thePlayer.addChatMessage(ChatBuilder.of(EnumChatFormatting.GOLD + "" + pastNames.get(pastNames.size() - 1) + EnumChatFormatting.GREEN + " has not changed their name.").build());
				}
			}
			else {
				mc.thePlayer.addChatMessage(ChatBuilder.of(EnumChatFormatting.GREEN + "Username: " + EnumChatFormatting.GOLD + originalName + EnumChatFormatting.GREEN + " does not exist!").build());
			}
			mc.thePlayer.addChatMessage(ChatBuilder.of("").build());
		}
	}

	private void fetchHistory(ETerminal termIn, String playerName) {
		if (playerName.equals(null)) { return; }
		changeDates.clear();
		pastNames.clear();
		preSorted.clear();
		startPos = 0;

		try {
			URL uuidFetcher = new URL("https://api.mojang.com/users/profiles/minecraft/" + playerName);
			InputStreamReader uuidReader = new InputStreamReader(uuidFetcher.openStream());
			BufferedReader getUuid = new BufferedReader(uuidReader);
			String testUUID = getUuid.readLine();
			if (testUUID != null) {
				int uuidIndex = EUtil.findStartingIndex(testUUID, "\"id\":\"") + 6;
				uuid = testUUID.substring(uuidIndex, testUUID.length() - 2);
			}
			
			//System.out.println(uuid);
			String hypuuid = uuid.substring(0, 8) + "-" + uuid.substring(8, 12) + "-" + uuid.substring(12, 16) + "-" + uuid.subSequence(16, 20) + "-" + uuid.substring(20);
			//System.out.println("/viewprofile " + hypuuid);
			
			getUuid.close();
			if (uuid != null) {
				URL nameFetcher = new URL("https://api.mojang.com/user/profiles/" + uuid + "/names");
				InputStreamReader nameReader = new InputStreamReader(nameFetcher.openStream());
				BufferedReader getName = new BufferedReader(nameReader);
				String names = getName.readLine();
				getName.close();
				
				names = names.replace("[", "").replace("{", "").replace("}", "").replace("\"", "");
				for (int i = 0; i < names.length(); i++) {
					if (names.charAt(i) == ':')
						startPos = i + 1;
					if (names.charAt(i) == ']' || names.charAt(i) == ',') {
						pastNames.add(names.substring(startPos, i));
						changeDates.add("Original");
						startPos = i;
						break;
					}
				}
				if (names.charAt(startPos) == ']') { return; }
				while (names.charAt(startPos) != ']') {
					names = names.substring(startPos + 1);
					preSorted.add(splitValues(names));
				}
				String last = "";
				while (!preSorted.isEmpty()) {
					Date changeDate = new Date(Long.valueOf(preSorted.get(1)));
					last = preSorted.get(0);
					pastNames.add(last);
					changeDates.add(changeDate.toString());
					preSorted.remove(0);
					preSorted.remove(0);
				}
				curName = last;
			}
		}
		catch (IOException e) {
			if (termIn != null) {
				termIn.writeln(EnumChatFormatting.GOLD + playerName + " " + EnumChatFormatting.GREEN + "is available.");
			}
			else {
				mc.thePlayer.addChatMessage(ChatBuilder.of("").build());
				mc.thePlayer.addChatMessage(ChatBuilder.of(EnumChatFormatting.GOLD + playerName + " " + EnumChatFormatting.GREEN + "is availible.").build());
				mc.thePlayer.addChatMessage(ChatBuilder.of("").build());
			}
		}
		return;
	}

	private String splitValues(String input) {
		for (int i = 0; i < input.length(); i++) {
			if (input.charAt(i) == ':') { startPos = i + 1; }
			if (input.charAt(i) == ']' || input.charAt(i) == ',') {
				String value = input.substring(startPos, i);
				startPos = i;
				return value;
			}
		}
		return null;
	}
	
}