package com.Whodundid.playerInfo.window;

import com.Whodundid.core.EnhancedMC;
import com.Whodundid.core.app.AppType;
import com.Whodundid.core.app.RegisteredApps;
import com.Whodundid.core.settings.KeyBindWindow;
import com.Whodundid.core.util.EUtil;
import com.Whodundid.core.util.renderUtil.CenterType;
import com.Whodundid.core.util.renderUtil.EColors;
import com.Whodundid.core.util.renderUtil.ScreenLocation;
import com.Whodundid.core.util.storageUtil.EDimension;
import com.Whodundid.core.windowLibrary.windowObjects.actionObjects.WindowButton;
import com.Whodundid.core.windowLibrary.windowObjects.actionObjects.WindowTextField;
import com.Whodundid.core.windowLibrary.windowObjects.advancedObjects.scrollList.WindowScrollList;
import com.Whodundid.core.windowLibrary.windowObjects.basicObjects.WindowLabel;
import com.Whodundid.core.windowLibrary.windowObjects.basicObjects.WindowRect;
import com.Whodundid.core.windowLibrary.windowTypes.WindowParent;
import com.Whodundid.core.windowLibrary.windowTypes.interfaces.IActionObject;
import com.Whodundid.playerInfo.PlayerInfoApp;
import com.Whodundid.playerInfo.util.PIResources;
import java.io.File;
import net.minecraft.util.MathHelper;

public class PlayerInfoSettings extends WindowParent {
	
	PlayerInfoApp app = (PlayerInfoApp) RegisteredApps.getApp(AppType.PLAYERINFO);
	WindowScrollList list;
	WindowButton animateSkins, drawCapes, drawNames, randomBackgrounds;
	WindowButton searchBtn;
	WindowButton skinsDir, editKeyBindings;
	WindowTextField searchField;
	
	File skinDir = new File(RegisteredApps.getAppConfigBaseFileLocation(AppType.PLAYERINFO).getAbsolutePath() + "/Player Skins");
	
	public PlayerInfoSettings() {
		super();
		aliases.add("pisettings");
		windowIcon = PIResources.icon;
	}
	
	@Override
	public void initWindow() {
		setObjectName("Player Info Settings");
		defaultDims();
		setMinDims(defaultWidth, 100);
		setResizeable(true);
		setMaximizable(true);
	}
	
	@Override
	public void initObjects() {
		defaultHeader(this);
		
		list = new WindowScrollList(this, startX + 2, startY + 2, width - 4, height - 4);
		list.setBackgroundColor(0xff303030);
		
		int searchY = addSearch(0);
		int skinViewerY = addSkinViewer(searchY);
		int skinDirY = addSkinDir(skinViewerY);
		
		list.fitItemsInList(4, 10);
		
		addObject(list);
	}
	
	@Override
	public void drawObject(int mXIn, int mYIn) {
		drawDefaultBackground();
		super.drawObject(mXIn, mYIn);
	}
	
	@Override
	public void actionPerformed(IActionObject object, Object... args) {
		if (object == animateSkins) { animateSkins.toggleTrueFalse(PlayerInfoApp.animateSkins, app, false); }
		if (object == drawCapes) { drawCapes.toggleTrueFalse(PlayerInfoApp.drawCapes, app, false); }
		if (object == drawNames) { drawNames.toggleTrueFalse(PlayerInfoApp.drawNames, app, false); }
		if (object == randomBackgrounds) { randomBackgrounds.toggleTrueFalse(PlayerInfoApp.randomBackgrounds, app, false); }
		if (object == searchField || object == searchBtn) { openInfoWindow(searchField.getText()); }
		if (object == skinsDir) { EUtil.openFile(skinDir); }
		if (object == editKeyBindings) { EnhancedMC.displayWindow(new KeyBindWindow(app.nameChecker)); }
	}
	
	@Override
	public PlayerInfoSettings resize(int xIn, int yIn, ScreenLocation areaIn) {
		if (getMaximizedPosition() != ScreenLocation.center && (xIn != 0 || yIn != 0)) {
			int vPos = list.getVScrollBar().getScrollPos();
			int hPos = list.getHScrollBar().getScrollPos();
			
			super.resize(xIn, yIn, areaIn);
			
			list.getVScrollBar().onResizeUpdate(vPos, xIn, yIn, areaIn);
			list.getHScrollBar().onResizeUpdate(hPos, xIn, yIn, areaIn);
			
			setPreMax(getDimensions());
		}
		return this;
	}
	
	private int addSearch(int yPos) {
		EDimension ld = list.getListDimensions();
		
		WindowLabel searchLabel = new WindowLabel(list, ld.midX, yPos + 4, "Search Player", EColors.orange.intVal).setDrawCentered(true);
		WindowRect labelBack = new WindowRect(list, 0, yPos, list.getListDimensions().endX, yPos + 16, EColors.black);
		WindowRect divider1 = new WindowRect(list, 0, yPos + 1, list.getDimensions().endX, yPos + 15, EColors.dsteel);
		
		searchField = new WindowTextField(list, ld.midX - 100, searchLabel.endY + 10, 130, 18) {
			@Override
			public void keyPressed(char typedChar, int keyCode) {
				super.keyPressed(typedChar, keyCode);
				
				if (searchBtn != null && app.isEnabled()) { searchBtn.setEnabled(getText().length() >= 3); }
			}
		};
		if (app.isEnabled()) {
			searchField.setTextWhenEmptyColor(EColors.gray.intVal);
			searchField.setTextWhenEmpty("player name");
		}
		else {
			searchField.setTextWhenEmptyColor(EColors.lred.intVal);
			searchField.setTextWhenEmpty("Player Info disabled!");
		}
		searchField.setClickable(app.isEnabled());
		
		searchBtn = new WindowButton(list, searchField.endX + 10, searchField.startY - 1, 60, 20, "Search");
		searchBtn.setEnabled(false);
		
		IActionObject.setActionReceiver(this, searchField, searchBtn);
		
		list.addAndIgnore(labelBack, divider1);
		list.addObjectToList(searchLabel, searchField, searchBtn);
		
		return (searchField.endY + 7) - list.getDimensions().startY;
	}
	
	private int addSkinViewer(int yPos) {
		EDimension ld = list.getListDimensions();
		
		WindowLabel skinViewerLabel = new WindowLabel(list, ld.midX, yPos + 4, "Skin Viewer", EColors.orange).setDrawCentered(true);
		WindowRect labelBack = new WindowRect(list, 0, yPos, list.getListDimensions().endX, yPos + 16, EColors.black);
		WindowRect divider1 = new WindowRect(list, 0, yPos + 1, list.getDimensions().endX, yPos + 15, EColors.dsteel);
		
		animateSkins = new WindowButton(list, ld.midX - 100, skinViewerLabel.endY + 10, 60, 20, app.animateSkins);
		WindowLabel animateSkinsLabel = new WindowLabel(list, animateSkins.endX + 10, animateSkins.startY + 6, "Animate Player Skins");
		
		drawCapes = new WindowButton(list, ld.midX - 100, animateSkins.endY + 6, 60, 20, app.drawCapes);
		WindowLabel drawCapesLabel = new WindowLabel(list, drawCapes.endX + 10, drawCapes.startY + 6, "Draw Player Capes");
		
		drawNames = new WindowButton(list, ld.midX - 100, drawCapes.endY + 6, 60, 20, app.drawNames);
		WindowLabel drawNamesLabel = new WindowLabel(list, drawNames.endX + 10, drawNames.startY + 6, "Draw Player Names");
		
		randomBackgrounds = new WindowButton(list, ld.midX - 100, drawNames.endY + 6, 60, 20, app.randomBackgrounds);
		WindowLabel randomBackgroundsLabel = new WindowLabel(list, randomBackgrounds.endX + 10, randomBackgrounds.startY + 6, "Draw Random Backgrounds");
		
		IActionObject.setActionReceiver(this, animateSkins, drawCapes, drawNames, randomBackgrounds);
		WindowLabel.setColor(EColors.lgray, animateSkinsLabel, drawCapesLabel, drawNamesLabel, randomBackgroundsLabel);
		
		animateSkinsLabel.setHoverText("Players will perform a walking animation");
		drawCapesLabel.setHoverText("Players who have a cape will have their cape drawn");
		drawNamesLabel.setHoverText("Players will have their name drawn above them");
		randomBackgroundsLabel.setHoverText("Will draw a random Minecraft themed background behind the player being viewed");
		
		list.addAndIgnore(labelBack, divider1, skinViewerLabel);
		list.addObjectToList(animateSkins, animateSkinsLabel);
		list.addObjectToList(drawCapes, drawCapesLabel);
		list.addObjectToList(drawNames, drawNamesLabel);
		list.addObjectToList(randomBackgrounds, randomBackgroundsLabel);
		
		return (randomBackgrounds.endY + 7) - list.getDimensions().startY;
	}
	
	private int addSkinDir(int yPos) {
		EDimension ld = list.getListDimensions();
		int w = MathHelper.clamp_int(ld.width - (ld.width / 3), 100, 200);
		
		WindowRect divider = new WindowRect(list, 0, yPos, list.getDimensions().endX, yPos + 1, EColors.black);
		WindowRect back = new WindowRect(list, 0, yPos + 1, list.getDimensions().endX, list.getDimensions().endY, EColors.steel);
		
		skinsDir = new WindowButton(list, ld.midX - (w / 2), divider.endY + 14, w, 20, "Open Skin Folder");
		editKeyBindings = new WindowButton(list, ld.midX - (w / 2), skinsDir.endY + 4, w, 20, "Edit Key Bindings");
		
		skinsDir.setStringColor(EColors.seafoam);
		editKeyBindings.setStringColor(EColors.yellow);
		
		skinsDir.setEnabled(skinDir.exists());
		
		skinsDir.setHoverText(skinDir.exists() ? "Opens the folder where player skins are downloaded to." : "The player skins folder has not yet been made. Download a skin first!");
		editKeyBindings.setHoverText("Opens the Player Info Keybindings within the Keybinds window.");
		
		IActionObject.setActionReceiver(this, skinsDir, editKeyBindings);
		
		setClickable(false, divider, back);
		
		list.addAndIgnore(divider, back);
		list.addObjectToList(skinsDir, editKeyBindings);
		
		return (editKeyBindings.endY + 13) - list.getDimensions().startY;
	}
	
	private void openInfoWindow(String name) {
		EnhancedMC.displayWindow(new PlayerInfoWindow(name), this, CenterType.object);
	}

}