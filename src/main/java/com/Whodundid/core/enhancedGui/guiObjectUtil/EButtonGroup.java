package com.Whodundid.core.enhancedGui.guiObjectUtil;

import java.util.ArrayList;
import java.util.Iterator;

import com.Whodundid.core.enhancedGui.guiObjects.EGuiButton;

//Last edited: Oct 26, 2018
//First Added: Oct 26, 2018
//Author: Hunter Bragg

public class EButtonGroup {
	
	protected ArrayList<EGuiButton> buttons;
	protected EGuiButton lastButtonPressed = null;
	protected int lastMouseButtonPressed = -1;
	
	public EButtonGroup() {
		buttons = new ArrayList();
	}
	
	public void buttonPressed(EGuiButton buttonIn, int mouseButton) {
		lastButtonPressed = buttonIn;
		lastMouseButtonPressed = mouseButton;
		onButtonPressed();
	}
	
	public EButtonGroup addButtons(EGuiButton... buttonsIn) {
		for (EGuiButton b : buttonsIn) { buttons.add(b.setButtonGroup(this)); }
		return this;
	}
	
	public EButtonGroup removeButtons(EGuiButton... buttonsIn) {
		for (EGuiButton b : buttonsIn) {
			Iterator<EGuiButton> it = buttons.iterator();
			while (it.hasNext()) {
				if (b.equals(it.next())) { it.remove(); }
			}
		}
		return this;
	}
	
	public ArrayList<EGuiButton> getButtons() { return buttons; }
	public EGuiButton getLastPressedButton() { return lastButtonPressed; }
	public int getLastMouseButtonPressed() { return lastMouseButtonPressed; }
	
	public void onButtonPressed() {}
}
