package com.Whodundid.blink;

import com.Whodundid.core.app.AppType;
import com.Whodundid.core.app.RegisteredApps;
import com.Whodundid.core.enhancedGui.guiObjects.actionObjects.EGuiButton;
import com.Whodundid.core.enhancedGui.guiObjects.actionObjects.EGuiTextField;
import com.Whodundid.core.enhancedGui.guiObjects.basicObjects.EGuiLabel;
import com.Whodundid.core.enhancedGui.types.WindowParent;
import com.Whodundid.core.enhancedGui.types.interfaces.IEnhancedActionObject;

public class BlinkGui extends WindowParent {

	BlinkApp mod = (BlinkApp) RegisteredApps.getApp(AppType.BLINK);
	EGuiTextField cooldownEntry;
	EGuiButton resetCooldown;
	EGuiLabel changeBlinkLabel;
	
	public BlinkGui() {
		super();
		defaultPos();
		setObjectName("Blink Settings");
		setMinWidth(170).setMinHeight(170);
		setResizeable(true);
		aliases.add("blinkgui");
	}
	
	@Override
	public void initObjects() {
		defaultHeader(this);
		
		cooldownEntry = new EGuiTextField(this, startX + 10, startY + 11, width - 90, 18).setOnlyAcceptNumbers(true);
		resetCooldown = new EGuiButton(this, cooldownEntry.endX + 10, cooldownEntry.startY - 1, 60, 20, "Reset");
		changeBlinkLabel = new EGuiLabel(this, startX + 10, cooldownEntry.endY + 10, "Current cooldown: " + mod.getBlinkCooldown());
		
		addObject(cooldownEntry, resetCooldown, changeBlinkLabel);
	}
	
	@Override
	public void drawObject(int mXIn, int mYIn) {
		drawDefaultBackground();
		super.drawObject(mXIn, mYIn);
	}
	
	@Override
	public void actionPerformed(IEnhancedActionObject object, Object... args) {
		if (object == cooldownEntry) {
			try {
				if (cooldownEntry.isNotEmpty()) {
					long val = Long.parseLong(cooldownEntry.getText());
					mod.setBlinkCooldown(val);
					changeBlinkLabel.setDisplayString("Current cooldown: " + mod.getBlinkCooldown());
				}
			} catch (Exception e) { e.printStackTrace(); }
		}
		if (object == resetCooldown) {
			mod.resetBlinkCooldown();
			changeBlinkLabel.setDisplayString("Current cooldown: " + mod.getBlinkCooldown());
		}
	}
 }
