package com.Whodundid.worldEditor.EditorScripts;

import com.Whodundid.core.app.AppType;
import com.Whodundid.core.app.RegisteredApps;
import com.Whodundid.core.util.storageUtil.EArrayList;
import com.Whodundid.scripts.EScript;
import com.Whodundid.scripts.ScriptingApp;
import com.Whodundid.worldEditor.EditorApp;

public class EditorScript_Paste extends EScript {
	
	
	ScriptingApp scriptMan = (ScriptingApp) RegisteredApps.getApp(AppType.SCRIPTS);
	EditorApp editor = null;
	boolean hasArg = false;
	static volatile boolean running = false;
	
	public EditorScript_Paste(EditorApp editorIn) {
		super("editor_paste", new EArrayList());
		editor = editorIn;
	}

	public void startScript(String[] args) {
		/*
		if (!running) {
			if (args != null) { hasArg = args[0].equals("-a"); }
			if (editor.isEditorOpen() && editor.getCopyPosition() != null) {
				Vector3D currentPosition = new Vector3D(PlayerTraits.getPlayerLocation());
				double distX = editor.getPos1().getX() - editor.getCopyPosition().getX();
				double distY = editor.getPos1().getY() - editor.getCopyPosition().getY();
				double distZ = editor.getPos1().getZ() - editor.getCopyPosition().getZ();
				
				Thread runner = new Thread() {
					@Override
					public void run() {
						running = true;
						int i = 0;
						refreshTime =  System.currentTimeMillis();
						while (i < 3 && !scriptMan.getGlobalKill()) {
							if (System.currentTimeMillis() - refreshTime >= 100) {
								switch (i) {
								case 0: mc.thePlayer.sendChatMessage("/tp " + (editor.getPlayerCopyPosition().getX() + distX) + " " +
										   (editor.getPlayerCopyPosition().getY() + distY) + " " +
										   (editor.getPlayerCopyPosition().getZ() + distZ));
										break;
								case 1: mc.thePlayer.sendChatMessage((hasArg) ? "//paste -a" : "//paste"); break;
								case 2: mc.thePlayer.sendChatMessage("/tp " + currentPosition.getX() + " " + currentPosition.getY() + " " + currentPosition.getZ()); break;
								}
								i++;
								refreshTime = System.currentTimeMillis();
							}
						}
						running = false;
					}
				};
				runner.start();
			}
		} else {
			
		}
		*/
	}
}
