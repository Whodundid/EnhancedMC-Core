package com.Whodundid.core.settings;

import com.Whodundid.core.enhancedGui.guiObjects.advancedObjects.textArea.EGuiTextArea;
import com.Whodundid.core.enhancedGui.guiObjects.advancedObjects.textArea.TextAreaLine;
import com.Whodundid.core.enhancedGui.guiObjects.basicObjects.EGuiButton;
import com.Whodundid.core.enhancedGui.types.EnhancedGuiObject;
import com.Whodundid.core.enhancedGui.types.WindowParent;
import com.Whodundid.core.enhancedGui.types.interfaces.IEnhancedActionObject;
import com.Whodundid.core.util.renderUtil.ScreenLocation;
import com.Whodundid.core.util.storageUtil.EArrayList;
import com.Whodundid.core.util.storageUtil.StorageBox;
import com.Whodundid.core.util.storageUtil.StorageBoxHolder;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.MathHelper;

public class KeyBindGui extends WindowParent {
	
	EGuiTextArea keyList;
	String description = "";
	String key = "";
	String defaultKey = "";
	String category = "";
	KeyBinding selectedKey = null;
	EGuiButton changeKey, resetKey;
	boolean changing = false;
	
	public KeyBindGui() {
		super();
		aliases.add("keybinds", "keybind", "controls");
	}
	
	@Override
	public void initGui() {
		setObjectName("Minecraft Controls");
		setDimensions(380, 254);
		super.initGui();
		setResizeable(true);
		setMinDims(150, 150);
	}
	
	@Override
	public void initObjects() {
		defaultHeader(this);
		
		keyList = new EGuiTextArea(this, startX + 10, startY + 20, width - 190, height - 30) {
			@Override
			public void mousePressed(int mX, int mY, int button) {
				if (getCurrentLine() != null && getCurrentLine().getStoredObj() != null) {
					KeyBinding k = (KeyBinding) getCurrentLine().getStoredObj();
					loadKeyValues(k);
				}
				else { resetValues(); }
				super.mousePressed(mX, mY, button);
			}
			@Override
			public void keyPressed(char typedChar, int keyCode) {
				super.keyPressed(typedChar, keyCode);
				TextAreaLine l = null;
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
			        changing = false;
			        changeKey.setDisplayString(GameSettings.getKeyDisplayString(selectedKey.getKeyCode()));
			        changeKey.setDisplayStringColor(selectedKey.getKeyCodeDefault() != selectedKey.getKeyCode() ? 0x55ff55 : 0xffffff);
			        resetKey.setEnabled(selectedKey.getKeyCodeDefault() != selectedKey.getKeyCode());
			        int pos = keyList.getVScrollBar().getScrollPos();
			        buildKeyList();
			        TextAreaLine l = keyList.getLineWithText("   " + I18n.format(selectedKey.getKeyDescription(), new Object[0]));
			        //System.out.println(l);
			        keyList.setSelectedLine(l);
			        keyList.getVScrollBar().setScrollBarPos(pos);
				}
			}
		};
		
		resetKey = new EGuiButton(this, keyList.endX + 100, startY + 140, 59, 20, "Reset");
		
		changeKey.setVisible(false);
		resetKey.setVisible(false);
		
		addObject(keyList, changeKey, resetKey);
		
		buildKeyList();
	}
	
	@Override
	public void drawObject(int mXIn, int mYIn, float ticks) {
		drawDefaultBackground();
		
		drawCenteredStringWithShadow("Minecraft Key Bindings", startX + 9 + keyList.width / 2, startY + 7, 0xb2b2b2);
		drawCenteredStringWithShadow("Key Binding Values", keyList.endX + 90, startY + 7, 0xb2b2b2);
		
		//draw hotkey value display container
		drawRect(keyList.endX + 9, startY + 20, endX - 10, endY - 10, 0xff000000);
		drawRect(keyList.endX + 10, startY + 21, endX - 11, endY - 11, 0xff2D2D2D);
		
		//draw separator lines
		//System.out.println(keyList.getCurrentLine());
		if (keyList.getCurrentLine() != null && keyList.getCurrentLine().getStoredObj() != null) {
			drawRect(keyList.endX + 10, startY + 61, endX - 10, startY + 62, 0xff000000);
			drawRect(keyList.endX + 10, startY + 103, endX - 10, startY + 104, 0xff000000);
		}
		
		int scale = mc.gameSettings.guiScale;
		
		//GL11.glEnable(GL11.GL_SCISSOR_TEST);
		//GL11.glScissor(
		//		((keyList.endX + 10) * scale),
		//		(Display.getHeight() - startY * scale) - (height - 1) * scale,
		//		(endX - keyList.endX - 21) * scale,
		//		(height - 1) * scale);
		
		drawKeyValues();
		//GL11.glDisable(GL11.GL_SCISSOR_TEST);
		
		super.drawObject(mXIn, mYIn, ticks);
	}
	
	@Override
	public EnhancedGuiObject resize(int xIn, int yIn, ScreenLocation areaIn) {
		int pos = keyList.getVScrollBar().getScrollPos();
		TextAreaLine l = keyList.getCurrentLine();
		super.resize(xIn, yIn, areaIn);
		keyList.getVScrollBar().setScrollBarPos(pos);
		keyList.setSelectedLine(l);
		return this;
	}
	
	protected void buildKeyList() {
		keyList.clear();
		
		StorageBoxHolder<String, KeyBinding> keys = new StorageBoxHolder();
		EArrayList<String> categories = new EArrayList();
		
		keys.setAllowDuplicates(true);
		
		for (KeyBinding k : mc.gameSettings.keyBindings) {
			String category = I18n.format(k.getKeyCategory(), new Object[0]);
			
			keys.add(category, k);
			
			if (!categories.contains(category)) {
				categories.add(category);
			}
		}
		
		for (int i = 0; i < categories.size(); i++) {
			String s = categories.get(i);
			keyList.addTextLine(EnumChatFormatting.BLUE + "" + EnumChatFormatting.BOLD + s).setLineNumberColor(0xb2b2b2);
			for (StorageBox<String, KeyBinding> b : keys.getAllBoxesWithObj(s)) {
				KeyBinding k = b.getValue();
				keyList.addTextLine("   " +	I18n.format(k.getKeyDescription(), new Object[0]), (k.getKeyCodeDefault() != k.getKeyCode() ? 0x55ff55 : 0xb2b2b2), k).setLineNumberColor(0xb2b2b2);
			}
			if (i < categories.size() - 1) { keyList.addTextLine(); }
		}
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
			drawCenteredStringWithShadow("Click on a key binding", keyList.endX + 90, startY + 120, 0xffbb00);
			drawCenteredStringWithShadow("to see its values.", keyList.endX + 90, startY + 132, 0xffbb00);
		}
	}
	
	public void loadKeyValues(KeyBinding keyIn) {
		try {
			resetValues();
			changeKey.setVisible(true);
			resetKey.setVisible(true);
			selectedKey = keyIn;
			category = I18n.format(keyIn.getKeyCategory(), new Object[0]);
			
			String descriptionCheck = I18n.format(keyIn.getKeyDescription(), new Object[0]);
			if (mc.fontRendererObj.getStringWidth(descriptionCheck) > 130) {
				description = descriptionCheck.substring(0, descriptionCheck.length() - 5) + "...";
			}
			else { description = descriptionCheck; }
			
			defaultKey = GameSettings.getKeyDisplayString(keyIn.getKeyCodeDefault());
			key = GameSettings.getKeyDisplayString(keyIn.getKeyCode());
			changeKey.setDisplayString(GameSettings.getKeyDisplayString(keyIn.getKeyCode()));
			changeKey.setDisplayStringColor(keyIn.getKeyCodeDefault() != keyIn.getKeyCode() ? 0x55ff55 : 0xffffff);
			resetKey.setEnabled(keyIn.getKeyCodeDefault() != keyIn.getKeyCode());
		} catch (Exception e) { e.printStackTrace(); }
	}
	
	public void resetValues() {
		selectedKey = null;
		changeKey.setVisible(false);
		resetKey.setVisible(false);
		category = "";
		description = "";
		defaultKey = "";
		key = "";
	}
	
	@Override
	public void actionPerformed(IEnhancedActionObject object, Object... args) {
		if (object == changeKey) {
			changing = true;
			changeKey.setDisplayString(EnumChatFormatting.WHITE + "> " + EnumChatFormatting.YELLOW + changeKey.getDisplayString() + EnumChatFormatting.WHITE + " <");
			changeKey.setDisplayStringColor(selectedKey.getKeyCodeDefault() != selectedKey.getKeyCode() ? 0x55ff55 : 0xffffff);
		}
		if (object == resetKey) {
			mc.gameSettings.setOptionKeyBinding(selectedKey, selectedKey.getKeyCodeDefault());
			changeKey.setDisplayString(GameSettings.getKeyDisplayString(selectedKey.getKeyCode()));
			changeKey.setDisplayStringColor(selectedKey.getKeyCodeDefault() != selectedKey.getKeyCode() ? 0x55ff55 : 0xffffff);
			resetKey.setEnabled(selectedKey.getKeyCodeDefault() != selectedKey.getKeyCode());
			KeyBinding.resetKeyBindingArrayAndHash();
		}
	}
	
	@Override
	public void keyPressed(char typedChar, int keyCode) {
		if (!changing) {
			if (keyCode == 1) { close(); }
		}
	}
}
