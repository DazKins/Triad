package com.dazkins.triad.networking.packet;

public class Packet005UpdatePlayerPosition extends Packet
{
	private float x;
	private float y;

	private float xa;
	private float ya;

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

	public float getXA()
	{
		return xa;
	}

	public void setXA(float xa)
	{
		this.xa = xa;
	}

	public float getYA()
	{
		return ya;
	}

	public void setYA(float ya)
	{
		this.ya = ya;
	}
}