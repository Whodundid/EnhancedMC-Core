package com.Whodundid.core.util.playerUtil;

import net.minecraft.client.Minecraft;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class PlayerInventory {
	
	static final Minecraft mc = Minecraft.getMinecraft();
	
	public static ItemStack[] getPlayerHotbar() {
		ItemStack[] hotbar = new ItemStack[8];
		for (int i = 0; i < 9; i++) {
			if (mc.thePlayer.inventory.getStackInSlot(i) != null) { hotbar[i] = mc.thePlayer.inventory.getStackInSlot(i); }
			else { hotbar[i] = null; }
		}
		return hotbar;
	}
	
	public static boolean doesHotbarContainItem(String name) {
		for (int q = 0; q < 9; q++) {
			if (mc.thePlayer.inventory.getStackInSlot(q) != null) {
				if (mc.thePlayer.inventory.getStackInSlot(q).getDisplayName().equals(name)) { return true; }
			}
		}
		return false;
	}
	
	public static int getSlotNumberForContaingItem(String name) {
		for (int q = 0; q < 9; q++) {
			if (mc.thePlayer.inventory.getStackInSlot(q) != null) {
				if (mc.thePlayer.inventory.getStackInSlot(q).getDisplayName().equals(name)) { return q; }
			}			
		}
		return -1;
	}
	
	public static boolean isPlayerInvSlotEmpty(int x) { return mc.thePlayer.inventory.getStackInSlot(x) == null; }
	public static boolean doesHotbarContainItem(Item i) { return doesHotbarContainItem(i.getUnlocalizedName()); }
	public static int getSlotNumberForContaingItem(Item i) { return getSlotNumberForContaingItem(i.getUnlocalizedName()); };
}
