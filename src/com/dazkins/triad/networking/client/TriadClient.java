package com.dazkins.triad.networking.client;

import java.io.IOException;

import com.dazkins.triad.game.inventory.Inventory;
import com.dazkins.triad.game.inventory.item.ItemStack;
import com.dazkins.triad.game.world.ChunkCoordinate;
import com.dazkins.triad.networking.Network;
import com.dazkins.triad.networking.packet.Packet;
import com.dazkins.triad.networking.packet.Packet002ChunkDataRequest;
import com.dazkins.triad.networking.packet.Packet011PlayerVelocity;
import com.dazkins.triad.networking.packet.Packet012Inventory;
import com.dazkins.triad.networking.packet.Packet013InteractCommand;
import com.dazkins.triad.networking.packet.Packet014InteractionUpdate;
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
	
	public void sendInventoryUpdate(Inventory inv, int eID)
	{
		Packet012Inventory p0 = Inventory.compressToPacket(inv, eID);
		
		client.sendTCP(p0);
	}
	
	public void updatePlayerInventory(Inventory inv)
	{
		Packet012Inventory p0 = Inventory.compressToPacket(inv, getPlayerID());
		
		client.sendTCP(p0);
	}
	
	public void sendInteractionRequest()
	{
		Packet013InteractCommand p0 = new Packet013InteractCommand();
		client.sendTCP(p0);
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

	public void cancelInteraction()
	{
		Packet014InteractionUpdate p0 = new Packet014InteractionUpdate();
		p0.setInteractingEntityID(playerID);
		p0.setStart(false);
		sendPacket(p0);
	}
}