package com.Whodundid.hotkeys.window;

import com.Whodundid.core.EnhancedMC;
import com.Whodundid.core.app.AppType;
import com.Whodundid.core.app.EMCApp;
import com.Whodundid.core.app.RegisteredApps;
import com.Whodundid.core.debug.IDebugCommand;
import com.Whodundid.core.util.EUtil;
import com.Whodundid.core.util.renderUtil.CenterType;
import com.Whodundid.core.util.renderUtil.EColors;
import com.Whodundid.core.windowLibrary.windowObjects.actionObjects.WindowButton;
import com.Whodundid.core.windowLibrary.windowObjects.advancedObjects.scrollList.WindowScrollList;
import com.Whodundid.core.windowLibrary.windowObjects.advancedObjects.textArea.TextAreaLine;
import com.Whodundid.core.windowLibrary.windowObjects.advancedObjects.textArea.WindowTextArea;
import com.Whodundid.core.windowLibrary.windowObjects.basicObjects.WindowLabel;
import com.Whodundid.core.windowLibrary.windowObjects.basicObjects.WindowRect;
import com.Whodundid.core.windowLibrary.windowObjects.windows.WindowDialogueBox;
import com.Whodundid.core.windowLibrary.windowObjects.windows.WindowSelectionList;
import com.Whodundid.core.windowLibrary.windowObjects.windows.WindowDialogueBox.DialogueBoxTypes;
import com.Whodundid.core.windowLibrary.windowTypes.WindowParent;
import com.Whodundid.core.windowLibrary.windowTypes.interfaces.IActionObject;
import com.Whodundid.hotkeys.HotKeyApp;
import com.Whodundid.hotkeys.control.Hotkey;
import com.Whodundid.hotkeys.control.KeyActionType;
import com.Whodundid.hotkeys.control.hotKeyTypes.CommandSenderHotkey;
import com.Whodundid.hotkeys.control.hotKeyTypes.DebugHotkey;
import com.Whodundid.hotkeys.control.hotKeyTypes.GuiOpenerHotkey;
import com.Whodundid.hotkeys.control.hotKeyTypes.ConditionalCommandSenderHotkey;
import com.Whodundid.hotkeys.control.hotKeyTypes.ModActivatorHotkey;
import com.Whodundid.hotkeys.control.hotKeyTypes.ModDeactivatorHotkey;
import com.Whodundid.hotkeys.util.HKResources;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.MathHelper;

//Last edited: Jan 7, 2019
//First Added: Jan 7, 2019
//Author: Hunter Bragg

public class HotKeyListWindow extends WindowParent {
	
	HotKeyApp mod = (HotKeyApp) RegisteredApps.getApp(AppType.HOTKEYS);
	WindowTextArea keyList;
	WindowScrollList valuesList;
	WindowButton edit, sortList, delete, toggleEnabled;
	WindowDialogueBox msgBox;
	WindowSelectionList sortSelection;
	EMCApp selectedMod;
	Hotkey currentKey;
	boolean hasCategory = false, hasArg1 = false, hasArg2 = false, hasDescription = false;
	WindowLabel keyName;
	WindowLabel keyType;
	WindowLabel keys;
	WindowLabel keyCategory;
	WindowLabel keyArg1String, keyArg1;
	WindowLabel keyArg2String, keyArg2;
	WindowLabel desc;
	int listVerticalPos = 0;
	Hotkey prev = null;
	
	private int vPos1 = 0, vPos2 = 0;
	private int hPos1 = 0, hPos2 = 0;
	private TextAreaLine line = null;
	
	public HotKeyListWindow() { this(null); }
	public HotKeyListWindow(EMCApp modIn) {
		super();
		selectedMod = modIn;
		aliases.add("hotkeylist", "keylist", "hklist");
		windowIcon = HKResources.iconList;
	}
	
	@Override
	public void initWindow() {
		setObjectName("Hotkey List");
		setDimensions(450, 275);
		setResizeable(true);
		setMinDims(341, 100);
		setMaximizable(true);
	}
	
	@Override
	public void initObjects() {
		defaultHeader(this);
		
		int keyW = (width - 24) / 3;
		
		keyList = new WindowTextArea(this, endX - keyW - 8, startY + 20, keyW, height - 50);
		valuesList = new WindowScrollList(this, startX + 9, keyList.startY, keyList.startX - 16 - startX, keyList.height);
		
		int bW = MathHelper.clamp_int(valuesList.width / 3, 0, 150);
		int enW = MathHelper.clamp_int((int) ((double) keyList.width / 1.5), 0, 150);
		int gap = keyList.width / 7;
		
		edit = new WindowButton(this, valuesList.midX - gap - bW, valuesList.endY + 5, bW, 20, "Edit Key");
		delete = new WindowButton(this, valuesList.midX + gap, valuesList.endY + 5, bW, 20, "Delete Key");
		toggleEnabled = new WindowButton(this, keyList.midX - enW / 2, valuesList.endY + 5, enW, 20, "Enabled");
		
		setEnabled(false, toggleEnabled, edit, delete);
		
		valuesList.setBackgroundColor(EColors.pdgray);
		valuesList.setDrawListObjects(prev != null);
		
		edit.setStringColor(EColors.yellow);
		delete.setStringColor(EColors.lred);
		
		buildValuesList();
		
		addObject(edit, delete, toggleEnabled, keyList, valuesList);
		
		buildKeyList(mod.getDefaultListSort());
	}
	
	@Override
	public void preReInit() {
		vPos1 = valuesList.getVScrollBar().getScrollPos();
		hPos1 = valuesList.getHScrollBar().getScrollPos();
		vPos2 = keyList.getVScrollBar().getScrollPos();
		hPos2 = keyList.getHScrollBar().getScrollPos();
		
		line = keyList.getCurrentLine();
	}
	
	@Override
	public void postReInit() {
		valuesList.getVScrollBar().setScrollBarPos(vPos1);
		valuesList.getHScrollBar().setScrollBarPos(hPos1);
		keyList.getVScrollBar().setScrollBarPos(vPos2);
		keyList.getHScrollBar().setScrollBarPos(hPos2);
		
		if (line != null) {
			keyList.setSelectedLine(keyList.getLineWithTextAndObject(line.getText(), line.getStoredObj()));
			loadKeyValues(currentKey);
		}
	}
	
	private void buildValuesList() {
		int over = 4;
		int offset = 5;
		int gap = 9;
		int hgap = 3;
		int rY = (hgap) / 2 + 1;
		
		WindowLabel keyNameLabel = new WindowLabel(valuesList, over, 4, "Name:", EColors.lorange);
		keyName = new WindowLabel(valuesList, keyNameLabel.startX + offset, keyNameLabel.endY + hgap, "", EColors.aquamarine);
		
		WindowLabel typeLabel = new WindowLabel(valuesList, over, keyName.endY + gap, "Type:", EColors.lorange);
		keyType = new WindowLabel(valuesList, typeLabel.startX + offset, typeLabel.endY + hgap, "", EColors.aquamarine);
		
		WindowLabel keysLabel = new WindowLabel(valuesList, over, keyType.endY + gap, "Keys:", EColors.lorange);
		keys = new WindowLabel(valuesList, keysLabel.startX + offset, keysLabel.endY + hgap, "", EColors.aquamarine);
		
		keyArg1String = new WindowLabel(valuesList, over, keys.endY + gap, "", EColors.lorange);
		keyArg1 = new WindowLabel(valuesList, keyArg1String.startX + offset, keyArg1String.endY + hgap, "", EColors.aquamarine);
		
		keyArg2String = new WindowLabel(valuesList, over, keyArg1.endY + gap, "", EColors.lorange);
		keyArg2 = new WindowLabel(valuesList, keyArg1String.startX + offset, keyArg2String.endY + hgap, "", EColors.aquamarine);
		
		WindowLabel categoryLabel = new WindowLabel(valuesList, over, keyArg2.endY + gap, "Category:", EColors.lorange);
		keyCategory = new WindowLabel(valuesList, categoryLabel.startX + offset, categoryLabel.endY + hgap, "No category", EColors.aquamarine);
		
		WindowLabel descLabel = new WindowLabel(valuesList, over, keyCategory.endY + gap, "Description:", EColors.lorange);
		desc = new WindowLabel(this, descLabel.startX + over, descLabel.endY + hgap, "", EColors.aquamarine).enableWordWrap(true, valuesList.getListDimensions().width - 20);
		
		valuesList.fitItemsInList(4, 4);
		int eX = res.getScaledWidth();
		
		WindowRect nameRect = new WindowRect(valuesList, 0, keyName.endY + rY, eX, keyName.endY + rY + 1, EColors.black);
		WindowRect typeRect = new WindowRect(valuesList, 0, keyType.endY + rY, eX, keyType.endY + rY + 1, EColors.black);
		WindowRect keysRect = new WindowRect(valuesList, 0, keys.endY + rY, eX, keys.endY + rY + 1, EColors.black);
		WindowRect arg1Rect = new WindowRect(valuesList, 0, keyArg1.endY + rY, eX, keyArg1.endY + rY + 1, EColors.black);
		WindowRect arg2Rect = new WindowRect(valuesList, 0, keyArg2.endY + rY, eX, keyArg2.endY + rY + 1, EColors.black);
		WindowRect catRect = new WindowRect(valuesList, 0, keyCategory.endY + rY, eX, keyCategory.endY + rY + 1, EColors.black);
		
		arg1Rect.setVisible(!keyArg2.isEmpty());
		
		valuesList.addToIgnoreList(nameRect, typeRect, keysRect, catRect, arg1Rect, arg2Rect);
		
		valuesList.addObjectToList(keyNameLabel, keyName, nameRect);
		valuesList.addObjectToList(typeLabel, keyType, typeRect);
		valuesList.addObjectToList(keysLabel, keys, keysRect);
		valuesList.addObjectToList(keyArg1String, keyArg1, arg1Rect);
		valuesList.addObjectToList(keyArg2String, keyArg2, arg2Rect);
		valuesList.addObjectToList(categoryLabel, keyCategory, catRect);
		valuesList.addObjectToList(descLabel, desc);
	}
	
	protected void updateKeyValues() {
		valuesList.setDrawListObjects(!keyName.isEmpty());
		
		if (!valuesList.getDrawListObjects()) {
			drawStringCS("Click on a hotkey from the", valuesList.midX, valuesList.midY - 11, EColors.orange);
			drawStringCS("list to see its values..", valuesList.midX, valuesList.midY, EColors.orange);
		}
	}
	
	@Override
	public void drawObject(int mXIn, int mYIn) {
		drawDefaultBackground();
		
		drawStringCS("Registered Hotkeys", keyList.midX, startY + 7, 0xb2b2b2);
		drawStringCS("Selected HotKey's Values", valuesList.midX, startY + 7, 0xb2b2b2);
		
		if (keyList.getCurrentLine() == null) { prev = null; }
		
		//draw separator lines
		if (keyList.getCurrentLine() != null && keyList.getCurrentLine().getStoredObj() != null) {
			Hotkey k = (Hotkey) keyList.getCurrentLine().getStoredObj();
			if (k != prev) {
				prev = k;
				currentKey = k;
				loadKeyValues(k);
			}
		}
		else { resetValues(); }
		
		super.drawObject(mXIn, mYIn);
		
		updateKeyValues();
	}
	
	@Override
	public void actionPerformed(IActionObject object, Object... args) {
		if (object.equals(edit)) {
			if (keyList.getCurrentLine() != null && keyList.getCurrentLine().getStoredObj() != null) {
				Hotkey key = (Hotkey) keyList.getCurrentLine().getStoredObj();
				EnhancedMC.displayWindow(new HotKeyCreatorWindow(key));
			}
		}
		if (object.equals(delete)) {
			if (keyList.getCurrentLine() != null && keyList.getCurrentLine().getStoredObj() != null) {
				deleteKey((Hotkey) keyList.getCurrentLine().getStoredObj());
			}
		}
		if (object.equals(toggleEnabled)) {
			if (keyList.getCurrentLine() != null && keyList.getCurrentLine().getStoredObj() != null) {
				Hotkey k = (Hotkey) keyList.getCurrentLine().getStoredObj();
				toggleEnabled(k);
			}
		}
		if (object.equals(sortList)) { openSortSelectionList(); }
		if (object.equals(sortSelection)) { if (sortSelection.getSelectedObject() instanceof String) { buildKeyList((String) sortSelection.getSelectedObject()); } }
	}
	
	public void loadAppKeys(EMCApp modIn) {
		
	}
	
	public void loadKeyValues(Hotkey keyIn) {
		try {
			resetValues();
			if (keyIn != null) {
				edit.setEnabled(true);
				delete.setEnabled(true);
				
				hasCategory = false; hasArg1 = false; hasArg2 = false; hasDescription = false;
				KeyActionType type = keyIn.getHotKeyType();
				
				keyName.setString(keyIn.getKeyName());
				keyType.setString(KeyActionType.getStringFromType(keyIn.getHotKeyType()));
				
				if (keyIn.getKeyCombo() != null && keyIn.getKeyCombo().getKeys() != null) {
					keys.setString(EUtil.keysToString(keyIn.getKeyCombo().getKeys()));
				}
				
				switch (type) {
				case APP: break;
				case COMMANDSENDER:
					hasArg1 = true;
					keyArg1String.setString("Command:");
					keyArg1.setString(((CommandSenderHotkey) keyIn).getCommand());
					break;
				case CONDITIONAL_COMMAND_ITEMTEST:
					ConditionalCommandSenderHotkey k = (ConditionalCommandSenderHotkey) keyIn;
					hasArg1 = true;
					keyArg1String.setString("Command:");
					keyArg1.setString(k.getCommand());
					hasArg2 = true;
					keyArg2String.setString("Item id");
					keyArg2.setString(k.getItemID() + "");
					break;
				case DEBUG:
					hasArg1 = true;
					keyArg1String.setString("Debug Command:");
					keyArg1.setString(IDebugCommand.getDebugCommandName(((DebugHotkey) keyIn).getDebugFunction()));
					break;
				case GUI_OPENER:
					hasArg1 = true;
					keyArg1String.setString("Gui to be opened:");
					keyArg1.setString(((GuiOpenerHotkey) keyIn).getGuiDisplayName());
					break;
				case MC_KEYBIND_MODIFIER: break; //i don't know what i am doing with this yet
				case APP_ACTIVATOR:
					hasArg1 = true;
					keyArg1String.setString("App to be activated:");
					keyArg1.setString(AppType.getAppName(((ModActivatorHotkey) keyIn).getApp()));
					break;
				case APP_DEACTIVATOR:
					hasArg1 = true;
					keyArg1String.setString("App to be deactivated:");
					keyArg1.setString(AppType.getAppName(((ModDeactivatorHotkey) keyIn).getApp()));
					break;
				case UNDEFINED: break;
				default: break;
				}
				
				if (keyIn.getKeyCategory() != null) {
					String catName = keyIn.getKeyCategory().getCategoryName();
					if (catName != null && !catName.equals("null") && !catName.equals("none")) {
						hasCategory = true;
						keyCategory.setString(keyIn.getKeyCategory().getCategoryName());
						keyCategory.setColor(EColors.aquamarine);
					}
					else {
						keyCategory.setString("No category.").setColor(EColors.lgray);
					}
				}
				
				if (!keyIn.getKeyDescription().isEmpty() && !keyIn.getKeyDescription().equals("No description set.")) {
					hasDescription = true;
					desc.setString(keyIn.getKeyDescription());
					desc.setColor(EColors.aquamarine);
					
				}
				else { desc.setString("No description."); desc.setColor(EColors.lgray); }
				
				toggleEnabled.setEnabled(true);
				toggleEnabled.setString(keyIn.isEnabled() ? "Enabled" : "Disabled");
				toggleEnabled.setStringColor(keyIn.isEnabled() ? 0x55ff55 : 0xff5555);
				
				//vPos1 = valuesList.getVScrollBar().getScrollPos();
				//hPos1 = valuesList.getHScrollBar().getScrollPos();
				valuesList.fitItemsInList(4, 2);
				//valuesList.getVScrollBar().setScrollBarPos(vPos1);
				//valuesList.getHScrollBar().setScrollBarPos(hPos1);
			}
		}
		catch (Exception e) { e.printStackTrace(); }
	}
	
	public void resetValues() {
		edit.setEnabled(false);
		delete.setEnabled(false);
		keyName.clear();
		keyType.clear();
		keys.clear();
		keyCategory.clear();
		keyArg1String.clear();
		keyArg2String.clear();
		desc.clear();
		toggleEnabled.setEnabled(false);
		toggleEnabled.setString("No Key");
		toggleEnabled.setStringColor(EColors.lgray);
	}
	
	private void toggleEnabled(Hotkey key) {
		key.setEnabled(!key.isEnabled());
		resetValues();
		buildKeyList(mod.getDefaultListSort());
		TextAreaLine l = keyList.getLineWithObject(key);
		if (l != null) { keyList.setSelectedLine(l); }
		mod.saveHotKeys();
	}
	
	private void deleteKey(Hotkey key) {
		if (key.isAppKey()) {
			msgBox = new WindowDialogueBox(DialogueBoxTypes.ok);
			msgBox.setTitle("HotKey Deletion Error");
			msgBox.setTitleColor(EColors.lgray.intVal);
			msgBox.setMessage("Cannot delete an EMC App's hotkey.").setMessageColor(0xff5555);
			EnhancedMC.displayWindow(msgBox);
			getTopParent().setFocusLockObject(msgBox);
		}
		else {
			msgBox = new WindowDialogueBox(DialogueBoxTypes.yesNo) {
				@Override
				public void actionPerformed(IActionObject object, Object... args) {
					if (object == yes) {
						close();
						if (mod.unregisterHotKey(key)) {
							mod.saveHotKeys();
							buildKeyList(mod.getDefaultListSort());
						}
						else {
							msgBox = new WindowDialogueBox(DialogueBoxTypes.ok);
							msgBox.setTitle("HotKey Deletion");
							msgBox.setTitleColor(EColors.lgray.intVal);
							msgBox.setMessage("Failed to delete hotkey: " + EnumChatFormatting.YELLOW +  key.getKeyName()).setMessageColor(0xff5555);
							EnhancedMC.displayWindow(msgBox);
							getTopParent().setFocusLockObject(msgBox);
						}
					}
					if (object == no) { close(); }
				}
			};
			msgBox.setTitle("Hotkey Deletion");
			msgBox.setTitleColor(EColors.lgray.intVal);
			msgBox.setMessage("Are you sure you want to delete hotkey: " + EnumChatFormatting.YELLOW + key.getKeyName()).setMessageColor(0xff5555);
			EnhancedMC.displayWindow(msgBox, CenterType.screen);
			getTopParent().setFocusLockObject(msgBox);
		}
	}
	
	private void openSortSelectionList() {
		sortSelection = new WindowSelectionList(this);
		sortSelection.addOption("Is Enabled", "enabled");
		sortSelection.addOption("By App", "app");
		sortSelection.addOption("By Category", "category");
		sortSelection.addOption("Alphabetically (A-Z)", "nameup");
		sortSelection.addOption("Alphabetically (Z-A)", "namedown");
		EnhancedMC.displayWindow(sortSelection, this, true, false, false, CenterType.object);
	}
	
	protected void buildKeyList(String sortType) {
		keyList.clear();
		
		if (mod.getRegisteredHotKeys().isEmpty()) {
			keyList.addTextLine("No Hotkeys", 0xb2b2b2);
		}
		else {
			switch (sortType) {
			case "enabled": sortListByEnabled(); break;
			case "app": sortListByApp(); break;
			case "category": sortListByCategory(); break;
			case "nameup": sortListByNameUp(); break;
			case "namedown": sortListByNameDown(); break;
			default: System.out.println("!! Unrecognized sort type !!"); break;
			}
		}
	}
	
	private void sortListByEnabled() {
		boolean anyDisabled = false, anyEnabled = false;
		for (Hotkey k : mod.getRegisteredHotKeys()) { if (!k.isEnabled()) { anyDisabled = true; break; } }
		for (Hotkey k : mod.getRegisteredHotKeys()) { if (k.isEnabled()) { anyEnabled = true; break; } }
		if (anyEnabled) { keyList.addTextLine("Enabled Hotkeys:").setLineNumberColor(0xb2b2b2).setTextColor(0x00ffdc); }
		for (Hotkey k : mod.getRegisteredHotKeys()) {
			if (k.isEnabled()) {
				TextAreaLine l = new TextAreaLine(keyList) {
					@Override
					public void onDoubleClick() {
						EnhancedMC.displayWindow(new HotKeyCreatorWindow((Hotkey) getStoredObj()));
					}
					
					@Override
					public void keyPressed(char typedChar, int keyCode) {
						super.keyPressed(typedChar, keyCode);
						if (keyCode == 28) { //enter
							EnhancedMC.displayWindow(new HotKeyCreatorWindow((Hotkey) getStoredObj()));
						}
					}
				};
				l.setText("   " + EnumChatFormatting.GREEN + k.getKeyName());
				l.setLineNumberColor(0xb2b2b2);
				l.setStoredObj(k);
				keyList.addTextLine(l);
			}
		}
		if (anyDisabled) { keyList.addTextLine("Disabled Hotkeys:").setLineNumberColor(0xb2b2b2).setTextColor(0x00ffdc); }
		for (Hotkey k : mod.getRegisteredHotKeys()) {
			if (!k.isEnabled()) {
				TextAreaLine l = new TextAreaLine(keyList) {
					@Override
					public void onDoubleClick() {
						EnhancedMC.displayWindow(new HotKeyCreatorWindow((Hotkey) getStoredObj()));
					}
					@Override
					public void keyPressed(char typedChar, int keyCode) {
						super.keyPressed(typedChar, keyCode);
						if (keyCode == 28) { //enter
							EnhancedMC.displayWindow(new HotKeyCreatorWindow((Hotkey) getStoredObj()));
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
	
	private void sortListByApp() {
		
	}
	
	private void sortListByCategory() {
		
	}
	
	private void sortListByNameUp() {
		
	}
	
	private void sortListByNameDown() {
		
	}
	
}
