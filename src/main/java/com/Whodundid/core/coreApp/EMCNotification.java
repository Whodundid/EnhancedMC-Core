package com.Whodundid.core.coreApp;

import com.Whodundid.core.EnhancedMC;
import com.Whodundid.core.enhancedGui.guiObjects.actionObjects.EGuiButton;
import com.Whodundid.core.notifications.util.NotificationObject;
import com.Whodundid.core.util.renderUtil.EColors;
import net.minecraft.client.renderer.GlStateManager;

//Author: Hunter Bragg

public class EMCNotification extends NotificationObject {
	
	public EMCNotification(String messageIn) {
		super(CoreApp.emcNotification);
		message = messageIn;
	}
	
	@Override
	public void initGui() {
		setDimensions(startX, res.getScaledHeight() - 52, 44 + mc.fontRendererObj.getStringWidth(message), 30);
		setPinned(true);
	}
	
	@Override
	public void drawObject(int mXIn, int mYIn) {
		super.drawObject(mXIn, mYIn);
		GlStateManager.color(1f, 1f, 1f, 0.75f);
		drawTexture(startX + 5, midY - 10, 20, 20, EMCResources.guiInfo);
		drawStringWithShadow(message, startX + 30, midY - 3, EColors.seafoam);
	}
	
	@Override
	public void mousePressed(int mXIn, int mYIn, int button) {
		super.mousePressed(mXIn, mYIn, button);
		
		if (button == 0) {
			EGuiButton.playPressSound();
			close();
			if (attentionObject != null) {
				if (EnhancedMC.getRenderer().getObjects().contains(attentionObject)) { attentionObject.requestFocus(); }
				else {
					EnhancedMC.displayWindow(attentionObject);
				}
			}
		}
	}
	
}
