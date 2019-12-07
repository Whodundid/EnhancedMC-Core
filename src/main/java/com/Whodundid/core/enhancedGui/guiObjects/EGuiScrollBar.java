package com.Whodundid.core.enhancedGui.guiObjects;

import com.Whodundid.core.enhancedGui.guiUtil.events.EventFocus;
import com.Whodundid.core.enhancedGui.types.EnhancedActionObject;
import com.Whodundid.core.enhancedGui.types.interfaces.IEnhancedGuiObject;
import com.Whodundid.core.util.renderUtil.ScreenLocation;
import com.Whodundid.core.util.storageUtil.EDimension;
import com.Whodundid.core.util.storageUtil.StorageBox;
import net.minecraft.util.MathHelper;

//Jan 9, 2019
//Jan 21, 2019
//Last edited: Jan 22, 2019
//First Added: Oct 3, 2018
//Author: Hunter Bragg

public class EGuiScrollBar extends EnhancedActionObject { //CHANGE TO ACTION OBJECT!
	
	public boolean vertical = false;
	public int scrollBarThickness = 3;
	public int thumbSize = 50;
	public int scrollPos = 0;
	protected int lastScrollChange = 0;
	public int highVal = 0;
	protected double interval = 0;
	protected int blockIncrement = 0;
	protected int visibleAmount = 0;
	protected int thumbStartX = 0, thumbStartY = 0;
	protected int thumbEndX = 0, thumbEndY = 0;
	public boolean isScrolling = false;
	public boolean renderThumb = true;
	private StorageBox<Integer, Integer> mousePos = new StorageBox(0, 0);
	
	public EGuiScrollBar(IEnhancedGuiObject parentIn, int visibleAmountIn, int highValIn) {
		this(parentIn, visibleAmountIn, highValIn, -1, -1, ScreenLocation.right); 
	}
	public EGuiScrollBar(IEnhancedGuiObject parentIn, int visibleAmountIn, int highValIn, int widthIn, int heightIn) {
		this(parentIn, visibleAmountIn, highValIn, widthIn, heightIn, ScreenLocation.right);
	}
	public EGuiScrollBar(IEnhancedGuiObject parentIn, int visibleAmountIn, int highValIn, ScreenLocation sideIn) {
		this(parentIn, visibleAmountIn, highValIn, -1, -1, sideIn);
	}
	public EGuiScrollBar(IEnhancedGuiObject parentIn, int visibleAmountIn, int highValIn, int widthIn, int heightIn, ScreenLocation sideIn) {
		EDimension dim = parentIn.getDimensions();
		
		if (sideIn == ScreenLocation.top || sideIn == ScreenLocation.bot) { vertical = false; }
		else { vertical = true; }
		
		int sWidth = vertical ? (widthIn < 0 ? scrollBarThickness : widthIn) : (widthIn < 0 ? dim.width - 2 : widthIn);
		int sHeight = vertical ? (heightIn < 0 ? dim.height - 2 : heightIn) : (heightIn < 0 ? scrollBarThickness : heightIn);

		
		switch (sideIn) {
		case top: init(parentIn, dim.startX + 1, dim.startY + 1, sWidth, sHeight); break;
		case bot: init(parentIn, dim.startX + 1, dim.endY - scrollBarThickness - 1, sWidth, sHeight); break;
		case right: init(parentIn, dim.endX - scrollBarThickness - 1, dim.startY + 1, sWidth, sHeight); break;
		case left: init(parentIn, dim.startX + 1, dim.startY + 1, sWidth, sHeight); break;
		default: init(parentIn, dim.endX - scrollBarThickness - 1, dim.startY + 1, sWidth, sHeight); break;
		}
		
		setThumb();
		
		setScrollBarValues(visibleAmountIn, highValIn, vertical ? height : width);
	}
	
	private void setThumb() {
		if (vertical) {
			thumbStartX = endX - scrollBarThickness;
			thumbStartY = startY;
			thumbEndX = endX;
			thumbEndY = startY + thumbSize;
		} else {
			thumbStartX = startX;
			thumbStartY = startY;
			thumbEndX = startX + thumbSize;
			thumbEndY = startY + scrollBarThickness;
		}
	}
	
	@Override
	public EGuiScrollBar resetPosition() {
		super.resetPosition();
		setScrollBarPos(scrollPos);
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
	public EGuiScrollBar setPosition(int newX, int newY) {
		super.setPosition(newX, newY);
		setScrollBarPos(scrollPos);
		return this;
	}
	
	@Override
	public void drawObject(int mX, int mY, float ticks) {
		if (isScrolling && mousePos != null && mousePos.getObject() != null && mousePos.getValue() != null) {
			if (vertical && mY - mousePos.getValue() != 0) { moveThumb(0, mY - mousePos.getValue()); }
			else if (mX - mousePos.getObject() != 0) { moveThumb(mX - mousePos.getObject(), 0); }
			mousePos.setValues(mX, mY);
		}
		drawRect(startX, startY, startX + width, startY + height, 0xff666666);
		if (renderThumb) { drawRect(thumbStartX, thumbStartY, thumbEndX, thumbEndY, (isMouseInThumb(mX, mY) || isScrolling) ? 0xffffffff : 0xffbbbbbb); }
		super.drawObject(mX, mY, ticks);
	}
	
	@Override
	public void mousePressed(int mX, int mY, int button) {
		super.mousePressed(mX, mY, button);
		if (isMouseInThumb(mX, mY)) {
			isScrolling = true;
			mousePos.setValues(mX, mY);
		}
	}
	
	@Override
	public void mouseReleased(int mX, int mY, int button) {
		isScrolling = false;
		super.mouseReleased(mX, mY, button);
	}
	
	@Override
	public void onFocusLost(EventFocus eventIn) {
		isScrolling = false;
	}
	
	private void moveThumb(int newX, int newY) {
		if (vertical) {
			if (!(thumbStartY + newY < startY) && (thumbEndY + newY < endY + 1)) {
				thumbStartY += newY;
				thumbEndY += newY;
			} else {
				if (newY < 0) {
					thumbStartY = startY;
					thumbEndY = startY + thumbSize;
				} else if (newY > 0) {
					thumbStartY = endY - thumbSize;
					thumbEndY = endY;
				}
			}
		} else {
			if (!(thumbStartX + newX < startX + 1) && (thumbEndX + newX < endX)) {
				thumbStartX += newX;
				thumbEndX += newX;
			} else {
				if (newX < 0) {
					thumbStartX = startX;
					thumbEndX = startX + thumbSize;
				} else if (newX > 0) {
					thumbStartX = endX - thumbSize;
					thumbEndX = endX;
				}
			}
		}
		calculateScrollPos();
		lastScrollChange = vertical ? newY : newX;
		getActionReciever().actionPerformed(this);
	}
	
	public boolean isMouseInThumb(int mX, int mY) {
		return (mX >= thumbStartX && mX <= thumbEndX && mY >= thumbStartY && mY <= thumbEndY);
	}
	
	public EGuiScrollBar setScrollBarPos(int pos) {
		//lastScrollChange = scrollPos + -;
		pos = pos < visibleAmount ? visibleAmount : pos;
		pos = pos > highVal ? highVal : pos;
		//System.out.println("pos pos: " + pos);
		scrollPos = pos;
		if (vertical) {
			thumbStartX = startX;
			thumbStartY = (int) (startY + (scrollPos - visibleAmount) * interval);
			thumbEndX = endX;
			thumbEndY = thumbStartY + thumbSize;
		} else {
			thumbStartX = (int) (startX + (scrollPos - visibleAmount) * interval);
			thumbStartY = startY;
			thumbEndX = thumbStartX + thumbSize;
			thumbEndY = endY;
		}
		getActionReciever().actionPerformed(this);
		return this;
	}
	
	private void calculateScrollPos() {
		double relativeThumbPos = 0;
		if (vertical) {
			relativeThumbPos = MathHelper.clamp_double((double)(thumbStartY - startY) / (height - thumbSize), 0f, 1f);
		}
		else {
			relativeThumbPos = MathHelper.clamp_double((double)(thumbStartX - startX) / (width - thumbSize), 0f, 1f);
		}
		double val = visibleAmount + (highVal - visibleAmount) * relativeThumbPos;
		scrollPos = (int) val;
	}
	
	private void recalculateInterval() {
		int dist = highVal - visibleAmount;
		if (dist > 0) {
			interval = vertical ? ((double) height - (double) thumbSize) / dist : ((double) width - (double) thumbSize) / dist;
		} else {
			interval = 0;
		}
	}
	
	private void setScrollBarValues(int visibleAmountIn, int highValIn, int visibleSize) {
		visibleAmount = visibleAmountIn;
		highVal = Math.max(highValIn, visibleAmount);
		double val = 1.0;
		if (highVal > visibleAmount) { val = (double) visibleAmount / (double) highVal; }
		thumbSize = (int) ((vertical ? height : width) * val);
		thumbSize = (int) Math.max(thumbSize, height * 0.125);
		thumbSize = Math.max(thumbSize, 1);
		thumbSize = Math.min(thumbSize, vertical ? height : width);
		blockIncrement = Math.max((int)(visibleSize * 0.9), 1);
		recalculateInterval();
		setScrollBarPos(scrollPos);
	}
	
	public EGuiScrollBar reset() { setScrollBarPos(0); return this; }
	public EGuiScrollBar setRenderThumb(boolean val) { renderThumb = val; return this; }
	public EGuiScrollBar setVisibleAmount(int sizeIn) { visibleAmount = sizeIn; setScrollBarValues(visibleAmount, highVal, vertical ? height : width); return this; }
	public EGuiScrollBar setHighVal(int valIn) { setScrollBarValues(visibleAmount, valIn, drawVertical() ? height : width); return this; }
	public EGuiScrollBar setLowVal(int valIn) { setScrollBarValues(valIn, highVal, drawVertical() ? height : width); return this; }
	
	public boolean drawVertical() { return vertical; }
	public boolean isThumbRendered() { return renderThumb; }
	public int getLastScrollChange() { return lastScrollChange; }
	public int getScrollBarThickness() { return scrollBarThickness; }
	public int getThumbSize() { return thumbSize; }
	public int getScrollPos() { return scrollPos; }
	public int getVisibleAmount() { return visibleAmount; }
	public int getHighVal() { return highVal; }
}
