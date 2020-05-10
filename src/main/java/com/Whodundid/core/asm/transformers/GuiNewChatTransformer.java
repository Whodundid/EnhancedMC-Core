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

//Author: Hunter Bragg

public class GuiNewChatTransformer extends IETransformer {
	
	@Override
	public String getClassName() { return "net.minecraft.client.gui.GuiNewChat"; }
	
	@Override
	public void transform(ClassNode classIn) {
		if (historyDisplay(classIn)) { System.out.println("EMC: GuiNewChat transform successful!"); }
		else { System.err.println("EMC: GUINEWCHAT ASM TRANSFORM FAILED!"); }
	}
	
	//------------
	//Transformers
	//------------
	
	private boolean chatGrabber(ClassNode classIn) {
		final String CHATMETHOD = isObfuscated ? "a" : "printChatMessageWithOptionalDeletion";
		final String CHATMETHOD_DESC = isObfuscated ? "(Leu;I)V" : "(Lnet/minecraft/util/IChatComponent;I)V";
		
		System.out.println("EMC: Starting ChatGrabber ASM");
		System.out.println("EMC: obfucated? " + isObfuscated);
		System.out.println("EMC: methodName: " + CHATMETHOD + " ; description: " + CHATMETHOD_DESC);
		
		for (MethodNode method : classIn.methods) {
			
			if (method.name.equals(CHATMETHOD) && method.desc.equals(CHATMETHOD_DESC)) {
				
				System.out.println("EMC: Found method: " + method);
				
				AbstractInsnNode targetNode = null;
				for (AbstractInsnNode instruction : method.instructions.toArray()) {
					if (instruction.getOpcode() == INVOKESPECIAL) {
						if (instruction.getPrevious() instanceof InsnNode && instruction.getNext() instanceof LabelNode) {
							targetNode = instruction;
							break;
						}
					}
				}
				
				String Minecraft = isObfuscated ? "ave" : "net/minecraft/client/Minecraft";
				String getMinecraft = isObfuscated? "A" : "getMinecraft";
				String ingameGUI = isObfuscated? "q" : "ingameGUI";
				String GuiInGame = isObfuscated ? "avo" : "net/minecraft/client/gui/GuiIngame";
				String getUpdateCounter = isObfuscated? "e" : "getUpdateCounter";
				String IChatComponent = isObfuscated? "eu" : "net/minecraft/util/IChatComponent";
				
				System.out.println("EMC: ChatGrabber target node: " + targetNode);
				
				if (targetNode != null) {
					InsnList toInsert = new InsnList();
					
					toInsert.add(new LabelNode());
					toInsert.add(new FieldInsnNode(GETSTATIC, "net/minecraftforge/common/MinecraftForge", "EVENT_BUS", "Lnet/minecraftforge/fml/common/eventhandler/EventBus;"));
					toInsert.add(new TypeInsnNode(NEW, "com/Whodundid/core/coreEvents/emcEvents/ChatLineCreatedEvent"));
					toInsert.add(new InsnNode(DUP));
					toInsert.add(new TypeInsnNode(NEW, "com/Whodundid/core/util/chatUtil/TimedChatLine"));
					toInsert.add(new InsnNode(DUP));
					toInsert.add(new MethodInsnNode(INVOKESTATIC, Minecraft, getMinecraft, "()" + Minecraft + ";", false));
					toInsert.add(new FieldInsnNode(GETFIELD, Minecraft, ingameGUI, "L" + GuiInGame + ";"));
					toInsert.add(new MethodInsnNode(INVOKEVIRTUAL, GuiInGame, getUpdateCounter, "()I", false));
					toInsert.add(new VarInsnNode(ALOAD, 1));
					toInsert.add(new VarInsnNode(ILOAD, 2));
					toInsert.add(new MethodInsnNode(INVOKESPECIAL, "com/Whodundid/core/util/chatUtil/TimedChatLine", "<init>", "(IL" + IChatComponent + ";I)V", false));
					toInsert.add(new MethodInsnNode(INVOKESPECIAL, "com/Whodundid/core/coreEvents/emcEvents/ChatLineCreatedEvent", "<init>", "(Lcom/Whodundid/core/util/chatUtil/TimedChatLine;)V", false));
					toInsert.add(new MethodInsnNode(INVOKEVIRTUAL, "net/minecraftforge/fml/common/eventhandler/EventBus", "post", "(Lnet/minecraftforge/fml/common/eventhandler/Event;)Z", false));
					toInsert.add(new InsnNode(POP));
					
					method.instructions.insert(targetNode, toInsert);
					System.out.println("EMC: ChatGrabber successful!");
					return true;
				}
				
			}
		}
		
		System.err.println("EMC: ChatGrabber FAILED!");
		return false;
	}
	
	private boolean historyDisplay(ClassNode classIn) {
		final String METHOD = isObfuscated ? "e" : "getChatOpen";
		final String METHOD_DESC = isObfuscated ? "()Z" : "()Z";
		
		System.out.println("EMC: Starting HistoryDisplay ASM");
		
		for (MethodNode method : classIn.methods) {
			if (method.name.equals(METHOD) && method.desc.equals(METHOD_DESC) && method.instructions.toArray().length > 0) {
				AbstractInsnNode targetNode = method.instructions.toArray()[0];
				
				if (targetNode != null) {
					
					//remove old code
					for (int i = 0; i < 6; i++) {
						targetNode = targetNode.getNext();
						method.instructions.remove(targetNode.getPrevious());
					}
					
					//insert new code
					InsnList toInsert = new InsnList();
					
					//redirect boolean return to EMC core
					toInsert.add(new MethodInsnNode(INVOKESTATIC, "com/Whodundid/core/coreApp/CoreApp", "getChatOpen", "()Z", false));
					
					method.instructions.insertBefore(targetNode, toInsert);
					
					System.out.println("EMC: HistoryDisplay successful!");
					return true;
				}
				
			}
		}
		
		System.err.println("EMC: HistoryDisplay FAILED!");
		return false;
	}
}
