package com.dazkins.triad.networking.server;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;

import com.dazkins.triad.game.entity.Entity;
import com.dazkins.triad.game.entity.IEntityWithInventory;
import com.dazkins.triad.game.entity.mob.EntityPlayerServer;
import com.dazkins.triad.game.entity.mob.Mob;
import com.dazkins.triad.game.inventory.Inventory;
import com.dazkins.triad.game.inventory.item.Item;
import com.dazkins.triad.game.inventory.item.ItemStack;
import com.dazkins.triad.game.world.Chunk;
import com.dazkins.triad.networking.Network;
import com.dazkins.triad.networking.TriadConnection;
import com.dazkins.triad.networking.client.AnimationUpdate;
import com.dazkins.triad.networking.packet.Packet;
import com.dazkins.triad.networking.packet.Packet006EntityPositionUpdate;
import com.dazkins.triad.networking.packet.Packet007EntityAnimationStart;
import com.dazkins.triad.networking.packet.Packet009EntityRemoved;
import com.dazkins.triad.networking.packet.Packet010PlayerNameSet;
import com.dazkins.triad.networking.packet.Packet012Inventory;
import com.dazkins.triad.networking.packet.Packet014InteractionUpdate;
import com.dazkins.triad.networking.packet.PacketSend;
import com.dazkins.triad.util.TriadLogger;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Server;

public class TriadServer
{
	private static final int PACKET_CUTOFF = 20000;
	
	private Server server;
	
	private ServerWorldManager serverWorldManager;

	private ArrayList<TriadConnection> connections;
	private ArrayList<TriadConnection> connectionsToBeRemoved;
	
	private Queue<PacketSend> packetSendQueue;

	private int port;
	
	private int runningTicks;
	private int packetCount;
	
	private ServerUpdate serverUpdate;

	public TriadServer()
	{
		server = new Server(1677216, 1677216);
		server.addListener(new ServerListener(this));

		connections = new ArrayList<TriadConnection>();

		port = 54555;

		serverWorldManager = new ServerWorldManager(this);
		
		packetSendQueue = new LinkedList<PacketSend>();
		connectionsToBeRemoved = new ArrayList<TriadConnection>();
		
		serverUpdate = new ServerUpdate();
		
		Network.register(server);
	}
	
	public void startup()
	{
		serverWorldManager.tick();
		serverWorldManager.generateSpawn();
		serverWorldManager.tick();
	}
	
	public ServerUpdate getUpdate()
	{
		return serverUpdate;
	}
	
	public ServerUpdate getAndPurgeUpdate()
	{
		ServerUpdate s = serverUpdate.clone();
		serverUpdate.reset();
		return s;
	}
	
	public ServerWorldManager getServerWorldManager()
	{
		return serverWorldManager;
	}
	
	public ArrayList<TriadConnection> getAllConnections()
	{
		return connections;
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

//	public void updatePlayer(TriadConnection c, float xa, float ya)
//	{
//		EntityPlayerServer p = players.get(c);
//		if (p != null)
//		{
//			p.push(xa, ya);
//		}
//	}
//	
//	public void handleInteraction(TriadConnection c, boolean start)
//	{
//		EntityPlayerServer e = players.get(c);
//		if (start)
//		{
//			e.attemptInteract();	
//		} else
//		{
//			e.setInteractingObject(null);
//		}
//	}

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
	
	public int getRunningTicks()
	{
		return runningTicks;
	}

	// Returns the id of the created player entity
	public int handleNewConnection(TriadConnection c)
	{
		for (Map.Entry<TriadConnection, EntityPlayerServer> mapE : serverWorldManager.getPlayers().entrySet())
		{
			EntityPlayerServer pl = mapE.getValue();
			Packet010PlayerNameSet namePacket = new Packet010PlayerNameSet();
			namePacket.setgID(pl.getGlobalID());
			namePacket.setName(pl.getName());
			
			c.sendPacket(namePacket, false);
		}
		
		connections.add(c);
		EntityPlayerServer p = serverWorldManager.addNewPlayer(c.getUsername(), c);
		TriadLogger.log("Registered new connection: " + c.getUsername() + " from: " + c.getIP(), false);
		
		for (TriadConnection tc : connections)
		{
			Packet010PlayerNameSet namePacket = new Packet010PlayerNameSet();
			namePacket.setName(c.getUsername());
			namePacket.setgID(p.getGlobalID());
			tc.sendPacket(namePacket, false);
		}
		
		ArrayList<Entity> ents = serverWorldManager.getWorld().getLoadedEntities();
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

		startup();
		
		//Yes, this loop makes me vomit as well...
		startupCheck : while (true)
		{
			serverWorldManager.getWorld().processWaitingChunkLoads();
			for (Chunk c : serverWorldManager.getSpawnChunks())
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
	
	public void sendInteractionUpdate(Mob m)
	{
		int eID = m.getGlobalID();
		Entity ie = (Entity) m.getInteractingObject();
		int iID = -1;
		if (ie != null)
			iID = ie.getGlobalID();
		
		Packet014InteractionUpdate p = new Packet014InteractionUpdate();
		p.setInteractingEntityID(eID);
		p.setEntityInteractedWithID(iID);
		p.setStart(true);
		sendPacketToAll(p, false);
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
		
		serverWorldManager.tick();
		
		for (TriadConnection t : connectionsToBeRemoved)
		{
			connections.remove(t);
		}
		
		runningTicks++;
		packetCount = 0;
	}
	
	public void handleChunkRequest(TriadConnection tc, int cx, int cy)
	{
		Chunk c = serverWorldManager.getWorld().getChunkWithForceLoad(cx, cy);
		serverUpdate.addChunkRequest(new ServerChunkRequest(tc, c));
		serverWorldManager.getWorld().addChunkToLoadQueue(c);
	}
	
	public void handleDisconnect(TriadConnection tc)
	{
		serverWorldManager.getPlayers().get(tc).remove();
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
	
	public void sendEntityRemoval(Entity e)
	{
		Packet009EntityRemoved p = new Packet009EntityRemoved();
		p.setGID(e.getGlobalID());
		sendPacketToAll(p, false);
	}
	
	public void sendAnimationUpdate(AnimationUpdate a)
	{
		Packet007EntityAnimationStart p = new Packet007EntityAnimationStart();
		p.setAnimID(a.getAnimID());
		p.setEntityGID(a.getEntityGID());
		p.setIndex(a.getIndex());
		p.setOverwrite(a.getOverwrite());
		p.setAnimSpeed(a.getAnimSpeed());
		sendPacketToAll(p, false);
	}
	
	public void sendEntityUpdate(Entity e)
	{
		Packet006EntityPositionUpdate p0 = new Packet006EntityPositionUpdate();
		p0.setX(e.getX());
		p0.setY(e.getY());
		p0.setFacing(e.getFacing());
		p0.settID(e.getTypeID());
		p0.setgID(e.getGlobalID());
		
		for (TriadConnection c : connections)
		{
			EntityPlayerServer ep = serverWorldManager.getPlayers().get(c);
			int id = ep.getGlobalID();
			
			if (id == e.getGlobalID())
				c.sendPacket(p0, true);
			else
				c.sendPacket(p0, false);
		}
	}
	
	public void sendChunkUpdate(Chunk c)
	{
		sendPacketToAll(c.compressToPacket(false), false);
	}
	
	public void sendEntityInventoryUpdate(IEntityWithInventory ie)
	{
		Entity e = (Entity) ie;
		int gID = e.getGlobalID();
		Inventory i = ie.getInventory();
		Packet012Inventory p = Inventory.compressToPacket(i, gID);
		sendPacketToAll(p, false);
	}
	
	public void handleInventoryUpdate(int gID, int width, int height, int items[], int stackCounts[])
	{
		ArrayList<Entity> entities = serverWorldManager.getWorld().getLoadedEntities();
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

	public void handleInteractionUpdate(TriadConnection tc, boolean s)
	{
		EntityPlayerServer e = serverWorldManager.getPlayers().get(tc);
		if (s)
		{
			e.attemptInteract();
		} else
		{
			e.setInteractingObject(null);
		}
	}
	
	public void handlePlayerVelocityUpdate(TriadConnection tc, float xa, float ya)
	{
		EntityPlayerServer e = serverWorldManager.getPlayers().get(tc);
		serverUpdate.addPlayerVelocityUpdate(new PlayerVelocityUpdate(e, xa, ya));
	}
}