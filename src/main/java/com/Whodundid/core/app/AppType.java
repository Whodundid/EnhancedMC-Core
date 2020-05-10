package com.Whodundid.core.app;

//Author: Hunter Bragg

/** A pre-built list of base EMC apps, additional apps can be registered beyond these.
 *  Anyone will in theory be able to create their own EMC apps and have them be compatible
 *  with the EMC core, their base mod classes only need to extend the EMCApp class.
 */
public enum AppType {
	AUTOCORRECT("CMD Autocorrect", "cmdautocorrect"),
	AUTOGM3("Auto /GM 3", "autogm3"),
	BLINK("Blinks", "blinks"),
	ENHANCEDCHAT("Enhanced Chat", "enhancedchat"),
	CLEARVISUALS("Clear Visuals", "clearvisuals"),
	CORE("Enhanced MC Core", "core"),
	HOTKEYS("Hotkeys", "hotkeys"),
	MINIMAP("Minimap", "minimap"),
	MULTIHOTBAR("Multi Hotbar", "multihotbar"),
	PLAYERINFO("Player Info", "playerinfo"),
	PARKOUR("Parkour AI", "parkourai"),
	PING("Ping Display", "pingdisplay"),
	SCRIPTS("Scripts", "scripts"),
	SLC("Skin Layer Control", "slc"),
	WINDOWHUD("Window HUD", "windowhud"),
	WORLDEDITOR("World Editor", "worldeditor"),
	UNKNOWN("Unknown", "unknown");
	
	String appName = "";
	String modid = "";
	
	private AppType(String appNameIn, String modidIn) {
		appName = appNameIn;
		modid = modidIn;
	}
	
	public String getAppName() { return appName; }
	public String getModid() { return modid; }
	
	public static String getAppName(AppType appIn) { return appIn.appName; }
	public static String getModid(AppType appIn) { return appIn.modid; }
	
	public static AppType getTypeFromString(String appIn) {
		try {
			return valueOf(appIn);
		} catch (IllegalArgumentException e) {}
		
		for (AppType t : AppType.values()) {
			if (appIn.equals(t.modid) || appIn.equals(t.appName)) { return t; }
		}
		
		return null;
	}
}
