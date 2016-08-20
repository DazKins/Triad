package com.dazkins.triad.networking.packet;

public class Packet021AbilityBar extends Packet
{
	private int gID;
	private int[] abilities;
	
	public void setAbilities(int[] i)
	{
		this.abilities = i;
	}
	
	public int[] getAbilities()
	{
		return this.abilities;
	}
	
	public void setGID(int i)
	{
		this.gID = i;
	}
	
	public int getGID()
	{
		return this.gID;
	}
}