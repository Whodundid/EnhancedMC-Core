package com.Whodundid.core.app.config;

import com.Whodundid.core.app.EMCApp;
import com.Whodundid.core.util.EUtil;
import com.Whodundid.core.util.miscUtil.DataType;
import com.Whodundid.core.util.storageUtil.EArrayList;
import java.util.Collection;

//Author: Hunter Bragg

public class AppConfigSetting<Val> {
	
	private String settingName = "";
	private String description = "";
	private EMCApp app;
	private boolean requiresDev = false;
	private Val val = null;
	private Val defaultVal = null;
	private DataType type;
	private EArrayList additionalArgs = new EArrayList();
	private final Class<Val> cType;
	private boolean ignoreConfigRead = false;
	private boolean ignoreConfigWrite = false;
	
	//-----------------------------
	//AppConfigSetting Constructors
	//-----------------------------
	
	public AppConfigSetting(Class<Val> cTypeIn, String settingNameIn, String descriptionIn, Val initialValue) {
		settingName = settingNameIn;
		val = initialValue;
		defaultVal = initialValue;
		cType = cTypeIn;
		description = (settingNameIn != null && descriptionIn == null) ? settingNameIn : descriptionIn;
		parseDataType();
	}
	
	//------------------------
	//AppConfigSetting Methods
	//------------------------
	
	public AppConfigSetting reset() { set(defaultVal); return this; }
	
	//------------------------
	//AppConfigSetting Getters
	//------------------------
	
	public String getName() { return settingName; }
	public String getDescription() { return description; }
	public String asString() { return val != null ? val.toString() : "null"; }
	public String defAsString() { return defaultVal != null ? defaultVal.toString() : "null"; }
	public EMCApp getApp() { return app; }
	public DataType getType() { return type; }
	public Class<Val> getClassType() { return cType; }
	public Val get() { return val; }
	public boolean getRequiresDev() { return requiresDev; }
	public EArrayList getArgs() { return additionalArgs; }
	public Val getDefault() { return defaultVal; }
	public boolean getIgnoreConfigRead() { return ignoreConfigRead; }
	public boolean getIgnoreConfigWrite() { return ignoreConfigWrite; }
	
	//------------------------
	//AppConfigSetting Setters
	//------------------------
	
	public AppConfigSetting setApp(EMCApp modIn) { app = modIn; return this; }
	public AppConfigSetting set(Val valIn) { val = valIn; return this; }
	public AppConfigSetting setArgs(Object... argsIn) { additionalArgs.addA(argsIn); return this; }
	public AppConfigSetting setArgs(Collection argsIn) { additionalArgs.addAll(argsIn); return this; }
	public AppConfigSetting setDevSetting(boolean val) { requiresDev = val; return this; }
	public AppConfigSetting setIgnoreConfigRead(boolean val) { ignoreConfigRead = val; return this; }
	public AppConfigSetting setIgnoreConfigWrite(boolean val) { ignoreConfigWrite = val; return this; }
	
	//---------------------------------
	//AppConfigSetting Internal Methods
	//---------------------------------
	
	private void parseDataType() {
		if (val != null) {
			Class c = val.getClass();
			try {
				if (c.isAssignableFrom(Boolean.class)) { type = DataType.BOOL; }
				if (c.isAssignableFrom(Byte.class)) { type = DataType.BYTE; }
				if (c.isAssignableFrom(Short.class)) { type = DataType.SHORT; }
				if (c.isAssignableFrom(Integer.class)) { type = DataType.INT; }
				if (c.isAssignableFrom(Long.class)) { type = DataType.LONG; }
				if (c.isAssignableFrom(Float.class)) { type = DataType.FLOAT; }
				if (c.isAssignableFrom(Double.class)) { type = DataType.DOUBLE; }
				if (c.isAssignableFrom(String.class)) { type = DataType.STRING; }
			}
			catch (ClassCastException e) {
				e.printStackTrace();
				type = DataType.OBJECT;
			}
		}
		else { type = DataType.OBJECT; }
	}
	
	//-------------------------------
	//AppConfigSetting Static Methods
	//-------------------------------
	
	public static void reset(AppConfigSetting setting, AppConfigSetting... additional) { EUtil.filterNullDo(s -> s.reset(), EUtil.add(setting, additional)); }
	
}
