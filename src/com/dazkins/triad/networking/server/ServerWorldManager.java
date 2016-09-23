package com.dazkins.triad.networking.server;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.dazkins.triad.game.ability.AbilityBar;
import com.dazkins.triad.game.entity.Entity;
import com.dazkins.triad.game.entity.EntityTorch;
import com.dazkins.triad.game.entity.IEntityWithAbilityBar;
import com.dazkins.triad.game.entity.IEntityWithEquipmentInventory;
import com.dazkins.triad.game.entity.IEntityWithInventory;
import com.dazkins.triad.game.entity.mob.EntityPlayer;
import com.dazkins.triad.game.entity.mob.Mob;
import com.dazkins.triad.game.inventory.EquipmentInventory;
import com.dazkins.triad.game.inventory.Inventory;
import com.dazkins.triad.game.inventory.InventoryType;
import com.dazkins.triad.game.world.Chunk;
import com.dazkins.triad.game.world.ChunkCoordinate;
import com.dazkins.triad.game.world.World;
import com.dazkins.triad.game.world.tile.Tile;
import com.dazkins.triad.math.MathHelper;
import com.dazkins.triad.networking.TriadConnection;
import com.dazkins.triad.networking.UpdateList;
import com.dazkins.triad.networking.client.update.ClientUpdateAnimation;
import com.dazkins.triad.networking.packet.Packet;
import com.dazkins.triad.networking.packet.Packet004LoginRequestResponse;
import com.dazkins.triad.networking.server.update.ServerUpdateAbilityUse;
import com.dazkins.triad.networking.server.update.ServerUpdateChunkRequest;
import com.dazkins.triad.networking.server.update.ServerUpdateInteraction;
import com.dazkins.triad.networking.server.update.ServerUpdateInventory;
import com.dazkins.triad.networking.server.update.ServerUpdateNewConnection;
import com.dazkins.triad.networking.server.update.ServerUpdatePlayerVelocity;
import com.dazkins.triad.util.TriadLogger;

public class ServerWorldManager
{
	private TriadServer server;
	
	private World world;
		
	private Map<TriadConnection, EntityPlayer> players;
	
	private ArrayList<Chunk> spawnChunks;
	private ArrayList<Chunk> chunksToUpdate;
	
	private ArrayList<ServerUpdateChunkRequest> chunkRequests;
	
	public ServerWorldManager(TriadServer s)
	{
		server = s;
		players = new HashMap<TriadConnection, EntityPlayer>();
		spawnChunks = new ArrayList<Chunk>();
		
		world = new World(this);
		
		chunksToUpdate = new ArrayList<Chunk>();
		
		chunkRequests = new ArrayList<ServerUpdateChunkRequest>();
	}
	
	//DELETE
	private boolean test = false;
	
	public void tick()
	{
		UpdateList update = server.getUpdate();
		
		ArrayList<Entity> ents = world.getLoadedEntities();
		for (Entity e : ents)
		{
			if (e.purgeUpdateFlag())
			{
				server.sendEntityUpdate(e);
			}

			if (e instanceof IEntityWithInventory)
			{
				IEntityWithInventory eInv = (IEntityWithInventory) e;
				Inventory inv = eInv.getInventory();
				if (inv != null)
				{
					if (inv.getAndPurgeHasChangedFlag())
					{
						server.sendEntityInventoryUpdate(eInv);
					}
				}
			}
			
			if (e instanceof IEntityWithAbilityBar)
			{
				IEntityWithAbilityBar eA = (IEntityWithAbilityBar) e;
				AbilityBar ab = eA.getAbilityBar();
				if (ab != null)
				{
					if (ab.getAndPurgeHasChangedFlag())
					{
						server.sendEntityAbilityBarUpdate(eA);
					}
				}
			}
			
			if (e instanceof IEntityWithEquipmentInventory)
			{
				
			}
		}
		
		ArrayList<ChunkCoordinate> anchors = new ArrayList<ChunkCoordinate>();
		
		//Add a default load anchor to spawn
		anchors.add(new ChunkCoordinate(0, 0));
		
		for (Map.Entry<TriadConnection, EntityPlayer> mapE : players.entrySet())
		{
			EntityPlayer p = mapE.getValue();
			int cx = (int) (p.getX() / (Tile.TILESIZE * Chunk.CHUNKS));
			int cy = (int) (p.getY() / (Tile.TILESIZE * Chunk.CHUNKS));
			
			anchors.add(new ChunkCoordinate(cx, cy));
		}
		
		world.handleChunkLoadsFromAchors(anchors);
		
		ArrayList<ServerUpdateNewConnection> newConnections = update.getAndPurgeUpdateListOfType(ServerUpdateNewConnection.class);
		for (ServerUpdateNewConnection con : newConnections)
		{
			int id = server.handleNewConnection(new TriadConnection(server, con.getConnection(), con.getUsername()));
			Packet004LoginRequestResponse p1 = new Packet004LoginRequestResponse();
			p1.setAccepted(true);
			p1.setPlayerID(id);
			server.getFromConnection(con.getConnection()).sendPacket(p1, false);
			server.sendGlobalMessage("Welcome to the server " + con.getUsername() + "!");
		}

		ArrayList<ServerUpdateChunkRequest> tChunkRequests = update.getAndPurgeUpdateListOfType(ServerUpdateChunkRequest.class);
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
		
		ArrayList<ServerUpdatePlayerVelocity> playerVelUpdates = update.getAndPurgeUpdateListOfType(ServerUpdatePlayerVelocity.class);
		for (ServerUpdatePlayerVelocity p : playerVelUpdates)
		{
			float xa = p.getXA();
			float ya = p.getYA();
			EntityPlayer e = p.getEnt();
			if (e != null)
				e.push(xa, ya);
		}
		
		ArrayList<ServerUpdateAbilityUse> abilityUpdates = update.getAndPurgeUpdateListOfType(ServerUpdateAbilityUse.class);
		for (ServerUpdateAbilityUse u : abilityUpdates)
		{
			int an = u.getAbiityNo();
			EntityPlayer p = u.getPlayer();
			p.useAbility(an);
		}
		
		ArrayList<ServerUpdateInventory> inventoryUpdates = update.getAndPurgeUpdateListOfType(ServerUpdateInventory.class);
		for (ServerUpdateInventory u : inventoryUpdates)
		{
			EntityPlayer p = u.getPlayer();
			Inventory inv = u.getInventory();
			if (inv.getType() == InventoryType.NORMAL)
				p.setInventory(inv);
			if (inv.getType() == InventoryType.EQUIPMENT)
				p.setEquipmentInventory((EquipmentInventory) inv);
		}
		
		ArrayList<ServerUpdateInteraction> interactionUpdates = update.getAndPurgeUpdateListOfType(ServerUpdateInteraction.class);
		for (ServerUpdateInteraction u : interactionUpdates)
		{
			EntityPlayer p = u .getPlayer();
			boolean s = u.getStatus();
			if (s)
				p.attemptInteract();
			else
				p.setInteractingObject(null);
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
		
		if (!test)
		{
			test = true;
			world.addEntity(new EntityTorch(world, MathHelper.getRandomWithNegatives() * 50.0f, MathHelper.getRandomWithNegatives() * 50.0f));
		}
	}
	
	public World getWorld()
	{
		return world;
	}
	
	public Map<TriadConnection, EntityPlayer> getPlayers()
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
	
	public EntityPlayer addNewPlayer(String username, TriadConnection c)
	{
		EntityPlayer e = new EntityPlayer(world, 0, 0, username);
		players.put(c, e);
		world.addEntity(e);
		
		return e;
	}
	
	public EntityPlayer getPlayer(TriadConnection c)
	{
		return players.get(c);
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

	public void handleHealthUpdate(Mob m)
	{
		server.sendHealthUpdate(m, m.getHealth());
	}

	public void handleCooldownUpdate(Mob m, int[] c)
	{
		server.sendCooldownUpdate(m, c);
	}
}