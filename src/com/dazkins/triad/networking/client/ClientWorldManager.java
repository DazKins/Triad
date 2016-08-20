package com.dazkins.triad.networking.client;

import java.util.ArrayList;

import com.dazkins.triad.game.ability.AbilityBar;
import com.dazkins.triad.game.entity.mob.PlayerClientController;
import com.dazkins.triad.game.entity.shell.EntityShell;
import com.dazkins.triad.game.inventory.Inventory;
import com.dazkins.triad.game.world.ChunkCoordinate;
import com.dazkins.triad.gfx.Camera;
import com.dazkins.triad.math.AABB;
import com.dazkins.triad.networking.UpdateList;
import com.dazkins.triad.networking.client.update.ClientUpdateAbilityBar;
import com.dazkins.triad.networking.client.update.ClientUpdateAnimation;
import com.dazkins.triad.networking.client.update.ClientUpdateChunk;
import com.dazkins.triad.networking.client.update.ClientUpdateCooldown;
import com.dazkins.triad.networking.client.update.ClientUpdateEntity;
import com.dazkins.triad.networking.client.update.ClientUpdateEntityHealthUpdate;
import com.dazkins.triad.networking.client.update.ClientUpdateInteraction;
import com.dazkins.triad.networking.client.update.ClientUpdateInventory;
import com.dazkins.triad.networking.client.update.ClientUpdateEntityName;
import com.dazkins.triad.networking.packet.Packet008CameraStateUpdate;

public class ClientWorldManager
{
	private ClientChunkManager crm;
	private ClientEntityManager cem;

	private int myPlayerID;
	private PlayerClientController player;

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

	public void setPlayer(PlayerClientController e)
	{
		e.setClient(client);
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
		
		UpdateList update = client.getClientUpdate();

		ArrayList<ChunkCoordinate> cs = crm.getRequestsForMissingChunks();
		for (ChunkCoordinate c : cs)
		{
			if (!requestedChunks.contains(c))
			{
				client.requestChunkData(c);
				requestedChunks.add(c);
			}
		}
		
		ArrayList<ClientUpdateEntity> eUpdates = update.getAndPurgeUpdateListOfType(ClientUpdateEntity.class);
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
		
		ArrayList<ClientUpdateAnimation> animUpdates = update.getAndPurgeUpdateListOfType(ClientUpdateAnimation.class);
		for (ClientUpdateAnimation a : animUpdates)
		{
			cem.handleAnimationUpdate(a);
		}

		ArrayList<ClientUpdateChunk> chunks = update.getAndPurgeUpdateListOfType(ClientUpdateChunk.class);
		for (ClientUpdateChunk c : chunks)
		{
			crm.handleChunkUpdate(c);
		}
		
		ArrayList<ClientUpdateEntityName> playerNameUpdates = update.getAndPurgeUpdateListOfType(ClientUpdateEntityName.class);
		for (ClientUpdateEntityName p : playerNameUpdates)
		{
			int gID = p.getGID();
			String name = p.getName();
			if (!cem.handleNameUpdate(gID, name))
			{
				update.addUpdate(p);
			}
		}
		
		ArrayList<ClientUpdateInventory> inventoryUpdates = update.getAndPurgeUpdateListOfType(ClientUpdateInventory.class);
		for (ClientUpdateInventory i : inventoryUpdates)
		{
			int gID = i.getGlobalID();
			Inventory inv = i.getInventory();
			if (!cem.handleInventoryUpdate(gID, inv))
			{
				update.addUpdate(i);
			}
		}
		
		ArrayList<ClientUpdateInteraction> interactionUpdates = update.getAndPurgeUpdateListOfType(ClientUpdateInteraction.class);
		for (ClientUpdateInteraction i : interactionUpdates)
		{
			int eID = i.getEntityID();
			int iID = i.getInteractingID();
			boolean s = i.isStart();
			if (!cem.handleInteractionUpdate(eID, iID, s))
			{
				update.addUpdate(i);
			}
		}
		
		ArrayList<ClientUpdateAbilityBar> abilityBarUpdate = update.getAndPurgeUpdateListOfType(ClientUpdateAbilityBar.class);
		for (ClientUpdateAbilityBar a : abilityBarUpdate)
		{
			int gID = a.getGID();
			AbilityBar b = a.getAbilityBar();
			if (!cem.handleAbilityBarUpdate(gID, b))
			{
				update.addUpdate(a);
			}
		}
		
		ArrayList<ClientUpdateEntityHealthUpdate> healthUpdates = update.getAndPurgeUpdateListOfType(ClientUpdateEntityHealthUpdate.class);
		for (ClientUpdateEntityHealthUpdate h : healthUpdates)
		{
			int gID = h.getGID();
			int health = h.getHealth();
			int maxHealth = h.getMaxHealth();
			if (!cem.handleHealthUpdate(gID, health, maxHealth))
			{
				update.addUpdate(h);
			}
		}
		
		ArrayList<ClientUpdateCooldown> cooldownUpdates = update.getAndPurgeUpdateListOfType(ClientUpdateCooldown.class);
		for (ClientUpdateCooldown c : cooldownUpdates)
		{
			int gID = c.getgID();
			int cd = c.getCooldown();
			int an = c.getAbilityNo();
			if (!cem.handleCooldownUpdate(gID, an, cd))
			{
				update.addUpdate(c);
			}
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