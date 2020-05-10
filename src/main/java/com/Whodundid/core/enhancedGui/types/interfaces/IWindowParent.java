package com.Whodundid.core.enhancedGui.types.interfaces;

import com.Whodundid.core.util.storageUtil.EArrayList;
import com.Whodundid.core.util.storageUtil.EDimension;
import java.util.Stack;

//Author: Hunter Bragg

public interface IWindowParent extends IEnhancedGuiObject {

	/** Returns true if this object will remain on the hud when a RendererProxyGui is closed. */
	public boolean isPinned();
	/** Returns true if this window is currently maximized. */
	public boolean isMaximized();
	/** Returns true if this window can be pinned. */
	public boolean isPinnable();
	/** Returns true if this window can be maximized. */
	public boolean isMaximizable();
	/** Sets this object to remain drawn on the renderer even when an IRendererProxy gui is displayed. */
	public IWindowParent setPinned(boolean val);
	/** Sets ths window to be maximized. */
	public IWindowParent setMaximized(boolean val);
	/** Makes this window pinnable. */
	public IWindowParent setPinnable(boolean val);
	/** Makes this window maximizable. */
	public IWindowParent setMaximizable(boolean val);
	
	public void maximize();
	public void miniturize();
	
	public EDimension getPreMax();
	public IWindowParent setPreMax(EDimension dimIn);
	
	public boolean isOpWindow();
	public boolean isDebugWindow();
	public boolean showInLists();
	
	public Stack<Object> getGuiHistory();
	public IWindowParent setGuiHistory(Stack<Object> historyIn);
	
	public long getInitTime();
	
	
	public EArrayList<String> getAliases();
}
