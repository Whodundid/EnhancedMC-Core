package com.Whodundid.core.notifications.util;

import com.Whodundid.core.EnhancedMC;
import com.Whodundid.core.coreApp.EMCResources;
import com.Whodundid.core.enhancedGui.guiObjects.actionObjects.EGuiButton;
import com.Whodundid.core.enhancedGui.types.WindowParent;
import com.Whodundid.core.enhancedGui.types.interfaces.IEnhancedActionObject;
import com.Whodundid.core.notifications.window.NotificationRCM;
import com.Whodundid.core.renderer.renderUtil.IRendererProxy;
import com.Whodundid.core.util.renderUtil.CenterType;

//Author: Hunter Bragg

public abstract class NotificationObject extends WindowParent {

	protected String message = "";
	protected NotificationType type;
	protected WindowParent attentionObject = null;
	protected EGuiButton close;
	protected boolean expires = true;
	protected boolean onlyDrawOnHud = false;
	boolean moveOut = false;
	boolean moving = false;
	boolean movingOut = false;
	long startTime = 0l;
	long birthTime = 0l;
	long timeOut = 4500l;
	double sX = -1;
	double v0 = -1;
	double a = -1;
	double dist = -1;
	double time = -1;
	
	protected NotificationObject(NotificationType typeIn) {
		super();
		type = typeIn;
	}
	
	@Override
	public void initObjects() {
		close = new EGuiButton(this, endX - 10, startY + 2, 8, 8);
		close.setTextures(EMCResources.guiCloseButton, EMCResources.guiCloseButtonSel);
		
		close.setVisible(false);
		
		addObject(close);
	}

	@Override
	public void drawObject(int mXIn, int mYIn) {
		if (onlyDrawOnHud && !(mc.currentScreen instanceof IRendererProxy)) { close(); }
		
		drawRect(startX, startY, startX + 1, endY, 0xff000000); //left border
		drawRect(startX + 1, endY - 1, endX - 1, endY, 0xff000000); //bottom border
		drawRect(endX - 1, startY, endX, endY, 0xff000000); //right border
		drawRect(startX + 1, startY, endX - 1, startY + 1, 0xff000000); //top border
		
		int inColor = isMouseInside(mXIn, mYIn) ? 0xff2b2b2b : 0xbb2b2b2b;
		drawRect(startX + 1, startY + 1, endX - 1, endY - 1, inColor); //background
		
		if (!isMouseInside(mXIn, mYIn)) { checkTime(); }
		else if (!moving) { birthTime = System.currentTimeMillis(); }
		close.setVisible(isMouseInside(mXIn, mYIn));
		
		calcPos();
		super.drawObject(mXIn, mYIn);
	}
	
	protected void calcPos() {
		if (moving) {
			double time = (double) (System.nanoTime() - startTime) / 1000000000;
			double nX = sX + (v0 * time) + (a * Math.pow(time, 2)) / 2;
			double dist = nX - startX;
			move((int) dist, 0);
			if (time >= this.time) {
				moving = false;
				startTime = 0l;
				if (moveOut) { close(); }
			}
		}
	}
	
	protected void checkTime() {
		if (!moveOut) {
			if (expires) {
				if (System.currentTimeMillis() - birthTime >= timeOut) {
					moveOut();
				}
			}
		}
	}

	@Override
	public void onFirstDraw() {
		super.onFirstDraw();
		birthTime = System.currentTimeMillis();
		setPosition(res.getScaledWidth(), startY);
		moveOverTime(-(width + 3), 0.45);
	}

	private void moveOverTime(int amount, double time) {
		this.time = time;
		sX = startX;
		double eX = sX + amount;
		v0 = (2 * (eX - sX)) / time;
		a = -v0 / time;
		startTime = System.nanoTime();
		moving = true;
	}

	@Override
	public void mousePressed(int mXIn, int mYIn, int button) {
		super.mousePressed(mXIn, mYIn, button);
		if (button == 1) {
			EnhancedMC.displayWindow(new NotificationRCM(this), CenterType.cursorCorner);
		}
	}

	@Override
	public void close() {
		super.close();
		EnhancedMC.getNotificationHandler().removeCurrentNotification();
	}
	
	@Override
	public void actionPerformed(IEnhancedActionObject object, Object... args) {
		if (object == close) { close(); }
	}

	public void moveOut() {
		moveOverTime(width + 3, 0.75);
		moveOut = true;
	}

	public String getMessage() { return message; }
	public NotificationType getType() { return type; }
	public WindowParent getAttentionObject() { return attentionObject; }
	public boolean onlyDrawsOnHud() { return onlyDrawOnHud; }

	public NotificationObject setOnlyDrawOnHud(boolean val) { onlyDrawOnHud = val; return this; }
	public NotificationObject setExpires(boolean val) { expires = val; return this; }
	public NotificationObject setTimeOut(long timeIn) { timeOut = timeIn; return this; }
	public NotificationObject setAttentionObject(WindowParent objIn) { attentionObject = objIn; return this; }
}
