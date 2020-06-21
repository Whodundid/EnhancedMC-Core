package com.Whodundid.playerInfo.cmd;

import java.util.List;
import com.Whodundid.core.util.chatUtil.ChatBuilder;
import com.Whodundid.core.util.storageUtil.EArrayList;
import com.Whodundid.playerInfo.PlayerInfoApp;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.util.EnumChatFormatting;

public class CMD_InfoPlayer extends CommandBase {
	
	PlayerInfoApp mod;
	
	public CMD_InfoPlayer(PlayerInfoApp modIn) {
		mod = modIn;
	}
	
	@Override public String getCommandName() { return "infoplayer"; }
	@Override public int getRequiredPermissionLevel() { return 0; }
	@Override public String getCommandUsage(ICommandSender sender) { return "/pinfo <playername>"; }
	@Override public List<String> getCommandAliases() { return new EArrayList("pinfo"); }

	@Override
	public void processCommand(ICommandSender sender, String[] args) throws CommandException {
		if (args.length == 0 || args.length > 1) { sender.addChatMessage(ChatBuilder.of(EnumChatFormatting.YELLOW + getCommandUsage(sender)).build()); }
		else {
			mod.openInfoPlayer = args[0];
		}
	}
	
}