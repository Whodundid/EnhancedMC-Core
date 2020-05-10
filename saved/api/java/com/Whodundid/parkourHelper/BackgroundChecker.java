package com.Whodundid.parkourHelper;

import com.Whodundid.core.renderer.BlockDrawer;
import com.Whodundid.core.util.playerUtil.Direction;
import com.Whodundid.core.util.playerUtil.PlayerFacing;
import com.Whodundid.core.util.storageUtil.EArrayList;
import com.Whodundid.core.util.storageUtil.StorageBoxHolder;
import com.Whodundid.core.util.worldUtil.WorldHelper;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.Entity;
import net.minecraft.util.BlockPos;

public class BackgroundChecker {

	public Minecraft mc = Minecraft.getMinecraft();
	ParkourApp mod;
	public volatile boolean running = false;
	public boolean jumpRunning = false;
	double maxHeight = 0;
	double startPos = 0;
	double startRunPos = 0;
	boolean jumped = false;
	boolean landed = false;
	boolean done = false;
	BlockPos prevPos = null;
	Direction prevDir = Direction.OUT;
	
	public BackgroundChecker(ParkourApp modIn) {
		mod = modIn;
	}
	
	public void kill() { running = false; BlockDrawer.clearBlocks(); prevPos = null; prevDir = Direction.OUT; }
	public void start() { if (!running) { running = true; } }
	
	public void checkTick() {
		if (!mod.isEnabled() && running) { kill(); }
		if (mod.isEnabled()) {
			if (running && mc.getRenderViewEntity() != null) {
				jumped = false;
				landed = false;
				
				findPossibleJump();
				if (!done) {
					if (mc.thePlayer.moveForward != 0) { done = true; }
					else { startRunPos = mc.thePlayer.posX; }
				}
				if (!jumped) {
					if (!mc.thePlayer.onGround) { jumped = true; }
					if (PlayerFacing.isXFacing()) { startPos = mc.thePlayer.posX; }
					else { startPos = mc.thePlayer.posZ; }
				} else {
					if (!landed) {
						if (mc.thePlayer.onGround) { landed = true; }
						if (PlayerFacing.isXFacing()) { maxHeight = Math.abs(startPos - mc.thePlayer.posX); }
						else { maxHeight = Math.abs(startPos - mc.thePlayer.posZ); }
					}
				}
				
				if (mod.readyToJump()) {
					if (!jumpRunning) {
						try {
							jumpRunning = true;
							KeyBinding.setKeyBindState(mc.gameSettings.keyBindJump.getKeyCode(), true);
							Thread starter = new Thread() {
								@Override
								public void run() {
									long startTime = System.currentTimeMillis();
									while (jumpRunning) {
										if (System.currentTimeMillis() - startTime >= 250) {
											KeyBinding.setKeyBindState(mc.gameSettings.keyBindJump.getKeyCode(), false);
											jumpRunning = false;
											break;
										}
									}
								}
							};
							starter.start();
						} catch (Exception q) { q.printStackTrace(); }
					}
				}
			}
		}
		maxHeight = 0;
	}
	
	public void findPossibleJump() {
		try {
			if (!isAirBeneath() && mc.thePlayer.onGround) {
				
				double pos1 = 0;
				boolean goNegative = false;
				boolean goX = false;
				
				switch (PlayerFacing.getCompassFacingDir()) {
				case N: goNegative = true;
				case S: pos1 = mc.getRenderViewEntity().posZ; break;
				case W: goNegative = true; goX = true;
				case E: pos1 = mc.getRenderViewEntity().posX; goX = true; break;
				default: break;
				}
				
				BlockPos startPos;
				Entity p = mc.thePlayer;
				if (goX) { startPos = new BlockPos(pos1, p.getEntityBoundingBox().minY + 3, p.posZ); }
				else { startPos = new BlockPos(p.posX, p.getEntityBoundingBox().minY + 3, pos1); }
				
				//if (prevPos == null) { prevPos = new BlockPos(startPos); }
				//if (prevDir == Direction.OUT) { prevDir = PlayerFacing.getCompassFacingDir(); }
				
				if ((prevPos == null || prevDir == Direction.OUT) || (startPos.compareTo(prevPos) != 0 || prevDir != PlayerFacing.getCompassFacingDir())) {
					prevPos = startPos;
					prevDir = PlayerFacing.getCompassFacingDir();
					BlockDrawer.clearBlocks();
					
					StorageBoxHolder<Integer, EArrayList<BlockPos>> ahead = new StorageBoxHolder();
					
					//region height
					for (int i = 0; i < 6; i++) {
						//region length
						EArrayList<BlockPos> row = new EArrayList();
						for (int j = 0; j < 10; j++) {
							BlockPos bPos;
							if (goX) { bPos = new BlockPos(pos1 + (goNegative ? -j : j), startPos.getY() - i, p.posZ); }
							else { bPos = new BlockPos(p.posX, startPos.getY() - i, pos1 + (goNegative ? -j : j)); }
							//BlockDrawer.addBlock(bPos, 0xff2b2b2b);
							row.add(bPos);
						}
						ahead.add(i, row);
					}
					
					if (ahead.size() > 0) {
						mod.drawer.jumpType = String.valueOf(JumpIdentifyer.determineTypeOfJump(ahead));
					}
				}
			}
		} catch (NullPointerException e) {
			e.printStackTrace();
			kill();
		}			
	}
	
	public boolean isAirBeneath() {
		try {
			BlockPos oneBelow = new BlockPos(mc.getRenderViewEntity().posX, mc.getRenderViewEntity().getEntityBoundingBox().minY - 1, mc.getRenderViewEntity().posZ);
			Block blocky = Minecraft.getMinecraft().theWorld.getBlockState(oneBelow).getBlock();
			int id = WorldHelper.getBlockID(blocky);
			if (id == 0 || id == 44 || id == 126 || id == 182) { return true; }
		} catch (Exception e) {
			e.printStackTrace();
			kill();
		}
		return false;
	}
}
