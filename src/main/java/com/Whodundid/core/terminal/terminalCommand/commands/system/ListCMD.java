package com.Whodundid.core.terminal.terminalCommand.commands.system;

import com.Whodundid.core.EnhancedMC;
import com.Whodundid.core.app.EMCApp;
import com.Whodundid.core.app.RegisteredApps;
import com.Whodundid.core.debug.ExperimentGui;
import com.Whodundid.core.enhancedGui.types.interfaces.IEnhancedGuiObject;
import com.Whodundid.core.enhancedGui.types.interfaces.IWindowParent;
import com.Whodundid.core.terminal.TerminalCommandHandler;
import com.Whodundid.core.terminal.gui.ETerminal;
import com.Whodundid.core.terminal.terminalCommand.CommandType;
import com.Whodundid.core.terminal.terminalCommand.IListableCommand;
import com.Whodundid.core.terminal.terminalCommand.TerminalCommand;
import com.Whodundid.core.util.EUtil;
import com.Whodundid.core.util.chatUtil.EChatUtil;
import com.Whodundid.core.util.renderUtil.EColors;
import com.Whodundid.core.util.storageUtil.EArrayList;
import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.ModContainer;

//Author: Hunter Bragg

public class ListCMD extends TerminalCommand {
	
	public ListCMD() {
		super(CommandType.NORMAL);
		numArgs = 1;
	}

	@Override public String getName() { return "list"; }
	@Override public boolean showInHelp() { return true; }
	@Override public EArrayList<String> getAliases() { return new EArrayList<String>("l"); }
	@Override public String getHelpInfo(boolean runVisually) { return "Used to list various things. (mods, players, etc.)"; }
	@Override public String getUsage() { return "ex: list players"; }
	
	@Override
	public void handleTabComplete(ETerminal termIn, EArrayList<String> args) {
		String[] types = {"apps", "mods", "objects", "guis"};
		super.basicTabComplete(termIn, args, new EArrayList().addA(types));
	}
	
	@Override
	public void runCommand(ETerminal termIn, EArrayList<String> args, boolean runVisually) {
		if (args.isEmpty()) {
			if (runVisually) { termIn.writeln("mods, players, objects, guis", EColors.green); }
			else { termIn.info(getUsage()); }
		}
		else if (args.size() > 1) { termIn.error("Too many arguments!"); }
		else {
			switch (args.get(0)) {
			case "m":
			case "mods": listMods(termIn, args, runVisually); break;
			case "a":
			case "app":
			case "apps": listApps(termIn, args, runVisually); break;
			case "p":
			case "player":
			case "players": listPlayers(termIn, args, runVisually); break;
			case "o":
			case "obj":
			case "objects": listObjects(termIn, args, runVisually); break;
			case "g":
			case "gui":
			case "guis": listGuis(termIn, args, runVisually); break;
			default:
				boolean found = false;
				
				//check if the input is a listable command
				for (TerminalCommand c : TerminalCommandHandler.getInstance().getCommandList()) {
					String name = args.get(0).toLowerCase();
					
					if (c.getName().equals(name) || EUtil.findMatch(name, c.getAliases())) {
						if (c instanceof IListableCommand) {
							found = true;
							((IListableCommand) c).list(termIn, args, runVisually);
						}
					}
				}
				
				if (!found) { termIn.error("Unrecognized list type!"); }
			}
		}
	}
	
	private void listApps(ETerminal termIn, EArrayList<String> args, boolean runVisually) {
		EArrayList<EMCApp> mods = RegisteredApps.getAppsList();
		
		if (mods != null) {
			termIn.info("Listing all EMC Apps");
			for (EMCApp m : RegisteredApps.getAppsList()) {
				EColors c = m.isIncompatible() ? EColors.red : EColors.lgray;
				String s = "-" + m.getName() + ": " + (m.isEnabled() ? EnumChatFormatting.GREEN + "Enabled" : EnumChatFormatting.RED + "Disabled");
				if (m.isIncompatible()) { s = "-" + m.getName() + ": Incompatible!"; }
				
				if (runVisually) {
					String aliasList = EnumChatFormatting.GRAY + " ; " + EnumChatFormatting.AQUA;
					for (int i = 0; i < m.getNameAliases().size(); i++) {
						String commandAlias = m.getNameAliases().get(i);
						if (i == m.getNameAliases().size() - 1) { aliasList += commandAlias; }
						else { aliasList += (commandAlias + ", "); }
					}
					s += aliasList;
				}
				
				termIn.writeln(s, c);
			}
			termIn.writeln("Total Apps: " + mods.size(), 0xffff00);
		}
	}
	
	private void listMods(ETerminal termIn, EArrayList<String> args, boolean runVisually) {
		termIn.info("Listing all Minecraft mods");
		int modCount = 0;
		for (ModContainer c : Loader.instance().getModList()) {
			Object m = c.getMod();
			
			if (!(m instanceof EMCApp)) {
				termIn.writeln("-" + c.getName() + ": " + EnumChatFormatting.WHITE + c.getDisplayVersion(), EColors.lgray);
				modCount++;
			}
		}
		termIn.writeln("Total mods: " + modCount, 0xffff00);
	}
	
	private void listPlayers(ETerminal termIn, EArrayList<String> args, boolean runVisually) {
		try {
			Minecraft mc = Minecraft.getMinecraft();
			List<EntityPlayer> list = mc.theWorld.playerEntities;
			
			termIn.info("Listing all current players in world");
			for (EntityPlayer p : list) {
				int c = 0xb2b2b2;
				String s = "-" + p.getName();
				if (p.isUser()) { s += EnumChatFormatting.GREEN + " (you)"; c = 0x66ff66; }
				if (p.isInvisible()) { s += " is invisible"; c = EColors.magenta.c(); }
				termIn.writeln(s, c);
			}
			termIn.writeln("Total players: " + list.size(), 0xffff00);
			
		} catch (Exception e) {
			termIn.error("Java Error!");
			termIn.writeln(e.getMessage(), 0xff0000);
		}
	}
	
	private void listObjects(ETerminal termIn, EArrayList<String> args, boolean runVisually) {
		termIn.info("Listing all current objects in renderer");
		if (runVisually) {
			int grandTotal = 0; //this isn't completely right tree wise, but whatever
			for (IEnhancedGuiObject obj : EnhancedMC.getRenderer().getObjects()) {
				termIn.writeln(obj.toString(), EColors.green);
				
				//int depth = 3;
				
				EArrayList<IEnhancedGuiObject> foundObjs = new EArrayList();
				EArrayList<IEnhancedGuiObject> objsWithChildren = new EArrayList();
				EArrayList<IEnhancedGuiObject> workList = new EArrayList();
				
				//grab all immediate children and add them to foundObjs, then check if any have children of their own
				obj.getObjects().forEach(o -> { foundObjs.add(o); if (!o.getObjects().isEmpty()) { objsWithChildren.add(o); } });
				//load the workList with every child found on each object
				objsWithChildren.forEach(c -> workList.addAll(c.getObjects()));
				
				for (IEnhancedGuiObject o : EArrayList.combineLists(objsWithChildren, workList)) {
					String s = "   ";
					//for (int i = 0; i < depth; i++) { s += " "; }
					termIn.writeln(s + EChatUtil.removeFormattingCodes(o.toString()), EColors.lgray);
				}
				//depth += 3;
				
				//only work as long as there are still child layers to process
				while (workList.isNotEmpty()) {
					//update the foundObjs
					foundObjs.addAll(workList);
					
					//for the current layer, find all objects that have children
					objsWithChildren.clear();
					workList.stream().filter(o -> !o.getObjects().isEmpty()).forEach(objsWithChildren::add);
					
					//put all children on the next layer into the work list
					workList.clear();
					objsWithChildren.forEach(c -> workList.addAll(c.getObjects()));
					
					for (IEnhancedGuiObject o : EArrayList.combineLists(objsWithChildren, workList)) {
						String s = "   ";
						//for (int i = 0; i < depth; i++) { s += " "; }
						termIn.writeln(s + EChatUtil.removeFormattingCodes(o.toString()), EColors.lgray);
					}
					//depth += 3;
				}
				
				termIn.writeln("Total objects: " + foundObjs.size(), EColors.yellow);
				
				grandTotal += foundObjs.size();
			}
			
			termIn.writeln("Grand total: " + grandTotal, EColors.orange);
		}
		else {
			for (IEnhancedGuiObject obj : EnhancedMC.getRenderer().getObjects()) {
				termIn.writeln(obj.toString(), EColors.green);
			}
			termIn.writeln("Total objects: " + EnhancedMC.getRenderer().getObjects().size(), 0xffff00);
		}
	}
	
	private void listGuis(ETerminal termIn, EArrayList<String> args, boolean runVisually) {
		EArrayList<EMCApp> mods = RegisteredApps.getAppsList();
		if (mods != null) {
			termIn.info("Listing all EMC Guis");
			
			termIn.writeln("Debug", EColors.cyan);
			String experiment = "  -" + ExperimentGui.class.getSimpleName();
			String terminal = "  -" + termIn.getClass().getSimpleName();
			
			experiment += ": " + EnumChatFormatting.GREEN + "experimentgui, experiment, testgui";
			terminal += ": " + EnumChatFormatting.GREEN;
			for (String s : termIn.getAliases()) { terminal += s + ", "; }
			if (termIn.getAliases().size() > 0) { terminal = terminal.substring(0, terminal.length() - 2); }
			
			termIn.writeln(experiment, EColors.lgray);
			termIn.writeln(terminal, EColors.lgray);
			
			for (EMCApp m : RegisteredApps.getAppsList()) {
				if (m.getGuis().isNotEmpty()) {
					termIn.writeln();
					termIn.writeln(m.getName(), EColors.cyan);
					for (IWindowParent g : m.getGuis()) {
						String gui = "  -" + g.getClass().getSimpleName();
						
						if (g.getAliases().isNotEmpty()) {
							gui += ": " + EnumChatFormatting.GREEN;
							for (String s : g.getAliases()) { gui += s + ", "; }
							if (g.getAliases().size() > 0) { gui = gui.substring(0, gui.length() - 2); }
						}
						
						termIn.writeln(gui, EColors.lgray);
					}
				}
			}
			
			//vanilla
			termIn.info("\nListing all Vanilla Guis");
			termIn.writeln("  -Chat", EColors.lgray);
			termIn.writeln("  -Pause", EColors.lgray);
			termIn.writeln("  -Mainmenu", EColors.lgray);
			termIn.writeln("  -Multiplayer", EColors.lgray);
			termIn.writeln("  -Skinoptions", EColors.lgray);
			termIn.writeln("  -Resourcepacks", EColors.lgray);
			termIn.writeln("  -Options", EColors.lgray);
			termIn.writeln("  -Videooptions", EColors.lgray);
			termIn.writeln("  -Chatoptions", EColors.lgray);
			termIn.writeln("  -Sound", EColors.lgray);
			termIn.writeln("  -Snooper", EColors.lgray);
			termIn.writeln("  -Language", EColors.lgray);
		}
		else { termIn.error("Unknown error!"); }
	}
	
}
