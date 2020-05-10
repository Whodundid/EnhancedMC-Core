package com.Whodundid.enhancedChat;

import com.Whodundid.core.EnhancedMC;
import com.Whodundid.core.app.AppConfigSetting;
import com.Whodundid.core.app.AppType;
import com.Whodundid.core.app.EMCApp;
import com.Whodundid.core.coreEvents.emcEvents.ChatLineCreatedEvent;
import com.Whodundid.core.coreEvents.emcEvents.EMCAppCalloutEvent;
import com.Whodundid.core.coreEvents.emcEvents.TabCompletionEvent;
import com.Whodundid.core.util.renderUtil.CenterType;
import com.Whodundid.core.util.storageUtil.EArrayList;
import com.Whodundid.core.util.storageUtil.StorageBoxHolder;
import com.Whodundid.enhancedChat.chatOrganizer.ChatFilterList;
import com.Whodundid.enhancedChat.chatOrganizer.ChatOrganizer;
import com.Whodundid.enhancedChat.chatUtil.RenderBridge;
import com.Whodundid.enhancedChat.chatWindow.ChatWindow;
import com.Whodundid.enhancedChat.guis.AppearanceGui;
import com.Whodundid.enhancedChat.guis.ChatOrganizerGui;
import com.Whodundid.enhancedChat.guis.EnhancedChatGuiMain;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent.ElementType;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import org.lwjgl.input.Keyboard;
import net.minecraftforge.fml.common.gameevent.InputEvent.KeyInputEvent;

//Dec 31, 2018
//Jan 13, 2019
//Jan 20, 2019
//Last edited: Jul 8, 2019
//Edit note: reformatted as individual mod
//First Added: Dec 31, 2018
//Author: Hunter Bragg

@Mod(modid = EnhancedChatApp.MODID, version = EnhancedChatApp.VERSION, name = EnhancedChatApp.NAME, dependencies = "required-after:enhancedmc")
public final class EnhancedChatApp extends EMCApp {
	
	public static final String MODID = "enhancedchat";
	public static final String VERSION = "0.9";
	public static final String NAME = "Enhanced Chat";
	
	private static ChatOrganizer organizer;
	private static RenderBridge bridge;
	
	public static AppConfigSetting<Boolean> showTimeStamps = new AppConfigSetting(Boolean.class, "showTimeStamps", "Show Chat Timestamps", true);
	public static AppConfigSetting<Boolean> showMoreChatInfo = new AppConfigSetting(Boolean.class, "showMoreChatInfo", "Show More Chat Info", true);
	public static AppConfigSetting<Boolean> enableChatOrganizer = new AppConfigSetting(Boolean.class, "enableChatOrganizer", "Enable Chat Organizer", true);
	public static AppConfigSetting<Boolean> highlightYourLines = new AppConfigSetting(Boolean.class, "highlightYourLines", "Highlight your Chat Lines", true);
	public static AppConfigSetting<Boolean> warnOnLinks = new AppConfigSetting(Boolean.class, "warnOnLinks", "Warn on Links", true);
	public static AppConfigSetting<Boolean> rememberPosAndSize = new AppConfigSetting(Boolean.class, "rememberPosAndSize", "Remember Position and Size", true);
	public static AppConfigSetting<Boolean> drawFieldOnUnfocused = new AppConfigSetting(Boolean.class, "drawFieldOnUnfocused", "Draw Input Field when Unfocused", true);
	public static AppConfigSetting<Boolean> useDefaultPos = new AppConfigSetting(Boolean.class, "useDefaultPos", "Use Default Position", true);
	public static AppConfigSetting<Boolean> useDefaultSize = new AppConfigSetting(Boolean.class, "useDefaultSize", "Use Default Size", true);
	public static AppConfigSetting<Boolean> useDefaultTabs = new AppConfigSetting(Boolean.class, "useDefaultTabs", "Use Default Chat Tabs", true);
	public static AppConfigSetting<Boolean> useDefaultOpacity = new AppConfigSetting(Boolean.class, "useDefaultOpacity", "Use Default Opacity", true);
	public static AppConfigSetting<Boolean> useDefaultOpacityTime = new AppConfigSetting(Boolean.class, "useDefaultOpacityTime", "Use Default Timestamp Opacity", true);
	public static AppConfigSetting<Boolean> useDefaultOpacityHeader = new AppConfigSetting(Boolean.class, "useDefaultOpacityHeader", "Use Default Header Opacity", true);
	public static AppConfigSetting<Boolean> makeNewWindowsPinned = new AppConfigSetting(Boolean.class, "makeNewWindowsPinned", "Make New Chat Windows Pinned", true);
	
	public static StorageBoxHolder<String, Integer> highlightedPlayers = new StorageBoxHolder();
	public static EArrayList<String> defaultFilters = new EArrayList("All", "Private", "Party", "Guild", "Lobby");
	public static EArrayList<String> userFilters = new EArrayList();
	public static int drawPosX = 0, drawPosY = 0;
	public static int defaultWidth = 375, defaultHeight = 242;
	public static int drawWidth = 375, drawHeight = 242;
	public static int defaultOpacity = 0x88000000, defaultTimeOpacity = 0xdd000000, defaultHeaderOpacity = 0xd0000000;
	public static int windowOpacity = 0x88000000, timeOpacity = 0xdd000000, headerOpacity = 0xd0000000;
	public static int chatHistoryLength = 1000, defaultHistoryLength = 1000;
	public static int msgLengthLimit = 4000, defaultLengthLimit = 1000;
	
	private static EnhancedChatApp instance;
	
	public EnhancedChatApp() {
		super(AppType.ENHANCEDCHAT);
		instance = this;
		shouldLoad = false;
		addDependency(AppType.CORE, "1.0");
		organizer = new ChatOrganizer(this);
		bridge = new RenderBridge(this);
		setMainGui(new EnhancedChatGuiMain());
		addGui(new AppearanceGui(), new ChatOrganizerGui(), new ChatWindow());
		configManager.setMainConfig(new EnhancedChatConfig(this, "enhancedChat"));
		setAliases("enhancedchat", "echat", "chat", "chatwindows");
		version = VERSION;
		author = "Whodundid";
	}
	
	public static EnhancedChatApp instance() { return instance; }
	
	@Override
	public void onPostInit(FMLPostInitializationEvent e) {
		bridge.setDependencies();
		
		if (useDefaultTabs.get()) {
			organizer.addFilter(new ChatFilterList("Private", 0xffaa00, EnumChatFormatting.LIGHT_PURPLE).addFilters("To ", "From "));
			organizer.addFilter(new ChatFilterList("Party", 0x5555ff, EnumChatFormatting.BLUE).addFilters("Party >"));
			organizer.addFilter(new ChatFilterList("Guild", 0x00aa00, EnumChatFormatting.DARK_GREEN).addFilters("Guild >"));
			organizer.addFilter(new ChatFilterList("Lobby", 0x55ff55, EnumChatFormatting.GREEN).addFilters(":"));
		}
	}
	
	@Override
	public void clientTickEvent(TickEvent.ClientTickEvent e) {
		organizer.onTick();
	}
	
	@Override
	public void keyEvent(KeyInputEvent e) {
		if (isEnabled()) {
			boolean chat = Keyboard.isKeyDown(mc.gameSettings.keyBindChat.getKeyCode());
			boolean command = Keyboard.isKeyDown(mc.gameSettings.keyBindCommand.getKeyCode());
			
			if (chat || command) {
				if (e.isCancelable()) { e.setCanceled(true); }
			}
			
			if (chat) { bridge.addIfNotOpen(); }
			if (command) { bridge.addIfNotOpen("/"); }
		}
	}
	
	@Override
	public void initGuiEvent(GuiScreenEvent.InitGuiEvent e) {
		if (isEnabled()) {
			/*
			if (e instanceof GuiScreenEvent.InitGuiEvent.Pre) {
				if (e.gui != null && !(e.gui instanceof ModdedChat)) {
					boolean is = false;
					for (GuiScreen g : getGuis()) {
						if (e.gui.getClass().isAssignableFrom(g.getClass())) { is = true; break; }
					}
					if (bridge != null && !is) { bridge.removeAllUnpinnedChatWindows(); }
				}
			}
			*/
		}
	}
	
	@Override
	public void overlayPreEvent(RenderGameOverlayEvent.Pre e) {
		if (isEnabled()) {
			if (e.type == ElementType.CHAT) {
				if (bridge.hasAChatWindow()) { e.setCanceled(true); }
			}
		}
	}
	
	@Override
	public void subModCalloutEvent(EMCAppCalloutEvent e) {
		if (isEnabled()) {
    		if (e.checkSenderMsg("EnhancedChat: has chat window")) {
    			e.setCanceled(bridge.hasAChatWindow());
    		}
    		if (e.checkSenderMsg("EnhancedChat: add window")) {
    			bridge.addChatWindow();
    		}
    		if (e.checkSenderMsg("EnhancedChat: add window cursor")) {
    			bridge.addChatWindow("", CenterType.cursor);
    		}
		}
	}
	
	@Override
	public Object sendArgs(Object... args) {
		if (isEnabled()) {
			if (args.length > 0) {
				if (args[0] instanceof String) {
					String cmd = (String) args[0];
					
					switch (cmd) {
					case "EnhancedChat: has chat window": return bridge.hasAChatWindow();
					case "EnhancedChat: add window": bridge.addChatWindow(); break;
					case "EnhancedChat: add window cursor": bridge.addChatWindow("", CenterType.cursor); break;
					default: break;
					}
				}
			}
		}
		return null;
	}
	
	@Override
	public void chatLineCreatedEvent(ChatLineCreatedEvent e) {
		organizer.readChat(e.getLine());
	}
	
	@Override
	public void tabCompletionEvent(TabCompletionEvent e) {
		//bridge.
	}
	
	@Override
	public EnhancedChatApp setEnabled(boolean valueIn) {
		organizer.getFilterHistoryList("All").clear();
		super.setEnabled(valueIn);
		if (!isEnabled() && EnhancedMC.isInitialized()) { bridge.removeAllChatWindows(); }
		return this;
	}
	
	public ChatOrganizer getChatOrganizer() { return organizer; }
	public RenderBridge getRenderBridge() { return bridge; }
}
