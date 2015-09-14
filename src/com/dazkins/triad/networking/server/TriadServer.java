package com.dazkins.triad.networking.server;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.dazkins.triad.game.entity.Entity;
import com.dazkins.triad.game.entity.mob.EntityPlayerServer;
import com.dazkins.triad.game.world.Chunk;
import com.dazkins.triad.game.world.World;
import com.dazkins.triad.networking.Network;
import com.dazkins.triad.networking.TriadConnection;
import com.dazkins.triad.networking.client.ChunkData;
import com.dazkins.triad.networking.packet.Packet003ChunkData;
import com.dazkins.triad.networking.packet.Packet006EntityPositionUpdate;
import com.dazkins.triad.util.debugmonitor.DebugMonitor;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Server;

public class TriadServer {
	private Server server;
	
	private ArrayList<TriadConnection> connections;
	private Map<TriadConnection, EntityPlayerServer>  players;
	
	private ArrayList<ServerChunkRequest> chunkRequests;
	
	private int port;
	
	private World world;

	public TriadServer() {
		server = new Server();
		server.addListener(new ServerListener(this));
		
		connections = new ArrayList<TriadConnection>();
		
		port = 54555;
		
		world = new World();
		
		chunkRequests = new ArrayList<ServerChunkRequest>();
		players = new HashMap<TriadConnection, EntityPlayerServer>();
		
		Network.register(server);
	}
 	
	public World getWorld() {
		return world;
	}
	
	public void sendPlayerUpdates() {
		for (TriadConnection c : connections) {
			Connection con = c.getConnection();
			for (Map.Entry<TriadConnection, EntityPlayerServer> e : players.entrySet()) {
				EntityPlayerServer p = e.getValue();
				Packet006EntityPositionUpdate p0 = new Packet006EntityPositionUpdate();
				p0.setgID(p.getGlobalID());
				p0.setX(p.getX());
				p0.setY(p.getY());
				con.sendTCP(p0);
			}
		}
	}
	
	public void updatePlayer(TriadConnection c, float x, float y, float xa, float ya) {
		EntityPlayerServer p = players.get(c);
		p.setX(x);
		p.setY(y);
		p.setXA(xa);
		p.setXA(ya);
	}
	
	public TriadConnection getFromConnection(Connection c) {
		for (TriadConnection t : connections) {
			if (t.getConnection().equals(c)) {
				return t;
			}
		}
		return null;
	}
	
	//Returns the id of the created player entity
	public int registerNewConnection(TriadConnection c) {
		connections.add(c);
		EntityPlayerServer p = new EntityPlayerServer(world, 0, 0);
		players.put(c, p);
		System.out.println("[SERVER] Registered new connection: " + c.getUsername() + " from: " + c.getIP());
		return p.getGlobalID();
	}
	
	public void start() {
		try {
			server.bind(port);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		System.out.println("[SERVER] >> Started on " + port);
		
		server.start();
		
		runLoop();
	}
	
	public void addChunkRequest(TriadConnection tc, Chunk c) {
		chunkRequests.add(new ServerChunkRequest(tc, c));
	}
	
	public void tick() {
		world.tick();

		ArrayList<Entity> ents = world.getLoadedEntities();
		for (Entity e : ents) {
			Packet006EntityPositionUpdate p0 = new Packet006EntityPositionUpdate();
			p0.setgID(e.getGlobalID());
			p0.settID(e.getTypeID());
			p0.setX(e.getX());
			p0.setY(e.getY());
			for (TriadConnection c : connections) {
				c.getConnection().sendTCP(p0);
			}
		}
		
		for (int i = 0; i < chunkRequests.size(); i++) {
			ServerChunkRequest c = chunkRequests.get(i);
			Chunk cs = c.getChunk();
			if (cs.isTileMapGenerated()) {
				TriadConnection tc = c.getConnection();
				Connection con = tc.getConnection();
				Packet003ChunkData p = new Packet003ChunkData();
				ChunkData d = new ChunkData(cs.getChunkCoord(), cs.getTiles(), cs.getLights());
				p.setTiles(d.getTileData());
				p.setLight(d.compressLight());
				p.setX(d.getCoords().getX());
				p.setY(d.getCoords().getY());
				con.sendTCP(p);
				chunkRequests.remove(c);
				i--;
			}
		}
		
		sendPlayerUpdates();
	}
	
	public void runLoop() {
		long lastTime = System.nanoTime();
		double nsPerTick = 1000000000D / 60D;
		int frames = 0;
		int ticks = 0;
		long lastTimer = System.currentTimeMillis();
		double delta = 0;

		while (true) {
			long now = System.nanoTime();
			delta += (now - lastTime) / nsPerTick;
			lastTime = now;
			while (delta >= 1) {
				ticks++;
				tick();
				delta -= 1;
			}
			frames++;

			if (System.currentTimeMillis() - lastTimer > 1000) {
				lastTimer += 1000;
				DebugMonitor.setVariableValue("FPS", frames);
				DebugMonitor.setVariableValue("UPS", ticks);
				frames = 0;
				ticks = 0;
			}
		}
	}
	
	public static void main(String args[]) {
		TriadServer t = new TriadServer();
		t.start();
	}
}