package com.Whodundid.core.util.renderUtil;

import net.minecraft.client.renderer.GlStateManager;

//Author: Hunter Bragg

public class EGLHelper {
	
	public static void setColor(int colorIn) {
		float f3 = (colorIn >> 24 & 255) / 255.0F;
        float f = (colorIn >> 16 & 255) / 255.0F;
        float f1 = (colorIn >> 8 & 255) / 255.0F;
        float f2 = (colorIn & 255) / 255.0F;
        GlStateManager.color(f, f1, f2, f3);
	}

}
