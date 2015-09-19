package com.dazkins.triad.networking.packet;

public class Packet006EntityPositionUpdate extends Packet
{
	private int gID;
	private int tID;

	private float x;
	private float y;
	
	private int facing;
	
	public int getFacing()
	{
		return facing;
	}
	
	public void setFacing(int f)
	{
		facing = f;
	}

	public int gettID()
	{
		return tID;
	}

	public void settID(int i)
	{
		tID = i;
	}

	public int getgID()
	{
		return gID;
	}

	public void setgID(int gID)
	{
		this.gID = gID;
	}

	public float getX()
	{
		return x;
	}

	public void setX(float x)
	{
		this.x = x;
	}

	public float getY()
	{
		return y;
	}

	public void setY(float y)
	{
		this.y = y;
	}
}