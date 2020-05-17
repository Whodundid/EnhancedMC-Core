package com.Whodundid.core.enhancedGui.guiObjects.windows;

import com.Whodundid.core.EnhancedMC;
import com.Whodundid.core.app.AppType;
import com.Whodundid.core.app.RegisteredApps;
import com.Whodundid.core.app.gui.AppInfoWindow;
import com.Whodundid.core.coreApp.CoreApp;
import com.Whodundid.core.coreApp.CoreAppSettingsGui;
import com.Whodundid.core.coreApp.EMCResources;
import com.Whodundid.core.enhancedGui.StaticEGuiObject;
import com.Whodundid.core.enhancedGui.guiObjects.actionObjects.EGuiButton;
import com.Whodundid.core.enhancedGui.guiObjects.advancedObjects.screenHandler.ScreenHandler;
import com.Whodundid.core.enhancedGui.guiObjects.advancedObjects.screenHandler.WindowScreen;
import com.Whodundid.core.enhancedGui.guiObjects.basicObjects.WindowTextBox;
import com.Whodundid.core.enhancedGui.types.WindowParent;
import com.Whodundid.core.enhancedGui.types.interfaces.IEnhancedActionObject;
import com.Whodundid.core.renderer.EnhancedMCRenderer;
import com.Whodundid.core.renderer.renderUtil.RendererRCM;
import com.Whodundid.core.settings.SettingsGuiMain;
import com.Whodundid.core.terminal.gui.ETerminal;
import com.Whodundid.core.util.renderUtil.CenterType;
import com.Whodundid.core.util.renderUtil.EColors;
import com.Whodundid.core.util.renderUtil.ScreenLocation;
import com.Whodundid.core.util.storageUtil.StorageBoxHolder;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraftforge.client.GuiIngameForge;
import org.lwjgl.input.Keyboard;

public class TutorialWindow extends WindowParent {
	
	private EGuiButton close, gotIt, next, back;
	private EGuiButton enable, info, settings;
	private EGuiButton terminal, rcmTerm;
	private ScreenHandler screenHandler;
	private WindowScreen s1, s2, s3, s4, s5;
	private AppInfoWindow infoWindow = null;
	private CoreAppSettingsGui appSettings = null;
	private RendererRCM renderRCM = null;
	private ETerminal term = null;
	private boolean preTerm;
	private boolean switchRender = false;
	private boolean updatedSetting = false;
	
	@Override
	public void initGui() {
		ScaledResolution res = new ScaledResolution(mc);
		
		int y = 0;
		int h = res.getScaledHeight();
		
		setDimensions(0, y, res.getScaledWidth(), h);
		
		EnhancedMCRenderer.getInstance().removeUnpinnedObjects();
		
		//disable crosshairs
		if (mc.ingameGUI instanceof GuiIngameForge) {
			((GuiIngameForge) mc.ingameGUI).renderCrosshairs = false;
		}
		
		preTerm = CoreApp.enableTerminal.get();
		CoreApp.enableTerminal.set(false);
	}
	
	@Override
	public void initObjects() {
		//no header
		
		//all screens
		close = new EGuiButton(this, midX - 140, endY - 60, 80, 20, "Close");
		back = new EGuiButton(this, midX - 40, endY - 60, 80, 20, "Back");
		next = new EGuiButton(this, midX + 60, endY - 60, 80, 20, "Next");
		
		//screen 2
		enable = new EGuiButton(this, 0, 0, 0, 0, "Disabled").setDisplayStringColor(EColors.lred);
		info = new EGuiButton(this, 0, 0, 0, 0).setTextures(EMCResources.guiInfo, EMCResources.guiInfoSel);
		settings = new EGuiButton(this, 0, 0, 0, 0, AppType.getAppName(AppType.CORE));
		
		//screen 4
		terminal = new EGuiButton(this, 0, 0, 0, 0).setTextures(EMCResources.terminalButton, EMCResources.terminalButtonSel);
		//enableTerm = new EGuiButton(this, 0, 0, 0, 0, "False").setDisplayStringColor(EColors.lred);
		rcmTerm = new EGuiButton(this, 0, 0, 0, 0) {
			@Override
			public void drawObject(int mXIn, int mYIn) {
				if (isMouseInside(mX, mY)) {
					drawRect(startX + textOffset - 1, startY, endX, endY + 1, 0x99adadad);
				}
			}
		};
		
		//screen 5
		gotIt = new EGuiButton(this, midX + 60, endY - 60, 80, 20, "Got It!");
		
		rcmTerm.setDrawTextures(false);
		rcmTerm.setDisplayStringOffset(22);
		
		//screens
		createScreen1();
		createScreen2();
		createScreen3();
		createScreen4();
		createScreen5();
		
		screenHandler = new ScreenHandler(this, s1, s2, s3, s4, s5);
		
		StaticEGuiObject.setVisible(false, back);
		StaticEGuiObject.setVisible(false, enable, info, settings);
		StaticEGuiObject.setVisible(false, terminal, rcmTerm);
		StaticEGuiObject.setVisible(false, gotIt);
		
		addObject(close, gotIt, next, back);
		addObject(enable, info, settings);
		addObject(terminal, rcmTerm);
		
		if (!CoreApp.openedTut.get() && !updatedSetting) {
			updatedSetting = true;
			CoreApp.openedTut.set(true);
			CoreApp app = (CoreApp) RegisteredApps.getApp(AppType.CORE);
			if (app != null) { app.getConfig().saveMainConfig(); }
		}
 	}
	
	@Override
	public void drawObject(int mXIn, int mYIn) {
		if (switchRender) { super.drawObject(mXIn, mYIn); }
		screenHandler.drawCurrentScreen(mXIn, mYIn);
		if (!switchRender) { super.drawObject(mXIn, mYIn); }
	}
	
	@Override
	public void actionPerformed(IEnhancedActionObject object, Object... args) {
		if (object == close) { close(); }
		if (object == next) { screenHandler.handleNext(); }
		if (object == back) { screenHandler.handlePrevious(); }
		
		if (object == screenHandler) {
			if (screenHandler.atBeginning()) { back.setVisible(false); }
			else if (screenHandler.atEnd()) { gotIt.setVisible(true); next.setVisible(false); }
			else { back.setVisible(true); gotIt.setVisible(false); next.setVisible(true); }
		}
		
		//screen 2
		if (object == enable) {
			boolean val = enable.getDisplayString().equals("Disabled");
			enable.setDisplayString(val ? "Enabled" : "Disabled").setDisplayStringColor(val ? EColors.green : EColors.lred);
		}
		
		if (object == info) {
			if (screenHandler.getCurrentStage() == 2) { screenHandler.handleNext(); }
		}
		
		if (object == settings) {
			if (screenHandler.getCurrentStage() == 4) { screenHandler.handleNext(); }
		}
		
		//screen 4
		if (object == terminal || object == rcmTerm) {
			if (screenHandler.getCurrentStage() == 1) { screenHandler.handleNext(); }
		}
		
		//screen 5
		if (object == gotIt) {
			EnhancedMC.displayWindow(new SettingsGuiMain(), this, true, true, false, CenterType.screen);
		}
	}
	
	@Override
	public void mousePressed(int mXIn, int mYIn, int button) {
		super.mousePressed(mXIn, mYIn, button);
	}
	
	@Override
	public void close() {
		super.close();
		
		if (infoWindow != null) { infoWindow.close(); infoWindow = null; }
		if (appSettings != null) { appSettings.close(); appSettings = null; }
		if (renderRCM != null) { renderRCM.close(); renderRCM = null; }
		
		if (mc.ingameGUI instanceof GuiIngameForge) {
			((GuiIngameForge) mc.ingameGUI).renderCrosshairs = true;
		}
		
		CoreApp.enableTerminal.set(preTerm);
	}
	
	private void createScreen1() {
		s1 = new WindowScreen(1) {
			@Override
			public void drawScreen(int mXIn, int mYIn) {
				drawRect(startX + 1, startY + 4, endX - 1, endY, 0x77121212);
				
				//draw logo background fade
				for (int i = 0; i < 20; i++) {
					drawFilledEllipse(midX, midY - 70, 150 - (i * 3), 120 - (i * 2), 18, 0x20050505);
				}
				
				//draw logo
				GlStateManager.enableBlend();
				drawTexture(midX - 130, midY - 194, 250, 250, EMCResources.logo);
				
				StorageBoxHolder<String, Integer> lines = new StorageBoxHolder();
				
				lines.add("Hello and welcome to EnhancedMC!", EColors.cyan.intVal);
				lines.add();
				lines.add("This brief tutorial will aim to cover the basics of EMC.", EColors.orange.intVal);
				lines.add("You can exit this tutorial at any time by pressing either", EColors.orange.intVal);
				lines.add("the 'Close' button or the escape key.", EColors.orange.intVal);
				
				WindowTextBox.drawBox(midX, midY + 110, lines);
			}
		};
	}
	
	private void createScreen2() {
		s2 = new WindowScreen(6) {
			
			@Override
			public void onLoaded() {
				enable.setDimensions(midX - 143, midY - 56, 53, 20);
				info.setDimensions(midX - 86, midY - 100, 20, 20);
				settings.setDimensions(midX - 264, midY - 100, 110, 20);
				
				enable.setVisible(true);
				info.setVisible(true);
				settings.setVisible(true);
				
				enable.setClickable(false);
				info.setClickable(false);
				settings.setClickable(false);
				
				enable.setDisplayString("Disabled").setDisplayStringColor(EColors.lred);
				
				onStageChanged();
			}
			
			@Override
			public void onUnloaded() {
				enable.setVisible(false);
				info.setVisible(false);
				settings.setVisible(false);
				
				if (infoWindow != null) { infoWindow.close(); infoWindow = null; }
				if (appSettings != null) { appSettings.close(); appSettings = null; }
			}
			
			@Override
			public void onStageChanged() {
				switch (getCurrentStage()) {
				case 0:
					enable.setClickable(false);
					break;
				case 1:
					enable.setClickable(true);
					info.setClickable(false);
					break;
				case 2:
					enable.setClickable(false);
					info.setClickable(true);
					if (infoWindow != null) { infoWindow.close(); infoWindow = null; }
					break;
				case 3:
					info.setClickable(false);
					settings.setClickable(false);
					if (infoWindow != null) { infoWindow.close(); infoWindow = null; }
					if (infoWindow == null) {
						infoWindow = new AppInfoWindow(CoreApp.instance());
						
						addObject(infoWindow);
						
						infoWindow.setPosition(midX - 4, midY - 60);
						
						infoWindow.setMoveable(false);
						infoWindow.setEntiretyClickable(false);
						infoWindow.setAlwaysOnTop(true);
						infoWindow.getHeader().setAlwaysDrawFocused(true);
					}
					break;
				case 4:
					settings.setClickable(true);
					if (infoWindow != null) { infoWindow.close(); infoWindow = null; }
					if (appSettings != null) { appSettings.close(); appSettings = null; }
					break;
				case 5:
					if (appSettings == null) {
						appSettings = new CoreAppSettingsGui();
						
						addObject(appSettings);
						
						appSettings.setPosition(midX - 275, midY - 126);
						
						appSettings.setMoveable(false);
						appSettings.setEntiretyClickable(false);
						appSettings.setAlwaysOnTop(true);
						appSettings.getHeader().setAlwaysDrawFocused(true);
					}
					break;
				}
			}
			
			@Override
			public void drawScreen(int mXIn, int mYIn) {
				drawRect(startX + 1, startY + 4, endX - 1, endY, 0x77121212);
				
				//draw settings example window
				drawTexture(midX - 275, midY - 145, 220, 274, EMCResources.tutSettings);
				drawRect(midX - 175, midY + 104, midX - 155, midY + 124, EColors.pdgray);
				
				StorageBoxHolder<String, Integer> lines;
				boolean draw = EnhancedMC.updateCounter / 30 % 2 == 0;
				
				switch (getCurrentStage()) {
				case 0:
					lines = new StorageBoxHolder();
					
					lines.add("EnhancedMC is a window-based platform", EColors.cyan.intVal);
					lines.add("in which EMC Apps run.", EColors.cyan.intVal);
					lines.add();
					lines.add("Registered apps will appear within", EColors.orange.intVal);
					lines.add("the main EMC settings window as shown.", EColors.orange.intVal);
					
					WindowTextBox.drawBox(midX + 100, midY - 40, lines);
					
					break;
				case 1:
					lines = new StorageBoxHolder();
					
					lines.add("Each app can be individually enabled and disabled", EColors.cyan.intVal);
					lines.add("for modularity between apps and forge mods.", EColors.cyan.intVal);
					lines.add();
					lines.add("To toggle an app's enabled state,", EColors.orange.intVal);
					lines.add("press the highlighted button", EColors.orange.intVal);
					
					WindowTextBox.drawBox(midX + 130, midY - 60, lines);
					
					if (draw) {
						if (enable.getDisplayString().equals("Disabled")) {
							drawHRect(midX - 145, midY - 58, midX - 88, midY - 34, 1, EColors.yellow);
						}
						else {
							drawHRect(next.startX - 2, next.startY - 2, next.endX + 2, next.endY + 2, 1, EColors.yellow);
						}
					}
					break;
				case 2:
					info.setClickable(true);
					
					lines = new StorageBoxHolder();
					lines.add("Each loaded app also contains infomation", EColors.cyan.intVal);
					lines.add("on its version, date, and dependencies.", EColors.cyan.intVal);
					lines.add();
					lines.add("To view an app's info,", EColors.orange.intVal);
					lines.add("press the highlighted button", EColors.orange.intVal);
					
					WindowTextBox.drawBox(midX + 100, midY - 40, lines);
					
					if (draw) { drawHRect(midX - 88, midY - 102, midX - 64, midY - 78, 1, EColors.yellow); }
					break;
				case 3:
					lines = new StorageBoxHolder();
					lines.add("Each app can be reloaded to update its", EColors.cyan.intVal);
					lines.add("config values or to perform a soft reset.", EColors.cyan.intVal);
					lines.add();
					lines.add("If an app requires certain dependencies to function,", EColors.orange.intVal);
					lines.add("each dependency will be shown in the dependency list.", EColors.orange.intVal);
					
					WindowTextBox.drawBox(midX + 125, midY - 130, lines);
					
					if (draw) {
						drawHRect(next.startX - 2, next.startY - 2, next.endX + 2, next.endY + 2, 1, EColors.yellow);
					}
					break;
				case 4:
					lines = new StorageBoxHolder();
					lines.add("Some apps may have a main settings window", EColors.cyan.intVal);
					lines.add("which will give access to enabling certain features.", EColors.cyan.intVal);
					lines.add();
					lines.add("If an app has a main settings window, it can", EColors.orange.intVal);
					lines.add("be accessed by pressing the highlighted button.", EColors.orange.intVal);
					
					WindowTextBox.drawBox(midX + 125, midY - 60, lines);
					
					if (draw) { drawHRect(midX - 266, midY - 102, midX - 152, midY - 78, 1, EColors.yellow); }
					break;
				case 5:
					lines = new StorageBoxHolder();
					lines.add("Apps may contain numerous features which can be", EColors.cyan.intVal);
					lines.add("individually enabled for various personalization", EColors.cyan.intVal);
					lines.add("or performance reasons.", EColors.cyan.intVal);
					lines.add();
					lines.add("To learn more about what a specific setting does,", EColors.orange.intVal);
					lines.add("hover your cursor over the setting's text.", EColors.orange.intVal);
					
					WindowTextBox.drawBox(midX + 125, midY - 30, lines);
					break;
				}
			}
		};
	}
	
	private void createScreen3() {
		s3 = new WindowScreen(2) {
			@Override
			public void onLoaded() {
				if (renderRCM != null) { renderRCM.close(); renderRCM = null; }
				CoreApp.enableTerminal.set(false);
				onStageChanged();
			}
			
			@Override
			public void onUnloaded() {
				if (renderRCM != null) { renderRCM.close(); renderRCM = null; }
				CoreApp.enableTerminal.set(false);
			}
			
			@Override
			public void onStageChanged() {
				switch (getCurrentStage()) {
				case 0:
					if (renderRCM != null) { renderRCM.close(); renderRCM = null; }
					break;
				case 1:
					if (renderRCM != null) { renderRCM.close(); renderRCM = null; }
					if (renderRCM == null) {
						renderRCM = new RendererRCM();
						
						EnhancedMC.displayWindow(renderRCM);
						
						renderRCM.setPosition(midX - 220, midY - 70);
						
						renderRCM.setMoveable(false);
						renderRCM.setEntiretyClickable(false);
						renderRCM.setAlwaysOnTop(true);
						renderRCM.setDontCloseOnPress(true);
					}
					break;
				}
			}
			
			@Override
			public void drawScreen(int mXIn, int mYIn) {
				drawRect(startX + 1, startY + 4, endX - 1, endY, 0x77121212);
				
				if (!CoreApp.drawHudBorder.get()) {
					int borderColor = 0x88ff0000;
					drawRect(0, 0, 1, res.getScaledHeight(), borderColor); //left
					drawRect(1, 0, res.getScaledWidth() - 1, 2, borderColor); //top
					drawRect(res.getScaledWidth() - 1, 0, res.getScaledWidth(), res.getScaledHeight(), borderColor); //right
					drawRect(1, res.getScaledHeight() - 2, res.getScaledWidth() - 1, res.getScaledHeight(), borderColor); //bottom
				}
				
				StorageBoxHolder<String, Integer> lines;
				
				switch (getCurrentStage()) {
				case 0:
					drawTexture(startX + 20, midY - 45, 64, 64, EMCResources.arrow);
					
					lines = new StorageBoxHolder();
					lines.add("All windows in EMC are drawn onto a hud overlay.", EColors.cyan.intVal);
					lines.add();
					lines.add("If enabled in the 'EnhancedMC Core' settings", EColors.orange.intVal);
					lines.add("window, a red border will be drawn to", EColors.orange.intVal);
					lines.add("indicate when the hud is open.", EColors.orange.intVal);
					
					WindowTextBox.drawBox(midX, midY - 30, lines);
					
					lines = new StorageBoxHolder();
					lines.add("Note: Windows and other EMC objects can", EColors.seafoam.intVal);
					lines.add("only be interacted with while the hud is open.", EColors.seafoam.intVal);
					
					WindowTextBox.drawBox(midX, midY + 40, lines);
					
					break;
				case 1:
					lines = new StorageBoxHolder();
					lines.add("An options menu will also appear by right clicking ", EColors.cyan.intVal);
					lines.add("anywhere on the screen while the hud is open.", EColors.cyan.intVal);
					
					WindowTextBox.drawBox(midX + 90, midY - 30, lines);
					
					break;
				}
			}
		};
	}
	
	private void createScreen4() {
		s4 = new WindowScreen(4) {
			@Override
			public void onLoaded() {
				terminal.setDimensions(midX - 275, midY + 104, 20, 20);
				rcmTerm.setDimensions(midX + 156, midY - 55, 128, 20);
				
				if (appSettings != null) { appSettings.close(); appSettings = null; }
				if (term != null) { term.close(); term = null; }
				
				onStageChanged();
			}
			
			@Override
			public void onUnloaded() {
				if (appSettings != null) { appSettings.close(); appSettings = null; }
				if (term != null) { term.close(); term = null; }
				
				terminal.setVisible(false);
				rcmTerm.setVisible(false);
				
				terminal.setClickable(false);
				rcmTerm.setClickable(false);
				
				switchRender = false;
			}
			
			@Override
			public void onStageChanged() {
				switch (getCurrentStage()) {
				case 0:
					switchRender = true;
					
					terminal.setVisible(false);
					rcmTerm.setVisible(false);
					terminal.setClickable(false);
					rcmTerm.setClickable(false);
					
					if (renderRCM != null) { renderRCM.close(); renderRCM = null; }
					if (appSettings != null) { appSettings.close(); appSettings = null; }
					
					if (appSettings == null) {
						appSettings = new CoreAppSettingsGui();
						
						guiInstance.addObject(appSettings);
						
						appSettings.setPosition(midX - 275, midY - 126);
						
						appSettings.setMoveable(false);
						appSettings.setEntiretyClickable(false);
						appSettings.getHeader().setAlwaysDrawFocused(true);
						appSettings.scrollToBottom();
						appSettings.settingsList.setAllowScrolling(false);
						
						appSettings.showTerminal.setClickable(true);
					}
					
					break;
				case 1:
					switchRender = false;
					
					CoreApp.enableTerminal.set(true);
					
					terminal.setVisible(true);
					rcmTerm.setVisible(true);
					terminal.setClickable(true);
					rcmTerm.setClickable(true);
					
					if (appSettings != null) { appSettings.close(); appSettings = null; }
					if (renderRCM != null) { renderRCM.close(); renderRCM = null; }
					if (term != null) { term.close(); term = null; }
					
					if (renderRCM == null) {
						renderRCM = new RendererRCM();
						
						EnhancedMC.displayWindow(renderRCM);
						
						renderRCM.setPosition(midX + 155, midY - 70);
						
						renderRCM.setMoveable(false);
						renderRCM.setEntiretyClickable(false);
						renderRCM.setDontCloseOnPress(true);
						
						renderRCM.sendToBack();
					}
					break;
				case 2:
					terminal.setClickable(false);
					rcmTerm.setClickable(false);
					terminal.setVisible(false);
					rcmTerm.setVisible(false);
					
					if (term != null) { term.close(); term = null; }
					if (appSettings != null) { appSettings.close(); appSettings = null; }
					if (renderRCM != null) { renderRCM.close(); renderRCM = null; }
					
					if (term == null) {
						term = new ETerminal();
						
						addObject(term);
						
						term.setPosition(midX - 350, midY - 120);
						term.resize(150, 100, ScreenLocation.botRight);
						
						term.setMoveable(false);
						term.setEntiretyClickable(false);
						term.getHeader().setAlwaysDrawFocused(true);
						term.getInputField().setText("help");
					}
					break;
				case 3:
					if (term == null) {
						term = new ETerminal();
						
						addObject(term);
						
						term.setPosition(midX - 350, midY - 120);
						term.resize(150, 100, ScreenLocation.botRight);
						
						term.setMoveable(false);
						term.setEntiretyClickable(false);
						term.getHeader().setAlwaysDrawFocused(true);
						term.getInputField().setText("help");
						
						term.getInputField().keyPressed('\u0000', Keyboard.KEY_RETURN);
						term.scrollToTop();
						term.setEntiretyClickable(false);
					}
					else if (term != null) {
						term.getInputField().keyPressed('\u0000', Keyboard.KEY_RETURN);
						term.scrollToTop();
						term.setEntiretyClickable(false);
					}
					break;
				}
			}
			
			@Override
			public void drawScreen(int mXIn, int mYIn) {
				StorageBoxHolder<String, Integer> lines;
				boolean draw = EnhancedMC.updateCounter / 30 % 2 != 0;
				
				switch (getCurrentStage()) {
				case 0:
					//draw illusion background
					drawRect(startX + 1, startY + 4, midX - 275, endY, 0x77121212);
					drawRect(midX - 275, startY + 4, midX - 55, midY - 145, 0x77121212);
					drawRect(midX - 275, midY + 129, midX - 140, endY, 0x77121212);
					drawRect(midX - 140, midY + 129, midX - 55, midY + 195, 0x77121212);
					drawRect(midX - 55, startY + 4, endX - 1, midY + 195, 0x77121212); //top right
					drawRect(midX - 140, midY + 215, midX - 60, endY, 0x77121212); //inner lower 1
					drawRect(midX - 60, midY + 195, midX - 40, endY, 0x77121212); //inner mid 1
					drawRect(midX - 40, midY + 215, midX + 40, endY, 0x77121212); //inner lower 2
					drawRect(midX + 40, midY + 195, midX + 60, endY, 0x77121212); //inner mid 2
					drawRect(midX + 60, midY + 215, midX + 140, endY, 0x77121212); //inner lower 2
					drawRect(midX + 140, midY + 195, endX - 1, endY, 0x77121212); //bot right
					
					lines = new StorageBoxHolder();
					lines.add("On a final note, EMC contains a terminal window", EColors.cyan.intVal);
					lines.add("that provides low-level access to all EMC settings.", EColors.cyan.intVal);
					lines.add();
					lines.add("The terminal can be enabled by toggling the", EColors.orange.intVal);
					lines.add("highlighted option.", EColors.orange.intVal);
					
					WindowTextBox.drawBox(midX + 110, midY - 30, lines);
					
					if (draw) {
						if (!CoreApp.enableTerminal.get()) {
							drawHRect(midX - 269, midY + 70, midX - 205, midY + 94, 1, EColors.yellow);
						}
						else {
							drawHRect(next.startX - 2, next.startY - 2, next.endX + 2, next.endY + 2, 1, EColors.yellow);
						}
					}
					break;
				case 1:
					//draw illusion background
					drawRect(startX + 1, startY + 4, midX + 155, endY, 0x77121212);
					drawRect(midX + 155, startY + 4, midX + 285, midY - 70, 0x77121212);
					drawRect(midX + 155, midY + 20, midX + 285, endY, 0x77121212);
					drawRect(midX + 285, startY + 4, endX - 1, endY, 0x77121212);
					
					//draw settings texture
					drawTexture(midX - 375, midY - 145, 220, 274, EMCResources.tutSettings);
					
					lines = new StorageBoxHolder();
					lines.add("To access an EMC terminal, either press", EColors.cyan.intVal);
					lines.add("the buttons on the EMC settings menu", EColors.cyan.intVal);
					lines.add("or on the hud options menu.", EColors.cyan.intVal);
					
					WindowTextBox.drawBox(midX, midY - 30, lines);
					
					if (draw) {
						drawHRect(midX - 277, midY + 102, midX - 253, midY + 126, 1, EColors.yellow);
						drawHRect(midX + 155, midY - 55, midX + 285, midY - 35, 1, EColors.yellow);
					}
					break;
				case 2:
					drawRect(startX + 1, startY + 4, endX - 1, endY, 0x77121212);
					
					lines = new StorageBoxHolder();
					lines.add("Terminals serve as a powerful interface allowing direct", EColors.cyan.intVal);
					lines.add("access to all of the 'Under-The-Hood' EMC settings.", EColors.cyan.intVal);
					lines.add();
					lines.add("The interface itself is losely based off of the ", EColors.orange.intVal);
					lines.add("Linux Shell and offers some similar functionality.", EColors.orange.intVal);
					
					WindowTextBox.drawBox(midX + 270, midY - 30, lines);
					
					lines = new StorageBoxHolder();
					lines.add("To view a list of available terminal commands,", EColors.seafoam.intVal);
					lines.add("type 'help' into the input field as shown.", EColors.seafoam.intVal);
					
					WindowTextBox.drawBox(midX + 270, midY + 40, lines);
					
					if (draw) {
						drawHRect(next.startX - 2, next.startY - 2, next.endX + 2, next.endY + 2, 1, EColors.yellow);
					}
					break;
				case 3:
					drawRect(startX + 1, startY + 4, endX - 1, endY, 0x77121212);
					
					lines = new StorageBoxHolder();
					lines.add("To view information on how a specific command", EColors.cyan.intVal);
					lines.add("is used, type 'help' followed by the", EColors.cyan.intVal);
					lines.add("command's name or one of its aliases.", EColors.cyan.intVal);
					
					WindowTextBox.drawBox(midX + 270, midY - 30, lines);
					
					break;
				}
			}
			
		};
	}
	
	private void createScreen5() {
		s5 = new WindowScreen(1) {
			@Override
			public void onLoaded() {
				gotIt.setVisible(true);
				next.setVisible(false);
				
				gotIt.setClickable(true);
				next.setClickable(false);
			}
			
			@Override
			public void onUnloaded() {
				gotIt.setVisible(false);
				next.setVisible(true);
				
				gotIt.setClickable(false);
				next.setClickable(true);
			}
			
			@Override
			public void onStageChanged() {
				switch (getCurrentStage()) {
				case 0:
				}
			}
			
			@Override
			public void drawScreen(int mXIn, int mYIn) {
				
				switch (getCurrentStage()) {
				case 0:
					drawRect(startX + 1, startY + 4, endX - 1, endY, 0x77121212);
					
					//draw logo background fade
					for (int i = 0; i < 30; i++) {
						drawFilledEllipse(midX, midY - 70, 150 - (i * 3), 120 - (i * 2), 18, 0x20050505);
					}
					
					//draw logo
					GlStateManager.enableBlend();
					drawTexture(midX - 130, midY - 194, 250, 250, EMCResources.logo);
					
					StorageBoxHolder<String, Integer> lines = new StorageBoxHolder();
					
					lines.add("Congratulations! You've reached the end of the tutorial!", EColors.cyan.intVal);
					lines.add();
					lines.add("After three long years of development.. we've finally made it!", EColors.seafoam.intVal);
					lines.add();
					lines.add("If you ever need to view this tutorial again, you can always ", EColors.orange.intVal);
					lines.add("find a button in the 'EnhancedMC Core' settings Window.", EColors.orange.intVal);
					
					WindowTextBox.drawBox(midX, midY + 110, lines);
					break;
				}
			}
			
		};
	}
	
}
