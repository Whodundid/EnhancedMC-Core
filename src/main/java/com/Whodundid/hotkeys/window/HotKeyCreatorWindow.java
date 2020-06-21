package com.Whodundid.hotkeys.window;

import com.Whodundid.core.EnhancedMC;
import com.Whodundid.core.app.AppType;
import com.Whodundid.core.app.RegisteredApps;
import com.Whodundid.core.coreApp.CoreApp;
import com.Whodundid.core.debug.ExperimentGui;
import com.Whodundid.core.debug.IDebugCommand;
import com.Whodundid.core.terminal.window.ETerminal;
import com.Whodundid.core.util.guiUtil.CommonVanillaGuis;
import com.Whodundid.core.util.renderUtil.CenterType;
import com.Whodundid.core.util.renderUtil.EColors;
import com.Whodundid.core.util.storageUtil.StorageBox;
import com.Whodundid.core.windowLibrary.windowObjects.actionObjects.WindowButton;
import com.Whodundid.core.windowLibrary.windowObjects.actionObjects.WindowTextField;
import com.Whodundid.core.windowLibrary.windowObjects.advancedObjects.dropDownList.WindowDropDownList;
import com.Whodundid.core.windowLibrary.windowObjects.windows.WindowDialogueBox;
import com.Whodundid.core.windowLibrary.windowObjects.windows.WindowSelectionList;
import com.Whodundid.core.windowLibrary.windowObjects.windows.WindowDialogueBox.DialogueBoxTypes;
import com.Whodundid.core.windowLibrary.windowTypes.WindowParent;
import com.Whodundid.core.windowLibrary.windowTypes.interfaces.IActionObject;
import com.Whodundid.hotkeys.HotKeyApp;
import com.Whodundid.hotkeys.control.HotKey;
import com.Whodundid.hotkeys.control.KeyActionType;
import com.Whodundid.hotkeys.control.hotKeyTypes.CommandSenderHotKey;
import com.Whodundid.hotkeys.control.hotKeyTypes.DebugHotKey;
import com.Whodundid.hotkeys.control.hotKeyTypes.GuiOpenerHotKey;
import com.Whodundid.hotkeys.control.hotKeyTypes.ConditionalCommandSenderHotKey;
import com.Whodundid.hotkeys.control.hotKeyTypes.KeyCategoryActivatorHotKey;
import com.Whodundid.hotkeys.control.hotKeyTypes.KeyCategoryDeactivatorHotKey;
import com.Whodundid.hotkeys.control.hotKeyTypes.ModActivatorHotKey;
import com.Whodundid.hotkeys.control.hotKeyTypes.ModDeactivatorHotKey;
import com.Whodundid.hotkeys.control.hotKeyUtil.*;
import com.Whodundid.hotkeys.control.hotKeyUtil.exceptions.MissingHotKeyArgumentException;
import com.Whodundid.hotkeys.keySaveLoad.HotKeyBuilder;
import com.Whodundid.hotkeys.util.HKResources;
import com.Whodundid.hotkeys.window.util.KeyEntryTextField;
import net.minecraft.client.gui.GuiMainMenu;
import net.minecraft.client.gui.GuiMultiplayer;
import net.minecraft.client.settings.KeyBinding;

//Jan 9, 2019
//Last edited: Feb 17, 2019
//Edit note: Added support for key categories
//First Added: Sep 14, 2018
//Author: Hunter Bragg

public class HotKeyCreatorWindow extends WindowParent {
	
	HotKeyApp man = (HotKeyApp) RegisteredApps.getApp(AppType.HOTKEYS);
	WindowButton create, back, setEnabled, selectType, selectArg1, stepByStep;
	WindowTextField keyNameEntry, mainArgEntry, secondaryArgEntry, categoryEntry, descriptionEntry;
	KeyEntryTextField keysEntry;
	WindowDropDownList trueFalseList;
	WindowSelectionList selectionList;
	WindowDialogueBox msgBox;
	HotKey key;
	KeyComboAction keys;
	KeyActionType selectedHotKeyType;
	Class selectedGui;
	IDebugCommand selectedDebug;
	AppType selectedMod;
	KeyBinding selectedKeyBind;
	boolean enabledVal = true;
	String originalKeyName = "";
	StorageBox<Class[], Object[]> additionalGuiArgs = null;
	
	protected enum SelectionType { Type, Gui, Script, Debug, Mod, Keybind; }
	
	public HotKeyCreatorWindow() { this(null); }
	public HotKeyCreatorWindow(HotKey keyIn) {
		super();
		key = keyIn;
		aliases.add("hotkeycreator", "keycreator", "hkc");
		windowIcon = HKResources.iconCreator;
	}
	
	@Override
	public void initWindow() {
		setObjectName("Hotkey Creator");
		setDimensions(256, 324);
	}
	
	@Override
	public void initObjects() {
		defaultHeader(this);
		
		keyNameEntry = new WindowTextField(this, startX + 23, startY + 38, width - 48, 13).setTextWhenEmpty("enter a key name");
		selectType = new WindowButton(this, midX - (width - 66) / 2, startY + 74, width - 66, 17, "Select a type");
		keysEntry = new KeyEntryTextField(this, startX + 23, startY + 114, width - 48, 13);
		selectArg1 = new WindowButton(this, startX + 23, startY + 149, 140, 17, "Arg1");
		mainArgEntry = new WindowTextField(this, startX + 23, startY + 152, width - 48, 13).setMaxStringLength(100);
		secondaryArgEntry = new WindowTextField(this, startX + 23, startY + 190, width - 48, 13).setMaxStringLength(200);
		categoryEntry = new WindowTextField(this, startX + 23, startY + 228, width - 48, 13).setMaxStringLength(20).setTextWhenEmpty("enter a category name");
		descriptionEntry = new WindowTextField(this, startX + 23, startY + 266, width - 48, 13).setMaxStringLength(200).setTextWhenEmpty("enter a hotkey description");
		
		create = new WindowButton(this, endX - 9 - 70, endY - 28, 70, 20, key != null ? "Edit" : "Create");
		setEnabled = new WindowButton(this, midX - 35, endY - 28, 70, 20, key != null ? key.isEnabled() ? "Enabled" : "Disabled" : "Enabled");
		back = new WindowButton(this, startX + 9, endY - 28, 70, 20, "Back");
		
		setEnabled.setStringColor(key != null ? key.isEnabled() ? 0x55ff55 : 0xff5555 : 0x55ff55);
		keysEntry.setTextWhenEmpty("enter keys");
		
		create.setStringColor(EColors.aquamarine);
		back.setStringColor(EColors.yellow);
		
		selectType.setStringColor(EColors.seafoam);
		
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
			case COMMANDSENDER: firstArgName = "Command:"; firstArgColor = 0xffd800; break;
			case CONDITIONAL_COMMAND_ITEMTEST: firstArgName = "Command:"; secondArgName = "Test Item ID:"; firstArgColor = 0xffd800; secondArgColor = 0xffd800; break;
			case DEBUG: firstArgName = "Debug Command:"; firstArgColor = 0xffd800; break;
			case GUI_OPENER: firstArgName = "Gui to be opened:"; firstArgColor = 0xffd800; break;
			case MC_KEYBIND_MODIFIER: firstArgName = "MC KeyBind:"; secondArgName = "New KeyBind Value: (true / false)"; firstArgColor = 0xffd800; secondArgColor = 0xffd800; break;
			case CATEGORY_ACTIVATOR: firstArgName = "Hotkey category to activate:"; firstArgColor = 0xffd800; break;
			case CATEGORY_DEACTIVATOR: firstArgName = "Hotkey category to deactivate:"; firstArgColor = 0xffd800; break;
			case APP_ACTIVATOR: firstArgName = "App to activate:"; firstArgColor = 0xffd800; break;
			case APP_DEACTIVATOR: firstArgName = "App to deactivate:"; firstArgColor = 0xffd800; break;
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
		selectType.setString(KeyActionType.getStringFromType(selectedHotKeyType));
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
		case COMMANDSENDER:
			mainArgEntry.setText(((CommandSenderHotKey) keyIn).getCommand()).setVisible(true);
			break;
		case CONDITIONAL_COMMAND_ITEMTEST:
			mainArgEntry.setText(((ConditionalCommandSenderHotKey) keyIn).getCommand()).setVisible(true);
			secondaryArgEntry.setText(((ConditionalCommandSenderHotKey) keyIn).getItemID() + "").setVisible(true);
			break;
		case DEBUG:
			selectArg1.setString(IDebugCommand.getDebugCommandName(((DebugHotKey) keyIn).getDebugFunction())).setVisible(true);
			selectedDebug = ((DebugHotKey) keyIn).getDebugFunction();
			break;
		case GUI_OPENER:
			selectArg1.setString(((GuiOpenerHotKey) keyIn).getGuiDisplayName()).setVisible(true);
			selectedGui = ((GuiOpenerHotKey) keyIn).getGui();
			break;
		case CATEGORY_ACTIVATOR:
			selectArg1.setString(((KeyCategoryActivatorHotKey) keyIn).getCategoryName()).setVisible(true);
			break;
		case CATEGORY_DEACTIVATOR:
			selectArg1.setString(((KeyCategoryDeactivatorHotKey) keyIn).getCategoryName()).setVisible(true);
			break;
		case APP_ACTIVATOR:
			selectArg1.setString(AppType.getAppName(((ModActivatorHotKey) keyIn).getApp())).setVisible(true);
			selectedMod = ((ModActivatorHotKey) keyIn).getApp();
			break;
		case APP_DEACTIVATOR:
			selectArg1.setString(AppType.getAppName(((ModDeactivatorHotKey) keyIn).getApp())).setVisible(true);
			selectedMod = ((ModDeactivatorHotKey) keyIn).getApp();
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
	public void actionPerformed(IActionObject object, Object... args) {
		if (object.equals(selectionList)) {
			if (selectionList.getSelectedObject() != null && selectionList.getStoredObject() instanceof SelectionType) {
				Object o = selectionList.getSelectedObject();
				switch ((SelectionType) selectionList.getStoredObject()) {
				case Debug: selectedDebug = (IDebugCommand) o; selectArg1.setString(IDebugCommand.getDebugCommandName(selectedDebug)).setVisible(true); break;
				case Gui:
					StorageBox<Class, StorageBox<Class[], Object[]>> values = (StorageBox<Class, StorageBox<Class[], Object[]>>) o;
					selectedGui = values.getObject();
					additionalGuiArgs = values.getValue();
					selectArg1.setString(selectedGui.getSimpleName()).setVisible(true);
					break;
				case Keybind: selectedKeyBind = (KeyBinding) o; selectArg1.setString(selectedKeyBind.getKeyDescription()).setVisible(true); break;
				case Mod: selectedMod = (AppType) o; selectArg1.setString(AppType.getAppName(selectedMod)).setVisible(true); break;
				//case Script: selectedScript = (EScript) o; selectArg1.setDisplayString(selectedScript.getScriptName()).setVisible(true); break;
				case Type:
					selectedHotKeyType = (KeyActionType) o;
					selectType.setString(KeyActionType.getStringFromType(selectedHotKeyType)).setVisible(true);
					updateVisibleObjects(selectedHotKeyType);
					break;
				default: break;
				}
			}
		}
		if (object.equals(selectType)) {
			if (selectedHotKeyType != KeyActionType.APP) {
				openSelectionGui(SelectionType.Type);
			}
			else {
				createErrorDialogue("Edit Error", "Cannot modify a Built In hotkey's key type!");
			}
		}
		if (object.equals(selectArg1)) {
			switch (selectedHotKeyType) {
			case DEBUG: openSelectionGui(SelectionType.Debug); break;
			case GUI_OPENER: openSelectionGui(SelectionType.Gui); break;
			case MC_KEYBIND_MODIFIER: openSelectionGui(SelectionType.Keybind); break;
			case APP_ACTIVATOR:
			case APP_DEACTIVATOR: openSelectionGui(SelectionType.Mod); break;
			default: break;
			}
		}
		if (object.equals(setEnabled)) {
			enabledVal = !enabledVal;
			setEnabled.setString(enabledVal ? "Enabled" : "Disabled");
			setEnabled.setStringColor(enabledVal ? 0x55ff55 : 0xff5555);
		}
		if (object == back) { fileUpAndClose(); }
		if (object.equals(create)) {
			try {
				if (!keyNameEntry.getText().isEmpty() && keyNameEntry.getText().equals("enter a key name")) { throw new MissingHotKeyArgumentException("Key name not defined!"); }
				
				if (key != null && keys == null) { throw new MissingHotKeyArgumentException("No keys defined!"); }
				else if (keysEntry.getKeys().length == 0) { throw new MissingHotKeyArgumentException("No keys defined!"); }
				
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
				case COMMANDSENDER:
					if (!mainArgEntry.getText().isEmpty() && mainArgEntry.getText().equals("enter command")) {
						throw new MissingHotKeyArgumentException("Command not set!");
					}
					builder.setBuilderCommand(mainArgEntry.getText());
					break;
				case CONDITIONAL_COMMAND_ITEMTEST:
					if (!mainArgEntry.getText().isEmpty() && mainArgEntry.getText().equals("enter command")) {
						throw new MissingHotKeyArgumentException("Command not set!");
					}
					
					try {
						if (!secondaryArgEntry.getText().isEmpty() && secondaryArgEntry.getText().equals("enter an item id to test for")) {
							throw new MissingHotKeyArgumentException("Item id not specified!");
						}
						Integer.parseInt(secondaryArgEntry.getText());
					}
					catch (NumberFormatException e) { throw new MissingHotKeyArgumentException("Could not parse item id!"); }
					catch (MissingHotKeyArgumentException e) { throw e; }
					catch (Exception e) { e.printStackTrace(); }
					
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
					builder.setBuilderCommand(categoryEntry.getText());
					break;
				case APP_ACTIVATOR:
				case APP_DEACTIVATOR:
					if (selectedMod == null) { throw new MissingHotKeyArgumentException("App not selected!"); }
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
					
					msgBox = new WindowDialogueBox(DialogueBoxTypes.custom) {
						{ okButton = new WindowButton(this, midX - 25, midY + 10, 50, 20, "Ok"); addObject(okButton); }
						
						@Override
						public void actionPerformed(IActionObject object, Object... args) {
							if (object == okButton) {
								close();
							}
						}
						
						@Override
						public void close() {
							super.close();
							man.saveHotKeys();
							windowInstance.fileUpAndClose();
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
					
					msgBox.setTitleColor(EColors.lgray.intVal);
					
					EnhancedMC.reloadAllWindows();
					
					EnhancedMC.displayWindow(msgBox, CenterType.screen);
					getTopParent().setFocusLockObject(msgBox);
				}
				else {
					throw new MissingHotKeyArgumentException("Cannot create hotkey!");
				}
			}
			catch (MissingHotKeyArgumentException e) { createErrorDialogue("Creation Error", e.getMessage()); }
			catch (Exception e) { e.printStackTrace(); }
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
		selectionList = new WindowSelectionList(this);
		selectionList.setStoredObject(typeIn);
		
		switch (typeIn) {
		case Debug: for (IDebugCommand c : IDebugCommand.values()) { selectionList.addOption(IDebugCommand.getDebugCommandName(c), EColors.green, c); } break;
		case Gui:
			if (RegisteredApps.getAllGuiClasses().size() > 0) {
				boolean flag = ((CoreApp) RegisteredApps.getApp(AppType.CORE)).enableTerminal.get();
				if (EnhancedMC.isDebugMode() || flag) {
					selectionList.writeLine("EMC Debug Guis", EColors.lgray);
					if (EnhancedMC.isDebugMode() || EnhancedMC.isUserDev()) {
						selectionList.addOption("Experiment Gui", EColors.pink, new StorageBox(ExperimentGui.class, null));
					}
					if (flag) {
						selectionList.addOption("Enhanced MC Terminal", EColors.pink, new StorageBox(ETerminal.class, null));
					}
					selectionList.writeLine();
				}
				
				selectionList.writeLine("EMC App Windows", EColors.lgray);
				
				for (Class c : RegisteredApps.getAllGuiClasses()) {
					selectionList.addOption(c.getSimpleName(), EColors.green, new StorageBox(c, null));
				}
			}
			
			if (CommonVanillaGuis.getGuis().size() > 0) {
				selectionList.writeLine();
				selectionList.writeLine("Vanilla Guis", EColors.lgray);
				for (StorageBox<Class, StorageBox<Class[], Object[]>> g : CommonVanillaGuis.getGuis()) {
					if (g.getObject() != GuiMainMenu.class && g.getObject() != GuiMultiplayer.class) {
						selectionList.addOption(g.getObject().getSimpleName(), EColors.green, g);
					}
				}
			}
			break;
		case Keybind: for (KeyBinding k : mc.gameSettings.keyBindings) { selectionList.addOption(k.getKeyDescription(), EColors.green, k); } break;
		case Mod: for (AppType m : AppType.values()) { selectionList.addOption(AppType.getAppName(m), EColors.green, m); } break;
		case Script: break;
		case Type:
			for (KeyActionType t : KeyActionType.values()) {
				if (t.canUserCreate()) {
					if (t == KeyActionType.DEBUG && !EnhancedMC.isDebugMode()) { continue; }
					selectionList.addOption(KeyActionType.getStringFromType(t), EColors.green, t);
				}
			}
			break;
		default: break;
		}
		
		EnhancedMC.displayWindow(selectionList, this, true, false, false, CenterType.object);
		getTopParent().setFocusLockObject(selectionList);
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
				case COMMANDSENDER: mainArgEntry.setTextWhenEmpty("enter command").setVisible(true); break;
				case CONDITIONAL_COMMAND_ITEMTEST:
					mainArgEntry.setTextWhenEmpty("enter command").setVisible(true);
					secondaryArgEntry.setTextWhenEmpty("enter an item id to test for").setVisible(true);
					break;
				case GUI_OPENER: selectArg1.setString("Select a Gui").setVisible(true); break;
				//case SCRIPT:
				//	selectArg1.setDisplayString("Select a Script").setVisible(true);
				//	secondaryArgEntry.setTextWhenEmpty("enter script arguments").setVisible(true);
				//	break;
				case DEBUG: selectArg1.setString("Select a Debug Command").setVisible(true); break;
				case CATEGORY_ACTIVATOR:
				case CATEGORY_DEACTIVATOR: mainArgEntry.setTextWhenEmpty("enter category name").setVisible(true); break;
				case APP_ACTIVATOR:
				case APP_DEACTIVATOR: selectArg1.setString("Select an App").setVisible(true); break;
				case MC_KEYBIND_MODIFIER: selectArg1.setString("Select a KeyBind").setVisible(true); trueFalseList.setVisible(true); break;
				default: break;
				}
			}
		} catch (Exception e) { e.printStackTrace(); }
	}
	
	private void createErrorDialogue(String errorName, String errorMessage) {
		msgBox = new WindowDialogueBox(DialogueBoxTypes.ok);
		msgBox.setMessage("Error: " + errorMessage).setMessageColor(0xff5555);
		msgBox.setTitle(errorName);
		msgBox.setTitleColor(EColors.lgray.intVal);
		EnhancedMC.displayWindow(msgBox, CenterType.screen);
		getTopParent().setFocusLockObject(msgBox);
	}
	
}
