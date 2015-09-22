package com.dazkins.triad.networking.client;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.dazkins.triad.game.entity.mob.EntityPlayerClient;
import com.dazkins.triad.game.entity.renderer.EntityRendererPlayer;
import com.dazkins.triad.game.world.ChunkCoordinate;
import com.dazkins.triad.gfx.Camera;

public class ClientWorldManager
{
	private ClientChunkManager crm;
	private EntityRendererManager erm;

	private Map<Integer, EntityPlayerClient> players;
	private int myPlayerID;

	private Camera cam;

	private TriadClient client;

	private ArrayList<ChunkCoordinate> requestedChunks;

	public ClientWorldManager(TriadClient cl, Camera c)
	{
		crm = new ClientChunkManager();
		erm = new EntityRendererManager();

		requestedChunks = new ArrayList<ChunkCoordinate>();

		client = cl;

		cam = c;

		players = new HashMap<Integer, EntityPlayerClient>();

		myPlayerID = -1;
	}

	public ClientChunkManager getCRM()
	{
		return crm;
	}

	public void render()
	{
		crm.render(cam);
		erm.render(cam);
	}

	public void setPlayerEntityRenderer(EntityRendererPlayer p)
	{
		erm.setPlayerEntityRenderer(p);
	}

	public void tick()
	{
		if (myPlayerID == -1)
			myPlayerID = client.getPlayerID();

		for (Map.Entry<Integer, EntityPlayerClient> v : players.entrySet())
		{
			v.getValue().tick();
		}

		ArrayList<ChunkCoordinate> cs = crm.getRequestsForMissingChunks();
		for (ChunkCoordinate c : cs)
		{
			if (!requestedChunks.contains(c))
			{
				client.requestChunkData(c);
				requestedChunks.add(c);
			}
		}

		ArrayList<EntityUpdate> eUpdates = client.getAndPurgeEntityUpdates();
		for (EntityUpdate c : eUpdates)
		{
			if (c != null)
			{
				int gID = c.getgID();
				int tID = c.gettID();
				float x = c.getX();
				float y = c.getY();
				int facing = c.getFacing();

				if (!erm.hasAlreadyEntity(gID))
				{
					erm.initRenderer(crm, gID, tID);
				}

				erm.updateRenderer(x, y, gID, tID, facing);
			}
		}

		ArrayList<ChunkData> chunks = client.getAndPurgeRecievedChunks();
		for (ChunkData c : chunks)
		{
			crm.updateData(c);
		}
	}
}