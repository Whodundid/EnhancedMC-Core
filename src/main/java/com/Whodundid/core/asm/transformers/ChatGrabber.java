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

public class ChatGrabber extends IETransformer {
	
	@Override
	public String getClassName() { return "net.minecraft.client.gui.GuiNewChat"; }
	
	@Override
	public void transform(ClassNode classIn) {
		final String CHATMETHOD = isObfuscated ? "a" : "printChatMessageWithOptionalDeletion";
		final String CHATMETHOD_DESC = isObfuscated ? "(Leu;I)V" : "(Lnet/minecraft/util/IChatComponent;I)V";
		
		for (MethodNode method : classIn.methods) {
			if (method.name.equals(CHATMETHOD) && method.desc.equals(CHATMETHOD_DESC)) {
				AbstractInsnNode targetNode = null;
				for (AbstractInsnNode instruction : method.instructions.toArray()) {
					if (instruction.getOpcode() == INVOKESPECIAL) {
						if (instruction.getPrevious() instanceof InsnNode && instruction.getNext() instanceof LabelNode) {
							targetNode = instruction;
							break;
						}
					}
				}
				
				if (targetNode != null) {
					InsnList toInsert = new InsnList();
					
					toInsert.add(new LabelNode());
					toInsert.add(new FieldInsnNode(GETSTATIC, "net/minecraftforge/common/MinecraftForge", "EVENT_BUS", "Lnet/minecraftforge/fml/common/eventhandler/EventBus;"));
					toInsert.add(new TypeInsnNode(NEW, "com/Whodundid/core/coreEvents/emcEvents/ChatLineCreatedEvent"));
					toInsert.add(new InsnNode(DUP));
					toInsert.add(new TypeInsnNode(NEW, "com/Whodundid/core/util/chatUtil/TimedChatLine"));
					toInsert.add(new InsnNode(DUP));
					toInsert.add(new MethodInsnNode(INVOKESTATIC, "net/minecraft/client/Minecraft", "getMinecraft", "()Lnet/minecraft/client/Minecraft;", false));
					toInsert.add(new FieldInsnNode(GETFIELD, "net/minecraft/client/Minecraft", "ingameGUI", "Lnet/minecraft/client/gui/GuiIngame;"));
					toInsert.add(new MethodInsnNode(INVOKEVIRTUAL, "net/minecraft/client/gui/GuiIngame", "getUpdateCounter", "()I", false));
					toInsert.add(new VarInsnNode(ALOAD, 1));
					toInsert.add(new VarInsnNode(ILOAD, 2));
					toInsert.add(new MethodInsnNode(INVOKESPECIAL, "com/Whodundid/core/util/chatUtil/TimedChatLine", "<init>", "(ILnet/minecraft/util/IChatComponent;I)V", false));
					toInsert.add(new MethodInsnNode(INVOKESPECIAL, "com/Whodundid/core/coreEvents/emcEvents/ChatLineCreatedEvent", "<init>", "(Lcom/Whodundid/core/util/chatUtil/TimedChatLine;)V", false));
					toInsert.add(new MethodInsnNode(INVOKEVIRTUAL, "net/minecraftforge/fml/common/eventhandler/EventBus", "post", "(Lnet/minecraftforge/fml/common/eventhandler/Event;)Z", false));
					toInsert.add(new InsnNode(POP));
					
					method.instructions.insert(targetNode, toInsert);
					System.out.println("EMC: GuiNewChat transform successful!");
				}
				else { System.out.println("EMC: GUINEWCHAT ASM TRANSFORM FAILED!"); }
			}
		}
	}
}
