package com.dazkins.triad.game.world;

import org.lwjgl.opengl.GL11;

import com.dazkins.triad.game.world.tile.Tile;
import com.dazkins.triad.gfx.BufferObject;
import com.dazkins.triad.gfx.Camera;
import com.dazkins.triad.gfx.Image;
import com.dazkins.triad.networking.client.ChunkData;

public class ChunkRenderer
{
	private IWorldAccess world;

	private ChunkData data;

	private boolean vboGenerated;

	private BufferObject tilePlane;

	public void initializeData(IWorldAccess w, ChunkData data)
	{
		this.data = data;
		vboGenerated = false;

		world = w;
	}

	public void markForRegeneration()
	{
		vboGenerated = false;
	}

	public void updateData(ChunkData d)
	{
		data = d;
		vboGenerated = false;
	}

	public int getChunkX()
	{
		return data.getCoords().getX();
	}

	public int getChunkY()
	{
		return data.getCoords().getY();
	}

	public void render(Camera cam)
	{
		if (!vboGenerated)
			generateVBO();
		GL11.glPushMatrix();
		GL11.glTranslatef(0, 0, Tile.yPosToDepthRelativeToCamera(cam, getChunkY() * Chunk.chunkS * Tile.tileSize));
		tilePlane.render();
		GL11.glPopMatrix();
	}

	public void generateVBO()
	{
		if (tilePlane != null)
			tilePlane.deleteBuffer();
		else
			tilePlane = new BufferObject(16 * 16 * 4 * 8 * 2 * 2);
		tilePlane.start();
		tilePlane.bindImage(Image.getImageFromName("spriteSheet"));
		for (int x = 0; x < Chunk.chunkS; x++)
		{
			for (int y = 0; y < Chunk.chunkS; y++)
			{
				int tileIndex = data.getTileData()[x + y * Chunk.chunkS];
				if (tileIndex != 0)
				{
					Tile.tiles[tileIndex].render(world, tilePlane, x * Tile.tileSize + (getChunkX() * Tile.tileSize * Chunk.chunkS), y * Tile.tileSize + (getChunkY() * Tile.tileSize * Chunk.chunkS));
				}
			}
		}
		tilePlane.stop();
		vboGenerated = true;
	}
}