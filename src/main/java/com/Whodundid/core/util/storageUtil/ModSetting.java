package com.Whodundid.core.util.storageUtil;

import com.Whodundid.core.subMod.SubMod;
import com.Whodundid.core.util.miscUtil.DataType;
import java.util.Collection;

//Author: Hunter Bragg

public class ModSetting<Val> {
	
	private String settingName = "";
	private SubMod mod;
	private Val val = null;
	private DataType type;
	private EArrayList additionalArgs = new EArrayList();
	
	public ModSetting(String settingNameIn, Val initialValue) {
		settingName = settingNameIn;
		val = initialValue;
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
	
	public ModSetting setMod(SubMod modIn) { mod = modIn; return this; }
	public ModSetting set(Val valIn) { val = valIn; return this; }
	public ModSetting setArgs(Object... argsIn) { additionalArgs.addA(argsIn); return this; }
	public ModSetting setArgs(Collection argsIn) { additionalArgs.addAll(argsIn); return this; }
	
	public String getName() { return settingName; }
	public SubMod getMod() { return mod; }
	public DataType getType() { return type; }
	public Val get() { return val; }
	public EArrayList getArgs() { return additionalArgs; }
}
