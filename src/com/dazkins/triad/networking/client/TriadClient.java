package com.dazkins.triad.networking.client;

import java.io.IOException;
import java.util.ArrayList;

import com.dazkins.triad.game.entity.mob.EntityPlayerClientController;
import com.dazkins.triad.game.world.ChunkCoordinate;
import com.dazkins.triad.networking.Network;
import com.dazkins.triad.networking.packet.Packet;
import com.dazkins.triad.networking.packet.Packet002ChunkDataRequest;
import com.dazkins.triad.networking.packet.Packet005UpdatePlayerPosition;
import com.dazkins.triad.networking.packet.Packet011PlayerVelocity;
import com.dazkins.triad.util.TriadLogger;
import com.esotericsoftware.kryonet.Client;

public class TriadClient
{
	private Client client;

	private String username;

	private String ip;

	private ClientUpdate update;

	private boolean running;

	private int playerID = -1;

	public TriadClient(String s)
	{
		client = new Client(65536, 65536);

		username = s;

		new Thread(client).start();

		update = new ClientUpdate();

		ip = "localhost";

		Network.register(client);
	}

	public int getPlayerID()
	{
		return playerID;
	}

	public void registerPlayerID(int i)
	{
		playerID = i;
	}

	public boolean isRunning()
	{
		return running;
	}
	
	public ClientUpdate getClientUpdate()
	{
		return update;
	}
	
	public ClientUpdate getAndPurgeUpdate()
	{
		ClientUpdate c = update.clone();
		update.reset();
		return c;
	}

	public void updatePlayerVelocity(float xa, float ya)
	{
		Packet011PlayerVelocity p0 = new Packet011PlayerVelocity();
		p0.setXa(xa);
		p0.setYa(ya);
		sendPacket(p0);
	}

	public void start()
	{
		ClientListener cl = new ClientListener(this);
		client.addListener(cl);

		try
		{
			client.connect(5000, ip, 54555);
		} catch (IOException e)
		{
			TriadLogger.log(e.getMessage(), true);
		}

		running = true;
	}

	public void stop()
	{
		client.close();
	}

	public String getUsername()
	{
		return username;
	}

	public void sendPacket(Packet p)
	{
		client.sendTCP(p);
	}

	public void requestChunkData(ChunkCoordinate c)
	{
		Packet002ChunkDataRequest p = new Packet002ChunkDataRequest();
		p.setCX(c.getX());
		p.setCY(c.getY());
		sendPacket(p);
	}
}