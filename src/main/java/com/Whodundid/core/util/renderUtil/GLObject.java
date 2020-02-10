package com.Whodundid.core.util.renderUtil;

import com.Whodundid.core.EnhancedMC;
import com.google.common.base.Splitter;
import com.google.common.collect.Sets;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;

//Author: Hunter Bragg

public class GLObject {
	
	protected static Minecraft mc = Minecraft.getMinecraft();
	public static final Set<String> PROTOCOLS = Sets.newHashSet(new String[] {"http", "https"});
	public static final Splitter NEWLINE_SPLITTER = Splitter.on('\n');
	public static final ResourceLocation optionsBackground = new ResourceLocation("textures/gui/options_background.png");
	public static final ResourceLocation statIcons = new ResourceLocation("textures/gui/container/stats_icons.png");
	public static final ResourceLocation icons = new ResourceLocation("textures/gui/icons.png");
	protected EFontRenderer fontRenderer = EnhancedMC.getFontRenderer();
	protected RenderItem renderItem = mc.getRenderItem();
	protected ScaledResolution res;
	protected float glZLevel;
	
	//------------------------
	//opengl drawing functions
	//------------------------
	
	public static int drawString(Object o, double x, double y, EColors colorIn) { return drawString(o.toString(), x, y, colorIn.c()); }
	public static int drawCenteredString(Object o, double x, double y, EColors colorIn) { return drawCenteredString(o.toString(), x, y, colorIn.c()); }
	public static int drawStringWithShadow(Object o, double x, double y, EColors colorIn) { return drawStringWithShadow(o.toString(), x, y, colorIn.c()); }
	public static int drawCenteredStringWithShadow(Object o, double x, double y, EColors colorIn) { return drawCenteredStringWithShadow(o.toString(), x, y, colorIn.c()); }
	
	public static int drawString(Object o, double x, double y, int color) { return EnhancedMC.getFontRenderer().drawStringI(o.toString(), x, y, color); }
	public static int drawCenteredString(Object o, double x, double y, int color) { return EnhancedMC.getFontRenderer().drawStringI(o.toString(), x - EnhancedMC.getFontRenderer().getStringWidth(o.toString()) / 2, y, color); }
	public static int drawStringWithShadow(Object o, double x, double y, int color) { return EnhancedMC.getFontRenderer().drawStringWithShadowI(o.toString(), x, y, color); }
	public static int drawCenteredStringWithShadow(Object o, double x, double y, int color) { return EnhancedMC.getFontRenderer().drawStringWithShadowI(o.toString(), x - EnhancedMC.getFontRenderer().getStringWidth(o.toString()) / 2, y, color); }
	
	public static int drawString(String text, double x, double y, EColors colorIn) { return drawString(text, x, y, colorIn.c()); }
	public static int drawCenteredString(String text, double x, double y, EColors colorIn) { return drawCenteredString(text, x, y, colorIn.c()); }
	public static int drawStringWithShadow(String text, double x, double y, EColors colorIn) { return drawStringWithShadow(text, x, y, colorIn.c()); }
	public static int drawCenteredStringWithShadow(String text, double x, double y, EColors colorIn) { return drawCenteredStringWithShadow(text, x, y, colorIn.c()); }
	
	public static int drawString(String text, double x, double y, int color) { return EnhancedMC.getFontRenderer().drawStringI(text, x, y, color); }
	public static int drawCenteredString(String text, double x, double y, int color) { return EnhancedMC.getFontRenderer().drawStringI(text, x - EnhancedMC.getFontRenderer().getStringWidth(text) / 2, y, color); }
	public static int drawStringWithShadow(String text, double x, double y, int color) { return EnhancedMC.getFontRenderer().drawStringWithShadowI(text, x, y, color); }
	public static int drawCenteredStringWithShadow(String text, double x, double y, int color) { return EnhancedMC.getFontRenderer().drawStringWithShadowI(text, x - EnhancedMC.getFontRenderer().getStringWidth(text) / 2, y, color); }
	
	public static int drawStringC(Object o, double x, double y, EColors colorIn) { return drawStringC(o.toString(), x, y, colorIn.c()); }
	public static int drawStringS(Object o, double x, double y, EColors colorIn) { return drawStringS(o.toString(), x, y, colorIn.c()); }
	public static int drawStringCS(Object o, double x, double y, EColors colorIn) { return drawStringCS(o.toString(), x, y, colorIn.c()); }
	public static int drawStringC(Object o, double x, double y, int color) { return drawCenteredString(o.toString(), x, y, color); }
	public static int drawStringS(Object o, double x, double y, int color) { return drawStringWithShadow(o.toString(), x, y, color); }
	public static int drawStringCS(Object o, double x, double y, int color) { return drawCenteredStringWithShadow(o.toString(), x, y, color); }
	
	public static int drawStringC(String text, double x, double y, EColors colorIn) { return drawStringC(text, x, y, colorIn.c()); }
	public static int drawStringS(String text, double x, double y, EColors colorIn) { return drawStringS(text, x, y, colorIn.c()); }
	public static int drawStringCS(String text, double x, double y, EColors colorIn) { return drawStringCS(text, x, y, colorIn.c()); }
	public static int drawStringC(String text, double x, double y, int color) { return drawCenteredString(text, x, y, color); }
	public static int drawStringS(String text, double x, double y, int color) { return drawStringWithShadow(text, x, y, color); }
	public static int drawStringCS(String text, double x, double y, int color) { return drawCenteredStringWithShadow(text, x, y, color); }
	
	protected void drawMenuGradient() { drawGradientRect(0, 0, res.getScaledWidth(), res.getScaledHeight(), -1072689136, -804253680); }
	protected void drawCreativeTabHoveringText(String tabName, int mX, int mY) { drawHoveringText(Arrays.<String>asList(new String[] {tabName}), mX, mY); }
	
	protected void renderToolTip(ItemStack stack, int x, int y) {
		List<String> list = stack.getTooltip(mc.thePlayer, mc.gameSettings.advancedItemTooltips);
		
		for (int i = 0; i < list.size(); i++) {
			if (i == 0) { list.set(i, stack.getRarity().rarityColor + list.get(i)); }
			else { list.set(i, EnumChatFormatting.GRAY + list.get(i)); }
		}
		drawHoveringText(list, x, y);
	}
	
	protected void drawHoveringText(List<String> textLines, int mX, int mY) {
		if (!textLines.isEmpty()) {
			GlStateManager.disableDepth();
			int i = 0;
			
			for (String s : textLines) {
				int j = fontRenderer.getStringWidth(s);
				if (j > i) { i = j; } //find longest string
			}
			
			int l1 = mX + 12; //x offset
			int i2 = mY - 12; //y offset
			int k = 8; //initial h offset
			
			if (textLines.size() > 1) { k += 2 + (textLines.size() - 1) * 10; } //calculate h
			ScaledResolution res = new ScaledResolution(mc);
			if (l1 + i + 4 > res.getScaledWidth()) { l1 = res.getScaledWidth() - i - 4; } //clamp w to screen
			if (mY + k - 7 > res.getScaledHeight()) { i2 = res.getScaledHeight() - k - 5; } //clamp h to screen
			
			glZLevel = 300.0F;
			renderItem.zLevel = 300.0F;
			int l = -267386864;
			drawGradientRect(l1 - 3, i2 - 4, l1 + i + 3, i2 - 3, l, l); //top
			drawGradientRect(l1 - 3, i2 + k + 3, l1 + i + 3, i2 + k + 4, l, l); //bottom
			drawGradientRect(l1 - 3, i2 - 3, l1 + i + 3, i2 + k + 3, l, l); //background
			drawGradientRect(l1 - 4, i2 - 3, l1 - 3, i2 + k + 3, l, l); //left
			drawGradientRect(l1 + i + 3, i2 - 3, l1 + i + 4, i2 + k + 3, l, l); //right
			int i1 = 1347420415;
			int j1 = (i1 & 16711422) >> 1 | i1 & -16777216;
			drawGradientRect(l1 - 3, i2 - 3 + 1, l1 - 3 + 1, i2 + k + 3 - 1, i1, j1); //inner left
			drawGradientRect(l1 + i + 2, i2 - 3 + 1, l1 + i + 3, i2 + k + 3 - 1, i1, j1); //inner right
			drawGradientRect(l1 - 3, i2 - 3, l1 + i + 3, i2 - 3 + 1, i1, i1); //inner top
			drawGradientRect(l1 - 3, i2 + k + 2, l1 + i + 3, i2 + k + 3, j1, j1); //inner bottom
			
			for (int k1 = 0; k1 < textLines.size(); k1++) {
				String s1 = textLines.get(k1);
				drawStringWithShadow(s1, l1, i2, -1);
				if (k1 == 0) { i2 += 2; }
				i2 += 10;
			}
			
			glZLevel = 0.0F;
			renderItem.zLevel = 0.0F;
			GlStateManager.enableDepth();
		}
	}
	
	public static void drawHorizontalLine(double startX, double endX, double y, EColors color) { drawHorizontalLine(startX, endX, y, color.c()); }
	public static void drawHorizontalLine(double startX, double endX, double y, int color) {
		if (endX < startX) {
			double i = startX;
			startX = endX;
			endX = i;
		}
		drawRect(startX, y, endX + 1, y + 1, color);
	}
	
	public static void drawVerticalLine(double x, double startY, double endY, EColors color) { drawVerticalLine(x, startY, endY, color.c()); }
	public static void drawVerticalLine(double x, double startY, double endY, int color) {
		if (endY < startY) {
			double i = startY;
			startY = endY;
			endY = i;
		}
		drawRect(x, startY + 1, x + 1, endY, color);
	}
	
	public static void drawCircle(double posX, double posY, double radius, int detail, EColors color) { drawCircle(posX, posY, radius, detail, color.c()); }
	public static void drawCircle(double posX, double posY, double radius, int detail, int color) {
		GlStateManager.enableBlend();
		GlStateManager.disableTexture2D();
		GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
		EGLHelper.setColor(color);
		GL11.glBegin(GL11.GL_LINE_LOOP);
		for (int i = 0; i < detail; i++) {
			double theta = 2.0f * Math.PI * i / detail;
			double x = radius * Math.cos(theta);
			double y = radius * Math.sin(theta);
			GL11.glVertex2d(x + posX, y + posY);
		}
		GL11.glEnd();
		GlStateManager.enableTexture2D();
		GlStateManager.disableBlend();
	}
	
	public static void drawFilledCircle(double posX, double posY, double radius, int detail, EColors color) { drawFilledCircle(posX, posY, radius, detail, color.c()); }
	public static void drawFilledCircle(double posX, double posY, double radius, int detail, int color) {
		GlStateManager.enableBlend();
		GlStateManager.disableTexture2D();
		GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
		EGLHelper.setColor(color);
		GL11.glBegin(GL11.GL_TRIANGLE_FAN);
		double prevX = 0.0, prevY = 0.0;
		GL11.glVertex2d(posX, posY);
		for (int i = 0; i < detail + 1; i++) {
			double theta = 2.0f * Math.PI * i / detail;
			double x = radius * Math.cos(theta);
			double y = radius * Math.sin(theta);
			GL11.glVertex2d(x + posX, y + posY);
			GL11.glVertex2d(posX + prevX, posY + prevY);
			prevX = x;
			prevY = y;
		}
		GL11.glEnd();
		GlStateManager.enableTexture2D();
		GlStateManager.disableBlend();
	}
	
	public static void drawEllipse(double posX, double posY, double radiusX, double radiusY, int detail, EColors color) { drawEllipse(posX, posY, radiusX, radiusY, detail, color.c()); }
	public static void drawEllipse(double posX, double posY, double radiusX, double radiusY, int detail, int color) {
		GlStateManager.enableBlend();
		GlStateManager.disableTexture2D();
		GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
		EGLHelper.setColor(color);
		GL11.glBegin(GL11.GL_LINE_LOOP);
		for (int i = 0; i < detail + 1; i++) {
			double theta = 2.0f * Math.PI * i / detail;
			double x = radiusX * Math.cos(theta);
			double y = radiusY * Math.sin(theta);
			GL11.glVertex2d(x + posX, y + posY);
		}
		GL11.glEnd();
		GlStateManager.enableTexture2D();
		GlStateManager.disableBlend();
	}
	
	public static void drawFilledEllipse(double posX, double posY, double radiusX, double radiusY, int detail, EColors color) { drawFilledEllipse(posX, posY, radiusX, radiusY, detail, color.c()); }
	public static void drawFilledEllipse(double posX, double posY, double radiusX, double radiusY, int detail, int color) {
		GlStateManager.enableBlend();
		GlStateManager.disableTexture2D();
		GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
		EGLHelper.setColor(color);
		GL11.glBegin(GL11.GL_TRIANGLE_FAN);
		double prevX = 0.0, prevY = 0.0;
		GL11.glVertex2d(posX, posY);
		for (int i = 0; i < detail + 1; i++) {
			double theta = 2.0f * Math.PI * i / detail;
			double x = radiusX * Math.cos(theta);
			double y = radiusY * Math.sin(theta);
			GL11.glVertex2d(x + posX, y + posY);
			GL11.glVertex2d(posX + prevX, posY + prevY);
			prevX = x;
			prevY = y;
		}
		GL11.glEnd();
		GlStateManager.enableTexture2D();
		GlStateManager.disableBlend();
	}
	
	public static void drawRect(double left, double top, double right, double bottom, EColors colorIn) { drawRect(left, top, right, bottom, colorIn.c()); }
	public static void drawRect(double left, double top, double right, double bottom, int color) {
		if (left < right) {
			double i = left;
			left = right;
			right = i;
		}
		
		if (top < bottom) {
			double j = top;
			top = bottom;
			bottom = j;
		}
		
		Tessellator tessellator = Tessellator.getInstance();
		WorldRenderer worldrenderer = tessellator.getWorldRenderer();
		GlStateManager.enableBlend();
		GlStateManager.disableTexture2D();
		GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
		EGLHelper.setColor(color);
		worldrenderer.begin(7, DefaultVertexFormats.POSITION);
		worldrenderer.pos(left, bottom, 0).endVertex();
		worldrenderer.pos(right, bottom, 0).endVertex();
		worldrenderer.pos(right, top, 0).endVertex();
		worldrenderer.pos(left, top, 0).endVertex();
		tessellator.draw();
		GlStateManager.enableTexture2D();
		GlStateManager.disableBlend();
	}
	
	protected void drawGradientRect(double left, double top, double right, double bottom, EColors startColor, EColors endColor) { drawGradientRect(left, top, right, bottom, startColor.c(), endColor.c()); }
	protected void drawGradientRect(double left, double top, double right, double bottom, int startColor, int endColor) {
		float f = (startColor >> 24 & 255) / 255.0F;
		float f1 = (startColor >> 16 & 255) / 255.0F;
		float f2 = (startColor >> 8 & 255) / 255.0F;
		float f3 = (startColor & 255) / 255.0F;
		float f4 = (endColor >> 24 & 255) / 255.0F;
		float f5 = (endColor >> 16 & 255) / 255.0F;
		float f6 = (endColor >> 8 & 255) / 255.0F;
		float f7 = (endColor & 255) / 255.0F;
		GlStateManager.disableTexture2D();
		GlStateManager.enableBlend();
		GlStateManager.disableAlpha();
		GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
		GlStateManager.shadeModel(7425);
		Tessellator tessellator = Tessellator.getInstance();
		WorldRenderer worldrenderer = tessellator.getWorldRenderer();
		worldrenderer.begin(7, DefaultVertexFormats.POSITION_COLOR);
		worldrenderer.pos(right, top, glZLevel).color(f1, f2, f3, f).endVertex();
		worldrenderer.pos(left, top, glZLevel).color(f1, f2, f3, f).endVertex();
		worldrenderer.pos(left, bottom, glZLevel).color(f5, f6, f7, f4).endVertex();
		worldrenderer.pos(right, bottom, glZLevel).color(f5, f6, f7, f4).endVertex();
		tessellator.draw();
		GlStateManager.shadeModel(7424);
		GlStateManager.disableBlend();
		GlStateManager.enableAlpha();
		GlStateManager.enableTexture2D();
	}
	
	public void drawTexturedModalRect(double x, double y, double oX, double oY, double w, double h) {
		float f = 0.00390625F;
		float f1 = 0.00390625F;
		Tessellator tessellator = Tessellator.getInstance();
		WorldRenderer worldrenderer = tessellator.getWorldRenderer();
		worldrenderer.begin(7, DefaultVertexFormats.POSITION_TEX);
		worldrenderer.pos(x + 0, y + h, glZLevel).tex((oX + 0) * f, (oY + h) * f1).endVertex();
		worldrenderer.pos(x + w, y + h, glZLevel).tex((oX + w) * f, (oY + h) * f1).endVertex();
		worldrenderer.pos(x + w, y + 0, glZLevel).tex((oX + w) * f, (oY + 0) * f1).endVertex();
		worldrenderer.pos(x + 0, y + 0, glZLevel).tex((oX + 0) * f, (oY + 0) * f1).endVertex();
		tessellator.draw();
	}
	
	public void drawTexturedModalRect(float x, float y, int minU, int minV, int maxU, int maxV) {
		float f = 0.00390625F;
		float f1 = 0.00390625F;
		Tessellator tessellator = Tessellator.getInstance();
		WorldRenderer worldrenderer = tessellator.getWorldRenderer();
		worldrenderer.begin(7, DefaultVertexFormats.POSITION_TEX);
		worldrenderer.pos(x + 0.0F, y + maxV, glZLevel).tex((minU + 0) * f, (minV + maxV) * f1).endVertex();
		worldrenderer.pos(x + maxU, y + maxV, glZLevel).tex((minU + maxU) * f, (minV + maxV) * f1).endVertex();
		worldrenderer.pos(x + maxU, y + 0.0F, glZLevel).tex((minU + maxU) * f, (minV + 0) * f1).endVertex();
		worldrenderer.pos(x + 0.0F, y + 0.0F, glZLevel).tex((minU + 0) * f, (minV + 0) * f1).endVertex();
		tessellator.draw();
	}
	
	public void drawTexturedModalRect(float x, float y, float minU, float minV, float maxU, float maxV) {
		float f = 0.00390625F;
		Tessellator tessellator = Tessellator.getInstance();
		WorldRenderer worldrenderer = tessellator.getWorldRenderer();
		worldrenderer.begin(7, DefaultVertexFormats.POSITION_TEX);
		worldrenderer.pos(x, y + maxV, glZLevel).tex(minU * f, (minV + maxV) * f).endVertex();
		worldrenderer.pos(x + maxU, y + maxV, glZLevel).tex((minU + maxU) * f, (minV + maxV) * f).endVertex();
		worldrenderer.pos(x + maxU, y, glZLevel).tex((minU + maxU) * f, minV * f).endVertex();
		worldrenderer.pos(x, y, glZLevel).tex(minU * f, minV * f).endVertex();
		tessellator.draw();
	}
	
	public void drawTexturedModalRect(double x, double y, TextureAtlasSprite sprite, int wIn, int hIn) {
		Tessellator tessellator = Tessellator.getInstance();
		WorldRenderer worldrenderer = tessellator.getWorldRenderer();
		worldrenderer.begin(7, DefaultVertexFormats.POSITION_TEX);
		worldrenderer.pos(x + 0, y + hIn, glZLevel).tex(sprite.getMinU(), sprite.getMaxV()).endVertex();
		worldrenderer.pos(x + wIn, y + hIn, glZLevel).tex(sprite.getMaxU(), sprite.getMaxV()).endVertex();
		worldrenderer.pos(x + wIn, y + 0, glZLevel).tex(sprite.getMaxU(), sprite.getMinV()).endVertex();
		worldrenderer.pos(x + 0, y + 0, glZLevel).tex(sprite.getMinU(), sprite.getMinV()).endVertex();
		tessellator.draw();
	}
	
	public static void drawModalRectWithCustomSizedTexture(int x, int y, float u, float v, int w, int h, float textureWidth, float textureHeight) {
		float f = 1.0F / textureWidth;
		float f1 = 1.0F / textureHeight;
		Tessellator tessellator = Tessellator.getInstance();
		WorldRenderer worldrenderer = tessellator.getWorldRenderer();
		worldrenderer.begin(7, DefaultVertexFormats.POSITION_TEX);
		worldrenderer.pos(x, y + h, 0).tex(u * f, (v + h) * f1).endVertex();
		worldrenderer.pos(x + w, y + h, 0).tex((u + w) * f, (v + h) * f1).endVertex();
		worldrenderer.pos(x + w, y, 0).tex((u + w) * f, v * f1).endVertex();
		worldrenderer.pos(x, y, 0).tex(u * f, v * f1).endVertex();
		tessellator.draw();
	}
	
	public static void drawScaledCustomSizeModalRect(int x, int y, float u, float v, int uWidth, int vHeight, int w, int h, float tileWidth, float tileHeight) {
		float f = 1.0F / tileWidth;
		float f1 = 1.0F / tileHeight;
		Tessellator tessellator = Tessellator.getInstance();
		WorldRenderer worldrenderer = tessellator.getWorldRenderer();
		worldrenderer.begin(7, DefaultVertexFormats.POSITION_TEX);
		worldrenderer.pos(x, y + h, 0).tex(u * f, (v + vHeight) * f1).endVertex();
		worldrenderer.pos(x + w, y + h, 0).tex((u + uWidth) * f, (v + vHeight) * f1).endVertex();
		worldrenderer.pos(x + w, y, 0).tex((u + uWidth) * f, v * f1).endVertex();
		worldrenderer.pos(x, y, 0).tex(u * f, v * f1).endVertex();
		tessellator.draw();
	}
	
	public static void scissor(int startX, int startY, int endX, int endY) { scissor(startX, startY, endX, endY, false); }
	public static void scissor(int startX, int startY, int endX, int endY, boolean useGlY) {
		GL11.glEnable(GL11.GL_SCISSOR_TEST);
		ScaledResolution res = new ScaledResolution(Minecraft.getMinecraft());
		int scale = res.getScaleFactor();
		int w = (endX - startX) * scale;
		int h = (endY - startY) * scale;
		int x = startX * scale;
		int y = useGlY ? startY * scale : (Display.getHeight() - (startY * scale) - h);
		if (w >= 0 && h >= 0) { GL11.glScissor(x, y, w, h); }
	}
	public static void endScissor() { GL11.glDisable(GL11.GL_SCISSOR_TEST); }
	
	public static void drawTexture(double x, double y, double w, double h) { drawTexture(x, y, w, h, 0, 0, w, h, 0.0D); }
	public static void drawTexture(double x, double y, double w, double h, double oX, double oY, double tW, double tH) { drawTexture(x, y, w, h, oX, oY, tW, tH, 0.0D); }
	public static void drawTexture(double x, double y, double w, double h, double oX, double oY, double tW, double tH, double z) {
		double f = 1.0 / tW;
		double f1 = 1.0 / tH;
		Tessellator tessellator = Tessellator.getInstance();
		WorldRenderer worldrenderer = tessellator.getWorldRenderer();
		worldrenderer.begin(7, DefaultVertexFormats.POSITION_TEX);
		worldrenderer.pos(x, y + h, z).tex(oX * f, (oY + h) * f1).endVertex();
		worldrenderer.pos(x + w, y + h, z).tex((oX + w) * f, (oY + h) * f1).endVertex();
		worldrenderer.pos(x + w, y, z).tex((oX + w) * f, oY * f1).endVertex();
		worldrenderer.pos(x, y, z).tex(oX * f, oY * f1).endVertex();
		tessellator.draw();
	}
	public static void drawTexture(double posX, double posY, double w, double h, ResourceLocation imageIn) {
		if (imageIn != null) {
			GlStateManager.enableAlpha();
			GlStateManager.enableBlend();
			GlStateManager.color(2.0f, 2.0f, 2.0f, 2.0f);
			mc.renderEngine.bindTexture(imageIn);
			drawTexture(posX, posY, w, h);
			GlStateManager.disableAlpha();
			GlStateManager.disableBlend();
		}
	}
}
