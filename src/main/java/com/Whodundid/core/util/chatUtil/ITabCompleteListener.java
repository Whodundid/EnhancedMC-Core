package com.Whodundid.core.util.chatUtil;

//Author: Hunter Bragg

public interface ITabCompleteListener {
	
	/** Event fired from EMC when a tab completion result has been returned. */
	public void onTabCompletion(String[] result);
	
	/** When a tab completion for minecraft is returned, set this as an object that will listen for the completion. */
	public void requestTabComplete(String checkWord, String upToCursor);
	
}
