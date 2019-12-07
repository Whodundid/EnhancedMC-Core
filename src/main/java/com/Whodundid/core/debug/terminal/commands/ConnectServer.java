package com.Whodundid.core.debug.terminal.commands;

import com.Whodundid.core.EnhancedMC;
import com.Whodundid.core.debug.terminal.gui.ETerminal;
import com.Whodundid.core.util.storageUtil.EArrayList;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.concurrent.atomic.AtomicInteger;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiMainMenu;
import net.minecraft.client.multiplayer.ServerAddress;
import net.minecraft.client.multiplayer.ServerData;
import net.minecraft.client.network.NetHandlerLoginClient;
import net.minecraft.client.resources.I18n;
import net.minecraft.network.EnumConnectionState;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.handshake.client.C00Handshake;
import net.minecraft.network.login.client.C00PacketLoginStart;
import net.minecraftforge.fml.client.FMLClientHandler;

public class ConnectServer implements IConsoleCommand {

	@Override public String getName() { return "connect"; }
	@Override public EArrayList<String> getAliases() { return new EArrayList<String>("con", "join", "server"); }
	@Override public String getCommandHelpInfo() { return "Attempts to connect to a Minecraft server."; }
	
	@Override
	public String getCommandErrorInfo(String arg) {
		if (arg != null) {
			switch (arg) {
			case "narg": return "Specify a server ip address to connect to!";
			case "targ": return "Too many arguments! Only specify one ip!";
			}
		}
		return "Unknown error!";
	}
	
	@Override public EArrayList<String> getTabCompleteList() { return null; }
	
	@Override
	public void runCommand(ETerminal conIn, EArrayList<String> args, boolean runVisually) {
		if (args.isEmpty()) { conIn.error(getCommandErrorInfo("narg")); }
		else if (args.size() > 1) { conIn.error(getCommandErrorInfo("targ")); }
		else {
			Minecraft mc = Minecraft.getMinecraft();
			mc.theWorld.sendQuittingDisconnectingPacket();
			mc.loadWorld(null);
			mc.displayGuiScreen(new GuiMainMenu());
			
			FMLClientHandler.instance().setupServerList();
			ServerData server = new ServerData("serverName", args.get(0), false);
			ServerAddress serveraddress = ServerAddress.fromString(server.serverIP);
	        mc.setServerData(server);
	        (new Thread("Server Connector #" + new AtomicInteger(0).incrementAndGet()) {
	            public void run() {
	                InetAddress inetaddress = null;

	                try {
	                	System.out.println("here");
	                    inetaddress = InetAddress.getByName(serveraddress.getIP());
	                    NetworkManager networkManager = NetworkManager.func_181124_a(inetaddress, serveraddress.getPort(), mc.gameSettings.func_181148_f());
	                    networkManager.setNetHandler(new NetHandlerLoginClient(networkManager, mc, null));
	                    networkManager.sendPacket(new C00Handshake(47, serveraddress.getIP(), serveraddress.getPort(), EnumConnectionState.LOGIN, true));
	                    networkManager.sendPacket(new C00PacketLoginStart(mc.getSession().getProfile()));
	                }
	                catch (UnknownHostException unknownhostexception) {

	                    EnhancedMC.EMCLogger.error("Couldn\'t connect to server", unknownhostexception);
	                    conIn.error(I18n.format("connect.failed", new Object[0]));
	                    conIn.error(I18n.format("disconnect.genericReason", new Object[] {"Unknown host"}));
	                }
	                catch (Exception exception) {

	                	EnhancedMC.EMCLogger.error("Couldn\'t connect to server", exception);
	                    String s = exception.toString();

	                    if (inetaddress != null) {
	                        String s1 = inetaddress.toString() + ":" + serveraddress.getPort();
	                        s = s.replaceAll(s1, "");
	                    }

	                    conIn.error(I18n.format("connect.failed", new Object[0]));
	                    conIn.error(I18n.format("disconnect.genericReason", new Object[] {s}));
	                }
	            }
	        }).start();
		}
	}
}
