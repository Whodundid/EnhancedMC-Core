package com.Whodundid.core.util.renderUtil;

import com.Whodundid.core.coreApp.CoreApp;
import com.Whodundid.core.util.storageUtil.EArrayList;
import com.Whodundid.core.util.storageUtil.StorageBox;
import com.Whodundid.core.util.storageUtil.StorageBoxHolder;
import com.Whodundid.core.util.storageUtil.Vector3D;
import com.Whodundid.core.util.worldUtil.WorldRegion;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import org.lwjgl.opengl.GL11;

//Author: Hunter Bragg

public class BlockDrawer {

	static StorageBoxHolder<StorageBox<Boolean, StorageBox<String, Integer>>, StorageBox<WorldRegion, Integer>> blocks = new StorageBoxHolder();
	static StorageBoxHolder<StorageBox<Boolean, StorageBox<String, Integer>>, StorageBox<WorldRegion, Integer>> toAdd = new StorageBoxHolder();

	public static void addBlock(BlockPos posIn, int colorIn) { addBlock(new Vector3D(posIn), colorIn); }
	public static void addBlock(Vector3D posIn, int colorIn) { blocks.add(null, new StorageBox(new WorldRegion(posIn), colorIn)); }
	public static void addBlock(WorldRegion regionIn, int colorIn) { blocks.add(null, new StorageBox(regionIn, colorIn)); }
	
	public static void addPlayer(EntityPlayer playerIn, int colorIn, boolean drawName, int nameColor) {
		if (playerIn != null) {
			Vector3D pos = new Vector3D(playerIn.posX, playerIn.posY, playerIn.posZ);
			WorldRegion loc = new WorldRegion(pos.x - 0.5, pos.y, pos.z - 0.5, pos.x + 0.5, pos.y + 2, pos.z + 0.5);
			blocks.add(new StorageBox(drawName, new StorageBox(playerIn.getName(), nameColor)), new StorageBox(loc, colorIn));
		}
	}

	public static void clearBlocks() { blocks.clear(); }

	public static void draw(RenderWorldLastEvent e) {
		if (CoreApp.enableBlockDrawer.get() && Minecraft.getMinecraft().thePlayer != null) {
			EntityPlayerSP p = Minecraft.getMinecraft().thePlayer;

			EArrayList<AxisAlignedBB> bbs = new EArrayList();

			for (StorageBox<StorageBox<Boolean, StorageBox<String, Integer>>, StorageBox<WorldRegion, Integer>> box : blocks) {
				WorldRegion r = box.getValue().getObject();
				bbs.add(new AxisAlignedBB(r.sX + r.width, r.sY + r.height, r.sZ + r.length, r.sX, r.sY, r.sZ));
			}

			double d0 = p.lastTickPosX + (p.posX - p.lastTickPosX) * e.partialTicks;
			double d1 = p.lastTickPosY + (p.posY - p.lastTickPosY) * e.partialTicks;
			double d2 = p.lastTickPosZ + (p.posZ - p.lastTickPosZ) * e.partialTicks;
			
			GL11.glTranslated(-d0, -d1, -d2);
			for (int i = 0; i < blocks.size(); i++) {
				int color = blocks.getValue(i).getValue();
				float f3 = (color >> 24 & 255) / 255.0F;
				float f = (color >> 16 & 255) / 255.0F;
				float f1 = (color >> 8 & 255) / 255.0F;
				float f2 = (color & 255) / 255.0F;
				
				String str = "";
				boolean drawString = false;
				int strColor = 0;
				if (blocks.getObject(i) != null) {
					drawString = blocks.getObject(i).getObject();
					str = blocks.getObject(i).getValue().getObject();
					strColor = blocks.getObject(i).getValue().getValue();
				}
				GL11.glColor4f(f, f1, f2, f3);
				startDrawingESPs(bbs.get(i), f, f1, f2, drawString, str, strColor);
			}
		}
	}

	public static void startDrawingESPs(AxisAlignedBB bb, float r, float b, float g, boolean drawString, String str, int strColor) {
		GL11.glPushMatrix();
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		GL11.glLineWidth(1.5F);
		GL11.glDisable(GL11.GL_LIGHTING);
		GL11.glDisable(GL11.GL_TEXTURE_2D);
		GL11.glEnable(GL11.GL_LINE_SMOOTH);
		GL11.glDisable(GL11.GL_DEPTH_TEST);
		GL11.glDepthMask(false);
		RenderHelper.disableStandardItemLighting();
		GL11.glColor4d(r, b, g, 0.185F);
		//GL11.glColor4d(r, b, g, 0F);
		drawBoundingBox(bb);
		GL11.glColor4d(r, b, g, 1.0F);
		drawOutlinedBoundingBox(bb);
		
		if (drawString) {
			GL11.glPushMatrix();
			FontRenderer fr = Minecraft.getMinecraft().fontRendererObj;
			float midX = (float) (bb.minX + (bb.maxX - bb.minX) / 2);
			float midZ = (float) (bb.minZ + (bb.maxZ - bb.minZ) / 2);
			
			GL11.glTranslated(midX, bb.maxY + 1, midZ);
			GL11.glRotated(-Minecraft.getMinecraft().thePlayer.rotationYaw, 0, 1, 0);
			GL11.glRotated(Minecraft.getMinecraft().thePlayer.rotationPitch, 1, 0, 0);
			GL11.glTranslated(1, 2, 0);
			
			GL11.glEnable(GL11.GL_TEXTURE_2D);
			GL11.glRotated(180, 0, 0, 1);
			GL11.glScaled(0.035, 0.035, 0.035);
			fr.drawString(str, (float) midX, (float) bb.maxY, strColor, true);
			GL11.glScaled(1, 1, 1);
			GL11.glTranslated(bb.minX, bb.minZ, 0);
			GL11.glPopMatrix();
		}
		
		RenderHelper.enableGUIStandardItemLighting();
		GL11.glLineWidth(2.0F);
		GL11.glDisable(GL11.GL_LINE_SMOOTH);
		GL11.glEnable(GL11.GL_TEXTURE_2D);
		GL11.glEnable(GL11.GL_LIGHTING);
		GL11.glEnable(GL11.GL_DEPTH_TEST);
		GL11.glDepthMask(true);
		GL11.glDisable(GL11.GL_BLEND);
		GL11.glPopMatrix();
	}

	public static void drawBoundingBox(AxisAlignedBB pos) {
		WorldRenderer renderer = Tessellator.getInstance().getWorldRenderer();
		Tessellator tessellator = Tessellator.getInstance();

		renderer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION);
		renderer.pos(pos.minX, pos.minY, pos.minZ).endVertex();
		renderer.pos(pos.minX, pos.maxY, pos.minZ).endVertex();
		renderer.pos(pos.maxX, pos.minY, pos.minZ).endVertex();
		renderer.pos(pos.maxX, pos.maxY, pos.minZ).endVertex();
		renderer.pos(pos.maxX, pos.minY, pos.maxZ).endVertex();
		renderer.pos(pos.maxX, pos.maxY, pos.maxZ).endVertex();
		renderer.pos(pos.minX, pos.minY, pos.maxZ).endVertex();
		renderer.pos(pos.minX, pos.maxY, pos.maxZ).endVertex();
		tessellator.draw();
		renderer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION);
		renderer.pos(pos.maxX, pos.maxY, pos.minZ).endVertex();
		renderer.pos(pos.maxX, pos.minY, pos.minZ).endVertex();
		renderer.pos(pos.minX, pos.maxY, pos.minZ).endVertex();
		renderer.pos(pos.minX, pos.minY, pos.minZ).endVertex();
		renderer.pos(pos.minX, pos.maxY, pos.maxZ).endVertex();
		renderer.pos(pos.minX, pos.minY, pos.maxZ).endVertex();
		renderer.pos(pos.maxX, pos.maxY, pos.maxZ).endVertex();
		renderer.pos(pos.maxX, pos.minY, pos.maxZ).endVertex();
		tessellator.draw();
		renderer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION);
		renderer.pos(pos.minX, pos.maxY, pos.minZ).endVertex();
		renderer.pos(pos.maxX, pos.maxY, pos.minZ).endVertex();
		renderer.pos(pos.maxX, pos.maxY, pos.maxZ).endVertex();
		renderer.pos(pos.minX, pos.maxY, pos.maxZ).endVertex();
		renderer.pos(pos.minX, pos.maxY, pos.minZ).endVertex();
		renderer.pos(pos.minX, pos.maxY, pos.maxZ).endVertex();
		renderer.pos(pos.maxX, pos.maxY, pos.maxZ).endVertex();
		renderer.pos(pos.maxX, pos.maxY, pos.minZ).endVertex();
		tessellator.draw();
		renderer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION);
		renderer.pos(pos.minX, pos.minY, pos.minZ).endVertex();
		renderer.pos(pos.maxX, pos.minY, pos.minZ).endVertex();
		renderer.pos(pos.maxX, pos.minY, pos.maxZ).endVertex();
		renderer.pos(pos.minX, pos.minY, pos.maxZ).endVertex();
		renderer.pos(pos.minX, pos.minY, pos.minZ).endVertex();
		renderer.pos(pos.minX, pos.minY, pos.maxZ).endVertex();
		renderer.pos(pos.maxX, pos.minY, pos.maxZ).endVertex();
		renderer.pos(pos.maxX, pos.minY, pos.minZ).endVertex();
		tessellator.draw();
		renderer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION);
		renderer.pos(pos.minX, pos.minY, pos.minZ).endVertex();
		renderer.pos(pos.minX, pos.maxY, pos.minZ).endVertex();
		renderer.pos(pos.minX, pos.minY, pos.maxZ).endVertex();
		renderer.pos(pos.minX, pos.maxY, pos.maxZ).endVertex();
		renderer.pos(pos.maxX, pos.minY, pos.maxZ).endVertex();
		renderer.pos(pos.maxX, pos.maxY, pos.maxZ).endVertex();
		renderer.pos(pos.maxX, pos.minY, pos.minZ).endVertex();
		renderer.pos(pos.maxX, pos.maxY, pos.minZ).endVertex();
		tessellator.draw();
		renderer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION);
		renderer.pos(pos.minX, pos.maxY, pos.maxZ).endVertex();
		renderer.pos(pos.minX, pos.minY, pos.maxZ).endVertex();
		renderer.pos(pos.minX, pos.maxY, pos.minZ).endVertex();
		renderer.pos(pos.minX, pos.minY, pos.minZ).endVertex();
		renderer.pos(pos.maxX, pos.maxY, pos.minZ).endVertex();
		renderer.pos(pos.maxX, pos.minY, pos.minZ).endVertex();
		renderer.pos(pos.maxX, pos.maxY, pos.maxZ).endVertex();
		renderer.pos(pos.maxX, pos.minY, pos.maxZ).endVertex();
		tessellator.draw();
	}

	public static void drawOutlinedBoundingBox(AxisAlignedBB pos) {
		WorldRenderer renderer = Tessellator.getInstance().getWorldRenderer();
		Tessellator tessellator = Tessellator.getInstance();

		renderer.begin(3, DefaultVertexFormats.POSITION);
		renderer.pos(pos.minX, pos.minY, pos.minZ).endVertex();
		renderer.pos(pos.maxX, pos.minY, pos.minZ).endVertex();
		renderer.pos(pos.maxX, pos.minY, pos.maxZ).endVertex();
		renderer.pos(pos.minX, pos.minY, pos.maxZ).endVertex();
		renderer.pos(pos.minX, pos.minY, pos.minZ).endVertex();
		tessellator.draw();
		renderer.begin(3, DefaultVertexFormats.POSITION);
		renderer.pos(pos.minX, pos.maxY, pos.minZ).endVertex();
		renderer.pos(pos.maxX, pos.maxY, pos.minZ).endVertex();
		renderer.pos(pos.maxX, pos.maxY, pos.maxZ).endVertex();
		renderer.pos(pos.minX, pos.maxY, pos.maxZ).endVertex();
		renderer.pos(pos.minX, pos.maxY, pos.minZ).endVertex();
		tessellator.draw();
		renderer.begin(1, DefaultVertexFormats.POSITION);
		renderer.pos(pos.minX, pos.minY, pos.minZ).endVertex();
		renderer.pos(pos.minX, pos.maxY, pos.minZ).endVertex();
		renderer.pos(pos.maxX, pos.minY, pos.minZ).endVertex();
		renderer.pos(pos.maxX, pos.maxY, pos.minZ).endVertex();
		renderer.pos(pos.maxX, pos.minY, pos.maxZ).endVertex();
		renderer.pos(pos.maxX, pos.maxY, pos.maxZ).endVertex();
		renderer.pos(pos.minX, pos.minY, pos.maxZ).endVertex();
		renderer.pos(pos.minX, pos.maxY, pos.maxZ).endVertex();
		tessellator.draw();
	}
	
}
