package com.Whodundid.hotkeys.hotkKeyGuis;

import com.Whodundid.core.EnhancedMC;
import com.Whodundid.core.app.AppType;
import com.Whodundid.core.app.RegisteredApps;
import com.Whodundid.core.enhancedGui.guiObjects.actionObjects.EGuiButton;
import com.Whodundid.core.enhancedGui.guiObjects.basicObjects.EGuiContainer;
import com.Whodundid.core.enhancedGui.guiObjects.basicObjects.EGuiLabel;
import com.Whodundid.core.enhancedGui.guiObjects.windows.EGuiDialogueBox;
import com.Whodundid.core.enhancedGui.guiObjects.windows.EGuiDialogueBox.DialogueBoxTypes;
import com.Whodundid.core.enhancedGui.types.WindowParent;
import com.Whodundid.core.enhancedGui.types.interfaces.IEnhancedActionObject;
import com.Whodundid.core.enhancedGui.types.interfaces.IEnhancedGuiObject;
import com.Whodundid.core.util.renderUtil.CenterType;
import com.Whodundid.core.util.storageUtil.EDimension;
import com.Whodundid.hotkeys.HotKeyApp;

public class HotKeySettingsGui extends WindowParent {
	
	HotKeyApp mod = (HotKeyApp) RegisteredApps.getApp(AppType.HOTKEYS);
	EGuiContainer settings, keyList;
	EGuiButton stopMovement, runKeyBuilderTutorial;
	EGuiButton loadKeyList, saveKeyList, resetKeyList;
	EGuiLabel stopMovementLabel, runKeyBuilderLabel;
	EGuiDialogueBox msgBox;
	
	public HotKeySettingsGui() { super(); }
	public HotKeySettingsGui(Object oldGuiIn) { super(oldGuiIn); }
	public HotKeySettingsGui(IEnhancedGuiObject parentIn) { super(parentIn); }
	public HotKeySettingsGui(IEnhancedGuiObject parentIn, Object oldGuiIn) { super(parentIn, oldGuiIn); }
	public HotKeySettingsGui(IEnhancedGuiObject parentIn, int posX, int posY) { super(parentIn, posX, posY); }
	public HotKeySettingsGui(IEnhancedGuiObject parentIn, int posX, int posY, Object oldGuiIn) { super(parentIn, posX, posY, oldGuiIn); }
	
	@Override
	public void initGui() {
		setObjectName("HotKey Settings");
		defaultPos();
		super.initGui();
	}
	
	@Override
	public void initObjects() {
		defaultHeader(this);
		
		settings = new EGuiContainer(this, startX + 2, startY + 2, width - 4, 140);
		settings.setTitle("Key Settings");
		EDimension sDim = settings.getDimensions();
		stopMovement = new EGuiButton(settings, sDim.startX + 10, sDim.startY + 26, 55, 20).setTrueFalseButton(true).updateTrueFalseDisplay(mod.doesCtrlKeyStopMovement());
		stopMovementLabel = new EGuiLabel(settings, stopMovement.endX + (endX - stopMovement.endX) / 2, stopMovement.startY + 4, "Stop movement when Ctrl is pressed", 0xb2b2b2);
		stopMovementLabel.enableShadow(true).enableWordWrap(true, 125).setDrawCentered(true);
		runKeyBuilderTutorial = new EGuiButton(settings, sDim.startX + 10, stopMovement.endY + 7, 55, 20).setTrueFalseButton(true).updateTrueFalseDisplay(mod.runTutorial());
		runKeyBuilderLabel = new EGuiLabel(settings, runKeyBuilderTutorial.endX + 15, runKeyBuilderTutorial.startY + 6, "Enable creation tutorial", 0xb2b2b2);
		runKeyBuilderLabel.enableShadow(true);
		
		settings.addObject(stopMovement, stopMovementLabel, runKeyBuilderTutorial, runKeyBuilderLabel);
		
		keyList = new EGuiContainer(this, startX + 2, settings.getDimensions().endY + 1, width - 4, (guiInstance.endY - 2) - (settings.getDimensions().endY + 1));
		keyList.setTitle("Hotkey Lists");
		EDimension kDim = keyList.getDimensions();
		loadKeyList = new EGuiButton(keyList, kDim.midX - (110 / 2), kDim.startY + 29, 110, 20, "Reload HotKeys");
		saveKeyList = new EGuiButton(keyList, kDim.midX - (110 / 2), loadKeyList.endY + 5, 110, 20, "Save HotKeys");
		resetKeyList = new EGuiButton(keyList, kDim.midX - (110 / 2), saveKeyList.endY + 5, 110, 20, "Reset HotKeys");
		
		keyList.addObject(loadKeyList, saveKeyList, resetKeyList);
		
		addObject(settings, keyList);
	}
	
	@Override
	public void drawObject(int mXIn, int mYIn) {
		drawDefaultBackground();
		super.drawObject(mXIn, mYIn);
	}
	
	@Override
	public void actionPerformed(IEnhancedActionObject object, Object... args) {
		if (object == stopMovement) {
			mod.setStopMovementOnPress(!mod.doesCtrlKeyStopMovement());
			stopMovement.updateTrueFalseDisplay(mod.doesCtrlKeyStopMovement());
			mod.getConfig().saveMainConfig();
		}
		if (object == runKeyBuilderTutorial) {
			mod.setRunTutorial(!mod.runTutorial());
			runKeyBuilderTutorial.updateTrueFalseDisplay(mod.runTutorial());
			mod.getConfig().saveMainConfig();
		}
		if (object.equals(loadKeyList)) { reloadKeys(); }
		if (object.equals(saveKeyList)) { saveKeys(); }
		if (object.equals(resetKeyList)) { resetKeys(); }
	}
	
	public void reloadKeys() {
		mod.loadHotKeys();
		msgBox = new EGuiDialogueBox(DialogueBoxTypes.ok);
		msgBox.setTitle("Load");
		msgBox.setMessage("Success! Hotkeys reloaded.").setMessageColor(0x55ff55);
		EnhancedMC.displayWindow(msgBox, CenterType.screen);
	}
	
	public void saveKeys() {
		mod.saveHotKeys();
		msgBox = new EGuiDialogueBox(DialogueBoxTypes.ok);
		msgBox.setTitle("Save");
		msgBox.setMessage("Success! Hotkeys saved.").setMessageColor(0x55ff55);
		EnhancedMC.displayWindow(msgBox, CenterType.screen);
	}
	
	public void resetKeys() {
		msgBox = new EGuiDialogueBox(DialogueBoxTypes.yesNo) {
			@Override
			public void actionPerformed(IEnhancedActionObject object, Object... args) {
				if (object == yes) {
					this.close();
					mod.reset();
					msgBox = new EGuiDialogueBox(DialogueBoxTypes.ok);
					msgBox.setTitle("HotKey Reset");
					msgBox.setMessage("Success! All user hotkeys have been deleted.").setMessageColor(0x55ff55);
					EnhancedMC.displayWindow(msgBox, CenterType.screen);
				}
				if (object == no) { this.close(); }
			}
		};
		msgBox.setTitle("HotKey Reset");
		msgBox.setMessage("Are you sure you want to reset hotkeys? This will delete all user made hotkeys and clear the user key file! CANNOT BE UNDONE!").setMessageColor(0xff5555);
		guiInstance.addObject(msgBox);
	}
}
