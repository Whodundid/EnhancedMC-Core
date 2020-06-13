package com.Whodundid.core.windowLibrary.windowObjects.actionObjects;

import com.Whodundid.core.util.storageUtil.StorageBox;
import com.Whodundid.core.windowLibrary.windowTypes.ActionObject;
import com.Whodundid.core.windowLibrary.windowTypes.interfaces.IWindowObject;
import com.Whodundid.core.windowLibrary.windowUtil.windowEvents.events.EventFocus;
import java.text.DecimalFormat;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.MathHelper;

//Author: Hunter Bragg

public class WindowSlider extends ActionObject {

	protected float sliderValue = 0;
	public float lowVal = 0, highVal = 0;
	protected float pos = 0.0f;
	protected boolean vertical;
	protected boolean isSliding = false;
	protected boolean defaultDisplayString = true;
	protected boolean drawDisplayString = true;
	protected double interval = 0;
	public String displayValue = "";
	public int displayValueColor = 0x00ff00;
	public int thumbSize = 8;
	private int thumbStartX = 0, thumbStartY = 0;
	private int thumbEndX = 0, thumbEndY = 0;
	public float defaultVal = 0;
	protected boolean continuouslyRunAction = true;
	protected StorageBox<Integer, Integer> mousePos = new StorageBox();
	
	public WindowSlider(IWindowObject parentIn, int xIn, int yIn, int widthIn, int heightIn, float lowValIn, float highValIn, boolean verticalIn) {
		this(parentIn, xIn, yIn, widthIn, heightIn, lowValIn, highValIn, 0.0f, verticalIn);
	}
	
	public WindowSlider(IWindowObject parentIn, int xIn, int yIn, int widthIn, int heightIn, float lowValIn, float highValIn, float startVal, boolean verticalIn) {
		super(parentIn);
		init(parentIn, xIn, yIn, widthIn, heightIn);
		lowVal = lowValIn;
		highVal = highValIn;
		vertical = verticalIn;
		defaultVal = startVal;
		
		setThumb();
		thumbSize = vertical ? height / 9 : width / 9;
		
		setSliderValue(defaultVal);
	}
	
	private void setThumb() {
		if (vertical) {
			thumbStartX = startX + 1;
			thumbStartY = startY + 1;
			thumbEndX = endX;
			thumbEndY = startY + thumbSize;
		}
		else {
			thumbStartX = startX + 1;
			thumbStartY = startY + 1;
			thumbEndX = startX + thumbSize;
			thumbEndY = endY - 1;
		}
	}
	
	@Override
	public void drawObject(int mX, int mY) {
		if (isSliding && mousePos != null && mousePos.getObject() != null && mousePos.getValue() != null) {
			if (vertical) { moveThumb(0, mY - mousePos.getValue()); }
			else { moveThumb(mX - mousePos.getObject(), 0); }
			mousePos.setValues(mX, mY);
		}
		
		drawRect(startX, startY, endX, endY, 0xff000000); //black border
		drawRect(startX + 1, startY + 1, endX - 1, endY - 1, 0xff666666); //gray inner
		
		drawRect(thumbStartX, thumbStartY, thumbEndX, thumbEndY, (isMouseInThumb(mX, mY) || isSliding) ? 0xffffffff : 0xffbbbbbb); //thumb
		
		if (defaultDisplayString) { displayValue = new DecimalFormat("0.00").format(sliderValue); }
		
		if (vertical && mc.fontRendererObj.getStringWidth(displayValue) > width) {
			GlStateManager.pushMatrix();
			int xPos = midX;
			int yPos = midY;
			GlStateManager.translate(xPos, yPos, 0);
			GlStateManager.rotate(90f, 0f, 0f, 45f);
			GlStateManager.translate(-xPos, -yPos, 0);
			
			if (drawDisplayString) { drawCenteredString(displayValue, midX, midY - mc.fontRendererObj.FONT_HEIGHT / 2 + 1, displayValueColor); }
			GlStateManager.popMatrix();
		}
		else if (drawDisplayString) { drawCenteredString(displayValue, midX, midY - mc.fontRendererObj.FONT_HEIGHT / 2 + 1, displayValueColor); }
		
		super.drawObject(mX, mY);
	}
	
	@Override
	public WindowSlider resetPosition() {
		super.resetPosition();
		setSliderValue(sliderValue);
		return this;
	}
	
	@Override
	public void move(int newX, int newY) {
		thumbStartX += newX;
		thumbStartY += newY;
		thumbEndX += newX;
		thumbEndY += newY;
		super.move(newX, newY);
	}
	
	@Override
	public WindowSlider setPosition(int newX, int newY) {
		super.setPosition(newX, newY);
		setSliderValue(sliderValue);
		return this;
	}
	
	@Override
	public void mousePressed(int mX, int mY, int button ) {
		if (button == 0) {
			if (isMouseInThumb(mX, mY)) {
				isSliding = true;
				mousePos.setValues(mX, mY);
			}
			else {
				calculateSliderPos(true);
				isSliding = true;
			}
		}
		else if (button == 1) {
			if (isMouseInThumb(mX, mY)) {
				WindowButton.playPressSound();
				reset();
			}
		}
		super.mousePressed(mX, mY, button);
	}
	
	@Override
	public void mouseReleased(int mX, int mY, int button) {
		isSliding = false;
		super.mouseReleased(mX, mY, button);
	}
	
	@Override
	public void onFocusLost(EventFocus eventIn) {
		isSliding = false;
	}
		
	//------------------
	//EGuiSlider methods
	//------------------
	
	private void moveThumb(int newX, int newY) {
		if (vertical) {
			if (!(thumbStartY + newY < startY) && (thumbEndY + newY < endY + 1)) {
				thumbStartY += newY;
				thumbEndY += newY;
			}
			else {
				if (newY < 0) {
					thumbStartY = startY;
					thumbEndY = startY + thumbSize;
				}
				else if (newY > 0) {
					thumbStartY = endY - thumbSize;
					thumbEndY = endY;
				}
			}
		}
		else {
			if (!(thumbStartX + newX < startX + 1) && (thumbEndX + newX < endX)) {
				thumbStartX += newX;
				thumbEndX += newX;
			}
			else {
				if (newX < 0) {
					thumbStartX = startX + 1;
					thumbEndX = startX + thumbSize;
				}
				else if (newX > 0) {
					thumbStartX = endX - thumbSize;
					thumbEndX = endX - 1;
				}
			}
		}
		calculateSliderPos(true);
	}
	
	public WindowSlider setSliderValue(float valIn) {
		sliderValue = valIn;
		pos = MathHelper.clamp_float((valIn - lowVal) / (highVal - lowVal), 0.0f, 1.0f);
		if (defaultDisplayString) { displayValue = new DecimalFormat("0.00").format(sliderValue); }
		if (valIn >= lowVal && valIn <= highVal) { calculateSliderPos(false); }
		else if (valIn > highVal) {
			if (vertical) {
				thumbStartX = startX + 1;
				thumbStartY = endY - thumbSize - 1;
				thumbEndX = endX - 1;
				thumbEndY = thumbStartY + thumbSize;
			}
			else {
				thumbStartX = endX - thumbSize - 1;
				thumbStartY = startY + 1;
				thumbEndX = thumbStartX + thumbSize;
				thumbEndY = endY - 1;
			}
		}
		else if (valIn < lowVal) {
			thumbStartX = startX + 1;
			thumbStartY = startY + 1;
			if (vertical) {
				thumbEndX = endX - 1;
				thumbEndY = thumbStartY + thumbSize;
			}
			else {
				thumbEndX = thumbStartX + thumbSize;
				thumbEndY = endY - 1;
			}
		}
		return this;
	}
	
	private void calculateSliderPos(boolean calc) {
		if (calc) {
			if (vertical) {
				pos = MathHelper.clamp_float((float)(mY - startY - thumbSize / 2) / (height - thumbSize - 1), 0f, 1f);
				sliderValue = lowVal + (highVal - lowVal) * (1 + -pos);
			}
			else {
				pos = MathHelper.clamp_float((float)(mX - startX - thumbSize / 2) / (width - thumbSize), 0f, 1f);
				sliderValue = lowVal + (highVal - lowVal) * pos;
			}
			if (defaultDisplayString) { displayValue = new DecimalFormat("0.00").format(sliderValue); }
		}
		if (vertical) {
			thumbStartX = startX + 1;
			thumbStartY = startY + 1 + (int)(Math.ceil(pos * (height - thumbSize - 2)));
			thumbEndX = endX - 1;
			thumbEndY = thumbStartY + thumbSize;
		}
		else {
			thumbStartX = startX + 1 + (int)(Math.ceil(pos * (width - thumbSize - 2)));
			thumbStartY = startY + 1;
			thumbEndX = thumbStartX + thumbSize;
			thumbEndY = endY - 1;
		}
		
		if (continuouslyRunAction) { performAction(this, getSliderValue()); }
	}
	
	public WindowSlider reset() { setSliderValue(defaultVal); return this; }
	public WindowSlider setDisplayString(String valIn) { displayValue = valIn; return this; }
	public WindowSlider setDisplayValueColor(int colorIn) { displayValueColor = colorIn; return this; }
	public WindowSlider setThumbSize(int sizeIn) { thumbSize = sizeIn; return this; }
	public WindowSlider setHighVal(int valIn) { highVal = valIn; return this; }
	public WindowSlider setLowVal(int valIn) { lowVal = valIn; return this; }
	public WindowSlider setDrawDefault(boolean valIn) { defaultDisplayString = valIn; return this; }
	public WindowSlider setDrawDisplayString(boolean valIn) { drawDisplayString = valIn; return this; }
	public WindowSlider setContinuouslyRunAction(boolean val) { continuouslyRunAction = val; return this; }
	
	public float getSliderValue() { return sliderValue; }
	public boolean drawVertical() { return vertical; }
	public int getThumbSize() { return thumbSize; }
	public float getLowVal() { return lowVal; }
	public float getHighVal() { return highVal; }
	public float getDefaultVal() { return defaultVal; }
	public boolean drawDefault() { return defaultDisplayString; }
	public boolean drawDisplayString() { return drawDisplayString; }
	public boolean isMouseInThumb(int mX, int mY) { return mX >= thumbStartX && mX <= thumbEndX && mY >= thumbStartY && mY <= thumbEndY; }
	public boolean getContinuouslyRunAction() { return continuouslyRunAction; }
	
}
