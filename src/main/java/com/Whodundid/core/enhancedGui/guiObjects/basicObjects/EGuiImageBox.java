package com.Whodundid.core.enhancedGui.guiObjects.basicObjects;

import com.Whodundid.core.enhancedGui.types.EnhancedGuiObject;
import com.Whodundid.core.enhancedGui.types.interfaces.IEnhancedGuiObject;
import com.Whodundid.core.util.renderUtil.EColors;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;

public class EGuiImageBox extends EnhancedGuiObject {
	
	ResourceLocation image = null;
	int borderColor = EColors.black.c();
	int backgroundColor = EColors.vdgray.c();
	boolean drawImage = true;
	boolean drawBorder = true;
	boolean drawBackground = true;
	boolean centerImage = true;
	
	public EGuiImageBox(IEnhancedGuiObject objIn, int xIn, int yIn, int widthIn, int heightIn) { this(objIn, xIn, yIn, widthIn, heightIn, null); }
	public EGuiImageBox(IEnhancedGuiObject objIn, int xIn, int yIn, int widthIn, int heightIn, ResourceLocation imageIn) {
		init(objIn, xIn, yIn, widthIn, heightIn);
		image = imageIn;
	}
	
	@Override
	public void drawObject(int mXIn, int mYIn, float ticks) {
		if (drawBorder) { drawRect(startX, startY, endX, endY, borderColor); }
		if (drawBackground) { drawRect(startX + 1, startY + 1, endX - 1, endY - 1, backgroundColor); }
		if (drawImage) {
			if (image != null) {
				GlStateManager.enableAlpha();
				GlStateManager.enableBlend();
				
				GlStateManager.color(2.0f, 2.0f, 2.0f, 2.0f);
				mc.renderEngine.bindTexture(image);
				
				double posX = startX + 2;
				double posY = startY + 2;
				double w = width - 4;
				double h = height - 4;
				
				if (centerImage) {
					double smaller = w < h ? w : h;
					
					posX = startX + 2 + ((w - smaller) / 2);
					posY = startY + 2 + ((h - smaller) / 2);
					
					w = smaller;
					h = smaller;
				}
				
				drawTexture(posX, posY, w, h);
				
				GlStateManager.disableAlpha();
				GlStateManager.disableBlend();
			}
		}
		
		super.drawObject(mXIn, mYIn, ticks);
	}
	
	public ResourceLocation getImage() { return image; }
	public int getBorderColor() { return borderColor; }
	public int getBackgroundColor() { return backgroundColor; }
	public boolean drawsImage() { return drawImage; }
	public boolean drawsBorder() { return drawBorder; }
	public boolean drawsBackground() { return drawBackground; }
	
	public EGuiImageBox setImage(ResourceLocation imageIn) { image = imageIn; return this; }
	public EGuiImageBox setDrawImage(boolean val) { drawImage = val; return this; }
	public EGuiImageBox setDrawBorder(boolean val) { drawBorder = val; return this; }
	public EGuiImageBox setDrawBackground(boolean val) { drawBackground = val; return this; }
	public EGuiImageBox setCenterImage(boolean val) { centerImage = val; return this; }
	public EGuiImageBox setBorderColor(EColors colorIn) { return setBorderColor(colorIn.c()); }
	public EGuiImageBox setBorderColor(int colorIn) { borderColor = colorIn; return this; }
	public EGuiImageBox setBackgroundColor(EColors colorIn) { return setBackgroundColor(colorIn.c()); }
	public EGuiImageBox setBackgroundColor(int colorIn) { backgroundColor = colorIn; return this; }
}
