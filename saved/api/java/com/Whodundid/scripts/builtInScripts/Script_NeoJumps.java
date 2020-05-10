/*package com.Whodundid.scripts.builtInScripts;

import com.Whodundid.main.util.playerUtil.PlayerFacing;
import com.Whodundid.main.util.playerUtil.PlayerMovement;
import com.Whodundid.main.util.playerUtil.PlayerFacing.Direction;
import com.Whodundid.scripts.ScriptManager;
import com.Whodundid.scripts.scriptBase.Script;
import com.Whodundid.scripts.scriptUtil.Exceptions.ScriptException.ScriptArgumentException;

public class Script_NeoJumps extends Script {
	
	int line = 0;
	
	public Script_NeoJumps() {
		this.ScriptName = "NeoJump";
	}

	public void startScript(String[] args) throws ScriptArgumentException {
		/*
		PlayerFacing.setFacingDir(PlayerFacing.getCompassFacingDir());
		PlayerMovement.setSprinting();
		PlayerMovement.pressMovementKey(Direction.N);
		
		runner = new Thread() {
			@Override
			public void run() {
				globalKill.set(false);
				long startTime = System.currentTimeMillis();
				
				PlayerFacing.setFacingDir(PlayerFacing.getDegreeFacingDir() + 5);
				PlayerMovement.setJumping();
				
				while (!globalKill.get()) {					
					if (System.currentTimeMillis() - startTime >= 250) {
						if (line == 0) {
							PlayerMovement.pressMovementKey(Direction.W);
							PlayerFacing.setFacingDir(PlayerFacing.getDegreeFacingDir() - 15);
							line++;
						} else if (line == 1) {
							PlayerMovement.unpressMovementKey(Direction.N);
							PlayerMovement.unpressMovementKey(Direction.W);
							PlayerMovement.setJumping(false);
							PlayerMovement.setSprinting(false);
							PlayerFacing.setFacingDir(PlayerFacing.getDegreeFacingDir() + 10);
							globalKill.set(true);
							ScriptManager.removeRunningScript(scriptID);
							break;
						}
						startTime = System.currentTimeMillis();						
					}
				}
			}
		};
		runner.start();
	}
}
*/