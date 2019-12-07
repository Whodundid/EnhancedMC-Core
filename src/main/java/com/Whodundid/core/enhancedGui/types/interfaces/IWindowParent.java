package com.Whodundid.core.enhancedGui.types.interfaces;

import java.util.Stack;

public interface IWindowParent extends IEnhancedGuiObject {

	/** Returns true if this object will remain on the hud when a RendererProxyGui is closed. */
	public boolean isPinned();
	/**Returns true if this window can be pinned. */
	public boolean isPinnable();
	/** Sets this object to remain drawn on the renderer even when an IRendererProxy gui is displayed. */
	public IEnhancedGuiObject setPinned(boolean val);
	/** Makes this window pinnable. */
	public IEnhancedGuiObject setPinnable(boolean val);
	
	public Stack<Object> getGuiHistory();
	public IWindowParent setGuiHistory(Stack<Object> historyIn);
}
