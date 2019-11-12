package com.Whodundid.core.asm;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.tree.ClassNode;

public abstract class IETransformer {

	public boolean isObfuscated = false;
	
	public byte[] start(byte[] classIn, boolean isObfuscatedIn) {
		isObfuscated = isObfuscatedIn;
		
		System.out.println("Attempting " + getClassName() + " transform");
		
		try {
			ClassNode node = new ClassNode();
			ClassReader reader = new ClassReader(classIn);
			reader.accept(node, 0);
			
			transform(node);
			
			ClassWriter writer = new ClassWriter(ClassWriter.COMPUTE_MAXS | ClassWriter.COMPUTE_FRAMES);
			node.accept(writer);
			byte[] ar = writer.toByteArray();
			return ar;
		}
		catch (Exception e) { e.printStackTrace(); }
		
		return classIn;
	}
	
	protected abstract void transform(ClassNode classObj);
	public abstract String getClassName();
}
