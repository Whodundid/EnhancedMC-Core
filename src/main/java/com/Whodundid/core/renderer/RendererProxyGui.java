package com.Whodundid.core.renderer;

import com.Whodundid.core.EnhancedMC;
import com.Whodundid.core.enhancedGui.InnerEnhancedGui;
import java.io.IOException;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.common.MinecraftForge;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

/** A type of GuiScreen which sends it's inputs to the EnhancedMCRenderer */
public class RendererProxyGui extends GuiScreen implements IRendererProxy {

	EnhancedMCRenderer renderer = EnhancedMCRenderer.getInstance();
	private long lastMouseEvent, timeSinceLastClick;
	public int mX, mY;
	public int lastMouseButton = -1;
	public int lastScrollChange = 0;
	public boolean mouseClicked = false;
	public boolean leftClick = false;
	public boolean rightClick = false;
	public boolean middleClick = false;

	public RendererProxyGui() {}
	public RendererProxyGui(InnerEnhancedGui guiIn) {
		renderer = EnhancedMC.getRenderer();
		renderer.addObject(guiIn);
	}

	@Override
	public void drawScreen(int mXIn, int mYIn, float ticks) {
		mX = mXIn;
		mY = mYIn;
		if (renderer.getImmediateChildren().isEmpty()) { mc.displayGuiScreen(null); mc.setIngameFocus(); }
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
			// mouseClicked(mX, mY, lastMouseButton);
		} else if (button != -1) {
			lastMouseButton = -1;
			renderer.mouseReleased(mX, mY, lastMouseButton);
			// mouseUnclicked(mX, mY, button);
		} else if (lastMouseButton != -1 && this.lastMouseEvent > 0L) {
			timeSinceLastClick = Minecraft.getSystemTime() - lastMouseEvent;
			renderer.mouseDragged(mX, mY, lastMouseButton, timeSinceLastClick);
			// mouseClickMove(mX, mY, lastMouseButton, timeSinceLastClick);
		}
	}

	@Override
	public void handleKeyboardInput() throws IOException {
		if (Keyboard.getEventKeyState()) {
			keyTyped(Keyboard.getEventCharacter(), Keyboard.getEventKey());
			renderer.keyPressed(Keyboard.getEventCharacter(), Keyboard.getEventKey());
		} else {
			renderer.keyReleased(Keyboard.getEventCharacter(), Keyboard.getEventKey());
		}
		mc.dispatchKeypresses();
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
	
	@Override public boolean doesGuiPauseGame() { return false; }

	// ------------------------
	// IRendererProxy Overrides
	// ------------------------

	@Override public int getMX() { return mX; }
	@Override public int getMY() { return mY; }
}
