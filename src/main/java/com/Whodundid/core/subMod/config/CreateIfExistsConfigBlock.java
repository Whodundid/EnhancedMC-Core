package com.Whodundid.core.subMod.config;

public class CreateIfExistsConfigBlock extends ConfigBlock {

	public String stringToCheckFor = "";
	
	public CreateIfExistsConfigBlock(String check, String... argsIn) {
		super(check, argsIn);
		stringToCheckFor = check;
	}
	
	public String getStringToCheckFor() { return stringToCheckFor; }
}
