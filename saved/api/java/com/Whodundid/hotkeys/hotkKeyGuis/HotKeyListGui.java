package com.Whodundid.hotkeys.hotkKeyGuis;

import com.Whodundid.core.EnhancedMC;
import com.Whodundid.core.app.AppType;
import com.Whodundid.core.app.EMCApp;
import com.Whodundid.core.app.RegisteredApps;
import com.Whodundid.core.debug.IDebugCommand;
import com.Whodundid.core.enhancedGui.guiObjects.actionObjects.EGuiButton;
import com.Whodundid.core.enhancedGui.guiObjects.advancedObjects.textArea.EGuiTextArea;
import com.Whodundid.core.enhancedGui.guiObjects.advancedObjects.textArea.TextAreaLine;
import com.Whodundid.core.enhancedGui.guiObjects.basicObjects.EGuiLabel;
import com.Whodundid.core.enhancedGui.guiObjects.windows.EGuiDialogueBox;
import com.Whodundid.core.enhancedGui.guiObjects.windows.EGuiSelectionList;
import com.Whodundid.core.enhancedGui.guiObjects.windows.EGuiDialogueBox.DialogueBoxTypes;
import com.Whodundid.core.enhancedGui.types.WindowParent;
import com.Whodundid.core.enhancedGui.types.interfaces.IEnhancedActionObject;
import com.Whodundid.core.enhancedGui.types.interfaces.IEnhancedGuiObject;
import com.Whodundid.core.util.EUtil;
import com.Whodundid.core.util.renderUtil.CenterType;
import com.Whodundid.core.util.storageUtil.StorageBoxHolder;
import com.Whodundid.hotkeys.HotKeyApp;
import com.Whodundid.hotkeys.control.HotKey;
import com.Whodundid.hotkeys.control.hotKeyTypes.MessageSenderHotKey;
import com.Whodundid.hotkeys.control.hotKeyTypes.DebugHotKey;
import com.Whodundid.hotkeys.control.hotKeyTypes.GuiOpenerHotKey;
import com.Whodundid.hotkeys.control.hotKeyTypes.ItemTestMessageSenderHotKey;
import com.Whodundid.hotkeys.control.hotKeyTypes.ModActivatorHotKey;
import com.Whodundid.hotkeys.control.hotKeyTypes.ModDeactivatorHotKey;
import com.Whodundid.hotkeys.control.hotKeyUtil.KeyActionType;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.util.EnumChatFormatting;

//Last edited: Jan 7, 2019
//First Added: Jan 7, 2019
//Author: Hunter Bragg

public class HotKeyListGui extends WindowParent {
	
	HotKeyApp mod = (HotKeyApp) RegisteredApps.getApp(AppType.HOTKEYS);
	EGuiTextArea keyList;
	EGuiButton edit, sortList, delete, toggleEnabled;
	EGuiDialogueBox msgBox;
	EGuiSelectionList sortSelection;
	EMCApp selectedMod;
	HotKey currentKey;
	boolean hasCategory = false, hasArg1 = false, hasArg2 = false, hasDescription = false;
	String keyName = "";
	String keyType = "";
	String keys = "";
	String keyCategory = "";
	String keyArg1String = "", keyArg1 = "";
	String keyArg2String = "", keyArg2 = "";
	EGuiLabel desc;
	int listVerticalPos = 0;
	
	public HotKeyListGui() { super(); }
	public HotKeyListGui(EMCApp modIn) { super(); selectedMod = modIn; }
	public HotKeyListGui(Object oldGui) { super(oldGui); }
	public HotKeyListGui(Object oldGui, EMCApp modIn) { super(oldGui); selectedMod = modIn; }
	public HotKeyListGui(IEnhancedGuiObject parentIn, int posX, int posY) { super(parentIn, posX, posY); }
	public HotKeyListGui(IEnhancedGuiObject parentIn, int posX, int posY, EMCApp modIn) { super(parentIn, posX, posY); selectedMod = modIn; }
	public HotKeyListGui(IEnhancedGuiObject parentIn, int posX, int posY, GuiScreen oldGui) { super(parentIn, posX, posY, oldGui); }
	public HotKeyListGui(IEnhancedGuiObject parentIn, int posX, int posY, GuiScreen oldGui, EMCApp modIn) { super(parentIn, posX, posY, oldGui); selectedMod = modIn; }
	
	@Override
	public void initGui() {
		centerObjectWithSize(450, 275);
		setObjectName("Hotkey List");
		super.initGui();
	}
	
	@Override
	public void initObjects() {
		defaultHeader(this);
		
		edit = new EGuiButton(this, startX + 9, endY - 28, 70, 20, "Edit Key");
		delete = new EGuiButton(this, startX + 175, endY - 28, 70, 20, "Delete Key");
		toggleEnabled = new EGuiButton(this, startX + 92, endY - 28, 70, 20, "Enabled");
		sortList = new EGuiButton(this, endX - 178, endY - 28, 150, 20, "Sort list by..");
		desc = new EGuiLabel(this, startX + 28, startY + 205, "").enableWordWrap(true, 208);
		
		toggleEnabled.setEnabled(false);
		edit.setEnabled(false);
		delete.setEnabled(false);
		
		keyList = new EGuiTextArea(this, endX - 198, startY + 20, 190, 220).setDrawLineNumbers(true);
		
		addObject(edit, delete, toggleEnabled, sortList, keyList, desc);
		
		buildKeyList(mod.getDefaultListSort());
	}
	
	@Override
	public void drawObject(int mXIn, int mYIn) {
		drawDefaultBackground();
		
		drawCenteredStringWithShadow("Registered Hotkeys", endX - keyList.width / 2 - 8, startY + 7, 0xb2b2b2);
		drawCenteredStringWithShadow("Selected HotKey's Values", startX + 126, startY + 7, 0xb2b2b2);
		
		//draw hotkey value display container
		drawRect(startX + 9, startY + 20, startX + 245, endY - 35, 0xff000000);
		drawRect(startX + 10, startY + 21, startX + 244, endY - 36, 0xff2D2D2D);
		
		//draw separator lines
		if (keyList.getCurrentLine() != null && keyList.getCurrentLine().getStoredObj() != null) {
			HotKey k = (HotKey) keyList.getCurrentLine().getStoredObj();
			loadKeyValues(k);
			drawRect(startX + 10, startY + 48, startX + 244, startY + 49, 0xff000000);
			drawRect(startX + 10, startY + 76, startX + 244, startY + 77, 0xff000000);
			drawRect(startX + 10, startY + 104, startX + 244, startY + 105, 0xff000000);
			drawRect(startX + 10, startY + 132, startX + 244, startY + 133, 0xff000000);
			if (hasArg2) { drawRect(startX + 10, startY + 160, startX + 244, startY + 161, 0xff000000); }
			drawRect(startX + 10, startY + 188, startX + 244, startY + 189, 0xff000000);
		} else { resetValues(); }
		
		drawKeyValues();
		
		super.drawObject(mXIn, mYIn);
	}
	
	@Override
	public void actionPerformed(IEnhancedActionObject object, Object... args) {
		if (object.equals(edit)) {
			if (keyList.getCurrentLine() != null && keyList.getCurrentLine().getStoredObj() != null) {
				HotKey key = (HotKey) keyList.getCurrentLine().getStoredObj();
				EnhancedMC.displayWindow(new HotKeyCreatorGui(key), this);
			}
		}
		if (object.equals(delete)) {
			if (keyList.getCurrentLine() != null && keyList.getCurrentLine().getStoredObj() != null) {
				deleteKey((HotKey) keyList.getCurrentLine().getStoredObj());
			}
		}
		if (object.equals(toggleEnabled)) {
			if (keyList.getCurrentLine() != null && keyList.getCurrentLine().getStoredObj() != null) {
				HotKey k = (HotKey) keyList.getCurrentLine().getStoredObj();
				toggleEnabled(k);
			}
		}
		if (object.equals(sortList)) { openSortSelectionList(); }
		if (object.equals(sortSelection)) { if (sortSelection.getSelectedObject() instanceof String) { buildKeyList((String) sortSelection.getSelectedObject()); } }
	}
	
	protected void drawKeyValues() {
		if (!keyName.isEmpty()) { drawStringWithShadow("Name:", startX + 14, startY + 24, 0xffbb00); }
		else {
			drawCenteredStringWithShadow("Click on a hotkey from the registered", startX + 126, startY + 120, 0xffbb00);
			drawCenteredStringWithShadow("hotkeys list to see its values.", startX + 126, startY + 132, 0xffbb00);
		}
		if (!keyType.isEmpty()) {
			drawStringWithShadow("Type:", startX + 14, startY + 52, 0xffbb00);
			drawStringWithShadow("Keys:", startX + 14, startY + 80, 0xffbb00);
			drawStringWithShadow("Category:", startX + 14, startY + 108, 0xffbb00);
			if (!keyArg1String.isEmpty()) { drawStringWithShadow(keyArg1String, startX + 14, startY + 136, 0xffbb00); }
			else { drawStringWithShadow("", startX + 14, startY + 136, 0xb2b2b2); }
			if (!keyArg2String.isEmpty()) { drawStringWithShadow(keyArg2String, startX + 14, startY + 164, 0xffbb00); }
			else { drawStringWithShadow("", startX + 14, startY + 164, 0xb2b2b2); }
			drawStringWithShadow("Description:", startX + 14, startY + 192, 0xffbb00);
			
			drawStringWithShadow(keyName, startX + 28, startY + 37, 0x00ffdc);
			drawStringWithShadow(keyType, startX + 28, startY + 65, 0x00ffdc);
			if (!keys.isEmpty()) { drawStringWithShadow(keys, startX + 28, startY + 93, 0x00ffdc); }
			else { drawStringWithShadow("No keys set", startX + 28, startY + 93, 0xb2b2b2); }
			if (hasCategory) { drawStringWithShadow(keyCategory, startX + 28, startY + 121, 0x00ffdc); }
			else { drawStringWithShadow("No category", startX + 28, startY + 121, 0xb2b2b2); }
			if (hasArg1) { drawStringWithShadow(keyArg1, startX + 28, startY + 149, 0x00ffdc); }
			else { drawStringWithShadow("", startX + 28, startY + 149, 0xb2b2b2); }
			if (hasArg2) { drawStringWithShadow(keyArg2, startX + 28, startY + 177, 0x00ffdc); }
			else { drawStringWithShadow("", startX + 28, startY + 177, 0xb2b2b2); }
			if (!hasDescription) { drawStringWithShadow("No description set", startX + 28, startY + 205, 0xb2b2b2); }
		}
	}
	
	public void loadSubModsKeys(EMCApp modIn) {
		
	}
	
	public void loadKeyValues(HotKey keyIn) {
		try {
			resetValues();
			if (keyIn != null) {
				edit.setEnabled(true);
				delete.setEnabled(true);
				
				hasCategory = false; hasArg1 = false; hasArg2 = false; hasDescription = false;
				KeyActionType type = keyIn.getHotKeyType();
				
				keyName = keyIn.getKeyName();
				keyType = KeyActionType.getStringFromType(keyIn.getHotKeyType());
				
				if (keyIn.getKeyCombo() != null && keyIn.getKeyCombo().getKeys() != null) {
					keys = EUtil.keysToString(keyIn.getKeyCombo().getKeys());
				}
				
				switch (type) {
				case SUBMOD: break;
				case MESSAGESENDER:
					hasArg1 = true;
					keyArg1String = "Command:";
					keyArg1 = ((MessageSenderHotKey) keyIn).getMessage();
					break;
				case CONDITIONAL_MESSAGE_ITEMTEST:
					ItemTestMessageSenderHotKey k = (ItemTestMessageSenderHotKey) keyIn;
					hasArg1 = true;
					keyArg1String = "Command:";
					keyArg1 = k.getMessage();
					hasArg2 = true;
					keyArg2String = "Item id";
					keyArg2 = k.getItemID() + "";
					break;
				case DEBUG:
					hasArg1 = true;
					keyArg1String = "Debug Command:";
					keyArg1 = IDebugCommand.getDebugCommandName(((DebugHotKey) keyIn).getDebugFunction());
					break;
				case GUI_OPENER:
					hasArg1 = true;
					keyArg1String = "Gui to be opened:";
					keyArg1 = ((GuiOpenerHotKey) keyIn).getGuiDisplayName();
					break;
				case MC_KEYBIND_MODIFIER: break; //i don't know what i am doing with this yet
				case MOD_ACTIVATOR:
					hasArg1 = true;
					keyArg1String = "SubMod to be activated:";
					keyArg1 = AppType.getAppName(((ModActivatorHotKey) keyIn).getSubMod());
					break;	
				case MOD_DEACTIVATOR:
					hasArg1 = true;
					keyArg1String = "SubMod to be deactivated:";
					keyArg1 = AppType.getAppName(((ModDeactivatorHotKey) keyIn).getSubMod());
					break;
				//case SCRIPT:
				//	hasArg1 = true;
				//	keyArg1String = "Script to be run:";
				//	keyArg1 = ((ScriptHotKey) keyIn).getScript().getScriptName();
				//	hasArg2 = true;
				//	keyArg2String = "Script arguments:";
				//	keyArg2 = ((ScriptHotKey) keyIn).getScriptArgs() + "";
				//	break;
				case UNDEFINED: break;
				default: break;
				}
				
				if (keyIn.getKeyCategory() != null) {
					String catName = keyIn.getKeyCategory().getCategoryName();
					if (catName != null && !catName.equals("null") && !catName.equals("none")) {
						hasCategory = true;
						keyCategory = keyIn.getKeyCategory().getCategoryName();
					}
				}
				
				if (!keyIn.getKeyDescription().isEmpty() && !keyIn.getKeyDescription().equals("No description set.")) {
					hasDescription = true;
					desc.setDisplayString(keyIn.getKeyDescription()).setDisplayStringColor(0x00ffdc);
				}
				
				toggleEnabled.setEnabled(true);
				toggleEnabled.setDisplayString(keyIn.isEnabled() ? "Enabled" : "Disabled");
				toggleEnabled.setDisplayStringColor(keyIn.isEnabled() ? 0x55ff55 : 0xff5555);
			}
		} catch (Exception e) { e.printStackTrace(); }
	}
	
	public void resetValues() {
		edit.setEnabled(false);
		delete.setEnabled(false);
		keyName = "";
		keyType = "";
		keys = "";
		keyCategory = "";
		keyArg1String = "";
		keyArg2String = "";
		desc.setDisplayString("");
		toggleEnabled.setEnabled(false);
		toggleEnabled.setDisplayStringColor(EGuiButton.defaultColor);
	}
	
	private void toggleEnabled(HotKey key) {
		key.setEnabled(!key.isEnabled());
		resetValues();
		buildKeyList(mod.getDefaultListSort());
		TextAreaLine l = keyList.getLineWithObject(key);
		if (l != null) { keyList.setSelectedLine(l); }
		mod.saveHotKeys();
	}
	
	private void deleteKey(HotKey key) {
		if (key.isSubModKey()) {
			msgBox = new EGuiDialogueBox(DialogueBoxTypes.ok);
			msgBox.setTitle("HotKey Deletion Error");
			msgBox.setMessage("Cannot delete a SubMod's hotkey.").setMessageColor(0xff5555);
			guiInstance.addObject(msgBox);
		} else {
			msgBox = new EGuiDialogueBox(DialogueBoxTypes.yesNo) {
				@Override
				public void actionPerformed(IEnhancedActionObject object, Object... args) {
					if (object == yes) {
						this.close();
						if (mod.unregisterHotKey(key)) {
							mod.saveHotKeys();
							buildKeyList(mod.getDefaultListSort());
							//msgBox = new EGuiDialogueBox(guiInstance, midX - 125, midY - 48, 250, 75, DialogueBoxTypes.ok);
							//msgBox.setDisplayString("HotKey Deletion");
							//msgBox.setMessage("Sucessfully deleted hotkey: " + key.getKeyName() + ".").setMessageColor(0x55ff55);
							//guiInstance.addObject(msgBox);
						} else {
							msgBox = new EGuiDialogueBox(DialogueBoxTypes.ok);
							msgBox.setTitle("HotKey Deletion");
							msgBox.setMessage("Failed to delete hotkey: " + key.getKeyName() + "!").setMessageColor(0xff5555);
							guiInstance.addObject(msgBox);
						}
					}
					if (object == no) { this.close(); }
				}
			};
			msgBox.setTitle("HotKey Deletion");
			msgBox.setMessage("Are you sure you want to delete hotkey: " + key.getKeyName() + "?").setMessageColor(0xff5555);
			EnhancedMC.displayWindow(msgBox, CenterType.screen);
		}
	}
	
	private void openSortSelectionList() {
		StorageBoxHolder<String, String> list = new StorageBoxHolder();
		list.add("Is Enabled", "enabled");
		list.add("By SubMod", "mod");
		list.add("By Category", "category");
		list.add("Alphabetically (A-Z)", "nameup");
		list.add("Alphabetically (Z-A)", "namedown");
		sortSelection = new EGuiSelectionList(this, list);
		addObject(sortSelection);
	}
	
	protected void buildKeyList(String sortType) {
		keyList.clear();
		
		if (mod.getRegisteredHotKeys().isEmpty()) {
			keyList.addTextLine("No Hotkeys", 0xb2b2b2);
		}
		else {
			switch (sortType) {
			case "enabled": sortListByEnabled(); break;
			case "mod": sortListByMod(); break;
			case "category": sortListByCategory(); break;
			case "nameup": sortListByNameUp(); break;
			case "namedown": sortListByNameDown(); break;
			default: System.out.println("!! Unrecognized sort type !!"); break;
			}
		}
	}
	
	private void sortListByEnabled() {
		boolean anyDisabled = false, anyEnabled = false;
		for (HotKey k : mod.getRegisteredHotKeys()) { if (!k.isEnabled()) { anyDisabled = true; break; } }
		for (HotKey k : mod.getRegisteredHotKeys()) { if (k.isEnabled()) { anyEnabled = true; break; } }
		if (anyEnabled) { keyList.addTextLine("Enabled HotKeys:").setLineNumberColor(0xb2b2b2).setTextColor(0x00ffdc); }
		for (HotKey k : mod.getRegisteredHotKeys()) {
			if (k.isEnabled()) {
				TextAreaLine l = new TextAreaLine(keyList) {
					@Override
					public void onDoubleClick() {
						EnhancedMC.displayWindow(new HotKeyCreatorGui((HotKey) getStoredObj()), guiInstance);
					}
					@Override
					public void keyPressed(char typedChar, int keyCode) {
						super.keyPressed(typedChar, keyCode);
						if (keyCode == 28) { //enter
							EnhancedMC.displayWindow(new HotKeyCreatorGui((HotKey) getStoredObj()), guiInstance);
						}
					}
				};
				l.setText("   " + EnumChatFormatting.GREEN + k.getKeyName());
				l.setLineNumberColor(0xb2b2b2);
				l.setStoredObj(k);
				keyList.addTextLine(l);
			}
		}
		if (anyDisabled) { keyList.addTextLine("Disabled HotKeys:").setLineNumberColor(0xb2b2b2).setTextColor(0x00ffdc); }
		for (HotKey k : mod.getRegisteredHotKeys()) {
			if (!k.isEnabled()) {
				TextAreaLine l = new TextAreaLine(keyList) {
					@Override
					public void onDoubleClick() {
						EnhancedMC.displayWindow(new HotKeyCreatorGui((HotKey) getStoredObj()));
					}
					@Override
					public void keyPressed(char typedChar, int keyCode) {
						super.keyPressed(typedChar, keyCode);
						if (keyCode == 28) { //enter
							EnhancedMC.displayWindow(new HotKeyCreatorGui((HotKey) getStoredObj()));
						}
					}
				};
				l.setText("   " + EnumChatFormatting.RED + k.getKeyName());
				l.setLineNumberColor(0xb2b2b2);
				l.setStoredObj(k);
				keyList.addTextLine(l);
			}
		}
	}
	
	private void sortListByMod() {
		
	}
	
	private void sortListByCategory() {
		
	}
	
	private void sortListByNameUp() {
		
	}
	
	private void sortListByNameDown() {
		
	}
}
