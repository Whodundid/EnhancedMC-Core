package com.Whodundid.scripts;

import java.util.concurrent.atomic.AtomicBoolean;
import com.Whodundid.core.app.AppType;
import com.Whodundid.core.app.EMCApp;
import com.Whodundid.scripts.guis.ScriptCreatorGui;
import com.Whodundid.scripts.guis.ScriptMainGui;
import com.Whodundid.scripts.guis.ScriptTaskManagerGui;
import com.Whodundid.scripts.scriptUtil.ScriptRunner;
import com.Whodundid.scripts.system.FunctionHandler;
import net.minecraftforge.fml.common.Mod;

@Mod(modid = ScriptingApp.MODID, version = ScriptingApp.VERSION, name = ScriptingApp.NAME, dependencies = "required-after:enhancedmc")
public final class ScriptingApp extends EMCApp {
	
	public static final String MODID = "emcscripts";
	public static final String VERSION = "0.1";
	public static final String NAME = "EMC Scripts";
	
	protected final AtomicBoolean globalKill = new AtomicBoolean(false);
	protected ScriptRunner runner;
	protected FunctionHandler handler;
	
	public ScriptingApp() {
		super(AppType.SCRIPTS);
		author = "Whodundid";
		version = VERSION;
		shouldLoad = false;
		addDependency(AppType.CORE, "1.0");
		addDependency(AppType.HOTKEYS, "2.1");
		setMainGui(new ScriptMainGui());
		addGui(new ScriptTaskManagerGui(), new ScriptCreatorGui());
		setAliases("scripts", "scripting");
		//runner = new ScriptRunner(this);
		//handler = new FunctionHandler(this);
	}
	
	@Override
	public EMCApp setEnabled(boolean valueIn) {
		enabled = valueIn;
		//runner.setRunning(enabled);
		return this;
	}
	
	public ScriptingApp toggleGlobalKill() { globalKill.set(!globalKill.get()); return this; }
	public ScriptingApp setGlobalKill(boolean val) { globalKill.set(val); return this; }
	public boolean getGlobalKill() { return globalKill.get(); }
	//public ScriptRunner getScriptRunner() { return runner; }
	//public FunctionHandler getFunctionHandler() { return handler; }
}