package com.dazkins.triad.networking.client;

import com.dazkins.triad.game.world.Chunk;
import com.dazkins.triad.game.world.ChunkCoordinate;
import com.dazkins.triad.gfx.Color;
import com.dazkins.triad.networking.client.update.ClientUpdateAnimation;
import com.dazkins.triad.networking.client.update.ClientUpdateChunk;
import com.dazkins.triad.networking.client.update.ClientUpdateEntity;
import com.dazkins.triad.networking.client.update.ClientUpdateInteraction;
import com.dazkins.triad.networking.client.update.ClientUpdateInventory;
import com.dazkins.triad.networking.client.update.ClientUpdatePlayerName;
import com.dazkins.triad.networking.packet.Packet;
import com.dazkins.triad.networking.packet.Packet000RawMessage;
import com.dazkins.triad.networking.packet.Packet001LoginRequest;
import com.dazkins.triad.networking.packet.Packet003ChunkData;
import com.dazkins.triad.networking.packet.Packet004LoginRequestResponse;
import com.dazkins.triad.networking.packet.Packet005UpdatePlayerPosition;
import com.dazkins.triad.networking.packet.Packet006EntityPositionUpdate;
import com.dazkins.triad.networking.packet.Packet007EntityAnimationStart;
import com.dazkins.triad.networking.packet.Packet009EntityRemoved;
import com.dazkins.triad.networking.packet.Packet010PlayerNameSet;
import com.dazkins.triad.networking.packet.Packet012Inventory;
import com.dazkins.triad.networking.packet.Packet014InteractionUpdate;
import com.dazkins.triad.networking.packet.Packet015SingleLightValueChunkUpdate;
import com.dazkins.triad.util.TriadLogger;
import com.dazkins.triad.util.debugmonitor.DebugMonitor;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;

public class ClientListener extends Listener
{
	private TriadClient client;

	public ClientListener(TriadClient tc)
	{
		client = tc;
	}

	public void received(Connection con, Object o)
	{
		if (o instanceof Packet)
		{
			Packet p = (Packet) o;
			if (p instanceof Packet000RawMessage)
			{
				TriadLogger.log("Recieved raw message from server: " + ((Packet000RawMessage) p).getMsg(), false);
			}
			if (p instanceof Packet003ChunkData)
			{
				Packet003ChunkData p0 = (Packet003ChunkData) p;
				int x = p0.getX();
				int y = p0.getY();
				byte[] tileData = p0.getTiles();
				byte[] lightData = p0.getLight();
				ChunkCoordinate c = new ChunkCoordinate(x, y);
				ChunkData d = new ChunkData(c, tileData, lightData);
				ClientUpdateChunk cu = new ClientUpdateChunk(d, true, true);
				client.getClientUpdate().addUpdate(cu);
			}
			if (p instanceof Packet004LoginRequestResponse)
			{
				Packet004LoginRequestResponse p0 = (Packet004LoginRequestResponse) p;
				client.registerPlayerID(p0.getChosenPlayerID());
				if (p0.isAccepted())
				{
					DebugMonitor.addMessage("Login accepted!");
					TriadLogger.log("Login accepted", false);
				}
			}
			if (p instanceof Packet006EntityPositionUpdate)
			{
				Packet006EntityPositionUpdate p0 = (Packet006EntityPositionUpdate) p;
				int gID = p0.getgID();
				int tID = p0.gettID();
				float x = p0.getX();
				float y = p0.getY();
				int facing = p0.getFacing();
				client.getClientUpdate().addUpdate(new ClientUpdateEntity(gID, tID, x, y, facing));
			}
			if (p instanceof Packet007EntityAnimationStart)
			{
				Packet007EntityAnimationStart p0 = (Packet007EntityAnimationStart) p;
				int gID = p0.getEntityGID();
				int aID = p0.getAnimID();
				int ind = p0.getIndex();
				boolean over = p0.getOverwrite();
				float s = p0.getAnimSpeed();
				ClientUpdateAnimation a = new ClientUpdateAnimation(gID, aID, ind, over, s);
				client.getClientUpdate().addUpdate(a);
			}
			if (p instanceof Packet009EntityRemoved)
			{
				Packet009EntityRemoved p0 = (Packet009EntityRemoved) p;
				int gID = p0.getGID();
				client.getClientUpdate().addUpdate(new ClientUpdateEntity(gID));
 			}
			if (p instanceof Packet010PlayerNameSet)
			{
				Packet010PlayerNameSet p0 = (Packet010PlayerNameSet) p;
				int gID = p0.getgID();
				String name = p0.getName();
				ClientUpdatePlayerName pn = new ClientUpdatePlayerName(name, gID);
				client.getClientUpdate().addUpdate(pn);
			}
			if (p instanceof Packet012Inventory)
			{
				Packet012Inventory p0 = (Packet012Inventory) p;
				int w = p0.getWidth();
				int h = p0.getHeight();
				int eID = p0.getEntityGID();
				ClientUpdateInventory u = new ClientUpdateInventory(eID, w, h);
				int items[] = p0.getItems();
				int stacks[] = p0.getStackCounts();
				for (int i = 0; i < w * h; i++)
				{
					if (stacks[i] > 0)
						u.setItemData(items[i], stacks[i], i);
				}
				client.getClientUpdate().addUpdate(u);
			}
			if (p instanceof Packet014InteractionUpdate)
			{
				Packet014InteractionUpdate p0 = (Packet014InteractionUpdate) p;
				int eID = p0.getInteractingEntityID();
				int iID = p0.getEntityInteractedWithID();
				boolean s = p0.isStart();
				client.getClientUpdate().addUpdate(new ClientUpdateInteraction(eID, iID, s));
			}
			if (p instanceof Packet015SingleLightValueChunkUpdate)
			{
				Packet015SingleLightValueChunkUpdate p0 = (Packet015SingleLightValueChunkUpdate) p;
				int x = p0.getX();
				int y = p0.getY();
				Color[] lightData = new Color[Chunk.CHUNKSS];
				for (int i = 0; i < Chunk.CHUNKSS; i++)
				{
					lightData[i] = new Color(p0.getR(), p0.getG(), p0.getB());
				}
				ChunkCoordinate c = new ChunkCoordinate(x, y);
				ChunkData d = new ChunkData(c, null, lightData);
				ClientUpdateChunk cu = new ClientUpdateChunk(d, false, true);
				client.getClientUpdate().addUpdate(cu);
			}
		}
	}

	public void connected(Connection c)
	{
		TriadLogger.log("Connected", false);
		Packet001LoginRequest p = new Packet001LoginRequest();
		p.setUsername(client.getUsername());
		client.sendPacket(p);
	}

	public void disconnected(Connection c)
	{
		TriadLogger.log("Disconnected", false);
	}

	public void idle(Connection c)
	{
	}
}