package com.Whodundid.core.util;

import static net.minecraftforge.client.event.RenderGameOverlayEvent.ElementType.*;

import com.Whodundid.core.EnhancedMC;
import com.Whodundid.core.util.miscUtil.EFontRenderer;
import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import java.awt.Color;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiIngame;
import net.minecraft.client.gui.GuiNewChat;
import net.minecraft.client.gui.GuiOverlayDebug;
import net.minecraft.client.gui.GuiPlayerTabOverlay;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.scoreboard.Score;
import net.minecraft.scoreboard.ScoreObjective;
import net.minecraft.scoreboard.ScorePlayerTeam;
import net.minecraft.scoreboard.Scoreboard;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.FoodStats;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StringUtils;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent.ElementType;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.common.MinecraftForge;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;

//Last edited: Dec 27, 2018
//First Added: Oct 20, 2018
//Author: Hunter Bragg

public class ModdedInGameGui extends GuiIngame {
	
	protected ScaledResolution res;
	protected EFontRenderer fontRenderer = EnhancedMC.getFontRenderer();
	protected RenderGameOverlayEvent eventParent;
	protected GuiOverlayDebugForge debugOverlay;
	protected final ModdedGuiNewChat persistantModdedChatGUI;
	protected GuiPlayerTabOverlay tabList;
    public static boolean renderHelmet = true, renderPortal = true, renderHotbar = true, renderCrosshairs = true, renderBossHealth = true;
    public static boolean renderHealth = true, renderArmor = true, renderFood = true, renderHealthMount = true, renderAir = true;
    public static boolean renderExperiance = true, renderJumpBar = true, renderObjective = true;
    public static int left_height = 39, right_height = 39;

	public ModdedInGameGui(Minecraft mc) {
		super(mc);
		debugOverlay = new GuiOverlayDebugForge(mc);
		persistantModdedChatGUI = new ModdedGuiNewChat(mc);
		tabList = new GuiPlayerTabOverlay(mc, this);
	}

	@Override
	public void renderGameOverlay(float partialTicks) {
		res = new ScaledResolution(mc);
		eventParent = new RenderGameOverlayEvent(partialTicks, res);
		
		int width = res.getScaledWidth();
		int height = res.getScaledHeight();
		renderHealthMount = mc.thePlayer.ridingEntity instanceof EntityLivingBase;
		renderFood = mc.thePlayer.ridingEntity == null;
		renderJumpBar = mc.thePlayer.isRidingHorse();
		right_height = 39;
		left_height = 39;
		
		if (pre(ALL)) { return; }
		
		mc.entityRenderer.setupOverlayRendering();
		
		GlStateManager.enableBlend();
		if (Minecraft.isFancyGraphicsEnabled()) { renderVignette(mc.thePlayer.getBrightness(partialTicks), res); }
		else { GlStateManager.tryBlendFuncSeparate(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA, 1, 0); }
		
		if (renderHelmet) { renderHelmet(res, partialTicks); }
		if (renderPortal && !mc.thePlayer.isPotionActive(Potion.confusion)) { renderPortal(res, partialTicks); }
		if (renderHotbar) { renderTooltip(res, partialTicks); }
		
		GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
		zLevel = -90.0F;
		rand.setSeed(updateCounter * 312871);
		if (renderCrosshairs) { renderCrosshairs(width, height); }
		
		if (renderBossHealth) { renderBossHealth(); }
		
		if (mc.playerController.shouldDrawHUD() && mc.getRenderViewEntity() instanceof EntityPlayer) {
			if (renderHealth) { renderHealth(width, height); }
			if (renderArmor) { renderArmor(width, height); }
			if (renderFood) { renderFood(width, height); }
			if (renderHealthMount) { renderHealthMount(width, height); }
			if (renderAir) { renderAir(width, height); }
		}
		
		renderSleepFade(width, height);
		
		if (renderJumpBar) { renderJumpBar(width, height); }
		else if (renderExperiance) { renderExperience(width, height); }
		
		renderToolHightlight(res);
		renderHUDText(width, height);
		renderRecordOverlay(width, height, partialTicks);
		renderTitle(width, height, partialTicks);
		Scoreboard scoreboard = mc.theWorld.getScoreboard();
		ScoreObjective objective = null;
		ScorePlayerTeam scoreplayerteam = scoreboard.getPlayersTeam(mc.thePlayer.getName());
		if (scoreplayerteam != null) {
			int slot = scoreplayerteam.getChatFormat().getColorIndex();
			if (slot >= 0) { objective = scoreboard.getObjectiveInDisplaySlot(3 + slot); }
		}
		ScoreObjective scoreobjective1 = objective != null ? objective : scoreboard.getObjectiveInDisplaySlot(1);
		if (renderObjective && scoreobjective1 != null) { renderScoreboard(scoreobjective1, res); }
		
		GlStateManager.enableBlend();
		GlStateManager.tryBlendFuncSeparate(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA, 1, 0);
		GlStateManager.disableAlpha();
		renderChat(width, height);
		renderPlayerList(width, height);
		GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
		GlStateManager.disableLighting();
		GlStateManager.enableAlpha();
		
		post(ALL);
	}

	protected void renderHelmet(ScaledResolution res, float partialTicks) {
		if (pre(HELMET)) { return; }
		ItemStack itemstack = mc.thePlayer.inventory.armorItemInSlot(3);
		if (mc.gameSettings.thirdPersonView == 0 && itemstack != null && itemstack.getItem() != null) {
			if (itemstack.getItem() == Item.getItemFromBlock(Blocks.pumpkin)) { renderPumpkinOverlay(res); }
			else { itemstack.getItem().renderHelmetOverlay(itemstack, mc.thePlayer, res, partialTicks); }
		}
		post(HELMET);
	}

    protected void renderCrosshairs(int width, int height) {
        if (pre(CROSSHAIRS)) { return; }
        if (showCrosshair()) {
            bind(Gui.icons);
            GlStateManager.enableBlend();
            GlStateManager.tryBlendFuncSeparate(GL11.GL_ONE_MINUS_DST_COLOR, GL11.GL_ONE_MINUS_SRC_COLOR, 1, 0);
            GlStateManager.enableAlpha();
            drawTexturedModalRect(width / 2 - 7, height / 2 - 7, 0, 0, 16, 16);
            GlStateManager.tryBlendFuncSeparate(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA, 1, 0);
            GlStateManager.disableBlend();
        }
        post(CROSSHAIRS);
    }
    
    @Override
    protected void renderBossHealth() {
        if (pre(BOSSHEALTH)) { return; }
        bind(Gui.icons);
        GlStateManager.tryBlendFuncSeparate(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA, 1, 0);
        mc.mcProfiler.startSection("bossHealth");
        GlStateManager.enableBlend();
        super.renderBossHealth();
        GlStateManager.disableBlend();
        mc.mcProfiler.endSection();
        post(BOSSHEALTH);
    }

    protected void renderArmor(int width, int height) {
        if (pre(ARMOR)) return;
        mc.mcProfiler.startSection("armor");

        GlStateManager.enableBlend();
        int left = width / 2 - 91;
        int top = height - left_height;

        int level = ForgeHooks.getTotalArmorValue(mc.thePlayer);
        for (int i = 1; level > 0 && i < 20; i += 2) {
            if (i < level) { drawTexturedModalRect(left, top, 34, 9, 9, 9); }
            else if (i == level) { drawTexturedModalRect(left, top, 25, 9, 9, 9); }
            else if (i > level) { drawTexturedModalRect(left, top, 16, 9, 9, 9); }
            left += 8;
        }
        left_height += 10;

        GlStateManager.disableBlend();
        mc.mcProfiler.endSection();
        post(ARMOR);
    }

    protected void renderPortal(ScaledResolution res, float partialTicks) {
        if (pre(PORTAL)) { return; }
        float f1 = mc.thePlayer.prevTimeInPortal + (mc.thePlayer.timeInPortal - mc.thePlayer.prevTimeInPortal) * partialTicks;
        if (f1 > 0.0F) { renderPortal(f1, res); }
        post(PORTAL);
    }

    @Override
    protected void renderTooltip(ScaledResolution res, float partialTicks) {
    	if (pre(HOTBAR)) { return; }
    	if (mc.playerController.isSpectator()) { spectatorGui.renderTooltip(res, partialTicks); }
    	else {
    		if (mc.getRenderViewEntity() instanceof EntityPlayer) {
    			GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
    			mc.getTextureManager().bindTexture(widgetsTexPath);
    			EntityPlayer entityplayer = (EntityPlayer)mc.getRenderViewEntity();
    			int i = res.getScaledWidth() / 2;
    			float f = zLevel;
    			zLevel = -90.0F;
    			
    			//MultiHotbar hotbar = (MultiHotbar) RegisteredSubMods.getMod(SubModType.MULTIHOTBAR);
    			//if (hotbar != null && hotbar.isEnabled()) { hotbar.renderHotbar(); }
    			//else {
    				drawTexturedModalRect(i - 91, res.getScaledHeight() - 22, 0, 0, 182, 22);
    				drawTexturedModalRect(i - 91 - 1 + entityplayer.inventory.currentItem * 20, res.getScaledHeight() - 23, 0, 22, 24, 23);
    			//}
    			
    			zLevel = f;
    			
    			GlStateManager.enableRescaleNormal();
    			GlStateManager.enableBlend();
    			GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
    			RenderHelper.enableGUIStandardItemLighting();
    			//if (hotbar != null && hotbar.isEnabled()) { hotbar.renderHotbarItems(partialTicks); }
    			//else {
    				for (int j = 0; j < 9; j++) {
        				int k = res.getScaledWidth() / 2 - 90 + j * 20 + 2;
        				int l = res.getScaledHeight() - 19;
        				renderHotbarItem(j, k, l, partialTicks, entityplayer);
        			}
    			//}
    			RenderHelper.disableStandardItemLighting();
    			GlStateManager.disableRescaleNormal();
    			GlStateManager.disableBlend();
            }
    	}
    	post(HOTBAR);
    }

    protected void renderAir(int width, int height) {
        if (pre(AIR)) { return; }
        mc.mcProfiler.startSection("air");
        EntityPlayer player = (EntityPlayer)mc.getRenderViewEntity();
        GlStateManager.enableBlend();
        int left = width / 2 + 91;
        int top = height - right_height;

        if (player.isInsideOfMaterial(Material.water)) {
            int air = player.getAir();
            int full = MathHelper.ceiling_double_int((air - 2) * 10.0D / 300.0D);
            int partial = MathHelper.ceiling_double_int(air * 10.0D / 300.0D) - full;

            for (int i = 0; i < full + partial; i++) { drawTexturedModalRect(left - i * 8 - 9, top, (i < full ? 16 : 25), 18, 9, 9); }
            right_height += 10;
        }

        GlStateManager.disableBlend();
        mc.mcProfiler.endSection();
        post(AIR);
    }

    public void renderHealth(int width, int height) {
    	if (pre(HEALTH)) { return; }
        bind(icons);
        mc.mcProfiler.startSection("health");
        GlStateManager.enableBlend();

        EntityPlayer player = (EntityPlayer)mc.getRenderViewEntity();
        int health = MathHelper.ceiling_float_int(player.getHealth());
        boolean highlight = healthUpdateCounter > updateCounter && (healthUpdateCounter - updateCounter) / 3L %2L == 1L;

        if (health < playerHealth && player.hurtResistantTime > 0) {
            lastSystemTime = Minecraft.getSystemTime();
            healthUpdateCounter = updateCounter + 20;
        }
        else if (health > playerHealth && player.hurtResistantTime > 0) {
            lastSystemTime = Minecraft.getSystemTime();
            healthUpdateCounter = updateCounter + 10;
        }

        if (Minecraft.getSystemTime() - lastSystemTime > 1000L) {
            playerHealth = health;
            lastPlayerHealth = health;
            lastSystemTime = Minecraft.getSystemTime();
        }

        playerHealth = health;
        int healthLast = lastPlayerHealth;

        IAttributeInstance attrMaxHealth = player.getEntityAttribute(SharedMonsterAttributes.maxHealth);
        float healthMax = (float)attrMaxHealth.getAttributeValue();
        float absorb = player.getAbsorptionAmount();

        int healthRows = MathHelper.ceiling_float_int((healthMax + absorb) / 2.0F / 10.0F);
        int rowHeight = Math.max(10 - (healthRows - 2), 3);

        rand.setSeed(updateCounter * 312871);

        //MultiHotbar hotbar = (MultiHotbar) RegisteredSubMods.getMod(SubModType.MULTIHOTBAR);
        int hotbarOffset = 0;
        //if (hotbar != null && hotbar.isEnabled()) { hotbarOffset = ((hotbar.numberOfLayers - 1) * 21); }
        
        int left = width / 2 - 91;
        int top = height - left_height - hotbarOffset;
        left_height += (healthRows * rowHeight);
        if (rowHeight != 10) { left_height += 10 - rowHeight; }

        int regen = -1;
        if (player.isPotionActive(Potion.regeneration)) { regen = updateCounter % 25; }

        final int TOP =  9 * (mc.theWorld.getWorldInfo().isHardcoreModeEnabled() ? 5 : 0);
        final int BACKGROUND = (highlight ? 25 : 16);
        int MARGIN = 16;
        if (player.isPotionActive(Potion.poison)) { MARGIN += 36; }
        else if (player.isPotionActive(Potion.wither)) { MARGIN += 72; }
        float absorbRemaining = absorb;

        for (int i = MathHelper.ceiling_float_int((healthMax + absorb) / 2.0F) - 1; i >= 0; --i) {
            //int b0 = (highlight ? 1 : 0);
            int row = MathHelper.ceiling_float_int((i + 1) / 10.0F) - 1;
            int x = left + i % 10 * 8;
            int y = top - row * rowHeight;

            if (health <= 4) { y += rand.nextInt(2); }
            if (i == regen) { y -= 2; }

            drawTexturedModalRect(x, y, BACKGROUND, TOP, 9, 9);

            if (highlight) {
                if (i * 2 + 1 < healthLast) { drawTexturedModalRect(x, y, MARGIN + 54, TOP, 9, 9); } //6
                else if (i * 2 + 1 == healthLast) { drawTexturedModalRect(x, y, MARGIN + 63, TOP, 9, 9); } //7
            }

            if (absorbRemaining > 0.0F) {
                if (absorbRemaining == absorb && absorb % 2.0F == 1.0F) { drawTexturedModalRect(x, y, MARGIN + 153, TOP, 9, 9); } //17
                else { drawTexturedModalRect(x, y, MARGIN + 144, TOP, 9, 9); } //16
                absorbRemaining -= 2.0F;
            }
            else {
                if (i * 2 + 1 < health) { drawTexturedModalRect(x, y, MARGIN + 36, TOP, 9, 9); } //4
                else if (i * 2 + 1 == health) { drawTexturedModalRect(x, y, MARGIN + 45, TOP, 9, 9); } //5
            }
        }

        GlStateManager.disableBlend();
        mc.mcProfiler.endSection();
        post(HEALTH);
    }

    public void renderFood(int width, int height) {
        if (pre(FOOD)) { return; }
        mc.mcProfiler.startSection("food");

        //MultiHotbar hotbar = (MultiHotbar) RegisteredSubMods.getMod(SubModType.MULTIHOTBAR);
        int hotbarOffset = 0;
        //if (hotbar != null && hotbar.isEnabled()) { hotbarOffset = ((hotbar.numberOfLayers - 1) * 21); }
        
        EntityPlayer player = (EntityPlayer)mc.getRenderViewEntity();
        GlStateManager.enableBlend();
        int left = width / 2 + 91;
        int top = height - right_height - hotbarOffset;
        right_height += 10;
        boolean unused = false;// Unused flag in vanilla, seems to be part of a 'fade out' mechanic

        FoodStats stats = mc.thePlayer.getFoodStats();
        int level = stats.getFoodLevel();
        int levelLast = stats.getPrevFoodLevel();

        for (int i = 0; i < 10; ++i) {
            int idx = i * 2 + 1;
            int x = left - i * 8 - 9;
            int y = top;
            int icon = 16;
            byte backgound = 0;

            if (mc.thePlayer.isPotionActive(Potion.hunger)) {
                icon += 36;
                backgound = 13;
            }
            if (unused) backgound = 1; //Probably should be a += 1 but vanilla never uses this

            if (player.getFoodStats().getSaturationLevel() <= 0.0F && updateCounter % (level * 3 + 1) == 0) { y = top + (rand.nextInt(3) - 1); }

            drawTexturedModalRect(x, y, 16 + backgound * 9, 27, 9, 9);

            if (unused) {
                if (idx < levelLast) { drawTexturedModalRect(x, y, icon + 54, 27, 9, 9); }
                else if (idx == levelLast) { drawTexturedModalRect(x, y, icon + 63, 27, 9, 9); }
            }

            if (idx < level) { drawTexturedModalRect(x, y, icon + 36, 27, 9, 9); }
            else if (idx == level) { drawTexturedModalRect(x, y, icon + 45, 27, 9, 9); }
        }
        GlStateManager.disableBlend();
        mc.mcProfiler.endSection();
        post(FOOD);
    }

    protected void renderSleepFade(int width, int height) {
        if (mc.thePlayer.getSleepTimer() > 0) {
            mc.mcProfiler.startSection("sleep");
            GlStateManager.disableDepth();
            GlStateManager.disableAlpha();
            int sleepTime = mc.thePlayer.getSleepTimer();
            float opacity = sleepTime / 100.0F;

            if (opacity > 1.0F) { opacity = 1.0F - (sleepTime - 100) / 10.0F; }

            int color = (int)(220.0F * opacity) << 24 | 1052704;
            drawRect(0, 0, width, height, color);
            GlStateManager.enableAlpha();
            GlStateManager.enableDepth();
            mc.mcProfiler.endSection();
        }
    }

    protected void renderExperience(int width, int height) {
        bind(icons);
        if (pre(EXPERIENCE)) return;
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        GlStateManager.disableBlend();

        if (mc.playerController.gameIsSurvivalOrAdventure()) {
            mc.mcProfiler.startSection("expBar");
            int cap = mc.thePlayer.xpBarCap();
            int left = width / 2 - 91;

            //MultiHotbar hotbar = (MultiHotbar) RegisteredSubMods.getMod(SubModType.MULTIHOTBAR);
            int hotbarOffset = 0;
            //if (hotbar != null && hotbar.isEnabled()) { hotbarOffset = ((hotbar.numberOfLayers - 1) * 21); }
            
            if (cap > 0) {
                short barWidth = 182;
                int filled = (int)(mc.thePlayer.experience * (barWidth + 1));
                int top = height - 29 - hotbarOffset;
                drawTexturedModalRect(left, top, 0, 64, barWidth, 5);

                if (filled > 0) { drawTexturedModalRect(left, top, 0, 69, filled, 5); }
            }

            mc.mcProfiler.endSection();


            if (mc.playerController.gameIsSurvivalOrAdventure() && mc.thePlayer.experienceLevel > 0) {
                mc.mcProfiler.startSection("expLevel");
                boolean flag1 = false;
                int color = flag1 ? 16777215 : 8453920;
                String text = "" + mc.thePlayer.experienceLevel;
                int x = (width - fontRenderer.getStringWidth(text)) / 2;
                int y = height - 35 - hotbarOffset;
                drawString(text, x + 1, y, 0);
                drawString(text, x - 1, y, 0);
                drawString(text, x, y + 1, 0);
                drawString(text, x, y - 1, 0);
                drawString(text, x, y, color);
                mc.mcProfiler.endSection();
            }
        }
        GlStateManager.enableBlend();
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);

        post(EXPERIENCE);
    }

    protected void renderJumpBar(int width, int height) {
        bind(icons);
        if (pre(JUMPBAR)) return;
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        GlStateManager.disableBlend();

        mc.mcProfiler.startSection("jumpBar");
        float charge = mc.thePlayer.getHorseJumpPower();
        final int barWidth = 182;
        int x = (width / 2) - (barWidth / 2);
        int filled = (int)(charge * (barWidth + 1));
        int top = height - 32 + 3;

        //MultiHotbar hotbar = (MultiHotbar) RegisteredSubMods.getMod(SubModType.MULTIHOTBAR);
        int hotbarOffset = 0;
        //if (hotbar != null && hotbar.isEnabled()) { hotbarOffset = ((hotbar.numberOfLayers - 1) * 21); }
        
        drawTexturedModalRect(x, top - hotbarOffset, 0, 84, barWidth, 5);

        if (filled > 0) { drawTexturedModalRect(x, top - hotbarOffset, 0, 89, filled, 5); }

        GlStateManager.enableBlend();
        mc.mcProfiler.endSection();
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);

        post(JUMPBAR);
    }

    protected void renderToolHightlight(ScaledResolution res) {
        if (mc.gameSettings.heldItemTooltips && !mc.playerController.isSpectator()) {
            mc.mcProfiler.startSection("toolHighlight");

            if (remainingHighlightTicks > 0 && highlightingItemStack != null) {
                String name = highlightingItemStack.getDisplayName();
                if (highlightingItemStack.hasDisplayName()) { name = EnumChatFormatting.ITALIC + name; }

                name = highlightingItemStack.getItem().getHighlightTip(highlightingItemStack, name);

                int opacity = (int)(remainingHighlightTicks * 256.0F / 10.0F);
                if (opacity > 255) { opacity = 255; }

                if (opacity > 0) {
                    int y = res.getScaledHeight() - 59;
                    if (!mc.playerController.shouldDrawHUD()) { y += 14; }

                    GlStateManager.pushMatrix();
                    GlStateManager.enableBlend();
                    GlStateManager.tryBlendFuncSeparate(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA, 1, 0);
                    FontRenderer font = highlightingItemStack.getItem().getFontRenderer(highlightingItemStack);
                    //MultiHotbar hotbar = (MultiHotbar) RegisteredSubMods.getMod(SubModType.MULTIHOTBAR);
                    int hotbarOffset = 0;
        			//if (hotbar != null && hotbar.isEnabled()) { hotbarOffset = -((hotbar.numberOfLayers - 1) * 21); }
                    if (font != null) {
                        int x = (res.getScaledWidth() - font.getStringWidth(name)) / 2;
                        font.drawStringWithShadow(name, x, y + hotbarOffset, 0xffffff | (opacity << 24));
                    } else {
                        int x = (res.getScaledWidth() - fontRenderer.getStringWidth(name)) / 2;
                        drawStringWithShadow(name, x, y + hotbarOffset, 0xffffff | (opacity << 24));
                    }
                    GlStateManager.disableBlend();
                    GlStateManager.popMatrix();
                }
            }

            mc.mcProfiler.endSection();
        }
        else if (mc.thePlayer.isSpectator()) { spectatorGui.func_175263_a(res); }
    }

    protected void renderHUDText(int width, int height) {
        mc.mcProfiler.startSection("forgeHudText");
        OpenGlHelper.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA, 1, 0);
        ArrayList<String> listL = new ArrayList<String>();
        ArrayList<String> listR = new ArrayList<String>();

        if (mc.isDemo()) {
            long time = mc.theWorld.getTotalWorldTime();
            if (time >= 120500L) { listR.add(I18n.format("demo.demoExpired")); }
            else { listR.add(I18n.format("demo.remainingTime", StringUtils.ticksToElapsedTime((int)(120500L - time)))); }
        }

        if (mc.gameSettings.showDebugInfo && !pre(DEBUG)) {
            listL.addAll(debugOverlay.getLeft());
            listR.addAll(debugOverlay.getRight());
            post(DEBUG);
        }

        RenderGameOverlayEvent.Text event = new RenderGameOverlayEvent.Text(eventParent, listL, listR);
        if (!MinecraftForge.EVENT_BUS.post(event)) {
            int top = 2;
            for (String msg : listL) {
                if (msg == null) continue;
                drawRect(1, top - 1, 2 + fontRenderer.getStringWidth(msg) + 1, top + fontRenderer.FONT_HEIGHT - 1, -1873784752);
                drawStringWithShadow(msg, 2, top, 14737632);
                top += fontRenderer.FONT_HEIGHT;
            }

            top = 2;
            for (String msg : listR) {
                if (msg == null) continue;
                int w = fontRenderer.getStringWidth(msg);
                int left = width - 2 - w;
                drawRect(left - 1, top - 1, left + w + 1, top + fontRenderer.FONT_HEIGHT - 1, -1873784752);
                drawString(msg, left, top, 14737632);
                top += fontRenderer.FONT_HEIGHT;
            }
        }

        mc.mcProfiler.endSection();
        post(TEXT);
    }

    protected void renderRecordOverlay(int width, int height, float partialTicks) {
        if (recordPlayingUpFor > 0) {
            mc.mcProfiler.startSection("overlayMessage");
            float hue = recordPlayingUpFor - partialTicks;
            int opacity = (int)(hue * 256.0F / 20.0F);
            if (opacity > 255) { opacity = 255; }

            if (opacity > 0) {
                GlStateManager.pushMatrix();
                GlStateManager.translate(width / 2, height - 68, 0.0F);
                GlStateManager.enableBlend();
                GlStateManager.tryBlendFuncSeparate(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA, 1, 0);
                int color = (recordIsPlaying ? Color.HSBtoRGB(hue / 50.0F, 0.7F, 0.6F) & 0xffffff : 0xffffff);
                //MultiHotbar hotbar = (MultiHotbar) RegisteredSubMods.getMod(SubModType.MULTIHOTBAR);
                int hotbarOffset = 0;
                //if (hotbar != null && hotbar.isEnabled()) {hotbarOffset = -((hotbar.numberOfLayers - 1) * 21); }
                drawString(recordPlaying, -fontRenderer.getStringWidth(recordPlaying) / 2, -4 - hotbarOffset, color | (opacity << 24));
                GlStateManager.disableBlend();
                GlStateManager.popMatrix();
            }

            mc.mcProfiler.endSection();
        }
    }

    protected void renderTitle(int width, int height, float partialTicks) {
        if (field_175195_w > 0) {
            mc.mcProfiler.startSection("titleAndSubtitle");
            float age = field_175195_w - partialTicks;
            int opacity = 255;

            if (field_175195_w > field_175193_B + field_175192_A) {
                float f3 = field_175199_z + field_175192_A + field_175193_B - age;
                opacity = (int)(f3 * 255.0F / field_175199_z);
            }
            if (field_175195_w <= field_175193_B) { opacity = (int)(age * 255.0F / field_175193_B); }

            opacity = MathHelper.clamp_int(opacity, 0, 255);

            if (opacity > 8) {
                GlStateManager.pushMatrix();
                GlStateManager.translate(width / 2, height / 2, 0.0F);
                GlStateManager.enableBlend();
                GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
                GlStateManager.pushMatrix();
                GlStateManager.scale(4.0F, 4.0F, 4.0F);
                int l = opacity << 24 & -16777216;
                getFontRenderer().drawString(field_175201_x, -getFontRenderer().getStringWidth(field_175201_x) / 2, -10.0F, 16777215 | l, true);
                GlStateManager.popMatrix();
                GlStateManager.pushMatrix();
                GlStateManager.scale(2.0F, 2.0F, 2.0F);
                getFontRenderer().drawString(field_175200_y, -getFontRenderer().getStringWidth(field_175200_y) / 2, 5.0F, 16777215 | l, true);
                GlStateManager.popMatrix();
                GlStateManager.disableBlend();
                GlStateManager.popMatrix();
            }

            mc.mcProfiler.endSection();
        }
    }

    protected void renderChat(int width, int height) {
        mc.mcProfiler.startSection("chat");

        RenderGameOverlayEvent.Chat event = new RenderGameOverlayEvent.Chat(eventParent, 0, height - 48);
        if (MinecraftForge.EVENT_BUS.post(event)) { return; }
        
        GlStateManager.pushMatrix();
        GlStateManager.translate(event.posX, event.posY, 0.0F);
        persistantModdedChatGUI.drawChat(updateCounter);
        GlStateManager.popMatrix();
        
        post(CHAT);

        mc.mcProfiler.endSection();
    }

    protected void renderPlayerList(int width, int height) {
        ScoreObjective scoreobjective = mc.theWorld.getScoreboard().getObjectiveInDisplaySlot(0);
        NetHandlerPlayClient handler = mc.thePlayer.sendQueue;

        if (mc.gameSettings.keyBindPlayerList.isKeyDown() && (!mc.isIntegratedServerRunning() || handler.getPlayerInfoMap().size() > 1 || scoreobjective != null)) {
            tabList.updatePlayerList(true);
            if (pre(PLAYER_LIST)) return;
            tabList.renderPlayerlist(width, mc.theWorld.getScoreboard(), scoreobjective);
            post(PLAYER_LIST);
        }
        else { tabList.updatePlayerList(false); }
    }

    protected void renderHealthMount(int width, int height) {
        EntityPlayer player = (EntityPlayer) mc.getRenderViewEntity();
        Entity tmp = player.ridingEntity;
        if (!(tmp instanceof EntityLivingBase)) { return; }

        bind(icons);

        if (pre(HEALTHMOUNT)) { return; }

        boolean unused = false;
        int left_align = width / 2 + 91;

        mc.mcProfiler.endStartSection("mountHealth");
        GlStateManager.enableBlend();
        EntityLivingBase mount = (EntityLivingBase)tmp;
        int health = (int)Math.ceil(mount.getHealth());
        float healthMax = mount.getMaxHealth();
        int hearts = (int)(healthMax + 0.5F) / 2;

        if (hearts > 30) hearts = 30;

        final int MARGIN = 52;
        final int BACKGROUND = MARGIN + (unused ? 1 : 0);
        final int HALF = MARGIN + 45;
        final int FULL = MARGIN + 36;

        //MultiHotbar hotbar = (MultiHotbar) RegisteredSubMods.getMod(SubModType.MULTIHOTBAR);
        int hotbarOffset = 0;
        //if (hotbar != null && hotbar.isEnabled()) { hotbarOffset = -((hotbar.numberOfLayers - 1) * 21); }
        
        for (int heart = 0; hearts > 0; heart += 20) {
            int top = height - right_height;

            int rowCount = Math.min(hearts, 10);
            hearts -= rowCount;

            for (int i = 0; i < rowCount; i++) {
                int x = left_align - i * 8 - 9;
                drawTexturedModalRect(x, top - hotbarOffset, BACKGROUND, 9, 9, 9);

                if (i * 2 + 1 + heart < health) { drawTexturedModalRect(x, top - hotbarOffset, FULL, 9, 9, 9); }
                else if (i * 2 + 1 + heart == health) { drawTexturedModalRect(x, top - hotbarOffset, HALF, 9, 9, 9); }
            }

            right_height += 10;
        }
        GlStateManager.disableBlend();
        post(HEALTHMOUNT);
    }
    
    @Override
    protected void renderScoreboard(ScoreObjective p_180475_1_, ScaledResolution p_180475_2_) {
        Scoreboard scoreboard = p_180475_1_.getScoreboard();
        Collection<Score> collection = scoreboard.getSortedScores(p_180475_1_);
        List<Score> list = Lists.newArrayList(Iterables.filter(collection, new Predicate<Score>() {
            public boolean apply(Score p_apply_1_) {
                return p_apply_1_.getPlayerName() != null && !p_apply_1_.getPlayerName().startsWith("#");
            }
        }));

        if (list.size() > 15) { collection = Lists.newArrayList(Iterables.skip(list, collection.size() - 15)); }
        else { collection = list; }

        int i = getFontRenderer().getStringWidth(p_180475_1_.getDisplayName());

        for (Score score : collection) {
            ScorePlayerTeam scoreplayerteam = scoreboard.getPlayersTeam(score.getPlayerName());
            String s = ScorePlayerTeam.formatPlayerName(scoreplayerteam, score.getPlayerName()) + ": " + EnumChatFormatting.RED + score.getScorePoints();
            i = Math.max(i, getFontRenderer().getStringWidth(s));
        }

        int i1 = collection.size() * getFontRenderer().FONT_HEIGHT;
        int j1 = p_180475_2_.getScaledHeight() / 2 + i1 / 3;
        int k1 = 3;
        int l1 = p_180475_2_.getScaledWidth() - i - k1;
        int j = 0;

        for (Score score1 : collection) {
            j++;
            ScorePlayerTeam scoreplayerteam1 = scoreboard.getPlayersTeam(score1.getPlayerName());
            String s1 = ScorePlayerTeam.formatPlayerName(scoreplayerteam1, score1.getPlayerName());
            String s2 = EnumChatFormatting.RED + "" + score1.getScorePoints();
            int k = j1 - j * getFontRenderer().FONT_HEIGHT;
            int l = p_180475_2_.getScaledWidth() - k1 + 2;
            drawRect(l1 - 2, k, l, k + getFontRenderer().FONT_HEIGHT, 1342177280);
            getFontRenderer().drawString(s1, l1, k, 553648127);
            getFontRenderer().drawString(s2, l - getFontRenderer().getStringWidth(s2), k, 553648127);

            if (j == collection.size()) {
                String s3 = p_180475_1_.getDisplayName();
                drawRect(l1 - 2, k - getFontRenderer().FONT_HEIGHT - 1, l, k - 1, 1610612736);
                drawRect(l1 - 2, k - 1, l, k, 1342177280);
                getFontRenderer().drawString(s3, l1 + i / 2 - getFontRenderer().getStringWidth(s3) / 2, k - getFontRenderer().FONT_HEIGHT, 553648127);
            }
        }
    }

	protected boolean pre(ElementType type) { return MinecraftForge.EVENT_BUS.post(new RenderGameOverlayEvent.Pre(eventParent, type)); }
	protected void post(ElementType type) { MinecraftForge.EVENT_BUS.post(new RenderGameOverlayEvent.Post(eventParent, type)); }
	protected void bind(ResourceLocation res) { mc.getTextureManager().bindTexture(res); }
	public ScaledResolution getResolution() { return res; }

	protected int drawString(String text, int x, int y, int color) { return fontRenderer.drawStringI(text, x, y, color); }
    protected int drawCenteredString(String text, int x, int y, int color) { return fontRenderer.drawStringI(text, x - fontRenderer.getStringWidth(text) / 2, y, color); }
	protected int drawStringWithShadow(String text, int x, int y, int color) { return fontRenderer.drawStringWithShadowI(text, x, y, color); }
	protected int drawCenteredStringWithShadow(String text, int x, int y, int color) { return fontRenderer.drawStringWithShadowI(text, x - fontRenderer.getStringWidth(text) / 2, y, color); }
	protected int drawString(String text, int x, int y, int color, float height) { return fontRenderer.drawStringIH(text, x, y, color, height); }
    protected int drawCenteredString(String text, int x, int y, int color, float height) { return fontRenderer.drawStringIH(text, x - fontRenderer.getStringWidth(text) / 2, y, color, height); }
	protected int drawStringWithShadow(String text, int x, int y, int color, float height) { return fontRenderer.drawStringWithShadowIH(text, x, y, color, height); }
	protected int drawCenteredStringWithShadow(String text, int x, int y, int color, float height) { return fontRenderer.drawStringWithShadowIH(text, x - fontRenderer.getStringWidth(text) / 2, y, color, height); }
	
	@Override public GuiNewChat getChatGUI() { return persistantModdedChatGUI; }
	@Override public GuiPlayerTabOverlay getTabList() { return tabList; }
	@Override public void func_181029_i() { tabList.func_181030_a(); }
	
	public ModdedInGameGui setTabList(GuiPlayerTabOverlay tabListIn) { tabList = tabListIn; return this; }
	
	public static boolean isCtrlKeyDown() { return Minecraft.isRunningOnMac ? Keyboard.isKeyDown(219) || Keyboard.isKeyDown(220) : Keyboard.isKeyDown(29) || Keyboard.isKeyDown(157); }
    public static boolean isShiftKeyDown() { return Keyboard.isKeyDown(42) || Keyboard.isKeyDown(54); }
    public static boolean isAltKeyDown() { return Keyboard.isKeyDown(56) || Keyboard.isKeyDown(184); }
	
    protected class GuiOverlayDebugForge extends GuiOverlayDebug {
        private GuiOverlayDebugForge(Minecraft mc){ super(mc); }
        @Override protected void renderDebugInfoLeft(){}
        @Override protected void renderDebugInfoRight(ScaledResolution res){}
        private List<String> getLeft(){ return call(); }
        private List<String> getRight(){ return getDebugInfoRight(); }
    }
}
