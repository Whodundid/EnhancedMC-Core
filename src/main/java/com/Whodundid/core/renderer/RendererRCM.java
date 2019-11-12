package com.Whodundid.core.renderer;

import com.Whodundid.core.EnhancedMC;
import com.Whodundid.core.enhancedGui.guiObjects.EGuiRightClickMenu;
import com.Whodundid.core.enhancedGui.types.interfaces.IEnhancedActionObject;
import com.Whodundid.core.enhancedGui.types.interfaces.IEnhancedGuiObject;
import com.Whodundid.core.events.emcEvents.ModCalloutEvent;
import com.Whodundid.core.settings.SettingsGuiMain;
import com.Whodundid.core.subMod.RegisteredSubMods;
import com.Whodundid.core.subMod.SubModType;
import com.Whodundid.core.util.renderUtil.Resources;
import net.minecraftforge.common.MinecraftForge;

public class RendererRCM extends EGuiRightClickMenu {
	
	public RendererRCM(IEnhancedGuiObject parentIn, int x, int y) {
		super(parentIn, x, y);
		
		if (!EnhancedMC.isEGuiOpen(SettingsGuiMain.class)) { addOption("Open EMC Settings", Resources.guiSettingsButton); }
		if (RegisteredSubMods.isModRegEn(SubModType.ENHANCEDCHAT)) { addOption("New Chat Window"); }
		addOption("Open Gui...");
		
		setRunActionOnPress(true);
		setActionReciever(this);
		
		setTitle("Enhanced MC");
	}
	
	@Override
	public void actionPerformed(IEnhancedActionObject object) {
		if (object == this) {
			switch ((String) getSelectedObject()) {
			case "Open EMC Settings": openSettings(); break;
			case "New Chat Window": openChatWindow(); break;
			case "Open Gui...": openGui(); break;
			}
		}
	}
	
	private void openSettings() {
		if (!EnhancedMC.isEGuiOpen(SettingsGuiMain.class)) {
			EnhancedMC.displayEGui(new SettingsGuiMain());
		}
		close();
	}
	
	private void openChatWindow() {
		MinecraftForge.EVENT_BUS.post(new ModCalloutEvent(this, "add window"));
	}
	
	private void openGui() {
		
	}
}
