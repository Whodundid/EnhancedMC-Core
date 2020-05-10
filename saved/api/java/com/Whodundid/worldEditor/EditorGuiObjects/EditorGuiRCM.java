package com.Whodundid.worldEditor.EditorGuiObjects;

import com.Whodundid.core.enhancedGui.guiObjects.actionObjects.EGuiButton;
import com.Whodundid.core.enhancedGui.types.interfaces.IEnhancedActionObject;
import com.Whodundid.core.util.storageUtil.Vector3D;
import com.Whodundid.core.util.storageUtil.Vector3DInt;
import com.Whodundid.worldEditor.EditorUtil.EditorGuiBase;

//Last edited: Nov 25, 2018
//First Added: unknown
//Author: Hunter Bragg

public class EditorGuiRCM extends EditorGuiObject {
	
	public Vector3D clickedPos;
	protected EGuiButton tpTo, reCenter;
	
	public EditorGuiRCM(EditorGuiBase guiIn, int xIn, int yIn) {
		super(guiIn);
		init(guiIn, xIn, yIn, 80, 100);
		clickedPos = new Vector3D();
	}
	
	@Override
	public void initObjects() {
		tpTo = new EGuiButton(this, startX + 5, startY + 20, width - 10, 20, "Tp To");
		reCenter = new EGuiButton(this, startX + 5, startY + 42, width - 10, 20, "Re-center");
		
		addObject(tpTo, reCenter);
	}

	@Override
	public void drawObject(int mX, int mY) {
		//mc.renderEngine.bindTexture(EMCResources.guiRCMBase);
		//guiInstance.drawModalRectWithCustomSizedTexture(startX, startY, 0, 0, width, height, width, height);
		drawText(mX, mY);
		super.drawObject(mX, mY);
	}
	
	@Override
	public void actionPerformed(IEnhancedActionObject object, Object... args) {
		if (object.equals(tpTo)) {
			if (editor.getRender3D()) {
				Vector3DInt pos = editor.get3D().getSelectedBlockPosition();
				if (pos != null) {
					mc.thePlayer.sendChatMessage("/tp " + pos.x + " " + (pos.z + 1) + ".00 " + pos.y);
					editor.get3D().moveTo(pos.x + 0.5, pos.y + 0.5, pos.z + 3);
					editor.setCenter(pos.x, pos.z, pos.y);
				}
			} else {
				mc.thePlayer.sendChatMessage("/tp " + clickedPos.x + " " + (clickedPos.y + 1) + " " + clickedPos.z);
				editor.setCenter(clickedPos);
			}
		} else if (object.equals(reCenter)) {
			editor.setCenter(clickedPos);
		}
		guiInstance.removeRCM();
	}
	
	private void drawText(int mX, int mY) {
		try {
			if (editor.getRender3D()) {
				//Vector3DInt pos = editor.renderer.getSelectedBlockPosition();
				//if (pos != null) {
				//	guiInstance.drawCenteredString(mc.fontRendererObj, "x: " + pos.x + ", y: " + pos.z + ", z: " + pos.y, startX + width / 2, startY + 6, 0x00FF00);
				//}
			}
			else {
				//guiInstance.drawCenteredString(mc.fontRendererObj, "x: " + clickedPos.x + ", z: " + clickedPos.z, startX + width / 2, startY + 6, 0x00FF00);
			}
		} catch (Exception e) { e.printStackTrace(); }
	}
	
	@Override
	public void keyPressed(char typedChar, int keyCode) {
		if (keyCode == 1) { guiInstance.removeRCM(); }
	}
	
	public EditorGuiRCM setClickedPos(Vector3D posIn) { if (posIn != null) { clickedPos = new Vector3D(posIn); } return this; }
	public EditorGuiRCM setClickedPos(Vector3DInt posIn) { return setClickedPos(new Vector3D(posIn)); }
	public EditorGuiRCM setClickedPos(double x, double y, double z) { return setClickedPos(new Vector3D(x, y, z)); }
}
