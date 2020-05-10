package com.Whodundid.autoGM3;

import com.Whodundid.core.app.AppType;
import com.Whodundid.core.app.EMCApp;
import com.Whodundid.core.util.playerUtil.PlayerTraits;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.gameevent.TickEvent;

//Last edited: 10-16-18
//First Added: 4-18-18
//Author: Hunter Bragg

@Mod(modid = AutoGM3App.MODID, version = AutoGM3App.VERSION, name = AutoGM3App.NAME, dependencies = "required-after:enhancedmc")
public class AutoGM3App extends EMCApp {

	public static final String MODID = "autogm3";
	public static final String VERSION = "0.1";
	public static final String NAME = "Auto GM 3";
	
	public boolean CommandDelayBuffer = false;
	public long CommandDelay = 0;
	public long recheckTime = 1000;
	private static AutoGM3App instance;
	
	public AutoGM3App() {
		super(AppType.AUTOGM3);
		instance = this;
		shouldLoad = false;
		addDependency(AppType.CORE, "1.0");
		setAliases("autogm3", "agm3", "gm3");
	}
	
	public static AutoGM3App instance() { return instance; }
	
	@Override
	public void clientTickEvent(TickEvent.ClientTickEvent e) {
		if (isEnabled()) {
			if (PlayerTraits.isInBlock() && !PlayerTraits.isSpectator()) {
				if (!CommandDelayBuffer) {
					CommandDelayBuffer = true;
					CommandDelay = System.currentTimeMillis();
					mc.thePlayer.sendChatMessage("/gm 3");
					mc.ingameGUI.getChatGUI().addToSentMessages("/gm 1");
				} else {
					if (System.currentTimeMillis() - CommandDelay >= recheckTime) { CommandDelayBuffer = false; }
				}
			}
		}
	}
}
