package com.Whodundid.core.util.miscUtil;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import org.lwjgl.input.Mouse;

//Author: Hunter Bragg

public class EMouseHelper {
	
	public static int mX = -1, mY = -1;
	public static int lastButton = -1;
	
	public static void updateMousePos() {
		ScaledResolution res = new ScaledResolution(Minecraft.getMinecraft());
		mX = Mouse.getX() * res.getScaledWidth() / Minecraft.getMinecraft().displayWidth;
        mY = res.getScaledHeight() - Mouse.getY() * res.getScaledHeight() / Minecraft.getMinecraft().displayHeight - 1;
	}
	
	public static void mouseClicked(int buttonIn) {
		lastButton = buttonIn;
	}
	
	public static int getMx() { return mX; }
	public static int getMy() { return mY; }
	public static int getLastButton() { return lastButton; }
}
