package com.Whodundid.enhancedChat.chatUtil;

import com.Whodundid.core.EnhancedMC;
import com.Whodundid.core.enhancedGui.types.interfaces.IEnhancedGuiObject;
import com.Whodundid.core.renderer.EnhancedMCRenderer;
import com.Whodundid.core.util.chatUtil.TimedChatLine;
import com.Whodundid.core.util.renderUtil.CenterType;
import com.Whodundid.core.util.storageUtil.EArrayList;
import com.Whodundid.enhancedChat.EnhancedChatApp;
import com.Whodundid.enhancedChat.chatWindow.ChatWindow;

public class RenderBridge {

	EnhancedChatApp mod = null;
	EnhancedMCRenderer renderer = null;
	
	public RenderBridge(EnhancedChatApp modIn) {
		mod = modIn;
	}
	
	public void setDependencies() {
		if (EnhancedMC.getRenderer() != null) {
			renderer = EnhancedMC.getRenderer();
		}
	}
	
	public void sendChatToCorrectWindow(String filterName, TimedChatLine l) {
		ChatWindow w = getChatWindowWithFilter(filterName);
		if (w != null) { w.addChatLine(l); }
	}
	
	public void addIfNotOpen() { addIfNotOpen("", CenterType.botLeftScreen); }
	public void addIfNotOpen(String defaultTextIn) { addIfNotOpen(defaultTextIn, CenterType.botLeftScreen); }
	public void addIfNotOpen(String defaultTextIn, CenterType pos) {
		if (getChatWindowWithFilter("All") == null) {
			EnhancedMC.displayWindow(new ChatWindow().updateInitText(defaultTextIn), pos);
		}
	}
	
	public void addChatWindow() { addChatWindow("", CenterType.botLeftScreen); }
	public void addChatWindow(String defaultTextIn) { addChatWindow(defaultTextIn, CenterType.botLeftScreen); }
	public void addChatWindow(String defaultTextIn, CenterType pos) {
		EnhancedMC.displayWindow(new ChatWindow().updateInitText(defaultTextIn), pos);
	}
	
	public void addChatWindowWithFilter(String filterName) {
		if (!getAllActiveChatWindowTypes().contains(filterName)) { EnhancedMC.displayWindow(new ChatWindow().setChatFilters(filterName)); }
	}
	
	public void removeChatWindowWithFilter(String filterName) {
		for (ChatWindow w : getAllActiveChatWindows()) { if (w.getAllFilters().contains(filterName)) { renderer.removeObject(w); } }
	}
	
	public void removeAllChatWindows() {
		for (ChatWindow w : getAllActiveChatWindows()) { renderer.removeObject(w); }
	}
	
	public void removeAllUnpinnedChatWindows() {
		System.out.println(getAllActiveChatWindows());
		for (ChatWindow w : getAllActiveChatWindows()) { if (!w.isPinned()) { renderer.removeObject(w); } }
	}
	
	public boolean hasAChatWindow() {
		for (IEnhancedGuiObject o : renderer.getObjects()) {
			if (o instanceof ChatWindow) { return true; }
		}
		return false;
	}
	
	public boolean areAnyChatWindowsPinned() {
		for (ChatWindow w : getAllActiveChatWindows()) { if (w.isPinned()) { return true; } }
		return false;
	}
	
	public ChatWindow getFocusedChatWindow() {
		return null; //rewrite
	}
	
	public EArrayList<ChatWindow> getAllPinnedChatWindows() {
		EArrayList<ChatWindow> pinned = new EArrayList();
		for (ChatWindow w : getAllActiveChatWindows()) { if (w.isPinned()) { pinned.add(w); } }
		return pinned;
	}
	
	public ChatWindow getChatWindowWithFilter(String filterNameIn) {
		for (ChatWindow w : getAllActiveChatWindows()) {
			if (w.getActiveFilters().contains(filterNameIn)) { return w; }
		}
		return null;
	}
	
	public EArrayList<String> getAllActiveChatWindowTypes() {
		EArrayList<String> windowTypes = new EArrayList();
		for (ChatWindow w : getAllActiveChatWindows()) {  } //not sure how to address yet
		return windowTypes;
	}
	
	public EArrayList<ChatWindow> getAllActiveChatWindows() {
		EArrayList<ChatWindow> windows = new EArrayList();
		for (IEnhancedGuiObject o : EArrayList.combineLists(renderer.getObjects(), renderer.getAddingObjects())) {
			if (o instanceof ChatWindow) { windows.add((ChatWindow) o); }
		}
		return windows;
	}
	
}
