package com.dazkins.triad.networking.client.update;

import com.dazkins.triad.networking.client.ChunkData;

public class ClientUpdateChunk
{
	private ChunkData data;
	
	private boolean updateTiles;
	private boolean updateLight;
	
	public ClientUpdateChunk(ChunkData d, boolean tiles, boolean light)
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