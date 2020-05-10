package com.Whodundid.core.settings;

import com.Whodundid.core.coreApp.CoreApp;
import com.Whodundid.core.coreApp.EMCResources;
import com.Whodundid.core.enhancedGui.guiObjects.actionObjects.EGuiButton;
import com.Whodundid.core.enhancedGui.guiObjects.advancedObjects.textArea.EGuiTextArea;
import com.Whodundid.core.enhancedGui.guiObjects.advancedObjects.textArea.TextAreaLine;
import com.Whodundid.core.enhancedGui.types.EnhancedGuiObject;
import com.Whodundid.core.enhancedGui.types.WindowParent;
import com.Whodundid.core.enhancedGui.types.interfaces.IEnhancedActionObject;
import com.Whodundid.core.renderer.taskView.TaskBar;
import com.Whodundid.core.util.renderUtil.EColors;
import com.Whodundid.core.util.renderUtil.ScreenLocation;
import com.Whodundid.core.util.storageUtil.EArrayList;
import com.Whodundid.core.util.storageUtil.EDimension;
import com.Whodundid.core.util.storageUtil.StorageBox;
import com.Whodundid.core.util.storageUtil.StorageBoxHolder;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.MathHelper;

//Author: Hunter Bragg

public class KeyBindGui extends WindowParent {
	
	EGuiTextArea keyList;
	String description = "";
	String key = "";
	String defaultKey = "";
	String category = "";
	KeyBinding selectedKey = null;
	EGuiButton changeKey, resetKey;
	boolean changing = false;
	StorageBoxHolder<String, KeyBinding> keys = new StorageBoxHolder();
	StorageBoxHolder<Integer, Integer> used = new StorageBoxHolder();
	EArrayList<String> categories = new EArrayList();
	TextAreaLine l = null;
	
	public KeyBindGui() {
		super();
		aliases.add("keybinds", "keybind", "controls");
		windowIcon = EMCResources.keyboardIcon;
	}
	
	@Override
	public void initGui() {
		setObjectName("Minecraft Controls");
		setDimensions(400, 254);
		super.initGui();
		setResizeable(true);
		setMaximizable(true);
		setMinDims(361, 178);
	}
	
	@Override
	public void initObjects() {
		defaultHeader(this);
		
		keyList = new EGuiTextArea(this, startX + 10, startY + 20, width - (width / 2 + 10), height - 30) {
			@Override
			public void mousePressed(int mX, int mY, int button) {
				super.mousePressed(mX, mY, button);
				System.out.println(this.getCurrentLine());
				if (getCurrentLine() != null && getCurrentLine().getStoredObj() != null) {
					KeyBinding k = (KeyBinding) getCurrentLine().getStoredObj();
					loadKeyValues(k);
				}
				else { resetValues(); }
			}
			@Override
			public void keyPressed(char typedChar, int keyCode) {
				super.keyPressed(typedChar, keyCode);
				if (keyCode == 200) { //up
					int lineNum = MathHelper.clamp_int(getCurrentLine().getLineNumber() - 2, 0, getTextDocument().size() - 1);
					l = (TextAreaLine) (getTextDocument().get(lineNum));
				}
				else if (keyCode == 208) { //down
					int lineNum = MathHelper.clamp_int(getCurrentLine().getLineNumber(), 0, getTextDocument().size() - 1);
					l = (TextAreaLine) (getTextDocument().get(lineNum));
				}
				if (l != null && l.getStoredObj() != null) {
					KeyBinding k = (KeyBinding) l.getStoredObj();
					loadKeyValues(k);
				}
				else { resetValues(); }
			}
		};
		keyList.setDrawLineNumbers(false).setResetDrawn(false);
		
		changeKey = new EGuiButton(this, keyList.endX + 20, startY + 140, 70, 20) {
			@Override
			public void keyPressed(char typedChar, int keyCode) {
				if (changing) {
					if (keyCode == 1) { mc.gameSettings.setOptionKeyBinding(selectedKey, 0); }
					else if (keyCode != 0) { mc.gameSettings.setOptionKeyBinding(selectedKey, keyCode); }
					else if (typedChar > 0) { mc.gameSettings.setOptionKeyBinding(selectedKey, typedChar + 256); }
					KeyBinding.resetKeyBindingArrayAndHash();
					updateListVisuals();
				}
			}
			
			@Override
			public void mousePressed(int mXIn, int mYIn, int button) {
				if (changing) {
					mc.gameSettings.setOptionKeyBinding(selectedKey, -100 + button);
					KeyBinding.resetKeyBindingArrayAndHash();
					updateListVisuals();
				}
				else { super.mousePressed(mXIn, mYIn, button); }
			}
		};
		
		resetKey = new EGuiButton(this, keyList.endX + 100, startY + 140, 59, 20, "Reset");
		
		changeKey.setVisible(false);
		resetKey.setVisible(false);
		
		addObject(keyList, changeKey, resetKey);
		
		buildKeyList();
	}
	
	@Override
	public void drawObject(int mXIn, int mYIn) {
		drawDefaultBackground();
		
		drawCenteredStringWithShadow("Minecraft Key Bindings", startX + 9 + keyList.width / 2, startY + 7, 0xb2b2b2);
		drawCenteredStringWithShadow("Key Binding Values", keyList.endX + (endX - (keyList.endX)) / 2, startY + 7, 0xb2b2b2);
		
		//draw hotkey value display container
		drawRect(keyList.endX + 9, startY + 20, endX - 10, endY - 10, 0xff000000);
		drawRect(keyList.endX + 10, startY + 21, endX - 11, endY - 11, 0xff2D2D2D);
		
		//draw separator lines
		if (selectedKey != null) {
			drawRect(keyList.endX + 10, startY + 61, endX - 10, startY + 62, 0xff000000);
			drawRect(keyList.endX + 10, startY + 103, endX - 10, startY + 104, 0xff000000);
		}
		
		int scale = mc.gameSettings.guiScale;
		
		scissor(startX, startY, endX - 11, endY);
		{
			drawKeyValues();
		}
		endScissor();
		
		super.drawObject(mXIn, mYIn);
	}
	
	@Override
	public EnhancedGuiObject resize(int xIn, int yIn, ScreenLocation areaIn) {
		if (xIn != 0 || yIn != 0) {
			int posY = keyList.getVScrollBar().getScrollPos();
			int posX = keyList.getHScrollBar().getScrollPos();
			
			TextAreaLine l = keyList.getCurrentLine();
			super.resize(xIn, yIn, areaIn);
			
			keyList.getVScrollBar().onResizeUpdate(posY, xIn, yIn, areaIn);
			keyList.getHScrollBar().onResizeUpdate(posX, 1, yIn, areaIn);
			if (l != null) {
				keyList.setSelectedLine(keyList.getLineWithTextAndObject(l.getText(), l.getStoredObj()));
			}
			if (selectedKey != null) {
				changeKey.setVisible(true);
				resetKey.setVisible(true);
				loadKeyValues(selectedKey);
			}
		}
		return this;
	}
	
	@Override
	public void maximize() {
		try {
			EDimension screen = getTopParent().getDimensions();
			
			if (getTopParent().containsObject(TaskBar.class)) {
				switch (CoreApp.taskBarSide.get()) {
				case "top": setDimensions(0, header.height + TaskBar.drawSize, screen.width, screen.height - (header.height + TaskBar.drawSize)); break;
				case "bottom": setDimensions(0, header.height, screen.width, screen.height - (header.height + TaskBar.drawSize)); break;
				case "left": setDimensions(TaskBar.drawSize, header.height, screen.width - TaskBar.drawSize, screen.height - header.height); break;
				case "right": setDimensions(0, 0, screen.width - TaskBar.drawSize, screen.height - header.height); break;
				}
			}
			else {
				setDimensions(0, header.height, screen.width, screen.height - header.height);
			}
			
			int pos = keyList.getVScrollBar().getScrollPos();
			reInitObjects();
			keyList.getVScrollBar().setScrollBarPos(pos);
		}
		catch (Exception e) { e.printStackTrace(); }
	}
	
	@Override
	public void miniturize() {
		setDimensions(getPreMax());
		
		int pos = keyList.getVScrollBar().getScrollPos();
		reInitObjects();
		keyList.getVScrollBar().setScrollBarPos(pos);
	}
	
	protected boolean updateLists() { return updateLists(null); }
	protected boolean updateLists(KeyBinding test) {
		try {
			//create lists for the keys to be added, total number of used keys, and key categories
			keys = new StorageBoxHolder();
			used = new StorageBoxHolder();
			categories = new EArrayList();
			
			//add all the keybindings and their descriptions to the key list
			for (KeyBinding k : mc.gameSettings.keyBindings) {
				String category = I18n.format(k.getKeyCategory(), new Object[0]);
				
				keys.add(category, k);
					
				//don't add the keybinding to the used list if the binding is 'NONE'
				if (k.getKeyCode() != 0) {
					int val = 1;
					if (used.contains(k.getKeyCode())) {
						val = used.getValueInBox(k.getKeyCode()) + 1; //increment the number of times used
					}
					used.put(k.getKeyCode(), val); //update the existing value
				}
				
				if (!categories.contains(category)) {
					categories.add(category);
				}
			}
				
			if (test != null) {
				return used.contains(test.getKeyCode()) && used.getValueInBox(test.getKeyCode()) > 1;
			}
		}
		catch (Exception e) { e.printStackTrace(); }
		
		return false;
	}
	
	protected boolean buildKeyList() { return buildKeyList(null); }
	protected boolean buildKeyList(KeyBinding test) {
		//prep the list to be rebuilt
		keyList.clear();
		
		boolean contains = updateLists(test);
		
		for (int i = 0; i < categories.size(); i++) {
			String s = categories.get(i);
			keyList.addTextLine(EnumChatFormatting.BLUE + "" + EnumChatFormatting.BOLD + s).setLineNumberColor(0xb2b2b2);
			for (StorageBox<String, KeyBinding> b : keys.getAllBoxesWithObj(s)) {
				KeyBinding k = b.getValue();
				
				int color = k.getKeyCode() != 0 ? used.getValueInBox(k.getKeyCode()) > 1 ? EColors.lred.c() : (k.getKeyCodeDefault() != k.getKeyCode() ? 0x55ff55 : 0xb2b2b2) : 0x606060;
				keyList.addTextLine("   " +	I18n.format(k.getKeyDescription(), new Object[0]), color, k).setLineNumberColor(0xb2b2b2);
			}
			if (i < categories.size() - 1) { keyList.addTextLine(); }
		}
		
		return contains;
	}
	
	protected void drawKeyValues() {
		
		if (selectedKey != null) {
			drawStringWithShadow("Key Name:", keyList.endX + 20, startY + 29, 0xffbb00);
			drawStringWithShadow("Default key:", keyList.endX + 20, startY + 71, 0xffbb00);
			drawStringWithShadow("Modify Key:", keyList.endX + 20, startY + 113, 0xffbb00);
			
			drawStringWithShadow(description, keyList.endX + 28, startY + 45, 0x00ffdc);
			drawStringWithShadow(defaultKey, keyList.endX + 28, startY + 86, 0x00ffdc);
		}
		else {
			drawCenteredStringWithShadow("Click on a key binding", keyList.endX + (endX - (keyList.endX)) / 2, startY + 10 + ((endY - 10) - (startY + 10)) / 2 - 6, 0xffbb00);
			drawCenteredStringWithShadow("to see its values.", keyList.endX + (endX - (keyList.endX)) / 2, startY + 10 + ((endY - 10) - (startY + 10)) / 2 + 6, 0xffbb00);
		}
	}
	
	public void loadKeyValues(KeyBinding keyIn) {
		try {
			resetValues();
			changeKey.setVisible(true);
			resetKey.setVisible(true);
			selectedKey = keyIn;
			category = I18n.format(keyIn.getKeyCategory(), new Object[0]);
			description = I18n.format(keyIn.getKeyDescription(), new Object[0]);
			defaultKey = GameSettings.getKeyDisplayString(keyIn.getKeyCodeDefault());
			key = GameSettings.getKeyDisplayString(keyIn.getKeyCode());
			changeKey.setDisplayString(GameSettings.getKeyDisplayString(keyIn.getKeyCode()));
			int color = !updateLists(selectedKey) ? (selectedKey.getKeyCodeDefault() != selectedKey.getKeyCode() ? 0x55ff55 : 0xffffff) : 0xff5555;
			changeKey.setDisplayStringColor(color);
			resetKey.setEnabled(keyIn.getKeyCodeDefault() != keyIn.getKeyCode());
		} catch (Exception e) { e.printStackTrace(); }
	}
	
	protected void updateListVisuals() {
		int pos = keyList.getVScrollBar().getScrollPos();
		int color = !buildKeyList(selectedKey) ? (selectedKey.getKeyCodeDefault() != selectedKey.getKeyCode() ? 0x55ff55 : 0xffffff) : 0xff5555;
		
		changing = false;
		changeKey.setDisplayString(GameSettings.getKeyDisplayString(selectedKey.getKeyCode()));
		changeKey.setDisplayStringColor(color);
		resetKey.setEnabled(selectedKey.getKeyCodeDefault() != selectedKey.getKeyCode());
		
		TextAreaLine l = keyList.getLineWithText("   " + I18n.format(selectedKey.getKeyDescription(), new Object[0]));
		keyList.setSelectedLine(l);
		keyList.getVScrollBar().setScrollBarPos(pos);
		
		getTopParent().setEscapeStopper(null);
	}
	
	@Override
	public void actionPerformed(IEnhancedActionObject object, Object... args) {
		if (object == changeKey) {
			changing = true;
			changeKey.setDisplayString(EnumChatFormatting.WHITE + "> " + EnumChatFormatting.YELLOW + changeKey.getDisplayString() + EnumChatFormatting.WHITE + " <");
			changeKey.setDisplayStringColor(selectedKey.getKeyCodeDefault() != selectedKey.getKeyCode() ? 0x55ff55 : 0xffffff);
			getTopParent().setEscapeStopper(this);
		}
		if (object == resetKey) {
			mc.gameSettings.setOptionKeyBinding(selectedKey, selectedKey.getKeyCodeDefault());
			KeyBinding.resetKeyBindingArrayAndHash();
			updateListVisuals();
			
			/*
			int pos = keyList.getVScrollBar().getScrollPos();
			int color = !buildKeyList(selectedKey) ? (selectedKey.getKeyCodeDefault() != selectedKey.getKeyCode() ? 0x55ff55 : 0xffffff) : 0xff5555;
			
			
			changeKey.setDisplayString(GameSettings.getKeyDisplayString(selectedKey.getKeyCode()));
			changeKey.setDisplayStringColor(color);
			resetKey.setEnabled(selectedKey.getKeyCodeDefault() != selectedKey.getKeyCode());
			
			TextAreaLine l = keyList.getLineWithText("   " + I18n.format(selectedKey.getKeyDescription(), new Object[0]));
			keyList.setSelectedLine(l);
			keyList.getVScrollBar().setScrollBarPos(pos);
			*/
		}
	}
	
	public void resetValues() {
		selectedKey = null;
		changing = false;
		changeKey.setVisible(false);
		resetKey.setVisible(false);
		category = "";
		description = "";
		defaultKey = "";
		key = "";
	}
	
	@Override
	public void keyPressed(char typedChar, int keyCode) {
		if (!changing) {
			if (keyCode == 1) { close(); }
		}
	}
	
	@Override
	public void close() {
		getTopParent().setEscapeStopper(null);
		super.close();
	}
	
}
