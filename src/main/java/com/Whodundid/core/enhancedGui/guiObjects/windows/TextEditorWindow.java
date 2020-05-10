package com.Whodundid.core.enhancedGui.guiObjects.windows;

import com.Whodundid.core.EnhancedMC;
import com.Whodundid.core.enhancedGui.guiObjects.actionObjects.EGuiButton;
import com.Whodundid.core.enhancedGui.guiObjects.advancedObjects.textArea.EGuiTextArea;
import com.Whodundid.core.enhancedGui.guiObjects.advancedObjects.textArea.TextAreaLine;
import com.Whodundid.core.enhancedGui.guiObjects.windows.EGuiDialogueBox.DialogueBoxTypes;
import com.Whodundid.core.enhancedGui.types.WindowParent;
import com.Whodundid.core.enhancedGui.types.interfaces.IEnhancedActionObject;
import com.Whodundid.core.util.chatUtil.EChatUtil;
import com.Whodundid.core.util.renderUtil.EColors;
import com.Whodundid.core.util.storageUtil.EArrayList;
import java.io.File;
import java.io.PrintWriter;
import java.util.Scanner;

public class TextEditorWindow extends WindowParent {
	
	File path = null;
	EGuiTextArea document;
	EGuiButton save, cancel;
	boolean failed = false;
	boolean newFile = false;
	
	public TextEditorWindow(File pathIn) {
		super();
		path = pathIn;
	}
	
	@Override
	public void initGui() {
		//getTopParent().setFocusLockObject(this);
		setDimensions(250, 250);
		setMinDims(75, 75);
		setResizeable(true);
		setObjectName("Editing: " + path != null ? path.getName() : "Unnamed File");
	}
	
	@Override
	public void initObjects() {
		defaultHeader(this);
		
		document = new EGuiTextArea(this, startX + 2, startY + 2, width - 4, height - 25);
		document.setEditable(!failed);
		if (!failed) { document.setDrawLineNumbers(true); }
		
		save = new EGuiButton(this, endX - (width / 3) - 2, document.endY + 1, (width / 3), 20, "Save");
		cancel = new EGuiButton(this, startX + 2, document.endY + 1, (width / 3), 20, "Cancel");
		
		addObject(document, save, cancel);
		
		loadFile();
	}
	
	@Override
	public void drawObject(int mXIn, int mYIn) {
		drawDefaultBackground();
		super.drawObject(mXIn, mYIn);
	}
	
	@Override
	public void actionPerformed(IEnhancedActionObject object, Object... args) {
		if (object == save) { saveFile(); }
		if (object == cancel) { close(); }
	}
	
	private void loadFile() {
		if (path != null && document != null) {
			if (path.exists()) {
				try (Scanner reader = new Scanner(path)) {
					while (reader.hasNext()) {
						document.addTextLine(reader.nextLine(), EColors.lgray.c());
					}
				}
				catch (Exception e) {
					e.printStackTrace();
					failed = true;
					document.addTextLine("Error: Cannot open file!", EColors.red.c());
					document.setEditable(false);
				}
				
				document.getVScrollBar().setScrollBarPos(0);
			}
			else { newFile = true; }
		}
	}
	
	private void saveFile() {
		if (path != null) {
			
			//create the required directories if they do not exist
			if (!path.getParentFile().exists()) { path.getParentFile().mkdirs(); }
			
			//save the file
			try (PrintWriter writer = new PrintWriter(path, "UTF-8")) {
				
				if (document != null) {
					EArrayList<TextAreaLine> lines = document.getTextDocument();
					
					for (TextAreaLine l : lines) {
						writer.println(EChatUtil.removeFormattingCodes(l.getText()));
					}
				}
				
				EnhancedMC.displayWindow(new EGuiDialogueBox(DialogueBoxTypes.ok).setMessage("File saved!").setMessageColor(EColors.green.c()));
			}
			catch (Exception e) {
				e.printStackTrace();
				EnhancedMC.displayWindow(new EGuiDialogueBox(DialogueBoxTypes.ok).setMessage("Failed to save file!").setMessageColor(EColors.lred.c()));
			}
		}
	}
	
	public EGuiTextArea getTextArea() { return document; }
	
	public TextEditorWindow setFocusToLineIfEmpty() {
		if (newFile) { document.addTextLine().requestFocus(); }
		return this;
	}

}
