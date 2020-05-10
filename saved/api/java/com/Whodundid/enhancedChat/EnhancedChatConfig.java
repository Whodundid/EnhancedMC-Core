package com.Whodundid.enhancedChat;

import com.Whodundid.core.app.config.CommentConfigBlock;
import com.Whodundid.core.app.config.ConfigBlock;
import com.Whodundid.core.app.config.AppConfigFile;
import com.Whodundid.core.util.storageUtil.EArrayList;
import com.Whodundid.core.util.storageUtil.StorageBoxHolder;

public class EnhancedChatConfig extends AppConfigFile {
	
	EnhancedChatApp mod;
	
	public EnhancedChatConfig(EnhancedChatApp modIn, String configNameIn) {
		super(modIn, configNameIn);
		mod = modIn;
	}
	
	@Override
	public boolean saveConfig() {
		EArrayList<ConfigBlock> configLines = new EArrayList();
		configLines.add(new CommentConfigBlock("Enhanced Chat Config").nl());
		
		configLines.add(new CommentConfigBlock("General"));
		configLines.add(new ConfigBlock("Show more info:", mod.showMoreChatInfo.get()));
		configLines.add(new ConfigBlock("Show time stamps:", mod.showTimeStamps.get()));
		configLines.add(new ConfigBlock("Warn on links:", mod.warnOnLinks.get()));
		configLines.add(new ConfigBlock("Make windows pinned:", mod.makeNewWindowsPinned.get()).nl());
		
		configLines.add(new CommentConfigBlock("Chat History"));
		configLines.add(new ConfigBlock("History length:", mod.chatHistoryLength).nl());
		
		configLines.add(new CommentConfigBlock("Chat Organizer"));
		configLines.add(new ConfigBlock("Enable chat organizer:", mod.enableChatOrganizer.get()));
		configLines.add(new ConfigBlock("Use default tabs:", mod.useDefaultTabs.get()));
		configLines.add(new ConfigBlock("Selected chat tabs:", mod.userFilters).nl());
		
		configLines.add(new CommentConfigBlock("Window Appearance"));
		configLines.add(new ConfigBlock("Highlight your lines:", mod.highlightYourLines.get()));
		configLines.add(new ConfigBlock("Remember pos and size:", mod.rememberPosAndSize.get()));
		configLines.add(new ConfigBlock("Draw field on unfocused:", mod.drawFieldOnUnfocused.get()).nl());
		
		configLines.add(new CommentConfigBlock("Pos & Size"));
		configLines.add(new ConfigBlock("Use default pos:", mod.useDefaultPos.get()));
		configLines.add(new ConfigBlock("Position:", mod.drawPosX, mod.drawPosY));
		configLines.add(new ConfigBlock("Use default size:", mod.useDefaultSize.get()));
		configLines.add(new ConfigBlock("Size:", mod.drawWidth, mod.drawHeight).nl());
		
		configLines.add(new CommentConfigBlock("Opacity"));
		configLines.add(new ConfigBlock("Window opacity:", mod.windowOpacity));
		configLines.add(new ConfigBlock("Timestamp opacity:", mod.timeOpacity));
		configLines.add(new ConfigBlock("Header opacity:", mod.headerOpacity).nl());
		
		configLines.add(new CommentConfigBlock("Highlighted Names"));
		configLines.add(new ConfigBlock("Names:", mod.highlightedPlayers.getObjects()));
		configLines.add(new ConfigBlock("Color:", mod.highlightedPlayers.getValues()));
		
		return createConfig(configLines);
	}
	
	@Override
	public boolean loadConfig() {
		try {
			if (getConfigContents().size() > 0) {
				mod.showMoreChatInfo.set(getConfigVal("Show more info:", Boolean.class, true));
				mod.showTimeStamps.set(getConfigVal("Show time stamps:", Boolean.class, true));
				mod.warnOnLinks.set(getConfigVal("Warn on links:", Boolean.class, true));
				mod.makeNewWindowsPinned.set(getConfigVal("Make windows pinned:", Boolean.class, true));
				
				mod.chatHistoryLength = getConfigVal("History length:", Integer.class, mod.defaultHistoryLength);
				
				mod.enableChatOrganizer.set(getConfigVal("Enable chat organizer:", Boolean.class, true));
				mod.useDefaultTabs.set(getConfigVal("Use default tabs:", Boolean.class, true));
				EArrayList<String> loadedTabs = loadTabs();
				if (loadedTabs != null) { mod.userFilters = loadedTabs; }
				
				mod.highlightYourLines.set(getConfigVal("Highlight your lines:", Boolean.class, false));
				mod.rememberPosAndSize.set(getConfigVal("Remember pos and size:", Boolean.class, false));
				mod.drawFieldOnUnfocused.set(getConfigVal("Draw field on unfocused:", Boolean.class, true));
				
				mod.useDefaultPos.set(getConfigVal("Use default pos:", Boolean.class, true));
				loadPos();
				mod.useDefaultSize.set(getConfigVal("Use default size:", Boolean.class, true));
				loadSize();
				
				mod.windowOpacity = getConfigVal("Window opacity:", Integer.class, mod.defaultOpacity);
				mod.timeOpacity = getConfigVal("Timestamp opacity:", Integer.class, mod.defaultTimeOpacity);
				mod.headerOpacity = getConfigVal("Header opacity:", Integer.class, mod.defaultHeaderOpacity);
				
				StorageBoxHolder<String, Integer> loadedNames = loadHighlightedNames();
				if (loadedNames != null) { mod.highlightedPlayers = loadedNames; }
			}
		} catch (Exception e) { e.printStackTrace(); }
		return false;
	}
	
	@Override
	public boolean resetConfig() {
		EArrayList<ConfigBlock> configLines = new EArrayList();
		configLines.add(new CommentConfigBlock("Enhanced Chat Config").nl());
		
		configLines.add(new CommentConfigBlock("General"));
		configLines.add(new ConfigBlock("Show more info:", mod.showMoreChatInfo.getDefault()));
		configLines.add(new ConfigBlock("Show time stamps:", mod.showTimeStamps.getDefault()));
		configLines.add(new ConfigBlock("Warn on links:", mod.warnOnLinks.getDefault()));
		configLines.add(new ConfigBlock("Make windows pinned:", mod.makeNewWindowsPinned.getDefault()).nl());
		
		configLines.add(new CommentConfigBlock("Chat History"));
		configLines.add(new ConfigBlock("History length:", mod.defaultHistoryLength).nl());
		
		configLines.add(new CommentConfigBlock("Chat Organizer"));
		configLines.add(new ConfigBlock("Enable chat organizer:", mod.enableChatOrganizer.getDefault()));
		configLines.add(new ConfigBlock("Use default tabs:", mod.useDefaultTabs.getDefault()));
		configLines.add(new ConfigBlock("Selected chat tabs:", mod.defaultFilters).nl());
		
		configLines.add(new CommentConfigBlock("Window Appearance"));
		configLines.add(new ConfigBlock("Highlight your lines:", mod.highlightYourLines.getDefault()));
		configLines.add(new ConfigBlock("Remember pos and size:", mod.rememberPosAndSize.getDefault()));
		configLines.add(new ConfigBlock("Draw field on unfocused:", mod.drawFieldOnUnfocused.getDefault()).nl());
		
		configLines.add(new CommentConfigBlock("Pos & Size"));
		configLines.add(new ConfigBlock("Use default pos:", mod.useDefaultPos.getDefault()));
		configLines.add(new ConfigBlock("Position:", mod.drawPosX, mod.drawPosY));
		configLines.add(new ConfigBlock("Use default size:", mod.useDefaultSize.getDefault()));
		configLines.add(new ConfigBlock("Size:", mod.defaultWidth, mod.defaultHeight).nl());
		
		configLines.add(new CommentConfigBlock("Opacity"));
		configLines.add(new ConfigBlock("Window opacity:", mod.defaultOpacity));
		configLines.add(new ConfigBlock("Timestamp opacity:", mod.defaultTimeOpacity));
		configLines.add(new ConfigBlock("Header opacity:", mod.defaultHeaderOpacity).nl());
		
		configLines.add(new CommentConfigBlock("Highlighted Names"));
		configLines.add(new ConfigBlock("Names:", mod.highlightedPlayers.getObjects()));
		configLines.add(new ConfigBlock("Color:", mod.highlightedPlayers.getValues()));
		
		return createConfig(configLines);
	}
	
	private EArrayList<String> loadTabs() throws Exception {
		try {
			EArrayList<String> loaded = configValues.getBoxWithObj("Selected chat tabs:").getValue();
			EArrayList<String> types = new EArrayList();
			for (String s : loaded) { types.add(s); }
			return types;
		} catch (Exception e) {
			System.out.println("Failure to read saved tab list");
			return null;
		}
	}
	
	private boolean loadPos() throws Exception {
		try {
			EArrayList<String> vals = configValues.getBoxWithObj("Position:").getValue();
			//System.out.println("vals: " + vals);
			mod.drawPosX = Integer.parseInt(vals.get(0));
			mod.drawPosY = Integer.parseInt(vals.get(1));
			return true;
		} catch (Exception e) {
			System.out.println("Failure to read x/y position");
			return false;
		}
	}
	
	private boolean loadSize() throws Exception {
		try {
			EArrayList<String> vals = configValues.getBoxWithObj("Size:").getValue();
			mod.drawWidth = Integer.parseInt(vals.get(0));
			mod.drawHeight = Integer.parseInt(vals.get(1));
			return true;
		} catch (Exception e) {
			System.out.println("Failure to read size");
			return false;
		}
	}
	
	private StorageBoxHolder<String, Integer> loadHighlightedNames() throws Exception {
		try {
			System.out.println(configValues.getBoxWithObj("Names:"));
			EArrayList<String> loadedNames = configValues.getBoxWithObj("Names:").getValue();
			EArrayList<String> loadedColor = configValues.getBoxWithObj("Color:").getValue();
			StorageBoxHolder<String, Integer> vals = new StorageBoxHolder();
			if (loadedNames.size() == loadedColor.size()) {
				for (int i = 0; i < loadedNames.size(); i++) {
					vals.add(loadedNames.get(i), Integer.parseInt(loadedColor.get(i)));
				}
			}
			return vals;
		} catch (Exception e) {
			System.out.println("Failure to read highlighted names");
			return null;
		}
	}
}
