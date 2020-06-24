package com.Whodundid.core.coreEvents.eventUtil;

import com.Whodundid.core.EnhancedMC;
import com.Whodundid.core.app.EMCApp;
import com.Whodundid.core.app.RegisteredApps;
import com.Whodundid.core.coreEvents.EMCEvent;
import com.Whodundid.core.coreEvents.emcEvents.AppsReloadedEvent;
import com.Whodundid.core.coreEvents.emcEvents.ChatLineCreatedEvent;
import com.Whodundid.core.coreEvents.emcEvents.EMCAppCalloutEvent;
import com.Whodundid.core.coreEvents.emcEvents.EMCPostInitEvent;
import com.Whodundid.core.coreEvents.emcEvents.GameWindowResizedEvent;
import com.Whodundid.core.coreEvents.emcEvents.ReloadingAppEvent;
import com.Whodundid.core.coreEvents.emcEvents.RendererRCMOpenEvent;
import com.Whodundid.core.coreEvents.emcEvents.TabCompletionEvent;
import com.Whodundid.core.coreEvents.emcEvents.WindowClosedEvent;
import com.Whodundid.core.coreEvents.emcEvents.WindowOpenedEvent;
import com.Whodundid.core.util.storageUtil.EArrayList;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.client.event.MouseEvent;
import net.minecraftforge.client.event.RenderBlockOverlayEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderPlayerEvent;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.client.event.EntityViewRenderEvent.FogDensity;
import net.minecraftforge.client.event.EntityViewRenderEvent.RenderFogEvent;
import net.minecraftforge.client.event.GuiScreenEvent.InitGuiEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent.ElementType;
import net.minecraftforge.client.event.RenderGameOverlayEvent.Post;
import net.minecraftforge.client.event.RenderGameOverlayEvent.Pre;
import net.minecraftforge.client.event.RenderGameOverlayEvent.Text;
import net.minecraftforge.event.CommandEvent;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;
import net.minecraftforge.event.world.WorldEvent.Load;
import net.minecraftforge.event.world.WorldEvent.Unload;
import net.minecraftforge.fml.common.event.FMLEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.Event;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent.KeyInputEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.ClientTickEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.RenderTickEvent;
import org.lwjgl.opengl.GL11;

//Author: Hunter Bragg

public class EMCEventDistributor {

	public static void distributeInit(EMCEventType type, FMLEvent e) {
		if (EnhancedMC.isInitialized()) {
			EArrayList<EMCApp> mods = RegisteredApps.getRegisteredAppsList();
			
			switch (type) {
			case postInit: mods.forEach(o -> o.onPostInit((FMLPostInitializationEvent) e)); break;
			default: throw new IllegalStateException("INVALID EMC INIT EVENT: " + type);
			}
		}
	}
	
	public static void distributeEvent(EMCEventType type, Event e) {
		if (EnhancedMC.isInitialized()) {
			EArrayList<EMCApp> apps = RegisteredApps.getRegisteredAppsList().stream().filter(a -> a.isEnabled()).collect(EArrayList.toEArrayList());
			
			switch (type) {
			//ticks
			case tick: for (EMCApp a : apps) { a.tickEvent((TickEvent) e); } break;
			case cTick: for (EMCApp a : apps) { a.clientTickEvent((ClientTickEvent) e); } break;
			case rTick: for (EMCApp a : apps) { a.renderTickEvent((RenderTickEvent) e); } break;
			case lTick: for (EMCApp a : apps) { a.livingTickEvent((LivingUpdateEvent) e); } break;
			
			//overlay renders
			case overlay: for (EMCApp a : apps) { a.overlayEvent((RenderGameOverlayEvent) e); } break;
			case overlayText: for (EMCApp a : apps) { a.overlayTextEvent((Text) e); } break;
			case overlayPre: for (EMCApp a : apps) { a.overlayPreEvent((Pre) e); } break;
			case overlayPost:
				if (((Post) e).type == ElementType.ALL) {
					GL11.glPushMatrix();
					GlStateManager.disableAlpha();
					GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
					for (EMCApp a : apps) { a.overlayPostEvent((Post) e); }
					GlStateManager.enableAlpha();
					GL11.glPopMatrix();
					EnhancedMC.getRenderer().onRenderTick((RenderGameOverlayEvent) e);
				}
				break;
			
			//visual renders
			case renderFogDensity: for (EMCApp a : apps) { a.renderFogDensityEvent((FogDensity) e); } break;
			case renderFog: for (EMCApp a : apps) { a.renderFogEvent((RenderFogEvent) e); } break;
			case renderBlock: for (EMCApp a : apps) { a.renderBlockOverlayEvent((RenderBlockOverlayEvent) e); } break;
			case renderPlayerPre: for (EMCApp a : apps) { a.renderPlayerPreEvent((RenderPlayerEvent.Pre) e); } break;
			case renderPlayerPost: for (EMCApp a : apps) { a.renderPlayerPostEvent((RenderPlayerEvent.Post) e); } break;
			case renderLastWorld: for (EMCApp a : apps) { a.renderLastWorldEvent((RenderWorldLastEvent) e); } break;
			
			//input
			case mouse: for (EMCApp a : apps) { a.mouseEvent((MouseEvent) e); } break;
			case keyboard: for (EMCApp a : apps) { a.keyEvent((KeyInputEvent) e); } break;
			case chat: for (EMCApp a : apps) { a.chatEvent((ClientChatReceivedEvent) e); } break;
			case command: for (EMCApp a : apps) { a.commandEvent((CommandEvent) e); } break;
			
			//guis
			case initGui: for (EMCApp a : apps) { a.initGuiEvent((InitGuiEvent) e); } break;
			
			//world
			case worldLoadClient: for (EMCApp a : apps) { a.worldLoadClientEvent((Load) e); } break;
			case worldLoadServer: for (EMCApp a : apps) { a.worldLoadServerEvent((Load) e); } break;
			case worldUnload: for (EMCApp a : apps) { a.worldUnloadEvent((Unload) e); } break;
			case serverJoin: for (EMCApp a : apps) { a.serverJoinEvent((EntityJoinWorldEvent) e); } break;
			
			//emc specific
			case rendererRCM: for (EMCApp a : apps) { a.rendererRCMOpenEvent((RendererRCMOpenEvent) e); } break;
			case tabComplete: for (EMCApp a : apps) { a.tabCompletionEvent((TabCompletionEvent) e); } break;
			case chatLine: for (EMCApp a : apps) { a.chatLineCreatedEvent((ChatLineCreatedEvent) e); } break;
			case appCallout: for (EMCApp a : apps) { a.EMCAppCalloutEvent((EMCAppCalloutEvent) e); } break;
			case windowOpened: for (EMCApp a : apps) { a.windowOpenedEvent((WindowOpenedEvent) e); } break;
			case windowClosed: for (EMCApp a : apps) { a.windowClosedEvent((WindowClosedEvent) e); } break;
			case gameWindowResized: for (EMCApp a : apps) { a.gameWindowResized((GameWindowResizedEvent) e); } break;
			case reloadingApp: for (EMCApp a : apps) { a.reloadingAppEvent((ReloadingAppEvent) e);} break;
			case appsReloaded: for (EMCApp a : apps) { a.appsReloadedEvent((AppsReloadedEvent) e); } break;
			case emcPostInit: for (EMCApp a : apps) { a.onEMCPostInitEvent((EMCPostInitEvent) e); } break;
			case genericEMC: for(EMCApp a : apps) { a.genericEMCEvent((EMCEvent) e); } break;
			
			default: throw new IllegalStateException("INVALID EMC EVENT: " + e);
			}
		}
	}
	
}
