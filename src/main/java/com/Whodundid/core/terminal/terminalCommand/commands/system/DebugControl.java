package com.Whodundid.core.terminal.terminalCommand.commands.system;

import com.Whodundid.core.EnhancedMC;
import com.Whodundid.core.debug.DebugFunctions;
import com.Whodundid.core.terminal.terminalCommand.CommandType;
import com.Whodundid.core.terminal.terminalCommand.TerminalCommand;
import com.Whodundid.core.terminal.window.ETerminal;
import com.Whodundid.core.util.renderUtil.EColors;
import com.Whodundid.core.util.storageUtil.EArrayList;
import net.minecraft.util.EnumChatFormatting;

//Author: Hunter Bragg

public class DebugControl extends TerminalCommand {
	
	public DebugControl() {
		super(CommandType.NORMAL);
		setCategory("System");
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
		for (int i = 0; i < DebugFunctions.getTotal(); i++) { vals.add("" + i); }
		super.basicTabComplete(termIn, args, vals);
	}
	
	@Override
	public void runCommand(ETerminal termIn, EArrayList<String> args, boolean runVisually) {
		if (args.size() >= 1) {
			try {
				String arg = args.get(0).toLowerCase();
				
				if (arg.equals("init")) {
					if (args.size() == 1) {
						boolean val = DebugFunctions.drawWindowInit;
						EnumChatFormatting c = val ? EnumChatFormatting.GREEN : EnumChatFormatting.RED;
						termIn.writeln("Draw window initialization times in debug: " + c + val, EColors.lgray);
					}
					else if (args.size() == 2) {
						try {
							boolean val = Boolean.parseBoolean(args.get(1).toLowerCase());
							EnumChatFormatting c = val ? EnumChatFormatting.GREEN : EnumChatFormatting.RED;
							DebugFunctions.drawWindowInit = val;
							termIn.writeln("Set draw init: " + c + val, EColors.lgray);
						}
						catch (Exception e) {
							termIn.error("Error parsing input!");
							error(termIn, e);
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
							boolean val = Boolean.parseBoolean(args.get(1).toLowerCase());
							EnumChatFormatting c = val ? EnumChatFormatting.GREEN : EnumChatFormatting.RED;
							DebugFunctions.drawWindowPID = val;
							termIn.writeln("Set draw PID: " + c + val, EColors.lgray);
						}
						catch (Exception e) {
							termIn.error("Error parsing input!");
							error(termIn, e);
						}
					}
				}
				else if (arg.equals("drawinfo")) {
					if (args.size() == 1) {
						boolean val = DebugFunctions.drawInfo;
						EnumChatFormatting c = val ? EnumChatFormatting.GREEN : EnumChatFormatting.RED;
						termIn.writeln("Draw EMC debug info: " + c + val, EColors.lgray);
					}
					else if (args.size() == 2) {
						try {
							boolean val = Boolean.parseBoolean(args.get(1).toLowerCase());
							EnumChatFormatting c = val ? EnumChatFormatting.GREEN : EnumChatFormatting.RED;
							DebugFunctions.drawInfo = val;
							termIn.writeln("Set draw EMC debug info: " + c + val, EColors.lgray);
						}
						catch (Exception e) {
							termIn.error("Error parsing input!");
							error(termIn, e);
						}
					}
				}
				else {
					try {
						int v = Integer.parseInt(args.get(0));
						if (v < 0 || v >= DebugFunctions.getTotal()) { termIn.error("Command number " + args.get(0) + " out of range!"); }
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
								DebugFunctions.runDebugFunction(v, termIn);
							}
						}
					}
					catch (Exception e) {
						termIn.error("Error: No debug value found for input!");
						error(termIn, e);
					}
				}
			}
			catch (Exception e) {
				termIn.error("Unexpected error..");
				error(termIn, e);
			}
		}
		else {
			if (runVisually) {
				termIn.writeln("Debug Mode: " + (EnhancedMC.isDebugMode() ? EnumChatFormatting.GREEN : EnumChatFormatting.RED) + EnhancedMC.isDebugMode(), EColors.lgray);
				termIn.writeln("drawInfo: " + (DebugFunctions.drawInfo ? EnumChatFormatting.GREEN : EnumChatFormatting.RED) + DebugFunctions.drawInfo, EColors.lgray);
				termIn.writeln("pid: " + (DebugFunctions.drawWindowPID ? EnumChatFormatting.GREEN : EnumChatFormatting.RED) + DebugFunctions.drawWindowPID, EColors.lgray);
				termIn.writeln("init: " + (DebugFunctions.drawWindowInit ? EnumChatFormatting.GREEN : EnumChatFormatting.RED) + DebugFunctions.drawWindowInit, EColors.lgray);
			}
			else {
				EnhancedMC.setDebugMode(!EnhancedMC.isDebugMode());
				termIn.writeln(EnhancedMC.isDebugMode() ? "Enabled Debug Mode" : "Disabled Debug Mode", 0xaabbcc);
			}
		}
		
	}
	
}
