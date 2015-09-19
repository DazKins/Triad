package com.dazkins.triad.networking.server;

import com.dazkins.triad.game.world.Chunk;
import com.dazkins.triad.networking.TriadConnection;

public class ServerChunkRequest
{
	private TriadConnection connection;

	private Chunk chunk;

	public ServerChunkRequest(TriadConnection tc, Chunk c)
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