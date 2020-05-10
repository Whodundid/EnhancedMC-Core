package com.Whodundid.miniMap;

import java.awt.image.BufferedImage;
import java.util.List;
import com.Whodundid.core.EnhancedMC;
import com.Whodundid.core.app.AppConfigSetting;
import com.Whodundid.core.app.AppType;
import com.Whodundid.core.app.EMCApp;
import com.Whodundid.core.app.IUseScreenLocation;
import com.Whodundid.core.app.RegisteredApps;
import com.Whodundid.core.coreEvents.emcEvents.EMCAppCalloutEvent;
import com.Whodundid.core.enhancedGui.types.interfaces.IWindowParent;
import com.Whodundid.core.terminal.gui.ETerminal;
import com.Whodundid.core.util.miscUtil.NetPlayerComparator;
import com.Whodundid.core.util.playerUtil.PlayerFacing;
import com.Whodundid.core.util.renderUtil.ScreenLocation;
import com.Whodundid.core.util.storageUtil.DynamicTextureHandler;
import com.Whodundid.core.util.storageUtil.StorageBox;
import com.Whodundid.core.util.storageUtil.StorageBoxHolder;
import com.Whodundid.core.util.worldUtil.WorldHelper;
import com.google.common.collect.Ordering;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.scoreboard.ScorePlayerTeam;
import net.minecraft.util.BlockPos;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent.ElementType;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

//Last edited: 12-14-18
//First Added: 11-16-17
//Author: Hunter Bragg

@Mod(modid = MiniMapApp.MODID, version = MiniMapApp.VERSION, name = MiniMapApp.NAME, dependencies = "required-after:enhancedmc")
public final class MiniMapApp extends EMCApp implements IUseScreenLocation {
	
	public static final String MODID = "emcminimap";
	public static final String VERSION = "0.5";
	public static final String NAME = "EMC Minimap";
	
	MiniMapDrawer drawer;
	public DynamicTextureHandler handler, border;
	private long timeSinceLastDraw;
	public volatile int downOffsetY = 0;
	public int mapSize = 150;
	public int previewSize = 157;
	public double zoom = 1;
	public double zoomXHigh = mapSize, zoomYHigh = mapSize;
	public double zoomXLow = 0, zoomYLow = 0;
	public double previewXHigh = previewSize, previewYHigh = previewSize;
	public double previewXLow = 0, previewYLow = 0;
	public boolean mapImageCreated = false;
	public static AppConfigSetting<Boolean> trackPlayers = new AppConfigSetting(Boolean.class, "trackPlayer", "Enable Player Tracking", false);
	public static AppConfigSetting<Boolean> drawVertical = new AppConfigSetting(Boolean.class, "drawVertical", "Draw Vertical", false);
	public static AppConfigSetting<Boolean> drawMap = new AppConfigSetting(Boolean.class, "drawMap", "Draw Map", true);
	public static AppConfigSetting<Boolean> drawCoords = new AppConfigSetting(Boolean.class, "drawCoords", "Draw Player Coordinates", true);
	public static AppConfigSetting<Boolean> drawMapDirections = new AppConfigSetting(Boolean.class, "drawMapDirections", "Draw Compass Directions", false);
	public static AppConfigSetting<Boolean> drawFacingDir = new AppConfigSetting(Boolean.class, "drawFacingDir", "Draw Facing Direction", false);
	public static AppConfigSetting<Boolean> drawBig = new AppConfigSetting(Boolean.class, "drawBig", "Draw Big", false);
	public static AppConfigSetting<Boolean> drawWithChatOpen = new AppConfigSetting(Boolean.class, "drawWithChatOpen", "Draw When Chat Is Open", true);
	public boolean doesEditorExist = false;
	public String findPlayer = "";
	public StorageBoxHolder<Integer, Integer> highlightedBlocks;
	ScreenLocation loc = ScreenLocation.topRight;
	protected int xPos = 0, yPos = 0;
	private static MiniMapApp instance;
	
	public MiniMapApp() {
		super(AppType.MINIMAP);
		instance = this;
		shouldLoad = false;
		addDependency(AppType.CORE, "1.0");
		setMiniMapZoom(5.5);
		setMainGui(new MiniMapGui());
		setAliases("minimap", "map");
		version = VERSION;
		author = "Whodundid";
	}
	
	public static MiniMapApp instance() { return instance; }
	
	@Override
	public void terminalRegisterCommandEvent(ETerminal conIn, boolean runVisually) {
		EnhancedMC.getTerminalHandler().registerCommand(new TrackCommand(), conIn, runVisually);
	}
	
	@Override
	public void onPostInit(FMLPostInitializationEvent e) {
		drawer = new MiniMapDrawer(this);
		handler = new DynamicTextureHandler(mc.getTextureManager(), new BufferedImage(301, 301, BufferedImage.TYPE_INT_RGB));
		border = new DynamicTextureHandler(mc.getTextureManager(), new BufferedImage(75, 75, BufferedImage.TYPE_INT_RGB));
		timeSinceLastDraw = System.currentTimeMillis();
		highlightedBlocks = new StorageBoxHolder();
		
		doesEditorExist = RegisteredApps.isAppRegistered(AppType.WORLDEDITOR);
	}
	
	@Override
	public void clientTickEvent(TickEvent.ClientTickEvent e) {
		if (isEnabled()) {
			if (mc.thePlayer != null && mc.theWorld != null && mc.getRenderViewEntity() != null) {
				if (System.currentTimeMillis() - timeSinceLastDraw >= 75) {
					drawWorld(drawVertical.get());
					drawMapOutline();
					if (!trackPlayers.get()) {
						int mWidth = handler.getTextureWidth() / 2;
						handler.GBI().setRGB(mWidth, mWidth, 0xFF0000);
					}					
					try {						
						handler.updateTextureData(handler.GBI());
					} catch (Exception q) { q.printStackTrace(); }
					if (!mapImageCreated) { mapImageCreated = true; }
					timeSinceLastDraw = System.currentTimeMillis();
				}
			}
		}
	}
	
	@Override
	public void OverlayPostEvent(RenderGameOverlayEvent.Post e) {
		if (e.type == ElementType.ALL) {
			if (isEnabled()) { drawer.draw(); }
		}
	}
	
	@Override
	public void subModCalloutEvent(EMCAppCalloutEvent e) {
		
	}
	
	@Override
	public Object sendArgs(Object... args) {
		if (isEnabled()) {
    		if (args.length >= 1) {
    			
    			if (args[0] instanceof String) {
    				String cmd = (String) args[0];
    				
    				switch (cmd) {
    				case "MiniMap: track": if (args[1] instanceof String) { findPlayer = (String) args[1]; } break;
    				}
    			}
    		}
		}
		return null;
	}
	
	@Override
	public void worldUnloadEvent(WorldEvent.Unload e) {
		findPlayer = "";
	}
	
	private void drawMapOutline() {
		for (int x = 0; x < border.GBI().getWidth(); x++) {
			border.GBI().setRGB(x, 0, 0xC8C8C8);
			border.GBI().setRGB(x, border.GBI().getHeight() - 1, 0xC8C8C8);
			border.GBI().setRGB(0, x, 0xC8C8C8);
			border.GBI().setRGB(border.GBI().getWidth() - 1, x, 0xC8C8C8);
		}
		if (!drawVertical.get()) {
			border.GBI().setRGB(37, 0, 0xFF0000);
			border.GBI().setRGB(36, 0, 0xFF5500);
			border.GBI().setRGB(38, 0, 0xFF5500);
		}
	}
	
	private void drawWorld(boolean vertical) {
		BlockPos center = new BlockPos(mc.getRenderViewEntity().posX, mc.getRenderViewEntity().getEntityBoundingBox().minY, mc.getRenderViewEntity().posZ);
		try {
			if (vertical) { drawVerticalWorld(center); } 
			else { drawHorizontalWorld(center); }
		} catch (Exception e) { e.printStackTrace(); }
	}
	
	private void drawHorizontalWorld(BlockPos center) {
		int w = handler.getTextureWidth() / 2;
		int mWidth = (int) Math.ceil(w / (this.zoomXHigh / w));
		for (int z = center.getZ() - mWidth; z < center.getZ() + mWidth + 1; z++) {
			for (int x = center.getX() - mWidth; x < center.getX() + mWidth + 1; x++) {				
				/*BlockPos pos = new BlockPos(x, 255, z);
				int i = 255;
				while (i > 0 && WorldHelper.getBlockID(pos) == 0) {
					i--;
					pos = new BlockPos(x, i, z);
				}*/
				BlockPos pos = new BlockPos(x, center.getY() - downOffsetY, z);
				IBlockState state = mc.theWorld.getBlockState(pos);
				int color = 0;
				if (WorldHelper.checkBlockForMapDraw(pos, state)) {
					color = WorldHelper.getCorrectMapColor(pos, state);
					if (WorldHelper.isFenceBlock(state) || (WorldHelper.getBlockID(state.getBlock()) == 107 && state.getBlock().getMetaFromState(state) == 2)) {
						//color = 0xff0000;
					}
				}
				//System.out.println(handler.getTextureWidth());
				handler.GBI().setRGB(x - center.getX() + w, z - center.getZ() + w, color);
				if (trackPlayers.get()) {
					drawPlayerLocations(x, downOffsetY, z, center);
				}
			}
		}
	}
	
	private void drawVerticalWorld(BlockPos center) {
		if (PlayerFacing.isXFacing()) {
			for (int x = center.getX() - 36; x < center.getX() + 37; x++) {
				for (int y = center.getY() - 36; y < center.getY() + 37; y++) {
					BlockPos pos = new BlockPos(x, y, center.getZ());
					IBlockState state = mc.theWorld.getBlockState(pos);
					int color = 0;
					if (WorldHelper.checkBlockForMapDraw(pos, state)) {
						color = WorldHelper.getCorrectMapColor(pos, state);
					}
					
					handler.GBI().setRGB(x - center.getX() + 37, y - center.getY() + 37, color);
					if (trackPlayers.get()) {
						drawPlayerLocations(x, y, center.getZ(), center);
					}					
				}
			}
		} else {
			for (int z = center.getZ() - 36; z < center.getZ() + 37; z++) {
				for (int y = center.getY() - 36; y < center.getY() + 37; y++) {
					BlockPos pos = new BlockPos(center.getX(), y, z);
					IBlockState state = mc.theWorld.getBlockState(pos);
					int color = 0;
					if (WorldHelper.checkBlockForMapDraw(pos, state)) {
						color = WorldHelper.getCorrectMapColor(pos, state);
					}
					
					handler.GBI().setRGB(z - center.getZ() + 37, y - center.getY() + 37, color);
					if (trackPlayers.get()) {
						drawPlayerLocations(center.getX(), y, z, center);
					}					
				}
			}
		}
	}
	
	private void drawPlayerLocations(int x, int y, int z, BlockPos center) {
		List<EntityPlayer> list = mc.theWorld.playerEntities;
		NetHandlerPlayClient nethandlerplayclient = mc.thePlayer.sendQueue;
		Ordering<NetworkPlayerInfo> order = Ordering.from(new NetPlayerComparator());
		List<NetworkPlayerInfo> nameList = order.sortedCopy(nethandlerplayclient.getPlayerInfoMap());
		
		for (EntityPlayer p : list) {
			//System.out.println(p.getName());
			if (drawVertical.get()) {
				if (PlayerFacing.isXFacing()) {
					if (p.getPosition().getX() == x && p.getPosition().getY() == y) {
						handler.GBI().setRGB(x - center.getX() + 37, y - center.getY() + 37, returnColor(nameList, p.getName()));
					}
				} else {
					if (p.getPosition().getZ() == z && p.getPosition().getY() == y) {
						handler.GBI().setRGB(z - center.getZ() + 37, y - center.getY() + 37, returnColor(nameList, p.getName()));
					}
				}
			} else {
				int mWidth = handler.getTextureWidth() / 2;
				if (p.getPosition().getX() == x && p.getPosition().getZ() == z) {
					handler.GBI().setRGB(x - center.getX() + mWidth, z - center.getZ() + mWidth, returnColor(nameList, p.getName()));
				}
			}			
		}
	}
	
	private int returnColor(List<NetworkPlayerInfo> in, String parseName) {
		for (NetworkPlayerInfo info : in) {
			String name;
			name = (info.getDisplayName() != null) ? info.getDisplayName().getUnformattedText() : ScorePlayerTeam.formatPlayerName(info.getPlayerTeam(), info.getGameProfile().getName());
			if (name.contains(parseName)) {
				if (mc.thePlayer != null && name.contains(mc.thePlayer.getName())) {
					return 0x00ff00;
				}
				else if (name.contains(findPlayer)) {
					return 0xff0000;
				}
							
			}
		}
		
		return 0x7b7b7b;
	}
	
	private String getOwnColor(List<NetworkPlayerInfo> in) {
		try {
			for (NetworkPlayerInfo info : in) {
				String name;
				name = (info.getDisplayName() != null) ? info.getDisplayName().getUnformattedText() : ScorePlayerTeam.formatPlayerName(info.getPlayerTeam(), info.getGameProfile().getName());
				if (name.contains(mc.thePlayer.getName())) {
					return name.substring(1, 2);
				}
			}
		} catch (Exception e) { e.printStackTrace(); }
		return "0";
	}
	
	public void zoomMiniMap(int scrollChange) {
		double currentZoom = zoom;
		zoom = currentZoom += (scrollChange * 0.25);
		setMiniMapZoom(zoom);
	}
	
	public void setMiniMapZoom(double zoomIn) {
		zoom = zoomIn;
		if (zoom < 1) { zoom = 1; }
		if (zoom > 10) { zoom = 10; }
		
		zoomXHigh = mapSize * zoom;
		zoomYHigh = mapSize * zoom;
		previewXHigh = previewSize * zoom;
		previewYHigh = previewSize * zoom;
		
		zoomXLow = (zoomXHigh - mapSize) / 2;
		zoomYLow = (zoomYHigh - mapSize) / 2;
		previewXLow = (previewXHigh - previewSize) / 2;
		previewYLow = (previewYHigh - previewSize) / 2;
	}
	
	public int getMapRadius() { int w = handler.getTextureWidth() / 2; int mWidth = (int) Math.ceil(w / (zoomXHigh / w)); return mWidth; }
	
	@Override public void setLocation(ScreenLocation locIn) { loc = locIn; }
	@Override public void setLocation(int xIn, int yIn) { loc = ScreenLocation.custom; xPos = xIn; yPos = yIn; }
	@Override public StorageBox<Integer, Integer> getLocation() { return new StorageBox<Integer, Integer>(xPos, yPos); }
	@Override public ScreenLocation getScreenLocation() { return loc; }
	@Override public IWindowParent getScreenLocationGui() { return null; }
	
	public boolean isMapImageCreated() { return mapImageCreated; }
}
