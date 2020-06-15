package com.Whodundid.core.coreEvents;

import com.Whodundid.core.coreEvents.emcEvents.ChatLineCreatedEvent;
import com.Whodundid.core.coreEvents.emcEvents.EMCAppCalloutEvent;
import com.Whodundid.core.coreEvents.emcEvents.RendererRCMOpenEvent;
import com.Whodundid.core.coreEvents.emcEvents.TabCompletionEvent;
import com.Whodundid.core.coreEvents.emcEvents.WindowClosedEvent;
import com.Whodundid.core.coreEvents.emcEvents.WindowOpenedEvent;
import com.Whodundid.core.coreEvents.eventUtil.EMCEventDistributor;
import com.Whodundid.core.coreEvents.eventUtil.EMCEvents;
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
	@EventHandler public void postInit(FMLPostInitializationEvent e) { distributeInit(EMCEvents.postInit, e); }
	
	//ticks
	@SubscribeEvent public void eventTick(TickEvent e) { distributeEvent(EMCEvents.tick, e); }
	@SubscribeEvent public void eventClientTick(ClientTickEvent e) { distributeEvent(EMCEvents.cTick, e); }
	@SubscribeEvent public void eventRenderTick(TickEvent.RenderTickEvent e) { distributeEvent(EMCEvents.rTick, e); }
	@SubscribeEvent public void eventLivingTick(LivingUpdateEvent e) { distributeEvent(EMCEvents.lTick, e); }
	
	//overlay renders
	@SubscribeEvent(priority = EventPriority.HIGH) public void eventOverlayRenderTick(RenderGameOverlayEvent e) { distributeEvent(EMCEvents.overlay, e); }
	@SubscribeEvent(priority = EventPriority.HIGH) public void eventTextOverlayRenderTick(RenderGameOverlayEvent.Text e) { distributeEvent(EMCEvents.overlayText, e); }
	@SubscribeEvent(priority = EventPriority.HIGH) public void eventPostOverlayRenderTick(RenderGameOverlayEvent.Post e) { distributeEvent(EMCEvents.overlayPost, e); }
	@SubscribeEvent(priority = EventPriority.HIGH) public void eventPreOverlayRenderTick(RenderGameOverlayEvent.Pre e) { distributeEvent(EMCEvents.overlayPre, e); }
	
	//visual renders
	@SubscribeEvent public void eventRenderFogTick(EntityViewRenderEvent.FogDensity e) { distributeEvent(EMCEvents.renderFogDensity, e); }
	@SubscribeEvent public void eventRenderFog(EntityViewRenderEvent.RenderFogEvent e) { distributeEvent(EMCEvents.renderFog, e); }
	@SubscribeEvent public void eventBlockOverlayTick(RenderBlockOverlayEvent e) { distributeEvent(EMCEvents.renderBlock, e); }
	@SubscribeEvent public void eventRenderPlayerPre(RenderPlayerEvent.Pre e) { distributeEvent(EMCEvents.renderPlayerPre, e); }
	@SubscribeEvent public void eventRenderPlayerPost(RenderPlayerEvent.Post e) { distributeEvent(EMCEvents.renderPlayerPost, e); }
	@SubscribeEvent public void eventLastWorldRender(RenderWorldLastEvent e) { distributeEvent(EMCEvents.renderLastWorld, e); }
	
	//input
	@SubscribeEvent public void eventMouse(MouseEvent e) { distributeEvent(EMCEvents.mouse, e); }
	@SideOnly(Side.CLIENT) @SubscribeEvent(priority = EventPriority.NORMAL, receiveCanceled = true) public void eventKey(KeyInputEvent e) { distributeEvent(EMCEvents.keyboard, e); }
	@SubscribeEvent public void eventChat(ClientChatReceivedEvent e) { distributeEvent(EMCEvents.chat, e); }
	@SubscribeEvent public void eventCommand(CommandEvent e) { distributeEvent(EMCEvents.command, e); }
	
	//guis
	@SubscribeEvent public void eventInitGui(GuiScreenEvent.InitGuiEvent e) { distributeEvent(EMCEvents.initGui, e); }
	
	//world
	@SideOnly(Side.CLIENT) @SubscribeEvent public void eventWorldLoadClient(WorldEvent.Load e) { distributeEvent(EMCEvents.worldLoadClient, e); }
	@SideOnly(Side.SERVER) @SubscribeEvent public void eventWorldLoadServer(WorldEvent.Load e) { distributeEvent(EMCEvents.worldLoadServer, e); }
	@SubscribeEvent public void eventWorldUnload(WorldEvent.Unload e) { distributeEvent(EMCEvents.worldUnload, e); }
	@SubscribeEvent public void eventServerJoin(EntityJoinWorldEvent e) { distributeEvent(EMCEvents.serverJoin, e); }
	
	//emc specific
	@SubscribeEvent public void eventRendererRCMOpen(RendererRCMOpenEvent e) { distributeEvent(EMCEvents.rendererRCM, e); }
	@SubscribeEvent public void eventTabCompletion(TabCompletionEvent e) { distributeEvent(EMCEvents.tabComplete, e); }
	@SubscribeEvent public void eventChatLineCreated(ChatLineCreatedEvent e) { distributeEvent(EMCEvents.chatLine, e); }
	@SubscribeEvent public void eventAppCallout(EMCAppCalloutEvent e) { distributeEvent(EMCEvents.appCallout, e); }
	@SubscribeEvent public void eventWindowOpened(WindowOpenedEvent e) { distributeEvent(EMCEvents.windowOpened, e); }
	@SubscribeEvent public void eventWindowOpened(WindowClosedEvent e) { distributeEvent(EMCEvents.windowClosed, e); }
	
	//-----------
	//distributor
	//-----------
	
	public void distributeInit(EMCEvents type, FMLEvent e) { distributor.distributeInit(type, e); }
	public void distributeEvent(EMCEvents type, Event e) { distributor.distributeEvent(type, e); }
}
