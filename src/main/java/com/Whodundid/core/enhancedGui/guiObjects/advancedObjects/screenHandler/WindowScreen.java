package com.Whodundid.core.enhancedGui.guiObjects.advancedObjects.screenHandler;

import com.Whodundid.core.enhancedGui.types.interfaces.IEnhancedGuiObject;
import com.Whodundid.core.util.EUtil;
import com.Whodundid.core.util.storageUtil.EArrayList;
import net.minecraft.util.MathHelper;

public class WindowScreen {
	
	private EArrayList<IEnhancedGuiObject> objects = new EArrayList();
	private int curStage = 0;
	private int numStages = 1;
	
	//-------------------------
	//WindowScreen Constructors
	//-------------------------
	
	public WindowScreen() { this(1); }
	public WindowScreen(int numStagesIn) { numStages = numStagesIn; curStage = 0; }
	
	//--------------------
	//WindowScreen Methods
	//--------------------
	
	public void drawScreen(int mXIn, int mYIn) {}
	public void onLoaded() {}
	public void onUnloaded() {}
	public void onStageChanged() {}
	
	public void setCurrentStage(int num) { curStage = MathHelper.clamp_int(num, 0, numStages); }
	public void nextStage() { curStage = MathHelper.clamp_int(curStage + 1, 0, numStages); }
	public void prevStage() { curStage = MathHelper.clamp_int(curStage - 1, 0, numStages); }
	
	public void showScreen() { objects.forEach(o -> EUtil.ifNotNullDo(o, i -> i.setVisible(true))); }
	public void hideScreen() { objects.forEach(o -> EUtil.ifNotNullDo(o, i -> i.setVisible(false))); }
	
	//--------------------
	//WindowScreen Getters
	//--------------------
	
	public EArrayList<IEnhancedGuiObject> getObjects() { return objects; }
	public int getNumStages() { return numStages; }
	public int getCurrentStage() { return curStage; }
	
	//--------------------
	//WindowScreen Setters
	//--------------------
	
	public WindowScreen setObjects(IEnhancedGuiObject... objectsIn) {
		objects.addA(objectsIn);
		return this;
	}
	
}
