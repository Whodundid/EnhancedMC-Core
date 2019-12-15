package com.Whodundid.core.renderer;

/** A bridge implementation that passes a mouse position along to the renderer. */
public interface IRendererProxy {
	public int getMX();
	public int getMY();
}
