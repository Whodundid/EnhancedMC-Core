package com.Whodundid.core.util.playerUtil;

import com.Whodundid.core.util.storageUtil.Vector3D;
import net.minecraft.client.Minecraft;
import net.minecraft.item.Item;

//Author: Hunter Bragg

public class PlayerTraits {
	
	public static Minecraft mc = Minecraft.getMinecraft();
	
	public static int getHeldItemId() { return mc.thePlayer != null && mc.thePlayer.getHeldItem() != null ? Item.getIdFromItem(mc.thePlayer.getHeldItem().getItem()) : -1; }
	public static Vector3D getPlayerLocation() { return mc.thePlayer != null ? new Vector3D(mc.thePlayer.posX, mc.thePlayer.posY, mc.thePlayer.posZ) : null; }
	
	public static boolean isCreative() { return mc.thePlayer != null ? mc.thePlayer.capabilities.isCreativeMode : false; }
	public static boolean isSpectator() { return mc.playerController != null ? mc.playerController.isSpectator() : false; }
	public static boolean isHoldingItem() { return mc.thePlayer != null ? mc.thePlayer.getHeldItem() != null : false; }
	public static boolean isInBlock() { return mc.thePlayer != null ? mc.thePlayer.isEntityInsideOpaqueBlock() : false; }
	public static boolean isFlying() { return mc.thePlayer != null ? mc.thePlayer.capabilities.isFlying : false; }
	public static boolean isJumping() { return mc.thePlayer != null && !isCreative() && mc.thePlayer.isAirBorne; }
	
}
