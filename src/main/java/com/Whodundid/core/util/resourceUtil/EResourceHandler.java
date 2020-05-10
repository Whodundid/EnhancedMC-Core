package com.Whodundid.core.util.resourceUtil;

import com.Whodundid.core.util.storageUtil.DynamicTextureHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.client.renderer.texture.TextureUtil;
import net.minecraft.util.ResourceLocation;

public class EResourceHandler {

	public static void bindTexture(ResourceLocation textureIn) {
		if (textureIn != null && isInit()) {
			Minecraft.getMinecraft().renderEngine.bindTexture(textureIn);
		}
	}
	
	public static void bindTexture(DynamicTexture textureIn) {
		if (isInit()) {
			ResourceLocation loc = Minecraft.getMinecraft().renderEngine.getDynamicTextureLocation("texture/", textureIn);
			if (loc != null) {
				bindTexture(loc);
			}
			else {
				System.err.println("EResourceHandler Error: DynamicTexture is null!");
			}
		}
	}
	
	public static void bindTexture(DynamicTextureHandler textureIn) {
		if (isInit()) {
			if (textureIn != null) {
				bindTexture(textureIn.getTextureLocation());
			}
		}
	}
	
	public static void bindTexture(EResource textureIn) {
		if (isInit()) {
			if (textureIn != null) {
				Minecraft.getMinecraft().renderEngine.bindTexture(textureIn.getResource());
			}
			else {
				bindTexture(TextureUtil.missingTexture);
			}
		}
	}
	
	//check that mc's renderer is initialized
	private static boolean isInit() {
		return Minecraft.getMinecraft() != null && Minecraft.getMinecraft().renderEngine != null;
	}
	
}
