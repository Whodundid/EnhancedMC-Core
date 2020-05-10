package com.Whodundid.scripts.scriptUtil.scriptObjects;

import java.util.ArrayList;
import com.Whodundid.core.util.playerUtil.Direction;
import com.Whodundid.core.util.playerUtil.PlayerFacing;
import com.Whodundid.core.util.playerUtil.PlayerMovement;
import com.Whodundid.core.util.storageUtil.Vector3D;

public class ScriptAction extends ScriptObject {
	
	public enum ScriptActionType { PLAYERACTION, WORLDACTION, DELAY, DEFINEVAR, UPDATEVAR, PRINTVAR, END, NONE; }
	public enum PlayerActionType { PRESSKEY, UNPRESSKEY, STOPMOVING, LOOK, GRADLOOK, JUMP, SNEAK, PUNCH, NONE; }
	public enum WorldActionType { GETBLOCKAT, NONE; }
	public enum VarTypes { INT, DOUBLE, FLOAT, STRING, NONE }
	
	private ScriptActionType sType = ScriptActionType.NONE;
	private PlayerActionType pType = PlayerActionType.NONE;
	private WorldActionType wType = WorldActionType.NONE;
	private boolean value = false;
	private Direction dir = Direction.OUT;
	private float degreeDir = 0f;
	private int lookSpeed = 1;
	private Vector3D location = new Vector3D();
	private long delayTime = 0;
	public boolean done = false;
	public boolean ended = false;
	
	private ArrayList<VarTypes> vars = new ArrayList();
	
	public ScriptAction(ScriptActionType typeIn) { sType = typeIn; }
	public ScriptAction(PlayerActionType typeIn, boolean valueIn) { this(typeIn, valueIn, 0, 0); }
	public ScriptAction(PlayerActionType typeIn, Direction dirIn) { this(typeIn, false, dirIn.getDegree(), 0); }
	public ScriptAction(PlayerActionType typeIn, float degreeDirIn) { this(typeIn, false, degreeDirIn, 0); }
	public ScriptAction(PlayerActionType typeIn, boolean valueIn, float degreeDirIn, int lookSpeedIn) {
		sType = ScriptActionType.PLAYERACTION;
		pType = typeIn;
		value = valueIn;
		degreeDir = degreeDirIn;
		lookSpeed = lookSpeedIn;
	}
	
	public String getActionType() { return sType.toString(); }
	
	public void performAction() {
		try {
			ended = sType == ScriptActionType.END;
			if (sType == ScriptActionType.PLAYERACTION) {
				switch (pType) {
				case LOOK: PlayerFacing.setFacingDir(degreeDir); done = true; break;
				//case GRADLOOK: PlayerFacing.graduallyFaceDir(degreeDir, lookSpeed); break;
				case PRESSKEY: PlayerMovement.pressMovementKey(dir); done = true; break;
				case UNPRESSKEY: PlayerMovement.unpressMovementKey(dir); done = true; break;
				case JUMP: PlayerMovement.setJumping(value); done = true; break;
				case SNEAK: PlayerMovement.setSneaking(value); done = true; break;
				case STOPMOVING: PlayerMovement.stopMovement(); done = true; break;
				//case PUNCH: break;
				default: break;
				}
			} else if (sType == ScriptActionType.WORLDACTION) {
				switch (wType) {
				case GETBLOCKAT: break;
				default: break;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}		
	}
}
