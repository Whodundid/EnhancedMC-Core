package com.Whodundid.core.notifications.util;

public class NotificationType {
	
	private String internalName = "";
	private String type = "";
	private String category = "";
	private String description = "";
	
	public NotificationType(String internalNameIn, String typeIn, String categoryIn) { this(internalNameIn, typeIn, categoryIn, null); }
	public NotificationType(String internalNameIn, String typeIn, String categoryIn, String descriptionIn) {
		internalName = internalNameIn;
		type = typeIn;
		category = categoryIn;
		description = descriptionIn;
	}
	
	public String getInternalName() { return internalName; }
	public String getType() { return type; }
	public String getCategory() { return category; }
	public String getDescription() { return description; }
	
	@Override
	public String toString() {
		return "[" + internalName + ", " + type + ", " + category + "]";
	}

}
