package com.Whodundid.core.util.serverUtil;

import com.Whodundid.core.EnhancedMC;
import com.Whodundid.core.terminal.gui.ETerminal;
import com.Whodundid.core.util.EUtil;
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

public class ServerConnector {

	private static String ipToConnectTo = null;
	private static ETerminal term = null;

	public static void update() {
		if (ipToConnectTo != null) {
			connectServer(ipToConnectTo);
			ipToConnectTo = null;
		}
	}

	public static void setIpToConnectTo(String ipIn) { ipToConnectTo = ipIn; }
	public static void setIpToConnectTo(ETerminal termIn, String ipIn) {
		term = termIn;
		ipToConnectTo = ipIn;
	}

	private static void connectServer(String ipIn) {
		EUtil.closeWorld(GuiMainMenu.class);

		Minecraft mc = Minecraft.getMinecraft();
		FMLClientHandler.instance().setupServerList();
		ServerData server = new ServerData("serverName", ipIn.toLowerCase(), false);
		ServerAddress serveraddress = ServerAddress.fromString(server.serverIP);
		mc.setServerData(server);

		(new Thread("Server Connector #" + new AtomicInteger(0).incrementAndGet()) {
			public void run() {
				InetAddress inetaddress = null;

				try {
					inetaddress = InetAddress.getByName(serveraddress.getIP());
					NetworkManager networkManager = NetworkManager.func_181124_a(inetaddress, serveraddress.getPort(), mc.gameSettings.func_181148_f());
					networkManager.setNetHandler(new NetHandlerLoginClient(networkManager, mc, null));
					networkManager.sendPacket(new C00Handshake(47, serveraddress.getIP(), serveraddress.getPort(), EnumConnectionState.LOGIN, true));
					networkManager.sendPacket(new C00PacketLoginStart(mc.getSession().getProfile()));
					System.out.println("here end");
				} catch (UnknownHostException unknownhostexception) {

					EnhancedMC.EMCLogger.error("Couldn\'t connect to server", unknownhostexception);
					if (term != null) {
						term.error(I18n.format("connect.failed", new Object[0]));
						term.error(I18n.format("disconnect.genericReason", new Object[] { "Unknown host" }));
					}
				} catch (Exception exception) {

					EnhancedMC.EMCLogger.error("Couldn\'t connect to server", exception);
					String s = exception.toString();

					if (inetaddress != null) {
						String s1 = inetaddress.toString() + ":" + serveraddress.getPort();
						s = s.replaceAll(s1, "");
					}

					if (term != null) {
						term.error(I18n.format("connect.failed", new Object[0]));
						term.badError(I18n.format("disconnect.genericReason", new Object[] { s }));

						term.getTextArea().getVScrollBar().setScrollBarPos(term.getTextArea().getVScrollBar().getHighVal());
						EnhancedMC.getTerminalHandler().drawSpace = true;
						term.setInputEnabled(true);
					}
				}

				term = null;
			}
		}).start();
	}
}
