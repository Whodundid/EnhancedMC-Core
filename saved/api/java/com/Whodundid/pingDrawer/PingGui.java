package com.Whodundid.pingDrawer;

import com.Whodundid.core.app.AppType;
import com.Whodundid.core.app.RegisteredApps;
import com.Whodundid.core.enhancedGui.guiObjects.actionObjects.EGuiButton;
import com.Whodundid.core.enhancedGui.guiObjects.utilityObjects.EGuiScreenLocationSelector;
import com.Whodundid.core.enhancedGui.types.WindowParent;
import com.Whodundid.core.enhancedGui.types.interfaces.IEnhancedActionObject;
import com.Whodundid.core.enhancedGui.types.interfaces.IEnhancedGuiObject;

//Last edited: Dec 10, 2018
//First Added: Dec 10, 2018
//Author: Hunter Bragg

public class PingGui extends WindowParent {

	PingApp pingMod = (PingApp) RegisteredApps.getApp(AppType.PING);
	EGuiButton chatDraw;
	EGuiScreenLocationSelector locationSelector;
	
	public PingGui() { super(); }
	public PingGui(Object oldGuiIn) { super(oldGuiIn); }
	public PingGui(IEnhancedGuiObject parentIn) { super(parentIn); }
	public PingGui(IEnhancedGuiObject parentIn, Object oldGuiIn) { super(parentIn, oldGuiIn); }
	public PingGui(IEnhancedGuiObject parentIn, int posX, int posY) { super(parentIn, posX, posY); }
	public PingGui(IEnhancedGuiObject parentIn, int posX, int posY, Object oldGuiIn) { super(parentIn, posX, posY, oldGuiIn); }
	
	@Override
	public void initGui() {
		setObjectName("Ping Settings");
		defaultPos();
		super.initGui();
	}
	
	@Override
	public void initObjects() {
		defaultHeader(this);
		
		chatDraw = new EGuiButton(this, midX - 90, endY - 35, 52, 20, pingMod.drawWithChatOpen ? "True" : "False").setDisplayStringColor(pingMod.drawWithChatOpen ? 0x55ff55 : 0xff5555);
		locationSelector = new EGuiScreenLocationSelector(this, pingMod, midX - 78, midY - 83, 156);
		locationSelector.setDisplayName("Ping");
		
		addObject(locationSelector, chatDraw);
	}
	
	@Override
	public void drawObject(int mXIn, int mYIn) {
		drawDefaultBackground();
		drawString("Draw while chat is open", midX - 28, endY - 29, 0xffbb00);
		//drawRect(startX + 5, startY + 5, endX - 5, endY - 5, 0xff000000);
		//drawRect(startX + 6, startY + 6, endX - 6, endY - 6, 0xff353535);
		super.drawObject(mXIn, mYIn);
	}
	
	@Override
	public void actionPerformed(IEnhancedActionObject object, Object... args) {
		if (object.equals(chatDraw)) {
			pingMod.setDrawWithChatOpen(!pingMod.drawWithChatOpen);
			chatDraw.setDisplayString(pingMod.drawWithChatOpen ? "True" : "False");
			chatDraw.color = pingMod.drawWithChatOpen ? 0x55ff55 : 0xff4444;
		}
		pingMod.getConfig().saveMainConfig();
	}
}
