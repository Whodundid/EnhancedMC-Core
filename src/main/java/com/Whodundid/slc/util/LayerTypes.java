package com.Whodundid.slc.util;

import net.minecraft.entity.player.EnumPlayerModelParts;

public enum LayerTypes {
	LL("ll"),
	RL("rl"),
	LA("la"),
	RA("ra"),
	J("j"),
	CA("ca"),
	H("h");
	
	private final String type;
	
	LayerTypes(String type) {
		this.type = type;
	}
	
	public String getLayerType() { return type; }
	
	public EnumPlayerModelParts getMCType() {
		switch (type) {
		case "ll": return EnumPlayerModelParts.LEFT_PANTS_LEG;
		case "rl": return EnumPlayerModelParts.RIGHT_PANTS_LEG;
		case "la": return EnumPlayerModelParts.LEFT_SLEEVE;
		case "ra": return EnumPlayerModelParts.RIGHT_SLEEVE;
		case "j": return EnumPlayerModelParts.JACKET;
		case "ca": return EnumPlayerModelParts.CAPE;
		case "h": return EnumPlayerModelParts.HAT;
		default: return null;
		}
	}
}