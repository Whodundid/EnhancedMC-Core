package com.Whodundid.core.coreApp.settings;

import com.Whodundid.core.EnhancedMC;
import com.Whodundid.core.app.config.AppConfigSetting;
import com.Whodundid.core.renderer.renderUtil.RendererProxyGui;
import com.Whodundid.core.util.EUtil;
import com.Whodundid.core.windowLibrary.windowTypes.WindowParent;
import net.minecraft.client.Minecraft;

public class EnableTaskBarSetting extends AppConfigSetting<Boolean> {
	
	public EnableTaskBarSetting() {
		super(Boolean.class, "enableTaskBar", "Enable Task Bar", false);
	}
	
	@Override
	public AppConfigSetting set(Boolean valIn) {
		super.set(valIn);
		
		if (Minecraft.getMinecraft().currentScreen instanceof RendererProxyGui) {
			if (valIn) {
				if (EnhancedMC.getRenderer().getTaskBar() == null) { EnhancedMC.getRenderer().addTaskBar(true); }
			}
			else {
				EUtil.ifNotNullDo(EnhancedMC.getRenderer().getTaskBar(), t -> t.close());
				
				for (WindowParent p : EnhancedMC.getAllActiveWindows()) {
					if (p.isMinimized()) { p.setMinimized(false); }
				}
			}
			
			EnhancedMC.reloadAllWindows();
		}
		return this;
	}

}
