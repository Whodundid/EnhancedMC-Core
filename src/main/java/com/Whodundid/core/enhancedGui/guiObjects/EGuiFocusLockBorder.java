package com.Whodundid.core.enhancedGui.guiObjects;

import com.Whodundid.core.enhancedGui.types.EnhancedGuiObject;
import com.Whodundid.core.enhancedGui.types.interfaces.IEnhancedGuiObject;
import com.Whodundid.core.util.renderUtil.Resources;
import com.Whodundid.core.util.storageUtil.EDimension;

import net.minecraft.client.audio.PositionedSoundRecord;

//Last edited: Jan 2, 2019
//First Added: Dec 25, 2018
//Author: Hunter Bragg

public class EGuiFocusLockBorder extends EnhancedGuiObject {
	
	protected long startTime = 0l;
	protected boolean first = false;
	protected boolean second = false;
	protected boolean drawingBorder = true;
	protected int borderColor = 0xffff2222;
	
	public EGuiFocusLockBorder(IEnhancedGuiObject parentIn) {
		if (parentIn != null) {
			EDimension dim = parentIn.getDimensions();
			init(parentIn, dim.startX, dim.startY, dim.width, dim.height);
			startTime = System.currentTimeMillis();
			mc.getSoundHandler().playSound(PositionedSoundRecord.create(Resources.buttonSound, 1.0F));
		}
	}
	
	public EGuiFocusLockBorder(IEnhancedGuiObject parentIn, int startXIn, int startYIn, int widthIn, int heightIn) {
		if (parentIn != null) {
			init(parentIn, startXIn, startYIn, widthIn, heightIn);
			startTime = System.currentTimeMillis();
			mc.getSoundHandler().playSound(PositionedSoundRecord.create(Resources.buttonSound, 1.0F));
		}
	}
	
	@Override
	public void drawObject(int mXIn, int mYIn, float ticks) {
		if (System.currentTimeMillis() - startTime >= 200) {
			mc.getSoundHandler().playSound(PositionedSoundRecord.create(Resources.buttonSound, 1.0F));
			if (second) { parent.removeObject(this); drawingBorder = false; }
			if (first) { second = true; drawingBorder = true; }
			if (!first) { first = true; drawingBorder = false; }
			startTime = System.currentTimeMillis();
		}
		if (drawingBorder) { drawBorder(); }
	}
	
	protected void drawBorder() {
		drawRect(startX - 1, startY, startX, endY, borderColor);
		drawRect(startX - 1, startY - 1, endX + 1, startY, borderColor);
		drawRect(endX, startY, endX + 1, endY, borderColor);
		drawRect(startX - 1, endY + 1, endX + 1, endY, borderColor);
	}
}
