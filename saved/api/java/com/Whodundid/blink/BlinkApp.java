package com.Whodundid.blink;

import org.lwjgl.input.Keyboard;
import com.Whodundid.core.EnhancedMC;
import com.Whodundid.core.app.AppType;
import com.Whodundid.core.app.EMCApp;
import com.Whodundid.core.terminal.gui.ETerminal;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.MouseEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.gameevent.TickEvent;

//Last edited: 10-16-18
//First Added: 4-19-18
//Author: Hunter Bragg

@Mod(modid = BlinkApp.MODID, version = BlinkApp.VERSION, name = BlinkApp.NAME, dependencies = "required-after:enhancedmc")
public class BlinkApp extends EMCApp {
	
	public static final String MODID = "blink";
	public static final String VERSION = "0.1";
	public static final String NAME = "Blink";
	
	protected BlinkDrawer drawer;
	public int blinkCount = 3;
	public long blinkRefreshCoolDown = 3000;
	public boolean refreshing = false;
	public long refreshProgress = 0;
	private boolean blinking = false;
	private long blinkProgress = 0;
	private double previousX;
	private double previousZ;
	private static BlinkApp instance;
	
	public BlinkApp() {
		super(AppType.BLINK);
		instance = this;
		shouldLoad = false;
		addDependency(AppType.CORE, "1.0");
		drawer = new BlinkDrawer(this);
		setMainGui(new BlinkGui());
		setAliases("blinks", "blink");
	}
	
	public static BlinkApp instance() { return instance; }
	
	@Override
	public void clientTickEvent(TickEvent.ClientTickEvent e) {
		if (isEnabled()) {
			if (blinkCount < 3) {
				if (!refreshing) {
					refreshing = true;
					refreshProgress = System.currentTimeMillis();
				} else {
					if (System.currentTimeMillis() - refreshProgress >= blinkRefreshCoolDown) {
						blinkCount++;
						refreshing = false;
					}
				}
			}
			if (blinking) {
				if (System.currentTimeMillis() - blinkProgress >= 25) {
					mc.thePlayer.motionX = previousX;
					mc.thePlayer.motionZ = previousZ;					
					previousX = 0;
					previousZ = 0;
					blinking = false;
				}
			}
		}		
	}
	
	@Override
	public void OverlayPostEvent(RenderGameOverlayEvent.Post e) {
		if (isEnabled()) { drawer.drawBlinkCharges(); }
	}
	
	@Override
	public void mouseEvent(MouseEvent e) {
		if (isEnabled()) {
			if (e.button == 1 && e.buttonstate) {
				if (mc.thePlayer.getHeldItem() == null) {
					if (blinkCount > 0) {
						blinkCount--;
						
						blinking = true;
						blinkProgress = System.currentTimeMillis();
						
						previousX = mc.thePlayer.motionX;
						previousZ = mc.thePlayer.motionZ;
						
						//System.out.println(mc.thePlayer.getLookVec());
						
						if (Keyboard.isKeyDown(mc.gameSettings.keyBindLeft.getKeyCode())) {
							
						} else if (Keyboard.isKeyDown(mc.gameSettings.keyBindRight.getKeyCode())) {
							
						} else if (Keyboard.isKeyDown(mc.gameSettings.keyBindBack.getKeyCode())) {
							
						} else {
							mc.thePlayer.motionX = mc.thePlayer.getLookVec().xCoord * 9.5;
							mc.thePlayer.motionZ = mc.thePlayer.getLookVec().zCoord * 9.5;
						}
						
						if (blinkCount == 2) {
							mc.thePlayer.playSound(new ResourceLocation("blink", "blink1").toString(), 6.0f, 1.0f);
						} else if (blinkCount == 1) {
							mc.thePlayer.playSound(new ResourceLocation("blink", "blink2").toString(), 6.0f, 1.0f);
						} else {
							mc.thePlayer.playSound(new ResourceLocation("blink", "blink3").toString(), 6.0f, 1.0f);
						}		
					}
				}
			}
		}
	}
	
	@Override
	public void terminalRegisterCommandEvent(ETerminal conIn, boolean runVisually) {
		EnhancedMC.getTerminalHandler().registerCommand(new BlinkCooldown(), conIn, runVisually);
	}
	
	public void resetBlinkCooldown() { blinkRefreshCoolDown = 3000; }
	public long getBlinkCooldown() { return blinkRefreshCoolDown; }
	public BlinkApp setBlinkCooldown(long in) { blinkRefreshCoolDown = in; return this; }
}
