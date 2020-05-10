package com.Whodundid.worldEditor.Editor3D.renderingUtil;

import com.Whodundid.core.util.storageUtil.Vector3D;

public class Plane {
	
	public Vector3D v1, v2;
	public Vector3D nV;
	public Vector3D p = new Vector3D();
	
	public Plane(DPolygon DP) {
		p.set(DP.x[0], DP.y[0], DP.z[0]);
		v1 = new Vector3D(DP.x[1] - DP.x[0], DP.y[1] - DP.y[0], DP.z[1] - DP.z[0]).normalize();
		v2 = new Vector3D(DP.x[2] - DP.x[0], DP.y[2] - DP.y[0], DP.z[2] - DP.z[0]).normalize();
		nV = v1.crossProduct(v2);
	}
	
	public Plane(Vector3D VE1, Vector3D VE2, Vector3D Z) {
		p = Z;
		v1 = VE1;
		v2 = VE2;
		nV = v1.crossProduct(v2);
	}
}
