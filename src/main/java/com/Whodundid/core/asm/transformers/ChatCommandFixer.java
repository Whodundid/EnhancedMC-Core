package com.Whodundid.core.asm.transformers;

import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.JumpInsnNode;
import org.objectweb.asm.tree.LabelNode;
import org.objectweb.asm.tree.LdcInsnNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.VarInsnNode;
import static org.objectweb.asm.Opcodes.*;

import com.Whodundid.core.asm.IETransformer;

public class ChatCommandFixer extends IETransformer {
	
	@Override
	public String getClassName() { return "net.minecraft.client.gui.GuiScreen"; }
	
	@Override
	protected void transform(ClassNode guiScreenClass) {
		final String CHATMETHOD = isObfuscated ? "b" : "sendChatMessage";
		final String CHATMETHOD_DESC = isObfuscated ? "(Ljava/lang/String;Z)V" : "(Ljava/lang/String;Z)V";
		
		for (MethodNode method : guiScreenClass.methods) {
			if (method.name.equals(CHATMETHOD) && method.desc.equals(CHATMETHOD_DESC)) {
				AbstractInsnNode targetNode = null;
				for (AbstractInsnNode instruction : method.instructions.toArray()) {
					if (instruction.getOpcode() == ALOAD) {
						if (((VarInsnNode) instruction).var == 0 && instruction.getPrevious().getOpcode() == GETSTATIC && instruction.getNext().getOpcode() == GETFIELD) {
							targetNode = instruction.getPrevious();
							break;
						}
					}
				}
				
				if (targetNode != null) {
					//targetNode = ' mv.visitFieldInsn(GETSTATIC, "net/minecraftforge/client/ClientCommandHandler", "instance", "Lnet/minecraftforge/client/ClientCommandHandler;"); '
					/*
					mv.visitJumpInsn(IFNULL, l2);
					mv.visitVarInsn(ALOAD, 1);
					mv.visitLdcInsn("/");
					mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/String", "startsWith", "(Ljava/lang/String;)Z", false);
					mv.visitJumpInsn(IFEQ, l2);
					*/
					//adding before
					/*
					ALOAD 1: msg
					LDC "/"
					INVOKEVIRTUAL String.startsWith(String):boolean
					mv.visitFieldInsn(GETSTATIC, "net/minecraftforge/client/ClientCommandHandler", "instance", "Lnet/minecraftforge/client/ClientCommandHandler;");
					mv.visitVarInsn(ALOAD, 0);
					mv.visitFieldInsn(GETFIELD, "net/minecraft/client/gui/GuiScreen", "mc", "Lnet/minecraft/client/Minecraft;");
					mv.visitFieldInsn(GETFIELD, "net/minecraft/client/Minecraft", "thePlayer", "Lnet/minecraft/client/entity/EntityPlayerSP;");
					mv.visitVarInsn(ALOAD, 1);
					mv.visitMethodInsn(INVOKEVIRTUAL, "net/minecraftforge/client/ClientCommandHandler", "executeCommand", "(Lnet/minecraft/command/ICommandSender;Ljava/lang/String;)I", false);
					*/
					
					AbstractInsnNode afterReturn = targetNode;
					for (int i = 0; i < 7; i++) { afterReturn = afterReturn.getNext(); }
					
					//System.out.println("after return node: " + afterReturn.getOpcode());
					
					LabelNode lNode = new LabelNode();
					
					InsnList toInsert = new InsnList();
					toInsert.add(new VarInsnNode(ALOAD, 1));
					toInsert.add(new LdcInsnNode("/"));
					toInsert.add(new MethodInsnNode(INVOKEVIRTUAL, "java/lang/String", "startsWith", "(Ljava/lang/String;)Z", false));
					toInsert.add(new JumpInsnNode(IFEQ, lNode));
					
					method.instructions.insertBefore(targetNode, toInsert);
					method.instructions.insert(afterReturn, lNode);
					
					System.out.println("inserting byte code into GuiScreen");
				}
				else {
					System.out.println("GUISCREEN CHAT ASM TRANSFORM FAILED");
				}
			}
		}
	}
}
