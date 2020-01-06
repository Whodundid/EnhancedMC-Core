package com.Whodundid.core.subMod.config;

import com.Whodundid.core.util.storageUtil.EArrayList;
import com.Whodundid.core.util.storageUtil.StorageBoxHolder;
import java.util.List;

//Author: Hunter Bragg

public class ConfigBlock {
	
	protected StorageBoxHolder<String, List<String>> blockContents;
	protected boolean createEmptyLine = true;
	
	public ConfigBlock() { blockContents = new StorageBoxHolder<String, List<String>>().setAllowDuplicates(true); }
	public ConfigBlock(StorageBoxHolder<String, List<String>> elementsIn) { blockContents = elementsIn; }
	
	public ConfigBlock(String identifier, int... val) {
		this();
		EArrayList<String> list = new EArrayList();
		for (int i : val) { list.add(Integer.toString(i)); }
		blockContents.add(identifier, list);
	}
	
	public ConfigBlock(String identifier, long... val) {
		this();
		EArrayList<String> list = new EArrayList();
		for (long i : val) { list.add(Long.toString(i)); }
		blockContents.add(identifier, list);
	}
	
	public ConfigBlock(String identifier, double... val) {
		this();
		EArrayList<String> list = new EArrayList();
		for (double i : val) { list.add(Double.toString(i)); }
		blockContents.add(identifier, list);
	}
	
	public ConfigBlock(String identifier, char... val) {
		this();
		EArrayList<String> list = new EArrayList();
		for (char i : val) { list.add(Character.toString(i)); }
		blockContents.add(identifier, list);
	}
	
	public ConfigBlock(String identifier, String... val) {
		this();
		EArrayList<String> list = new EArrayList();
		for (String i : val) { list.add(i); }
		blockContents.add(identifier, list);
	}
	
	public ConfigBlock(String identifier, boolean... val) {
		this();
		EArrayList<String> list = new EArrayList();
		for (boolean i : val) { list.add(Boolean.toString(i)); }
		blockContents.add(identifier, list);
	}
	
	public ConfigBlock(String identifier, List vals) {
		this();
		blockContents.add(identifier, vals);
	}
	
	public ConfigBlock createEmptyLine(boolean valIn) { createEmptyLine = valIn; return this; }
	public ConfigBlock nl() { return noEmptyLine(); }
	public ConfigBlock noEmptyLine() { createEmptyLine = false; return this; }
	
	public boolean createEmptyLineAfterBlock() { return createEmptyLine; }
	public StorageBoxHolder<String, List<String>> getBlockContents() { return blockContents; }
}
