package com.Whodundid.core.subMod;

import com.Whodundid.core.EnhancedMC;
import java.io.File;
import java.io.PrintWriter;
import java.util.Scanner;
import org.apache.logging.log4j.Level;

//Last edited: 10-16-18
//First Added: 9-14-18
//Author: Hunter Bragg

public class SubModSettings {
	
	public static void updateModState(SubMod modIn, boolean state) {
		for (SubMod m : RegisteredSubMods.getRegisteredModsList()) {
			if (m.equals(modIn)) {
				if (!m.isIncompatible()) {
					if (m.isDisableable()) {
						m.setEnabled(state);
						updateConfig();
					}
					else {
						if (!m.isEnabled() && state == false) {
							m.setEnabled(state);
						}
					}
				}
				break;
			}
		}
	}
	
	public static void updateModState(SubModType typeIn, boolean state) { updateModState(typeIn.modName, state); }
	public static void updateModState(String modName, boolean state) {
		SubMod m = RegisteredSubMods.getMod(modName);
		if (m != null) {
			if (!m.isIncompatible()) {
				if (m.isDisableable()) {
					m.setEnabled(state);
					updateConfig();
				}
				else {
					if (!m.isEnabled() && state == false) {
						m.setEnabled(state);
					}
				}
			}
		}
	}
	
	private static void updateConfig() {
		try {
			File settings = new File("EnhancedMC/");
			if (!settings.exists()) { settings.mkdirs(); }
			PrintWriter saver = new PrintWriter("EnhancedMC/subMods.cfg" , "UTF-8");
			saver.println("SubMods");
			saver.println();
			for (SubMod m : RegisteredSubMods.getRegisteredModsList()) {
				saver.println(m.getModType() + " " + m.isEnabled());
			}
			saver.println();
			saver.print("END");
			saver.close();
		} catch (Exception e) { e.printStackTrace(); }
	}
	
	public static void loadConfig() {
		try {
			File loadedSettings = new File("EnhancedMC/subMods.cfg");
			if (loadedSettings.exists()) {
				Scanner loader = new Scanner(loadedSettings);
				while (loader.hasNextLine()) {
					if (loader.nextLine().equals("SubMods")) {
						while (loader.hasNextLine()) {
							String line = loader.nextLine();
							Scanner lineReader = new Scanner(line);
							if (!line.equals("END")) {
								if (lineReader.hasNext()) {
									String readMod = lineReader.next();
									SubModType type = SubModType.getTypeFromString(readMod);
									String valIn = lineReader.next();
									boolean val = false;
									try {
										val = Boolean.parseBoolean(valIn);
									} catch (Exception f) { EnhancedMC.log(Level.WARN, "failed to parse emc subModSettings file line: " + valIn); val = false; }
									updateModState(type != null ? type.modName : readMod, val);
								}
								lineReader.close();
							}
							else { break; }
						}
					}
				}
				loader.close();
			} else { updateConfig(); }
		} catch (Exception e) { e.printStackTrace(); updateConfig(); }
	}
}
