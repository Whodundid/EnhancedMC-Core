package com.Whodundid.core.util.hypixel;

import com.google.gson.JsonObject;

public class HypixelData {
	
	private String server;
	private String gameTypeString = "none";
	private String mode = "none";
	private String map = "none";
	
	private HypixelGameType gameType;
	
	//------------------------
	//HypixelData Constructors
	//------------------------
	
	public HypixelData(JsonObject jsonIn) {
		try { server = jsonIn.get("server").getAsString(); } catch (Exception e) {}
		try { gameTypeString = jsonIn.get("gametype").getAsString(); } catch (Exception e) {}
		try { mode = jsonIn.get("mode").getAsString(); } catch (Exception e) {}
		try { map = jsonIn.get("map").getAsString(); } catch (Exception e) {}
		
		switch (server) {
		case "limbo": gameType = HypixelGameType.LIMBO; break;
		}
		
		if (gameTypeString != null) {
			switch (gameTypeString.toLowerCase()) {
			case "housing": gameType = HypixelGameType.HOUSING; break;
			case "main": gameType = HypixelGameType.MAIN; break;
			case "skyblock": gameType = HypixelGameType.SKYBLOCK; break;
			case "pit": gameType = HypixelGameType.THEPIT; break;
			case "prototype": gameType = HypixelGameType.PROTOTYPE; break;
			case "bedwars": gameType = HypixelGameType.BEDWARS; break;
			case "skywars": gameType = HypixelGameType.SKYWARS; break;
			case "murder_mystery": gameType = HypixelGameType.MURDERMYSTERY; break;
			case "arcade": gameType = HypixelGameType.ARCADE; break;
			case "build_battle": gameType = HypixelGameType.BUILDBATTLE; break;
			case "duels": gameType = HypixelGameType.DUELS; break;
			case "uhc": gameType = HypixelGameType.UHC; break;
			case "tntgames": gameType = HypixelGameType.TNTGAMES; break;
			case "legacy": gameType = HypixelGameType.CLASSIC; break;
			case "mcgo": gameType = HypixelGameType.CVC; break;
			case "survival_games": gameType = HypixelGameType.BLITZSG; break;
			case "walls3": gameType = gameType.MEGAWALLS; break;
			case "super_smash": gameType = HypixelGameType.SMASHHEROES; break;
			case "battleground": gameType = HypixelGameType.WARLORDS; break;
			case "true_combat": gameType = HypixelGameType.CRAZYWALLS; break;
			default: gameType = HypixelGameType.UNKNOWN;
			}
		}
	}
	
	//----------------
	//Object Overrides
	//----------------
	
	@Override public String toString() { return "[" + server + ", " + gameType + ", " + mode + ", " + map + "]"; }
	
	//-------------------
	//HypixelData Methods
	//-------------------
	
	public boolean isLimbo() { return server.equals("limbo"); }
	public boolean isLobby() { return server.contains("lobby"); }
	
	public boolean isPvp() {
		if (gameType != null) {
			switch (gameType) {
			case ARCADE:
				if (mode != null) {
					switch (mode.toLowerCase()) {
					case "hole_in_the_wall": return false;
					case "soccer": return false;
					case "oneinthequiver": return true;
					case "draw_their_thing": return false;
					case "dragonwars2": return true;
					case "ender": return true;
					case "starwars": return true;
					case "throw_out": return true;
					case "defender": return false;
					case "farm_hunt": return true;
					case "party": return true;
					case "zombies_dead_end": return false;
					case "zombies_bad_blood": return false;
					case "zombies_alien_arcadium": return false;
					case "hide_and_seek_prop_hunt": return true;
					case "hide_and_seek_party_pooper": return false;
					case "simon_says": return false;
					case "mini_walls": return true;
					case "dayone": return false;
					}
				}
				return true;
			case SKYBLOCK: return !(mode.equals("dynamic") && map.equals("Private World"));
			case PROTOTYPE:
				if (mode != null) {
					switch (mode.toLowerCase()) {
					case "pvp_ctw": return true;
					case "towerwars_solo": return false;
					case "towerwars_team_of_two": return false;
					}
				}
				return true;
			case TNTGAMES:
				if (mode != null) {
					switch (mode.toLowerCase()) {
					case "tntrun": return false;
					case "pvprun": return true;
					case "bowspleef": return true;
					case "tntag": return true;
					case "capture": return true;
					}
				}
				return true;
			default: return gameType.isPvp();
			}
		}
		return true;
	}
	
	//-------------------
	//HypxielData Getters
	//-------------------

	public String getServer() { return server; }
	public String getGameTypeString() { return gameTypeString; }
	public String getMode() { return mode; }
	public String getMap() { return map; }
	
	public HypixelGameType getGameType() { return gameType; }
	
}
