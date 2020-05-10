package com.Whodundid.hotkeys;

import com.Whodundid.core.EnhancedMC;
import com.Whodundid.core.app.AppType;
import com.Whodundid.core.app.EMCApp;
import com.Whodundid.core.util.storageUtil.EArrayList;
import com.Whodundid.hotkeys.control.HotKey;
import com.Whodundid.hotkeys.control.hotKeyTypes.MessageSenderHotKey;
import com.Whodundid.hotkeys.control.hotKeyUtil.KeyComboAction;
import com.Whodundid.hotkeys.hotkKeyGuis.HotKeyCreationStepByStep;
import com.Whodundid.hotkeys.hotkKeyGuis.HotKeyCreatorGui;
import com.Whodundid.hotkeys.hotkKeyGuis.HotKeyGuiMain;
import com.Whodundid.hotkeys.hotkKeyGuis.HotKeyListGui;
import com.Whodundid.hotkeys.hotkKeyGuis.RegisteredKeyVisualGui;
import com.Whodundid.hotkeys.keySaveLoad.HotKeyBuilder;
import com.Whodundid.hotkeys.keySaveLoad.KeyLoader;
import com.Whodundid.hotkeys.keySaveLoad.KeySaver;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent.KeyInputEvent;
import org.apache.logging.log4j.Level;
import org.lwjgl.input.Keyboard;

//Last edited: Oct 16, 2018
//First Added: Sep 14, 2018
//Author: Hunter Bragg

@Mod(modid = HotKeyApp.MODID, version = HotKeyApp.VERSION, name = HotKeyApp.NAME, dependencies = "required-after:enhancedmc")
public final class HotKeyApp extends EMCApp {
	
	public static final String MODID = "hotkeys";
	public static final String VERSION = "2.1";
	public static final String NAME = "Hotkeys";
	protected EArrayList<HotKey> registeredHotKeys;
	protected EArrayList<HotKey> subModHotKeys;
	protected KeySaver saver;
	protected KeyLoader loader;
	protected HotKeyBuilder builder;
	protected boolean stopMovement = true;
	protected boolean runCreationTutorial = true;
	protected boolean createExampleKey = true;
	protected String defaultListSort = "enabled";
	private static HotKeyApp instance;
	
	public HotKeyApp() {
		super(AppType.HOTKEYS);
		instance = this;
		shouldLoad = false;
		version = VERSION;
		author = "Whodundid";
		
		addDependency(AppType.CORE, "1.0");
		configManager.setMainConfig(new HotKeyConfig(this, "hotKeySettings"));
		setMainGui(new HotKeyGuiMain());
		addGui(new HotKeyCreationStepByStep(), new HotKeyCreatorGui(), new HotKeyListGui(), new RegisteredKeyVisualGui());
		setAliases("hotkeys", "hk");
		
		registeredHotKeys = new EArrayList();
		subModHotKeys = new EArrayList();
		
		saver = new KeySaver(this);
		loader = new KeyLoader(this);
		builder = new HotKeyBuilder(this);
	}
	
	public static HotKeyApp instance() { return instance; }
	
	@Override
	public void onPostInit(FMLPostInitializationEvent e) {
		loadHotKeys();
	}
	
	private void makeExampleKey() {
		registerHotKey(new MessageSenderHotKey("ExampleHotKey", new KeyComboAction(Keyboard.KEY_H), "Hello World!") {
			{
				setKeyDescription("This is an example of a MessageSender Hotkey. When pressed it types 'Hello World!' into the chat.");
				setKeyCategory("Example");
			}
		});
		createExampleKey = false;
		getConfig().saveMainConfig();
	}
	
	public boolean registerHotKey(HotKey keyIn) {
		if (keyIn != null) {
			synchronized (registeredHotKeys) {
				if (!doesListAlreadyContain(keyIn, registeredHotKeys)) {
					registeredHotKeys.add(keyIn);
					EnhancedMC.log(Level.INFO, "[EMC Hotkeys]: hotkey registered: " + keyIn.getKeyName());
				}
				if (keyIn.isSubModKey()) {
					if (!doesListAlreadyContain(keyIn, subModHotKeys)) { subModHotKeys.add(keyIn); return true; }
				}
			}
		}
		return false;
	}
	
	private boolean doesListAlreadyContain(HotKey keyIn, EArrayList<HotKey> list) {
		for (HotKey k : list) {
			if (k.getKeyName().equals(keyIn.getKeyName())) {
				if (k.getBuiltInSubModType().equals(keyIn.getBuiltInSubModType())) { return true; }
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
					if (!key.isSubModKey()) {
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
					if (!key.isSubModKey()) {
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
					if (!key.isSubModKey()) {
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
		for (HotKey k : subModHotKeys) { registerHotKey(k); }
		loader.loadKeysFromFile();
		if (createExampleKey) { makeExampleKey(); }
		for (HotKey k : loader.getLoadedKeys()) { registerHotKey(k); }
		saver.saveKeysToFile();
	}
	
	public void reset() {
		registeredHotKeys.clear();
		saver.saveKeysToFile();
		for (HotKey k : subModHotKeys) { registerHotKey(k); }
		if (createExampleKey) { makeExampleKey(); }
	}
	
	@Override
	public void keyEvent(KeyInputEvent e) {
		if (isEnabled()) {
			try {
				if (Keyboard.isCreated()) {
					if (Keyboard.getEventKeyState()) {
						int keyCode = Keyboard.getEventKey();
						EArrayList<Integer> checkKeys = new EArrayList();
						
						boolean isCtrl = (Minecraft.isRunningOnMac && (keyCode == 219 || keyCode == 220)) || keyCode == 29 || keyCode == 157;
						boolean isShift = keyCode == 42 || keyCode == 54;
						boolean isAlt = keyCode == 56 || keyCode == 184;
						
						boolean mcCtrl = GuiScreen.isCtrlKeyDown();
						
						if (stopMovement) {
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
						} else {
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
										registeredHotKeys.get(i).executeHotKeyAction();
										//System.out.println(registeredHotKeys.get(i).getKeyDescription());
									}
								}
							}
						}
					}
				}
			} catch (Exception q) { q.printStackTrace(); }
		}
	}
	
	public List<HotKey> getRegisteredHotKeys() { return Collections.unmodifiableList(registeredHotKeys); }
	public List<HotKey> getSubModHotKeys() { return Collections.unmodifiableList(subModHotKeys); }
	public KeySaver getKeySaver() { return saver; }
	public KeyLoader getKeyLoader() { return loader; }
	public HotKeyBuilder getKeyBuilder() { return builder; }
	
	public boolean doesCtrlKeyStopMovement() { return stopMovement; }
	public boolean runTutorial() { return runCreationTutorial; }
	public boolean isExampleKeyMade() { return createExampleKey; }
	public String getDefaultListSort() { return defaultListSort; }
	public HotKeyApp setStopMovementOnPress(boolean val) { stopMovement = val; return this; }
	public HotKeyApp setRunTutorial(boolean val) { runCreationTutorial = val; return this; }
	public HotKeyApp setCreateExampleKey(boolean val) { createExampleKey = val; return this; }
	public HotKeyApp setDefaultListSort(String sortIn) { defaultListSort = sortIn; return this; }
}
