package com.Whodundid.core.debug.terminal.commands;

import com.Whodundid.core.debug.terminal.gui.ETerminal;
import com.Whodundid.core.util.storageUtil.EArrayList;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiMainMenu;
import net.minecraft.client.gui.GuiMultiplayer;
import net.minecraft.realms.RealmsBridge;

public class DisconnectServer implements IConsoleCommand {

	@Override public String getName() { return "disconnect"; }
	@Override public EArrayList<String> getAliases() { return new EArrayList<String>("dis", "discon", "log", "logout", "logoff", "quit"); }
	@Override public String getCommandHelpInfo() { return "Disconnects from the current game server and returns to the main menu."; }
	@Override public String getCommandErrorInfo(String arg) { return null; }
	@Override public EArrayList<String> getTabCompleteList() { return null; }	
	
	@Override
	public void runCommand(ETerminal conIn, EArrayList<String> args, boolean runVisually) {
		Minecraft mc = Minecraft.getMinecraft();
		boolean flag = mc.isIntegratedServerRunning();
        boolean flag1 = mc.func_181540_al();
		mc.theWorld.sendQuittingDisconnectingPacket();
		mc.loadWorld(null);
		if (flag) { mc.displayGuiScreen(new GuiMainMenu()); }
		else if (flag1) {
			RealmsBridge bridge = new RealmsBridge();
			bridge.switchToRealms(new GuiMainMenu());
		} else { mc.displayGuiScreen(new GuiMultiplayer(new GuiMainMenu())); }
	}
}
