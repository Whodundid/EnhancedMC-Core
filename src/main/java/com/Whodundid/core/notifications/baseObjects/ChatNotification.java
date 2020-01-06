package com.Whodundid.core.notifications.baseObjects;

import com.Whodundid.core.coreSubMod.EMCResources;
import com.Whodundid.core.notifications.NotificationObject;
import com.Whodundid.core.notifications.NotificationType;
import com.Whodundid.core.util.renderUtil.EColors;
import net.minecraft.client.renderer.GlStateManager;

//Author: Hunter Bragg

public class ChatNotification extends NotificationObject {
	
	public String sender;
	
	public ChatNotification(String senderIn, String messageIn) {
		sender = senderIn;
		message = messageIn;
		type = NotificationType.chat;
	}
	
	@Override
	public void initGui() {
		setDimensions(startX, res.getScaledHeight() - 52, 44 + fontRenderer.getStringWidth(sender) + fontRenderer.getStringWidth(message), 30);
		setPinned(true);
	}
	
	@Override
	public void drawObject(int mXIn, int mYIn, float ticks) {
		super.drawObject(mXIn, mYIn, ticks);
		GlStateManager.color(1f, 1f, 1f, 0.75f);
		mc.renderEngine.bindTexture(EMCResources.guiInfo);
		this.drawTexture(startX + 5, midY - 10, 0, 0, 20, 20, 20, 20);
		int dist = drawStringWithShadow(sender + ":", startX + 30, midY - 3, EColors.cyan.c());
		this.drawStringWithShadow(message, dist + 5, midY - 3, EColors.lgray.c());
	}
	
	@Override
	public void mousePressed(int mXIn, int mYIn, int button) {
		super.mousePressed(mXIn, mYIn, button);
		
		if (button == 0) {
			close();
		}
	}
}
