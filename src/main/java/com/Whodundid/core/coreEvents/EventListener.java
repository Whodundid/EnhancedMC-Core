package com.Whodundid.core.coreEvents;

import org.lwjgl.opengl.GL11;
import com.Whodundid.core.EnhancedMC;
import com.Whodundid.core.coreEvents.emcEvents.ChatLineCreatedEvent;
import com.Whodundid.core.coreEvents.emcEvents.ModCalloutEvent;
import com.Whodundid.core.coreEvents.emcEvents.TabCompletionEvent;
import com.Whodundid.core.subMod.RegisteredSubMods;
import com.Whodundid.core.subMod.SubMod;
import net.minecraftforge.client.event.RenderPlayerEvent;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent.ElementType;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.client.event.EntityViewRenderEvent;
import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.client.event.MouseEvent;
import net.minecraftforge.client.event.RenderBlockOverlayEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent.KeyInputEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.event.CommandEvent;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;
import net.minecraftforge.event.world.WorldEvent;

/** A forge event wrapper for EnhancedMC and its SubMods */
public class EventListener {
	
	Minecraft mc = Minecraft.getMinecraft();
	
	@SubscribeEvent
    public void eventClientTick(TickEvent.ClientTickEvent e) {
		if (EnhancedMC.isInitialized()) {
			for (SubMod m : RegisteredSubMods.getRegisteredModsList()) { m.eventClientTick(e); }
		}
    }
	
	@SubscribeEvent
    public void eventTick(TickEvent e) {
		if (EnhancedMC.isInitialized()) {
			for (SubMod m : RegisteredSubMods.getRegisteredModsList()) { m.eventTick(e); }
		}
    }
	
	@SubscribeEvent
	public void eventInitGui(GuiScreenEvent.InitGuiEvent e) {
		if (EnhancedMC.isInitialized()) {
			for (SubMod m : RegisteredSubMods.getRegisteredModsList()) { m.eventInitGui(e); }
		}
	}
	
	@SubscribeEvent
	public void eventRenderPlayer(RenderPlayerEvent.Pre e) {
		if (EnhancedMC.isInitialized()) {
			for (SubMod m : RegisteredSubMods.getRegisteredModsList()) { m.eventRenderPlayer(e); }
		}
	}
	
	@SubscribeEvent
	public void eventRenderFogTick(EntityViewRenderEvent.FogDensity e) {
		if (EnhancedMC.isInitialized()) {
			for (SubMod m : RegisteredSubMods.getRegisteredModsList()) { m.eventRenderFogTick(e); }
		}
	}
	
	@SubscribeEvent
	public void eventBlockOverlayTick(RenderBlockOverlayEvent e) {
		if (EnhancedMC.isInitialized()) {
			for (SubMod m : RegisteredSubMods.getRegisteredModsList()) { m.eventBlockOverlayTick(e); }
		}
	}
	
	@SubscribeEvent
	public void eventLivingTick(LivingUpdateEvent e) {
		if (EnhancedMC.isInitialized()) {
			for (SubMod m : RegisteredSubMods.getRegisteredModsList()) { m.eventLivingTick(e); }
		}
	}
	
	@SideOnly(Side.CLIENT)
	@SubscribeEvent(priority = EventPriority.NORMAL, receiveCanceled = true)
	public void eventKey(KeyInputEvent e) {
		if (EnhancedMC.isInitialized()) {
			for (SubMod m : RegisteredSubMods.getRegisteredModsList()) { m.eventKey(e); }
		}
	}
	
	@SubscribeEvent
	public void eventMouse(MouseEvent e) {
		if (EnhancedMC.isInitialized()) {
			for (SubMod m : RegisteredSubMods.getRegisteredModsList()) { m.eventMouse(e); }
		}
	}
	
	@SubscribeEvent(priority = EventPriority.HIGH)
	public void eventOverlayRenderTick(RenderGameOverlayEvent e) {
		if (EnhancedMC.isInitialized()) {
			for (SubMod m : RegisteredSubMods.getRegisteredModsList()) { m.eventOverlayRenderTick(e); }
		}
	}
	
	@SubscribeEvent
	public void eventTextOverlayRenderTick(RenderGameOverlayEvent.Text e) {
		if (EnhancedMC.isInitialized()) {
			for (SubMod m : RegisteredSubMods.getRegisteredModsList()) { m.eventTextOverlayRenderTick(e); }
		}
	}
	
	@SubscribeEvent
	public void eventPreOverlayRenderTick(RenderGameOverlayEvent.Pre e) {
		if (EnhancedMC.isInitialized()) {
			for (SubMod m : RegisteredSubMods.getRegisteredModsList()) { m.eventPreOverlayRenderTick(e); }
		}
	}
	
	@SubscribeEvent
	public void eventRenderTick(TickEvent.RenderTickEvent e) {
		if (EnhancedMC.isInitialized()) {
			for (SubMod m : RegisteredSubMods.getRegisteredModsList()) { m.eventRenderTick(e); }
		}
	}
	
	@SubscribeEvent(priority = EventPriority.HIGH)
	public void eventPostOverlayRenderTick(RenderGameOverlayEvent.Post e) {
		if (EnhancedMC.isInitialized()) {
			if (e.type == ElementType.ALL) {
				GL11.glPushMatrix();
				GlStateManager.disableAlpha();
				GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
				for (SubMod m : RegisteredSubMods.getRegisteredModsList()) { m.eventPostOverlayRenderTick(e); }
				GlStateManager.enableAlpha();
				GL11.glPopMatrix();
			}
		}
	}
	
	@SubscribeEvent
	public void eventLastWorldRender(RenderWorldLastEvent e) {
		if (EnhancedMC.isInitialized()) {
			for (SubMod m : RegisteredSubMods.getRegisteredModsList()) { m.eventLastWorldRender(e); }
		}
	}
	
	@SubscribeEvent
	public void eventChat(ClientChatReceivedEvent e) {
		if (EnhancedMC.isInitialized()) {
			for (SubMod m : RegisteredSubMods.getRegisteredModsList()) { m.eventChat(e); }
		}
	}
	
	@SubscribeEvent
	public void eventChatLineCreated(ChatLineCreatedEvent e) {
		if (EnhancedMC.isInitialized()) {
			for (SubMod m : RegisteredSubMods.getRegisteredModsList()) { m.eventChatLineCreated(e); }
		}
	}
	
	@SubscribeEvent
	public void eventTabCompletion(TabCompletionEvent e) {
		if (EnhancedMC.isInitialized()) {
			for (SubMod m : RegisteredSubMods.getRegisteredModsList()) { m.eventTabCompletion(e); }
		}
	}
	
	@SubscribeEvent
	public void eventWorldUnload(WorldEvent.Unload e) {
		if (EnhancedMC.isInitialized()) {
			for (SubMod m : RegisteredSubMods.getRegisteredModsList()) { m.eventWorldUnload(e); }
		}
	}
	
	@SubscribeEvent
	@SideOnly(Side.CLIENT)
	public void eventWorldLoadClient(WorldEvent.Load e) {
		if (EnhancedMC.isInitialized()) {
			for (SubMod m : RegisteredSubMods.getRegisteredModsList()) { m.eventWorldLoadClient(e); }
		}
	}
	
	@SubscribeEvent
	@SideOnly(Side.SERVER)
	public void eventWorldLoadServer(WorldEvent.Load e) {
		if (EnhancedMC.isInitialized()) {
			for (SubMod m : RegisteredSubMods.getRegisteredModsList()) { m.eventWorldLoadServer(e); }
		}
	}
	
	@SubscribeEvent
    public void eventServerJoin(EntityJoinWorldEvent e) {
		if (EnhancedMC.isInitialized()) {
			for (SubMod m : RegisteredSubMods.getRegisteredModsList()) { m.eventServerJoin(e); }
		}
    }
	
	@SubscribeEvent
	public void eventCommand(CommandEvent e) {
		if (EnhancedMC.isInitialized()) {
			for (SubMod m : RegisteredSubMods.getRegisteredModsList()) { m.eventCommand(e); }
		}
	}
	
	@SubscribeEvent
	public void eventModCallout(ModCalloutEvent e) {
		if (EnhancedMC.isInitialized()) {
			for (SubMod m : RegisteredSubMods.getRegisteredModsList()) { m.eventModCallout(e); }
		}
	}
}