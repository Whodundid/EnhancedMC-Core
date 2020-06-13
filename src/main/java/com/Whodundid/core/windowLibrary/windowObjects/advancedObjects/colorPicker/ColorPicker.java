package com.Whodundid.core.windowLibrary.windowObjects.advancedObjects.colorPicker;

import com.Whodundid.core.util.renderUtil.HSLColor;
import com.Whodundid.core.util.storageUtil.EArrayList;
import com.Whodundid.core.windowLibrary.windowObjects.actionObjects.WindowButton;
import com.Whodundid.core.windowLibrary.windowObjects.actionObjects.WindowSlider;
import com.Whodundid.core.windowLibrary.windowObjects.advancedObjects.header.WindowHeader;
import com.Whodundid.core.windowLibrary.windowTypes.WindowParent;
import com.Whodundid.core.windowLibrary.windowTypes.interfaces.IActionObject;
import com.Whodundid.core.windowLibrary.windowTypes.interfaces.IWindowObject;

//Author: Hunter Bragg

public class ColorPicker extends WindowParent {

	int color = 0x00000000;
	boolean enableTransparency = true;
	WindowButton select, cancel;
	WindowSlider rSlider, gSlider, bSlider, aSlider, hSlider, sSlider, vSlider;
	EArrayList<Integer> list = new EArrayList();
	int div = 16;
	int sX = 0;
	int sY = 0;
	int eX = 0;
	int eY = 0;

	public ColorPicker(IWindowObject parentIn, int xIn, int yIn) {
		init(parentIn, xIn, yIn, 200, 200);
		aliases.add("colorpicker");
	}

	@Override
	public void initWindow() {
		createPallete();
		setResizeable(true);
	}

	@Override
	public void initObjects() {
		setHeader(new WindowHeader(this));
		header.setTitle("Color Picker");
	}

	@Override
	public void drawObject(int mXIn, int mYIn) {
		super.drawObject(mXIn, mYIn);
		drawDefaultBackground();

		drawPallete();
	}

	@Override
	public void mousePressed(int mXIn, int mYIn, int button) {
		super.mousePressed(mXIn, mYIn, button);
	}

	private void createPallete() {
		try {
			int rI = 255;
			int gI = 0;
			int bI = 0;

			/*
			int red = 0;
			int green = 0;
			int blue = 0;
			int sections = 99;

			for (int y = 0; y < sections; y++) {
				for (int x = 0; x < sections; x++) {

					// red to yellow
					if (x < sections / 6) {
						red = 255;
						green = 255 * x * 6 / sections;
						blue = 0;

					// yellow to green
					} else if (x < 2 * sections / 6) {
						red = 255 - 255 * (x - sections / 6) * 6 / sections;
						green = 255;
						blue = 0;

					// green to cyan
					} else if (x < 3 * sections / 6) {
						green = 255;
						blue = 255 * (x - 2 * sections / 6) * 6 / sections;
						red = 0;

					// cyan to blue
					} else if (x < 4 * sections / 6) {
						blue = 255;
						green = 255 - 255 * (x - 3 * sections / 6) * 6 / sections;
						red = 0;

					// blue to magenta
					} else if (x < 5 * sections / 6) {
						blue = 255;
						red = 255 * (x - 4 * sections / 6) * 6 / sections;
						green = 0;

					// magenta to red
					} else {
						blue = 255 - 255 * (x - 5 * sections / 6) * 6 / sections;
						red = 255;
						green = 0;
					}
					
					list.add(new Color(red, green, blue, 255 - y * 255 / sections).getRGB());
				}
			}
			*/
			
			
			 boolean pass1 = true;
			 boolean pass2 = false;
			 boolean pass3 = false;
			 boolean pass4 = false;
			 boolean pass5 = false;
			 boolean pass6 = false;
			 
			 while (pass1 || pass2 || pass3 || pass4 || pass5 || pass6) {
				 String rC = Integer.toString(rI, 16);
				 String gC = Integer.toString(gI, 16);
				 String bC = Integer.toString(bI, 16);
				 if (rC.length() == 1) { rC = ("0" + rC); }
				 if (gC.length() == 1) { gC = ("0" + gC); }
				 if (bC.length() == 1) { bC = ("0" + bC); }
				 int c = Integer.parseInt(rC + gC + bC, 16);
			 
			 //System.out.println(c + " : " + rC + " " + gC + " " + bC + " : " + rI + " " + gI + " " + bI);
			 
			 list.add(c);
			 
			 if (pass1) { gI++; if (gI == 255) { pass1 = false; pass2 = true; } } else if
			 (pass2) { rI--; if (rI == 0) { pass2 = false; pass3 = true; } } else if
			 (pass3) { bI++; if (bI == 255) { pass3 = false; pass4 = true; } } else if
			 (pass4) { gI--; if (gI == 0) { pass4 = false; pass5 = true; } } else if
			 (pass5) { rI++; if (rI == 255) { pass5 = false; pass6 = true; } } else if
			 (pass6) { bI--; if (bI == 0) { pass6 = false; break; } } }
		}
		catch (Exception e) { e.printStackTrace(); }
	}

	private void drawPallete() {
		try {

			sX = startX + 15;
			sY = startY + 15;
			eX = sX + (list.size() / div) + 4;
			eY = sY + (list.size() / div) + 4;
			int drawHeight = eY - sY;
			
			//System.out.println(list.size());
			
			for (int j = 0; j < drawHeight; j++) {
				for (int i = 0; i < list.size() / 16; i++) {
					int c = 0xff000000 + list.get(i * div);
					drawRect(sX + i, sY + j, sX + i + 4, sY + j + 1, c);
				}
				//drawRect(sX, sY + j, eX, sY + j + 1, (j << 31));
			}

			HSLColor colorTest = new HSLColor(69, 0.81, 0.47);
			// 0xff000000 + colorTest.getRGB()
			drawRect(midX + 50, midY + 50, endX - 5, endY - 5, 0xff000000 + colorTest.getRGB());
			// colorTest.getRGB();

		}
		catch (Exception e) { e.printStackTrace(); }
	}

	private int getColorAtMouse(int mXIn, int mYIn) {
		return 0x00000000;
	}

	@Override
	public void actionPerformed(IActionObject object, Object... args) {
		if (object == select) {
			getParent().actionPerformed(object);
		}
		if (object == cancel) {
			close();
		}
	}

	public int getColor() {
		return color;
	}
	
}
