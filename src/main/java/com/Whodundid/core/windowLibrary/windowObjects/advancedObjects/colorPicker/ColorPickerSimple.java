package com.Whodundid.core.windowLibrary.windowObjects.advancedObjects.colorPicker;

import com.Whodundid.core.EnhancedMC;
import com.Whodundid.core.util.renderUtil.CenterType;
import com.Whodundid.core.util.renderUtil.EColors;
import com.Whodundid.core.util.storageUtil.EDimension;
import com.Whodundid.core.windowLibrary.windowObjects.actionObjects.WindowButton;
import com.Whodundid.core.windowLibrary.windowObjects.actionObjects.WindowTextField;
import com.Whodundid.core.windowLibrary.windowObjects.basicObjects.WindowContainer;
import com.Whodundid.core.windowLibrary.windowObjects.basicObjects.WindowLabel;
import com.Whodundid.core.windowLibrary.windowObjects.basicObjects.WindowRect;
import com.Whodundid.core.windowLibrary.windowObjects.windows.WindowDialogueBox;
import com.Whodundid.core.windowLibrary.windowObjects.windows.WindowDialogueBox.DialogueBoxTypes;
import com.Whodundid.core.windowLibrary.windowTypes.ActionWindowParent;
import com.Whodundid.core.windowLibrary.windowTypes.interfaces.IActionObject;
import com.Whodundid.core.windowLibrary.windowTypes.interfaces.IWindowObject;

//Author: Hunter Bragg

public class ColorPickerSimple extends ActionWindowParent {
	
	//color buttons
	ColorPickerButton lred, red, maroon, brown, dorange, borange, orange, lorange, yellow;
	ColorPickerButton lime, green, lgreen, dgreen, seafoam, turquoise, aquamarine;
	ColorPickerButton cyan, skyblue, blue, regal, grape, navy, violet, eggplant, purple, magenta, pink, hotpink;
	ColorPickerButton white, lgray, gray, mgray, dgray, pdgray, steel, dsteel, vdgray, black;
	
	//functional objects
	WindowTextField inputField;
	WindowButton select, back;
	WindowButton advanced;
	WindowRect colorDisplay;
	WindowLabel colorLabel, inputLabel;
	
	//the current color
	int currentColor = 0xffffffff;
	
	public ColorPickerSimple(IWindowObject parentIn) { this(parentIn, 0xffffffff); }
	public ColorPickerSimple(IWindowObject parentIn, int colorIn) {
		super(parentIn);
		currentColor = colorIn;
	}
	
	@Override
	public void initWindow() {
		setObjectName("Color Picker");
		setDimensions(211, 198);
		setPinnable(false);
	}
	
	@Override
	public void initObjects() {
		//initialize header
		defaultHeader(this);
		
		//create color display objects
		colorDisplay = new WindowRect(this, midX - 25, startY + 22, midX + 25, startY + 72, currentColor);
		colorLabel = new WindowLabel(this, midX, startY + 8, "", EColors.lime);
				
		colorLabel.setDrawCentered(true);
				
		updateValues();
		
		//create color buttons
		WindowContainer colorContainer = new WindowContainer(this, startX + 5, colorDisplay.endY + 6, width - 10, 48);
		colorContainer.setTitle("Color Palette");
		colorContainer.setTitleColor(EColors.lgray.intVal);
		colorContainer.setBackgroundColor(0xff383838);
		colorContainer.setDrawTitle(false);
		
		int y = colorContainer.startY + 4;
		int w = 11;
		int h = 11;
		
		lred = new ColorPickerButton(this, colorContainer.startX + 5, y, w, h, EColors.lred);
		red = new ColorPickerButton(this, lred.endX + 1, y, w, h, EColors.red);
		maroon = new ColorPickerButton(this, red.endX + 1, y, w, h, EColors.maroon);
		brown = new ColorPickerButton(this, maroon.endX + 1, y, w, h, EColors.brown);
		dorange = new ColorPickerButton(this, brown.endX + 1, y, w, h, EColors.dorange);
		borange = new ColorPickerButton(this, dorange.endX + 1, y, w, h, EColors.borange);
		orange = new ColorPickerButton(this, borange.endX + 1, y, w, h, EColors.orange);
		lorange = new ColorPickerButton(this, orange.endX + 1, y, w, h, EColors.lorange);
		yellow = new ColorPickerButton(this, lorange.endX + 1, y, w, h, EColors.yellow);
		lime = new ColorPickerButton(this, yellow.endX + 1, y, w, h, EColors.lime);
		green = new ColorPickerButton(this, lime.endX + 1, y, w, h, EColors.green);
		seafoam = new ColorPickerButton(this, green.endX + 1, y, w, h, EColors.seafoam);
		lgreen = new ColorPickerButton(this, seafoam.endX + 1, y, w, h, EColors.lgreen);
		dgreen = new ColorPickerButton(this, lgreen.endX + 1, y, w, h, EColors.dgreen);
		turquoise = new ColorPickerButton(this, dgreen.endX + 1, y, w, h, EColors.turquoise);
		aquamarine = new ColorPickerButton(this, turquoise.endX + 1, y, w, h, EColors.aquamarine);
		
		int y1 = lred.endY + 1;
		
		cyan = new ColorPickerButton(this, maroon.startX, y1, w, h, EColors.cyan);
		skyblue = new ColorPickerButton(this, cyan.endX + 1, y1, w, h, EColors.skyblue);
		blue = new ColorPickerButton(this, skyblue.endX + 1, y1, w, h, EColors.blue);
		regal = new ColorPickerButton(this, blue.endX + 1, y1, w, h, EColors.regal);
		grape = new ColorPickerButton(this, regal.endX + 1, y1, w, h, EColors.grape);
		navy = new ColorPickerButton(this, grape.endX + 1, y1, w, h, EColors.navy);
		violet = new ColorPickerButton(this, navy.endX + 1, y1, w, h, EColors.violet);
		eggplant = new ColorPickerButton(this, violet.endX + 1, y1, w, h, EColors.eggplant);
		purple = new ColorPickerButton(this, eggplant.endX + 1, y1, w, h, EColors.purple);
		pink = new ColorPickerButton(this, purple.endX + 1, y1, w, h, EColors.pink);
		hotpink = new ColorPickerButton(this, pink.endX + 1, y1, w, h, EColors.hotpink);
		magenta = new ColorPickerButton(this, hotpink.endX + 1, y1, w, h, EColors.magenta);
		
		int y2 = navy.endY + 5;
		
		white = new ColorPickerButton(this, brown.startX, y2, w, h, EColors.white);
		lgray = new ColorPickerButton(this, white.endX + 1, y2, w, h, EColors.lgray);
		gray = new ColorPickerButton(this, lgray.endX + 1, y2, w, h, EColors.gray);
		mgray = new ColorPickerButton(this, gray.endX + 1, y2, w, h, EColors.mgray);
		dgray = new ColorPickerButton(this, mgray.endX + 1, y2, w, h, EColors.dgray);
		pdgray = new ColorPickerButton(this, dgray.endX + 1, y2, w, h, EColors.pdgray);
		steel = new ColorPickerButton(this, pdgray.endX + 1, y2, w, h, EColors.steel);
		dsteel = new ColorPickerButton(this, steel.endX + 1, y2, w, h, EColors.dsteel);
		vdgray = new ColorPickerButton(this, dsteel.endX + 1, y2, w, h, EColors.vdgray);
		black = new ColorPickerButton(this, vdgray.endX + 1, y2, w, h, EColors.black);
		
		//create manual color input field & label
		WindowContainer inputContainer = new WindowContainer(this, startX + 5, colorContainer.endY + 2, width - 10, 41);
		inputContainer.setTitle("Hex Color Code");
		inputContainer.setTitleColor(EColors.lgray.intVal);
		inputContainer.setBackgroundColor(0xff383838);
		inputContainer.setHoverText("The current color value in (RGB) hexadecimal format");
		
		inputField = new WindowTextField(this, inputContainer.startX + 6, inputContainer.startY + 22, 66, 14);
		inputLabel = new WindowLabel(this, inputField.endX + 10, inputField.startY + 3, "(ARGB) color value");
		
		inputField.setMaxStringLength(8);
		//inputLabel.setHoverText("The current color value in (RGB) hexadecimal format");
		inputLabel.setColor(EColors.seafoam);
		
		//create select and back buttons
		back = new WindowButton(this, inputContainer.midX - 10 - 80, inputContainer.endY + 4, 80, 20, "Back");
		select = new WindowButton(this, inputContainer.midX + 10, inputContainer.endY + 4, 80, 20, "Confirm");
		
		back.setStringColor(EColors.yellow);
		select.setStringColor(EColors.lgreen);
		
		//add containers
		addObject(colorContainer, inputContainer);
		
		//add color buttons
		colorContainer.addObject(lred, red, maroon, brown, dorange, borange, orange, lorange, yellow);
		colorContainer.addObject(lime, green, lgreen, dgreen, seafoam, turquoise, aquamarine);
		colorContainer.addObject(cyan, skyblue, blue, regal, navy, grape, violet, eggplant, purple, magenta, pink, hotpink);
		colorContainer.addObject(white, lgray, gray, mgray, dgray, pdgray, steel, dsteel, vdgray, black);
		
		//add functional objects
		inputContainer.addObject(inputField, inputLabel);
		
		addObject(select, back);
		addObject(colorDisplay, colorLabel);
		
		updateValues();
	}
	
	@Override
	public void drawObject(int mXIn, int mYIn) {
		drawDefaultBackground();
		//drawRect(0xff3b3b3b, 1);
		
		if (colorDisplay != null) {
			EDimension cDim = colorDisplay.getDimensions();
			drawRect(cDim.startX - 1, cDim.startY - 1, cDim.endX + 1, cDim.endY + 1, 0xff000000);
		}
		
		super.drawObject(mXIn, mYIn);
	}
	
	@Override
	public void actionPerformed(IActionObject object, Object... args) {
		if (object instanceof ColorPickerButton) {
			ColorPickerButton b = (ColorPickerButton) object;
			
			if (args.length > 0 && args[0] instanceof String) {
				String val = (String) args[0];
				if (val != null && val.equals("dc")) {
					performAction(b.getColor());
					close();
				}
			}
			else {
				currentColor = b.getColor();
				updateValues();
			}
		}
		else {
			if (object == inputField) { parseInputColor(); }
			if (object == select) { performAction(currentColor); close(); }
			if (object == back) { close(); }
		}
	}
	
	private void parseInputColor() {
		try {
			if (inputField.getText().length() > 6) {
				String alpha = inputField.getText().substring(0, 2);
				currentColor = (int) Long.parseLong(alpha + inputField.getText().substring(2), 16);
			}
			else {
				currentColor = 0xff000000 + Integer.parseInt(inputField.getText(), 16);
			}
			updateValues();
		}
		catch (Exception e) {
			e.printStackTrace();
			WindowDialogueBox error = new WindowDialogueBox(DialogueBoxTypes.ok);
			error.setTitle("Error!");
			error.setTitleColor(EColors.lred.c());
			error.setMessage("Cannot parse the value: " + inputField.getText());
			error.setMessageColor(EColors.lgray.c());
			EnhancedMC.displayWindow(error, CenterType.screen);
			inputField.clear();
			inputField.setTextWhenEmpty(inputField.textWhenEmpty);
		}
	}
	
	private void updateValues() {
		colorDisplay.setColor(currentColor);
		EColors c = EColors.getEColor(currentColor);
		colorDisplay.setHoverText(c != null ? c.name : "" + currentColor).setHoverTextColor(EColors.seafoam.intVal);
		String cs = "Current Color: " + (c != null ? c.name : "" + Integer.toHexString(currentColor).substring(2));
		colorLabel.setString(cs);
		try {
			String val = "";
			if (inputField != null && inputField.getText() != null) {
				//if (inputField.getText().length() > 6) {
					val = String.format("%8x", currentColor);
				//}
				//else {
				//	val = String.format("%6x", currentColor).substring(2);
				//}
				inputField.setTextWhenEmpty(val);
				inputField.setText(val);
			}
		}
		catch (Exception e) { e.printStackTrace(); }
	}
	
	@Override
	public IActionObject setSelectedObject(Object objIn) {
		if (objIn instanceof Integer) { currentColor = (int) objIn; }
		return this;
	}
	
	@Override public Object getSelectedObject() { return currentColor; }
	
}
