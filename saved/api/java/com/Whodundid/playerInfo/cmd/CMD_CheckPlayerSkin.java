package com.Whodundid.playerInfo.cmd;

import com.Whodundid.core.util.chatUtil.ChatBuilder;
import com.Whodundid.core.util.storageUtil.EArrayList;
import com.Whodundid.playerInfo.PlayerInfoApp;
import java.util.List;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.util.EnumChatFormatting;

public class CMD_CheckPlayerSkin extends CommandBase {
	
	PlayerInfoApp mod;
	
	public CMD_CheckPlayerSkin(PlayerInfoApp modIn) {
		mod = modIn;
	}
	
	@Override public String getCommandName() { return "skin"; }
	@Override public int getRequiredPermissionLevel() { return 0; }
	@Override public String getCommandUsage(ICommandSender sender) { return "/skin <playername>"; }
	@Override public List<String> getCommandAliases() { return new EArrayList(); }

	@Override
	public void processCommand(ICommandSender sender, String[] args) throws CommandException {
		if (args.length == 0 || args.length > 1) { sender.addChatMessage(ChatBuilder.of(EnumChatFormatting.YELLOW + getCommandUsage(sender)).build()); }
		else { mod.fetchSkin(null, args[0]); }
	}
}