package com.Whodundid.hotkeys.keySaveLoad;

import com.Whodundid.core.app.AppType;
import com.Whodundid.core.debug.IDebugCommand;
import com.Whodundid.core.util.storageUtil.EArrayList;
import com.Whodundid.hotkeys.HotKeyApp;
import com.Whodundid.hotkeys.control.HotKey;
import com.Whodundid.hotkeys.control.hotKeyUtil.KeyActionType;
import com.Whodundid.hotkeys.control.hotKeyUtil.KeyComboAction;
import java.io.File;
import java.util.Scanner;
import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;

//Oct 5, 2018
//Last edited: Feb 17, 2019
//Edit note: Added support for key categories
//First Added: Sep 25, 2018
//Author: Hunter Bragg

public class KeyLoader {
	
	static Minecraft mc = Minecraft.getMinecraft();
	HotKeyApp man;
	EArrayList<HotKey> loadedKeys;
	
	public KeyLoader(HotKeyApp manIn) {
		man = manIn;
		loadedKeys = new EArrayList();
	}
	
	public void loadKeysFromFile() {
		loadedKeys.clear();
		loadSubModKeys();
		loadUserMadeKeys();
	}
	
	private void loadSubModKeys() {
		File builtInKeys = new File("EnhancedMC/HotKeys/SudModKeys.cfg");
		if (builtInKeys.exists()) {
			boolean isEnd = false;
			String command, configLine;
			try (Scanner fileReader = new Scanner(builtInKeys)) {
				while (!isEnd && fileReader.hasNextLine()) {
					configLine = fileReader.nextLine();
					Scanner line = new Scanner(configLine);
					if (line.hasNext()) {
						command = line.next();
						
						if (command.equals("END")) { isEnd = true; }
						if (!command.equals("**")) {
							for (HotKey k : man.getSubModHotKeys()) {
								if (k.getKeyName().equals(command)) {
									String[] keysIn = line.next().split(",");
									int[] keyCodes = new int[keysIn.length];
									
									for (int i = 0; i < keysIn.length; i++) {
										keyCodes[i] = Integer.valueOf(keysIn[i]);
									}
									
									k.getKeyCombo().setKeys(keyCodes);
									
									k.setEnabled(Boolean.parseBoolean(line.next()));
									break;
								}
							}
						}
					}
					
					line.close();
				}
				fileReader.close();
			} catch (Exception e) { e.printStackTrace(); }
		} else {
			//try {
				//man.getKeySaver().saveHotKeys();
			//} catch (IOException e) { e.printStackTrace(); }
		}
	}
	
	private void loadUserMadeKeys() {
		File userMadeKeys = new File("EnhancedMC/HotKeys/KeyList.cfg");
		if (userMadeKeys.exists()) {
			
			HotKeyBuilder builder = man.getKeyBuilder();
			String keyName = "";
			KeyActionType keyType = KeyActionType.UNDEFINED;
			KeyComboAction keys = null;
			String keyCategory = "";
			String keyDesc = "";
			boolean keyEnabled = false;
			//EScript script = null;
			String readCommand = "";
			
			boolean isEnd = false;
			String command, configLine;
			try (Scanner fileReader = new Scanner(userMadeKeys)) {
				while (!isEnd && fileReader.hasNextLine()) {
					configLine = fileReader.nextLine();
					Scanner line = new Scanner(configLine);
					if (line.hasNext()) {
						command = line.next();
						switch (command) {
						case "**": break;
						case "END": isEnd = true; break;
						case "KEYDEF": while (line.hasNext()) { keyName += line.next(); } break;
						case "KEYTYPE": keyType = KeyActionType.getActionTypeFromString(line.next()); break;
						case "KEYS":
							String[] readKeys = line.next().split(",");
							int[] iKeys = new int[readKeys.length];
							for (int i = 0; i < readKeys.length; i++) { iKeys[i] = Integer.valueOf(readKeys[i]); }
							keys = new KeyComboAction(iKeys);
							builder.setBuilderKeys(keys);
							break;
						case "KEYCATEGORY": keyCategory = line.next(); break;
						case "KEYDESC":
							while (line.hasNext()) {
								String more = line.next();
								if (line.hasNext()) { keyDesc += (more + " "); } 
								else { keyDesc += more; }
							}
							break;
						case "KEYENABLED": keyEnabled = Boolean.parseBoolean(line.next()); break;
						case "COMMAND":
							while (line.hasNext()) {
								while (line.hasNext()) {
									String more = line.next();
									if (line.hasNext()) { readCommand += (more + " "); } 
									else { readCommand += more; }
								}
							}
							builder.setBuilderCommand(readCommand);
							break;
						case "ITEM": builder.setBuilderCommandAndItemArgs(readCommand, Integer.parseInt(line.next())); break;
						case "DEBUGFUNC": builder.setBuilderDebugCommand(IDebugCommand.valueOf(line.next())); break;
						case "GUI":
							Class<?> guiClass = Class.forName(line.next());
							builder.setBuilderGuiToBeOpened(guiClass);
							break;
						case "KEYBIND":
							String keyBindIn = line.next();
							KeyBinding keyBind = null;
							for (KeyBinding k : mc.gameSettings.keyBindings) {
								if (k.getKeyDescription().equals(keyBindIn)) {
									keyBind = k;
									break;
								}
							}
							builder.setBuilderKeyBindingIn(keyBind, Boolean.parseBoolean(line.next()));
							break;
						case "MOD": builder.setBuilderSubMod(AppType.getTypeFromString(line.next())); break;
						case "SCRIPT": break;
						//case "SCRIPTARGS": builder.setBuilderScriptToBeRun(script, line.next().split(",")); break;
						case "KEYDEFEND":
							builder.buildHotKey(keyName, keyCategory, keyDesc, keyEnabled, keyType);
							if (builder.getBuiltKey() != null) { loadedKeys.add(builder.getBuiltKey()); }
							
							builder.clearBuilderArgs();
							keyName = "";
							keyType = KeyActionType.UNDEFINED;
							keys = null;
							keyDesc = "";
							keyEnabled = false;
							//script = null;
							readCommand = "";
							break;
						}
					}
					line.close();
				}
				fileReader.close();
			} catch (Exception e) { e.printStackTrace(); }
		} else {
			//try {
				//man.getKeySaver().saveHotKeys();
			//} catch (IOException e) { e.printStackTrace(); }
		}
	}
	
	public EArrayList<HotKey> getLoadedKeys() { return loadedKeys; }
}
