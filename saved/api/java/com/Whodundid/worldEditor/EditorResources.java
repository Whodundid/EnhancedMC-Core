package com.Whodundid.worldEditor;

import net.minecraft.util.ResourceLocation;

public class EditorResources {
	
	//textures
	public static final ResourceLocation editorToolPics;
	public static final ResourceLocation editorPanU;
	public static final ResourceLocation editorPanG;
	public static final ResourceLocation editorOrbit;
	
	static {
		//textures
		editorToolPics = new ResourceLocation("worldeditor", "editorToolPics.png");
		editorPanU = new ResourceLocation("worldeditor", "editor_panU.png");
		editorPanG = new ResourceLocation("worldeditor", "editor_panG.png");
		editorOrbit = new ResourceLocation("worldeditor", "editor_orbit.png");
	}

}
