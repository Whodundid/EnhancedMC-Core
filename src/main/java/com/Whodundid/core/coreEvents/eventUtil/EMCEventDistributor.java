package com.Whodundid.core.coreEvents.eventUtil;

import com.Whodundid.core.EnhancedMC;
import com.Whodundid.core.app.EMCApp;
import com.Whodundid.core.app.RegisteredApps;
import com.Whodundid.core.coreEvents.emcEvents.ChatLineCreatedEvent;
import com.Whodundid.core.coreEvents.emcEvents.EMCAppCalloutEvent;
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

	public static void distributeInit(EMCEvents type, FMLEvent e) {
		if (EnhancedMC.isInitialized()) {
			EArrayList<EMCApp> mods = RegisteredApps.getRegisteredAppList();
			
			switch (type) {
			case postInit: mods.forEach(o -> o.onPostInit((FMLPostInitializationEvent) e)); break;
			default: throw new IllegalStateException("INVALID EMC INIT EVENT: " + type);
			}
		}
	}
	
	public static void distributeEvent(EMCEvents type, Event e) {
		if (EnhancedMC.isInitialized()) {
			EArrayList<EMCApp> mods = RegisteredApps.getRegisteredAppList().stream().filter(a -> a.isEnabled()).collect(EArrayList.toEArrayList());
			
			switch (type) {
			//ticks
			case tick: for (EMCApp m : mods) { m.tickEvent((TickEvent) e); } break;
			case cTick: for (EMCApp m : mods) { m.clientTickEvent((ClientTickEvent) e); } break;
			case rTick: for (EMCApp m : mods) { m.renderTickEvent((RenderTickEvent) e); } break;
			case lTick: for (EMCApp m : mods) { m.livingTickEvent((LivingUpdateEvent) e); } break;
			
			//overlay renders
			case overlay: for (EMCApp m : mods) { m.overlayEvent((RenderGameOverlayEvent) e); } break;
			case overlayText: for (EMCApp m : mods) { m.overlayTextEvent((Text) e); } break;
			case overlayPre: for (EMCApp m : mods) { m.overlayPreEvent((Pre) e); } break;
			case overlayPost:
				if (((Post) e).type == ElementType.ALL) {
					GL11.glPushMatrix();
					GlStateManager.disableAlpha();
					GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
					for (EMCApp m : mods) { m.overlayPostEvent((Post) e); }
					GlStateManager.enableAlpha();
					GL11.glPopMatrix();
					EnhancedMC.getRenderer().onRenderTick((RenderGameOverlayEvent) e);
				}
				break;
			
			//visual renders
			case renderFogDensity: for (EMCApp m : mods) { m.renderFogDensityEvent((FogDensity) e); } break;
			case renderFog: for (EMCApp m : mods) { m.renderFogEvent((RenderFogEvent) e); } break;
			case renderBlock: for (EMCApp m : mods) { m.renderBlockOverlayEvent((RenderBlockOverlayEvent) e); } break;
			case renderPlayerPre: for (EMCApp m : mods) { m.renderPlayerPreEvent((RenderPlayerEvent.Pre) e); } break;
			case renderPlayerPost: for (EMCApp m : mods) { m.renderPlayerPostEvent((RenderPlayerEvent.Post) e); } break;
			case renderLastWorld: for (EMCApp m : mods) { m.renderLastWorldEvent((RenderWorldLastEvent) e); } break;
			
			//input
			case mouse: for (EMCApp m : mods) { m.mouseEvent((MouseEvent) e); } break;
			case keyboard: for (EMCApp m : mods) { m.keyEvent((KeyInputEvent) e); } break;
			case chat: for (EMCApp m : mods) { m.chatEvent((ClientChatReceivedEvent) e); } break;
			case command: for (EMCApp m : mods) { m.commandEvent((CommandEvent) e); } break;
			
			//guis
			case initGui: for (EMCApp m : mods) { m.initGuiEvent((InitGuiEvent) e); } break;
			
			//world
			case worldLoadClient: for (EMCApp m : mods) { m.worldLoadClientEvent((Load) e); } break;
			case worldLoadServer: for (EMCApp m : mods) { m.worldLoadServerEvent((Load) e); } break;
			case worldUnload: for (EMCApp m : mods) { m.worldUnloadEvent((Unload) e); } break;
			case serverJoin: for (EMCApp m : mods) { m.serverJoinEvent((EntityJoinWorldEvent) e); } break;
			
			//emc specific
			case rendererRCM: for (EMCApp m : mods) { m.rendererRCMOpenEvent((RendererRCMOpenEvent) e); } break;
			case tabComplete: for (EMCApp m : mods) { m.tabCompletionEvent((TabCompletionEvent) e); } break;
			case chatLine: for (EMCApp m : mods) { m.chatLineCreatedEvent((ChatLineCreatedEvent) e); } break;
			case appCallout: for (EMCApp m : mods) { m.subModCalloutEvent((EMCAppCalloutEvent) e); } break;
			case windowOpened: for (EMCApp m : mods) { m.windowOpenedEvent((WindowOpenedEvent) e); } break;
			case windowClosed: for (EMCApp m : mods) { m.windowClosedEvent((WindowClosedEvent) e); } break;
			default: throw new IllegalStateException("INVALID EMC EVENT: " + e);
			}
		}
	}
	
}
