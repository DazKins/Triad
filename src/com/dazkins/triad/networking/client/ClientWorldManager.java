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
	private ClientEntityManager cem;

	private Map<Integer, EntityPlayerClient> players;
	private int myPlayerID;

	private Camera cam;

	private TriadClient client;

	private ArrayList<ChunkCoordinate> requestedChunks;

	public ClientWorldManager(TriadClient cl, Camera c)
	{
		crm = new ClientChunkManager();
		cem = new ClientEntityManager();

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
		cem.render(cam);
	}

	public void setPlayerEntityRenderer(EntityRendererPlayer p)
	{
		cem.setPlayerEntityRenderer(p);
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

				if (!cem.hasAlreadyEntity(gID))
				{
					cem.initRenderer(crm, gID, tID);
				}

				cem.updateRenderer(x, y, gID, tID, facing);
			}
		}
		
		ArrayList<AnimationUpdate> animUpdates = client.getAndPurgeAnimationUpdates();
		for (AnimationUpdate a : animUpdates)
		{
			cem.handleAnimationUpdate(a);
		}

		ArrayList<ChunkData> chunks = client.getAndPurgeRecievedChunks();
		for (ChunkData c : chunks)
		{
			crm.updateData(c);
		}
		
		cem.tick();
	}
}