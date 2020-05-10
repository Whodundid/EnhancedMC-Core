package com.Whodundid.guiCreator.handles;

import com.Whodundid.guiCreator.gui.CreatorGui;

public abstract class TypeHandler {
	
	public CreatorGui parentCreator;
	
	protected TypeHandler(CreatorGui guiIn) {
		parentCreator = guiIn;
	}
	
	public abstract void handlePress(int x, int y, int button);
	public abstract void handleRelease(int x, int y, int button);

	public CreatorGui getParentCreator() { return parentCreator; }
	
}
