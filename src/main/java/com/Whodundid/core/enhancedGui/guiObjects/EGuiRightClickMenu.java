package com.Whodundid.core.enhancedGui.guiObjects;

import com.Whodundid.core.enhancedGui.EnhancedActionObject;
import com.Whodundid.core.enhancedGui.guiUtil.events.EventMouse;
import com.Whodundid.core.enhancedGui.guiUtil.events.ObjectEvent;
import com.Whodundid.core.enhancedGui.guiUtil.events.eventUtil.MouseType;
import com.Whodundid.core.enhancedGui.interfaces.IEnhancedGuiObject;
import com.Whodundid.core.util.storageUtil.StorageBox;
import com.Whodundid.core.util.storageUtil.StorageBoxHolder;
import java.util.List;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;

//Last edited: Oct 26, 2018
//First Added: Oct 26, 2018
//Author: Hunter Bragg

public class EGuiRightClickMenu extends EnhancedActionObject {
	
	EGuiRightClickMenu instance = null;
	protected StorageBoxHolder<String, EGuiButton> options = new StorageBoxHolder();
	public String title = "";
	public boolean useTitle = false;
	
	public EGuiRightClickMenu(IEnhancedGuiObject parentIn, int x, int y) {
		init(parentIn, x, y, 125, 15);
		setZLevel(1000);
		instance = this;
		getTopParent().registerListener(this);
	}
	
	public void addOption(String... optionNames) { for (String s : optionNames) { addOption(s); } }
	public void addOption(String optionName) { addOption(optionName, null); }
	public void removeOption(String... optionNames) { for (String s : optionNames) { removeOption(s); } }
	
	public void addOption(String optionName, ResourceLocation optionIcon) {
		if (optionName != null && !options.contains(optionName)) {
			EGuiButton b = new EGuiButton(this, startX + 2, startY + (options.size() * 17 + options.size()) + 2, width - 4, 17, optionName) {
				@Override
				public void drawObject(int mX, int mY, float ticks) {
					if (isMouseInside(mX, mY)) {
						drawRect(startX + textOffset - 1, startY, endX, endY, 0xffadadad);
					}
					if (optionIcon != null) {
						mc.renderEngine.bindTexture(optionIcon);
						GlStateManager.enableBlend();
						if (isMouseInside(mX, mY)) { GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F); }
						else { GlStateManager.color(0.75F, 0.75F, 0.75F, 0.75F); }
						drawCustomSizedTexture(startX + 1, startY + 1, 16, 16, 16, 16, 16, 16);
					}
					super.drawObject(mX, mY, ticks);
				}
				@Override
				public void performAction() {
					if (getPressedButton() == 0) {
						playPressSound();
						instance.setSelectedObject(getDisplayString());
						instance.getParent().actionPerformed(instance);
						getTopParent().unregisterListener(instance);
						instance.close();
					}
				}
			};
			b.setDrawStringCentered(false);
			b.setDisplayStringOffset(22);
			b.setDrawDefault(false);
			b.setRunActionOnPress(true);
			options.add(optionName, b);
			addObject(b);
			resize();
		}
	}
	
	public void removeOption(String optionName) {
		if (optionName != null && options.contains(optionName)) {
			List<StorageBox<String, EGuiButton>> l = options.removeBoxesContainingObj(optionName);
			if (!l.isEmpty()) {
				StorageBox<String, EGuiButton> s = l.get(0);
				if (s != null) {
					EGuiButton b = s.getValue();
					removeObject(b);
				}
			}
			resize();
		}
	}
	
	private void resize() {
		int newWidth = 0;
		int longestOption = 0;
		for (String s : options.getObjects()) {
			int w = fontRenderer.getStringWidth(s);
			if (w > longestOption) { longestOption = w; }
		}
		newWidth = longestOption + 40;
		
		int newHeight = 0;
		newHeight = options.size() * 17 + options.size() + 3;
		
		setDimensions(startX, startY, newWidth, newHeight);
		for (EGuiButton b : options.getValues()) {
			b.setDimensions(b.startX, b.startY, newWidth - 4, b.height);
		}
	}
	
	@Override
	public void drawObject(int mX, int mY, float ticks) {
		drawRect(startX + 1, startY + 1, endX - 1, endY - 1, 0xf0686868); //background
		drawRect(startX, startY, startX + 1, endY, 0xff000000); //left
		drawRect(startX, startY, endX, startY + 1, 0xff000000); //top
		drawRect(endX - 1, startY, endX, endY, 0xff000000); //right
		drawRect(startX, endY - 1, endX, endY, 0xff000000); //bottom
		drawRect(startX + 21, startY + 1, startX + 22, endY - 1, 0xf0505050); //separator line
		super.drawObject(mX, mY, ticks);
	}
	
	@Override
	public void mousePressed(int mXIn, int mYIn, int button) {
		if (runActionOnPress) { runActionOnPress(); }
		super.mousePressed(mXIn, mYIn, button);
	}
	
	@Override
	public void mouseReleased(int mXIn, int mYIn, int button) {
		super.mouseReleased(mXIn, mYIn, button);
	}
	
	@Override
	public void keyPressed(char typedChar, int keyCode) {
		if (keyCode == 1) { getTopParent().removeObject(this); }
	}
	
	@Override
	public void onListen(ObjectEvent e) {
		if (e instanceof EventMouse) {
			if (((EventMouse) e).getMouseType() == MouseType.Pressed) {
				if (!isMouseInside(((EventMouse) e).getMouseX(), ((EventMouse) e).getMouseY())) {
					getTopParent().unregisterListener(this);
					close();
				}
			}
		}
	}
	
	public EGuiRightClickMenu setTitle(String titleIn) { title = titleIn; return this; }
	public EGuiRightClickMenu setUseTitle(boolean val) { useTitle = val; return this; }
	public String getTitle() { return title; }
	public boolean usesTitle() { return useTitle; }
}
