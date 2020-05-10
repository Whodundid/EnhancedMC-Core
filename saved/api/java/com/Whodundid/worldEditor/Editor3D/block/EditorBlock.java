package com.Whodundid.worldEditor.Editor3D.block;

import com.Whodundid.core.util.storageUtil.Vector3D;
import com.Whodundid.core.util.storageUtil.Vector3DInt;
import com.Whodundid.core.util.worldUtil.WorldHelper;
import com.Whodundid.worldEditor.EditorApp;
import com.Whodundid.worldEditor.Editor3D.Editor3D;
import com.Whodundid.worldEditor.Editor3D.chunkUtil.EditorChunk;
import com.Whodundid.worldEditor.Editor3D.renderingUtil.DPolygon;

import net.minecraft.block.Block;

import java.awt.Color;
import java.util.ArrayList;

//Last edited: 9-26-18
//First Added: 9-10-18
//Author: Hunter Bragg

public class EditorBlock {
	
	protected EditorApp editor;
	protected Editor3D renderer;
	protected EditorChunk chunk;
	public Vector3DInt position;
	public Block block;
	public EditorMaterial material;
	ArrayList<EditorDirection> renderedFaces = new ArrayList();
	double x, y, z, width, length, height, rot = Math.PI * 0.75;
	double[] RotAdd = new double[4];
	Color c;
	double x1, x2, x3, x4, y1, y2, y3, y4;
	private ArrayList<DPolygon> vertexData = new ArrayList();
	double[] angle;
	
	public EditorBlock(EditorApp editorIn, int x, int y, int z, EditorMaterial materialIn, EditorChunk chunkIn) { this (editorIn, new Vector3DInt(x, y, z), new Vector3D(1, 1, 1), materialIn, chunkIn); }
	public EditorBlock(EditorApp editorIn, Vector3DInt positionIn, Vector3D dimensions, EditorMaterial materialIn, EditorChunk chunkIn) {
		editor = editorIn;
		chunk = chunkIn;
		position = positionIn;
		block = WorldHelper.getBlock(position);
		material = materialIn;
		x = position.x;
		y = position.z;
		z = position.y;
		width = dimensions.x;
		length = dimensions.y;
		height = dimensions.z;
		c = new Color(WorldHelper.getCorrectMapColor(WorldHelper.getBlockPos(position), WorldHelper.getBlockState(position)));
	}
	
	public void createVertexData() {
		if (!material.equals(EditorMaterial.AIR)) {
			determineRenderedFaces();
			
			for (EditorDirection d : renderedFaces) {
				switch (d) {
				case DOWN: 
					vertexData.add(new DPolygon(editor, new double[] {x, x + width, x + width, x},
												new double[] {y, y, y + length, y + length},
												new double[] {z, z, z, z},
												c, this, EditorDirection.DOWN));
					break;
				case EAST: 
					vertexData.add(new DPolygon(editor, new double[] {x + width, x + width, x + width, x + width},
												new double[] {y, y, y + length, y + length},
												new double[] {z, z + height, z + height, z},
												c, this, EditorDirection.EAST));
					break;
				case NORTH: 
					vertexData.add(new DPolygon(editor, new double[] {x, x, x + width, x + width}, 
												new double[] {y + length, y + length, y + length, y + length}, 
												new double[] {z, z + height, z + height, z}, 
												c, this, EditorDirection.NORTH));
					break;
				case SOUTH: 
					vertexData.add(new DPolygon(editor, new double[] {x, x, x + width, x + width},
												new double[] {y, y, y, y},
												new double[] {z, z + height, z + height, z},
												c, this, EditorDirection.SOUTH));
					break;
				case UP: 
					vertexData.add(new DPolygon(editor, new double[] {x, x + width, x + width, x},
												new double[] {y, y, y + length, y + length},
												new double[] {z + height, z + height, z + height, z + height},
												c, this, EditorDirection.UP));                
					break;
				case WEST:
					vertexData.add(new DPolygon(editor, new double[] {x, x, x, x},
												new double[] {y, y, y + length, y + length},
												new double[] {z, z + height, z + height, z},
												c, this, EditorDirection.WEST));
					break;
				default: break;
				}
			}
			setRotAdd();
			updatePoly();
		}
	}
	
	private void determineRenderedFaces() {
		EditorBlock up = editor.getChunkCache().getBlockAtPosition(new Vector3DInt(position.x, position.y + 1, position.z));
		EditorBlock down = editor.getChunkCache().getBlockAtPosition(new Vector3DInt(position.x, position.y - 1, position.z));
		EditorBlock north = editor.getChunkCache().getBlockAtPosition(new Vector3DInt(position.x, position.y, position.z - 1));
		EditorBlock east = editor.getChunkCache().getBlockAtPosition(new Vector3DInt(position.x - 1, position.y, position.z));
		EditorBlock south = editor.getChunkCache().getBlockAtPosition(new Vector3DInt(position.x, position.y, position.z + 1));
		EditorBlock west = editor.getChunkCache().getBlockAtPosition(new Vector3DInt(position.x + 1, position.y, position.z));
		
		if (up == null || up.material.equals(EditorMaterial.AIR)) { renderedFaces.add(EditorDirection.UP); }
		if (down == null || down.material.equals(EditorMaterial.AIR)) { renderedFaces.add(EditorDirection.DOWN); }
		if (north == null || north.material.equals(EditorMaterial.AIR)) { renderedFaces.add(EditorDirection.NORTH); }
		if (east == null || east.material.equals(EditorMaterial.AIR)) { renderedFaces.add(EditorDirection.EAST); }
		if (south == null || south.material.equals(EditorMaterial.AIR)) { renderedFaces.add(EditorDirection.SOUTH); }
		if (west == null || west.material.equals(EditorMaterial.AIR)) { renderedFaces.add(EditorDirection.WEST); }
	}
	
	public void updateDirection(double toX, double toY) {
		double xdif = toX - (x + width / 2) + 0.00001;
		double ydif = toY - (y + length / 2) + 0.00001;
		double anglet = Math.atan(ydif / xdif) + 0.75 * Math.PI;
		if (xdif < 0) { anglet += Math.PI; }
		rot = anglet;
		updatePoly();
	}
	
	private void setRotAdd() {
		angle = new double[4];
		double xDif = 0, yDif = 0;
		for (int i = 0; i < 4; i++) {
			if (i == 0) { xDif = -width / 2; yDif = -length / 2; }
			if (i == 1) { xDif = width / 2; yDif = -length / 2; }
			if (i == 2) { xDif = width / 2; yDif = length / 2; }
			if (i == 3) { xDif = -width / 2; yDif = length / 2; }
			angle[i] = Math.atan(yDif / xDif);
			if (xDif < 0) { angle[i] += Math.PI; }
			RotAdd[i] = angle[i] + 0.25 * Math.PI;
		}
	}

	private void updatePoly() {
		double radius = Math.sqrt(width * width + length * length);
		
		x1 = x + width * 0.5 + radius * 0.5 * Math.cos(rot + RotAdd[0]);
		x2 = x + width * 0.5 + radius * 0.5 * Math.cos(rot + RotAdd[1]);
		x3 = x + width * 0.5 + radius * 0.5 * Math.cos(rot + RotAdd[2]);
		x4 = x + width * 0.5 + radius * 0.5 * Math.cos(rot + RotAdd[3]);
			   
		y1 = y + length * 0.5 + radius * 0.5 * Math.sin(rot + RotAdd[0]);
		y2 = y + length * 0.5 + radius * 0.5 * Math.sin(rot + RotAdd[1]);
		y3 = y + length * 0.5 + radius * 0.5 * Math.sin(rot + RotAdd[2]);
		y4 = y + length * 0.5 + radius * 0.5 * Math.sin(rot + RotAdd[3]);
		
		for (DPolygon g : vertexData) {
			switch (g.renderDir) {
			case DOWN:
				g.x = new double[] {x1, x2, x3, x4};
				g.y = new double[] {y1, y2, y3, y4};
				g.z = new double[] {z, z, z, z};
				break;
			case EAST:
				g.x = new double[] {x2, x2, x3, x3};
				g.y = new double[] {y2, y2, y3, y3};
				g.z = new double[] {z, z + height, z + height, z};
				break;
			case NORTH:
				g.x = new double[] {x3, x3, x4, x4};
				g.y = new double[] {y3, y3, y4, y4};
				g.z = new double[] {z, z + height, z + height, z};
				break;
			case SOUTH:
				g.x = new double[] {x1, x1, x2, x2};
				g.y = new double[] {y1, y1, y2, y2};
				g.z = new double[] {z, z + height, z + height, z};
				break;
			case UP:
				g.x = new double[] {x4, x3, x2, x1};
				g.y = new double[] {y4, y3, y2, y1};
				g.z = new double[] {z + height, z + height, z + height, z + height};
				break;
			case WEST:
				g.x = new double[] {x4, x4, x1, x1};
				g.y = new double[] {y4, y4, y1, y1};
				g.z = new double[] {z, z + height, z + height, z};
				break;
			default: break;
			}
		}
	}

	public void setSeeThrough(boolean val) {
		for (DPolygon p : vertexData) { p.dPoly.seeThrough = val; }
	}
	
	public boolean isAir() { return material.equals(EditorMaterial.AIR); }
	public boolean isLiquid() { return material.equals(EditorMaterial.LIQUID); }
	public ArrayList<DPolygon> getVertexData() { return vertexData; }
	public EditorChunk getChunk() { return chunk; }
}
