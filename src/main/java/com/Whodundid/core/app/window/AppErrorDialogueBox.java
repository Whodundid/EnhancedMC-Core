package com.Whodundid.core.app.window;

import com.Whodundid.core.EnhancedMC;
import com.Whodundid.core.app.AppSettings;
import com.Whodundid.core.app.AppType;
import com.Whodundid.core.app.EMCApp;
import com.Whodundid.core.app.window.windowUtil.AppErrorType;
import com.Whodundid.core.settings.SettingsWindowMain;
import com.Whodundid.core.util.storageUtil.EArrayList;
import com.Whodundid.core.windowLibrary.windowObjects.actionObjects.WindowButton;
import com.Whodundid.core.windowLibrary.windowObjects.advancedObjects.header.WindowHeader;
import com.Whodundid.core.windowLibrary.windowObjects.windows.WindowDialogueBox;
import com.Whodundid.core.windowLibrary.windowTypes.interfaces.IWindowObject;

//Author: Hunter Bragg

public class AppErrorDialogueBox extends WindowDialogueBox {
	
	protected AppErrorType type;
	protected EMCApp mod;
	protected EArrayList<EMCApp> mods = new EArrayList();
	
	public AppErrorDialogueBox(IWindowObject parentIn, AppErrorType typeIn, EMCApp modIn) {
		init(parentIn);
		centerObjectWithSize(250, 75);
		mInit(typeIn, modIn);
	}
	public AppErrorDialogueBox(IWindowObject parentIn, int xPos, int yPos, int width, int height, AppErrorType typeIn, EMCApp modIn) {
		init(parentIn, xPos, yPos, width, height);
		mInit(typeIn, modIn);
	}
	
	private void mInit(AppErrorType typeIn, EMCApp modIn) {
		type = typeIn;
		mod = modIn;
		requestFocus();
		getTopParent().setFocusLockObject(this);
		setMessageColor(0xff5555);
		bringToFront();
		setObjectName("Error");
		setPinnable(false);
	}
	
	@Override
	public void initObjects() {
		this.setHeader(new WindowHeader(this));
		
		switch (type) {
		case ENABLE: setTitle("App Enable Error"); addEnable(); addCancel(); break;
		case DISABLE: setTitle("App Disable Error"); addDisable(); addCancel(); break;
		case NOGUI: setTitle("No Gui Found"); addOk(); break;
		case NOTFOUND: setTitle("App Not Found"); addOk(); break;
		case INCOMPATIBLE: setTitle("App Incompatible"); addOk(); break;
		case ERROR: setTitle("App Error!"); addOk(); break;
		}
	}
	
	private void addEnable() {
		WindowButton enableAll = new WindowButton(this, midX - 90, midY + 7, 65, 20, "Enable All") {
			{ setRunActionOnPress(true); }
			@Override public void onPress() {
				playPressSound();
				reloadSettings(true);
				parent.close();
			}
		};
		addObject(null, enableAll.setZLevel(1));
	}
	
	private void addDisable() {
		WindowButton disableAll = new WindowButton(this, midX - 90, midY + 7, 65, 20, "Disable All") {
			{ setRunActionOnPress(true); }
			@Override public void onPress() {
				playPressSound();
				reloadSettings(false);
				parent.close();
			}
		};
		addObject(null, disableAll.setZLevel(1));
	}
	
	private void addCancel() {
		WindowButton cancel = new WindowButton(this, midX + 30, midY + 7, 65, 20, "Cancel") {
			{ setRunActionOnPress(true); }
			@Override public void onPress() {
				playPressSound();
				parent.close();
			}
		};
		addObject(null, cancel.setZLevel(1));
	}
	
	private void addOk() {
		WindowButton okButton = new WindowButton(this, midX - 25, midY + 7, 50, 20, "Ok") {
			{ setRunActionOnPress(true); }
			@Override
			public void onPress() {
				playPressSound();
				parent.close();
			}
		};
		addObject(null, okButton.setZLevel(1));
	}
	
	public AppErrorDialogueBox createErrorMessage(Exception e, EArrayList<EMCApp> modsIn) {
		if (e != null) { setMessage(e.toString() + (e.getStackTrace().length > 0 ? " at " + e.getStackTrace()[0] : "")); }
		else if (modsIn != null && !modsIn.isEmpty()) {
			mods.addAll(modsIn);
			message += "Mods: (";
			modsIn.forEach((m) -> { message += (AppType.getAppName(m.getAppType()) + ", "); } );
			message = message.substring(0, message.length() - 2);
			message += ")";
			switch (type) {
			case ENABLE: message += " are required to enable " + AppType.getAppName(mod.getAppType()) + "."; break;
			case DISABLE: message += " require " + AppType.getAppName(mod.getAppType()) + " to properly function."; break;
			default: break;
			}
			setMessage(message);
		}
		else {
			setMessage(message = mod.getName() + " does not have a main window.");
		}
		
		if (type == AppErrorType.INCOMPATIBLE) {
			setMessage(message = mod.getName() + " is either incompatible or deals with incompatible mods!");
		}
		
		setMessageColor(0xff5555);
		return this;
	}
	
	private void reloadSettings(boolean val) {
		mods.forEach(m -> { AppSettings.updateAppState(m, val); });
		AppSettings.updateAppState(mod, val);
		IWindowObject window = EnhancedMC.getWindowInstance(SettingsWindowMain.class);
		if (EnhancedMC.getWindowInstance(SettingsWindowMain.class) != null) {
			window.sendArgs("Reload");
		}
	}
}
