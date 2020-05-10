package com.Whodundid.windowHUD.windowObjects.hotbar;

import com.Whodundid.core.EnhancedMC;
import com.Whodundid.core.enhancedGui.objectEvents.eventUtil.ObjectModifyType;
import com.Whodundid.core.enhancedGui.types.WindowParent;
import com.Whodundid.core.util.EUtil;
import com.Whodundid.core.util.renderUtil.CenterType;
import com.Whodundid.windowHUD.WindowHudResources;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

public final class HotBarRenderer extends WindowParent {
	
	protected static final ResourceLocation widgetsTexPath = new ResourceLocation("textures/gui/widgets.png");
	public static volatile float ticks = 0;
	private static boolean drawVertical = false;
	
	@Override
	public void initGui() {
		ScaledResolution res = new ScaledResolution(mc);
		
		setResizeable(true);
		
		if (drawVertical) {
			setDimensions(res.getScaledWidth() - 22, res.getScaledHeight() / 2 - 91, 22, 182);
			//setDimensions(startX, startY, 22, 182);
			setMinDims(10, 100);
		} else {
			setDimensions(res.getScaledWidth() / 2 - 91, res.getScaledHeight() - 22, 182, 22);
			//setDimensions(startX, startY, 182, 22);
			setMinDims(100, 10);
		}
		
		setPinned(true);
		setCloseable(false);
	}
	
	@Override
	public void initObjects() {
		//defaultHeader(this);
		//header.setTitle("Hotbar");
	}
	
	@Override
	public void drawObject(int mXIn, int mYIn) {
		super.drawObject(mXIn, mYIn);
		//System.out.println(getDimensions());
		//this.drawRect(startX, startY, endX, endY, 0x88ff0000);
		if (mc.ingameGUI != null) {
			if (mc.playerController.isSpectator()) {
				mc.ingameGUI.getSpectatorGui().renderTooltip(res, ticks);
			} else {
				if (mc.getRenderViewEntity() instanceof EntityPlayer) {
					GlStateManager.color(2.0F, 1.0F, 1.0F, 1.0F);
					EntityPlayer player = (EntityPlayer) mc.getRenderViewEntity();
					
					double onePixelW = (drawVertical ? height : width) / (double) EUtil.getImageWidth(WindowHudResources.hotbar);
					double onePixelH = (drawVertical ? width : height) / (double) EUtil.getImageHeight(WindowHudResources.hotbar);
					double borderW = onePixelW * 3;
					double borderH = onePixelH * 3;
					double midIW = onePixelW * 4;
					double midIH = onePixelH * 4;
					double midW = onePixelW * 16;
					double midH = onePixelH * 16;
					
					double val = midW < midH ? midW : midH;
					double scale = val * 0.0625;
					double scaleX = midW * 0.0625;
					
					//System.out.println(val);
					
					float oldF = glZLevel;
					glZLevel = -90.0f;
					
					if (drawVertical) {
						GlStateManager.pushMatrix();
						GlStateManager.translate(startX, startY, 0);
						GlStateManager.rotate(90, 0, 0, 45);
						GlStateManager.translate(-startX, -startY - width, 0);
					}
					
					double drawW = drawVertical ? height : width;
					double drawH = drawVertical ? width : height;
					double drawDW = drawVertical ? (onePixelW * 24) : height + 2;
					double drawDH = drawVertical ? height + 2 : (onePixelW * 24);
					
					mc.getTextureManager().bindTexture(WindowHudResources.hotbar);
					drawTexture(startX, startY, drawW, drawH);
					mc.getTextureManager().bindTexture(WindowHudResources.hotbarSelection);
					drawTexture(startX - onePixelW + player.inventory.currentItem * (midW + midIW), startY - 1, (onePixelW * 24), drawH + 2);
					glZLevel = oldF;
					
					if (drawVertical) { GlStateManager.popMatrix(); }
					
					GlStateManager.enableRescaleNormal();
					GlStateManager.enableBlend();
					GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
					RenderHelper.enableGUIStandardItemLighting();
					
					GlStateManager.pushMatrix();
					GlStateManager.scale(drawVertical ? scale : scaleX, drawVertical ? scaleX : scale, 1.0F);
					
					for (int j = 0; j < 9; j++) {
						
						double xPos = (drawVertical ? startY : startX) + borderW + (midW / 2) + j * (midW + midIW);
						double yPos = (drawVertical ? startX : startY) + borderH + (midH / 2);
						double x = drawVertical ? yPos / scale : xPos / scaleX;
						double y = drawVertical ? xPos / scaleX : yPos /scale;
						double xT = x + (drawVertical ? onePixelH : onePixelW * 3);
						double yT = y + (drawVertical ? onePixelW : onePixelH);
						
						ItemStack itemstack = player.inventory.mainInventory[j];
						if (itemstack != null) {
							float f = itemstack.animationsToGo - ticks;
							EnhancedMC.getItemDrawer().renderItemAndEffectIntoGUI(itemstack, x, y);
							EnhancedMC.getItemDrawer().renderItemOverlays(mc.fontRendererObj, itemstack, xT - (onePixelW / width), yT);
						}
					}
					
					GlStateManager.popMatrix();
					

					RenderHelper.disableStandardItemLighting();
					GlStateManager.disableRescaleNormal();
					GlStateManager.disableBlend();
				}
			}
		}
	}
	
	@Override
	public void mousePressed(int mXIn, int mYIn, int button) {
		if (button == 0) {
			getTopParent().setModifyingObject(this, ObjectModifyType.Move);
			getTopParent().setModifyMousePos(mXIn, mYIn);
			super.mousePressed(mXIn, mYIn, button);
		} else if (button == 1) {
			EnhancedMC.displayWindow(new HotBarRCM(this), CenterType.cursorCorner);
		}
	}
	
	@Override
	public void mouseReleased(int mXIn, int mYIn, int button) {
		getTopParent().clearModifyingObject();
		super.mouseReleased(mXIn, mYIn, button);
	}
	
	@Override
	public void mouseScrolled(int change) {
		if (mc.thePlayer != null) { mc.thePlayer.inventory.changeCurrentItem(change); }
	}

	public void flipOrientation() {
		drawVertical = !drawVertical;
		
		if (drawVertical) {
			setDimensions(startX, startY, 22, 182);
			setMinDims(10, 100);
		} else {
			setDimensions(startX, startY, 182, 22);
			setMinDims(100, 10);
		}
	}
	
	public static boolean getOrientation() { return drawVertical; }
}
