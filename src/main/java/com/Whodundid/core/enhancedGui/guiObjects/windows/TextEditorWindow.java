package com.Whodundid.core.enhancedGui.guiObjects.windows;

import com.Whodundid.core.EnhancedMC;
import com.Whodundid.core.coreApp.EMCResources;
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
import net.minecraft.util.MathHelper;

public class TextEditorWindow extends WindowParent {
	
	File path = null;
	EGuiTextArea document;
	EGuiButton save, cancel, reload;
	boolean failed = false;
	boolean newFile = false;
	
	public TextEditorWindow(File pathIn) {
		super();
		path = pathIn;
		windowIcon = EMCResources.textEditorIcon;
	}
	
	@Override
	public void initGui() {
		setDimensions(250, 250);
		setMinDims(125, 125);
		setResizeable(true);
		setMaximizable(true);
		setObjectName("Editing: " + path != null ? path.getName() : "Unnamed File");
	}
	
	@Override
	public void initObjects() {
		defaultHeader(this);
		
		document = new EGuiTextArea(this, startX + 2, startY + 2, width - 4, height - 31);
		document.setEditable(!failed);
		if (!failed) { document.setDrawLineNumbers(true); }
		
		int w = MathHelper.clamp_int((width - 10 - 24) / 2, 45, 100);
		
		cancel = new EGuiButton(this, midX - 15 - w, document.endY + 3, w, 20, "Cancel");
		save = new EGuiButton(this, midX + 15, document.endY + 1, width % 2 == 1 ? w + 1 : w, 20, "Save");
		reload = new EGuiButton(this, midX - 10, document.endY + 1, 20, 20).setTextures(EMCResources.refresh, EMCResources.refreshSel);
		
		addObject(document, save, cancel, reload);
		
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
		if (object == reload) { reloadFile(); }
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
	
	private void reloadFile() {
		reInitObjects();
	}
	
	public EGuiTextArea getTextArea() { return document; }
	
	public TextEditorWindow setFocusToLineIfEmpty() {
		if (newFile) { document.addTextLine().requestFocus(); }
		return this;
	}

}
