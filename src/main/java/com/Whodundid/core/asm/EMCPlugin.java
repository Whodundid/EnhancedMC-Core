package com.Whodundid.core.asm;

import java.util.Map;
import net.minecraftforge.fml.relauncher.IFMLLoadingPlugin;

//Author: Hunter Bragg

@IFMLLoadingPlugin.MCVersion("1.8.9")
@IFMLLoadingPlugin.TransformerExclusions({"com.Whodundid.core.asm"})
public class EMCPlugin implements IFMLLoadingPlugin {
	
	@Override
	public String[] getASMTransformerClass() {
		return new String[] {"com.Whodundid.core.asm.EMCTransformer"};
	}
	
	@Override public String getModContainerClass() { return null; }
	@Override public String getSetupClass() { return null; }
	@Override public void injectData(Map<String, Object> data) { }
	@Override public String getAccessTransformerClass() { return null; }
}
