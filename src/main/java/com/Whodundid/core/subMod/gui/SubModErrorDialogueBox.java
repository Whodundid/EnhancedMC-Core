package com.Whodundid.core.subMod.gui;

import com.Whodundid.core.enhancedGui.guiObjects.EGuiButton;
import com.Whodundid.core.enhancedGui.guiObjects.EGuiDialogueBox;
import com.Whodundid.core.enhancedGui.guiObjects.EGuiHeader;
import com.Whodundid.core.enhancedGui.interfaces.IEnhancedGuiObject;
import com.Whodundid.core.settings.SettingsGuiMain;
import com.Whodundid.core.subMod.SubMod;
import com.Whodundid.core.subMod.SubModSettings;
import com.Whodundid.core.subMod.SubModType;
import com.Whodundid.core.util.storageUtil.EArrayList;

//Last edited: Dec 28, 2018
//First Added: Dec 28, 2018
//Author: Hunter Bragg

public class SubModErrorDialogueBox extends EGuiDialogueBox {
	
	protected SubModErrorType type;
	protected SubMod mod;
	protected EArrayList<SubMod> mods = new EArrayList();
	
	public SubModErrorDialogueBox(IEnhancedGuiObject parentIn, SubModErrorType typeIn, SubMod modIn) {
		init(parentIn);
		type = typeIn;
		mod = modIn;
		centerObjectWithSize(250, 75);
		requestFocus();
		getTopParent().setFocusLockObject(this);
		setZLevel(99999);
	}
	
	public SubModErrorDialogueBox(IEnhancedGuiObject parentIn, int xPos, int yPos, int width, int height, SubModErrorType typeIn, SubMod modIn) {
		init(parentIn, xPos, yPos, width, height);
		type = typeIn;
		mod = modIn;
		requestFocus();
		getTopParent().setFocusLockObject(this);
		setMessageColor(0xff5555);
		setZLevel(99999);
	}
	
	@Override
	public void initObjects() {
		this.setHeader(new EGuiHeader(this));
		
		switch (type) {
		case ENABLE: setDisplayString("Mod Enable Error"); addEnable(); addCancel(); break;
		case DISABLE: setDisplayString("Mod Disable Error"); addDisable(); addCancel(); break;
		case NOGUI: setDisplayString("No Gui Found"); addOk(); break;
		case NOTFOUND: setDisplayString("Mod Not Found"); addOk(); break;
		case INCOMPATIBLE: setDisplayString("Mod Incompatible"); addOk(); break;
		case ERROR: setDisplayString("Submod Error!"); addOk(); break;
		}
	}
	
	private void addEnable() {
		EGuiButton enableAll = new EGuiButton(this, midX - 90, midY + 7, 65, 20, "Enable All") {
			{ setRunActionOnPress(true); }
			@Override public void performAction() {
				playPressSound();
				reloadSettings(true);
				parent.close();
			}
		};
		addObject(enableAll.setZLevel(1));
	}
	
	private void addDisable() {
		EGuiButton disableAll = new EGuiButton(this, midX - 90, midY + 7, 65, 20, "Disable All") {
			{ setRunActionOnPress(true); }
			@Override public void performAction() {
				playPressSound();
				reloadSettings(false);
				parent.close();
			}
		};
		addObject(disableAll.setZLevel(1));
	}
	
	private void addCancel() {
		EGuiButton cancel = new EGuiButton(this, midX + 30, midY + 7, 65, 20, "Cancel") {
			{ setRunActionOnPress(true); }
			@Override public void performAction() {
				playPressSound();
				parent.close();
			}
		};
		addObject(cancel.setZLevel(1));
	}
	
	private void addOk() {
		EGuiButton okButton = new EGuiButton(this, midX - 25, midY + 7, 50, 20, "Ok") {
			{ setRunActionOnPress(true); }
			@Override
			public void performAction() {
				playPressSound();
				parent.close();
			}
		};
		addObject(okButton.setZLevel(1));
	}
	
	public SubModErrorDialogueBox createErrorMessage(Exception e, EArrayList<SubMod> modsIn) {
		if (e != null) {
			setMessage(e.getMessage());
		}
		else if (modsIn != null && !modsIn.isEmpty()) {
			mods.addAll(modsIn);
			message += "Mods: (";
			modsIn.forEach((m) -> { message += (SubModType.getModName(m.getModType()) + ", "); } );
			message = message.substring(0, message.length() - 2);
			message += ")";
			switch (type) {
			case ENABLE: message += " are required to enable " + SubModType.getModName(mod.getModType()) + "."; break;
			case DISABLE: message += " require " + SubModType.getModName(mod.getModType()) + " to properly function."; break;
			default: break;
			}
			setMessage(message);
		}
		
		if (type == SubModErrorType.INCOMPATIBLE) {
			setMessage(mod.getName() + " is either incompatible or deals with incompatible mods!");
			this.setMessageColor(0xff5555);
		}
		return this;
	}
	
	private void reloadSettings(boolean val) {
		mods.forEach(m -> { SubModSettings.updateModState(m, val); });
		SubModSettings.updateModState(mod, val);
		if (mc.currentScreen instanceof SettingsGuiMain) { ((SettingsGuiMain) mc.currentScreen).updateList(); }
	}
}
