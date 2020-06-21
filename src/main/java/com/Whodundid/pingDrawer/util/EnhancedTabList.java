package com.Whodundid.pingDrawer.util;

import java.util.Comparator;
import java.util.List;
import com.Whodundid.core.EnhancedMC;
import com.Whodundid.core.coreApp.CoreApp;
import com.Whodundid.core.renderer.renderUtil.IRendererProxy;
import com.Whodundid.core.renderer.taskView.TaskBar;
import com.Whodundid.core.util.renderUtil.GLObject;
import com.Whodundid.pingDrawer.PingApp;
import com.google.common.collect.ComparisonChain;
import com.google.common.collect.Ordering;
import com.mojang.authlib.GameProfile;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiIngame;
import net.minecraft.client.gui.GuiPlayerTabOverlay;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EnumPlayerModelParts;
import net.minecraft.scoreboard.IScoreObjectiveCriteria;
import net.minecraft.scoreboard.ScoreObjective;
import net.minecraft.scoreboard.ScorePlayerTeam;
import net.minecraft.scoreboard.Scoreboard;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.IChatComponent;
import net.minecraft.util.MathHelper;
import net.minecraft.world.WorldSettings;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class EnhancedTabList extends GuiPlayerTabOverlay {

	Minecraft mc = Minecraft.getMinecraft();
	PingApp mod = null;
	GuiIngame gig;
	private static final Ordering<NetworkPlayerInfo> field_175252_a = Ordering.from(new PlayerComparator());
	private IChatComponent footer;
    private IChatComponent header;
    private long lastTimeOpened;
    private boolean isBeingRendered;

	public EnhancedTabList(PingApp modIn, GuiIngame guiIn) {
		super(Minecraft.getMinecraft(), guiIn);
		mod = modIn;
		gig = guiIn;
	}

	@Override
	public void renderPlayerlist(int width, Scoreboard scoreboardIn, ScoreObjective scoreObjectiveIn) {
		NetHandlerPlayClient nethandlerplayclient = mc.thePlayer.sendQueue;
		List<NetworkPlayerInfo> list = field_175252_a.<NetworkPlayerInfo>sortedCopy(nethandlerplayclient.getPlayerInfoMap());
		int i = 0;
		int j = 0;

		for (NetworkPlayerInfo networkplayerinfo : list) {
			int k = mc.fontRendererObj.getStringWidth(getPlayerName(networkplayerinfo));
			i = Math.max(i, k);

			if (scoreObjectiveIn != null && scoreObjectiveIn.getRenderType() != IScoreObjectiveCriteria.EnumRenderType.HEARTS) {
				k = mc.fontRendererObj.getStringWidth(" " + scoreboardIn.getValueFromObjective(networkplayerinfo.getGameProfile().getName(), scoreObjectiveIn).getScorePoints());
				j = Math.max(j, k);
			}
		}

		list = list.subList(0, Math.min(list.size(), 80));
		int l3 = list.size();
		int i4 = l3;
		int j4;

		for (j4 = 1; i4 > 20; i4 = (l3 + j4 - 1) / j4) { ++j4; }

		boolean flag = mc.isIntegratedServerRunning() || mc.getNetHandler().getNetworkManager().getIsencrypted();
		int l;

		if (scoreObjectiveIn != null) {
			if (scoreObjectiveIn.getRenderType() == IScoreObjectiveCriteria.EnumRenderType.HEARTS) { l = 90; }
			else { l = j; }
		}
		else { l = 0; }

		int i1 = (Math.min(j4 * ((flag ? 9 : 0) + i + l + 13), width - 50) / j4) + 20;
		int j1 = width / 2 - (i1 * j4 + (j4 - 1) * 5) / 2;
		int k1 = 10;
		int l1 = i1 * j4 + (j4 - 1) * 5;
		List<String> list1 = null;
		List<String> list2 = null;

		if (header != null) {
			list1 = mc.fontRendererObj.listFormattedStringToWidth(header.getFormattedText(), width - 50);

			for (String s : list1) {
				l1 = Math.max(l1, mc.fontRendererObj.getStringWidth(s));
			}
		}

		if (footer != null) {
			list2 = mc.fontRendererObj.listFormattedStringToWidth(footer.getFormattedText(), width - 50);

			for (String s2 : list2) {
				l1 = Math.max(l1, mc.fontRendererObj.getStringWidth(s2));
			}
		}

		if (list1 != null) {
			drawRect(width / 2 - l1 / 2 - 1, k1 - 1, width / 2 + l1 / 2 + 1, k1 + list1.size() * mc.fontRendererObj.FONT_HEIGHT, Integer.MIN_VALUE);

			for (String s3 : list1) {
				int i2 = mc.fontRendererObj.getStringWidth(s3);
				mc.fontRendererObj.drawStringWithShadow(s3, width / 2 - i2 / 2, k1, -1);
				k1 += mc.fontRendererObj.FONT_HEIGHT;
			}

			++k1;
		}
		
		boolean hud = Minecraft.getMinecraft().currentScreen instanceof IRendererProxy;
		TaskBar taskbar = EnhancedMC.getRenderer().getTaskBar();
		boolean border = CoreApp.drawHudBorder.get();
		
		if (hud) {
			k1 += (taskbar != null ? taskbar.height : 0);
			k1 += (border ? 2 : 0);
		}

		drawRect(width / 2 - l1 / 2 - 1, k1 - 1, width / 2 + l1 / 2 + 1, k1 + i4 * 9, Integer.MIN_VALUE);

		for (int k4 = 0; k4 < l3; ++k4) {
			int l4 = k4 / i4;
			int i5 = k4 % i4;
			int j2 = j1 + l4 * i1 + l4 * 5;
			int k2 = k1 + i5 * 9;
			drawRect(j2, k2, j2 + i1, k2 + 8, 553648127);
			GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
			GlStateManager.enableAlpha();
			GlStateManager.enableBlend();
			GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);

			if (k4 < list.size()) {
				NetworkPlayerInfo networkplayerinfo1 = list.get(k4);
				String s1 = getPlayerName(networkplayerinfo1);
				GameProfile gameprofile = networkplayerinfo1.getGameProfile();

				if (flag) {
					EntityPlayer entityplayer = mc.theWorld.getPlayerEntityByUUID(gameprofile.getId());
					boolean flag1 = entityplayer != null && entityplayer.isWearing(EnumPlayerModelParts.CAPE) && (gameprofile.getName().equals("Dinnerbone") || gameprofile.getName().equals("Grumm"));
					mc.getTextureManager().bindTexture(networkplayerinfo1.getLocationSkin());
					int l2 = 8 + (flag1 ? 8 : 0);
					int i3 = 8 * (flag1 ? -1 : 1);
					Gui.drawScaledCustomSizeModalRect(j2, k2, 8.0F, l2, 8, i3, 8, 8, 64.0F, 64.0F);

					if (entityplayer != null && entityplayer.isWearing(EnumPlayerModelParts.HAT)) {
						int j3 = 8 + (flag1 ? 8 : 0);
						int k3 = 8 * (flag1 ? -1 : 1);
						Gui.drawScaledCustomSizeModalRect(j2, k2, 40.0F, j3, 8, k3, 8, 8, 64.0F, 64.0F);
					}

					j2 += 9;
				}

				if (networkplayerinfo1.getGameType() == WorldSettings.GameType.SPECTATOR) {
					s1 = EnumChatFormatting.ITALIC + s1;
					mc.fontRendererObj.drawStringWithShadow(s1, j2, k2, -1862270977);
				}
				else {
					mc.fontRendererObj.drawStringWithShadow(s1, j2, k2, -1);
				}

				if (scoreObjectiveIn != null && networkplayerinfo1.getGameType() != WorldSettings.GameType.SPECTATOR) {
					int k5 = j2 + i + 1;
					int l5 = k5 + l;

					if (l5 - k5 > 5) {
						drawScoreboardValues(scoreObjectiveIn, k2, gameprofile.getName(), k5, l5, networkplayerinfo1);
					}
				}

				drawPing(i1, j2 - (flag ? 9 : 0), k2, networkplayerinfo1);
			}
		}

		if (list2 != null) {
			k1 = k1 + i4 * 9 + 1;
			drawRect(width / 2 - l1 / 2 - 1, k1 - 1, width / 2 + l1 / 2 + 1, k1 + list2.size() * mc.fontRendererObj.FONT_HEIGHT, Integer.MIN_VALUE);

			for (String s4 : list2) {
				int j5 = mc.fontRendererObj.getStringWidth(s4);
				mc.fontRendererObj.drawStringWithShadow(s4, width / 2 - j5 / 2, k1, -1);
				k1 += mc.fontRendererObj.FONT_HEIGHT;
			}
		}
	}

	@Override
	protected void drawPing(int rowWidth, int listStartX, int rowYPos, NetworkPlayerInfo networkPlayerInfoIn) {
		GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
		
		int latency = networkPlayerInfoIn.getResponseTime();
		int startX = listStartX + rowWidth - 25;
		int endX = listStartX + rowWidth;
		int startY = rowYPos;
		int endY = rowYPos + mc.fontRendererObj.FONT_HEIGHT - 1;
		int pingColor = mod.getPingColor(latency);
		
		drawRect(startX, startY, startX + 1, endY, 0xff000000);
		drawRect(endX - 1, startY, endX, endY, 0xff000000);
		drawRect(startX, startY, endX, endY, 0x88000000); //background
		GLObject.drawCenteredStringWithShadow("" + latency, startX + 13, rowYPos, pingColor);
	}

	private void drawScoreboardValues(ScoreObjective p_175247_1_, int p_175247_2_, String p_175247_3_, int p_175247_4_, int p_175247_5_, NetworkPlayerInfo p_175247_6_) {
		int i = p_175247_1_.getScoreboard().getValueFromObjective(p_175247_3_, p_175247_1_).getScorePoints();

		if (p_175247_1_.getRenderType() == IScoreObjectiveCriteria.EnumRenderType.HEARTS) {
			mc.getTextureManager().bindTexture(icons);

			if (lastTimeOpened == p_175247_6_.func_178855_p()) {
				if (i < p_175247_6_.func_178835_l()) {
					p_175247_6_.func_178846_a(Minecraft.getSystemTime());
					p_175247_6_.func_178844_b(gig.getUpdateCounter() + 20);
				}
				else if (i > p_175247_6_.func_178835_l()) {
					p_175247_6_.func_178846_a(Minecraft.getSystemTime());
					p_175247_6_.func_178844_b(gig.getUpdateCounter() + 10);
				}
			}

			if (Minecraft.getSystemTime() - p_175247_6_.func_178847_n() > 1000L || lastTimeOpened != p_175247_6_.func_178855_p()) {
				p_175247_6_.func_178836_b(i);
				p_175247_6_.func_178857_c(i);
				p_175247_6_.func_178846_a(Minecraft.getSystemTime());
			}

			p_175247_6_.func_178843_c(lastTimeOpened);
			p_175247_6_.func_178836_b(i);
			int j = MathHelper.ceiling_float_int(Math.max(i, p_175247_6_.func_178860_m()) / 2.0F);
			int k = Math.max(MathHelper.ceiling_float_int(i / 2), Math.max(MathHelper.ceiling_float_int(p_175247_6_.func_178860_m() / 2), 10));
			boolean flag = p_175247_6_.func_178858_o() > gig.getUpdateCounter() && (p_175247_6_.func_178858_o() - gig.getUpdateCounter()) / 3L % 2L == 1L;

			if (j > 0) {
				float f = Math.min((float) (p_175247_5_ - p_175247_4_ - 4) / (float) k, 9.0F);

				if (f > 3.0F) {
					for (int l = j; l < k; ++l) { drawTexturedModalRect(p_175247_4_ + l * f, p_175247_2_, flag ? 25 : 16, 0, 9, 9); }

					for (int j1 = 0; j1 < j; ++j1) { drawTexturedModalRect(p_175247_4_ + j1 * f, p_175247_2_, flag ? 25 : 16, 0, 9, 9);

						if (flag) {
							if (j1 * 2 + 1 < p_175247_6_.func_178860_m()) { drawTexturedModalRect(p_175247_4_ + j1 * f, p_175247_2_, 70, 0, 9, 9); }
							if (j1 * 2 + 1 == p_175247_6_.func_178860_m()) { drawTexturedModalRect(p_175247_4_ + j1 * f, p_175247_2_, 79, 0, 9, 9); }
						}

						if (j1 * 2 + 1 < i) { drawTexturedModalRect(p_175247_4_ + j1 * f, p_175247_2_, j1 >= 10 ? 160 : 52, 0, 9, 9); }
						if (j1 * 2 + 1 == i) { drawTexturedModalRect(p_175247_4_ + j1 * f, p_175247_2_, j1 >= 10 ? 169 : 61, 0, 9, 9); }
					}
				}
				else {
					float f1 = MathHelper.clamp_float(i / 20.0F, 0.0F, 1.0F);
					int i1 = (int) ((1.0F - f1) * 255.0F) << 16 | (int) (f1 * 255.0F) << 8;
					String s = "" + i / 2.0F;

					if (p_175247_5_ - mc.fontRendererObj.getStringWidth(s + "hp") >= p_175247_4_) {
						s = s + "hp";
					}

					mc.fontRendererObj.drawStringWithShadow(s, (p_175247_5_ + p_175247_4_) / 2 - mc.fontRendererObj.getStringWidth(s) / 2, p_175247_2_, i1);
				}
			}
		}
		else {
			String s1 = EnumChatFormatting.YELLOW + "" + i;
			mc.fontRendererObj.drawStringWithShadow(s1, p_175247_5_ - mc.fontRendererObj.getStringWidth(s1), p_175247_2_, 16777215);
		}
	}
	
	public void setFooter(IChatComponent footerIn) { footer = footerIn; }
	public void setHeader(IChatComponent headerIn) { header = headerIn; }
	public void func_181030_a() { header = null; footer = null; }

	@SideOnly(Side.CLIENT)
	static class PlayerComparator implements Comparator<NetworkPlayerInfo> {
	private PlayerComparator() {}

	public int compare(NetworkPlayerInfo p_compare_1_, NetworkPlayerInfo p_compare_2_) {
		ScorePlayerTeam scoreplayerteam = p_compare_1_.getPlayerTeam();
		ScorePlayerTeam scoreplayerteam1 = p_compare_2_.getPlayerTeam();
		return ComparisonChain.start().compareTrueFirst(p_compare_1_.getGameType() != WorldSettings.GameType.SPECTATOR, p_compare_2_.getGameType() != WorldSettings.GameType.SPECTATOR)
					.compare(scoreplayerteam != null ? scoreplayerteam.getRegisteredName() : "", scoreplayerteam1 != null ? scoreplayerteam1.getRegisteredName() : "")
					.compare(p_compare_1_.getGameProfile().getName(), p_compare_2_.getGameProfile().getName()).result();
		}
	}
	
}
