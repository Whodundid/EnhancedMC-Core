package com.Whodundid.core.terminal.terminalCommand.commands.system;

import com.Whodundid.core.EnhancedMC;
import com.Whodundid.core.app.AppResources;
import com.Whodundid.core.app.EMCApp;
import com.Whodundid.core.app.RegisteredApps;
import com.Whodundid.core.terminal.terminalCommand.CommandType;
import com.Whodundid.core.terminal.terminalCommand.TerminalCommand;
import com.Whodundid.core.terminal.window.ETerminal;
import com.Whodundid.core.util.renderUtil.EColors;
import com.Whodundid.core.util.resourceUtil.EResource;
import com.Whodundid.core.util.storageUtil.EArrayList;
import java.lang.reflect.Field;
import net.minecraft.util.EnumChatFormatting;

public class ReloadResources extends TerminalCommand {
	
	public ReloadResources() {
		super(CommandType.NORMAL);
		setCategory("System");
		numArgs = 0;
	}

	@Override public String getName() { return "reloadresources"; }
	@Override public boolean showInHelp() { return EnhancedMC.isDevMode(); }
	@Override public EArrayList<String> getAliases() { return new EArrayList<String>("relres"); }
	@Override public String getHelpInfo(boolean runVisually) { return "Reloads all EMC resources"; }
	@Override public String getUsage() { return "ex: relres"; }
	@Override public void handleTabComplete(ETerminal termIn, EArrayList<String> args) { }
	
	@Override
	public void runCommand(ETerminal termIn, EArrayList<String> args, boolean runVisually) {
		if (EnhancedMC.isDevMode()) {
			termIn.writeln("Reloading EMC Resources...\n", EColors.yellow);
			
			for (EMCApp app : RegisteredApps.getAppsList()) {
				AppResources resources = app.getResources();
				
				if (resources != null) {
					Class c = resources.getClass();
					Field[] fields = c.getDeclaredFields();
					
					for (Field f : fields) {
						if (f.getType().isAssignableFrom(EResource.class)) {
							try {
								EResource er = (EResource) f.get(resources);
								
								termIn.writeln("Re-Registering: " + EnumChatFormatting.GREEN + er.getFullPath(), EColors.cyan);
								er.register();
							}
							catch (Exception e) {
								error(termIn, e);
							}
						}
					}
					
				}
				
			}
			
			EnhancedMC.reloadAllWindows();
		}
		else { termIn.error("Unrecognized command."); }
	}
	
}