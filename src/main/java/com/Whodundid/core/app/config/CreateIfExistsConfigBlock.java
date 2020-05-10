package com.Whodundid.core.app.config;

//Author: Hunter Bragg

/** A special type of config block that is only created if the specified config line already exists in the file.
 *  In essence, these config lines will only be made if a user manually adds the specific line to the config file. */
public class CreateIfExistsConfigBlock extends ConfigBlock {

	public String stringToCheckFor = "";
	
	public CreateIfExistsConfigBlock(String check, String... argsIn) {
		super(check, argsIn);
		stringToCheckFor = check;
	}
	
	public String getStringToCheckFor() { return stringToCheckFor; }
}
