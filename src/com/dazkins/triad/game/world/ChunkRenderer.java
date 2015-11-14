package com.dazkins.triad.game.world;

import org.lwjgl.opengl.GL11;

import com.dazkins.triad.game.world.tile.Tile;
import com.dazkins.triad.gfx.BufferObject;
import com.dazkins.triad.gfx.Camera;
import com.dazkins.triad.gfx.Image;
import com.dazkins.triad.networking.client.ChunkData;
import com.dazkins.triad.util.Loadable;
import com.dazkins.triad.util.LoaderManager;

public class ChunkRenderer implements Loadable
{
	private static LoaderManager cg = new LoaderManager(1);
	
	private IWorldAccess world;

	private ChunkData data;

	private boolean vboNeedsGenerating;
	private boolean needsUpdating;
	private boolean isLoading;

	private BufferObject tilePlane;

	public void initializeData(IWorldAccess w, ChunkData data)
	{
		this.data = data;
		world = w;
		tilePlane = new BufferObject(16 * 16 * 4 * 9);
	}

	public void updateData(ChunkData d)
	{
		data = d;
		handleUpdate();
	}

	public int getChunkX()
	{
		return data.getCoords().getX();
	}

	public int getChunkY()
	{
		return data.getCoords().getY();
	}
	
	public void handleUpdate() 
	{
		if (!isLoading)
			needsUpdating = true;
	}

	public void render(Camera cam)
	{
		if (needsUpdating && !isLoading)
		{
			if (cg.hasSpace())
			{
				cg.addLoadable(this);
				isLoading = true;
				needsUpdating = false;
			}
		}
		if (vboNeedsGenerating)
		{
			tilePlane.deleteBuffer();
			tilePlane.compile();
			vboNeedsGenerating = false;
		}
		if (tilePlane != null)
		{
			GL11.glPushMatrix();
			GL11.glTranslatef(0, 0, Tile.yPosToDepthRelativeToCamera(cam, getChunkY() * Chunk.CHUNKS * Tile.TILESIZE));
			tilePlane.render();
			GL11.glPopMatrix();
		}
	}

	public void load()
	{
		tilePlane.resetData();
		tilePlane.getData().bindImage(Image.getImageFromName("spriteSheet"));
		for (int x = 0; x < Chunk.CHUNKS; x++)
		{
			for (int y = 0; y < Chunk.CHUNKS; y++)
			{
				int tileIndex = data.getTileData()[x + y * Chunk.CHUNKS];
				if (tileIndex != 0)
				{
					Tile.tiles[tileIndex].render(world, tilePlane.getData(), x * Tile.TILESIZE + (getChunkX() * Tile.TILESIZE * Chunk.CHUNKS), y * Tile.TILESIZE + (getChunkY() * Tile.TILESIZE * Chunk.CHUNKS));
				}
			}
		}
		vboNeedsGenerating = true;
		isLoading = false;
	}
}