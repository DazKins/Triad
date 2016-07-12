package com.dazkins.triad.networking.server.update;

public class ServerUpdateChatMessage extends ServerUpdate
{
	private String message;
	private String sender;
	
	public ServerUpdateChatMessage(String s, String m)
	{
		this.message = m;
		this.sender = s;
	}
	
	public String getMessage()
	{
		return message;
	}
	
	public String getSender()
	{
		return sender;
	}
}
