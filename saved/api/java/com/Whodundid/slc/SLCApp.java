package com.Whodundid.slc;

import com.Whodundid.core.app.AppType;
import com.Whodundid.core.app.EMCApp;
import com.Whodundid.core.util.storageUtil.EArrayList;
import com.Whodundid.core.util.storageUtil.StorageBox;
import com.Whodundid.core.util.storageUtil.StorageBoxHolder;
import com.Whodundid.slc.config.*;
import com.Whodundid.slc.gui.SLCGlobalOptionsGui;
import com.Whodundid.slc.gui.SLCMainGui;
import com.Whodundid.slc.gui.SLCPartGui;
import com.Whodundid.slc.util.*;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.gameevent.TickEvent;

@Mod(modid = SLCApp.MODID, version = SLCApp.VERSION, name = SLCApp.NAME, dependencies = "required-after:enhancedmc")
public final class SLCApp extends EMCApp {
	
	public static final String MODID = "skinlayercontrol";
	public static final String VERSION = "4.0";
	public static final String NAME = "Skin Layer Control";
	
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
    
    public SLCApp() {
    	super(AppType.SLC);
    	shouldLoad = false;
    	addDependency(AppType.CORE, "1.0");
    	partModifier = new PartModifier(this);
    	partStats = new PartStats(this);
    	parts = new EArrayList();
    	curPartRates = new StorageBoxHolder();
    	configManager.setMainConfig(new SLCGlobalConfig(this, "slc_global"));
    	setMainGui(new SLCMainGui());
    	addGui(new SLCGlobalOptionsGui(), new SLCPartGui());
    	setAliases("skincontrol", "skinswitcher", "slc");
		
		for (LayerTypes t : LayerTypes.values()) { parts.add(new SkinPart(t)); }
    	for (LayerTypes t : LayerTypes.values()) { curPartRates.put(t, 0l); }
    	
    	version = VERSION;
		author = "Whodundid";
    }
    
    @Override
    public void clientTickEvent(TickEvent.ClientTickEvent e) {
    	if (isEnabled()) {
    		if (checkIfAnyPartsEnabled()) {
        		for (SkinPart p : parts) { checkPartUpdateStatus(p); }
        	}
    	}    	
    }
    
    public void prepareProfileLoad(EntityJoinWorldEvent event) {
    	if (isEnabled()) {
    		if (event.entity != null && event.entity instanceof EntityPlayerSP) { loadSelectedProfile(); }
    	}    	
    }
    
    public void loadSelectedProfile() {
    	if (isEnabled()) {
    		partModifier.setAllPartsToState(false);
        	SLCLoadSkinConfigProfile.load(this, Integer.toString(currentLoadedProfile));
        	//SLSGlobalConfig.loadGlobalConfig(true);
        	partModifier.resetCurrentPartRates();
    	}    	
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
    	} else { globalSwitchingEnabled = false; }
    }
    
    public void setGlobalBlink(boolean state) {
    	if (state) {
    		if (!globalBlinkingEnabled) { globalBlinkingEnabled = true; }
    		globalSwitchingEnabled = false;
    	} else { globalBlinkingEnabled = false; }
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
    	} else if ((System.currentTimeMillis() - globalCurrentBlinkTick) >= globalBlinkDelay) {
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
                		} else {
                			if ((System.currentTimeMillis() - curPartRate) >= part.getSwitchSpeed()) {
                				part.togglePartState();
                    			updateMCSkinLayers(part);
                    			curPartRates.put(part.getType(), (long) 0);
                			}
                		}        			
            		}
        		} else if (part.isBlinking()) {
        			if (curPartRate == 0) { curPartRates.put(part.getType(), System.currentTimeMillis()); }
        			else {
        				if (part.hasAlreadyBlinked()) {
                			if ((System.currentTimeMillis() - curPartRate) >= part.getBlinkDuration()) {
                				part.togglePartState();
                				updateMCSkinLayers(part);
                				part.setAlreadyBlinked(false);
                				curPartRates.put(part.getType(), (long) 0);
                			}
                		} else if ((System.currentTimeMillis() - curPartRate) >= part.getBlinkSpeed()) {
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