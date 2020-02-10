package com.Whodundid.core.notifications.baseObjects;

import com.Whodundid.core.EnhancedMC;
import com.Whodundid.core.coreSubMod.EMCResources;
import com.Whodundid.core.notifications.NotificationObject;
import com.Whodundid.core.notifications.NotificationType;
import com.Whodundid.core.util.renderUtil.EColors;
import net.minecraft.client.renderer.GlStateManager;

//Author: Hunter Bragg

public class EMCNotification extends NotificationObject {
	
	public EMCNotification(String messageIn) {
		message = messageIn;
		type = NotificationType.emc;
	}
	
	@Override
	public void initGui() {
		setDimensions(startX, res.getScaledHeight() - 52, 44 + fontRenderer.getStringWidth(message), 30);
		setPinned(true);
	}
	
	@Override
	public void drawObject(int mXIn, int mYIn, float ticks) {
		super.drawObject(mXIn, mYIn, ticks);
		GlStateManager.color(1f, 1f, 1f, 0.75f);
		mc.renderEngine.bindTexture(EMCResources.guiInfo);
		drawTexture(startX + 5, midY - 10, 20, 20);
		drawStringWithShadow(message, startX + 30, midY - 3, EColors.cyan.c());
	}
	
	@Override
	public void mousePressed(int mXIn, int mYIn, int button) {
		super.mousePressed(mXIn, mYIn, button);
		
		if (button == 0) {
			close();
			if (attentionObject != null) {
				if (EnhancedMC.getRenderer().getObjects().contains(attentionObject)) { attentionObject.requestFocus(); }
				else {
					EnhancedMC.displayEGui(attentionObject);
				}
			}
		}
	}
}
