package com.Whodundid.core.coreEvents.eventUtil;

public enum EMCEvents {
	
	//ticks
	tick,
	cTick,
	rTick,
	lTick,
	
	//overlay renders
	overlay,
	overlayText,
	overlayPre,
	overlayPost,
	
	//visual renders
	renderFogDensity,
	renderFog,
	renderBlock,
	renderPlayerPre,
	renderPlayerPost,
	renderLastWorld,
	
	//input
	mouse,
	keyboard,
	chat,
	command,
	
	//guis
	initGui,
	
	//world
	worldLoadClient,
	worldLoadServer,
	worldUnload,
	serverJoin,
	
	//emc specific
	tabComplete,
	chatLine,
	modCallout;
}
