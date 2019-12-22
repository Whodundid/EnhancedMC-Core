package com.Whodundid.core.notifications.baseObjects;

import com.Whodundid.core.notifications.Notification;
import com.Whodundid.core.notifications.NotificationObject;
import com.Whodundid.core.notifications.NotificationType;
import com.Whodundid.core.util.renderUtil.EColors;
import com.Whodundid.core.util.renderUtil.Resources;
import net.minecraft.client.renderer.GlStateManager;
import org.lwjgl.opengl.GL11;

public class ChatNotification extends NotificationObject {
	
	public String sender;
	
	public ChatNotification(String senderIn, String messageIn) {
		super();
		note = new Notification(NotificationType.chat, messageIn);
		sender = senderIn;
	}
	
	@Override
	public void initGui() {
		setDimensions(startX, res.getScaledHeight() - 52, 44 + fontRenderer.getStringWidth(sender) + fontRenderer.getStringWidth(note.getMessage()), 30);
		setPinned(true);
	}
	
	@Override
	public void initObjects() {
		//defaultHeader(this);
	}
	
	@Override
	public void drawObject(int mXIn, int mYIn, float ticks) {
		super.drawObject(mXIn, mYIn, ticks);
		GlStateManager.color(1f, 1f, 1f, 0.75f);
		mc.renderEngine.bindTexture(Resources.guiInfo);
		this.drawCustomSizedTexture(startX + 5, midY - 10, 0, 0, 20, 20, 20, 20);
		int dist = drawStringWithShadow(sender + ":", startX + 30, midY - 3, EColors.cyan.c());
		this.drawStringWithShadow(note.getMessage(), dist + 5, midY - 3, EColors.lgray.c());
	}
	
	@Override
	public void mousePressed(int mXIn, int mYIn, int button) {
		this.close();
	}
}
