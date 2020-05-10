package com.Whodundid.hotkeys.keySaveLoad;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import com.Whodundid.hotkeys.HotKeyApp;
import com.Whodundid.hotkeys.control.HotKey;
import com.Whodundid.hotkeys.control.hotKeyTypes.MessageSenderHotKey;
import com.Whodundid.hotkeys.control.hotKeyTypes.DebugHotKey;
import com.Whodundid.hotkeys.control.hotKeyTypes.GuiOpenerHotKey;
import com.Whodundid.hotkeys.control.hotKeyTypes.ItemTestMessageSenderHotKey;
import com.Whodundid.hotkeys.control.hotKeyTypes.KeyBindModifierHotKey;
import com.Whodundid.hotkeys.control.hotKeyTypes.ModActivatorHotKey;
import com.Whodundid.hotkeys.control.hotKeyTypes.ModDeactivatorHotKey;

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
			
			saveSubModKeys();
			saveHotKeys();
			
		} catch (IOException e) { e.printStackTrace(); }
		finally {
			if (saver != null) { saver.close(); }
		}
	}
	
	private void saveSubModKeys() throws FileNotFoundException, UnsupportedEncodingException {
		saver = new PrintWriter("EnhancedMC/HotKeys/SubModKeys.cfg", "UTF-8");
		
		saver.println("** SubMod Hot Keys");
		saver.println("** Key, keyCodes, isEnabled");
		saver.println();
		
		String previousType = "";
		
		if (man.getSubModHotKeys() != null && !man.getSubModHotKeys().isEmpty() && man.getSubModHotKeys().get(0) != null) {
			previousType = man.getSubModHotKeys().get(0).getBuiltInSubModType();
			saver.println("** " + previousType + " **");
			for (HotKey k : man.getSubModHotKeys()) {
				if (!k.getBuiltInSubModType().equals(previousType)) {
					previousType = k.getBuiltInSubModType();
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
		
		saver.println("** Hot Keys");
		saver.println();
		
		if (man.getRegisteredHotKeys() != null && !man.getRegisteredHotKeys().isEmpty()) {
			for (HotKey k : man.getRegisteredHotKeys()) {
				if (!k.isSubModKey()) {
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
					case MESSAGESENDER:
						saver.println("COMMAND " + ((MessageSenderHotKey)k).getMessage());
						break;
					case CONDITIONAL_MESSAGE_ITEMTEST:
						saver.println("COMMAND " + ((ItemTestMessageSenderHotKey)k).getMessage());
						saver.println("ITEM " + ((ItemTestMessageSenderHotKey)k).getItemID());
						break;
					case DEBUG:
						saver.println("DEBUGFUNC " + ((DebugHotKey)k).getDebugFunction());
						break;
					case GUI_OPENER:
						saver.println("GUI " + ((GuiOpenerHotKey)k).getGuiName());
						//String types = "";
						//for (int i = 0; i < ((GuiOpenerHotKey)k).getParamTypes().length; i++) {
						//	Class c = ((GuiOpenerHotKey)k).getParamTypes()[i];
						//	types += (c + ",");
						//}
						//saver.println("TYPES " + types);
						break;
					case MC_KEYBIND_MODIFIER:
						saver.println("KEYBIND " + ((KeyBindModifierHotKey)k).getKeyBinding().getKeyDescription() + " " + ((KeyBindModifierHotKey)k).getNewVal());
						break;
					case MOD_ACTIVATOR:
						saver.println("MOD " + ((ModActivatorHotKey)k).getSubMod());
						break;
					case MOD_DEACTIVATOR:
						saver.println("MOD " + ((ModDeactivatorHotKey)k).getSubMod());
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
