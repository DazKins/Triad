package com.dazkins.triad.networking.packet;

public class Packet002ChunkDataRequest extends Packet
{
	private int cx;
	private int cy;

	public int getCX()
	{
		return cx;
	}

	public void setCX(int cx)
	{
		this.cx = cx;
	}

	public int getCY()
	{
		return cy;
	}

	public void setCY(int cy)
	{
		this.cy = cy;
	}
}