package com.Whodundid.core.terminal.terminalCommand.commands;

import com.Whodundid.core.subMod.RegisteredSubMods;
import com.Whodundid.core.subMod.SubMod;
import com.Whodundid.core.terminal.gui.ETerminal;
import com.Whodundid.core.terminal.terminalCommand.IConsoleCommand;
import com.Whodundid.core.util.renderUtil.EColors;
import com.Whodundid.core.util.storageUtil.EArrayList;
import com.Whodundid.core.util.storageUtil.StorageBox;
import com.Whodundid.core.util.storageUtil.StorageBoxHolder;
import net.minecraft.util.EnumChatFormatting;

public class ModInfo implements IConsoleCommand {

	@Override public String getName() { return "modinfo"; }
	@Override public boolean showInHelp() { return true; }
	@Override public EArrayList<String> getAliases() { return new EArrayList<String>("info", "i"); }
	@Override public String getHelpInfo(boolean runVisually) { return "Displays information on a specific EMC submod"; }
	@Override public String getUsage() { return "ex: info core"; }
	@Override public EArrayList<String> getTabCompleteList() { return null; }
	
	@Override
	public void runCommand(ETerminal conIn, EArrayList<String> args, boolean runVisually) {
		if (args.size() > 1) {
			conIn.error("Too many arguments!");
			conIn.error(getUsage());
		}
		else if (args.size() == 1) {
			SubMod m = RegisteredSubMods.getModByAlias(args.get(0));
			if (m == null) { conIn.error("No mod by that name found!"); return; }
			
			conIn.writeln("SubMod: " + m.getName(), EColors.cyan);
			conIn.writeln(m.isEnabled() ? "Enabled" : "Disabled", m.isEnabled() ? EColors.green : EColors.lightRed);
			conIn.writeln("Author: " + m.getAuthor(), EColors.lgray);
			conIn.writeln("Version: " + m.getVersion(), EColors.lgray);
			conIn.writeln("Version Date: " + m.getVersionDate(), EColors.lgray);
			
			if (m.getDependencies().isEmpty()) { conIn.writeln("Dependencies: none", EColors.lgray); }
			else {
				conIn.writeln("Dependencies:", EColors.lgray);
				for (StorageBox<String, String> depBox : m.getDependencies()) {
					conIn.writeln(" -" + depBox.getObject() + " v" + depBox.getValue(), EColors.lgray);
				}
			}
			
			if (m.isIncompatible()) {
				conIn.writeln("Incompatible", EColors.lightRed);
				StorageBoxHolder<SubMod, String> incompatMods = RegisteredSubMods.getModImcompatibility(m);
				for (StorageBox<SubMod, String> box : incompatMods) {
					SubMod dep = box.getObject();
					conIn.writeln(" -" + EnumChatFormatting.RED + "requires " + EnumChatFormatting.YELLOW + 
								  dep.getName() + EnumChatFormatting.RED + " version '" + box.getValue() + "'", EColors.lgray);
				}
			}
		}
		else {
			conIn.info(getUsage());
		}
	}
}
