package com.Whodundid.core.util.storageUtil;

import java.awt.image.BufferedImage;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.BufferUtils;

//Author: Hunter Bragg

public class DynamicTextureHandler {

	private final TextureManager textureManager;
	private DynamicTexture texture;
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
		final boolean alpha = newImage.getAlphaRaster() != null;
		
		int width = newImage.getWidth();
		int height = newImage.getHeight();
		
		int[] pixels = new int[width * height];
		newImage.getRGB(0, 0, width, height, pixels, 0, width);
		
		ByteBuffer buffer = BufferUtils.createByteBuffer(width * height * 4);
		
		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				int pixel = pixels[y * width + x];
				
				buffer.put((byte) ((pixel >> 16) & 0xff));
	            buffer.put((byte) ((pixel >> 8) & 0xff));
	            buffer.put((byte) (pixel & 0xff));
	            buffer.put((byte) ((pixel >> 24) & 0xff));
			}
		}
		buffer.flip();
		
		IntBuffer intBuff = buffer.asIntBuffer();
		
		int[] newImgData = new int[intBuff.limit()];
		intBuff.get(newImgData);
		
		if (textureData.length == newImgData.length) {
			for (int i = 0; i < newImgData.length; i++) {
				textureData[i] = newImgData[i];
			}
		}
		texture.updateDynamicTexture();
	}

	public ResourceLocation getTextureLocation() { return location; }
	public DynamicTexture getDynamicTexture() { return texture; }
	public BufferedImage GBI() { return image; } //get buffered image
	public int getTextureHeight() {	return image.getHeight(); }
	public int getTextureWidth() { return image.getWidth(); }
	public void destroy() { texture.deleteGlTexture(); }
	
}
