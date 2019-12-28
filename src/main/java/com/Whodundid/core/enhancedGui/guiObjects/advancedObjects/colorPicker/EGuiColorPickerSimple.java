package com.Whodundid.core.enhancedGui.guiObjects.advancedObjects.colorPicker;

import com.Whodundid.core.EnhancedMC;
import com.Whodundid.core.enhancedGui.guiObjects.basicObjects.EGuiButton;
import com.Whodundid.core.enhancedGui.guiObjects.basicObjects.EGuiContainer;
import com.Whodundid.core.enhancedGui.guiObjects.basicObjects.EGuiLabel;
import com.Whodundid.core.enhancedGui.guiObjects.basicObjects.EGuiRect;
import com.Whodundid.core.enhancedGui.guiObjects.basicObjects.EGuiTextField;
import com.Whodundid.core.enhancedGui.guiObjects.windows.EGuiDialogueBox;
import com.Whodundid.core.enhancedGui.guiObjects.windows.EGuiDialogueBox.DialogueBoxTypes;
import com.Whodundid.core.enhancedGui.types.ActionWindowParent;
import com.Whodundid.core.enhancedGui.types.interfaces.IEnhancedActionObject;
import com.Whodundid.core.enhancedGui.types.interfaces.IEnhancedGuiObject;
import com.Whodundid.core.util.renderUtil.CenterType;
import com.Whodundid.core.util.renderUtil.EColors;
import com.Whodundid.core.util.storageUtil.EDimension;

public class EGuiColorPickerSimple extends ActionWindowParent {
	
	//color buttons
	ColorButton red, orange, yellow;
	ColorButton lime, green, seafoam;
	ColorButton cyan, blue, navy;
	ColorButton purple, magenta, pink;
	ColorButton white, lgray, gray, dgray, steel, black;
	
	//functional objects
	EGuiTextField inputField;
	EGuiButton select, back;
	EGuiButton advanced;
	EGuiRect colorDisplay;
	EGuiLabel colorLabel, inputLabel;
	
	//the current color
	int currentColor = 0xffffffff;
	
	public EGuiColorPickerSimple(IEnhancedGuiObject parentIn) { this(parentIn, 0xffffffff); }
	public EGuiColorPickerSimple(IEnhancedGuiObject parentIn, int colorIn) {
		super(parentIn);
		currentColor = colorIn;
	}
	
	@Override
	public void initGui() {
		setDimensions(211, 118);
		setPinnable(false);
		setObjectName("Color Picker");
		setResizeable(true);
	}
	
	@Override
	public void initObjects() {
		//initialize header
		defaultHeader(this);
		
		//create color display objects
		colorDisplay = new EGuiRect(this, midX - 25, startY + 24, midX + 25, startY + 74, currentColor);
		colorLabel = new EGuiLabel(this, midX, startY + 8);
				
		colorLabel.setDrawCentered(true);
				
		updateValues();
		
		//create color buttons
		EGuiContainer colorContainer = new EGuiContainer(this, startX + 5, startY + 5, width - 10, 41);
		colorContainer.setTitle("Color Palette");
		colorContainer.setTitleColor(EColors.lgray.c());
		colorContainer.setBackgroundColor(0xff555555);
		colorContainer.setDrawTitle(false);
		
		red = new ColorButton(this, colorContainer.startX + 5, colorContainer.startY + 5, 15, 15, "Red", EColors.red);
		orange = new ColorButton(this, red.endX + 1, red.startY, 15, 15, "Orange", EColors.orange);
		yellow = new ColorButton(this, orange.endX + 1, red.startY, 15, 15, "Yellow", EColors.yellow);
		lime = new ColorButton(this, yellow.endX + 1, red.startY, 15, 15, "Lime", EColors.lime);
		green = new ColorButton(this, lime.endX + 1, red.startY, 15, 15, "Green", EColors.darkGreen);
		seafoam = new ColorButton(this, green.endX + 1, red.startY, 15, 15, "Seafoam", EColors.seafoam);
		cyan = new ColorButton(this, seafoam.endX + 1, red.startY, 15, 15, "Cyan", EColors.cyan);
		blue = new ColorButton(this, cyan.endX + 1, red.startY, 15, 15, "Blue", EColors.blue);
		navy = new ColorButton(this, blue.endX + 1, red.startY, 15, 15, "Navy", EColors.navy);
		purple = new ColorButton(this, navy.endX + 1, red.startY, 15, 15, "Purple", EColors.purple);
		magenta = new ColorButton(this, purple.endX + 1, red.startY, 15, 15, "Magenta", EColors.magenta);
		pink = new ColorButton(this, magenta.endX + 1, red.startY, 15, 15, "Pink", EColors.pink);
		
		white = new ColorButton(this, red.startX, purple.endY + 1, 15, 15, "White", EColors.white);
		lgray = new ColorButton(this, white.endX + 1, purple.endY + 1, 15, 15, "Light Gray", EColors.lgray);
		gray = new ColorButton(this, lgray.endX + 1, purple.endY + 1, 15, 15, "Gray", EColors.gray);
		dgray = new ColorButton(this, gray.endX + 1, purple.endY + 1, 15, 15, "Dark Gray", EColors.dgray);
		steel = new ColorButton(this, dgray.endX + 1, purple.endY + 1, 15, 15, "Steel", EColors.steel);
		black = new ColorButton(this, steel.endX + 1, purple.endY + 1, 15, 15, "Black", EColors.black);
		
		/*
		pink = new ColorButton(this, colorContainer.midX - (15 * 3 + 3), colorContainer.startY + 21, 15, 15, "Pink", EColors.pink);
		magenta = new ColorButton(this, pink.endX + 1, pink.startY, 15, 15, "Magenta", EColors.magenta);
		red = new ColorButton(this, magenta.endX + 1, pink.startY, 15, 15, "Red", EColors.red);
		orange = new ColorButton(this, red.endX + 1, pink.startY, 15, 15, "Orange", EColors.orange);
		yellow = new ColorButton(this, orange.endX + 1, pink.startY, 15, 15, "Yellow", EColors.yellow);
		lime = new ColorButton(this, yellow.endX + 1, pink.startY, 15, 15, "Lime", EColors.lime);
		
		green = new ColorButton(this, pink.startX, lime.endY + 1, 15, 15, "Green", EColors.darkGreen);
		seafoam = new ColorButton(this, green.endX + 1, lime.endY + 1, 15, 15, "Seafoam", EColors.seafoam);
		cyan = new ColorButton(this, red.startX, lime.endY + 1, 15, 15, "Cyan", EColors.cyan);
		blue = new ColorButton(this, cyan.endX + 1, lime.endY + 1, 15, 15, "Blue", EColors.blue);
		navy = new ColorButton(this, blue.endX + 1, lime.endY + 1, 15, 15, "Navy", EColors.navy);
		purple = new ColorButton(this, navy.endX + 1, lime.endY + 1, 15, 15, "Purple", EColors.purple);
		
		white = new ColorButton(this, green.startX, purple.endY + 1, 15, 15, "White", EColors.white);
		lgray = new ColorButton(this, white.endX + 1, purple.endY + 1, 15, 15, "Light Gray", EColors.lgray);
		gray = new ColorButton(this, lgray.endX + 1, purple.endY + 1, 15, 15, "Gray", EColors.gray);
		dgray = new ColorButton(this, gray.endX + 1, purple.endY + 1, 15, 15, "Dark Gray", EColors.dgray);
		steel = new ColorButton(this, dgray.endX + 1, purple.endY + 1, 15, 15, "Steel", EColors.steel);
		black = new ColorButton(this, steel.endX + 1, purple.endY + 1, 15, 15, "Black", EColors.black);
		*/
		
		//create manual color input field & label
		EGuiContainer inputContainer = new EGuiContainer(this, startX + 5, colorContainer.endY + 2, width - 10, 41);
		inputContainer.setTitle("Hex Color Code");
		inputContainer.setTitleColor(EColors.lgray.c());
		inputContainer.setBackgroundColor(0xff555555);
		inputContainer.setHoverText("The current color value in (RGB) hexadecimal format");
		
		inputField = new EGuiTextField(this, inputContainer.startX + 6, inputContainer.startY + 22, 66, 14);
		//inputLabel = new EGuiLabel(this, inputContainer.startX + 10, inputField.startY + 3, "Hexadecimal color value");
		
		inputField.setMaxStringLength(8);
		//inputLabel.setHoverText("The current color value in (RGB) hexadecimal format");
		//inputLabel.setDisplayStringColor(EColors.lgray);
		
		//create select and back buttons
		back = new EGuiButton(this, startX + 5, endY - 25, 62, 20, "Back");
		select = new EGuiButton(this, endX - 67, endY - 25, 62, 20, "Confirm");
		
		//add containers
		addObject(colorContainer, inputContainer);
		
		//add color buttons
		colorContainer.addObject(red, orange, yellow, lime, green, seafoam);
		colorContainer.addObject(cyan, blue, navy, purple, magenta, pink);
		colorContainer.addObject(white, lgray, gray, dgray, steel, black);
		
		//add functional objects
		inputContainer.addObject(inputField);
		
		addObject(select, back);
		//addObject(colorDisplay, colorLabel);
		
		updateValues();
	}
	
	@Override
	public void drawObject(int mXIn, int mYIn, float ticks) {
		drawDefaultBackground();
		
		if (colorDisplay != null) {
			EDimension cDim = colorDisplay.getDimensions();
			//drawRect(cDim.startX - 1, cDim.startY - 1, cDim.endX + 1, cDim.endY + 1, 0xff000000);
		}
		
		super.drawObject(mXIn, mYIn, ticks);
	}
	
	@Override
	public void actionPerformed(IEnhancedActionObject object, Object... args) {
		if (object instanceof ColorButton) {
			currentColor = ((ColorButton) object).getColor();
			updateValues();
			
		}
		else {
			if (object == inputField) { parseInputColor(); }
			if (object == select) { getActionReciever().actionPerformed(this, currentColor); close(); }
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
		} catch (Exception e) {
			e.printStackTrace();
			EGuiDialogueBox error = new EGuiDialogueBox(DialogueBoxTypes.ok);
			error.setTitle("Error!");
			error.setTitleColor(EColors.lightRed.c());
			error.setMessage("Cannot parse the value: " + inputField.getText());
			error.setMessageColor(EColors.lgray.c());
			EnhancedMC.displayEGui(error, CenterType.screen);
			inputField.clear();
			inputField.setTextWhenEmpty(inputField.textWhenEmpty);
		}
	}
	
	private void updateValues() {
		colorDisplay.setColor(currentColor);
		EColors c = EColors.getEColor(currentColor);
		colorDisplay.setHoverText(c != null ? c.n() : "" + currentColor).setHoverTextColor(EColors.white.c());
		String cs = "Current Color: " + (c != null ? c.n() : "" + Integer.toHexString(currentColor).substring(2));
		colorLabel.setDisplayString(cs);
		try {
			String val = "";
			if (inputField != null && inputField.getText() != null) {
				if (inputField.getText().length() > 6) {
					val = inputField.getText().substring(0, 2) + String.format("%6x", currentColor).substring(2);
				}
				else {
					val = String.format("%6x", currentColor).substring(2);
				}
				inputField.setTextWhenEmpty(val);
				inputField.setText(val);
			}
		} catch (Exception e) { e.printStackTrace(); }
	}
	
	@Override
	public IEnhancedActionObject setSelectedObject(Object objIn) {
		if (objIn instanceof Integer) { currentColor = (int) objIn; }
		return this;
	}
	@Override public Object getSelectedObject() { return currentColor; }
}
