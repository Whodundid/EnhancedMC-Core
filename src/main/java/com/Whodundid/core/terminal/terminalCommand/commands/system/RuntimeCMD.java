package com.Whodundid.core.terminal.terminalCommand.commands.system;

import com.Whodundid.core.EnhancedMC;
import com.Whodundid.core.terminal.terminalCommand.CommandType;
import com.Whodundid.core.terminal.terminalCommand.TerminalCommand;
import com.Whodundid.core.terminal.window.ETerminal;
import com.Whodundid.core.util.EUtil;
import com.Whodundid.core.util.renderUtil.EColors;
import com.Whodundid.core.util.storageUtil.EArrayList;
import java.util.Comparator;
import net.minecraft.util.EnumChatFormatting;

public class RuntimeCMD extends TerminalCommand {
	
	public RuntimeCMD() {
		super(CommandType.NORMAL);
		setCategory("System");
		numArgs = 0;
	}

	@Override public String getName() { return "runtime"; }
	@Override public boolean showInHelp() { return true; }
	@Override public EArrayList<String> getAliases() { return new EArrayList<String>("rt"); }
	@Override public String getHelpInfo(boolean runVisually) { return "Gets info on the current system run time"; }
	@Override public String getUsage() { return "ex: runtime"; }
	@Override public void handleTabComplete(ETerminal termIn, EArrayList<String> args) {}
	
	@Override
	public void runCommand(ETerminal termIn, EArrayList<String> args, boolean runVisually) {
		if (args.isNotEmpty()) { termIn.error("This command does not take any arguments"); }
		else {
			Runtime rt = Runtime.getRuntime();
			
			double memJVMTotal = (double) ((double) rt.maxMemory() / 1024d / 1024d / 1024d);
			double memJVMUsed = (double) ((double) rt.totalMemory() / 1024d / 1024d / 1024d);
			double memJVMFree = (double) ((double) rt.freeMemory() / 1024d / 1024d / 1024d);
			
			String memJVMTotalString = String.format("%.2f gb", memJVMTotal);
			String memJVMUsedString = String.format("%.2f gb", memJVMUsed);
			String memJVMFreeString = String.format("%.2f gb", memJVMFree);
			
			String javaVer = "Java Version: " + EnumChatFormatting.GREEN + Runtime.class.getPackage().getImplementationVersion();
			String totMem = "Total Memory: " + EnumChatFormatting.GREEN + memJVMTotalString;
			String usedMem = "Used Memory: " + EnumChatFormatting.GREEN + memJVMUsedString;
			String freeMem = "Free Memory: " + EnumChatFormatting.GREEN + memJVMFreeString;
			String obfus = "Obfuscated: " + EnumChatFormatting.GREEN + EnhancedMC.isObfus();
			
			String longest = EArrayList.of(javaVer, totMem, usedMem, obfus).stream().max(Comparator.comparingInt(String::length)).get();
			int len = (longest != null) ? longest.length() : 0;
			
			String divider = EUtil.repeatString("-", len - 3);
			
			//java version
			termIn.writeln(javaVer, EColors.cyan);
			termIn.writeln(divider, EColors.lgray);
			
			//memory
			termIn.writeln(totMem, EColors.cyan);
			termIn.writeln(usedMem, EColors.cyan);
			termIn.writeln(freeMem, EColors.cyan);
			termIn.writeln(divider, EColors.lgray);
			
			//obfus
			termIn.writeln(obfus, EColors.cyan);
		}
		
	}
	
}
