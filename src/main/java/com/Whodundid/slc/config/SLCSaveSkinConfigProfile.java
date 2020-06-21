package com.Whodundid.slc.config;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import com.Whodundid.core.app.RegisteredApps;
import com.Whodundid.slc.SLCApp;
import com.Whodundid.slc.util.LayerTypes;
import com.Whodundid.slc.util.PartStats;

public class SLCSaveSkinConfigProfile {
	
	public static void updateProfile(SLCApp slc, int profileNum) {
		try {
			File config = RegisteredApps.getAppConfigBaseFileLocation(slc.getAppType());
			if (!config.exists()) { config.mkdirs(); }
			PartStats s = slc.getPartStats();
			PrintWriter saver = new PrintWriter(RegisteredApps.getAppConfigBaseFileLocation(slc.getAppType()) + "/slc_profile" + profileNum + ".cfg", "UTF-8");
			saver.println("** Skin Layer Switcher Profile Config **");
			saver.println("** NOTE: Only change these values if you know what you are doing!");
			saver.println("** Wrong values could potentially crash MC!");
			saver.println("** --------------------------");
			saver.println("VERSION: " + slc.getVersion());
			saver.println("PROFILE: " + profileNum);
			saver.println("profileSaveStates: " + slc.savePartStates);
			saver.println("** --------------------------");
			saver.println("globalSwitching: " + slc.getGlobalSwitchingStatus());
			saver.println("globalBlinking: " + slc.getGlobalBlinkingStatus());
			saver.println("globalBlinkingFlipped: " + slc.getGlobalBlinkFlipped());
			saver.println("**");
			saver.println("globalSwitchSpeed: " + slc.getGlobalSwitchingSpeed());
			saver.println("globalBlinkDelay: " + slc.getGlobalBlinkingSpeed());
			saver.println("globalBlinkDuration: " + slc.getGlobalBlinkDuration());
			saver.println("**");
			saver.println("hat: " + s.getPartEnabled(LayerTypes.H));
			saver.println("jacket: " + s.getPartEnabled(LayerTypes.J));
			saver.println("cape: " + s.getPartEnabled(LayerTypes.CA));
			saver.println("leftArm: " + s.getPartEnabled(LayerTypes.LA));
			saver.println("rightArm: " + s.getPartEnabled(LayerTypes.RA));
			saver.println("leftLeg: " + s.getPartEnabled(LayerTypes.LL));
			saver.println("rightLeg: " + s.getPartEnabled(LayerTypes.RL));
			saver.println("**");
			saver.println("hatState: " + s.getPartState(LayerTypes.H));
			saver.println("jacketState: " + s.getPartState(LayerTypes.J));
			saver.println("capeState: " + s.getPartState(LayerTypes.CA));
			saver.println("leftArmState: " + s.getPartState(LayerTypes.LA));
			saver.println("rightArmState: " + s.getPartState(LayerTypes.RA));
			saver.println("leftLegState: " + s.getPartState(LayerTypes.LL));
			saver.println("rightLegState: " + s.getPartState(LayerTypes.RL));
			saver.println("**");
			saver.println("hatMode: " + s.getPartMode(LayerTypes.H));
			saver.println("jacketMode: " + s.getPartMode(LayerTypes.J));
			saver.println("capeMode: " + s.getPartMode(LayerTypes.CA));
			saver.println("leftArmMode: " + s.getPartMode(LayerTypes.LA));
			saver.println("rightArmMode: " + s.getPartMode(LayerTypes.RA));
			saver.println("leftLegMode: " + s.getPartMode(LayerTypes.LL));
			saver.println("rightLegMode: " + s.getPartMode(LayerTypes.RL));
			saver.println("**");
			saver.println("hatSwitchSpeed: " + s.getPartSwitchSpeed(LayerTypes.H));
			saver.println("jacketSwitchSpeed: " + s.getPartSwitchSpeed(LayerTypes.J));
			saver.println("capeSwitchSpeed: " + s.getPartSwitchSpeed(LayerTypes.CA));
			saver.println("leftArmSwitchSpeed: " + s.getPartSwitchSpeed(LayerTypes.LA));
			saver.println("rightArmSwitchSpeed: " + s.getPartSwitchSpeed(LayerTypes.RA));
			saver.println("leftLegSwitchSpeed: " + s.getPartSwitchSpeed(LayerTypes.LL));
			saver.println("rightLegSwitchSpeed: " + s.getPartSwitchSpeed(LayerTypes.RL));
			saver.println("**");
			saver.println("hatBlinkDelay: " + s.getPartBlinkSpeed(LayerTypes.H));
			saver.println("jacketBlinkDelay: " + s.getPartBlinkSpeed(LayerTypes.J));
			saver.println("capeBlinkDelay: " + s.getPartBlinkSpeed(LayerTypes.CA));
			saver.println("leftArmBlinkDelay: " + s.getPartBlinkSpeed(LayerTypes.LA));
			saver.println("rightArmBlinkDelay: " + s.getPartBlinkSpeed(LayerTypes.RA));
			saver.println("leftLegBlinkDelay: " + s.getPartBlinkSpeed(LayerTypes.LL));
			saver.println("rightLegBlinkDelay: " + s.getPartBlinkSpeed(LayerTypes.RL));
			saver.println("**");
			saver.println("hatBlinkDuration: " + s.getPartBlinkDuration(LayerTypes.H));
			saver.println("jacketBlinkDuration: " + s.getPartBlinkDuration(LayerTypes.J));
			saver.println("capeBlinkDuration: " + s.getPartBlinkDuration(LayerTypes.CA));
			saver.println("leftArmBlinkDuration: " + s.getPartBlinkDuration(LayerTypes.LA));
			saver.println("rightArmBlinkDuration: " + s.getPartBlinkDuration(LayerTypes.RA));
			saver.println("leftLegBlinkDuration: " + s.getPartBlinkDuration(LayerTypes.LL));
			saver.println("rightLegBlinkDuration: " + s.getPartBlinkDuration(LayerTypes.RL));
			saver.println("**");
			saver.println("hatOffset: " + s.getPartOffset(LayerTypes.H));
			saver.println("jacketOffset: " + s.getPartOffset(LayerTypes.J));
			saver.println("capeOffset: " + s.getPartOffset(LayerTypes.CA));
			saver.println("leftArmOffset: " + s.getPartOffset(LayerTypes.LA));
			saver.println("rightArmOffset: " + s.getPartOffset(LayerTypes.RA));
			saver.println("leftLegOffset: " + s.getPartOffset(LayerTypes.LL));
			saver.println("rightLegOffset: " + s.getPartOffset(LayerTypes.RL));
			saver.println("**");
			saver.println("hatFlipped: " + s.isPartStateFlipped(LayerTypes.H));
			saver.println("jacketFlipped: " + s.isPartStateFlipped(LayerTypes.J));
			saver.println("capeFlipped: " + s.isPartStateFlipped(LayerTypes.CA));
			saver.println("leftArmFlipped: " + s.isPartStateFlipped(LayerTypes.LA));
			saver.println("rightArmFlipped: " + s.isPartStateFlipped(LayerTypes.RA));
			saver.println("leftLegFlipped: " + s.isPartStateFlipped(LayerTypes.LL));
			saver.println("rightLegFlipped: " + s.isPartStateFlipped(LayerTypes.RL));
			saver.print("END");
			saver.close();
		} catch (FileNotFoundException | UnsupportedEncodingException e) { e.printStackTrace(); }
	}
}