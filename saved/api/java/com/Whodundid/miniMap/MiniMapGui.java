package com.Whodundid.miniMap;

import com.Whodundid.core.app.AppType;
import com.Whodundid.core.app.RegisteredApps;
import com.Whodundid.core.enhancedGui.guiObjects.actionObjects.EGuiButton;
import com.Whodundid.core.enhancedGui.guiObjects.actionObjects.EGuiSlider;
import com.Whodundid.core.enhancedGui.guiObjects.actionObjects.EGuiTextField;
import com.Whodundid.core.enhancedGui.guiObjects.basicObjects.EGuiContainer;
import com.Whodundid.core.enhancedGui.guiObjects.utilityObjects.EGuiScreenLocationSelector;
import com.Whodundid.core.enhancedGui.objectEvents.EventFocus;
import com.Whodundid.core.enhancedGui.types.WindowParent;
import com.Whodundid.core.enhancedGui.types.interfaces.IEnhancedActionObject;
import com.Whodundid.core.enhancedGui.types.interfaces.IEnhancedGuiObject;
import com.Whodundid.core.util.storageUtil.EDimension;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.EnumChatFormatting;

public class MiniMapGui extends WindowParent {
	
	MiniMapApp map = (MiniMapApp) RegisteredApps.getApp(AppType.MINIMAP);
	EGuiTextField downOffsetInput, trackPlayerName, zoomEntry;
	EGuiButton drawPlayerLocations, drawVertical, drawMap, drawCoords, drawDirections, drawFacingDir;
	EGuiButton drawChat, clearTrack, resetYAxis, zoomReset, gotoHotKeys;
	EGuiContainer location, zooming, tracking, yAxis, mapProperties, mapVisual, colorKey, hotkeys;
	EGuiScreenLocationSelector locationSelector;
	EGuiSlider zoomSlider;
	
	public MiniMapGui() { super(); }
	public MiniMapGui(Object oldGuiIn) { super(oldGuiIn); }
	public MiniMapGui(IEnhancedGuiObject parentIn) { super(parentIn); }
	public MiniMapGui(IEnhancedGuiObject parentIn, Object oldGuiIn) { super(parentIn, oldGuiIn); }
	public MiniMapGui(IEnhancedGuiObject parentIn, int posX, int posY) { super(parentIn, posX, posY); }
	public MiniMapGui(IEnhancedGuiObject parentIn, int posX, int posY, Object oldGuiIn) { super(parentIn, posX, posY, oldGuiIn); }
	
	@Override
	public void initGui() {
		centerObjectWithSize(560, 302);
		super.initGui();
		this.setObjectName("Minimap Settings");
	}
	
	@Override
	public void initObjects() {
		//location
		
		defaultHeader(this);
		
		location = new EGuiContainer(this, startX + 2, startY + 2, 195, 162) {
			{ setTitle("MiniMap Location"); }
		};
		EDimension lod = location.getDimensions();
		locationSelector = new EGuiScreenLocationSelector(this, map, lod.startX + 47, lod.startY + 49, 100);
		locationSelector.setDisplayName("MiniMap");
		locationSelector.setActionReciever(this);
		location.addObject(locationSelector);
		
		//hotkeys
		
		hotkeys = new EGuiContainer(this, startX + 2, startY + 213, 195, 87) {
			{ setTitle("MiniMap HotKeys"); }
			@Override
			public void drawObject(int mXIn, int mYIn) {
				super.drawObject(mXIn, mYIn);
			}
		};
		
		//zooming
		
		zooming = new EGuiContainer(this, startX + 198, startY + 183, 163, 117) {
			{ setTitle("Zooming"); }
			@Override
			public void drawObject(int mXIn, int mYIn) {
				super.drawObject(mXIn, mYIn);
				drawStringWithShadow("Custom Zoom Entry", midX - 22, startY + 55, 0xb2b2b2);
				drawStringWithShadow("Current draw radius: ", startX + 11, endY - 17, 0xffd800);
				drawStringWithShadow("" + map.getMapRadius(), startX + 124, endY - 17, 0x00ff00);
			}
		};
		EDimension zod = zooming.getDimensions();
		zoomSlider = new EGuiSlider(zooming, zod.startX + 10, zod.startY + 25, zod.width - 20, 18, 1, 10, (float) map.zoom, false);
		zoomReset = new EGuiButton(zooming, zod.startX + 10, zod.startY + 74, 85, 18, "Reset Zoom");
		zoomEntry = new EGuiTextField(zooming, zod.startX + 11, zod.startY + 50, 40, 17) {
			{ setText("" + zoomSlider.displayValue); }
			@Override
			public void keyPressed(char typedChar, int keyCode) {
				super.keyPressed(typedChar, keyCode);
				if (keyCode == 28) {
					if (!zoomEntry.getText().isEmpty()) {
						
					}
				}
			}
		};
		zoomSlider.setActionReciever(this);
		zoomReset.setActionReciever(this);
		zooming.addObject(zoomSlider, zoomReset, zoomEntry);
		
		//tracking
		
		tracking = new EGuiContainer(this, startX + 362, startY + 213, 196, 87) {
			{ setTitle("Player Tracking"); }
			@Override
			public void drawObject(int mXIn, int mYIn) {
				super.drawObject(mXIn, mYIn);
				drawStringWithShadow("Tracking:", startX + 7, startY + 48, 0xffd800);
				drawStringWithShadow("Draw player locations", startX + 59, endY - 19, 0xb2b2b2);
				drawStringWithShadow(map.findPlayer, startX + 60, startY + 48, 0x00ffdc);
			}
		};
		EDimension trd = tracking.getDimensions();
		clearTrack = new EGuiButton(tracking, trd.startX + 122, trd.startY + 23, 45, 18, "Clear");
		trackPlayerName = new EGuiTextField(tracking, trd.startX + 7, trd.startY + 24, 105, 16) {
			{ setMaxStringLength(16).setText(map.findPlayer.isEmpty() ? EnumChatFormatting.GRAY + "enter a name.." : map.findPlayer); }
			@Override public void keyPressed(char typedChar, int keyCode) {
				super.keyPressed(typedChar, keyCode);
				if (keyCode == 28) { map.findPlayer = trackPlayerName.getText(); }
			}
			@Override public void onFocusGained(EventFocus eventIn) { 
				if (getText().equals(EnumChatFormatting.GRAY + "enter a name..")) { setText(""); } setSelectionPos(0);
			}
			@Override public void onFocusLost(EventFocus eventIn) {
				setText(map.findPlayer.isEmpty() ? EnumChatFormatting.GRAY + "enter a name.." : map.findPlayer);
				setSelectionPos(getText().length());
			}
		};
		drawPlayerLocations = new EGuiButton(tracking, trd.startX + 6, trd.endY - 24, 45, 18).setTrueFalseButton(true, map.trackPlayers);
		clearTrack.setActionReciever(this);
		drawPlayerLocations.setActionReciever(this);
		tracking.addObject(trackPlayerName, drawPlayerLocations, clearTrack);
		
		//yAxis
		
		yAxis = new EGuiContainer(this, startX + 362, startY + 165, 196, 47) {
			{ setTitle("Y-Axis Offset"); }
			@Override
			public void drawObject(int mXIn, int mYIn) {
				super.drawObject(mXIn, mYIn);
				drawStringWithShadow("Current offset:", startX + 94, startY + 28, 0xffd800);
				drawStringWithShadow("" + map.downOffsetY, startX + 178, startY + 28, 0x00ff00);
			}
		};
		EDimension yad = yAxis.getDimensions();
		resetYAxis = new EGuiButton(yAxis, yad.startX + 43, yad.startY + 23, 45, 18, "Reset");
		downOffsetInput = new EGuiTextField(yAxis, yad.startX + 7, yad.startY + 24, 26, 16) {
			{ setMaxStringLength(3).setOnlyAcceptNumbers(true).setText("" + map.downOffsetY).setAllowClipboardPastes(false); }
			@Override public void keyPressed(char typedChar, int keyCode) {
				super.keyPressed(typedChar, keyCode);
				if (keyCode == 28) {
					if (getText().isEmpty()) { map.downOffsetY = 0; }
					else { map.downOffsetY = Integer.parseInt(getText()); }
				}
			}
			@Override public void onFocusGained(EventFocus eventIn) { setSelectionPos(0); }
			@Override public void onFocusLost(EventFocus eventIn) {
				setSelectionPos(getText().length()); 
				if (getText().isEmpty()) { setText("" + map.downOffsetY); }
			}
		};
		resetYAxis.setActionReciever(this);
		yAxis.addObject(downOffsetInput, resetYAxis);
		
		//map properties
		
		mapProperties = new EGuiContainer(this, startX + 362, startY + 2, 196, 162) {
			{ setTitle("Map Properties"); }
			@Override
			public void drawObject(int mXIn, int mYIn) {
				super.drawObject(mXIn, mYIn);
				drawStringWithShadow("Draw Vertical", startX + 60, startY + 28, 0xb2b2b2);
				drawStringWithShadow("Draw With Chat Open", startX + 60, startY + 51, 0xb2b2b2);
				drawStringWithShadow("Draw map", startX + 60, startY + 74, 0xb2b2b2);
				drawStringWithShadow("Draw coords", startX + 60, startY + 97, 0xb2b2b2);
				drawStringWithShadow("Draw directions", startX + 60, startY + 120, 0xb2b2b2);
				drawStringWithShadow("Draw facing line", startX + 60, startY + 143, 0xb2b2b2);
			}
		};
		EDimension mpd = mapProperties.getDimensions();
		drawVertical = new EGuiButton(mapProperties, mpd.startX + 6, mpd.startY + 23, 45, 18).setTrueFalseButton(true, map.drawVertical);
		drawChat = new EGuiButton(mapProperties, mpd.startX + 6, mpd.startY + 46, 45, 18).setTrueFalseButton(true, map.drawWithChatOpen);
		drawMap = new EGuiButton(mapVisual, mpd.startX + 6, mpd.startY + 69, 45, 18).setTrueFalseButton(true, map.drawMap);
		drawCoords = new EGuiButton(mapVisual, mpd.startX + 6, mpd.startY + 92, 45, 18).setTrueFalseButton(true, map.drawCoords);
		drawDirections = new EGuiButton(mapVisual, mpd.startX + 6, mpd.startY + 115, 45, 18).setTrueFalseButton(true, map.drawMapDirections);
		drawFacingDir = new EGuiButton(mapVisual, mpd.startX + 6, mpd.startY + 138, 45, 18).setTrueFalseButton(true, map.drawFacingDir);
		drawVertical.setActionReciever(this);
		drawChat.setActionReciever(this);
		drawMap.setActionReciever(this);
		drawCoords.setActionReciever(this);
		drawFacingDir.setActionReciever(this);
		mapProperties.addObject(drawVertical, drawChat, drawMap, drawCoords, drawDirections, drawFacingDir);
		
		//map visual
		
		mapVisual = new EGuiContainer(this, startX + 198, startY + 2, 163, 180) {
			{ setTitle("Map Preview"); setBackgroundColor(0xffa8a8a8);}
			@Override
			public void drawObject(int mXIn, int mYIn) {
				super.drawObject(mXIn, mYIn);
				GlStateManager.disableAlpha();
				GlStateManager.disableBlend();
				GlStateManager.pushMatrix();
				GlStateManager.color(1.0f, 1.0f, 1.0f);
				mc.renderEngine.bindTexture(map.handler.getTextureLocation());
				int xPos = midX - map.previewSize / 2 - 1;
				int yPos = midY - map.previewSize / 2 - 9;
				if (map.drawVertical.get()) {
					GlStateManager.translate(midX, midY, 0);
					GlStateManager.rotate(180, 0, 0, 45);
					GlStateManager.translate(-midX, -midY, 0);
				} else { xPos += 1; yPos += 17; }
				drawTexture(xPos, yPos, map.previewSize, map.previewSize, map.previewXLow, map.previewYLow, map.previewXHigh, map.previewYHigh);
				
				GlStateManager.popMatrix();
				GlStateManager.enableAlpha();
				GlStateManager.enableBlend();
				
				if (!map.isMapImageCreated()) {
					drawCenteredStringWithShadow("Enable MiniMap to generate", midX, midY - 1, 0xFF4747);
					drawCenteredStringWithShadow("a map preview!", midX, midY + 9, 0xFF4747);
				}
			}
		};
		
		//color key
		
		colorKey = new EGuiContainer(this, startX + 2, startY + 165, 195, 47) {
			{ setTitle("Map Color Key"); }
			@Override
			public void drawObject(int mXIn, int mYIn) {
				super.drawObject(mXIn, mYIn);
			}
		};
		
		addObject(location, zooming, tracking, yAxis, mapProperties, mapVisual, colorKey, hotkeys);
	}
	
	@Override
	public void drawObject(int mXIn, int mYIn) {
		drawDefaultBackground();
		super.drawObject(mXIn, mYIn);
	}
	
	@Override
	public void actionPerformed(IEnhancedActionObject object, Object... args) {
		if (object == zoomSlider) {
			map.setMiniMapZoom(zoomSlider.getSliderValue());
			zoomEntry.setText("" + zoomSlider.displayValue);
		}
		if (object == clearTrack) {
			map.findPlayer = "";
			trackPlayerName.setText(EnumChatFormatting.GRAY + "enter a name..");
		}
		if (object == resetYAxis) {
			map.downOffsetY = 0;
			downOffsetInput.setText("" + map.downOffsetY);
		}
		if (object == drawPlayerLocations) {
			drawPlayerLocations.toggleTrueFalse(map.trackPlayers, map, false);
		}
		if (object == drawVertical) {
			drawVertical.toggleTrueFalse(map.drawVertical, map, false);
		}
		if (object == drawChat) {
			drawChat.toggleTrueFalse(map.drawWithChatOpen, map, false);
		}
		if (object == drawMap) {
			drawMap.toggleTrueFalse(map.drawMap, map, false);
		}
		if (object == drawCoords) {
			drawCoords.toggleTrueFalse(map.drawCoords, map, false);
		}
		if (object == drawDirections) {
			drawDirections.toggleTrueFalse(map.drawMapDirections, map, false);
		}
		if (object == drawFacingDir) {
			drawFacingDir.toggleTrueFalse(map.drawFacingDir, map, false);
		}
	}
}
