package com.Whodundid.parkourHelper;

import com.Whodundid.core.renderer.BlockDrawer;
import com.Whodundid.core.util.storageUtil.EArrayList;
import com.Whodundid.core.util.storageUtil.StorageBoxHolder;
import com.Whodundid.core.util.storageUtil.Vector3D;
import com.Whodundid.core.util.worldUtil.WorldHelper;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MathHelper;

public abstract class JumpIdentifyer {
	
	public enum JumpType {
		one,
		two,
		three,
		four,
		five,
		neo_one,
		neo_two,
		neo_three,
		neo_four,
		very_far,
		//IDK WHAT THESE 2 ARE!
		FIVE35,
		SIX29,
		none;
	}
	
	public static JumpType determineTypeOfJump(StorageBoxHolder<Integer, EArrayList<BlockPos>> blocks) {
		int gapSize = getGapSize(blocks);
		
		if (gapSize > 0) {
			switch (gapSize) {
			case 1: return JumpType.one;
			case 2: return JumpType.two;
			case 3: return JumpType.three;
			case 4: return JumpType.four;
			case 5: return JumpType.five;
			}
			if (gapSize > 5) { return JumpType.very_far; }
		}
		else {
			//check if neo
			int neoSize = getNeoSize(blocks);
			if (neoSize > 0) {
				switch (neoSize) {
				case 1: return JumpType.neo_one;
				case 2: return JumpType.neo_two;
				case 3: return JumpType.neo_three;
				case 4: return JumpType.neo_four;
				}
				if (neoSize > 4) { return JumpType.very_far; }
			}
		}
		return JumpType.none;
	}
	
	/** searches blocks out ahead of the player for any gaps in the floor and attempts to return the size of the fist gap it finds. */
	public static int getGapSize(StorageBoxHolder<Integer, EArrayList<BlockPos>> blocks) {
		try {
			if (blocks != null && blocks.isNotEmpty() && blocks.getValue(0) != null) {
				int rowSize = blocks.getValue(0).size();
				int colSize = blocks.size();
				
				//first check the blocks under the player's feet to see if there is even a gap at all
				boolean gapFound = false;
				boolean endFound = false;
				Vector3D startPos = new Vector3D();
				Vector3D endPos = new Vector3D();
				int gapSize = 0;
				
				for (int i = 0; i < rowSize; i++) {
					BlockPos pos = blocks.getValue(colSize - 2).get(i);
					if (gapFound) { gapSize++; }
					//check to see if the block is air
					if (WorldHelper.compareID(pos, 0)) {
						//check to see if it blocks player movement
						if (!WorldHelper.getBlock(pos).getMaterial().blocksMovement()) {
							if (!gapFound) { gapFound = true; startPos.set(blocks.getValue(colSize - 2).get(MathHelper.clamp_int(i - 1, 0, colSize - 1))); }
						}
					}
					else if (gapFound) {
						endFound = true;
						endPos = endPos.set(pos);
						break;
					}
				}
				if (gapFound && endFound) {
					BlockDrawer.addBlock(startPos, 0xff55ff55);
					BlockDrawer.addBlock(endPos, 0xffff5555);
					return gapSize;
				}
			}
		} catch (Exception e) { e.printStackTrace(); }
		return 0;
	}
	
	public static int getNeoSize(StorageBoxHolder<Integer, EArrayList<BlockPos>> blocks) {
		try {
			if (blocks != null && blocks.isNotEmpty() && blocks.getValue(0) != null) {
				int rowSize = blocks.getValue(0).size();
				int colSize = blocks.size();
				
				boolean wallFound = false;
				boolean endFound = false;
				Vector3D startPos = new Vector3D();
				Vector3D endPos = new Vector3D();
				int neoSize = 0;
				
				for (int i = 0; i < rowSize; i++) {
					BlockPos pos = blocks.getValue(colSize - 3).get(i);
					//check to see if the block is a full block
					if (WorldHelper.isWholeBlock(WorldHelper.getBlockState(pos))) {
						//if it is then check to see if it is not air
						if (!WorldHelper.compareID(pos, 0)) {
							//if it is not air, check if it is a block the player can walk through
							if (WorldHelper.getBlock(pos).getMaterial().blocksMovement()) {
								//if it is not air, check if a wall was already found, then add to the neo size
								if (!wallFound) { wallFound = true; startPos.set(blocks.getValue(colSize - 2).get(i - 1)); }
								neoSize++;
							}
						}
						else if (wallFound) {
							BlockPos testAir = blocks.getValue(colSize - 2).get(i);
							if (!WorldHelper.compareID(testAir, 0)) {
								endFound = true;
								endPos.set(testAir);
								break;
							}
						}
					}
				}
				if (wallFound && endFound) {
					BlockDrawer.addBlock(startPos, 0xff55ff55);
					BlockDrawer.addBlock(endPos, 0xffff5555);
					return neoSize;
				}
			}
		} catch (Exception e) { e.printStackTrace(); }
		return 0;
	}
}
