package com.Whodundid.core.asm;

import com.Whodundid.core.asm.transformers.*;
import com.Whodundid.core.util.storageUtil.EArrayList;
import net.minecraft.launchwrapper.IClassTransformer;

public class EMCTransformer implements IClassTransformer {

	private static final EArrayList<IETransformer> transformers = new EArrayList();
	private static boolean isObfuscated = false;
	
	public EMCTransformer() {
		transformers.add(
			new ChatCommandFixer(),
			new ChatGrabber(),
			new ScoreboardListener(),
			new GIGFItemHighlight()
		);
	}
	
	@Override
	public byte[] transform(String name, String transformedName, byte[] basicClass) {
		if (!isObfuscated && name.equals("net.minecraft.client.Minecraft")) { isObfuscated = !name.equals(transformedName); }
		for (int i = 0; i < transformers.size(); i++) {
			IETransformer t = transformers.get(i);
			if (t.getClassName().equals(transformedName)) { return t.start(basicClass, isObfuscated); }
		}
		return basicClass;
	}
}
