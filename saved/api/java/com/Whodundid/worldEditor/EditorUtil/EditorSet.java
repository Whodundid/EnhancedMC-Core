package com.Whodundid.worldEditor.EditorUtil;

import com.Whodundid.core.util.storageUtil.Vector3DInt;
import com.Whodundid.worldEditor.EditorApp;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.util.BlockPos;

public class EditorSet {
	
	static Minecraft mc = Minecraft.getMinecraft();
	
	public static void set(EditorApp editor) {
		try {
			if (!mc.isSingleplayer()) {
				Vector3DInt pos1 = editor.pos1;
				Vector3DInt pos2 = editor.pos2;
				
				int startX, startY, startZ;
				int endX, endY, endZ;
				
				startX = (pos1.x < pos2.x) ? pos1.x : pos2.x;
				startY = (pos1.y < pos2.y) ? pos1.y : pos2.y;
				startZ = (pos1.z < pos2.z) ? pos1.z : pos2.z;
				endX = (pos1.x < pos2.x) ? pos2.x : pos1.x;
				endY = (pos1.y < pos2.y) ? pos2.y : pos1.y;
				endZ = (pos1.z < pos2.z) ? pos2.z : pos1.z;
				
				System.out.println(startX + " " + startY + " " + startZ);
				
				for (int x = startX; x < endX + 1; x++) {
					for (int z = startZ; z < endZ + 1; z++) {
						for (int y = startY; y < endY + 1; y++) {
							int id = 0;
							String blockInput = editor.getGuiInstance().editorTextInput.idInput.getText();
							if (!blockInput.isEmpty()) { id = Integer.parseInt(blockInput); }
							mc.theWorld.setBlockState(new BlockPos(x, y, z), Block.getStateById(id));
						}
					}
				}
			} else {
				String id = "0";
				String blockInput = editor.getGuiInstance().editorTextInput.idInput.getText();
				if (!blockInput.isEmpty()) { id = editor.getGuiInstance().editorTextInput.idInput.getText(); }
				mc.thePlayer.sendChatMessage("//set " + blockInput);
			}
		} catch (Exception e) { e.printStackTrace(); }
		
		//editor.requestRedraw();
		if (editor.render3D) {
			//editor.renderer.generateWorld(StaticEditor.getCamPos());
		}
	}
}
