package com.Whodundid.core.enhancedGui.guiObjects.basicObjects;

import com.Whodundid.core.app.EMCApp;
import com.Whodundid.core.enhancedGui.types.EnhancedGuiObject;
import com.Whodundid.core.enhancedGui.types.interfaces.IEnhancedGuiObject;
import com.Whodundid.core.util.renderUtil.EColors;
import com.Whodundid.core.util.resourceUtil.EResource;
import com.Whodundid.core.util.storageUtil.EArrayList;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;

//Author: Hunter Bragg

public class EGuiImageBox extends EnhancedGuiObject {
	
	EArrayList<ResourceLocation> images = new EArrayList();
	int borderColor = EColors.black.c();
	int backgroundColor = EColors.vdgray.c();
	boolean drawImage = true;
	boolean drawBorder = true;
	boolean drawBackground = true;
	boolean centerImage = true;
	boolean singleImage = false;
	String nullText = "Texture is null!";
	int nullTextColor = EColors.lred.intVal;
	long updateInterval = 500l;
	long timeSince = 0l;
	int curImage = 0;
	
	public EGuiImageBox(IEnhancedGuiObject objIn, int xIn, int yIn, int widthIn, int heightIn) { this(objIn, xIn, yIn, widthIn, heightIn, null); }
	public EGuiImageBox(IEnhancedGuiObject objIn, int xIn, int yIn, int widthIn, int heightIn, ResourceLocation imageIn) {
		init(objIn, xIn, yIn, widthIn, heightIn);
		images.add(imageIn);
		singleImage = true;
	}
	
	@Override
	public void drawObject(int mXIn, int mYIn) {
		if (drawBorder) { drawRect(startX, startY, endX, endY, borderColor); }
		if (drawBackground) { drawRect(startX + 1, startY + 1, endX - 1, endY - 1, backgroundColor); }
		if (drawImage) {
			if (images.isNotEmpty() && images.get(0) != null) {
				GlStateManager.enableAlpha();
				GlStateManager.enableBlend();
				
				GlStateManager.color(2.0f, 2.0f, 2.0f, 2.0f);
				
				if (singleImage) { bindTexture(images.get(0)); }
				else {
					if (System.currentTimeMillis() - timeSince >= updateInterval) {
						curImage++;
						if (curImage == images.size()) { curImage = 0; }
						timeSince = System.currentTimeMillis();
					}
					bindTexture(images.get(curImage));
				}
				
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
			else {
				drawStringC(nullText, midX, midY - (mc.fontRendererObj.FONT_HEIGHT / 2), nullTextColor);
			}
		}
		
		super.drawObject(mXIn, mYIn);
	}
	
	public EArrayList<ResourceLocation> getImages() { return images; }
	public int getBorderColor() { return borderColor; }
	public int getBackgroundColor() { return backgroundColor; }
	public long getUpdateInterval() { return updateInterval; }
	public boolean drawsImage() { return drawImage; }
	public boolean drawsBorder() { return drawBorder; }
	public boolean drawsBackground() { return drawBackground; }
	
	public EGuiImageBox setImage(ResourceLocation imageIn) {
		if (imageIn != null) {
			images = new EArrayList(imageIn);
			singleImage = true;
		}
		return this;
	}
	
	public EGuiImageBox setImage(EResource imageIn) {
		if (imageIn != null && imageIn.getHandler() != null) {
			images = new EArrayList<ResourceLocation>(imageIn.getResource());
			singleImage = true;
		}
		return this;
	}
	
	public EGuiImageBox setImages(ResourceLocation... imagesIn) {
		if (imagesIn != null) {
			images = new EArrayList(imagesIn);
			singleImage = images.size() == 1;
		}
		return this;
	}
	
	public EGuiImageBox setImages(EResource... imagesIn) {
		if (imagesIn != null) {
			EArrayList<ResourceLocation> list = new EArrayList();
			for (EResource r : imagesIn) {
				list.add(r.getResource());
			}
			images = list;
			singleImage = images.size() == 1;
		}
		return this;
	}
	
	public EGuiImageBox setImages(EMCApp appIn) {
		if (appIn != null) {
			EArrayList<ResourceLocation> list = new EArrayList();
			for (EResource r : appIn.getLogo()) {
				list.add(r.getResource());
			}
			images = list;
			singleImage = images.size() == 1;
		}
		return this;
	}
	
	public EGuiImageBox setNullText(String textIn) { nullText = textIn; return this; }
	public EGuiImageBox setNullTextColor(EColors colorIn) { return setNullTextColor(colorIn.intVal); }
	public EGuiImageBox setNullTextColor(int colorIn) { nullTextColor = colorIn; return this; }
	public EGuiImageBox setUpdateInterval(long time) { updateInterval = (long) MathHelper.clamp_double(time, 0, Long.MAX_VALUE); return this; }
	public EGuiImageBox setDrawImage(boolean val) { drawImage = val; return this; }
	public EGuiImageBox setDrawBorder(boolean val) { drawBorder = val; return this; }
	public EGuiImageBox setDrawBackground(boolean val) { drawBackground = val; return this; }
	public EGuiImageBox setCenterImage(boolean val) { centerImage = val; return this; }
	public EGuiImageBox setBorderColor(EColors colorIn) { return setBorderColor(colorIn.c()); }
	public EGuiImageBox setBorderColor(int colorIn) { borderColor = colorIn; return this; }
	public EGuiImageBox setBackgroundColor(EColors colorIn) { return setBackgroundColor(colorIn.c()); }
	public EGuiImageBox setBackgroundColor(int colorIn) { backgroundColor = colorIn; return this; }
}
