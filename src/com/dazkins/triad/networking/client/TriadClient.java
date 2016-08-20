package com.dazkins.triad.networking.client;

import java.io.IOException;

import com.dazkins.triad.game.chat.Chat;
import com.dazkins.triad.game.inventory.Inventory;
import com.dazkins.triad.game.world.ChunkCoordinate;
import com.dazkins.triad.networking.Network;
import com.dazkins.triad.networking.UpdateList;
import com.dazkins.triad.networking.packet.Packet;
import com.dazkins.triad.networking.packet.Packet000RawMessage;
import com.dazkins.triad.networking.packet.Packet002ChunkDataRequest;
import com.dazkins.triad.networking.packet.Packet011PlayerVelocity;
import com.dazkins.triad.networking.packet.Packet012Inventory;
import com.dazkins.triad.networking.packet.Packet013InteractCommand;
import com.dazkins.triad.networking.packet.Packet014InteractionUpdate;
import com.dazkins.triad.networking.packet.Packet016ReadyToReceive;
import com.dazkins.triad.networking.packet.Packet018Ping;
import com.dazkins.triad.networking.packet.Packet020UseAbility;
import com.dazkins.triad.util.TriadLogger;
import com.dazkins.triad.util.debugmonitor.DebugMonitor;
import com.esotericsoftware.kryonet.Client;

public class TriadClient
{
	private Client client;
	private ClientListener clientListener;

	private String username;

	private String ip;

	private UpdateList update;

	private boolean running;

	private int playerID = -1;
	
	private boolean loginAccepted;
	
	private int packetSendCount;
	
	private boolean pingSent;
	private long pingTime;
	private float ping;

	public TriadClient(String s)
	{
		client = new Client(65536, 65536);

		username = s;

		new Thread(client).start();

		update = new UpdateList();

		ip = "127.0.0.1";

		Network.register(client);
	}
	
	private boolean sentReadyToReceive;
	
	public boolean hasSentReadyToReceive()
	{
		return sentReadyToReceive;
	}
	
	public void markReadyToReceive()
	{
		sendPacket(new Packet016ReadyToReceive());
		sentReadyToReceive = true;
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
	
	public boolean isLoginAccepted()
	{
		return loginAccepted;
	}
	
	public UpdateList getClientUpdate()
	{
		return update;
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
		
		sendPacket(p0);
	}
	
	public void updatePlayerInventory(Inventory inv)
	{
		Packet012Inventory p0 = Inventory.compressToPacket(inv, getPlayerID());
		sendPacket(p0);
	}
	
	public void handleLoginRequestResponse(boolean a, int pID)
	{
		if (a)
		{
			DebugMonitor.addMessage("Login accepted!");
			TriadLogger.log("Login accepted", false);
			registerPlayerID(pID);
			loginAccepted = true;
		}
	}
	
	public void sendInteractionRequest()
	{
		Packet013InteractCommand p0 = new Packet013InteractCommand();
		sendPacket(p0);
	}
	
	public void useAbility(int i)
	{
		Packet020UseAbility p0 = new Packet020UseAbility();
		p0.setAbilityNo(i);
		sendPacket(p0);
	}

	public void start()
	{
		clientListener = new ClientListener(this);
		client.addListener(clientListener);

		try
		{
			client.connect(5000, ip, 56355);
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
		packetSendCount++;
	}
	
	public void resetCounters()
	{
		packetSendCount = 0;
		clientListener.resetCounter();
	}
	
	public int getPacketSendCount()
	{
		return packetSendCount;
	}
	
	public int getPacketReceiveCount()
	{
		return clientListener.getPacketReceiveCount();
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

	public void sendMessage(String s)
	{
		Packet000RawMessage p0 = new Packet000RawMessage();
		p0.setMessage(s);
		sendPacket(p0);
	}
	
	public void tick()
	{
		if (!pingSent && loginAccepted && (System.nanoTime() - pingTime) > 500000000)
		{
			Packet018Ping p = new Packet018Ping();
			sendPacket(p);
			pingTime = System.nanoTime();
			pingSent = true;
		}
	}
	
	public void onPong()
	{
		ping = (float) (System.nanoTime() - pingTime) / 1000000.0f;
		DebugMonitor.setVariableValue("Ping", ping);
		pingSent = false;
	}
}