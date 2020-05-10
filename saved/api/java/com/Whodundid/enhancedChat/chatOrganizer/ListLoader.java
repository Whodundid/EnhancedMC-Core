package com.Whodundid.enhancedChat.chatOrganizer;

import com.Whodundid.core.app.AppType;
import com.Whodundid.core.app.RegisteredApps;
import com.Whodundid.core.util.storageUtil.StorageBox;
import com.Whodundid.core.util.storageUtil.StorageBoxHolder;
import java.io.File;
import java.util.Scanner;

public class ListLoader {
	
	ChatOrganizer organizer;
	StorageBoxHolder<String, String> loadedKeyWords = new StorageBoxHolder();

	public ListLoader(ChatOrganizer organizerIn) {
		organizer = organizerIn;
	}
	
	public void loadKeyWords() {
		loadedKeyWords.clear();
		load("trashWords", "Trash");
		load("gameWords", "Game");
		load("userFilters", "user");
		
		for (StorageBox<String, String> box : loadedKeyWords) {
			organizer.addKeyWord(box.getObject(), box.getValue());
		}
	}
	
	private void load(String fileName, String type) {
		File gameWords = new File(RegisteredApps.getAppConfigBaseFileLocation(AppType.ENHANCEDCHAT) + "/KeyWords/" + fileName + ".cfg");
		if (gameWords.exists()) {
			boolean isEnd = false;
			String filterName = "";
			String command, configLine;
			try (Scanner fileReader = new Scanner(gameWords)) {
				while (!isEnd && fileReader.hasNextLine()) {
					configLine = fileReader.nextLine();
					Scanner line = new Scanner(configLine);
					if (line.hasNext()) {
						command = line.next();
						//System.out.println(fileName + " " + configLine);
						if (command.equals("END")) { isEnd = true; }
						else if (command.equals("filterName:")) { filterName = line.next(); } //search for filer name
						else if (!command.equals("**")) {
							loadedKeyWords.add(configLine, type.equals("user") ? filterName : type); //set the proper filter name based on the type
						}
					}					
					line.close();
				}
			} catch (Exception e) { e.printStackTrace(); }
		}
	}
}
