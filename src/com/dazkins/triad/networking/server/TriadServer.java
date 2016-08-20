package com.dazkins.triad.networking.server;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;

import com.dazkins.triad.game.ability.Ability;
import com.dazkins.triad.game.ability.AbilityBar;
import com.dazkins.triad.game.entity.Entity;
import com.dazkins.triad.game.entity.IEntityWithAbilityBar;
import com.dazkins.triad.game.entity.IEntityWithEquipmentInventory;
import com.dazkins.triad.game.entity.IEntityWithInventory;
import com.dazkins.triad.game.entity.mob.EntityPlayer;
import com.dazkins.triad.game.entity.mob.Mob;
import com.dazkins.triad.game.inventory.EquipmentInventory;
import com.dazkins.triad.game.inventory.Inventory;
import com.dazkins.triad.game.inventory.item.Item;
import com.dazkins.triad.game.inventory.item.ItemStack;
import com.dazkins.triad.game.world.Chunk;
import com.dazkins.triad.networking.Network;
import com.dazkins.triad.networking.TriadConnection;
import com.dazkins.triad.networking.UpdateList;
import com.dazkins.triad.networking.client.update.ClientUpdateAnimation;
import com.dazkins.triad.networking.packet.Packet;
import com.dazkins.triad.networking.packet.Packet000RawMessage;
import com.dazkins.triad.networking.packet.Packet006EntityPositionUpdate;
import com.dazkins.triad.networking.packet.Packet007EntityAnimationStart;
import com.dazkins.triad.networking.packet.Packet009EntityRemoved;
import com.dazkins.triad.networking.packet.Packet010PlayerNameSet;
import com.dazkins.triad.networking.packet.Packet012Inventory;
import com.dazkins.triad.networking.packet.Packet014InteractionUpdate;
import com.dazkins.triad.networking.packet.Packet017ChatMessage;
import com.dazkins.triad.networking.packet.Packet019Pong;
import com.dazkins.triad.networking.packet.Packet021AbilityBar;
import com.dazkins.triad.networking.packet.Packet022EntityHealthUpdate;
import com.dazkins.triad.networking.packet.Packet023CooldownUpdate;
import com.dazkins.triad.networking.packet.PacketSend;
import com.dazkins.triad.networking.server.update.ServerUpdateChatMessage;
import com.dazkins.triad.networking.server.update.ServerUpdateChunkRequest;
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
	
	private UpdateList serverUpdate;

	public TriadServer()
	{
		server = new Server(1677216, 1677216);
		server.addListener(new ServerListener(this));

		connections = new ArrayList<TriadConnection>();

		port = 56355;

		serverWorldManager = new ServerWorldManager(this);
		
		packetSendQueue = new LinkedList<PacketSend>();
		connectionsToBeRemoved = new ArrayList<TriadConnection>();
		
		serverUpdate = new UpdateList();
		
		Network.register(server);
	}
	
	public void startup()
	{
		serverWorldManager.tick();
		serverWorldManager.generateSpawn();
		serverWorldManager.tick();
	}
	
	public UpdateList getUpdate()
	{
		return serverUpdate;
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
			return;
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
		for (Map.Entry<TriadConnection, EntityPlayer> mapE : serverWorldManager.getPlayers().entrySet())
		{
			EntityPlayer pl = mapE.getValue();
			Packet010PlayerNameSet namePacket = new Packet010PlayerNameSet();
			namePacket.setgID(pl.getGlobalID());
			namePacket.setName(pl.getName());
			
			c.sendPacket(namePacket, false);
		}
		
		connections.add(c);
		EntityPlayer p = serverWorldManager.addNewPlayer(c.getUsername(), c);
		TriadLogger.log("Registered new connection: " + c.getUsername() + " from: " + c.getIP(), false);
		
		Packet010PlayerNameSet namePacket = new Packet010PlayerNameSet();
		namePacket.setName(c.getUsername());
		namePacket.setgID(p.getGlobalID());
		sendPacketToAll(namePacket, false);
		
		Packet012Inventory inventoryPacket = Inventory.compressToPacket(p.getEquipmentInventory(), p.getGlobalID());
		sendPacketToAll(inventoryPacket, false);
		
		Packet022EntityHealthUpdate healthUpdate = new Packet022EntityHealthUpdate();
		int h = p.getHealth();
		int gID = p.getGlobalID();
		healthUpdate.setgID(gID);
		healthUpdate.setHealth(h);
		sendPacketToAll(healthUpdate, false);
		
		ArrayList<Entity> ents = serverWorldManager.getWorld().getLoadedEntities();
		for (Entity e : ents)
		{
			Packet006EntityPositionUpdate p0 = new Packet006EntityPositionUpdate();
			
			p0.setgID(e.getGlobalID());
			p0.settID(e.getTypeID());
			p0.setX(e.getX());
			p0.setY(e.getY());
			p0.setFacing(e.getFacing());
			
			c.sendPacket(p0, false);
			
			if (e instanceof Mob)
			{
				Mob m = (Mob) e;
				int health = m.getHealth();
				int mHealth = m.getMaxHealth();
				Packet022EntityHealthUpdate p1 = new Packet022EntityHealthUpdate();
				p1.setgID(e.getGlobalID());
				p1.setHealth(health);
				p1.setMaxHealth(mHealth);
				c.sendPacket(p1, false);
			}
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
			if (e instanceof IEntityWithAbilityBar)
			{
				IEntityWithAbilityBar eAb = (IEntityWithAbilityBar) e;
				AbilityBar a = eAb.getAbilityBar();
				if (a != null)
				{
					Packet021AbilityBar p1 = AbilityBar.compressToPacket(a, e.getGlobalID());
					c.sendPacket(p1, false);
				}
			}
			if (e instanceof IEntityWithEquipmentInventory)
			{
				IEntityWithEquipmentInventory eInv = (IEntityWithEquipmentInventory) e;
				EquipmentInventory i = eInv.getEquipmentInventory();
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
			System.exit(1);
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
					TriadConnection connection = getFromConnection(p.getCon());
					if (connection.isReadyToReceive())
					{
						p.getCon().sendTCP(p.getPacket());
						packetCount++;
					} else  
					{
						packetSendQueue.add(p);
					}
				}
			} else { break; }
		}
		
		serverWorldManager.tick();
		
		for (TriadConnection t : connectionsToBeRemoved)
		{
			connections.remove(t);
		}
		
		ArrayList<ServerUpdateChatMessage> msgs = serverUpdate.getAndPurgeUpdateListOfType(ServerUpdateChatMessage.class);
		for (ServerUpdateChatMessage c : msgs)
		{
			String msg = c.getMessage();
			String sender = c.getSender();
			Packet017ChatMessage p = new Packet017ChatMessage();
			p.setMsg(msg);
			p.setSender(sender);
			sendPacketToAll(p, false);
		}
		
		runningTicks++;
		packetCount = 0;
	}
	
	public void sendGlobalMessage(String m)
	{
		Packet000RawMessage p = new Packet000RawMessage();
		p.setMessage(m);
		sendPacketToAll(p, false);
	}
	
	public void handleChunkRequest(TriadConnection tc, int cx, int cy)
	{
		Chunk c = serverWorldManager.getWorld().getChunkWithForceLoad(cx, cy);
		serverUpdate.addUpdate(new ServerUpdateChunkRequest(tc, c));
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
	
	public void sendAnimationUpdate(ClientUpdateAnimation a)
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
			EntityPlayer ep = serverWorldManager.getPlayers().get(c);
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

	public void sendEntityAbilityBarUpdate(IEntityWithAbilityBar ae)
	{
		Entity e = (Entity) ae;
		int gID = e.getGlobalID();
		AbilityBar a = ae.getAbilityBar();
		Packet021AbilityBar p = AbilityBar.compressToPacket(a, gID);
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
	
	public void markReadyToReceive(TriadConnection tc)
	{
		tc.markAsReadyToReceive();
	}
	
	public EntityPlayer getPlayer(TriadConnection c)
	{
		return serverWorldManager.getPlayer(c);
	}

	public void handleInteractionUpdate(TriadConnection tc, boolean s)
	{
		EntityPlayer e = serverWorldManager.getPlayers().get(tc);
		if (s)
		{
			e.attemptInteract();
		} else
		{
			e.setInteractingObject(null);
		}
	}
	
//	public void handlePlayerVelocityUpdate(TriadConnection tc, float xa, float ya)
//	{
//		EntityPlayer e = serverWorldManager.getPlayers().get(tc);
//		serverUpdate.addUpdate(new ServerUpdatePlayerVelocity(e, xa, ya));
//	}

	public void onPing(TriadConnection c)
	{
		c.sendPacket(new Packet019Pong(), true);
	}

	public void sendHealthUpdate(Mob m, int h)
	{
		Packet022EntityHealthUpdate p = new Packet022EntityHealthUpdate();
		p.setgID(m.getGlobalID());
		p.setHealth(h);
		p.setMaxHealth(m.getMaxHealth());
		sendPacketToAll(p, false);
	}

	public void sendCooldownUpdate(Mob m, int[] c)
	{
		for (int i = 0; i < c.length; i++)
		{
			if (c[i] != -1)
			{
				Packet023CooldownUpdate p = new Packet023CooldownUpdate();
				p.setAbilityNumber(i);
				p.setgID(m.getGlobalID());
				p.setCooldownRemaining(c[i]);
				sendPacketToAll(p, false);
			}
		}
	}
}