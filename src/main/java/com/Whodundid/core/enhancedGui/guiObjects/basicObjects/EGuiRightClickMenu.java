package com.Whodundid.core.enhancedGui.guiObjects.basicObjects;

import com.Whodundid.core.enhancedGui.objectEvents.EventMouse;
import com.Whodundid.core.enhancedGui.objectEvents.ObjectEvent;
import com.Whodundid.core.enhancedGui.objectEvents.eventUtil.MouseType;
import com.Whodundid.core.enhancedGui.types.WindowParent;
import com.Whodundid.core.enhancedGui.types.interfaces.IEnhancedActionObject;
import com.Whodundid.core.enhancedGui.types.interfaces.IEnhancedGuiObject;
import com.Whodundid.core.util.miscUtil.EMouseHelper;
import com.Whodundid.core.util.storageUtil.StorageBox;
import com.Whodundid.core.util.storageUtil.StorageBoxHolder;
import java.util.List;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;

//Last edited: Oct 26, 2018
//First Added: Oct 26, 2018
//Author: Hunter Bragg

public class EGuiRightClickMenu extends WindowParent implements IEnhancedActionObject {
	
	EGuiRightClickMenu instance = null;
	protected StorageBoxHolder<String, EGuiButton> options = new StorageBoxHolder();
	public EGuiLabel title;
	public boolean useTitle = false;
	public int optionHeight = 17;
	public int titleHeight = 14;
	public int backgroundColor = 0xff4b4b4b;
	public int titleBackgroundColor = 0xff1f1f1f;
	public int separatorLineColor = 0xff000000;
	public int borderColor = 0xff000000;
	protected Object selectedObject = null;
	protected IEnhancedGuiObject actionReciever;
	protected boolean runActionOnPress = false;
	
	public EGuiRightClickMenu() {
		setDimensions(EMouseHelper.getMx(), EMouseHelper.getMy(), 125, 15);
		setZLevel(1000);
		instance = this;
		getTopParent().registerListener(this);
		actionReciever = parent;
		
		title = new EGuiLabel(this, 0, 0, "");
		title.setVisible(useTitle);
		title.setDrawCentered(true);
		title.setDisplayStringColor(0xffbb00);
		addObject(title);
		
		setUseTitle(true);
	}
	
	public void addOption(String... optionNames) { for (String s : optionNames) { addOption(s); } }
	public void addOption(String optionName) { addOption(optionName, null); }
	public void removeOption(String... optionNames) { for (String s : optionNames) { removeOption(s); } }
	
	public void addOption(String optionName, ResourceLocation optionIcon) {
		if (optionName != null && !options.contains(optionName)) {
			EGuiButton b = new EGuiButton(this, 0, 0, 0, 0, optionName) {
				@Override
				public void drawObject(int mX, int mY, float ticks) {
					if (isMouseInside(mX, mY)) {
						drawRect(startX + textOffset - 1, startY, endX, endY + 1, 0x99adadad);
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
						instance.actionReciever.actionPerformed(instance);
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
		int longestOption = 0;
		int newWidth = 0;
		int newHeight = options.size() * optionHeight + options.size() + 4 + (useTitle ? titleHeight : 0);
		int sX = startX;
		int sY = startY;
		
		for (String s : options.getObjects()) {
			int w = fontRenderer.getStringWidth(s);
			if (w > longestOption) { longestOption = w; }
		}
		
		if (useTitle) {
			int len = fontRenderer.getStringWidth(title.getDisplayString());
			if (len > longestOption) { longestOption = len; }
		}
		
		newWidth = longestOption + 40;
		
		int testHeight = startY + newHeight;
		if (testHeight > res.getScaledHeight()) {
			int diff = testHeight - res.getScaledHeight();
			sY -= diff;
		}
		
		setDimensions(sX, sY, newWidth, newHeight);
		
		title.setDimensions(midX, startY + titleHeight / 2 - 3, 0, 0);
		
		for (int i = 0; i < options.size(); i++) {
			EGuiButton b = options.getValue(i);
			b.setDimensions(sX + 2, sY + (useTitle ? titleHeight : 0) + 2 + (optionHeight * i + i), newWidth - 4, optionHeight);
		}
	}
	
	@Override
	public void drawObject(int mX, int mY, float ticks) {
		drawRect(startX + 1, useTitle ? startY + titleHeight : startY + 1, endX - 1, endY, backgroundColor); //background
		drawRect(startX, startY, startX + 1, endY, borderColor); //left
		drawRect(startX, startY, endX, startY + 1, borderColor); //top
		drawRect(endX - 1, startY, endX, endY, borderColor); //right
		drawRect(startX, endY - 1, endX, endY, borderColor); //bottom
		drawRect(startX + 21, useTitle ? startY + titleHeight : startY + 1, startX + 22, endY - 1, separatorLineColor); //separator line
		
		if (useTitle) {
			drawRect(startX + 1, startY + 1, endX - 1, startY + titleHeight, titleBackgroundColor);
			drawRect(startX + 1, startY + titleHeight, endX - 1, startY + titleHeight + 1, separatorLineColor);
		}
		
		super.drawObject(mX, mY, ticks);
	}
	
	@Override
	public void mousePressed(int mXIn, int mYIn, int button) {
		if (runActionOnPress) { runActionOnPress(); }
		super.mousePressed(mXIn, mYIn, button);
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
	
	public EGuiRightClickMenu setBackgroundColor(int colorIn) { backgroundColor = colorIn; return this; }
	public EGuiRightClickMenu setTitleBackgroundColor(int colorIn) { titleBackgroundColor = colorIn; return this; }
	public EGuiRightClickMenu setSeparatorLineColor(int colorIn) { separatorLineColor = colorIn; return this; }
	public EGuiRightClickMenu setBorderColor(int colorIn) { borderColor = colorIn; return this; }
	public EGuiRightClickMenu setTitleHeight(int heightIn) { titleHeight = heightIn; resize(); return this; }
	public EGuiRightClickMenu setTitle(String titleIn) { title.setDisplayString(titleIn); return this; }
	public EGuiRightClickMenu setUseTitle(boolean val) { useTitle = val; title.setVisible(val); resize(); return this; }
	
	public int getBackgroundColor() { return backgroundColor; }
	public int getTitleBackgroundColor() { return titleBackgroundColor; }
	public int getLineSepartorColor() { return separatorLineColor; }
	public int getBorderColor() { return borderColor; }
	public int getTitleHeight() { return titleHeight; }
	public EGuiLabel getTitle() { return title; }
	public boolean hasTitle() { return useTitle; }
	
	@Override public boolean runActionOnPress() { return false; }
	@Override public EGuiRightClickMenu setRunActionOnPress(boolean val) { return null; }
	@Override public void performAction() {}
	@Override public EGuiRightClickMenu setActionReciever(IEnhancedGuiObject objIn) { actionReciever = objIn; return this; }
	@Override public IEnhancedGuiObject getActionReciever() { return actionReciever; }
	@Override public EGuiRightClickMenu setSelectedObject(Object objIn) { selectedObject = objIn; return this; }
	@Override public Object getSelectedObject() { return selectedObject; }
}
