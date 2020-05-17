package com.Whodundid.core.debug;

import com.Whodundid.core.EnhancedMC;
import com.Whodundid.core.app.EMCApp;
import com.Whodundid.core.coreApp.EMCNotification;
import com.Whodundid.core.coreApp.EMCResources;
import com.Whodundid.core.coreEvents.emcEvents.ChatLineCreatedEvent;
import com.Whodundid.core.coreEvents.emcEvents.TabCompletionEvent;
import com.Whodundid.core.enhancedGui.guiObjects.advancedObjects.colorPicker.EGuiColorPicker;
import com.Whodundid.core.enhancedGui.guiObjects.advancedObjects.colorPicker.EGuiColorPickerSimple;
import com.Whodundid.core.enhancedGui.guiObjects.utilityObjects.EGuiPlayerViewer;
import com.Whodundid.core.enhancedGui.guiObjects.windows.TextureDisplayer;
import com.Whodundid.core.enhancedGui.types.WindowParent;
import com.Whodundid.core.terminal.gui.ETerminal;
import com.Whodundid.core.util.EUtil;
import com.Whodundid.core.util.playerUtil.PlayerFacing;
import com.Whodundid.core.util.renderUtil.CenterType;
import com.Whodundid.core.util.renderUtil.CursorHelper;
import com.Whodundid.core.util.renderUtil.EColors;
import com.Whodundid.core.util.storageUtil.DynamicTextureHandler;
import com.Whodundid.core.util.storageUtil.Matrix;
import java.awt.image.BufferedImage;
import java.io.File;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.net.URI;
import java.net.URL;
import java.nio.ByteBuffer;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.Iterator;
import java.util.Map;
import java.util.Optional;
import javax.imageio.IIOException;
import javax.imageio.ImageIO;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiChat;
import net.minecraft.client.renderer.texture.ITextureObject;
import net.minecraft.client.resources.FallbackResourceManager;
import net.minecraft.client.resources.IResource;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.network.PacketThreadUtil;
import net.minecraft.network.play.server.S3APacketTabComplete;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.ModContainer;
import org.lwjgl.BufferUtils;

//Author: Hunter Bragg

@SuppressWarnings("unused")
public class DebugFunctions {

	static Minecraft mc = Minecraft.getMinecraft();
	public static boolean drawWindowInit = false;
	public static boolean drawWindowPID = true;
	
	public static void runDebugFunction(IDebugCommand function) { runDebugFunction(function.getDebugCommandID(), null); }
	public static boolean runDebugFunction(int functionID) { return runDebugFunction(functionID, null); }
	public static boolean runDebugFunction(int functionID, ETerminal termIn, String... args) {
		try {
			switch (functionID) {
			case 0: debug_0(termIn, args); return true;
			case 1: debug_1(termIn, args); return true;
			case 2: debug_2(termIn, args); return true;
			case 3: debug_3(termIn, args); return true;
			case 4: debug_4(termIn, args); return true;
			}
		} catch (Throwable e) { e.printStackTrace(); }
		return false;
	}
	
	public static int getNum() { return IDebugCommand.values().length; }

	private static void debug_0(ETerminal termIn, String... args) throws Throwable {
		
	}
	
	private static void debug_1(ETerminal termIn, String... args) throws Throwable {
		
	}
	
	private static void debug_2(ETerminal termIn, String... args) throws Throwable {
		
	}
	
	private static void debug_3(ETerminal termIn, String... args) throws Throwable {
		
	}
	
	private static void debug_4(ETerminal termIn, String... args) throws Throwable {
		
	}
	
}
