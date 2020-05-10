package com.Whodundid.pingDrawer;

import com.Whodundid.core.app.AppType;
import com.Whodundid.core.app.RegisteredApps;
import com.Whodundid.core.enhancedGui.objectEvents.EventMouse;
import com.Whodundid.core.enhancedGui.objectEvents.ObjectEvent;
import com.Whodundid.core.enhancedGui.objectEvents.eventUtil.MouseType;
import com.Whodundid.core.enhancedGui.types.SetLocationGui;

//Last edited: Dec 10, 2018
//First Added: Dec 10, 2018
//Author: Hunter Bragg

public class PingSetLocationGui extends SetLocationGui {

	PingApp pingMod = (PingApp) RegisteredApps.getApp(AppType.PING);
	
	@Override
	public void initGui() {
		super.initGui();
		enableHeader(false);
		getTopParent().registerListener(this);
		hideAllOnRenderer(this);
	}
	
	@Override
	public void drawObject(int mX, int mY) {
		int midX = res.getScaledWidth() / 2;
		int midY = res.getScaledHeight() / 2;
		
		drawRect(0, 0, res.getScaledWidth(), res.getScaledHeight(), 0x55000000); //background overlay
		
		drawCenteredStringWithShadow("Move to desired location.", midX, midY - 25, 0xffcc00);
		drawCenteredStringWithShadow("Press left click to confirm, Esc to cancel.", midX, midY - 15, 0xffcc00);
		
		String msg = pingMod.doesClientHavePing() ? "PING: " + pingMod.getClientServerPing() + " ms" : "Calculating..";
		int l = mc.fontRendererObj.getStringWidth(msg);
		drawRect(mX, mY + 1, mX + l + 1, mY - 9, Integer.MIN_VALUE);
		mc.fontRendererObj.drawString(msg, mX + 1, mY - 8, 0x00ff00);
	}
	
	@Override
	public void onEvent(ObjectEvent e) {
		if (e instanceof EventMouse) {
			EventMouse me = (EventMouse) e;
			if (me.getMouseType() == MouseType.Pressed && me.getMouseButton() == 0) {
				pingMod.setLocation(me.getMouseX(), me.getMouseY() + 1);
				pingMod.getConfig().saveMainConfig();
				getTopParent().unregisterListener(this);
				unideAllOnRenderer();
				close();
			}
		}
	}
}
