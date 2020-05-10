package com.Whodundid.worldEditor.EditorGuiObjects;

import com.Whodundid.core.enhancedGui.guiObjects.advancedObjects.scrollList.EGuiScrollList;
import com.Whodundid.worldEditor.EditorUtil.EditorGuiBase;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.item.ItemStack;
import org.lwjgl.opengl.GL11;

//Last edited: Nov 25, 2018
//First Added: unknown
//Author: Hunter Bragg

public class EditorBlockPalette extends EditorGuiObject {
	
	protected EGuiScrollList blockList;
	protected ItemStack current = null;
	public Block primary, secondary;
	
	public EditorBlockPalette(EditorGuiBase guiIn) {
		super(guiIn);
		init(guiIn, guiIn.midX + 225, guiIn.midY - 251, 150, 300);
	}
	
	@Override
	public void initGui() {
		super.initGui();
		current = new ItemStack(Block.getBlockById(1));
	}

	@Override
	public void drawObject(int mX, int mY) {
		super.drawObject(mX, mY);
		//mc.renderEngine.bindTexture(EMCResources.guiBase);
		//guiInstance.drawModalRectWithCustomSizedTexture(startX, startY, 0, 0, width, height, width, height);
		//guiInstance.drawRect(startX + 4, startY + 4, endX - 4, startY + 141, -6250336);
		//guiInstance.drawRect(startX + 5, startY + 5, endX - 5, startY + 140, -16777216);
		renderBlockImages();
	}
	
	@Override
	public void mousePressed(int mX, int mY, int button) {
		try {
			Block b = Block.getBlockById(Integer.parseInt(guiInstance.editorTextInput.idInput.getText()));
			ItemStack testStack = new ItemStack(b);
			//guiInstance.itemRenderer.renderItemIntoGUI(testStack, startX / 6 + 5, startY / 6 + 4);
			current = testStack;
		} catch (Exception e) { current = new ItemStack(Block.getBlockById(1)); }
		
		super.mousePressed(mX, mY, button);
	}
	
	@Override
	public void mouseScrolled(int change) {
		super.mouseScrolled(change);
	}
	
	private void buildBlockList() {
		
	}
	
	private void renderBlockImages() {
		if (current != null) {
			GL11.glPushMatrix();
			GlStateManager.scale(6, 6, 6);
			RenderHelper.enableGUIStandardItemLighting();
			//guiInstance.itemRenderer.renderItemIntoGUI(current, startX / 6 + 5, startY / 6 + 4);
			RenderHelper.disableStandardItemLighting();
			GL11.glPopMatrix();
		}
	}
}
