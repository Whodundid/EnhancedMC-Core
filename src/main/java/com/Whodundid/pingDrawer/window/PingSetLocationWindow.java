package com.Whodundid.pingDrawer.window;

import com.Whodundid.core.app.AppType;
import com.Whodundid.core.app.RegisteredApps;
import com.Whodundid.core.windowLibrary.windowTypes.SetLocationWindow;
import com.Whodundid.core.windowLibrary.windowUtil.windowEvents.ObjectEvent;
import com.Whodundid.core.windowLibrary.windowUtil.windowEvents.eventUtil.MouseType;
import com.Whodundid.core.windowLibrary.windowUtil.windowEvents.events.EventMouse;
import com.Whodundid.pingDrawer.PingApp;

//Last edited: Dec 10, 2018
//First Added: Dec 10, 2018
//Author: Hunter Bragg

public class PingSetLocationWindow extends SetLocationWindow {

	PingApp pingMod = (PingApp) RegisteredApps.getApp(AppType.PING);
	
	@Override
	public void initWindow() {
		setDimensions(res.getScaledWidth(), res.getScaledHeight());
		enableHeader(false);
		getTopParent().registerListener(this);
		getTopParent().setEscapeStopper(this);
		hideAllOnRenderer(this);
	}
	
	@Override
	public void drawObject(int mX, int mY) {
		int midX = res.getScaledWidth() / 2;
		int midY = res.getScaledHeight() / 2;
		
		drawRect(0, 0, res.getScaledWidth(), res.getScaledHeight(), 0x55000000); //background overlay
		
		drawStringCS("Move to desired location.", midX, midY - 25, 0xffcc00);
		drawStringCS("Press left click to confirm, Esc to cancel.", midX, midY - 15, 0xffcc00);
		
		String msg = "Calculating..";
		String pingS = "";
		
		if (pingMod.doesClientHavePing()) {
			msg = "PING: ";
			pingS = pingMod.getClientServerPing() + " ms";
		}
		
		int msgLen = mc.fontRendererObj.getStringWidth(msg);
		int pingLen = mc.fontRendererObj.getStringWidth(pingS);
		int l = msgLen + pingLen;
		
		drawRect(mX, mY + 1, mX + l + 1, mY - 9, Integer.MIN_VALUE);
		
		if (PingApp.drawOwnThresholds.get()) {
			int eX = drawString(msg, mX + 1, mY - 8, PingApp.ownColor.get());
			drawString(pingS, eX, mY - 8, pingMod.getPingColor(pingMod.getClientServerPing()));
		}
		else {
			drawString(msg, mX + 1, mY - 8, PingApp.ownColor.get());
		}
		
		super.drawObject(mX, mY);
	}
	
	@Override
	public void keyPressed(char typedChar, int keyCode) {
		if (keyCode == 1) { close(); }
		
		super.keyPressed(typedChar, keyCode);
	}
	
	@Override
	public void onEvent(ObjectEvent e) {
		if (e instanceof EventMouse) {
			EventMouse me = (EventMouse) e;
			if (me.getMouseType() == MouseType.Pressed && me.getMouseButton() == 0) {
				pingMod.setLocation(me.getMouseX(), me.getMouseY() + 1);
				pingMod.getConfig().saveMainConfig();
				close();
			}
		}
	}
	
	@Override
	public void close() {
		super.close();
		
		System.out.println("unhiding");
		
		unideAllOnRenderer();
		getTopParent().unregisterListener(this);
		getTopParent().setEscapeStopper(null);
	}
	
}
