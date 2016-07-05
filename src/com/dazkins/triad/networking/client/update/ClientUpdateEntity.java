package com.dazkins.triad.networking.client.update;

public class ClientUpdateEntity
{
	private int gID;
	private int tID;

	private float x;
	private float y;
	
	private int facing;
	
	private boolean remove;

	public ClientUpdateEntity(int gid, int tid, float x, float y, int f)
	{
		gID = gid;
		tID = tid;
		this.x = x;
		this.y = y;
		facing = f;
		remove = false;
	}
	
	//Sets up removal update
	public ClientUpdateEntity(int g)
	{
		gID = g;
		remove = true;
	}
	
	public boolean isRemove()
	{
		return remove;
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