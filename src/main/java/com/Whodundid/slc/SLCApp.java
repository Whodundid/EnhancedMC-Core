package com.Whodundid.slc;

import com.Whodundid.core.EnhancedMC;
import com.Whodundid.core.app.AppType;
import com.Whodundid.core.app.EMCApp;
import com.Whodundid.core.util.resourceUtil.EResource;
import com.Whodundid.core.util.storageUtil.EArrayList;
import com.Whodundid.core.util.storageUtil.StorageBox;
import com.Whodundid.core.util.storageUtil.StorageBoxHolder;
import com.Whodundid.slc.config.*;
import com.Whodundid.slc.util.*;
import com.Whodundid.slc.window.SLCGlobalOptionsWindow;
import com.Whodundid.slc.window.SLCMainWindow;
import com.Whodundid.slc.window.SLCNew;
import com.Whodundid.slc.window.SLCPartWindow;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.gameevent.TickEvent;

@Mod(modid = SLCApp.MODID, version = SLCApp.VERSION, name = SLCApp.NAME, dependencies = "required-after:enhancedmc")
public final class SLCApp extends EMCApp {
	
	public static final String MODID = "skinlayercontrol";
	public static final String VERSION = "4.0";
	public static final String NAME = "Skin Layer Control";
	
	//---------
	//Resources
	//---------
	
	public static final SLCResources resources = new SLCResources();
	
	public PartModifier partModifier;
	public PartStats partStats;
	public final int MinRate = 200;
	public final int MinBlinkDurRate = 200;
	public final int MaxRate = 3600000;
	public int lastX, lastY;
	public int currentSkinProfile = 1;
	public int currentLoadedProfile = 1;
	public int defaultLoadedProfile = 1;
	public GlobalModes currentMode = GlobalModes.OFF;
	public boolean savePartStates = true;
	public int currentChangeValue = 25;
	public boolean skinFrontFacing = true;
	public PartModes resetMode = PartModes.N;
	public LayerTypes currentPart = null;
	public boolean globalOn = true;
	public boolean globalSwitchingEnabled = false;
	public boolean globalBlinkingEnabled = false;
	private boolean globalAlreadyBlinked = false;
	private boolean globalFlipBlink = false;
	private long globalCurrentSwitchTick = 0;
	private long globalCurrentBlinkTick = 0;
	public final int defaultSwitchSpeed = 1000;
	public final int defaultBlinkDelay = 6000;
	public final int defaultBlinkDuration = 200;
	private int globalSwitchSpeed = defaultSwitchSpeed;
	private int globalBlinkDelay = defaultBlinkDelay;
	private int globalBlinkDuration = defaultBlinkDuration;
	private EArrayList<SkinPart> parts;
	private StorageBoxHolder<LayerTypes, Long> curPartRates;
	
	private static SLCApp instance = null;
	
	public SLCApp() {
		super(AppType.SLC);
		instance = this;
	}
	
	@Override
	public void build() {
		version = VERSION;
		versionDate = "June 11, 2020";
		author = "Whodundid";
		artist = "Mr.JamminOtter";
		donation = new StorageBox("Consider donating to support EMC development!", "https://www.paypal.me/Whodundid");
		additionalInfo = "" + EnumChatFormatting.RED + "Will not work on mc.hypixel.net!";
		blockedOnHypixel = true;
		addDependency(AppType.CORE, "1.0");
		
		configManager.setMainConfig(new SLCGlobalConfig(this, "slc_global"));
		setAliases("skincontrol", "skinswitcher", "slc");
		
		windows.clear();
		
		setMainWindow(new SLCMainWindow());
		addWindow(new SLCGlobalOptionsWindow(), new SLCPartWindow(), new SLCNew());
		
		setResources(resources);
		logo = new EArrayList<EResource>(SLCResources.logo0, SLCResources.logo1);
		logoInterval = 1000l;
		
		partModifier = new PartModifier(this);
		partStats = new PartStats(this);
		parts = new EArrayList();
		curPartRates = new StorageBoxHolder();
		
		parts.clear();
		curPartRates.clear();
		
		for (LayerTypes t : LayerTypes.values()) { parts.add(new SkinPart(t)); }
		for (LayerTypes t : LayerTypes.values()) { curPartRates.put(t, 0l); }
	}
	
	public static SLCApp instance() { return instance; }
	
	@Override
	public void clientTickEvent(TickEvent.ClientTickEvent e) {
		if (checkIfAnyPartsEnabled()) {
			for (SkinPart p : parts) { checkPartUpdateStatus(p); }
		}
		
		if (EnhancedMC.isHypixel()) {
			setEnabled(false);
			canBeEnabled = false;
			EnhancedMC.reloadAllWindows();
		}
		else { canBeEnabled = true; }
	}
	
	@Override
	public void serverJoinEvent(EntityJoinWorldEvent e) {
		if (e.entity == mc.thePlayer) {
			if (EnhancedMC.isHypixel()) {
				setEnabled(false);
				canBeEnabled = false;
			}
			else { canBeEnabled = true; }
		}
	}
	
	public void prepareProfileLoad(EntityJoinWorldEvent event) {
		if (event.entity != null && event.entity instanceof EntityPlayerSP) { loadSelectedProfile(); }
	}
	
	public void loadSelectedProfile() {
		partModifier.setAllPartsToState(false);
		SLCLoadSkinConfigProfile.load(this, Integer.toString(currentLoadedProfile));
		//SLSGlobalConfig.loadGlobalConfig(true);
		partModifier.resetCurrentPartRates();
	}
	
	public SkinPart getPart(LayerTypes type) {
		for (SkinPart p : parts) {
			if (p.compareType(type)) { return p; }
		}
		return null;
	}
	
	protected boolean checkIfAnyPartsEnabled() {
		for (SkinPart p : parts) {
			if (p.isEnabled()) { return true; }
		}
		return false;
	}
	
	protected boolean checkIfAnyPartsBlink() {
		for (SkinPart p : parts) {
			if (p.isEnabled() && p.isBlinking()) { return true; }
		}
		return false;
	}
	
	protected boolean checkIfAnyPartsSwitch() {
		for (SkinPart p : parts) {
			if (p.isEnabled() && p.isSwitching()) { return true; }
		}
		return false;
	}
	
	public void setGlobalSwitch(boolean state) {
		if (state) {
			if (!globalSwitchingEnabled) { globalSwitchingEnabled = true; }
			globalBlinkingEnabled = false;
			globalAlreadyBlinked = false;
		}
		else { globalSwitchingEnabled = false; }
	}
	
	public void setGlobalBlink(boolean state) {
		if (state) {
			if (!globalBlinkingEnabled) { globalBlinkingEnabled = true; }
			globalSwitchingEnabled = false;
		}
		else { globalBlinkingEnabled = false; }
	}
	
	public void toggleGlobalBlinkFlipped() {
		for (LayerTypes t : LayerTypes.values()) { partModifier.flipPartState(t); }
	}
	
	private void checkPartUpdateStatus(SkinPart part) {
		if (globalSwitchingEnabled) { testGlobalSwitching(); }
		else if (globalBlinkingEnabled) { testGlobalBlinking(); }
		else { testGenericTiming(part); }
	}
	
	private void testGlobalSwitching() {
		if (globalCurrentSwitchTick == 0) { globalCurrentSwitchTick = System.currentTimeMillis(); }
		if ((System.currentTimeMillis() - globalCurrentSwitchTick) >= globalSwitchSpeed) {
			toggleParts();
			globalCurrentSwitchTick = 0; 
		}
	}
	
	private void testGlobalBlinking() {
		if (globalCurrentBlinkTick == 0) { globalCurrentBlinkTick = System.currentTimeMillis(); }
		if (globalAlreadyBlinked) {
			if ((System.currentTimeMillis() - globalCurrentBlinkTick) >= globalBlinkDuration) {
				toggleParts();
				globalAlreadyBlinked = false;
				globalCurrentBlinkTick = 0;
			}
		}
		else if ((System.currentTimeMillis() - globalCurrentBlinkTick) >= globalBlinkDelay) {
			toggleParts();
			globalAlreadyBlinked = true;
			globalCurrentBlinkTick = 0;
		}
	}
	
	private void testGenericTiming(SkinPart part) {
		if (globalOn) {
			if (part.isEnabled()) {
				long curPartRate = getCurrentPartRate(part);
				if (part.isSwitching()) {
					if (curPartRate == 0) { curPartRates.put(part.getType(), System.currentTimeMillis()); }
					else {
						if (!part.hasOffsetBeenUsed()) {
							if ((System.currentTimeMillis() - curPartRate) >= part.getOffset()) {
								part.setOffsetUsed(true);
								curPartRates.put(part.getType(), (long) 0);
							}
						}
						else if ((System.currentTimeMillis() - curPartRate) >= part.getSwitchSpeed()) {
							part.togglePartState();
							updateMCSkinLayers(part);
							curPartRates.put(part.getType(), (long) 0);
						}
					}
				}
				else if (part.isBlinking()) {
					if (curPartRate == 0) { curPartRates.put(part.getType(), System.currentTimeMillis()); }
					else {
						if (part.hasAlreadyBlinked()) {
							if ((System.currentTimeMillis() - curPartRate) >= part.getBlinkDuration()) {
								part.togglePartState();
								updateMCSkinLayers(part);
								part.setAlreadyBlinked(false);
								curPartRates.put(part.getType(), (long) 0);
							}
						}
						else if ((System.currentTimeMillis() - curPartRate) >= part.getBlinkSpeed()) {
							part.setAlreadyBlinked(true);
							part.togglePartState();
							updateMCSkinLayers(part);
							curPartRates.put(part.getType(), (long) 0);
						}
					}
				}
			}
		}
	}
	
	private void toggleParts() {
		for (SkinPart p : parts) {
			if (p.isEnabled()) {
				p.togglePartState();
				updateMCSkinLayers(p);
			}
		}
	}
	
	public void setGlobalSwitchSpeed(int speed) {
		globalSwitchSpeed = speed;
		SLCSaveSkinConfigProfile.updateProfile(this, currentSkinProfile); 
	}
	
	public void setGlobalBlinkDelay(int speed) {
		globalBlinkDelay = speed;
		SLCSaveSkinConfigProfile.updateProfile(this, currentSkinProfile); 
	}
	
	public void setGlobalBlinkDuration(int speed) {
		globalBlinkDuration = speed;
		SLCSaveSkinConfigProfile.updateProfile(this, currentSkinProfile);
	}
	
	public long getCurrentPartRate(LayerTypes type) {
		StorageBox<LayerTypes, Long> b = curPartRates.getBoxWithObj(type);
		return b != null ? b.getValue() : 0l;
	}
	
	public void updateMCSkinLayers(SkinPart part) { mc.gameSettings.setModelPartEnabled(part.getType().getMCType(), part.getState()); }
	public SLCApp rotateSkin(String isFront) { skinFrontFacing = isFront.equals("Front"); return this; }
	
	public PartModifier getPartModifier() { return partModifier; }
	public PartStats getPartStats() { return partStats; }
	public StorageBoxHolder<LayerTypes, Long> getCurrentPartRates() { return curPartRates; }
	private long getCurrentPartRate(SkinPart part) { return getCurrentPartRate(part.getType()); }
	public EArrayList<SkinPart> getParts() { return parts; }
	public boolean getGlobalSwitchingStatus() { return globalSwitchingEnabled; }
	public boolean getGlobalBlinkingStatus() { return globalBlinkingEnabled; }
	public boolean getGlobalBlinkFlipped() { return globalFlipBlink; }
	public int getGlobalSwitchingSpeed() { return globalSwitchSpeed; }
	public int getGlobalBlinkingSpeed() { return globalBlinkDelay; }
	public int getGlobalBlinkDuration() { return globalBlinkDuration; }
	public int getCurrentInterval() { return currentChangeValue; }
	public int getDefaultLoadedProfile() { return defaultLoadedProfile; }
	public String getDefaultSkinFacing() { return skinFrontFacing ? "front" : "back"; }
	
}
