package com.dazkins.triad.game.world;

import java.util.ArrayList;

import com.dazkins.triad.game.entity.Entity;
import com.dazkins.triad.game.entity.mob.EntityZombie;
import com.dazkins.triad.game.world.tile.Tile;
import com.dazkins.triad.gfx.Color;
import com.dazkins.triad.math.AABB;
import com.dazkins.triad.math.MathHelper;
import com.dazkins.triad.networking.client.ChunkData;
import com.dazkins.triad.networking.packet.Packet003ChunkData;
import com.dazkins.triad.util.Loadable;
import com.dazkins.triad.util.LoaderManager;
import com.dazkins.triad.util.TriadLogger;

public class Chunk implements Loadable
{
	public static final int CHUNKS = 16;
	public static final int CHUNKSS = CHUNKS * CHUNKS;

	private static float lFadeOut = 0.85f;

	private World world;
	
	private ChunkData data;
	
	// Store light value from the previous tick
	private Color[] pTileColors;

	private boolean tilesGenerated = false;

	private boolean isBeingLoaded;

	private ArrayList<Entity>[] entitiesInTiles;
	private int entityCount;

	public Chunk(World w, ChunkCoordinate c)
	{
		data = new ChunkData(c);
		
		this.world = w;

		pTileColors = new Color[CHUNKSS];

		resetLightLevels();

		entitiesInTiles = new ArrayList[CHUNKSS];
		for (int i = 0; i < CHUNKSS; i++)
		{
			entitiesInTiles[i] = new ArrayList<Entity>();
		}
	}
	
	public ChunkData getData()
	{
		return data;
	}
	
	public Packet003ChunkData compressToPacket() 
	{
		Packet003ChunkData p = new Packet003ChunkData();
		p.setTiles(data.getTileData());
		p.setLight(ChunkData.compressLight(data.getLight()));
		p.setX(data.getCoords().getX());
		p.setY(data.getCoords().getY());
		return p;
	}

	public boolean addToLoader(LoaderManager l)
	{
		if (!isBeingLoaded)
		{
			if (l.addLoadable(this)) 
			{
				isBeingLoaded = true;
				return true;
			}
		}
		return false;
	}

	public void addEntity(Entity e)
	{
		int xx = (int) (e.getX() / Tile.TILESIZE);
		int yy = (int) (e.getY() / Tile.TILESIZE);
		
		int x0 = data.getTileCoords().getX();
		int y0 = data.getTileCoords().getY();
		int x1 = x0 + CHUNKS;
		int y1 = y0 + CHUNKS;

		if (xx < x0 || yy < y0 || xx >= x1|| yy >= y1)
		{
			TriadLogger.log("Attempted to add entity that was out of range of the chunk boundaries!", true);
			return;
		}

		entityCount++;

		entitiesInTiles[MathHelper.convertToChunkTileX(xx) + MathHelper.convertToChunkTileY(yy) * CHUNKS].add(e);
	}

	public ArrayList<Entity> getEntitiesInTile(int tx, int ty)
	{
		return entitiesInTiles[tx + ty * CHUNKS];
	}

	public ArrayList<Entity> getEntities()
	{
		ArrayList<Entity> ents = new ArrayList<Entity>();
		for (ArrayList<Entity> e : entitiesInTiles)
		{
			ents.addAll(e);
		}
		return ents;
	}

	public void generateTileMap()
	{
		for (int x = 0; x < CHUNKS; x++)
		{
			for (int y = 0; y < CHUNKS; y++)
			{
				world.getWorldGenerator().generate(x + data.getTileCoords().getX(), y + data.getTileCoords().getY());
			}
		}

		tilesGenerated = true;
	}

	private boolean isValidTilePos(int x, int y)
	{
		if (x < 0 || y < 0 || x >= CHUNKS || y >= CHUNKS)
			return false;
		return true;
	}

	public Color getTileColor(int x, int y)
	{
		if (!isValidTilePos(x, y))
		{
			return new Color(0);
		}
		return data.getLight()[x + y * CHUNKS];
	}

	public void blendTileColor(Color c, int x, int y)
	{
		if (!isValidTilePos(x, y))
			return;
		data.getLight()[x + y * CHUNKS].blend(c);
	}

	public void setTile(Tile t, int x, int y)
	{
		if (getTile(x, y) != t)
		{
			if (!isValidTilePos(x, y))
				return;
			data.getTileData()[x + y * CHUNKS] = t.getID();
		}
	}

	public Tile getTile(int x, int y)
	{
		if (!isValidTilePos(x, y))
			return null;
		return Tile.tiles[data.getTileData()[x + y * CHUNKS]];
	}

	private void resetLightLevels()
	{
		Color col = world.getAmbientLight();
		int r = (int) (col.getR() * lFadeOut);
		int g = (int) (col.getG() * lFadeOut);
		int b = (int) (col.getB() * lFadeOut);
		Color[] c = data.getLight();
		for (int i = 0; i < c.length; i++)
		{
			c[i] = new Color(r, g, b);
		}
	}

	public AABB getBounds()
	{
		int x = data.getCoords().getX();
		int y = data.getCoords().getY();
		return new AABB(x * Tile.TILESIZE * CHUNKS, y * Tile.TILESIZE * CHUNKS, x * Tile.TILESIZE * CHUNKS + (CHUNKS * Tile.TILESIZE), y * Tile.TILESIZE * CHUNKS + (CHUNKS * Tile.TILESIZE));
	}

	public synchronized boolean isLoaded()
	{
		return tilesGenerated;
	}

	public void markForTileGeneration()
	{
		tilesGenerated = false;
	}

	public void tick()
	{
		for (int x = 0; x < CHUNKS; x++)
		{
			for (int y = 0; y < CHUNKS; y++)
			{
				if (getTileColor(x, y).getBrightness() < 125 && Math.random() * 20000000.0f < 1)
				{
					if (entityCount < 1)
					{
						world.addEntity(new EntityZombie(world, data.getWorldCoords().getX(), data.getWorldCoords().getY()));
					}
				}
			}
		}
		
		for (int i = 0; i < CHUNKSS; i++)
		{
			ArrayList<Entity> es = entitiesInTiles[i];
			for (int u = 0; u < es.size(); u++)
			{
				Entity e = es.get(u);

				if (e.needsToBeRemoved())
				{
					es.remove(e);
					entityCount--;
					world.getServer().registerEntityRemoval(e);
					continue;
				}

				int x0 = (int) (e.getX() / Tile.TILESIZE);
				int y0 = (int) (e.getY() / Tile.TILESIZE);

				if (!world.entityHasBeenTicked(e))
				{
					e.tick();
				}

				int x1 = (int) (e.getX() / Tile.TILESIZE);
				int y1 = (int) (e.getY() / Tile.TILESIZE);

				if (x0 != x1 || y0 != y1)
				{
					es.remove(e);
					entityCount--;
					world.addEntity(e);
					u--;
				}
			}
		}

		Color wc = world.getAmbientLight();
		int r = (int) (wc.getR() * lFadeOut);
		int g = (int) (wc.getG() * lFadeOut);
		int b = (int) (wc.getB() * lFadeOut);
		Color[] col = data.getLight();
		for (int i = 0; i < col.length; i++)
		{
			Color c = col[i];
			if (c.getR() * lFadeOut > r)
			{
				c.setR((int) (c.getR() * lFadeOut));
			} else if (c.getR() > r)
			{
				c.setR(r);
			}
			if (c.getG() * lFadeOut > g)
			{
				c.setG((int) (c.getG() * lFadeOut));
			} else if (c.getG() > g)
			{
				c.setG(g);
			}
			if (c.getB() * lFadeOut > b)
			{
				c.setB((int) (c.getB() * lFadeOut));
			} else if (c.getB() > b)
			{
				c.setB(b);
			}
			if (c.getR() < r)
			{
				c.setR(r);
			}
			if (c.getG() < g)
			{
				c.setG(g);
			}
			if (c.getB() < b)
			{
				c.setB(b);
			}
		}
	}

	public void postTick()
	{
		Color[] col = data.getLight();
		for (int i = 0; i < col.length; i++)
		{
			pTileColors[i] = col[i].copyOf();
		}
	}

	public boolean hasLightChanged()
	{
		Color[] col = data.getLight();
		for (int i = 0; i < col.length; i++)
		{
			Color c = col[i];
			Color pc = pTileColors[i];
			if (!c.equals(pc))
				return true;
		}
		return false;
	}

	public void load()
	{
		if (!isLoaded())
		{
			this.generateTileMap();
		}
	}
}