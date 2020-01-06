package com.Whodundid.core.util.playerUtil;

import com.Whodundid.core.util.storageUtil.Vector3D;
import com.google.common.base.Predicate;
import com.google.common.base.Predicates;
import java.util.List;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityOtherPlayerMP;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EntitySelectors;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import net.minecraftforge.fml.common.gameevent.TickEvent;

//Author: Hunter Bragg

/**
 * PlayerFacing contains methods for determining the player's current facing
 * direction and methods for checking if the player is facing a type of entity
 * or block. PlayerFacing is also used to set the players facing direction both
 * instantaneously and gradually. PlayerFacing
 */
public final class PlayerFacing {

	static Minecraft mc = Minecraft.getMinecraft();
	public static Vector3D playerEyeVec3;
	public static MovingObjectPosition rtResult;
	private static Entity hitEntity = null;

	/**
	 * Gets the cardinal or ordinal direction the player is facing.
	 * 
	 * @return PlayerFacing.Direction
	 */
	public static Direction getCompassFacingDir() {
		Direction direction = Direction.OUT;
		if (mc.thePlayer != null) {
			float rotation = (mc.thePlayer.rotationYaw - 90) % 360;
			try {
				if (rotation < 0) { rotation += 360.0; }

				if (0 <= rotation && rotation < 22.5) { direction = Direction.W; }
				else if (22.5 <= rotation && rotation < 67.5) { direction = Direction.NW; }
				else if (67.5 <= rotation && rotation < 112.5) { direction = Direction.N; }
				else if (112.5 <= rotation && rotation < 157.5) { direction = Direction.NE; }
				else if (157.5 <= rotation && rotation < 202.5) { direction = Direction.E; }
				else if (202.5 <= rotation && rotation < 247.5) { direction = Direction.SE; }
				else if (247.5 <= rotation && rotation < 292.5) { direction = Direction.S; }
				else if (292.5 <= rotation && rotation < 337.5) { direction = Direction.SW; }
				else if (337.5 <= rotation && rotation < 360.0) { direction = Direction.W; }
				else { System.out.println("NoWhere: " + rotation); }
				return direction;
				
			} catch (Exception e) { e.printStackTrace(); }
		}
		return direction;
	}

	/**
	 * Gets the exact degree the player is facing.
	 * 
	 * @return float
	 */
	public static float getDegreeFacingDir() {
		return mc.thePlayer != null ? mc.thePlayer.rotationYaw : -1f;
	}

	/**
	 * Returns true if the player is facing along the x axis.
	 * 
	 * @return boolean
	 */
	public static boolean isXFacing() {
		if (mc.thePlayer != null) {
			float rotation = (mc.thePlayer.rotationYaw - 90) % 360;
			if (rotation < 0) { rotation += 360.0; }
			if (0 <= rotation && rotation < 45.0) { return true; }
			else if (135.0 <= rotation && rotation < 225) { return true; }
			else if (315.0 <= rotation && rotation < 360.0) { return true; }
		}
		return false;
	}

	/**
	 * Returns true if the player is facing a positive x or z direction.
	 * 
	 * @return boolean
	 */
	public static boolean isPositiveXZFacing() {
		if (mc.thePlayer != null) {
			float rotation = (mc.thePlayer.rotationYaw - 90) % 360;
			if (rotation < 0) {
				rotation += 360.0;
			}
			if (rotation >= 135.0 && rotation <= 315.0) {
				return true;
			}
		}
		return false;
	}

	public static void checkEyePosition(TickEvent.RenderTickEvent e) {
		if (mc.thePlayer != null) {
			playerEyeVec3 = new Vector3D(mc.thePlayer.getPositionEyes(e.renderTickTime));
		}
	}

	private static void updateRayTrace() {
		float partialTicks = 1.0f;
		double range = 75;

		if (mc.thePlayer != null) {
			if (mc.theWorld != null) {
				rtResult = mc.thePlayer.rayTrace(range, partialTicks);
				Vec3 vec3 = new Vec3(playerEyeVec3.x, playerEyeVec3.y, playerEyeVec3.z);
				boolean flag = false;
				int i = 3;

				Vec3 vec31 = mc.thePlayer.getLook(partialTicks);
				Vec3 vec32 = vec3.addVector(vec31.xCoord * range, vec31.yCoord * range, vec31.zCoord * range);
				hitEntity = null;
				Vec3 vec33 = null;
				float f = 1.0F;
				List<Entity> list = mc.theWorld.getEntitiesInAABBexcluding(mc.thePlayer, mc.thePlayer.getEntityBoundingBox()
									.addCoord(vec31.xCoord * range, vec31.yCoord * range, vec31.zCoord * range).expand(f, f, f),
									Predicates.and(EntitySelectors.NOT_SPECTATING, new Predicate<Entity>() {
										public boolean apply(Entity p_apply_1_) {
											return p_apply_1_.canBeCollidedWith();
										}
									}
				));

				for (int j = 0; j < list.size(); ++j) {
					Entity entity1 = list.get(j);
					float f1 = entity1.getCollisionBorderSize();
					AxisAlignedBB axisalignedbb = entity1.getEntityBoundingBox().expand(f1, f1, f1);
					MovingObjectPosition movingobjectposition = axisalignedbb.calculateIntercept(vec3, vec32);

					if (axisalignedbb.isVecInside(vec3)) {
						if (range >= 0.0D) {
							hitEntity = entity1;
							vec33 = movingobjectposition == null ? vec3 : movingobjectposition.hitVec;
							range = 0.0D;
						}
					} else if (movingobjectposition != null) {
						double d3 = vec3.distanceTo(movingobjectposition.hitVec);

						if (d3 < range || range == 0.0D) {
							if (entity1 == mc.thePlayer.ridingEntity && !mc.thePlayer.canRiderInteract()) {
								if (range == 0.0D) {
									hitEntity = entity1;
									vec33 = movingobjectposition.hitVec;
								}
							} else {
								hitEntity = entity1;
								vec33 = movingobjectposition.hitVec;
								range = d3;
							}
						}
					}
				}

				if (hitEntity != null && flag && vec3.distanceTo(vec33) > 3.0D) {
					hitEntity = null;
					rtResult = new MovingObjectPosition(MovingObjectPosition.MovingObjectType.MISS, vec33, null,
							new BlockPos(vec33));
				}

				rtResult = hitEntity != null ? new MovingObjectPosition(hitEntity, vec33) : rtResult;
			}
		}
	}

	/**
	 * Returns true if the player is currently facing another player in the world.
	 * 
	 * @return boolean
	 */
	public static boolean isFacingPlayer() {
		updateRayTrace();
		if (rtResult != null) {
			//if (rtResult.getBlockPos() != null) {
			//	BlockDrawer.clearBlocks();
			//	BlockDrawer.addBlock(rtResult.getBlockPos(), 0xffff5555);
			//}
			if (hitEntity != null) {
				return hitEntity instanceof EntityPlayerMP || hitEntity instanceof EntityOtherPlayerMP || hitEntity instanceof EntityPlayerSP;
			}
		}
		return false;
	}

	/**
	 * Returns the name of the player that is currently being faced.
	 * 
	 * @return boolean
	 */
	public static String getFacingPlayerName() {
		if (hitEntity != null) {
			if (hitEntity instanceof EntityPlayerMP) {
				EntityPlayerMP facingPlayer = (EntityPlayerMP) hitEntity;
				return facingPlayer.getDisplayNameString();
			}
			else if (hitEntity instanceof EntityOtherPlayerMP) {
				EntityOtherPlayerMP facingPlayer = (EntityOtherPlayerMP) hitEntity;
				return facingPlayer.getDisplayNameString();
			}
			else if (hitEntity instanceof EntityPlayerSP) {
				EntityPlayerSP facingPlayer = (EntityPlayerSP) hitEntity;
				return facingPlayer.getDisplayNameString();
			}
		}
		return null;
	}

	/**
	 * Returns the Minecraft.Block object the player is currently facing.
	 * 
	 * @return Minecraft.Block
	 */
	public static Block getFacingBlock() {
		try {
			MovingObjectPosition mop = mc.thePlayer.rayTrace(200, 1.0F);
			if (mop != null) {
				Block blocky = mc.theWorld.getBlockState(mop.getBlockPos()).getBlock();
				return blocky;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * Returns the Minecraft.BlockPos object of the block the player is currently
	 * facing.
	 * 
	 * @return Minecraft.BlockPos
	 */
	public static BlockPos getFacingBlockPos() {
		try {
			MovingObjectPosition mop = Minecraft.getMinecraft().getRenderViewEntity().rayTrace(200, 1.0F);
			if (mop != null) {
				return mop.getBlockPos();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * Returns the base id of the block the player is currently facing.
	 * 
	 * @return integer
	 */
	public static int getFacingBlockId() {
		Block getBlock = getFacingBlock();
		if (getBlock != null) {
			return Block.getIdFromBlock(getBlock);
		}
		return -1;
	}

	/**
	 * Returns the meta data value of the block the player is currently facing.
	 * 
	 * @return integer
	 */
	public static int getFacingBlockMetaData() {
		BlockPos getPos = getFacingBlockPos();
		if (getPos != null) {
			if (mc.theWorld != null) {
				IBlockState state = mc.theWorld.getBlockState(getPos);
				Block getBlock = getFacingBlock();
				if (getBlock != null) {
					return getFacingBlock().getMetaFromState(state);
				}
			}
		}
		return -1;
	}

	/**
	 * Sets the players facing direction instantaneously.
	 * 
	 * @param PlayerFacing.Direction
	 */
	public static void setFacingDir(Direction dir) {
		setFacingDir(dir.getDegree());
	}

	/**
	 * Sets the players facing direction instantaneously.
	 * 
	 * @param float
	 */
	public static void setFacingDir(double dir) {
		//try {
		//	mc.getRenderViewEntity().rotationYaw = (float) dir;
		//} catch (Exception e) {
		//	e.printStackTrace();
		//}
		
		 if (mc.thePlayer != null) { double curRot = mc.thePlayer.rotationYaw; double
		 rotMod = curRot % 360; double diff = 0; dir = dir % 360;
		 boolean toZero = Math.abs(rotMod) - 180 < 0;
		 boolean isFullRot = (dir == 0 || dir == 360);
		 
		 if (rotMod < 0) { rotMod += 360; diff = isFullRot ? (toZero ? (360 - rotMod)
		 : -rotMod) : dir - rotMod; } else { diff = isFullRot ? (toZero ? -rotMod :
		 (360 - rotMod)) : dir - rotMod; }
		 
		 curRot += diff;
		 
		 System.out.println("diff: " + diff + " ; " + dir + " ; " + rotMod + " ; " +
		 isFullRot + " " + toZero); mc.thePlayer.rotationYaw = (float) curRot; }
		 
	}

	public static void graduallyFaceDir(Direction dir, int speed) {
		graduallyFaceDir(dir.getDegree(), speed);
	}

	public static void graduallyFaceDir(float dir, int speed) {
		// unfinished
	}
}