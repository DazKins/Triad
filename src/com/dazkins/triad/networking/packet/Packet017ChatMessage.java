package com.dazkins.triad.networking.packet;

public class Packet017ChatMessage extends Packet
{
	private String msg;
	private String sender;
	
	public String getMsg()
	{
		return msg;
	}
	
	public void setMsg(String msg)
	{
		this.msg = msg;
	}
	
	public String getSender()
	{
		return sender;
	}
	
	public void setSender(String sender)
	{
		this.sender = sender;
	}
}