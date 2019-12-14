package com.Whodundid.core.subMod;

//Oct 15, 2018
//Jun 28, 2019: added 'Unknown' type

//Last edited: Jun 28, 2019
//Edit note: removed 'All' type
//First Added: Oct 15, 2018
//Author: Hunter Bragg

/** A pre-built list of base EMC mods, additional mods can be registered beyond these.
 * Anyone will in theory be able to create their own submods and have them be compatible
 * with the EMC core, their base mod classes only need to extend the SubMod class.
 * 
 * @author Hunter Bragg
 *
 */
public enum SubModType {
	AUTOCORRECT("CMD Autocorrect", "cmdautocorrect"),
	AUTOGM3("Auto /GM 3", "autogm3"),
	BLINK("Blinks", "blinks"),
	ENHANCEDCHAT("Enhanced Chat", "enhancedchat"),
	CLEARVISUALS("Clear Visuals", "clearvisuals"),
	CORE("Enhanced MC Core", "core"),
	HOTKEYS("Hotkeys", "hotkeys"),
	MINIMAP("Minimap", "minimap"),
	MULTIHOTBAR("Multi Hotbar", "multihotbar"),
	NAMEHISTORY("Name History", "namehistory"),
	PARKOUR("Parkour AI", "parkourai"),
	PING("Ping Display", "pingdisplay"),
	SCRIPTS("Scripts", "scripts"),
	SLC("Skin Layer Control", "slc"),
	WINDOWHUD("Window HUD", "windowhud"),
	WORLDEDITOR("World Editor", "worldeditor"),
	UNKNOWN("Unknown", "unknown");
	
	String modName = "";
	String modid = "";
	
	private SubModType(String modNameIn, String modidIn) {
		modName = modNameIn;
		modid = modidIn;
	}
	
	public String getModName() { return modName; }
	public String getModid() { return modid; }
	
	public static String getModName(SubModType modIn) { return modIn.modName; }
	public static String getModid(SubModType modIn) { return modIn.modid; }
	
	public static SubModType getTypeFromString(String modIn) {
		try {
			return valueOf(modIn);
		} catch (IllegalArgumentException e) {}
		
		for (SubModType t : SubModType.values()) {
			if (modIn.equals(t.modid) || modIn.equals(t.modName)) { return t; }
		}
		
		return null;
	}
}
