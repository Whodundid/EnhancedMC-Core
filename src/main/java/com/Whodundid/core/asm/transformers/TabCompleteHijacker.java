package com.Whodundid.core.asm.transformers;

import static org.objectweb.asm.Opcodes.*;

import com.Whodundid.core.asm.IETransformer;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.FieldInsnNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.InsnNode;
import org.objectweb.asm.tree.LabelNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.TypeInsnNode;
import org.objectweb.asm.tree.VarInsnNode;

public class TabCompleteHijacker extends IETransformer {
	
	@Override
	public String getClassName() { return "net.minecraft.client.network.NetHandlerPlayClient"; }
	
	@Override
	protected void transform(ClassNode classIn) {
		final String METHOD = isObfuscated ? "a" : "handleTabComplete";
		final String METHOD_DESC = isObfuscated ? "(Lfx;)V" : "(Lnet/minecraft/network/play/server/S3APacketTabComplete;)V";
		
		for (MethodNode method : classIn.methods) {
			if (method.name.equals(METHOD) && method.desc.equals(METHOD_DESC)) {
				AbstractInsnNode targetNode = null;
				for (AbstractInsnNode instruction : method.instructions.toArray()) {
					if (instruction.getOpcode() == ASTORE) {
						if (instruction.getPrevious() instanceof MethodInsnNode && instruction.getNext() instanceof LabelNode) {
							targetNode = instruction;
							break;
						}
					}
				}
				
				if (targetNode != null) {
					
					InsnList toInsert = new InsnList();
					
					toInsert.add(new LabelNode());
					toInsert.add(new FieldInsnNode(GETSTATIC, "net/minecraftforge/common/MinecraftForge", "EVENT_BUS", "Lnet/minecraftforge/fml/common/eventhandler/EventBus;"));
					toInsert.add(new TypeInsnNode(NEW, "com/Whodundid/core/coreEvents/emcEvents/TabCompletionEvent"));
					toInsert.add(new InsnNode(DUP));
					toInsert.add(new VarInsnNode(ALOAD, 2));
					toInsert.add(new MethodInsnNode(INVOKESPECIAL, "com/Whodundid/core/coreEvents/emcEvents/TabCompletionEvent", "<init>", "([Ljava/lang/String;)V", false));
					toInsert.add(new MethodInsnNode(INVOKEVIRTUAL, "net/minecraftforge/fml/common/eventhandler/EventBus", "post", "(Lnet/minecraftforge/fml/common/eventhandler/Event;)Z", false));
					toInsert.add(new InsnNode(POP));
					
					method.instructions.insert(targetNode, toInsert);
					
					System.out.println("EMC: NetHandlerPlayClient transform successful!");
				}
				else { System.out.println("EMC: NETHANDLERPLAYCLIENT ASM TRANSFORM FAILED!"); }
			}
		}
	}
}
