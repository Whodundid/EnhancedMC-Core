package com.Whodundid.core.debug;

import java.awt.Color;
import com.Whodundid.core.coreApp.EMCResources;
import com.Whodundid.core.util.renderUtil.GLObject;
import com.Whodundid.core.windowLibrary.windowTypes.OverlayWindow;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;

//Author: Hunter Bragg

public class ImportantWindow extends OverlayWindow {
	
	int importantColor = 0xff000000;
	
	@Override
	public void initObjects() {
		ScaledResolution res = new ScaledResolution(mc);
		centerObjectWithSize(res.getScaledWidth(), res.getScaledHeight());
	}
	
	@Override
	public void drawObject(int mX, int mY) {
		drawRect(0, 0, res.getScaledWidth(), res.getScaledHeight(), importantColor);
		GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
		
		int smaller = Math.min(width, height);
		
		GLObject.drawTexture(midX - smaller / 2, midY - smaller / 2, smaller, smaller, EMCResources.important);
		
		importantColor = Color.HSBtoRGB(System.currentTimeMillis() % 10000L / 5000.0f, 0.8f, 1f);
		GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
		GlStateManager.scale(2.0f, 2.0f, 2.0f);
		drawStringCS("W H Y  H E L L O  T H E R E,  F R I E N D !", midX / 2, midY / 2 + 50, -importantColor);
		GlStateManager.scale(1.0f, 1.0f, 1.0f);
	}
	
	@Override public boolean showInLists() { return false; }
	
}
