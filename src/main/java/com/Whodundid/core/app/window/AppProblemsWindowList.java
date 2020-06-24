package com.Whodundid.core.app.window;

import com.Whodundid.core.app.EMCApp;
import com.Whodundid.core.app.RegisteredApps;
import com.Whodundid.core.util.renderUtil.EColors;
import com.Whodundid.core.util.storageUtil.EArrayList;
import com.Whodundid.core.util.storageUtil.StorageBox;
import com.Whodundid.core.util.storageUtil.StorageBoxHolder;
import com.Whodundid.core.windowLibrary.windowObjects.actionObjects.WindowButton;
import com.Whodundid.core.windowLibrary.windowObjects.advancedObjects.textArea.WindowTextArea;
import com.Whodundid.core.windowLibrary.windowObjects.basicObjects.WindowLabel;
import com.Whodundid.core.windowLibrary.windowObjects.basicObjects.WindowRect;
import com.Whodundid.core.windowLibrary.windowTypes.WindowParent;
import com.Whodundid.core.windowLibrary.windowTypes.interfaces.IActionObject;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.MathHelper;

//Author: Hunter Bragg

public class AppProblemsWindowList extends WindowParent {
	
	WindowButton okButton;
	WindowLabel problem;
	WindowRect topLine;
	WindowTextArea problemList;
	
	private int vPos, hPos;
	
	public AppProblemsWindowList() {
		super();
		setDimensions(272, 200);
		getTopParent().setFocusLockObject(this);
		setResizeable(true);
		setMinDims(160, 101);
		setMaximizable(true);
		setPinnable(false);
		setObjectName("App Problems");
	}
	
	@Override
	public void initObjects() {
		defaultHeader(this);
		
		int bW = MathHelper.clamp_int(width / 3, 0, 200);
		
		okButton = new WindowButton(this, midX - bW / 2, endY - 27, bW, 20, "Ok");
		
		problem = new WindowLabel(this, midX, startY + 6, "The following EMC Apps have problems.");
		problem.enableWordWrap(true, width - 15).setLineGapHeight(3).enableShadow(true).setDrawCentered(true).setColor(0xffbb00);
		
		topLine = new WindowRect(this, startX + 1, problem.startY + problem.getTextHeight() + 2, endX - 1, problem.startY + problem.getTextHeight() + 3, EColors.black);
		
		problemList = new WindowTextArea(this, startX + 7, topLine.endY + 6, width - 14, okButton.startY - topLine.endY - 12, false).setDrawLineNumbers(true);
		addObject(okButton, topLine, problem, problemList);
		
		buildList();
	}
	
	@Override
	public void preReInit() {
		vPos = problemList.getVScrollBar().getScrollPos();
		hPos = problemList.getHScrollBar().getScrollPos();
	}
	
	@Override
	public void postReInit() {
		problemList.getVScrollBar().setScrollBarPos(vPos);
		problemList.getHScrollBar().setScrollBarPos(hPos);
	}
	
	@Override
	public void drawObject(int mXIn, int mYIn) {
		drawDefaultBackground();
		super.drawObject(mXIn, mYIn);
	}
	
	@Override
	public void actionPerformed(IActionObject object, Object... args) {
		if (object == okButton) { fileUpAndClose(); }
	}
	
	private void buildList() {
		StorageBoxHolder<EMCApp, Throwable> broken = RegisteredApps.getBrokenAppsList();
		EArrayList<EMCApp> incompats = RegisteredApps.getIncompatibleAppsList();
		
		for (StorageBox<EMCApp, Throwable> a : broken) {
			EMCApp app = a.getObject();
			Throwable reason = a.getValue();
			
			String name = app.getClass().getName() + " ";
			String version = "";
			
			try {
				name = app.getName() + " ";
				version = app.getVersion();
			}
			catch (Exception e) { e.printStackTrace(); }
			
			problemList.addTextLine(name + version + ": " + EnumChatFormatting.RED + EnumChatFormatting.BOLD + "Failed to build!", EColors.orange.intVal);
			problemList.addTextLine("   " + reason.toString(), EColors.lgray.intVal);
			
			StackTraceElement[] trace = reason.getStackTrace();
			if (trace != null && trace.length > 0) {
				for (int i = 0; i < trace.length && i < 4; i++) {
					problemList.addTextLine("   " + trace[i], EColors.lgray.intVal);
				}
				if (trace.length > 4) { problemList.addTextLine("   ...", EColors.lgray.intVal); }
			}
		}
		
		if (broken.isNotEmpty() && incompats.isNotEmpty()) { problemList.addTextLine(); }
		
		for (EMCApp a : incompats) {
			StorageBoxHolder<EMCApp, String> incompatApps = RegisteredApps.getAppImcompatibility(a);
			
			problemList.addTextLine(EnumChatFormatting.GRAY + a.getName() + " " + a.getVersion() + ":");
			
			for (StorageBox<EMCApp, String> box : incompatApps) {
				EMCApp m = box.getObject();
				problemList.addTextLine("   - " + EnumChatFormatting.RED + "requires " + EnumChatFormatting.YELLOW + 
											 m.getName() + EnumChatFormatting.RED + " version '" + box.getValue() + "'", EColors.lgray.intVal);
			}
			
			if (incompats.indexOf(a) < incompats.size() - 1) { problemList.addTextLine(); }
		}
		
		if (broken.isEmpty() && incompats.isEmpty()) { problemList.addTextLine("None", EColors.lgray.intVal); }
	}
	
}