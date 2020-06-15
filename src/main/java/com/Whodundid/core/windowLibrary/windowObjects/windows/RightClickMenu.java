package com.Whodundid.core.windowLibrary.windowObjects.windows;

import com.Whodundid.core.EnhancedMC;
import com.Whodundid.core.util.miscUtil.EMouseHelper;
import com.Whodundid.core.util.resourceUtil.EResource;
import com.Whodundid.core.util.storageUtil.StorageBox;
import com.Whodundid.core.util.storageUtil.StorageBoxHolder;
import com.Whodundid.core.windowLibrary.windowObjects.actionObjects.WindowButton;
import com.Whodundid.core.windowLibrary.windowObjects.basicObjects.WindowLabel;
import com.Whodundid.core.windowLibrary.windowTypes.ActionWindowParent;
import com.Whodundid.core.windowLibrary.windowUtil.windowEvents.ObjectEvent;
import com.Whodundid.core.windowLibrary.windowUtil.windowEvents.eventUtil.MouseType;
import com.Whodundid.core.windowLibrary.windowUtil.windowEvents.events.EventMouse;
import java.util.List;
import net.minecraft.client.renderer.GlStateManager;

//Author: Hunter Bragg

public class RightClickMenu extends ActionWindowParent {
	
	RightClickMenu instance = null;
	protected StorageBoxHolder<String, WindowButton> options = new StorageBoxHolder();
	public WindowLabel title;
	public boolean useTitle = false;
	protected boolean dontCloseOnPress = false;
	public int optionHeight = 17;
	public int titleHeight = 14;
	public int backgroundColor = 0xff4b4b4b;
	public int titleBackgroundColor = 0xff1f1f1f;
	public int separatorLineColor = 0xff000000;
	public int borderColor = 0xff000000;
	
	public RightClickMenu() {
		super(EnhancedMC.getRenderer());
		setDimensions(EMouseHelper.getMx(), EMouseHelper.getMy(), 125, 15);
		instance = this;
		getTopParent().registerListener(this);
		actionReciever = parent;
		
		title = new WindowLabel(this, 0, 0, "");
		title.setVisible(useTitle);
		title.setDrawCentered(true);
		title.setColor(0xffbb00);
		addObject(title);
		
		setUseTitle(true);
		showInTaskBar = false;
	}
	
	public void addOption(String... optionNames) { for (String s : optionNames) { addOption(s); } }
	public void addOption(String optionName) { addOption(optionName, null); }
	public void removeOption(String... optionNames) { for (String s : optionNames) { removeOption(s); } }
	
	public void addOption(String optionName, EResource optionIcon) {
		if (optionName != null && !options.contains(optionName)) {
			WindowButton b = new WindowButton(this, 0, 0, 0, 0, optionName) {
				@Override
				public void drawObject(int mX, int mY) {
					if (isMouseInside(mX, mY)) {
						drawRect(startX + textOffset - 1, startY, endX, endY + 1, 0x99adadad);
					}
					if (optionIcon != null) {
						bindTexture(optionIcon);
						GlStateManager.enableBlend();
						if (isMouseInside(mX, mY)) { GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F); }
						else { GlStateManager.color(0.75F, 0.75F, 0.75F, 0.75F); }
						//GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_LINEAR);
						drawTexture(startX + 1, startY + 1, 16, 16, 16, 16, 16, 16);
					}
					super.drawObject(mX, mY);
				}
				@Override
				public void onPress() {
					if (getPressedButton() == 0 && isEnabled()) {
						playPressSound();
						instance.setSelectedObject(getString());
						instance.performAction(null, null);
						getTopParent().unregisterListener(instance);
						instance.close();
					}
					else { instance.close(); }
				}
			};
			b.setDrawStringCentered(false);
			b.setDisplayStringOffset(22);
			b.setDrawTextures(false);
			b.setRunActionOnPress(true);
			options.add(optionName, b);
			addObject(b);
			resize();
		}
	}
	
	public void addOptionAtPos(String optionName, EResource optionIcon, int posIn) {
		WindowButton b = new WindowButton(this, 0, 0, 0, 0, optionName) {
			@Override
			public void drawObject(int mX, int mY) {
				if (isMouseInside(mX, mY)) {
					drawRect(startX + textOffset - 1, startY, endX, endY + 1, 0x99adadad);
				}
				if (optionIcon != null) {
					bindTexture(optionIcon);
					GlStateManager.enableBlend();
					if (isMouseInside(mX, mY)) { GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F); }
					else { GlStateManager.color(0.75F, 0.75F, 0.75F, 0.75F); }
					//GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_LINEAR);
					drawTexture(startX + 1, startY + 1, 16, 16, 16, 16, 16, 16);
				}
				super.drawObject(mX, mY);
			}
			@Override
			public void onPress() {
				if (getPressedButton() == 0 && isEnabled()) {
					playPressSound();
					instance.setSelectedObject(getString());
					instance.performAction(null, null);
					getTopParent().unregisterListener(instance);
					instance.close();
				}
				else { instance.close(); }
			}
		};
		b.setDrawStringCentered(false);
		b.setDisplayStringOffset(22);
		b.setDrawTextures(false);
		b.setRunActionOnPress(true);
		options.add(posIn, optionName, b);
		addObject(b);
		resize();
	}
	
	public void removeOption(String optionName) {
		if (optionName != null && options.contains(optionName)) {
			List<StorageBox<String, WindowButton>> l = options.removeBoxesContainingObj(optionName);
			if (!l.isEmpty()) {
				StorageBox<String, WindowButton> s = l.get(0);
				if (s != null) {
					WindowButton b = s.getValue();
					removeObject(b);
				}
			}
			resize();
		}
	}
	
	public RightClickMenu setOptionEnabled(String optionName, boolean enabledVal) {
		List<StorageBox<String, WindowButton>> boxes = options.getAllBoxesWithObj(optionName);
		if (boxes.size() > 0) {
			for (StorageBox<String, WindowButton> b : boxes) {
				if (b.getObject().equals(optionName)) { b.getValue().setEnabled(enabledVal); }
			}
		}
		return this;
	}
	
	private void resize() {
		int longestOption = 0;
		int newWidth = 0;
		int newHeight = options.size() * optionHeight + options.size() + 4 + (useTitle ? titleHeight : 0);
		int sX = startX;
		int sY = startY;
		
		for (String s : options.getObjects()) {
			int w = mc.fontRendererObj.getStringWidth(s);
			if (w > longestOption) { longestOption = w; }
		}
		
		if (useTitle) {
			int len = mc.fontRendererObj.getStringWidth(title.getString());
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
			WindowButton b = options.getValue(i);
			b.setDimensions(sX + 2, sY + (useTitle ? titleHeight : 0) + 2 + (optionHeight * i + i), newWidth - 4, optionHeight);
		}
	}
	
	@Override
	public void drawObject(int mX, int mY) {
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
		
		super.drawObject(mX, mY);
	}
	
	@Override
	public void mousePressed(int mXIn, int mYIn, int button) {
		if (button == 1) { close(); }
		if (runActionOnPress) { onPress(); }
		super.mousePressed(mXIn, mYIn, button);
	}
	
	@Override
	public void keyPressed(char typedChar, int keyCode) {
		if (keyCode == 1) { getTopParent().removeObject(this); }
	}
	
	@Override
	public void onEvent(ObjectEvent e) {
		if (e instanceof EventMouse) {
			if (((EventMouse) e).getMouseType() == MouseType.Pressed) {
				if (!isMouseInside(((EventMouse) e).getMouseX(), ((EventMouse) e).getMouseY())) {
					if (!dontCloseOnPress) {
						getTopParent().unregisterListener(this);
						close();
					}
				}
			}
		}
	}
	
	public int getBackgroundColor() { return backgroundColor; }
	public int getTitleBackgroundColor() { return titleBackgroundColor; }
	public int getLineSepartorColor() { return separatorLineColor; }
	public int getBorderColor() { return borderColor; }
	public int getTitleHeight() { return titleHeight; }
	public WindowLabel getTitle() { return title; }
	public boolean hasTitle() { return useTitle; }
	
	public RightClickMenu setDontCloseOnPress(boolean val) { dontCloseOnPress = val; return this; }
	public RightClickMenu setBackgroundColor(int colorIn) { backgroundColor = colorIn; return this; }
	public RightClickMenu setTitleBackgroundColor(int colorIn) { titleBackgroundColor = colorIn; return this; }
	public RightClickMenu setSeparatorLineColor(int colorIn) { separatorLineColor = colorIn; return this; }
	public RightClickMenu setBorderColor(int colorIn) { borderColor = colorIn; return this; }
	public RightClickMenu setTitleHeight(int heightIn) { titleHeight = heightIn; resize(); return this; }
	public RightClickMenu setTitle(String titleIn) { title.setString(titleIn); return this; }
	public RightClickMenu setUseTitle(boolean val) { useTitle = val; title.setVisible(val); resize(); return this; }
	
}
