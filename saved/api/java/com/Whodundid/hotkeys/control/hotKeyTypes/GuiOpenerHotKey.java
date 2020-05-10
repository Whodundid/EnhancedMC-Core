package com.Whodundid.hotkeys.control.hotKeyTypes;

import com.Whodundid.core.util.chatUtil.ChatBuilder;
import com.Whodundid.core.util.guiUtil.GuiOpener;
import com.Whodundid.hotkeys.control.HotKey;
import com.Whodundid.hotkeys.control.hotKeyUtil.KeyActionType;
import com.Whodundid.hotkeys.control.hotKeyUtil.KeyComboAction;
import net.minecraft.util.EnumChatFormatting;

//Last edited: 9-30-18
//First Added: 9-14-18
//Author: Hunter Bragg

public class GuiOpenerHotKey extends HotKey {
	
	public Class gui;
	public Class[] paramTypes;
	public Object[] paramValues;
	
	public GuiOpenerHotKey(String keyNameIn, KeyComboAction keysIn, Class guiIn) { this(keyNameIn, keysIn, guiIn, false, "", null); }
	public GuiOpenerHotKey(String keyNameIn, KeyComboAction keysIn, Class guiIn, boolean builtInVal) { this(keyNameIn, keysIn, guiIn, false, "", null); }
	public GuiOpenerHotKey(String keyNameIn, KeyComboAction keysIn, Class guiIn, String descriptionIn) { this(keyNameIn, keysIn, guiIn, false, descriptionIn, null); }
	public GuiOpenerHotKey(String keyNameIn, KeyComboAction keysIn, Class guiIn, boolean builtInVal, String descriptionIn, String builtInSubModTypeIn) {
		super(keyNameIn, keysIn, builtInVal, KeyActionType.GUI_OPENER, builtInSubModTypeIn);
		if (descriptionIn != null && !descriptionIn.isEmpty()) { description = descriptionIn; }
		gui = guiIn;
	}
	
	public Class getGui() { return gui; }
	public String getGuiName() { return gui.getName(); }
	public String getGuiDisplayName() { return gui.getSimpleName(); }
	public GuiOpenerHotKey setGui(Class guiIn) { gui = guiIn; return this; }
	public Class[] getParamTypes() { return paramTypes; }
	public Object[] getParamValues() { return paramValues; }
	public GuiOpenerHotKey setParamTypes(Class[] typesIn) { paramTypes = typesIn; return this; }
	public GuiOpenerHotKey setParamValues(Object[] valuesIn) { paramValues = valuesIn; return this; }
	
	@Override
	public void executeHotKeyAction() {
		try {
			GuiOpener.openGui(gui, paramTypes, paramValues);
		} catch (Exception e) {
			e.printStackTrace();
			if (mc.thePlayer != null) {
				mc.thePlayer.addChatMessage(ChatBuilder.of(EnumChatFormatting.RED + "EMC Hotkey Error!\n" +
														   EnumChatFormatting.RED + "Error opening gui: " + EnumChatFormatting.AQUA + (gui != null ? gui.getName() : "UnknownGui") + "\n" +
														   EnumChatFormatting.WHITE + e).build());
			}
		}
	}
	
	@Override
	public String getHotKeyStatistics() {
		String base = super.getHotKeyStatistics();
		base += ("; " + gui.getSimpleName());
		return base;
	}
}
