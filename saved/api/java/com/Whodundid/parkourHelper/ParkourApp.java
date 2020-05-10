package com.Whodundid.parkourHelper;

import com.Whodundid.core.app.AppType;
import com.Whodundid.core.app.EMCApp;
import com.Whodundid.core.util.playerUtil.PlayerFacing;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import org.lwjgl.input.Keyboard;
import net.minecraftforge.fml.common.gameevent.InputEvent.KeyInputEvent;

@Mod(modid = ParkourApp.MODID, version = ParkourApp.VERSION, name = ParkourApp.NAME, dependencies = "required-after:enhancedmc")
public final class ParkourApp extends EMCApp {
	
	public static final String MODID = "parkourheper";
	public static final String VERSION = "0.1";
	public static final String NAME = "Parkour Helper";
	
	private static boolean constantSprint = false;
	private static boolean edgeJump = false;
	public static volatile double playerDist = 0;
	public static volatile double jumpPos = 0;
	public static volatile double jumpOffset = 0;
	public BackgroundChecker checker;
	public ScreenDrawer drawer;
	
	public ParkourApp() {
		super(AppType.PARKOUR);
		addDependency(AppType.CORE, "1.0");
		setMainGui(new ParkourGui());
		shouldLoad = false;
		checker = new BackgroundChecker(this);
		drawer = new ScreenDrawer(this);
		version = VERSION;
		author = "Whodundid";
		setAliases("parkourhelper", "parkour");
	}
	
	@Override
	public void clientTickEvent(TickEvent.ClientTickEvent e) {
		if (isEnabled() && checker != null && checker.running) {
			checkIfSprinting();
			checker.checkTick();
		}
	}
	
	@Override
	public void OverlayPostEvent(RenderGameOverlayEvent.Post e) {
		if (isEnabled()) {
			if (checker != null && checker.running) {
				drawer.drawScreen();
			}
		}
	}
	
	@Override
	public void keyEvent(KeyInputEvent e) {
		if (isEnabled()) {
			if (Keyboard.isKeyDown(Keyboard.KEY_E) && Keyboard.isKeyDown(Keyboard.KEY_LSHIFT) && Keyboard.isKeyDown(Keyboard.KEY_LCONTROL)) {
				if (mc.thePlayer != null) {
					if (checker != null) {
						toggleRunning();
						drawer.reset();
					}
				}
			}
			
			if (Keyboard.isKeyDown(Keyboard.KEY_M)) {
				PlayerFacing.setFacingDir(PlayerFacing.getCompassFacingDir());
			}
		}
	}
	
	public void toggleRunning() {
		if (isEnabled()) {
			if (checker.running) { checker.kill(); }
			else { checker.start(); }
		}
	}
	
	public void checkIfSprinting() {
		if (isEnabled()) {
			if (mc.thePlayer != null) {
				//if (!mc.thePlayer.isSprinting() && !mc.isGamePaused() && mc.inGameHasFocus) { KeyBinding.setKeyBindState(mc.gameSettings.keyBindSprint.getKeyCode(), true); }		
			}
		}
	}
	
	public void drawStatusOnScreen(FontRenderer fontRender, ScaledResolution res) {	
	}
	
	public boolean testBlockJumpHeights(int blockY, double playerYPos) {
		return (playerYPos == blockY || //base 0.0 height
			playerYPos == blockY - 0.984375 || //lilipad
			playerYPos == blockY - 0.9375 || //carpet
			playerYPos == blockY - 0.875 || //repeater & snow layer
			playerYPos == blockY - 0.8125 || //trapdoor
			playerYPos == blockY - 0.75 || //snow layer
			playerYPos == blockY - 0.625 || //daylight sensor & snowlayer & flower pot
			playerYPos == blockY - 0.5 || //half block
			playerYPos == blockY - 0.4375 || //bed
			playerYPos == blockY - 0.375 || //snow layer
			playerYPos == blockY - 0.25 || //enchant & snow layer
			playerYPos == blockY - 0.1875 || //end port							
			playerYPos == blockY - 0.125 || //chest & snow layer & soul sand
			playerYPos == blockY - 0.0625 || //cactus
			playerYPos == blockY + 0.5); //fence

	}
	
	public boolean readyToJump() {
		/*
		if (HelperBlock.getHelperBlock() != null) {
			if (mc.getRenderViewEntity() != null) {
				Block block = HelperBlock.getHelperBlock();
				IBlockState state = HelperBlock.getHelperBlockState();
				double yPos = mc.getRenderViewEntity().getEntityBoundingBox().minY;
				int bY = HelperBlock.getHelperBlockLocation().getY();
				double yCor = yPos - 1;
				if (testBlockJumpHeights(bY, yCor)) {
					if (jumpOffset == 0.0) {
						if (WorldHelper.isFenceBlock(state)) { jumpOffset = 0.135; }
						else if (WorldHelper.isPane(state)) { jumpOffset = 0.035; }
						else if (WorldHelper.isTrapDoor(state)) {
							if ((boolean) state.getProperties().values().toArray()[2]) { jumpOffset = -0.535; }
						} else if (WorldHelper.compareID(block, 139)) { jumpOffset = 0.335; } //cobblestone wall
						else { jumpOffset = 0.535; }
					}
					
					if (HelperBlock.getHelperBlockID() == 144) { jumpOffset = 0.335; }
					if (HelperBlock.getHelperBlockID() == 102) { jumpOffset = -0.135; }
			        
			        if (PlayerFacing.isXFacing()) { playerDist = mc.getRenderViewEntity().posX; } 
			        else { playerDist = mc.getRenderViewEntity().posZ; }
			        
			        if (HelperBlock.isPositiveXZFacing) { jumpPos = HelperBlock.getJumpPos() + jumpOffset; } 
			        else { jumpPos = HelperBlock.getJumpPos() - jumpOffset; }
			        
			        // for 3 over 1.0625 up carpet
			        // 0.629 distance			        
			        // 1.1439613639
			        // 1.1863137861
			        // (0.0423524222 window)
			        
			        // for daylight 3 over 1.125 upt
			        // 0.629 distance
			        // x & z oriented - (10 precision)
			        // 1.1439781408
			        // 1.1863137861
			        // (0.0423356453 window) (slightly smaller window)
			        
			        // (0.0000167769 difference)

			        //13 precision is the most precise (14 and after does not seem to be handled very well)
			        
			        double val = 0;
			        
			        val = Math.abs(playerDist - jumpPos);
			        //System.out.println(val);
			        ScaledResolution res = new ScaledResolution(mc);
			        boolean pass = false;
			        String msg = "FAIL";
			        if (jumpOffset == 0.535) {
			        	if (val >= 0.575 && val <= 0.620) { pass = true; }
			        	else if (val >= 0.86 && val <= 0.93) { pass = true; }
			        	else if (val >= 1.07 && val <= 1.22) { pass = true; }
			        	else if (val >= 1.33 && val <= 1.52) { pass = true; }
			        	else if (val >= 1.60 && val <= 1.80) { pass = true; }
			        	else if (val >= 1.88 && val <= 2.08) { pass = true; }
			        	else if (val >= 2.18 && val <= 2.30) { pass = true; }
			        	else if (val > 2.30 && val < 6) { pass = true; }
			        	else if (val >= 6) { pass = true; }
			        	if (pass) { msg = "PASS"; }
			        	drawer.pass = EnumChatFormatting.GREEN +  msg + ": " + EnumChatFormatting.WHITE + val;
			        } else if (jumpOffset == 0.629) {
			        	if (val >= 1.1439781408 && val <= 1.1863137861) {
			        		drawer.pass = EnumChatFormatting.GREEN + "PASS: " + EnumChatFormatting.WHITE + val;
			        	} else {
			        		drawer.pass = EnumChatFormatting.RED + "FAIL: " + EnumChatFormatting.WHITE + val;
			        	}
			        }
			        else {
			        	drawer.pass = "UHHH: " + val;
			        }
			        
				    if (val <= 0.2478625 && val >= 0.0001) { return true; }
				}
			}
		}
		*/
		return false;
	}
	
	public static boolean isConstantlySprinting() { return constantSprint; }
	public static boolean isEdgeJumping() { return edgeJump; }
	public static void setConstantlySprinting(boolean value) { constantSprint = value; }
	public static void setEdgeJumping(boolean value) { edgeJump = value; }
}
