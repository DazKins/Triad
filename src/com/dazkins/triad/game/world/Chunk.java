package com.dazkins.triad.game.world;

import java.util.ArrayList;

import org.lwjgl.opengl.GL11;

import com.dazkins.triad.game.entity.Entity;
import com.dazkins.triad.game.entity.EntityFlower;
import com.dazkins.triad.game.entity.EntitySandGrass;
import com.dazkins.triad.game.entity.harvestable.EntityTree;
import com.dazkins.triad.game.entity.mob.EntityZombie;
import com.dazkins.triad.game.world.tile.Tile;
import com.dazkins.triad.gfx.BufferObject;
import com.dazkins.triad.gfx.Color;
import com.dazkins.triad.gfx.Image;
import com.dazkins.triad.math.AABB;
import com.dazkins.triad.math.MathHelper;
import com.dazkins.triad.networking.client.ChunkData;
import com.dazkins.triad.networking.packet.Packet003ChunkData;
import com.dazkins.triad.networking.packet.Packet006EntityPositionUpdate;
import com.dazkins.triad.util.ChunkLoader;
import com.dazkins.triad.util.Loadable;

public class Chunk implements Loadable
{
	public static int chunkS = 16;
	public static int chunkSS = chunkS * chunkS;

	private static float lFadeOut = 0.85f;

	// Individual for each chunk
	private ChunkCoordinate chunkCoord;

	private World world;

	public int chunkX, chunkY;
	public int rChunkX, rChunkY;
	private byte[] tiles;

	private Color[] tileColors;
	// Store light value from the previous tick
	private Color[] pTileColors;

	private boolean tilesGenerated;

	private boolean isBeingLoaded;

	private ArrayList<Entity>[] entitiesInTiles;
	private int entityCount;

	public Chunk(World w, int xp, int yp)
	{
		this.chunkX = xp;
		this.chunkY = yp;
		this.world = w;

		this.rChunkX = chunkX * chunkS;
		this.rChunkY = chunkY * chunkS;

		tileColors = new Color[chunkSS];
		pTileColors = new Color[chunkSS];

		resetLightLevels();

		tiles = new byte[chunkSS];
		entitiesInTiles = new ArrayList[chunkSS];
		for (int i = 0; i < chunkSS; i++)
		{
			entitiesInTiles[i] = new ArrayList<Entity>();
		}

		chunkCoord = new ChunkCoordinate(xp, yp);
	}

	public ChunkCoordinate getChunkCoord()
	{
		return chunkCoord;
	}

	public byte[] getTiles()
	{
		return tiles;
	}

	public Color[] getLights()
	{
		return tileColors;
	}
	
	public Packet003ChunkData compressToPacket() 
	{
		Packet003ChunkData p = new Packet003ChunkData();
		p.setTiles(getTiles());
		p.setLight(ChunkData.compressLight(getLights()));
		p.setX(chunkX);
		p.setY(chunkY);
		return p;
	}

	public void addToLoader(ChunkLoader l)
	{
		if (!isBeingLoaded)
		{
			l.addChunk(this);
			isBeingLoaded = true;
		}
	}

	public void addEntity(Entity e)
	{
		int xx = (int) (e.getX() / Tile.tileSize);
		int yy = (int) (e.getY() / Tile.tileSize);

		if (xx < rChunkX || yy < rChunkY || xx >= rChunkX + chunkS || yy >= rChunkY + chunkS)
		{
			System.err.println("OHNOES!!");
			return;
		}

		entityCount++;

		entitiesInTiles[MathHelper.convertToChunkTileX(xx) + MathHelper.convertToChunkTileY(yy) * chunkS].add(e);
	}

	public ArrayList<Entity> getEntitiesInTile(int tx, int ty)
	{
		return entitiesInTiles[tx + ty * chunkS];
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
		for (int x = 0; x < chunkS; x++)
		{
			for (int y = 0; y < chunkS; y++)
			{
				world.getWorldGenerator().generate(x + rChunkX, y + rChunkY);
			}
		}

		tilesGenerated = true;
	}

	private boolean isValidTilePos(int x, int y)
	{
		if (x < 0 || y < 0 || x >= chunkS || y >= chunkS)
			return false;
		return true;
	}

	public Color getTileColor(int x, int y)
	{
		if (!isValidTilePos(x, y))
		{
			return new Color(0);
		}
		return tileColors[x + y * chunkS];
	}

	public void blendTileColor(Color c, int x, int y)
	{
		if (!isValidTilePos(x, y))
			return;
		tileColors[x + y * chunkS].blend(c);
	}

	public void setTile(Tile t, int x, int y)
	{
		if (getTile(x, y) != t)
		{
			if (!isValidTilePos(x, y))
				return;
			tiles[x + y * chunkS] = t.getID();
		}
	}

	public Tile getTile(int x, int y)
	{
		if (!isValidTilePos(x, y))
			return null;
		return Tile.tiles[tiles[x + y * chunkS]];
	}

	private void resetLightLevels()
	{
		Color c = world.getAmbientLight();
		int r = (int) (c.getR() * lFadeOut);
		int g = (int) (c.getG() * lFadeOut);
		int b = (int) (c.getB() * lFadeOut);
		for (int i = 0; i < tileColors.length; i++)
		{
			tileColors[i] = new Color(r, g, b);
		}
	}

	public AABB getBounds()
	{
		return new AABB(chunkX * Tile.tileSize * chunkS, chunkY * Tile.tileSize * chunkS, chunkX * Tile.tileSize * chunkS + (chunkS * Tile.tileSize), chunkY * Tile.tileSize * chunkS + (chunkS * Tile.tileSize));
	}

	public synchronized boolean isTileMapGenerated()
	{
		return tilesGenerated;
	}

	public void markForTileGeneration()
	{
		tilesGenerated = false;
	}

	public void tick()
	{
		for (int x = 0; x < chunkS; x++)
		{
			for (int y = 0; y < chunkS; y++)
			{
				if (getTileColor(x, y).getBrightness() < 125 && Math.random() * 20000000.0f < 1)
				{
					if (entityCount < 1)
					{
						world.addEntity(new EntityZombie(world, (0 + rChunkX) * Tile.tileSize, ((0 + rChunkY) * Tile.tileSize)));
					}
				}
			}
		}
		
		for (int i = 0; i < chunkSS; i++)
		{
			ArrayList<Entity> es = entitiesInTiles[i];
			for (int u = 0; u < es.size(); u++)
			{
				Entity e = es.get(u);

				if (e.needsToBeRemoved())
				{
					es.remove(e);
					entityCount--;
					continue;
				}

				int x0 = (int) (e.getX() / Tile.tileSize);
				int y0 = (int) (e.getY() / Tile.tileSize);

				if (!world.entityHasBeenTicked(e))
				{
					e.tick();
				}

				int x1 = (int) (e.getX() / Tile.tileSize);
				int y1 = (int) (e.getY() / Tile.tileSize);

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
		for (int i = 0; i < tileColors.length; i++)
		{
			Color c = tileColors[i];
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
		for (int i = 0; i < tileColors.length; i++)
		{
			pTileColors[i] = tileColors[i].copyOf();
		}
	}

	public boolean hasLightChanged()
	{
		for (int i = 0; i < tileColors.length; i++)
		{
			Color c = tileColors[i];
			Color pc = pTileColors[i];
			if (!c.equals(pc))
				return true;
		}
		return false;
	}

	public void load()
	{
		if (!isTileMapGenerated())
		{
			this.generateTileMap();
		}
	}
}