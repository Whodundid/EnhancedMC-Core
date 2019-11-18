package com.Whodundid.core.util.storageUtil;

import net.minecraft.util.Vec3;
import net.minecraft.util.Vec3i;

//Last edited: 9-26-18
//First Added: 9-10-18
//Author: Hunter Bragg

/**
 * The {@code Vector3DInt} class is a data type containing three individual {@code Integer} values: x, y, and z.
 * <blockquote><pre>
 *     Vector3DInt vec = new Vector3DInt(x, y, z);
 * </pre></blockquote><p>
 * @author Hunter Bragg
 */
public class Vector3DInt {
	
	public int x = 0, y = 0, z = 0;
	
	/**
	 * Initializes a {@code Vector3DInt} object with default 0 {@code Integer} values.
	 */
	public Vector3DInt() {
		set(0, 0 , 0); 
	}
	
	/**
	 * Initializes a {@code Vector3DInt} object based upon another {@code Vector3DInt} object's values.
	 * @param vecIn {@code Vector3DInt}
	 */
	public Vector3DInt(Vector3DInt vecIn) {
		set(vecIn.x, vecIn.y, vecIn.z); 
	}
	
	/**
	 * Intializes a {@code Vector3DInt} object based upon the values of a given {@code Vector3D} object.
	 * Each {@code Double} value in the given {@code Vector3D} object is floored then converted to an {@code Integer}.
	 * @param vecIn {@code Vector3D}
	 * @see com.Whodundid.core.util.storageUtil.Vector3D
	 * @see java.lang.Math#floor(double)
	 */
	public Vector3DInt(Vector3D vecIn) {
		set(vecIn.x, vecIn.y, vecIn.z);
	}
	
	/**
	 * Intializes a {@code Vector3DInt} object based upon the values of a given {@code Vector3D} object.
	 * Each {@code Double} value in the given {@code Vector3D} object is floored then converted to an {@code Integer}.
	 * @param vecIn {@code Vec3}
	 * @see net.minecraft.util.Vec3
	 * @see java.lang.Math#floor(double)
	 */
	public Vector3DInt(Vec3 vecIn) {
		set((int) vecIn.xCoord, (int) vecIn.yCoord, (int) vecIn.zCoord);
	}
	
	/**
	 * Intializes a {@code Vector3DInt} object based upon the values of a given {@code Vec3i} object.
	 * @param posIn {@code Vec3i}
	 * @see net.minecraft.util.Vec3i
	 */
	public Vector3DInt(Vec3i vecIn) {
		set(vecIn.getX(), vecIn.getY(), vecIn.getZ());
	}
	
	/**
	 * Initializes a {@code Vector3DInt} object with given {@code Double} values.
	 * Each given {@code Double} value is floored then convereted to an {@code Integer}.
	 * @param x {@code Double}
	 * @param y {@code Double}
	 * @param z {@code Double}
	 * @see java.lang.Math#floor(double)
	 */
	public Vector3DInt(double x, double y, double z) {
		set((int) Math.floor(x), (int) Math.floor(y), (int) Math.floor(z));
	}
	
	/**
	 * Initializes a {@code Vector3DInt} object with given {@code Integer} values.
	 * @param x {@code Integer}
	 * @param y {@code Integer}
	 * @param z {@code Integer}
	 */
	public Vector3DInt(int x, int y, int z) {
		set(x, y, z);
	}
	
	
	//full setters
	
	/**
	 * Sets this {@code Vector3DInt} object's values to the given {@code Integer} values.
	 * @param x {@code Integer}
	 * @param y {@code Integer}
	 * @param z {@code Integer}
	 * @return {@code Vector3DInt}
	 */
	public Vector3DInt set(int x, int y, int z) {
		this.x = x;
		this.y = y;
		this.z = z;
		return this;
	}
	
	/**
	 * Sets this {@code Vector3DInt} object's values to the given {@code Float} values.
	 * Each given {@code Float} value is floored then convereted to an {@code Integer}.
	 * @param x {@code Float}
	 * @param y {@code Float}
	 * @param z {@code Float}
	 * @return {@code Vector3DInt}
	 * @see java.lang.Math#floor(double)
	 */
	public Vector3DInt set(float x, float y, float z) {
		this.x = (int) Math.floor(x);
		this.y = (int) Math.floor(y);
		this.z = (int) Math.floor(z);
		return this;
	}
	
	/**
	 * Sets this {@code Vector3DInt} object's values to the given {@code Double} values.
	 * Each given {@code Double} value is floored then convereted to an {@code Integer}.
	 * @param x {@code Double}
	 * @param y {@code Double}
	 * @param z {@code Double}
	 * @return {@code Vector3DInt}
	 * @see java.lang.Math#floor(double)
	 */
	public Vector3DInt set(double x, double y, double z) {
		this.x = (int) Math.floor(x);
		this.y = (int) Math.floor(y);
		this.z = (int) Math.floor(z);
		return this;
	}
	
	/**
	 * Sets this {@code Vector3DInt} object's values equal to a given {@code Vector3DInt} object's values.
	 * @param vecIn {@code Vector3DInt}
	 * @return {@code Vector3DInt}
	 */
	public Vector3DInt set(Vector3DInt vecIn) {
		set(vecIn.x, vecIn.y, vecIn.z);
		return this;
	}
	
	/**
	 * Sets this {@code Vector3DInt} object's values equal to a given {@code Vector3D} object's values.
	 * Each {@code Double} value in the given {@code Vector3D} object is floored then converted to an {@code Integer}.
	 * @param vecIn {@code Vector3DInt}
	 * @return {@code Vector3DInt}
	 * @see com.Whodundid.core.util.storageUtil.Vector3D
	 * @see java.lang.Math#floor(double)
	 */
	public Vector3DInt set(Vector3D vecIn) {
		set((int) vecIn.x, (int) vecIn.y, (int) vecIn.z);
		return this;
	}
	
	/**
	 * Sets this {@code Vector3DInt} object's values equal to a given {@code Vec3} object's values.
	 * Each {@code Double} value in the given {@code Vec3} object is floored then converted to an {@code Integer}.
	 * @param vecIn {@code Vec3}
	 * @return {@code Vector3DInt}
	 * @see net.minecraft.util.Vec3
	 * @see java.lang.Math#floor(double)
	 */
	public Vector3DInt set(Vec3 vecIn) {
		set((int) vecIn.xCoord, (int) vecIn.yCoord, (int) vecIn.zCoord);
		return this;
	}
	
	/**
	 * Sets this {@code Vector3DInt} object's values equal to a given {@code Vec3i} object's values.
	 * Each {@code Double} value in the given {@code Vec3i} object is floored then converted to an {@code Integer}.
	 * @param vecIn {@code Vec3i}
	 * @return {@code Vector3DInt}
	 * @see net.minecraft.util.Vec3i
	 * @see java.lang.Math#floor(double)
	 */
	public Vector3DInt set(Vec3i vecIn) {
		set(vecIn.getX(), vecIn.getY(), vecIn.getZ());
		return this;
	}
	
	
	//getters
	
	/** 
	 * Returns this {@code Vector3DInt} object's x value.
	 * @return {@code Integer}
	 */
	public int getX() {
		return x;
	}
	
	/** 
	 * Returns this {@code Vector3DInt} object's y value.
	 * @return {@code Integer}
	 */
	public int getY() {
		return y;
	}
	
	/** 
	 * Returns this {@code Vector3DInt} object's z value.
	 * @return {@code Integer}
	 */
	public int getZ() {
		return z;
	}
	
	
	//setters
	
	
	/** 
	 * Sets this {@code Vector3DInt} object's x value to the given {@code Integer} value.
	 * @param x {@code Integer}
	 * @return {@code Vector3DInt}
	 */
	public Vector3DInt setX(int x) {
		this.x = x;
		return this;
	}
	
	/** 
	 * Sets this {@code Vector3DInt} object's y value to the given {@code Integer} value.
	 * @param y {@code Integer}
	 * @return {@code Vector3DInt}
	 */
	public Vector3DInt setY(int y) {
		this.y = y;
		return this;
	}
	
	/** 
	 * Sets this {@code Vector3DInt} object's z value to the given {@code Integer} value.
	 * @param z {@code Integer}
	 * @return {@code Vector3DInt}
	 */
	public Vector3DInt setZ(int z) {
		this.z = z;
		return this;
	}
	
	
	//general methods
	
	
	/** 
	 * Sets all stored x, y, and z {@code Integer} values in this {@code Vector3DInt} object to 0.
	 * @return {@code Vector3DInt}
	 */
	public Vector3DInt clear() {
		this.x = 0; this.y = 0; this.z = 0;
		return this;
	}
	
	/** 
	 * Returns a new {@code Vector3D} object based upon the values of this {@code Vector3DInt} object.
	 * @return {@code Vector3D}
	 * @see com.Whodundid.core.util.storageUtil.Vector3D
	 */
	public Vector3D getDoubleVector() {
		return new Vector3D(x, y, z);
	}
	
	/** Compares this {@code Vector3DInt} object's values to another {@code Vector3DInt} object's values. Returns true if each value is equal.
	 * @param vecIn {@code Vector3DInt}
	 * @return {@code Boolean}
	 */
	public boolean compare(Vector3DInt vecIn) {
		return (vecIn != null) ? (x == vecIn.x && y == vecIn.y && z == vecIn.z) : false;
	}
	
	/** 
	 * Compares this {@code Vector3DInt} object's values to each given {@code Integer} value. Returns true if each value is equal.
	 * @param x {@code Integer}
	 * @param y {@code Integer}
	 * @param z {@code Integer}
	 * @return {@code Boolean}
	 */
	public boolean compare(int x, int y, int z) {
		return (this.x == x && this.y == y && this.z == z);
	}
	
	@Override public String toString() { return "[" + x + ", " + y + ", " + z + "]"; }
}
