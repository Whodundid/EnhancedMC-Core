package com.Whodundid.core.util.playerUtil;

//Author: Hunter Bragg

public abstract class PlayerLimits {
	
	public static final float maxVerticalJumpHeight = 1.249187078744681f;
	public static final int maxVerticalBlockJumpHeight = 1;
	public static final float maxHorizontalJumpDistance = 3.148553262444352f;
	public static final int maxHorizontalBlockJumpDistance = 4;
	
	//momentums
	public static final float maxWalkMomentum = 0.11785904894762855f;
	public static final float maxWalkJumpMomentum = 0.18215586373616366f;
	public static final float maxDiagWalkMomentum = 0.08326698902208074f;
	public static final float maxDiagWalkJumpMomentum = 0.12869245388055234f;
	
	public static final float maxSprintMomentum = 0.15321675646360336f;
	public static final float maxSprintJumpMomentum = 0.33425207448130040f; //it takes 20 consecutive jumps to achieve this
	public static final float maxDiagSprintMomentum = 0.10834060354973962f;
	public static final float maxDiagSprintJumpMomentum = 0.23635190926240520f;
	
	public static final float maxFlyMomentum = 0.49544462780531000f; //literally half of sprint fly
	public static final float maxSprintFlyMomentum = 0.99088925561062000f;
	public static final float maxDiagFlyMomentum = 0.35033223737703840f;
	public static final float maxDiagSprintFlyMomentum = 0.70073174772351310f;
	
	
	//@ 0.535
	//3.119432377849165  //1.07
	//3.139713621072456  //1.73
	//3.132653257608581  //1.33
	//3.139871858795672  //1.60
	//3.095217344540714  //0.93
	//3.0952183151682817 //0.86
	//3.5926143107868143 //1.11
	//3.119432377849165  //1.1226
	
}
