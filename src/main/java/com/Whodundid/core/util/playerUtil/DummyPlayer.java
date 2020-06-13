package com.Whodundid.core.util.playerUtil;

import com.Whodundid.core.util.chatUtil.EChatUtil;
import com.mojang.authlib.GameProfile;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.entity.player.EnumPlayerModelParts;
import net.minecraft.util.MathHelper;
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
	private String name = "";
	private Thread animationUpdater;
	private volatile boolean animate = true;
	private volatile boolean run = true;
	
	public DummyPlayer() { this(null, null, false, null, "dummy", false); }
	public DummyPlayer(ResourceLocation skinIn, ResourceLocation capeIn, boolean alexModel, String uuidIn, String nameIn, boolean animate) {
		super(Minecraft.getMinecraft().theWorld, new GameProfile(null, nameIn));
		skin = skinIn;
		cape = capeIn;
		alex = alexModel;
		name = nameIn;
		instance = this;
		
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
		animationUpdater.start();
	}
	
	public void updateAnimation() {
		if (animate) {
			
			//limbs
			
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
			
			//head
			
			//position fixing
			if (rotationYawHead >= (float) (rotationYaw + 25.4f)) {
				rotationYawHead = (float) (rotationYaw + 25.1f);
				headLeft = false;
			}
			if (rotationYawHead <= (float) (rotationYaw - 25.4f)) {
				rotationYawHead = (float) (rotationYaw - 25.1f);
				headLeft = true;
			}
			
			//update pos
			if (headLeft) {
				if (rotationYawHead < (rotationYaw + 25f)) {
					rotationYawHead += 0.080f;
				}
				else { headLeft = false; }
			}
			else {
				if (rotationYawHead > (rotationYaw - 25f)) {
					rotationYawHead -= 0.080f;
				}
				else { headLeft = true; }
			}
			
			//limit pos
			rotationYawHead = MathHelper.clamp_float(rotationYawHead, rotationYaw - 25f, rotationYaw + 25f);
		}
	}

	@Override public ResourceLocation getLocationSkin() { return skin; }
	@Override public ResourceLocation getLocationCape() { return cape; }
	
	@Override
	public boolean isWearing(EnumPlayerModelParts m) {
		return !name.toLowerCase().equals("notch");
	}
	
	//to prevent minecraft vanilla cape draw
	@Override
	public boolean hasPlayerInfo() {
		return false;
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
		EChatUtil.threadTextObject = null;
	}
	
	@Override
	public void finalize() throws Throwable {
		destroy();
		super.finalize();
	}
	
	public DummyPlayer setDrawName(boolean val) { drawName = val; return this; }
	
	public DummyPlayer setAnimate(boolean val) {
		animate = val;
		
		if (!animate) {
			limbSwingAmount = 0;
			rotationYawHead = rotationYaw;
		}
		
		return this;
	}
	
	public boolean getArmLeft() { return armLeft; }
	public boolean getHeadLeft() { return headLeft; }
	
}
