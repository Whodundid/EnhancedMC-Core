package com.Whodundid.core.coreSubMod;

import com.Whodundid.core.enhancedGui.EnhancedGui;
import com.Whodundid.core.enhancedGui.guiObjects.EGuiButton;
import com.Whodundid.core.enhancedGui.interfaces.IEnhancedActionObject;
import com.Whodundid.core.subMod.RegisteredSubMods;
import com.Whodundid.core.subMod.SubModType;
import net.minecraft.client.gui.GuiScreen;

public class EMCSettingsGui extends EnhancedGui {
	
	EnhancedMCMod mod = (EnhancedMCMod) RegisteredSubMods.getMod(SubModType.CORE);
	EGuiButton menuOverride;
	
	public EMCSettingsGui() { super(); }
	public EMCSettingsGui(int x, int y) { super(x, y); }
	public EMCSettingsGui(GuiScreen guiIn) { super(guiIn); }
	public EMCSettingsGui(int x, int y, GuiScreen guiIn) { super(x, y, guiIn); }
	
	@Override
	public void initGui() {
		//centerGuiWithDimensions(220, 256);
		setGuiName("EMC Core Mod Settings");
		super.initGui();
	}
	
	@Override
	public void initObjects() {
		menuOverride = new EGuiButton(this, startX + 10, startY + 25, 60, 20).setTrueFalseButton(true);
		menuOverride.updateTrueFalseDisplay(EnhancedMCMod.emcMenuOverride.get());
		
		addObject(menuOverride);
	}
	
	@Override
	public void drawObject(int mXIn, int mYIn, float ticks) {
		drawDefaultBackground();
		
		drawRect(startX + 1, startY + 19, endX - 1, endY - 1, -0x00cfcfcf); //grey background
		drawRect(startX, startY + 18, endX, startY + 19, 0xff000000); //top line
		
		drawCenteredStringWithShadow("Enhanced MC Settings", midX, startY + 6, 0xffbb00);
		
		drawStringWithShadow("Override Pause Menu", startX + 80, startY + 31, 0xb2b2b2);
		
		super.drawObject(mXIn, mYIn, ticks);
	}
	
	@Override
	public void actionPerformed(IEnhancedActionObject object) {
		if (object == menuOverride) {
			menuOverride.toggleTrueFalse(mod.emcMenuOverride, mod, false);
		}
	}
}
