package com.Whodundid.enhancedChat.chatOrganizer;

import com.Whodundid.core.app.AppType;
import com.Whodundid.core.app.RegisteredApps;
import com.Whodundid.core.util.storageUtil.EArrayList;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;

public class ListGenerators {

	ChatOrganizer organizer;
	private PrintWriter saver = null;
	private EArrayList<String> gameKeyWords = new EArrayList();
	private EArrayList<String> trashKeyWords = new EArrayList();
	
	public ListGenerators(ChatOrganizer organizerIn) {
		organizer = organizerIn;
		
		gameKeyWords.add("Guild Name:", "Total Members:", "Online Members:", "https://store.hypixel.net", "/watchdogreport", "Total Earned Coins:", "Total Earned Experience:");
		gameKeyWords.add("Quakecraft Coins", "Hypixel Experience", "Rate this map by clicking:", "Automatically activated:", "Daily Quest:", "Hotkey Mode:");
		gameKeyWords.add("Click the link to visit our website and claim your reward:", "CHALLENCGE IN:", "Current Tool:", "Brush Type:", "Performer:", "Replace Material: ");
		gameKeyWords.add("Voxel:", "Water Mode:", "Brush Size:", "Currently selected biome type:", "Also join our discord server!", "Get deals and news sent to your email!");
		gameKeyWords.add("See all the posts shared by Hypixel on Facebook!", "That warp does not exist.", "Be the first to watch Hypixel YouTube videos!");
		gameKeyWords.add("If you happen to come across any bugs make sure you report them at", "Keep up with the latest from Hypixel on Twitter!", "The time was set to ");
		gameKeyWords.add("Now playing:", "Unknown direction:", "Usage:", "CHALLENGE IN:", "§r§6MVP§r§c++§r§6 §r§ahas access to the following emotes in chat:§r");
		gameKeyWords.add(":star:", ":yes:", ":no:", ":java:", ":arrow:", ":shrug:", ":tableflip:", ":123:", ":totem:", ":typing:", ":maths:", ":snail:", ":thinking:");
		gameKeyWords.add("The entity UUID provided is in an invalid format]", ": Teleported", ": Saved the world]", ": Set own game mode to ");
		gameKeyWords.add("For parameter 'to': Invalid block '", "Network Boosters are available at ", "http://store.hypixel.net/");
		gameKeyWords.add("Total Earned Guild Experience: ", "[@: Set the time to ", "[@: Toggled downfall]", "Command set: ", "§r§6Script: ");
		
		trashKeyWords.add("Ping:", "Miss Rate:", "Damage Rate:", "Selected Kit:", "HP:", "You received:", "Murderer Chance:", "Detective:", "Hero:");
		trashKeyWords.add("Achievement Unlocked:", "Winner:", "Murderer:", "You picked up the Bow!", "To leave, type", "Cages open in:");
		trashKeyWords.add("You can find mystery Boxes by", " entered the world.");
	}
	
	public void generateDefaultKeyLists() {
		try {
			File keywordsFile = new File(RegisteredApps.getAppConfigBaseFileLocation(AppType.ENHANCEDCHAT) + "/KeyWords");
			if (!keywordsFile.exists()) { keywordsFile.mkdirs(); }
			
			File userFilterListsFile = new File(RegisteredApps.getAppConfigBaseFileLocation(AppType.ENHANCEDCHAT) + "/KeyWords/userFilterLists.cfg");
			if (userFilterListsFile.exists()) {
				saver = new PrintWriter(userFilterListsFile);
				saver.println("** EnhancedChat User Filter Lists");
				saver.println();
				saver.println("END");
			}
			
			generateDefaultGameKeyWords();
			generateDefaultTrashKeyWords();
		}
		catch (IOException e) { e.printStackTrace(); }
		finally { if (saver != null) { saver.close(); } }
	}
	
	private void generateDefaultGameKeyWords() throws IOException {
		saver = new PrintWriter(RegisteredApps.getAppConfigBaseFileLocation(AppType.ENHANCEDCHAT) + "/KeyWords/gameWords.cfg", "UTF-8");
		
		saver.println("** EnhanchedChat Game Keywords");
		saver.println();
		
		for (String s : gameKeyWords) {
			saver.println(s);
		}
		
		saver.println("END");
		
		if (saver != null) { saver.close(); }
	}
	
	private void generateDefaultTrashKeyWords() throws IOException {
		saver = new PrintWriter(RegisteredApps.getAppConfigBaseFileLocation(AppType.ENHANCEDCHAT) + "/KeyWords/trashWords.cfg", "UTF-8");
		
		saver.println("** EnhanchedChat Trash Keywords");
		saver.println();
		
		for (String s : trashKeyWords) {
			saver.println(s);
		}
		
		saver.println("END");
		
		if (saver != null) { saver.close(); }
	}
}
