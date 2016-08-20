package com.dazkins.triad.networking.server;

import com.dazkins.triad.game.entity.mob.EntityPlayer;
import com.dazkins.triad.game.inventory.Inventory;
import com.dazkins.triad.math.AABB;
import com.dazkins.triad.networking.packet.Packet;
import com.dazkins.triad.networking.packet.Packet000RawMessage;
import com.dazkins.triad.networking.packet.Packet001LoginRequest;
import com.dazkins.triad.networking.packet.Packet002ChunkDataRequest;
import com.dazkins.triad.networking.packet.Packet005UpdatePlayerPosition;
import com.dazkins.triad.networking.packet.Packet008CameraStateUpdate;
import com.dazkins.triad.networking.packet.Packet011PlayerVelocity;
import com.dazkins.triad.networking.packet.Packet012Inventory;
import com.dazkins.triad.networking.packet.Packet013InteractCommand;
import com.dazkins.triad.networking.packet.Packet014InteractionUpdate;
import com.dazkins.triad.networking.packet.Packet016ReadyToReceive;
import com.dazkins.triad.networking.packet.Packet018Ping;
import com.dazkins.triad.networking.packet.Packet020UseAbility;
import com.dazkins.triad.networking.server.update.ServerUpdateAbilityUse;
import com.dazkins.triad.networking.server.update.ServerUpdateCameraState;
import com.dazkins.triad.networking.server.update.ServerUpdateChatMessage;
import com.dazkins.triad.networking.server.update.ServerUpdateInteraction;
import com.dazkins.triad.networking.server.update.ServerUpdateInventory;
import com.dazkins.triad.networking.server.update.ServerUpdateNewConnection;
import com.dazkins.triad.networking.server.update.ServerUpdatePlayerVelocity;
import com.dazkins.triad.util.TriadLogger;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.FrameworkMessage.KeepAlive;
import com.esotericsoftware.kryonet.Listener;

public class ServerListener extends Listener
{
	private TriadServer server;

	public ServerListener(TriadServer s)
	{
		server = s;
	}

	public void received(Connection con, Object o)
	{
		if (o instanceof Packet)
		{
			Packet p = (Packet) o;

			if (p instanceof Packet000RawMessage)
			{
				Packet000RawMessage p0 = (Packet000RawMessage) p;
				TriadLogger.log("Recieved raw message from: " + con + " saying: " + ((Packet000RawMessage) p).getMsg(), false);
				String sender = server.getFromConnection(con).getUsername();
				String message = p0.getMsg();
				ServerUpdateChatMessage up = new ServerUpdateChatMessage(sender, message);
				server.getUpdate().addUpdate(up);
			}
			if (p instanceof Packet001LoginRequest)
			{
				Packet001LoginRequest p0 = (Packet001LoginRequest) p;
				server.getUpdate().addUpdate(new ServerUpdateNewConnection(p0.getUsername(), con));
			}
			if (p instanceof Packet002ChunkDataRequest)
			{
				Packet002ChunkDataRequest p0 = (Packet002ChunkDataRequest) p;
				int x = p0.getCX();
				int y = p0.getCY();
				server.handleChunkRequest(server.getFromConnection(con), x, y);
			}
			if (p instanceof Packet005UpdatePlayerPosition)
			{
				TriadLogger.log("Shouldn't have recieved this packet!", true);
			}
			if (p instanceof Packet008CameraStateUpdate)
			{
				Packet008CameraStateUpdate p0 = (Packet008CameraStateUpdate) p;
				CameraState cs = new CameraState(new AABB(p0.getX0(), p0.getY0(), p0.getX1(), p0.getY1()));
				server.getUpdate().addUpdate(new ServerUpdateCameraState(cs, server.getFromConnection(con)));
			}
			if (p instanceof Packet011PlayerVelocity)
			{
				Packet011PlayerVelocity p0 = (Packet011PlayerVelocity) p;
				float xa = p0.getXa();
				float ya = p0.getYa();
				EntityPlayer e = server.getPlayer(server.getFromConnection(con));
				server.getUpdate().addUpdate(new ServerUpdatePlayerVelocity(e, xa, ya));
			}
			if (p instanceof Packet012Inventory)
			{
				Packet012Inventory p0 = (Packet012Inventory) p;
				Inventory i = Inventory.createInventoryObject(p0);
				EntityPlayer player = server.getPlayer(server.getFromConnection(con));
				ServerUpdateInventory u = new ServerUpdateInventory(i, player);
				server.getUpdate().addUpdate(u);
			}
			if (p instanceof Packet013InteractCommand)
			{
				EntityPlayer player = server.getPlayer(server.getFromConnection(con));
				ServerUpdateInteraction u = new ServerUpdateInteraction(player, true);
				server.getUpdate().addUpdate(u);
			}
			if (p instanceof Packet014InteractionUpdate)
			{
				EntityPlayer player = server.getPlayer(server.getFromConnection(con));
				ServerUpdateInteraction u = new ServerUpdateInteraction(player, false);
				server.getUpdate().addUpdate(u);
			}
			if (p instanceof Packet016ReadyToReceive)
			{
				server.markReadyToReceive(server.getFromConnection(con));
			}
			if (p instanceof Packet018Ping)
			{
				server.onPing(server.getFromConnection(con));
			}
			if (p instanceof Packet020UseAbility)
			{
				Packet020UseAbility p0 = (Packet020UseAbility) p;
				int n = p0.getAbilityNo();
				EntityPlayer player = server.getPlayer(server.getFromConnection(con));
				ServerUpdateAbilityUse u = new ServerUpdateAbilityUse(n, player);
				server.getUpdate().addUpdate(u);
			}
		} else if (!(o instanceof KeepAlive))
		{
			TriadLogger.log("Just received some weird data here! " + o, false);
		}
	}

	public void connected(Connection c)
	{
		TriadLogger.log("New connection from: " + c.getRemoteAddressTCP().getHostName() + " on " + c, false);
	}

	public void disconnected(Connection c)
	{
		server.handleDisconnect(server.getFromConnection(c));
		TriadLogger.log(c + " has disconnected", false);
	}

	public void idle(Connection c)
	{
	}
}