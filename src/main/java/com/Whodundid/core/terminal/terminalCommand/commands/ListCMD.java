package com.Whodundid.core.terminal.terminalCommand.commands;

import com.Whodundid.core.EnhancedMC;
import com.Whodundid.core.debug.ExperimentGui;
import com.Whodundid.core.enhancedGui.types.interfaces.IEnhancedGuiObject;
import com.Whodundid.core.enhancedGui.types.interfaces.IWindowParent;
import com.Whodundid.core.subMod.RegisteredSubMods;
import com.Whodundid.core.subMod.SubMod;
import com.Whodundid.core.terminal.gui.ETerminal;
import com.Whodundid.core.terminal.terminalCommand.IConsoleCommand;
import com.Whodundid.core.util.chatUtil.EChatUtil;
import com.Whodundid.core.util.miscUtil.NetPlayerComparator;
import com.Whodundid.core.util.renderUtil.EColors;
import com.Whodundid.core.util.storageUtil.EArrayList;
import com.google.common.collect.Ordering;
import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumChatFormatting;

//Author: Hunter Bragg

public class ListCMD implements IConsoleCommand {

	@Override public String getName() { return "list"; }
	@Override public boolean showInHelp() { return true; }
	@Override public EArrayList<String> getAliases() { return new EArrayList<String>("l"); }
	@Override public String getHelpInfo(boolean runVisually) { return "Used to list various things. (mods, players, etc.)"; }
	@Override public String getUsage() { return "ex: list players"; }
	@Override public EArrayList<String> getTabCompleteList() { return new EArrayList("submods", "players", "objects", "guis"); }
	
	@Override
	public void runCommand(ETerminal conIn, EArrayList<String> args, boolean runVisually) {
		if (args.isEmpty()) {
			if (runVisually) { conIn.writeln("mods, players, objects, guis", EColors.green); }
			else { conIn.info(getUsage()); }
		}
		else if (args.size() > 1) { conIn.error("Too many arguments!"); }
		else {
			switch (args.get(0)) {
			case "m":
			case "mods":
			case "submods": listMods(conIn, args, runVisually); break;
			case "p":
			case "player":
			case "players": listPlayers(conIn, args, runVisually); break;
			case "o":
			case "obj":
			case "objects": listObjects(conIn, args, runVisually); break;
			case "g":
			case "gui":
			case "guis": listGuis(conIn, args, runVisually); break;
			default: conIn.error("Unrecognized list type!");
			}
		}
	}
	
	private void listMods(ETerminal conIn, EArrayList<String> args, boolean runVisually) {
		EArrayList<SubMod> mods = RegisteredSubMods.getModsList();
		if (mods != null) {
			conIn.writeln("Listing all EMC Submods...", 0x00ffff);
			for (SubMod m : RegisteredSubMods.getModsList()) {
				EColors c = m.isIncompatible() ? EColors.lightRed : EColors.lgray;
				String s = "-" + m.getName() + ": " + (m.isEnabled() ? EnumChatFormatting.GREEN + "Enabled" : EnumChatFormatting.RED + "Disabled");
				
				if (runVisually) {
					String aliasList = EnumChatFormatting.GRAY + " ; " + EnumChatFormatting.AQUA;
					for (int i = 0; i < m.getNameAliases().size(); i++) {
						String commandAlias = m.getNameAliases().get(i);
						if (i == m.getNameAliases().size() - 1) { aliasList += commandAlias; }
						else { aliasList += (commandAlias + ", "); }
					}
					s += aliasList;
				}
				
				conIn.writeln(s, c);
			}
			conIn.writeln("Total mods: " + mods.size(), 0xffff00);
		} else { conIn.error("Unknown error!"); }
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
				if (runVisually && EnhancedMC.isOpMode()) {
					BlockPos pos = p.getPosition();
					s += EnumChatFormatting.LIGHT_PURPLE + " (" + pos.getX() + ", " + pos.getY() + ", " + pos.getZ() + ")";
				}
				if (p.isUser()) { s += EnumChatFormatting.GREEN + " (you)"; c = 0x66ff66; }
				if (p.isInvisible()) { s += " is invisible"; c = EColors.magenta.c(); }
				conIn.writeln(s, c);
			}
			conIn.writeln("Total players: " + list.size(), 0xffff00);
			
		} catch (Exception e) {
			conIn.error("Java Error!");
			conIn.writeln(e.getMessage(), 0xff0000);
		}
	}
	
	private void listObjects(ETerminal conIn, EArrayList<String> args, boolean runVisually) {
		conIn.writeln("Listing all current objects in renderer...", 0x00ffff);
		if (runVisually) {
			int grandTotal = 0; //this isn't completely right tree wise, but whatever
			for (IEnhancedGuiObject obj : EnhancedMC.getRenderer().getObjects()) {
				conIn.writeln(obj.toString(), EColors.green);
				
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
					conIn.writeln(s + EChatUtil.removeFormattingCodes(o.toString()), EColors.lgray);
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
						conIn.writeln(s + EChatUtil.removeFormattingCodes(o.toString()), EColors.lgray);
					}
					//depth += 3;
				}
				
				conIn.writeln("Total objects: " + foundObjs.size(), EColors.yellow);
				
				grandTotal += foundObjs.size();
			}
			
			conIn.writeln("Grand total: " + grandTotal, EColors.orange);
		}
		else {
			for (IEnhancedGuiObject obj : EnhancedMC.getRenderer().getObjects()) {
				conIn.writeln(obj.toString(), EColors.green);
			}
			conIn.writeln("Total objects: " + EnhancedMC.getRenderer().getObjects().size(), 0xffff00);
		}
	}
	
	private void listGuis(ETerminal conIn, EArrayList<String> args, boolean runVisually) {
		EArrayList<SubMod> mods = RegisteredSubMods.getModsList();
		if (mods != null) {
			conIn.writeln("Listing all EMC Guis...", EColors.cyan);
			
			conIn.writeln("-Debug", EColors.green);
			String experiment = "    " + ExperimentGui.class.getSimpleName();
			String terminal = "    " + conIn.getClass().getSimpleName();
			if (runVisually) {
				experiment += " " + EnumChatFormatting.AQUA + "experimentgui, experiment, testgui";
				terminal += " " + EnumChatFormatting.AQUA;
				for (String s : conIn.getAliases()) { terminal += s + ", "; }
				if (conIn.getAliases().size() > 0) { terminal = terminal.substring(0, terminal.length() - 2); }
			}
			conIn.writeln(experiment, EColors.lgray);
			conIn.writeln(terminal, EColors.lgray);
			
			for (SubMod m : RegisteredSubMods.getModsList()) {
				if (m.getGuis().isNotEmpty()) {
					conIn.writeln("-" + m.getName(), EColors.green);
					for (IWindowParent g : m.getGuis()) {
						String gui = "    " + g.getClass().getSimpleName();
						if (runVisually) {
							gui += " " + EnumChatFormatting.AQUA;
							for (String s : g.getAliases()) { gui += s + ", "; }
							if (g.getAliases().size() > 0) { gui = gui.substring(0, gui.length() - 2); }
						}
						conIn.writeln(gui, EColors.lgray);
					}
				}
			}
		} else { conIn.error("Unknown error!"); }
	}
}
