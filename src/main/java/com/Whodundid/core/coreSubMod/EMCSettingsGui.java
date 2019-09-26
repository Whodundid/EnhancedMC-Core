package com.Whodundid.core.coreSubMod;

import com.Whodundid.core.enhancedGui.EnhancedGui;
import com.Whodundid.core.enhancedGui.guiObjects.EGuiButton;
import com.Whodundid.core.enhancedGui.interfaces.IEnhancedActionObject;
import com.Whodundid.core.subMod.RegisteredSubMods;
import com.Whodundid.core.subMod.SubModType;
import net.minecraft.client.gui.GuiScreen;

public class EMCSettingsGui extends EnhancedGui {
	
	EnhancedMCMod mod = (EnhancedMCMod) RegisteredSubMods.getMod(SubModType.CORE);
	EGuiButton menuOverride, showIncompats, useDebugKey;
	
	public EMCSettingsGui() { super(); }
	public EMCSettingsGui(int x, int y) { super(x, y); }
	public EMCSettingsGui(GuiScreen guiIn) { super(guiIn); }
	public EMCSettingsGui(int x, int y, GuiScreen guiIn) { super(x, y, guiIn); }
	
	@Override
	public void initGui() {
		setGuiName("EMC Core Mod Settings");
		super.initGui();
	}
	
	@Override
	public void initObjects() {
		menuOverride = new EGuiButton(this, startX + 11, startY + 25, 60, 20).setTrueFalseButton(true, EnhancedMCMod.emcMenuOverride);
		showIncompats = new EGuiButton(this, startX + 11, menuOverride.endY + 5, 60, 20).setTrueFalseButton(true, EnhancedMCMod.showIncompats);
		useDebugKey = new EGuiButton(this, startX + 11, showIncompats.endY + 5, 60, 20).setTrueFalseButton(true, EnhancedMCMod.useDebugKey);
		
		addObject(menuOverride, showIncompats, useDebugKey);
	}
	
	@Override
	public void drawObject(int mXIn, int mYIn, float ticks) {
		drawDefaultBackground();
		
		drawRect(startX + 1, startY + 19, endX - 1, endY - 1, -0x00cfcfcf); //grey background
		drawRect(startX, startY + 18, endX, startY + 19, 0xff000000); //top line
		
		drawCenteredStringWithShadow("Enhanced MC Settings", midX, startY + 6, 0xffbb00);
		
		drawStringWithShadow("Override Pause Menu", startX + 80, startY + 31, 0xb2b2b2);
		drawStringWithShadow("Display Incompatible Mods", startX + 80, startY + 56, 0xb2b2b2);
		drawStringWithShadow("Use Debug Key", startX + 80, startY + 81, 0xb2b2b2);
		
		super.drawObject(mXIn, mYIn, ticks);
	}
	
	@Override
	public void actionPerformed(IEnhancedActionObject object) {
		if (object == menuOverride) {
			menuOverride.toggleTrueFalse(mod.emcMenuOverride, mod, false);
		}
		if (object == showIncompats) {
			showIncompats.toggleTrueFalse(mod.showIncompats, mod, false);
		}
		if (object == useDebugKey) {
			useDebugKey.toggleTrueFalse(mod.useDebugKey, mod, false);
		}
	}
}
