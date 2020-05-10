package com.Whodundid.worldEditor.Editor3D.chunkUtil;

import com.Whodundid.core.util.storageUtil.Vector3D;
import com.Whodundid.core.util.storageUtil.Vector3DInt;
import com.Whodundid.core.util.worldUtil.WorldHelper;
import com.Whodundid.worldEditor.EditorApp;
import com.Whodundid.worldEditor.Editor3D.block.EditorBlock;
import com.Whodundid.worldEditor.Editor3D.block.EditorMaterial;
import com.Whodundid.worldEditor.Editor3D.renderingUtil.DPolygon;
import java.util.ArrayList;
import net.minecraft.util.Vec3;
import net.minecraft.util.Vec3i;

//Last edited: 11-20-18
//First Added: 9-10-18
//Author: Hunter Bragg

public class EditorChunk {
	
	EditorApp editor;
	EditorChunkCache chunks;
	ArrayList<EditorBlock> blockList = new ArrayList();
	ArrayList<DPolygon> chunkVertexData = new ArrayList();
	public Vector3DInt startPos;
	public int startX, startY, startZ;
	public int endX, endY, endZ;
	protected boolean building;
	
	public EditorChunk(EditorApp editorIn, EditorChunkCache cache) { this(editorIn, cache, new Vector3DInt()); }
	public EditorChunk(EditorApp editorIn, EditorChunkCache cache, Vector3DInt startPosIn) {
		editor = editorIn;
		chunks = cache;
		startPos = startPosIn;
		startX = startPos.x;
		startY = startPos.y;
		startZ = startPos.z;
		endX = startX + 16;
		endY = startY + 16;
		endZ = startZ + 16;
	}
	
	public void buildChunk() {
		building = true;
		fillChunkWithBlocks();
		building = false;
	}
		
	public void fillChunkWithAir() {
		for (int x = startPos.x; x < endX; x++) {
			for (int z = startPos.z; z < endZ; z++) {
				for (int y = startPos.y; y < endY; y++) {
					blockList.add(new EditorBlock(editor, x, y, z, EditorMaterial.AIR, this));
				}
			}
		}
	}
	
	public void fillChunkWithBlocks() {
		for (int x = startPos.x; x < endX; x++) {
			for (int z = startPos.z; z < endZ; z++) {
				for (int y = startPos.y; y < endY; y++) {
					EditorMaterial m;
					
					switch (WorldHelper.getBlockID(x, y, z)) {
					case 0: m = EditorMaterial.AIR; break;
					case 9: case 11: m = EditorMaterial.LIQUID; break;
					default: m = EditorMaterial.SOLID;
					}
					
					blockList.add(new EditorBlock(editor, x, y, z, m, this));
				}
			}
		}
	}
	
	public boolean isPositionWithinChunk(Vector3D posIn) { return isPositionWithinChunk(new Vector3DInt(posIn)); }
	public boolean isPositionWithinChunk(Vec3 posIn) { return isPositionWithinChunk(new Vector3DInt(posIn)); }
	public boolean isPositionWithinChunk(Vec3i posIn) { return isPositionWithinChunk(new Vector3DInt(posIn)); }
	public boolean isPositionWithinChunk(Vector3DInt posIn) { return isPositionWithinChunk(posIn.x, posIn.y, posIn.z); }
	public boolean isPositionWithinChunk(double x, double y, double z) { return isPositionWithinChunk((int) Math.floor(x), (int) Math.floor(y), (int) Math.floor(z)); }
	public boolean isPositionWithinChunk(int x, int y, int z) {
		return (x >= startX && x < endX && y >= startY && y < endY && z >= startZ && z < endZ);
	}
	
	public EditorBlock getBlockAtPos(Vector3DInt posIn) { return getBlockAtPos(posIn.x, posIn.getY(), posIn.z); }
	public EditorBlock getBlockAtPos(int x, int y, int z) {
		if (isPositionWithinChunk(x, y, z)) {
			for (EditorBlock b : blockList) {
				if (b.position.compare(x, y, z)) { return b; }
			}
		} else {
			return editor.chunkCache.getBlockAtPosition(new Vector3DInt(x, y, z));
		}
		
		/*else {
			EditorChunk c = StaticEditor.chunkCache.getNeighborChunk(new Vector3DInt(x, y, z));
			if (c != null) {
				for (EditorBlock b : c.getBlocks()) {
					if (b.position.compare(x, y, z)) { return b; }
				}
			}
		}*/
		return null;
	}
	
	public ArrayList<EditorBlock> getBlocks() { return blockList; }
	public ArrayList<DPolygon> getChunkVertexData() { return chunkVertexData; }
	public boolean isChunkBuilding() { return building; }
}
