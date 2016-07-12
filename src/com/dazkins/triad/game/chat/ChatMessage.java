package com.dazkins.triad.game.chat;

public class ChatMessage
{
	private String msg;
	private	String sender;
	
	public ChatMessage(String m, String s)
	{
		this.msg = m;
		this.sender = s;
	}
	
	public String getMessage()
	{
		return msg;
	}
	
	public String getSender()
	{
		return sender;
	}
}