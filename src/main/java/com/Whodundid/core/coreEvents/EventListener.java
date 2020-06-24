package com.Whodundid.core.coreEvents;

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
import com.Whodundid.core.coreEvents.eventUtil.EMCEventDistributor;
import com.Whodundid.core.coreEvents.eventUtil.EMCEventType;
import net.minecraftforge.client.event.RenderPlayerEvent;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraft.client.Minecraft;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.client.event.EntityViewRenderEvent;
import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.client.event.MouseEvent;
import net.minecraftforge.client.event.RenderBlockOverlayEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.Event;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.ClientTickEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent.KeyInputEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.event.CommandEvent;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;
import net.minecraftforge.event.world.WorldEvent;

//Author: Hunter Bragg

/** A forge event wrapper for EnhancedMC and its SubMods */
public class EventListener {
	
	Minecraft mc = Minecraft.getMinecraft();
	EMCEventDistributor distributor;
	private static EventListener instance = null;
	
	private EventListener() {
		distributor = new EMCEventDistributor();
	}
	
	public static EventListener getInstance() { return instance = (instance != null) ? instance : new EventListener(); }
	
	//-----------
	//event hooks
	//-----------
	
	//init
	@EventHandler public void postInit(FMLPostInitializationEvent e) { distributeInit(EMCEventType.postInit, e); }
	
	//ticks
	@SubscribeEvent public void eventTick(TickEvent e) { distributeEvent(EMCEventType.tick, e); }
	@SubscribeEvent public void eventClientTick(ClientTickEvent e) { distributeEvent(EMCEventType.cTick, e); }
	@SubscribeEvent public void eventRenderTick(TickEvent.RenderTickEvent e) { distributeEvent(EMCEventType.rTick, e); }
	@SubscribeEvent public void eventLivingTick(LivingUpdateEvent e) { distributeEvent(EMCEventType.lTick, e); }
	
	//overlay renders
	@SubscribeEvent(priority = EventPriority.HIGH) public void eventOverlayRenderTick(RenderGameOverlayEvent e) { distributeEvent(EMCEventType.overlay, e); }
	@SubscribeEvent(priority = EventPriority.HIGH) public void eventTextOverlayRenderTick(RenderGameOverlayEvent.Text e) { distributeEvent(EMCEventType.overlayText, e); }
	@SubscribeEvent(priority = EventPriority.HIGH) public void eventPostOverlayRenderTick(RenderGameOverlayEvent.Post e) { distributeEvent(EMCEventType.overlayPost, e); }
	@SubscribeEvent(priority = EventPriority.HIGH) public void eventPreOverlayRenderTick(RenderGameOverlayEvent.Pre e) { distributeEvent(EMCEventType.overlayPre, e); }
	
	//visual renders
	@SubscribeEvent public void eventRenderFogTick(EntityViewRenderEvent.FogDensity e) { distributeEvent(EMCEventType.renderFogDensity, e); }
	@SubscribeEvent public void eventRenderFog(EntityViewRenderEvent.RenderFogEvent e) { distributeEvent(EMCEventType.renderFog, e); }
	@SubscribeEvent public void eventBlockOverlayTick(RenderBlockOverlayEvent e) { distributeEvent(EMCEventType.renderBlock, e); }
	@SubscribeEvent public void eventRenderPlayerPre(RenderPlayerEvent.Pre e) { distributeEvent(EMCEventType.renderPlayerPre, e); }
	@SubscribeEvent public void eventRenderPlayerPost(RenderPlayerEvent.Post e) { distributeEvent(EMCEventType.renderPlayerPost, e); }
	@SubscribeEvent public void eventLastWorldRender(RenderWorldLastEvent e) { distributeEvent(EMCEventType.renderLastWorld, e); }
	
	//input
	@SubscribeEvent public void eventMouse(MouseEvent e) { distributeEvent(EMCEventType.mouse, e); }
	@SideOnly(Side.CLIENT) @SubscribeEvent(priority = EventPriority.NORMAL, receiveCanceled = true) public void eventKey(KeyInputEvent e) { distributeEvent(EMCEventType.keyboard, e); }
	@SubscribeEvent public void eventChat(ClientChatReceivedEvent e) { distributeEvent(EMCEventType.chat, e); }
	@SubscribeEvent public void eventCommand(CommandEvent e) { distributeEvent(EMCEventType.command, e); }
	
	//guis
	@SubscribeEvent public void eventInitGui(GuiScreenEvent.InitGuiEvent e) { distributeEvent(EMCEventType.initGui, e); }
	
	//world
	@SideOnly(Side.CLIENT) @SubscribeEvent public void eventWorldLoadClient(WorldEvent.Load e) { distributeEvent(EMCEventType.worldLoadClient, e); }
	@SideOnly(Side.SERVER) @SubscribeEvent public void eventWorldLoadServer(WorldEvent.Load e) { distributeEvent(EMCEventType.worldLoadServer, e); }
	@SubscribeEvent public void eventWorldUnload(WorldEvent.Unload e) { distributeEvent(EMCEventType.worldUnload, e); }
	@SubscribeEvent public void eventServerJoin(EntityJoinWorldEvent e) { distributeEvent(EMCEventType.serverJoin, e); }
	
	//emc specific
	@SubscribeEvent public void eventRendererRCMOpen(RendererRCMOpenEvent e) { distributeEvent(EMCEventType.rendererRCM, e); }
	@SubscribeEvent public void eventTabCompletion(TabCompletionEvent e) { distributeEvent(EMCEventType.tabComplete, e); }
	@SubscribeEvent public void eventChatLineCreated(ChatLineCreatedEvent e) { distributeEvent(EMCEventType.chatLine, e); }
	@SubscribeEvent public void eventAppCallout(EMCAppCalloutEvent e) { distributeEvent(EMCEventType.appCallout, e); }
	@SubscribeEvent public void eventWindowOpened(WindowOpenedEvent e) { distributeEvent(EMCEventType.windowOpened, e); }
	@SubscribeEvent public void eventWindowOpened(WindowClosedEvent e) { distributeEvent(EMCEventType.windowClosed, e); }
	@SubscribeEvent public void eventGameWindowResized(GameWindowResizedEvent e) { distributeEvent(EMCEventType.gameWindowResized, e); }
	@SubscribeEvent public void eventReloadingApp(ReloadingAppEvent.Pre e) { distributeEvent(EMCEventType.reloadingApp, e); }
	@SubscribeEvent public void eventReloadingApp(ReloadingAppEvent.Post e) { distributeEvent(EMCEventType.reloadingApp, e); }
	@SubscribeEvent public void eventAppsReloaded(AppsReloadedEvent.Pre e) { distributeEvent(EMCEventType.appsReloaded, e); }
	@SubscribeEvent public void eventAppsReloaded(AppsReloadedEvent.Post e) { distributeEvent(EMCEventType.appsReloaded, e); }
	@SubscribeEvent public void eventEMCPostInit(EMCPostInitEvent e) { distributeEvent(EMCEventType.emcPostInit, e); }
	@SubscribeEvent public void eventGenericEMC(EMCEvent e) { distributeEvent(EMCEventType.genericEMC, e); }
	
	//-----------
	//distributor
	//-----------
	
	public void distributeInit(EMCEventType type, FMLEvent e) { distributor.distributeInit(type, e); }
	public void distributeEvent(EMCEventType type, Event e) { distributor.distributeEvent(type, e); }
}
