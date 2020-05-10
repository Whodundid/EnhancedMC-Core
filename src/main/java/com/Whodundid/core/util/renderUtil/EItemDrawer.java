package com.Whodundid.core.util.renderUtil;

import java.util.List;
import java.util.concurrent.Callable;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.EntityRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.ItemModelMesher;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.texture.TextureUtil;
import net.minecraft.client.renderer.tileentity.TileEntityItemStackRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.resources.model.IBakedModel;
import net.minecraft.crash.CrashReport;
import net.minecraft.crash.CrashReportCategory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ReportedException;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.ForgeHooksClient;
import net.minecraftforge.client.model.pipeline.LightUtil;

//Author: Hunter Bragg

public class EItemDrawer {

	public Minecraft mc = Minecraft.getMinecraft();
	private static final ResourceLocation RES_ITEM_GLINT = new ResourceLocation(
			"textures/misc/enchanted_item_glint.png");
	public float zLevel;
	private ItemModelMesher itemModelMesher;
	private TextureManager textureManager;

	public EItemDrawer() {
		itemModelMesher = Minecraft.getMinecraft().getRenderItem().getItemModelMesher();
		textureManager = Minecraft.getMinecraft().getTextureManager();
	}

	public void renderItemAndEffectIntoGUI(final ItemStack stack, double xPosition, double yPosition) {
		if (stack != null && stack.getItem() != null) {
			zLevel += 50.0F;

			try {
				renderItemIntoGUI(stack, xPosition, yPosition);
			}
			catch (Throwable throwable) {
				CrashReport crashreport = CrashReport.makeCrashReport(throwable, "Rendering item");
				CrashReportCategory crashreportcategory = crashreport.makeCategory("Item being rendered");
				crashreportcategory.addCrashSectionCallable("Item Type", new Callable<String>() { public String call() throws Exception { return String.valueOf(stack.getItem()); } });
				crashreportcategory.addCrashSectionCallable("Item Aux", new Callable<String>() { public String call() throws Exception { return String.valueOf(stack.getMetadata()); } });
				crashreportcategory.addCrashSectionCallable("Item NBT", new Callable<String>() { public String call() throws Exception { return String.valueOf(stack.getTagCompound()); } });
				crashreportcategory.addCrashSectionCallable("Item Foil", new Callable<String>() { public String call() throws Exception { return String.valueOf(stack.hasEffect()); } });
				throw new ReportedException(crashreport);
			}

			zLevel -= 50.0F;
		}
	}

	public void renderItemIntoGUI(ItemStack stack, double x, double y) {
		IBakedModel ibakedmodel = itemModelMesher.getItemModel(stack);
		GlStateManager.pushMatrix();
		textureManager.bindTexture(TextureMap.locationBlocksTexture);
		textureManager.getTexture(TextureMap.locationBlocksTexture).setBlurMipmap(false, false);
		GlStateManager.enableRescaleNormal();
		GlStateManager.enableAlpha();
		GlStateManager.alphaFunc(516, 0.1F);
		GlStateManager.enableBlend();
		GlStateManager.blendFunc(770, 771);
		GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
		setupGuiTransform(x, y, ibakedmodel.isGui3d());
		ibakedmodel = ForgeHooksClient.handleCameraTransforms(ibakedmodel, ItemCameraTransforms.TransformType.GUI);
		renderItem(stack, ibakedmodel);
		GlStateManager.disableAlpha();
		GlStateManager.disableRescaleNormal();
		GlStateManager.disableLighting();
		GlStateManager.popMatrix();
		textureManager.bindTexture(TextureMap.locationBlocksTexture);
		textureManager.getTexture(TextureMap.locationBlocksTexture).restoreLastBlurMipmap();
	}

	private void setupGuiTransform(double xPosition, double yPosition, boolean isGui3d) {
		GlStateManager.translate(xPosition, yPosition, 10.0F + zLevel);
		GlStateManager.scale(1.0F, 1.0F, -1.0F);
		GlStateManager.scale(0.5F, 0.5F, 0.5F);

		if (isGui3d) {
			GlStateManager.scale(40.0F, 40.0F, 40.0F);
			GlStateManager.rotate(210.0F, 1.0F, 0.0F, 0.0F);
			GlStateManager.rotate(-135.0F, 0.0F, 1.0F, 0.0F);
			GlStateManager.enableLighting();
		} else {
			GlStateManager.scale(64.0F, 64.0F, 64.0F);
			GlStateManager.rotate(180.0F, 1.0F, 0.0F, 0.0F);
			GlStateManager.disableLighting();
		}
	}

	public void renderItem(ItemStack stack, IBakedModel model) {
		if (stack != null) {
			GlStateManager.pushMatrix();
			GlStateManager.scale(0.5F, 0.5F, 0.5F);

			if (model.isBuiltInRenderer()) {
				GlStateManager.rotate(180.0F, 0.0F, 1.0F, 0.0F);
				GlStateManager.translate(-0.5F, -0.5F, -0.5F);
				GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
				GlStateManager.enableRescaleNormal();
				TileEntityItemStackRenderer.instance.renderByItem(stack);
			} else {
				GlStateManager.translate(-0.5F, -0.5F, -0.5F);
				renderModel(model, stack);

				if (stack.hasEffect()) {
					renderEffect(model);
				}
			}

			GlStateManager.popMatrix();
		}
	}

	private void renderEffect(IBakedModel model) {
		GlStateManager.depthMask(false);
		GlStateManager.depthFunc(514);
		GlStateManager.disableLighting();
		GlStateManager.blendFunc(768, 1);
		textureManager.bindTexture(RES_ITEM_GLINT);
		GlStateManager.matrixMode(5890);
		GlStateManager.pushMatrix();
		GlStateManager.scale(8.0F, 8.0F, 8.0F);
		float f = Minecraft.getSystemTime() % 3000L / 3000.0F / 8.0F;
		GlStateManager.translate(f, 0.0F, 0.0F);
		GlStateManager.rotate(-50.0F, 0.0F, 0.0F, 1.0F);
		renderModel(model, -8372020);
		GlStateManager.popMatrix();
		GlStateManager.pushMatrix();
		GlStateManager.scale(8.0F, 8.0F, 8.0F);
		float f1 = Minecraft.getSystemTime() % 4873L / 4873.0F / 8.0F;
		GlStateManager.translate(-f1, 0.0F, 0.0F);
		GlStateManager.rotate(10.0F, 0.0F, 0.0F, 1.0F);
		renderModel(model, -8372020);
		GlStateManager.popMatrix();
		GlStateManager.matrixMode(5888);
		GlStateManager.blendFunc(770, 771);
		GlStateManager.enableLighting();
		GlStateManager.depthFunc(515);
		GlStateManager.depthMask(true);
		textureManager.bindTexture(TextureMap.locationBlocksTexture);
	}

	private void renderModel(IBakedModel model, ItemStack stack) { renderModel(model, -1, stack); }
	private void renderModel(IBakedModel model, int color) { renderModel(model, color, (ItemStack) null); }
	private void renderModel(IBakedModel model, int color, ItemStack stack) {
		Tessellator tessellator = Tessellator.getInstance();
		WorldRenderer worldrenderer = tessellator.getWorldRenderer();
		worldrenderer.begin(7, DefaultVertexFormats.ITEM);

		for (EnumFacing enumfacing : EnumFacing.values()) {
			renderQuads(worldrenderer, model.getFaceQuads(enumfacing), color, stack);
		}

		renderQuads(worldrenderer, model.getGeneralQuads(), color, stack);
		tessellator.draw();
	}

	private void renderQuads(WorldRenderer renderer, List<BakedQuad> quads, int color, ItemStack stack) {
		boolean flag = color == -1 && stack != null;
		int i = 0;

		for (int j = quads.size(); i < j; ++i) {
			BakedQuad bakedquad = quads.get(i);
			int k = color;

			if (flag && bakedquad.hasTintIndex()) {
				k = stack.getItem().getColorFromItemStack(stack, bakedquad.getTintIndex());

				if (EntityRenderer.anaglyphEnable) {
					k = TextureUtil.anaglyphColor(k);
				}

				k = k | -16777216;
			}

			LightUtil.renderQuadColor(renderer, bakedquad, k);
		}
	}

	public void renderItemOverlays(FontRenderer fr, ItemStack stack, double xPosition, double yPosition) {
		renderItemOverlayIntoGUI(fr, stack, xPosition, yPosition, (String) null);
	}

	/** Renders the stack size and/or damage bar for the given ItemStack. */
	public void renderItemOverlayIntoGUI(FontRenderer fr, ItemStack stack, double xPosition, double yPosition, String text) {
		if (stack != null) {
			if (stack.stackSize != 1 || text != null) {
				String s = text == null ? String.valueOf(stack.stackSize) : text;

				if (text == null && stack.stackSize < 1) {
					s = EnumChatFormatting.RED + String.valueOf(stack.stackSize);
				}

				GlStateManager.disableLighting();
				GlStateManager.disableDepth();
				GlStateManager.disableBlend();
				GLObject.drawStringWithShadow(s, xPosition, yPosition, 16777215);
				GlStateManager.enableLighting();
				GlStateManager.enableDepth();
			}

			if (stack.getItem().showDurabilityBar(stack)) {
				double health = stack.getItem().getDurabilityForDisplay(stack);
				int j = (int) Math.round(13.0D - health * 13.0D);
				int i = (int) Math.round(255.0D - health * 255.0D);
				GlStateManager.disableLighting();
				GlStateManager.disableDepth();
				GlStateManager.disableTexture2D();
				GlStateManager.disableAlpha();
				GlStateManager.disableBlend();
				Tessellator tessellator = Tessellator.getInstance();
				WorldRenderer worldrenderer = tessellator.getWorldRenderer();
				drawRect(worldrenderer, xPosition, yPosition, 13, 2, 0, 0, 0, 255);
				drawRect(worldrenderer, xPosition, yPosition, 12, 1, (255 - i) / 4, 64, 0, 255);
				drawRect(worldrenderer, xPosition, yPosition, j, 1, 255 - i, i, 0, 255);
				// GlStateManager.enableBlend(); // Forge: Disable Blend because it screws with a lot of things down the line.
				GlStateManager.enableAlpha();
				GlStateManager.enableTexture2D();
				GlStateManager.enableLighting();
				GlStateManager.enableDepth();
			}
		}
	}

	private void drawRect(WorldRenderer worldRenderer, double xPos, double yPos, double width, double height, int r, int g, int b, int a) {
		worldRenderer.begin(7, DefaultVertexFormats.POSITION_COLOR);
		worldRenderer.pos(xPos + 0, yPos + 0, 0.0D).color(r, g, b, a).endVertex();
		worldRenderer.pos(xPos + 0, yPos + height, 0.0D).color(r, g, b, a).endVertex();
		worldRenderer.pos(xPos + width, yPos + height, 0.0D).color(r, g, b, a).endVertex();
		worldRenderer.pos(xPos + width, yPos + 0, 0.0D).color(r, g, b, a).endVertex();
		Tessellator.getInstance().draw();
	}
}
