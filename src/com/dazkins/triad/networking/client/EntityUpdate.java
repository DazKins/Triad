package com.dazkins.triad.networking.client;

public class EntityUpdate
{
	private int gID;
	private int tID;

	private float x;
	private float y;
	
	private int facing;

	public EntityUpdate(int gid, int tid, float x, float y, int f)
	{
		gID = gid;
		tID = tid;
		this.x = x;
		this.y = y;
		facing = f;
	}
	
	public int getFacing()
	{
		return facing;
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