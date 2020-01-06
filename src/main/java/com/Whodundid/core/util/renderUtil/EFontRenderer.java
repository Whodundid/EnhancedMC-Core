package com.Whodundid.core.util.renderUtil;

import com.Whodundid.core.coreSubMod.EMCResources;
import com.ibm.icu.text.ArabicShaping;
import com.ibm.icu.text.ArabicShapingException;
import com.ibm.icu.text.Bidi;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Random;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.renderer.texture.TextureUtil;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.client.resources.IResourceManagerReloadListener;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.util.ResourceLocation;
import org.apache.commons.io.IOUtils;
import org.lwjgl.opengl.GL11;

//Author: Hunter Bragg

public class EFontRenderer extends GLObject implements IResourceManagerReloadListener {
	
	private static final ResourceLocation[] unicodePageLocations = new ResourceLocation[256];
	protected int[] charWidth = new int[256];
	public int FONT_HEIGHT = 9;
	public Random fontRandom = new Random();
	protected byte[] glyphWidth = new byte[65536];
	private int[] colorCode = new int[32];
	protected final ResourceLocation locationFontTexture;
	private final TextureManager renderEngine;
	protected double posX;
	protected double posY;
	private boolean unicodeFlag;
	private boolean bidiFlag;
	private float red;
	private float blue;
	private float green;
	private float alpha;
	private int textColor;
	private boolean randomStyle;
	private boolean boldStyle;
	private boolean italicStyle;
	private boolean underlineStyle;
	private boolean strikethroughStyle;

	public EFontRenderer(GameSettings gameSettingsIn, ResourceLocation location, TextureManager textureManagerIn, boolean unicode) {
		locationFontTexture = location;
		renderEngine = textureManagerIn;
		unicodeFlag = unicode;
		bindTexture(locationFontTexture);
		for (int i = 0; i < 32; i++) {
			int j = (i >> 3 & 1) * 85;
			int k = (i >> 2 & 1) * 170 + j;
			int l = (i >> 1 & 1) * 170 + j;
			int i1 = (i >> 0 & 1) * 170 + j;
			if (i == 6) { k += 85; }
			if (gameSettingsIn.anaglyph) {
				int j1 = (k * 30 + l * 59 + i1 * 11) / 100;
				int k1 = (k * 30 + l * 70) / 100;
				int l1 = (k * 30 + i1 * 70) / 100;
				k = j1;
				l = k1;
				i1 = l1;
			}
			if (i >= 16) {
				k /= 4;
				l /= 4;
				i1 /= 4;
			}
			colorCode[i] = (k & 255) << 16 | (l & 255) << 8 | i1 & 255;
		}
		readGlyphSizes();
	}
	
	public int drawStringI(String text, double x, double y, int color) { return drawString(text, x, y, color, false); }
	public int drawCenteredStringI(String text, double x, double y, int color) { return drawString(text, x - getStringWidth(text) / 2, y, color, false); }
	public int drawStringWithShadowI(String text, double x, double y, int color) { return drawString(text, x, y, color, true); }
	public int drawCenteredStringWithShadowI(String text, double x, double y, int color) { return drawString(text, x - getStringWidth(text) / 2, y, color, true); }

	public void onResourceManagerReload(IResourceManager resourceManager) { readFontTexture(); readGlyphSizes(); }

	private void readFontTexture() {
		BufferedImage bufferedimage;
		try {
			bufferedimage = TextureUtil.readBufferedImage(getResourceInputStream(locationFontTexture));
		} catch (IOException ioexception) { throw new RuntimeException(ioexception); }
		int i = bufferedimage.getWidth();
		int j = bufferedimage.getHeight();
		int[] aint = new int[i * j];
		bufferedimage.getRGB(0, 0, i, j, aint, 0, i);
		int k = j / 16;
		int l = i / 16;
		int i1 = 1;
		float f = 8.0F / l;
		for (int j1 = 0; j1 < 256; j1++) {
			int k1 = j1 % 16;
			int l1 = j1 / 16;
			if (j1 == 32) { charWidth[j1] = 3 + i1; }
			int i2;
			for (i2 = l - 1; i2 >= 0; i2--) {
				int j2 = k1 * l + i2;
				boolean flag = true;
				for (int k2 = 0; k2 < k && flag; k2++) {
					int l2 = (l1 * l + k2) * i;
					if ((aint[j2 + l2] >> 24 & 255) != 0) { flag = false; }
				}
				if (!flag) { break; }
			}
			i2++;
			charWidth[j1] = (int) (0.5D + i2 * f) + i1;
		}
	}

	private void readGlyphSizes() {
		InputStream inputstream = null;
		try {
			inputstream = getResourceInputStream(new ResourceLocation("font/glyph_sizes.bin"));
			inputstream.read(glyphWidth);
		}
		catch (IOException ioexception) { throw new RuntimeException(ioexception); }
		finally { IOUtils.closeQuietly(inputstream); }
	}

	private float func_181559_a(char ch, boolean italic) {
		if (ch == 32) { return 4.0F; }
		int i = "\u00c0\u00c1\u00c2\u00c8\u00ca\u00cb\u00cd\u00d3\u00d4\u00d5\u00da\u00df\u00e3\u00f5\u011f\u0130\u0131\u0152\u0153\u015e\u015f\u0174\u0175\u017e\u0207\u0000\u0000\u0000\u0000\u0000\u0000\u0000 !\"#$%&\'()*+,-./0123456789:;<=>?@ABCDEFGHIJKLMNOPQRSTUVWXYZ[\\]^_`abcdefghijklmnopqrstuvwxyz{|}~\u0000\u00c7\u00fc\u00e9\u00e2\u00e4\u00e0\u00e5\u00e7\u00ea\u00eb\u00e8\u00ef\u00ee\u00ec\u00c4\u00c5\u00c9\u00e6\u00c6\u00f4\u00f6\u00f2\u00fb\u00f9\u00ff\u00d6\u00dc\u00f8\u00a3\u00d8\u00d7\u0192\u00e1\u00ed\u00f3\u00fa\u00f1\u00d1\u00aa\u00ba\u00bf\u00ae\u00ac\u00bd\u00bc\u00a1\u00ab\u00bb\u2591\u2592\u2593\u2502\u2524\u2561\u2562\u2556\u2555\u2563\u2551\u2557\u255d\u255c\u255b\u2510\u2514\u2534\u252c\u251c\u2500\u253c\u255e\u255f\u255a\u2554\u2569\u2566\u2560\u2550\u256c\u2567\u2568\u2564\u2565\u2559\u2558\u2552\u2553\u256b\u256a\u2518\u250c\u2588\u2584\u258c\u2590\u2580\u03b1\u03b2\u0393\u03c0\u03a3\u03c3\u03bc\u03c4\u03a6\u0398\u03a9\u03b4\u221e\u2205\u2208\u2229\u2261\u00b1\u2265\u2264\u2320\u2321\u00f7\u2248\u00b0\u2219\u00b7\u221a\u207f\u00b2\u25a0\u0000"
				.indexOf(ch);
		//System.out.println(ch);
		return i != -1 && !unicodeFlag ? renderDefaultChar(i, italic) : renderUnicodeChar(ch, italic);
	}
	
	protected float renderDefaultChar(int ch, boolean italic) {
		GlStateManager.pushMatrix();
		GlStateManager.scale(0.5f, 0.5f, 0.5f);
		bindTexture(italic ? EMCResources.asciiItallic : EMCResources.ascii);
		int i = ch % 16 * 8;
		int j = ch / 16 * 8;
		int k = italic ? 2 : 0;
		int l = charWidth[ch];
		double startX = posX * 2;
		double startY = posY * 2;
		float textStartX = i * 2;
		float textStartY = j * 2;
		float width = charWidth[ch] * 2 + k;
		float height = 8 * 2;
		float f = 0.00390625F;
        Tessellator tessellator = Tessellator.getInstance();
        WorldRenderer worldrenderer = tessellator.getWorldRenderer();
        worldrenderer.begin(7, DefaultVertexFormats.POSITION_TEX);
        worldrenderer.pos(startX, startY + height, glZLevel).tex(textStartX * f, (textStartY + height) * f).endVertex();                   //bot left
        worldrenderer.pos(startX + width, startY + height, glZLevel).tex((textStartX + width) * f, (textStartY + height) * f).endVertex(); //bot right
        worldrenderer.pos(startX + width, startY, glZLevel).tex((textStartX + width) * f, textStartY * f).endVertex();                     //top right
        worldrenderer.pos(startX, startY, glZLevel).tex(textStartX * f, textStartY * f).endVertex();                                       //top left
        tessellator.draw();
		GlStateManager.popMatrix();
		return l;
	}

	private ResourceLocation getUnicodePageLocation(int page) {
		if (unicodePageLocations[page] == null) {
			unicodePageLocations[page] = new ResourceLocation(String.format("textures/font/unicode_page_%02x.png", new Object[] { Integer.valueOf(page) }));
		}
		return unicodePageLocations[page];
	}
	
	private void loadGlyphTexture(int page) { bindTexture(getUnicodePageLocation(page)); }
	
	protected float renderUnicodeChar(char ch, boolean italic) {
		if (glyphWidth[ch] == 0) { return 0.0F; }
		int i = ch / 256;
		loadGlyphTexture(i);
		int j = glyphWidth[ch] >>> 4;
		int k = glyphWidth[ch] & 15;
		float f = j;
		float f1 = k + 1;
		float f2 = ch % 16 * 16 + f;
		float f3 = (ch & 255) / 16 * 16;
		float f4 = f1 - f - 0.02F;
		float f5 = italic ? 1.0F : 0.0F;
		GL11.glBegin(GL11.GL_TRIANGLE_STRIP);
		GL11.glTexCoord2f(f2 / 256.0F, f3 / 256.0F);
		GL11.glVertex3d(posX + f5, posY, 0.0F);
		GL11.glTexCoord2f(f2 / 256.0F, (f3 + 15.98F) / 256.0F);
		GL11.glVertex3d(posX - f5, posY + 7.99F, 0.0F);
		GL11.glTexCoord2f((f2 + f4) / 256.0F, f3 / 256.0F);
		GL11.glVertex3d(posX + f4 / 2.0F + f5, posY, 0.0F);
		GL11.glTexCoord2f((f2 + f4) / 256.0F, (f3 + 15.98F) / 256.0F);
		GL11.glVertex3d(posX + f4 / 2.0F - f5, posY + 7.99F, 0.0F);
		GL11.glEnd();
		return (f1 - f) / 2.0F + 1.0F;
	}
	
	private int drawString(String text, double x, double y, int color, boolean dropShadow) { 
		enableAlpha();
		resetStyles();
		int i;
		if (dropShadow) {
			i = renderString(text, x + 1.0F, y + 1.0F, color, true);
			i = Math.max(i, renderString(text, x, y, color, false));
		} else {
			i = renderString(text, x, y, color, false);
		}
		return i;
	}
	
	private String bidiReorder(String text) {
		try {
			Bidi bidi = new Bidi((new ArabicShaping(8)).shape(text), 127);
			bidi.setReorderingMode(0);
			return bidi.writeReordered(2);
		} catch (ArabicShapingException var3) { return text; }
	}
	
	private void resetStyles() {
		randomStyle = false;
		boldStyle = false;
		italicStyle = false;
		underlineStyle = false;
		strikethroughStyle = false;
	}
	
	private void renderStringAtPos(String text, boolean shadow) {
		for (int i = 0; i < text.length(); ++i) {
			char c0 = text.charAt(i);
			if (c0 == 167 && i + 1 < text.length()) {
				int i1 = "0123456789abcdefklmnor".indexOf(text.toLowerCase(Locale.ENGLISH).charAt(i + 1));
				if (i1 < 16) {
					randomStyle = false;
					boldStyle = false;
					strikethroughStyle = false;
					underlineStyle = false;
					italicStyle = false;
					if (i1 < 0 || i1 > 15) {
						i1 = 15;
					}
					if (shadow) {
						i1 += 16;
					}
					int j1 = colorCode[i1];
					textColor = j1;
					setColor((j1 >> 16) / 255.0F, (j1 >> 8 & 255) / 255.0F, (j1 & 255) / 255.0F, alpha);
					//System.out.println(((j1 >> 16) / 255.0F) + " " + ((j1 >> 8 & 255) / 255.0F) + " " + ((j1 & 255) / 255.0F));
				} 
				else if (i1 == 16) { randomStyle = true; }
				else if (i1 == 17) { boldStyle = true; }
				else if (i1 == 18) { strikethroughStyle = true; }
				else if (i1 == 19) { underlineStyle = true; }
				else if (i1 == 20) { italicStyle = true; }
				else if (i1 == 21) {
					randomStyle = false;
					boldStyle = false;
					strikethroughStyle = false;
					underlineStyle = false;
					italicStyle = false;
					setColor(red, blue, green, alpha);
				}
				i++;
			} else {
				int j = "\u00c0\u00c1\u00c2\u00c8\u00ca\u00cb\u00cd\u00d3\u00d4\u00d5\u00da\u00df\u00e3\u00f5\u011f\u0130\u0131\u0152\u0153\u015e\u015f\u0174\u0175\u017e\u0207\u0000\u0000\u0000\u0000\u0000\u0000\u0000 !\"#$%&\'()*+,-./0123456789:;<=>?@ABCDEFGHIJKLMNOPQRSTUVWXYZ[\\]^_`abcdefghijklmnopqrstuvwxyz{|}~\u0000\u00c7\u00fc\u00e9\u00e2\u00e4\u00e0\u00e5\u00e7\u00ea\u00eb\u00e8\u00ef\u00ee\u00ec\u00c4\u00c5\u00c9\u00e6\u00c6\u00f4\u00f6\u00f2\u00fb\u00f9\u00ff\u00d6\u00dc\u00f8\u00a3\u00d8\u00d7\u0192\u00e1\u00ed\u00f3\u00fa\u00f1\u00d1\u00aa\u00ba\u00bf\u00ae\u00ac\u00bd\u00bc\u00a1\u00ab\u00bb\u2591\u2592\u2593\u2502\u2524\u2561\u2562\u2556\u2555\u2563\u2551\u2557\u255d\u255c\u255b\u2510\u2514\u2534\u252c\u251c\u2500\u253c\u255e\u255f\u255a\u2554\u2569\u2566\u2560\u2550\u256c\u2567\u2568\u2564\u2565\u2559\u2558\u2552\u2553\u256b\u256a\u2518\u250c\u2588\u2584\u258c\u2590\u2580\u03b1\u03b2\u0393\u03c0\u03a3\u03c3\u03bc\u03c4\u03a6\u0398\u03a9\u03b4\u221e\u2205\u2208\u2229\u2261\u00b1\u2265\u2264\u2320\u2321\u00f7\u2248\u00b0\u2219\u00b7\u221a\u207f\u00b2\u25a0\u0000"
						.indexOf(c0);
				if (randomStyle && j != -1) {
					int k = getCharWidth(c0);
					char c1;
					while (true) {
						j = fontRandom.nextInt(
								"\u00c0\u00c1\u00c2\u00c8\u00ca\u00cb\u00cd\u00d3\u00d4\u00d5\u00da\u00df\u00e3\u00f5\u011f\u0130\u0131\u0152\u0153\u015e\u015f\u0174\u0175\u017e\u0207\u0000\u0000\u0000\u0000\u0000\u0000\u0000 !\"#$%&\'()*+,-./0123456789:;<=>?@ABCDEFGHIJKLMNOPQRSTUVWXYZ[\\]^_`abcdefghijklmnopqrstuvwxyz{|}~\u0000\u00c7\u00fc\u00e9\u00e2\u00e4\u00e0\u00e5\u00e7\u00ea\u00eb\u00e8\u00ef\u00ee\u00ec\u00c4\u00c5\u00c9\u00e6\u00c6\u00f4\u00f6\u00f2\u00fb\u00f9\u00ff\u00d6\u00dc\u00f8\u00a3\u00d8\u00d7\u0192\u00e1\u00ed\u00f3\u00fa\u00f1\u00d1\u00aa\u00ba\u00bf\u00ae\u00ac\u00bd\u00bc\u00a1\u00ab\u00bb\u2591\u2592\u2593\u2502\u2524\u2561\u2562\u2556\u2555\u2563\u2551\u2557\u255d\u255c\u255b\u2510\u2514\u2534\u252c\u251c\u2500\u253c\u255e\u255f\u255a\u2554\u2569\u2566\u2560\u2550\u256c\u2567\u2568\u2564\u2565\u2559\u2558\u2552\u2553\u256b\u256a\u2518\u250c\u2588\u2584\u258c\u2590\u2580\u03b1\u03b2\u0393\u03c0\u03a3\u03c3\u03bc\u03c4\u03a6\u0398\u03a9\u03b4\u221e\u2205\u2208\u2229\u2261\u00b1\u2265\u2264\u2320\u2321\u00f7\u2248\u00b0\u2219\u00b7\u221a\u207f\u00b2\u25a0\u0000"
										.length());
						c1 = "\u00c0\u00c1\u00c2\u00c8\u00ca\u00cb\u00cd\u00d3\u00d4\u00d5\u00da\u00df\u00e3\u00f5\u011f\u0130\u0131\u0152\u0153\u015e\u015f\u0174\u0175\u017e\u0207\u0000\u0000\u0000\u0000\u0000\u0000\u0000 !\"#$%&\'()*+,-./0123456789:;<=>?@ABCDEFGHIJKLMNOPQRSTUVWXYZ[\\]^_`abcdefghijklmnopqrstuvwxyz{|}~\u0000\u00c7\u00fc\u00e9\u00e2\u00e4\u00e0\u00e5\u00e7\u00ea\u00eb\u00e8\u00ef\u00ee\u00ec\u00c4\u00c5\u00c9\u00e6\u00c6\u00f4\u00f6\u00f2\u00fb\u00f9\u00ff\u00d6\u00dc\u00f8\u00a3\u00d8\u00d7\u0192\u00e1\u00ed\u00f3\u00fa\u00f1\u00d1\u00aa\u00ba\u00bf\u00ae\u00ac\u00bd\u00bc\u00a1\u00ab\u00bb\u2591\u2592\u2593\u2502\u2524\u2561\u2562\u2556\u2555\u2563\u2551\u2557\u255d\u255c\u255b\u2510\u2514\u2534\u252c\u251c\u2500\u253c\u255e\u255f\u255a\u2554\u2569\u2566\u2560\u2550\u256c\u2567\u2568\u2564\u2565\u2559\u2558\u2552\u2553\u256b\u256a\u2518\u250c\u2588\u2584\u258c\u2590\u2580\u03b1\u03b2\u0393\u03c0\u03a3\u03c3\u03bc\u03c4\u03a6\u0398\u03a9\u03b4\u221e\u2205\u2208\u2229\u2261\u00b1\u2265\u2264\u2320\u2321\u00f7\u2248\u00b0\u2219\u00b7\u221a\u207f\u00b2\u25a0\u0000"
								.charAt(j);
						if (k == getCharWidth(c1)) {
							break;
						}
					}
					c0 = c1;
				}
				float f1 = j == -1 || unicodeFlag ? 0.5f : 1f;
				boolean flag = (c0 == 0 || j == -1 || unicodeFlag) && shadow;
				if (flag) {
					posX -= f1;
					posY -= f1;
				}
				float f = func_181559_a(c0, italicStyle);
				if (flag) {
					posX += f1;
					posY += f1;
				}
				if (boldStyle) {
					posX += f1;
					if (flag) {
						posX -= f1;
						posY -= f1;
					}
					func_181559_a(c0, italicStyle);
					posX -= f1;
					if (flag) {
						posX += f1;
						posY += f1;
					}
					f++;
				}
				doDraw(f);
			}
		}
	}

	protected void doDraw(float f) {
		if (strikethroughStyle) {
			Tessellator tessellator = Tessellator.getInstance();
			WorldRenderer worldrenderer = tessellator.getWorldRenderer();
			GlStateManager.disableTexture2D();
			worldrenderer.begin(7, DefaultVertexFormats.POSITION);
			worldrenderer.pos(posX, posY + FONT_HEIGHT / 2, 0.0D).endVertex();
			worldrenderer.pos(posX + f, posY + FONT_HEIGHT / 2, 0.0D).endVertex();
			worldrenderer.pos(posX + f, posY + FONT_HEIGHT / 2 - 1.0F, 0.0D).endVertex();
			worldrenderer.pos(posX, posY + FONT_HEIGHT / 2 - 1.0F, 0.0D).endVertex();
			tessellator.draw();
			GlStateManager.enableTexture2D();
		}
		if (underlineStyle) {
			Tessellator tessellator1 = Tessellator.getInstance();
			WorldRenderer worldrenderer1 = tessellator1.getWorldRenderer();
			GlStateManager.disableTexture2D();
			worldrenderer1.begin(7, DefaultVertexFormats.POSITION);
			int l = underlineStyle ? -1 : 0;
			worldrenderer1.pos(posX + l, posY + FONT_HEIGHT, 0.0D).endVertex();
			worldrenderer1.pos(posX + f, posY + FONT_HEIGHT, 0.0D).endVertex();
			worldrenderer1.pos(posX + f, posY + FONT_HEIGHT - 1.0F, 0.0D).endVertex();
			worldrenderer1.pos(posX + l, posY + FONT_HEIGHT - 1.0F, 0.0D).endVertex();
			tessellator1.draw();
			GlStateManager.enableTexture2D();
		}
		posX += ((int) f);
	}
	
	private int renderStringAligned(String text, int x, int y, int width, int color, boolean dropShadow) {
		if (bidiFlag) {
			int i = getStringWidth(bidiReorder(text));
			x = x + width - i;
		}
		return renderString(text, x, y, color, dropShadow);
	}
	
	private int renderString(String text, double x, double y, int color, boolean dropShadow) {
		if (text == null) { return 0; }
		if (bidiFlag) { text = bidiReorder(text); }
		if ((color & -67108864) == 0) { color |= -16777216; }
		if (dropShadow) { color = (color & 16579836) >> 2 | color & -16777216; }
		red = (color >> 16 & 255) / 255.0F;
		blue = (color >> 8 & 255) / 255.0F;
		green = (color & 255) / 255.0F;
		alpha = (color >> 24 & 255) / 255.0F;
		setColor(red, blue, green, alpha);
		posX = x;
		posY = y;
		renderStringAtPos(text, dropShadow);
		return (int) posX;
	}
	
	public int getStringWidth(String text) {
		if (text == null) { return 0; }
		int i = 0;
		boolean flag = false;
		for (int j = 0; j < text.length(); j++) {
			char c0 = text.charAt(j);
			int k = getCharWidth(c0);
			if (k < 0 && j < text.length() - 1) {
				j++;
				c0 = text.charAt(j);
				if (c0 != 108 && c0 != 76) {
					if (c0 == 114 || c0 == 82) { flag = false; }
				} else { flag = true; }
				k = 0;
			}
			i += k;
			if (flag && k > 0) { i++; }
		}
		return i;
	}
	
	public int getCharWidth(char character) {
		if (character == 167) { return -1; } 
		else if (character == 32) { return 4; }
		else {
			int i = "\u00c0\u00c1\u00c2\u00c8\u00ca\u00cb\u00cd\u00d3\u00d4\u00d5\u00da\u00df\u00e3\u00f5\u011f\u0130\u0131\u0152\u0153\u015e\u015f\u0174\u0175\u017e\u0207\u0000\u0000\u0000\u0000\u0000\u0000\u0000 !\"#$%&\'()*+,-./0123456789:;<=>?@ABCDEFGHIJKLMNOPQRSTUVWXYZ[\\]^_`abcdefghijklmnopqrstuvwxyz{|}~\u0000\u00c7\u00fc\u00e9\u00e2\u00e4\u00e0\u00e5\u00e7\u00ea\u00eb\u00e8\u00ef\u00ee\u00ec\u00c4\u00c5\u00c9\u00e6\u00c6\u00f4\u00f6\u00f2\u00fb\u00f9\u00ff\u00d6\u00dc\u00f8\u00a3\u00d8\u00d7\u0192\u00e1\u00ed\u00f3\u00fa\u00f1\u00d1\u00aa\u00ba\u00bf\u00ae\u00ac\u00bd\u00bc\u00a1\u00ab\u00bb\u2591\u2592\u2593\u2502\u2524\u2561\u2562\u2556\u2555\u2563\u2551\u2557\u255d\u255c\u255b\u2510\u2514\u2534\u252c\u251c\u2500\u253c\u255e\u255f\u255a\u2554\u2569\u2566\u2560\u2550\u256c\u2567\u2568\u2564\u2565\u2559\u2558\u2552\u2553\u256b\u256a\u2518\u250c\u2588\u2584\u258c\u2590\u2580\u03b1\u03b2\u0393\u03c0\u03a3\u03c3\u03bc\u03c4\u03a6\u0398\u03a9\u03b4\u221e\u2205\u2208\u2229\u2261\u00b1\u2265\u2264\u2320\u2321\u00f7\u2248\u00b0\u2219\u00b7\u221a\u207f\u00b2\u25a0\u0000"
					.indexOf(character);
			if (character > 0 && i != -1 && !unicodeFlag) { return charWidth[i]; }
			else if (glyphWidth[character] != 0) {
				int j = glyphWidth[character] >>> 4;
				int k = glyphWidth[character] & 15;
				k++;
				return (k - j) / 2 + 1;
			}
			else { return 0; }
		}
	}
	
	public String trimStringToWidth(String text, int width) { return trimStringToWidth(text, width, false); }
	
	public String trimStringToWidth(String text, int width, boolean reverse) {
		StringBuilder stringbuilder = new StringBuilder();
		int i = 0;
		int j = reverse ? text.length() - 1 : 0;
		int k = reverse ? -1 : 1;
		boolean flag = false;
		boolean flag1 = false;
		for (int l = j; l >= 0 && l < text.length() && i < width; l += k) {
			char c0 = text.charAt(l);
			int i1 = getCharWidth(c0);
			if (flag) {
				flag = false;
				if (c0 != 108 && c0 != 76) {
					if (c0 == 114 || c0 == 82) { flag1 = false; }
				} else { flag1 = true; }
			} 
			else if (i1 < 0) { flag = true; }
			else {
				i += i1;
				if (flag1) { i++; }
			}
			if (i > width) { break; }
			if (reverse) { stringbuilder.insert(0, c0); }
			else { stringbuilder.append(c0); }
		}
		return stringbuilder.toString();
	}
	
	private String trimStringNewline(String text) {
		while (text != null && text.endsWith("\n")) { text = text.substring(0, text.length() - 1); }
		return text;
	}
	
	public void drawSplitString(String str, int x, int y, int wrapWidth, int textColorIn) {
		resetStyles();
		textColor = textColorIn;
		str = trimStringNewline(str);
		renderSplitString(str, x, y, wrapWidth, false);
	}
	
	private void renderSplitString(String str, int x, int y, int wrapWidth, boolean addShadow) {
		for (String s : listFormattedStringToWidth(str, wrapWidth)) {
			renderStringAligned(s, x, y, wrapWidth, textColor, addShadow);
			y += FONT_HEIGHT;
		}
	}
	
	public int splitStringWidth(String p_78267_1_, int p_78267_2_) {
		return FONT_HEIGHT * listFormattedStringToWidth(p_78267_1_, p_78267_2_).size();
	}
	
	public void setUnicodeFlag(boolean unicodeFlagIn) { unicodeFlag = unicodeFlagIn; }
	public boolean getUnicodeFlag() { return unicodeFlag; }
	public void setBidiFlag(boolean bidiFlagIn) { bidiFlag = bidiFlagIn; }

	public List<String> listFormattedStringToWidth(String str, int wrapWidth) {
		return Arrays.<String>asList(wrapFormattedStringToWidth(str, wrapWidth).split("\n"));
	}
	
	String wrapFormattedStringToWidth(String str, int wrapWidth) {
		int i = sizeStringToWidth(str, wrapWidth);
		if (str.length() <= i) {
			return str;
		}
		String s = str.substring(0, i);
		char c0 = str.charAt(i);
		boolean flag = c0 == 32 || c0 == 10;
		String s1 = getFormatFromString(s) + str.substring(i + (flag ? 1 : 0));
		return s + "\n" + wrapFormattedStringToWidth(s1, wrapWidth);
	}
	
	private int sizeStringToWidth(String str, int wrapWidth) {
		int i = str.length();
		int j = 0;
		int k = 0;
		int l = -1;
		for (boolean flag = false; k < i; ++k) {
			char c0 = str.charAt(k);
			switch (c0) {
			case '\n': k--; break;
			case ' ': l = k;
			default: j += getCharWidth(c0); if (flag) { j++; } break;
			case '\u00a7':
				if (k < i - 1) {
					k++;
					char c1 = str.charAt(k);
					if (c1 != 108 && c1 != 76) {
						if (c1 == 114 || c1 == 82 || isFormatColor(c1)) { flag = false; }
					} else { flag = true; }
				}
			}
			if (c0 == 10) {
				k++;
				l = k;
				break;
			}
			if (j > wrapWidth) { break; }
		}
		return k != i && l != -1 && l < k ? l : k;
	}
	
	private static boolean isFormatColor(char colorChar) {
		return colorChar >= 48 && colorChar <= 57 || colorChar >= 97 && colorChar <= 102 || colorChar >= 65 && colorChar <= 70;
	}
	
	private static boolean isFormatSpecial(char formatChar) {
		return formatChar >= 107 && formatChar <= 111 || formatChar >= 75 && formatChar <= 79 || formatChar == 114 || formatChar == 82;
	}
	
	public static String getFormatFromString(String text) {
		String s = "";
		int i = -1;
		int j = text.length();
		while ((i = text.indexOf(167, i + 1)) != -1) {
			if (i < j - 1) {
				char c0 = text.charAt(i + 1);
				if (isFormatColor(c0)) { s = "\u00a7" + c0; } 
				else if (isFormatSpecial(c0)) { s = s + "\u00a7" + c0; }
			}
		}
		return s;
	}
	
	public boolean getBidiFlag() { return bidiFlag; }
	protected void setColor(float r, float g, float b, float a) { GlStateManager.color(r, g, b, a); }
	protected void enableAlpha() { GlStateManager.enableAlpha(); }
	protected void bindTexture(ResourceLocation location) { renderEngine.bindTexture(location); }
	protected InputStream getResourceInputStream(ResourceLocation location) throws IOException { return Minecraft.getMinecraft().getResourceManager().getResource(location).getInputStream(); }
	public int getColorCode(char character) { return colorCode["0123456789abcdef".indexOf(character)]; }
}
