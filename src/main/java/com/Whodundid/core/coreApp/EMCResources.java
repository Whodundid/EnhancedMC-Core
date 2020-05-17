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
	public static EResource logo = new EResource(EnhancedMC.MODID, "global/logo.png");
	public static EResource arrow = new EResource(EnhancedMC.MODID, "global/arrow.png");
	public static EResource tutSettings = new EResource(EnhancedMC.MODID, "global/tut_settings.png");
	public static EResource important1 = new EResource(EnhancedMC.MODID, "global/important1.png");
	public static EResource important2 = new EResource(EnhancedMC.MODID, "global/important2.png");
	public static EResource important3 = new EResource(EnhancedMC.MODID, "global/important3.png");
	public static EResource important4 = new EResource(EnhancedMC.MODID, "global/important4.png");
	public static EResource important5 = new EResource(EnhancedMC.MODID, "global/important5.png");
	public static EResource important6 = new EResource(EnhancedMC.MODID, "global/important6.png");
	public static EResource important7 = new EResource(EnhancedMC.MODID, "global/important7.png");
	public static EResource important8 = new EResource(EnhancedMC.MODID, "global/important8.png");
	//enhancedGui
	public static EResource guiMoveButton = new EResource(EnhancedMC.MODID, "window/move.png");
	public static EResource guiMoveButtonSel = new EResource(EnhancedMC.MODID, "window/move_sel.png");
	public static EResource guiButtonBase = new EResource(EnhancedMC.MODID, "window/btn.png");
	public static EResource guiButtonSel = new EResource(EnhancedMC.MODID, "window/btn_sel.png");
	public static EResource guiButtonLeft = new EResource(EnhancedMC.MODID, "window/left.png");
	public static EResource guiButtonLeftSel = new EResource(EnhancedMC.MODID, "window/left_sel.png");
	public static EResource guiButtonRight = new EResource(EnhancedMC.MODID, "window/right.png");
	public static EResource guiButtonRightSel = new EResource(EnhancedMC.MODID, "window/right_sel.png");
	public static EResource guiCloseButton = new EResource(EnhancedMC.MODID, "window/close.png");
	public static EResource guiCloseButtonSel = new EResource(EnhancedMC.MODID, "window/close_sel.png");
	public static EResource guiFileUpButton = new EResource(EnhancedMC.MODID, "window/fileup.png");
	public static EResource guiFileUpButtonSel = new EResource(EnhancedMC.MODID, "window/fileup_sel.png");
	public static EResource guiPinButton = new EResource(EnhancedMC.MODID, "window/pin.png");
	public static EResource guiPinButtonSel = new EResource(EnhancedMC.MODID, "window/pin_sel.png");
	public static EResource guiPinButtonOpen = new EResource(EnhancedMC.MODID, "window/pin_open.png");
	public static EResource guiPinButtonOpenSel = new EResource(EnhancedMC.MODID, "window/pin_open_sel.png");
	public static EResource guiSettingsButton = new EResource(EnhancedMC.MODID, "window/settings.png");
	public static EResource guiSettingsButtonSel = new EResource(EnhancedMC.MODID, "window/settings_sel.png");
	public static EResource guiInfo = new EResource(EnhancedMC.MODID, "window/info.png");
	public static EResource guiInfoSel = new EResource(EnhancedMC.MODID, "window/info_sel.png");
	public static EResource guiProblem = new EResource(EnhancedMC.MODID, "window/problem.png");
	public static EResource guiProblemSel = new EResource(EnhancedMC.MODID, "window/problem_sel.png");
	public static EResource guiProblemOpen = new EResource(EnhancedMC.MODID, "window/problem_open.png");
	public static EResource guiProblemOpenSel = new EResource(EnhancedMC.MODID, "window/problem_open_sel.png");
	public static EResource guiCheck = new EResource(EnhancedMC.MODID, "window/checkmark.png");
	public static EResource guiMaxButton = new EResource(EnhancedMC.MODID, "window/max.png");
	public static EResource guiMaxButtonSel = new EResource(EnhancedMC.MODID, "window/max_sel.png");
	public static EResource guiMinButton = new EResource(EnhancedMC.MODID, "window/min.png");
	public static EResource guiMinButtonSel = new EResource(EnhancedMC.MODID, "window/min_sel.png");
	public static EResource terminalButton = new EResource(EnhancedMC.MODID, "window/terminal.png");
	public static EResource terminalButtonSel = new EResource(EnhancedMC.MODID, "window/terminal_sel.png");
	public static EResource plusButton = new EResource(EnhancedMC.MODID, "window/plus.png");
	public static EResource plusButtonSel = new EResource(EnhancedMC.MODID, "window/plus_sel.png");
	public static EResource guiX = new EResource(EnhancedMC.MODID, "window/x_image.png");
	public static EResource refresh = new EResource(EnhancedMC.MODID, "window/refresh.png");
	public static EResource refreshSel = new EResource(EnhancedMC.MODID, "window/refresh_sel.png");
	//taskbar icons
	public static EResource emcSettingsIcon = new EResource(EnhancedMC.MODID, "taskbar/emc_settings.png");
	public static EResource settingsIcon = new EResource(EnhancedMC.MODID, "taskbar/settings.png");
	public static EResource terminalIcon = new EResource(EnhancedMC.MODID, "taskbar/terminal.png");
	public static EResource experimentIcon = new EResource(EnhancedMC.MODID, "taskbar/experiment.png");
	public static EResource keyboardIcon = new EResource(EnhancedMC.MODID, "taskbar/keyboard.png");
	public static EResource openGuiIcon = new EResource(EnhancedMC.MODID, "taskbar/opengui.png");
	public static EResource windowIcon = new EResource(EnhancedMC.MODID, "taskbar/window.png");
	public static EResource notificationIcon = new EResource(EnhancedMC.MODID, "taskbar/notification.png");
	public static EResource textEditorIcon = new EResource(EnhancedMC.MODID, "taskbar/texteditor.png");
	public static EResource textureViewerIcon = new EResource(EnhancedMC.MODID, "taskbar/textureviewer.png");
	public static EResource appinfoIcon = new EResource(EnhancedMC.MODID, "taskbar/appinfo.png");
	//cursors
	public static EResource mouseIBeam = new EResource(EnhancedMC.MODID, "cursor/text_pos.png");
	public static EResource mouseResizeNS = new EResource(EnhancedMC.MODID, "cursor/resize_ns.png");
	public static EResource mouseResizeEW = new EResource(EnhancedMC.MODID, "cursor/resize_ew.png");
	public static EResource mouseResizeDL = new EResource(EnhancedMC.MODID, "cursor/resize_dl.png");
	public static EResource mouseResizeDR = new EResource(EnhancedMC.MODID, "cursor/resize_dr.png");
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
		important1.register();
		important2.register();
		important3.register();
		important4.register();
		important5.register();
		important6.register();
		important7.register();
		important8.register();
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
