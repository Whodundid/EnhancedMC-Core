package com.Whodundid.core.app.window;

import com.Whodundid.core.EnhancedMC;
import com.Whodundid.core.app.AppLoader;
import com.Whodundid.core.app.EMCApp;
import com.Whodundid.core.coreApp.EMCResources;
import com.Whodundid.core.util.EUtil;
import com.Whodundid.core.util.renderUtil.CenterType;
import com.Whodundid.core.util.renderUtil.EColors;
import com.Whodundid.core.util.storageUtil.EArrayList;
import com.Whodundid.core.util.storageUtil.StorageBox;
import com.Whodundid.core.windowLibrary.windowObjects.actionObjects.WindowButton;
import com.Whodundid.core.windowLibrary.windowObjects.advancedObjects.textArea.TextAreaLine;
import com.Whodundid.core.windowLibrary.windowObjects.advancedObjects.textArea.WindowTextArea;
import com.Whodundid.core.windowLibrary.windowObjects.basicObjects.WindowImageBox;
import com.Whodundid.core.windowLibrary.windowObjects.basicObjects.WindowLabel;
import com.Whodundid.core.windowLibrary.windowObjects.basicObjects.WindowRect;
import com.Whodundid.core.windowLibrary.windowObjects.windows.WindowDialogueBox;
import com.Whodundid.core.windowLibrary.windowObjects.windows.WindowDialogueBox.DialogueBoxTypes;
import com.Whodundid.core.windowLibrary.windowTypes.WindowParent;
import com.Whodundid.core.windowLibrary.windowTypes.interfaces.IActionObject;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.MathHelper;

//Author: Hunter Bragg

public class AppInfoWindow extends WindowParent {

	protected EMCApp app;
	private WindowImageBox logoBox;
	private WindowRect topLine;
	private WindowButton okButton, reloadButton;
	private WindowLabel appName;
	private WindowTextArea info, dependencies;
	
	public AppInfoWindow(EMCApp modIn) {
		super();
		app = modIn;
		windowIcon = EMCResources.appinfoIcon;
	}
	
	@Override
	public void initWindow() {
		setDimensions(275, 210);
		setResizeable(true);
		setPinnable(false);
		setMaximizable(true);
		setMinDims(140, 140);
		setObjectName("App Info");
	}
	
	@Override
	public void initObjects() {
		defaultHeader(this);
		
		appName = new WindowLabel(this, midX, startY + 5, app.getName()).enableShadow(true).setDrawCentered(true).setColor(0xffbb00);
		
		WindowRect nameBack = new WindowRect(this, startX + 1, startY + 1, endX - 1, startY + 16, EColors.pdgray);
		topLine = new WindowRect(this, startX + 1, startY + 16, endX - 1, startY + 17, 0xff000000);
		
		int btnWidth = width / 3;
		int logoWidth = (width + 26) / 3;
		
		logoWidth = MathHelper.clamp_int(logoWidth, 0, (height + 6) / 2);
		
		reloadButton = new WindowButton(this, midX + (width / 16), endY - 26, btnWidth, 20, "Rebuild");
		okButton = new WindowButton(this, midX - btnWidth - (width / 16), endY - 26, btnWidth, 20, "Ok");
		
		logoBox = new WindowImageBox(this, endX - 6 - logoWidth, topLine.endY + 5, logoWidth, logoWidth);
		logoBox.setImages(app);
		logoBox.setUpdateInterval(app.getLogoInterval());
		logoBox.setNullText("No logo :(").setNullTextColor(EColors.gray);
		
		dependencies = new WindowTextArea(this, startX + 6, logoBox.endY + 5, width - 12, okButton.startY - logoBox.endY - 10);
		dependencies.setDrawLineNumbers(true).setEditable(false).setResetDrawn(false);
		
		dependencies.addTextLine("Dependencies:", EColors.seafoam.intVal);
		dependencies.addTextLine();
		
		if (!app.getDependencies().getObjects().isEmpty()) {
			for (StorageBox<String, String> box : app.getDependencies()) {
				dependencies.addTextLine(box.getObject() + EnumChatFormatting.YELLOW + " (" + box.getValue() + ")", 0x55ff55);
			}
		}
		else { dependencies.addTextLine("None", 0xb2b2b2); }
		
		info = new WindowTextArea(this, startX + 6, topLine.endY + 5, logoBox.startX - startX - 10, dependencies.startY - topLine.endY - 10);
		info.setDrawLineNumbers(false).setEditable(false).setResetDrawn(false);
		info.setDrawLineHighlight(false);
		
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
		
		if (app.getAdditionalInfo() != null) {
			info.addTextLine(app.getAdditionalInfo());
		}
		
		if (app.isIncompatible()) {
			info.addTextLine();
			info.addTextLine("Incompatible Mod!", EColors.lred.intVal);
		}
		
		StorageBox<String, String> donation = app.getDonation();
		if (donation != null) {
			info.addTextLine();
			
			String message = donation.getObject();
			EArrayList<String> lines = EUtil.createWordWrapString(message, info.getListDimensions().width - 10);
			
			for (String s : lines) {
				info.addTextLine(s, EColors.lgray.intVal);
			}
			
			String link = donation.getValue();
			String linkColor = "" + EnumChatFormatting.AQUA + EnumChatFormatting.UNDERLINE + "Donation Link" + EnumChatFormatting.RESET;
			TextAreaLine linkLine = info.addTextLine(linkColor);
			linkLine.setLinkText("Donation Link", link, true);
		}
		
		addObject(null, nameBack, appName, topLine, reloadButton, okButton);
		addObject(null, logoBox, dependencies, info);
	}
	
	@Override
	public void drawObject(int mXIn, int mYIn) {
		drawDefaultBackground();
		super.drawObject(mXIn, mYIn);
	}
	
	@Override
	public void actionPerformed(IActionObject object, Object... args) {
		if (object == okButton) { fileUpAndClose(); }
		
		if (object == reloadButton) {
			if (AppLoader.reloadApp(app)) {
				WindowDialogueBox success = new WindowDialogueBox(DialogueBoxTypes.ok);
				success.setTitle("Reload Success");
				success.setTitleColor(EColors.gray.intVal);
				success.setMessage("Successfully rebuilt app: " + app.getName() + "!");
				success.setMessageColor(EColors.green.intVal);
				EnhancedMC.displayWindow(success, CenterType.screen);
			}
			else {
				WindowDialogueBox fail = new WindowDialogueBox(DialogueBoxTypes.ok);
				fail.setTitle("Reload Fail");
				fail.setTitleColor(EColors.gray.intVal);
				fail.setMessage("Failed to rebuild app: " + app.getName() + "!");
				fail.setMessageColor(EColors.lred.intVal);
				EnhancedMC.displayWindow(fail, CenterType.screen);
			}
		}
	}
	
	public WindowTextArea getInfoArea() { return info; }
	public WindowTextArea getDependenciesArea() { return dependencies; }
	public WindowImageBox getLogoBox() { return logoBox; }
	
}
