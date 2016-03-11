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
	private static LoaderManager cg = new LoaderManager(5);
	
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
		needsUpdating = true;
	}
	
	public synchronized void setVBONeedsGenerating(boolean b)
	{
		vboNeedsGenerating = b;
	}

	public synchronized void setIsLoading(boolean b)
	{
		isLoading = b;
	}

	public void render(Camera cam)
	{
		if (needsUpdating && !isLoading && !vboNeedsGenerating)
		{
			if (cg.hasSpace())
			{
				setIsLoading(true);
				needsUpdating = false;
				cg.addLoadable(this);
			}
		}
		if (vboNeedsGenerating)
		{
			tilePlane.deleteBuffer();
			tilePlane.compile();
			setVBONeedsGenerating(false);
		}
		if (tilePlane != null && !vboNeedsGenerating)
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
		setIsLoading(false);
		setVBONeedsGenerating(true);
	}
}