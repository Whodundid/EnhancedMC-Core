package com.Whodundid.guiCreator.gui.creatorParts.toolbelt.toolbeltParts;

import java.util.List;
import com.Whodundid.core.util.storageUtil.EArrayList;
import com.Whodundid.guiCreator.gui.creatorParts.toolbelt.CreatorToolBelt;
import com.Whodundid.guiCreator.util.CreatorTool;
import com.Whodundid.guiCreator.util.ToolCategory;

public class CreatorCategory {
	
	CreatorToolBelt parentToolBelt;
	public ToolCategory cat;
	public EArrayList<CreatorToolButton> buttons = new EArrayList();
	
	public CreatorCategory(CreatorToolBelt toolBeltIn, ToolCategory catIn) {
		cat = catIn;
		parentToolBelt = toolBeltIn;
	}
	
	public CreatorCategory add(CreatorToolButton buttonIn) {
		buttons.add(buttonIn);
		return this;
	}
	
	public CreatorCategory addToolType(List<CreatorTool> typesIn) {
		for (CreatorTool t : typesIn) { buttons.add(new CreatorToolButton(parentToolBelt, this, t)); }
		return this;
	}
	
	public CreatorCategory addToolType(CreatorTool... typesIn) {
		for (CreatorTool t : typesIn) { buttons.add(new CreatorToolButton(parentToolBelt, this, t)); }
		return this;
	}
	
	public ToolCategory getCat() { return cat; }
	public EArrayList<CreatorToolButton> getButtons() { return buttons; }
	
	public static CreatorCategory from(CreatorToolBelt toolBeltIn, ToolCategory catType, List<CreatorTool> typesIn) {
		return new CreatorCategory(toolBeltIn, catType).addToolType(typesIn);
	}
}