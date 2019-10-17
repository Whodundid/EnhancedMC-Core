package com.Whodundid.core.asm;

import static org.objectweb.asm.Opcodes.*;
import java.util.Arrays;
import net.minecraft.launchwrapper.IClassTransformer;
import net.minecraft.scoreboard.ScorePlayerTeam;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.JumpInsnNode;
import org.objectweb.asm.tree.LabelNode;
import org.objectweb.asm.tree.LdcInsnNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.VarInsnNode;

public class ScoreboardTransformer implements IClassTransformer {
	
	private static final String[] classes = { "net.minecraft.scoreboard.Scoreboard" };
		
	@Override
	public byte[] transform(String name, String transformedName, byte[] classBeingTransformed) {
		boolean isObfuscated = !name.equals(transformedName);
		int index = Arrays.asList(classes).indexOf(transformedName);
		System.out.println(transformedName + " " + classBeingTransformed + " :: " + isObfuscated + ", " + index);
		return index != -1 ? transform(index, classBeingTransformed, isObfuscated) : classBeingTransformed;
	}
	
	private static byte[] transform(int index, byte[] classBeingTransformed, boolean isObfuscated) {
		System.out.println("transforming: " + classes[index]);
		
		try {
			ClassNode node = new ClassNode();
			ClassReader reader = new ClassReader(classBeingTransformed);
			reader.accept(node, 0);
			
			switch (index) {
			case 0: transformScoreboard(node, isObfuscated); break;
			default: System.out.println("broke!"); break;
			}
			
			ClassWriter writer = new ClassWriter(ClassWriter.COMPUTE_MAXS | ClassWriter.COMPUTE_FRAMES);
			node.accept(writer);
			byte[] ar = writer.toByteArray();
			return ar;
		}
		catch (Exception e) { e.printStackTrace(); }
		
		return classBeingTransformed;
	}
	
	private static void transformScoreboard(ClassNode scoreboardClass, boolean isObfuscated) {
		final String CHATMETHOD = isObfuscated ? "a" : "removePlayerFromTeam";
		final String CHATMETHOD_DESC = isObfuscated ? "(Ljava/lang/String;Laul;)V" : "(Ljava/lang/String;Lnet/minecraft/scoreboard/ScorePlayerTeam;)V";
		
		for (MethodNode method : scoreboardClass.methods) {
			if (method.name.equals(CHATMETHOD) && method.desc.equals(CHATMETHOD_DESC)) {
				AbstractInsnNode targetNode = null;
				for (AbstractInsnNode instruction : method.instructions.toArray()) {
					//System.out.println(instruction);
					if (instruction.getOpcode() == NEW) {
						System.out.println("A NODE TYPE: " + instruction.getType());
						if (((VarInsnNode) instruction).var == 0 &&
								instruction.getPrevious().getOpcode() == GETSTATIC &&
								instruction.getNext().getOpcode() == GETFIELD) {
							targetNode = instruction.getPrevious();
							break;
						}
					}
				}
				
				if (targetNode != null) {
					
					AbstractInsnNode afterReturn = targetNode;
					for (int i = 0; i < 7; i++) {
						afterReturn = afterReturn.getNext();
						//System.out.println("traversingNodes: " + afterReturn.getOpcode());
					}
					
					//System.out.println("after return node: " + afterReturn.getOpcode());
					
					LabelNode lNode = new LabelNode();
					
					InsnList toInsert = new InsnList();
					//toInsert.add(new VarInsnNode(ALOAD, 1));
					//toInsert.add(new LdcInsnNode("/"));
					//toInsert.add(new MethodInsnNode(INVOKEVIRTUAL, "java/lang/String", "startsWith", "(Ljava/lang/String;)Z", false));
					//toInsert.add(new JumpInsnNode(IFEQ, lNode));
					
					//method.instructions.insertBefore(targetNode, toInsert);
					//method.instructions.insert(afterReturn, lNode);
					
					System.out.println("inserting byte code into Scoreboard");
				}
				else {
					System.out.println("SCOREBOARD ASM TRANSFORM FAILED");
				}
			}
		}
	}
}
