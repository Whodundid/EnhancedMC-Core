package com.Whodundid.worldEditor;

import com.Whodundid.core.app.AppType;
import com.Whodundid.core.app.EMCApp;
import com.Whodundid.core.util.playerUtil.PlayerTraits;
import com.Whodundid.core.util.renderUtil.CursorHelper;
import com.Whodundid.core.util.storageUtil.DynamicTextureHandler;
import com.Whodundid.core.util.storageUtil.StorageBox;
import com.Whodundid.core.util.storageUtil.Vector3D;
import com.Whodundid.core.util.storageUtil.Vector3DInt;
import com.Whodundid.worldEditor.Editor2D.Editor2D;
import com.Whodundid.worldEditor.Editor3D.Editor3D;
import com.Whodundid.worldEditor.Editor3D.block.EditorBlock;
import com.Whodundid.worldEditor.Editor3D.chunkUtil.EditorChunkCache;
import com.Whodundid.worldEditor.EditorScripts.EditorScript_Paste;
import com.Whodundid.worldEditor.EditorUtil.EditorGuiBase;
import com.Whodundid.worldEditor.EditorUtil.EditorTools;
import java.awt.image.BufferedImage;
import net.minecraft.util.Vec3;
import net.minecraft.util.Vec3i;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

//Last edited: 11-25-18
//First Added: 4-18-18
//Author: Hunter Bragg

@Mod(modid = EditorApp.MODID, version = EditorApp.VERSION, name = EditorApp.NAME, dependencies = "required-after:enhancedmc")
public final class EditorApp extends EMCApp {
	
	public static final String MODID = "worldeditor";
	public static final String VERSION = "0.1";
	public static final String NAME = "World Editor";
	
	private EditorGuiBase guiInstance;
	public boolean editorOpen = false;
	public EditorBlock selectedBlock = null;
	public Vector3DInt copyStartPosition;
	public Vector3D playerCopyPosition;
	public Vector3DInt pos1 = new Vector3DInt(), pos2 = new Vector3DInt();
	public EditorTools selectedTool = EditorTools.SELECT;
	public boolean insideEditor = false;
	public boolean render3D = false;
	public boolean renderVertical = false;
	public boolean renderVertNS = true;
	public boolean renderBiomeMap = false;
	public double zoom = 0.5;
	public StorageBox<Integer, Integer> selectionPos1, selectionPos2;
	public Vector3D center = new Vector3D();
	public boolean firstCenterSet = false;
	public double pitch = 0, yaw = 0;
	public double movementSpeed = 0.5, fieldOfView = 525;
	public boolean moveWithWorld = true;
	public boolean drawOutlines = true;
	public boolean render3DHighRes = true;
	public Vector3D camPos = new Vector3D(), camLook = new Vector3D(0, 0, 0), light = new Vector3D(0, 0, -200);
	public EditorChunkCache chunkCache;
	public int drawWidth = 453;
	public int drawHeight = 453;
	public int imgWidth = 453;
	public int imgHeight = 453;
	public DynamicTextureHandler imageHandler2D, imageHandler3D, imageHandler3DHiRes, imageHandlerBorder;
	public Editor2D editor2D;
	public Editor3D editor3D;
	
	public EditorApp() {
		super(AppType.WORLDEDITOR);
		shouldLoad = false;
		addDependency(AppType.CORE, "1.0");
		addDependency(AppType.SCRIPTS, "0.1");
		setMainGui(new EditorGuiBase());
		setAliases("worldeditor", "editor");
		version = VERSION;
		author = "Whodundid";
	}
	
	@Override
	public void onPostInit(FMLPostInitializationEvent e) {
		imageHandler2D = new DynamicTextureHandler(mc.getTextureManager(), new BufferedImage(301, 301, BufferedImage.TYPE_INT_RGB));
		imageHandler3D = new DynamicTextureHandler(mc.getTextureManager(), new BufferedImage(453, 453, BufferedImage.TYPE_INT_RGB));
		imageHandler3DHiRes = new DynamicTextureHandler(mc.getTextureManager(), new BufferedImage(906, 906, BufferedImage.TYPE_INT_RGB));
		imageHandlerBorder = new DynamicTextureHandler(mc.getTextureManager(), new BufferedImage(153, 153, BufferedImage.TYPE_INT_RGB));
		editor2D = new Editor2D();
		editor3D = new Editor3D();
	}
	
	@Override
	public Object sendArgs(Object... args) {
		if (isEnabled()) {
			if (args.length == 1) {
				if (args[0] instanceof String) {
					String arg = (String) args[0];
					
					switch (arg) {
					case "WorldEditor: open": return isEditorOpen();
					case "WorldEditor: center": return getCenter();
					case "WorldEditor: 3D": return getRender3D();
					}
				}
			}
		}
		return null;
	}
	
	@Override
	public void clientTickEvent(TickEvent.ClientTickEvent e) {
		//editorOpen = mc.currentScreen instanceof EditorGuiBase;
	}
	
	@Override
	public void worldUnloadEvent(WorldEvent.Unload e) {
		editorOpen = false;
		guiInstance = null;
		resetFirstCenterSet();
	}
	
	public synchronized void openEditor() {
		//mc.displayGuiScreen(guiInstance = new EditorGuiBase());
		editorOpen = true;
	}
	
	public synchronized void closeEditor() {
		editorOpen = false;
		guiInstance = null;
		CursorHelper.setVisible();
	}
	
	public void setPosition1(Vector3DInt positionIn) {
		pos1 = positionIn;
		if (pos1 != null) {
			mc.thePlayer.sendChatMessage("//pos1 " + pos1.x + "," + pos1.y + "," + pos1.z);
		}
	}
	
	public void setPosition2(Vector3DInt positionIn) {
		pos2 = positionIn;
		if (pos2 != null) {
			mc.thePlayer.sendChatMessage("//pos2 " + pos2.x + "," + pos2.y + "," + pos2.z);
		}
	}
	
	public void copyRegion() {
		mc.thePlayer.sendChatMessage("//copy");
		setCopyPosition(new Vector3DInt(pos1));
		setPlayerCopyPosition(new Vector3D(PlayerTraits.getPlayerLocation()));
	}
	
	public void pasteRegion() {
		try {
			new EditorScript_Paste(this).startScript(null);
		} catch (Exception e) { e.printStackTrace(); }
	}
	
	//Editor setters
	public void setCopyPosition(Vector3DInt vecIn) { copyStartPosition = vecIn; }
	public void setPlayerCopyPosition(Vector3D vecIn) { playerCopyPosition = vecIn; }
	public void setSelectedTool(EditorTools toolIn) { selectedTool = toolIn; }
	public void setRender3D(boolean val) { render3D = val; }
	public void setRenderVertical(boolean val) { renderVertical = val; }
	public void setRenderVertNS(boolean val) { renderVertNS = val; }
	public void setRenderBiomeMap(boolean val) { renderBiomeMap = val; }
	public void setZoomScale(double zoomIn) { zoom = zoomIn; }
	public void setSelectionPos1(StorageBox<Integer, Integer> posIn) { selectionPos1 = posIn; }
	public void setSelectionPos2(StorageBox<Integer, Integer> posIn) { selectionPos2 = posIn; }
	public void setCenter(int x, int y, int z) { setCenter(new Vector3D(x, y, z)); }
	public void setCenter(double x, double y, double z) { setCenter(new Vector3D(x, y, z)); }
	public void setCenter(Vec3 centerIn) { setCenter(new Vector3D(centerIn)); }
	public void setCenter(Vec3i centerIn) { setCenter(new Vector3D(centerIn)); }
	public void setCenter(Vector3D centerIn) { center = centerIn; }
	public void setPitch(double pitchIn) { pitch = pitchIn; }
	public void setYaw(double yawIn) { yaw = yawIn; }
	public void setMovementSpeed(double speedIn) { movementSpeed = speedIn; }
	public void setfieldOfView(double fieldOfViewIn) { fieldOfView = fieldOfViewIn; }
	public void setMoveWithWorld(boolean val) { moveWithWorld = val; }
	public void setDraw3DOutlines(boolean val) { drawOutlines = val; }
	public void setRenderHiRes(boolean val) { render3DHighRes = val; }
	public void setEditorGuiReference(EditorGuiBase guiIn) { editor2D.setGui(guiIn); editor3D.setGui(guiIn); }
		
	//Editor getters
	public Vector3D getCamPos() { return new Vector3D(camPos.x, camPos.z, camPos.y); }
	public Vector3DInt getCopyPosition() { return copyStartPosition; }
	public Vector3D getPlayerCopyPosition() { return playerCopyPosition; }
	public Vector3DInt getPos1() { return pos1; }
	public Vector3DInt getPos2() { return pos2; }
	public EditorTools getSelectedTool() { return selectedTool; }
	public boolean getRender3D() { return render3D; }
	public boolean getRenderVertical() { return renderVertical; }
	public boolean getRenderVertNS() { return renderVertNS; }
	public boolean getRenderBiomeMap() { return renderBiomeMap; }
	public double getZoomScale() { return zoom; }
	public StorageBox<Integer, Integer> getSelectinPos1() { return selectionPos1; }
	public StorageBox<Integer, Integer> getSelectinPos2() { return selectionPos2; }
	public Vector3D getCenter() { return center; }
	public void resetFirstCenterSet() { firstCenterSet = false; }
	public double getPitch() { return pitch; }
	public double getYaw() { return yaw; }
	public double getMovementSpeed() { return movementSpeed; }
	public double getFieldOfView() { return fieldOfView; }
	public boolean getMoveWithWorld() { return moveWithWorld; }
	public boolean getDrawOutlines() { return drawOutlines; }
	public boolean getRenderHiRes() { return render3DHighRes; }
	public EditorChunkCache getChunkCache() { return chunkCache; }
	public Vector3D getCamLook() { return camLook; }
	public Vector3D getLight() { return light; }
	public boolean isMouseInsideEditor() { return insideEditor; }
	public boolean isEditorOpen() { return editorOpen; }
	public EditorGuiBase getGuiInstance() {
		//if (mc.currentScreen instanceof EditorGuiBase) { return (EditorGuiBase) mc.currentScreen; }
		return null;
	}
	public Editor2D get2D() { return editor2D; }
	public Editor3D get3D() { return editor3D; }
	
	public synchronized void reset() {
		editor2D = new Editor2D();
		editor3D = new Editor3D();
		if (editorOpen && guiInstance != null) { setEditorGuiReference(guiInstance); }
	}
}
