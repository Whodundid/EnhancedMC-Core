package com.Whodundid.hotkeys.control.hotKeyUtil;

import com.Whodundid.core.util.storageUtil.EArrayList;
import com.Whodundid.hotkeys.HotKeyApp;
import com.Whodundid.hotkeys.control.HotKey;
import net.minecraft.client.Minecraft;

//Last edited: Sep 30, 2018
//First Added: Sep 25, 2018
//Author: Hunter Bragg

public abstract class SubModHotKeys {
	
	protected static Minecraft mc = Minecraft.getMinecraft();
	protected String subModName = "";
	protected EArrayList<HotKey> keys = new EArrayList();
	protected HotKeyApp man;
	
	public void registerKeys() {
		for (HotKey k : keys) { man.registerHotKey(k); }
	}
	
	public SubModHotKeys rebuildKeys() {
		keys.clear();
		addKeys();
		return this;
	}
	
	public EArrayList<HotKey> getKeys() { return keys; }
	
	protected abstract void addKeys();
}
