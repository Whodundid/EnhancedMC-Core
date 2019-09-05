package com.Whodundid.core.util;

import com.Whodundid.core.events.emcEvents.ChatLineCreatedEvent;
import com.Whodundid.core.util.chatUtil.EChatUtil;
import com.Whodundid.core.util.chatUtil.TimedChatLine;
import com.Whodundid.core.util.miscUtil.ChatBuilder;
import com.Whodundid.core.util.storageUtil.EArrayList;
import java.util.Iterator;
import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ChatLine;
import net.minecraft.client.gui.GuiChat;
import net.minecraft.client.gui.GuiNewChat;
import net.minecraft.client.gui.GuiUtilRenderComponents;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.IChatComponent;
import net.minecraft.util.MathHelper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@SideOnly(Side.CLIENT)
public class ModdedGuiNewChat extends GuiNewChat {
	
	protected static Minecraft mc;
	protected static final Logger logger = LogManager.getLogger();
	public int historyLength = 1000;
	public final EArrayList<String> sentMessages = new EArrayList<String>();
	public final EArrayList<TimedChatLine> totalLines = new EArrayList<TimedChatLine>();
	public final EArrayList<TimedChatLine> chatHistory = new EArrayList<TimedChatLine>();
	public int scrollPos;
	public boolean isScrolled;
	
	public ModdedGuiNewChat(Minecraft mcIn) {
		super(mcIn);
		mc = mcIn;
	}

	public void drawChat(int updateCounterIn) {
		if (mc.gameSettings.chatVisibility != EntityPlayer.EnumChatVisibility.HIDDEN && totalLines != null) {
			int i = getLineCount();
			boolean flag = false;
			int j = 0;
			int k = totalLines.size();
			float f = mc.gameSettings.chatOpacity * 0.9F + 0.1F;
			if (k > 0) {
				flag = getChatOpen();
				float f1 = getChatScale();
				int l = MathHelper.ceiling_float_int(getChatWidth() / f1);
				GlStateManager.pushMatrix();
				GlStateManager.translate(2.0F, 20.0F, 0.0F);
				GlStateManager.scale(f1, f1, 1.0F);
				for (int i1 = 0; i1 + scrollPos < totalLines.size() && i1 < i; ++i1) {
					//System.out.println(i1 + scrollPos);
					ChatLine chatline = totalLines.get(i1 + scrollPos);
					if (chatline != null) {
						int j1 = updateCounterIn - chatline.getUpdatedCounter();
						if (j1 < 200 || flag) {
							double d0 = j1 / 200.0D;
							d0 = 1.0D - d0;
							d0 = d0 * 10.0D;
							d0 = MathHelper.clamp_double(d0, 0.0D, 1.0D);
							d0 = d0 * d0;
							int l1 = (int) (255.0D * d0);
							if (flag) {
								l1 = 255;
							}
							l1 = (int) (l1 * f);
							++j;
							if (l1 > 3) {
								int i2 = 0;
								int j2 = -i1 * 9;
								drawRect(i2, j2 - 9, i2 + l + 4, j2, l1 / 2 << 24);
								String s = chatline.getChatComponent().getFormattedText();
								GlStateManager.enableBlend();
								mc.fontRendererObj.drawStringWithShadow(s, i2, j2 - 8, 16777215 + (l1 << 24));
								GlStateManager.disableAlpha();
								GlStateManager.disableBlend();
							}
						}
					}
				}
				if (flag) {
					int k2 = mc.fontRendererObj.FONT_HEIGHT;
					GlStateManager.translate(-3.0F, 0.0F, 0.0F);
					int l2 = k * k2 + k;
					int i3 = j * k2 + j;
					int j3 = scrollPos * i3 / k;
					int k1 = i3 * i3 / l2;
					if (l2 != i3) {
						int k3 = j3 > 0 ? 170 : 96;
						int l3 = isScrolled ? 13382451 : 3355562;
						drawRect(0, -j3, 2, -j3 - k1, l3 + (k3 << 24));
						drawRect(2, -j3, 1, -j3 - k1, 13421772 + (k3 << 24));
					}
				}
				GlStateManager.popMatrix();
			}
		}
	}

	public void clearChatMessages() {
		chatHistory.clear();
		totalLines.clear();
		sentMessages.clear();
	}
	
	public void printChatMessageWithOptionalDeletion(IChatComponent chatComponent, int chatLineId) {
		String logPrint = chatComponent.getUnformattedText();
		ChatComponentTranslation ret = new ChatComponentTranslation("commands.generic.notFound");
        ret.getChatStyle().setColor(EnumChatFormatting.RED);
		if (!EChatUtil.getLastTypedMessage().isEmpty() && chatComponent.getFormattedText().equals(ret.getFormattedText())) {
			IChatComponent noCommand = ChatBuilder.of(EnumChatFormatting.RED + "Command: " +
						EnumChatFormatting.YELLOW + EChatUtil.getLastTypedMessage() + 
						EnumChatFormatting.RED +  " not found. Try /help for a list of commands").build();
			logPrint = noCommand.getUnformattedText();
			setChatLine(noCommand, chatLineId, mc.ingameGUI.getUpdateCounter(), false);
			EChatUtil.setLastTypedMessage("");
		}
		else {
			setChatLine(chatComponent, chatLineId, mc.ingameGUI.getUpdateCounter(), false);
		}
		logger.info("[CHAT] " + logPrint);
	}

	private void setChatLine(IChatComponent chatComponent, int chatLineId, int updateCounter, boolean deletion) {
		if (chatLineId != 0) { deleteChatLine(chatLineId); }
		int i = MathHelper.floor_float(getChatWidth() / getChatScale());
		List<IChatComponent> list = GuiUtilRenderComponents.func_178908_a(chatComponent, i, mc.fontRendererObj, false, false);
		boolean flag = getChatOpen();
		for (IChatComponent ichatcomponent : list) {
			if (flag && scrollPos > 0) {
				isScrolled = true;
				scroll(1);
			}
			TimedChatLine l = new TimedChatLine(updateCounter, ichatcomponent, chatLineId);
			totalLines.add(0, l);
		}
		while (chatHistory.size() > historyLength) { chatHistory.remove(chatHistory.size() - 1); }
		if (!deletion) {
			chatHistory.add(0, new TimedChatLine(updateCounter, chatComponent, chatLineId));
			while (totalLines.size() > historyLength) { totalLines.remove(totalLines.size() - 1); }
		}
		net.minecraftforge.common.MinecraftForge.EVENT_BUS.post(new ChatLineCreatedEvent(new TimedChatLine(updateCounter, chatComponent, chatLineId)));
	}

	public void refreshChat() {
		chatHistory.clear();
		resetScroll();
		for (int i = totalLines.size() - 1; i >= 0; --i) {
			ChatLine chatline = totalLines.get(i);
			setChatLine(chatline.getChatComponent(), chatline.getChatLineID(), chatline.getUpdatedCounter(), true);
		}
	}
	
	public void addToSentMessages(String msgIn) {
		if (sentMessages.isEmpty() || !sentMessages.get(sentMessages.size() - 1).equals(msgIn)) { sentMessages.add(msgIn); }
	}
	
	public void resetScroll() {
		scrollPos = 0;
		isScrolled = false;
	}
	
	public void scroll(int p_146229_1_) {
		scrollPos += p_146229_1_;
		int i = chatHistory.size();
		if (scrollPos > i - getLineCount()) { scrollPos = i - getLineCount(); }
		if (scrollPos <= 0) {
			scrollPos = 0;
			isScrolled = false;
		}
	}
	
	public IChatComponent getChatComponent(int p_146236_1_, int p_146236_2_) {
		if (!getChatOpen()) { return null; }
		ScaledResolution scaledresolution = new ScaledResolution(mc);
		int i = scaledresolution.getScaleFactor();
		float f = getChatScale();
		int j = p_146236_1_ / i - 3;
		int k = p_146236_2_ / i - 27;
		j = MathHelper.floor_float(j / f);
		k = MathHelper.floor_float(k / f);
		if (j >= 0 && k >= 0) {
			int l = Math.min(getLineCount(), chatHistory.size());
			if (j <= MathHelper.floor_float(getChatWidth() / getChatScale()) && k < mc.fontRendererObj.FONT_HEIGHT * l + l) {
				int i1 = k / mc.fontRendererObj.FONT_HEIGHT + scrollPos;
				if (i1 >= 0 && i1 < chatHistory.size()) {
					ChatLine chatline = chatHistory.get(i1);
					int j1 = 0;
					for (IChatComponent ichatcomponent : chatline.getChatComponent()) {
						if (ichatcomponent instanceof ChatComponentText) {
							j1 += mc.fontRendererObj.getStringWidth(GuiUtilRenderComponents.func_178909_a(((ChatComponentText) ichatcomponent).getChatComponentText_TextValue(), false));
							if (j1 > j) { return ichatcomponent; }
						}
					}
				}
			}
		}
		return null;
	}
	
	public void deleteChatLine(int lineIdIn) {
		Iterator<TimedChatLine> iterator = chatHistory.iterator();
		while (iterator.hasNext()) {
			ChatLine chatline = iterator.next();
			if (chatline.getChatLineID() == lineIdIn) {
				iterator.remove();
			}
		}
		iterator = totalLines.iterator();
		while (iterator.hasNext()) {
			ChatLine chatline1 = iterator.next();
			if (chatline1.getChatLineID() == lineIdIn) {
				iterator.remove();
				break;
			}
		}
	}

	public static int calculateChatboxWidth(float p_146233_0_) {
		int i = 320;
		int j = 40;
		return MathHelper.floor_float(p_146233_0_ * (i - j) + j);
	}

	public static int calculateChatboxHeight(float p_146243_0_) {
		int i = 180;
		int j = 20;
		return MathHelper.floor_float(p_146243_0_ * (i - j) + j);
	}
	
	public boolean getChatOpen() { return mc.currentScreen instanceof GuiChat; }
	public EArrayList<TimedChatLine> getTotalLines() { return totalLines; }
	public void printChatMessage(IChatComponent chatComponent) { printChatMessageWithOptionalDeletion(chatComponent, 0); }
	public int getChatWidth() { return calculateChatboxWidth(mc.gameSettings.chatWidth); }
	public int getChatHeight() { return calculateChatboxHeight(getChatOpen() ? mc.gameSettings.chatHeightFocused : mc.gameSettings.chatHeightUnfocused); }
	public float getChatScale() { return mc.gameSettings.chatScale; }
	public int getLineCount() { return getChatHeight() / 9; }
	public List<String> getSentMessages() { return sentMessages; }
}