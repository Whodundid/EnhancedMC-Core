package com.Whodundid.core.enhancedGui.guiObjects.advancedObjects.screenHandler;

import com.Whodundid.core.enhancedGui.types.interfaces.IEnhancedActionObject;
import com.Whodundid.core.enhancedGui.types.interfaces.IEnhancedGuiObject;
import com.Whodundid.core.util.EUtil;
import com.Whodundid.core.util.storageUtil.EArrayList;
import net.minecraft.util.MathHelper;

public class ScreenHandler implements IEnhancedActionObject {
	
	private IEnhancedGuiObject parent;
	private EArrayList<WindowScreen> screens = new EArrayList();
	private int currentScreen = 0;
	private boolean atBeginning = false;
	private boolean atEnd = false;
	
	protected boolean runActionOnPress = false;
	protected Object selectedObject = null;
	protected Object storedObject = null;
	protected IEnhancedGuiObject actionReceiver;
	
	//--------------------------
	//ScreenHandler Constructors
	//--------------------------
	
	public ScreenHandler(IEnhancedGuiObject parentIn) { this(parentIn, null); }
	public ScreenHandler(IEnhancedGuiObject parentIn, WindowScreen... screensIn) {
		parent = parentIn;
		if (screensIn != null) { screens.addA(screensIn); }
		atBeginning = true;
		actionReceiver = parentIn;
	}
	
	//-------------------------------
	//IEnhancedActionObject Overrides
	//-------------------------------
	
	@Override public void performAction(Object... args) {}
	@Override public void onPress() {}
	@Override public boolean runsActionOnPress() { return runActionOnPress; }
	@Override public IEnhancedActionObject setRunActionOnPress(boolean val) { runActionOnPress = val; return this; }
	@Override public IEnhancedActionObject setActionReceiver(IEnhancedGuiObject objIn) { actionReceiver = objIn; return this; }
	@Override public IEnhancedGuiObject getActionReceiver() { return actionReceiver; }
	
	@Override public IEnhancedActionObject setStoredObject(Object objIn) { storedObject = objIn; return this; }
	@Override public Object getStoredObject() { return storedObject; }
	@Override public IEnhancedActionObject setSelectedObject(Object objIn) { selectedObject = objIn; return this; }
	@Override public Object getSelectedObject() { return selectedObject; }
	
	//---------------------
	//ScreenHandler Methods
	//---------------------
	
	public void drawCurrentScreen(int mXIn, int mYIn) {
		if (currentScreen >= 0 && currentScreen < screens.size()) {
			EUtil.ifNotNullDo(screens.get(currentScreen), s -> s.drawScreen(mXIn, mYIn));
		}
	}
	
	public void handleNext() { if (!nextScreenStage()) { nextScreen(); } }
	public void handlePrevious() { if (!previousScreenStage()) { previousScreen(); } }
	
	public boolean nextScreen() {
		if (currentScreen < screens.size() - 1) { //make sure not at end
			hideCurrent();
			currentScreen++;
			showCurrent();
			
			//check if at beginning or end
			checkScreen();
			actionReceiver.actionPerformed(this, currentScreen);
			return true;
		}
		return false;
	}
	
	public boolean previousScreen() {
		if (currentScreen > 0) { //make sure not at beginning
			hideCurrent();
			currentScreen--;
			showCurrent();
			
			//check if at beginning or end
			checkScreen();
			actionReceiver.actionPerformed(this, currentScreen);
			return true;
		}
		return false;
	}
	
	public boolean nextScreenStage() {
		if (screens.isNotEmpty()) {
			WindowScreen screen = screens.get(currentScreen);
			if (screen != null) {
				if (screen.getCurrentStage() < screen.getNumStages() - 1) {
					screen.nextStage();
					screen.onStageChanged();
					actionReceiver.actionPerformed(this, currentScreen);
					return true;
				}
			}
		}
		return false;
	}
	
	public boolean previousScreenStage() {
		WindowScreen screen = screens.get(currentScreen);
		if (screen != null) {
			if (screen.getCurrentStage() >= 1) {
				screen.prevStage();
				screen.onStageChanged();
				actionReceiver.actionPerformed(this, currentScreen);
				return true;
			}
		}
		return false;
	}
	
	public void showCurrent() { EUtil.ifNotNullDo(screens.get(currentScreen), s -> { s.showScreen(); s.onLoaded(); }); }
	public void hideCurrent() { EUtil.ifNotNullDo(screens.get(currentScreen), s -> { s.hideScreen(); s.onUnloaded(); }); }
	
	public void addScreen(WindowScreen... screensIn) { screens.addA(screensIn); }
	
	//---------------------
	//ScreenHandler Getters
	//---------------------
	
	public WindowScreen getCurrent() { return screens.get(currentScreen); }
	public int getCurrentNum() { return currentScreen; }
	public EArrayList<WindowScreen> getScreens() { return screens; }
	public IEnhancedGuiObject getParent() { return parent; }
	
	public int getCurrentStage() { return (screens.isNotEmpty()) ? screens.get(currentScreen).getCurrentStage() : -1; }
	
	public boolean atBeginning() { return atBeginning; }
	public boolean atEnd() { return atEnd; }
	
	//---------------------
	//ScreenHandler Setters
	//---------------------
	
	public ScreenHandler setCurrentScreen(int num) {
		currentScreen = MathHelper.clamp_int(num, 0, screens.size() - 1);
		checkScreen();
		return this;
	}
	
	//------------------------------
	//ScreenHandler Internal Methods
	//------------------------------
	
	private void checkScreen() {
		WindowScreen s = screens.get(currentScreen);
		
		atBeginning = false;
		atEnd = false;
		
		if (currentScreen == screens.size() - 1) {
			atEnd = (s.getCurrentStage() == s.getNumStages() - 1);
		}
		else if (currentScreen == 0) {
			atBeginning = (s.getCurrentStage() == 0);
		}
	}
	
}
