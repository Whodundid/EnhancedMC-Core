package com.Whodundid.blink;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;

//Last edited: 10-16-18
//First Added: 4-19-18
//Author: Hunter Bragg

public class BlinkDrawer {
	
	static Minecraft mc = Minecraft.getMinecraft();
	protected BlinkApp base;
	
	public BlinkDrawer(BlinkApp baseIn) {
		base = baseIn;
	}
	
	public void drawBlinkCharges() {
		ScaledResolution res = new ScaledResolution(mc);
		GlStateManager.pushMatrix();
		GlStateManager.enableAlpha();
		GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
		if (base.blinkCount == 3) {
			mc.renderEngine.bindTexture(BlinkResources.blinkFull);
			Gui.drawModalRectWithCustomSizedTexture(res.getScaledWidth() / 2 - 17, res.getScaledHeight() / 2 + 10, 0, 0, 35, 11, 35, 11);
			Gui.drawModalRectWithCustomSizedTexture(res.getScaledWidth() / 2 - 21, res.getScaledHeight() / 2 + 20, 0, 0, 43, 13, 43, 13);
			Gui.drawModalRectWithCustomSizedTexture(res.getScaledWidth() / 2 - 24, res.getScaledHeight() / 2 + 32, 0, 0, 49, 15, 49, 15);
		} else if (base.blinkCount == 2) {
			mc.renderEngine.bindTexture(BlinkResources.blinkFull);
			Gui.drawModalRectWithCustomSizedTexture(res.getScaledWidth() / 2 - 17, res.getScaledHeight() / 2 + 10, 0, 0, 35, 11, 35, 11);				
			Gui.drawModalRectWithCustomSizedTexture(res.getScaledWidth() / 2 - 21, res.getScaledHeight() / 2 + 20, 0, 0, 43, 13, 43, 13);
			mc.renderEngine.bindTexture(BlinkResources.blinkUsed);
			Gui.drawModalRectWithCustomSizedTexture(res.getScaledWidth() / 2 - 24, res.getScaledHeight() / 2 + 32, 0, 0, 49, 15, 49, 15);
		} else if (base.blinkCount == 1) {
			mc.renderEngine.bindTexture(BlinkResources.blinkFull);
			Gui.drawModalRectWithCustomSizedTexture(res.getScaledWidth() / 2 - 17, res.getScaledHeight() / 2 + 10, 0, 0, 35, 11, 35, 11);
			mc.renderEngine.bindTexture(BlinkResources.blinkUsed);
			Gui.drawModalRectWithCustomSizedTexture(res.getScaledWidth() / 2 - 21, res.getScaledHeight() / 2 + 20, 0, 0, 43, 13, 43, 13);				
			Gui.drawModalRectWithCustomSizedTexture(res.getScaledWidth() / 2 - 24, res.getScaledHeight() / 2 + 32, 0, 0, 49, 15, 49, 15);
		} else {
			mc.renderEngine.bindTexture(BlinkResources.blinkUsed);
			Gui.drawModalRectWithCustomSizedTexture(res.getScaledWidth() / 2 - 17, res.getScaledHeight() / 2 + 10, 0, 0, 35, 11, 35, 11);	
			Gui.drawModalRectWithCustomSizedTexture(res.getScaledWidth() / 2 - 21, res.getScaledHeight() / 2 + 20, 0, 0, 43, 13, 43, 13);
			Gui.drawModalRectWithCustomSizedTexture(res.getScaledWidth() / 2 - 24, res.getScaledHeight() / 2 + 32, 0, 0, 49, 15, 49, 15);
		}
		GlStateManager.disableAlpha();
		GlStateManager.popMatrix();
	}
}
