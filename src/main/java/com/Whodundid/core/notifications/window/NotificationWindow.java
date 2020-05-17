package com.Whodundid.core.notifications.window;

import com.Whodundid.core.EnhancedMC;
import com.Whodundid.core.coreApp.EMCResources;
import com.Whodundid.core.enhancedGui.guiObjects.actionObjects.EGuiButton;
import com.Whodundid.core.enhancedGui.guiObjects.actionObjects.EGuiTextField;
import com.Whodundid.core.enhancedGui.guiObjects.advancedObjects.scrollList.EGuiScrollList;
import com.Whodundid.core.enhancedGui.guiObjects.basicObjects.EGuiLabel;
import com.Whodundid.core.enhancedGui.types.WindowParent;
import com.Whodundid.core.enhancedGui.types.interfaces.IEnhancedActionObject;
import com.Whodundid.core.notifications.util.NotificationType;
import com.Whodundid.core.util.renderUtil.EColors;
import com.Whodundid.core.util.storageUtil.EArrayList;
import com.Whodundid.core.util.storageUtil.EDimension;
import com.Whodundid.core.util.storageUtil.StorageBox;
import java.util.Iterator;

public class NotificationWindow extends WindowParent {
	
	EGuiButton enableAll, disableAll, back;
	EGuiScrollList nList;
	EGuiTextField searchField;
	EArrayList<StorageBox<String, EArrayList<NotificationType>>> notes = new EArrayList();
	
	public NotificationWindow() {
		super();
		aliases.add("notifications", "notif", "noteman", "note");
		windowIcon = EMCResources.notificationIcon;
	}
	
	@Override
	public void initGui() {
		defaultDims();
		setObjectName("Notification Manager");
		
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
		
		nList = new EGuiScrollList(this, startX + 2, startY + 20, width - 4, height - 50);
		nList.setBackgroundColor(EColors.pdgray.intVal);
		EDimension ld = nList.getListDimensions();
		
		int yPos = 8;
		
		for (StorageBox<String, EArrayList<NotificationType>> boxes : notes) {
			String catName = boxes.getObject();
			EArrayList<NotificationType> types = boxes.getValue();
			
			nList.addObjectToList(new EGuiLabel(nList, 6, yPos, catName, EColors.orange));
			yPos += 15;
			
			for (NotificationType t : types) {
				NoteSettingContainer con = new NoteSettingContainer(nList, t, yPos, false);
				yPos += con.getYPos();
			}
		}
		
		nList.fitItemsInList();
		
		enableAll = new EGuiButton(this, startX + 5, endY - 25, 64, 20, "Enable All").setDisplayStringColor(EColors.green);
		disableAll = new EGuiButton(this, midX - 32, endY - 25, 64, 20, "Disable All").setDisplayStringColor(EColors.lred);
		back = new EGuiButton(this, endX - 69, endY - 25, 64, 20, "Back").setDisplayStringColor(EColors.yellow);
		
		addObject(nList);
		addObject(enableAll, disableAll, back);
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
	public void actionPerformed(IEnhancedActionObject object, Object... args) {
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
				if (msg.equals("Reload Notifications")) {
					int pos = nList.getVScrollBar().getScrollPos();
					reInitObjects();
					nList.getVScrollBar().setScrollBarPos(pos);
				}
			}
		}
	}
	
}
 