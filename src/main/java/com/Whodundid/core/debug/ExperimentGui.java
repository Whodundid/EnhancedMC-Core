package com.Whodundid.core.debug;

import java.awt.Color;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;
import java.util.Random;
import java.util.Scanner;
import com.Whodundid.core.EnhancedMC;
import com.Whodundid.core.app.AppSettings;
import com.Whodundid.core.app.AppType;
import com.Whodundid.core.app.EMCApp;
import com.Whodundid.core.app.RegisteredApps;
import com.Whodundid.core.app.gui.AppErrorDialogueBox;
import com.Whodundid.core.app.gui.AppErrorType;
import com.Whodundid.core.app.gui.AppInfoWindow;
import com.Whodundid.core.coreApp.EMCResources;
import com.Whodundid.core.enhancedGui.StaticEGuiObject;
import com.Whodundid.core.enhancedGui.guiObjects.actionObjects.EGuiButton;
import com.Whodundid.core.enhancedGui.guiObjects.actionObjects.EGuiCheckBox;
import com.Whodundid.core.enhancedGui.guiObjects.actionObjects.EGuiRadioButton;
import com.Whodundid.core.enhancedGui.guiObjects.actionObjects.EGuiScrollBar;
import com.Whodundid.core.enhancedGui.guiObjects.actionObjects.EGuiSlider;
import com.Whodundid.core.enhancedGui.guiObjects.advancedObjects.colorPicker.EGuiColorPicker;
import com.Whodundid.core.enhancedGui.guiObjects.advancedObjects.header.EGuiHeader;
import com.Whodundid.core.enhancedGui.guiObjects.advancedObjects.scrollList.EGuiScrollList;
import com.Whodundid.core.enhancedGui.guiObjects.advancedObjects.textArea.EGuiTextArea;
import com.Whodundid.core.enhancedGui.guiObjects.basicObjects.EGuiContainer;
import com.Whodundid.core.enhancedGui.guiObjects.basicObjects.EGuiImageBox;
import com.Whodundid.core.enhancedGui.guiObjects.basicObjects.EGuiLabel;
import com.Whodundid.core.enhancedGui.guiObjects.miscObjects.KeyOverlay;
import com.Whodundid.core.enhancedGui.guiObjects.utilityObjects.EGuiPlayerViewer;
import com.Whodundid.core.enhancedGui.guiObjects.windows.EGuiDialogueBox;
import com.Whodundid.core.enhancedGui.guiObjects.windows.EGuiRightClickMenu;
import com.Whodundid.core.enhancedGui.types.EnhancedGui;
import com.Whodundid.core.enhancedGui.types.EnhancedGuiObject;
import com.Whodundid.core.enhancedGui.types.WindowParent;
import com.Whodundid.core.enhancedGui.types.interfaces.IEnhancedActionObject;
import com.Whodundid.core.enhancedGui.types.interfaces.IEnhancedGuiObject;
import com.Whodundid.core.settings.KeyBindGui;
import com.Whodundid.core.settings.SettingsGuiMain;
import com.Whodundid.core.util.renderUtil.EColors;
import com.Whodundid.core.util.renderUtil.PlayerDrawer;
import com.Whodundid.core.util.renderUtil.ScreenLocation;
import com.Whodundid.core.util.storageUtil.EArrayList;
import com.Whodundid.core.util.storageUtil.EDimension;
import com.Whodundid.core.util.storageUtil.StorageBox;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.minecraft.MinecraftProfileTexture;
import com.mojang.authlib.minecraft.MinecraftProfileTexture.Type;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.ThreadDownloadImageData;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.ResourceLocation;

//Author: Hunter Bragg

@SuppressWarnings("unused")
public class ExperimentGui extends WindowParent {
	
	static Minecraft mc = Minecraft.getMinecraft();
	public static EArrayList<String> aliases = new EArrayList("experimentgui", "experiment", "expGui", "tgui", "testgui");
	ArrayList<Integer> values = new ArrayList();
	EGuiButton test1, test2, test3, test4;
	public boolean bool1, bool2, bool3, bool4;
	public int color = 0;
	EGuiLabel label;
	EGuiTextArea textArea;
	EGuiDialogueBox dialogueBox;
	EGuiScrollList scrollList;
	EGuiSlider slider, slider2;
	EGuiRightClickMenu rcm;
	EGuiColorPicker colorPicker;
	
	@Override
	public void initGui() {
		//EDimension d = this.getDimensions();
		setDimensions(startX, startY, defaultWidth, defaultHeight);
		//centerObjectWithSize(defaultWidth, defaultHeight);
		setObjectName("Experiment Gui");
		setResizeable(true);
		windowIcon = EMCResources.experimentIcon;
	}
	
	@Override
	public void initObjects() {
		defaultHeader(this);
		header.setTitleColor(0x000000);
		header.setParentFocusDrawn(false);
		
		//EGuiImageBox ibox = new EGuiImageBox(this, startX + 5, startY + 5, width - 10, height - 10, EMCResources.logo);
		//addObject(ibox);
		
		textArea = new EGuiTextArea(this, startX + 5, startY + 5, width - 10, height - 10, true, false).setDrawLineNumbers(true);
		addObject(textArea);
		
		//textArea.addTextLine("Doggy");
		
		textArea.addTextLine("Did you ever hear the tragedy of Darth Plagueis the Wise?");
		textArea.addTextLine("I thought not. It's not a story the Jedi would tell you. It's a Sith legend.");
		textArea.addTextLine("Darth Plagueis was a Dark Lord of the Sith, so powerful and so wise he");
		textArea.addTextLine("could use the Force to influence the midichlorians to create life... He had");
		textArea.addTextLine("such a knowledge of the dark side that he could even keep the ones he");
		textArea.addTextLine("cared about from dying. The dark side of the Force is a pathway to many");
		textArea.addTextLine("abilities some consider to be unnatural. He became so powerful... the only");
		textArea.addTextLine("thing he was afraid of was losing his power, which eventually, of course, he");
		textArea.addTextLine("did. Unfortunately, he taught his apprentice everything he knew, then his");
		textArea.addTextLine("apprentice killed him in his sleep. It's ironic he could save others from");
		textArea.addTextLine("death, but not himself.");
		
		/*
		for (int i = 0; i < 50; i++) {
			String s = i + "";
			if (s.length() == 1) { s += " :"; }
			s += ": ";
			for (int j = 0; j < 50; j++) {
				Random rand = new Random();
				int val = rand.nextInt(35);
				if (val >= 26) { s += " "; }
				else { s += (char)(val + 'a'); }
			}
			textArea.addTextLine(s, EColors.white.c());
		}
		*/
		
		/*
		//textArea.addTextLine("this is an intentionally very long line of text to test horizontal scrolling!");
		
		
		File gFile = null;
		
		String path = System.getProperty("user.dir");
		try {
			if (path != null && path.length() >= 5) {
				path = path.substring(0, path.length() - 3);
				path += "src\\main\\java\\com\\Whodundid\\core\\enhancedGui\\StaticEGuiObject.java";
				
				gFile = new File(path);
				Scanner reader = null;
				
				if (gFile.exists()) {
					reader = new Scanner(gFile);
					while (reader.hasNextLine()) { textArea.addTextLine(reader.nextLine()); }
				}
				
				if (reader != null) { reader.close(); }
			}
		} catch (Exception e) { e.printStackTrace(); }
		*/
		
		//for (int i = 1; i <= 60; i++) { textArea.addTextLine(i + " cow"); }
		
		
		
		//textArea.addTextLine("this is an intentionally very long line of text to test horizontal scrolling!");
	}
	
	@Override
	public void drawObject(int mXIn, int mYIn) {
		updateBeforeNextDraw(mXIn, mYIn);
		drawDefaultBackground();
		
		updateColor();
		
		try {
			if (checkDraw()) {
				GlStateManager.pushMatrix();
				GlStateManager.enableBlend();
				guiObjects.stream().filter(o -> o.checkDraw()).forEach(o -> {
					
					if (!o.hasFirstDraw()) { o.onFirstDraw(); o.onFirstDraw(); }
					if (o instanceof EGuiHeader) { 
						o.drawObject(mX, mY);
					}
					else {
						//scissor(startX + 1, startY + 1, endX - 1, endY - 1);
						o.drawObject(mX, mY);
						//endScissor();
					}
					
					IEnhancedGuiObject f = getTopParent().getFocusLockObject();
					if (f != null && o instanceof EGuiHeader && (!o.equals(f) && !f.getAllChildren().contains(o))) {
						if (o.isVisible()) {
							EDimension d = o.getDimensions();
							this.drawRect(d.startX, d.startY, d.endX, d.endY, 0x88000000);
						}
					}
				});
				GlStateManager.popMatrix();
			}
		} catch (Exception e) { e.printStackTrace(); }
	}
	
	@Override
	public EnhancedGuiObject resize(int xIn, int yIn, ScreenLocation areaIn) {
		if (textArea != null) {
			//EArrayList doc = textArea.getTextDocument();
			int pos = textArea.getVScrollBar().getScrollPos();
			super.resize(xIn, yIn, areaIn);
			textArea.getVScrollBar().setScrollBarPos(pos);
			//textArea.setTextDocument(doc);
		} else {
			super.resize(xIn, yIn, areaIn);
		}
		return this;
	}
	
	public void updateColor() {
		color = Color.HSBtoRGB(System.currentTimeMillis() % 10000L / 10000.0f, 0.8f, 1f);
		if (label != null) { label.setDisplayStringColor(-color + 0xff222222); }
		header.setMainColor(color);
	}
	
	@Override
	public void mousePressed(int mX, int mY, int button) {
		super.mousePressed(mX, mY, button);
		//testMethod2();
		//if (textArea != null) {
			//textArea.addTextLine("clicked " + mX + ", " + mY + " with button " + button, 0xffaa00);
			//EGuiScrollBar b = textArea.getVScrollBar();
			//if (b != null) { b.setScrollBarPos(b.getHighVal()); }
		//}
	}
	
	@Override
	public void mouseReleased(int mX, int mY, int button) {
		super.mouseReleased(mX, mY, button);
	}
	
	@Override
	public void mouseScrolled(int change) {
		super.mouseScrolled(change);
	}
	
	@Override
	public void keyPressed(char typedChar, int keyCode) {
		super.keyPressed(typedChar, keyCode);
	}
	
	@Override
	public void keyReleased(char typedChar, int keyCode) {
		super.keyReleased(typedChar, keyCode);
	}
	
	@Override
	public void actionPerformed(IEnhancedActionObject object, Object... args) {
		if (object.equals(test1)) {
			
		}
	}
	
	public void testMethod1() {
		scrollList.clearList();
		scrollList.setListHeight(scrollList.height - 2);
		
		EDimension l = scrollList.getListDimensions();
		
		int size = 20;
		
		for (int i = 0; i < size; i++) {
			
			int dist = 22;
			
			EGuiButton modSettings = new EGuiButton(scrollList, l.startX + 5, l.startY + 2 + (i * dist), 110, 20, "name");
			EGuiButton enabled = new EGuiButton(scrollList, l.endX - 79, l.startY + 2 + (i * dist), 50, 20, "enable");
			EGuiButton info = new EGuiButton(scrollList, l.endX - 25, l.startY + 2 + (i * dist), 20, 20).setTextures(EMCResources.guiInfo, EMCResources.guiInfoSel);
			
			if (i >= 8) {
				scrollList.setListHeight(scrollList.getListHeight() + 2 + modSettings.height);
			}
			
			scrollList.addObjectToList(modSettings, enabled, info);
		}
		
		//if (size > 8) { scrollList.setListHeight(scrollList.getListHeight() + 1); }
		scrollList.renderVScrollBarThumb(size > 8);
	}
	
	public void testMethod2() {
		if (textArea != null) {
			//long start = System.nanoTime();
			System.out.println(textArea.getTextLine(60));
			//System.out.println(System.nanoTime() - start);
		}
	}
	
	public void testMethodWithArgs(String[] args) {
		
	}
	
	public int testMethodInt() {
		return 0;
	}
	
	public boolean testMethodBoolean(String[] args) {
		return false;
	}
	
	@Override public boolean isOpWindow() { return true; }
	@Override public boolean showInLists() { return false; }
	
 }
