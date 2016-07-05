package com.dazkins.triad.networking.server;

import com.dazkins.triad.game.world.Chunk;
import com.dazkins.triad.math.AABB;
import com.dazkins.triad.networking.TriadConnection;
import com.dazkins.triad.networking.packet.Packet;
import com.dazkins.triad.networking.packet.Packet000RawMessage;
import com.dazkins.triad.networking.packet.Packet001LoginRequest;
import com.dazkins.triad.networking.packet.Packet002ChunkDataRequest;
import com.dazkins.triad.networking.packet.Packet004LoginRequestResponse;
import com.dazkins.triad.networking.packet.Packet005UpdatePlayerPosition;
import com.dazkins.triad.networking.packet.Packet008CameraStateUpdate;
import com.dazkins.triad.networking.packet.Packet011PlayerVelocity;
import com.dazkins.triad.networking.packet.Packet012Inventory;
import com.dazkins.triad.networking.packet.Packet013InteractCommand;
import com.dazkins.triad.networking.packet.Packet014InteractionUpdate;
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
				TriadLogger.log("Recieved raw message from: " + con + " saying: " + ((Packet000RawMessage) p).getMsg(), false);
			}
			if (p instanceof Packet001LoginRequest)
			{
				Packet001LoginRequest p0 = (Packet001LoginRequest) p;
				int id = server.handleNewConnection(new TriadConnection(server, con, p0.getUsername()));
				Packet004LoginRequestResponse p1 = new Packet004LoginRequestResponse();
				p1.setAccepted(true);
				p1.setPlayerID(id);
				server.getFromConnection(con).sendPacket(p1, false);
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
				TriadConnection t = server.getFromConnection(con);
				
				CameraState cs = new CameraState(new AABB(p0.getX0(), p0.getY0(), p0.getX1(), p0.getY1()));
				t.handleCameraUpdate(cs);
			}
			if (p instanceof Packet011PlayerVelocity)
			{
				Packet011PlayerVelocity p0 = (Packet011PlayerVelocity) p;
				float xa = p0.getXa();
				float ya = p0.getYa();
				server.handlePlayerVelocityUpdate(server.getFromConnection(con), xa, ya);
			}
			if (p instanceof Packet012Inventory)
			{
				Packet012Inventory p0 = (Packet012Inventory) p;
				int gID = p0.getEntityGID();
				int width = p0.getWidth();
				int height = p0.getHeight();
				int items[] = p0.getItems();
				int stackCounts[] = p0.getStackCounts();
				server.handleInventoryUpdate(gID, width, height, items, stackCounts);
			}
			if (p instanceof Packet013InteractCommand)
			{
				server.handleInteractionUpdate(server.getFromConnection(con), true);
			}
			if (p instanceof Packet014InteractionUpdate)
			{
				//When interaction updates are received on the sever, they always indicate the end of an interaction
				server.handleInteractionUpdate(server.getFromConnection(con), false);
				
			}
		} else if (!(o instanceof KeepAlive))
		{
			TriadLogger.log("Just received some weird data here! " + o, false);
		}
	}

	public void connected(Connection c)
	{
		TriadLogger.log("New connection from: " + c.getRemoteAddressTCP().getHostName() + " on " + c, false);
		Packet000RawMessage p = new Packet000RawMessage();
		p.setMessage("Hello " + c.getRemoteAddressTCP().getHostName() + "!");
		c.sendTCP(p);
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