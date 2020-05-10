package com.Whodundid.parkourHelper;

import com.Whodundid.parkourHelper.JumpIdentifyer.JumpType;
import net.minecraft.util.BlockPos;

public class JumpSolution {
	
	JumpType type = JumpType.none;
	BlockPos start = null;
	BlockPos end = null;
	
	public JumpSolution(JumpType typeIn, BlockPos startIn, BlockPos endIn) {
		type = typeIn;
		start = startIn;
		end = endIn;
	}
	
	public boolean isPossible() { return type != JumpType.none && start != null && end != null; }
	public JumpType getJumpType() { return type; }
	public BlockPos getStart() { return start; }
	public BlockPos getEnd() { return end; }
}
