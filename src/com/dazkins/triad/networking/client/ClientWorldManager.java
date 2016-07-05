package com.dazkins.triad.networking.client;

import java.util.ArrayList;

import com.dazkins.triad.game.entity.mob.EntityPlayerClientController;
import com.dazkins.triad.game.entity.shell.EntityShell;
import com.dazkins.triad.game.inventory.Inventory;
import com.dazkins.triad.game.inventory.item.ItemStack;
import com.dazkins.triad.game.world.ChunkCoordinate;
import com.dazkins.triad.gfx.Camera;
import com.dazkins.triad.math.AABB;
import com.dazkins.triad.networking.UpdateList;
import com.dazkins.triad.networking.client.update.ClientUpdateAnimation;
import com.dazkins.triad.networking.client.update.ClientUpdateChunk;
import com.dazkins.triad.networking.client.update.ClientUpdateEntity;
import com.dazkins.triad.networking.client.update.ClientUpdateInteraction;
import com.dazkins.triad.networking.client.update.ClientUpdateInventory;
import com.dazkins.triad.networking.client.update.ClientUpdatePlayerName;
import com.dazkins.triad.networking.packet.Packet008CameraStateUpdate;

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
		cem = new ClientEntityManager(this);

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
	
	public void setPlayerInteraction(EntityShell e)
	{
		player.setInteractingObject(e);
	}
	
	public int getMyPlayerID()
	{
		return myPlayerID;
	}
	
	public void clientUpdatePlayer(float x, float y, int f)
	{
		cem.updatePlayerEntity(x, y, f);
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
		
		UpdateList update = client.getAndPurgeUpdate();

		ArrayList<ChunkCoordinate> cs = crm.getRequestsForMissingChunks();
		for (ChunkCoordinate c : cs)
		{
			if (!requestedChunks.contains(c))
			{
				client.requestChunkData(c);
				requestedChunks.add(c);
			}
		}
		
		ArrayList<ClientUpdateEntity> eUpdates = update.getUpdateListOfType(ClientUpdateEntity.class);
		for (ClientUpdateEntity c : eUpdates)
		{
			if (c != null)
			{
				int gID = c.getgID();
				
				if (c.isRemove())
				{
					cem.removeEntity(gID);
					continue;
				}
				
				int tID = c.gettID();
				float x = c.getX();
				float y = c.getY();
				int facing = c.getFacing();

				if (gID == myPlayerID)
				{
					player.setX(x);
					player.setY(y);
				}

				if (!cem.hasAlreadyEntity(gID))
				{
					cem.initEntity(crm, gID, tID);
				}

				cem.updateEntity(x, y, gID, tID, facing);
			}
		}
		
		ArrayList<ClientUpdateAnimation> animUpdates = update.getUpdateListOfType(ClientUpdateAnimation.class);
		for (ClientUpdateAnimation a : animUpdates)
		{
			cem.handleAnimationUpdate(a);
		}

		ArrayList<ClientUpdateChunk> chunks = update.getUpdateListOfType(ClientUpdateChunk.class);
		for (ClientUpdateChunk c : chunks)
		{
			crm.handleChunkUpdate(c);
		}
		
		ArrayList<ClientUpdatePlayerName> playerNameUpdates = update.getUpdateListOfType(ClientUpdatePlayerName.class);
		for (ClientUpdatePlayerName p : playerNameUpdates)
		{
			int gID = p.getGID();
			String name = p.getName();
			cem.handlePlayerNameUpdate(gID, name);
		}
		
		ArrayList<ClientUpdateInventory> inventoryUpdates = update.getUpdateListOfType(ClientUpdateInventory.class);
		for (ClientUpdateInventory i : inventoryUpdates)
		{
			int gID = i.getEntityID();
			Inventory inv = Inventory.createInventoryObject(i);
			cem.handleInventoryUpdate(gID, inv);
		}
		
		ArrayList<ClientUpdateInteraction> interactionUpdates = update.getUpdateListOfType(ClientUpdateInteraction.class);
		for (ClientUpdateInteraction i : interactionUpdates)
		{
			int eID = i.getEntityID();
			int iID = i.getInteractingID();
			boolean s = i.isStart();
			cem.handleInteractionUpdate(eID, iID, s);
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
	
	public EntityShell getPlayerEntityShell()
	{
		return cem.getMyPlayerShell();
	}
}