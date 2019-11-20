package com.Whodundid.core.enhancedGui.guiUtil;

import com.Whodundid.core.EnhancedMC;
import com.Whodundid.core.util.miscUtil.EFontRenderer;
import com.Whodundid.core.util.renderUtil.EGLHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

//Last edited: Jan 3, 2019
//First Added: Dec 18, 2018
//Author: Hunter Bragg

public class EGui {
	
	protected static Minecraft mc = Minecraft.getMinecraft();
	protected EFontRenderer fontRenderer = EnhancedMC.getFontRenderer();
	protected RenderItem renderItem = mc.getRenderItem();
	public static final ResourceLocation optionsBackground = new ResourceLocation("textures/gui/options_background.png");
    public static final ResourceLocation statIcons = new ResourceLocation("textures/gui/container/stats_icons.png");
    public static final ResourceLocation icons = new ResourceLocation("textures/gui/icons.png");
    protected float zLevel;
	
    public static int drawString(String text, int x, int y, int color) { return EnhancedMC.getFontRenderer().drawStringI(text, x, y, color); }
    public static int drawCenteredString(String text, int x, int y, int color) { return EnhancedMC.getFontRenderer().drawStringI(text, x - EnhancedMC.getFontRenderer().getStringWidth(text) / 2, y, color); }
    public static int drawStringWithShadow(String text, int x, int y, int color) { return EnhancedMC.getFontRenderer().drawStringWithShadowI(text, x, y, color); }
    public static int drawCenteredStringWithShadow(String text, int x, int y, int color) { return EnhancedMC.getFontRenderer().drawStringWithShadowI(text, x - EnhancedMC.getFontRenderer().getStringWidth(text) / 2, y, color); }
    public static int drawString(String text, int x, int y, int color, float height) { return EnhancedMC.getFontRenderer().drawStringIH(text, x, y, color, height); }
    public static int drawCenteredString(String text, int x, int y, int color, float height) { return EnhancedMC.getFontRenderer().drawStringIH(text, x - EnhancedMC.getFontRenderer().getStringWidth(text) / 2, y, color, height); }
    public static int drawStringWithShadow(String text, int x, int y, int color, float height) { return EnhancedMC.getFontRenderer().drawStringWithShadowIH(text, x, y, color, height); }
    public static int drawCenteredStringWithShadow(String text, int x, int y, int color, float height) { return EnhancedMC.getFontRenderer().drawStringWithShadowIH(text, x - EnhancedMC.getFontRenderer().getStringWidth(text) / 2, y, color, height); }
	
    protected static void drawHorizontalLine(int startX, int endX, int y, int color) {
        if (endX < startX) {
            int i = startX;
            startX = endX;
            endX = i;
        }
        drawRect(startX, y, endX + 1, y + 1, color);
    }
    
    protected static void drawVerticalLine(int x, int startY, int endY, int color) {
        if (endY < startY) {
            int i = startY;
            startY = endY;
            endY = i;
        }
        drawRect(x, startY + 1, x + 1, endY, color);
    }
    
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
        	prevX = x; prevY = y;
        }
        GL11.glEnd();
        GlStateManager.enableTexture2D();
        GlStateManager.disableBlend();
    }
    
    public static void drawElipse(double posX, double posY, double radiusX, double radiusY, int detail, int color) {
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
    
    public static void drawFilledElipse(double posX, double posY, double radiusX, double radiusY, int detail, int color) {
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
        	prevX = x; prevY = y;
        }
        GL11.glEnd();
        GlStateManager.enableTexture2D();
        GlStateManager.disableBlend();
    }
    
    public static void drawRect(int left, int top, int right, int bottom, int color) {
        if (left < right) {
            int i = left;
            left = right;
            right = i;
        }

        if (top < bottom) {
            int j = top;
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
        worldrenderer.pos(left, bottom, 0.0D).endVertex();
        worldrenderer.pos(right, bottom, 0.0D).endVertex();
        worldrenderer.pos(right, top, 0.0D).endVertex();
        worldrenderer.pos(left, top, 0.0D).endVertex();
        tessellator.draw();
        GlStateManager.enableTexture2D();
        GlStateManager.disableBlend();
    }
    
    protected void drawGradientRect(int left, int top, int right, int bottom, int startColor, int endColor) {
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
        worldrenderer.pos(right, top, zLevel).color(f1, f2, f3, f).endVertex();
        worldrenderer.pos(left, top, zLevel).color(f1, f2, f3, f).endVertex();
        worldrenderer.pos(left, bottom, zLevel).color(f5, f6, f7, f4).endVertex();
        worldrenderer.pos(right, bottom, zLevel).color(f5, f6, f7, f4).endVertex();
        tessellator.draw();
        GlStateManager.shadeModel(7424);
        GlStateManager.disableBlend();
        GlStateManager.enableAlpha();
        GlStateManager.enableTexture2D();
    }
    
    public void drawTexturedModalRect(int x, int y, int textureX, int textureY, int width, int height) {
        float f = 0.00390625F;
        float f1 = 0.00390625F;
        Tessellator tessellator = Tessellator.getInstance();
        WorldRenderer worldrenderer = tessellator.getWorldRenderer();
        worldrenderer.begin(7, DefaultVertexFormats.POSITION_TEX);
        worldrenderer.pos(x + 0, y + height, zLevel).tex((textureX + 0) * f, (textureY + height) * f1).endVertex();
        worldrenderer.pos(x + width, y + height, zLevel).tex((textureX + width) * f, (textureY + height) * f1).endVertex();
        worldrenderer.pos(x + width, y + 0, zLevel).tex((textureX + width) * f, (textureY + 0) * f1).endVertex();
        worldrenderer.pos(x + 0, y + 0, zLevel).tex((textureX + 0) * f, (textureY + 0) * f1).endVertex();
        tessellator.draw();
    }
    
    public void drawTexturedModalRect(float xCoord, float yCoord, int minU, int minV, int maxU, int maxV) {
        float f = 0.00390625F;
        float f1 = 0.00390625F;
        Tessellator tessellator = Tessellator.getInstance();
        WorldRenderer worldrenderer = tessellator.getWorldRenderer();
        worldrenderer.begin(7, DefaultVertexFormats.POSITION_TEX);
        worldrenderer.pos(xCoord + 0.0F, yCoord + maxV, zLevel).tex((minU + 0) * f, (minV + maxV) * f1).endVertex();
        worldrenderer.pos(xCoord + maxU, yCoord + maxV, zLevel).tex((minU + maxU) * f, (minV + maxV) * f1).endVertex();
        worldrenderer.pos(xCoord + maxU, yCoord + 0.0F, zLevel).tex((minU + maxU) * f, (minV + 0) * f1).endVertex();
        worldrenderer.pos(xCoord + 0.0F, yCoord + 0.0F, zLevel).tex((minU + 0) * f, (minV + 0) * f1).endVertex();
        tessellator.draw();
    }
    
    public void drawTexturedModalRect(float xCoord, float yCoord, float minU, float minV, float maxU, float maxV) {
        float f = 0.00390625F;
        Tessellator tessellator = Tessellator.getInstance();
        WorldRenderer worldrenderer = tessellator.getWorldRenderer();
        worldrenderer.begin(7, DefaultVertexFormats.POSITION_TEX);
        worldrenderer.pos(xCoord, yCoord + maxV, zLevel).tex(minU * f, (minV + maxV) * f).endVertex();
        worldrenderer.pos(xCoord + maxU, yCoord + maxV, zLevel).tex((minU + maxU) * f, (minV + maxV) * f).endVertex();
        worldrenderer.pos(xCoord + maxU, yCoord, zLevel).tex((minU + maxU) * f, minV  * f).endVertex();
        worldrenderer.pos(xCoord, yCoord, zLevel).tex(minU * f, minV * f).endVertex();
        tessellator.draw();
    }
    
    public void drawTexturedModalRect(int xCoord, int yCoord, TextureAtlasSprite textureSprite, int widthIn, int heightIn) {
        Tessellator tessellator = Tessellator.getInstance();
        WorldRenderer worldrenderer = tessellator.getWorldRenderer();
        worldrenderer.begin(7, DefaultVertexFormats.POSITION_TEX);
        worldrenderer.pos(xCoord + 0, yCoord + heightIn, zLevel).tex(textureSprite.getMinU(), textureSprite.getMaxV()).endVertex();
        worldrenderer.pos(xCoord + widthIn, yCoord + heightIn, zLevel).tex(textureSprite.getMaxU(), textureSprite.getMaxV()).endVertex();
        worldrenderer.pos(xCoord + widthIn, yCoord + 0, zLevel).tex(textureSprite.getMaxU(), textureSprite.getMinV()).endVertex();
        worldrenderer.pos(xCoord + 0, yCoord + 0, zLevel).tex(textureSprite.getMinU(), textureSprite.getMinV()).endVertex();
        tessellator.draw();
    }
    
    public static void drawModalRectWithCustomSizedTexture(int x, int y, float u, float v, int width, int height, float textureWidth, float textureHeight) {
        float f = 1.0F / textureWidth;
        float f1 = 1.0F / textureHeight;
        Tessellator tessellator = Tessellator.getInstance();
        WorldRenderer worldrenderer = tessellator.getWorldRenderer();
        worldrenderer.begin(7, DefaultVertexFormats.POSITION_TEX);
        worldrenderer.pos(x, y + height, 0.0D).tex(u * f, (v + height) * f1).endVertex();
        worldrenderer.pos(x + width, y + height, 0.0D).tex((u + width) * f, (v + height) * f1).endVertex();
        worldrenderer.pos(x + width, y, 0.0D).tex((u + width) * f, v * f1).endVertex();
        worldrenderer.pos(x, y, 0.0D).tex(u * f, v * f1).endVertex();
        tessellator.draw();
    }
    
    public static void drawScaledCustomSizeModalRect(int x, int y, float u, float v, int uWidth, int vHeight, int width, int height, float tileWidth, float tileHeight) {
        float f = 1.0F / tileWidth;
        float f1 = 1.0F / tileHeight;
        Tessellator tessellator = Tessellator.getInstance();
        WorldRenderer worldrenderer = tessellator.getWorldRenderer();
        worldrenderer.begin(7, DefaultVertexFormats.POSITION_TEX);
        worldrenderer.pos(x, y + height, 0.0D).tex(u * f, (v + vHeight) * f1).endVertex();
        worldrenderer.pos(x + width, y + height, 0.0D).tex((u + uWidth) * f, (v + vHeight) * f1).endVertex();
        worldrenderer.pos(x + width, y, 0.0D).tex((u + uWidth) * f, v * f1).endVertex();
        worldrenderer.pos(x, y, 0.0D).tex(u * f, v * f1).endVertex();
        tessellator.draw();
    }
	
	public static void drawCustomSizedTexture(int x, int y, double u, double v, double width, double height, double textureWidth, double textureHeight) {
        double f = 1.0 / textureWidth;
        double f1 = 1.0 / textureHeight;
        Tessellator tessellator = Tessellator.getInstance();
        WorldRenderer worldrenderer = tessellator.getWorldRenderer();
        worldrenderer.begin(7, DefaultVertexFormats.POSITION_TEX);
        worldrenderer.pos(x, y + height, 0.0D).tex(u * f, (v + height) * f1).endVertex();
        worldrenderer.pos(x + width, y + height, 0.0D).tex((u + width) * f, (v + height) * f1).endVertex();
        worldrenderer.pos(x + width, y, 0.0D).tex((u + width) * f, v * f1).endVertex();
        worldrenderer.pos(x, y, 0.0D).tex(u * f, v * f1).endVertex();
        tessellator.draw();
    }
}
