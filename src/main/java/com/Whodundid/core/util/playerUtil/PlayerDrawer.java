package com.Whodundid.core.util.playerUtil;

import org.lwjgl.opengl.GL11;
import com.Whodundid.core.EnhancedMC;
import java.lang.reflect.Field;
import java.util.Map;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.RenderPlayer;

//Author: Hunter Bragg

public class PlayerDrawer {

	public double capeSwing = -20.0;
	public boolean swingOut = false;
	public boolean animateCape = true;
	
	public void drawPlayer(AbstractClientPlayer entityIn, double posX, double posY, double horizontalRotIn, double verticalRotIn, double scaleIn) {
		drawPlayer(entityIn, posX, posY, horizontalRotIn, verticalRotIn, scaleIn, false, false);
	}
	
	public void drawPlayer(AbstractClientPlayer entityIn, double posX, double posY, double horizontalRotIn, double verticalRotIn, double scaleIn, boolean fixHead, boolean drawCape) {
		double scale = scaleIn;
		double hRot = -horizontalRotIn;
		double vRot = verticalRotIn;
		AbstractClientPlayer ent = entityIn;

		if (ent != null) {
			float f = ent.renderYawOffset;
			float f1 = ent.rotationYaw;
			float f2 = ent.rotationPitch;
			float f4 = ent.rotationYawHead;
			
			GlStateManager.enableColorMaterial();
			GL11.glPushMatrix();
			GL11.glTranslated(posX, posY, 150.0);
			GL11.glScaled(-scale, scale, scale);
			GL11.glRotated(180.0, 0.0, 0.0, 1.0);
			GL11.glRotated(135.0, 0.0, 1.0, 0.0);
			RenderHelper.enableStandardItemLighting();
			GL11.glRotated(-135.0, 0.0, 1.0, 0.0);
			GL11.glRotated(-Math.atan(vRot / 250.0) * 20.0, 1.0, 0.0, 0.0);
			
			ent.renderYawOffset = (float) (hRot / 2);
			ent.rotationYaw = (float) (hRot / 2);
			ent.rotationPitch = (float) (-(Math.atan(vRot / 250.0)) * 20.0);
			ent.rotationYawHead = fixHead ? ent.rotationYaw : (ent.rotationYaw + ent.rotationYawHead);
			
			RenderManager rendermanager = Minecraft.getMinecraft().getRenderManager();
			rendermanager.doRenderEntity(ent, 0.0d, 0.0d, 0.0d, 90.0f, 1.0f, false);
			
			if (animateCape) { updateCapeSwing(); }
			else { capeSwing = -20f; }
			
			//render cape
			RenderPlayer rp = getRenderPlayer(ent);
			if (drawCape && rp != null && ent.getLocationCape() != null) {
				Minecraft.getMinecraft().renderEngine.bindTexture(ent.getLocationCape());
				GL11.glPushMatrix();
				GL11.glTranslated(0, 1.36, 0.125f); //move up to player
				GL11.glRotated(-ent.rotationYaw, 0.0f, 1.0f, 0.0f); //rotate with player
				GL11.glRotated(180.0f, 1f, 0f, 0.0f); //flip rightside up
				GL11.glRotated(180.0f, 0f, 1f, 0.0f); //flip to the back of player
				GL11.glRotated(capeSwing, 1.0F, 0.0F, 0.0F); //swing cape angle
				GL11.glTranslated(0.0, 0, -0.1); //move slightly behind player
				
				//fix cape lighting
				GL11.glColor4d(10, 10, 10, 10);
				GlStateManager.disableRescaleNormal();
				GlStateManager.setActiveTexture(OpenGlHelper.lightmapTexUnit);
				GlStateManager.disableTexture2D();
				GlStateManager.setActiveTexture(OpenGlHelper.defaultTexUnit);
				
				rp.getMainModel().renderCape(0);
				GL11.glPopMatrix();
			}
			
			ent.renderYawOffset = f;
			ent.rotationYaw = f1;
			ent.rotationPitch = f2;
			ent.rotationYawHead = f4;
			
			GL11.glPopMatrix();
			RenderHelper.disableStandardItemLighting();
			GlStateManager.disableRescaleNormal();
			GlStateManager.setActiveTexture(OpenGlHelper.lightmapTexUnit);
			GlStateManager.disableTexture2D();
			GlStateManager.setActiveTexture(OpenGlHelper.defaultTexUnit);
		}
	}
	
	private void updateCapeSwing() {
		if (swingOut) {
			if (capeSwing > -30.0) { capeSwing -= 0.035; }
			else { swingOut = false; }
		}
		else {
			if (capeSwing < -20.0) { capeSwing += 0.035; }
			else { swingOut = true; }
		}
	}
	
	public void setAnimateCapes(boolean val) { animateCape = val; }
	
	public static RenderPlayer getRenderPlayer(AbstractClientPlayer playerIn) {
		if (playerIn != null) {
			RenderManager man = Minecraft.getMinecraft().getRenderManager();
			
			try {
				Class c = man.getClass();
				Field f = c.getDeclaredField(EnhancedMC.isObfus() ? "field_178636_l" : "skinMap");
				
				f.setAccessible(true);
				Map<String, RenderPlayer> skinMap = (Map<String, RenderPlayer>) f.get(man);
				RenderPlayer rp = skinMap.get(playerIn.getSkinType());
				f.setAccessible(false);
				
				return rp;
			}
			catch (Exception e) { e.printStackTrace(); }
		}
		return null;
	}
	
}
