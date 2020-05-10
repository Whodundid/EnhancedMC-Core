package com.Whodundid.playerInfo.gui;

import com.Whodundid.core.enhancedGui.guiObjects.actionObjects.EGuiButton;
import com.Whodundid.core.enhancedGui.guiObjects.utilityObjects.EGuiPlayerViewer;
import com.Whodundid.core.enhancedGui.types.WindowParent;
import com.Whodundid.core.enhancedGui.types.interfaces.IEnhancedActionObject;
import com.Whodundid.core.util.playerUtil.DummyPlayer;
import com.Whodundid.core.util.renderUtil.EColors;
import com.Whodundid.core.util.storageUtil.DynamicTextureHandler;
import com.Whodundid.playerInfo.util.PlayerInfoResources;
import java.awt.image.BufferedImage;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class SkinDisplayWindow extends WindowParent {

	String uuid;
	String playerName;
	DynamicTextureHandler skin;
	DynamicTextureHandler cape;
	boolean isAlex = false;
	DummyPlayer playerEntity;
	
	boolean animate = true;
	boolean flat = false;
	
	EGuiPlayerViewer skinViewer;
	EGuiButton closeBtn;
	EGuiButton animateBtn;
	EGuiButton flatBtn;
	
	public SkinDisplayWindow(String uuidIn, String playerNameIn) {
		uuid = uuidIn;
		playerName = playerNameIn;
		windowIcon = PlayerInfoResources.viewerIcon;
	}
	
	public void setSkin(BufferedImage skinIn, BufferedImage capeIn, boolean isAlexSkin, boolean hasCape) {
		
		//set skin
		if (skinIn != null) { skin = new DynamicTextureHandler(mc.renderEngine, skinIn); }
		else { skin = new DynamicTextureHandler(mc.renderEngine, new BufferedImage(64, 64, BufferedImage.TYPE_INT_ARGB)); }
		
		//set cape
		if (capeIn != null) { cape = new DynamicTextureHandler(mc.renderEngine, capeIn); }
		else { cape = new DynamicTextureHandler(mc.renderEngine, new BufferedImage(64, 32, BufferedImage.TYPE_INT_ARGB)); }
		
		isAlex = isAlexSkin;
		
		playerEntity = new DummyPlayer(skin.getTextureLocation(), cape.getTextureLocation(), isAlex, uuid, playerName);
		
		
		
		//items
		ItemStack i = null;
		
		switch (uuid) {
		case "be8ba05926444f4ca5e788a38e555b1e": i = new ItemStack(Item.getItemById(398), 1); break; //Whodundid - blue orchid
		case "d3dcdba8d6ee42778a810f359e4def25": i = new ItemStack(Item.getItemById(344), 1); break; //JamminOtter - egg
		}
		
		if (i != null) {
			playerEntity.inventory.mainInventory[playerEntity.inventory.currentItem] = i;
		}
	}
	
	@Override
	public void initGui() {
		defaultPos();
		setObjectName(playerName + "'s Skin");
		//setPinnable(true);
		
		super.initGui();
	}
	
	@Override
	public void initObjects() {
		defaultHeader(this);
		
		skinViewer = new EGuiPlayerViewer(this, startX + 10, startY + 10, width - 20, width - 20, playerEntity);
		
		closeBtn = new EGuiButton(this, endX - 70, endY - 30, 60, 20, "Close");
		animateBtn = new EGuiButton(this, startX + 10, endY - 30, 60, 20, "Animate").setDisplayStringColor(animate ? EColors.green : EColors.lred);
		flatBtn = new EGuiButton(this, midX - 30, endY - 30, 60, 20, flat ? "3D Model" : "Texture").setDisplayStringColor(EColors.yellow);
		
		addObject(skinViewer, closeBtn, animateBtn, flatBtn);
	}
	
	@Override
	public void drawObject(int mXIn, int mYIn) {
		drawDefaultBackground();
		
		if (!flat && animate) { playerEntity.updateArms(); }
		
		if (flat) {
			drawRect(startX + 10, startY + 10, endX - 10, endY - 45, EColors.black);
			//mc.renderEngine.bindTexture(EMCResources.windowIcon);
			//GlStateManager.color(0.25f, 0.25f, 0.25f, 0.25f);
			drawRect(startX + 11, startY + 11, endX - 11, endY - 46, 0xff878787);
			
			mc.renderEngine.bindTexture(skin.getTextureLocation());
			GlStateManager.color(2.0f, 2.0f, 2.0f, 2.0f);
			drawTexture(startX + 11, startY + 11, width - 22, height - 57);
		}
		
		super.drawObject(mXIn, mYIn);
	}
	
	@Override
	public void actionPerformed(IEnhancedActionObject object, Object... args) {
		if (object == closeBtn) { close(); }
		
		if (object == animateBtn) {
			animate = !animate;
			animateBtn.setDisplayStringColor(animate ? EColors.green : EColors.lred);
		}
		
		if (object == flatBtn) {
			flat = !flat;
			skinViewer.setVisible(!flat);
			flatBtn.setDisplayString(flat ? "3D Model" : "Texture");
		}
	}
	
	@Override
	public void close() {
		super.close();
		
		if (skin != null) { skin.getDynamicTexture().deleteGlTexture(); }
		if (cape != null) { cape.getDynamicTexture().deleteGlTexture(); }
	}
	
	@Override
	public void finalize() throws Throwable {
		if (skin != null) { skin.getDynamicTexture().deleteGlTexture(); }
		if (cape != null) { cape.getDynamicTexture().deleteGlTexture(); }
		
		super.finalize();
	}
	
	@Override public boolean showInLists() { return false; }
	
}
