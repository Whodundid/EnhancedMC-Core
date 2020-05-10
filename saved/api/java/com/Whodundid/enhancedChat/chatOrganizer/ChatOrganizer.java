package com.Whodundid.enhancedChat.chatOrganizer;

import com.Whodundid.core.app.AppType;
import com.Whodundid.core.app.RegisteredApps;
import com.Whodundid.core.util.chatUtil.TimedChatLine;
import com.Whodundid.core.util.storageUtil.EArrayList;
import com.Whodundid.core.util.storageUtil.StorageBox;
import com.Whodundid.core.util.storageUtil.StorageBoxHolder;
import com.Whodundid.enhancedChat.EnhancedChatApp;
import java.io.File;
import net.minecraft.client.Minecraft;
import net.minecraft.util.EnumChatFormatting;

//First Added: Apr 14, 2017
//Author: Hunter Bragg

public class ChatOrganizer {
	
	protected Minecraft mc = Minecraft.getMinecraft();
	protected EnhancedChatApp mod;
	protected final ListGenerators generators;
	protected final ListLoader loader;
	protected final ListSaver saver;
	private static boolean listLock = false;
	
    public static ChatFilterList trash = new ChatFilterList("Trash", 0xb2b2b2, EnumChatFormatting.GRAY);
    public static ChatFilterList game = new ChatFilterList("Game", 0xffaa00, EnumChatFormatting.GOLD);
    
    public static EArrayList<ChatFilterList> filters = new EArrayList();
    public static StorageBoxHolder<String, EArrayList<TimedChatLine>> filterHistory = new StorageBoxHolder();
	
    private static EArrayList<ChatFilterList> filtersToBeAdded = new EArrayList();
    private static EArrayList<ChatFilterList> filtersToBeRemoved = new EArrayList();
	private static StorageBoxHolder<String, String> wordsToBeAdded = new StorageBoxHolder();
	private static StorageBoxHolder<String, String> wordsToBeRemoved = new StorageBoxHolder();
    
	public ChatOrganizer(EnhancedChatApp modIn) {
		mod = modIn;
		
		filterHistory.setAllowDuplicates(false);
		
		filterHistory.add("All", new EArrayList<TimedChatLine>()); //create the 'all' chat filter history
		filterHistory.add("Game", new EArrayList<TimedChatLine>()); //create the 'game' chat filter history
		filterHistory.add("Trash", new EArrayList<TimedChatLine>()); //create the trash chat filter history
		filters.add(trash, game);
		
		generators = new ListGenerators(this);
		loader = new ListLoader(this);
		saver = new ListSaver(this);
		
		wordsToBeAdded.setAllowDuplicates(true);
		wordsToBeRemoved.setAllowDuplicates(true);
		
		if (doKeyWordFilesExist()) { loader.loadKeyWords(); }
		else { generators.generateDefaultKeyLists(); }
	}
	
	public static boolean doKeyWordFilesExist() {
		File gameWords = new File(RegisteredApps.getAppConfigBaseFileLocation(AppType.ENHANCEDCHAT) + "/KeyWords/gameWords.cfg");
		File trashWords = new File(RegisteredApps.getAppConfigBaseFileLocation(AppType.ENHANCEDCHAT) + "/KeyWords/trashWords.cfg");
		
		return gameWords.exists() && trashWords.exists();
	}
	
	public static boolean doUserFilterListsExist() {
		File userFilters = new File(RegisteredApps.getAppConfigBaseFileLocation(AppType.ENHANCEDCHAT) + "/KeyWords/userFilters.cfg");
		return userFilters.exists();
	}
	
	public void onTick() {
		updateKeywordLists();
	}
	
	public void resetKeyWordLists() { listLock = true; clearKeyWordLists(); generators.generateDefaultKeyLists(); loadKeyWordLists(); listLock = false; }
	public void clearKeyWordLists() { listLock = true; filters.clear(); saveKeyWordLists(); listLock = false; }
	public void saveKeyWordLists() { saver.saveLists(); }
	public void loadKeyWordLists() { loader.loadKeyWords(); }
	
	public void readChat(TimedChatLine e) {
		addTo("All", e);
		if (mod.enableChatOrganizer.get() && !listLock) {
			String message = e.getChatComponent().getFormattedText();
			String messageNoFormat = e.getChatComponent().getUnformattedText();
			String messageFirstPart = "";
			//System.out.println("no format:" + messageNoFormat);
			if (messageNoFormat.length() > 15) { messageFirstPart = messageNoFormat.substring(0, 15); }
			//System.out.println(messageFirstPart);
			if (!checkListForMessage(getFilterList("Trash").getFilterList(), e)) {
				if (checkListForMessage(getFilterList("Game").getFilterList(), e)) { addTo("Game", e); }
				else {
					boolean filterFound = false;
					for (ChatFilterList l : filters) {
						if (checkListForMessage(l.getFilterList(), e)) {
							addTo(l.getFilterName(), e);
							filterFound = true;
							break;
						}
					}
					if (!filterFound) { addTo("Game", e); }
				}
			}
			else { addTo("Trash", e); }
		}
	}
	
	private synchronized void updateKeywordLists() {
		if (filtersToBeAdded.size() > 0) { addFilters(); }
		if (wordsToBeAdded.size() > 0) { addWords(); }
		if (wordsToBeRemoved.size() > 0) { removeWords(); }
	}
	
	public static boolean createFilter(String filterNameIn) {
		if (getFilterList(filterNameIn) == null) {
			ChatFilterList l = new ChatFilterList(filterNameIn);
			filtersToBeAdded.add(l);
			return true;
		}
		return false;
	}
	
	public static boolean removeFilter(String filterNameIn) {
		ChatFilterList l = getFilterList(filterNameIn);
		if (l != null) { filtersToBeRemoved.add(l); return true; }
		return false;
	}
	
	public static EArrayList<String> getAllFilterNames() {
		return new EArrayList<String>(filterHistory.getObjects());
	}
	
	public static EArrayList<ChatFilterList> getAllFilters() {
		return filters;
	}
	
	//interal getter for grabbing a filter if it exists
	public static ChatFilterList getFilterList(String filterName) {
		for (ChatFilterList l : filters) {
			if (l.getFilterName().equals(filterName)) { return l; }
		}
		return null;
	}
	
	//internal getter for grabbing a filter's chat history if it exists
	public static EArrayList<TimedChatLine> getFilterHistoryList(String filterName) {
		for (StorageBox<String, EArrayList<TimedChatLine>> box : filterHistory) {
			if (box.getObject().equals(filterName)) { return box.getValue(); }
		}
		return null;
	}
	
	private void addFilters() {
		for (ChatFilterList l : filtersToBeAdded) {
			if (l != null) {
				if (!l.getFilterName().equals("All")) { //check that the filter isn't 'All'
					ChatFilterList fl = getFilterList(l.getFilterName());
					if (fl == null) { //only add if it doesn't exist
						filters.add(l);
						filterHistory.add(l.getFilterName(), new EArrayList<TimedChatLine>());
					} 
					else {
						fl.addAllFilters(l.getFilterList()); //otherwise add the filter words to the existing list
					}
				}
			}
		}
		filtersToBeAdded.clear();
	}
	
	private void removeFilters() {
		for (ChatFilterList l : filtersToBeRemoved) {
			ChatFilterList list = getFilterList(l.getFilterName()); //grab filter from list
			if (list != null) { filters.remove(list); filterHistory.removeBoxesContainingObj(l.getFilterName()); } //remove the filter and it's associated history
		}
		filtersToBeRemoved.clear();
	}
	
	private void addWords() {
		for (StorageBox<String, String> wordList : wordsToBeAdded) {
			String type = wordList.getObject();
			String word = wordList.getValue();
			if (!type.equals("All")) { //check that the filter isn't 'All'
				ChatFilterList fl = getFilterList(type); //get existing filter if it exists
				if (fl == null) { //create filter list if it didn't exist and add it to filters
					fl = new ChatFilterList(type);
					filters.add(fl);
					filterHistory.add(type, new EArrayList<TimedChatLine>());
				}
				fl.addFilters(word); //add the word to the filter
			}
		}
		wordsToBeAdded.clear();
	}
	
	private void removeWords() {
		for (StorageBox<String, String> wordList : wordsToBeRemoved) {
			String type = wordList.getObject();
			String word = wordList.getValue();
			if (!type.equals("All")) { //check that the filter isn't 'All'
				ChatFilterList fl = getFilterList(type);
				if (fl != null) { //only proceed if the filter exists, no point in creating something that's being removed
					fl.removeFilters(wordList.getValue()); //remove the word from the filter
				}
			}
		}
		wordsToBeRemoved.clear();
	}
	
	public void addFilter(ChatFilterList filterIn) { filtersToBeAdded.add(filterIn); }
	public void removeFilter(ChatFilterList filterIn) { filtersToBeRemoved.add(filterIn); }
	public void addKeyWord(String word, String type) { wordsToBeAdded.add(type, word); }
	public void removeKeyWord(String word, String type) { wordsToBeRemoved.add(type, word); }
	
	public void addTo(String filterNameIn, TimedChatLine line) {
		EArrayList list = getFilterHistoryList(filterNameIn);
		if (list != null) {
			if (list.size() > EnhancedChatApp.chatHistoryLength) { list.remove(list.size() - 1); }
	    	list.add(0, line);
	    	mod.getRenderBridge().sendChatToCorrectWindow(filterNameIn, line);
		}
	}
	
	public static boolean checkListForMessage(EArrayList<String> checkList, TimedChatLine messageIn) {
		String message = messageIn.getChatComponent().getUnformattedText();
		//System.out.println("The message: " + message);
		for (String keyWord : checkList) {
			if (message.contains(keyWord)) { return true; }
		}
		return false;
	}
	
	public static boolean checkListForMessage(EArrayList<String> checkList, String messageIn) {
		for (String keyWord : checkList) {
			if (messageIn.contains(keyWord)) { return true; }
		}
		return false;
	}
}
