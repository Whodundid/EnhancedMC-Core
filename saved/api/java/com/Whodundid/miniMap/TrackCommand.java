package com.Whodundid.miniMap;

import com.Whodundid.core.EnhancedMC;
import com.Whodundid.core.app.AppType;
import com.Whodundid.core.app.RegisteredApps;
import com.Whodundid.core.terminal.gui.ETerminal;
import com.Whodundid.core.terminal.terminalCommand.CommandType;
import com.Whodundid.core.terminal.terminalCommand.TerminalCommand;
import com.Whodundid.core.util.renderUtil.EColors;
import com.Whodundid.core.util.storageUtil.EArrayList;
import net.minecraft.client.Minecraft;
import net.minecraft.util.EnumChatFormatting;

public class TrackCommand extends TerminalCommand {
	
	public TrackCommand() {
		super(CommandType.MOD_COMMAND);
	}

	MiniMapApp mod = (MiniMapApp) RegisteredApps.getApp(AppType.MINIMAP);
	
	@Override public String getName() { return "track"; }
	@Override public boolean showInHelp() { return EnhancedMC.isOpMode(); }
	@Override public EArrayList<String> getAliases() { return null; }
	@Override public String getHelpInfo(boolean runVisually) { return "Tracks a player using the minimap"; }
	@Override public String getUsage() { return "ex: track notch"; }
	
	@Override
	public void handleTabComplete(ETerminal conIn, EArrayList<String> args) {
		try {
			if (Minecraft.getMinecraft().theWorld != null) {
				EArrayList<String> names = new EArrayList();
				
				Minecraft.getMinecraft().theWorld.playerEntities.forEach(p -> names.add(p.getName()));
				conIn.buildTabCompletions(names);
			
			}
		} catch (Exception e) { e.printStackTrace(); }
	}
	
	@Override
	public void runCommand(ETerminal conIn, EArrayList<String> args, boolean runVisually) {
		mod = (MiniMapApp) RegisteredApps.getApp(AppType.MINIMAP);
		if (args.isEmpty()) {
			if (mod.findPlayer.isEmpty()) { conIn.info(getUsage()); }
			else {
				mod.findPlayer = "";
				mod.trackPlayers.set(false);
				conIn.writeln("Stopped tracker", EColors.cyan);
			}
		}
		if (args.size() == 1) {
			mod.trackPlayers.set(true);
			mod.findPlayer = args.get(0);
			conIn.writeln("Tracking: " + EnumChatFormatting.GREEN + mod.findPlayer, EColors.cyan);
		}
		if (args.size() > 1) {
			conIn.error("Too many arguments!");
		}
	}
}
