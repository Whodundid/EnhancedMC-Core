package com.Whodundid.core.subMod;

import com.Whodundid.core.EnhancedMC;
import com.Whodundid.core.enhancedGui.guiObjects.EGuiButton;
import com.Whodundid.core.enhancedGui.guiObjects.EGuiDialogueBox;
import com.Whodundid.core.enhancedGui.guiObjects.EGuiHeader;
import com.Whodundid.core.enhancedGui.guiObjects.EGuiLabel;
import com.Whodundid.core.enhancedGui.guiObjects.EGuiRect;
import com.Whodundid.core.enhancedGui.guiObjects.EGuiTextArea;
import com.Whodundid.core.enhancedGui.interfaces.IEnhancedGuiObject;
import com.Whodundid.core.util.storageUtil.StorageBox;
import net.minecraft.util.EnumChatFormatting;

public class IncompatibleWindowList extends EGuiDialogueBox {
	
	public IncompatibleWindowList(IEnhancedGuiObject parentIn, int xPos, int yPos, int width, int height) {
		init(parentIn, xPos, yPos, width, height);
		addObject(header = new EGuiHeader(this));
		requestFocus();
		getTopParent().setFocusLockObject(this);
		setDisplayStringColor(0xc2c2c2);
		setDisplayString("SubMod Incompatibility");
		setZLevel(2);
		
		EGuiButton okButton;
		
		okButton = new EGuiButton(this, midX - 25, endY - 27, 50, 20, "Ok") {
			{ setRunActionOnPress(true); }
			@Override
			public void performAction() {
				playPressSound();
				parent.close();
			}
		};
		addObject(okButton.setZLevel(1));
		
		EGuiLabel problem;
		
		problem = new EGuiLabel(this, midX, startY + 6, "The following EMC SubMods are incompatible with this core version '" + EnhancedMC.VERSION + "'");
		problem.enableWordWrap(true, width - 15).setLineGapHeight(3).enableShadow(true).setDrawCentered(true).setDisplayStringColor(0xffbb00);
		
		EGuiRect topLine;
		
		topLine = new EGuiRect(this, startX + 1, problem.startY + problem.getTextHeight() + 2, endX - 1, problem.startY + problem.getTextHeight() + 3, 0xff000000);
		addObject(topLine);
		
		EGuiTextArea incompatibleList = new EGuiTextArea(this, startX + 7, topLine.endY + 6, width - 14, okButton.startY - topLine.endY - 12).setDrawLineNumbers(true).setEditable(false);
		addObject(incompatibleList);
		
		if (RegisteredSubMods.getIncompatibleModsList().isNotEmpty()) {
			for (SubMod s : RegisteredSubMods.getIncompatibleModsList()) {
				//StorageBox<String, BigDecimal> box = s.getDependencies().getBoxWithObj(SubModType.CORE);
				//if (box != null) {
				//	incompatibleList.addTextLine(EnumChatFormatting.GRAY + s.getName() + " : " + 
				//								 EnumChatFormatting.RED + "requires version '" + box.getValue() + "'", 0x55ff55);
				//}
			}
		} else { incompatibleList.addTextLine("None", 0xb2b2b2); }
		
		addObject(problem);
	}
}
