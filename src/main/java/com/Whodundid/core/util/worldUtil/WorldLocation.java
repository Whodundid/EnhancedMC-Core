package com.Whodundid.core.util.worldUtil;

import com.Whodundid.core.util.storageUtil.Vector3D;

//Last edited: Jan 23, 2019
//First Added: Jan 23, 2019
//Author: Hunter Bragg

public class WorldLocation {
	
	Vector3D position;
	double pitch;
	double yaw;
	
	public WorldLocation(Vector3D posIn, double pitchIn, double yawIn) {
		position = posIn;
		pitch = pitchIn;
		yaw = yawIn;
	}
	
	public Vector3D getPosition() { return position; }
	public double getPitch() { return pitch; }
	public double getYaw() { return yaw; }
	
	public WorldLocation setPosition(Vector3D posIn) { position = posIn; return this; }
	public WorldLocation setPitch(double pitchIn) { pitch = pitchIn; return this; }
	public WorldLocation setYaw(double yawIn) { yaw = yawIn; return this; }
}
