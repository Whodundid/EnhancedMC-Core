package com.Whodundid.worldEditor.EditorUtil;

import com.Whodundid.core.app.AppType;
import com.Whodundid.core.app.RegisteredApps;
import com.Whodundid.core.enhancedGui.guiObjects.actionObjects.EGuiButton;
import com.Whodundid.core.enhancedGui.types.EnhancedGui;
import com.Whodundid.core.enhancedGui.types.WindowParent;
import com.Whodundid.core.enhancedGui.types.interfaces.IEnhancedActionObject;
import com.Whodundid.core.renderer.renderUtil.IRendererProxy;
import com.Whodundid.worldEditor.EditorApp;
import com.Whodundid.worldEditor.EditorGuiObjects.EditorBlockPalette;
import com.Whodundid.worldEditor.EditorGuiObjects.EditorCursor;
import com.Whodundid.worldEditor.EditorGuiObjects.EditorGuiRCM;
import com.Whodundid.worldEditor.EditorGuiObjects.EditorGuiTextInput;
import com.Whodundid.worldEditor.EditorGuiObjects.EditorGuiToolList;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.BlockPos;

//Last edited: Dec 10, 2018
//First Added: Nov 25, 2018
//Author: Hunter Bragg

public class EditorGuiBase extends WindowParent implements IRendererProxy {

	public EditorApp editor = (EditorApp) RegisteredApps.getApp(AppType.WORLDEDITOR);
	public EditorCursor editorCursor;
	public EditorGuiTextInput editorTextInput;
	public EditorGuiToolList editorToolList;
	public EditorGuiRCM editorRightClickMenu;
	public EditorBlockPalette editorBlockPalette;
	public EGuiButton draw3D, drawVertical, toggleHiRes, copyButton, pasteButton, renderBiomeMap;
	protected ScaledResolution res;
	public int imgWidth = 453;
	public int imgHeight = 453;
	public int drawWidth = 453;
	public int drawHeight = 453;
	public int locX = 231;
	public int locY = 251;
	public double zoomXHigh = 453, zoomYHigh = 453;
	public double zoomXLow = 0, zoomYLow = 0;
	public int centerX, centerY;
	
	@Override
	public void initGui() {
		super.initGui();
		
		enableHeader(false);
		
		res = new ScaledResolution(mc);
		centerX = res.getScaledWidth() / 2;
		centerY = res.getScaledHeight() / 2;
		startX = centerX - locX;
		startY = centerY - locY;
		endX = startX + imgWidth;
		endY = startY + imgHeight;
		
		zoomEditor(0);
		createBorder();
		
		if (!editor.firstCenterSet) {
			BlockPos pos = mc.thePlayer.getPosition();
			editor.setCenter(pos.getX(), pos.getY(), pos.getZ());
			editor.camPos.set(pos.getX(), pos.getZ(), pos.getY());
			editor.firstCenterSet = true;
		}
		
		editor.setEditorGuiReference(this);
		editor.get2D().init();
		editor.get3D().init();
	}
	
	@Override
	public void initObjects() {
		editorCursor = new EditorCursor(this);
		editorTextInput = new EditorGuiTextInput(this);
		editorToolList = new EditorGuiToolList(this);
		editorBlockPalette = new EditorBlockPalette(this);
		
		draw3D = new EGuiButton(this, midX - 359, midY - 168, 110, 20, (!editor.render3D) ? "Render 3D" : "Render by Layer");
		drawVertical = new EGuiButton(this, midX - 472, midY - 168, 110, 20, (!editor.renderVertical) ? "Render Horizontal" : "Render Vertical");
		renderBiomeMap = new EGuiButton(this, midX - 472, midY - 192, 110, 20, (!editor.renderBiomeMap) ? "Show Biome View" : "Hide Biome View");
		
		toggleHiRes = new EGuiButton(this, midX - 359, midY - 145, 110, 20, (editor.render3DHighRes) ? "Render Low Res" : "Render High Res");
		
		
		draw3D.setObjectID(1);
		drawVertical.setObjectID(2);
		renderBiomeMap.setObjectID(3);
		toggleHiRes.setObjectID(4);
		
		addObject(editorCursor, editorTextInput, editorToolList, editorBlockPalette);
		addObject(draw3D, drawVertical, renderBiomeMap, toggleHiRes);
	}
	
	@Override
	public void drawObject(int mXIn, int mYIn) {
		int halfWidth = res.getScaledWidth() / 2, halfHeight = res.getScaledHeight() / 2;
		editor.insideEditor = (mXIn >= startX && mXIn <= endX - 1 && mYIn >= startY + 3 && mYIn <= endY + 3);
		
		GlStateManager.disableAlpha();
		GlStateManager.disableBlend();
		mc.renderEngine.bindTexture(editor.imageHandlerBorder.getTextureLocation());
		GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
		drawTexture(halfWidth - locX - 3, halfHeight - locY, 0, 0, imgWidth + 6, imgHeight + 6, drawWidth + 6, drawHeight + 6);
		if (editor.render3D) {
			mc.renderEngine.bindTexture(editor.render3DHighRes ? editor.imageHandler3DHiRes.getTextureLocation() : editor.imageHandler3D.getTextureLocation());
			GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
			drawTexture(halfWidth - locX, halfHeight - locY + 3, 0, 0, 453, 453, 453, 453);
		} else {
			mc.renderEngine.bindTexture(editor.imageHandler2D.getTextureLocation());
			if (editor.getRenderVertical()) {
				GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
				drawTexture(halfWidth - locX, halfHeight - locY + 3, 0, 0, 453, 453, -453, -453);
			} else {
				GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
				drawTexture(halfWidth - locX, halfHeight - locY + 3, zoomXLow, zoomYLow, drawWidth, drawHeight, zoomXHigh, zoomYHigh);
			}
		}
		drawMainWindow();
		GlStateManager.enableAlpha();
		GlStateManager.enableBlend();
		
		super.drawObject(mXIn, mYIn);
	}
	
	protected void drawMainWindow() {
		if (editor.getRender3D()) {
			editor.get3D().draw();
		} else {
			editor.get2D().draw();
		}
	}
	
	@Override
	public void actionPerformed(IEnhancedActionObject object, Object... args) {
		if (object.equals(draw3D)) {
			editor.setRender3D(!editor.getRender3D());
			draw3D.setDisplayString((!editor.getRender3D()) ? "Render 3D" : "Render by Layer");
			if (editor.render3D) { editor.get3D().open(); }
		}
		if (object.equals(drawVertical)) {
			editor.setRenderVertical(!editor.getRenderVertical());
			if (editor.getRenderVertical()) { editor.setRender3D(false); }
			drawVertical.setDisplayString((!editor.getRenderVertical()) ? "Render Horizontal" : "Render Vertical");
		}
		if (object.equals(toggleHiRes)) {
			editor.setRenderHiRes(!editor.render3DHighRes);
			toggleHiRes.setDisplayString((editor.render3DHighRes) ? "Render Low Res" : "Render High Res");
		}
		
		if (object.equals(renderBiomeMap)) {
			editor.setRenderBiomeMap(!editor.renderBiomeMap);
			renderBiomeMap.setDisplayString((!editor.renderBiomeMap) ? "Show Biome View" : "Hide Biome View");
		}
	}
	
	@Override
	public void parseMousePosition(int mXIn, int mYIn) {
		super.parseMousePosition(mXIn, mYIn);
		if (editor.render3D) { editor.get3D().parseMousePosition(mXIn, mYIn); }
		else { editor.get2D().parseMousePosition(mXIn, mYIn); }
	}
	
	@Override
	public void mousePressed(int mXIn, int mYIn, int button) {
		super.mousePressed(mXIn, mYIn, button);
		if (editor.render3D) { editor.get3D().handleMousePress(mXIn, mYIn, button); }
		else { editor.get2D().handleMousePress(mXIn, mYIn, button); }
	}
	
	@Override
	public void mouseReleased(int mXIn, int mYIn, int button) {
		super.mouseReleased(mXIn, mYIn, button);
		if (editor.render3D) { editor.get3D().handleMouseRelease(mXIn, mYIn, button); }
		else { editor.get2D().handleMouseRelease(mXIn, mYIn, button); }
	}
	
	@Override
	public void mouseDragged(int mXIn, int mYIn, int button, long timeSinceLastClick) {
		super.mouseDragged(mXIn, mYIn, button, timeSinceLastClick);
		if (editor.render3D) { editor.get3D().handleMouseDrag(mXIn, mYIn, button, timeSinceLastClick); }
		else { editor.get2D().handleMouseDrag(mXIn, mYIn, button, timeSinceLastClick); }
	}
	
	@Override
	public void mouseScrolled(int change) {
		super.mouseScrolled(change);
		if (editor.render3D) { editor.get3D().handleMouseScroll(change); }
		else { editor.get2D().handleMouseScroll(change); }
	}
	
	@Override
	public void keyPressed(char typedChar, int keyCode) {
		if (editor.render3D) { editor.get3D().handleKeyPress(typedChar, keyCode); }
		else { editor.get2D().handleKeyPress(typedChar, keyCode); }
	}
	
	@Override
	public void keyReleased(char typedChar, int keyCode) {
		if (editor.render3D) { editor.get3D().handleKeyRelease(typedChar, keyCode); }
		else { editor.get2D().handleKeyRelease(typedChar, keyCode); }
	}
	
	@Override
	public void onClosed() {
		editor.get2D().onGuiClosed();
		editor.get3D().onGuiClosed();
		editor.closeEditor();
	}
	
	private void createBorder() {
		for (int x = 0; x < editor.imageHandlerBorder.GBI().getWidth(); x++) {
			if (x % 2 == 1) {
				editor.imageHandlerBorder.GBI().setRGB(x, 0, 0x330000);
				editor.imageHandlerBorder.GBI().setRGB(x, editor.imageHandlerBorder.GBI().getHeight() - 1, 0x330000);
				editor.imageHandlerBorder.GBI().setRGB(0, x, 0x330000);
				editor.imageHandlerBorder.GBI().setRGB(editor.imageHandlerBorder.GBI().getWidth() - 1, x, 0x330000);				
			} else {
				editor.imageHandlerBorder.GBI().setRGB(x, 0, 0xCC0000);
				editor.imageHandlerBorder.GBI().setRGB(x, editor.imageHandlerBorder.GBI().getHeight() - 1, 0xCC0000);
				editor.imageHandlerBorder.GBI().setRGB(0, x, 0xCC0000);
				editor.imageHandlerBorder.GBI().setRGB(editor.imageHandlerBorder.GBI().getWidth() - 1, x, 0xCC0000);
			}
		}
		editor.imageHandlerBorder.GBI().setRGB(76, 0, 0x00FF00);
		editor.imageHandlerBorder.GBI().setRGB(0, 76, 0x00FF00);
		editor.imageHandlerBorder.GBI().setRGB(152, 76, 0x00FF00);
		editor.imageHandlerBorder.GBI().setRGB(76, 152, 0x00FF00);
		editor.imageHandlerBorder.updateTextureData(editor.imageHandlerBorder.GBI());
	}
	
	public void zoomEditor(int scrollChange) {
		double currentZoom = editor.getZoomScale();
		editor.setZoomScale(currentZoom += (scrollChange * 0.25));
		if (editor.getZoomScale() < 1) { editor.setZoomScale(1); }
		if (editor.getZoomScale() > 25) { editor.setZoomScale(25); }
		
		zoomXHigh = imgWidth * editor.getZoomScale();
		zoomYHigh = imgHeight * editor.getZoomScale();
		
		zoomXLow = (zoomXHigh - drawWidth) / 2;
		zoomYLow = (zoomYHigh - drawHeight) / 2;
	}

	public boolean isThereRCM() { return editorRightClickMenu != null; }
	public EditorGuiRCM getRCM() { return editorRightClickMenu; }
	public void removeRCM() { removeObject(editorRightClickMenu); editorRightClickMenu = null; getTopParent().clearFocusedObject(); }
	
	public EditorGuiRCM addRCM(int mX, int mY) {
		editorRightClickMenu = new EditorGuiRCM(this, mX, mY);
		addObject(editorRightClickMenu);
		return editorRightClickMenu;
	}

	@Override public int getMX() { return mX; }
	@Override public int getMY() { return mY; }
}
