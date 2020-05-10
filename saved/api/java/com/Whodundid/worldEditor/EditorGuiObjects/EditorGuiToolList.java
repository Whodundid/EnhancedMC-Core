package com.Whodundid.worldEditor.EditorGuiObjects;

import com.Whodundid.worldEditor.EditorResources;
import com.Whodundid.worldEditor.EditorUtil.EditorGuiBase;
import com.Whodundid.worldEditor.EditorUtil.EditorTools;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.util.ResourceLocation;

public class EditorGuiToolList extends EditorGuiObject {
	
	//drawn values are not based off of gui! FIX!
	
	private boolean inSel, inMoveSelection, inMoveSel, inPan, inPencil, inBucket, inReplace, inDropper;
	
	public EditorGuiToolList(EditorGuiBase guiIn) {
		super(guiIn);
		init(guiIn, guiIn.midX - 272, guiIn.midY - 251, 38, 80);
	}
	
	@Override
	public void drawObject(int mX, int mY) {
		checkMouseEneteredButton(mX, mY);
		drawToolList(mX, mY);
		drawSelectedTool();
		super.drawObject(mX, mY);
	}
	
	private void drawToolList(int mX, int mY) {
		//mc.renderEngine.bindTexture(EMCResources.guiRCMBase);
		//guiInstance.drawModalRectWithCustomSizedTexture(startX, startY, 0, 0, 38, height, 38, height);
		mc.renderEngine.bindTexture(EditorResources.editorToolPics);
		guiInstance.drawTexturedModalRect(startX + 2, startY + 2, 1, 1, 16, 16); //select
		guiInstance.drawTexturedModalRect(startX + 20, startY + 2, 18, 1, 16, 16); //moveSel
		guiInstance.drawTexturedModalRect(startX + 2, startY + 21, 54, 1, 16, 16); //pan
		guiInstance.drawTexturedModalRect(startX + 20, startY + 21, 36, 1, 16, 16); //moveSelect
		guiInstance.drawTexturedModalRect(startX + 2, startY + 40, 70, 1, 16, 16); //pencil
		guiInstance.drawTexturedModalRect(startX + 20, startY + 40, 87, 1, 16, 16); //bucket
		guiInstance.drawTexturedModalRect(startX + 2, startY + 59, 104, 1, 16, 16); //replace
		guiInstance.drawTexturedModalRect(startX + 20, startY + 59, 122, 1, 16, 16); //dropper
	}
	
	private void drawSelectedTool() {
		mc.renderEngine.bindTexture(EditorResources.editorToolPics);
		switch (editor.getSelectedTool()) {
		case SELECT: guiInstance.drawTexturedModalRect(startX + 1, startY + 1, 0, 18, 18, 18); break;
		case MOVE_SELECTION: guiInstance.drawTexturedModalRect(startX + 19, startY + 1, 0, 18, 18, 18); break;
		case PAN: guiInstance.drawTexturedModalRect(startX + 1, startY + 20, 0, 18, 18, 18); break;
		case MOVE_SELECT: guiInstance.drawTexturedModalRect(startX + 19, startY + 20, 0, 18, 18, 18); break;
		case PENCIL: guiInstance.drawTexturedModalRect(startX + 1, startY + 39, 0, 18, 18, 18); break;
		case PAINTBUCKET: guiInstance.drawTexturedModalRect(startX + 19, startY + 39, 0, 18, 18, 18); break;
		case REPLACE: guiInstance.drawTexturedModalRect(startX + 1, startY + 58, 0, 18, 18, 18); break;
		case EYEDROPPER: guiInstance.drawTexturedModalRect(startX + 19, startY + 58, 0, 18, 18, 18); break;
		default: break;
		}
	}
	
	private void checkMouseEneteredButton(int mX, int mY) {
		inSel = false;
		inMoveSelection = false;
		inPan = false;
		inMoveSel = false;
		inPencil = false;
		inBucket = false;
		inReplace = false;
		inDropper = false;
		
		if (!editor.insideEditor) {
			if (mX >= startX && mX <= startX + 18) {
				if (mY >= startY + 1 && mY <= startY + 18) { inSel = true; }
				if (mY >= startY + 19 && mY <= startY + 38) { inPan = true; }
				if (mY >= startY + 39 && mY <= startY + 57) { inPencil = true; }
				if (mY >= startY + 58 && mY <= startY + 75) { inReplace = true; }
			}
			if (mX >= startX + 19 && mX <= startX + 37) {
				if (mY >= startY + 1 && mY <= startY + 18) { inMoveSelection = true; }
				if (mY >= startY + 19 && mY <= startY + 38) { inMoveSel = true; }
				if (mY >= startY + 39 && mY <= startY + 57) { inBucket = true; }
				if (mY >= startY + 58 && mY <= startY + 75) { inDropper = true; }
			}
		}
	}
	
	@Override
	public void keyPressed(char typedChar, int keyCode) {
		switch (keyCode) {
		//case 44: Editor.setCurrentTool(EditorTools.SELECT); break;
		//case 45: Editor.setCurrentTool(EditorTools.MOVE_SELECTION); break;
		//case 46: Editor.setCurrentTool(EditorTools.PAN); break;
		//case 47: Editor.setCurrentTool(EditorTools.MOVE_SELECT); break;
		}
	}
	
	@Override
	public void mousePressed(int mX, int mY, int button) {
		super.mousePressed(mX, mY, button);
		if (inSel) { editor.setSelectedTool(EditorTools.SELECT); mc.getSoundHandler().playSound(PositionedSoundRecord.create(new ResourceLocation("gui.button.press"), 1.0F)); }
		if (inMoveSelection) { editor.setSelectedTool(EditorTools.MOVE_SELECTION); mc.getSoundHandler().playSound(PositionedSoundRecord.create(new ResourceLocation("gui.button.press"), 1.0F)); }
		if (inPan) { editor.setSelectedTool(EditorTools.PAN); mc.getSoundHandler().playSound(PositionedSoundRecord.create(new ResourceLocation("gui.button.press"), 1.0F)); }
		if (inMoveSel) { editor.setSelectedTool(EditorTools.MOVE_SELECT); mc.getSoundHandler().playSound(PositionedSoundRecord.create(new ResourceLocation("gui.button.press"), 1.0F)); }
		if (inPencil) { editor.setSelectedTool(EditorTools.PENCIL); mc.getSoundHandler().playSound(PositionedSoundRecord.create(new ResourceLocation("gui.button.press"), 1.0F)); }
		if (inBucket) { editor.setSelectedTool(EditorTools.PAINTBUCKET); mc.getSoundHandler().playSound(PositionedSoundRecord.create(new ResourceLocation("gui.button.press"), 1.0F)); }
		if (inReplace) { editor.setSelectedTool(EditorTools.REPLACE); mc.getSoundHandler().playSound(PositionedSoundRecord.create(new ResourceLocation("gui.button.press"), 1.0F)); }
		if (inDropper) { editor.setSelectedTool(EditorTools.EYEDROPPER); mc.getSoundHandler().playSound(PositionedSoundRecord.create(new ResourceLocation("gui.button.press"), 1.0F)); }
	}
}
