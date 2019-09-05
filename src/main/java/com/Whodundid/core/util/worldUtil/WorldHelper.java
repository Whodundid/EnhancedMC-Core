package com.Whodundid.core.util.worldUtil;

import com.Whodundid.core.util.playerUtil.Direction;
import com.Whodundid.core.util.storageUtil.EArrayList;
import com.Whodundid.core.util.storageUtil.Vector3D;
import com.Whodundid.core.util.storageUtil.Vector3DInt;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.util.BlockPos;
import net.minecraft.util.Vec3;

//Last edited: 10-16-18
//First Added: 9-14-18
//Author: Hunter Bragg

public abstract class WorldHelper {
	
	static Minecraft mc = Minecraft.getMinecraft();
	
	public static BlockPos getBlockPos(int x, int y, int z) { return new BlockPos(x, y, z); }
	public static BlockPos getBlockPos(double x, double y, double z) { return new BlockPos(x, y, z); }
	public static BlockPos getBlockPos(Vector3DInt posIn) { return new BlockPos(posIn.x, posIn.y, posIn.z); }
	public static BlockPos getBlockPos(Vector3D posIn) { return new BlockPos(posIn.x, posIn.y, posIn.z); }
	public static BlockPos getBlockPos(Vec3 vecIn) { return new BlockPos(vecIn); }
	
	public static Block getBlock(int x, int y, int z) { return getBlock(getBlockPos(x, y, z)); }
	public static Block getBlock(double x, double y, double z) { return getBlock(getBlockPos(x, y, z)); }
	public static Block getBlock(Vector3DInt vecIn) { return getBlock(getBlockPos(vecIn)); }
	public static Block getBlock(Vector3D vecIn) { return getBlock(getBlockPos(vecIn)); }
	public static Block getBlock(Vec3 vecIn) { return getBlock(getBlockPos(vecIn)); }
	public static Block getBlock(BlockPos pos) { return mc.theWorld != null ? mc.theWorld.getBlockState(pos).getBlock() : null; }
	
	public static IBlockState getBlockState(int x, int y, int z) { return getBlockState(getBlockPos(x, y, z)); }
	public static IBlockState getBlockState(double x, double y, double z) { return getBlockState(getBlockPos(x, y, z)); }
	public static IBlockState getBlockState(Vector3DInt vecIn) { return getBlockState(getBlockPos(vecIn)); }
	public static IBlockState getBlockState(Vector3D vecIn) { return getBlockState(getBlockPos(vecIn)); }
	public static IBlockState getBlockState(Vec3 vecIn) { return getBlockState(getBlockPos(vecIn)); }
	public static IBlockState getBlockState(BlockPos pos) { return mc.theWorld != null ? mc.theWorld.getBlockState(pos) : null; }
	
	public static int getBlockID(int x, int y, int z) { return getBlockID(getBlockPos(x, y, z)); }
	public static int getBlockID(double x, double y, double z) { return getBlockID(getBlock(x, y, z)); }
	public static int getBlockID(Vector3DInt vecIn) { return getBlockID(getBlock(vecIn)); }
	public static int getBlockID(Vector3D vecIn) { return getBlockID(getBlock(vecIn)); }
	public static int getBlockID(Vec3 vecIn) { return getBlockID(getBlock(vecIn)); }
	public static int getBlockID(BlockPos posIn) { return getBlockID(getBlock(posIn)); }
	public static int getBlockID(Block blockIn) { return Block.getIdFromBlock(blockIn); }
	
	public static boolean compareID(Block blockIn, int id) { return getBlockID(blockIn) == id; }
	public static boolean compareID(BlockPos blockPosIn, int id) { return compareID(getBlock(blockPosIn), id); }
	public static boolean testBlockIDFor(Block blockIn, EArrayList<Integer> listIn) { return listIn.contains(getBlockID(blockIn)); }
	public static boolean testBlockForPassable(BlockPos blockPosIn) { return testBlockForPassable(getBlock(blockPosIn)); }
	
	public static boolean testBlockForPassable(Block blockIn) {
		int id = Block.getIdFromBlock(blockIn);
		switch (id) {
		case 0: case 31: case 32: case 37: case 38: //air, tall grass, dead bush, flower yellow, flower red
		case 39: case 40: case 50: case 55: case 66: //mushroom brown, mushroom red, torch, redstone dust, rail base
		case 69: case 70: case 72: case 76: case 77: //lever, stone pressure plate, wood pressure plate, redstone torch on, stone button
		case 78: case 132: case 143: case 171: case 175: //snow layer, string on block, wood button, carpet, double grass
		case 323: case 425: return true; //sign, banner
		default: return false;
		}
	}
	
	public static boolean checkBlockForMapDraw(BlockPos pos, IBlockState state) {
		EArrayList<Integer> l = new EArrayList();
		l.add(31); l.add(175); l.add(37); l.add(38); l.add(171);
		return !WorldHelper.testBlockIDFor(state.getBlock(), l);
	}
	
	public static int getCorrectMapColor(BlockPos pos, IBlockState state) {
		//BiomeGenBase b = mc.theWorld.getBiomeGenForCoords(pos);
		switch (getBlockID(state.getBlock())) {
		case 1: //stone
			switch (state.getBlock().getMetaFromState(state)) {
			case 1: case 2: return 0xD19F89; //granite
			case 3: case 4: return 0xE3E3E6; //diorite
			case 5: case 6: return 0x8D8D91; //andesite
			default: return Block.getBlockById(getBlockID(state.getBlock())).getMapColor(state).colorValue;
			}
		//case 2: return b.fillerBlockMetadata; //needs work
		case 3: //dirt
			switch (state.getBlock().getMetaFromState(state)) {
			case 0: return 0x966C4A; //base dirt
			case 1: return 0x79553A; //coarse dirt
			case 2: return 0x82571F; //podzol
			}
		case 4: return 0x858585; //cobble
		case 14: return 0xFCEE4B; //gold ore
		case 15: return 0xffffff; //iron ore
		case 18: //leaves
			switch (state.getBlock().getMetaFromState(state)) {
			case 0: case 4: case 8: case 12: return 0x4F721F; //oak
			case 1: case 5: case 9: case 13: return 0x395939; //spruce
			case 2: case 6: case 10: case 14: return 0x3A4B26; //birch
			case 3: case 7: case 11: case 15: return 0x6C911D; //jungle
			}
		case 16: return 0x8F0303; //coal ore
		case 20: return 0xEEF3FA; //glass
		case 21: return 0x1B469D; //lapis ore
		case 44: //slabs
			switch (state.getBlock().getMetaFromState(state)) {
			case 6: case 14: return 0x37181E; //nether
			default: return Block.getBlockById(getBlockID(state.getBlock())).getMapColor(state).colorValue;
			}
		case 49: return 0x290d4c; //obsidian
		case 55: return 0xD80000; //redstone dust
		case 56: return 0x2BE1F4; //diamond ore
		case 73: return 0xF92100; //redstone ore
		case 98: case 109: return 0x7D7D7D; //stonebrick
		case 112: case 114: return 0x37181E; //netherbrick
		case 153: return 0xFFFFFF; //nether quartz ore
		case 159: //stained clays
			switch (state.getBlock().getMetaFromState(state)) {
			//case 1: return 0xDB6D36;
			case 9: return 0x55595A; //cyan
			default: return Block.getBlockById(getBlockID(state.getBlock())).getMapColor(state).colorValue;
			}
		case 172: return 0xD1744D; //hard clay
		default: return Block.getBlockById(getBlockID(state.getBlock())).getMapColor(state).colorValue;
		}
	}
	
	public static boolean isWholeBlock(IBlockState state) {
		return !(isHalfBlock(state) || isStairBlock(state) || isFenceBlock(state) || getBlockPixelHeight(state) != 16 || isBlockFlatPlane(state));
	}
	
	public static boolean isHalfBlock(IBlockState state) {
		switch (getBlockID(state.getBlock())) {
		case 44: case 126: case 182: return true;
		default: return false;
		}
	}
	
	public static boolean isHalfBlockUp(IBlockState state) {
		switch (getBlockID(state.getBlock())) {
		case 44:
			switch (state.getBlock().getMetaFromState(state)) {
			case 8: case 9: case 10: case 11: case 12: 
			case 13: case 14: case 15: return true;
			default: return false;
			}
		case 126:
			switch (state.getBlock().getMetaFromState(state)) {
			case 8: case 9: case 10: case 11: case 12: 
			case 13: return true;
			default: return false;
			}
		case 182:
			switch (state.getBlock().getMetaFromState(state)) {
			case 8: return true;
			default: return false;
			}
		default: return false;
		}
	}
	
	public static boolean isStairBlock(IBlockState state) {
		switch (getBlockID(state.getBlock())) {
		case 53: case 67: case 108: case 109: case 114: case 128: //oak, stone, brick, stonebrick, nether, sandstone
		case 134: case 135: case 136: case 156: case 163: case 164: //spruce, brich, jungle, quartz, acacia, darkoak
		case 180: return true; //redsand
		default: return false;
		}
	}
	
	public static boolean isStairBlockUp(IBlockState state) {
		switch (getBlockID(state.getBlock())) {
		case 53: case 67: case 108: case 109: case 114: //oak, cobble, brick, stone, nether
		case 128: case 134: case 135: case 136: case 156: //sandstone, spruce, birch, jungle, quartz
		case 163: case 164: case 180: return true; //acacia, dark, redsand
		default: return false;
		}
	}
	
	public static Direction getStairBlockFacingDir(IBlockState state) {
		switch (state.getBlock().getMetaFromState(state)) {
		case 4: return Direction.E;
		case 5: return Direction.W;
		case 6: return Direction.N;
		case 7: return Direction.S;
		default: return Direction.OUT;
		}
	}
	
	public static boolean isTrapDoor(IBlockState state) {
		return getBlockID(state.getBlock()) == 96 || getBlockID(state.getBlock()) == 167;
	}
	
	public static boolean isPane(IBlockState state) {
		return getBlockID(state.getBlock()) == 101 || getBlockID(state.getBlock()) == 102 || getBlockID(state.getBlock()) == 160;
	}
	
	public static boolean isFenceBlock(IBlockState state) {
		switch (getBlockID(state.getBlock())) {
		case 85: case 113: case 188: case 189: case 190: case 191: case 192: //oak, nether, spruce, birch, jungle, dark, acacia fences
		case 139: return true; //cobble wall
		default: return false;
		}
	}
	
	public static boolean isTranslucentBlock(IBlockState state) {
		switch (getBlockID(state.getBlock())) {
		case 20: case 95: case 102: case 160: return true; //glass, stained, pane, stainedpane
		default: return false;
		}
	}
	
	public static boolean isRedstoneRelated(IBlockState state) {
		switch (getBlockID(state.getBlock())) {
		case 55: case 69: case 70: case 72: //redstone dust, lever, pressure plate stone, pressure plate wood
		case 75: case 76: case 77: case 93: //torch off, torch on, stone button, repeater
		case 131: case 143: case 147: case 148: //tripwire hook, button wood, pressure plate gold, pressure plate iron
		case 149: case 151: return true; //comparator, daylight sensor
		default: return false;
		}
	}
	
	public static boolean isBlockFlatPlane(IBlockState state) {
		switch (getBlockID(state.getBlock())) {
		case 55: case 27: case 28: case 66: case 157: //redstone dust, powered, detector, base, activator rails
		case 132: case 111: return true; //tripwire, lilypad
		default: return false;
		}
	}
	
	public static boolean is16thBlockHeight(IBlockState state) {
		switch (getBlockID(state.getBlock())) {
		//case 55: case 70: case 72: case 147: case 148: //redstone dust, pressure plates
		//case 27: case 28: case 66: case 157: //powered, detector, base, activator rails
		case 171: return true; //carpets
		default: return false;
		}
	}
	
	public static boolean is8thBlockHeight(IBlockState state) {
		switch (getBlockID(state.getBlock())) {
		default: return false;
		}
	}
	
	public static int getBlockPixelHeight(IBlockState state) {
		if (isHalfBlock(state)) { return 8; }
		if (is16thBlockHeight(state)) { return 1; }
		if (is8thBlockHeight(state)) { return 2; }
		switch (getBlockID(state.getBlock())) {
		case 171: return 1;
		case 78: return 2 + (2 * state.getBlock().getMetaFromState(state)); //snow layers
		case 93: case 149: return 2; //repeater, comparator
		case 96: case 167: return 3; //wood, iron trapdoor
		case 140: case 151: return 6; //flower pot, daylight sensor
		case 28: return 9; //bed
		case 116: return 12; //enchant table
		case 120: return 13; //end portal frame
		case 9: case 11: case 54: case 130: case 146: return 14; //water, lava, base chest, end chest, trap chest
		default: return 16;
		}
	}
}
