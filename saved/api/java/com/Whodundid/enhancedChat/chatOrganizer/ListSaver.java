package com.Whodundid.enhancedChat.chatOrganizer;

import com.Whodundid.core.app.AppType;
import com.Whodundid.core.app.RegisteredApps;
import java.io.File;
import java.io.PrintWriter;

public class ListSaver {

	ChatOrganizer organizer;
	private PrintWriter saver = null;
	
	public ListSaver(ChatOrganizer organizerIn) {
		organizer = organizerIn;
	}
	
	public void saveLists() {
		File keywordsFile = new File(RegisteredApps.getAppConfigBaseFileLocation(AppType.ENHANCEDCHAT) + "/KeyWords");
		if (!keywordsFile.exists()) { keywordsFile.mkdirs(); organizer.resetKeyWordLists(); return; }
		
		try {
			saver = new PrintWriter(RegisteredApps.getAppConfigBaseFileLocation(AppType.ENHANCEDCHAT) + "/KeyWords/gameWords.cfg", "UTF-8");
			
			saver.println("** EnhanchedChat Game Keywords");
			saver.println();
			
			for (String s : ChatOrganizer.game.getFilterList()) {
				saver.println(s);
			}
			
			saver.println("END");
		}
		catch (Exception e) { System.out.println("Error saving game keywords.."); e.printStackTrace(); }
		finally { if (saver != null) { saver.close(); } }
		
		try {
			saver = new PrintWriter(RegisteredApps.getAppConfigBaseFileLocation(AppType.ENHANCEDCHAT) + "/KeyWords/trashWords.cfg", "UTF-8");
			
			saver.println("** EnhanchedChat Game Keywords");
			saver.println();
			
			for (String s : ChatOrganizer.trash.getFilterList()) {
				saver.println(s);
			}
			
			saver.println("END");
		}
		catch (Exception e) { System.out.println("Error saving trash keywords.."); e.printStackTrace(); }
		finally { if (saver != null) { saver.close(); } }
		
		try {
			saver = new PrintWriter(RegisteredApps.getAppConfigBaseFileLocation(AppType.ENHANCEDCHAT) + "/KeyWords/userFilters.cfg", "UTF-8");
			
			saver.println("** EnhancedChat User Filter Lists");
			saver.println();
			
			for (ChatFilterList l : ChatOrganizer.filters) {
				saver.println("filterName: " + l.getFilterName());
				saver.println();
				for (String s : l.getFilterList()) {
					saver.println(s);
				}
			}
			
			saver.println("END");
		}
		catch (Exception e) { System.out.println("Error saving user filter lists.."); e.printStackTrace(); }
		finally { if (saver != null) { saver.close(); } }
	}
}
