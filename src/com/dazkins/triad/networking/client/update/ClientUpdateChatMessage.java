package com.dazkins.triad.networking.client.update;

import com.dazkins.triad.game.chat.ChatMessage;

public class ClientUpdateChatMessage extends ClientUpdate
{
	private ChatMessage msg;
	
	public ClientUpdateChatMessage(ChatMessage m)
	{
		this.msg = m;
	}
	
	public ChatMessage getMessage()
	{
		return msg;
	}
}
