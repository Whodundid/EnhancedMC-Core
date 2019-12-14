package com.Whodundid.core.debug;

import com.Whodundid.core.EnhancedMC;
import com.Whodundid.core.coreEvents.emcEvents.ChatLineCreatedEvent;
import com.Whodundid.core.enhancedGui.guiObjects.windows.EGuiColorPicker;
import com.Whodundid.core.util.EUtil;
import com.Whodundid.windowHUD.windowObjects.hotbar.HotBarRenderer;
import java.io.File;
import net.minecraft.client.Minecraft;

//Last edited: 12-12-18
//First Added: 9-14-18
//Author: Hunter Bragg

@SuppressWarnings("unused")
public class DebugFunctions {

	static Minecraft mc = Minecraft.getMinecraft();

	public static void runDebugFunction(IDebugCommand function) {
		runDebugFunction(function.getDebugCommandID());
	}

	public static boolean runDebugFunction(int functionID) {
		try {
			switch (functionID) {
			case 0: debug_0(); return true;
			case 1: debug_1(); return true;
			case 2: debug_2(); return true;
			case 3: debug_3(); return true;
			}
		} catch (Throwable e) { e.printStackTrace(); }
		return false;
	}

	private static void debug_0() throws Throwable {
		//EnhancedMC.getRenderer().addObject(new HotBarRenderer());
		//System.out.println(EUtil.getFullEMCSourceFilePath(EnhancedMC.class));
		EnhancedMC.getRenderer().addObject(new EGuiColorPicker(EnhancedMC.getRenderer(), 150, 150));
	}
	
	private static void debug_1() throws Throwable {
		
	}
	
	private static void debug_2() throws Throwable {
		
	}
	
	private static void debug_3() throws Throwable {
		
	}
}
