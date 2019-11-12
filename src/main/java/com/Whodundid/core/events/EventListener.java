package com.Whodundid.core.events;

import org.lwjgl.opengl.GL11;
import com.Whodundid.core.EnhancedMC;
import com.Whodundid.core.coreSubMod.EMCInGameMenu;
import com.Whodundid.core.coreSubMod.EnhancedMCMod;
import com.Whodundid.core.events.emcEvents.ChatLineCreatedEvent;
import com.Whodundid.core.events.emcEvents.ModCalloutEvent;
import com.Whodundid.core.renderer.RendererProxyGui;
import com.Whodundid.core.subMod.RegisteredSubMods;
import com.Whodundid.core.util.chatUtil.EChatUtil;
import com.Whodundid.core.util.playerUtil.PlayerFacing;
import com.Whodundid.core.util.renderUtil.BlockDrawer;
import com.Whodundid.core.util.renderUtil.CursorHelper;
import com.Whodundid.core.util.worldUtil.WorldEditListener;
import net.minecraftforge.client.event.RenderPlayerEvent;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent.ElementType;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiIngameMenu;
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

//Oct 26, 2018

//Last edited: Jun 22, 2019
//Edit Note: added initial support for ERenderManager
//First Added: Oct 9, 2017
//Author: Hunter Bragg

/** A global listener for all events in EnhancedMC */
public class EventListener {
	
	Minecraft mc = Minecraft.getMinecraft();
	
	@SubscribeEvent
    public void onClientTick(TickEvent.ClientTickEvent e) {
		if (EnhancedMC.isInitialized()) {
			RegisteredSubMods.getRegisteredModsList().forEach(m -> m.eventClientTick(e));
			if (e.side == Side.CLIENT) {
				if (EnhancedMC.updateCounter == Integer.MAX_VALUE) {
					EnhancedMC.updateCounter = 0;
				}
				EnhancedMC.updateCounter++;
			}
		}
    }
	
	@SubscribeEvent
    public void onTick(TickEvent e) {
		if (EnhancedMC.isInitialized()) {
			RegisteredSubMods.getRegisteredModsList().forEach(m -> m.eventTick(e));
		}
    }
	
	@SubscribeEvent
	public void onInitGui(GuiScreenEvent.InitGuiEvent e) {
		if (EnhancedMC.isInitialized()) {
			CursorHelper.reset();
			if (e.gui instanceof GuiIngameMenu) {
				if (EnhancedMCMod.emcMenuOverride.get()) { mc.displayGuiScreen(new EMCInGameMenu()); }
			}
			RegisteredSubMods.getRegisteredModsList().forEach(m -> m.eventInitGui(e));
		}
	}
	
	@SubscribeEvent
	public void onPlayerRender(RenderPlayerEvent.Pre e) {
		if (EnhancedMC.isInitialized()) {
			RegisteredSubMods.getRegisteredModsList().forEach(m -> m.eventRenderPlayer(e));
		}
	}
	
	@SubscribeEvent
	public void onRenderFog(EntityViewRenderEvent.FogDensity e) {
		if (EnhancedMC.isInitialized()) {
			RegisteredSubMods.getRegisteredModsList().forEach(m -> m.eventRenderFogTick(e));
		}
	}
	
	@SubscribeEvent
	public void onBlockOverlayRender(RenderBlockOverlayEvent e) {
		if (EnhancedMC.isInitialized()) {
			RegisteredSubMods.getRegisteredModsList().forEach(m -> m.eventBlockOverlayTick(e));
		}
	}
	
	@SubscribeEvent
	public void living(LivingUpdateEvent e) {
		if (EnhancedMC.isInitialized()) {
			RegisteredSubMods.getRegisteredModsList().forEach(m -> m.eventLivingTick(e));
		}
	}
	
	@SideOnly(Side.CLIENT)
	@SubscribeEvent(priority = EventPriority.NORMAL, receiveCanceled = true)
	public  void onKeyEvent(KeyInputEvent e) {
		if (EnhancedMC.isInitialized()) {
			EnhancedMC.checkKeyBinds();
			RegisteredSubMods.getRegisteredModsList().forEach(m -> m.eventKey(e));
		}
	}
	
	@SubscribeEvent
	public void onMouseEvent(MouseEvent e) {
		if (EnhancedMC.isInitialized()) {
			RegisteredSubMods.getRegisteredModsList().forEach(m -> m.eventMouse(e));
		}
	}
	
	@SubscribeEvent(priority = EventPriority.HIGH)
	public void onOverlayRender(RenderGameOverlayEvent e) {
		if (EnhancedMC.isInitialized()) {
			RegisteredSubMods.getRegisteredModsList().forEach(m -> m.eventOverlayRenderTick(e));
		}
	}
	
	@SubscribeEvent
	public void onOverlayRender(RenderGameOverlayEvent.Text e) {
		if (EnhancedMC.isInitialized()) {
			RegisteredSubMods.getRegisteredModsList().forEach(m -> m.eventTextOverlayRenderTick(e));
		}
	}
	
	@SubscribeEvent
	public void onPreOverlayRender(RenderGameOverlayEvent.Pre e) {
		if (EnhancedMC.isInitialized()) {
			if (e.type == ElementType.CHAT && mc.currentScreen instanceof RendererProxyGui) { e.setCanceled(true); }
			RegisteredSubMods.getRegisteredModsList().forEach(m -> m.eventPreOverlayRenderTick(e));
		}
	}
	
	@SubscribeEvent
	public void onRenderTick(TickEvent.RenderTickEvent e) {
		if (EnhancedMC.isInitialized()) {
			PlayerFacing.checkEyePosition(e);
			RegisteredSubMods.getRegisteredModsList().forEach(m -> m.eventRenderTick(e));
		}
	}
	
	@SubscribeEvent(priority = EventPriority.HIGH)
	public void onPostOverlayRender(RenderGameOverlayEvent.Post e) {
		if (EnhancedMC.isInitialized()) {
			if (e.type == ElementType.ALL) {
				GL11.glPushMatrix();
				GlStateManager.disableAlpha();
				GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
				RegisteredSubMods.getRegisteredModsList().forEach(m -> m.eventPostOverlayRenderTick(e));
				GlStateManager.enableAlpha();
				GL11.glPopMatrix();
				EnhancedMC.getRenderer().onRenderTick(e);
			}
		}
	}
	
	@SubscribeEvent
	public void onLastWorldRender(RenderWorldLastEvent e) {
		if (EnhancedMC.isInitialized()) {
			BlockDrawer.draw(e);
			RegisteredSubMods.getRegisteredModsList().forEach(m -> m.eventLastWorldRender(e));
		}
	}
	
	@SubscribeEvent
	public void onChat(ClientChatReceivedEvent e) {
		if (EnhancedMC.isInitialized()) {
			EChatUtil.readChat(e.message);
			WorldEditListener.checkForPositions();
			RegisteredSubMods.getRegisteredModsList().forEach(m -> m.eventChat(e));
		}
	}
	
	@SubscribeEvent
	public void onChatLineCreated(ChatLineCreatedEvent e) {
		if (EnhancedMC.isInitialized()) {
			RegisteredSubMods.getRegisteredModsList().forEach(m -> m.eventChatLineCreated(e));
		}
	}
	
	@SubscribeEvent
	public void onWorldUnload(WorldEvent.Unload e) {
		if (EnhancedMC.isInitialized()) {
			CursorHelper.reset();
			RegisteredSubMods.getRegisteredModsList().forEach(m -> m.eventWorldUnload(e));
			BlockDrawer.clearBlocks();
			EnhancedMC.getRenderer().getImmediateChildren().clear();
		}
	}
	
	@SubscribeEvent
	public void onWorldLoad(WorldEvent.Load e) {
		if (EnhancedMC.isInitialized()) {
			CursorHelper.reset();
			//mc.ingameGUI = EnhancedMC.enhancedMCGui;
			RegisteredSubMods.getRegisteredModsList().forEach(m -> m.eventWorldLoad(e));
		}
	}
	
	@SubscribeEvent
    public void onServerJoin(EntityJoinWorldEvent e) {
		if (EnhancedMC.isInitialized()) {
			CursorHelper.reset();
			RegisteredSubMods.getRegisteredModsList().forEach(m -> m.eventServerJoin(e));
		}
    }
	
	@SubscribeEvent
	public void onCommand(CommandEvent e) {
		if (EnhancedMC.isInitialized()) {
			RegisteredSubMods.getRegisteredModsList().forEach(m -> m.eventCommand(e));
		}
	}
	
	@SubscribeEvent
	public void onModCallout(ModCalloutEvent e) {
		if (EnhancedMC.isInitialized()) {
			RegisteredSubMods.getRegisteredModsList().forEach(m -> m.eventModCallout(e));
		}
	}
}