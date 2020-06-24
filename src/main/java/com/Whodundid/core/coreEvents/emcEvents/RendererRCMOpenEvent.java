package com.Whodundid.core.coreEvents.emcEvents;

import com.Whodundid.core.coreEvents.EMCEvent;
import com.Whodundid.core.renderer.renderUtil.RendererRCM;
import net.minecraftforge.fml.common.eventhandler.Cancelable;

@Cancelable
public class RendererRCMOpenEvent extends EMCEvent {
	
	RendererRCM rcm;
	
	public RendererRCMOpenEvent(RendererRCM in) {
		rcm = in;
	}
	
	public RendererRCM getRCM() { return rcm; }

}
