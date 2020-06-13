package com.Whodundid.core.coreApp;

import com.Whodundid.core.EnhancedMC;
import com.Whodundid.core.app.AppResources;
import com.Whodundid.core.util.renderUtil.CursorHelper;
import com.Whodundid.core.util.resourceUtil.EResource;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.input.Cursor;

//Author: Hunter Bragg

public class EMCResources extends AppResources {
	//textures
	
	//global
	public static final EResource logo = new EResource(EnhancedMC.MODID, "global/logo.png");
	public static final EResource arrow = new EResource(EnhancedMC.MODID, "global/arrow.png");
	public static final EResource tutSettings = new EResource(EnhancedMC.MODID, "global/tut_settings.png");
	public static final EResource tutHide = new EResource(EnhancedMC.MODID, "global/tut_settings_hide.png");
	public static final EResource important = new EResource(EnhancedMC.MODID, "global/important.png");
	//window library
	public static final EResource guiMoveButton = new EResource(EnhancedMC.MODID, "window/move.png");
	public static final EResource guiMoveButtonSel = new EResource(EnhancedMC.MODID, "window/move_sel.png");
	public static final EResource guiButtonBase = new EResource(EnhancedMC.MODID, "window/btn.png");
	public static final EResource guiButtonSel = new EResource(EnhancedMC.MODID, "window/btn_sel.png");
	public static final EResource guiButtonLeft = new EResource(EnhancedMC.MODID, "window/left.png");
	public static final EResource guiButtonLeftSel = new EResource(EnhancedMC.MODID, "window/left_sel.png");
	public static final EResource guiButtonRight = new EResource(EnhancedMC.MODID, "window/right.png");
	public static final EResource guiButtonRightSel = new EResource(EnhancedMC.MODID, "window/right_sel.png");
	public static final EResource guiCloseButton = new EResource(EnhancedMC.MODID, "window/close.png");
	public static final EResource guiCloseButtonSel = new EResource(EnhancedMC.MODID, "window/close_sel.png");
	public static final EResource guiFileUpButton = new EResource(EnhancedMC.MODID, "window/fileup.png");
	public static final EResource guiFileUpButtonSel = new EResource(EnhancedMC.MODID, "window/fileup_sel.png");
	public static final EResource guiPinButton = new EResource(EnhancedMC.MODID, "window/pin.png");
	public static final EResource guiPinButtonSel = new EResource(EnhancedMC.MODID, "window/pin_sel.png");
	public static final EResource guiPinButtonOpen = new EResource(EnhancedMC.MODID, "window/pin_open.png");
	public static final EResource guiPinButtonOpenSel = new EResource(EnhancedMC.MODID, "window/pin_open_sel.png");
	public static final EResource guiSettingsButton = new EResource(EnhancedMC.MODID, "window/settings.png");
	public static final EResource guiSettingsButtonSel = new EResource(EnhancedMC.MODID, "window/settings_sel.png");
	public static final EResource guiInfo = new EResource(EnhancedMC.MODID, "window/info.png");
	public static final EResource guiInfoSel = new EResource(EnhancedMC.MODID, "window/info_sel.png");
	public static final EResource guiProblem = new EResource(EnhancedMC.MODID, "window/problem.png");
	public static final EResource guiProblemSel = new EResource(EnhancedMC.MODID, "window/problem_sel.png");
	public static final EResource guiProblemOpen = new EResource(EnhancedMC.MODID, "window/problem_open.png");
	public static final EResource guiProblemOpenSel = new EResource(EnhancedMC.MODID, "window/problem_open_sel.png");
	public static final EResource guiCheck = new EResource(EnhancedMC.MODID, "window/checkmark.png");
	public static final EResource guiMaxButton = new EResource(EnhancedMC.MODID, "window/max.png");
	public static final EResource guiMaxButtonSel = new EResource(EnhancedMC.MODID, "window/max_sel.png");
	public static final EResource guiMinButton = new EResource(EnhancedMC.MODID, "window/min.png");
	public static final EResource guiMinButtonSel = new EResource(EnhancedMC.MODID, "window/min_sel.png");
	public static final EResource terminalButton = new EResource(EnhancedMC.MODID, "window/terminal.png");
	public static final EResource terminalButtonSel = new EResource(EnhancedMC.MODID, "window/terminal_sel.png");
	public static final EResource plusButton = new EResource(EnhancedMC.MODID, "window/plus.png");
	public static final EResource plusButtonSel = new EResource(EnhancedMC.MODID, "window/plus_sel.png");
	public static final EResource guiX = new EResource(EnhancedMC.MODID, "window/x_image.png");
	public static final EResource refresh = new EResource(EnhancedMC.MODID, "window/refresh.png");
	public static final EResource refreshSel = new EResource(EnhancedMC.MODID, "window/refresh_sel.png");
	public static final EResource minimize = new EResource(EnhancedMC.MODID, "window/minimize.png");
	public static final EResource minimizeSel = new EResource(EnhancedMC.MODID, "window/minimize_sel.png");
	public static final EResource backButton = new EResource(EnhancedMC.MODID, "window/back.png");
	public static final EResource backButtonSel = new EResource(EnhancedMC.MODID, "window/back_sel.png");
	//taskbar icons
	public static final EResource emcSettingsIcon = new EResource(EnhancedMC.MODID, "taskbar/emc_settings.png");
	public static final EResource settingsIcon = new EResource(EnhancedMC.MODID, "taskbar/settings.png");
	public static final EResource terminalIcon = new EResource(EnhancedMC.MODID, "taskbar/terminal.png");
	public static final EResource experimentIcon = new EResource(EnhancedMC.MODID, "taskbar/experiment.png");
	public static final EResource keyboardIcon = new EResource(EnhancedMC.MODID, "taskbar/keyboard.png");
	public static final EResource openGuiIcon = new EResource(EnhancedMC.MODID, "taskbar/opengui.png");
	public static final EResource windowIcon = new EResource(EnhancedMC.MODID, "taskbar/window.png");
	public static final EResource notificationIcon = new EResource(EnhancedMC.MODID, "taskbar/notification.png");
	public static final EResource textEditorIcon = new EResource(EnhancedMC.MODID, "taskbar/texteditor.png");
	public static final EResource textureViewerIcon = new EResource(EnhancedMC.MODID, "taskbar/textureviewer.png");
	public static final EResource appinfoIcon = new EResource(EnhancedMC.MODID, "taskbar/appinfo.png");
	//cursors
	public static final EResource mouseIBeam = new EResource(EnhancedMC.MODID, "cursor/text_pos.png");
	public static final EResource mouseResizeNS = new EResource(EnhancedMC.MODID, "cursor/resize_ns.png");
	public static final EResource mouseResizeEW = new EResource(EnhancedMC.MODID, "cursor/resize_ew.png");
	public static final EResource mouseResizeDL = new EResource(EnhancedMC.MODID, "cursor/resize_dl.png");
	public static final EResource mouseResizeDR = new EResource(EnhancedMC.MODID, "cursor/resize_dr.png");
	public static Cursor cursorIBeam;
	public static Cursor cursorResizeNS;
	public static Cursor cursorResizeEW;
	public static Cursor cursorResizeDL;
	public static Cursor cursorResizeDR;
	
	//default mc
	public static ResourceLocation mcIcons = new ResourceLocation("textures/gui/icons.png");
	public static ResourceLocation mcWidgets = new ResourceLocation("textures/gui/widgets.png");
	
	//sounds
	public static ResourceLocation buttonSound = new ResourceLocation("gui.button.press");
	
	@Override
	public void registerResources() {
		//textures
		//global
		logo.register();
		arrow.register();
		tutSettings.register();
		tutHide.register();
		important.register();
		//enhancedGui
		guiMoveButton.register();
		guiMoveButtonSel.register();
		guiButtonBase.register();
		guiButtonSel.register();
		guiButtonLeft.register();
		guiButtonLeftSel.register();
		guiButtonRight.register();
		guiButtonRightSel.register();
		guiCloseButton.register();
		guiCloseButtonSel.register();
		guiFileUpButton.register();
		guiFileUpButtonSel.register();
		guiPinButton.register();
		guiPinButtonSel.register();
		guiPinButtonOpen.register();
		guiPinButtonOpenSel.register();
		guiSettingsButton.register();
		guiSettingsButtonSel.register();
		guiInfo.register();
		guiInfoSel.register();
		guiProblem.register();
		guiProblemSel.register();
		guiProblemOpen.register();
		guiProblemOpenSel.register();
		guiCheck.register();
		guiMaxButton.register();
		guiMaxButtonSel.register();
		guiMinButton.register();
		guiMinButtonSel.register();
		terminalButton.register();
		terminalButtonSel.register();
		plusButton.register();
		plusButtonSel.register();
		guiX.register();
		refresh.register();
		refreshSel.register();
		minimize.register();
		minimizeSel.register();
		backButton.register();
		backButtonSel.register();
		//taskbar icons
		emcSettingsIcon.register();
		settingsIcon.register();
		terminalIcon.register();
		experimentIcon.register();
		keyboardIcon.register();
		openGuiIcon.register();
		windowIcon.register();
		notificationIcon.register();
		textEditorIcon.register();
		textureViewerIcon.register();
		appinfoIcon.register();
		//cursors
		mouseIBeam.register();
		mouseResizeNS.register();
		mouseResizeEW.register();
		mouseResizeDL.register();
		mouseResizeDR.register();
		cursorIBeam = CursorHelper.createCursorFromEResource(mouseIBeam);
		cursorResizeNS = CursorHelper.createCursorFromEResource(mouseResizeNS);
		cursorResizeEW = CursorHelper.createCursorFromEResource(mouseResizeEW);
		cursorResizeDL = CursorHelper.createCursorFromEResource(mouseResizeDL);
		cursorResizeDR = CursorHelper.createCursorFromEResource(mouseResizeDR);
	}
	
}
