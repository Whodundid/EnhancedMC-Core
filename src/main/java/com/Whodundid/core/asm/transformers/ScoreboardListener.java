package com.Whodundid.core.asm.transformers;

import com.Whodundid.core.asm.IETransformer;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.MethodNode;

public class ScoreboardListener extends IETransformer {
	
	@Override
	public String getClassName() { return "net.minecraft.scoreboard.Scoreboard"; }
	
	@Override
	protected void transform(ClassNode scoreboardClass) {
		final String METHOD = isObfuscated ? "a" : "removePlayerFromTeam";
		final String METHOD_DESC = isObfuscated ? "(Ljava/lang/String;Laul;)V" : "(Ljava/lang/String;Lnet/minecraft/scoreboard/ScorePlayerTeam;)V";
		
		for (MethodNode method : scoreboardClass.methods) {
			if (method.name.equals(METHOD) && method.desc.equals(METHOD_DESC)) {
				AbstractInsnNode targetNode = null;
				for (AbstractInsnNode instruction : method.instructions.toArray()) {
					
				}
			}
		}
	}
}
