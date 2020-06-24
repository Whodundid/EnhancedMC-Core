package com.Whodundid.hotkeys.control.hotKeyTypes;

import com.Whodundid.core.util.chatUtil.EChatUtil;
import com.Whodundid.core.util.guiUtil.GuiOpener;
import com.Whodundid.hotkeys.control.Hotkey;
import com.Whodundid.hotkeys.control.KeyActionType;
import com.Whodundid.hotkeys.control.hotKeyUtil.KeyComboAction;
import net.minecraft.util.EnumChatFormatting;

//Last edited: 9-30-18
//First Added: 9-14-18
//Author: Hunter Bragg

public class GuiOpenerHotkey extends Hotkey {
	
	public Class gui;
	public Class[] paramTypes;
	public Object[] paramValues;
	
	public GuiOpenerHotkey(String keyNameIn, KeyComboAction keysIn, Class guiIn) { this(keyNameIn, keysIn, guiIn, false, "", null); }
	public GuiOpenerHotkey(String keyNameIn, KeyComboAction keysIn, Class guiIn, boolean builtInVal) { this(keyNameIn, keysIn, guiIn, false, "", null); }
	public GuiOpenerHotkey(String keyNameIn, KeyComboAction keysIn, Class guiIn, String descriptionIn) { this(keyNameIn, keysIn, guiIn, false, descriptionIn, null); }
	public GuiOpenerHotkey(String keyNameIn, KeyComboAction keysIn, Class guiIn, boolean builtInVal, String descriptionIn, String builtInAppTypeIn) {
		super(keyNameIn, keysIn, builtInVal, KeyActionType.GUI_OPENER, builtInAppTypeIn);
		if (descriptionIn != null && !descriptionIn.isEmpty()) { description = descriptionIn; }
		gui = guiIn;
	}
	
	public Class getGui() { return gui; }
	public String getGuiName() { return gui.getName(); }
	public String getGuiDisplayName() { return gui.getSimpleName(); }
	public GuiOpenerHotkey setGui(Class guiIn) { gui = guiIn; return this; }
	public Class[] getParamTypes() { return paramTypes; }
	public Object[] getParamValues() { return paramValues; }
	public GuiOpenerHotkey setParamTypes(Class[] typesIn) { paramTypes = typesIn; return this; }
	public GuiOpenerHotkey setParamValues(Object[] valuesIn) { paramValues = valuesIn; return this; }
	
	@Override
	public void executeHotKeyAction() {
		try {
			GuiOpener.openGui(gui, paramTypes, paramValues);
		}
		catch (Exception e) {
			e.printStackTrace();
			EChatUtil.show(EnumChatFormatting.RED + "EMC Hotkey Error!\n" +
					   	   EnumChatFormatting.RED + "Error opening gui: " + EnumChatFormatting.AQUA + (gui != null ? gui.getName() : "UnknownGui") + "\n" +
					   	   EnumChatFormatting.WHITE + e);
		}
	}
	
	@Override
	public String getHotKeyStatistics() {
		String base = super.getHotKeyStatistics();
		base += ("; " + gui.getSimpleName());
		return base;
	}
	
}
