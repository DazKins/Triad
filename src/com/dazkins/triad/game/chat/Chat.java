package com.dazkins.triad.game.chat;

import java.util.ArrayList;

import com.dazkins.triad.networking.client.TriadClient;
import com.dazkins.triad.networking.client.update.ClientUpdateChatMessage;

public class Chat
{
	private ArrayList<ChatMessage> msgs;
	private TriadClient client;
	
	private boolean hasChatChanged;
	
	public Chat()
	{
		msgs = new ArrayList<ChatMessage>();
	}
	
	public void initClient(TriadClient c)
	{
		this.client = c;
	}
	
	public ArrayList<ChatMessage> getChatMessages()
	{
		return msgs;
	}
	
	public void sendMessage(String s)
	{
		client.sendMessage(s);
	}
	
	public boolean getAndPurgeHasChatChanged()
	{
		if (hasChatChanged)
		{
			hasChatChanged = false;
			return true;
		}
		return false;
	}
	
	public void tick()
	{
		if (client != null)
		{
			ArrayList<ClientUpdateChatMessage> newMsgs = (ArrayList<ClientUpdateChatMessage>) client.getClientUpdate().getAndPurgeUpdateListOfType(ClientUpdateChatMessage.class);
			if (newMsgs.size() != 0)
			{
				hasChatChanged = true;
				for (ClientUpdateChatMessage c : newMsgs)
				{
					msgs.add(c.getMessage());
				}
			}
		}
	}
}