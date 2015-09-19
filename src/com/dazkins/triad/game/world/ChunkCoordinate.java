package com.dazkins.triad.game.world;

public class ChunkCoordinate
{
	private int x;
	private int y;

	public ChunkCoordinate(int x, int y)
	{
		this.x = x;
		this.y = y;
	}

	public int getX()
	{
		return x;
	}

	public int getY()
	{
		return y;
	}

	public ChunkCoordinate addX(int x)
	{
		return new ChunkCoordinate(this.x + x, y);
	}

	public ChunkCoordinate addY(int y)
	{
		return new ChunkCoordinate(x, this.y + y);
	}

	// For weird mapping stuff...
	public int hashCode()
	{
		return x + y * 248569;
	}

	public boolean equals(Object o)
	{
		if (o == null)
			return false;
		ChunkCoordinate c = (ChunkCoordinate) o;
		return (c.x == this.x) && (c.y == this.y);
	}

	public String toString()
	{
		return "X : " + this.x + " Y : " + this.y;
	}
}