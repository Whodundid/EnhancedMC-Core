package com.Whodundid.core.enhancedGui.guiObjects.windows;

import com.Whodundid.core.enhancedGui.guiObjects.advancedObjects.header.EGuiHeader;
import com.Whodundid.core.enhancedGui.guiObjects.basicObjects.EGuiButton;
import com.Whodundid.core.enhancedGui.guiObjects.basicObjects.EGuiSlider;
import com.Whodundid.core.enhancedGui.types.WindowParent;
import com.Whodundid.core.enhancedGui.types.interfaces.IEnhancedActionObject;
import com.Whodundid.core.enhancedGui.types.interfaces.IEnhancedGuiObject;
import com.Whodundid.core.util.renderUtil.HSLColor;
import com.Whodundid.core.util.storageUtil.EArrayList;

public class EGuiColorPicker extends WindowParent {

	int color = 0x00000000;
	boolean enableTransparency = true;
	EGuiButton select, cancel;
	EGuiSlider rSlider, gSlider, bSlider, aSlider, hSlider, sSlider, vSlider;
	EArrayList<Integer> list = new EArrayList();
	int div = 16;
	int sX = 0;
	int sY = 0;
	int eX = 0;
	int eY = 0;
	
	public EGuiColorPicker(IEnhancedGuiObject parentIn, int xIn, int yIn) {
		init(parentIn, xIn, yIn, 200, 200);
	}
	
	@Override
	public void initGui() {
		createPallete();
	}
	
	@Override
	public void initObjects() {
		setHeader(new EGuiHeader(this));
		header.setTitle("Color Picker");
	}
	
	@Override
	public void drawObject(int mXIn, int mYIn, float ticks) {
		super.drawObject(mXIn, mYIn, ticks);
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
				
				if (pass1) {
					gI++;
					if (gI == 255) { pass1 = false; pass2 = true; }
				}
				else if (pass2) {
					rI--;
					if (rI == 0) { pass2 = false; pass3 = true; }
				}
				else if (pass3) {
					bI++;
					if (bI == 255) { pass3 = false; pass4 = true; }
				}
				else if (pass4) {
					gI--;
					if (gI == 0) { pass4 = false; pass5 = true; }
				}
				else if (pass5) {
					rI++;
					if (rI == 255) { pass5 = false; pass6 = true; }
				}
				else if (pass6) {
					bI--;
					if (bI == 0) { pass6 = false; break; }
				}
			}
		} catch (Exception e) { e.printStackTrace(); }
	}
	
	private void drawPallete() {
		try {
			sX = startX + 15;
			sY = startY + 15;
			eX = sX + (list.size() / div) + 4;
			eY = sY + (list.size() / div) + 4;
			drawRect(sX - 1, sY - 1, eX, eY + 1, 0xff000000);
			
			for (int i = 0; i < list.size() / div; i++) {
				int c = 0xff000000 + list.get(i * div);
				drawRect(sX + (i * 1), sY, sX + ((i * 1) + 4), eY, c);
			}
			
			//EArrayList<Integer> blackList = new EArrayList();
			//for (int i = 0; i < 255; i++) {
			//	String bC = Integer.toString(i, 16);
			//	if (bC.length() == 1) { bC = ("0" + bC); }
			//	blackList.add(Integer.parseInt("0000" + bC, 16));
			//}
			
			//for (int i = 1; i < blackList.size() / 2; i++) {
			//	int blackTint = (blackList.get(i * 2) << 24);
			//	drawRect(sX, eY - (i * 1), eX - 1, eY - (i * 1) + 1, -blackTint);
			//	//drawString("" + i + " " + w + " " + blackTint, midX + 20, startX - 600 + (i * 10), 0xff000000);
			//}
			
			HSLColor colorTest = new HSLColor(69, 0.81, 0.47);
			//0xff000000 + colorTest.getRGB()
			drawRect (midX + 50, midY + 50, endX - 5, endY - 5, 0xff000000 + colorTest.getRGB());
			//colorTest.getRGB();
			
		} catch (Exception e) { e.printStackTrace(); }
	}
	
	private int getColorAtMouse(int mXIn, int mYIn) {
		return 0x00000000;
	}
	
	@Override
	public void actionPerformed(IEnhancedActionObject object) {
		if (object == select) { getParent().actionPerformed(object); }
		if (object == cancel) { close(); }
	}
	
	public int getColor() { return color; }
	
	
}
