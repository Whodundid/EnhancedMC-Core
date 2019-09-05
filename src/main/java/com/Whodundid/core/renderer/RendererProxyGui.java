package com.Whodundid.core.renderer;

import com.Whodundid.core.EnhancedMC;
import com.Whodundid.core.enhancedGui.EnhancedGui;

/** A type of EnhancedGui which sends it's inputs to the EnhancedMCRenderer */
public class RendererProxyGui extends EnhancedGui implements IRendererProxy {
	
	EnhancedMCRenderer renderer = null;
	
	@Override
	public void initGui() {
		renderer = EnhancedMC.getRenderer();
		super.initGui();
	}
	
	@Override
	public void parseMousePosition(int mX, int mY) {
		if (renderer != null) { renderer.parseMousePosition(mX, mY); }
		super.parseMousePosition(mX, mY);
	}
	
	@Override
	public void mousePressed(int mX, int mY, int button) {
		if (renderer != null) { renderer.mousePressed(mX, mY, button); }
		super.mousePressed(mX, mY, button);
	}
	
	@Override
	public void mouseReleased(int mX, int mY, int button) {
		if (renderer != null) { renderer.mouseReleased(mX, mY, button); }
		super.mouseReleased(mX, mY, button);
	}
	
	@Override
	public void mouseDragged(int mX, int mY, int button, long timeSinceLastClick) {
		renderer.mouseDragged(mX, mY, button, timeSinceLastClick);
		super.mouseDragged(mX, mY, button, timeSinceLastClick);
	}
	
	@Override
	public void keyPressed(char typedChar, int keyCode) {
		renderer.keyPressed(typedChar, keyCode);
		super.keyPressed(typedChar, keyCode);
	}
	
	@Override
	public void keyReleased(char typedChar, int keyCode) {
		renderer.keyReleased(typedChar, keyCode);
		super.keyReleased(typedChar, keyCode);
	}
	
    //------------------------
  	//IRendererProxy Overrides
  	//------------------------
    
    @Override public int getMX() { return mX; }
    @Override public int getMY() { return mY; }
}
