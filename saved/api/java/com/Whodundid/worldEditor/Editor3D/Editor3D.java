package com.Whodundid.worldEditor.Editor3D;

import java.awt.Color;
import java.awt.Graphics2D;
import java.text.DecimalFormat;
import java.util.ArrayList;
import net.minecraft.client.Minecraft;
import com.Whodundid.core.debug.DebugFunctions;
import com.Whodundid.core.debug.IDebugCommand;
import com.Whodundid.core.util.playerUtil.Direction;
import com.Whodundid.core.util.renderUtil.GLObject;
import com.Whodundid.core.util.storageUtil.StorageBox;
import com.Whodundid.core.util.storageUtil.Vector3D;
import com.Whodundid.core.util.storageUtil.Vector3DInt;
import com.Whodundid.worldEditor.EditorApp;
import com.Whodundid.worldEditor.Editor3D.chunkUtil.EditorChunkCache;
import com.Whodundid.worldEditor.Editor3D.renderingUtil.Calculator;
import com.Whodundid.worldEditor.Editor3D.renderingUtil.DPolygon;
import com.Whodundid.worldEditor.EditorUtil.EditorGuiBase;

//Last edited: 11-25-18
//First Added: 9-13-18
//Author: Hunter Bragg

public class Editor3D {
	
	protected Minecraft mc = Minecraft.getMinecraft();
	public EditorGuiBase gui;
	private Graphics2D graphics, hiResGraphics;
	public ArrayList<DPolygon> polyList;
	private long startTime = System.currentTimeMillis();
	private boolean wasBlockSelected = false;
	public boolean[] keys = new boolean[10];
	private Vector3DInt lastCamPos;
	private int[] newOrder;
	public double maxFOV = 100, minFOV = 2500;
	public double yawSpeed = 3200, pitchSpeed = 5700;
	protected boolean redrawRequested = false;
	protected boolean anyMovementKeyIsPressed = false;
	protected StorageBox<Integer, Integer> mouseGrabPos = new StorageBox();
	EditorApp editor;
	
	public void setGui(EditorGuiBase guiIn) { gui = guiIn; editor = gui.editor; }
	
	public void init() {
		graphics = editor.imageHandler3D.GBI().createGraphics();
		hiResGraphics = editor.imageHandler3DHiRes.GBI().createGraphics();
		polyList = new ArrayList();
		lastCamPos = new Vector3DInt();
		if (editor.render3D) { open(); }
	}
	
	public void onGuiClosed() {
		if (graphics != null) { graphics.dispose(); }
		if (hiResGraphics != null) { hiResGraphics.dispose(); }
	}
	
	public void draw() {
		gui.drawRect(0, 0, 200, 55, 0xff222222);
		GLObject.drawString("FOV: " + editor.fieldOfView, 3, 3, 0x00FF00);
		String degree = new DecimalFormat("0.000").format(getFacingDegree());
		String pitch = new DecimalFormat("0.000").format(editor.pitch);
		GLObject.drawString("Facing pitch: " + pitch, 3, 13, 0x00FF00);
		GLObject.drawString("Facing yaw: " + degree, 3, 23, 0x00FF00);
		GLObject.drawString("Facing direction: " + getFacingDir(), 3, 33, 0x00FF00);
		String xPos = new DecimalFormat("0.000").format(editor.getCamPos().x);
		String yPos = new DecimalFormat("0.000").format(editor.getCamPos().y);
		String zPos = new DecimalFormat("0.000").format(editor.getCamPos().z);
		GLObject.drawString("x:" + xPos + " y:" + yPos + " z:" + zPos, 3, 43, 0x00FF00);
		
		if (editor.chunkCache != null && hiResGraphics != null && graphics != null) {
			try {
				update();
				if (editor.getRenderHiRes()) {
					hiResGraphics.setColor(new Color(140, 180, 200));
					hiResGraphics.fillRect(0, 0, 906, 906);
					
					Calculator.SetPrederterminedInfo(editor);
					
					for (DPolygon d : editor.chunkCache.getWorldVertexData()) { d.updatePolygon(editor); }
					
					int q = 0;
					if (q == 0) {
						setVertexDataRenderOrder();
						for (int i = 0; i < newOrder.length; i++) {
							editor.chunkCache.getWorldVertexData().get(newOrder[i]).dPoly.drawPolygon(hiResGraphics);
						}
					} else {
						for (int i = 0; i < editor.chunkCache.getWorldVertexData().size(); i++) {
							editor.chunkCache.getWorldVertexData().get(i).dPoly.drawPolygon(hiResGraphics);
						}
					}
					editor.imageHandler3DHiRes.updateTextureData(editor.imageHandler3DHiRes.GBI());
				} else {
					graphics.setColor(new Color(140, 180, 200));
					graphics.fillRect(0, 0, 906, 906);

					Calculator.SetPrederterminedInfo(editor);
					
					for (DPolygon d : editor.chunkCache.getWorldVertexData()) { d.updatePolygon(editor); }
					
					setVertexDataRenderOrder();
					for (int i = 0; i < newOrder.length; i++) {
						editor.chunkCache.getWorldVertexData().get(newOrder[i]).dPoly.drawPolygon(graphics);
					}
					editor.imageHandler3D.updateTextureData(editor.imageHandler3D.GBI());
				}
			} catch (Exception e) { e.printStackTrace(); }
		}
		if (wasBlockSelected && editor.selectedBlock == null) {
			wasBlockSelected = false;
			requestRedraw();
		}
	}
	
	public void parseMousePosition(int mX, int mY) {
		switch (editor.getSelectedTool()) {
		case SELECT:
			if (!gui.isThereRCM()) {
				if (editor.insideEditor) {
					setPolygonOver(mX - gui.startX, mY - gui.startY); 
				}
			}
			break;
		case PAN:
		default: break;
		}
	}
	
	public void handleMousePress(int mX, int mY, int button) {
		if (editor.insideEditor) {
			if (button == 0) {
				switch (editor.getSelectedTool()) {
				case SELECT:
					if (getSelectedBlockPosition() != null) {
						Vector3DInt pos = getSelectedBlockPosition();
						editor.setPosition1(new Vector3DInt(pos.x, pos.z, pos.y));
					}
					break;
				case PAN:
					mouseGrabPos.setValues(mX, mY);
				default: break;
				}
				if (gui.isThereRCM()) { gui.removeRCM(); }
			} else if (button == 1) {
				if (!gui.isThereRCM()) { gui.addRCM(mX, mY); }
				else { gui.removeRCM(); }
			}
		} else {
			if (gui.isThereRCM()) { gui.removeRCM(); }
		}
	}
	
	public void handleMouseRelease(int mX, int mY, int button) {
		if (editor.insideEditor) {
			if (button == 0) {
				switch (editor.getSelectedTool()) {
				case SELECT:
					if (getSelectedBlockPosition() != null) {
						Vector3DInt pos = getSelectedBlockPosition();
						editor.setPosition2(new Vector3DInt(pos.x, pos.z, pos.y));
					}
					break;
				default: break;
				}
			}
		}
	}
	
	public void handleMouseDrag(int mX, int mY, int button, long timeSinceLastClick) {
		if (editor.insideEditor) {
			if (button == 0) {
				switch (editor.getSelectedTool()) {
				case PAN:
					if (mouseGrabPos != null && mouseGrabPos.getObject() != null && mouseGrabPos.getValue() != null) {
						int difX = mX - mouseGrabPos.getObject();
						int difY = mY - mouseGrabPos.getValue();
						
						if (gui.isShiftKeyDown()) {
							editor.yaw += difX / yawSpeed * 22.30;
						} else if (gui.isCtrlKeyDown()) {
							editor.pitch -= difY / pitchSpeed * 30;
						} else {
							editor.pitch -= difY / pitchSpeed * 30;
							editor.yaw += difX / yawSpeed * 22.30;
						}
						
						if (editor.pitch > 0.999) { editor.pitch = 0.999; }
						if (editor.pitch < -0.999) { editor.pitch = -0.999; }
						
						updateView();
						mouseGrabPos.setValues(mX, mY);
					}
					break;
				default: break;
				}
			}
		}
	}
	
	public void handleMouseScroll(int change) {
		if (change > 0) {
			if (editor.fieldOfView < minFOV) { editor.fieldOfView += 25 * change; } 
			else { editor.fieldOfView = minFOV; }
			//requestRedraw();
		} else if (change < 0) {
			if (editor.fieldOfView > maxFOV) { editor.fieldOfView += 25 * change; } 
			else { editor.fieldOfView = maxFOV; }
			//requestRedraw();
		}
	}
	
	public void handleKeyPress(char typedChar, int keyCode) {
		switch (keyCode) {
		//case 1: editor.closeEditor(); break;
		
		case 17: keys[0] = true; break;
		case 30: keys[1] = true; break;
		case 31: keys[2] = true; break;
		case 32: keys[3] = true; break;
		case 57: keys[4] = true; break;
		case 42: keys[5] = true; break;
		case 200: keys[6] = true; break;
		case 203: keys[7] = true; break;
		case 208: keys[8] = true; break;
		case 205: keys[9] = true; break;
		
		case 39: open(); break;
		
		case 24: editor.setDraw3DOutlines(!editor.drawOutlines); break; //requestRedraw();
		case 33: editor.setMoveWithWorld(!editor.moveWithWorld); break;
		case 37:
			moveTo(new Vector3DInt(mc.thePlayer.getPosition().getX(), mc.thePlayer.getPosition().getY() + 2, mc.thePlayer.getPosition().getZ()));
			open();
			break;
		case 41: DebugFunctions.runDebugFunction(IDebugCommand.DEBUG_3); break;
		default: break;
		}
		
		//guiInstance.editorToolList.keyPressed(typedChar, keyCode);
	}
	
	public void handleKeyRelease(char typedChar, int keyCode) {
		switch (keyCode) {
		case 17: keys[0] = false; break;
		case 30: keys[1] = false; break;
		case 31: keys[2] = false; break;
		case 32: keys[3] = false; break;
		case 57: keys[4] = false; break;
		case 42: keys[5] = false; break;
		case 200: keys[6] = false; break;
		case 203: keys[7] = false; break;
		case 208: keys[8] = false; break;
		case 205: keys[9] = false; break;
		default: break;
		}
	}
	
	public void reset() {
		if (editor.chunkCache != null) { editor.chunkCache.destroyChunks(); }
		editor.selectedBlock = null;
		moveTo(0, 0, 0);
	}
	
	public void open() {
		editor.chunkCache = new EditorChunkCache(editor);
		editor.chunkCache.AssembleCache();
		moveTo(editor.getCamPos());
		requestRedraw();
	}
	
	public void update() {
		if (System.currentTimeMillis() - startTime >= 50) {
			checkMovementKeys();
			cameraMovement();
			startTime = System.currentTimeMillis();
		}
	}
	
	public void setPolygonOver(int mX, int mY) {
		if (editor.chunkCache != null) {
			try {
				editor.selectedBlock = null;
				if (newOrder != null) {
					for (int i = newOrder.length - 1; i >= 0; i--) {
						DPolygon p = editor.chunkCache.getWorldVertexData().get(newOrder[i]);
						if (p.dPoly.visible && p.draw && p.dPoly.MouseOver(mX, mY)) {
							//System.out.println(polyList.get(i).AvgDist);
							editor.selectedBlock = editor.chunkCache.getWorldVertexData().get(newOrder[i]).parentBlock;
							wasBlockSelected = true;
							requestRedraw();
							break;
						}
					}
				}
			} catch (Exception e) { e.printStackTrace(); }
		}
	}
	
	private void setVertexDataRenderOrder() {
		int vertexes = editor.chunkCache.getWorldVertexData().size();
		double[] polys = new double[vertexes];
		newOrder = new int[vertexes];

		for (int i = 0; i < vertexes; i++) {
			polys[i] = editor.chunkCache.getWorldVertexData().get(i).AvgDist;
			newOrder[i] = i;
		}
		
	    double temp;
	    int tempr;
		for (int a = 0; a < polys.length - 1; a++) {
			for (int b = 0; b < polys.length - 1; b++) {
				if (polys[b] < polys[b + 1]) {
					temp = polys[b];
					tempr = newOrder[b];
					newOrder[b] = newOrder[b + 1];
					polys[b] = polys[b + 1];
					   
					newOrder[b + 1] = tempr;
					polys[b + 1] = temp;
				}
				
			}
		}
	}
	
	public void checkMovementKeys() {
		anyMovementKeyIsPressed = false;
		for (int i = 0; i < keys.length; i++) {
			if (keys[i]) { 
				anyMovementKeyIsPressed = true;
				requestRedraw();
				break;
			}
		}
	}
	
	private void cameraMovement() {
		if (anyMovementKeyIsPressed) {
			editor.selectedBlock = null;
			Vector3D viewVector = Vector3D.difference(editor.camLook, editor.camPos);
			Vector3D verticalVector = new Vector3D(0, 0, 1);
			Vector3D sideViewVector = viewVector.crossProduct(verticalVector);
			double xMove = 0, yMove = 0, zMove = 0;
			double moveSpeed = editor.getMovementSpeed();
			
			if (keys[4]) { zMove = editor.camLook.z; }
			if (keys[5]) { zMove = -editor.camLook.z; }
			
			if (keys[0]) {
				xMove += viewVector.x;
				yMove += viewVector.y;
			}

			if (keys[1]) {
				xMove += sideViewVector.x;
				yMove += sideViewVector.y;
			}
			
			if (keys[2]) {
				xMove -= viewVector.getX();
				yMove -= viewVector.getY();
			}

			if (keys[3]) {
				xMove -= sideViewVector.getX();
				yMove -= sideViewVector.getY();
			}
			
			if (keys[6]) {
				editor.pitch += 150 / pitchSpeed;
				if (editor.pitch > 0.99999) { editor.pitch = 0.99999; }
			}
			if (keys[8]) {
				editor.pitch -= 150 / pitchSpeed;
				if (editor.pitch < -0.99999) { editor.pitch = -0.99999; }
			}
			if (keys[7]) { editor.yaw -= 150 / yawSpeed; }
			if (keys[9]) { editor.yaw += 150 / yawSpeed; }
			
			Vector3D moveVector = new Vector3D(xMove, yMove, zMove).normalize();
			Vector3D camPos = editor.camPos;
			moveTo(camPos.x + moveVector.x * moveSpeed, camPos.y + moveVector.y * moveSpeed, camPos.z + moveVector.z * moveSpeed);
		}
	}
	
	public void moveTo(Vector3D vecIn) { moveTo(vecIn.x, vecIn.z, vecIn.y); }
	public void moveTo(Vector3DInt vecIn) { moveTo(vecIn.x, vecIn.z, vecIn.y); }

	public void moveTo(double x, double y, double z) {
		editor.camPos.x = x;
		editor.camPos.y = y;
		editor.camPos.z = z > 0 ? z : 0.5;
		if (lastCamPos.x != (int) (Math.floor(x)) || lastCamPos.y != (int) (Math.floor(y)) || lastCamPos.z != (int) (Math.floor(z))) {
			lastCamPos.x = (int) Math.floor(x);
			lastCamPos.y = (int) Math.floor(y);
			lastCamPos.z = (int) Math.floor(z);
			editor.setCenter(new Vector3D(editor.camPos.x, editor.camPos.z, editor.camPos.y));
			//if (editor.getGui().editorTextInput != null ) { editor.getGui().editorTextInput.resetAllBoxesText(); }
			if (editor.moveWithWorld) { }
		}
		updateView();
	}
	
	public void updateView() {
		double r = Math.sqrt(1 - (editor.pitch * editor.pitch));
		editor.camLook.x = editor.camPos.x + r * Math.cos(editor.yaw);
		editor.camLook.y = editor.camPos.y + r * Math.sin(editor.yaw);
		editor.camLook.z = editor.camPos.z + editor.pitch;
		requestRedraw();
	}
	
	public Vector3DInt getSelectedBlockPosition() {
		Vector3DInt pos = null;
		if (editor.selectedBlock != null) { pos = editor.selectedBlock.position; }
		if (pos != null) { return new Vector3DInt(pos.x, pos.z, pos.y); }
		return null;
	}
	
	public double getFacingDegree() { return Math.abs((editor.yaw * (180 / Math.PI) % 360)); }
	
	public Direction getFacingDir() {
		double dir = getFacingDegree();
		if (0 <= dir && dir < 22.5) { return Direction.W; } 
        else if (22.5 <= dir && dir < 67.5) { return Direction.SW; }
        else if (67.5 <= dir && dir < 112.5) { return Direction.S; }
        else if (112.5 <= dir && dir < 157.5) { return Direction.SE; }
        else if (157.5 <= dir && dir < 202.5) { return Direction.E; }
        else if (202.5 <= dir && dir < 247.5) { return Direction.NE; }
        else if (247.5 <= dir && dir < 292.5) { return Direction.N; }
        else if (292.5 <= dir && dir < 337.5) { return Direction.NW; }
        else if (337.5 <= dir && dir < 360.0) { return Direction.W; }
        else { return Direction.OUT; }
	}
	
	public void requestRedraw() { redrawRequested = true; }
}
