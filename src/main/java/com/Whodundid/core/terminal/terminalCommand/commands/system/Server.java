package com.Whodundid.core.terminal.terminalCommand.commands.system;

import com.Whodundid.core.EnhancedMC;
import com.Whodundid.core.terminal.gui.ETerminal;
import com.Whodundid.core.terminal.terminalCommand.CommandType;
import com.Whodundid.core.terminal.terminalCommand.TerminalCommand;
import com.Whodundid.core.util.renderUtil.EColors;
import com.Whodundid.core.util.storageUtil.EArrayList;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.concurrent.atomic.AtomicInteger;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiMainMenu;
import net.minecraft.client.gui.GuiMultiplayer;
import net.minecraft.client.multiplayer.ServerAddress;
import net.minecraft.client.multiplayer.ServerData;
import net.minecraft.client.network.NetHandlerLoginClient;
import net.minecraft.client.resources.I18n;
import net.minecraft.network.EnumConnectionState;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.handshake.client.C00Handshake;
import net.minecraft.network.login.client.C00PacketLoginStart;
import net.minecraft.realms.RealmsBridge;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.fml.client.FMLClientHandler;

//Author: Hunter Bragg

public class Server extends TerminalCommand {
	
	public Server() {
		super(CommandType.NORMAL);
		numArgs = 1;
	}

	@Override public String getName() { return "server"; }
	@Override public boolean showInHelp() { return EnhancedMC.isOpMode(); }
	@Override public EArrayList<String> getAliases() { return null; }
	@Override public String getHelpInfo(boolean runVisually) { return "Used to interface with Minecraft servers." + (runVisually ? " connect, disconnect" : ""); }
	@Override public String getUsage() { return "ex: server connect localhost"; }
	
	@Override
	public void handleTabComplete(ETerminal termIn, EArrayList<String> args) {
		super.basicTabComplete(termIn, args, new EArrayList("disconnect", "connect", "ping"));
	}
	
	@Override
	public void runCommand(ETerminal termIn, EArrayList<String> args, boolean runVisually) {
		if (args.isEmpty()) {
			if (runVisually) { termIn.writeln("connect, disconnect, ping", EColors.green); }
			else {
				ServerData data = Minecraft.getMinecraft().getCurrentServerData();
				if (data != null) { termIn.writeln("Current Server: " + EnumChatFormatting.GREEN + data.serverIP, EColors.cyan); }
				else { termIn.writeln("Singleplayer world", EColors.cyan); }
			}
		}
		else if (args.size() > 2) { termIn.error("Too many arguments!"); }
		else if (args.size() >= 1) {
			switch (args.get(0).toLowerCase()) {
			case "dis":
			case "discon":
			case "disconnect":
			case "log":
			case "logout":
			case "logoff":
			case "quit": disconnect(termIn, args, runVisually); break;
			case "p":
			case "ping": pingServer(termIn, args, runVisually); break;
			case "con":
			case "connect":
			case "join":
			case "logon":
			case "login": connect(termIn, args, runVisually); break;
			default: termIn.error("Unrecognized argument!");
			}
		}
	}
	
	private void connect(ETerminal termIn, EArrayList<String> args, boolean runVisually) {
		if (args.isEmpty()) { termIn.error("Specify a server ip address to connect to!"); }
		else if (args.size() > 2) { termIn.error("Too many arguments! Only specify one ip!"); }
		else {
			Minecraft mc = Minecraft.getMinecraft();
			mc.theWorld.sendQuittingDisconnectingPacket();
			mc.loadWorld(null);
			mc.displayGuiScreen(new GuiMainMenu());
			
			FMLClientHandler.instance().setupServerList();
			ServerData server = new ServerData("serverName", args.get(1).toLowerCase(), false);
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
	                    termIn.error(I18n.format("connect.failed", new Object[0]));
	                    termIn.error(I18n.format("disconnect.genericReason", new Object[] {"Unknown host"}));
	                }
	                catch (Exception exception) {

	                	EnhancedMC.EMCLogger.error("Couldn\'t connect to server", exception);
	                    String s = exception.toString();

	                    if (inetaddress != null) {
	                        String s1 = inetaddress.toString() + ":" + serveraddress.getPort();
	                        s = s.replaceAll(s1, "");
	                    }

	                    termIn.error(I18n.format("connect.failed", new Object[0]));
	                    termIn.error(I18n.format("disconnect.genericReason", new Object[] {s}));
	                }
	            }
	        }).start();
		}
	}
	
	private void disconnect(ETerminal termIn, EArrayList<String> args, boolean runVisually) {
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
	
	private void pingServer(ETerminal termIn, EArrayList<String> args, boolean runVisually) {
		if (args.size() > 1 && args.get(1) != null && !args.get(1).isEmpty()) {
			termIn.setInputEnabled(false);
			Thread pinger = new Thread() {
				@Override
				public void run() {
					try {
						InetAddress server = InetAddress.getByName(args.get(1).toLowerCase());
						if (server.isReachable(5000)) { Server.this.onPingResult(termIn, server.getHostAddress() + " is online.", true); }
						else { Server.this.onPingResult(termIn, server.getHostAddress() + " connection timed out.", false); }
					}
					catch (Exception e) {
						Server.this.onPingResult(termIn, "Unknown Host: " + args.get(1), false);
						e.printStackTrace();
					}
				}
			};
			pinger.start();
		}
		else {
			termIn.error("Specify an ip address to ping!");
		}
		EnhancedMC.getTerminalHandler().drawSpace = false;
	}
	
	private void onPingResult(ETerminal termIn, String ip, boolean result) {
		if (result) { termIn.writeln(ip, EColors.green); }
		else { termIn.writeln(ip, EColors.lred); }
		termIn.writeln();
		termIn.getTextArea().getVScrollBar().setScrollBarPos(termIn.getTextArea().getVScrollBar().getHighVal());
		EnhancedMC.getTerminalHandler().drawSpace = true;
		termIn.setInputEnabled(true);
	}
}
