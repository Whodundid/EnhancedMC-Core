package com.Whodundid.core.renderer;

import com.Whodundid.core.EnhancedMC;
import com.Whodundid.core.enhancedGui.guiObjects.EGuiTextField;
import com.Whodundid.core.enhancedGui.types.WindowParent;
import com.google.common.collect.Lists;
import com.google.common.collect.ObjectArrays;
import java.io.IOException;
import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiChat;
import net.minecraft.network.play.client.C14PacketTabComplete;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovingObjectPosition;
import net.minecraftforge.client.ClientCommandHandler;
import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.common.MinecraftForge;
import org.apache.commons.lang3.StringUtils;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

/** A type of GuiScreen which sends it's inputs to the EnhancedMCRenderer.
 *  Extends GuiChat to intercept autocomplete events.
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

	public RendererProxyGui() {}
	public RendererProxyGui(WindowParent guiIn) {
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
			keyTyped(Keyboard.getEventCharacter(), Keyboard.getEventKey());
			renderer.keyPressed(Keyboard.getEventCharacter(), Keyboard.getEventKey());
		} else {
			renderer.keyReleased(Keyboard.getEventCharacter(), Keyboard.getEventKey());
		}
		mc.dispatchKeypresses();
	}
	
	@Override
	public void keyTyped(char typedChar, int keyCode) throws IOException {
        waitingOnAutocomplete = false;
        if (keyCode == 15) { autocompletePlayerNames(); }
        else { playerNamesFound = false; }

        //if (keyCode == 1) { closeGui(); }
        if (keyCode != 28 && keyCode != 156) {
            if (keyCode == 200) { getSentHistory(-1); }
            else if (keyCode == 208) { getSentHistory(1); }
            else if (keyCode == 201) { mc.ingameGUI.getChatGUI().scroll(mc.ingameGUI.getChatGUI().getLineCount() - 1); }
            else if (keyCode == 209) { mc.ingameGUI.getChatGUI().scroll(-mc.ingameGUI.getChatGUI().getLineCount() + 1); }
            //else if (!mainChatWindow.getEntryField().hasFocus()) { mainChatWindow.getEntryField().keyPressed(typedChar, keyCode); }
        } else {
        	/*if (!mainChatWindow.getEntryField().hasFocus()) {
        		if (!mainChatWindow.getEntryField().getText().isEmpty()) {
        			sendChatMessage(mainChatWindow.getEntryField().getText());
        			mainChatWindow.setText("", true);
        		}
        	}*/
            //closeGui();
        }
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
	
	@Override
    public void autocompletePlayerNames() {
		EGuiTextField f = new EGuiTextField(renderer, 0, 0, 0, 0);
		if (playerNamesFound) {
            f.deleteFromCursor(f.func_146197_a(-1, f.getCursorPosition(), false) - f.getCursorPosition());
            if (autocompleteIndex >= foundPlayerNames.size()) { autocompleteIndex = 0; }
        } else {
        	int i = f.func_146197_a(-1, f.getCursorPosition(), false);
            foundPlayerNames.clear();
            autocompleteIndex = 0;
            String s = f.getText().substring(i).toLowerCase();
            String s1 = f.getText().substring(0, f.getCursorPosition());
            sendAutocompleteRequest(s1, s);

            if (foundPlayerNames.isEmpty()) { return; }
            playerNamesFound = true;
            f.deleteFromCursor(i - f.getCursorPosition());
        }

        if (foundPlayerNames.size() > 1) {
            StringBuilder stringbuilder = new StringBuilder();

            for (String s2 : foundPlayerNames) {
                if (stringbuilder.length() > 0) { stringbuilder.append(", "); }
                stringbuilder.append(s2);
            }
            mc.ingameGUI.getChatGUI().printChatMessageWithOptionalDeletion(new ChatComponentText(stringbuilder.toString()), 1);
        }
        f.writeText(EnumChatFormatting.getTextWithoutFormattingCodes(foundPlayerNames.get(autocompleteIndex++)));
    }
    
    private void sendAutocompleteRequest(String p_146405_1_, String p_146405_2_) {
        if (p_146405_1_.length() >= 1) {
            ClientCommandHandler.instance.autoComplete(p_146405_1_, p_146405_2_);
            BlockPos blockpos = null;

            if (mc.objectMouseOver != null && mc.objectMouseOver.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK) { blockpos = mc.objectMouseOver.getBlockPos(); }
            mc.thePlayer.sendQueue.addToSendQueue(new C14PacketTabComplete(p_146405_1_, blockpos));
            waitingOnAutocomplete = true;
        }
    }
    
    @Override
    public void onAutocompleteResponse(String[] response) {
        if (waitingOnAutocomplete) {
        	EGuiTextField f = new EGuiTextField(renderer, 0, 0, 0, 0);
            playerNamesFound = false;
            foundPlayerNames.clear();

            String[] complete = ClientCommandHandler.instance.latestAutoComplete;
            if (complete != null) { response = ObjectArrays.concat(complete, response, String.class); }

            for (String s : response) { if (s.length() > 0) { foundPlayerNames.add(s); } }

            String s1 = f.getText().substring(f.func_146197_a(-1, f.getCursorPosition(), false));
            String s2 = StringUtils.getCommonPrefix(response);
            s2 = EnumChatFormatting.getTextWithoutFormattingCodes(s2);

            if (s2.length() > 0 && !s1.equalsIgnoreCase(s2)) {
                f.deleteFromCursor(f.func_146197_a(-1, f.getCursorPosition(), false) - f.getCursorPosition());
                f.writeText(s2);
            }
            else if (foundPlayerNames.size() > 0) {
                playerNamesFound = true;
                autocompletePlayerNames();
            }
        }
    }
    
    @Override
    public void getSentHistory(int msgPos) {
    	EGuiTextField f = new EGuiTextField(renderer, 0, 0, 0, 0);
		int i = sentHistoryCursor + msgPos;
        int j = mc.ingameGUI.getChatGUI().getSentMessages().size();
        i = MathHelper.clamp_int(i, 0, j);

        if (i != sentHistoryCursor) {
            if (i == j) {
                sentHistoryCursor = j;
                f.setText(historyBuffer);
            } else {
                if (sentHistoryCursor == j) { historyBuffer = f.getText(); }
                f.setText(mc.ingameGUI.getChatGUI().getSentMessages().get(i));
                sentHistoryCursor = i;
            }
        }
    }
	
	@Override public boolean doesGuiPauseGame() { return false; }

	// ------------------------
	// IRendererProxy Overrides
	// ------------------------

	@Override public int getMX() { return mX; }
	@Override public int getMY() { return mY; }
}
