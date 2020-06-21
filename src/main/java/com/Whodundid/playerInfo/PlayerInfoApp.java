package com.Whodundid.playerInfo;

import com.Whodundid.core.EnhancedMC;
import com.Whodundid.core.app.AppType;
import com.Whodundid.core.app.EMCApp;
import com.Whodundid.core.app.config.AppConfigFile;
import com.Whodundid.core.app.config.AppConfigSetting;
import com.Whodundid.core.terminal.window.ETerminal;
import com.Whodundid.core.util.EUtil;
import com.Whodundid.core.util.chatUtil.ChatBuilder;
import com.Whodundid.core.util.mathUtil.NumberUtil;
import com.Whodundid.core.util.playerUtil.PlayerFacing;
import com.Whodundid.core.util.resourceUtil.EResource;
import com.Whodundid.core.util.storageUtil.EArrayList;
import com.Whodundid.core.util.storageUtil.StorageBox;
import com.Whodundid.playerInfo.cmd.CMD_CheckNameHistory;
import com.Whodundid.playerInfo.cmd.CMD_CheckPlayerSkin;
import com.Whodundid.playerInfo.cmd.CMD_InfoPlayer;
import com.Whodundid.playerInfo.terminal.Term_InfoPlayer;
import com.Whodundid.playerInfo.terminal.Term_NameHistory;
import com.Whodundid.playerInfo.terminal.Term_PlayerSkin;
import com.Whodundid.playerInfo.util.PIResources;
import com.Whodundid.playerInfo.util.PlayerSkinFetcher;
import com.Whodundid.playerInfo.util.SkinContainer;
import com.Whodundid.playerInfo.window.PlayerInfoSettings;
import com.Whodundid.playerInfo.window.PlayerInfoWindow;
import com.Whodundid.playerInfo.window.SkinDisplayWindow;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.client.ClientCommandHandler;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent.KeyInputEvent;
import org.lwjgl.input.Keyboard;

@Mod(modid = PlayerInfoApp.MODID, version = PlayerInfoApp.VERSION, name = PlayerInfoApp.NAME, dependencies = "required-after:enhancedmc")
public final class PlayerInfoApp extends EMCApp {

	public static final String MODID = "playerinfo";
	public static final String VERSION = "2.0";
	public static final String NAME = "EMC Player Info";
	
	//---------
	//Resources
	//---------
	
	public static final PIResources resources = new PIResources();
	
	//---------------
	//config settings
	//---------------
	
	public static final AppConfigSetting<Boolean> drawCapes = new AppConfigSetting(Boolean.class, "drawCapes", "Draw Capes", true);
	public static final AppConfigSetting<Boolean> animateSkins = new AppConfigSetting(Boolean.class, "animateSkin", "Anitate Skin Display", true);
	public static final AppConfigSetting<Boolean> drawNames = new AppConfigSetting(Boolean.class, "drawName", "Draw Player Name", false);
	public static final AppConfigSetting<Boolean> randomBackgrounds = new AppConfigSetting(Boolean.class, "randomBackgrounds", "Randomize Player Backgrounds", true);
	public static final AppConfigSetting<Long> capeFixTime = new AppConfigSetting(Long.class, "capeFixTime", "Cape Fix Time", 20l);
	
	private ArrayList<String> preSorted = new ArrayList<String>();
	private ArrayList<String> pastNames = new ArrayList<String>();
	private ArrayList<String> changeDates = new ArrayList<String>();
	private int startPos = 0;
	public String uuid = null;
	public String curName = "";
	public static final KeyBinding nameChecker = new KeyBinding("Fetch Name History", Keyboard.KEY_N, "EnhancedMC");
	
	public String openInfoPlayer = null;
	
	public static long timeSinceLast = 0l;
	public static boolean received = false;
	public static SkinContainer container = null;
	public static PlayerInfoWindow window;
	public static PlayerInfoApp instance = null;

	//--------------------------
	//PlayerInfoApp Constructors
	//--------------------------
	
	public PlayerInfoApp() {
		super(AppType.PLAYERINFO);
		instance = this;
	}
	
	@Override
	public void build() {
		version = VERSION;
		versionDate = "June 11, 2020";
		author = "Whodundid";
		artist = "Mr.JamminOtter";
		donation = new StorageBox("Consider donating to support EMC development!", "https://www.paypal.me/Whodundid");
		addDependency(AppType.CORE, "1.0");
		
		configManager.setMainConfig(new AppConfigFile(this, "playerinfo", "EMC Player Info Config"));
		setMainWindow(new PlayerInfoSettings());
		addWindow(new SkinDisplayWindow("", ""), new PlayerInfoWindow(""));
		
		setResources(resources);
		logo = new EArrayList<EResource>(PIResources.logo);
		
		registerSetting(drawCapes, animateSkins, drawNames, randomBackgrounds, capeFixTime);
		
		setAliases("playerinfo", "pinfo", "pi");
	}
	
	public static PlayerInfoApp instance() { return instance; }

	@Override
	public void onPostInit(FMLPostInitializationEvent event) {
		if (!isIncompatible()) {
			ClientRegistry.registerKeyBinding(nameChecker);
			ClientCommandHandler.instance.registerCommand(new CMD_CheckNameHistory(this));
			ClientCommandHandler.instance.registerCommand(new CMD_CheckPlayerSkin(this));
			ClientCommandHandler.instance.registerCommand(new CMD_InfoPlayer(this));
		}
	}

	@Override
	public void keyEvent(KeyInputEvent e) {
		if (isEnabled()) {
			if (nameChecker.isPressed()) {
				if (PlayerFacing.isFacingPlayer()) {
					fetchNameHistory(null, true);
				}
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
					case "PlayerInfo: fetch skin": fetchSkin(null, null, player); break;
					default: break;
					}
				}
			}
		}
		return null;
	}
	
	@Override
	public void renderLastWorldEvent(RenderWorldLastEvent e) {
		if (isEnabled()) {
			if (openInfoPlayer != null) {
				EnhancedMC.displayWindow(new PlayerInfoWindow(openInfoPlayer));
				openInfoPlayer = null;
			}
			if (received) {
				if (container != null) {
					try {
						if (window != null) {
							window.onSkinResponse(container);
						}
						else {
							//create the skin window
							SkinDisplayWindow displayer = new SkinDisplayWindow(container.getUUID(), container.getName());
							displayer.setSkin(container.getSkinImage(), container.getCapeImage(), container.isAlex(), container.hasCape());
							
							//display skin window
							EnhancedMC.displayWindow(displayer);
						}
					}
					catch (Exception q) { q.printStackTrace(); }
				}
				
				received = false;
			}
		}
	}
	
	@Override
	public void terminalRegisterCommandEvent(ETerminal termIn, boolean runVisually) {
		if (!isIncompatible()) {
			EnhancedMC.getTerminalHandler().registerCommand(new Term_NameHistory(), termIn, runVisually);
			EnhancedMC.getTerminalHandler().registerCommand(new Term_PlayerSkin(), termIn, runVisually);
			EnhancedMC.getTerminalHandler().registerCommand(new Term_InfoPlayer(), termIn, runVisually);
		}
	}
	
	public static EResource getRandomBackground() {
		int num = NumberUtil.getRoll(0, 14);
		
		switch (num) {
		case 0: return PIResources.viewerBackground0;
		case 1: return PIResources.viewerBackground1;
		case 2: return PIResources.viewerBackground2;
		case 3: return PIResources.viewerBackground3;
		case 4: return PIResources.viewerBackground4;
		case 5: return PIResources.viewerBackground5;
		case 6: return PIResources.viewerBackground6;
		case 7: return PIResources.viewerBackground7;
		case 8: return PIResources.viewerBackground8;
		case 9: return PIResources.viewerBackground9;
		case 10: return PIResources.viewerBackground10;
		case 11: return PIResources.viewerBackground11;
		case 12: return PIResources.viewerBackground12;
		case 13: return PIResources.viewerBackground13;
		case 14: return PIResources.viewerBackground14;
		}
		
		return null;
	}
	
	//---------------------
	//PlayerInfoApp Methods
	//---------------------

	public void fetchNameHistory(String playerName, boolean isFacing) { fetchNameHistory(null, null, playerName, isFacing); }
	public void fetchNameHistory(PlayerInfoWindow windowIn, ETerminal termIn, String playerName, boolean isFacing) {
		if (isEnabled()) {
			Thread fetcher = new Thread() {
				@Override
				public void run() {
					try {
						String name = PlayerFacing.getFacingPlayerName();
						if (isFacing) { fetchHistory(termIn, name); }
						else { fetchHistory(termIn, playerName); }
						displayPlayerNameHistory(windowIn, termIn, isFacing ? name : curName);
					}
					catch (Exception e) { e.printStackTrace(); }
				}
			};
			fetcher.start();
		}
		else if (termIn != null) { termIn.error("PlayerInfo App is disabled! Enable it to use."); }
		else if (mc.thePlayer != null) { mc.thePlayer.addChatMessage(ChatBuilder.of(EnumChatFormatting.RED + "PlayerInfo App is disabled! Enable to use.").build()); }
	}
	
	public void fetchSkin(String playerName) { fetchSkin(null, null, playerName); }
	public void fetchSkin(PlayerInfoWindow windowIn, ETerminal termIn, String playerName) {
		if (isEnabled()) {
			PlayerSkinFetcher.getSkinPic(windowIn, termIn, playerName);
		}
		else if (termIn != null) { termIn.error("PlayerInfo App is disabled! Enable it to use."); }
		else if (mc.thePlayer != null) { mc.thePlayer.addChatMessage(ChatBuilder.of(EnumChatFormatting.RED + "PlayerInfo App is disabled! Enable to use.").build()); }
	}
	
	//------------------------------
	//PlayerInfoApp Internal Methods
	//------------------------------

	private void displayPlayerNameHistory(PlayerInfoWindow windowIn, ETerminal termIn, String originalName) {
		if (windowIn != null) {
			EArrayList<String> lines = new EArrayList();
			
			if (!pastNames.isEmpty() && !changeDates.isEmpty()) {
				if (pastNames.size() > 1) {
					lines.add(EnumChatFormatting.GOLD + "" + originalName + "'s " + EnumChatFormatting.GREEN + "previous name history:");
					lines.add(EnumChatFormatting.AQUA + "--------------------------------------");
					for (int i = 0; i < pastNames.size(); i++) {
						lines.add(EnumChatFormatting.GOLD + "" + pastNames.get(i) + " : " + EnumChatFormatting.GREEN + changeDates.get(i));
					}
					lines.add(EnumChatFormatting.AQUA + "--------------------------------------");
				}
				else {
					String result = EnumChatFormatting.GOLD + "" + pastNames.get(pastNames.size() - 1) + EnumChatFormatting.GREEN + " has not changed their name.";
					lines.add(result);
				}
			}
			
			windowIn.onNamesResponse(!originalName.isEmpty() ? originalName : pastNames.get(pastNames.size() - 1), uuid, lines);
		}
		else if (termIn != null) {
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
				
				//if (names.charAt(startPos) == ']') { return; }
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