package com.Whodundid.core.app.config;

import com.Whodundid.core.util.storageUtil.EArrayList;
import com.Whodundid.core.util.storageUtil.StorageBoxHolder;
import java.util.List;
import java.util.stream.Collectors;

//Author: Hunter Bragg

/** Creates a config line with a specified 'identifier' keyword String and pairs it against a corresponding config value. */
public class ConfigBlock {
	
	protected StorageBoxHolder<String, List<String>> blockContents;
	protected boolean createEmptyLine = false;
	
	public ConfigBlock() { blockContents = new StorageBoxHolder<String, List<String>>().setAllowDuplicates(true); }
	public ConfigBlock(StorageBoxHolder<String, List<String>> elementsIn) { blockContents = elementsIn; }
	
	public ConfigBlock(String identifier, Boolean... val) { this(identifier, new EArrayList<Boolean>(val)); }
	public ConfigBlock(String identifier, Character... val) { this(identifier, new EArrayList<Character>(val)); }
	public ConfigBlock(String identifier, Byte... val) { this(identifier, new EArrayList<Byte>(val)); }
	public ConfigBlock(String identifier, Short... val) { this(identifier, new EArrayList<Short>(val)); }
	public ConfigBlock(String identifier, Long... val) { this(identifier, new EArrayList<Long>(val)); }
	public ConfigBlock(String identifier, Integer... val) { this(identifier, new EArrayList<Integer>(val)); }
	public ConfigBlock(String identifier, Float... val) { this(identifier, new EArrayList<Float>(val)); }
	public ConfigBlock(String identifier, Double... val) { this(identifier, new EArrayList<Double>(val)); }
	public ConfigBlock(String identifier, String... val) { this(identifier, new EArrayList<String>(val)); }
	public ConfigBlock(String identifier, Enum... val) { this(identifier, new EArrayList<Enum>(val)); }
	
	/*
	public ConfigBlock(String identifier, List<Integer> val) {
		this();
		EArrayList<String> list = new EArrayList();
		for (int i : val) { list.add(((Integer) i) != null ? Integer.toString(i) : "0"); }
		blockContents.add(identifier, list);
	}
	
	public ConfigBlock(String identifier, long... val) {
		this();
		EArrayList<String> list = new EArrayList();
		for (long i : val) { list.add(((Long) i) != null ? Long.toString(i) : "0"); }
		blockContents.add(identifier, list);
	}
	
	public ConfigBlock(String identifier, double... val) {
		this();
		EArrayList<String> list = new EArrayList();
		for (double i : val) { list.add(((Double) i) != null ? Double.toString(i) : "0.0"); }
		blockContents.add(identifier, list);
	}
	
	public ConfigBlock(String identifier, char... val) {
		this();
		EArrayList<String> list = new EArrayList();
		for (char i : val) { list.add(((Character) i) != null ? Character.toString(i) : "null"); }
		blockContents.add(identifier, list);
	}
	
	public ConfigBlock(String identifier, String... val) {
		this();
		EArrayList<String> list = new EArrayList();
		for (String i : val) { list.add(i != null ? i : "null"); }
		blockContents.add(identifier, list);
	}
	
	private ConfigBlock(String identifier, boolean... val) {
		this();
		EArrayList<String> list = new EArrayList();
		for (boolean i : val) { list.add(((Boolean) i) != null ? Boolean.toString(i) : "false"); }
		blockContents.add(identifier, list);
	}
	
	public ConfigBlock(String identifier, Enum... val) {
		this();
		EArrayList<String> list = new EArrayList();
		for (Enum i : val) { list.add(val.toString()); }
		blockContents.add(identifier, list);
	}
	*/
	
	public <T> ConfigBlock(String identifier, List<T> vals) {
		this();
		blockContents.add(identifier, vals.stream().map(o -> String.valueOf(o)).collect(Collectors.toList()));
	}
	
	public ConfigBlock(String identifier, AppConfigSetting setting) { this(identifier, setting, false); }
	public ConfigBlock(String identifier, AppConfigSetting setting, boolean useDefault) {
		this();
		if (setting != null) {
			Object val = useDefault ? setting.getDefault() : setting.get();
			blockContents.add(identifier, new EArrayList<String>(String.valueOf(val)));
		}
	}
	
	public ConfigBlock createEmptyLine(boolean valIn) { createEmptyLine = valIn; return this; }
	public ConfigBlock nl() { return createEmptyLine(true); }
	public ConfigBlock noEmptyLine() { createEmptyLine = false; return this; }
	
	public boolean createsEmptyLineAfterBlock() { return createEmptyLine; }
	public StorageBoxHolder<String, List<String>> getBlockContents() { return blockContents; }
}
