package com.Whodundid.core.app.gui;

import com.Whodundid.core.EnhancedMC;
import com.Whodundid.core.app.AppLoader;
import com.Whodundid.core.app.EMCApp;
import com.Whodundid.core.coreApp.EMCResources;
import com.Whodundid.core.enhancedGui.guiObjects.actionObjects.EGuiButton;
import com.Whodundid.core.enhancedGui.guiObjects.advancedObjects.textArea.EGuiTextArea;
import com.Whodundid.core.enhancedGui.guiObjects.basicObjects.EGuiImageBox;
import com.Whodundid.core.enhancedGui.guiObjects.basicObjects.EGuiLabel;
import com.Whodundid.core.enhancedGui.guiObjects.basicObjects.EGuiRect;
import com.Whodundid.core.enhancedGui.guiObjects.windows.EGuiDialogueBox;
import com.Whodundid.core.enhancedGui.guiObjects.windows.EGuiDialogueBox.DialogueBoxTypes;
import com.Whodundid.core.enhancedGui.types.WindowParent;
import com.Whodundid.core.util.renderUtil.CenterType;
import com.Whodundid.core.util.renderUtil.EColors;
import com.Whodundid.core.util.storageUtil.StorageBox;
import net.minecraft.util.EnumChatFormatting;

//Author: Hunter Bragg

public class AppInfoWindow extends WindowParent {

	protected EMCApp app;
	private EGuiImageBox logoBox;
	private EGuiRect topLine;
	private EGuiButton okButton, reload;
	private EGuiLabel modName;
	private EGuiTextArea info, dependencies;
	
	public AppInfoWindow(EMCApp modIn) {
		super();
		app = modIn;
		windowIcon = EMCResources.appinfoIcon;
	}
	
	@Override
	public void initGui() {
		setDimensions(266, 202);
		setResizeable(true);
		setPinnable(false);
		setMinDims(140, 202);
		setObjectName("App Info");
	}
	
	@Override
	public void initObjects() {
		defaultHeader(this);
		
		modName = new EGuiLabel(this, midX, startY + 5, app.getName()).enableShadow(true).setDrawCentered(true).setDisplayStringColor(0xffbb00);
		
		topLine = new EGuiRect(this, startX + 1, startY + 16, endX - 1, startY + 17, 0xff000000);
		
		int btnWidth = width / 3;
		
		reload = new EGuiButton(this, midX - btnWidth - (width / 16), endY - 26, btnWidth, 20, "Reload") {
			{ setRunActionOnPress(true); }
			@Override
			public void onPress() {
				playPressSound();
				if (AppLoader.reloadApp(app)) {
					EGuiDialogueBox success = new EGuiDialogueBox(DialogueBoxTypes.ok);
					success.setTitle("Reload Success");
					success.setTitleColor(EColors.gray.intVal);
					success.setMessage("Successfully reloaded app: " + app.getName() + "!");
					success.setMessageColor(EColors.green.intVal);
					EnhancedMC.displayWindow(success, CenterType.screen);
				}
				else {
					EGuiDialogueBox fail = new EGuiDialogueBox(DialogueBoxTypes.ok);
					fail.setTitle("Reload Fail");
					fail.setTitleColor(EColors.gray.intVal);
					fail.setMessage("Failed to reload app: " + app.getName() + "!");
					fail.setMessageColor(EColors.lred.intVal);
					EnhancedMC.displayWindow(fail, CenterType.screen);
				}
			}
		};
		
		//reload.setEnabled(app.getAppType() != AppType.CORE);
		
		okButton = new EGuiButton(this, midX + (width / 16), endY - 26, btnWidth, 20, "Ok") {
			{ setRunActionOnPress(true); }
			@Override
			public void onPress() {
				playPressSound();
				parent.close();
			}
		};
		
		logoBox = new EGuiImageBox(this, endX - 106, topLine.endY + 5, 100, 100);
		logoBox.setImages(app);
		logoBox.setUpdateInterval(app.getLogoInterval());
		logoBox.setNullText("No logo :(").setNullTextColor(EColors.gray);
		
		dependencies = new EGuiTextArea(this, startX + 6, logoBox.endY + 5, width - 12, okButton.startY - logoBox.endY - 10);
		dependencies.setDrawLineNumbers(true).setEditable(false).setResetDrawn(false);
		
		dependencies.addTextLine("EMC App Dependencies:", EColors.seafoam.intVal);
		dependencies.addTextLine();
		
		if (!app.getDependencies().getObjects().isEmpty()) {
			for (StorageBox<String, String> box : app.getDependencies()) {
				dependencies.addTextLine(box.getObject() + EnumChatFormatting.YELLOW + " (" + box.getValue() + ")", 0x55ff55);
			}
		}
		else { dependencies.addTextLine("None", 0xb2b2b2); }
		
		info = new EGuiTextArea(this, startX + 6, topLine.endY + 5, logoBox.startX - startX - 10, dependencies.startY - topLine.endY - 10);
		info.setDrawLineNumbers(false).setEditable(false).setResetDrawn(false);
		
		info.addTextLine("Author: " + EnumChatFormatting.GRAY + app.getAuthor(), EColors.orange.intVal);
		info.addTextLine("Artist: " + EnumChatFormatting.GRAY + app.getArtist(), EColors.orange.intVal);
		
		if (app.getContributors().isNotEmpty()) {
			if (app.getContributors().size() == 1) {
				info.addTextLine("Contributors: " + EnumChatFormatting.GRAY + app.getContributors().get(0), EColors.orange.intVal);
			}
			else {
				info.addTextLine("Contributors:", EColors.orange.intVal);
				for (String s : app.getContributors()) {
					info.addTextLine(EnumChatFormatting.GRAY + "  " + s);
				}
			}
		}
		
		info.addTextLine("Version: " + EnumChatFormatting.GRAY + app.getVersion(), EColors.orange.intVal);
		info.addTextLine("Version Date: " + EnumChatFormatting.GRAY + app.getVersionDate(), EColors.orange.intVal);
		
		if (app.isIncompatible()) {
			info.addTextLine();
			info.addTextLine("Incompatible Mod!", EColors.lred.intVal);
		}
		
		addObject(modName, topLine, reload, okButton);
		addObject(logoBox, dependencies, info);
	}
	
	@Override
	public void drawObject(int mXIn, int mYIn) {
		drawDefaultBackground();
		super.drawObject(mXIn, mYIn);
	}
	
}
