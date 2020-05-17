package com.Whodundid.core.notifications.window;

import com.Whodundid.core.EnhancedMC;
import com.Whodundid.core.enhancedGui.guiObjects.actionObjects.EGuiButton;
import com.Whodundid.core.enhancedGui.guiObjects.actionObjects.EGuiCheckBox;
import com.Whodundid.core.enhancedGui.guiObjects.advancedObjects.scrollList.EGuiScrollList;
import com.Whodundid.core.enhancedGui.guiObjects.basicObjects.EGuiLabel;
import com.Whodundid.core.enhancedGui.types.EnhancedGuiObject;
import com.Whodundid.core.enhancedGui.types.interfaces.IEnhancedActionObject;
import com.Whodundid.core.notifications.util.NotificationType;
import com.Whodundid.core.util.renderUtil.EColors;
import com.Whodundid.core.util.storageUtil.EDimension;

public class NoteSettingContainer extends EnhancedGuiObject {
	
	private EGuiScrollList parent;
	private NotificationType type;
	private int yPos = 0;
	private boolean drawOffset = false;
	private EGuiCheckBox button;
	private EGuiLabel label;
	
	public NoteSettingContainer(EGuiScrollList parentIn, NotificationType typeIn, int yPosIn, boolean drawOffsetIn) {
		init(parentIn);
		parent = parentIn;
		type = typeIn;
		yPos = yPosIn;
		drawOffset = drawOffsetIn;
		build();
	}
	
	private void build() {
		EDimension d = parent.getListDimensions();
		
		button = new EGuiCheckBox(parent, d.startX + 10, yPos, 20, 20);
		label = new EGuiLabel(parent, button.endX + 8, button.startY + 6, type.getType()) {
			@Override
			public void mousePressed(int mXIn, int mYIn, int buttonIn) {
				if (buttonIn == 0) {
					EGuiButton.playPressSound();
					EnhancedMC.getNotificationHandler().toggleNotificationEnabled(type, true);
					button.setChecked(EnhancedMC.getNotificationHandler().isNotificationTypeEnabled(type));
				}
			}
		};
		
		label.setDisplayStringColor(EColors.lgray);
		label.setHoverText(type.getDescription() != null ? type.getDescription() : "No description");
		
		button.setChecked(EnhancedMC.getNotificationHandler().isNotificationTypeEnabled(type));
		button.setActionReceiver(this);
		
		parent.addObjectToList(button, label);
		
		yPos = button.height + 8;
	}
	
	@Override
	public void actionPerformed(IEnhancedActionObject object, Object... args) {
		if (object == button) {
			EnhancedMC.getNotificationHandler().toggleNotificationEnabled(type, true);
			button.setChecked(EnhancedMC.getNotificationHandler().isNotificationTypeEnabled(type));
		}
	}
	
	public int getYPos() { return yPos; }

}
