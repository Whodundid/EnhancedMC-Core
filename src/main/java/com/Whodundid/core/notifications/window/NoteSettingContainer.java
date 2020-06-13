package com.Whodundid.core.notifications.window;

import com.Whodundid.core.EnhancedMC;
import com.Whodundid.core.notifications.util.NotificationType;
import com.Whodundid.core.util.renderUtil.EColors;
import com.Whodundid.core.util.storageUtil.EDimension;
import com.Whodundid.core.windowLibrary.windowObjects.actionObjects.WindowButton;
import com.Whodundid.core.windowLibrary.windowObjects.actionObjects.WindowCheckBox;
import com.Whodundid.core.windowLibrary.windowObjects.advancedObjects.scrollList.WindowScrollList;
import com.Whodundid.core.windowLibrary.windowObjects.basicObjects.WindowLabel;
import com.Whodundid.core.windowLibrary.windowTypes.WindowObject;
import com.Whodundid.core.windowLibrary.windowTypes.interfaces.IActionObject;

public class NoteSettingContainer extends WindowObject {
	
	private WindowScrollList parent;
	private NotificationType type;
	private int yPos = 0;
	private boolean drawOffset = false;
	private WindowCheckBox button;
	private WindowLabel label;
	
	public NoteSettingContainer(WindowScrollList parentIn, NotificationType typeIn, int yPosIn, boolean drawOffsetIn) {
		init(parentIn);
		parent = parentIn;
		type = typeIn;
		yPos = yPosIn;
		drawOffset = drawOffsetIn;
		build();
	}
	
	private void build() {
		EDimension d = parent.getListDimensions();
		
		button = new WindowCheckBox(parent, d.startX + 10, yPos, 20, 20);
		label = new WindowLabel(parent, button.endX + 8, button.startY + 6, type.getType()) {
			@Override
			public void mousePressed(int mXIn, int mYIn, int buttonIn) {
				if (buttonIn == 0) {
					WindowButton.playPressSound();
					EnhancedMC.getNotificationHandler().toggleNotificationEnabled(type, true);
					button.setChecked(EnhancedMC.getNotificationHandler().isNotificationTypeEnabled(type));
				}
			}
		};
		
		label.setColor(EColors.lgray);
		label.setHoverText(type.getDescription() != null ? type.getDescription() : "No description");
		
		button.setChecked(EnhancedMC.getNotificationHandler().isNotificationTypeEnabled(type));
		button.setActionReceiver(this);
		
		parent.addObjectToList(button, label);
		
		yPos = button.height + 8;
	}
	
	@Override
	public void actionPerformed(IActionObject object, Object... args) {
		if (object == button) {
			EnhancedMC.getNotificationHandler().toggleNotificationEnabled(type, true);
			button.setChecked(EnhancedMC.getNotificationHandler().isNotificationTypeEnabled(type));
		}
	}
	
	public int getYPos() { return yPos; }

}
