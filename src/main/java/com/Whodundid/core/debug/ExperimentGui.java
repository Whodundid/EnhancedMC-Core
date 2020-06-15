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
import com.Whodundid.core.app.window.AppErrorDialogueBox;
import com.Whodundid.core.app.window.AppInfoWindow;
import com.Whodundid.core.app.window.windowUtil.AppErrorType;
import com.Whodundid.core.coreApp.EMCResources;
import com.Whodundid.core.settings.KeyBindWindow;
import com.Whodundid.core.settings.SettingsWindowMain;
import com.Whodundid.core.util.playerUtil.PlayerDrawer;
import com.Whodundid.core.util.renderUtil.EColors;
import com.Whodundid.core.util.renderUtil.ScreenLocation;
import com.Whodundid.core.util.storageUtil.EArrayList;
import com.Whodundid.core.util.storageUtil.EDimension;
import com.Whodundid.core.util.storageUtil.StorageBox;
import com.Whodundid.core.windowLibrary.WindowObjectS;
import com.Whodundid.core.windowLibrary.windowObjects.actionObjects.WindowButton;
import com.Whodundid.core.windowLibrary.windowObjects.actionObjects.WindowCheckBox;
import com.Whodundid.core.windowLibrary.windowObjects.actionObjects.WindowRadioButton;
import com.Whodundid.core.windowLibrary.windowObjects.actionObjects.WindowScrollBar;
import com.Whodundid.core.windowLibrary.windowObjects.actionObjects.WindowSlider;
import com.Whodundid.core.windowLibrary.windowObjects.advancedObjects.colorPicker.ColorPicker;
import com.Whodundid.core.windowLibrary.windowObjects.advancedObjects.dropDownList.WindowDropDownList;
import com.Whodundid.core.windowLibrary.windowObjects.advancedObjects.header.WindowHeader;
import com.Whodundid.core.windowLibrary.windowObjects.advancedObjects.scrollList.WindowScrollList;
import com.Whodundid.core.windowLibrary.windowObjects.advancedObjects.textArea.WindowTextArea;
import com.Whodundid.core.windowLibrary.windowObjects.basicObjects.WindowContainer;
import com.Whodundid.core.windowLibrary.windowObjects.basicObjects.WindowImageBox;
import com.Whodundid.core.windowLibrary.windowObjects.basicObjects.WindowLabel;
import com.Whodundid.core.windowLibrary.windowObjects.miscObjects.KeyOverlay;
import com.Whodundid.core.windowLibrary.windowObjects.utilityObjects.PlayerViewer;
import com.Whodundid.core.windowLibrary.windowObjects.windows.RightClickMenu;
import com.Whodundid.core.windowLibrary.windowObjects.windows.WindowDialogueBox;
import com.Whodundid.core.windowLibrary.windowTypes.EnhancedGui;
import com.Whodundid.core.windowLibrary.windowTypes.WindowObject;
import com.Whodundid.core.windowLibrary.windowTypes.WindowParent;
import com.Whodundid.core.windowLibrary.windowTypes.interfaces.IActionObject;
import com.Whodundid.core.windowLibrary.windowTypes.interfaces.IWindowObject;
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
	WindowButton test1, test2, test3, test4;
	public boolean bool1, bool2, bool3, bool4;
	public int color = 0;
	WindowLabel label;
	WindowTextArea textArea;
	WindowDialogueBox dialogueBox;
	WindowScrollList scrollList;
	WindowSlider slider, slider2;
	RightClickMenu rcm;
	ColorPicker colorPicker;
	WindowDropDownList dList;
	
	@Override public boolean isOpWindow() { return true; }
	@Override public boolean showInLists() { return false; }
	
	@Override
	public void initWindow() {
		setObjectName("Experiment Gui");
		defaultDims();
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
		
		textArea = new WindowTextArea(this, startX + 5, startY + 5, width - 10, height - 10, true, false).setDrawLineNumbers(true);
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
		
		dList = new WindowDropDownList(this, endX + 10, startY + 10);
		
		dList.addEntry("This");
		dList.addEntry("Is");
		dList.addEntry("A");
		dList.addEntry("Test");
		
		addObject(dList);
		
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
		drawDefaultBackground();
		updateColor();
		super.drawObject(mXIn, mYIn);
	}
	
	@Override
	public WindowObject resize(int xIn, int yIn, ScreenLocation areaIn) {
		if (textArea != null) {
			int pos = textArea.getVScrollBar().getScrollPos();
			super.resize(xIn, yIn, areaIn);
			textArea.getVScrollBar().setScrollBarPos(pos);
		}
		else {
			super.resize(xIn, yIn, areaIn);
		}
		return this;
	}
	
	public void updateColor() {
		color = Color.HSBtoRGB(System.currentTimeMillis() % 10000L / 10000.0f, 0.8f, 1f);
		if (label != null) { label.setColor(-color + 0xff222222); }
		header.setMainColor(color);
	}
	
	@Override
	public void mousePressed(int mX, int mY, int button) {
		super.mousePressed(mX, mY, button);
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
	public void actionPerformed(IActionObject object, Object... args) {
		if (object.equals(test1)) {
			
		}
	}
	
	public void testMethod1() {
		
	}
	
	public void testMethod2() {
		
	}
	
	public void testMethodWithArgs(String[] args) {
		
	}
	
	public int testMethodInt() {
		return 0;
	}
	
	public boolean testMethodBoolean(String[] args) {
		return false;
	}
	
 }
