package com.Whodundid.core.app.window;

import com.Whodundid.core.app.EMCApp;
import com.Whodundid.core.app.RegisteredApps;
import com.Whodundid.core.renderer.EnhancedMCRenderer;
import com.Whodundid.core.util.storageUtil.EArrayList;
import com.Whodundid.core.util.storageUtil.StorageBox;
import com.Whodundid.core.util.storageUtil.StorageBoxHolder;
import com.Whodundid.core.windowLibrary.windowObjects.actionObjects.WindowButton;
import com.Whodundid.core.windowLibrary.windowObjects.advancedObjects.header.WindowHeader;
import com.Whodundid.core.windowLibrary.windowObjects.advancedObjects.textArea.WindowTextArea;
import com.Whodundid.core.windowLibrary.windowObjects.basicObjects.WindowLabel;
import com.Whodundid.core.windowLibrary.windowObjects.basicObjects.WindowRect;
import com.Whodundid.core.windowLibrary.windowObjects.windows.WindowDialogueBox;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.MathHelper;

//Author: Hunter Bragg

public class IncompatibleWindowList extends WindowDialogueBox {
	
	WindowButton okButton;
	WindowLabel problem;
	WindowRect topLine;
	WindowTextArea incompatibleList;
	int longest = 0;
	
	public IncompatibleWindowList() {
		init(EnhancedMCRenderer.getInstance());
		centerObjectWithSize(272, 200);
		mInit();
	}
	
	public IncompatibleWindowList(int xPos, int yPos, int width, int height) {
		init(EnhancedMCRenderer.getInstance(), xPos, yPos, width, height);
		mInit();
	}
	
	private void mInit() {
		requestFocus();
		getTopParent().setFocusLockObject(this);
		setResizeable(true);
		setMinDims(160, 101);
		setMaximizable(true);
		setPinnable(false);
	}
	
	@Override
	public void initObjects() {
		setHeader(new WindowHeader(this));
		
		int bW = MathHelper.clamp_int(width / 3, 0, 200);
		
		okButton = new WindowButton(this, midX - bW / 2, endY - 27, bW, 20, "Ok") {
			@Override
			public void onPress() {
				playPressSound();
				parent.close();
			}
		};
		okButton.setRunActionOnPress(true);
		addObject(okButton.setZLevel(1));
		
		problem = new WindowLabel(this, midX, startY + 6, "The following EMC Apps are incompatible.");
		problem.enableWordWrap(true, width - 15).setLineGapHeight(3).enableShadow(true).setDrawCentered(true).setColor(0xffbb00);
		
		topLine = new WindowRect(this, startX + 1, problem.startY + problem.getTextHeight() + 2, endX - 1, problem.startY + problem.getTextHeight() + 3, 0xff000000);
		addObject(topLine);
		
		incompatibleList = new WindowTextArea(this, startX + 7, topLine.endY + 6, width - 14, okButton.startY - topLine.endY - 12).setDrawLineNumbers(true).setEditable(false);
		addObject(incompatibleList);
		
		EArrayList<EMCApp> mods = RegisteredApps.getIncompatibleAppList();
		if (mods.isNotEmpty()) {
			for (EMCApp s : mods) {
				StorageBoxHolder<EMCApp, String> incompatMods = RegisteredApps.getAppImcompatibility(s);
				
				incompatibleList.addTextLine(EnumChatFormatting.GRAY + s.getName() + " " + s.getVersion() + ":");
				
				for (StorageBox<EMCApp, String> box : incompatMods) {
					EMCApp m = box.getObject();
					incompatibleList.addTextLine("   - " + EnumChatFormatting.RED + "requires " + EnumChatFormatting.YELLOW + 
												 m.getName() + EnumChatFormatting.RED + " version '" + box.getValue() + "'", 0xb2b2b2);
				}
				
				if (mods.indexOf(s) < mods.size() - 1) { incompatibleList.addTextLine(); }
			}
		} else { incompatibleList.addTextLine("None", 0xb2b2b2); }
		
		addObject(problem);
		
		setTitleColor(0xc2c2c2);
		setTitle("App Incompatibility");
	}
}
