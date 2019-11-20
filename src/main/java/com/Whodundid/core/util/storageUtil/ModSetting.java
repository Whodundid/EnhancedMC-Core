package com.Whodundid.core.util.storageUtil;

/**
 * Essentially, a boolean holder so that boolean values can be modified on a 'pass by reference' basis.
 * Defaults to true.
 * @author Hunter Bragg
 *
 */
public class ModSetting {
	
	private boolean val = true;
	
	public ModSetting() { this(true); }
	public ModSetting(boolean initialValue) { val = initialValue; }
	
	public ModSetting set(boolean valIn) { val = valIn; return this; }
	public boolean get() { return val; }
}
