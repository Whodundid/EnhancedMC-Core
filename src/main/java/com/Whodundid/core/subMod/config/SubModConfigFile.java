package com.Whodundid.core.subMod.config;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.Scanner;

import com.Whodundid.core.subMod.SubMod;
import com.Whodundid.core.util.storageUtil.EArrayList;
import com.Whodundid.core.util.storageUtil.StorageBox;
import com.Whodundid.core.util.storageUtil.StorageBoxHolder;

//Last edited: Dec 12, 2018
//First Added: Dec 12, 2018
//Author: Hunter Bragg

public abstract class SubModConfigFile {
	
	protected SubMod mod;
	protected String configName = "";
	protected StorageBoxHolder<String, EArrayList<String>> configValues;
	
	public SubModConfigFile(SubMod modIn, String name) {
		mod = modIn;
		configName = name;
		configValues = new StorageBoxHolder<String, EArrayList<String>>();
	}
	
	public String getConfigName() { return configName; }
	public boolean exists() { return mod.getConfig().getConfigFileLocation(configName).exists(); }
	
	public StorageBoxHolder getConfigContents() {
		if (configValues == null) { configValues = new StorageBoxHolder<String, EArrayList<String>>(); }
		configValues.clear();
		parseFile();
		return configValues;
	}
	
	public abstract boolean saveConfig();
	public abstract boolean loadConfig();
	
	protected <Val extends Object> Val getConfigVal(String keyWord, Class<Val> asType) throws Exception { return getConfigVal(keyWord, asType, null); }
	protected <Val extends Object> Val getConfigVal(String keyWord, Class<Val> asType, Object defaultVal) throws Exception {
		StorageBoxHolder holder = configValues;
		StorageBox<String, EArrayList<String>> box = holder.getBoxWithObj(keyWord);
		if (box != null) {
			String sVal = box.getValue().get(0);
			Val returnVal = null;
			
			try {
				if (asType.isAssignableFrom(Boolean.class)) { returnVal = asType.cast(Boolean.parseBoolean(sVal)); }
				if (asType.isAssignableFrom(Byte.class)) { returnVal = asType.cast(Byte.parseByte(sVal)); }
				if (asType.isAssignableFrom(Short.class)) { returnVal = asType.cast(Short.parseShort(sVal)); }
				if (asType.isAssignableFrom(Integer.class)) { returnVal = asType.cast(Integer.parseInt(sVal)); }
				if (asType.isAssignableFrom(Long.class)) { returnVal = asType.cast(Long.parseLong(sVal)); }
				if (asType.isAssignableFrom(Float.class)) { returnVal = asType.cast(Float.parseFloat(sVal)); }
				if (asType.isAssignableFrom(Double.class)) { returnVal = asType.cast(Double.parseDouble(sVal)); }
				if (asType.isAssignableFrom(String.class)) { returnVal = asType.cast(new String(sVal)); }
			} catch (ClassCastException e) {
				e.printStackTrace();
				return (Val) defaultVal;
			}
			
			if (returnVal == null && defaultVal != null) {
				returnVal = (Val) defaultVal;
			}
			
			return returnVal;
		}
		return (Val) defaultVal;
	}
	
	private void parseFile() {
		File configFile = mod.getConfig().getConfigFileLocation(configName);
		if (configFile.exists()) {
			Scanner reader = null;
			try {
				reader = new Scanner(configFile);
				while (reader.hasNextLine()) {
					String line = reader.nextLine();
					if (line.length() >= 2 && line.substring(0, 2).equals("**")) { continue; } //comment identifier
					if (line.equals("END")) { break; } //config end identifier
					if (line.length() == 1) { break; } //ignore one character long lines
					
					String identifier = "", restOfLine = "";
					if (line.length() > 0) {
						for (int i = 0; i < line.length(); i++) {
							if (line.charAt(i) == ':') {
								identifier = line.substring(0, i + 1);
								restOfLine = line.substring(i + 1, line.length());
								break;
							}
						}
						if (!identifier.isEmpty()) {
							if (restOfLine.isEmpty()) {
								configValues.add(identifier, new EArrayList<String>());
							} else {
								String[] lineContents = restOfLine.split(" ");
								if (lineContents.length > 1) {
									EArrayList<String> values = new EArrayList();
									for (int i = 1; i < lineContents.length; i++) { values.add(lineContents[i]); }
									configValues.add(identifier, values);
								}
							}
						}
					}
				}
			} catch (FileNotFoundException e) { e.printStackTrace(); }
			finally {
				if (reader != null) { reader.close(); }
			}
		}
	}
	
	protected boolean doesFileContainIdentifier(String identifierIn) {
		File configFile = mod.getConfig().getConfigFileLocation(configName);
		if (configFile.exists()) {
			Scanner reader = null;
			try {
				reader = new Scanner(configFile);
				while (reader.hasNextLine()) {
					String line = reader.nextLine();
					if (line.length() >= 2 && line.substring(0, 2).equals("**")) { continue; } //comment identifier
					if (line.equals("END")) { break; } //config end identifier
					if (line.length() == 1) { break; } //ignore one character long lines
					
					String identifier = "";
					if (line.length() > 0) {
						for (int i = 0; i < line.length(); i++) {
							if (line.charAt(i) == ':') {
								identifier = line.substring(0, i + 1);
								if (identifier.equals(identifierIn)) { return true; }
								break;
							}
						}
					}
				}
			} catch (FileNotFoundException e) { e.printStackTrace(); }
			finally {
				if (reader != null) { reader.close(); }
			}
		}
		return false;
	}
	
	protected boolean createConfig(EArrayList<ConfigBlock> configContentsIn) {
		PrintWriter saver = null;
		try {
			//check for any special config blocks
			for (ConfigBlock block : new EArrayList<ConfigBlock>(configContentsIn)) {
				if (block instanceof CreateIfExistsConfigBlock) {
					//if the block is a 'CreateIfExistsConfigBlock', check if the config file exists -- if it does, check if the specific config line identifier exists in the file.
					//if the file exists, but the file doesn't contain the identifier, remove this block from the lines that will be saved.
					if (!doesFileContainIdentifier(((CreateIfExistsConfigBlock) block).getStringToCheckFor())) { configContentsIn.remove(block); }
				}
			}
			
			//move on to creating/overwriting the original file
			saver = new PrintWriter(mod.getConfig().getConfigFileLocation(configName), "UTF-8");
			for (ConfigBlock block : configContentsIn) {
				StorageBoxHolder<String, EArrayList<String>> blockContents = block.getBlockContents();
				for (StorageBox<String, EArrayList<String>> line : blockContents) {
					saver.print(line.getObject() + " ");
					EArrayList<String> values = line.getValue();
					for (int i = 0; i < values.size(); i++) {
						if (i != values.size() - 1) { saver.print(values.get(i) + " "); }
						else { saver.print(values.get(i)); }
					}
					saver.println();
				}
				if (block.createEmptyLineAfterBlock()) { saver.println(); }
			}
			saver.print("END");
		} catch (Exception e) { e.printStackTrace(); return false; }
		finally { if (saver != null) { saver.close(); } }
		return true;
	}
}
