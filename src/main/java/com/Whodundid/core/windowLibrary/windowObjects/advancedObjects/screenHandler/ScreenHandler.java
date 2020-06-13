package com.Whodundid.core.windowLibrary.windowObjects.advancedObjects.screenHandler;

import com.Whodundid.core.util.EUtil;
import com.Whodundid.core.util.storageUtil.EArrayList;
import com.Whodundid.core.windowLibrary.windowTypes.ActionObject;
import com.Whodundid.core.windowLibrary.windowTypes.interfaces.IWindowObject;
import net.minecraft.util.MathHelper;

public class ScreenHandler extends ActionObject {
	
	private IWindowObject parent;
	private EArrayList<WindowScreen> screens = new EArrayList();
	private int currentScreen = 0;
	private boolean atBeginning = false;
	private boolean atEnd = false;
	
	//--------------------------
	//ScreenHandler Constructors
	//--------------------------
	
	public ScreenHandler(IWindowObject parentIn) { this(parentIn, (WindowScreen[]) null); }
	public ScreenHandler(IWindowObject parentIn, WindowScreen... screensIn) {
		super(parentIn);
		if (screensIn != null) { screens.addA(screensIn); }
		atBeginning = true;
		actionReceiver = parentIn;
	}
	
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
	
	public WindowScreen getCurrentScreen() { return screens.get(currentScreen); }
	public int getCurrentScreenNum() { return currentScreen; }
	public int getCurrentStage() { return (screens.isNotEmpty()) ? screens.get(currentScreen).getCurrentStage() : -1; }
	public EArrayList<WindowScreen> getScreens() { return screens; }
	public IWindowObject getParent() { return parent; }
	
	public boolean atBeginning() { return atBeginning; }
	public boolean atEnd() { return atEnd; }
	
	//---------------------
	//ScreenHandler Setters
	//---------------------
	
	public ScreenHandler setCurrentScreen(int screenNum, int stageNum) {
		setCurrentScreen(screenNum);
		setCurrentStage(stageNum);
		return this;
	}
	
	public ScreenHandler setCurrentScreen(int num) {
		currentScreen = MathHelper.clamp_int(num, 0, screens.size() - 1);
		checkScreen();
		return this;
	}
	
	public ScreenHandler setCurrentStage(int num) {
		WindowScreen screen = screens.get(currentScreen);
		if (screen != null) {
			num = MathHelper.clamp_int(num, 0, screen.getNumStages());
			screen.setCurrentStage(num);
		}
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
