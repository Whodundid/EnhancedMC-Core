package com.Whodundid.core.debug;

import java.awt.Color;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;
import java.util.Scanner;
import com.Whodundid.core.EnhancedMC;
import com.Whodundid.core.enhancedGui.guiObjects.advancedObjects.header.EGuiHeader;
import com.Whodundid.core.enhancedGui.guiObjects.advancedObjects.textArea.EGuiTextArea;
import com.Whodundid.core.enhancedGui.guiObjects.basicObjects.EGuiButton;
import com.Whodundid.core.enhancedGui.guiObjects.basicObjects.EGuiContainer;
import com.Whodundid.core.enhancedGui.guiObjects.basicObjects.EGuiLabel;
import com.Whodundid.core.enhancedGui.guiObjects.basicObjects.EGuiRightClickMenu;
import com.Whodundid.core.enhancedGui.guiObjects.basicObjects.EGuiScrollBar;
import com.Whodundid.core.enhancedGui.guiObjects.basicObjects.EGuiScrollList;
import com.Whodundid.core.enhancedGui.guiObjects.basicObjects.EGuiSlider;
import com.Whodundid.core.enhancedGui.guiObjects.miscObjects.KeyOverlay;
import com.Whodundid.core.enhancedGui.guiObjects.utilityObjects.EGuiPlayerViewer;
import com.Whodundid.core.enhancedGui.guiObjects.windows.EGuiColorPicker;
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
import com.Whodundid.core.util.renderUtil.Resources;
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
import net.minecraft.client.renderer.ThreadDownloadImageData;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.ResourceLocation;

//Last edited: 10-14-18
//First Added: 9-5-18
//Author: Hunter Bragg

@SuppressWarnings("unused")
public class ExperimentGui extends WindowParent {
	
	static Minecraft mc = Minecraft.getMinecraft();
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
		setHeader(new EGuiHeader(this));
		header.setTitleColor(0x000000);
		header.setParentFocusDrawn(false);
		
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
		
		textArea = new EGuiTextArea(this, startX + 5, startY + 5, width - 10, height - 10, true, false).setDrawLineNumbers(true);
		textArea.addTextLine("this is an intentionally very long line of text to test horizontal scrolling!");
		
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
		*/
		
		for (int i = 1; i <= 60; i++) { textArea.addTextLine(i + " cow"); }
		addObject(textArea);
		textArea.addTextLine("this is an intentionally very long line of text to test horizontal scrolling!");
		
		
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
		drawDefaultBackground();
		//drawRect(startX + 69, startY + 89, endX - 69, endY - 59, 0xff000000);
		//drawRect(startX + 70, startY + 90, endX - 70, endY - 60, 0xff1b1b1b);
		GlStateManager.color(2.0f, 2.0f, 2.0f, 2.0f);
		//GuiInventory.drawEntityOnScreen(midX, midY, 90, 0, 0, mc.thePlayer);
		
		//Map<Type, MinecraftProfileTexture> map = mc.getSkinManager().loadSkinFromCache(new GameProfile(null, "Whodundid"));
		//System.out.println(map);
		//if (map.containsKey(Type.SKIN)) {
		//	ResourceLocation s = mc.getSkinManager().loadSkin(map.get(Type.SKIN), Type.SKIN);
		//	mc.renderEngine.bindTexture(s);
		//	this.drawTexturedModalRect(startX, startY, 0, 0, width, height);
		//}
		
		
		
		//int posX = midX;
		//int posY = midY + 60;
		//int scale = 90;
		//float mouseX = -slider.getSliderValue();
		//float mouseY = slider2.getSliderValue();
		//EntityLivingBase ent = mc.thePlayer;
		
		//PlayerDrawer.drawPlayer(mc.thePlayer, posX, posY, -slider.getSliderValue(), slider2.getSliderValue(), 45);
		
		/*
		GlStateManager.enableColorMaterial();
        GlStateManager.pushMatrix();
        GlStateManager.translate((float)posX, (float)posY, 50.0F);
        GlStateManager.scale((float)(-scale), (float)scale, (float)scale);
        GlStateManager.rotate(180.0F, 0.0F, 0.0F, 1.0F);
        float f = ent.renderYawOffset;
        float f1 = ent.rotationYaw;
        float f2 = ent.rotationPitch;
        float f3 = ent.prevRotationYawHead;
        float f4 = ent.rotationYawHead;
        GlStateManager.rotate(135.0F, 0.0F, 1.0F, 0.0F);
        RenderHelper.enableStandardItemLighting();
        GlStateManager.rotate(-135.0F, 0.0F, 1.0F, 0.0F);
        GlStateManager.rotate(-((float) Math.atan((double) (mouseY / 250.0F))) * 20.0F, 1.0F, 0.0F, 0.0F);
        ent.renderYawOffset = -slider.getSliderValue() / 2;
        //ent.renderYawOffset = (float) Math.atan((double) (mouseX / 40.0F)) * 20.0F;
        ent.rotationYaw = -slider.getSliderValue() / 2;
        //ent.rotationYaw = (float) Math.atan((double) (mouseX / 40.0F)) * 40.0F;
        ent.rotationPitch = -((float) Math.atan((double) (mouseY / 250.0F))) * 20.0F;
        //System.out.println(-mX / 2);
        ent.rotationYawHead = ent.rotationYaw;
        ent.prevRotationYawHead = ent.rotationYaw;
        GlStateManager.translate(0.0F, 0.0F, 0.0F);
        RenderManager rendermanager = Minecraft.getMinecraft().getRenderManager();
        rendermanager.setPlayerViewY(180.0F);
        rendermanager.setRenderShadow(false);
        rendermanager.renderEntityWithPosYaw(ent, 0.0D, 0.0D, 0.0D, 0.0F, 1.0F);
        rendermanager.setRenderShadow(true);
        //ent.swingProgress = 0.4f;
        ent.renderYawOffset = f;
        ent.rotationYaw = f1;
        ent.rotationPitch = f2;
        ent.prevRotationYawHead = f3;
        ent.rotationYawHead = f4;
        GlStateManager.popMatrix();
        RenderHelper.disableStandardItemLighting();
        GlStateManager.disableRescaleNormal();
        GlStateManager.setActiveTexture(OpenGlHelper.lightmapTexUnit);
        GlStateManager.disableTexture2D();
        GlStateManager.setActiveTexture(OpenGlHelper.defaultTexUnit);
		
		//System.out.println(this.getObjectUnderMouse());
		/*
		GlStateManager.pushMatrix();
		GlStateManager.enableBlend();
        GlStateManager.tryBlendFuncSeparate(770, 771, 0, 1);
        GlStateManager.disableAlpha();
        GlStateManager.shadeModel(7425);
        //GlStateManager.disableTexture2D();
		Tessellator tessellator = Tessellator.getInstance();
        WorldRenderer worldrenderer = tessellator.getWorldRenderer();
        worldrenderer.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR);
        worldrenderer.pos(wPos + 150, hPos - 150, -1.0D).tex(1.0D, 0.0D).color(255, 255, 255, 255).endVertex();
        worldrenderer.pos(wPos - 250, hPos - 150, 0.0D).tex(0.0D, 0.0D).color(255, 255, 255, 255).endVertex();
        worldrenderer.pos(wPos - 150, hPos + 150, 0.0D).tex(0.0D, 1.0D).color(255, 255, 255, 255).endVertex();
        //worldrenderer.pos(wPos + 150, hPos + 150, 0.0D).tex(1.0D, 1.0D).color(255, 255, 255, 255).endVertex();
        worldrenderer.pos(wPos + 250, hPos + 150, 0.0D).tex(1.0D, 1.0D).color(255, 255, 255, 255).endVertex();
        tessellator.draw();
        //GlStateManager.enableTexture2D();
        //GlStateManager.shadeModel(7424);
        //GlStateManager.enableAlpha();
        //GlStateManager.disableBlend();
        GlStateManager.popMatrix();
        */
		updateColor();
		super.drawObject(mXIn, mYIn, ticks);
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
	public void actionPerformed(IEnhancedActionObject object) {
		if (object.equals(test1)) {
			//ChatWindowFrame f = c.getChatWindow(ChatType.LOBBY);
			//if (f != null) { f.setVisible(true); }
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
			EGuiButton info = new EGuiButton(scrollList, l.endX - 25, l.startY + 2 + (i * dist), 20, 20).setTextures(Resources.guiInfo, Resources.guiInfoSel);
			
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
