package com.Whodundid.enhancedChat.chatOrganizer;

import com.Whodundid.core.util.storageUtil.EArrayList;
import java.util.Iterator;
import net.minecraft.util.EnumChatFormatting;

public class ChatFilterList implements Iterable<String> {

	public String filterName;
	public EArrayList<String> filters = new EArrayList();
	public int color = 0xffffff;
	public EnumChatFormatting mcColor = EnumChatFormatting.WHITE;
	
	public ChatFilterList(String filterNameIn) {
		filterName = filterNameIn;
	}
	
	public ChatFilterList(String filterNameIn, int colorIn, EnumChatFormatting mcColorIn) {
		filterName = filterNameIn;
		color = colorIn;
		mcColor = mcColorIn;
	}
	
	@Override public Iterator<String> iterator() { return filters.iterator(); }
	
	public ChatFilterList setFilterName(String newNameIn) { filterName = newNameIn; return this; }
	public String getFilterName() { return filterName; }
	public EArrayList<String> getFilterList() { return filters; }
	
	public ChatFilterList addFilters(String... in) {
		return addAllFilters(new EArrayList<String>(in));
	}
	
	public ChatFilterList addAllFilters(EArrayList<String> in) {
		filters.addAll(in);
		return this;
	}
	
	public ChatFilterList removeFilters(String... in) {
		return removeAllFilters(new EArrayList<String>(in));
	}
	
	public ChatFilterList removeAllFilters(EArrayList<String> in) {
		filters.removeAll(in);
		return this;
	}
	
	public int getColor() { return color; }
	public EnumChatFormatting getMCColor() { return mcColor; }
}
