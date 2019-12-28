package com.Whodundid.core.asm.transformers;

import com.Whodundid.core.asm.IETransformer;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.MethodNode;

public class GIGFItemHighlight extends IETransformer {
	
	@Override
	public String getClassName() { return "net.minecraftforge.client.GuiIngameForge"; }
	
	@Override
	protected void transform(ClassNode gigfClass) {
		System.out.println("HERER!: " + gigfClass + " " + isObfuscated);
		final String CHATMETHOD = "renderToolHightlight";
		final String CHATMETHOD_DESC = isObfuscated ? "(Lavr)V" : "(Lnet/minecraft/client/gui/ScaledResolution)V";
		
		for (MethodNode method : gigfClass.methods) {
			if (method.name.equals(CHATMETHOD) && method.desc.equals(CHATMETHOD_DESC)) {
				AbstractInsnNode targetNode = null;
				for (AbstractInsnNode instruction : method.instructions.toArray()) {
					//if (instruction.getOpcode() == ALOAD) {
					//	if (((VarInsnNode) instruction).var == 0 && instruction.getPrevious().getOpcode() == GETSTATIC && instruction.getNext().getOpcode() == GETFIELD) {
					//		targetNode = instruction.getPrevious();
					//		break;
					//	}
					//}
				}
				
				//if (targetNode != null) {
					
				//}
				//else {
					//System.out.println("EMC: GUIINGAMEFORGE ASM TRANSFORM FAILED!");
				//}
			}
		}
	}
}
