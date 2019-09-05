package com.Whodundid.core.util.playerUtil;

import com.Whodundid.core.util.storageUtil.Vector3D;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MovingObjectPosition;
import net.minecraftforge.fml.common.gameevent.TickEvent;

/**
 * PlayerFacing contains methods for determining the player's current facing direction and
 * methods for checking if the player is facing a type of entity or block. PlayerFacing is
 * also used to set the players facing direction both instantaneously and gradually.
 * PlayerFacing 
 *
 * Created by Hunter Bragg on 5/20/2017.
 */
public abstract class PlayerFacing {

	static Minecraft mc = Minecraft.getMinecraft();
	public static Vector3D playerEyeVec3;
	
	/**
	 * Gets the cardinal or ordinal direction the player is facing.
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
		        else { System.out.println("NoWhere"); }
		        return direction;
			} catch (Exception e) {
				e.printStackTrace();
			}
		}		
		return direction;
	}
	
	/**
	 * Gets the exact degree the player is facing in from 0 to 360.
	 * @return float
	 */
	public static float getDegreeFacingDir() {
		if (mc.thePlayer != null) {
			float rotation = (mc.thePlayer.rotationYaw) % 360;
			if (rotation < 0) {
	            rotation += 360.0;
	        }
			return rotation;
		}
		return -1f;
	}
	
	/**
	 * Returns true if the player is facing along the x axis.
	 * @return boolean
	 */
	public static boolean isXFacing() {
		if (mc.thePlayer != null) {
			float rotation = (mc.thePlayer.rotationYaw - 90) % 360;
			if (rotation < 0) {
	            rotation += 360.0;
	        }
			if (0 <= rotation && rotation < 45.0) {
	        	return true;
	        } else if (135.0 <= rotation && rotation < 225) {
	        	return true;
	        } else if (315.0 <= rotation && rotation < 360.0) {
	        	return true;
	        }
		}
		return false;
	}
	
	/**
	 * Returns true if the player is facing a positive x or z direction.
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

	/**
	 * Returns true if the player is currently facing another player in the world.
	 * @return boolean
	 */
    public static boolean isFacingPlayer() {
        if (mc.objectMouseOver.entityHit != null) {
        	if (mc.objectMouseOver.entityHit instanceof EntityPlayerMP) {
        		return true;
        	}                
        }
        return false;
    }

    /**
     * Returns the name of the player that is currently being faced.
     * @return boolean
     */
    public static String getFacingPlayerName() {
        if (mc.objectMouseOver.entityHit != null) {
        	if (mc.objectMouseOver.entityHit instanceof EntityPlayerMP) {
                EntityPlayerMP facingPlayer = (EntityPlayerMP) mc.objectMouseOver.entityHit;
                return facingPlayer.getDisplayNameString();
            }
        }            
        return null;
    }
    
    /**
     * Returns the Minecraft.Block object the player is currently facing.
     * @return Minecraft.Block
     */
    public static Block getFacingBlock() {
    	try {
    		MovingObjectPosition mop = Minecraft.getMinecraft().getRenderViewEntity().rayTrace(200, 1.0F);
    		if (mop != null) {
    			Block blocky = mc.theWorld.getBlockState(mop.getBlockPos()).getBlock();
    			return blocky;
    		}     	
    	} catch (Exception e) { e.printStackTrace(); }
    	return null;
    }
    
    /**
     * Returns the Minecraft.BlockPos object of the block the player is currently facing.
     * @return Minecraft.BlockPos
     */
    public static BlockPos getFacingBlockPos() {
    	try {
    		MovingObjectPosition mop = Minecraft.getMinecraft().getRenderViewEntity().rayTrace(200, 1.0F);
    		if (mop != null) {
    			return mop.getBlockPos();
    		}
    	} catch (Exception e) { e.printStackTrace(); }
    	return null;
    }
    
    /**
     * Returns the base id of the block the player is currently facing.
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
     * @param PlayerFacing.Direction
     */
    public static void setFacingDir(Direction dir) {
    	setFacingDir(dir.getDegree());
    }
    
    /**
     * Sets the players facing direction instantaneously.
     * @param float
     */
    public static void setFacingDir(float dir) {
    	try {
    		mc.getRenderViewEntity().rotationYaw = dir;
    	} catch (Exception e) {
    		e.printStackTrace();
    	}    	
    }    
    
    public static void graduallyFaceDir(Direction dir, int speed) {
    	graduallyFaceDir(dir.getDegree(), speed);
    }
    
    public static void graduallyFaceDir(float dir, int speed) {
    	//unfinished
    }
}