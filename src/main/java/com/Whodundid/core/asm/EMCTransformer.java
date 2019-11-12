package com.Whodundid.core.asm;

import com.Whodundid.core.asm.transformers.*;
import com.Whodundid.core.util.storageUtil.EArrayList;
import net.minecraft.launchwrapper.IClassTransformer;

public class EMCTransformer implements IClassTransformer {

	private static final EArrayList<IETransformer> transformers = new EArrayList();
	
	public EMCTransformer() {
		transformers.add(
			new ChatCommandFixer(),
			new ChatGrabber(),
			new ScoreboardListener()
		);
	}
	
	@Override
	public byte[] transform(String name, String transformedName, byte[] basicClass) {
		for (int i = 0; i < transformers.size(); i++) {
			IETransformer t = transformers.get(i);
			if (t.getClassName().equals(transformedName)) { return t.start(basicClass, !name.equals(transformedName)); }
		}
		return basicClass;
	}
}
