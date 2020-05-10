package com.Whodundid.hotkeys.hotkKeyGuis;

import com.Whodundid.core.EnhancedMC;
import com.Whodundid.core.app.AppType;
import com.Whodundid.core.app.RegisteredApps;
import com.Whodundid.core.coreApp.CoreApp;
import com.Whodundid.core.debug.ExperimentGui;
import com.Whodundid.core.debug.IDebugCommand;
import com.Whodundid.core.enhancedGui.guiObjects.actionObjects.EGuiButton;
import com.Whodundid.core.enhancedGui.guiObjects.actionObjects.EGuiTextField;
import com.Whodundid.core.enhancedGui.guiObjects.advancedObjects.dropDownList.EGuiDropDownList;
import com.Whodundid.core.enhancedGui.guiObjects.windows.EGuiDialogueBox;
import com.Whodundid.core.enhancedGui.guiObjects.windows.EGuiSelectionList;
import com.Whodundid.core.enhancedGui.guiObjects.windows.EGuiDialogueBox.DialogueBoxTypes;
import com.Whodundid.core.enhancedGui.types.WindowParent;
import com.Whodundid.core.enhancedGui.types.interfaces.IEnhancedActionObject;
import com.Whodundid.core.enhancedGui.types.interfaces.IEnhancedGuiObject;
import com.Whodundid.core.terminal.gui.ETerminal;
import com.Whodundid.core.util.guiUtil.CommonVanillaGuis;
import com.Whodundid.core.util.renderUtil.CenterType;
import com.Whodundid.core.util.storageUtil.StorageBox;
import com.Whodundid.core.util.storageUtil.StorageBoxHolder;
import com.Whodundid.hotkeys.HotKeyApp;
import com.Whodundid.hotkeys.control.HotKey;
import com.Whodundid.hotkeys.control.hotKeyTypes.MessageSenderHotKey;
import com.Whodundid.hotkeys.control.hotKeyTypes.DebugHotKey;
import com.Whodundid.hotkeys.control.hotKeyTypes.GuiOpenerHotKey;
import com.Whodundid.hotkeys.control.hotKeyTypes.ItemTestMessageSenderHotKey;
import com.Whodundid.hotkeys.control.hotKeyTypes.KeyCategoryActivatorHotKey;
import com.Whodundid.hotkeys.control.hotKeyTypes.KeyCategoryDeactivatorHotKey;
import com.Whodundid.hotkeys.control.hotKeyTypes.ModActivatorHotKey;
import com.Whodundid.hotkeys.control.hotKeyTypes.ModDeactivatorHotKey;
import com.Whodundid.hotkeys.control.hotKeyUtil.*;
import com.Whodundid.hotkeys.hotkKeyGuis.util.KeyEntryTextField;
import com.Whodundid.hotkeys.keySaveLoad.HotKeyBuilder;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.util.EnumChatFormatting;

//Jan 9, 2019
//Last edited: Feb 17, 2019
//Edit note: Added support for key categories
//First Added: Sep 14, 2018
//Author: Hunter Bragg

public class HotKeyCreatorGui extends WindowParent {
	
	HotKeyApp man = (HotKeyApp) RegisteredApps.getApp(AppType.HOTKEYS);
	EGuiButton create, back, setEnabled, selectType, selectArg1, stepByStep;
	EGuiTextField keyNameEntry, mainArgEntry, secondaryArgEntry, categoryEntry, descriptionEntry;
	KeyEntryTextField keysEntry;
	EGuiDropDownList trueFalseList;
	EGuiSelectionList selectionList;
	EGuiDialogueBox msgBox;
	HotKey key;
	KeyComboAction keys;
	KeyActionType selectedHotKeyType;
	Class selectedGui;
	//EScript selectedScript;
	IDebugCommand selectedDebug;
	AppType selectedMod;
	KeyBinding selectedKeyBind;
	boolean enabledVal = true;
	String originalKeyName = "";
	StorageBox<Class[], Object[]> additionalGuiArgs = null;
	
	public HotKeyCreatorGui() { super(); }
	public HotKeyCreatorGui(HotKey keyIn) { super(); key = keyIn; }
	public HotKeyCreatorGui(Object oldGui) { super(oldGui); }
	public HotKeyCreatorGui(Object oldGui, HotKey keyIn) { super(oldGui); key = keyIn; }
	public HotKeyCreatorGui(IEnhancedGuiObject parentIn, int posX, int posY) { super(parentIn, posX, posY); }
	public HotKeyCreatorGui(IEnhancedGuiObject parentIn, int posX, int posY, HotKey keyIn) { super(parentIn, posX, posY); key = keyIn; }
	public HotKeyCreatorGui(IEnhancedGuiObject parentIn, int posX, int posY, Object oldGui) { super(parentIn, posX, posY, oldGui); }
	public HotKeyCreatorGui(IEnhancedGuiObject parentIn, int posX, int posY, Object oldGui, HotKey keyIn) { super(parentIn, posX, posY, oldGui); key = keyIn; }
	
	protected enum SelectionType { Type, Gui, Script, Debug, Mod, Keybind; }
	
	@Override
	public void initGui() {
		centerObjectWithSize(256, 324);
		setObjectName("Hotkey Creator");
		super.initGui();
	}
	
	@Override
	public void initObjects() {
		defaultHeader(this);
		//trueFalseList = new EGuiDropDownList(this, wPos - 91, hPos + 37, 17).setFixedWidth(true, 70);
		
		keyNameEntry = new EGuiTextField(this, startX + 23, startY + 38, width - 48, 13).setTextWhenEmpty("enter a key name");
		selectType = new EGuiButton(this, midX - (width - 66) / 2, startY + 74, width - 66, 17, "Select a type");
		keysEntry = new KeyEntryTextField(this, startX + 23, startY + 114, width - 48, 13);
		selectArg1 = new EGuiButton(this, startX + 23, startY + 149, 140, 17, "Arg1");
		mainArgEntry = new EGuiTextField(this, startX + 23, startY + 152, width - 48, 13).setMaxStringLength(100);
		secondaryArgEntry = new EGuiTextField(this, startX + 23, startY + 190, width - 48, 13).setMaxStringLength(200);
		categoryEntry = new EGuiTextField(this, startX + 23, startY + 228, width - 48, 13).setMaxStringLength(20).setTextWhenEmpty("enter a category name");
		descriptionEntry = new EGuiTextField(this, startX + 23, startY + 266, width - 48, 13).setMaxStringLength(200).setTextWhenEmpty("enter a hotkey description");
		
		create = new EGuiButton(this, startX + 9, endY - 28, 60, 20, key != null ? "Edit" : "Create");
		setEnabled = new EGuiButton(this, midX - 30, endY - 28, 60, 20, key != null ? key.isEnabled() ? "Enabled" : "Disabled" : "Enabled");
		back = new EGuiButton(this, endX - 69, endY - 28, 60, 20, "Back");
		
		setEnabled.setDisplayStringColor(key != null ? key.isEnabled() ? 0x55ff55 : 0xff5555 : 0x55ff55);
		keysEntry.setTextWhenEmpty("enter keys");
		
		//trueFalseList.addListEntry(new DropDownListEntry<Boolean>(Boolean.TRUE.toString(), Boolean.TRUE));
		//trueFalseList.addListEntry(new DropDownListEntry<Boolean>(Boolean.FALSE.toString(), Boolean.FALSE));
		
		setEnabled.setVisible(true);
		
		//addObject(trueFalseList);
		addObject(keyNameEntry, keysEntry, mainArgEntry, secondaryArgEntry, categoryEntry, descriptionEntry, setEnabled);
		addObject(create, selectType, selectArg1, back);
		
		if (key != null) { loadKeyValues(key); }
		else { updateVisibleObjects(); }
	}

	@Override
	public void drawObject(int mXIn, int mYIn) {
		drawDefaultBackground();
		
		drawCenteredStringWithShadow("Hotkey Creation", midX, startY + 8, 0xb2b2b2);
		
		//draw container
		drawRect(startX + 9, startY + 20, endX - 9, endY - 35, 0xff000000);
		drawRect(startX + 10, startY + 21, endX - 10, endY - 36, 0xff2D2D2D);
		
		//draw separation lines
		drawRect(startX + 10, startY + 58, endX - 9, startY + 59, 0xff000000);
		drawRect(startX + 10, startY + 96, endX - 9, startY + 97, 0xff000000);
		drawRect(startX + 10, startY + 134, endX - 9, startY + 135, 0xff000000);
		drawRect(startX + 10, startY + 172, endX - 9, startY + 173, 0xff000000);
		drawRect(startX + 10, startY + 210, endX - 9, startY + 211, 0xff000000);
		drawRect(startX + 10, startY + 248, endX - 9, startY + 249, 0xff000000);
		
		//draw separation titles
		drawBuilder(mXIn, mYIn);
		
		super.drawObject(mXIn, mYIn);
	}
	
	private void drawBuilder(int mX, int mY) {
		drawStringWithShadow("Name:", startX + 13, startY + 24, 0xffd800);
		drawStringWithShadow("Type:", startX + 13, startY + 62, 0xffd800);
		drawStringWithShadow("Keys:", startX + 13, startY + 100, 0xffd800);
		
		int firstArgColor = 0xb2b2b2, secondArgColor = 0xb2b2b2;
		String firstArgName = "Key Data Entry", secondArgName = "Extra Data Entry";
		if (selectedHotKeyType != null) {
			switch (selectedHotKeyType) {
			case MESSAGESENDER: firstArgName = "Message:"; firstArgColor = 0xffd800; break;
			case CONDITIONAL_MESSAGE_ITEMTEST: firstArgName = "Message:"; secondArgName = "Test Item ID:"; firstArgColor = 0xffd800; secondArgColor = 0xffd800; break;
			case DEBUG: firstArgName = "Debug Command:"; firstArgColor = 0xffd800; break;
			case GUI_OPENER: firstArgName = "Gui to be opened:"; firstArgColor = 0xffd800; break;
			case MC_KEYBIND_MODIFIER: firstArgName = "MC KeyBind:"; secondArgName = "New KeyBind Value: (true / false)"; firstArgColor = 0xffd800; secondArgColor = 0xffd800; break;
			case CATEGORY_ACTIVATOR: firstArgName = "Hotkey category to activate:"; firstArgColor = 0xffd800; break;
			case CATEGORY_DEACTIVATOR: firstArgName = "Hotkey category to deactivate:"; firstArgColor = 0xffd800; break;
			case MOD_ACTIVATOR: firstArgName = "Mod to activate:"; firstArgColor = 0xffd800; break;
			case MOD_DEACTIVATOR: firstArgName = "Mod to deactivate:"; firstArgColor = 0xffd800; break;
			//case SCRIPT: firstArgName = "Script:"; secondArgName = "Script Arguments:"; firstArgColor = 0xffd800; secondArgColor = 0xffd800;break;
			default: break;
			}
		}
		
		drawStringWithShadow(firstArgName, startX + 13, startY + 138, firstArgColor);
		drawStringWithShadow(secondArgName, startX + 13, startY + 176, secondArgColor);
		drawStringWithShadow("Category:", startX + 13, startY + 214, 0xffd800);
		drawStringWithShadow("Description:", startX + 13, startY + 252, 0xffd800);
	}
	
	private void loadKeyValues(HotKey keyIn) {
		originalKeyName = keyIn.getKeyName();
		keyNameEntry.setText(keyIn.getKeyName());
		enabledVal = keyIn.isEnabled();
		selectedHotKeyType = keyIn.getHotKeyType();
		selectType.setDisplayString(KeyActionType.getStringFromType(selectedHotKeyType));
		keysEntry.setKeys(keyIn.getKeyCombo().getKeys());
		keys = new KeyComboAction(keysEntry.getKeys());
		if (keyIn.getKeyCategory() != null && keyIn.getKeyCategory().getCategoryName() != null) {
			String catName = keyIn.getKeyCategory().getCategoryName().equals("null") ? "" : keyIn.getKeyCategory().getCategoryName();
			categoryEntry.setText(catName);
		}
		
		mainArgEntry.setVisible(false);
		secondaryArgEntry.setVisible(false);
		selectArg1.setVisible(false);
		
		switch (selectedHotKeyType) {
		case MESSAGESENDER:
			mainArgEntry.setText(((MessageSenderHotKey) keyIn).getMessage()).setVisible(true);
			break;
		case CONDITIONAL_MESSAGE_ITEMTEST:
			mainArgEntry.setText(((ItemTestMessageSenderHotKey) keyIn).getMessage()).setVisible(true);
			secondaryArgEntry.setText(((ItemTestMessageSenderHotKey) keyIn).getItemID() + "").setVisible(true);
			break;
		case DEBUG:
			selectArg1.setDisplayString(IDebugCommand.getDebugCommandName(((DebugHotKey) keyIn).getDebugFunction())).setVisible(true);
			selectedDebug = ((DebugHotKey) keyIn).getDebugFunction();
			break;
		case GUI_OPENER:
			selectArg1.setDisplayString(((GuiOpenerHotKey) keyIn).getGuiDisplayName()).setVisible(true);
			selectedGui = ((GuiOpenerHotKey) keyIn).getGui();
			break;
		case CATEGORY_ACTIVATOR:
			selectArg1.setDisplayString(((KeyCategoryActivatorHotKey) keyIn).getCategoryName()).setVisible(true);
			break;
		case CATEGORY_DEACTIVATOR:
			selectArg1.setDisplayString(((KeyCategoryDeactivatorHotKey) keyIn).getCategoryName()).setVisible(true);
			break;
		case MOD_ACTIVATOR:
			selectArg1.setDisplayString(AppType.getAppName(((ModActivatorHotKey) keyIn).getSubMod())).setVisible(true);
			selectedMod = ((ModActivatorHotKey) keyIn).getSubMod();
			break;
		case MOD_DEACTIVATOR:
			selectArg1.setDisplayString(AppType.getAppName(((ModDeactivatorHotKey) keyIn).getSubMod())).setVisible(true);
			selectedMod = ((ModDeactivatorHotKey) keyIn).getSubMod();
			break;
		//case SCRIPT:
		//	selectArg1.setDisplayString(((ScriptHotKey) keyIn).getScript().getScriptName()).setVisible(true);
		//	secondaryArgEntry.setText(((ScriptHotKey) keyIn).getScriptArgs() + "").setVisible(true);
		//	selectedScript = ((ScriptHotKey) keyIn).getScript();
		//	break;
		default: break;
		}
		String desc = keyIn.getKeyDescription().equals("No description set.") ? "" : keyIn.getKeyDescription();
		descriptionEntry.setText(desc);
	}
	
	@Override
	public void actionPerformed(IEnhancedActionObject object, Object... args) {
		if (object.equals(selectionList)) {
			if (selectionList.getSelectedObject() != null && selectionList.getStoredObject() instanceof SelectionType) {
				Object o = selectionList.getSelectedObject();
				switch ((SelectionType) selectionList.getStoredObject()) {
				case Debug: selectedDebug = (IDebugCommand) o; selectArg1.setDisplayString(IDebugCommand.getDebugCommandName(selectedDebug)).setVisible(true); break;
				case Gui:
					StorageBox<Class, StorageBox<Class[], Object[]>> values = (StorageBox<Class, StorageBox<Class[], Object[]>>) o;
					selectedGui = values.getObject();
					additionalGuiArgs = values.getValue();
					selectArg1.setDisplayString(selectedGui.getSimpleName()).setVisible(true);
					break;
				case Keybind: selectedKeyBind = (KeyBinding) o; selectArg1.setDisplayString(selectedKeyBind.getKeyDescription()).setVisible(true); break;
				case Mod: selectedMod = (AppType) o; selectArg1.setDisplayString(AppType.getAppName(selectedMod)).setVisible(true); break;
				//case Script: selectedScript = (EScript) o; selectArg1.setDisplayString(selectedScript.getScriptName()).setVisible(true); break;
				case Type:
					selectedHotKeyType = (KeyActionType) o;
					selectType.setDisplayString(KeyActionType.getStringFromType(selectedHotKeyType)).setVisible(true);
					updateVisibleObjects(selectedHotKeyType);
					break;
				default: break;
				}
			}
		}
		if (object.equals(selectType)) {
			if (selectedHotKeyType != KeyActionType.SUBMOD) {
				openSelectionGui(SelectionType.Type);
			} else {
				createErrorDialogue("Edit Error", "Cannot modify a Built In hotkey's key type!");
			}
		}
		if (object.equals(selectArg1)) {
			switch (selectedHotKeyType) {
			case DEBUG: openSelectionGui(SelectionType.Debug); break;
			case GUI_OPENER: openSelectionGui(SelectionType.Gui); break;
			case MC_KEYBIND_MODIFIER: openSelectionGui(SelectionType.Keybind); break;
			case MOD_ACTIVATOR:
			case MOD_DEACTIVATOR: openSelectionGui(SelectionType.Mod); break;
			default: break;
			}
		}
		if (object.equals(setEnabled)) {
			enabledVal = !enabledVal;
			setEnabled.setDisplayString(enabledVal ? "Enabled" : "Disabled");
			setEnabled.setDisplayStringColor(enabledVal ? 0x55ff55 : 0xff5555);
		}
		if (object == back) { fileUpAndClose(); }
		if (object.equals(create)) {
			try {
				if (!keyNameEntry.getText().isEmpty() && keyNameEntry.getText().equals("enter a key name")) { throw new MissingHotKeyArgumentException("Key name not defined!"); }
				
				if (key != null) {
					if (keys == null) { throw new MissingHotKeyArgumentException("No keys defined!"); }
				}
				else {
					if (keysEntry.getKeys().length == 0) { throw new MissingHotKeyArgumentException("No keys defined!"); }
				}
				
				//get key builder instance
				HotKeyBuilder builder = man.getKeyBuilder();
				
				//grab field data
				String keyName = keyNameEntry.getText();
				String category = categoryEntry.getText().equals("enter a category name") ? null : categoryEntry.getText();
				String mainArg = mainArgEntry.getText();
				String secondaryArg = secondaryArgEntry.getText();
				String description = descriptionEntry.getText().equals("enter a hotkey description") ? "No description set." : descriptionEntry.getText();
				
				//check field data against selected key type
				switch (selectedHotKeyType) {
				case MESSAGESENDER:
					if (!mainArgEntry.getText().isEmpty() && mainArgEntry.getText().equals("enter command")) {
						throw new MissingHotKeyArgumentException("Command not set!");
					}
					builder.setBuilderCommand(mainArgEntry.getText());
					break;
				case CONDITIONAL_MESSAGE_ITEMTEST:
					if (!mainArgEntry.getText().isEmpty() && mainArgEntry.getText().equals("enter command")) {
						throw new MissingHotKeyArgumentException("Command not set!");
					}
					
					try {
						if (!secondaryArgEntry.getText().isEmpty() && secondaryArgEntry.getText().equals("enter an item id to test for")) {
							throw new MissingHotKeyArgumentException("Item id not specified!");
						}
						Integer.parseInt(secondaryArgEntry.getText());
					} catch (NumberFormatException e) { throw new MissingHotKeyArgumentException("Could not parse item id!");
					} catch (MissingHotKeyArgumentException e) { throw e;
					} catch (Exception e) { e.printStackTrace(); }
					
					builder.setBuilderCommandAndItemArgs(mainArgEntry.getText(), Integer.parseInt(secondaryArgEntry.getText()));
					break;
				case DEBUG:
					if (selectedDebug == null) { throw new MissingHotKeyArgumentException("DebugCommand not selected!"); }
					builder.setBuilderDebugCommand(selectedDebug);
					break;
				case GUI_OPENER:
					if (selectedGui == null) { throw new MissingHotKeyArgumentException("No gui not selected!"); }
					if (additionalGuiArgs != null) { builder.setBuilderGuiToBeOpened(selectedGui, additionalGuiArgs); }
					else { builder.setBuilderGuiToBeOpened(selectedGui); }
					break;
				case CATEGORY_ACTIVATOR:
				case CATEGORY_DEACTIVATOR:
					if (!categoryEntry.getText().isEmpty() && categoryEntry.getText().equals("enter a category name")) { throw new MissingHotKeyArgumentException("Category name not entered!"); }
					break;
				case MOD_ACTIVATOR:
				case MOD_DEACTIVATOR:
					if (selectedMod == null) { throw new MissingHotKeyArgumentException("SubMod not selected!"); }
					builder.setBuilderSubMod(selectedMod);
					break;
				default: break;
				}
				
				//build the new key instance
				builder.setBuilderKeys(new KeyComboAction(keysEntry.getKeys()));
				
				//check if editing
				if (key != null) {
					//try to unregister old key
					if (man.unregisterHotKey(key)) { System.out.println("EnhancedMC: Unregistered hotkey: " + key.getKeyName()); }
					else { System.out.println("FAILED TO UNREGISTER HOTKEY: " + key.getKeyName()); return; }
				}
				if (builder.buildHotKey(keyNameEntry.getText(), category, description, enabledVal, selectedHotKeyType)) {
					man.registerHotKey(builder.getBuiltKey());
					msgBox = new EGuiDialogueBox(DialogueBoxTypes.custom) {
						{ okButton = new EGuiButton(this, midX - 25, midY + 10, 50, 20, "Ok"); addObject(okButton); }
						@Override
						public void actionPerformed(IEnhancedActionObject object, Object... args) {
							if (object == okButton) {
								man.saveHotKeys();
								close();
							}
						}
					};
					
					//change msg if editing existing key or not
					if (key != null) {
						msgBox.setMessage("Success! Hotkey: " + originalKeyName + " edited.").setMessageColor(0x55ff55);
						msgBox.setTitle("Hotkey Edit");
					}
					else {
						msgBox.setMessage("Success! Hotkey: " + keyNameEntry.getText() + " created.").setMessageColor(0x55ff55);
						msgBox.setTitle("Hotkey Creation");
					}
					
					EnhancedMC.displayWindow(msgBox, CenterType.screen);
				}
			} catch (MissingHotKeyArgumentException e) { createErrorDialogue("Creation Error", e.getMessage());
			} catch (Exception e) { e.printStackTrace(); }
		}
	}
	
	public void clearEntryData() {
		keyNameEntry.setText("");
		keysEntry.setText("");
		mainArgEntry.setText("");
		secondaryArgEntry.setText("");
		descriptionEntry.setText("");
	}
	
	protected void openSelectionGui(SelectionType typeIn) {
		StorageBoxHolder<String, Object> list = new StorageBoxHolder();
		
		switch (typeIn) {
		case Debug: for (IDebugCommand c : IDebugCommand.values()) { list.add(EnumChatFormatting.GREEN + IDebugCommand.getDebugCommandName(c), c); } break;
		case Gui:
			if (RegisteredApps.getAllGuiClasses().size() > 0) {
				boolean flag = ((CoreApp) RegisteredApps.getApp(AppType.CORE)).enableTerminal.get();
				if (EnhancedMC.isDebugMode() || flag) {
					list.add(EnumChatFormatting.GRAY + "EMC Debug Guis", null);
					if (EnhancedMC.isDebugMode() || EnhancedMC.isUserDev()) {
						list.add(EnumChatFormatting.LIGHT_PURPLE + "Experiment Gui", new StorageBox<Class, StorageBox<Class[], Object[]>>(ExperimentGui.class, null));
					}
					if (flag) {
						list.add(EnumChatFormatting.LIGHT_PURPLE + "Enhanced MC Terminal", new StorageBox<Class, StorageBox<Class[], Object[]>>(ETerminal.class, null));
					}
					list.add("", null);
				}
				
				list.add(EnumChatFormatting.GRAY + "EMC SubMod Guis", null);
				
				for (Class c : RegisteredApps.getAllGuiClasses()) {
					list.add(EnumChatFormatting.GREEN + c.getSimpleName(), new StorageBox<Class, StorageBox<Class[], Object[]>>(c, null));
				}
			}
			
			if (CommonVanillaGuis.getGuis().size() > 0) {
				list.add("", null);
				list.add(EnumChatFormatting.GRAY + "Vanilla Guis", null);
				for (StorageBox<Class, StorageBox<Class[], Object[]>> g : CommonVanillaGuis.getGuis()) {
					list.add(EnumChatFormatting.GREEN + g.getObject().getSimpleName(), g);
				}
			}
			break;
		case Keybind: for (KeyBinding k : mc.gameSettings.keyBindings) { list.add(EnumChatFormatting.GREEN + k.getKeyDescription(), k); } break;
		case Mod: for (AppType m : AppType.values()) { list.add(EnumChatFormatting.GREEN + AppType.getAppName(m), m); } break;
		case Script: break;
		case Type:
			for (KeyActionType t : KeyActionType.values()) {
				if (t.canUserCreate()) {
					if (t == KeyActionType.DEBUG && !EnhancedMC.isDebugMode()) { continue; }
					list.add(EnumChatFormatting.GREEN + KeyActionType.getStringFromType(t), t);
				}
			}
			break;
		default: break;
		}
		
		selectionList = new EGuiSelectionList(this, list);
		selectionList.setStoredObject(typeIn);
		EnhancedMC.displayWindow(selectionList, this, true, false, false, CenterType.object);
	}
	
	public void updateVisibleObjects() { updateVisibleObjects(null); }
	public void updateVisibleObjects(KeyActionType typeIn) {
		try {
			mainArgEntry.setVisible(false);
			secondaryArgEntry.setVisible(false);
			selectArg1.setVisible(false);
			
			KeyActionType testType;
			testType = selectedHotKeyType != null ? typeIn : selectedHotKeyType;
			
			if (testType != null) {
				switch (testType) {
				case MESSAGESENDER: mainArgEntry.setTextWhenEmpty("enter command").setVisible(true); break;
				case CONDITIONAL_MESSAGE_ITEMTEST:
					mainArgEntry.setTextWhenEmpty("enter command").setVisible(true);
					secondaryArgEntry.setTextWhenEmpty("enter an item id to test for").setVisible(true);
					break;
				case GUI_OPENER: selectArg1.setDisplayString("Select a Gui").setVisible(true); break;
				//case SCRIPT:
				//	selectArg1.setDisplayString("Select a Script").setVisible(true);
				//	secondaryArgEntry.setTextWhenEmpty("enter script arguments").setVisible(true);
				//	break;
				case DEBUG: selectArg1.setDisplayString("Select a Debug Command").setVisible(true); break;
				case CATEGORY_ACTIVATOR:
				case CATEGORY_DEACTIVATOR: mainArgEntry.setTextWhenEmpty("enter category name").setVisible(true); break;
				case MOD_ACTIVATOR:
				case MOD_DEACTIVATOR: selectArg1.setDisplayString("Select a Mod").setVisible(true); break;
				case MC_KEYBIND_MODIFIER: selectArg1.setDisplayString("Select a KeyBind").setVisible(true); trueFalseList.setVisible(true); break;
				default: break;
				}
			}
		} catch (Exception e) { e.printStackTrace(); }
	}
	
	private void createErrorDialogue(String errorName, String errorMessage) {
		msgBox = new EGuiDialogueBox(DialogueBoxTypes.ok);
		msgBox.setMessage("Error: " + errorMessage).setMessageColor(0xff5555);
		msgBox.setTitle(errorName);
		EnhancedMC.displayWindow(msgBox, CenterType.screen);
	}
}
