package com.Whodundid.core.enhancedGui.guiObjects.basicObjects;

import com.Whodundid.core.enhancedGui.types.interfaces.IEnhancedGuiObject;
import com.Whodundid.core.subMod.SubMod;
import com.Whodundid.core.util.renderUtil.EColors;
import com.Whodundid.core.util.storageUtil.EArrayList;
import com.Whodundid.core.util.storageUtil.ModSetting;

//Author: Hunter Bragg

public class EGuiButton3Stage extends EGuiButton {

	protected String stage0 = "und";
	protected String stage1 = "und";
	protected String stage2 = "und";
	protected int color0 = 0xffffffff;
	protected int color1 = 0xffffffff;
	protected int color2 = 0xffffffff;
	protected int curStage = 0;
	
	public EGuiButton3Stage(IEnhancedGuiObject parentIn, int posX, int posY, int width, int height) {
		super(parentIn, posX, posY, width, height);
	}
	public EGuiButton3Stage(IEnhancedGuiObject parentIn, int posX, int posY, int width, int height, ModSetting settingIn) {
		super(parentIn, posX, posY, width, height);
		parseStages(settingIn);
	}
	public EGuiButton3Stage(IEnhancedGuiObject parentIn, int posX, int posY, int width, int height, ModSetting settingIn, int colorIn) {
		super(parentIn, posX, posY, width, height);
		parseStages(settingIn);
		setColor(colorIn);
	}
	public EGuiButton3Stage(IEnhancedGuiObject parentIn, int posX, int posY, int width, int height, ModSetting settingIn, int color0In, int color1In, int color2In) {
		super(parentIn, posX, posY, width, height);
		parseStages(settingIn);
		setColors(color0In, color1In, color2In);
	}
	
	@Override
	public void mousePressed(int mXIn, int mYIn, int button) {
		if (enabled && checkDraw()) {
			pressedButton = button;
			if (runActionOnPress) { onPress(); }
			else if (button == 0) {
				playPressSound();
				curStage = curStage >= 2 ? 0 : curStage + 1;
				loadStage(curStage);
				performAction(curStage);
			}
		}
	}
	
	public EGuiButton3Stage loadStage(int stageNum) {
		switch (stageNum) {
		case 0: setDisplayString(stage0).setDisplayStringColor(color0); curStage = 0; break;
		case 1: setDisplayString(stage1).setDisplayStringColor(color1); curStage = 1; break;
		case 2: setDisplayString(stage2).setDisplayStringColor(color2); curStage = 2; break;
		default: System.out.println("Out of range! -- This shouldn't happen!");
		}
		return this;
	}
	
	public EGuiButton3Stage loadStage(ModSetting settingIn) {
		if (settingIn.get().getClass().isAssignableFrom(String.class)) {
			String val = ((String) settingIn.get());
			if (stage0.equals("und") && stage1.equals("und") && stage2.equals("und")) { parseStages(settingIn); }
			else if (val.equals(stage0)) { loadStage(0); }
			else if (val.equals(stage1)) { loadStage(1); }
			else if (val.equals(stage2)) { loadStage(2); }
		}
		return this;
	}
	
	private void parseStages(ModSetting in) {
		if (in.get().getClass().isAssignableFrom(String.class)) {
			EArrayList<String> stages = in.getArgs();
			if (stages != null) {
				for (int i = 0; i < stages.size() && i < 3; i++) {
					switch (i) {
					case 0: setStage0(stages.get(i)); break;
					case 1: setStage1(stages.get(i)); break;
					case 2: setStage2(stages.get(i)); break;
					}
				}
			}
			loadStage(in);
		}
	}
	
	public EGuiButton3Stage updateDislay(ModSetting settingIn, SubMod modIn, boolean saveAll) {
		if (settingIn.get().getClass().isAssignableFrom(String.class)) {
			loadStage(curStage);
			settingIn.set(getDisplayString());
			if (modIn != null) {
				if (saveAll) { modIn.getConfig().saveAllConfigs(); }
				else { modIn.getConfig().saveMainConfig(); }
			}
		}
		return this;
	}
	
	//getters
	
	public int getCurrentStage() { return curStage; }
	
	public String getStage0() { return stage0; }
	public String getStage1() { return stage1; }
	public String getStage2() { return stage2; }
	
	public int getColor0() { return color0; }
	public int getColor1() { return color1; }
	public int getColor2() { return color2; }
	
	//setters
	
	public EGuiButton3Stage setColor(EColors colorIn) { return setColor(colorIn.c()); }
	public EGuiButton3Stage setColor(int colorIn) { color0 = colorIn; color1 = colorIn; color2 = colorIn; return this; }
	
	public EGuiButton3Stage setStage0(String textIn, EColors colorIn) { stage0 = textIn; color0 = colorIn.c(); return this; }
	public EGuiButton3Stage setStage1(String textIn, EColors colorIn) { stage1 = textIn; color1 = colorIn.c(); return this; }
	public EGuiButton3Stage setStage2(String textIn, EColors colorIn) { stage2 = textIn; color2 = colorIn.c(); return this; }
	
	public EGuiButton3Stage setStage0(String textIn, int colorIn) { stage0 = textIn; color0 = colorIn; return this; }
	public EGuiButton3Stage setStage1(String textIn, int colorIn) { stage1 = textIn; color1 = colorIn; return this; }
	public EGuiButton3Stage setStage2(String textIn, int colorIn) { stage2 = textIn; color2 = colorIn; return this; }
	
	public EGuiButton3Stage setStage0(String textIn) { stage0 = textIn; return this; }
	public EGuiButton3Stage setStage1(String textIn) { stage1 = textIn; return this; }
	public EGuiButton3Stage setStage2(String textIn) { stage2 = textIn; return this; }
	public EGuiButton3Stage setStages(String stage0In, String stage1In, String stage2In) { stage0 = stage0In; stage1 = stage1In; stage2 = stage2In; return this; }
	
	public EGuiButton3Stage setColor0(EColors colorIn) { color0 = colorIn.c(); return this; }
	public EGuiButton3Stage setColor1(EColors colorIn) { color1 = colorIn.c(); return this; }
	public EGuiButton3Stage setColor2(EColors colorIn) { color2 = colorIn.c(); return this; }
	public EGuiButton3Stage setColors(EColors color0In, EColors color1In, EColors color2In) { color0 = color0In.c(); color1 = color1In.c(); color2 = color2In.c(); return this; }
	
	public EGuiButton3Stage setColor0(int colorIn) { color0 = colorIn; return this; }
	public EGuiButton3Stage setColor1(int colorIn) { color1 = colorIn; return this; }
	public EGuiButton3Stage setColor2(int colorIn) { color2 = colorIn; return this; }
	public EGuiButton3Stage setColors(int color0In, int color1In, int color2In) { color0 = color0In; color1 = color1In; color2 = color2In; return this; }
}
