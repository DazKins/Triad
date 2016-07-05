package com.dazkins.triad.networking.client;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.dazkins.triad.game.world.Chunk;
import com.dazkins.triad.game.world.ChunkCoordinate;
import com.dazkins.triad.game.world.ChunkRenderer;
import com.dazkins.triad.game.world.IWorldAccess;
import com.dazkins.triad.game.world.tile.Tile;
import com.dazkins.triad.gfx.Camera;
import com.dazkins.triad.gfx.Color;
import com.dazkins.triad.math.AABB;
import com.dazkins.triad.math.MathHelper;
import com.dazkins.triad.networking.client.update.ClientUpdateChunk;

public class ClientChunkManager implements IWorldAccess
{
	private Map<ChunkCoordinate, ChunkData> data = new HashMap<ChunkCoordinate, ChunkData>();

	private Map<ChunkCoordinate, ChunkRenderer> renderers = new HashMap<ChunkCoordinate, ChunkRenderer>();
	private ArrayList<ChunkCoordinate> requests = new ArrayList<ChunkCoordinate>();
	
	private Map<ChunkCoordinate, ArrayList<ClientUpdateChunk>> chunkUpdates = new HashMap<ChunkCoordinate, ArrayList<ClientUpdateChunk>>();

	public void handleChunkUpdate(ClientUpdateChunk d)
	{
		if (d != null)
		{
			ArrayList<ClientUpdateChunk> ups = chunkUpdates.get(d.getData().getCoords());
			
			if (ups == null)
			{
				chunkUpdates.put(d.getData().getCoords(), new ArrayList<ClientUpdateChunk>());
			}
			
			ups = chunkUpdates.get(d.getData().getCoords());
			if (ups.size() > 4)
				ups.remove(0);
				
			chunkUpdates.get(d.getData().getCoords()).add(d);
		}
	}
	
	public void handleChunkUpdate(ChunkCoordinate coords) 
	{
		ArrayList<ClientUpdateChunk> ups = chunkUpdates.get(coords);
		
//		if (ups.size() > 10)
//			System.out.println(ups.size());
		
		for (ClientUpdateChunk d : ups)
		{
			if (data.containsKey(coords))
			{
				if (d.updateLight())
					data.get(coords).setLightData(d.getData().getLight());
				if (d.updateTiles())
					data.get(coords).setTileData(d.getData().getTileData());
				ChunkRenderer r = renderers.get(coords);
				r.updateData(data.get(coords));
			} else
			{
				if (d.updateTiles())
				{
					data.put(coords, d.getData());
					//Just in case a deprecated chunk renderer got left behind
					renderers.remove(coords);
					ChunkRenderer nr = new ChunkRenderer();
					nr.initializeData(this, data.get(coords));
					renderers.put(coords, nr);
				}
			}
		}
		
		if (requests.contains(coords))
			requests.remove(coords);

		int x0 = coords.getX() - 1;
		int y0 = coords.getY() - 1;
		int x1 = x0 + 2;
		int y1 = y0 + 2;

		for (int x = x0; x <= x1; x++)
		{
			for (int y = y0; y <= y1; y++)
			{
				ChunkRenderer r = renderers.get(new ChunkCoordinate(x, y));
				if (r != null)
				{
					r.handleUpdate();
				}
			}
		}
		
		ArrayList<ClientUpdateChunk> aUps = chunkUpdates.get(coords);
		if (aUps != null)
			aUps.clear();
	}

	public ArrayList<ChunkCoordinate> getRequestsForMissingChunks()
	{
		return requests;
	}

	public void render(Camera cam)
	{
		AABB b = cam.getViewportBounds();
		float x0 = b.getX0();
		float x1 = b.getX1();
		float y0 = b.getY0();
		float y1 = b.getY1();

		float div = Chunk.CHUNKS * Tile.TILESIZE;

		int ix0 = (int) Math.floor((x0 / div) - 2.0f);
		int ix1 = (int) Math.floor((x1 / div) + 2.0f);
		int iy0 = (int) Math.floor((y0 / div) - 2.0f);
		int iy1 = (int) Math.floor((y1 / div) + 2.0f);
		
		int chunkUpdatesHandled = 0;
		
		for (int x = ix0; x < ix1; x++)
		{
			for (int y = iy0; y < iy1; y++)
			{
				ChunkCoordinate c = new ChunkCoordinate(x, y);
				if (chunkUpdates.containsKey(c)) 
				{
					if (chunkUpdates.get(c).size() != 0)
					{
						handleChunkUpdate(c);
						chunkUpdatesHandled++;
					}
				}
				ChunkData cd = data.get(c);
				if (cd == null)
				{
					if (!requests.contains(c))
					{
						requests.add(c);
					}
				} else
				{
					ChunkRenderer r = renderers.get(c);
					r.render(cam);
				}
			}
		}
		
		if (chunkUpdatesHandled < 100)
		{
			for (Map.Entry<ChunkCoordinate, ArrayList<ClientUpdateChunk>> entry : chunkUpdates.entrySet())
			{
				ChunkCoordinate c = entry.getKey();
				ArrayList<ClientUpdateChunk> cs = entry.getValue();
				if (cs.size() != 0)
				{
					handleChunkUpdate(c);
					chunkUpdatesHandled++;
				}
				if (chunkUpdatesHandled > 100)
					break;
			}
		}
	}

	public Tile getTile(int x, int y)
	{
		int cx = MathHelper.getChunkXFromTileX(x);
		int cy = MathHelper.getChunkYFromTileY(y);

		int rx = MathHelper.convertToChunkTileX(x);
		int ry = MathHelper.convertToChunkTileY(y);

		ChunkCoordinate c = new ChunkCoordinate(cx, cy);

		ChunkData d = data.get(c);

		if (d == null)
		{
			return null;
		} else
		{
			return Tile.tiles[d.getTileData()[rx + ry * Chunk.CHUNKS]];
		}
	}

	public Color getTileColor(int x, int y)
	{
		int cx = MathHelper.getChunkXFromTileX(x);
		int cy = MathHelper.getChunkYFromTileY(y);

		int rx = MathHelper.convertToChunkTileX(x);
		int ry = MathHelper.convertToChunkTileY(y);

		ChunkCoordinate c = new ChunkCoordinate(cx, cy);

		ChunkData d = data.get(c);

		if (d == null)
		{
			return new Color(1, 1, 1);
		} else
		{
			return d.getLight()[rx + ry * Chunk.CHUNKS];
		}
	}
}