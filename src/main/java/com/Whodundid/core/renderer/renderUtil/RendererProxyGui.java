package com.Whodundid.core.renderer.renderUtil;

import com.Whodundid.core.EnhancedMC;
import com.Whodundid.core.app.AppType;
import com.Whodundid.core.app.RegisteredApps;
import com.Whodundid.core.coreApp.CoreApp;
import com.Whodundid.core.coreApp.EMCNotification;
import com.Whodundid.core.enhancedGui.guiObjects.windows.TutorialWindow;
import com.Whodundid.core.enhancedGui.types.WindowParent;
import com.Whodundid.core.enhancedGui.types.interfaces.IEnhancedGuiObject;
import com.Whodundid.core.notifications.util.NotificationObject;
import com.Whodundid.core.renderer.EnhancedMCRenderer;
import com.Whodundid.core.renderer.taskView.TaskBar;
import com.google.common.collect.Lists;
import java.io.IOException;
import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiChat;
import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.common.MinecraftForge;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

//Author: Hunter Bragg

/** A type of GuiScreen which sends it's inputs to the EnhancedMCRenderer.
 *  Extends GuiChat to allow autocomplete events to be processed fully.
 */
public class RendererProxyGui extends GuiChat implements IRendererProxy {

	EnhancedMCRenderer renderer = EnhancedMCRenderer.getInstance();
	public List<String> foundPlayerNames = Lists.<String>newArrayList();
	private long lastMouseEvent, timeSinceLastClick;
	public String acReponse = "";
	public int mX, mY;
	public int lastMouseButton = -1;
	public int lastScrollChange = 0;
	public boolean mouseClicked = false;
	public boolean leftClick = false;
	public boolean rightClick = false;
	public boolean middleClick = false;
	public boolean playerNamesFound;
    public boolean waitingOnAutocomplete;
    public int autocompleteIndex;
    public int sentHistoryCursor = -1;
    public String historyBuffer = "";
    public static boolean pauseGame = false;
    public boolean ignoreEmpty = false;
    
	public RendererProxyGui() { this(false); }
	public RendererProxyGui(boolean ignoreEmptyIn) {
		ignoreEmpty = ignoreEmptyIn;
		handleOpen();
	}
	public RendererProxyGui(WindowParent guiIn) { this(guiIn, false); }
	public RendererProxyGui(WindowParent guiIn, boolean ignoreEmptyIn) {
		renderer = EnhancedMC.getRenderer();
		renderer.addObject(guiIn);
		handleOpen();
		ignoreEmpty = ignoreEmptyIn;
	}
	
	private void handleOpen() {
		if (CoreApp.enableTaskBar.get()) { renderer.addTaskBar(); }
		
		if (EnhancedMC.getNotificationHandler().isNotificationTypeEnabled(CoreApp.emcNotification)) {
			if (!CoreApp.firstUse.get()) {
				EMCNotification note = new EMCNotification("EMC: Welcome to EnhancedMC!");
				note.setHoverText("Click to open a tutorial on EMC");
				note.setAttentionObject(new TutorialWindow());
				note.setOnlyDrawOnHud(true);
				note.setTimeOut(12000l);
				
				EnhancedMC.postNotification(note);
				
				CoreApp.firstUse.set(true);
				CoreApp app = (CoreApp) RegisteredApps.getApp(AppType.CORE);
				if (app != null) { app.getConfig().saveMainConfig(); }
			}
			else if (!CoreApp.openedTut.get()) {
				EMCNotification note = new EMCNotification("EMC: Click to view Tutorial!");
				note.setHoverText("Click to open a tutorial on EMC");
				note.setAttentionObject(new TutorialWindow());
				note.setOnlyDrawOnHud(true);
				note.setTimeOut(12000l);
				
				EnhancedMC.postNotification(note);
			}
		}
	}
	
	// ------------------------
	// IRendererProxy Overrides
	// ------------------------
	
	@Override public int getMX() { return mX; }
	@Override public int getMY() { return mY; }
	
	@Override
	public void drawScreen(int mXIn, int mYIn, float ticks) {
		mX = mXIn;
		mY = mYIn;
		
		if (!ignoreEmpty) {
			if (CoreApp.closeHudWhenEmpty.get()) {
				if (renderer.getObjects().isEmpty()) {
					mc.displayGuiScreen(null);
					mc.setIngameFocus();
				}
				else if (renderer.getObjects().size() == 1 && renderer.getObjects().containsInstanceOf(TaskBar.class)) {
					renderer.getObjects().get(0).close();
					mc.displayGuiScreen(null);
					mc.setIngameFocus();
				}
				else {
					boolean onlyNotifications = false;
					for (IEnhancedGuiObject o : renderer.getObjects()) {
						if (o instanceof TaskBar) { continue; }
						if (o instanceof NotificationObject) { onlyNotifications = true; }
						else { onlyNotifications = false; break; }
					}
					
					if (onlyNotifications) {
						for (IEnhancedGuiObject o : renderer.getObjects()) {
							if (o instanceof NotificationObject) {
								NotificationObject n = (NotificationObject) o;
								if (n.onlyDrawsOnHud()) { n.close(); }
							}
							else { o.close(); }
						}
						mc.displayGuiScreen(null);
						mc.setIngameFocus();
					}
				}
				
			}
		}
	}
	
	// basic inputs
	@Override
	protected void mouseClicked(int mX, int mY, int button) throws IOException {
		renderer.mousePressed(mX, mY, button);
	}
	
	protected void mouseUnclicked(int mX, int mY, int button) {
		renderer.mouseReleased(mX, mY, button);
		super.mouseReleased(mX, mY, button);
	}
	
	@Override
	public void mouseClickMove(int mX, int mY, int button, long timeSinceLastClick) {
		renderer.mouseDragged(mX, mY, button, timeSinceLastClick);
		super.mouseClickMove(mX, mY, button, timeSinceLastClick);
	}
	
	// basic input handlers
	@Override
	public void handleMouseInput() throws IOException {
		mX = (Mouse.getEventX() * width / mc.displayWidth);
		mY = (height - Mouse.getEventY() * height / mc.displayHeight - 1);
		int button = Mouse.getEventButton();
		renderer.parseMousePosition(mX, mY);

		if (Mouse.hasWheel()) {
			lastScrollChange = Integer.signum(Mouse.getEventDWheel());
			if (lastScrollChange != 0) {
				renderer.mouseScrolled(lastScrollChange);
			}
		}
		
		if (Mouse.getEventButtonState()) {
			lastMouseEvent = Minecraft.getSystemTime();
			lastMouseButton = button;
			renderer.mousePressed(mX, mY, lastMouseButton);
		} else if (button != -1) {
			lastMouseButton = -1;
			renderer.mouseReleased(mX, mY, lastMouseButton);
		} else if (lastMouseButton != -1 && this.lastMouseEvent > 0L) {
			timeSinceLastClick = Minecraft.getSystemTime() - lastMouseEvent;
			renderer.mouseDragged(mX, mY, lastMouseButton, timeSinceLastClick);
		}
	}
	
	@Override
	public void handleKeyboardInput() throws IOException {
		if (Keyboard.getEventKeyState()) {
			//keyTyped(Keyboard.getEventCharacter(), Keyboard.getEventKey());
			renderer.keyPressed(Keyboard.getEventCharacter(), Keyboard.getEventKey());
		} else {
			renderer.keyReleased(Keyboard.getEventCharacter(), Keyboard.getEventKey());
		}
		mc.dispatchKeypresses();
	}
	
	@Override
	protected void keyTyped(char typedChar, int keyCode) throws IOException {
		
	}
	
	@Override
	public void setWorldAndResolution(Minecraft mc, int width, int height) {
		this.mc = mc;
		itemRender = mc.getRenderItem();
		fontRendererObj = mc.fontRendererObj;
		this.width = width;
		this.height = height;
		if (!MinecraftForge.EVENT_BUS.post(new GuiScreenEvent.InitGuiEvent.Pre(this, this.buttonList))) {
			buttonList.clear();
			initGui();
		}
		MinecraftForge.EVENT_BUS.post(new GuiScreenEvent.InitGuiEvent.Post(this, this.buttonList));
	}
	
	// resize
	@Override
	public void onResize(Minecraft mcIn, int width, int height) {
		renderer.windowResized(width, height);
		setWorldAndResolution(Minecraft.getMinecraft(), width, height);
	}
	
	@Override public boolean doesGuiPauseGame() { return pauseGame; }
	
}
