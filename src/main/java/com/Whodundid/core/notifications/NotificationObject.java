package com.Whodundid.core.notifications;

import com.Whodundid.core.EnhancedMC;
import com.Whodundid.core.enhancedGui.types.WindowParent;
import com.Whodundid.core.notifications.baseObjects.NotificationRCM;
import com.Whodundid.core.util.renderUtil.CenterType;

public abstract class NotificationObject extends WindowParent {

	protected String message = "";
	protected NotificationType type = NotificationType.emc;
	protected WindowParent attentionObject = null;
	boolean moveOut = false;
	boolean moving = false;
	boolean movingOut = false;
	long startTime = 0l;
	long birthTime = 0l;
	double sX = -1;
	double v0 = -1;
	double a = -1;
	double dist = -1;
	double time = -1;

	@Override
	public void drawObject(int mXIn, int mYIn, float ticks) {
		drawRect(startX, startY, startX + 1, endY, 0xff000000); //left border
		drawRect(startX + 1, endY - 1, endX - 1, endY, 0xff000000); //bottom border
		drawRect(endX - 1, startY, endX, endY, 0xff000000); //right border
		drawRect(startX + 1, startY, endX - 1, startY + 1, 0xff000000); //top border
		
		int inColor = isMouseInside(mXIn, mYIn) ? 0xff2b2b2b : 0xbb2b2b2b;
		drawRect(startX + 1, startY + 1, endX - 1, endY - 1, inColor); //background
		
		if (!isMouseInside(mXIn, mYIn)) { checkTime(); }
		calcPos();
		super.drawObject(mXIn, mYIn, ticks);
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
			if (System.currentTimeMillis() - birthTime >= 4500) {
				moveOut();
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
		if (button == 1) {
			EnhancedMC.displayEGui(new NotificationRCM(), CenterType.cursorCorner);
		}
		super.mousePressed(mXIn, mYIn, button);
	}

	@Override
	public void close() {
		super.close();
		EnhancedMC.getNotificationHandler().removeCurrentNotification();
	}

	public void moveOut() {
		moveOverTime(width + 3, 0.75);
		moveOut = true;
	}

	public String getMessage() { return message; }
	public NotificationType getNotificationType() { return type; }
	public WindowParent getAttentionObject() { return attentionObject; }

	public NotificationObject setAttentionObject(WindowParent objIn) { attentionObject = objIn; return this; }

}