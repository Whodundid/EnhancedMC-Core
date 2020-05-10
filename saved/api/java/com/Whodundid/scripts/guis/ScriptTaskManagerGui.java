package com.Whodundid.scripts.guis;

import javax.management.AttributeList;
import javax.management.ObjectName;
import com.Whodundid.core.app.AppType;
import com.Whodundid.core.app.RegisteredApps;
import com.Whodundid.core.enhancedGui.guiObjects.actionObjects.EGuiButton;
import com.Whodundid.core.enhancedGui.types.WindowParent;
import com.Whodundid.core.enhancedGui.types.interfaces.IEnhancedActionObject;
import com.Whodundid.core.enhancedGui.types.interfaces.IEnhancedGuiObject;
import com.Whodundid.core.util.storageUtil.StorageBox;
import com.Whodundid.core.util.storageUtil.StorageBoxHolder;
import com.Whodundid.scripts.EScript;
import com.Whodundid.scripts.ScriptingApp;
import net.minecraft.client.Minecraft;
import java.lang.management.ManagementFactory;
import java.text.DecimalFormat;

public class ScriptTaskManagerGui extends WindowParent {
	
	static Minecraft mc = Minecraft.getMinecraft();
	ScriptingApp man = null;
	EGuiButton killScript;
	AttributeList sysList;
	StorageBoxHolder<Integer, EScript> selected = new StorageBoxHolder();
	protected static boolean loadedOnce = false;
	
	public ScriptTaskManagerGui() { super(); }
	public ScriptTaskManagerGui(Object oldGuiIn) { super(oldGuiIn); }
	public ScriptTaskManagerGui(IEnhancedGuiObject parentIn) { super(parentIn); }
	public ScriptTaskManagerGui(IEnhancedGuiObject parentIn, Object oldGuiIn) { super(parentIn, oldGuiIn); }
	public ScriptTaskManagerGui(IEnhancedGuiObject parentIn, int posX, int posY) { super(parentIn, posX, posY); }
	public ScriptTaskManagerGui(IEnhancedGuiObject parentIn, int posX, int posY, Object oldGuiIn) { super(parentIn, posX, posY, oldGuiIn); }
	
	@Override
	public void initGui() {
		defaultPos();
		super.initGui();
		man = (ScriptingApp) RegisteredApps.getApp(AppType.SCRIPTS);
	}
	
	@Override
	public void initObjects() {
		defaultHeader(this);
		
		killScript = new EGuiButton(this, endX - 65, endY - 25, 60, 20, "Kill Script");
		killScript.setEnabled(false);
		
		addObject(killScript);
	}
	
	@Override
	public void drawObject(int mXIn, int mYIn) {
		drawDefaultBackground();
		
		if (hasFirstDraw()) {
			drawCenteredString("Script name", midX - 37, midY - 123, 0x00FF00);
			drawCenteredString("ID", midX + 38, midY - 123, 0x00FF00);
			drawCenteredString("RTime", midX + 74, midY - 123, 0x00FF00);
			
			drawRect(startX + 5, startY + 14, endX - 5, endY - 30, 0xff000000);
			drawRect(startX + 6, startY + 15, endX - 6, endY - 31, 0xff2b2b2b);
			
			drawHardwareStatistics();
			drawActiveScripts();
			drawSelectedScripts();
		}
		
		super.drawObject(mXIn, mYIn);
	}
	
	@Override
	public void onFirstDraw() {
		initializeSys();
		drawCenteredStringWithShadow("Loading...", midX, midY, 0x55ff55);
		super.onFirstDraw();
	}
	
	private void drawActiveScripts() {
		drawInnerGui();
		//EArrayList<EScript> runningScripts = man.getScriptRunner().getScripts();
		//for (int i = 0; i < runningScripts.size(); i++) {
		//	drawCenteredString(runningScripts.get(i).getScriptName(), midX - 37, midY - 106 + ((i * 17) + i), 0xFFFFFF);
		//	drawCenteredString(runningScripts.get(i).getScriptID() + "", midX + 38, midY - 106 + ((i * 17) + i), 0xFFFFFF);
		//	drawCenteredString(runningScripts.get(i).getAliveTime() + "", midX + 74, midY - 106 + ((i * 17) + i), 0xFFFFFF);
		//}
	}
	
	private void drawInnerGui() {
		
	}
	
	@Override
	public void mousePressed(int mX, int mY, int button) {
		super.mousePressed(mX, mY, button);
		/*
		if (mX >= midX - 90 && mX <= midX + 90) {
			if (mY >= midY - 110 && mY <= midY + 87) {
				int startY = mY - (midY - 110);
				int tempPos = (startY / 18);
				if (!selected.contains(tempPos)) {
					if (tempPos <= ScriptManager.getCurrentlyRunningScripts().size() - 1) {
						if (this.isCtrlKeyDown()) {
							selected.add(tempPos, ScriptManager.getCurrentlyRunningScripts().get(tempPos));
							return;
						} else if (this.isShiftKeyDown()) {
							if (!selected.isEmpty()) {
								int highest = this.getHighestSelectedPosition();
								int lowest = this.getLowestSelectedPosition();
								if (tempPos < highest) {
									while (tempPos < highest) {
										selected.add(tempPos, ScriptManager.getCurrentlyRunningScripts().get(tempPos));
										tempPos++;
									}
								} else {
									while (tempPos > lowest) {
										selected.add(tempPos, ScriptManager.getCurrentlyRunningScripts().get(tempPos));
										tempPos--;
									}
								}
							}
							return;
						} else {
							selected.clear();
							selected.add(tempPos, ScriptManager.getCurrentlyRunningScripts().get(tempPos));
							return;
						}
					}
				} else {
					if (tempPos <= ScriptManager.getCurrentlyRunningScripts().size() - 1) {
						if (this.isCtrlKeyDown()) {
							try {
								selected.removeBoxesContainingObj(tempPos);
								return;
							} catch (Exception e) { e.printStackTrace(); }
						}
					}
				}
			}
		}
		selected.clear();
		*/
	}
	
	@Override
	public void actionPerformed(IEnhancedActionObject object, Object... args) {
		if (object.equals(killScript)) {
			killSelectedScripts();
		}
	}
	
	private void drawSelectedScripts() {
		for (StorageBox<Integer, EScript> i : selected) {
			//mc.renderEngine.bindTexture(EMCResources.scriptSelectionBox);
			//drawModalRectWithCustomSizedTexture(midX - 94, midY - 111 + ((i.getObject() * 17) + i.getObject()), 0, 0, 188, 17, 188, 17);
		}
	}
	
	private void killSelectedScripts() {
		/*
		if (!selected.isEmpty()) {
			for (StorageBox<Integer, Script> s : selected) {
				ScriptManager.removeRunningScript(s.getValue().scriptID);
			}
		}
		*/
	}
	
	private int getHighestSelectedPosition() {
		int returnVal = 0;
		for (StorageBox<Integer, EScript> i : selected) {
			if (i.getObject() > returnVal) { returnVal = i.getObject(); }
		}
		return returnVal;
	}
	
	private int getLowestSelectedPosition() {
		int returnVal = 11;
		for (StorageBox<Integer, EScript> i : selected) {
			if (i.getObject() < returnVal) { returnVal = i.getObject(); }
		}
		return returnVal;
	}
	
	private void initializeSys() {
		Thread runner = new Thread(() -> {
			try {
				sysList = ManagementFactory.getPlatformMBeanServer().getAttributes(ObjectName.getInstance("java.lang:type=OperatingSystem"), new String[] { "ProcessCpuLoad" });
			} catch (Exception e) { e.printStackTrace(); }
		});
		runner.start();
	}
	
	private void drawHardwareStatistics() {
		String cpuStat = "";
		if (sysList != null) {
			try {
				double cpuProcessUsage = 0;
				Double value = (Double) sysList.asList().get(0).getValue();
				cpuProcessUsage = (value * 1000) / 10;
				String usage = new DecimalFormat("0.0").format(cpuProcessUsage);
				cpuStat = "CPU usage: " + usage + "%";
				sysList = ManagementFactory.getPlatformMBeanServer().getAttributes(ObjectName.getInstance("java.lang:type=OperatingSystem"), new String[] { "ProcessCpuLoad" });
			} catch (Exception e) { e.printStackTrace(); }
		}
		else { cpuStat = "Calculating..."; }
		drawStringWithShadow(cpuStat, startX + 5, endY - 25, 0x44ff44);
		
		long memory = (Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory()) / 1000000;
		drawStringWithShadow("RAM usage: " + String.valueOf(memory) + " MB", startX + 5, endY - 13, 0x44ff44);
	}
}
