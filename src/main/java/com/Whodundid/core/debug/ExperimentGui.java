package com.Whodundid.core.debug;

import java.awt.Color;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;
import java.util.Scanner;
import com.Whodundid.core.EnhancedMC;
import com.Whodundid.core.coreSubMod.EMCResources;
import com.Whodundid.core.enhancedGui.guiObjects.advancedObjects.colorPicker.EGuiColorPicker;
import com.Whodundid.core.enhancedGui.guiObjects.advancedObjects.header.EGuiHeader;
import com.Whodundid.core.enhancedGui.guiObjects.advancedObjects.textArea.EGuiTextArea;
import com.Whodundid.core.enhancedGui.guiObjects.basicObjects.EGuiButton;
import com.Whodundid.core.enhancedGui.guiObjects.basicObjects.EGuiCheckBox;
import com.Whodundid.core.enhancedGui.guiObjects.basicObjects.EGuiContainer;
import com.Whodundid.core.enhancedGui.guiObjects.basicObjects.EGuiImageBox;
import com.Whodundid.core.enhancedGui.guiObjects.basicObjects.EGuiLabel;
import com.Whodundid.core.enhancedGui.guiObjects.basicObjects.EGuiRadioButton;
import com.Whodundid.core.enhancedGui.guiObjects.basicObjects.EGuiRightClickMenu;
import com.Whodundid.core.enhancedGui.guiObjects.basicObjects.EGuiScrollBar;
import com.Whodundid.core.enhancedGui.guiObjects.basicObjects.EGuiScrollList;
import com.Whodundid.core.enhancedGui.guiObjects.basicObjects.EGuiSlider;
import com.Whodundid.core.enhancedGui.guiObjects.miscObjects.KeyOverlay;
import com.Whodundid.core.enhancedGui.guiObjects.utilityObjects.EGuiPlayerViewer;
import com.Whodundid.core.enhancedGui.guiObjects.windows.EGuiDialogueBox;
import com.Whodundid.core.enhancedGui.types.EnhancedGui;
import com.Whodundid.core.enhancedGui.types.EnhancedGuiObject;
import com.Whodundid.core.enhancedGui.types.WindowParent;
import com.Whodundid.core.enhancedGui.types.interfaces.IEnhancedActionObject;
import com.Whodundid.core.enhancedGui.types.interfaces.IEnhancedGuiObject;
import com.Whodundid.core.settings.KeyBindGui;
import com.Whodundid.core.settings.SettingsGuiMain;
import com.Whodundid.core.subMod.RegisteredSubMods;
import com.Whodundid.core.subMod.SubMod;
import com.Whodundid.core.subMod.SubModSettings;
import com.Whodundid.core.subMod.SubModType;
import com.Whodundid.core.subMod.gui.SubModErrorDialogueBox;
import com.Whodundid.core.subMod.gui.SubModErrorType;
import com.Whodundid.core.subMod.gui.SubModInfoDialogueBox;
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

//Last edited: 10-14-18
//First Added: 9-5-18
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
	}
	
	@Override
	public void initObjects() {
		defaultHeader(this);
		header.setTitleColor(0x000000);
		header.setParentFocusDrawn(false);
		
		EGuiCheckBox cbox = new EGuiCheckBox(this, startX + 10, startY + 10, 20, 20);
		addObject(cbox);
		
		EGuiRadioButton rbtn = new EGuiRadioButton(this, startX + 10, cbox.endY + 10, 20, 20);
		addObject(rbtn);
		
		EGuiImageBox ibox = new EGuiImageBox(this, cbox.endX + 10, cbox.startY, width - cbox.width - 30, height - 20, EMCResources.logo);
		addObject(ibox);
		
		/*
		EGuiContainer con = new EGuiContainer(this, startX + 5, startY + 5, width - 10, height - 10);
		EGuiContainer a = new EGuiContainer(con, startX + 7, startY + 24, 100, 120);
		EGuiContainer b = new EGuiContainer(con, startX + 113, startY + 24, 100, 120);
		EGuiContainer i = new EGuiContainer(a, startX + 12, startY + 48, 80, 80);
		EGuiContainer ii = new EGuiContainer(b, startX + 123, startY + 48, 80, 80);
		
		con.setTitle("Con");
		a.setTitle("a");
		b.setTitle("b");
		i.setTitle("i");
		ii.setTitle("ii");
		
		con.addObject(a, b);
		a.addObject(i);
		b.addObject(ii);
		
		EGuiButton btn1 = new EGuiButton(i, startX + 20, startY + 70, 60, 20, "button 1");
		EGuiButton btn2 = new EGuiButton(ii, startX + 130, startY + 70, 60, 20, "button 2");
		i.addObject(btn1);
		ii.addObject(btn2);
		
		this.addObject(con);
		*/
		
		/*
		WindowParent inner = new WindowParent(this, endX + 15, midY - 5, 219, 190) {
			@Override
			public void drawObject(int mXIn, int mYIn, float ticks) {
				drawDefaultBackground();
				super.drawObject(mXIn, mYIn, ticks);
			}
		};
		
		inner.setHeader(new EGuiHeader(inner));
		if (inner.getHeader() != null) { inner.getHeader().setTitle("New Window"); }
		
		scrollList = new EGuiScrollList(inner, endX + 20, midY, 210, 180);
		
		testMethod1();
		
		//scrollList.addObjectToList(new EGuiButton(scrollList, 10, 5, 50, 20, "button 1"));
		//scrollList.addObjectToList(new EGuiLabel(scrollList, 30, 50, "Scrolling Label Test!"));
		//scrollList.addObjectToList(new EGuiButton(scrollList, 10, 80, 90, 20, "button 2") {
		//	{ setRunActionOnPress(true); }
		//	@Override public void performAction() {
		//		mc.displayGuiScreen(new SettingsGuiMain());
		//		playPressSound();
		//	}
		//});
		//scrollList.addObjectToList(new EGuiLabel(scrollList, 30, 370, "At bottom!"));
		//scrollList.addObjectToList(new EGuiButton(scrollList, 45, 152, 90, 20, "test button 3").setDisplayStringColor(0xf63233).setEnabled(false));
		//scrollList.addObjectToList(new KeyOverlay(scrollList, 5, 220));
		
		//scrollList.setListHeight(scrollList.getDimensions().height + 200);
		
		inner.addObject(scrollList);
		
		WindowParent window2 = new WindowParent(this, endX + 15, 35, 219, 190) {
			@Override
			public void drawObject(int mXIn, int mYIn, float ticks) {
				drawDefaultBackground();
				super.drawObject(mXIn, mYIn, ticks);
			}
		};
		
		window2.setHeader(new EGuiHeader(window2));
		*/
		
		//textArea = new EGuiTextArea(this, startX + 5, startY + 5, width - 10, height - 10, true, false).setDrawLineNumbers(true);
		//addObject(textArea);
		
		//textArea.addTextLine("this is an intentionally very long line of text to test horizontal scrolling!");
		
		/*
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
		
		
		//for (int i = 1; i <= 60; i++) { textArea.addTextLine(i + " cow"); }
		*/
		
		
		
		//textArea.addTextLine("this is an intentionally very long line of text to test horizontal scrolling!");
		
		
		/*
		EGuiContainer con = new EGuiContainer(this, startX + 5, startY + 5, width - 10, height - 10).setTitle("testy cat");
		System.out.println("con Dims: " + con.getDimensions());
		EGuiScrollList l = new EGuiScrollList(con, startX + 7, startY + 24, width - 14, height - 31);
		System.out.println("list dims: " + l.getDimensions());
		EGuiButton beUtton = new EGuiButton(l, 5, 5, 100, 20, "Beeeee utton");
		System.out.println("button dims: " + beUtton.getDimensions() + " : " + beUtton);
		l.addObjectToList(beUtton);
		//System.out.println(beUtton.getDimensions());
		con.addObject(l);
		addObject(con);
		*/
		
		//enableHeader(false);
		//EScreenLocationSelector selector = new EScreenLocationSelector(this, wPos - 300, hPos - 200, 100);
		//EGuiHeader header = new EGuiHeader(this);
		//addObject(test1 = new EGuiButton(this, midX + 32, midY + 100, 60, 20, "Lobby"));
		//addObject(selector);
		//addObject(header);
		//buttonList.add(new GuiButton(0, wPos + 70, hPos + 70, 200, 20, "no"));
		
		
		//EGuiTextArea test = new EGuiTextArea(dialogueBox, dBoxDim.startX + 2, dBoxDim.startY + 2, 150, 150);
		//dialogueBox.addObject(test.setZLevel(1));
		
		//label = new EGuiLabel(this, wPos, hPos + 60, "The quick brown fox jumped over the incredibly large boulder at an impressive 32.04 kph.");
		//label.setDrawCentered(true).enableShadow(false).enableWordWrap(true, 190);
		//addObject(label);
		
		//dialogueBox = new EGuiDialogueBox(this, 50, 50, 200, 100, DialogueBoxTypes.ok);
		//dialogueBox.setDisplayString("Notice");
		//dialogueBox.setMessage("Press ok!");
		//addObject(dialogueBox);
		
		//textArea = new EGuiTextArea(this, startX + 3, startY + 3, width - 6, 150, false).setDrawLineNumbers(true);
		//for (ChatFilterList l : ChatOrganizer.filters) {
		//	textArea.addTextLine(l.getFilterName(), 0xff0000);
		//	for (String s : l.getFilterList()) {
		//		textArea.addTextLine(s);
		//	}
		//	textArea.addTextLine();
		//}
		
		//addObject(textArea);
		
		//addObject(inner, window2);
		//addObject(window2);
		//https://hypixel.net/my2018/?5c2a973544f4e2a67393289c
		
		//addObject(new EGuiLinkConfirmationDialogueBox(this, "https://www.google.com"));
		
		//chatWindow = new InGameChatWindow(this);
		//addObject(chatWindow);
		
		
		//colorPicker = new EGuiColorPicker(this, 150, 150);
		
		//EGuiPlayerViewer viewer = new EGuiPlayerViewer(colorPicker, startX + 35, startY + 30, 150, 200);
		//addObject(viewer);
		//viewer.setHSliderOrientation(ScreenLocation.top);
		//viewer.setVSliderOrientation(ScreenLocation.left);
		
		//addObject(colorPicker);
	}
	
	@Override
	public void drawObject(int mXIn, int mYIn, float ticks) {
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
						o.drawObject(mX, mY, ticks);
					}
					else {
						//scissor(startX + 1, startY + 1, endX - 1, endY - 1);
						o.drawObject(mX, mY, ticks);
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
		testMethod2();
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
 }
