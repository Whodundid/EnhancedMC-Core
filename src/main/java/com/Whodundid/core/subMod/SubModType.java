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
	WORLDEDITOR("World Editor", "worldeditor"),
	UNKNOWN("Unknown", "unknown");
	
	String modName = "";
	String modid = "";
	
	private SubModType(String modNameIn, String modidIn) {
		modName = modNameIn;
		modid = modidIn;
	}
	
	public static String getModName(SubModType modIn) { return modIn.modName; }
	public static String getModid(SubModType modIn) { return modIn.modid; }
	
	public static SubModType getSubModFromString(String modIn) {
		try {
			return valueOf(modIn);
		} catch (IllegalArgumentException e) {}
		
		switch (modIn.toLowerCase()) {
		case "cmdautocorrect": return AUTOCORRECT;
		case "autogm3": return AUTOGM3;
		case "blinks": return BLINK;
		case "enhancedchat": return ENHANCEDCHAT;
		case "clearvisuals": return CLEARVISUALS;
		case "core": return CORE;
		case "hotkeys": return HOTKEYS;
		case "minimap": return MINIMAP;
		case "multihotbar": return MULTIHOTBAR;
		case "namehistory": return NAMEHISTORY;
		case "parkourai": return PARKOUR;
		case "pingdisplay": return PING;
		case "scripts": return SCRIPTS;
		case "skinlayercontrol": return SLC;
		case "worldeditor": return WORLDEDITOR;
		case "unknown": return UNKNOWN;
		default: return null;
		}
	}
}
