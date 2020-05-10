package com.Whodundid.worldEditor.Editor3D.renderingUtil;

import com.Whodundid.core.util.storageUtil.Vector3D;
import com.Whodundid.worldEditor.EditorApp;

public class Calculator {
	public static double t = 0;
	static Vector3D w1, w2;
	static Plane p;
	public static double[] fovFocus = new double[2];
	
	public static void SetPrederterminedInfo(EditorApp editor) {
		Vector3D camPos = editor.camPos;
		Vector3D camLook = editor.camLook;
		Vector3D viewVec = Vector3D.difference(camLook, camPos);		
		Vector3D dirVec = new Vector3D(1, 1, 1);
		Vector3D pVec1 = viewVec.crossProduct(dirVec);
		Vector3D pVec2 = viewVec.crossProduct(pVec1);
		p = new Plane(pVec1, pVec2, camLook);

		Vector3D rotationVector = Calculator.getRotationVector(camPos, camLook);
		w1 = viewVec.crossProduct(rotationVector);
		w2 = viewVec.crossProduct(w1);
		
		fovFocus = Calculator.CalculatePositionP(camPos, camLook);
		
		fovFocus[0] = editor.fieldOfView * fovFocus[0] - ((editor.render3DHighRes) ? 453 / 2 : 0);
		fovFocus[1] = editor.fieldOfView * fovFocus[1] - ((editor.render3DHighRes) ? 453 / 2 : 0);
	}
	
	public static Vector3D getProj(Vector3D camPos, Vector3D camLook, Plane P) {
		Vector3D viewToPoint = Vector3D.difference(camLook, camPos).normalize();
		t = (Vector3D.multiplyAndAdd(P.nV, P.p) - Vector3D.multiplyAndAdd(P.nV, camPos)) / Vector3D.multiplyAndAdd(P.nV, viewToPoint);
		return camLook.set(camPos.x + viewToPoint.x * t, camPos.y + viewToPoint.y * t, camPos.z + viewToPoint.z * t);
	}
	
	public static double[] CalculatePositionP(Vector3D camPos, Vector3D camTo) {		
		Vector3D projP = getProj(camPos, camTo, p);
		return new double[] {Vector3D.multiplyAndAdd(w2, projP), Vector3D.multiplyAndAdd(w1, projP)};
	}
	
	private static Vector3D getRotationVector(Vector3D camPos, Vector3D camLook) {
		double dx = Math.abs(camPos.x - camLook.x);
		double dy = Math.abs(camPos.y - camLook.y);
		double xRot, yRot;
		xRot = dy / (dx + dy);		
		yRot = dx / (dx + dy);
		if (camPos.y > camLook.y) { xRot = -xRot; }
		if (camPos.x < camLook.x) { yRot = -yRot; }
		return new Vector3D(xRot, yRot, 0).normalize();
	}
}
