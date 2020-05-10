package com.Whodundid.worldEditor.Editor3D.renderingUtil;

import java.awt.Color;

import com.Whodundid.core.util.storageUtil.Vector3D;
import com.Whodundid.worldEditor.EditorApp;
import com.Whodundid.worldEditor.Editor3D.block.EditorBlock;
import com.Whodundid.worldEditor.Editor3D.block.EditorDirection;

//Last edited: 11-20-18
//First Added: 9-10-18
//Author: Hunter Bragg

public class DPolygon {
	public Color c;
	public double[] x, y, z;
	public EditorBlock parentBlock;
	public EditorDirection renderDir;
	public boolean draw = true;
	public double[] CalcPos, newX, newY;
	public double AvgDist;
	public PolygonObject dPoly;
	EditorApp editor;
	
	public DPolygon(EditorApp editorIn, double[] x, double[] y,  double[] z, Color c, EditorBlock cubeIn, EditorDirection dirIn) {
		editor = editorIn;
		this.x = x;
		this.y = y;
		this.z = z;
		this.c = c;
		this.parentBlock = cubeIn;
		this.renderDir = dirIn;
		createPolygon();
	}
	
	public void createPolygon() { dPoly = new PolygonObject(editor, new double[x.length], new double[x.length], c); }
	public double GetDistanceToP(int i) { return Vector3D.magnitude(Vector3D.difference(editor.camPos, new Vector3D(x[i], y[i], z[i]))); }
	
	public double GetDist() {
		double total = 0;
		for (int i = 0; i < x.length; i++) { total += GetDistanceToP(i); }
		return total / x.length;
	}
	
	public void updatePolygon(EditorApp editor) {		
		newX = new double[x.length];
		newY = new double[x.length];
		draw = true;
		for (int i = 0; i < x.length; i++) {
			CalcPos = Calculator.CalculatePositionP(editor.camPos, new Vector3D(x[i], y[i], z[i]));
			newX[i] = (editor.drawWidth / 2 - Calculator.fovFocus[0]) + CalcPos[0] * editor.fieldOfView;
			newY[i] = (editor.drawHeight / 2 - Calculator.fovFocus[1]) + CalcPos[1] * editor.fieldOfView;
			if (Calculator.t < 0) { draw = false; }
		}
		
		calcLighting();
		
		dPoly.draw = draw;
		dPoly.updatePolygon(newX, newY);
		AvgDist = GetDist();
	}
	
	private void calcLighting() {
		if (editor != null) {
			Plane lightingPlane = new Plane(this);
			double angle = Vector3D.dotProduct(lightingPlane.nV, editor.light);
			dPoly.lighting = 0.2 + 1 - Math.sqrt(Math.toDegrees(angle) / 180);
			if (dPoly.lighting > 1) { dPoly.lighting = 1; }
			if (dPoly.lighting < 0) { dPoly.lighting = 0; }
		}
	}
}
