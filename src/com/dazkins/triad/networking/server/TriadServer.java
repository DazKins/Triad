package com.dazkins.triad.networking.server;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;

import com.dazkins.triad.game.entity.Entity;
import com.dazkins.triad.game.entity.IEntityWithInventory;
import com.dazkins.triad.game.entity.Interactable;
import com.dazkins.triad.game.entity.mob.EntityPlayerServer;
import com.dazkins.triad.game.entity.mob.Mob;
import com.dazkins.triad.game.inventory.Inventory;
import com.dazkins.triad.game.inventory.item.Item;
import com.dazkins.triad.game.inventory.item.ItemStack;
import com.dazkins.triad.game.world.Chunk;
import com.dazkins.triad.game.world.ChunkCoordinate;
import com.dazkins.triad.game.world.World;
import com.dazkins.triad.game.world.tile.Tile;
import com.dazkins.triad.gfx.Image;
import com.dazkins.triad.networking.Network;
import com.dazkins.triad.networking.TriadConnection;
import com.dazkins.triad.networking.client.AnimationUpdate;
import com.dazkins.triad.networking.packet.Packet;
import com.dazkins.triad.networking.packet.Packet003ChunkData;
import com.dazkins.triad.networking.packet.Packet006EntityPositionUpdate;
import com.dazkins.triad.networking.packet.Packet007EntityAnimationStart;
import com.dazkins.triad.networking.packet.Packet009EntityRemoved;
import com.dazkins.triad.networking.packet.Packet010PlayerNameSet;
import com.dazkins.triad.networking.packet.Packet012Inventory;
import com.dazkins.triad.networking.packet.Packet013InteractCommand;
import com.dazkins.triad.networking.packet.Packet014InteractionUpdate;
import com.dazkins.triad.networking.packet.PacketSend;
import com.dazkins.triad.util.TriadLogger;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Server;

public class TriadServer
{
	private static final int PACKET_CUTOFF = 20000;
	
	private Server server;

	private ArrayList<TriadConnection> connections;
	private Map<TriadConnection, EntityPlayerServer> players;
	private ArrayList<TriadConnection> connectionsToBeRemoved;

	private ArrayList<ServerChunkRequest> chunkRequests;
	private ArrayList<Chunk> chunksToUpdate;
	
	private ArrayList<AnimationUpdate> animUpdates; 
	
	private Queue<PacketSend> packetSendQueue;
	
	private ArrayList<Chunk> spawnChunks;

	private int port;

	private World world;
	
	private int runningTicks;
	private int packetCount;

	public TriadServer()
	{
		server = new Server(1677216, 1677216);
		server.addListener(new ServerListener(this));

		connections = new ArrayList<TriadConnection>();

		port = 54555;

		chunkRequests = new ArrayList<ServerChunkRequest>();
		players = new HashMap<TriadConnection, EntityPlayerServer>();
		chunksToUpdate = new ArrayList<Chunk>();
		spawnChunks = new ArrayList<Chunk>();
		animUpdates = new ArrayList<AnimationUpdate>();
		packetSendQueue = new LinkedList<PacketSend>();
		connectionsToBeRemoved = new ArrayList<TriadConnection>();

		Network.register(server);
	}
	
	public void startup()
	{
		world.tick();
		generateSpawn();
		world.tick();
	}
	
	private void generateSpawn() 
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
	
	public void addAnimUpdate(AnimationUpdate a)
	{
		animUpdates.add(a);
	}
	
	public void addChunkUpdate(Chunk c) 
	{
		chunksToUpdate.add(c);
	}

	public World getWorld()
	{
		return world;
	}
	
	public void addPacketToSendQueue(Packet p, Connection c, boolean priority)
	{
		if (priority)
		{
			c.sendTCP(p);
			packetCount++;
		}
		
		if (packetCount < PACKET_CUTOFF)
		{
			c.sendTCP(p);
			packetCount++;
		} else 
		{
			packetSendQueue.add(new PacketSend(p, c));
		}
	}

	public void updatePlayer(TriadConnection c, float xa, float ya)
	{
		EntityPlayerServer p = players.get(c);
		if (p != null)
		{
			p.push(xa, ya);
		}
	}
	
	public void handleInteraction(TriadConnection c, boolean start)
	{
		EntityPlayerServer e = players.get(c);
		if (start)
		{
			e.attemptInteract();	
		} else
		{
			e.setInteractingObject(null);
		}
	}

	public TriadConnection getFromConnection(Connection c)
	{
		for (TriadConnection t : connections)
		{
			if (t.isConnection(c))
			{
				return t;
			}
		}
		return null;
	}

	// Returns the id of the created player entity
	public int registerNewConnection(TriadConnection c)
	{
		for (Map.Entry<TriadConnection, EntityPlayerServer> mapE : players.entrySet())
		{
			EntityPlayerServer pl = mapE.getValue();
			Packet010PlayerNameSet namePacket = new Packet010PlayerNameSet();
			namePacket.setgID(pl.getGlobalID());
			namePacket.setName(pl.getName());
			
			c.sendPacket(namePacket, false);
		}
		
		connections.add(c);
		EntityPlayerServer p = new EntityPlayerServer(world, 0, 0, c.getUsername());
		players.put(c, p);
		world.addEntity(p);
		TriadLogger.log("Registered new connection: " + c.getUsername() + " from: " + c.getIP(), false);
		
		for (TriadConnection tc : connections)
		{
			Packet010PlayerNameSet namePacket = new Packet010PlayerNameSet();
			namePacket.setName(c.getUsername());
			namePacket.setgID(p.getGlobalID());
			tc.sendPacket(namePacket, false);
		}
		
		ArrayList<Entity> ents = world.getLoadedEntities();
		for (Entity e : ents)
		{
			Packet006EntityPositionUpdate p0 = new Packet006EntityPositionUpdate();
			
			p0.setgID(e.getGlobalID());
			p0.settID(e.getTypeID());
			p0.setX(e.getX());
			p0.setY(e.getY());
			p0.setFacing(e.getFacing());
			
			if (e instanceof IEntityWithInventory)
			{
				IEntityWithInventory eInv = (IEntityWithInventory) e;
				Inventory i = eInv.getInventory();
				if (i != null)
				{
					Packet012Inventory p1 = Inventory.compressToPacket(i, e.getGlobalID());
					
					c.sendPacket(p1, false);
				}
			} 
		}
		
		return p.getGlobalID();
	}

	public void start()
	{
		try
		{
			server.bind(port);
		} catch (IOException e)
		{
			TriadLogger.log(e.getMessage(), true);
		}
		
		TriadLogger.log("Generating spawn...", false);

		world = new World(this);
		generateSpawn();
		
		//Yes, this loop makes me vomit as well...
		startupCheck : while (true)
		{
			world.processWaitingChunkLoads();
			for (Chunk c : spawnChunks)
			{
				if (!c.isLoaded())
					continue startupCheck;
			}
			break;
		}
		
		TriadLogger.log("Spawn generated", false);

		server.start();

		TriadLogger.log("Started on port: " + port, false);

		runLoop();
	}
	
	public void onMobInteraction(Mob m)
	{
		Entity iEnt = (Entity) (m.getInteractingObject());
		int eid = m.getGlobalID();
		if (iEnt != null)
		{
			int iid = iEnt.getGlobalID();
			Packet014InteractionUpdate p =  new Packet014InteractionUpdate();
			p.setEntityInteractedWithID(iid);
			p.setInteractingEntityID(eid);
			p.setStart(true);
			sendPacketToAll(p, false);
		} else
		{
			Packet014InteractionUpdate p =  new Packet014InteractionUpdate();
			p.setInteractingEntityID(eid);
			p.setStart(false);
			sendPacketToAll(p, false);
		}
	}

	public void addChunkRequest(TriadConnection tc, Chunk c)
	{
		chunkRequests.add(new ServerChunkRequest(tc, c));
	}

	public void tick()
	{
		while (!packetSendQueue.isEmpty())
		{
			if (packetCount < PACKET_CUTOFF) 
			{
				PacketSend p = packetSendQueue.remove();
				
				if (p != null)
				{
					p.getCon().sendTCP(p.getPacket());
					packetCount++;
				}
			} else { break; }
		}
		
		ArrayList<Entity> ents = world.getLoadedEntities();
		for (Entity e : ents)
		{
			Packet006EntityPositionUpdate p0 = new Packet006EntityPositionUpdate();
			
			p0.setgID(e.getGlobalID());
			p0.settID(e.getTypeID());
			p0.setX(e.getX());
			p0.setY(e.getY());
			p0.setFacing(e.getFacing());
			
			for (int i = 0; i < connections.size(); i++)
			{
				TriadConnection c = connections.get(i);
				int cID = players.get(c).getGlobalID();
				
				if (e.getGlobalID() == cID)
					c.sendPacket(p0, true);
				else
					c.sendPacket(p0, false);
			}

			if (e instanceof IEntityWithInventory)
			{
				IEntityWithInventory eInv = (IEntityWithInventory) e;
				Inventory inv = eInv.getInventory();
				if (inv != null)
				{
					if (inv.hasChanged())
					{
						Packet012Inventory p1 = Inventory.compressToPacket(inv, e.getGlobalID());
						
						sendPacketToAll(p1, false);
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

		for (int i = 0; i < chunkRequests.size(); i++)
		{
			ServerChunkRequest c = chunkRequests.get(i);
			Chunk cs = c.getChunk();
			if (cs.isLoaded())
			{
				TriadConnection tc = c.getConnection();
				Packet003ChunkData p = cs.compressToPacket();
				tc.sendPacket(p, false);
				chunkRequests.remove(c);
				i--;
			}
		}
		
		for (int i = 0; i < animUpdates.size(); i++)
		{
			AnimationUpdate a = animUpdates.get(i);
			Packet007EntityAnimationStart p = new Packet007EntityAnimationStart();
			p.setAnimID(a.getAnimID());
			p.setEntityGID(a.getEntityGID());
			p.setIndex(a.getIndex());
			p.setOverwrite(a.getOverwrite());
			p.setAnimSpeed(a.getAnimSpeed());
			sendPacketToAll(p, false);
		}
		
		animUpdates.clear();
		
		world.tick();
		
		//Chunk updates that need to be sent to all clients
		if (runningTicks % 10 == 0)
		{
			for (Chunk c : chunksToUpdate) 
			{
				if (c.isLoaded())
				{
					sendPacketToAll(c.compressToPacket(), false);
				}
			}
			chunksToUpdate.clear();
		}
		
		for (TriadConnection t : connectionsToBeRemoved)
		{
			connections.remove(t);
		}
		
		runningTicks++;
		packetCount = 0;
	}
	
	public void handleDisconnect(TriadConnection tc)
	{
		players.get(tc).remove();
		connectionsToBeRemoved.add(tc);
	}
	
	public void sendPacketToAll(Packet p, boolean priority) 
	{
		for (int i = 0; i < connections.size(); i++) 
		{
			TriadConnection t = connections.get(i);
			t.sendPacket(p, priority);
		}
	}
	
	public void registerEntityRemoval(Entity e)
	{
		Packet009EntityRemoved p = new Packet009EntityRemoved();
		p.setGID(e.getGlobalID());
		sendPacketToAll(p, false);
	}
	
	public void handleInventoryUpdate(int gID, int width, int height, int items[], int stackCounts[])
	{
		ArrayList<Entity> entities = world.getLoadedEntities();
		for (Entity e : entities)
		{
			if (e.getGlobalID() == gID)
			{
				if (e instanceof IEntityWithInventory)
				{
					IEntityWithInventory eInv = (IEntityWithInventory) e;
					int w = width;
					int h = height;
					eInv.setInventory(new Inventory(w, h));
					Inventory i = eInv.getInventory();
					for (int i0 = 0; i0 < items.length; i0++)
					{
						int itemIndex = items[i0];
						if (itemIndex >= 0)
						{
							Item item = Item.items[itemIndex];
							int stackCount = stackCounts[i0];
							if (stackCount > 0)
							{
								ItemStack itemStack = new ItemStack(item, stackCount);
								i.addItemStack(itemStack, i0);
							}
						}
					}
				}
			}
		}
	}

	public void runLoop()
	{
		long lastTime = System.nanoTime();
		double nsPerTick = 1000000000D / 60D;
		int ticks = 0;
		long lastTimer = System.currentTimeMillis();
		double delta = 0;

		while (true)
		{
			long now = System.nanoTime();
			delta += (now - lastTime) / nsPerTick;
			lastTime = now;
			while (delta >= 1)
			{
				ticks++;
				tick();
				delta -= 1;
			}

			if (System.currentTimeMillis() - lastTimer > 1000)
			{
				lastTimer += 1000;
				ticks = 0;
			}
		}
	}

	public static void main(String args[])
	{
		TriadLogger.initServerLog();
		TriadServer t = new TriadServer();
		t.start();
	}
}