package com.dazkins.triad.networking.server;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;

import com.dazkins.triad.game.entity.Entity;
import com.dazkins.triad.game.entity.mob.EntityPlayerServer;
import com.dazkins.triad.game.world.Chunk;
import com.dazkins.triad.game.world.ChunkCoordinate;
import com.dazkins.triad.game.world.World;
import com.dazkins.triad.game.world.tile.Tile;
import com.dazkins.triad.networking.Network;
import com.dazkins.triad.networking.TriadConnection;
import com.dazkins.triad.networking.client.AnimationUpdate;
import com.dazkins.triad.networking.packet.Packet;
import com.dazkins.triad.networking.packet.Packet003ChunkData;
import com.dazkins.triad.networking.packet.Packet006EntityPositionUpdate;
import com.dazkins.triad.networking.packet.Packet007EntityAnimationStart;
import com.dazkins.triad.networking.packet.PacketSend;
import com.dazkins.triad.util.TriadLogger;
import com.dazkins.triad.util.debugmonitor.DebugMonitor;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Server;

public class TriadServer
{
	private static final int PACKET_CUTOFF = 1000;
	
	private Server server;

	private ArrayList<TriadConnection> connections;
	private Map<TriadConnection, EntityPlayerServer> players;

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

		Network.register(server);
	}
	
	public void startup()
	{
		generateSpawn();
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
	
	public void addPacketToSendQueue(Packet p, Connection c)
	{
		if (packetCount < PACKET_CUTOFF)
		{
			c.sendTCP(p);
			packetCount++;
		} else {
			packetSendQueue.add(new PacketSend(p, c));
		}
	}

	public void updatePlayer(TriadConnection c, float x, float y)
	{
		EntityPlayerServer p = players.get(c);
		p.setX(x);
		p.setY(y);
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
		connections.add(c);
		EntityPlayerServer p = new EntityPlayerServer(world, 0, 0);
		players.put(c, p);
		world.addEntity(p);
		TriadLogger.log("Registered new connection: " + c.getUsername() + " from: " + c.getIP(), false);
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
			sendPacketToAll(p0);
		}
		
		ArrayList<ChunkCoordinate> anchors = new ArrayList<ChunkCoordinate>();
		
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
				tc.sendPacket(p);
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
			sendPacketToAll(p);
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
					sendPacketToAll(c.compressToPacket());
				}
			}
			chunksToUpdate.clear();
		}
		
		runningTicks++;
		packetCount = 0;
	}
	
	public void sendPacketToAll(Packet p) 
	{
		for (TriadConnection t : connections) 
		{
			t.sendPacket(p);
		}
	}

	public void runLoop()
	{
		long lastTime = System.nanoTime();
		double nsPerTick = 1000000000D / 60D;
		int frames = 0;
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
			frames++;

			if (System.currentTimeMillis() - lastTimer > 1000)
			{
				lastTimer += 1000;
				DebugMonitor.setVariableValue("FPS", frames);
				DebugMonitor.setVariableValue("UPS", ticks);
				frames = 0;
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