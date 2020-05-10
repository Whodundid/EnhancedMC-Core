package com.Whodundid.worldEditor.Editor3D.renderingUtil;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Polygon;
import com.Whodundid.worldEditor.EditorApp;

public class PolygonObject {
	public Polygon poly;
	public Color color;
	public boolean draw = true, visible = true;
	public boolean seeThrough = false;
	double lighting = 1;
	EditorApp editor;
	
	public PolygonObject(EditorApp editorIn, double[] x, double[] y, Color c) {
		editor = editorIn;
		poly = new Polygon();
		for (int i = 0; i < x.length; i++) { poly.addPoint((int) x[i], (int) y[i]); }
		this.color = c;
	}
	
	public void updatePolygon(double[] x, double[] y) {
		poly.reset();
		for (int i = 0; i < x.length; i++) {
			poly.xpoints[i] = (int) x[i];
			poly.ypoints[i] = (int) y[i];
			poly.npoints = x.length;
		}
	}
	
	public void drawPolygon(Graphics g) {
		if (draw && visible) {
			g.setColor(new Color((int)(color.getRed() * lighting), (int)(color.getGreen() * lighting), (int)(color.getBlue() * lighting)));
			
			if (seeThrough) { g.drawPolygon(poly); }
			else { g.fillPolygon(poly); }
			
			if (editor.drawOutlines) {
				g.setColor(new Color(0, 0, 0));
				g.drawPolygon(poly);
			}

			if (editor.selectedBlock != null) {
				boolean passed = false;
				for (DPolygon p : editor.selectedBlock.getVertexData()) {
					if (p.dPoly == this) {
						passed = true;
					}
				}
				if (passed) {
					for (DPolygon p : editor.selectedBlock.getVertexData()) {
						g.setColor(new Color(255, 144, 144, 100));
						g.fillPolygon(p.dPoly.poly);
					}
				}
			}
		}
	}
	
	public boolean MouseOver(int mX, int mY) {
		if (editor.insideEditor) {
			if (editor.render3DHighRes) {
				int ratioX = (mX * editor.imageHandler3DHiRes.getTextureWidth()) / editor.imgWidth;
				int ratioY = (mY * editor.imageHandler3DHiRes.getTextureHeight()) / editor.imgHeight;
				return poly.contains(ratioX, ratioY - 5);
			}
			int ratioX = (mX * editor.imageHandler3D.getTextureWidth()) / editor.imgWidth;
			int ratioY = (mY * editor.imageHandler3D.getTextureHeight()) / editor.imgHeight;
			return poly.contains(ratioX, ratioY - 2);
		}
		return false;
	}
}
