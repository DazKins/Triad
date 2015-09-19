package com.dazkins.triad.networking.packet;

public class Packet001LoginRequest extends Packet
{
	private String username;

	public void setUsername(String s)
	{
		username = s;
	}

	public String getUsername()
	{
		return username;
	}
}