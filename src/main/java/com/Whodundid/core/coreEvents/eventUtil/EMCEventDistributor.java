package com.Whodundid.core.coreEvents.eventUtil;

import com.Whodundid.core.EnhancedMC;
import com.Whodundid.core.coreEvents.emcEvents.ChatLineCreatedEvent;
import com.Whodundid.core.coreEvents.emcEvents.SubModCalloutEvent;
import com.Whodundid.core.coreEvents.emcEvents.TabCompletionEvent;
import com.Whodundid.core.subMod.RegisteredSubMods;
import com.Whodundid.core.subMod.SubMod;
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
import net.minecraftforge.fml.common.eventhandler.Event;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent.KeyInputEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.ClientTickEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.RenderTickEvent;
import org.lwjgl.opengl.GL11;

//Author: Hunter Bragg

public class EMCEventDistributor {

	public static void distributeEvent(EMCEvents type, Event e) {
		if (EnhancedMC.isInitialized()) {
			EArrayList<SubMod> mods = RegisteredSubMods.getRegisteredModsList();
			
			switch (type) {
			//ticks
			case tick: for (SubMod m : mods) { m.tickEvent((TickEvent) e); } break;
			case cTick: for (SubMod m : mods) { m.clientTickEvent((ClientTickEvent) e); } break;
			case rTick: for (SubMod m : mods) { m.renderTickEvent((RenderTickEvent) e); } break;
			case lTick: for (SubMod m : mods) { m.livingTickEvent((LivingUpdateEvent) e); } break;
			
			//overlay renders
			case overlay: for (SubMod m : mods) { m.overlayEvent((RenderGameOverlayEvent) e); } break;
			case overlayText: for (SubMod m : mods) { m.overlayTextEvent((Text) e); } break;
			case overlayPre: for (SubMod m : mods) { m.overlayPreEvent((Pre) e); } break;
			case overlayPost:
				if (((Post) e).type == ElementType.ALL) {
					GL11.glPushMatrix();
					GlStateManager.disableAlpha();
					GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
					for (SubMod m : mods) { m.OverlayPostEvent((Post) e); }
					GlStateManager.enableAlpha();
					GL11.glPopMatrix();
					EnhancedMC.getRenderer().onRenderTick((RenderGameOverlayEvent) e);
				}
				break;
			
			//visual renders
			case renderFogDensity: for (SubMod m : mods) { m.renderFogDensityEvent((FogDensity) e); } break;
			case renderFog: for (SubMod m : mods) { m.renderFogEvent((RenderFogEvent) e); } break;
			case renderBlock: for (SubMod m : mods) { m.renderBlockOverlayEvent((RenderBlockOverlayEvent) e); } break;
			case renderPlayerPre: for (SubMod m : mods) { m.renderPlayerPreEvent((RenderPlayerEvent.Pre) e); } break;
			case renderPlayerPost: for (SubMod m : mods) { m.renderPlayerPostEvent((RenderPlayerEvent.Post) e); } break;
			case renderLastWorld: for (SubMod m : mods) { m.renderLastWorldEvent((RenderWorldLastEvent) e); } break;
			
			//input
			case mouse: for (SubMod m : mods) { m.mouseEvent((MouseEvent) e); } break;
			case keyboard: for (SubMod m : mods) { m.keyEvent((KeyInputEvent) e); } break;
			case chat: for (SubMod m : mods) { m.chatEvent((ClientChatReceivedEvent) e); } break;
			case command: for (SubMod m : mods) { m.commandEvent((CommandEvent) e); } break;
			
			//guis
			case initGui: for (SubMod m : mods) { m.initGuiEvent((InitGuiEvent) e); } break;
			
			//world
			case worldLoadClient: for (SubMod m : mods) { m.worldLoadClientEvent((Load) e); } break;
			case worldLoadServer: for (SubMod m : mods) { m.worldLoadServerEvent((Load) e); } break;
			case worldUnload: for (SubMod m : mods) { m.worldUnloadEvent((Unload) e); } break;
			case serverJoin: for (SubMod m : mods) { m.serverJoinEvent((EntityJoinWorldEvent) e); } break;
			
			//emc specific
			case tabComplete: for (SubMod m : mods) { m.tabCompletionEvent((TabCompletionEvent) e); } break;
			case chatLine: for (SubMod m : mods) { m.chatLineCreatedEvent((ChatLineCreatedEvent) e); } break;
			case modCallout: for (SubMod m : mods) { m.subModCalloutEvent((SubModCalloutEvent) e); } break;
			}
		}
	}
}
