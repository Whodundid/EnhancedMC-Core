package com.Whodundid.core.util.storageUtil;

import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.util.ResourceLocation;

public class DynamicTextureHandler {

	private final TextureManager textureManager;
	private final DynamicTexture texture;
	private BufferedImage image;
	private ResourceLocation location;
	private int[] textureData;

	public DynamicTextureHandler(TextureManager manager, BufferedImage imageIn) {
		textureManager = manager;
		texture = new DynamicTexture(imageIn);
		textureData = texture.getTextureData();
		image = imageIn;
		location = textureManager.getDynamicTextureLocation("texture/", texture);
	}

	public void updateTextureData(BufferedImage newImage) {
		int[] newImgData = ((DataBufferInt)newImage.getRaster().getDataBuffer()).getData();
		if (this.textureData.length == newImgData.length) {
			for (int i = 0; i < newImgData.length; i++) {
				this.textureData[i] = newImgData[i];
			}
		}
		this.texture.updateDynamicTexture();
	}

	public ResourceLocation getTextureLocation() { return location; }
	public DynamicTexture getDynamicTexture() { return texture; }
	public BufferedImage GBI() { return image; } //get buffered image
	public int getTextureHeight() {	return image.getHeight(); }
	public int getTextureWidth() { return image.getWidth(); }
	public void kill() { textureManager.deleteTexture(location); }
}