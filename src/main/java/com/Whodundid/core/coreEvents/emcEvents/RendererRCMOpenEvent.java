package com.Whodundid.core.coreEvents.emcEvents;

import com.Whodundid.core.renderer.renderUtil.RendererRCM;
import net.minecraftforge.fml.common.eventhandler.Cancelable;
import net.minecraftforge.fml.common.eventhandler.Event;

@Cancelable
public class RendererRCMOpenEvent extends Event {
	
	RendererRCM rcm;
	
	public RendererRCMOpenEvent(RendererRCM in) {
		rcm = in;
	}
	
	public RendererRCM getRCM() { return rcm; }

}
