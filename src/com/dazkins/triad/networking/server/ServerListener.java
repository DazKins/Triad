package com.dazkins.triad.networking.server;

import com.dazkins.triad.game.world.Chunk;
import com.dazkins.triad.networking.TriadConnection;
import com.dazkins.triad.networking.packet.Packet;
import com.dazkins.triad.networking.packet.Packet000RawMessage;
import com.dazkins.triad.networking.packet.Packet001LoginRequest;
import com.dazkins.triad.networking.packet.Packet002ChunkDataRequest;
import com.dazkins.triad.networking.packet.Packet004LoginRequestResponse;
import com.dazkins.triad.networking.packet.Packet005UpdatePlayerPosition;
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
				int id = server.registerNewConnection(new TriadConnection(server, con, p0.getUsername()));
				Packet004LoginRequestResponse p1 = new Packet004LoginRequestResponse();
				p1.setAccepted(true);
				p1.setPlayerID(id);
				server.getFromConnection(con).sendPacket(p1);
			}
			if (p instanceof Packet002ChunkDataRequest)
			{
				Packet002ChunkDataRequest p0 = (Packet002ChunkDataRequest) p;
				int x = p0.getCX();
				int y = p0.getCY();
				Chunk c =  server.getWorld().getChunkWithForceLoad(x, y);
				server.addChunkRequest(server.getFromConnection(con), c);
				server.getWorld().addChunkToLoadQueue(c);
			}
			if (p instanceof Packet005UpdatePlayerPosition)
			{
				Packet005UpdatePlayerPosition p0 = (Packet005UpdatePlayerPosition) p;
				float x = p0.getX();
				float y = p0.getY();
				server.updatePlayer(server.getFromConnection(con), x, y);
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
		TriadLogger.log(c + " has disconnected", false);
	}

	public void idle(Connection c)
	{
	}
}