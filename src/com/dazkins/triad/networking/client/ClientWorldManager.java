package com.dazkins.triad.networking.client;

import java.util.ArrayList;

import com.dazkins.triad.game.entity.StorageEntityID;
import com.dazkins.triad.game.entity.mob.EntityPlayerClientController;
import com.dazkins.triad.game.entity.renderer.EntityRendererPlayer;
import com.dazkins.triad.game.world.ChunkCoordinate;
import com.dazkins.triad.gfx.Camera;
import com.dazkins.triad.math.AABB;
import com.dazkins.triad.networking.packet.Packet008CameraStateUpdate;
import com.dazkins.triad.util.TriadLogger;

public class ClientWorldManager
{
	private ClientChunkManager crm;
	private ClientEntityManager cem;

	private int myPlayerID;
	private EntityPlayerClientController player;

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

		myPlayerID = -1;
	}

	public ClientChunkManager getCCM()
	{
		return crm;
	}

	public void setPlayer(EntityPlayerClientController e)
	{
		player = e;
	}
	
	public void render()
	{
		crm.render(cam);
		cem.render(cam);
	}

	public void tick()
	{
		if (myPlayerID == -1)
			myPlayerID = client.getPlayerID();
		
		ClientUpdate update = client.getAndPurgeUpdate();

		ArrayList<ChunkCoordinate> cs = crm.getRequestsForMissingChunks();
		for (ChunkCoordinate c : cs)
		{
			if (!requestedChunks.contains(c))
			{
				client.requestChunkData(c);
				requestedChunks.add(c);
			}
		}

		boolean playerUpdateReceived = false;
		
		ArrayList<EntityUpdate> eUpdates = update.getEntityUpdates();
		for (EntityUpdate c : eUpdates)
		{
			if (c != null)
			{
				int gID = c.getgID();
				
				if (c.isRemove())
				{
					cem.removeRenderer(gID);
					continue;
				}
				
				int tID = c.gettID();
				float x = c.getX();
				float y = c.getY();
				int facing = c.getFacing();

				if (gID == myPlayerID)
				{
					playerUpdateReceived = true;
					player.setX(x);
					player.setY(y);
				}

				if (!cem.hasAlreadyEntity(gID))
				{
					cem.initRenderer(crm, gID, tID);
				}

				cem.updateRenderer(x, y, gID, tID, facing);
			}
		}
		
		if (!playerUpdateReceived)
			TriadLogger.log("Did not recieve player update packet this frame!", true);
		
		ArrayList<AnimationUpdate> animUpdates = update.getAnimUpdates();
		for (AnimationUpdate a : animUpdates)
		{
			cem.handleAnimationUpdate(a);
		}

		ArrayList<ChunkData> chunks = update.getChunkUpdates();
		for (ChunkData c : chunks)
		{
			crm.updateData(c);
		}
		
		ArrayList<PlayerNameUpdate> playerNameUpdates = update.getPlayerNameUpdates();
		for (PlayerNameUpdate p : playerNameUpdates)
		{
			int gID = p.getGID();
			String name = p.getName();
			cem.handlePlayerNameUpdate(gID, name);
		}
		
		cem.tick();
		
		AABB b = cam.getViewportBounds();
		Packet008CameraStateUpdate camP = new Packet008CameraStateUpdate();
		camP.setX0(b.getX0());
		camP.setY0(b.getY0());
		camP.setX1(b.getX1());
		camP.setY1(b.getY1());
		
		client.sendPacket(camP);
	}
}