package com.Whodundid.core.util.renderUtil;

import com.Whodundid.core.util.resourceUtil.EResource;
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

//Author: Hunter Bragg

public class CursorHelper {
	
	private static Cursor cursor;
	public static Cursor invisibleCursor;
	public static boolean isVisible = true;
	
	public static void init() {
		cursor = Mouse.getNativeCursor();
		invisibleCursor = createCursor(new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB));
	}
	
	/** Will only change the cursor if the current one is different than the one to be set. */
	public static void updateCursor(Cursor cursorIn) {
		if (getCursor() == null) {
			if (cursorIn != null) { setCursor(cursorIn); }
		}
		else if (!(getCursor() == cursorIn)) {
			setCursor(cursorIn);
		}
	}
	
	/** Attempts to set the cursor to the one specified. */
	public static void setCursor(Cursor cursorIn) {
		try {
			Mouse.setNativeCursor(cursorIn);
		}
		catch (LWJGLException e) { e.printStackTrace(); }
	}
	
	/** Returns a new Cursor Object created from an EMC EResource. */
	public static Cursor createCursorFromEResource(EResource resIn) {
		try {
			if (resIn != null && resIn.getHandler() != null) {
				return createCursor(resIn.getHandler().GBI());
			}
		}
		catch (Exception e) { e.printStackTrace(); }
		return null;
	}
	
	/** Returns a new Cursor Object created from a Minecraft ResourceLocation. */
	public static Cursor createCursorFromResourceLocation(ResourceLocation locIn) {
		try {
			IResource resource = Minecraft.getMinecraft().getResourceManager().getResource(locIn);
			InputStream stream = resource.getInputStream();
			BufferedImage image = ImageIO.read(stream);
			return createCursor(image);		
		}
		catch (Exception e) { e.printStackTrace(); }
		return null;
	}
	
	/** Returns a new Cursor Object created from an image file. */
	public static Cursor createCursorFromFile(File fileIn) {
		try {
			InputStream stream = new FileInputStream(fileIn);
			BufferedImage image = ImageIO.read(stream);
			return createCursor(image);
		}
		catch (Exception e) { e.printStackTrace(); }
		return null;
	}
	
	/** Internal function used to map stitched image resources together to create a cursor of the same image. */
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
					
					buffer.put((byte) ((pixel >> 16) & 0xff));
		            buffer.put((byte) ((pixel >> 8) & 0xff));
		            buffer.put((byte) (pixel & 0xff));
		            buffer.put((byte) ((pixel >> 24) & 0xff));
				}
			}
			buffer.flip();
			
			Cursor createdCursor = new Cursor(width, height, width / 2, height / 2, 1, buffer.asIntBuffer(), null);
			return createdCursor;			
		}
		catch (Exception e) { e.printStackTrace(); }
		return null;
	}
	
	
	/** Sets the cursor to be invisible. */
	public static void setInvisible() { setCursorVisibility(false); }
	/** Sets the cursor to be visible. */
	public static void setVisible() { setCursorVisibility(true); }
	/** Sets the cursor to be either visible or invisible. */
	public static void setCursorVisibility(boolean visible) {
		if (isVisible != visible) {
			setCursor(visible ? cursor : invisibleCursor);
			isVisible = visible;
		}
	}
	
	
	/** Returns the mouse location in terms of OpenGL. */
	public static Point getPosGL() { return MouseInfo.getPointerInfo() != null ? MouseInfo.getPointerInfo().getLocation() : new Point(0, 0); }
	/** Returns the mouse location in terms of Minecraft. */
	public static Point getPosMC() { return Mouse.isCreated() ? new Point(Mouse.getX(), Mouse.getY()) : new Point(0, 0); }
	
	
	/** Resets the cursor image back to default. */
	public static void reset() {
		setCursor(null);
	}
	/** Returns true if the cursor is currently the default cursor. */
	public static boolean isNormalCursor() { return Mouse.isCreated() && Mouse.getNativeCursor() == null; }
	/** Returns the current cursor. */
	public static Cursor getCursor() { return Mouse.getNativeCursor(); }
	/** Returns true if the cursor is visible. */
	public static boolean isCursorVisible() { return isVisible; }
}
