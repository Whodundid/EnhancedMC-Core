package com.Whodundid.core.util.worldUtil;

import com.Whodundid.core.util.storageUtil.Vector3D;

//Author: Hunter Bragg

public class WorldRegion {
	
	public double sX = 0, eX = 0;
	public double sY = 0, eY = 0;
	public double sZ = 0, eZ = 0;
	public double mX = 0, mY = 0, mZ = 0;
	public double width = 0, height = 0, length = 0;
	
	public WorldRegion(double startXIn, double startYIn, double startZIn, double endXIn, double endYIn, double endZIn) {
		sX = startXIn;
		sY = startYIn;
		sZ = startZIn;
		eX = endXIn;
		eY = endYIn;
		eZ = endZIn;
		width = endXIn - startXIn;
		height = endYIn - startYIn;
		length = endZIn - startZIn;
		mX = getMidX();
		mY = getMidY();
		mZ = getMidZ();
	}
	
	public WorldRegion(WorldRegion dimIn) {
		sX = dimIn.sX;
		sY = dimIn.sY;
		sZ = dimIn.sZ;
		eX = dimIn.eX;
		eY = dimIn.eY;
		eZ = dimIn.eZ;
		width = eX - sX;
		height = eY - sY;
		length = eZ - sZ;
		mX = getMidX();
		mY = getMidY();
		mZ = getMidZ();
	}
	
	public WorldRegion(Vector3D vecIn) {
		sX = vecIn.x;
		sY = vecIn.y;
		sZ = vecIn.z;
		eX = sX + 1;
		eY = sY + 1;
		eZ = sZ + 1;
		width = 1;
		height = 1;
		length = 1;
		mX = getMidX();
		mY = getMidY();
		mZ = getMidZ();
	}
	
	public WorldRegion move(double changeX, double changeY, double changeZ) {
		sX += changeX;
		sY += changeY;
		sZ += changeZ;
		reDimension();
		return this;
	}
	
	public WorldRegion setPosition(double newX, double newY, double newZ) {
		sX = newX;
		sY = newY;
		sZ = newZ;
		reDimension();
		return this;
	}
	
	public WorldRegion setWidth(double newWidth) {
		width = newWidth;
		reDimension();
		return this;
	}
	
	public WorldRegion setHeight(double newHeight) {
		height = newHeight;
		reDimension();
		return this;
	}
	
	public WorldRegion setLength(double newLength) {
		length = newLength;
		reDimension();
		return this;
	}
	
	private void reDimension() {
		eX = sX + width;
		eY = sY + height;
		eZ = sZ + length;
		mX = getMidX();
		mY = getMidY();
		mZ = getMidZ();
	}
	
	public double getMidX() { return sX + (width / 2); }
	public double getMidY() { return sY + (height / 2); }
	public double getMidZ() { return sZ + (length / 2); }
	
	public WorldRegion translateHorizontal(double amount) { sX += amount; return this; }
	public WorldRegion translateVertical(double amount) { sY += amount; return this; }
	
	public boolean isFullyCovering(WorldRegion dimIn) { return sX > dimIn.sX && sY > dimIn.sY && eX > dimIn.eX && eY > dimIn.eY; }
	public boolean isGreaterThan(WorldRegion dimIn) { return sX > dimIn.sX && sY > dimIn.sY && width > dimIn.width && height > dimIn.height; }
	public boolean isLessThan(WorldRegion dimIn) { return sX < dimIn.sX && sY < dimIn.sY && width < dimIn.width && height < dimIn.height; }
	public boolean isEqualTo(WorldRegion dimIn) { return sX == dimIn.sX && sY == dimIn.sY && width == dimIn.width && height == dimIn.height; }
	
	@Override public String toString() {
		return "[startX/Y/Z: " + sX + ", " + sY + ", " + sZ + 
			   "; endX/Y/Z: " + eX + ", " + eY + ", " + eZ + 
			   "; width/Height/Length: " + width + ", " + height + ", " + length + "]";
	}
	
}
