package com.Whodundid.core.debug.terminal.commands;

import com.Whodundid.core.debug.terminal.gui.ETerminal;
import com.Whodundid.core.subMod.RegisteredSubMods;
import com.Whodundid.core.subMod.SubMod;
import com.Whodundid.core.util.miscUtil.NetPlayerComparator;
import com.Whodundid.core.util.storageUtil.EArrayList;
import com.google.common.collect.Ordering;
import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumChatFormatting;

public class ListCMD implements IConsoleCommand {

	@Override public String getName() { return "list"; }
	@Override public EArrayList<String> getAliases() { return null; }
	@Override public String getCommandHelpInfo() { return "Used to list various things. (mods, players, etc.)"; }
	
	@Override
	public String getCommandErrorInfo(String arg) {
		if (arg != null) {
			switch (arg) {
			case "arg": return "Too many arguments provided!";
			case "unk": return "Unrecognized list type!";
			}
		}
		return "Unknown error!";
	}
	
	@Override
	public EArrayList<String> getTabCompleteList() {
		return null;
	}
	
	@Override
	public void runCommand(ETerminal conIn, EArrayList<String> args, boolean runVisually) {
		if (args.isEmpty()) { conIn.writeln(getCommandHelpInfo(), 0xff00dd); }
		else if (args.size() > 1) { conIn.error(getCommandErrorInfo("arg")); }
		else {
			switch (args.get(0)) {
			case "m":
			case "mods": listMods(conIn, args, runVisually); break;
			case "p":
			case "player":
			case "players": listPlayers(conIn, args, runVisually); break;
			default: conIn.error(getCommandErrorInfo("unk"));
			}
		}
	}
	
	private void listMods(ETerminal conIn, EArrayList<String> args, boolean runVisually) {
		EArrayList<SubMod> mods = RegisteredSubMods.getModsList();
		if (mods != null) {
			conIn.writeln("Listing all EMC Submods...", 0x00ffff);
			for (SubMod m : RegisteredSubMods.getModsList()) {
				int c = 0xb2b2b2;
				String s = "-" + m.getName();
				if (m.isIncompatible()) { s += " incompatible"; c = 0xff5555; }
				if (!m.isDisableable()) { s += (EnumChatFormatting.YELLOW + " cannot be disabled"); }
				conIn.writeln(s, c);
			}
			conIn.writeln("Total mods: " + mods.size(), 0xffff00);
		} else { conIn.error(getCommandErrorInfo("")); }
	}
	
	private void listPlayers(ETerminal conIn, EArrayList<String> args, boolean runVisually) {
		try {
			Minecraft mc = Minecraft.getMinecraft();
			List<EntityPlayer> list = mc.theWorld.playerEntities;
			NetHandlerPlayClient nethandlerplayclient = mc.thePlayer.sendQueue;
			Ordering<NetworkPlayerInfo> order = Ordering.from(new NetPlayerComparator());
			List<NetworkPlayerInfo> nameList = order.sortedCopy(nethandlerplayclient.getPlayerInfoMap());
			
			conIn.writeln("Listing all current players in world...", 0x00ffff);
			for (EntityPlayer p : list) {
				int c = 0xb2b2b2;
				String s = "-" + p.getName();
				if (runVisually) {
					BlockPos pos = p.getPosition();
					s += EnumChatFormatting.LIGHT_PURPLE + " (" + pos.getX() + ", " + pos.getY() + ", " + pos.getZ() + ")";
				}
				if (p.isUser()) { s += EnumChatFormatting.GREEN + " (you)"; c = 0x66ff66; }
				conIn.writeln(s, c);
			}
			conIn.writeln("Total players: " + list.size(), 0xffff00);
			
		} catch (Exception e) {
			conIn.error("Java Error!");
			conIn.writeln(e.getMessage(), 0xff0000);
		}
	}
}
