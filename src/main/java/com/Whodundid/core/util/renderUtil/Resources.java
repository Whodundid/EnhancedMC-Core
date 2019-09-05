package com.Whodundid.core.util.renderUtil;

import net.minecraft.util.ResourceLocation;

//Dec 18, 2018
//Last edited: Feb 18, 2019
//Edit note: changed ArrayList -> EArrayList. Added support to find all submod guis.
//First Added: Sep 14, 2018
//Author: Hunter Bragg

public class Resources {
	//global
	public final static ResourceLocation emptyPixel;
	public final static ResourceLocation important1;
	public final static ResourceLocation important2;
	public final static ResourceLocation important3;
	public final static ResourceLocation important4;
	public final static ResourceLocation important5;
	public final static ResourceLocation important6;
	public final static ResourceLocation important7;
	public final static ResourceLocation important8;
	//ascii
	public final static ResourceLocation ascii;
	public final static ResourceLocation asciiItallic;
	//blink
	public final static ResourceLocation blinkFull;
	public final static ResourceLocation blinkUsed;
	//enhancedGui
	public final static ResourceLocation guiBase;
	public final static ResourceLocation guiMoveButton;
	public final static ResourceLocation guiMoveButtonSel;
	public final static ResourceLocation guiRCMBase;
	public final static ResourceLocation guiButtons;
	public final static ResourceLocation guiButtonBase;
	public final static ResourceLocation guiButtonSel;
	public final static ResourceLocation guiButtonLeft;
	public final static ResourceLocation guiButtonLeftSel;
	public final static ResourceLocation guiButtonRight;
	public final static ResourceLocation guiButtonRightSel;
	public final static ResourceLocation guiButtonSmall;
	public final static ResourceLocation guiButtonSmallSel;
	public final static ResourceLocation guiCloseButton;
	public final static ResourceLocation guiCloseButtonSel;
	public final static ResourceLocation guiFileUpButton;
	public final static ResourceLocation guiFileUpButtonSel;
	public final static ResourceLocation guiPinButton;
	public final static ResourceLocation guiPinButtonSel;
	public final static ResourceLocation guiPinButtonOpen;
	public final static ResourceLocation guiPinButtonOpenSel;
	public final static ResourceLocation guiSettingsButton;
	public final static ResourceLocation guiSettingsButtonSel;
	public final static ResourceLocation guiInfo;
	public final static ResourceLocation guiInfoSel;
	//cursor
	public final static ResourceLocation mouseIBeam;
	public final static ResourceLocation mouseResizeNS;
	public final static ResourceLocation mouseResizeEW;
	public final static ResourceLocation mouseResizeDL;
	public final static ResourceLocation mouseResizeDR;
	//minimap
	public final static ResourceLocation facingLine;
	//multihotbar
	public final static ResourceLocation hotbar;
	public final static ResourceLocation hotbarSelection;
	//scripts
	public final static ResourceLocation innerTaskManager;
	public final static ResourceLocation scriptSelectionBox;
	//skinlayerswitcher
	public final static ResourceLocation SLSguiTexture;
	public final static ResourceLocation SLSsettingsGuiSteve;
	public final static ResourceLocation SLSsettingsGuiAlex;
	public final static ResourceLocation SLSpartGui;
	//worldEditor
	public final static ResourceLocation editorSelectionBase;
	public final static ResourceLocation editorGrid;
	public final static ResourceLocation editorToolPics;
	public final static ResourceLocation editorPanU;
	public final static ResourceLocation editorPanG;
	public final static ResourceLocation editorOrbit;
	//default mc
	public final static ResourceLocation mcIcons;
	public final static ResourceLocation mcWidgets;
	//sounds
	public final static ResourceLocation buttonSound;
	
	static {
		//global
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
		//blink
		blinkFull = new ResourceLocation("blink", "blink_full.png");
		blinkUsed = new ResourceLocation("blink", "blink_used.png");
		//enhancedGui
		guiBase = new ResourceLocation("enhancedgui", "gui_base.png");
		guiMoveButton = new ResourceLocation("enhancedgui", "gui_moveBtn.png");
		guiMoveButtonSel = new ResourceLocation("enhancedgui", "gui_moveBtnSel.png");
		guiRCMBase = new ResourceLocation("enhancedgui", "rcm_base.png");
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
		
		//cursor
		mouseIBeam = new ResourceLocation("cursor", "mouse_text_pos.png");
		mouseResizeNS = new ResourceLocation("cursor", "mouse_resize_ns.png");
		mouseResizeEW = new ResourceLocation("cursor", "mouse_resize_ew.png");
		mouseResizeDL = new ResourceLocation("cursor", "mouse_resize_dl.png");
		mouseResizeDR = new ResourceLocation("cursor", "mouse_resize_dr.png");
		
		//minimap
		facingLine = new ResourceLocation("minimap", "miniMapFacingLine.png");
		//multihotbar
		hotbar = new ResourceLocation("multihotbar", "hotbar.png");
		hotbarSelection = new ResourceLocation("multihotbar", "hotbarSelection.png");
		//scripts
		innerTaskManager = new ResourceLocation("scripts", "ScriptTaskManagerInner.png");
		scriptSelectionBox = new ResourceLocation("scripts", "ScriptSelectionBox.png");
		//skinlayerswitcher
		SLSguiTexture = new ResourceLocation("skinlayerswitcher", "sls_options.png");
		SLSsettingsGuiSteve = new ResourceLocation("skinlayerswitcher", "sls_gui_steve.png");
		SLSsettingsGuiAlex = new ResourceLocation("skinlayerswitcher", "sls_gui_alex.png");
		SLSpartGui = new ResourceLocation("skinlayerswitcher", "sls_part_menu.png");
		//worldEditord
		editorSelectionBase = new ResourceLocation("worldeditor", "selectionBase.png");
		editorGrid = new ResourceLocation("worldeditor", "grid.png");
		editorToolPics = new ResourceLocation("worldeditor", "editorToolPics.png");
		editorPanU = new ResourceLocation("worldeditor", "editor_panU.png");
		editorPanG = new ResourceLocation("worldeditor", "editor_panG.png");
		editorOrbit = new ResourceLocation("worldeditor", "editor_orbit.png");
		//default mc
		mcIcons = new ResourceLocation("textures/gui/icons.png");
		mcWidgets = new ResourceLocation("textures/gui/widgets.png");
		//sounds
		buttonSound = new ResourceLocation("gui.button.press");
	}
}
