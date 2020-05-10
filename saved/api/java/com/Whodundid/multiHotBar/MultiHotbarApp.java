package com.Whodundid.multiHotBar;

import com.Whodundid.core.app.AppType;
import com.Whodundid.core.app.EMCApp;
import java.util.ArrayList;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.event.MouseEvent;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import org.lwjgl.input.Keyboard;
import net.minecraftforge.fml.common.gameevent.InputEvent.KeyInputEvent;

//Last edited: Nov, 2 2018
//First Added: Oct 23, 2018
//Author: Hunter Bragg

//removing for time being

//@Mod(modid = MultiHotbarMod.MODID, version = MultiHotbarMod.VERSION, name = MultiHotbarMod.NAME, dependencies = "required-after:enhancedmc")
public class MultiHotbarApp extends EMCApp {

	public static final String MODID = "emcmultihotbar";
	public static final String VERSION = "0.1";
	public static final String NAME = "EMC MultiHotbar";
	
	public int mcCurrentSlotNum = 0;
	public int currentSlotNum = 0;
	public int numberOfLayers = 2;
	boolean layered = true;
	public boolean slotSet = false;
	public boolean debugMode = false;
	private int lastKeyPressed = -1;
	private int lastLayer = 0;
	ItemStack[] inventoryRender = new ItemStack[40];
	ArrayList<ItemStack> visualInventory = new ArrayList();
	private static MultiHotbarApp instance;
	
	public MultiHotbarApp() {
		super(AppType.MULTIHOTBAR);
		instance = this;
		shouldLoad = false;
		addDependency(AppType.CORE, "1.0");
		setMainGui(new HotBarGui());
		setAliases("multihotbar", "hotbar");
	}
	
	public static MultiHotbarApp instance() { return instance; }
	
	@Override
	public EMCApp setEnabled(boolean valueIn) {
		super.setEnabled(valueIn);
		return this;
	}
	
	@Override
	public void clientTickEvent(TickEvent.ClientTickEvent e) {
		if (isEnabled()) {
			if (mc.thePlayer != null && mc.thePlayer.inventory != null) {
				mcCurrentSlotNum = mc.thePlayer.inventory.currentItem;
				if (!slotSet) {
					currentSlotNum = mcCurrentSlotNum;
					slotSet = true;
				}
				//compareLists();
			}
		}
	}
	
	@Override
	public void serverJoinEvent(EntityJoinWorldEvent e) {
		if (e.entity.equals(mc.thePlayer)) {
			if (isEnabled()) {
				mcCurrentSlotNum = mc.thePlayer.inventory.currentItem;
				currentSlotNum = currentSlotNum % 9;
			}
		}
	}
	
	@Override
	public void mouseEvent(MouseEvent e) {
		try {
			if (isEnabled()) {
				int change = Integer.signum(e.dwheel);
				if (mc.thePlayer != null) {
					mcCurrentSlotNum = mc.thePlayer.inventory.currentItem;
					int oldSlotNum = currentSlotNum;
					currentSlotNum -= change;
					if (currentSlotNum > (numberOfLayers * 9) - 1) { currentSlotNum = 0; }
					if (currentSlotNum < 0) { currentSlotNum = (numberOfLayers * 9) - 1; }
					if (change != 0) { lastKeyPressed = -1; updateSlots(currentSlotNum, oldSlotNum); }
				}
				if (e.buttonstate && e.button == 2) { slotSet = false; }
			}
		} catch (Exception q) { q.printStackTrace(); }
	}
	
	@Override
	public void keyEvent(KeyInputEvent e) {
		if (isEnabled()) {
			if (Keyboard.getEventKeyState()) {
				int key = Keyboard.getEventKey();
				if (key >= 2 && key <= 10) {
					if (layered && key == lastKeyPressed) {
						int oldSlotNum = currentSlotNum;
						if (currentSlotNum <= 8) { currentSlotNum += 9; }
						else { currentSlotNum = key - 2; }
						updateSlots(currentSlotNum, oldSlotNum);
					} else { currentSlotNum = key - 2; }
					lastKeyPressed = key;
				}
				if (key == Keyboard.KEY_GRAVE) {
					currentSlotNum = 0;
					mc.thePlayer.inventory.currentItem = 0;
					compareLists();
				}
			}
		}
	}
	
	public void renderHotbar() {
		/*
		try {
			if (layered) {
				ScaledResolution res = new ScaledResolution(mc);
				mc.renderEngine.bindTexture(EMCResources.hotbar);
				for (int q = 0; q < numberOfLayers; q++) { Gui.drawModalRectWithCustomSizedTexture((res.getScaledWidth() / 2) - 91, res.getScaledHeight() - 22 - (21 * q), 0, 0, 182, 22, 182, 22); }
				
				if (debugMode) {
					int offset = 120;
					for (int q = 0; q < numberOfLayers; q++) { Gui.drawModalRectWithCustomSizedTexture((res.getScaledWidth() / 2) - 91, res.getScaledHeight() - 22 - offset - (21 * q), 0, 0, 182, 22, 182, 22); }
				}
				
				mc.renderEngine.bindTexture(EMCResources.hotbarSelection);
				int xPos = (res.getScaledWidth() / 2) + ((currentSlotNum % 9) * 20) - 92;
				int yPos;
				if (currentSlotNum > 8) { yPos = res.getScaledHeight() - 23 - (21 * (int) Math.ceil(currentSlotNum / 9)); }
				else { yPos = res.getScaledHeight() - 23 - (21 * (int) Math.ceil(currentSlotNum / 9)); }
				Gui.drawModalRectWithCustomSizedTexture(xPos, yPos, 0, 0, 24, 24, 24, 24);
			} else {
				ScaledResolution res = new ScaledResolution(mc);
				mc.renderEngine.bindTexture(EMCResources.hotbar);
				int midX = (numberOfLayers * 182) / numberOfLayers;
								
				mc.renderEngine.bindTexture(EMCResources.hotbarSelection);
				int xPos = (res.getScaledWidth() / 2 - (numberOfLayers / 2) * 182 - 4 + (20 * currentSlotNum));
				int yPos = res.getScaledHeight() - 23;
				Gui.drawModalRectWithCustomSizedTexture(xPos, yPos, 0, 0, 24, 24, 24, 24);
			}
		} catch (Exception e) { e.printStackTrace(); }
		*/
	}
	
	public void renderHotbarItems(float ticks) {
		try {
			if (layered && visualInventory.size() > 0 && numberOfLayers > 0) {
				ScaledResolution res = new ScaledResolution(mc);
				for (int i = 0; i < (9 * numberOfLayers); i++) {
					int xPos, yPos;
					xPos = (res.getScaledWidth() / 2) - 88 + (20 * (i % 9));
					//if (i > 8) { yPos = res.getScaledHeight() - (19 + 21 * numberOfLayers) + (21 * (int) Math.ceil(i / 9)); }
					yPos = res.getScaledHeight() - 19 - (21 * (int) Math.ceil(i / 9));
					
					ItemStack stack = visualInventory.get(i);
					
					if (stack != null) {
						float f = stack.animationsToGo - ticks;
						
						if (f > 0.0F) {
			                GlStateManager.pushMatrix();
			                float f1 = 1.0F + f / 5.0F;
			                GlStateManager.translate(xPos + 8, yPos + 12, 0.0F);
			                GlStateManager.scale(1.0F / f1, (f1 + 1.0F) / 2.0F, 1.0F);
			                GlStateManager.translate((-(xPos + 8)), (-(yPos + 12)), 0.0F);
			            }
						
			            mc.getRenderItem().renderItemAndEffectIntoGUI(stack, xPos, yPos);
			            if (f > 0.0F) { GlStateManager.popMatrix(); }
			            mc.getRenderItem().renderItemOverlays(mc.fontRendererObj, stack, xPos, yPos);
					}
				}
				if (debugMode) {
					for (int i = 0; i < (9 * numberOfLayers); i++) {
						int xPos, yPos, offset = 120;
						xPos = (res.getScaledWidth() / 2) - 88 + (20 * (i % 9));
						//if (i > 8) { yPos = res.getScaledHeight() - (19 + 21 * numberOfLayers) + (21 * (int) Math.ceil(i / 9)); }
						yPos = res.getScaledHeight() - 19 - (21 * (int) Math.ceil(i / 9));
						yPos -= offset;
						
						ItemStack stack = mc.thePlayer.inventory.mainInventory[i];
						
						if (stack != null) {
							float f = stack.animationsToGo - ticks;
							
							if (f > 0.0F) {
				                GlStateManager.pushMatrix();
				                float f1 = 1.0F + f / 5.0F;
				                GlStateManager.translate(xPos + 8, yPos + 12, 0.0F);
				                GlStateManager.scale(1.0F / f1, (f1 + 1.0F) / 2.0F, 1.0F);
				                GlStateManager.translate((-(xPos + 8)), (-(yPos + 12)), 0.0F);
				            }

				            mc.getRenderItem().renderItemAndEffectIntoGUI(stack, xPos, yPos);

				            if (f > 0.0F) { GlStateManager.popMatrix(); }

				            mc.getRenderItem().renderItemOverlays(mc.fontRendererObj, stack, xPos, yPos);
						}
					}
				}
			} else {
				if (visualInventory.size() > 0) {
					ScaledResolution res = new ScaledResolution(mc);
					for (int i = 0; i < (9 * numberOfLayers); i++) {
						int xPos, yPos;
						xPos = (res.getScaledWidth() / 2 - (numberOfLayers / 2) * 182 + (i * 20));
						//if (i > 8) { yPos = res.getScaledHeight() - (19 + 21 * numberOfLayers) + (21 * (int) Math.ceil(i / 9)); }
						yPos = res.getScaledHeight() - 19;
						
						ItemStack stack = visualInventory.get(i);
						
						if (stack != null) {
							float f = stack.animationsToGo - ticks;
							
							if (f > 0.0F) {
				                GlStateManager.pushMatrix();
				                float f1 = 1.0F + f / 5.0F;
				                GlStateManager.translate(xPos + 8, yPos + 12, 0.0F);
				                GlStateManager.scale(1.0F / f1, (f1 + 1.0F) / 2.0F, 1.0F);
				                GlStateManager.translate((-(xPos + 8)), (-(yPos + 12)), 0.0F);
				            }
							
				            mc.getRenderItem().renderItemAndEffectIntoGUI(stack, xPos, yPos);
				            if (f > 0.0F) { GlStateManager.popMatrix(); }
				            mc.getRenderItem().renderItemOverlays(mc.fontRendererObj, stack, xPos, yPos);
						}
					}
				}
			}
		} catch (Exception e) { e.printStackTrace(); }
	}
	
	private void updateSlots(int slot, int fromSlot) {
		try {
			//if (layered) {
				int toLayer, fromLayer;
				toLayer = (int) Math.ceil(slot / 9);
				fromLayer = (int) Math.ceil(fromSlot / 9);
				boolean layerChange = toLayer != fromLayer;
				if (layerChange) {
					int layer;
					if (toLayer > fromLayer) {
						layer = toLayer;
					} else {
						layer = fromLayer;
					}
					for (int i = layer * 9; i < layer * 9 + 9; i++) {
						mc.playerController.windowClick(mc.thePlayer.inventoryContainer.windowId, i, i % 9, 2, mc.thePlayer);
					}
				}
			//}
		} catch (Exception e) { e.printStackTrace(); }
	}
	
	private void compareLists() {
		visualInventory.clear();
		for (ItemStack s : mc.thePlayer.inventory.mainInventory) { visualInventory.add(s); }
	}
	
	public int getCurrentSlotNum() { return currentSlotNum; }
	public MultiHotbarApp setCurrentSlotNum(int numIn) { currentSlotNum = numIn; return this; }
	public MultiHotbarApp setLayered(boolean val) { layered = val; return this; }
	public boolean isLayered() { return layered; }
}
