package com.dazkins.triad.networking.server.update;

import com.dazkins.triad.game.world.Chunk;
import com.dazkins.triad.networking.TriadConnection;

public class ServerUpdateChunkRequest extends ServerUpdate
{
	private TriadConnection connection;

	private Chunk chunk;

	public ServerUpdateChunkRequest(TriadConnection tc, Chunk c)
	{
		connection = tc;
		chunk = c;
	}

	public TriadConnection getConnection()
	{
		return connection;
	}

	public Chunk getChunk()
	{
		return chunk;
	}
}
