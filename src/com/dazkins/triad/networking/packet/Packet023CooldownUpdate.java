package com.dazkins.triad.networking.packet;

public class Packet023CooldownUpdate extends Packet
{
	private int gID;
	private int abilityNumber;
	private int cooldownRemaining;
	
	public int getgID()
	{
		return gID;
	}
	
	public void setgID(int gID)
	{
		this.gID = gID;
	}
	
	public int getAbilityNumber()
	{
		return abilityNumber;
	}
	
	public void setAbilityNumber(int abilityNumber)
	{
		this.abilityNumber = abilityNumber;
	}
	
	public int getCooldownRemaining()
	{
		return cooldownRemaining;
	}
	
	public void setCooldownRemaining(int cooldownRemaining)
	{
		this.cooldownRemaining = cooldownRemaining;
	}
}