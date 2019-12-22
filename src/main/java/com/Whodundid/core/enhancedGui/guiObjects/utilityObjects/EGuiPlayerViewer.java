package com.Whodundid.core.enhancedGui.guiObjects.utilityObjects;

import com.Whodundid.core.enhancedGui.guiObjects.basicObjects.EGuiButton;
import com.Whodundid.core.enhancedGui.guiObjects.basicObjects.EGuiSlider;
import com.Whodundid.core.enhancedGui.types.EnhancedGuiObject;
import com.Whodundid.core.enhancedGui.types.interfaces.IEnhancedActionObject;
import com.Whodundid.core.enhancedGui.types.interfaces.IEnhancedGuiObject;
import com.Whodundid.core.util.renderUtil.PlayerDrawer;
import com.Whodundid.core.util.renderUtil.ScreenLocation;
import net.minecraft.client.renderer.GlStateManager;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;

public class EGuiPlayerViewer extends EnhancedGuiObject {
	
	ScreenLocation hLoc = ScreenLocation.left;
	ScreenLocation vLoc = ScreenLocation.bot;
	ScreenLocation rLoc = ScreenLocation.botLeft;
	EGuiSlider hSlider, vSlider;
	EGuiButton reset;
	int xs, xe, ys, ye;
	
	public EGuiPlayerViewer(IEnhancedGuiObject parentIn, int posX, int posY, int widthIn, int heightIn) {
		init(parentIn, posX, posY, widthIn, heightIn);
	}
	
	@Override
	public void initObjects() {
		hSlider = new EGuiSlider(this, startX, startY, width, 12, 0, 1440, 720, false).setDrawDefault(false).setDisplayString("");
		vSlider = new EGuiSlider(this, startX, startY, 12, height, -300, 300, 0, true).setDrawDefault(false).setDisplayString("");
		
		reset = new EGuiButton(this, startX, startY, 16, 16);
		
		addObject(hSlider, vSlider, reset);
		
		reorient();
	}
	
	@Override
	public void drawObject(int mXIn, int mYIn, float ticks) {
		//draw backgrounds
		GlStateManager.color(2.0f, 2.0f, 2.0f, 2.0f);
		drawRect(startX, startY, endX, endY, 0xff000000); //black border
		drawRect(xs, ys, xe, ye, 0xff1b1b1b); //gray inner
		drawRect(xs + 1, ys + 1, xe - 1, ye - 1, 0xff232323); //gray inner
		drawRect(xs + 2, ys + 2, xe - 2, ye - 2, 0xff2b2b2b); //gray inner
		drawRect(xs + 3, ys + 3, xe - 3, ye - 3, 0xff303030); //gray inner
		
		int scale = res.getScaleFactor();
		GL11.glEnable(GL11.GL_SCISSOR_TEST);
		GL11.glScissor((vSlider.endX + 1) * scale,
					   (Display.getHeight() - startY * scale) - (height - hSlider.height - 1) * scale,
					   (width - vSlider.width - 2) * scale,
					   (height - 2 - hSlider.height) * scale);
		{ //scissor start
			
			//draw player model
			GlStateManager.color(2.0f, 2.0f, 2.0f, 2.0f);
			double val = (height - (height / 10) - hSlider.height < width - (width / 10) - vSlider.width ? height - (height / 10) - hSlider.height : width - (width / 10) - vSlider.width) / 2.0;
			PlayerDrawer.drawPlayer(mc.thePlayer, (xs + xe) / 2, ye - (height / 16), hSlider.getSliderValue(), vSlider.getSliderValue(), (float) val);
			
		}
		GL11.glDisable(GL11.GL_SCISSOR_TEST);
		
		super.drawObject(mXIn, mYIn, ticks);
	}
	
	@Override
	public void actionPerformed(IEnhancedActionObject object, Object... args) {
		if (object == reset) {
			hSlider.reset();
			vSlider.reset();
		}
	}
	
	@Override
	public void move(int newX, int newY) {
		xs += newX;
		ys += newY;
		xe += newX;
		ye += newY;
		super.move(newX, newY);
	}
	
	@Override
	public EGuiPlayerViewer setPosition(int newX, int newY) {
		int oldXS = xs - startX;
		int oldYS = ys - startY;
		int oldXE = xe - startX;
		int oldYE = ye - startY;
	
		super.setPosition(newX, newY);
		
		xs = newX + oldXS;
		ys = newY + oldYS;
		xe = newX + oldXE;
		ye = newY + oldYE;
		return this;
	}
	
	private void reorient() {
		//check values
		if (vLoc != ScreenLocation.left && vLoc != ScreenLocation.right) { vLoc = ScreenLocation.left; }
		if (hLoc != ScreenLocation.top && hLoc != ScreenLocation.bot) { hLoc = ScreenLocation.bot; }
		if (!ScreenLocation.isCorner(rLoc)) { rLoc = ScreenLocation.botLeft; }
		
		int calcW = width - vSlider.width;
		int calcH = height - hSlider.height;
		
		//determine x
		if (vLoc == ScreenLocation.left) {
			vSlider.setDimensions(startX, hLoc == ScreenLocation.bot ? startY : hSlider.endY, vSlider.width, calcH);
			xs = vSlider.endX;
			xe = endX - 1;
		}
		else {
			vSlider.setDimensions(endX - vSlider.width, hLoc == ScreenLocation.bot ? startY : hSlider.endY, vSlider.width, calcH);
			xs = startX + 1;
			xe = vSlider.startX;
		}
		
		//determine y
		if (hLoc == ScreenLocation.top) {
			hSlider.setDimensions(vLoc == ScreenLocation.right ? startX : vSlider.endX, startY, calcW, hSlider.height);
			ys = hSlider.endY;
			ye = endY - 1;
		}
		else if (hLoc == ScreenLocation.bot) {
			hSlider.setDimensions(vLoc == ScreenLocation.right ? startX : vSlider.endX, endY - hSlider.height, calcW, hSlider.height);
			ys = startY + 1;
			ye = hSlider.startY;
		}
		
		//button loc
		int bX = 0, bY = 0;
		bX = vLoc == ScreenLocation.left ? startX : xe;
		bY = hLoc == ScreenLocation.top ? startY : vSlider.endY;
		reset.setDimensions(bX, bY, vSlider.width, hSlider.height);
		
		vSlider.setSliderValue(vSlider.getSliderValue());
		hSlider.setSliderValue(hSlider.getSliderValue());
	}
	
	public EGuiPlayerViewer setHSliderOrientation(ScreenLocation locIn) { hLoc = locIn; if (isInit()) { reorient(); } return this; }
	public EGuiPlayerViewer setVSliderOrientation(ScreenLocation locIn) { vLoc = locIn; if (isInit()) { reorient(); } return this; }
	public EGuiPlayerViewer setResetButtonOrientation(ScreenLocation locIn) { rLoc = locIn; if (isInit()) { reorient(); } return this; }
	public EGuiPlayerViewer setHSliderHeight(int heightIn) { hSlider.setDimensions(hSlider.startX, hSlider.startY, hSlider.width, heightIn); if (isInit()) { reorient(); } return this; }
	public EGuiPlayerViewer setVSliderWidth(int widthIn) { vSlider.setDimensions(vSlider.startX, vSlider.startY, widthIn, vSlider.height); if (isInit()) { reorient(); } return this; }
}
