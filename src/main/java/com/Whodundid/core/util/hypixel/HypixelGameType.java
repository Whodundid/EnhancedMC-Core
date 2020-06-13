package com.Whodundid.core.util.hypixel;

public enum HypixelGameType {

	ARCADE(true),
	BEDWARS(true),
	BLITZSG(true),
	BUILDBATTLE(false),
	CLASSIC(true),
	CRAZYWALLS(true),
	CVC(true),
	DUELS(true),
	HOUSING(false),
	LIMBO(false),
	MAIN(false),
	MEGAWALLS(true),
	MURDERMYSTERY(true),
	PROTOTYPE(false),
	SKYBLOCK(true),
	SKYWARS(true),
	SMASHHEROES(true),
	THEPIT(true),
	TNTGAMES(true),
	UHC(true),
	UNKNOWN(true), //to stay on the safe sides
	WARLORDS(true);
	
	private boolean pvp = false;
	
	HypixelGameType(boolean pvpIn) {
		pvp = pvpIn;
	}
	
	public boolean isPvp() { return pvp; }
	
}
