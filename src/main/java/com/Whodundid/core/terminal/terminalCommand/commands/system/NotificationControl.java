package com.Whodundid.core.terminal.terminalCommand.commands.system;

import com.Whodundid.core.EnhancedMC;
import com.Whodundid.core.notifications.util.NotificationType;
import com.Whodundid.core.terminal.gui.ETerminal;
import com.Whodundid.core.terminal.terminalCommand.CommandType;
import com.Whodundid.core.terminal.terminalCommand.TerminalCommand;
import com.Whodundid.core.util.renderUtil.EColors;
import com.Whodundid.core.util.storageUtil.EArrayList;
import net.minecraft.util.EnumChatFormatting;

public class NotificationControl extends TerminalCommand {
	
	public NotificationControl() {
		super(CommandType.NORMAL);
		numArgs = 2;
	}

	@Override public String getName() { return "notification"; }
	@Override public boolean showInHelp() { return true; }
	@Override public EArrayList<String> getAliases() { return new EArrayList<String>("note"); }
	@Override public String getHelpInfo(boolean runVisually) { return "Enables or disables a specified notification type"; }
	@Override public String getUsage() { return "ex: note en emcGeneral"; }
	
	@Override
	public void handleTabComplete(ETerminal termIn, EArrayList<String> args) {
		EArrayList<String> options = new EArrayList("enable", "disable");
		EArrayList<String> types = EnhancedMC.getNotificationHandler().getInternalNames();
		
		if (args.size() <= 1) { basicTabComplete(termIn, args, options); }
		else if (args.size() == 2) { basicTabComplete(termIn, args, types); }
	}
	
	@Override
	public void runCommand(ETerminal termIn, EArrayList<String> args, boolean runVisually) {
		if (args.size() < 2) { termIn.error("Not enough arguments!"); termIn.info(getUsage()); }
		else if (args.size() == 2) {
			String option = args.get(0);
			String type = args.get(1);
			
			switch (option) {
			case "e":
			case "en":
			case "enable": updateType(termIn, type, true); break;
			case "d":
			case "dis":
			case "disable": updateType(termIn, type, false); break;
			default: termIn.error("Invalid action '" + option + "' input!");
			}
		}
		else { termIn.error("Too many arguments!"); termIn.info(getUsage()); }
	}
	
	private void updateType(ETerminal termIn, String type, boolean enable) {
		EArrayList<NotificationType> types = EnhancedMC.getNotificationHandler().getNotificationTypes();
		
		NotificationType theNote = null;
		for (NotificationType t : types) {
			if (t.getInternalName().toLowerCase().equals(type)) {
				theNote = t;
				break;
			}
		}
		
		if (theNote != null) {
			if (enable) { EnhancedMC.getNotificationHandler().enableNotificationType(theNote, true); }
			else { EnhancedMC.getNotificationHandler().disableNotificationType(theNote, true); }
			
			termIn.writeln(type + " " + (enable ? EnumChatFormatting.GREEN + "Enabled" : EnumChatFormatting.RED + "Disabled"), EColors.orange);
		}
		else { termIn.error("The notification type: '" + type + "' does not exist!"); }
	}
	
}
