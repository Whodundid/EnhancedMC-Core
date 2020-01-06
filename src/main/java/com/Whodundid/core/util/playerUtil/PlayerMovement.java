package com.Whodundid.core.util.playerUtil;

import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;

//Author: Hunter Bragg

public class PlayerMovement {
	
	private static boolean isSprinting = false;
	
	static Minecraft mc = Minecraft.getMinecraft();
	
	public static void pressMovementKey(Direction dir) {
		switch (dir) {
		case N: KeyBinding.setKeyBindState(mc.gameSettings.keyBindForward.getKeyCode(), true); break;
		case E:  KeyBinding.setKeyBindState(mc.gameSettings.keyBindRight.getKeyCode(), true); break;
		case S: KeyBinding.setKeyBindState(mc.gameSettings.keyBindBack.getKeyCode(), true); break;
		case W: KeyBinding.setKeyBindState(mc.gameSettings.keyBindLeft.getKeyCode(), true); break;
		default: break;		
		}
	}
	
	public static void unpressMovementKey(Direction dir) {
		switch (dir) {
		case N: KeyBinding.setKeyBindState(mc.gameSettings.keyBindForward.getKeyCode(), false); break;
		case E: KeyBinding.setKeyBindState(mc.gameSettings.keyBindRight.getKeyCode(), false); break;
		case S: KeyBinding.setKeyBindState(mc.gameSettings.keyBindBack.getKeyCode(), false); break;
		case W: KeyBinding.setKeyBindState(mc.gameSettings.keyBindLeft.getKeyCode(), false); break;
		default: break;		
		}
	}
	
	public static void setSneaking() { setSneaking(true); }	
	public static void setSneaking(boolean state) { KeyBinding.setKeyBindState(mc.gameSettings.keyBindSneak.getKeyCode(), state); }
	
	public static void setJumping() { setJumping(true); }	
	public static void setJumping(boolean state) { KeyBinding.setKeyBindState(mc.gameSettings.keyBindJump.getKeyCode(), state); }
	
	public static void setSprinting() { setSprinting(true); }	
	public static void setSprinting(boolean state) {
		isSprinting = state;
		KeyBinding.setKeyBindState(mc.gameSettings.keyBindSprint.getKeyCode(), state);
	}
	
	public static void stopMovement() {
		KeyBinding.setKeyBindState(mc.gameSettings.keyBindForward.getKeyCode(), false);
		KeyBinding.setKeyBindState(mc.gameSettings.keyBindRight.getKeyCode(), false);
		KeyBinding.setKeyBindState(mc.gameSettings.keyBindBack.getKeyCode(), false);
		KeyBinding.setKeyBindState(mc.gameSettings.keyBindLeft.getKeyCode(), false);
	}
	
	public static void instantlyStopAllMovement() {
		stopMovement();
		mc.thePlayer.motionX = 0;
		mc.thePlayer.motionY = 0;
		mc.thePlayer.motionZ = 0;
	}
}
