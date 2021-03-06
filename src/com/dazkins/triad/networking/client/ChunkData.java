package com.dazkins.triad.networking.client;

import com.dazkins.triad.game.world.Chunk;
import com.dazkins.triad.game.world.ChunkCoordinate;
import com.dazkins.triad.game.world.tile.Tile;
import com.dazkins.triad.gfx.Color;

public class ChunkData
{
	private ChunkCoordinate coords;
	
	private byte[] tileData;
	private Color[] light;

	public ChunkData(ChunkCoordinate c, byte[] data, Color[] col)
	{
		coords = c;
		tileData = data;
		light = col;
	}

	public ChunkData(ChunkCoordinate c, byte[] data, byte[] col)
	{
		coords = c;
		tileData = data;
		decompressLight(col);
	}

	public ChunkData(ChunkCoordinate c)
	{
		coords = c;
		tileData = new byte[Chunk.CHUNKSS];
		light = new Color[Chunk.CHUNKSS];
	}
	
	public void setTileData(byte[] d)
	{
		tileData = d;
	}
	
	public void setLightData(Color[] c)
	{
		light = c;
	}
	
	public ChunkData copyOf()
	{
		byte td[] = new byte[tileData.length];
		for (int i = 0; i < td.length; i++)
		{
			td[i] = tileData[i];
		}
		
		Color cd[] = new Color[light.length];
		for (int i = 0; i < cd.length; i++)
		{
			cd[i] = light[i].copyOf();
		}
		
		return new ChunkData(coords, td, cd);
	}

	public ChunkCoordinate getCoords()
	{
		return coords;
	}
	
	public ChunkCoordinate getTileCoords()
	{
		return coords.multiply(Chunk.CHUNKS);
	}
	
	public ChunkCoordinate getWorldCoords()
	{
		return coords.multiply(Tile.TILESIZE * Chunk.CHUNKS);
	}

	public byte[] getTileData()
	{
		return tileData;
	}

	public Color[] getLight()
	{
		return light;
	}

	private void decompressLight(byte[] l)
	{
		light = new Color[Chunk.CHUNKSS];
		for (int i = 0; i < l.length; i += 3)
		{
			int index = i / 3;
			byte r = l[i];
			byte g = l[i + 1];
			byte b = l[i + 2];
			light[index] = new Color(r, g, b);
		}
	}

	// For packet sending
	public byte[] compressLight()
	{
		byte[] r = new byte[Chunk.CHUNKSS * 3];

		for (int i = 0; i < light.length; i++)
		{
			Color c = light[i];
			r[i * 3] = (byte) c.getR();
			r[i * 3 + 1] = (byte) c.getG();
			r[i * 3 + 2] = (byte) c.getB();
		}

		return r;
	}
	
	public static byte[] compressLight(Color[] cs)
	{
		byte[] r = new byte[Chunk.CHUNKSS * 3];

		for (int i = 0; i < cs.length; i++)
		{
			Color c = cs[i];
			r[i * 3] = (byte) c.getR();
			r[i * 3 + 1] = (byte) c.getG();
			r[i * 3 + 2] = (byte) c.getB();
		}

		return r;
	}
}