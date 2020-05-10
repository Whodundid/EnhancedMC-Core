package com.Whodundid.core.enhancedGui.guiObjects.utilityObjects;

import com.Whodundid.core.enhancedGui.guiObjects.basicObjects.EGuiImageBox;
import com.Whodundid.core.enhancedGui.types.WindowParent;
import com.Whodundid.core.enhancedGui.types.interfaces.IEnhancedActionObject;
import com.Whodundid.core.util.storageUtil.DynamicTextureHandler;
import net.minecraft.util.ResourceLocation;

public class TextureDisplayer extends WindowParent {
	
	private EGuiImageBox imageBox;
	private DynamicTextureHandler handler;
	
	public TextureDisplayer() { this(null); }
	public TextureDisplayer(DynamicTextureHandler textureIn) { handler = textureIn; }
	
	@Override
	public void initGui() {
		setDimensions(250, 250);
		setResizeable(true);
		setMinDims(100, 100);
		setObjectName("Texture Viewer");
	}
	
	@Override
	public void initObjects() {
		defaultHeader(this);
		
		ResourceLocation loc = handler != null ? handler.getTextureLocation() : null;
		imageBox = new EGuiImageBox(this, startX + 5, startY + 5, width - 10, height - 10, loc);
		
		addObject(imageBox);
	}
	
	@Override
	public void drawObject(int mXIn, int mYIn) {
		drawDefaultBackground();
		super.drawObject(mXIn, mYIn);
	}
	
	@Override
	public void actionPerformed(IEnhancedActionObject object, Object... args) {
		
	}
	
	@Override
	public void close() {
		if (handler != null) { handler.destroy(); }
		super.close();
	}

}
