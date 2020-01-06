package com.Whodundid.core.debug;

import java.awt.Color;
import com.Whodundid.core.EnhancedMC;
import com.Whodundid.core.coreSubMod.EMCResources;
import com.Whodundid.core.enhancedGui.types.WindowParent;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;

//Author: Hunter Bragg

public class ImportantGui extends WindowParent {
	
	importantHolder holder;
	int importantColor = 0xff000000;
	
	@Override
	public void initGui() {
		ScaledResolution res = new ScaledResolution(mc);
		centerObjectWithSize(res.getScaledWidth(), res.getScaledHeight());
		holder = new importantHolder();
		super.initGui();
	}
	
	@Override
	public void drawObject(int mX, int mY, float ticks) {
		drawRect(0, 0, res.getScaledWidth(), res.getScaledHeight(), importantColor);
		drawRect(midX - 257, 0, midX + 257, res.getScaledHeight(), 0xff4f4f40);
		GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
		holder.draw();
		importantColor = Color.HSBtoRGB(System.currentTimeMillis() % 10000L / 5000.0f, 0.8f, 1f);
		GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
		GlStateManager.scale(2.0f, 2.0f, 2.0f);
		drawCenteredStringWithShadow("W H Y  H E L L O  T H E R E,  F R I E N D !", midX / 2, midY / 2 + 50, -importantColor);
		GlStateManager.scale(1.0f, 1.0f, 1.0f);
	}

	private class importantHolder {
		
		public void draw() {
			mc.renderEngine.bindTexture(EMCResources.important1);
			guiInstance.drawModalRectWithCustomSizedTexture(midX - 256, midY - 256, 0, 0, 256, 256, 256, 256);
			mc.renderEngine.bindTexture(EMCResources.important2);
			guiInstance.drawModalRectWithCustomSizedTexture(midX, midY - 256, 0, 0, 256, 256, 256, 256);
			mc.renderEngine.bindTexture(EMCResources.important3);
			guiInstance.drawModalRectWithCustomSizedTexture(midX, midY, 0, 0, 256, 256, 256, 256);
			mc.renderEngine.bindTexture(EMCResources.important4);
			guiInstance.drawModalRectWithCustomSizedTexture(midX - 256, midY, 0, 0, 256, 256, 256, 256);
			
			if (EnhancedMC.updateCounter / 20 % 2 == 0) {
				mc.renderEngine.bindTexture(EMCResources.important5);
				guiInstance.drawModalRectWithCustomSizedTexture(midX - 450, midY - 200, 0, 0, 256, 166, 256, 166);
				mc.renderEngine.bindTexture(EMCResources.important6);
				guiInstance.drawModalRectWithCustomSizedTexture(midX - 450, midY - 35, 0, 0, 256, 165, 256, 165);
			}
			else {
				mc.renderEngine.bindTexture(EMCResources.important7);
				guiInstance.drawModalRectWithCustomSizedTexture(midX + 200, midY - 200, 0, 0, 256, 166, 256, 166);
				mc.renderEngine.bindTexture(EMCResources.important8);
				guiInstance.drawModalRectWithCustomSizedTexture(midX + 200, midY - 35, 0, 0, 256, 165, 256, 165);
			}
		}
	}
}
