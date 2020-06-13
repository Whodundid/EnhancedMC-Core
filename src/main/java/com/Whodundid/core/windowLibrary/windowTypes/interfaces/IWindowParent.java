package com.Whodundid.core.windowLibrary.windowTypes.interfaces;

import com.Whodundid.core.util.renderUtil.ScreenLocation;
import com.Whodundid.core.util.storageUtil.EArrayList;
import com.Whodundid.core.util.storageUtil.EDimension;
import java.util.Stack;

//Author: Hunter Bragg

public interface IWindowParent extends IWindowObject {

	/** Returns true if this object will remain on the hud when a RendererProxyGui is closed. */
	public boolean isPinned();
	/** Returns true if this window is currently maximized. */
	public boolean isMaximized();
	/** Returns true if this window is currently minimized. */
	public boolean isMinimized();
	/** Returns true if this window can be pinned. */
	public boolean isPinnable();
	/** Returns true if this window can be maximized. */
	public boolean isMaximizable();
	/** Returns true if this window can be minimized. */
	public boolean isMinimizable();
	/** Sets this object to remain drawn on the renderer even when an IRendererProxy gui is displayed. */
	public IWindowParent setPinned(boolean val);
	/** Sets ths window to be maximized. */
	public IWindowParent setMaximized(ScreenLocation position);
	/** Sets this window to be minimized. */
	public IWindowParent setMinimized(boolean val);
	/** Returns the way this window is currently maximized. If the window is not maximized, this will return ScreenLocation.out. */
	public ScreenLocation getMaximizedPosition();
	/** Makes this window pinnable. */
	public IWindowParent setPinnable(boolean val);
	/** Makes this window maximizable. */
	public IWindowParent setMaximizable(boolean val);
	/** Makes this window minimizeable. */
	public IWindowParent setMinimizable(boolean val);
	/** Sets this object to draw even when minimized. Used for TaskBar previews. */
	public IWindowParent setDrawWhenMinimized(boolean val);
	/** Returns true if this object will draw even if minimized. */
	public boolean drawsWhileMinimized();
	
	public void maximize();
	public void miniaturize();
	
	public EDimension getPreMax();
	public IWindowParent setPreMax(EDimension dimIn);
	
	public boolean isOpWindow();
	public boolean isDebugWindow();
	public boolean showInLists();
	
	public Stack<Object> getWindowHistory();
	public IWindowParent setWindowHistory(Stack<Object> historyIn);
	
	public void renderTaskBarPreview(int xPos, int yPos);
	
	public void drawHighlightBorder();
	
	public long getInitTime();
	
	public EArrayList<String> getAliases();
	
}
