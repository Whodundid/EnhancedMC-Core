package com.Whodundid.core.terminal.terminalCommand.commands.system;

import com.Whodundid.core.EnhancedMC;
import com.Whodundid.core.debug.DebugFunctions;
import com.Whodundid.core.terminal.gui.ETerminal;
import com.Whodundid.core.terminal.terminalCommand.CommandType;
import com.Whodundid.core.terminal.terminalCommand.TerminalCommand;
import com.Whodundid.core.util.renderUtil.EColors;
import com.Whodundid.core.util.storageUtil.EArrayList;
import net.minecraft.util.EnumChatFormatting;

//Author: Hunter Bragg

public class DebugControl extends TerminalCommand {
	
	public DebugControl() {
		super(CommandType.NORMAL);
		numArgs = 1;
	}

	@Override public String getName() { return "debug"; }
	@Override public boolean showInHelp() { return false; }
	@Override public EArrayList<String> getAliases() { return new EArrayList<String>("deb", "dev"); }
	@Override public String getHelpInfo(boolean runVisually) { return "Toggles debug mode for EMC."; }
	@Override public String getUsage() { return "ex: deb | deb 0"; }
	
	@Override
	public void handleTabComplete(ETerminal termIn, EArrayList<String> args) {
		EArrayList<String> vals = new EArrayList();
		for (int i = 0; i < DebugFunctions.getNum(); i++) { vals.add("" + i); }
		super.basicTabComplete(termIn, args, vals);
	}
	
	@Override
	public void runCommand(ETerminal termIn, EArrayList<String> args, boolean runVisually) {
		if (args.size() >= 1) {
			try {
				String arg = args.get(0);
				
				if (arg.equals("init")) {
					if (args.size() == 1) {
						boolean val = DebugFunctions.drawWindowInit;
						EnumChatFormatting c = val ? EnumChatFormatting.GREEN : EnumChatFormatting.RED;
						termIn.writeln("Draw window initialization times in debug: " + c + val, EColors.lgray);
					}
					else if (args.size() == 2) {
						try {
							boolean val = Boolean.parseBoolean(args.get(1));
							EnumChatFormatting c = val ? EnumChatFormatting.GREEN : EnumChatFormatting.RED;
							DebugFunctions.drawWindowInit = val;
							termIn.writeln("Set draw init: " + c + val, EColors.lgray);
						}
						catch (Exception e) {
							termIn.error("Error parsing input!");
						}
					}
				}
				else if (arg.equals("pid")) {
					if (args.size() == 1) {
						boolean val = DebugFunctions.drawWindowPID;
						EnumChatFormatting c = val ? EnumChatFormatting.GREEN : EnumChatFormatting.RED;
						termIn.writeln("Draw window process id in debug: " + c + val, EColors.lgray);
					}
					else if (args.size() == 2) {
						try {
							boolean val = Boolean.parseBoolean(args.get(1));
							EnumChatFormatting c = val ? EnumChatFormatting.GREEN : EnumChatFormatting.RED;
							DebugFunctions.drawWindowPID = val;
							termIn.writeln("Set draw PID: " + c + val, EColors.lgray);
						}
						catch (Exception e) {
							termIn.error("Error parsing input!");
						}
					}
				}
				else {
					try {
						int v = Integer.parseInt(args.get(0));
						if (v < 0 || v >= DebugFunctions.getNum()) { termIn.error("Command number " + args.get(0) + " out of range!"); }
						else {
							termIn.writeln("Running debug " + v, 0x55ff55);
							
							if (args.size() > 1) {
								String[] arr = new String[args.size() - 1];
								for (int i = 0; i < args.size() - 1; i++) {
									arr[i] = args.get(i + 1);
								}
								
								DebugFunctions.runDebugFunction(v, termIn, arr);
							}
							else {
								DebugFunctions.runDebugFunction(v);
							}
						}
					}
					catch (Exception e) {
						termIn.error("Error: No debug value found for input!");
					}
				}
			}
			catch (Exception e) {
				e.printStackTrace();
				termIn.error("Unexpected error..");
			}
		}
		else {
			EnhancedMC.setDebugMode(!EnhancedMC.isDebugMode());
			termIn.writeln(EnhancedMC.isDebugMode() ? "Enabled Debug Mode" : "Disabled Debug Mode", 0xaabbcc);
		}
		
	}
	
}
