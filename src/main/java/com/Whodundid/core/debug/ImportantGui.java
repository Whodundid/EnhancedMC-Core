package com.Whodundid.core.debug;

import java.awt.Color;
import com.Whodundid.core.EnhancedMC;
import com.Whodundid.core.coreApp.EMCResources;
import com.Whodundid.core.enhancedGui.types.WindowParent;
import com.Whodundid.core.util.renderUtil.GLObject;
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
	public void drawObject(int mX, int mY) {
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
			GLObject.drawTexture(midX - 256, midY - 256, 256, 256, EMCResources.important1);
			GLObject.drawTexture(midX, midY - 256, 256, 256, EMCResources.important2);
			GLObject.drawTexture(midX, midY, 256, 256, EMCResources.important3);
			GLObject.drawTexture(midX - 256, midY, 256, 256, EMCResources.important4);
			
			if (EnhancedMC.updateCounter / 20 % 2 == 0) {
				GLObject.drawTexture(midX - 450, midY - 200, 256, 166, EMCResources.important5);
				GLObject.drawTexture(midX - 450, midY - 35, 256, 165, EMCResources.important6);
			}
			else {
				GLObject.drawTexture(midX + 200, midY - 200, 256, 166, EMCResources.important7);
				GLObject.drawTexture(midX + 200, midY - 35, 256, 165, EMCResources.important8);
			}
		}
	}
	
	@Override public boolean showInLists() { return false; }
}
