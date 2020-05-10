package com.Whodundid.parkourHelper;

import com.Whodundid.core.util.playerUtil.PlayerFacing;
import com.Whodundid.core.util.renderUtil.GLObject;
import java.text.DecimalFormat;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.EnumChatFormatting;

public class ScreenDrawer extends GLObject {

	public ParkourApp mod;
	public String jumpType = "";
	public String pos = "";
	public String pass = "";
	private double maxX = 0;
	
	public ScreenDrawer(ParkourApp modIn) {
		mod = modIn;
	}
	
	public void drawScreen() {
		GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
		ScaledResolution res = new ScaledResolution(mc);
		String posX = new DecimalFormat("0.0000000000").format(mc.getRenderViewEntity().posX);
		String posY = new DecimalFormat("0.0000000000").format(mc.getRenderViewEntity().getEntityBoundingBox().minY);
		String posZ = new DecimalFormat("0.0000000000").format(mc.getRenderViewEntity().posZ);				
		pos = (EnumChatFormatting.AQUA + posX + " / " + posY + " / " + posZ + " - " + PlayerFacing.getCompassFacingDir().name() + "; " + PlayerFacing.getCompassFacingDir().getXZFacing());
		drawRect(0, res.getScaledHeight() - 43, getLongestString() + 15, res.getScaledHeight(), 0xff000000); //black
		drawRect(1, res.getScaledHeight() - 42, getLongestString() + 14, res.getScaledHeight() - 1, 0xff2b2b2b); //grey
		drawString(jumpType, 10, res.getScaledHeight() - 38, 0xffffff);
		drawString(pos, 10, res.getScaledHeight() - 26, 0xffffff);
		drawString(pass, 10, res.getScaledHeight() - 14, 0xffffff);
		if (mc.thePlayer != null) {
			double mo = mc.thePlayer.motionX;
			if (Math.abs(mo) > Math.abs(maxX)) { maxX = mo; }
			drawRect(9, res.getScaledHeight() - 71, 190, res.getScaledHeight() - 39, 0xff000000);
			drawString(String.format("momentumX: %.17f", mc.thePlayer.motionX), 10, res.getScaledHeight() - 60, 0xffff5555);
			drawString(String.format("max X: %.17f", maxX), 10, res.getScaledHeight() - 50, 0xff55ff55);
			drawString(String.format("facingX: %.17f", mc.thePlayer.rotationYaw), 10, res.getScaledHeight() - 70, 0xff5555ff);
		}
	}
	
	private int getLongestString() {
		int jTL = mc.fontRendererObj.getStringWidth(jumpType);
		int posL = mc.fontRendererObj.getStringWidth(pos);
		int passL = mc.fontRendererObj.getStringWidth(pass);
		
		if (jTL > posL && jTL > passL) { return jTL; }
		else if (posL > jTL && posL > passL) { return posL; }
		else if (passL > jTL && passL > posL) { return passL; }
		else { return jTL; }
	}
	
	public void reset() {
		maxX = 0;
	}
}
