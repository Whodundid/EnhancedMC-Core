package com.Whodundid.hotkeys;

import com.Whodundid.core.EnhancedMC;
import com.Whodundid.core.app.AppType;
import com.Whodundid.core.app.EMCApp;
import com.Whodundid.core.app.config.AppConfigFile;
import com.Whodundid.core.app.config.AppConfigSetting;
import com.Whodundid.core.terminal.window.ETerminal;
import com.Whodundid.core.util.resourceUtil.EResource;
import com.Whodundid.core.util.storageUtil.EArrayList;
import com.Whodundid.core.util.storageUtil.StorageBox;
import com.Whodundid.hotkeys.control.HotKey;
import com.Whodundid.hotkeys.control.KeyActionType;
import com.Whodundid.hotkeys.control.hotKeyTypes.CommandSenderHotKey;
import com.Whodundid.hotkeys.control.hotKeyUtil.KeyComboAction;
import com.Whodundid.hotkeys.keySaveLoad.HotKeyBuilder;
import com.Whodundid.hotkeys.keySaveLoad.KeyLoader;
import com.Whodundid.hotkeys.keySaveLoad.KeySaver;
import com.Whodundid.hotkeys.settings.InputDelay;
import com.Whodundid.hotkeys.terminal.CreateExampleKey;
import com.Whodundid.hotkeys.util.HKResources;
import com.Whodundid.hotkeys.window.HotKeyCreatorWindow;
import com.Whodundid.hotkeys.window.HotKeyMainWindow;
import com.Whodundid.hotkeys.window.HotKeyListWindow;
import com.Whodundid.hotkeys.window.HotKeySettingsWindow;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.gameevent.InputEvent.KeyInputEvent;
import org.apache.logging.log4j.Level;
import org.lwjgl.input.Keyboard;

//Last edited: Oct 16, 2018
//First Added: Sep 14, 2018
//Author: Hunter Bragg

@Mod(modid = HotKeyApp.MODID, version = HotKeyApp.VERSION, name = HotKeyApp.NAME, dependencies = "required-after:enhancedmc")
public final class HotKeyApp extends EMCApp {
	
	//---------
	//Resources
	//---------
	
	public static final HKResources resources = new HKResources();
	
	//---------------
	//config settings
	//---------------
	
	public static final AppConfigSetting<Boolean> stopMovement = new AppConfigSetting(Boolean.class, "stopMovement", "Stop Movement on Ctrl", false);
	public static final AppConfigSetting<Boolean> createExampleKey = new AppConfigSetting(Boolean.class, "createExampleKey", "Create Example Hotkey", true);
	public static final AppConfigSetting<Boolean> exampleKeyCreated = new AppConfigSetting(Boolean.class, "exampleKeyCreated", "Example Hotkey Created", false);
	public static final AppConfigSetting<Boolean> runCreationTutorial = new AppConfigSetting(Boolean.class, "runCreationTutorial", "Run Hotkey Creation Tutorial", true);
	public static final InputDelay keyInputDelay = new InputDelay();
	
	public static final String MODID = "hotkeys";
	public static final String VERSION = "2.1";
	public static final String NAME = "Hotkeys";
	protected EArrayList<HotKey> registeredHotKeys;
	protected EArrayList<HotKey> appHotKeys;
	protected KeySaver saver;
	protected KeyLoader loader;
	protected HotKeyBuilder builder;
	protected String defaultListSort = "enabled";
	protected long lastCommandKeyTime = 0l;
	private static HotKeyApp instance;
	
	public HotKeyApp() {
		super(AppType.HOTKEYS);
		instance = this;
	}
	
	@Override
	public void build() {
		version = VERSION;
		versionDate = "June 14, 2020";
		author = "Whodundid";
		artist = "Mr.JamminOtter";
		donation = new StorageBox("Consider donating to support EMC development!", "https://www.paypal.me/Whodundid");
		addDependency(AppType.CORE, "1.0");
		
		registeredHotKeys.clear();
		appHotKeys.clear();
		
		registerSetting(stopMovement, createExampleKey, exampleKeyCreated, keyInputDelay);
		
		AppConfigFile config = new AppConfigFile(this, "hotKeySettings", "EMC Hotkey Config") {
			@Override
			public boolean loadConfig() {
				boolean val = super.loadConfig();
				loadHotKeys();
				return val;
			}
			@Override
			public boolean saveConfig() {
				boolean val = super.saveConfig();
				saveHotKeys();
				return val;
			}
		};
		configManager.setMainConfig(config);
		
		setMainWindow(new HotKeyMainWindow());
		addWindow(new HotKeyCreatorWindow(), new HotKeyListWindow(), new HotKeySettingsWindow());
		setAliases("hotkeys", "hk");
		
		setResources(resources);
		logo = new EArrayList<EResource>(HKResources.logo);
		
		registeredHotKeys = new EArrayList();
		appHotKeys = new EArrayList();
		
		saver = new KeySaver(this);
		loader = new KeyLoader(this);
		builder = new HotKeyBuilder(this);
	}
	
	public static HotKeyApp instance() { return instance; }
	
	@Override
	public void onDevModeDisabled() {
		keyInputDelay.set(keyInputDelay.get());
	}
	
	@Override
	public void terminalRegisterCommandEvent(ETerminal termIn, boolean runVisually) {
		if (!isIncompatible()) {
			EnhancedMC.getTerminalHandler().registerCommand(new CreateExampleKey(), termIn, runVisually);
		}
	}
	
	public void createExampleKey() {
		registerHotKey(new CommandSenderHotKey("Example Hotkey", new KeyComboAction(Keyboard.KEY_H), "/help") {
			{
				setKeyDescription("This is an example of a CommandSender Hotkey. When pressed it send the command '/help'.");
				setKeyCategory("Example");
			}
		});
		exampleKeyCreated.set(true);
		getConfig().saveMainConfig();
	}
	
	public boolean registerHotKey(HotKey keyIn) {
		if (keyIn != null) {
			synchronized (registeredHotKeys) {
				if (!doesListAlreadyContain(keyIn, registeredHotKeys)) {
					registeredHotKeys.add(keyIn);
					EnhancedMC.log(Level.INFO, "[EMC Hotkeys]: hotkey registered: " + keyIn.getKeyName());
				}
				if (keyIn.isAppKey()) {
					if (!doesListAlreadyContain(keyIn, appHotKeys)) { appHotKeys.add(keyIn); return true; }
				}
			}
		}
		return false;
	}
	
	private boolean doesListAlreadyContain(HotKey keyIn, EArrayList<HotKey> list) {
		for (HotKey k : list) {
			if (k.getKeyName().equals(keyIn.getKeyName())) {
				if (k.getBuiltInAppType().equals(keyIn.getBuiltInAppType())) { return true; }
			}
		}
		return false;
	}
	
	public boolean unregisterHotKey(int... keyCodes) {
		synchronized (registeredHotKeys) {
			Iterator<HotKey> it = registeredHotKeys.iterator();
			while (it.hasNext()) {
				HotKey key = it.next();
				if (key.getKeyCombo().checkKeys(keyCodes)) {
					if (!key.isAppKey()) {
						it.remove();
						return true;
					}
				}
			}
		}
		return false;
	}
	
	public boolean unregisterHotKey(String keyName) {
		synchronized (registeredHotKeys) {
			Iterator<HotKey> it = registeredHotKeys.iterator();
			while (it.hasNext()) {
				HotKey key = it.next();
				if (key.getKeyName().equals(keyName)) {
					if (!key.isAppKey()) {
						it.remove();
						return true;
					}
				}
			}
		}
		return false;
	}
	
	public boolean unregisterHotKey(HotKey keyIn) {
		synchronized (registeredHotKeys) {
			Iterator<HotKey> it = registeredHotKeys.iterator();
			while (it.hasNext()) {
				HotKey key = it.next();
				if (key.equals(keyIn)) {
					if (!key.isAppKey()) {
						it.remove();
						return true;
					}
				}
			}
		}
		return false;
	}
	
	public HotKey getHotKey(int... keyCodes) {
		for (HotKey k : registeredHotKeys) {
			if (k.getKeyCombo().checkKeys(keyCodes)) { return k; }
		}
		return null;
	}
	
	public HotKey getHotKey(String keyNameIn) {
		for (HotKey k : registeredHotKeys) {
			if (k.getKeyName().equals(keyNameIn)) { return k; }
		}
		return null;
	}
	
	public boolean checkIfKeyComboAlreadyExists(int... keyCodes) {
		for (HotKey k : registeredHotKeys) {
			if (k.getKeyCombo().checkKeys(keyCodes)) { return true; }
		}
		return false;
	}
	
	public synchronized void saveHotKeys() {
		saver.saveKeysToFile();
	}
	
	public synchronized void loadHotKeys() {
		registeredHotKeys.clear();
		for (HotKey k : appHotKeys) { registerHotKey(k); }
		loader.loadKeysFromFile();
		if (createExampleKey.get() && !exampleKeyCreated.get()) { createExampleKey(); }
		for (HotKey k : loader.getLoadedKeys()) { registerHotKey(k); }
		saver.saveKeysToFile();
		EnhancedMC.reloadAllWindows();
	}
	
	public void reset() {
		registeredHotKeys.clear();
		saver.saveKeysToFile();
		for (HotKey k : appHotKeys) { registerHotKey(k); }
		if (createExampleKey.get() && !exampleKeyCreated.get()) { createExampleKey(); }
		EnhancedMC.reloadAllWindows();
	}
	
	@Override
	public void keyEvent(KeyInputEvent e) {
		if (Keyboard.getEventKeyState()) {
			try {
				if (Keyboard.isCreated() && Keyboard.getEventKeyState()) {
					int keyCode = Keyboard.getEventKey();
					EArrayList<Integer> checkKeys = new EArrayList();
					
					boolean isCtrl = (Minecraft.isRunningOnMac && (keyCode == 219 || keyCode == 220)) || keyCode == 29 || keyCode == 157;
					boolean isShift = keyCode == 42 || keyCode == 54;
					boolean isAlt = keyCode == 56 || keyCode == 184;
					
					boolean mcCtrl = GuiScreen.isCtrlKeyDown();
					
					if (stopMovement.get()) {
						if (mcCtrl && mc.gameSettings.keyBindForward.isPressed()) { KeyBinding.setKeyBindState(mc.gameSettings.keyBindForward.getKeyCode(), false); }
						if (mcCtrl && mc.gameSettings.keyBindLeft.isPressed()) { KeyBinding.setKeyBindState(mc.gameSettings.keyBindLeft.getKeyCode(), false); }
						if (mcCtrl && mc.gameSettings.keyBindBack.isPressed()) { KeyBinding.setKeyBindState(mc.gameSettings.keyBindBack.getKeyCode(), false); }
						if (mcCtrl && mc.gameSettings.keyBindRight.isPressed()) { KeyBinding.setKeyBindState(mc.gameSettings.keyBindRight.getKeyCode(), false); }
						if (mcCtrl && mc.gameSettings.keyBindJump.isPressed()) { KeyBinding.setKeyBindState(mc.gameSettings.keyBindJump.getKeyCode(), false); }
						if (mcCtrl && mc.gameSettings.keyBindSneak.isPressed()) { KeyBinding.setKeyBindState(mc.gameSettings.keyBindSneak.getKeyCode(), false); }
					}
					
					//control keys
					if (Minecraft.isRunningOnMac) {
						if (Keyboard.isKeyDown(219)) { checkKeys.add(219); }
						if (Keyboard.isKeyDown(220)) { checkKeys.add(220); }
					}
					else {
						if (Keyboard.isKeyDown(29)) { checkKeys.add(29); }
						if (Keyboard.isKeyDown(157)) { checkKeys.add(157); }
					}
					
					//shift keys
					if (Keyboard.isKeyDown(42)) { checkKeys.add(42); }
					if (Keyboard.isKeyDown(54)) { checkKeys.add(54); }
					
					//alt keys
					if (Keyboard.isKeyDown(56)) { checkKeys.add(56); }
					if (Keyboard.isKeyDown(184)) { checkKeys.add(184); }
					
					if (!isCtrl && !isShift && !isAlt) { checkKeys.add(keyCode); }
					
					synchronized (registeredHotKeys) {
						for (int i = 0; i < registeredHotKeys.size(); i++) {
							HotKey key = registeredHotKeys.get(i);
							if (key.getKeyCombo() != null) {
								if (key.getKeyCombo().checkKeys(checkKeys) && key.isEnabled()) {
									if (key.getHotKeyType() == KeyActionType.COMMANDSENDER || key.getHotKeyType() == KeyActionType.CONDITIONAL_COMMAND_ITEMTEST) {
										if (System.currentTimeMillis() - lastCommandKeyTime >= keyInputDelay.get()) { //limit inputs
											lastCommandKeyTime = System.currentTimeMillis();
											registeredHotKeys.get(i).executeHotKeyAction();
										}
									}
									else {
										registeredHotKeys.get(i).executeHotKeyAction();
									}
								}
							}
						}
					} //sync
					
				}
			}
			catch (Exception q) { q.printStackTrace(); }
		}
	}
	
	public List<HotKey> getRegisteredHotKeys() { return Collections.unmodifiableList(registeredHotKeys); }
	public List<HotKey> getAppHotKeys() { return Collections.unmodifiableList(appHotKeys); }
	public KeySaver getKeySaver() { return saver; }
	public KeyLoader getKeyLoader() { return loader; }
	public HotKeyBuilder getKeyBuilder() { return builder; }
	
	public boolean doesCtrlKeyStopMovement() { return stopMovement.get(); }
	public boolean runTutorial() { return runCreationTutorial.get(); }
	public boolean isExampleKeyMade() { return createExampleKey.get(); }
	public String getDefaultListSort() { return defaultListSort; }
	public HotKeyApp setStopMovementOnPress(boolean val) { stopMovement.set(val); return this; }
	public HotKeyApp setRunTutorial(boolean val) { runCreationTutorial.set(val); return this; }
	public HotKeyApp setCreateExampleKey(boolean val) { createExampleKey.set(val); return this; }
	public HotKeyApp setDefaultListSort(String sortIn) { defaultListSort = sortIn; return this; }
	
}
