package com.Whodundid.core.debug;

import com.Whodundid.core.EnhancedMC;
import com.Whodundid.core.app.AppResources;
import com.Whodundid.core.app.EMCApp;
import com.Whodundid.core.app.RegisteredApps;
import com.Whodundid.core.coreApp.CoreApp;
import com.Whodundid.core.coreApp.EMCNotification;
import com.Whodundid.core.coreApp.EMCResources;
import com.Whodundid.core.coreEvents.emcEvents.ChatLineCreatedEvent;
import com.Whodundid.core.coreEvents.emcEvents.TabCompletionEvent;
import com.Whodundid.core.terminal.window.ETerminal;
import com.Whodundid.core.util.EUtil;
import com.Whodundid.core.util.mathUtil.NumberUtil;
import com.Whodundid.core.util.playerUtil.DummyPlayer;
import com.Whodundid.core.util.playerUtil.PlayerDrawer;
import com.Whodundid.core.util.playerUtil.PlayerFacing;
import com.Whodundid.core.util.renderUtil.CenterType;
import com.Whodundid.core.util.renderUtil.CursorHelper;
import com.Whodundid.core.util.renderUtil.EColors;
import com.Whodundid.core.util.renderUtil.GLObject;
import com.Whodundid.core.util.resourceUtil.DynamicTextureHandler;
import com.Whodundid.core.util.resourceUtil.EResource;
import com.Whodundid.core.util.storageUtil.EArrayList;
import com.Whodundid.core.util.storageUtil.Matrix;
import com.Whodundid.core.windowLibrary.windowObjects.advancedObjects.colorPicker.ColorPicker;
import com.Whodundid.core.windowLibrary.windowObjects.advancedObjects.colorPicker.ColorPickerSimple;
import com.Whodundid.core.windowLibrary.windowObjects.utilityObjects.PlayerViewer;
import com.Whodundid.core.windowLibrary.windowObjects.windows.TextureDisplayer;
import com.Whodundid.core.windowLibrary.windowTypes.WindowParent;
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
import net.minecraft.client.renderer.entity.RenderPlayer;
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
	public static boolean drawInfo = true;
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
	
	public static int getTotal() { return IDebugCommand.values().length; }

	private static void debug_0(ETerminal termIn, String... args) throws Throwable {
		System.out.println("is remote: " + mc.theWorld.isRemote);
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
