package com.dazkins.triad.networking.packet;

public class Packet020UseAbility extends Packet
{
	private int abilityNo;
	
	public void setAbilityNo(int n)
	{
		this.abilityNo = n;
	}
	
	public int getAbilityNo()
	{
		return this.abilityNo;
	}
}