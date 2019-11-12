package com.Whodundid.core.util.renderUtil;

import com.Whodundid.core.util.storageUtil.EArrayList;
import com.Whodundid.core.util.storageUtil.StorageBox;
import com.Whodundid.core.util.storageUtil.StorageBoxHolder;
import com.Whodundid.core.util.storageUtil.Vector3D;
import com.Whodundid.core.util.storageUtil.WorldRegion;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import org.lwjgl.opengl.GL11;

public class BlockDrawer {

	static StorageBoxHolder<WorldRegion, Integer> blocks = new StorageBoxHolder();
	static StorageBoxHolder<WorldRegion, Integer> toAdd = new StorageBoxHolder();

	public static void addBlock(BlockPos posIn, int colorIn) {
		addBlock(new Vector3D(posIn), colorIn);
	}

	public static void addBlock(Vector3D posIn, int colorIn) {
		blocks.add(new WorldRegion(posIn), colorIn);
	}
	
	public static void addBlock(WorldRegion regionIn, int colorIn) {
		blocks.add(regionIn, colorIn);
	}

	public static void clearBlocks() {
		blocks.clear();
	}

	public static void draw(RenderWorldLastEvent e) {
		if (Minecraft.getMinecraft().thePlayer != null) {
			EntityPlayerSP p = Minecraft.getMinecraft().thePlayer;

			EArrayList<AxisAlignedBB> bbs = new EArrayList();

			for (StorageBox<WorldRegion, Integer> box : blocks) {
				WorldRegion r = box.getObject();
				//System.out.println(r);
				bbs.add(new AxisAlignedBB(r.sX + r.width, r.sY + r.height, r.sZ + r.length, r.sX, r.sY, r.sZ));
			}

			double d0 = p.lastTickPosX + (p.posX - p.lastTickPosX) * e.partialTicks;
			double d1 = p.lastTickPosY + (p.posY - p.lastTickPosY) * e.partialTicks;
			double d2 = p.lastTickPosZ + (p.posZ - p.lastTickPosZ) * e.partialTicks;
			
			GL11.glTranslated(-d0, -d1, -d2);
			for (int i = 0; i < blocks.size(); i++) {
				int color = blocks.getValue(i);
				float f3 = (color >> 24 & 255) / 255.0F;
				float f = (color >> 16 & 255) / 255.0F;
				float f1 = (color >> 8 & 255) / 255.0F;
				float f2 = (color & 255) / 255.0F;
				GL11.glColor4f(f, f1, f2, f3);
				startDrawingESPs(bbs.get(i), f, f1, f2);
			}
		}
	}

	public static void startDrawingESPs(AxisAlignedBB bb, float r, float b, float g) {
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
