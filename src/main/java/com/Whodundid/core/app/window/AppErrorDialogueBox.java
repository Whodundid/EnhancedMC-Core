package com.Whodundid.core.app.window;

import com.Whodundid.core.EnhancedMC;
import com.Whodundid.core.app.AppSettings;
import com.Whodundid.core.app.AppType;
import com.Whodundid.core.app.EMCApp;
import com.Whodundid.core.app.window.windowUtil.AppErrorType;
import com.Whodundid.core.settings.SettingsWindowMain;
import com.Whodundid.core.util.storageUtil.EArrayList;
import com.Whodundid.core.windowLibrary.windowObjects.actionObjects.WindowButton;
import com.Whodundid.core.windowLibrary.windowObjects.windows.WindowDialogueBox;
import com.Whodundid.core.windowLibrary.windowTypes.interfaces.IActionObject;
import com.Whodundid.core.windowLibrary.windowTypes.interfaces.IWindowObject;
import net.minecraft.util.MathHelper;

//Author: Hunter Bragg

public class AppErrorDialogueBox extends WindowDialogueBox {
	
	protected AppErrorType type;
	protected EMCApp mod;
	protected EArrayList<EMCApp> mods = new EArrayList();
	
	private WindowButton okButton, disable, enable, cancel;
	private int wPos, gap;
	
	public AppErrorDialogueBox(IWindowObject parentIn, AppErrorType typeIn, EMCApp modIn) {
		super();
		type = typeIn;
		mod = modIn;
	}
	
	@Override
	public void initWindow() {
		setDimensions(250, 75);
		getTopParent().setFocusLockObject(this);
		setMessageColor(0xff5555);
		setObjectName("Error");
		setPinnable(false);
	}
	
	@Override
	public void initObjects() {
		defaultHeader(this);
		
		wPos = width / 3;
		gap = width / 20;
		
		wPos = MathHelper.clamp_int(wPos, 60, 120);
		
		switch (type) {
		case ENABLE: setTitle("App Enable Error"); addEnable(); addCancel(); break;
		case DISABLE: setTitle("App Disable Error"); addDisable(); addCancel(); break;
		case NOGUI: setTitle("No Gui Found"); addOk(); break;
		case NOTFOUND: setTitle("App Not Found"); addOk(); break;
		case INCOMPATIBLE: setTitle("App Incompatible"); addOk(); break;
		case ERROR: setTitle("App Error!"); addOk(); break;
		}
		
		if (message != null) { setMessage(message); }
	}
	
	@Override
	public void actionPerformed(IActionObject object, Object... args) {
		if (object == okButton || object == cancel) { fileUpAndClose(); }
		if (object == disable) { reloadSettings(false); fileUpAndClose(); }
		if (object == enable) { reloadSettings(true); fileUpAndClose(); }
	}
	
	private void addEnable() {
		enable = new WindowButton(this, midX - wPos - gap, midY + 7, wPos, 20, "Enable All");
		addObject(enable);
	}
	
	private void addDisable() {
		disable = new WindowButton(this, midX - wPos - gap, midY + 7, wPos, 20, "Disable All");
		addObject(disable);
	}
	
	private void addCancel() {
		cancel = new WindowButton(this, midX + gap, midY + 7, wPos, 20, "Cancel");
		addObject(cancel);
	}
	
	private void addOk() {
		okButton = new WindowButton(this, midX - (wPos / 2), midY + 7, wPos, 20, "Ok");
		addObject(okButton);
	}
	
	public AppErrorDialogueBox createErrorMessage(Exception e, EArrayList<EMCApp> modsIn) {
		if (e != null) { setMessage(e.toString() + (e.getStackTrace().length > 0 ? " at " + e.getStackTrace()[0] : "")); }
		else if (modsIn != null && !modsIn.isEmpty()) {
			mods.addAll(modsIn);
			message += "Apps: (";
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
			setMessage(message = mod.getName() + " is either incompatible or deals with incompatible apps!");
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
