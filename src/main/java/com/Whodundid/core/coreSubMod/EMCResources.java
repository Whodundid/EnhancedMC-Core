package com.Whodundid.core.coreSubMod;

import com.Whodundid.core.util.renderUtil.CursorHelper;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.input.Cursor;

//Author: Hunter Bragg

public class EMCResources {
	//textures
	//global
	public static final ResourceLocation logo;
	public static final ResourceLocation emptyPixel;
	public static final ResourceLocation important1;
	public static final ResourceLocation important2;
	public static final ResourceLocation important3;
	public static final ResourceLocation important4;
	public static final ResourceLocation important5;
	public static final ResourceLocation important6;
	public static final ResourceLocation important7;
	public static final ResourceLocation important8;
	//ascii
	public static final ResourceLocation ascii;
	public static final ResourceLocation asciiItallic;
	//enhancedGui
	public static final ResourceLocation guiMoveButton;
	public static final ResourceLocation guiMoveButtonSel;
	public static final ResourceLocation guiButtons;
	public static final ResourceLocation guiButtonBase;
	public static final ResourceLocation guiButtonSel;
	public static final ResourceLocation guiButtonLeft;
	public static final ResourceLocation guiButtonLeftSel;
	public static final ResourceLocation guiButtonRight;
	public static final ResourceLocation guiButtonRightSel;
	public static final ResourceLocation guiButtonSmall;
	public static final ResourceLocation guiButtonSmallSel;
	public static final ResourceLocation guiCloseButton;
	public static final ResourceLocation guiCloseButtonSel;
	public static final ResourceLocation guiFileUpButton;
	public static final ResourceLocation guiFileUpButtonSel;
	public static final ResourceLocation guiPinButton;
	public static final ResourceLocation guiPinButtonSel;
	public static final ResourceLocation guiPinButtonOpen;
	public static final ResourceLocation guiPinButtonOpenSel;
	public static final ResourceLocation guiSettingsButton;
	public static final ResourceLocation guiSettingsButtonSel;
	public static final ResourceLocation guiInfo;
	public static final ResourceLocation guiInfoSel;
	public static final ResourceLocation guiProblem;
	public static final ResourceLocation guiProblemSel;
	public static final ResourceLocation guiProblemOpen;
	public static final ResourceLocation guiProblemOpenSel;
	public static final ResourceLocation guiCheck;
	//default mc
	public static final ResourceLocation mcIcons;
	public static final ResourceLocation mcWidgets;
	//cursors
	public static final ResourceLocation mouseIBeam;
	public static final ResourceLocation mouseResizeNS;
	public static final ResourceLocation mouseResizeEW;
	public static final ResourceLocation mouseResizeDL;
	public static final ResourceLocation mouseResizeDR;
	public static final Cursor iBeam;
	public static final Cursor resizeNS;
	public static final Cursor resizeEW;
	public static final Cursor resizeDL;
	public static final Cursor resizeDR;
	
	//sounds
	public static final ResourceLocation buttonSound;
	
	static {
		//textures
		//global
		logo = new ResourceLocation("global", "emc_core.png");
		emptyPixel = new ResourceLocation("global", "emptyPixel.png");
		important1 = new ResourceLocation("global", "important1.png");
		important2 = new ResourceLocation("global", "important2.png");
		important3 = new ResourceLocation("global", "important3.png");
		important4 = new ResourceLocation("global", "important4.png");
		important5 = new ResourceLocation("global", "important5.png");
		important6 = new ResourceLocation("global", "important6.png");
		important7 = new ResourceLocation("global", "important7.png");
		important8 = new ResourceLocation("global", "important8.png");
		
		//ascii
		ascii = new ResourceLocation("ascii", "ascii.png");
		asciiItallic = new ResourceLocation("ascii", "ascii_itallic.png");
		
		//enhancedGui
		guiMoveButton = new ResourceLocation("enhancedgui", "gui_moveBtn.png");
		guiMoveButtonSel = new ResourceLocation("enhancedgui", "gui_moveBtnSel.png");
		guiButtons = new ResourceLocation("enhancedgui", "gui_btn.png");
		guiButtonBase = new ResourceLocation("enhancedgui", "gui_btn_base.png");
		guiButtonSel = new ResourceLocation("enhancedgui", "gui_btn_sel.png");
		guiButtonLeft = new ResourceLocation("enhancedgui", "gui_leftBtn.png");
		guiButtonLeftSel = new ResourceLocation("enhancedgui", "gui_leftBtn_sel.png");
		guiButtonRight = new ResourceLocation("enhancedgui", "gui_RightBtn.png");
		guiButtonRightSel = new ResourceLocation("enhancedgui", "gui_RightBtn_sel.png");
		guiButtonSmall = new ResourceLocation("enhancedgui", "gui_smallBtn.png");
		guiButtonSmallSel = new ResourceLocation("enhancedgui", "gui_smallBtn_sel.png");
		guiCloseButton = new ResourceLocation("enhancedgui", "gui_closeBtn.png");
		guiCloseButtonSel = new ResourceLocation("enhancedgui", "gui_closeBtn_sel.png");
		guiFileUpButton = new ResourceLocation("enhancedgui", "gui_fileUpBtn.png");
		guiFileUpButtonSel = new ResourceLocation("enhancedgui", "gui_fileUpBtn_sel.png");
		guiPinButton = new ResourceLocation("enhancedgui", "gui_pinBtn.png");
		guiPinButtonSel = new ResourceLocation("enhancedgui", "gui_pinBtn_sel.png");
		guiPinButtonOpen = new ResourceLocation("enhancedgui", "gui_pinBtn_open.png");
		guiPinButtonOpenSel = new ResourceLocation("enhancedgui", "gui_pinBtn_open_sel.png");
		guiSettingsButton = new ResourceLocation("enhancedgui", "gui_settingsBtn.png");
		guiSettingsButtonSel = new ResourceLocation("enhancedgui", "gui_settingsBtn_sel.png");
		guiInfo = new ResourceLocation("enhancedgui", "gui_info.png");
		guiInfoSel = new ResourceLocation("enhancedgui", "gui_info_sel.png");
		guiProblem = new ResourceLocation("enhancedgui", "gui_problem.png");
		guiProblemSel = new ResourceLocation("enhancedgui", "gui_problem_sel.png");
		guiProblemOpen = new ResourceLocation("enhancedgui", "gui_problem_open.png");
		guiProblemOpenSel = new ResourceLocation("enhancedgui", "gui_problem_open_sel.png");
		guiCheck = new ResourceLocation("enhancedgui", "check_mark.png");
		
		//default mc
		mcIcons = new ResourceLocation("textures/gui/icons.png");
		mcWidgets = new ResourceLocation("textures/gui/widgets.png");
		
		//cursors
		mouseIBeam = new ResourceLocation("cursor", "mouse_text_pos.png");
		mouseResizeNS = new ResourceLocation("cursor", "mouse_resize_ns.png");
		mouseResizeEW = new ResourceLocation("cursor", "mouse_resize_ew.png");
		mouseResizeDL = new ResourceLocation("cursor", "mouse_resize_dl.png");
		mouseResizeDR = new ResourceLocation("cursor", "mouse_resize_dr.png");
		iBeam = CursorHelper.createCursorFromResourceLocation(EMCResources.mouseIBeam);
		resizeNS = CursorHelper.createCursorFromResourceLocation(EMCResources.mouseResizeNS);
		resizeEW = CursorHelper.createCursorFromResourceLocation(EMCResources.mouseResizeEW);
		resizeDL = CursorHelper.createCursorFromResourceLocation(EMCResources.mouseResizeDL);
		resizeDR = CursorHelper.createCursorFromResourceLocation(EMCResources.mouseResizeDR);
		
		//sounds
		buttonSound = new ResourceLocation("gui.button.press");
	}
}
