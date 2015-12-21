package com.dazkins.triad.networking.packet;

public class Packet011PlayerVelocity extends Packet
{
	private float xa;
	private float ya;
	
	public float getXa()
	{
		return xa;
	}
	
	public void setXa(float xa)
	{
		this.xa = xa;
	}
	
	public float getYa()
	{
		return ya;
	}
	
	public void setYa(float ya)
	{
		this.ya = ya;
	}
}