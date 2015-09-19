package com.dazkins.triad.networking.packet;

public class Packet000RawMessage extends Packet
{
	private String msg;

	public void setMessage(String s)
	{
		msg = s;
	}

	public String getMsg()
	{
		return msg;
	}
}