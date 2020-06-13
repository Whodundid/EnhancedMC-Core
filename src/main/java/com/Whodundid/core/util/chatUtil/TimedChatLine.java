package com.Whodundid.core.util.chatUtil;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;
import net.minecraft.client.gui.ChatLine;
import net.minecraft.util.IChatComponent;

//Author: Hunter Bragg

public class TimedChatLine extends ChatLine implements Comparable<TimedChatLine> {

	public Long creationTime = 0l;
	public String creationTimeStamp = "";
	
	public TimedChatLine(int updateCounter, IChatComponent lineIn, int idIn) {
		super(updateCounter, lineIn, idIn);
		creationTime = System.currentTimeMillis();
		generateTimeStamp();
	}
	
	protected void generateTimeStamp() {
		Date date = new Date(creationTime);
		SimpleDateFormat formatter = new SimpleDateFormat("HH:mm:ss");
		formatter.setTimeZone(TimeZone.getDefault());
		creationTimeStamp = formatter.format(date);
	}

	public String getTimeStamp() { return creationTimeStamp; }
	public TimedChatLine setTimeStamp(String timeIn) { creationTimeStamp = timeIn; return this; }
	public long getCreationTime() { return creationTime; }
	public TimedChatLine setCreationTime(long timeIn) { creationTime = timeIn; generateTimeStamp(); return this; }

	@Override public int compareTo(TimedChatLine o) { return creationTime.compareTo(o.getCreationTime()); }
	
}
