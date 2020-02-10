package com.Whodundid.core.coreSubMod;

import com.Whodundid.core.EnhancedMC;
import com.Whodundid.core.enhancedGui.guiObjects.actionObjects.EGuiButton;
import com.Whodundid.core.enhancedGui.types.EnhancedGui;
import com.Whodundid.core.enhancedGui.types.interfaces.IEnhancedActionObject;
import net.minecraft.client.gui.GuiMainMenu;
import net.minecraft.client.gui.GuiMultiplayer;
import net.minecraft.client.gui.GuiOptions;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiShareToLan;
import net.minecraft.client.gui.achievement.GuiAchievements;
import net.minecraft.client.gui.achievement.GuiStats;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.resources.I18n;
import net.minecraft.realms.RealmsBridge;
import net.minecraftforge.fml.client.GuiModList;

//Author: Hunter Bragg

public class EMCPauseMenu extends EnhancedGui {

	EGuiButton backToGame, eSettings, achievements, stats, openToLan, options, modOptions, quitToMenu;
	
	@Override
	public void initGui() {
		super.initGui();
		backwardsTraverseable = false;
		enableHeader(false);
	}
	
	@Override
	public void initObjects() {
		int i = sWidth / 2;
		int j = sHeight / 4;
		
		backToGame = new EGuiButton(this, i - 100, j - 16, 200, 20, I18n.format("menu.returnToGame", new Object[0]));
		eSettings = new EGuiButton(this, i - 100, j + 8, 200, 20, "Enhanced MC Settings");
		achievements = new EGuiButton(this, i - 100, j + 32, 98, 20, I18n.format("gui.achievements", new Object[0]));
		stats = new EGuiButton(this, i + 2, j + 32, 98, 20, I18n.format("gui.stats", new Object[0]));
		openToLan = new EGuiButton(this, i - 100, j + 56, 200, 20, I18n.format("menu.shareToLan", new Object[0]));
		options = new EGuiButton(this, i - 100, j + 80, 98, 20, I18n.format("menu.options", new Object[0]));
		modOptions = new EGuiButton(this, i + 2, j + 80, 98, 20, I18n.format("fml.menu.modoptions"));
		quitToMenu = new EGuiButton(this, i - 100, j + 104, 200, 20, mc.isIntegratedServerRunning() ? I18n.format("menu.returnToMenu", new Object[0]) : I18n.format("menu.disconnect", new Object[0]));
		
		openToLan.setEnabled(mc.isSingleplayer() && !mc.getIntegratedServer().getPublic());
		
		addObject(backToGame, eSettings, achievements, stats, openToLan, options, modOptions, quitToMenu);
	}
	
	@Override
	public void drawObject(int mXIn, int mYIn, float ticks) {
		updateBeforeNextDraw(mXIn, mYIn);
		if (EnhancedMC.isDebugMode() && !mc.gameSettings.showDebugInfo) { drawDebugInfo(); }
		
		drawMenuGradient();
		drawCenteredStringWithShadow(I18n.format("menu.game", new Object[0]), res.getScaledWidth() / 2, (height / 2 - 16) / 2, 16777215);
		
		if (checkDraw()) {
			GlStateManager.pushMatrix();
			GlStateManager.enableBlend();
			GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
			guiObjects.stream().filter(o -> o.checkDraw()).forEach(o -> { GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F); o.drawObject(mX, mY, ticks); });
			
			GlStateManager.popMatrix();
		}
	}
	
	@Override
	public void actionPerformed(IEnhancedActionObject object, Object... args) {
		GuiScreen proxy = EnhancedMC.getRenderer().getProxyGuiScreen();
		if (object.equals(backToGame)) { closeGui(true); }
		if (object.equals(achievements)) { if (mc.thePlayer != null) { mc.displayGuiScreen(new GuiAchievements(proxy, mc.thePlayer.getStatFileWriter())); } }
		if (object.equals(stats)) { if (mc.thePlayer != null) { mc.displayGuiScreen(new GuiStats(proxy, mc.thePlayer.getStatFileWriter())); } }
		if (object.equals(openToLan)) { mc.displayGuiScreen(new GuiShareToLan(proxy)); }
		if (object.equals(options)) { mc.displayGuiScreen(new GuiOptions(proxy, mc.gameSettings)); }
		if (object.equals(modOptions)) { mc.displayGuiScreen(new GuiModList(proxy)); }
		if (object.equals(eSettings)) { EnhancedMC.openSettingsGui(); }
		
		if (object.equals(quitToMenu)) {
			boolean flag = mc.isIntegratedServerRunning();
            boolean flag1 = mc.func_181540_al();
			mc.theWorld.sendQuittingDisconnectingPacket();
			mc.loadWorld(null);
			if (flag) { mc.displayGuiScreen(new GuiMainMenu()); }
			else if (flag1) {
				RealmsBridge bridge = new RealmsBridge();
				bridge.switchToRealms(new GuiMainMenu());
			} else { mc.displayGuiScreen(new GuiMultiplayer(new GuiMainMenu())); }
		}
	}
}
