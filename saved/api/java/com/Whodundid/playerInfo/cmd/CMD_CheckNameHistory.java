package com.Whodundid.playerInfo.cmd;

import java.util.List;
import com.Whodundid.core.util.chatUtil.ChatBuilder;
import com.Whodundid.core.util.storageUtil.EArrayList;
import com.Whodundid.playerInfo.PlayerInfoApp;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.util.EnumChatFormatting;

//Last edited: 10-16-18
//First Added: 11-16-17
//Author: Hunter Bragg

public class CMD_CheckNameHistory extends CommandBase {
	
	PlayerInfoApp mod;
	
	public CMD_CheckNameHistory(PlayerInfoApp modIn) {
		mod = modIn;
	}
	
	@Override public String getCommandName() { return "names"; }
	@Override public int getRequiredPermissionLevel() { return 0; }
	@Override public String getCommandUsage(ICommandSender sender) { return "/names <playername>"; }
	@Override public List<String> getCommandAliases() { return new EArrayList(); }

	@Override
	public void processCommand(ICommandSender sender, String[] args) throws CommandException {
		if (args.length == 0 || args.length > 1) { sender.addChatMessage(ChatBuilder.of(EnumChatFormatting.YELLOW + getCommandUsage(sender)).build()); }
		else { mod.fetchNameHistory(args[0], false); }
	}
}