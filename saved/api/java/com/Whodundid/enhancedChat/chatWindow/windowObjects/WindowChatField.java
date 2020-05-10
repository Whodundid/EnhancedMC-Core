package com.Whodundid.enhancedChat.chatWindow.windowObjects;

import com.Whodundid.core.app.AppType;
import com.Whodundid.core.app.RegisteredApps;
import com.Whodundid.core.enhancedGui.guiObjects.actionObjects.EGuiTextField;
import com.Whodundid.core.util.chatUtil.EChatUtil;
import com.Whodundid.enhancedChat.EnhancedChatApp;
import com.Whodundid.enhancedChat.chatWindow.ChatWindow;
import com.google.common.collect.Lists;
import com.google.common.collect.ObjectArrays;
import java.util.List;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.MathHelper;
import net.minecraftforge.client.ClientCommandHandler;
import org.apache.commons.lang3.StringUtils;
import org.lwjgl.input.Keyboard;

public class WindowChatField extends EGuiTextField {

	ChatWindow window;
	private String historyBuffer = "";
	private int sentHistoryCursor = -1;
	private boolean playerNamesFound;
	private boolean waitingOnAutocomplete;
	private int autocompleteIndex;
	private List<String> foundPlayerNames = Lists.<String>newArrayList();

	public WindowChatField(ChatWindow windowIn, int x, int y, int widthIn, int heightIn) {
		super(windowIn, x, y, widthIn, heightIn);
		window = windowIn;
		setEnableBackgroundDrawing(false);
		setMaxStringLength(256);
		setUseObjectGroupForCursorDraws(true);
	}
	
	@Override
	public void keyPressed(char typedChar, int keyCode) {
		waitingOnAutocomplete = false;
		super.keyPressed(typedChar, keyCode);
		switch (keyCode) {
		case 15: autocompletePlayerNames(); break; //tab
		case 28: sendMessage(); break; //enter
		case 33: handleFind(); break; //f
		case 200: getSentHistory(-1); break; //up
		case 201: mc.ingameGUI.getChatGUI().scroll(mc.ingameGUI.getChatGUI().getLineCount() - 1); break; //pageup
		case 208: getSentHistory(1); break; //down;
		case 209: mc.ingameGUI.getChatGUI().scroll(-mc.ingameGUI.getChatGUI().getLineCount() + 1); break; //pagedown
		default:
			playerNamesFound = false;
			window.updateInitText(getText());
		}
	}
	
	public void getSentHistory(int msgPos) {
		int i = sentHistoryCursor + msgPos;
		int j = mc.ingameGUI.getChatGUI().getSentMessages().size();
		i = MathHelper.clamp_int(i, 0, j);
		
		if (i != sentHistoryCursor) {
			if (i == j) {
				sentHistoryCursor = j;
				setText(historyBuffer);
			}
			else {
				if (sentHistoryCursor == j) {
					historyBuffer = getText();
				}
				
				setText(mc.ingameGUI.getChatGUI().getSentMessages().get(i));
				sentHistoryCursor = i;
			}
		}
	}
	
	private void handleFind() {
		if (Keyboard.isKeyDown(29)) { //ctrl
			System.out.println("ye");
		}
	}
	
	private void sendMessage() {
		if (!getText().isEmpty()) {
			EChatUtil.setLastTypedMessage(getText());
			sendChatMessage(getText());
		}
		setText("");
		window.updateInitText("");
		if (!window.isPinned()) { window.close(); }
	}
	
	
	@Override
	public void onTabCompletion(String[] result) {
		if (waitingOnAutocomplete) {
			playerNamesFound = false;
			foundPlayerNames.clear();

			String[] complete = ClientCommandHandler.instance.latestAutoComplete;
			if (complete != null) { result = ObjectArrays.concat(complete, result, String.class); }

			for (String s : result) {
				if (s.length() > 0) { foundPlayerNames.add(s); }
			}

			String s1 = getText().substring(getNthWordFromPos(-1, getCursorPosition(), false));
			String s2 = StringUtils.getCommonPrefix(result);
			s2 = EnumChatFormatting.getTextWithoutFormattingCodes(s2);

			if (s2.length() > 0 && !s1.equalsIgnoreCase(s2)) {
				deleteFromCursor(getNthWordFromPos(-1, getCursorPosition(), false) - getCursorPosition());
				writeText(s2);
			} else if (foundPlayerNames.size() > 0) {
				playerNamesFound = true;
				autocompletePlayerNames();
			}
		}
	}

	public void autocompletePlayerNames() {
		if (playerNamesFound) {
			deleteFromCursor(getNthWordFromPos(-1, getCursorPosition(), false) - getCursorPosition());
			if (autocompleteIndex >= foundPlayerNames.size()) { autocompleteIndex = 0; }
		}
		else {
			int i = getNthWordFromPos(-1, getCursorPosition(), false);
			foundPlayerNames.clear();
			autocompleteIndex = 0;
			String s = getText().substring(i).toLowerCase();
			String s1 = getText().substring(0, getCursorPosition());
			requestTabComplete(s1, s);
			waitingOnAutocomplete = true;

			if (foundPlayerNames.isEmpty()) { return; }

			playerNamesFound = true;
			deleteFromCursor(i - getCursorPosition());
		}

		if (foundPlayerNames.size() > 1) {
			StringBuilder stringbuilder = new StringBuilder();

			for (String s2 : foundPlayerNames) {
				if (stringbuilder.length() > 0) { stringbuilder.append(", "); }
				stringbuilder.append(s2);
			}

			mc.ingameGUI.getChatGUI().printChatMessageWithOptionalDeletion(new ChatComponentText(stringbuilder.toString()), 1);
			EnhancedChatApp mod = (EnhancedChatApp) RegisteredApps.getApp(AppType.ENHANCEDCHAT);
			mod.getChatOrganizer().getFilterHistoryList("All").remove(0);
		}

		writeText(EnumChatFormatting.getTextWithoutFormattingCodes(foundPlayerNames.get(autocompleteIndex++)));
	}
}
