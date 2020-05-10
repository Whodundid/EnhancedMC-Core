package com.Whodundid.worldEditor.EditorGuiObjects;

import java.awt.Color;
import java.text.DecimalFormat;
import com.Whodundid.core.app.AppType;
import com.Whodundid.core.app.RegisteredApps;
import com.Whodundid.core.enhancedGui.guiObjects.actionObjects.EGuiButton;
import com.Whodundid.core.enhancedGui.guiObjects.actionObjects.EGuiTextField;
import com.Whodundid.core.enhancedGui.types.interfaces.IEnhancedActionObject;
import com.Whodundid.core.util.mathUtil.NumberUtil;
import com.Whodundid.core.util.storageUtil.Vector3D;
import com.Whodundid.core.util.storageUtil.Vector3DInt;
import com.Whodundid.core.util.worldUtil.WorldHelper;
import com.Whodundid.worldEditor.EditorApp;
import com.Whodundid.worldEditor.EditorUtil.EditorGuiBase;
import com.Whodundid.worldEditor.EditorUtil.EditorSet;
import com.Whodundid.worldEditor.EditorUtil.EditorTools;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.BlockPos;

//Last edited: Nov 25, 2018
//First Added: unknown
//Author: Hunter Bragg

public class EditorGuiTextInput extends EditorGuiObject {
	
	public EGuiTextField xCoord, yCoord, zCoord, idInput;
	public EGuiButton tpTo, setButton;
	
	public EditorGuiTextInput(EditorGuiBase guiIn) {
		super(guiIn);
		init(guiIn, guiIn.midX - 474, guiIn.midY - 117, 240, 140);
	}
	
	@Override
	public void initObjects() {
		xCoord = new EGuiTextField(this, guiInstance.midX - 442, guiInstance.midY - 112, 40, 20);
		yCoord = new EGuiTextField(this, guiInstance.midX - 397, guiInstance.midY - 112, 40, 20);
		zCoord = new EGuiTextField(this, guiInstance.midX - 352, guiInstance.midY - 112, 40, 20);
		idInput = new EGuiTextField(this, guiInstance.midX - 420, guiInstance.midY - 10, 60, 20);
		
		tpTo = new EGuiButton(this, guiInstance.midX - 308, guiInstance.midY - 112, 70, 20, "Tp to");
		setButton = new EGuiButton(this, guiInstance.midX - 339, guiInstance.midY - 10, 90, 20, "Set Region");
		
		resetAllBoxesText();
		
		addObject(xCoord, yCoord, zCoord, idInput, tpTo, setButton);
	}
	
	@Override
	public void drawObject(int mX, int mY) {
		//mc.renderEngine.bindTexture(EMCResources.guiRCMBase);
		//guiInstance.drawModalRectWithCustomSizedTexture(startX, startY, 0, 0, width, height, width, height);
		drawText(mX, mY);
		//System.out.println(mX + " " + mY);
		//guiInstance.drawString(mX + " " + mY, 50, 380, 0xffffff);
		//guiInstance.drawString("" + CursorHelper.getExactMouseLocationMC(), 50, 400, 0xffffff);
		super.drawObject(mX, mY);
	}
	
	private void drawText(int mX, int mY) {
		//String mousePos = "mX: " + editor.mouseInput.mX + ", mY: " + editor.mouseInput.mY;
		String worldPos = "", blockStats = "", biome = "";
		Vector3DInt p = new Vector3DInt();
		
		if (editor.insideEditor) {
			if (!editor.getRender3D()) {
				if (editor.getSelectedTool().equals(EditorTools.SELECT)) {
					if (!editor.render3D) {
						p = editor.get2D().getWorldCoordsAtMouseLocation(mX, mY);
					}
					IBlockState state = WorldHelper.getBlockState(p);
					String x = new DecimalFormat("0.00").format(p.getX());
					String y = new DecimalFormat("0.00").format(p.getY());
					String z = new DecimalFormat("0.00").format(p.getZ());
					worldPos = "x: " + p.x + ", y: " + p.y + ", z: " + p.z;
					blockStats = state.getBlock().getLocalizedName() + " (" + WorldHelper.getBlockID(state.getBlock()) + ":" + state.getBlock().getMetaFromState(state) + ")";
					biome = "Biome: " + mc.theWorld.getBiomeGenForCoords(new BlockPos(p.x, p.y, p.z)).biomeName;
				}
			} else {
				if (editor.selectedBlock != null) {
					p = editor.selectedBlock.position;
					IBlockState state = WorldHelper.getBlockState(p.x, p.y, p.z);
					worldPos = "x: " + p.x + ", y: " + p.y + ", z: " + p.z;
					blockStats = state.getBlock().getLocalizedName() + " (" + WorldHelper.getBlockID(state.getBlock()) + ":" + state.getBlock().getMetaFromState(state) + ")";
					biome = "Biome: " + mc.theWorld.getBiomeGenForCoords(new BlockPos(p.x, p.z, p.y)).biomeName;
				}
			}
		}
		
		//guiInstance.drawString(mc.fontRendererObj, "XYZ:", guiInstance.midX - 467, guiInstance.midY - 106, Color.GREEN.getRGB());
		
		//guiInstance.drawString(mc.fontRendererObj, mousePos, guiInstance.midX - 467, guiInstance.midY - 85, Color.GREEN.getRGB());
		//guiInstance.drawString(mc.fontRendererObj, worldPos, guiInstance.midX - 467, guiInstance.midY - 75, /*(Editor.getCenter().compare(p)) ? 0x00BEFF :*/ 0x00FF00);
		//guiInstance.drawString(mc.fontRendererObj, blockStats, guiInstance.midX - 467, guiInstance.midY - 65, 0x00FF00);
		//guiInstance.drawString(mc.fontRendererObj, biome, guiInstance.midX - 467, guiInstance.midY - 55, Color.GREEN.getRGB());
		
		//guiInstance.drawString(mc.fontRendererObj, "Zoom: x" + editor.getZoomScale(), guiInstance.midX - 460, guiInstance.midY - 137, 0x00ff00);
		
		//guiInstance.drawString(mc.fontRendererObj, "pos1: " + editor.getPos1(), guiInstance.midX - 460, guiInstance.midY + 37, 0x00ff00);
		//guiInstance.drawString(mc.fontRendererObj, "pos2: " + editor.getPos2(), guiInstance.midX - 460, guiInstance.midY + 47, 0x00ff00);
	}
	
	private void parseTextBoxesInput(int input) {
		if (input < 3) {
			if (NumberUtil.isInteger(xCoord.getText()) && NumberUtil.isInteger(yCoord.getText()) && NumberUtil.isInteger(zCoord.getText())) {
				double passX, passY, passZ;
				passX = (!xCoord.getText().isEmpty()) ? Double.parseDouble(xCoord.getText()) : editor.getCenter().x;
				passY = (!yCoord.getText().isEmpty()) ? Double.parseDouble(yCoord.getText()) : editor.getCenter().y;
				passZ = (!zCoord.getText().isEmpty()) ? Double.parseDouble(zCoord.getText()) : editor.getCenter().z;
				editor.setCenter(passX, passY, passZ);
			}
			resetAllBoxesText();
		}
	}
	
	public void resetAllBoxesText() {
		Vector3D center = editor.getCenter();
		xCoord.setText(String.valueOf(center.x > 0 ? Math.floor(center.x) : Math.ceil(center.x)));
		yCoord.setText(String.valueOf(center.y > 0 ? Math.floor(center.y) : Math.ceil(center.y)));
		zCoord.setText(String.valueOf(center.z > 0 ? Math.floor(center.z) : Math.ceil(center.z)));
		
		xCoord.setSelectionPos(xCoord.getText().length());
		yCoord.setSelectionPos(yCoord.getText().length());
		zCoord.setSelectionPos(zCoord.getText().length());
	}
	
	@Override
	public void actionPerformed(IEnhancedActionObject object, Object... args) {
		try {
			if (object.equals(tpTo)) {
				double x = Double.parseDouble(xCoord.getText());
				double y = Double.parseDouble(yCoord.getText());
				double z = Double.parseDouble(zCoord.getText());
				x = x < 0 ? x - 0.5 : x + 0.5;
				z = z < 0 ? z - 0.5 : z + 0.5;
				mc.thePlayer.sendChatMessage("/tp " + x + " " + (y + 1) + " " + z);
			}
			if (object.equals(setButton)) {
				EditorSet.set((EditorApp) RegisteredApps.getApp(AppType.WORLDEDITOR));
			}
		} catch (Exception e) { e.printStackTrace(); }
	}
}
