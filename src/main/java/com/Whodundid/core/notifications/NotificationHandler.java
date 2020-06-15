package com.Whodundid.core.notifications;

import com.Whodundid.core.EnhancedMC;
import com.Whodundid.core.app.AppType;
import com.Whodundid.core.app.RegisteredApps;
import com.Whodundid.core.coreApp.EMCNotification;
import com.Whodundid.core.notifications.util.NotificationObject;
import com.Whodundid.core.notifications.util.NotificationType;
import com.Whodundid.core.util.storageUtil.EArrayList;
import com.Whodundid.core.windowLibrary.windowTypes.WindowParent;
import java.io.File;
import java.io.PrintWriter;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Scanner;
import net.minecraft.client.Minecraft;

//Author: Hunter Bragg

public class NotificationHandler {
	
	private static NotificationHandler instance = null;
	public static File configLocation = new File(RegisteredApps.getAppConfigBaseFileLocation(AppType.CORE).getAbsolutePath() + "/notifications.cfg");
	protected Deque<NotificationObject> notificationQueue;
	protected EArrayList<NotificationType> enabledNotifications = new EArrayList();
	protected EArrayList<NotificationType> disabledNotifications = new EArrayList();
	protected NotificationObject curNote = null;
	protected long delayStart = 0l;
	protected long delayTime = 300l;
	
	//-----------------------------------
	//NotificationHandler Static Instance
	//-----------------------------------
	
	public static NotificationHandler getInstance() {
		return instance = instance != null ? instance : new NotificationHandler();
	}

	private NotificationHandler() {
		notificationQueue = new ArrayDeque();
	}
	
	//--------------------------
	//NotificationHandler Config
	//--------------------------
	
	public boolean loadConfig() {
		if (!configLocation.exists()) { saveConfig(); }
		
		if (load()) { EnhancedMC.info("EMC: Successfully loaded Notification config file!"); }
		else { EnhancedMC.error("EMC Error: Failed to load Notification config file!"); }
		
		return false;
	}
	
	private boolean load() {
		EnhancedMC.info("EMC: Loading Notification config file...");
		try (Scanner reader = new Scanner(configLocation)) {
			while (reader.hasNextLine()) {
				String line = reader.nextLine();
				
				if (line.equals("END")) { break; } //config end identifier
				if (line.isEmpty() || line.startsWith("**")) { continue; } //ignore comment line
				
				String[] parts = line.split(",");
				if (parts.length == 2) {
					
					String internal = "";
					boolean enabled = false;
					
					internal = parts[0];
					enabled = Boolean.parseBoolean(parts[1]);
					
					if (internal != null && !internal.isEmpty()) {
						for (NotificationType o : getNotificationTypes()) {
							if (o != null) {
								if (o.getInternalName().equals(internal)) {
									if (enabled) { enableNotificationType(o, false); }
									else { disableNotificationType(o, false); }
								}
							}
						}
					}
					
				} //while
			}
			
			return true;
		}
		catch (Exception e) { e.printStackTrace(); }
		return false;
	}
	
	public boolean saveConfig() {
		if (!configLocation.exists()) {
			EnhancedMC.info("EMC: Notification config not found, attempting to create a new one...");
			
			//if no config exists, enable all
			for (NotificationType o : getNotificationTypes()) {
				enableNotificationType(o, false);
			}
		}
		
		if (!save()) {
			EnhancedMC.error("EMC Error: Failed to save Notification config file!");
			return false;
		}
		
		return true;
	}
	
	private boolean save() {
		try (PrintWriter saver = new PrintWriter(configLocation, "UTF-8")) {
			saver.println("** EMC Notification Config **");
			saver.println();
			
			boolean oneEnabled = false;
			
			for (NotificationType o : getNotificationTypes()) {
				if (o != null) {
					oneEnabled = true;
					saver.println(o.getInternalName() + "," + isNotificationTypeEnabled(o));
				}
			}
			
			if (oneEnabled) { saver.println(); }
			saver.print("END");
			
			return true;
		}
		catch (Exception e) { e.printStackTrace(); }
		return false;
	}

	//---------------------------
	//NotificationHandler Methods
	//---------------------------
	
	public NotificationHandler post(String message, WindowParent attentionWindow) { return post(new EMCNotification(message)); }
	public NotificationHandler post(NotificationObject obj) {
		if (obj != null) { notificationQueue.add(obj); }
		return this;
	}
	
	public void update() {
		if (curNote == null && !notificationQueue.isEmpty()) {
			if (System.currentTimeMillis() - delayStart > delayTime) {
				if (Minecraft.getMinecraft().thePlayer != null && Minecraft.getMinecraft().ingameGUI != null) {
					displayNextNotification();
				}
			}
		}
	}
	
	public void clearAllNotifications() {
		if (EnhancedMC.isEGuiOpen(NotificationObject.class)) {
			Object o = EnhancedMC.getWindowInstance(NotificationObject.class);
			if (o instanceof NotificationObject) {
				NotificationObject obj = (NotificationObject) o;
				obj.close();
			}
		}
		notificationQueue.clear();
		curNote = null;
	}
	
	public void removeCurrentNotification() {
		curNote = null;
		delayStart = System.currentTimeMillis();
	}
	
	public void registerNotificationType(NotificationType typeIn) {
		if (!containsType(typeIn)) { enabledNotifications.add(typeIn); }
		else { EnhancedMC.error("EMC Error: A NotificationType of " + typeIn.getInternalName() + " already exists!"); }
	}
	
	public void unregisterNotificationType(NotificationType typeIn) {
		if (containsType(typeIn)) { 
			enabledNotifications.removeIfContains(typeIn);
			disabledNotifications.removeIfContains(typeIn);
		}
		else { EnhancedMC.error("EMC Error: A NotificationType of " + typeIn.getInternalName() + " does not exist!"); }
	}
	
	public void enableNotificationType(NotificationType typeIn, boolean save) {
		enabledNotifications.addIfNotNullAndNotContains(typeIn);
		disabledNotifications.removeIfContains(typeIn);
		if (save) { saveConfig(); }
		EnhancedMC.reloadAllWindows();
		reloadWindows();
	}
	
	public void disableNotificationType(NotificationType typeIn, boolean save) {
		enabledNotifications.removeIfContains(typeIn);
		disabledNotifications.addIfNotNullAndNotContains(typeIn);
		if (save) { saveConfig(); }
		EnhancedMC.reloadAllWindows();
		reloadWindows();
	}
	
	public boolean toggleNotificationEnabled(NotificationType typeIn, boolean save) {
		if (enabledNotifications.contains(typeIn)) { disableNotificationType(typeIn, save); return false; }
		else if (disabledNotifications.contains(typeIn)) { enableNotificationType(typeIn, save); return true; }
		return false;
	}
	
	public boolean isNotificationTypeEnabled(NotificationType typeIn) {
		return enabledNotifications.contains(typeIn);
	}
	
	public NotificationHandler reloadWindows() { EnhancedMC.getAllActiveWindows().forEach(w -> w.sendArgs("Reload Notifications")); return this; }
	
	//---------------------------
	//NotificationHandler Getters
	//---------------------------
	
	public EArrayList<NotificationType> getNotificationTypes() {
		return EArrayList.combineLists(enabledNotifications, disabledNotifications);
	}
	
	public EArrayList<String> getInternalNames() {
		EArrayList<String> names = new EArrayList();
		for (NotificationType t : getNotificationTypes()) {
			names.add(t.getInternalName());
		}
		return names;
	}
	
	public NotificationObject getCurrentNotification() { return curNote; }
	
	//---------------------------
	//NotificationHandler Setters
	//---------------------------
	
	public void setNotificationDelay(long delayIn) { delayTime = delayIn; }
	
	//------------------------------------
	//NotificationHandler Internal Methods
	//------------------------------------
	
	protected void displayNextNotification() {
		if (!notificationQueue.isEmpty()) {
			NotificationObject n = notificationQueue.pop();
			curNote = n;
			EnhancedMC.getRenderer().addObject(null, curNote);
		}
	}
	
	protected boolean containsType(NotificationType typeIn) {
		if (typeIn != null) {
			for (NotificationType t : getNotificationTypes()) {
				if (t.getInternalName() == typeIn.getInternalName()) { return true; }
			}
		}
		return false;
	}
	
}
