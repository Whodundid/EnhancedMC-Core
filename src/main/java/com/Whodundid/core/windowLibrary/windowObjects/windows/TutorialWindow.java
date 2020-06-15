package com.Whodundid.core.windowLibrary.windowObjects.windows;

import com.Whodundid.core.EnhancedMC;
import com.Whodundid.core.app.AppType;
import com.Whodundid.core.app.RegisteredApps;
import com.Whodundid.core.app.window.AppInfoWindow;
import com.Whodundid.core.coreApp.CoreApp;
import com.Whodundid.core.coreApp.EMCResources;
import com.Whodundid.core.coreApp.window.CoreAppSettingsWindow;
import com.Whodundid.core.renderer.EnhancedMCRenderer;
import com.Whodundid.core.renderer.renderUtil.RendererRCM;
import com.Whodundid.core.terminal.window.ETerminal;
import com.Whodundid.core.util.renderUtil.CenterType;
import com.Whodundid.core.util.renderUtil.EColors;
import com.Whodundid.core.util.renderUtil.ScreenLocation;
import com.Whodundid.core.util.storageUtil.EDimension;
import com.Whodundid.core.util.storageUtil.StorageBoxHolder;
import com.Whodundid.core.windowLibrary.windowObjects.actionObjects.WindowButton;
import com.Whodundid.core.windowLibrary.windowObjects.advancedObjects.screenHandler.ScreenHandler;
import com.Whodundid.core.windowLibrary.windowObjects.advancedObjects.screenHandler.WindowScreen;
import com.Whodundid.core.windowLibrary.windowObjects.basicObjects.WindowTextBox;
import com.Whodundid.core.windowLibrary.windowTypes.OverlayWindow;
import com.Whodundid.core.windowLibrary.windowTypes.interfaces.IActionObject;
import com.Whodundid.core.windowLibrary.windowTypes.interfaces.IWindowObject;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraftforge.client.GuiIngameForge;
import org.lwjgl.input.Keyboard;

public class TutorialWindow extends OverlayWindow {
	
	private WindowButton close, gotIt, next, back;
	private WindowButton enable, info, settings;
	private WindowButton terminal, rcmTerm;
	private ScreenHandler screenHandler;
	private WindowScreen s1, s2, s3, s4, s5;
	private AppInfoWindow infoWindow = null;
	private CoreAppSettingsWindow appSettings = null;
	private RendererRCM renderRCM = null;
	private ETerminal term = null;
	private boolean preTerm;
	private boolean updatedSetting = false;
	
	private int lastScreen = 0;
	private int lastStage = 0;
	
	public TutorialWindow() {
		super();
		aliases.add("tutorial", "tut");
	}
	
	@Override
	public void initWindow() {
		EnhancedMCRenderer.getInstance().hideUnpinnedObjects();
		
		//disable crosshairs
		if (mc.ingameGUI instanceof GuiIngameForge) {
			((GuiIngameForge) mc.ingameGUI).renderCrosshairs = false;
		}
		
		preTerm = CoreApp.enableTerminal.get();
		
		CoreApp.enableTerminal.set(false);
	}
	
	@Override
	public void initObjects() {
		ScaledResolution res = new ScaledResolution(mc);
		
		int y = 0;
		int h = res.getScaledHeight();
		
		setDimensions(res.getScaledWidth(), h);
		
		//no header
		
		//all screens
		close = new WindowButton(this, midX - 140, endY - 60, 80, 20, "Close");
		back = new WindowButton(this, midX - 40, endY - 60, 80, 20, "Back");
		next = new WindowButton(this, midX + 60, endY - 60, 80, 20, "Next");
		
		//screen 2
		enable = new WindowButton(this, 0, 0, 0, 0, "Disabled").setStringColor(EColors.lred);
		info = new WindowButton(this, 0, 0, 0, 0).setTextures(EMCResources.guiInfo, EMCResources.guiInfoSel);
		settings = new WindowButton(this, 0, 0, 0, 0, AppType.getAppName(AppType.CORE));
		
		//screen 4
		terminal = new WindowButton(this, 0, 0, 0, 0).setTextures(EMCResources.terminalButton, EMCResources.terminalButtonSel);
		//enableTerm = new EGuiButton(this, 0, 0, 0, 0, "False").setDisplayStringColor(EColors.lred);
		rcmTerm = new WindowButton(this, 0, 0, 0, 0) {
			@Override
			public void drawObject(int mXIn, int mYIn) {
				if (isMouseInside(mX, mY)) {
					drawRect(startX + textOffset - 1, startY, endX, endY + 1, 0x99adadad);
				}
			}
		};
		
		//screen 5
		gotIt = new WindowButton(this, midX + 60, endY - 60, 80, 20, "Got It!");
		
		rcmTerm.setDrawTextures(false);
		rcmTerm.setDisplayStringOffset(22);
		
		//screens
		createScreen1();
		createScreen2();
		createScreen3();
		createScreen4();
		createScreen5();
		
		screenHandler = new ScreenHandler(this, s1, s2, s3, s4, s5);
		
		setVisible(false, back);
		setVisible(false, enable, info, settings);
		setVisible(false, terminal, rcmTerm);
		setVisible(false, gotIt);
		
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
	public void preReInit() {
		lastScreen = screenHandler.getCurrentScreenNum();
		lastStage = screenHandler.getCurrentStage();
	}
	
	@Override
	public void postReInit() {
		screenHandler.setCurrentScreen(lastScreen, lastStage);
	}
	
	@Override
	public void drawObject(int mXIn, int mYIn) {
		screenHandler.drawCurrentScreen(mXIn, mYIn);
		super.drawObject(mXIn, mYIn);
	}
	
	@Override
	public void actionPerformed(IActionObject object, Object... args) {
		if (object == close || object == gotIt) { EnhancedMC.getRenderer().revealHiddenObjects(); close(); }
		if (object == next) { screenHandler.handleNext(); }
		if (object == back) { screenHandler.handlePrevious(); }
		
		if (object == screenHandler) {
			if (screenHandler.atBeginning()) { back.setVisible(false); }
			else if (screenHandler.atEnd()) { gotIt.setVisible(true); next.setVisible(false); }
			else { back.setVisible(true); gotIt.setVisible(false); next.setVisible(true); }
		}
		
		//screen 2
		if (object == enable) {
			boolean val = enable.getString().equals("Disabled");
			enable.setString(val ? "Enabled" : "Disabled").setStringColor(val ? EColors.green : EColors.lred);
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
	}
	
	@Override
	public void mousePressed(int mXIn, int mYIn, int button) {
		super.mousePressed(mXIn, mYIn, button);
		
		if (button == 1) {
			if (screenHandler.getCurrentScreenNum() == 2 && screenHandler.getCurrentStage() == 1) {
				if (renderRCM != null) { renderRCM.close(); }
				renderRCM = new RendererRCM();
				EnhancedMC.displayWindow(renderRCM, CenterType.cursorCorner);
				
				renderRCM.setMoveable(false);
				renderRCM.setEntiretyClickable(false);
				renderRCM.setAlwaysOnTop(true);
				renderRCM.setDontCloseOnPress(true);
			}
		}
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
				drawRect(startX, startY, endX, endY + 6, 0x77121212);
				
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
				lines.add("This brief tutorial will aim to cover the basics.", EColors.orange.intVal);
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
				
				setVisible(true, enable, info, settings);
				setClickable(false, enable, info, settings);
				
				enable.setString("Disabled").setStringColor(EColors.lred);
				
				onStageChanged();
			}
			
			@Override
			public void onUnloaded() {
				setVisible(false, enable, info, settings);
				
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
						infoWindow.getInfoArea().setEntiretyClickable(true);
						infoWindow.getDependenciesArea().setEntiretyClickable(true);
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
						appSettings = new CoreAppSettingsWindow();
						
						appSettings.setPosition(midX - 275, midY - 126);
						addObject(appSettings);
						
						appSettings.setMoveable(false);
						appSettings.setEntiretyClickable(false);
						appSettings.setAlwaysOnTop(true);
						appSettings.getHeader().setAlwaysDrawFocused(true);
						appSettings.getHeader().closeButton.setClickable(false);
						appSettings.list.getVScrollBar().setClickable(true);
					}
					break;
				}
			}
			
			@Override
			public void drawScreen(int mXIn, int mYIn) {
				drawRect(startX, startY, endX, endY + 6, 0x77121212);
				
				//draw settings example window
				drawTexture(midX - 275, midY - 145, 220, 274, EMCResources.tutSettings);
				
				StorageBoxHolder<String, Integer> lines;
				boolean draw = EnhancedMC.updateCounter / 30 % 2 == 0;
				
				switch (getCurrentStage()) {
				case 0:
					lines = new StorageBoxHolder();
					
					lines.add("EnhancedMC is a window-based mod", EColors.cyan.intVal);
					lines.add("platform in which EMC Apps run.", EColors.cyan.intVal);
					lines.add();
					lines.add("Registered apps will appear within", EColors.orange.intVal);
					lines.add("the main 'EMC Settings' window as shown.", EColors.orange.intVal);
					
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
					
					lines.clear();
					lines.add("Disabled apps are effectively disconnected from", EColors.lgreen.intVal);
					lines.add("Minecraft because their event hooks are blocked.", EColors.lgreen.intVal);
					
					WindowTextBox.drawBox(midX + 130, midY + 10, lines);
					
					if (draw) {
						if (enable.getString().equals("Disabled")) {
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
					lines.add("Each app can be reloaded to refresh its", EColors.cyan.intVal);
					lines.add("config values or to perform a soft reset.", EColors.cyan.intVal);
					lines.add();
					lines.add("If an app requires certain dependencies to function,", EColors.orange.intVal);
					lines.add("each dependency will be shown in its dependency list.", EColors.orange.intVal);
					
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
					lines.add("hover your cursor over the setting itself.", EColors.orange.intVal);
					
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
				drawRect(startX, startY + 4, endX, endY + 6, 0x77121212);
				StorageBoxHolder<String, Integer> lines;
				
				switch (getCurrentStage()) {
				case 0:
					boolean draw = EnhancedMC.updateCounter / 20 % 2 == 0;
					int borderColor = 0x88ff0000;
					
					if (draw) {
						drawRect(0, 0, 2, res.getScaledHeight(), borderColor); //left
						drawRect(2, 0, res.getScaledWidth() - 2, 3, borderColor); //top
						drawRect(res.getScaledWidth() - 2, 0, res.getScaledWidth(), res.getScaledHeight(), borderColor); //right
						drawRect(2, res.getScaledHeight() - 3, res.getScaledWidth() - 2, res.getScaledHeight(), borderColor); //bottom
					}
					
					lines = new StorageBoxHolder();
					lines.add("All windows in EMC are drawn onto a hud overlay.", EColors.cyan.intVal);
					lines.add();
					lines.add("If enabled in the 'EnhancedMC Core' settings", EColors.orange.intVal);
					lines.add("window, a red border will be drawn to", EColors.orange.intVal);
					lines.add("indicate when the hud is open.", EColors.orange.intVal);
					
					WindowTextBox.drawBox(midX, midY - 30, lines);
					
					lines = new StorageBoxHolder();
					lines.add("Note: Windows and other EMC objects can", EColors.lgreen.intVal);
					lines.add("only be interacted with while the hud is open.", EColors.lgreen.intVal);
					
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
				enable.setDimensions(midX - 243, midY - 56, 53, 20);
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
				
				setVisible(false, terminal, rcmTerm);
				setClickable(false, terminal, rcmTerm);
			}
			
			@Override
			public void onStageChanged() {
				switch (getCurrentStage()) {
				case 0:
					setVisible(false, enable, terminal, rcmTerm);
					setClickable(false, terminal, rcmTerm);
					
					if (renderRCM != null) { renderRCM.close(); renderRCM = null; }
					if (appSettings != null) { appSettings.close(); appSettings = null; }
					
					if (appSettings == null) {
						appSettings = new CoreAppSettingsWindow() {
							@Override
							public void drawObject(int mXIn, int mYIn) {
								super.drawObject(mXIn, mYIn);
								
								boolean draw = EnhancedMC.updateCounter / 30 % 2 != 0;
								EDimension ld = list.getDimensions();
								
								scissor(ld.startX + 1, ld.startY + 1, ld.endX - 1, ld.endY - 1);
								if (draw && !CoreApp.enableTerminal.get()) {
									drawHRect(showTerminal.startX - 2, showTerminal.startY - 2, showTerminal.endX + 2, showTerminal.endY + 2, 1, EColors.yellow);
								}
								endScissor();
							}
							
							@Override public void sendArgs(Object... args) {}
							
						};
						
						addObject(appSettings);
						
						appSettings.setPosition(midX - 275, midY - 126);
						appSettings.setDontReloadWindows(true);
						
						appSettings.setMoveable(false);
						appSettings.setEntiretyClickable(false);
						appSettings.getHeader().setAlwaysDrawFocused(true);
						appSettings.getHeader().closeButton.setClickable(false);
						appSettings.scrollToBottom();
						appSettings.list.getVScrollBar().setClickable(true);
						
						appSettings.showTerminal.setClickable(true);
					}
					
					break;
				case 1:
					CoreApp.enableTerminal.set(true);
					
					setVisible(true, enable, terminal, rcmTerm);
					setClickable(true, terminal, rcmTerm);
					
					if (appSettings != null) { appSettings.close(); appSettings = null; }
					if (renderRCM != null) { renderRCM.close(); renderRCM = null; }
					if (term != null) { term.close(); term = null; }
					
					if (renderRCM == null) {
						renderRCM = new RendererRCM() {
							@Override
							public void drawObject(int mXIn, int mYIn) {
								super.drawObject(mXIn, mYIn);
								
								boolean draw = EnhancedMC.updateCounter / 30 % 2 != 0;
								if (draw) {
									WindowButton theButton = null;
									for (IWindowObject o : windowObjects) {
										if (o instanceof WindowButton) {
											if (((WindowButton) o).getString().equals("New Terminal")) { theButton = (WindowButton) o; break; }
										}
									}
									
									if (theButton != null) {
										drawHRect(theButton.startX - 2, theButton.startY - 2, theButton.endX + 2, theButton.endY + 2, 1, EColors.yellow);
									}
								}
							}
							
							@Override public void sendArgs(Object... args) {}
							
						};
						
						addObject(renderRCM);
						
						renderRCM.setPosition(midX + 155, midY - 70);
						
						renderRCM.setMoveable(false);
						renderRCM.setEntiretyClickable(false);
						renderRCM.setDontCloseOnPress(true);
						
						renderRCM.sendToBack();
					}
					break;
				case 2:
					setVisible(false, enable, terminal, rcmTerm);
					setClickable(false, terminal, rcmTerm);
					
					if (term != null) { term.close(); term = null; }
					if (appSettings != null) { appSettings.close(); appSettings = null; }
					if (renderRCM != null) { renderRCM.close(); renderRCM = null; }
					
					if (term == null) {
						term = new ETerminal() {
							@Override public void sendArgs(Object... args) {}
						};
						
						addObject(term);
						
						term.setPosition(midX - 350, midY - 120);
						term.resize(150, 100, ScreenLocation.botRight);
						
						term.setMoveable(false);
						term.getHeader().setAlwaysDrawFocused(true);
						term.getHeader().setEntiretyClickable(false);
						term.setResizeable(false);
						term.getInputField().setText("help");
					}
					break;
				case 3:
					if (term == null) {
						term = new ETerminal() {
							@Override public void sendArgs(Object... args) {}
						};
						
						addObject(term);
						
						term.setPosition(midX - 350, midY - 120);
						term.resize(150, 100, ScreenLocation.botRight);
						
						term.setMoveable(false);
						term.getHeader().setAlwaysDrawFocused(true);
						term.getHeader().setEntiretyClickable(false);
						term.getInputField().setText("help");
						
						term.getInputField().keyPressed('\u0000', Keyboard.KEY_RETURN);
						term.setResizeable(false);
						
						term.getInputField().setText("list -i");
						term.scrollToTop();
					}
					else if (term != null) {
						term.getInputField().keyPressed('\u0000', Keyboard.KEY_RETURN);
						term.getInputField().setText("list -i");
						
						term.getHeader().closeButton.setClickable(false);
						term.setResizeable(false);
					}
					
					if (term != null) { term.requestFocus(); }
					
					break;
				}
			}
			
			@Override
			public void drawScreen(int mXIn, int mYIn) {
				StorageBoxHolder<String, Integer> lines;
				boolean draw = EnhancedMC.updateCounter / 30 % 2 != 0;
				
				drawRect(startX, startY + 4, endX, endY + 6, 0x77121212);
				
				switch (getCurrentStage()) {
				case 0:
					lines = new StorageBoxHolder();
					lines.add("On a final note, EMC contains a terminal window", EColors.cyan.intVal);
					lines.add("that provides low-level access to all EMC settings.", EColors.cyan.intVal);
					lines.add();
					lines.add("The terminal can be enabled by toggling", EColors.orange.intVal);
					lines.add("the highlighted option.", EColors.orange.intVal);
					
					WindowTextBox.drawBox(midX + 110, midY - 30, lines);
					
					if (draw && CoreApp.enableTerminal.get()) {
						drawHRect(next.startX - 2, next.startY - 2, next.endX + 2, next.endY + 2, 1, EColors.yellow);
					}
					break;
				case 1:
					//draw settings texture
					drawTexture(midX - 375, midY - 145, 220, 274, EMCResources.tutSettings);
					drawTexture(midX - 370, midY + 104, 210, 20, EMCResources.tutHide);
					
					lines = new StorageBoxHolder();
					lines.add("To access a terminal, press one of the highlighted", EColors.cyan.intVal);
					lines.add("buttons on either the EMC Settings window", EColors.cyan.intVal);
					lines.add("or on the hud's right-click menu.", EColors.cyan.intVal);
					
					WindowTextBox.drawBox(midX, midY - 30, lines);
					
					if (draw) {
						drawHRect(midX - 277, midY + 102, midX - 253, midY + 126, 1, EColors.yellow);
					}
					break;
				case 2:
					lines = new StorageBoxHolder();
					lines.add("Terminals are a powerful interface that allow direct", EColors.cyan.intVal);
					lines.add("access to all of the 'Under-The-Hood' EMC settings.", EColors.cyan.intVal);
					lines.add();
					lines.add("The interface itself is losely based off of the ", EColors.orange.intVal);
					lines.add("Linux Shell and offers some similar functionality.", EColors.orange.intVal);
					
					WindowTextBox.drawBox(midX + 270, midY - 30, lines);
					
					lines.clear();
					lines.add("To view a list of available terminal commands,", EColors.lgreen.intVal);
					lines.add("type 'help' into the input field as shown.", EColors.lgreen.intVal);
					
					WindowTextBox.drawBox(midX + 270, midY + 40, lines);
					
					if (draw) {
						drawHRect(next.startX - 2, next.startY - 2, next.endX + 2, next.endY + 2, 1, EColors.yellow);
					}
					break;
				case 3:
					lines = new StorageBoxHolder();
					lines.add("To view information on how a specific command", EColors.cyan.intVal);
					lines.add("is used, type 'help' followed by the", EColors.cyan.intVal);
					lines.add("command's name or one of its aliases.", EColors.cyan.intVal);
					
					WindowTextBox.drawBox(midX + 270, midY - 80, lines);
					
					lines.clear();
					lines.add("Some commands require a certain number", EColors.orange.intVal);
					lines.add("of arguments to be specified in order to run.", EColors.orange.intVal);
					
					WindowTextBox.drawBox(midX + 270, midY - 20, lines);
					
					lines.clear();
					lines.add("You can view what kind of arguments", EColors.lgreen.intVal);
					lines.add("are to be expected by either pressing", EColors.lgreen.intVal);
					lines.add("tab or adding -i to the end of a command.", EColors.lgreen.intVal);
					WindowTextBox.drawBox(midX + 270, midY + 40, lines);
					
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
				drawRect(startX, startY + 4, endX, endY + 6, 0x77121212);
				
				switch (getCurrentStage()) {
				case 0:
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
					lines.add("After three long years of development.. we've finally made it!", EColors.orange.intVal);
					lines.add();
					lines.add("If you ever need to view this tutorial again, you can always ", EColors.lgreen.intVal);
					lines.add("click the 'Tutorial' button in the 'EnhancedMC Core' settings Window.", EColors.lgreen.intVal);
					
					WindowTextBox.drawBox(midX, midY + 110, lines);
					break;
				}
			}
			
		};
	}
	
}
