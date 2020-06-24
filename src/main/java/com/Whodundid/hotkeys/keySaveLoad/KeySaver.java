package com.Whodundid.hotkeys.keySaveLoad;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import com.Whodundid.hotkeys.HotKeyApp;
import com.Whodundid.hotkeys.control.Hotkey;
import com.Whodundid.hotkeys.control.hotKeyTypes.CommandSenderHotkey;
import com.Whodundid.hotkeys.control.hotKeyTypes.DebugHotkey;
import com.Whodundid.hotkeys.control.hotKeyTypes.GuiOpenerHotkey;
import com.Whodundid.hotkeys.control.hotKeyTypes.ConditionalCommandSenderHotkey;
import com.Whodundid.hotkeys.control.hotKeyTypes.KeyBindModifierHotkey;
import com.Whodundid.hotkeys.control.hotKeyTypes.ModActivatorHotkey;
import com.Whodundid.hotkeys.control.hotKeyTypes.ModDeactivatorHotkey;

import net.minecraft.client.Minecraft;

//Sep 30, 2018
//Last edited: Feb 17, 2019
//Edit note: Added support for key categories
//First Added: Sep 25, 2018
//Author: Hunter Bragg

public class KeySaver {
	
	static Minecraft mc = Minecraft.getMinecraft();
	HotKeyApp man;
	private PrintWriter saver = null;
	
	public KeySaver(HotKeyApp manIn) {
		man = manIn;
	}
	
	public void saveKeysToFile() {
		try {
			File hotKeyFolder = new File("EnhancedMC/HotKeys/");
			
			if (!hotKeyFolder.exists()) { hotKeyFolder.mkdirs(); }
			
			saveAppKeys();
			saveHotKeys();
			
		} catch (IOException e) { e.printStackTrace(); }
		finally {
			if (saver != null) { saver.close(); }
		}
	}
	
	private void saveAppKeys() throws FileNotFoundException, UnsupportedEncodingException {
		saver = new PrintWriter("EnhancedMC/HotKeys/AppKeys.cfg", "UTF-8");
		
		saver.println("** App HotKeys");
		saver.println("** Key, keyCodes, isEnabled");
		saver.println();
		
		String previousType = "";
		
		if (man.getAppHotKeys() != null && !man.getAppHotKeys().isEmpty() && man.getAppHotKeys().get(0) != null) {
			previousType = man.getAppHotKeys().get(0).getBuiltInAppType();
			saver.println("** " + previousType + " **");
			for (Hotkey k : man.getAppHotKeys()) {
				if (!k.getBuiltInAppType().equals(previousType)) {
					previousType = k.getBuiltInAppType();
					saver.println();
					saver.println("** " + previousType + " **");
				}
				
				saver.print(k.getKeyName() + " ");
				for (int i = 0, size = k.getKeyCombo().getKeys().size(); i < size; i++) {
					int code = k.getKeyCombo().getKeys().get(i);
					saver.print((i == size - 1) ? code : code + ",");
				}
				saver.print(" " + k.isEnabled());
				saver.println();
			}
		}
		
		saver.print("END");
		
		if (saver != null) { saver.close(); }
	}
	
	public void saveHotKeys() throws FileNotFoundException, UnsupportedEncodingException {
		saver = new PrintWriter("EnhancedMC/HotKeys/KeyList" + ".cfg", "UTF-8");
		
		saver.println("** HotKeys");
		saver.println();
		
		if (man.getRegisteredHotKeys() != null && !man.getRegisteredHotKeys().isEmpty()) {
			for (Hotkey k : man.getRegisteredHotKeys()) {
				if (!k.isAppKey()) {
					saver.println("KEYDEF " + k.getKeyName());
					saver.println("KEYTYPE " + k.getHotKeyType());
					
					saver.print("KEYS ");
					for (int i = 0; i < k.getKeyCombo().getKeys().size(); i++) {
						int key = k.getKeyCombo().getKeys().get(i);
						saver.print((i == k.getKeyCombo().getKeys().size() - 1) ? key : key + ",");
					}
					saver.println();
					
					saver.println("KEYCATEGORY " + k.getKeyCategory().getCategoryName());
					saver.println("KEYDESC " + k.getKeyDescription());
					saver.println("KEYENABLED " + k.isEnabled());
					
					switch (k.getHotKeyType()) {
					case COMMANDSENDER:
						saver.println("COMMAND " + ((CommandSenderHotkey)k).getCommand());
						break;
					case CONDITIONAL_COMMAND_ITEMTEST:
						saver.println("COMMAND " + ((ConditionalCommandSenderHotkey)k).getCommand());
						saver.println("ITEM " + ((ConditionalCommandSenderHotkey)k).getItemID());
						break;
					case DEBUG:
						saver.println("DEBUGFUNC " + ((DebugHotkey)k).getDebugFunction());
						break;
					case GUI_OPENER:
						saver.println("GUI " + ((GuiOpenerHotkey)k).getGuiName());
						//String types = "";
						//for (int i = 0; i < ((GuiOpenerHotKey)k).getParamTypes().length; i++) {
						//	Class c = ((GuiOpenerHotKey)k).getParamTypes()[i];
						//	types += (c + ",");
						//}
						//saver.println("TYPES " + types);
						break;
					case MC_KEYBIND_MODIFIER:
						saver.println("KEYBIND " + ((KeyBindModifierHotkey)k).getKeyBinding().getKeyDescription() + " " + ((KeyBindModifierHotkey)k).getNewVal());
						break;
					case APP_ACTIVATOR:
						saver.println("MOD " + ((ModActivatorHotkey)k).getApp());
						break;
					case APP_DEACTIVATOR:
						saver.println("MOD " + ((ModDeactivatorHotkey)k).getApp());
						break;
					//case SCRIPT:
					//	saver.println("SCRIPT " + ((ScriptHotKey)k).getScript().getScriptName());
					//	saver.print("SCRIPTARGS ");
					//	for (int i = 0; i < ((ScriptHotKey)k).getScriptArgs().length; i++) {
					//		String arg = ((ScriptHotKey)k).getScriptArgs()[i];
					//		saver.print((i == ((ScriptHotKey)k).getScriptArgs().length - 1) ? arg : arg + ",");
					//	}
					//	saver.println();
					//	break;
					default: break;
					}
					saver.println("KEYDEFEND");
					saver.println();
				}
			}
		}
		
		saver.print("END");
		if (saver != null) { saver.close(); }
	}
	
}
