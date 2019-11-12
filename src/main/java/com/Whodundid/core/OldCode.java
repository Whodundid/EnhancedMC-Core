package com.Whodundid.core;

public class OldCode {
	/*
	final Node<K, V> getNode(int hash, Object key) {
		
        Node<K, V>[] tab = table;
        Node<K, V> first = tab[(tab.length - 1) & hash];
        Node<K, V> next;
        
        if (tab != null && tab.length > 0 && first != null) {
            if (first.hash == hash && (first.key == key || (key != null && key.equals(first.key)))) { // always check first node
            	next = first.next;
            	if (next != null) {
                    if (first instanceof TreeNode) { return ((TreeNode<K, V>) first).getTreeNode(hash, key); }
                    do {
                        if (next.hash == hash && (next.key == key || (key != null && key.equals(next.key)))) { return next; }
                        next = next.next;
                    } while (next != null);
                }
            }
        }
        return null;
    }
	
	
	public void printChatMessageWithOptionalDeletion(IChatComponent chatComponent, int chatLineId) {
		MinecraftForge.EVENT_BUS.post(new ChatLineCreatedEvent(new TimedChatLine(mc.ingameGUI.getUpdateCounter(), chatComponent, chatLineId)));
		String logPrint = chatComponent.getUnformattedText();
		ChatComponentTranslation ret = new ChatComponentTranslation("commands.generic.notFound");
    	ret.getChatStyle().setColor(EnumChatFormatting.RED);
		if (!EChatUtil.getLastTypedMessage().isEmpty() && chatComponent.getFormattedText().equals(ret.getFormattedText())) {
			IChatComponent noCommand = ChatBuilder.of(EnumChatFormatting.RED + "Command: " +
						EnumChatFormatting.YELLOW + EChatUtil.getLastTypedMessage() + 
						EnumChatFormatting.RED +  " not found. Try /help for a list of commands").build();
			logPrint = noCommand.getUnformattedText();
			setChatLine(noCommand, chatLineId, mc.ingameGUI.getUpdateCounter(), false);
			EChatUtil.setLastTypedMessage("");
		}
		else {
			setChatLine(chatComponent, chatLineId, mc.ingameGUI.getUpdateCounter(), false);
		}
		logger.info("[CHAT] " + logPrint);
	}
	
	*/
}
