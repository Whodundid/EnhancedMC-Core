package com.Whodundid.guiCreator.gui.creatorParts.toolbelt;

import static com.Whodundid.guiCreator.util.CreatorTool.*;
import static com.Whodundid.guiCreator.util.ToolCategory.*;

import com.Whodundid.core.enhancedGui.guiObjects.advancedObjects.header.EGuiHeader;
import com.Whodundid.core.enhancedGui.guiObjects.basicObjects.EGuiRect;
import com.Whodundid.core.enhancedGui.types.EnhancedGuiObject;
import com.Whodundid.core.enhancedGui.types.interfaces.IEnhancedActionObject;
import com.Whodundid.core.util.renderUtil.EColors;
import com.Whodundid.core.util.storageUtil.EArrayList;
import com.Whodundid.guiCreator.gui.CreatorGui;
import com.Whodundid.guiCreator.gui.creatorParts.toolbelt.toolbeltParts.CreatorCategory;
import com.Whodundid.guiCreator.gui.creatorParts.toolbelt.toolbeltParts.CreatorToolButton;

public class CreatorToolBelt extends EnhancedGuiObject {

	CreatorGui parentCreator;
	EArrayList<CreatorCategory> tools = new EArrayList();
	int toolWidth = 2;
	
	public CreatorToolBelt(CreatorGui guiIn, int xIn, int yIn) { this(guiIn, xIn, yIn, 2); }
	public CreatorToolBelt(CreatorGui guiIn, int xIn, int yIn, int toolWidthIn) {
		init(guiIn, xIn, yIn + 14);
		parentCreator = guiIn;
		toolWidth = toolWidthIn;
	}
	
	//----------------------------
	//IEnhancedGuiObject Overrides
	//----------------------------
	
	@Override
	public void initObjects() {
		CreatorCategory creator = CreatorCategory.from(this, CREATOR, new EArrayList(SELECT, MOVE, RESIZE, EYEDROPPER));
		CreatorCategory shapes = CreatorCategory.from(this, SHAPE, new EArrayList(LINE, SQUARE, CIRCLE));
		CreatorCategory action = CreatorCategory.from(this, ACTION, new EArrayList(BUTTON, BUTTON3, CHECKBOX, RADIOBUTTON, SLIDER, TEXTFIELD));
		CreatorCategory basic = CreatorCategory.from(this, BASIC, new EArrayList(HEADER, CONTAINER, IMAGEBOX, LABEL));
		CreatorCategory advanced = CreatorCategory.from(this, ADVANCED, new EArrayList(SCROLLLIST, TEXTAREA, DROPDOWNLIST, PLAYERVIEWER, PROGRESSBAR));
		
		//add each category to the tool list
		tools.add(creator, shapes, action, basic, advanced);
		
		//position each tool within the list and actually create and apply the dimensions
		buildTools();
		
		EGuiHeader h = new EGuiHeader(this, false, 14, "Tools");
		h.setMoveable(false);
		h.setParentFocusDrawn(false);
		h.setDrawTitleCentered(true);
		h.setTitleColor(EColors.lime.c());
		
		addObject(h);
	}
	
	@Override
	public void drawObject(int mXIn, int mYIn) {
		drawRect(startX, startY, endX, endY, 0xff000000);
		drawRect(startX + 1, startY + 1, endX - 1, endY - 1, 0xff444444);
		super.drawObject(mXIn, mYIn);
	}
	
	@Override
	public void actionPerformed(IEnhancedActionObject object, Object... args) {
		if (object instanceof CreatorToolButton) {
			parentCreator.setCurrentTool(((CreatorToolButton) object).getType());
		}

		//DO NOT PASS THE SUPER!
	}
	
	//-----------------------
	//CreatorToolBelt Methods
	//-----------------------
	
	private void buildTools() {
		int toolSize = 20; //arbitrary tool size
		
		//determine toolbelt dims
		int w = (toolWidth * toolSize) + toolWidth + 3; //add the width back so that there are gaps
		int h = 2; //starting height value
		
		//position each tool
		int curY = startY + 2;
		for (int c = 0; c < tools.size(); c++) {
			CreatorCategory cat = tools.get(c);
			int rows = (int) (Math.ceil((double) cat.getButtons().size() / (double) toolWidth));
			
			for (int i = 0; i < cat.getButtons().size(); i++) {
				CreatorToolButton button = cat.getButtons().get(i);
				int xPos = i % toolWidth; //x offset multiple starting at 0
				int yPos = i / toolWidth; //y offset multiple starting at 0
				
				int bx = startX + 2 + (toolSize * xPos) + xPos;
				int by = curY + (toolSize * yPos) + yPos;
				
				//add to list first then dimension
				addObject(button);
				button.setDimensions(bx, by, toolSize, toolSize);
			}
			
			//update height values
			curY += (rows * toolSize) + rows;
			h += (rows * toolSize) + rows;
			
			//add a small divider in between categories
			if (c < tools.size() - 1) {
				EGuiRect r = new EGuiRect(this, 0, 0, 0, 0, 0xff303030);
				EGuiRect t = new EGuiRect(this, 0, 0, 0, 0, EColors.vdgray);
				addObject(r, t);
				r.setDimensions(startX + 1, curY, w - 2, 3);
				t.setDimensions(startX + 1, curY + 1, w - 2, 1);
				
				curY += 4;
				h += 4;
			}
		}
		
		//add space to match top
		h += 1;
		
		//assign our dimensions
		setDimensions(w, h);
	}
	
	//-----------------------
	//CreatorToolBelt Getters
	//-----------------------
	
	public EArrayList<CreatorCategory> getTools() { return tools; }
	public CreatorGui getParentCreator() { return parentCreator; }
}
