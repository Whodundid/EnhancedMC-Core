package com.Whodundid.core.app;

import com.Whodundid.core.util.miscUtil.DataType;
import com.Whodundid.core.util.storageUtil.EArrayList;
import java.util.Collection;

//Author: Hunter Bragg

public class AppConfigSetting<Val> {
	
	private String settingName = "";
	private String description = "";
	private EMCApp mod;
	private boolean requiresOp = false;
	private Val val = null;
	private Val defaultVal = null;
	private DataType type;
	private EArrayList additionalArgs = new EArrayList();
	private final Class<Val> cType;
	
	public AppConfigSetting(Class<Val> cTypeIn, String settingNameIn, String descriptionIn, Val initialValue) {
		settingName = settingNameIn;
		val = initialValue;
		defaultVal = initialValue;
		cType = cTypeIn;
		description = (settingNameIn != null && descriptionIn == null) ? settingNameIn : descriptionIn;
		parseDataType();
	}
	
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
	
	//------------------
	//ModSetting Getters
	//------------------
	
	public String getName() { return settingName; }
	public String getDescription() { return description; }
	public String asString() { return val != null ? val.toString() : "null"; }
	public String defAsString() { return defaultVal != null ? defaultVal.toString() : "null"; }
	public EMCApp getMod() { return mod; }
	public DataType getType() { return type; }
	public Class<Val> getClassType() { return cType; }
	public Val get() { return val; }
	public boolean getRequiresOp() { return requiresOp; }
	public EArrayList getArgs() { return additionalArgs; }
	public Val getDefault() { return defaultVal; }
	
	//------------------
	//ModSetting Setters
	//------------------
	
	public AppConfigSetting setMod(EMCApp modIn) { mod = modIn; return this; }
	public AppConfigSetting set(Val valIn) { val = valIn; return this; }
	public AppConfigSetting setArgs(Object... argsIn) { additionalArgs.addA(argsIn); return this; }
	public AppConfigSetting setArgs(Collection argsIn) { additionalArgs.addAll(argsIn); return this; }
	public AppConfigSetting setOpSetting(boolean val) { requiresOp = val; return this; }
	
}
