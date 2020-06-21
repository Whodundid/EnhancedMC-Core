package com.Whodundid.slc.config;

import java.io.File;
import java.util.Scanner;
import com.Whodundid.core.app.RegisteredApps;
import com.Whodundid.slc.SLCApp;
import com.Whodundid.slc.util.GlobalModes;
import com.Whodundid.slc.util.LayerTypes;
import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.entity.player.EnumPlayerModelParts;

public class SLCLoadSkinConfigProfile {
	
	static boolean isEnd = false;
	static String configLine = "";
	static String command = "";
	static float versionNum;
	static String saveStates = "";
	
	static String globalSwitchingState = "", globalBlinkState = "", globalBlinkFlipState = "";
	static String hFlipped = "", jFlipped = "", caFlipped = "", laFlipped = "", raFlipped = "", llFlipped = "", rlFlipped = "";
	static String hatEnabledState = "", jacketEnabledState = "", capeEnabledState = "", laEnabledState = "";
	static String raEnabledState = "", llEnabledState = "", rlEnabledState = "";
	static String hState = "", jState = "", caState = "", laState = "", raState = "", llState = "", rlState = "";
	static String hMode = "", jMode = "", caMode = "", laMode = "", raMode = "", llMode = "", rlMode = "";
	static boolean globalSwitching = false, globalBlinking = false, globalBlinkFlip = false, states = false;
	static boolean hST = false, jST = false, caST = false, laST = false, raST = false, llST = false, rlST = false;
	static boolean h = false, j = false, ca = false, la = false, ra = false, ll = false, rl = false;
	static boolean hf = false, jf = false, caf = false, laf = false, raf = false, llf = false, rlf = false;
	static int globalSwitchSpeed, globalBlinkDelay, globalBlinkDuration;
	static int hS, jS, caS, laS, raS, llS, rlS;
	static int hbS, jbS, cabS, labS, rabS, llbS, rlbS;
	static int hbD, jbD, cabD, labD, rabD, llbD, rlbD;
	static int hO, jO, caO, laO, raO, llO, rlO;

	public static void load(SLCApp slc, String profile) {
		if (profile != null) {
			File profConfig;
			try {
				profConfig = new File(RegisteredApps.getAppConfigBaseFileLocation(slc.getAppType()) + "/SLC_Profile" + profile + ".cfg");
				if (profConfig.exists()) {
					isEnd = false;
					resetValues(slc);
					try (Scanner fileReader = new Scanner(profConfig)) {
						while (!isEnd) {
							configLine = fileReader.nextLine();
							Scanner line = new Scanner(configLine);
							command = line.next();
							switch (command) {
							case "**": break; //ignore line
							case "VERSION:": versionNum = Float.valueOf(line.next()); break;
							case "profileSaveStates:": saveStates = line.next(); break;
							
							case "globalSwitching:": globalSwitchingState = line.next(); break;
							case "globalBlinking:": globalBlinkState = line.next(); break;
							case "globalBlinkFlipped:": globalBlinkFlipState = line.next(); break;
							case "globalSwitchSpeed:": globalSwitchSpeed = Integer.valueOf(line.next()); break;
							case "globalBlinkDelay:": globalBlinkDelay = Integer.valueOf(line.next()); break;
							case "globalBlinkDuration:": globalBlinkDuration = Integer.valueOf(line.next()); break;
							
							case "hat:": hatEnabledState = line.next(); break;
							case "jacket:": jacketEnabledState = line.next(); break;
							case "cape:": capeEnabledState = line.next(); break;
							case "leftArm:": laEnabledState = line.next(); break;
							case "rightArm:": raEnabledState = line.next(); break;
							case "leftLeg:": llEnabledState = line.next(); break;
							case "rightLeg:": rlEnabledState = line.next(); break;
							
							case "hatState:": hState = line.next(); break;
							case "jacketState:": jState = line.next(); break;
							case "capeState:": caState = line.next(); break;
							case "leftArmState:": laState = line.next(); break;
							case "rightArmState:": raState = line.next(); break;
							case "leftLegState:": llState = line.next(); break;
							case "rightLegState:": rlState = line.next(); break;
							
							case "hatMode:": hMode = line.next(); break;
							case "jacketMode:": jMode = line.next(); break;
							case "capeMode:": caMode = line.next(); break;
							case "leftArmMode:": laMode = line.next(); break;
							case "rightArmMode:": raMode = line.next(); break;
							case "leftLegMode:": llMode = line.next(); break;
							case "rightLegMode:": rlMode = line.next(); break;
							
							case "hatSwitchSpeed:": hS = Integer.valueOf(line.next()); break;
							case "jacketSwitchSpeed:": jS = Integer.valueOf(line.next()); break;
							case "capeSwitchSpeed:": caS = Integer.valueOf(line.next()); break;
							case "leftArmSwitchSpeed:": laS = Integer.valueOf(line.next()); break;
							case "rightArmSwitchSpeed:": raS = Integer.valueOf(line.next()); break;
							case "leftLegSwitchSpeed:": llS = Integer.valueOf(line.next()); break;
							case "rightLegSwitchSpeed:": rlS = Integer.valueOf(line.next()); break;
							
							case "hatBlinkDelay:": hbS = Integer.valueOf(line.next()); break;
							case "jacketBlinkDelay:": jbS = Integer.valueOf(line.next()); break;
							case "capeBlinkDelay:": cabS = Integer.valueOf(line.next()); break;
							case "leftArmBlinkDelay:": labS = Integer.valueOf(line.next()); break;
							case "rightArmBlinkDelay:": rabS = Integer.valueOf(line.next()); break;
							case "leftLegBlinkDelay:": llbS = Integer.valueOf(line.next()); break;
							case "rightLegBlinkDelay:": rlbS = Integer.valueOf(line.next()); break;
							
							case "hatBlinkDuration:": hbD = Integer.valueOf(line.next()); break;
							case "jacketBlinkDuration:": jbD = Integer.valueOf(line.next()); break;
							case "capeBlinkDuration:": cabD = Integer.valueOf(line.next()); break;
							case "leftArmBlinkDuration:": labD = Integer.valueOf(line.next()); break;
							case "rightArmBlinkDuration:": rabD = Integer.valueOf(line.next()); break;
							case "leftLegBlinkDuration:": llbD = Integer.valueOf(line.next()); break;
							case "rightLegBlinkDuration:": rlbD = Integer.valueOf(line.next()); break;
							
							case "hatOffset:": hO = Integer.valueOf(line.next()); break;
							case "jacketOffset:": jO = Integer.valueOf(line.next()); break;
							case "capeOffset:": caO = Integer.valueOf(line.next()); break;
							case "leftArmOffset:": laO = Integer.valueOf(line.next()); break;
							case "rightArmOffset:": raO = Integer.valueOf(line.next()); break;
							case "leftLegOffset:": llO = Integer.valueOf(line.next()); break;
							case "rightLegOffset:": rlO = Integer.valueOf(line.next()); break;
							
							case "hatFlipped:": hFlipped = line.next(); break;
							case "jacketFlipped:": jFlipped = line.next(); break;
							case "capeFlipped:": caFlipped = line.next(); break;
							case "leftArmFlipped:": laFlipped = line.next(); break;
							case "rightArmFlipped:": raFlipped = line.next(); break;
							case "leftLegFlipped:": llFlipped = line.next(); break;
							case "rightLegFlipped:": rlFlipped = line.next(); break;
								
							case "END": isEnd = true;
							default: break;
							}
							line.close();
						}
						fileReader.close();
						correctValues();
						updateValues(slc);
					} catch (Exception e) { System.out.println("File corrupted.. Attempting to create new config."); createNewConfig(slc, profile); }
				} else { createNewConfig(slc, profile); }
			} catch (Exception e) { System.out.println("UNEXPECTED ERROR"); e.printStackTrace(); }
		}
	}
	
	public static void createNewConfig(SLCApp slc, String profile) {
		System.out.println("Creating new SLS profile " + profile + " config.");
		SLCSaveSkinConfigProfile.updateProfile(slc, Integer.valueOf(profile));
		switch (slc.resetMode) {
		case SW: globalSwitching = true; break;
		case BL: globalBlinking = true; break;
		default: globalSwitching = false; globalBlinking = false; break;
		}
		globalBlinkFlip = false;
		states = true;
		globalSwitchSpeed = slc.defaultSwitchSpeed;
		globalBlinkDelay = slc.defaultBlinkDelay;
		globalBlinkDuration = slc.defaultBlinkDuration;
		h = true; hf = false;
		j = true; jf = false;
		ca = true; caf = false;
		la = true; laf = false;
		ra = true; raf = false;
		ll = true; llf = false;
		rl = true; rlf = false;
		int s = slc.defaultSwitchSpeed;
		int b = slc.defaultBlinkDelay;
		int d = slc.defaultBlinkDuration;
		hFlipped = ""; jFlipped = ""; caFlipped = ""; laFlipped = ""; raFlipped = ""; llFlipped = ""; rlFlipped = "";
		hatEnabledState = ""; jacketEnabledState = ""; capeEnabledState = ""; laEnabledState = "";
		raEnabledState = ""; llEnabledState = ""; rlEnabledState = "";
		hState = ""; jState = ""; caState = ""; laState = ""; raState = ""; llState = ""; rlState = "";
		hMode = "none"; jMode = "none"; caMode = "none"; laMode = "none"; 
		raMode = "none"; llMode = "none"; rlMode = "none";
		hS = s; jS = s; caS = s; laS = s; raS = s; llS = s; rlS = s;
		hbS = b; jbS = b; cabS = b; labS = b; rabS = b; llbS = b; rlbS = b;
		hbD = d; jbD = d; cabD = d; labD = d; rabD = d; llbD = d; rlbD = d;
		hO = 0; jO = 0; caO = 0; laO = 0; raO = 0; llO = 0; rlO = 0;
		versionNum = Float.valueOf(slc.getVersion());
		correctValues();
		updateValues(slc);
	}
	
	private static void resetValues(SLCApp slc) {
		globalSwitchingState = "";
		globalBlinkState = "";
		globalBlinkFlipState = "";
		hatEnabledState = ""; hFlipped = ""; hState = "";
		jacketEnabledState = ""; jFlipped = ""; jState = "";
		capeEnabledState = ""; caFlipped = ""; caState = "";
		laEnabledState = ""; laState = ""; laFlipped = "";
		raEnabledState = ""; raState = ""; raFlipped = "";
		llEnabledState = ""; llState = ""; llFlipped = "";
		rlEnabledState = ""; rlState = ""; rlFlipped = "";
		globalSwitching = false;
		globalBlinking = false;
		globalBlinkFlip = false;
		states = false;
		h = false; hf = false; hST = false;
		j = false; jf = false; jST = false;
		ca = false; caf = false; caST = false;
		la = false; laf = false; laST = false;
		ra = false; raf = false; raST = false;
		ll = false; llf = false; llST = false;
		rl = false; rlf = false; rlST = false;
		globalSwitchSpeed = 0;
		globalBlinkDelay = 0;
		globalBlinkDuration = 0;
		hS = 0; jS = 0; caS = 0; laS = 0; raS = 0; llS = 0; rlS = 0;
		hbS = 0; jbS = 0; cabS = 0; labS = 0; rabS = 0; llbS = 0; rlbS = 0;
		hbD = 0; jbD = 0; cabD = 0; labD = 0; rabD = 0; llbD = 0; rlbD = 0;
		hO = 0; jO = 0; caO = 0; laO = 0; raO = 0; llO = 0; rlO = 0;
		versionNum = Float.valueOf(slc.getVersion());
	}
	
	private static void correctValues() {
		if (globalSwitchingState.equalsIgnoreCase("true")) { globalSwitching = true; }
		if (globalBlinkState.equalsIgnoreCase("true")) { globalBlinking = true; }
		if (globalBlinkFlipState.equalsIgnoreCase("true")) { globalBlinkFlip = true; }
		if (hatEnabledState.equalsIgnoreCase("true")) { h = true; }
		if (jacketEnabledState.equalsIgnoreCase("true")) { j = true; }
		if (capeEnabledState.equalsIgnoreCase("true")) { ca = true; }
		if (laEnabledState.equalsIgnoreCase("true")) { la = true; }
		if (raEnabledState.equalsIgnoreCase("true")) { ra = true; }
		if (llEnabledState.equalsIgnoreCase("true")) { ll = true; }
		if (rlEnabledState.equalsIgnoreCase("true")) { rl = true; }
		if (hState.equalsIgnoreCase("true")) { hST = true; }
		if (jState.equalsIgnoreCase("true")) { jST = true; }
		if (caState.equalsIgnoreCase("true")) { caST = true; }
		if (laState.equalsIgnoreCase("true")) { laST = true; }
		if (raState.equalsIgnoreCase("true")) { raST = true; }
		if (llState.equalsIgnoreCase("true")) { llST = true; }
		if (rlState.equalsIgnoreCase("true")) { rlST = true; }
		if (hFlipped.equalsIgnoreCase("true")) { hf = true; }
		if (jFlipped.equalsIgnoreCase("true")) { jf = true; }
		if (caFlipped.equalsIgnoreCase("true")) { caf = true; }
		if (laFlipped.equalsIgnoreCase("true")) { laf = true; }
		if (raFlipped.equalsIgnoreCase("true")) { raf = true; }
		if (llFlipped.equalsIgnoreCase("true")) { llf = true; }
		if (rlFlipped.equalsIgnoreCase("true")) { rlf = true; }
		if (saveStates.equalsIgnoreCase("true")) { states = true; }
	}
	
	private static void updateValues(SLCApp slc) {
		if (versionNum < Float.valueOf(slc.getVersion())) {
			System.out.println("WARNING! Using older configs in skin switcher could cause unpredicatble events!");
		}
		if (versionNum > Float.valueOf(slc.getVersion())) {
			System.out.println("WARNING! Future profile config version detected! Unpredicatble results may occur!");
		}
		
		if (states) { slc.savePartStates = true; }
		else { slc.savePartStates = false; }
		
		if (globalSwitching && globalBlinking) { globalBlinking = false; }
		if (globalSwitching) { 
			slc.setGlobalSwitch(true);
			slc.currentMode = GlobalModes.SW;
		}
		else if (globalBlinking) {
			slc.setGlobalSwitch(false);
			slc.currentMode = GlobalModes.BL;
		} else {
			slc.setGlobalSwitch(false);
			slc.setGlobalBlink(false);
			slc.currentMode = GlobalModes.IN;
		}
		if (globalBlinking) { slc.setGlobalBlink(true); }
		else { slc.setGlobalBlink(false); }
		
		if (globalBlinkFlip) { slc.toggleGlobalBlinkFlipped(); }
		
		if (globalSwitchSpeed < slc.MinRate) { slc.setGlobalSwitchSpeed(slc.MinRate); }
		else if (globalSwitchSpeed > slc.MaxRate) { slc.setGlobalSwitchSpeed(slc.MaxRate); }
		else { slc.setGlobalSwitchSpeed(globalSwitchSpeed); }
		if (globalBlinkDelay < slc.MinRate) { slc.setGlobalBlinkDelay(slc.MinRate); }
		else if (globalBlinkDelay > 100000) { slc.setGlobalBlinkDelay(slc.MaxRate); }
		else { slc.setGlobalBlinkDelay(globalBlinkDelay); }
		if (globalBlinkDuration < 200) { slc.setGlobalBlinkDuration(200); }
		else if (globalBlinkDuration > 100000) { slc.setGlobalBlinkDuration(100000); }
		else { slc.setGlobalBlinkDuration(globalBlinkDuration); }
		
		if (hMode.equals("switching")) { slc.getPart(LayerTypes.H).setSwitching(); }
		else if (hMode.equals("blinking")) { slc.getPart(LayerTypes.H).setBlinking(); }
		else { slc.getPart(LayerTypes.H).setNoMode(); }
		if (jMode.equals("switching")) { slc.getPart(LayerTypes.J).setSwitching(); }
		else if (jMode.equals("blinking")) { slc.getPart(LayerTypes.J).setBlinking(); }
		else { slc.getPart(LayerTypes.J).setNoMode(); }
		if (caMode.equals("switching")) { slc.getPart(LayerTypes.CA).setSwitching(); }
		else if (caMode.equals("blinking")) { slc.getPart(LayerTypes.CA).setBlinking(); }
		else { slc.getPart(LayerTypes.CA).setNoMode(); }
		if (laMode.equals("switching")) { slc.getPart(LayerTypes.LA).setSwitching(); }
		else if (laMode.equals("blinking")) { slc.getPart(LayerTypes.LA).setBlinking(); }
		else { slc.getPart(LayerTypes.LA).setNoMode(); }
		if (raMode.equals("switching")) { slc.getPart(LayerTypes.RA).setSwitching(); }
		else if (raMode.equals("blinking")) { slc.getPart(LayerTypes.RA).setBlinking(); }
		else { slc.getPart(LayerTypes.RA).setNoMode(); }
		if (llMode.equals("switching")) { slc.getPart(LayerTypes.LL).setSwitching(); }
		else if (llMode.equals("blinking")) { slc.getPart(LayerTypes.LL).setBlinking(); }
		else { slc.getPart(LayerTypes.LL).setNoMode(); }
		if (rlMode.equals("switching")) { slc.getPart(LayerTypes.RL).setSwitching(); }
		else if (rlMode.equals("blinking")) { slc.getPart(LayerTypes.RL).setBlinking(); }
		else { slc.getPart(LayerTypes.RL).setNoMode(); }
	
		if (h) { slc.getPart(LayerTypes.H).setEnabled(true); }
		if (j) { slc.getPart(LayerTypes.J).setEnabled(true); }
		if (ca) { slc.getPart(LayerTypes.CA).setEnabled(true); }
		if (la) { slc.getPart(LayerTypes.LA).setEnabled(true); }
		if (ra) { slc.getPart(LayerTypes.RA).setEnabled(true); }
		if (ll) { slc.getPart(LayerTypes.LL).setEnabled(true); }
		if (rl) { slc.getPart(LayerTypes.RL).setEnabled(true); }
		
		GameSettings s = Minecraft.getMinecraft().gameSettings;
		
		if (slc.savePartStates) {
			if (hST) {
				slc.getPart(LayerTypes.H).togglePartStateTo(true);
				s.setModelPartEnabled(EnumPlayerModelParts.HAT, true);
			} else s.setModelPartEnabled(EnumPlayerModelParts.HAT, false);
			if (jST) {
				slc.getPart(LayerTypes.J).togglePartStateTo(true);
				s.setModelPartEnabled(EnumPlayerModelParts.JACKET, true);
			} else s.setModelPartEnabled(EnumPlayerModelParts.JACKET, false);
			if (caST) {
				slc.getPart(LayerTypes.CA).togglePartStateTo(true);
				s.setModelPartEnabled(EnumPlayerModelParts.CAPE, true);
			} else s.setModelPartEnabled(EnumPlayerModelParts.CAPE, false);
			if (laST) {
				slc.getPart(LayerTypes.LA).togglePartStateTo(true);
				s.setModelPartEnabled(EnumPlayerModelParts.LEFT_SLEEVE, true);
			} else s.setModelPartEnabled(EnumPlayerModelParts.LEFT_SLEEVE, false);
			if (raST) {
				slc.getPart(LayerTypes.RA).togglePartStateTo(true);
				s.setModelPartEnabled(EnumPlayerModelParts.RIGHT_SLEEVE, true);
			} else s.setModelPartEnabled(EnumPlayerModelParts.RIGHT_SLEEVE, false);
			if (llST) {
				slc.getPart(LayerTypes.LL).togglePartStateTo(true);
				s.setModelPartEnabled(EnumPlayerModelParts.LEFT_PANTS_LEG, true);
			} else s.setModelPartEnabled(EnumPlayerModelParts.LEFT_PANTS_LEG, false);
			if (rlST) {
				slc.getPart(LayerTypes.RL).togglePartStateTo(true);
				s.setModelPartEnabled(EnumPlayerModelParts.RIGHT_PANTS_LEG, true);
			} else s.setModelPartEnabled(EnumPlayerModelParts.RIGHT_PANTS_LEG, false);
		} else {
			slc.getPart(LayerTypes.H).togglePartStateTo(false);
			slc.getPart(LayerTypes.J).togglePartStateTo(false);
			slc.getPart(LayerTypes.CA).togglePartStateTo(false);
			slc.getPart(LayerTypes.LA).togglePartStateTo(false);
			slc.getPart(LayerTypes.RA).togglePartStateTo(false);
			slc.getPart(LayerTypes.LL).togglePartStateTo(false);
			slc.getPart(LayerTypes.RL).togglePartStateTo(false);
			s.setModelPartEnabled(EnumPlayerModelParts.HAT, false);
			s.setModelPartEnabled(EnumPlayerModelParts.JACKET, false);
			s.setModelPartEnabled(EnumPlayerModelParts.CAPE, false);
			s.setModelPartEnabled(EnumPlayerModelParts.LEFT_SLEEVE, false);
			s.setModelPartEnabled(EnumPlayerModelParts.RIGHT_SLEEVE, false);
			s.setModelPartEnabled(EnumPlayerModelParts.LEFT_PANTS_LEG, false);
			s.setModelPartEnabled(EnumPlayerModelParts.RIGHT_PANTS_LEG, false);
		}
		
		if (hf) {
			slc.getPart(LayerTypes.H).setStateFlipped(true);
			slc.getPart(LayerTypes.H).togglePartStateTo(true);
			s.setModelPartEnabled(EnumPlayerModelParts.HAT, true);
			System.out.println(slc.getPart(LayerTypes.H).getState());
		} if (jf) {
			slc.getPart(LayerTypes.J).setStateFlipped(true);
			slc.getPart(LayerTypes.J).togglePartStateTo(!jST);
			s.setModelPartEnabled(EnumPlayerModelParts.JACKET, !jST);;
		} if (caf) {
			slc.getPart(LayerTypes.CA).setStateFlipped(true);
			slc.getPart(LayerTypes.CA).togglePartStateTo(!caST);
			s.setModelPartEnabled(EnumPlayerModelParts.CAPE, !caST);
		} if (laf) {
			slc.getPart(LayerTypes.LA).setStateFlipped(true);
			slc.getPart(LayerTypes.LA).togglePartStateTo(!laST);
			s.setModelPartEnabled(EnumPlayerModelParts.LEFT_SLEEVE, !laST);
		} if (raf) {
			slc.getPart(LayerTypes.RA).setStateFlipped(true);
			slc.getPart(LayerTypes.RA).togglePartStateTo(!raST);
			s.setModelPartEnabled(EnumPlayerModelParts.RIGHT_SLEEVE, !raST);
		} if (llf) {
			slc.getPart(LayerTypes.LL).setStateFlipped(true);
			slc.getPart(LayerTypes.LL).togglePartStateTo(!llST);
			s.setModelPartEnabled(EnumPlayerModelParts.LEFT_PANTS_LEG, !llST);
		} if (rlf) {
			slc.getPart(LayerTypes.RL).setStateFlipped(true);
			slc.getPart(LayerTypes.RL).togglePartStateTo(!rlST);
			s.setModelPartEnabled(EnumPlayerModelParts.RIGHT_PANTS_LEG, !rlST);
		}

		if (hS < slc.MinRate) { slc.getPart(LayerTypes.H).setSwitchSpeed(slc.MinRate); }
		else if (hS > slc.MaxRate) { slc.getPart(LayerTypes.H).setSwitchSpeed(slc.MaxRate); }
		else { slc.getPart(LayerTypes.H).setSwitchSpeed(hS); }
		if (jS < slc.MinRate) { slc.getPart(LayerTypes.J).setSwitchSpeed(slc.MinRate); }
		else if (jS > slc.MaxRate) { slc.getPart(LayerTypes.J).setSwitchSpeed(slc.MaxRate); }
		else { slc.getPart(LayerTypes.J).setSwitchSpeed(jS); }
		if (caS < slc.MinRate) { slc.getPart(LayerTypes.CA).setSwitchSpeed(slc.MinRate); }
		else if (caS > slc.MaxRate) { slc.getPart(LayerTypes.CA).setSwitchSpeed(slc.MaxRate); }
		else { slc.getPart(LayerTypes.CA).setSwitchSpeed(caS); }
		if (laS < slc.MinRate) { slc.getPart(LayerTypes.LA).setSwitchSpeed(slc.MinRate); }
		else if (laS > slc.MaxRate) { slc.getPart(LayerTypes.LA).setSwitchSpeed(slc.MaxRate); }
		else { slc.getPart(LayerTypes.LA).setSwitchSpeed(laS); }
		if (raS < slc.MinRate) { slc.getPart(LayerTypes.RA).setSwitchSpeed(slc.MinRate); }
		else if (raS > slc.MaxRate) { slc.getPart(LayerTypes.RA).setSwitchSpeed(slc.MaxRate); }
		else { slc.getPart(LayerTypes.RA).setSwitchSpeed(raS); }
		if (llS < slc.MinRate) { slc.getPart(LayerTypes.LL).setSwitchSpeed(slc.MinRate); }
		else if (llS > slc.MaxRate) { slc.getPart(LayerTypes.LL).setSwitchSpeed(slc.MaxRate); }
		else { slc.getPart(LayerTypes.LL).setSwitchSpeed(llS); }
		if (rlS < slc.MinRate) { slc.getPart(LayerTypes.RL).setSwitchSpeed(slc.MinRate); }
		else if (rlS > slc.MaxRate) { slc.getPart(LayerTypes.RL).setSwitchSpeed(slc.MaxRate); }
		else { slc.getPart(LayerTypes.RL).setSwitchSpeed(rlS); }
		
		if (hbS < slc.MinRate) { slc.getPart(LayerTypes.H).setBlinkSpeed(slc.MinRate); }
		else if (hbS > slc.MaxRate) { slc.getPart(LayerTypes.H).setBlinkSpeed(slc.MaxRate); }
		else { slc.getPart(LayerTypes.H).setBlinkSpeed(hbS); }
		if (jbS < slc.MinRate) { slc.getPart(LayerTypes.J).setBlinkSpeed(slc.MinRate); }
		else if (jbS > slc.MaxRate) { slc.getPart(LayerTypes.J).setBlinkSpeed(slc.MaxRate); }
		else { slc.getPart(LayerTypes.J).setBlinkSpeed(jbS); }
		if (cabS < slc.MinRate) { slc.getPart(LayerTypes.CA).setBlinkSpeed(slc.MinRate); }
		else if (cabS > slc.MaxRate) { slc.getPart(LayerTypes.CA).setBlinkSpeed(slc.MaxRate); }
		else { slc.getPart(LayerTypes.CA).setBlinkSpeed(cabS); }
		if (labS < slc.MinRate) { slc.getPart(LayerTypes.LA).setBlinkSpeed(slc.MinRate); }
		else if (labS > slc.MaxRate) { slc.getPart(LayerTypes.LA).setBlinkSpeed(slc.MaxRate); }
		else { slc.getPart(LayerTypes.LA).setBlinkSpeed(labS); }
		if (rabS < slc.MinRate) { slc.getPart(LayerTypes.RA).setBlinkSpeed(slc.MinRate); }
		else if (rabS > slc.MaxRate) { slc.getPart(LayerTypes.RA).setBlinkSpeed(slc.MaxRate); }
		else { slc.getPart(LayerTypes.RA).setBlinkSpeed(rabS); }
		if (llbS < slc.MinRate) { slc.getPart(LayerTypes.LL).setBlinkSpeed(slc.MinRate); }
		else if (llbS > slc.MaxRate) { slc.getPart(LayerTypes.LL).setBlinkSpeed(slc.MaxRate); }
		else { slc.getPart(LayerTypes.LL).setBlinkSpeed(llbS); }
		if (rlbS < slc.MinRate) { slc.getPart(LayerTypes.RL).setBlinkSpeed(slc.MinRate); }
		else if (rlbS > slc.MaxRate) { slc.getPart(LayerTypes.RL).setBlinkSpeed(slc.MaxRate); }
		else { slc.getPart(LayerTypes.RL).setBlinkSpeed(rlbS); }
		
		if (hbD < slc.MinRate) { slc.getPart(LayerTypes.H).setBlinkDuration(slc.MinBlinkDurRate); }
		else if (hbD > slc.MaxRate) { slc.getPart(LayerTypes.H).setBlinkDuration(slc.MaxRate); }
		else { slc.getPart(LayerTypes.H).setBlinkDuration(hbS); }
		if (jbD < slc.MinRate) { slc.getPart(LayerTypes.J).setBlinkDuration(slc.MinBlinkDurRate); }
		else if (jbD > slc.MaxRate) { slc.getPart(LayerTypes.J).setBlinkDuration(slc.MaxRate); }
		else { slc.getPart(LayerTypes.J).setBlinkDuration(jbS); }
		if (cabD < slc.MinRate) { slc.getPart(LayerTypes.CA).setBlinkDuration(slc.MinBlinkDurRate); }
		else if (cabD > slc.MaxRate) { slc.getPart(LayerTypes.CA).setBlinkDuration(slc.MaxRate); }
		else { slc.getPart(LayerTypes.CA).setBlinkDuration(cabS); }
		if (labD < slc.MinRate) { slc.getPart(LayerTypes.LA).setBlinkDuration(slc.MinBlinkDurRate); }
		else if (labD > slc.MaxRate) { slc.getPart(LayerTypes.LA).setBlinkDuration(slc.MaxRate); }
		else { slc.getPart(LayerTypes.LA).setBlinkDuration(labS); }
		if (rabD < slc.MinRate) { slc.getPart(LayerTypes.RA).setBlinkDuration(slc.MinBlinkDurRate); }
		else if (rabD > slc.MaxRate) { slc.getPart(LayerTypes.RA).setBlinkDuration(slc.MaxRate); }
		else { slc.getPart(LayerTypes.RA).setBlinkDuration(rabS); }
		if (llbD < slc.MinRate) { slc.getPart(LayerTypes.LL).setBlinkDuration(slc.MinBlinkDurRate); }
		else if (llbD > slc.MaxRate) { slc.getPart(LayerTypes.LL).setBlinkDuration(slc.MaxRate); }
		else { slc.getPart(LayerTypes.LL).setBlinkDuration(llbS); }
		if (rlbD < slc.MinRate) { slc.getPart(LayerTypes.RL).setBlinkDuration(slc.MinBlinkDurRate); }
		else if (rlbD > slc.MaxRate) { slc.getPart(LayerTypes.RL).setBlinkDuration(slc.MaxRate); }
		else { slc.getPart(LayerTypes.RL).setBlinkDuration(rlbS); }
		
		if (hO < 0) { slc.getPart(LayerTypes.H).setOffset(0); }
		else if (hO > slc.MaxRate) { slc.getPart(LayerTypes.H).setOffset(slc.MaxRate); }
		else { slc.getPart(LayerTypes.H).setOffset(hO); }
		if (jO < 0) { slc.getPart(LayerTypes.J).setOffset(0); }
		else if (jO > slc.MaxRate) { slc.getPart(LayerTypes.J).setOffset(slc.MaxRate); }
		else { slc.getPart(LayerTypes.J).setOffset(jO); }
		if (caO < 0) { slc.getPart(LayerTypes.CA).setOffset(0); }
		else if (caO > slc.MaxRate) { slc.getPart(LayerTypes.CA).setOffset(slc.MaxRate); }
		else { slc.getPart(LayerTypes.CA).setOffset(caO); }
		if (laO < 0) { slc.getPart(LayerTypes.LA).setOffset(0); }
		else if (laO > slc.MaxRate) { slc.getPart(LayerTypes.LA).setOffset(slc.MaxRate); }
		else { slc.getPart(LayerTypes.LA).setOffset(laO); }
		if (raO < 0) { slc.getPart(LayerTypes.RA).setOffset(0); }
		else if (raO > slc.MaxRate) { slc.getPart(LayerTypes.RA).setOffset(slc.MaxRate); }
		else { slc.getPart(LayerTypes.RA).setOffset(raO); }
		if (llO < 0) { slc.getPart(LayerTypes.LL).setOffset(0); }
		else if (llO > slc.MaxRate) { slc.getPart(LayerTypes.LL).setOffset(slc.MaxRate); }
		else { slc.getPart(LayerTypes.LL).setOffset(llO); }
		if (rlO < 0) { slc.getPart(LayerTypes.RL).setOffset(0); }
		else if (rlO > slc.MaxRate) { slc.getPart(LayerTypes.RL).setOffset(slc.MaxRate); }
		else { slc.getPart(LayerTypes.RL).setOffset(rlO); }
	}
}