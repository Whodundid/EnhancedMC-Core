package com.Whodundid.core.coreEvents.eventUtil;

//Author: Hunter Bragg

public enum EMCEventType {
	
	//init
	postInit,
	
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
	rendererRCM,
	tabComplete,
	chatLine,
	appCallout,
	windowOpened,
	windowClosed,
	gameWindowResized,
	reloadingApp,
	appsReloaded,
	emcPostInit,
	genericEMC;
	
}
