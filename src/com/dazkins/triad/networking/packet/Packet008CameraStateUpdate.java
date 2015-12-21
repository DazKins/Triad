package com.dazkins.triad.networking.packet;

public class Packet008CameraStateUpdate extends Packet
{
	private float x0, y0;
	private float x1, y1;
	
	public float getX0()
	{
		return x0;
	}
	
	public void setX0(float x0)
	{
		this.x0 = x0;
	}
	
	public float getY0()
	{
		return y0;
	}
	
	public void setY0(float y0)
	{
		this.y0 = y0;
	}
	
	public float getX1()
	{
		return x1;
	}
	
	public void setX1(float x1)
	{
		this.x1 = x1;
	}
	
	public float getY1()
	{
		return y1;
	}
	
	public void setY1(float y1)
	{
		this.y1 = y1;
	}
}