package com.Whodundid.core.util.renderUtil;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.EntityLivingBase;

//Author: Hunter Bragg

public class PlayerDrawer {

	public static void drawPlayer(EntityLivingBase entityIn, double posX, double posY, double horizontalRotIn, double verticalRotIn, double scaleIn) {
		double scale = scaleIn;
		double hRot = -horizontalRotIn;
		double vRot = verticalRotIn;
		EntityLivingBase ent = entityIn;

		if (ent != null) {
			GlStateManager.enableColorMaterial();
			GlStateManager.pushMatrix();
			GL11.glTranslated(posX, posY, 50.0);
			GlStateManager.scale((-scale), scale, scale);
			GL11.glRotated(180.0, 0.0, 0.0, 1.0);
			float f = ent.renderYawOffset;
			float f1 = ent.rotationYaw;
			float f2 = ent.rotationPitch;
			float f3 = ent.prevRotationYawHead;
			float f4 = ent.rotationYawHead;
			GL11.glRotated(135.0, 0.0, 1.0, 0.0);
			RenderHelper.enableStandardItemLighting();
			GL11.glRotated(-135.0, 0.0, 1.0, 0.0);
			GL11.glRotated(-Math.atan(vRot / 250.0) * 20.0, 1.0, 0.0, 0.0);
			ent.renderYawOffset = (float) (hRot / 2);
			ent.rotationYaw = (float) (hRot / 2);
			ent.rotationPitch = (float) (-(Math.atan(vRot / 250.0)) * 20.0);
			ent.rotationYawHead = ent.rotationYaw;
			ent.prevRotationYawHead = ent.rotationYaw;
			GL11.glTranslated(0.0, 0.0, 0.0);
			RenderManager rendermanager = Minecraft.getMinecraft().getRenderManager();
			rendermanager.setPlayerViewY(180.0F);
			rendermanager.setRenderShadow(false);
			rendermanager.renderEntityWithPosYaw(ent, 0.0d, 0.0d, 0.0d, 0.0f, 1.0f);
			rendermanager.setRenderShadow(true);
			ent.renderYawOffset = f;
			ent.rotationYaw = f1;
			ent.rotationPitch = f2;
			ent.prevRotationYawHead = f3;
			ent.rotationYawHead = f4;
			GlStateManager.popMatrix();
			RenderHelper.disableStandardItemLighting();
			GlStateManager.disableRescaleNormal();
			GlStateManager.setActiveTexture(OpenGlHelper.lightmapTexUnit);
			GlStateManager.disableTexture2D();
			GlStateManager.setActiveTexture(OpenGlHelper.defaultTexUnit);
		}
	}
}
