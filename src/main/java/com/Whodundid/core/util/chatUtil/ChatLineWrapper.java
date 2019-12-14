package com.Whodundid.core.util.chatUtil;

import com.Whodundid.core.EnhancedMC;
import com.Whodundid.core.util.renderUtil.EFontRenderer;
import com.google.common.collect.Lists;
import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.IChatComponent;

//Last edited: Jan 10, 2019
//First Added: Jan 10, 2019
//Author: Hunter Bragg

public class ChatLineWrapper {
	
	public static Minecraft mc = Minecraft.getMinecraft();
	public static EFontRenderer fontRenderer = EnhancedMC.getFontRenderer();
	
	public static String func_178909_a(String p_178909_0_, boolean p_178909_1_) {
        return !p_178909_1_ && !mc.gameSettings.chatColours ? EnumChatFormatting.getTextWithoutFormattingCodes(p_178909_0_) : p_178909_0_;
    }

    public static List<IChatComponent> makeList(IChatComponent lineIn, int sizeIn, boolean val1, boolean val2) {
        int i = 0;
        IChatComponent ichatcomponent = new ChatComponentText("");
        List<IChatComponent> list = Lists.<IChatComponent>newArrayList();
        List<IChatComponent> list1 = Lists.newArrayList(lineIn);
        
        for (int j = 0; j < list1.size(); j++) {
            IChatComponent ichatcomponent1 = list1.get(j);
            String s = ichatcomponent1.getUnformattedTextForChat();
            
            boolean flag = false;
            
            if (s.contains("\n")) {
                int k = s.indexOf(10);
                String s1 = s.substring(k + 1);
                s = s.substring(0, k + 1);
                ChatComponentText chatcomponenttext = new ChatComponentText(s1);
                chatcomponenttext.setChatStyle(ichatcomponent1.getChatStyle().createShallowCopy());
                list1.add(j + 1, chatcomponenttext);
                flag = true;
            }

            String s4 = func_178909_a(ichatcomponent1.getChatStyle().getFormattingCode() + s, val2);
            String s5 = s4.endsWith("\n") ? s4.substring(0, s4.length() - 1) : s4;
            int i1 = fontRenderer.getStringWidth(s5);
            ChatComponentText chatcomponenttext1 = new ChatComponentText(s5);
            chatcomponenttext1.setChatStyle(ichatcomponent1.getChatStyle().createShallowCopy());
            
            if (i + i1 > sizeIn) {
                String s2 = fontRenderer.trimStringToWidth(s4, sizeIn - i, false);
                String s3 = s2.length() < s4.length() ? s4.substring(s2.length()) : null;

                if (s3 != null && s3.length() > 0) {
                    int l = s2.lastIndexOf(" ");

                    if (l >= 0 && fontRenderer.getStringWidth(s4.substring(0, l)) > 0) {
                        s2 = s4.substring(0, l);

                        if (val1) { ++l; }

                        s3 = s4.substring(l);
                    }
                    else if (i > 0 && !s4.contains(" ")) {
                        s2 = "";
                        s3 = s4;
                    }

                    s3 = FontRenderer.getFormatFromString(s2) + s3; //Forge: Fix chat formatting not surviving line wrapping.
                    ChatComponentText chatcomponenttext2 = new ChatComponentText(s3);
                    chatcomponenttext2.setChatStyle(ichatcomponent1.getChatStyle().createShallowCopy());
                    list1.add(j + 1, chatcomponenttext2);
                }

                i1 = fontRenderer.getStringWidth(s2);
                chatcomponenttext1 = new ChatComponentText(s2);
                chatcomponenttext1.setChatStyle(ichatcomponent1.getChatStyle().createShallowCopy());
                flag = true;
            }

            if (i + i1 <= sizeIn) {
                i += i1;
                ichatcomponent.appendSibling(chatcomponenttext1);
            }
            else { flag = true; }

            if (flag) {
                list.add(ichatcomponent);
                i = 0;
                ichatcomponent = new ChatComponentText("");
            }
        }

        list.add(ichatcomponent);
        return list;
    }
}
