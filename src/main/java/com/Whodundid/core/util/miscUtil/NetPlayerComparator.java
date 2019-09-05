package com.Whodundid.core.util.miscUtil;

import com.google.common.collect.ComparisonChain;
import java.util.Comparator;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.scoreboard.ScorePlayerTeam;
import net.minecraft.world.WorldSettings;

//Last edited: Oct 19, 2018
//First Added: Oct 19, 2018
//Author: Hunter Bragg

public class NetPlayerComparator implements Comparator<NetworkPlayerInfo> {
	
	 @Override
	public int compare(NetworkPlayerInfo p_compare_1_, NetworkPlayerInfo p_compare_2_) {
		 ScorePlayerTeam scoreplayerteam = p_compare_1_.getPlayerTeam();
		 ScorePlayerTeam scoreplayerteam1 = p_compare_2_.getPlayerTeam();
		 return ComparisonChain.start().compareTrueFirst(p_compare_1_.getGameType() != WorldSettings.GameType.SPECTATOR, p_compare_2_.getGameType() != WorldSettings.GameType.SPECTATOR).compare(scoreplayerteam != null ? scoreplayerteam.getRegisteredName() : "", scoreplayerteam1 != null ? scoreplayerteam1.getRegisteredName() : "").compare(p_compare_1_.getGameProfile().getName(), p_compare_2_.getGameProfile().getName()).result();
	 }
}
