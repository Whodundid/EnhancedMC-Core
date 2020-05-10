package com.Whodundid.parkourHelper;

import com.Whodundid.core.app.AppType;
import com.Whodundid.core.app.RegisteredApps;
import com.Whodundid.core.enhancedGui.guiObjects.actionObjects.EGuiButton;
import com.Whodundid.core.enhancedGui.guiObjects.actionObjects.EGuiTextField;
import com.Whodundid.core.enhancedGui.types.WindowParent;
import com.Whodundid.core.enhancedGui.types.interfaces.IEnhancedActionObject;
import com.Whodundid.core.enhancedGui.types.interfaces.IEnhancedGuiObject;

public class ParkourGui extends WindowParent {
	
	ParkourApp mod = (ParkourApp) RegisteredApps.getApp(AppType.PARKOUR);
	
	public ParkourGui() { super(); }
	public ParkourGui(Object oldGuiIn) { super(oldGuiIn); }
	public ParkourGui(IEnhancedGuiObject parentIn) { super(parentIn); }
	public ParkourGui(IEnhancedGuiObject parentIn, Object oldGuiIn) { super(parentIn, oldGuiIn); }
	public ParkourGui(IEnhancedGuiObject parentIn, int posX, int posY) { super(parentIn, posX, posY); }
	public ParkourGui(IEnhancedGuiObject parentIn, int posX, int posY, Object oldGuiIn) { super(parentIn, posX, posY, oldGuiIn); }
	
	double curVal;
	EGuiTextField inputField;
	EGuiButton enable;
	
	@Override
	public void initGui() {
		setObjectName("Parkour Settings");
		curVal = mod.jumpOffset;
		defaultPos();
		super.initGui();
	}
	
	@Override
	public void initObjects() {
		defaultHeader(this);
		
		enable = new EGuiButton(this, startX + 20, endY - 50, 80, 20, "enable");
		
		inputField = new EGuiTextField(this, startX + 30, midY, width - 60, 16) {
			@Override
			public void keyPressed(char typedChar, int keyCode) {
				super.keyPressed(typedChar, keyCode);
				double previousVal = new Double(curVal);
				if (keyCode == 28) { parseInput(inputField.getText()); }
			}
		};
		
		addObject(enable, inputField);
	}
	
	@Override
	public void drawObject(int mXIn, int mYIn) {
		drawDefaultBackground();
		drawCenteredString("" + curVal, midX, midY - 76, 0xffffff);
		drawCenteredString("0.535 & 0.629", midX, midY + 76, 0x00ff00);
		super.drawObject(mXIn, mYIn);
	}
	
	@Override
	public void mousePressed(int mXIn, int mYIn, int button) {
		if (inputField != null) { inputField.requestFocus(); }
	}
	
	@Override
	public void actionPerformed(IEnhancedActionObject object, Object... args) {
		if (object == enable) {
			mod.toggleRunning();
		}
	}
	
	private boolean parseInput(String input) {
		if (input != null && !input.isEmpty()) {
			double previousVal = new Double(curVal);
			try {
				double parsedVal = Double.parseDouble(input);
				ParkourApp.jumpOffset = parsedVal;
				curVal = ParkourApp.jumpOffset;
				return true;
			} catch (Exception e) {
				e.printStackTrace();
				curVal = previousVal;
			}
		}
		return false;
	}
}
