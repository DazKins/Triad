package com.dazkins.triad.networking.server;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.dazkins.triad.game.entity.Entity;
import com.dazkins.triad.game.entity.IEntityWithInventory;
import com.dazkins.triad.game.entity.mob.EntityPlayerServer;
import com.dazkins.triad.game.entity.mob.Mob;
import com.dazkins.triad.game.inventory.Inventory;
import com.dazkins.triad.game.world.Chunk;
import com.dazkins.triad.game.world.ChunkCoordinate;
import com.dazkins.triad.game.world.World;
import com.dazkins.triad.game.world.tile.Tile;
import com.dazkins.triad.networking.TriadConnection;
import com.dazkins.triad.networking.client.update.ClientUpdateAnimation;
import com.dazkins.triad.networking.packet.Packet;
import com.dazkins.triad.networking.server.update.ServerUpdateChunkRequest;
import com.dazkins.triad.networking.server.update.ServerUpdatePlayerVelocity;
import com.dazkins.triad.networking.UpdateList;

public class ServerWorldManager
{
	private TriadServer server;
	
	private World world;
		
	private Map<TriadConnection, EntityPlayerServer> players;
	
	private ArrayList<Chunk> spawnChunks;
	private ArrayList<Chunk> chunksToUpdate;
	
	private ArrayList<ServerUpdateChunkRequest> chunkRequests;
	
	public ServerWorldManager(TriadServer s)
	{
		server = s;
		players = new HashMap<TriadConnection, EntityPlayerServer>();
		spawnChunks = new ArrayList<Chunk>();
		
		world = new World(this);
		
		chunksToUpdate = new ArrayList<Chunk>();
		
		chunkRequests = new ArrayList<ServerUpdateChunkRequest>();
	}
	
	public void tick()
	{
		UpdateList update = server.getAndPurgeUpdate();
		
		ArrayList<Entity> ents = world.getLoadedEntities();
		for (Entity e : ents)
		{
			server.sendEntityUpdate(e);

			if (e instanceof IEntityWithInventory)
			{
				IEntityWithInventory eInv = (IEntityWithInventory) e;
				Inventory inv = eInv.getInventory();
				if (inv != null)
				{
					if (inv.hasChanged())
					{
						server.sendEntityInventoryUpdate(eInv);
					}
					inv.resetHasChangedFlag();
				}
			}
		}
		
		ArrayList<ChunkCoordinate> anchors = new ArrayList<ChunkCoordinate>();
		
		//Add a default load anchor to spawn
		anchors.add(new ChunkCoordinate(0, 0));
		
		for (Map.Entry<TriadConnection, EntityPlayerServer> mapE : players.entrySet())
		{
			EntityPlayerServer p = mapE.getValue();
			int cx = (int) (p.getX() / (Tile.TILESIZE * Chunk.CHUNKS));
			int cy = (int) (p.getY() / (Tile.TILESIZE * Chunk.CHUNKS));
			
			anchors.add(new ChunkCoordinate(cx, cy));
		}
		
		world.handleChunkLoadsFromAchors(anchors);

		ArrayList<ServerUpdateChunkRequest> tChunkRequests = update.getUpdateListOfType(ServerUpdateChunkRequest.class);
		for (ServerUpdateChunkRequest c : tChunkRequests)
		{
			chunkRequests.add(c);
			world.addChunkToLoadQueue(c.getChunk());
		}

		for (int i = 0; i < chunkRequests.size(); i++)
		{
			ServerUpdateChunkRequest c = chunkRequests.get(i);
			Chunk cs = c.getChunk();
			if (cs.isLoaded())
			{
				TriadConnection tc = c.getConnection();
				Packet p = cs.compressToPacket(true);
				tc.sendPacket(p, false);
				chunkRequests.remove(c);
				i--;
			}
		}
		
		ArrayList<ServerUpdatePlayerVelocity> playerVelUpdates = update.getUpdateListOfType(ServerUpdatePlayerVelocity.class);
		for (ServerUpdatePlayerVelocity p : playerVelUpdates)
		{
			float xa = p.getXA();
			float ya = p.getYA();
			EntityPlayerServer e = p.getEnt();
			if (e != null)
				e.push(xa, ya);
		}
		
		world.tick();
		
		if (server.getRunningTicks() % 10 == 0)
		{
			for (Chunk c : chunksToUpdate) 
			{
				if (c.isLoaded())
				{
					server.sendChunkUpdate(c);
				}
			}
			chunksToUpdate.clear();
		}
	}
	
	public World getWorld()
	{
		return world;
	}
	
	public Map<TriadConnection, EntityPlayerServer> getPlayers()
	{
		return players;
	}
	
	public void generateSpawn() 
	{
		int x0 = -20;
		int y0 = -20;
		int x1 = 20;
		int y1 = 20;
		
		for (int x = x0; x <= x1; x++) {
			for (int y = y0; y <= y1; y++) {
				Chunk c = world.getChunkWithForceLoad(x, y);
				world.addChunkToLoadQueue(c);
				spawnChunks.add(c);
			}
		}
	}
	
	public void handleAnimationUpdate(int globalID, int animationID, int animIndex, boolean overwrite, float speed)
	{
		server.sendAnimationUpdate(new ClientUpdateAnimation(globalID, animationID, animIndex, overwrite, speed));
	}
	
	public ArrayList<Chunk> getSpawnChunks()
	{
		return spawnChunks;
	}
	
	public EntityPlayerServer addNewPlayer(String username, TriadConnection c)
	{
		EntityPlayerServer e = new EntityPlayerServer(world, 0, 0, username);
		players.put(c, e);
		world.addEntity(e);
		
		return e;
	}

	public UpdateList getUpdate()
	{
		return server.getUpdate();
	}

	public void addChunkToUpdate(Chunk c)
	{
		chunksToUpdate.add(c);
	}

	public void registerEntityRemoval(Entity e)
	{
		server.sendEntityRemoval(e);
	}

	public void onInteractUpdate(Mob mob)
	{
		server.sendInteractionUpdate(mob);
	}
}