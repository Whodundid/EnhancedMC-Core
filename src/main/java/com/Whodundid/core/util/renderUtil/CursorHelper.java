package com.Whodundid.core.util.renderUtil;

import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.nio.ByteBuffer;
import javax.imageio.ImageIO;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.IResource;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.BufferUtils;
import org.lwjgl.LWJGLException;
import org.lwjgl.input.Cursor;
import org.lwjgl.input.Mouse;

//Last edited: Dec 23, 2018
//First Added: Sep 14, 2018
//Author: Hunter Bragg

public class CursorHelper {
	
	private static Cursor cursor;
	public static Cursor iBeamCursor, invisibleCursor;
	public static boolean isVisible = true;
	
	public static void init() {
		cursor = Mouse.getNativeCursor();
		iBeamCursor = createCursorFromResourceLocation(Resources.mouseIBeam);
		invisibleCursor = createCursorFromResourceLocation(Resources.emptyPixel);
	}
	
	public static void setCursorVisibility(boolean visible) {
		if (isVisible != visible) {
			try {
				Mouse.setNativeCursor(visible ? cursor : invisibleCursor);
			} catch (LWJGLException e) { e.printStackTrace(); }
			isVisible = visible;
		}
	}
	
	public static void setCursorInvisible() {
		if (isVisible) {
			try {
				Mouse.setNativeCursor(invisibleCursor);
			} catch (LWJGLException e) { e.printStackTrace(); }
			isVisible = false;
		}
	}
	
	public static void setCursorVisible() {
		if (!isVisible) {
			try {
				Mouse.setNativeCursor(cursor);
			} catch (LWJGLException e) { e.printStackTrace(); }
			isVisible = true;
		}
	}
	
	public static Point getExactMouseScreenLocation() {
		if (MouseInfo.getPointerInfo() != null) {
			return MouseInfo.getPointerInfo().getLocation();
		}
		return new Point(0, 0);
	}
	
	public static Point getExactMouseLocationMC() {
		return Mouse.isCreated() ? new Point(Mouse.getX(), Mouse.getY()) : new Point(0, 0);
	}
	
	public static void setCursor(Cursor cursorIn) {
		try {
			Mouse.setNativeCursor(cursorIn);
		} catch (LWJGLException e) { e.printStackTrace(); }
	}
	
	public static Cursor createCursorFromResourceLocation(ResourceLocation locIn) {
		try {
			IResource resource = Minecraft.getMinecraft().getResourceManager().getResource(locIn);
			InputStream stream = resource.getInputStream();
			BufferedImage image = ImageIO.read(stream);
			return createCursor(image);		
		} catch (Exception e) { e.printStackTrace(); }
		return null;
	}
	
	public static Cursor createCursorFromFile(File fileIn) {
		try {
			InputStream stream = new FileInputStream(fileIn);
			BufferedImage image = ImageIO.read(stream);
			return createCursor(image);
		} catch (Exception e) { e.printStackTrace(); }
		return null;
	}
	
	private static Cursor createCursor(BufferedImage imageIn) {
		try {
			int width = imageIn.getWidth();
			int height = imageIn.getHeight();
			
			int[] pixels = new int[width * height];
			imageIn.getRGB(0, 0, width, height, pixels, 0, width);
			
			ByteBuffer buffer = BufferUtils.createByteBuffer(width * height * 4);
			
			for (int y = 0; y < height; y++) {
				for (int x = 0; x < width; x++) {
					int pixel = pixels[y * width + x];
					
					buffer.put((byte) ((pixel >> 16) & 0xFF));
		            buffer.put((byte) ((pixel >> 8) & 0xFF));
		            buffer.put((byte) (pixel & 0xFF));
		            buffer.put((byte) ((pixel >> 24) & 0xFF));
				}
			}
			buffer.flip();
			
			Cursor createdCursor = new Cursor(width, height, width / 2, height / 2, 1, buffer.asIntBuffer(), null);
			return createdCursor;			
		} catch (Exception e) { e.printStackTrace(); }
		return null;
	}
	
	public static void reset() { setCursor(cursor); }
	public static boolean isNormalCursor() {
		if (Mouse.isCreated() && Mouse.getNativeCursor() != null) {
			return Mouse.getNativeCursor().equals(cursor);
		}
		return false;
	}
}
