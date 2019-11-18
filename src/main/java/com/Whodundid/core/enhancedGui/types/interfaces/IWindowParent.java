package com.Whodundid.core.enhancedGui.types.interfaces;

import java.util.Stack;

public interface IWindowParent extends IEnhancedGuiObject {

	public Stack<Object> getGuiHistory();
	public IWindowParent setGuiHistory(Stack<Object> historyIn);
}
