package com.Whodundid.core.util.storageUtil;

import net.minecraft.util.Vec3;
import net.minecraft.util.Vec3i;

//Last edited: 9-26-18
//First Added: 9-10-18
//Author: Hunter Bragg

/**
 * The {@code Vector3D} class is a data type containing three individual {@code Double} values: x, y, and z.
 * The {@code Vector3D} class provides numerous functions for performing vector math both locally and statically.
 * <blockquote><pre>
 *     Vector3D vec = new Vector3D(x, y, z);
 * </pre></blockquote><p>
 * @author Hunter Bragg
 */
public class Vector3D {

	public double x = 0, y = 0, z = 0;
	
	/** 
	 * Initializes a {@code Vector3D} object with default 0 {@code Double} values.
	 */
	public Vector3D() {
		set(0, 0 , 0);
	}
	
	/** 
	 * Initializes a {@code Vector3D} object based upon another {@code Vector3D} object's values.
	 * @param vecIn {@code Vector3D}
	 */
	public Vector3D(Vector3D vecIn) {
		set(vecIn.x, vecIn.y, vecIn.z);
	}
	
	/** 
	 * Initializes a Vecotr3D object based upon a {@code Vector3DInt} objects's values.
	 * @param vecIn {@code Vector3DInt}
	 * @see com.Whodundid.core.util.storageUtil.Vector3DInt
	 */
	public Vector3D(Vector3DInt vecIn) {
		set(vecIn.x, vecIn.y, vecIn.z);
	}
	
	/** 
	 * Initializes a Vector3D object based upon a Vec3 object's values.
	 * @param vecIn {@code Vec3}
	 * @see net.minecraft.util.Vec3
	 */
	public Vector3D(Vec3 vecIn) {
		set(vecIn.xCoord, vecIn.yCoord, vecIn.zCoord);
	}
	
	/**
	 * Initializes a Vector3D object based upon a {@code Vec3i} object's values.
	 * @param posIn {@code Vec3i}
	 * @see net.minecraft.util.Vec3i
	 */
	public Vector3D(Vec3i vecIn) {
		set(vecIn.getX(), vecIn.getY(), vecIn.getZ());
	}
	
	/**
	 * Initializes a Vector3D object with given {@code Double} values.
	 * @param x {@code Double}
	 * @param y {@code Double}
	 * @param z {@code Double}
	 */
	public Vector3D(double x, double y, double z) {
		set(x, y, z);
	}
	
	
	//full setters
	
	/** 
	 * Sets this {@code Vector3D} object's values equal to that of another {@code Vector3D} object's values.
	 * @param vecIn {@code Vector3D}
	 * @return {@code Vector3D}
	 */
	public Vector3D set(Vector3D vecIn) {
		set(vecIn.x, vecIn.y, vecIn.z);
		return this; 
	}
	
	/** 
	 * Sets this {@code Vector3D} object's values equal to that of a {@code Vector3DInt} object's values.
	 * @param vecIn {@code Vector3DInt}
	 * @return {@code Vector3D}
	 * @see com.Whodundid.core.util.storageUtil.Vector3DInt
	 */
	public Vector3D set(Vector3DInt vecIn) {
		set(vecIn.x, vecIn.y, vecIn.z);
		return this; 
	}
	
	/** 
	 * Sets this {@code Vector3D} object's values equal to that of a given {@code Vec3} object's values.
	 * @param vecIn {@code Vec3}
	 * @return {@code Vector3D}
	 * @see net.minecraft.util.Vec3
	 */
	public Vector3D set(Vec3 vecIn) {
		set(vecIn.xCoord, vecIn.yCoord, vecIn.zCoord);
		return this;
	}
	
	/** 
	 * Sets this {@code Vector3D} object's values equal to that of given {@code Vec3i} object's values.
	 * @param vecIn {@code Vec3i}
	 * @return {@code Vector3D}
	 * @see net.minecraft.util.Vec3i
	 */
	public Vector3D set(Vec3i vecIn) {
		set(vecIn.getX(), vecIn.getY(), vecIn.getZ());
		return this;
	}
	
	/** 
	 * Sets this {@code Vector3D} object's values equal to that of each given {@code Double} value.
	 * @param x {@code Double}
	 * @param y {@code Double}
	 * @param z {@code Double}
	 * @return {@code Vector3D}
	 */
	public Vector3D set(double x, double y, double z) {
		this.x = x; this.y = y; this.z = z;
		return this;
	}
	
	
	//getters
	
	/** 
	 * Returns this {@code Vector3D} object's x value.
	 * @return {@code Double}
	 */
	public double getX() {
		return x;
	}
	
	/** 
	 * Returns this {@code Vector3D} object's y value.
	 * @return {@code Double}
	 */
	public double getY() {
		return y;
	}
	
	/** 
	 * Returns this {@code Vector3D} object's z value.
	 * @return {@code Double}
	 */
	public double getZ() {
		return z;
	}
	
	
	//setters
	
	/** 
	 * Sets this {@code Vector3D} object's x value to the given {@code Double} value.
	 * @param x {@code Double}
	 * @return {@code Vector3D}
	 */
	public Vector3D setX(double x) {
		this.x = x;
		return this;
	}
	
	/** 
	 * Sets this {@code Vector3D} object's y value to the given {@code Double} value.
	 * @param y {@code Double}
	 * @return {@code Vector3D}
	 */
	public Vector3D setY(double y) {
		this.y = y;
		return this;
	}
	
	/** 
	 * Sets this {@code Vector3D} object's z value to the given {@code Double} value.
	 * @param z {@code Double}
	 * @return {@code Vector3D}
	 */
	public Vector3D setZ(double z) {
		this.z = z;
		return this;
	}
	
	
	//general methods
	
	/** 
	 * Sets all stored x, y, and z {@code Double} values in this {@code Vector3D} object to 0.
	 * @return {@code Vector3D}
	 */
	public Vector3D clear() {
		x = 0;
		y = 0;
		z = 0;
		return this;
	}
	
	/** 
	 * Performs {@code floor} on each value in this {@code Vector3D} Object.
	 * @return {@code Vector3D}
	 * @see java.lang.Math#floor(double)
	 */
	public Vector3D floor() {
		set(Math.floor(x), Math.floor(y), Math.floor(z));
		return this;
	}
	
	/** 
	 * Performs {@code ceil} on each value in this {@code Vector3D} Object.
	 * @return {@code Vector3D}
	 * @see java.lang.Math#ceil(double)
	 */
	public Vector3D ceil() {
		set(Math.ceil(x), Math.ceil(y), Math.ceil(z));
		return this;
	}
	
	/** 
	 * Returns a new {@code Vector3DInt} object based upon the values of this {@code Vector3D} object.
	 * Data loss will occur during conversion of {@code Double} to {@code Integer}!
	 * @return {@code Vector3DInt}
	 * @see com.Whodundid.core.util.storageUtil.Vector3DInt
	 */
	public Vector3DInt getIntVector() {
		return new Vector3DInt(x, y, z);
	}
	
	/** Compares this {@code Vector3D} object's values to another {@code Vector3D} object's values. Returns true if each value is equal.
	 * Due to the nature of {@code Double} floating point precision, this method may prove unreliable.
	 * @param vecIn {@code Vector3D}
	 * @return {@code Boolean}
	 */
	public boolean compare(Vector3D vecIn) {
		return (vecIn != null) ? compare(vecIn.x, vecIn.y, vecIn.z) : false;
	}
	
	/** 
	 * Compares this {@code Vector3D} object's values to each given {@code Double} value. Returns true if each value is equal.
	 * Due to the nature of {@code Double} floating point precision, this method may prove unreliable.
	 * @param x {@code Double}
	 * @param y {@code Double}
	 * @param z {@code Double}
	 * @return {@code Boolean}
	 */
	public boolean compare(double x, double y, double z) {
		return (this.x == x && this.y == y && this.z == z);
	}
	
	/** 
	 * Compares this {@code Vector3D} object's x value to the given {@code Double} x value. Returns true if each value is equal.
	 * Due to the nature of {@code Double} floating point precision, this method may prove unreliable.
	 * @param xIn {@code Double}
	 * @return {@code Boolean}
	 */
	public boolean compareX(double xIn) {
		return this.x == xIn;
	}
	
	/** 
	 * Compares this {@code Vector3D} object's y value to the given {@code Double} y value. Returns true if each value is equal.
	 * Due to the nature of {@code Double} floating point precision, this method may prove unreliable.
	 * @param yIn {@code Double}
	 * @return {@code Boolean}
	 */
	public boolean compareY(double yIn) {
		return this.y == yIn;
	}
	
	/** Compares this {@code Vector3D} object's z value to the given {@code Double} z value. Returns true if each value is equal.
	 * Due to the nature of {@code Double} floating point precision, this method may prove unreliable.
	 * @param zIn {@code Double}
	 * @return {@code Boolean}
	 */
	public boolean compareZ(double zIn) {
		return this.z == zIn;
	}
	
	
	//local vector math
	
	/** 
	 * Return the magnitude of this {@code Vector3D}.
	 * @return {@code Double}
	 */
	public double magnitude() {
		return Math.sqrt(x * x + y * y + z * z);
	}
	
	/** 
	 * Return the dot product of this {@code Vector3D} against another {@code Vector3D} object.
	 * @param vecIn {@code Vector3D}
	 * @return {@code Double}
	 */
	public double dotProduct(Vector3D vecIn) {
		return Math.acos(multiplyAndAdd(this, vecIn) / magnitude(vecIn));
	}
	
	/** 
	 * Multiplies each value in this {@code Vector3D} object against each value of another {@code Vector3D}
	 * object and finally adds the multiplied result of each x, y, and z {@code Double} value together.
	 * @param vecIn {@code Vector3D}
	 * @return {@code Double}
	 */
	public double multiplyAndAdd(Vector3D vecIn) {
		return (x * vecIn.x + y * vecIn.y + z * vecIn.z);
	}
	
	/** 
	 * Multiplies each value in this {@code Vector3D} object by the given {@code Double} value.
	 * @param val {@code Double}
	 * @return {@code Vector3D}
	 */
	public Vector3D scale(double val) {
		x *= val; y *= val; z *= val;
		return this; 
	}
	
	/** 
	 * Returns a new {@code Vector3D} containing the crossProduct of this {@code Vector3D} against another {@code Vector3D} object.
	 * @param vecIn {@code Vector3D}
	 * @return {@code Vector3D}
	 */
	public Vector3D crossProduct(Vector3D vecIn) {
		return new Vector3D(y * vecIn.z - z * vecIn.y, z * vecIn.x - x * vecIn.z, x * vecIn.y - y * vecIn.x);
	}
	
	/**
	 * Returns a new {@code Vector3D} containing the difference of this {@code Vector3D} against another {@code Vector3D} object.
	 * @param vecIn {@code Vector3D}
	 * @return {@code Vector3D}
	 */
	public Vector3D difference(Vector3D vecIn) {
		return new Vector3D(x - vecIn.x, y - vecIn.y, z - vecIn.z);
	}
	
	/**
	 * Normalizes the values of this {@code Vector3D} by this {@code Vector3D}'s magnitude.
	 * @return {@code Vector3D}
	 * @see com.Whodundid.core.util.storageUtil.Vector3D#magnitude()
	 */
	public Vector3D normalize() {
		double length = magnitude();
		if (length > 0) { x /= length; y /= length; z /= length; }
		return this;
	}
	
	
	//static vector math
	
	/**
	 * Returns the magnitude of a given {@code Vector3D} object.
	 * @param vecIn {@code Vector3D}
	 * @return {@code Double}
	 */
	public static double magnitude(Vector3D vecIn) {
		return Math.sqrt(vecIn.x * vecIn.x + vecIn.y * vecIn.y + vecIn.z * vecIn.z);
	}
	
	/**
	 * Returns the dot product of two given {@code Vector3D} objects.
	 * @param vec1 {@code Vector3D}
	 * @param vec2 {@code Vector3D}
	 * @return {@code Double}
	 */
	public static double dotProduct(Vector3D vec1, Vector3D vec2) {
		return Math.acos(multiplyAndAdd(vec1, vec2) / magnitude(vec2));
	}
	
	/**
	 * Returns the value of each given {@code Vector3D}'s value's multiplied against each other, then
	 * finally adds the multiplied results of each {@code Vector3D}'s x, y, and z {@code Double} values together.
	 * @param vec1 {@code Vector3D}
	 * @param vec2 {@code Vector3D}
	 * @return {@code Double}
	 */
	public static double multiplyAndAdd(Vector3D vec1, Vector3D vec2) {
		return (vec1.x * vec2.x + vec1.y * vec2.y + vec1.z * vec2.z);
	}
	
	/**
	 * Multiplies each value of a given {@code Vector3D} object by a given {@code Double} value.
	 * @param vecIn {@code Vector3D}
	 * @param val {@code Double}
	 * @return {@code Vector3D}
	 */
	public static Vector3D scale(Vector3D vecIn, double val) {
		vecIn.x *= val; vecIn.y *= val; vecIn.z *= val;
		return vecIn;
	}
	
	/**
	 * Returns a new {@code Vector3D} object containing the cross product of two given {@code Vector3D} objects.
	 * @param vec1 {@code Vector3D}
	 * @param vec2 {@code Vector3D}
	 * @return {@code Vector3D}
	 */
	public static Vector3D crossProduct(Vector3D vec1, Vector3D vec2) {
		return new Vector3D(vec1.y * vec2.z - vec1.z * vec2.y, vec1.z * vec2.x - vec1.x * vec2.z, vec1.x * vec2.y - vec1.y * vec2.x);
	}
	
	/**
	 * Returns a new {@code Vector3D} object containing the difference of two given {@code Vector3D} objects.
	 * @param vec1 {@code Vector3D}
	 * @param vec2 {@code Vector3D}
	 * @return {@code Vector3D}
	 */
	public static Vector3D difference(Vector3D vec1, Vector3D vec2) {
		return new Vector3D(vec1.x - vec2.x, vec1.y - vec2.y, vec1.z - vec2.z);
	}
	
	/**
	 * Normalizes the values of the given {@code Vector3D} by its own magnitude.
	 * @param vecIn {@code Vector3D}
	 * @return {@code Vector3D}
	 */
	public static Vector3D normalize(Vector3D vecIn) {
		double length = magnitude(vecIn);
		if (length > 0) { vecIn.x /= length; vecIn.y /= length; vecIn.z /= length; }
		return vecIn;
	}
	
	@Override public String toString() { return "[" + x + ", " + y + ", " + z + "]"; }
}
