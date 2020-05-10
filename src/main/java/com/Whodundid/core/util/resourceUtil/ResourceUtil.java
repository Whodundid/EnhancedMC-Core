package com.Whodundid.core.util.resourceUtil;

import com.Whodundid.core.util.storageUtil.DynamicTextureHandler;
import java.awt.image.BufferedImage;
import java.io.InputStream;
import javax.imageio.ImageIO;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.IResource;
import net.minecraft.util.ResourceLocation;

public class ResourceUtil {

	/** Returns the actual width in pixels for the given RescoureLocation. */
	public static int getImageWidth(ResourceLocation locIn) {
		try {
			IResource resource = Minecraft.getMinecraft().getResourceManager().getResource(locIn);
			InputStream stream = resource.getInputStream();
			BufferedImage image = ImageIO.read(stream);
			return image.getWidth();
		} catch (Exception e) { e.printStackTrace(); }
		return -1;
	}
	
	/** Returns the actual height in pixels for the given RescoureLocation. */
	public static int getImageHeight(ResourceLocation locIn) {
		try {
			IResource resource = Minecraft.getMinecraft().getResourceManager().getResource(locIn);
			InputStream stream = resource.getInputStream();
			BufferedImage image = ImageIO.read(stream);
			return image.getHeight();
		} catch (Exception e) { e.printStackTrace(); }
		return -1;
	}
	
	/** Returns the actual width in pixels for the given RescoureLocation. */
	public static int getImageWidth(DynamicTextureHandler locIn) {
		try {
			return locIn.getTextureWidth();
		} catch (Exception e) { e.printStackTrace(); }
		return -1;
	}
	
	/** Returns the actual height in pixels for the given RescoureLocation. */
	public static int getImageHeight(DynamicTextureHandler locIn) {
		try {
			return locIn.getTextureHeight();
		} catch (Exception e) { e.printStackTrace(); }
		return -1;
	}
	
	/** Returns the actual width in pixels for the given RescoureLocation. */
	public static int getImageWidth(EResource locIn) {
		try {
			return locIn.getHandler().getTextureWidth();
		} catch (Exception e) { e.printStackTrace(); }
		return -1;
	}
	
	/** Returns the actual height in pixels for the given RescoureLocation. */
	public static int getImageHeight(EResource locIn) {
		try {
			return locIn.getHandler().getTextureHeight();
		} catch (Exception e) { e.printStackTrace(); }
		return -1;
	}
	
}
