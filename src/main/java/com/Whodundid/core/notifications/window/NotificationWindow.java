package com.Whodundid.core.notifications.window;

import com.Whodundid.core.EnhancedMC;
import com.Whodundid.core.coreApp.EMCResources;
import com.Whodundid.core.notifications.util.NotificationType;
import com.Whodundid.core.util.renderUtil.EColors;
import com.Whodundid.core.util.storageUtil.EArrayList;
import com.Whodundid.core.util.storageUtil.EDimension;
import com.Whodundid.core.util.storageUtil.StorageBox;
import com.Whodundid.core.windowLibrary.windowObjects.actionObjects.WindowButton;
import com.Whodundid.core.windowLibrary.windowObjects.actionObjects.WindowTextField;
import com.Whodundid.core.windowLibrary.windowObjects.advancedObjects.scrollList.WindowScrollList;
import com.Whodundid.core.windowLibrary.windowObjects.basicObjects.WindowLabel;
import com.Whodundid.core.windowLibrary.windowTypes.WindowParent;
import com.Whodundid.core.windowLibrary.windowTypes.interfaces.IActionObject;
import java.util.Iterator;

public class NotificationWindow extends WindowParent {
	
	WindowButton enableAll, disableAll, back;
	WindowScrollList nList;
	WindowTextField searchField;
	EArrayList<StorageBox<String, EArrayList<NotificationType>>> notes = new EArrayList();
	
	public NotificationWindow() {
		super();
		aliases.add("notifications", "notif", "noteman", "note");
		windowIcon = EMCResources.notificationIcon;
	}
	
	@Override
	public void initWindow() {
		defaultDims();
		setObjectName("Notification Manager");
		setMaximizable(true);
		setResizeable(true);
		setMinDims(defaultWidth, defaultHeight);
		
		EArrayList<NotificationType> unSorted = new EArrayList(EnhancedMC.getNotificationHandler().getNotificationTypes());
		
		EArrayList<String> categories = new EArrayList();
		for (NotificationType t : unSorted) { categories.addIfNotNullAndNotContains(t.getCategory()); }
		
		//search for emc
		for (String s : categories) {
			EArrayList<NotificationType> types = new EArrayList();
			Iterator<NotificationType> it = unSorted.iterator();
			
			boolean found = false;
			while (it.hasNext()) {
				NotificationType t = it.next();
				if (t.getCategory() != null && t.getCategory().equals("EMC")) {
					types.add(t);
					it.remove();
					found = true;
					break;
				}
			}
			
			if (found) { notes.add(new StorageBox("EMC", new EArrayList(types))); break; }
		}
		
		if (categories.contains("EMC")) { categories.remove("EMC"); }
		
		//get all other categories
		for (String s : categories) {
			EArrayList<NotificationType> types = new EArrayList();
			Iterator<NotificationType> it = unSorted.iterator();
			while (it.hasNext()) {
				NotificationType t = it.next();
				if (t.getCategory() != null && t.getCategory().equals(s)) {
					types.add(t);
					it.remove();
				}
			}
			
			notes.add(new StorageBox(s, new EArrayList(types)));
		}
		
		//add all the rest as generic
		if (unSorted.isNotEmpty()) {
			notes.add(new StorageBox("Non Specific", new EArrayList(unSorted)));
		}
	}
	
	@Override
	public void initObjects() {
		defaultHeader(this);
		
		nList = new WindowScrollList(this, startX + 2, startY + 20, width - 4, height - 50);
		nList.setBackgroundColor(EColors.pdgray.intVal);
		EDimension ld = nList.getListDimensions();
		
		int yPos = 8;
		
		for (StorageBox<String, EArrayList<NotificationType>> boxes : notes) {
			String catName = boxes.getObject();
			EArrayList<NotificationType> types = boxes.getValue();
			
			nList.addObjectToList(new WindowLabel(nList, 6, yPos, catName, EColors.orange));
			yPos += 15;
			
			for (NotificationType t : types) {
				NoteSettingContainer con = new NoteSettingContainer(nList, t, yPos, false);
				yPos += con.getYPos();
			}
		}
		
		nList.fitItemsInList();
		
		enableAll = new WindowButton(this, startX + 5, endY - 25, 64, 20, "Enable All").setStringColor(EColors.green);
		disableAll = new WindowButton(this, midX - 32, endY - 25, 64, 20, "Disable All").setStringColor(EColors.lred);
		back = new WindowButton(this, endX - 69, endY - 25, 64, 20, "Back").setStringColor(EColors.yellow);
		
		addObject(null, nList);
		addObject(null, enableAll, disableAll, back);
	}
	
	@Override
	public void drawObject(int mXIn, int mYIn) {
		drawDefaultBackground();
		
		drawRect(startX + 2, startY + 2, endX - 2, startY + 20, EColors.black);
		drawRect(startX + 3, startY + 3, endX - 3, startY + 20, EColors.steel);
		drawStringCS("Select Enabled Notifications", midX, startY + 8, EColors.orange);
		
		super.drawObject(mXIn, mYIn);
	}
	
	@Override
	public void actionPerformed(IActionObject object, Object... args) {
		if (object == enableAll) {
			for (NotificationType t : EnhancedMC.getNotificationHandler().getNotificationTypes()) {
				EnhancedMC.getNotificationHandler().enableNotificationType(t, true);
			}
			
			int pos = nList.getVScrollBar().getScrollPos();
			reInitObjects();
			nList.getVScrollBar().setScrollBarPos(pos);
		}
		if (object == disableAll) {
			for (NotificationType t : EnhancedMC.getNotificationHandler().getNotificationTypes()) {
				EnhancedMC.getNotificationHandler().disableNotificationType(t, true);
			}

			int pos = nList.getVScrollBar().getScrollPos();
			reInitObjects();
			nList.getVScrollBar().setScrollBarPos(pos);
		}
		if (object == back) { fileUpAndClose(); }
	}
	
	@Override
	public void sendArgs(Object... args) {
		if (args.length == 1) {
			if (args[0] instanceof String) {
				String msg = (String) args[0];
				if (msg.equals("Reload Notifications") || msg.equals("Reload")) {
					int pos = nList.getVScrollBar().getScrollPos();
					reInitObjects();
					nList.getVScrollBar().setScrollBarPos(pos);
				}
			}
		}
	}
	
}
 