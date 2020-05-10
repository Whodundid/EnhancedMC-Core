package com.Whodundid.core.app.gui;

import com.Whodundid.core.EnhancedMC;
import com.Whodundid.core.app.AppLoader;
import com.Whodundid.core.app.EMCApp;
import com.Whodundid.core.coreApp.EMCResources;
import com.Whodundid.core.enhancedGui.guiObjects.actionObjects.EGuiButton;
import com.Whodundid.core.enhancedGui.guiObjects.advancedObjects.header.EGuiHeader;
import com.Whodundid.core.enhancedGui.guiObjects.advancedObjects.textArea.EGuiTextArea;
import com.Whodundid.core.enhancedGui.guiObjects.basicObjects.EGuiImageBox;
import com.Whodundid.core.enhancedGui.guiObjects.basicObjects.EGuiLabel;
import com.Whodundid.core.enhancedGui.guiObjects.basicObjects.EGuiRect;
import com.Whodundid.core.enhancedGui.guiObjects.utilityObjects.EGuiFocusLockBorder;
import com.Whodundid.core.enhancedGui.guiObjects.windows.EGuiDialogueBox;
import com.Whodundid.core.enhancedGui.types.interfaces.IEnhancedGuiObject;
import com.Whodundid.core.renderer.EnhancedMCRenderer;
import com.Whodundid.core.util.renderUtil.CenterType;
import com.Whodundid.core.util.renderUtil.EColors;
import com.Whodundid.core.util.storageUtil.EDimension;
import com.Whodundid.core.util.storageUtil.StorageBox;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.EnumChatFormatting;

//Author: Hunter Bragg

public class AppInfoDialogueBox extends EGuiDialogueBox {

	protected EMCApp app;
	EGuiImageBox logoBox;
	EGuiRect topLine;
	EGuiButton okButton, reload;
	EGuiLabel modName;
	EGuiTextArea info, dependencies;
	
	public AppInfoDialogueBox(EMCApp modIn) {
		init(EnhancedMCRenderer.getInstance());
		centerObjectWithSize(258, 202);
		mInit(modIn);
		windowIcon = EMCResources.guiInfo;
	}
	public AppInfoDialogueBox(int xPos, int yPos, int width, int height, EMCApp modIn) {
		init(EnhancedMCRenderer.getInstance(), xPos, yPos, width, height);
		mInit(modIn);
	}
	
	private void mInit(EMCApp modIn) {
		app = modIn;
		requestFocus();
		bringToFront();
		setResizeable(true);
		setMinWidth(140).setMinHeight(202);
		setObjectName("App Info");
		setPinnable(false);
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
		if (EnhancedMC.isDebugMode()) {
			int y = hasHeader() ? getHeader().startY - 9 : startY - 9;
			int pos = mc.fontRendererObj.getStringWidth("InitTime: " + EnumChatFormatting.YELLOW + initTime);
			drawRect(startX, y - 1, startX + pos + 5, y + mc.fontRendererObj.FONT_HEIGHT + 1, EColors.black);
			drawRect(startX + 1, y, startX + pos + 4, y + mc.fontRendererObj.FONT_HEIGHT, EColors.dgray);
			drawString("InitTime: " + EnumChatFormatting.YELLOW + initTime, startX + 3, y + 1, EColors.cyan);
		}
		updateBeforeNextDraw(mXIn, mYIn);
		try {
			if (checkDraw()) {
				GlStateManager.pushMatrix();
				GlStateManager.enableBlend();
				guiObjects.stream().filter(o -> o.checkDraw()).forEach(o -> {
					if (!o.hasFirstDraw()) { o.onFirstDraw(); o.onFirstDraw(); }
					
					if (!(o instanceof EGuiHeader || o instanceof EGuiFocusLockBorder)) { scissor(startX, startY, endX - 1, endY); }
					o.drawObject(mX, mY);
					if (!(o instanceof EGuiHeader || o instanceof EGuiFocusLockBorder)) { endScissor(); }
					
					IEnhancedGuiObject f = getTopParent().getFocusLockObject();
					if (f != null && o instanceof EGuiHeader && (!o.equals(f) && !f.getAllChildren().contains(o))) {
						if (o.isVisible()) {
							EDimension d = o.getDimensions();
							drawRect(d.startX, d.startY, d.endX, d.endY, 0x88000000);
						}
					}
				});
				GlStateManager.popMatrix();
			}
		}
		catch (Exception e) { e.printStackTrace(); }
	}
	
}
