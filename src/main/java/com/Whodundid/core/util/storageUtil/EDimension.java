package com.Whodundid.core.util.storageUtil;

//Last edited: Nov 3, 2018
//First Added: Nov 3, 2018
//Author: Hunter Bragg

public class EDimension {
	
	public int startX = 0, endX = 0;
	public int startY = 0, endY = 0;
	public int midX = 0, midY = 0;
	public int width = 0, height = 0;
	
	public EDimension(int startXIn, int startYIn, int endXIn, int endYIn) {
		startX = startXIn;
		startY = startYIn;
		endX = endXIn;
		endY = endYIn;
		width = endXIn - startXIn;
		height = endYIn - startYIn;
		midX = getMidX();
		midY = getMidY();
	}
	
	public EDimension(EDimension dimIn) {
		startX = dimIn.startX;
		startY = dimIn.startY;
		endX = dimIn.endX;
		endY = dimIn.endY;
		width = endX - startX;
		height = endY - startY;
		midX = getMidX();
		midY = getMidY();
	}
	
	public EDimension move(int changeX, int changeY) {
		startX += changeX;
		startY += changeY;
		reDimension();
		return this;
	}
	
	public EDimension setPosition(int newX, int newY) {
		startX = newX;
		startY = newY;
		reDimension();
		return this;
	}
	
	public EDimension setWidth(int newWidth) {
		width = newWidth;
		reDimension();
		return this;
	}
	
	public EDimension setHeight(int newHeight) {
		height = newHeight;
		reDimension();
		return this;
	}
	
	private void reDimension() {
		endX = startX + width;
		endY = startY + height;
		midX = getMidX();
		midY = getMidY();
	}
	
	public int getMidX() { return startX + (width / 2); }
	public int getMidY() { return startY + (height / 2); }
	
	public EDimension translateHorizontal(int amount) { startX += amount; return this; }
	public EDimension translateVertical(int amount) { startY += amount; return this; }
	
	public boolean isFullyCovering(EDimension dimIn) { return startX > dimIn.startX && startY > dimIn.startY && endX > dimIn.endX && endY > dimIn.endY; }
	public boolean isGreaterThan(EDimension dimIn) { return startX > dimIn.startX && startY > dimIn.startY && width > dimIn.width && height > dimIn.height; }
	public boolean isLessThan(EDimension dimIn) { return startX < dimIn.startX && startY < dimIn.startY && width < dimIn.width && height < dimIn.height; }
	public boolean isEqualTo(EDimension dimIn) { return startX == dimIn.startX && startY == dimIn.startY && width == dimIn.width && height == dimIn.height; }
	
	@Override public String toString() { return "[startX/Y: " + startX + ", " + startY + "; endX/Y: " + endX + ", " + endY + "; width/Height: " + width + ", " + height + "]"; }
}
