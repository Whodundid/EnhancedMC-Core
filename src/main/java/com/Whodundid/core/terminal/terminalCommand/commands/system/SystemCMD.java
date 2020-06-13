package com.Whodundid.core.terminal.terminalCommand.commands.system;

import com.Whodundid.core.terminal.terminalCommand.CommandType;
import com.Whodundid.core.terminal.terminalCommand.TerminalCommand;
import com.Whodundid.core.terminal.window.ETerminal;
import com.Whodundid.core.util.renderUtil.EColors;
import com.Whodundid.core.util.storageUtil.EArrayList;
import com.sun.management.OperatingSystemMXBean;
import java.io.File;
import java.lang.management.ManagementFactory;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.util.EnumChatFormatting;
import org.lwjgl.opengl.GL11;
import oshi.SystemInfo;

public class SystemCMD extends TerminalCommand {
	
	public SystemCMD() {
		super(CommandType.NORMAL);
		setCategory("System");
		numArgs = 0;
	}

	@Override public String getName() { return "system"; }
	@Override public boolean showInHelp() { return true; }
	@Override public EArrayList<String> getAliases() { return new EArrayList<String>("sys"); }
	@Override public String getHelpInfo(boolean runVisually) { return "Displays information on the system"; }
	@Override public String getUsage() { return "ex: sys"; }
	@Override public void handleTabComplete(ETerminal termIn, EArrayList<String> args) { }
	
	@Override
	public void runCommand(ETerminal termIn, EArrayList<String> args, boolean runVisually) {
		if (args.isNotEmpty()) { termIn.error("This command does not take any arguments"); }
		else {
			SystemInfo info = new SystemInfo();
			OperatingSystemMXBean bean = ManagementFactory.getPlatformMXBean(OperatingSystemMXBean.class);
			
			//os
			termIn.writeln("OS:", EColors.orange);
			
			try {
				termIn.writeln("  Brand: " + EnumChatFormatting.GREEN + System.getProperty("os.name"), EColors.cyan);
				termIn.writeln("  Manufacturer: " + EnumChatFormatting.GREEN + info.getOperatingSystem().getManufacturer(), EColors.cyan);
				termIn.writeln("  Version: " + EnumChatFormatting.GREEN + System.getProperty("os.version"), EColors.cyan);
				termIn.writeln("  Architecture: " + EnumChatFormatting.GREEN + System.getProperty("sun.arch.data.model") + "-bit", EColors.cyan);
			}
			catch (Exception e) {
				termIn.error("Error fetching operating system values..");
				error(termIn, e);
			}
			
			//cpu
			termIn.writeln("\nCPU:", EColors.orange);
			
			try {
				termIn.writeln("  CPU Name: " + EnumChatFormatting.GREEN + OpenGlHelper.getCpu(), EColors.cyan);
				termIn.writeln("  Brand: " + EnumChatFormatting.GREEN + System.getenv("PROCESSOR_IDENTIFIER"), EColors.cyan);
			}
			catch (Exception e) {
				termIn.error("Error fetching CPU values..");
				error(termIn, e);
			}
			
			//gpu
			termIn.writeln("\nGPU:", EColors.orange);
			
			try {
				termIn.writeln("  Model: " + EnumChatFormatting.GREEN + GL11.glGetString(GL11.GL_RENDERER), EColors.cyan);
				termIn.writeln("  Vendor: " + EnumChatFormatting.GREEN + GL11.glGetString(GL11.GL_VENDOR), EColors.cyan);
				termIn.writeln("  Version: " + EnumChatFormatting.GREEN + GL11.glGetString(GL11.GL_VERSION), EColors.cyan);
			}
			catch (Exception e) {
				termIn.error("Error fetching GPU values..");
				error(termIn, e);
			}
			
			//memory
			termIn.writeln("\nRAM: (gb)", EColors.orange);
			
			try {
				double memTotal = ((double) bean.getTotalPhysicalMemorySize() / 1024d / 1024d / 1024d);
				double memFree =  ((double) bean.getFreePhysicalMemorySize() / 1024d / 1024d / 1024d);
				double memUsed = memTotal - memFree;
				
				String memTotalString = String.format("%.2f", memTotal);
				String memFreeString = String.format("%.2f", memFree);
				String memUsedString = String.format("%.2f", memUsed);
				
				termIn.writeln("  Total: " + EnumChatFormatting.GREEN + memTotalString, EColors.cyan);
				termIn.writeln("  Available: " + EnumChatFormatting.GREEN + memFreeString, EColors.cyan);
				termIn.writeln("  Used: " + EnumChatFormatting.GREEN + memUsedString, EColors.cyan);
			}
			catch (Exception e) {
				termIn.error("Error fetching system RAM values..");
				error(termIn, e);
			}
			
			//jvm memory
			termIn.writeln("\nJVM Memory: (gb)", EColors.orange);
			
			try {
				double memJVMTotal = (double) ((double) Runtime.getRuntime().maxMemory() / 1024d / 1024d / 1024d);
				double memJVMUsed = (double) ((double) Runtime.getRuntime().totalMemory() / 1024d / 1024d / 1024d);
				double memJVMFree = (double) ((double) Runtime.getRuntime().freeMemory() / 1024d / 1024d / 1024d);
				
				String memJVMTotalString = String.format("%.2f", memJVMTotal);
				String memJVMUsedString = String.format("%.2f", memJVMUsed);
				String memJVMFreeString = String.format("%.2f", memJVMFree);
				
				termIn.writeln("  Total: " + EnumChatFormatting.GREEN + memJVMTotalString, EColors.cyan);
				termIn.writeln("  Used: " + EnumChatFormatting.GREEN + memJVMUsedString, EColors.cyan);
				termIn.writeln("  Available: " + EnumChatFormatting.GREEN + memJVMFreeString, EColors.cyan);
			}
			catch (Exception e) {
				termIn.error("Error fetching JVM Memory values..");
				error(termIn, e);
			}
			
			//drives
			termIn.writeln("\nDrives: (gb)", EColors.orange);
			
			try {
				File[] drives = File.listRoots();
				for (File d : drives) {
					String driveName = d.getAbsolutePath();
					boolean primary = false;
					
					try {
						primary = driveName.substring(0, driveName.length() - 1).equals(System.getenv("SystemDrive"));
					}
					catch (Exception e) {
						error(termIn, e);
					}
					
					
					termIn.writeln("  " + d.getAbsolutePath() + (primary ? EnumChatFormatting.LIGHT_PURPLE + " (primary)" : ""), EColors.blue);
					
					double total = (double) ((double) d.getTotalSpace() / 1024d / 1024d / 1024d);
					double free = (double) ((double) d.getFreeSpace() / 1024d / 1024d / 1024d);
					
					String totalString = String.format("%.2f", total);
					String freeString = String.format("%.2f", free);
					
					termIn.writeln("    Total: " + EnumChatFormatting.GREEN + totalString, EColors.cyan);
					termIn.writeln("    Free: " + EnumChatFormatting.GREEN + freeString, EColors.cyan);
				}
			}
			catch (Exception q) {
				termIn.error("Error fetching system drive values..");
				error(termIn, q);
			}
		} //else
	}
	
}
