package com.dazkins.triad.networking.client;

public class ChunkUpdate
{
	private ChunkData data;
	
	private boolean updateTiles;
	private boolean updateLight;
	
	public ChunkUpdate(ChunkData d, boolean tiles, boolean light)
	{
		data = d;
		updateTiles = tiles;
		updateLight = light;
	}
	
	public ChunkData getData()
	{
		return data;
	}
	
	public boolean updateTiles()
	{
		return updateTiles;
	}
	
	public boolean updateLight()
	{
		return updateLight;
	}
}