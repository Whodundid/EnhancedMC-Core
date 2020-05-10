package com.Whodundid.guiCreator.util;

import net.minecraft.util.ResourceLocation;

public class CreatorResources {

	//--------
	//textures
	//--------
	
	//modes
	public static final ResourceLocation play;
	public static final ResourceLocation stop;
	
	//creator
	public static final ResourceLocation select;
	public static final ResourceLocation move;
	public static final ResourceLocation resize;
	public static final ResourceLocation eyedropper;
	
	//shape
	public static final ResourceLocation line;
	public static final ResourceLocation square;
	public static final ResourceLocation circle;
	
	//action
	public static final ResourceLocation btn;
	public static final ResourceLocation btn3;
	public static final ResourceLocation check;
	public static final ResourceLocation radio;
	public static final ResourceLocation slider;
	public static final ResourceLocation textbox;
	
	//basic
	public static final ResourceLocation header;
	public static final ResourceLocation container;
	public static final ResourceLocation imgbox;
	public static final ResourceLocation label;
	
	//advanced
	public static final ResourceLocation list;
	public static final ResourceLocation tlist;
	public static final ResourceLocation dropdown;
	public static final ResourceLocation pviewer;
	public static final ResourceLocation progress;
	
	static {
		
		//--------
		//textures
		//--------
		
		//modes
		play = new ResourceLocation("guicreator", "creator_play.png");
		stop = new ResourceLocation("guicreator", "creator_stop.png");
		
		//creator
		select = new ResourceLocation("guicreator", "creator_select.png");
		move = new ResourceLocation("guicreator", "creator_move.png");
		resize = new ResourceLocation("guicreator", "creator_resize.png");
		eyedropper = new ResourceLocation("guicreator", "creator_eyedropper.png");
		
		//shape
		line = new ResourceLocation("guicreator", "creator_line.png");
		square = new ResourceLocation("guicreator", "creator_square.png");
		circle = new ResourceLocation("guicreator", "creator_circle.png");
		
		//action
		btn = new ResourceLocation("guicreator", "creator_btn.png");
		btn3 = new ResourceLocation("guicreator", "creator_btn3.png");
		check = new ResourceLocation("guicreator", "creator_check.png");
		radio = new ResourceLocation("guicreator", "creator_radio.png");
		slider = new ResourceLocation("guicreator", "creator_slider.png");
		textbox = new ResourceLocation("guicreator", "creator_textbox.png");
		
		//basic
		header = new ResourceLocation("guicreator", "creator_header.png");
		container = new ResourceLocation("guicreator", "creator_container.png");
		imgbox = new ResourceLocation("guicreator", "creator_imgbox.png");
		label = new ResourceLocation("guicreator", "creator_label.png");
		
		//advanced
		list = new ResourceLocation("guicreator", "creator_list.png");
		tlist = new ResourceLocation("guicreator", "creator_tlist.png");
		dropdown = new ResourceLocation("guicreator", "creator_dropdown.png");
		pviewer = new ResourceLocation("guicreator", "creator_pviewer.png");
		progress = new ResourceLocation("guicreator", "creator_progress.png");
	}
	
}
