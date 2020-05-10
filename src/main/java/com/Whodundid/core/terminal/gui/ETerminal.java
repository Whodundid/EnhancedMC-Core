package com.Whodundid.core.terminal.gui;

import com.Whodundid.core.EnhancedMC;
import com.Whodundid.core.coreApp.CoreApp;
import com.Whodundid.core.coreApp.EMCResources;
import com.Whodundid.core.enhancedGui.guiObjects.advancedObjects.textArea.EGuiTextArea;
import com.Whodundid.core.enhancedGui.guiObjects.advancedObjects.textArea.TextAreaLine;
import com.Whodundid.core.enhancedGui.guiUtil.EObjectGroup;
import com.Whodundid.core.enhancedGui.objectEvents.EventFocus;
import com.Whodundid.core.enhancedGui.objectEvents.EventMouse;
import com.Whodundid.core.enhancedGui.objectEvents.ObjectEvent;
import com.Whodundid.core.enhancedGui.objectEvents.eventUtil.FocusType;
import com.Whodundid.core.enhancedGui.objectEvents.eventUtil.MouseType;
import com.Whodundid.core.enhancedGui.types.WindowParent;
import com.Whodundid.core.enhancedGui.types.interfaces.IEnhancedActionObject;
import com.Whodundid.core.renderer.taskView.TaskBar;
import com.Whodundid.core.terminal.TerminalCommandHandler;
import com.Whodundid.core.terminal.gui.termParts.TerminalTextField;
import com.Whodundid.core.terminal.gui.termParts.TerminalTextLine;
import com.Whodundid.core.util.EUtil;
import com.Whodundid.core.util.renderUtil.CenterType;
import com.Whodundid.core.util.renderUtil.EColors;
import com.Whodundid.core.util.renderUtil.ScreenLocation;
import com.Whodundid.core.util.storageUtil.EArrayList;
import com.Whodundid.core.util.storageUtil.EDimension;
import java.io.File;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.util.MathHelper;

//Author: Hunter Bragg

public class ETerminal extends WindowParent {
	
	TerminalTextField inputField;
	EGuiTextArea history;
	boolean init = false;
	public int historyLine = 0;
	public int lastUsed = 2;
	public String preservedInput = "";
	File dir;
	public String textTabBegin = "";
	public boolean tab1 = false;
	public int tabPos = -1;
	public int startArgPos = -1;
	public EArrayList<String> tabData = new EArrayList();
	EArrayList<TextAreaLine> tabDisplayLines = new EArrayList();
	
	public ETerminal() {
		super();
		aliases.add("terminal", "console", "term");
		dir = new File(System.getProperty("user.dir"));
		windowIcon = EMCResources.terminalIcon;
	}
	
	@Override
	public void initGui() {
		setObjectName("EMC Terminal" + (EnhancedMC.isOpMode() ? " +" : ""));
		setDimensions(300, 153);
		setMinDims(70, 32);
		setResizeable(true);
		setMaximizable(true);
		//setPinnable(true);
	}
	
	@Override
	public void initObjects() {
		defaultHeader(this);
		
		inputField = new TerminalTextField(this, startX + 2, endY - 14, width - 6, 12);
		history = new EGuiTextArea(this, startX + 1, startY + 1, width - 2, height - 3 - inputField.height) {
			
			@Override
			public void mousePressed(int mXIn, int mYIn, int button) {
				super.mousePressed(mXIn, mYIn, button);
				EUtil.ifNotNullDo(getWindowParent(), w -> w.bringToFront());
				if (button == 1) {
					guiInstance.bringToFront();
					guiInstance.mousePressed(mXIn, mYIn, button);
					//EnhancedMC.displayWindow(new TerminalRCM((ETerminal) guiInstance), CenterType.cursorCorner);
				}
				else {
					inputField.requestFocus();
				}
			}
		};
		
		inputField.setBackgroundColor(0xff000000);
		inputField.setBorderColor(0xff222222);
		inputField.setTextColor(0x00ff00);
		inputField.setMaxStringLength(255);
		
		history.setBackgroundColor(0xff000000);
		history.setBorderColor(0xff222222);
		history.setDrawLineNumbers(true);
		history.setResetDrawn(false);
		
		objectGroup = new EObjectGroup(this);
		objectGroup.addObject(header, history, inputField);
		header.setObjectGroup(objectGroup);
		history.setObjectGroup(objectGroup);
		inputField.setObjectGroup(objectGroup);
		
		addObject(inputField, history);
		
		if (!init) {
			history.addTextLine("EMC " + (EnhancedMC.isOpMode() ? "Op " : "") + "Terminal v1.0 initialized..", 0xffff00);
			history.addTextLine();
			//info("> Current Dir: " + EnumChatFormatting.AQUA + EnumChatFormatting.UNDERLINE + dir);
			init = true;
		}
		
		if (getTopParent().getModifyingObject() != this) { inputField.requestFocus(); }
	}
	
	@Override
	public void drawObject(int mXIn, int mYIn) {
		if (header != null) { header.setTitleColor(-EColors.rainbow() + 0xff222222); }
		drawRect(startX, startY, endX, endY, 0xff000000);
		history.setBackgroundColor(CoreApp.termBackground.get());
		
		super.drawObject(mXIn, mYIn);
	}
	
	@Override
	public void keyPressed(char typedChar, int keyCode) {
		if (GuiScreen.isKeyComboCtrlC(keyCode)) {
			System.out.println("ctrl c");
		}
		if (inputField != null) { inputField.requestFocus(); }
	}
	
	@Override
	public void mousePressed(int mXIn, int mYIn, int button) {
		super.mousePressed(mXIn, mYIn, button);
		if (button == 1) { EnhancedMC.displayWindow(new TerminalRCM(this), CenterType.cursorCorner); }
		else { inputField.requestFocus(); }
	}
	
	@Override
	public void mouseReleased(int mXIn, int mYIn, int button) {
		inputField.requestFocus();
		super.mouseReleased(mXIn, mYIn, button);
	}
	
	@Override
	public void onFocusGained(EventFocus eventIn) {
		if (eventIn.getFocusType().equals(FocusType.MousePress)) { mousePressed(eventIn.getMX(), eventIn.getMY(), eventIn.getActionCode()); }
		else { inputField.requestFocus(); }
	}
	
	@Override
	public void onGroupNotification(ObjectEvent e) {
		if (e instanceof EventMouse) {
			EventMouse m = (EventMouse) e;
			if (m.getMouseType() == MouseType.Pressed) {
				bringToFront();
				if (e.getEventParent() instanceof EGuiTextArea || e.getEventParent() instanceof TextAreaLine) {
					if (inputField != null) { inputField.requestFocus(); }
				}
			}
			if (m.getMouseType() == MouseType.Released) {
				if (inputField != null) { inputField.requestFocus(); }
			}
		}
	}
	
	@Override
	public ETerminal resize(int xIn, int yIn, ScreenLocation areaIn) {
		try {
			if (!isMaximized() && (xIn != 0 || yIn != 0)) {
				String text = inputField.getText();
				int vPos = history.getVScrollBar().getScrollPos();
				int hPos = history.getHScrollBar().getScrollPos();
				
				EArrayList<TextAreaLine> lines = new EArrayList();
				for (TextAreaLine l : new EArrayList<TextAreaLine>(history.getTextDocument())) {
					if (tabDisplayLines.notContains(l)) { lines.add(l); }
				}
				clearTabCompletions();
				
				history.clear();
				super.resize(xIn, yIn, areaIn);
				
				for (TextAreaLine l : lines) {
					TerminalTextLine n = new TerminalTextLine(this, l.getText(), l.mainDrawColor, l.getStoredObj(), l.getLineNumber());
					history.addTextLine(n);
				}
				
				history.getVScrollBar().onResizeUpdate(vPos, xIn, yIn, areaIn);
				history.getHScrollBar().onResizeUpdate(hPos, xIn, yIn, areaIn);
				inputField.setText(text);
				
				setPreMax(getDimensions());
			}
		} catch (Exception e) { e.printStackTrace(); }
		return this;
	}
	
	@Override
	public void actionPerformed(IEnhancedActionObject object, Object... args) {
		if (object == inputField) {
			String cmd = inputField.getText();
			
			boolean isTab = false;
			for (Object o : args) {
				if (o.equals("tab")) { isTab = true; break; }
			}
			
			try {
				if (!cmd.isEmpty()) {
					if (isTab) { handleTab(); }
					else {
						if (!(cmd.equals("clear") || cmd.equals("clr") || cmd.equals("cls"))) {
							writeln("> " + cmd, 0xffffff);
						}
						EnhancedMC.getTerminalHandler().cmdHistory.add(cmd);
						EnhancedMC.getTerminalHandler().executeCommand(this, cmd);
						inputField.setText("");
						scrollToBottom();
						tab1 = false;
						tabData.clear();
					}
				}
				else if (isTab) { handleTab(); }
			}
			catch (Exception e) { e.printStackTrace(); }
		}
	}
	
	@Override
	public void maximize() {
		try {
			EDimension screen = getTopParent().getDimensions();
			
			if (getTopParent().containsObject(TaskBar.class)) {
				switch (CoreApp.taskBarSide.get()) {
				case "top": setDimensions(0, header.height + TaskBar.drawSize, screen.width, screen.height - (header.height + TaskBar.drawSize)); break;
				case "bottom": setDimensions(0, header.height, screen.width, screen.height - (header.height + TaskBar.drawSize)); break;
				case "left": setDimensions(TaskBar.drawSize, header.height, screen.width - TaskBar.drawSize, screen.height - header.height); break;
				case "right": setDimensions(0, 0, screen.width - TaskBar.drawSize, screen.height - header.height); break;
				}
			}
			else {
				setDimensions(0, header.height, screen.width, screen.height - header.height);
			}
			
			String text = inputField.getText();
			int vPos = history.getVScrollBar().getScrollPos();
			int hPos = history.getHScrollBar().getScrollPos();
			
			EArrayList<TextAreaLine> lines = new EArrayList();
			for (TextAreaLine l : new EArrayList<TextAreaLine>(history.getTextDocument())) {
				if (tabDisplayLines.notContains(l)) { lines.add(l); }
			}
			clearTabCompletions();
			
			history.clear();
			reInitObjects();
			
			for (TextAreaLine l : lines) {
				TerminalTextLine n = new TerminalTextLine(this, l.getText(), l.mainDrawColor, l.getStoredObj(), l.getLineNumber());
				history.addTextLine(n);
			}
			
			history.getVScrollBar().setScrollBarPos(vPos);
			history.getHScrollBar().setScrollBarPos(hPos);
			inputField.setText(text);
		}
		catch (Exception e) { e.printStackTrace(); }
	}
	
	@Override
	public void miniturize() {
		setDimensions(getPreMax());
		
		String text = inputField.getText();
		int vPos = history.getVScrollBar().getScrollPos();
		int hPos = history.getHScrollBar().getScrollPos();
		
		EArrayList<TextAreaLine> lines = new EArrayList();
		for (TextAreaLine l : new EArrayList<TextAreaLine>(history.getTextDocument())) {
			if (tabDisplayLines.notContains(l)) { lines.add(l); }
		}
		clearTabCompletions();
		
		history.clear();
		reInitObjects();
		
		for (TextAreaLine l : lines) {
			TerminalTextLine n = new TerminalTextLine(this, l.getText(), l.mainDrawColor, l.getStoredObj(), l.getLineNumber());
			history.addTextLine(n);
		}
		
		history.getVScrollBar().setScrollBarPos(vPos);
		history.getHScrollBar().setScrollBarPos(0);
		inputField.setText(text);
	}
	
	@Override public boolean isDebugWindow() { return true; }
	
	//-----------------
	//ETerminal Methods
	//-----------------
	
	public ETerminal scrollToBottom() {
		history.getVScrollBar().setScrollBarPos(history.getVScrollBar().getHighVal());
		return this;
	}
	
	public void handleTab() {
		String input = inputField.getText();
		
		try {
			if (!input.isEmpty()) {
				//only test on a command if the starting input wasn't at arg 0 or if it was at -1
				if (startArgPos != 0) {
					EnhancedMC.getTerminalHandler().executeCommand(this, input, true);
				}
				
				//set tab true only after parsing the first if condition
				if (!tab1) { tab1 = true; }
				
				//only run if there is tab data to iterate over
				if (tabPos >= 0 && tabData.isNotEmpty()) {
					
					//determine where we are getting tab completion data from
					if (startArgPos != 0) {
						//grab everything up to the argument being tabbed
						String f = "";
						for (int i = 0, spaces = 0; i < input.length(); i++) {
							if (spaces == startArgPos) { f = input.substring(0, i - 1); break; }
							else if (input.charAt(i) == ' ') { spaces++; }
							
							if (i == input.length() - 1) { f = input.trim(); }
						}
						
						//append the next tab completion onto the previous string
						String tabCompletion = (startArgPos > 0) ? " " + tabData.get(tabPos) : tabData.get(tabPos);
						f += tabCompletion;
						
						//set the terminal's output to the original text with the new tab completion
						inputField.setText(f);
					}
					else {
						inputField.setText(tabData.get(tabPos));
					}
					
					//update tab position
					if (tabPos == tabData.size() - 1) { tabPos = 0; }
					else { tabPos++; }
				}
			}
			else {
				if (!tab1) {
					EArrayList<String> cnames = TerminalCommandHandler.getInstance().getSortedCommandNames();
					for (String s : cnames) { s = s.toLowerCase(); }
					
					buildTabCompletions(cnames);
					inputField.setText(tabData.get(tabPos));
					
					//update tab position
					if (tabPos == tabData.size() - 1) { tabPos = 0; }
					else { tabPos++; }
					
					tab1 = true;
				}
				else {
					System.out.println("c: " + startArgPos);
				}
			}
		}
		catch (Exception e) { e.printStackTrace(); }
	}
	
	
	
	public ETerminal buildTabCompletions(String... dataIn) { return buildTabCompletions(new EArrayList().addA(dataIn)); }
	public ETerminal buildTabCompletions(EArrayList<String> dataIn) {
		clearTabCompletions();
		
		if (dataIn.isNotEmpty()) {
			if (!tab1) {
				tabData = new EArrayList(dataIn);
				tabPos = 0;
				startArgPos = getCurrentArg();
			}
			
			int textWidth = 100;
			int maxData = 0;
			int spaceAmount = 3;
			int longest = 0;
			
			for (String s : dataIn) {
				if (mc.fontRendererObj.getStringWidth(s) > longest) { longest = mc.fontRendererObj.getStringWidth(s); }
			}
			
			//System.out.println(longest);
			
			//determine the maximum number of autocomplete options that can fit on one line
			for (int i = 1; i < dataIn.size() + 1; i++) {
				textWidth += longest + mc.fontRendererObj.getStringWidth(EUtil.repeatString(" ", spaceAmount));
				if (textWidth < width) {
					maxData = i;
				}
				else { break; }
			}
			
			//System.out.println("maxData: " + maxData);
			maxData = MathHelper.clamp_int(maxData, 1, Integer.MAX_VALUE);
			
			//position each autocomplete option on one line up to the max line width
			int amount = dataIn.size();
			int i = 0;
			int cur = 1;
			String line = "";
			
			while (amount > 0) {
				//System.out.println(amount + " " + cur + " " + i + " " + maxData + " " + (dataIn.size() == i));
				line += dataIn.get(i) + ", ";
				
				//System.out.println(line);
				//System.out.println((cur == maxData) + " " + (amount == 1) + "\n");
				if (cur == maxData || amount == 1) {
					//if (amount == 0) { line += dataIn.get(i) + ", "; cur++; }
					try {
						String format = EUtil.repeatString("%-" + (longest / 4) + "s" + EUtil.repeatString(" ", spaceAmount), cur);
						String[] args = line.split(", ");
						line = String.format(format, args);
					} catch (Exception e) { e.printStackTrace(); }
					
					if (!line.isEmpty()) { tabDisplayLines.add(new TextAreaLine(history, line, EColors.lgray.c())); }
					line = "";
					cur = 0;
				}
				
				amount--;
				cur++;
				i++;
			}
			
			//add each created line to the grid
			for (TextAreaLine l : tabDisplayLines) {
				history.addTextLine(l);
			}
			scrollToBottom();
		}
		return this;
	}
	
	public ETerminal clearTabCompletions() {
		//System.out.println("Size: " + infoLines.size());
		for (TextAreaLine l : tabDisplayLines) {
			history.deleteLine(l);
		}
		
		tabDisplayLines.clear();
		//tabData.clear();
		
		return this;
	}
	
	public ETerminal clear() { history.clear(); return this; }
	public ETerminal clearTabData() { tabData.clear(); return this; }
	
	public int getCurrentArg() {
		int arg = 0;
		
		if (inputField.isNotEmpty()) {
			int spaces = EUtil.countSpaces(inputField.getText());
			
			if (spaces == 0) { arg = 1; }
			else { arg = spaces; }
		}
		
		return arg;
	}
	
	public void resetTab() {
		clearTabCompletions();
		tab1 = false;
		textTabBegin = "";
		tabData.clear();
		startArgPos = -1;
		scrollToBottom();
		//history.getVScrollBar().setScrollBarPos(history.getVScrollBar().getHighVal());
	}
	
	public ETerminal writeln() { return writeln("", 0xffffff); }
	public ETerminal writeln(Object objIn) { return writeln(objIn != null ? objIn.toString() : "null", 0xffffff); }
	public ETerminal writeln(Object objIn, EColors colorIn) { return writeln(objIn != null ? objIn.toString() : "null", colorIn.intVal); }
	public ETerminal writeln(Object objIn, int colorIn) { return writeln(objIn != null ? objIn.toString() : "null", colorIn); }
	public ETerminal writeln(String msgIn) { return writeln(msgIn, 0xffffff); }
	public ETerminal writeln(String msgIn, EColors colorIn) { return writeln(msgIn, colorIn.intVal); }
	public ETerminal writeln(String msgIn, int colorIn) {
		parseText(msgIn, colorIn);
		return this;
	}
	
	public ETerminal info(String msgIn) { parseText(msgIn, 0xffff00); return this; }
	public ETerminal warn(String msgIn) { parseText("Warning: " + msgIn, EColors.orange.intVal); return this; }
	public ETerminal error(String msgIn) { parseText(msgIn, 0xff5555); return this; }
	public ETerminal badError(String msgIn) { parseText(msgIn, 0xff0000); return this; }
	
	private void parseText(String msgIn, int colorIn) {
		String[] lines = msgIn.split("[\\r?\\n]+", -1);
		
		for (String s : lines) {
			TerminalTextLine line = new TerminalTextLine(this, s, colorIn);
			line.setFocusRequester(this).setObjectGroup(objectGroup);
			history.addTextLine(line);
			
		}
		
		scrollToBottom();
	}
	
	//-----------------
	//ETerminal Getters
	//-----------------
	
	public int getLastUsed() { return lastUsed; }
	public int getHisLine() { return historyLine; }
	public EGuiTextArea getTextArea() { return history; }
	public TerminalTextField getInputField() { return inputField; }
	public File getDir() { return dir; }
	public EArrayList<TextAreaLine> getInfoLines() { return tabDisplayLines; }
	public int getTabPos() { return tabPos; }
	public boolean getTab1() { return tab1; }
	public String getTextTabBegin() { return textTabBegin; }
	public int getTabArgStart() { return startArgPos; }
	
	//-----------------
	//ETerminal Setters
	//-----------------
	
	public ETerminal setDir(File dirIn) { dir = dirIn; return this; }
	public synchronized ETerminal setInputEnabled(boolean val) { inputField.setEnabled(val); return this; }
	public ETerminal setLastUsed(int in) { lastUsed = in; return this; }
	public ETerminal setHistoryLine(int in) { historyLine = in; return this; }
	public ETerminal setTabPos(int in) { tabPos = in; return this; }
	public ETerminal setTab1(boolean val) { tab1 = val; return this; }
	public ETerminal setTextTabBeing(String in) { textTabBegin = in; return this; }
	
}
