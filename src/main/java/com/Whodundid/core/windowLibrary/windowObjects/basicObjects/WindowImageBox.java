package com.Whodundid.core.windowLibrary.windowObjects.basicObjects;

import com.Whodundid.core.app.EMCApp;
import com.Whodundid.core.util.EUtil;
import com.Whodundid.core.util.renderUtil.EColors;
import com.Whodundid.core.util.resourceUtil.EResource;
import com.Whodundid.core.util.resourceUtil.ResourceUtil;
import com.Whodundid.core.util.storageUtil.EArrayList;
import com.Whodundid.core.windowLibrary.windowTypes.WindowObject;
import com.Whodundid.core.windowLibrary.windowTypes.interfaces.IWindowObject;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;

//Author: Hunter Bragg

public class WindowImageBox extends WindowObject {
	
	EArrayList<ResourceLocation> images = new EArrayList();
	int borderColor = EColors.black.c();
	int backgroundColor = EColors.vdgray.c();
	boolean drawImage = true;
	boolean drawBorder = true;
	boolean drawBackground = true;
	boolean centerImage = true;
	boolean drawStretched = false;
	boolean singleImage = false;
	String nullText = "Texture is null!";
	int nullTextColor = EColors.lred.intVal;
	long updateInterval = 500l;
	long timeSince = 0l;
	int curImage = 0;
	
	public WindowImageBox(IWindowObject objIn, int xIn, int yIn, int widthIn, int heightIn) { this(objIn, xIn, yIn, widthIn, heightIn, (EResource) null); }
	public WindowImageBox(IWindowObject objIn, int xIn, int yIn, int widthIn, int heightIn, ResourceLocation imageIn) {
		init(objIn, xIn, yIn, widthIn, heightIn);
		images.add(imageIn);
		singleImage = true;
	}
	
	public WindowImageBox(IWindowObject objIn, int xIn, int yIn, int widthIn, int heightIn, EResource imageIn) {
		init(objIn, xIn, yIn, widthIn, heightIn);
		EUtil.ifNotNullDo(imageIn, i -> images.add(i.getResource()));
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
				
				double posX = startX + 2;
				double posY = startY + 2;
				double w = width - 4;
				double h = height - 4;
				double smaller = w <= h ? w : h;
				
				if (centerImage) {
					posX = startX + 2 + ((w - smaller) / 2);
					posY = startY + 2 + ((h - smaller) / 2);
					
					w = smaller;
					h = smaller;
				}
				else if (!drawStretched) {
					ResourceLocation cur = images.get(curImage);
					
					double imgW = ResourceUtil.getImageWidth(cur);
					double imgH = ResourceUtil.getImageHeight(cur);
					
					//image ratio equations
					
					if (w <= h) {
						h = MathHelper.clamp_double((w / imgW) * imgH, 0, (height - 4));
						w = (h / imgH) * imgW;
					}
					else {
						w = MathHelper.clamp_double((h / imgH) * imgW, 0, (width - 4));
						h = (w / imgW) * imgH;
					}
					
					posY = startY + 2 + ((height - 4) - h) / 2;
					posX = startX + 2 + ((width - 4) - w) / 2;
				}
				
				if (singleImage) { bindTexture(images.get(0)); }
				else {
					if (System.currentTimeMillis() - timeSince >= updateInterval) {
						curImage++;
						if (curImage == images.size()) { curImage = 0; }
						timeSince = System.currentTimeMillis();
					}
					bindTexture(images.get(curImage));
				}
				
				GlStateManager.color(2.0f, 2.0f, 2.0f, 2.0f);
				
				scissor(startX, startY, endX, endY);
				drawTexture(posX, posY, w, h);
				endScissor();
				
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
	public boolean drawsStretched() { return drawStretched; }
	
	public WindowImageBox setImage(ResourceLocation imageIn) {
		if (imageIn != null) {
			images = new EArrayList(imageIn);
			singleImage = true;
		}
		return this;
	}
	
	public WindowImageBox setImage(EResource imageIn) {
		if (imageIn != null && imageIn.getHandler() != null) {
			images = new EArrayList<ResourceLocation>(imageIn.getResource());
			singleImage = true;
		}
		return this;
	}
	
	public WindowImageBox setImages(ResourceLocation... imagesIn) {
		if (imagesIn != null) {
			images = new EArrayList((Object[]) imagesIn);
			singleImage = images.size() == 1;
		}
		return this;
	}
	
	public WindowImageBox setImages(EResource... imagesIn) {
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
	
	public WindowImageBox setImages(EMCApp appIn) {
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
	
	public WindowImageBox setDrawStretched(boolean val) { drawStretched = val; return this; }
	public WindowImageBox setNullText(String textIn) { nullText = textIn; return this; }
	public WindowImageBox setNullTextColor(EColors colorIn) { return setNullTextColor(colorIn.intVal); }
	public WindowImageBox setNullTextColor(int colorIn) { nullTextColor = colorIn; return this; }
	public WindowImageBox setUpdateInterval(long time) { updateInterval = (long) MathHelper.clamp_double(time, 0, Long.MAX_VALUE); return this; }
	public WindowImageBox setDrawImage(boolean val) { drawImage = val; return this; }
	public WindowImageBox setDrawBorder(boolean val) { drawBorder = val; return this; }
	public WindowImageBox setDrawBackground(boolean val) { drawBackground = val; return this; }
	public WindowImageBox setCenterImage(boolean val) { centerImage = val; return this; }
	public WindowImageBox setBorderColor(EColors colorIn) { return setBorderColor(colorIn.c()); }
	public WindowImageBox setBorderColor(int colorIn) { borderColor = colorIn; return this; }
	public WindowImageBox setBackgroundColor(EColors colorIn) { return setBackgroundColor(colorIn.c()); }
	public WindowImageBox setBackgroundColor(int colorIn) { backgroundColor = colorIn; return this; }
	
}
