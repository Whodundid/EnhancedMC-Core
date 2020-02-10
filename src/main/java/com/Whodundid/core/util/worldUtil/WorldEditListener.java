package com.Whodundid.core.util.worldUtil;

import com.Whodundid.core.EnhancedMC;
import com.Whodundid.core.util.chatUtil.EChatUtil;
import com.Whodundid.core.util.renderUtil.BlockDrawer;
import com.Whodundid.core.util.storageUtil.Vector3D;
import com.Whodundid.core.util.storageUtil.WorldRegion;

//Author: Hunter Bragg

public class WorldEditListener {
	
	static Vector3D pos1 = new Vector3D(), pos2 = new Vector3D();
	static boolean pos1Set = false, pos2Set = false;
	
	public static Vector3D getPos1() { return pos1; }
	public static Vector3D getPos2() { return pos2; }
	public static boolean posSet() { return (pos1Set && pos2Set); }
	public static void reset() { pos1.clear(); pos2.clear(); pos1Set = false; pos2Set = false; }
	
	public static void checkForPositions() {
		if (EnhancedMC.isOpMode() && EnhancedMC.isUserDev()) {
			try {
				parse(EChatUtil.checkMsgUnfContains("First position set to ("), EChatUtil.getLMsgUnf());
			} catch (Exception e) { e.printStackTrace(); }
			
			if (EChatUtil.checkMsgUnfContains("First position set to (") || EChatUtil.checkMsgUnfContains("Second position set to (")) {
				if (getPos1() != null && getPos2() != null) {
					BlockDrawer.clearBlocks();
					
					double startX = Math.min(pos1.x, pos2.x);
					double startY = Math.min(pos1.y, pos2.y);
					double startZ = Math.min(pos1.z, pos2.z);
					double endX = Math.max(pos1.x, pos2.x);
					double endY = Math.max(pos1.y, pos2.y);
					double endZ = Math.max(pos1.z, pos2.z);
					
					BlockDrawer.addBlock(new WorldRegion(startX, startY, startZ, endX + 1, endY + 1, endZ + 1), 0xffffff55);
					BlockDrawer.addBlock(getPos1(), 0xff55ff55);
					BlockDrawer.addBlock(getPos2(), 0xff5555ff);
				}
			}
		}
	}
	
	private static void parse(boolean isPos1, String in) {
		String[] parts = null;
		
		if (in.contains("First ")) { parts = in.substring(23, in.length() - 2).split(", "); }
		else if (in.contains("Second ")) { parts = in.substring(24, in.length() - 2).split(", "); }
		
		if (parts != null) {
			
			int pos = 1;
			String p3 = parts[2];
			while (p3.charAt(p3.length() - pos) != ')') { pos++; }
			parts[2] = p3.substring(0, p3.length() - pos);
			
			double x = Double.parseDouble(parts[0]);
			double y = Double.parseDouble(parts[1]);
			double z = Double.parseDouble(parts[2]);
		
			if (isPos1) { pos1.set(x, y, z); pos1Set = true; }
			else { pos2.set(x, y, z); pos2Set = true; }
		}
	}
}
