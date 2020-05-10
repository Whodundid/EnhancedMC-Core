package com.Whodundid.core.app.config;

import com.Whodundid.core.util.storageUtil.EArrayList;
import com.Whodundid.core.util.storageUtil.StorageBoxHolder;
import java.util.List;

//Author: Hunter Bragg

/** Allows comment blocks to be inserted within config data. */
public class CommentConfigBlock extends ConfigBlock {
	
	public CommentConfigBlock() { super(); }
	public CommentConfigBlock(StorageBoxHolder<String, List<String>> elementsIn) { super(elementsIn); }
	
	public CommentConfigBlock(String... comments) {
		super();
		addLine(comments);
	}
	
	public CommentConfigBlock addLine(String... comments) {
		for (String s : comments) { addLine(s); }
		return this;
	}
	public CommentConfigBlock addLine(String commentIn) {
		blockContents.add("**", new EArrayList<String>(commentIn));
		return this;
	}
}
