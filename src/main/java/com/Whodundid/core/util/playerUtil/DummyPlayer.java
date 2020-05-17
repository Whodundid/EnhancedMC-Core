package com.Whodundid.core.util.playerUtil;

import com.mojang.authlib.GameProfile;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.entity.player.EnumPlayerModelParts;
import net.minecraft.util.ResourceLocation;

public class DummyPlayer extends AbstractClientPlayer {
	
	private DummyPlayer instance;
	private ResourceLocation skin;
	private ResourceLocation cape;
	private boolean alex;
	private boolean armLeft = true;
	private boolean headLeft = true;
	private boolean drawSkin = false;
	private boolean drawName = true;
	private float swingAmount = 0.7f;
	private float headAmount = 0f;
	private String name = "";
	private Thread animationUpdater;
	private volatile boolean run = true;
	
	public DummyPlayer() { this(null, null, false, null, "dummy", false); }
	public DummyPlayer(ResourceLocation skinIn, ResourceLocation capeIn, boolean alexModel, String uuidIn, String nameIn, boolean animate) {
		super(Minecraft.getMinecraft().theWorld, new GameProfile(null, nameIn));
		skin = skinIn;
		cape = capeIn;
		alex = alexModel;
		name = nameIn;
		instance = this;
		headAmount = rotationYawHead;
		
		animationUpdater = new Thread() {
			@Override
			public void run() {
				while (instance.run) {
					try {
						instance.updateAnimation();
						Thread.currentThread().sleep(0, 500);
					}
					catch (Exception e) { e.printStackTrace(); }
					
				}
			}
		};
		if (animate) { animationUpdater.start(); }
	}
	
	public void updateAnimation() {
		if (armLeft) {
			if (limbSwingAmount <= swingAmount) {
				limbSwingAmount += 0.002f;
			}
			else { armLeft = false; }
		}
		else {
			if (limbSwingAmount >= -swingAmount) {
				limbSwingAmount -= 0.002f;
			}
			else { armLeft = true; }
		}
		
		if (headLeft) {
			if (headAmount <= 25) {
				headAmount += 0.065f;
				rotationYawHead = headAmount;
			}
			else { headLeft = false; }
		}
		else {
			if (headAmount >= -25) {
				headAmount -= 0.065f;
				rotationYawHead = headAmount;
			}
			else { headLeft = true; }
		}
	}

	@Override public ResourceLocation getLocationSkin() { return skin; }
	@Override public ResourceLocation getLocationCape() { return cape; }
	
	@Override
	public boolean isWearing(EnumPlayerModelParts m) {
		if (name.toLowerCase().equals("notch")) {
			return false;
		}
		return true;
	}
	
	@Override
	public boolean hasPlayerInfo() {
		return drawSkin;
	}
	
	@Override
	public String getName() {
		if (drawName) { return super.getName(); }
		return "";
	}
	
	@Override
	public String getSkinType() {
		return alex ? "slim" : "default";
	}
	
	public DummyPlayer setDrawSkin(boolean val) { drawSkin = val; return this; }
	
	public void destroy() {
		if (animationUpdater != null) {
			instance.run = false;
		}
	}
	
	@Override
	public void finalize() throws Throwable {
		destroy();
		super.finalize();
	}
	
	public DummyPlayer setDrawName(boolean val) { drawName = val; return this; }
}
