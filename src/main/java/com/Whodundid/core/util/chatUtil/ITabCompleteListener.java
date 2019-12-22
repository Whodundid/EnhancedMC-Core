package com.Whodundid.core.util.chatUtil;

public interface ITabCompleteListener {
	
	/** Event fired from EMC when a tab completion result has been returned. */
	public void onTabCompletion(String[] result);
	
	/** When a tab completion for minecraft is returned, set this as an object that will listen for the completion. 
	 * @param in1 TODO
	 * @param in2 TODO*/
	public void requestTabComplete(String checkWord, String upToCursor);
}
