package com.dazkins.triad.networking.client;

import java.util.ArrayList;

public class ClientUpdate
{
	private ArrayList<ArrayList> updateLists = new ArrayList<ArrayList>();
	
	private ArrayList<ChunkUpdate> chunkUpdates;
	private ArrayList<AnimationUpdate> animUpdates;
	private ArrayList<EntityUpdate> entityUpdates;
	private ArrayList<PlayerNameUpdate> playerNameUpdates;
	private ArrayList<InventoryUpdate> inventoryUpdates;
	private ArrayList<InteractionUpdate> interactionUpdates;

	public ClientUpdate()
	{
		chunkUpdates = new ArrayList<ChunkUpdate>();
		updateLists.add(chunkUpdates);
		animUpdates = new ArrayList<AnimationUpdate>();
		updateLists.add(animUpdates);
		entityUpdates = new ArrayList<EntityUpdate>();
		updateLists.add(entityUpdates);
		playerNameUpdates = new ArrayList<PlayerNameUpdate>();
		updateLists.add(playerNameUpdates);
		inventoryUpdates = new ArrayList<InventoryUpdate>();
		updateLists.add(inventoryUpdates);
		interactionUpdates = new ArrayList<InteractionUpdate>();
		updateLists.add(interactionUpdates);
	}
	
	@SuppressWarnings("unchecked")
	public ClientUpdate clone()
	{
		ClientUpdate c = new ClientUpdate();
		c.chunkUpdates = (ArrayList<ChunkUpdate>) chunkUpdates.clone();
		c.animUpdates = (ArrayList<AnimationUpdate>) animUpdates.clone();
		c.entityUpdates = (ArrayList<EntityUpdate>) entityUpdates.clone();
		c.playerNameUpdates = (ArrayList<PlayerNameUpdate>) playerNameUpdates.clone();
		c.inventoryUpdates = (ArrayList<InventoryUpdate>)inventoryUpdates.clone();
		c.interactionUpdates = (ArrayList<InteractionUpdate>) interactionUpdates.clone();
		c.updateLists = (ArrayList<ArrayList>) updateLists.clone();
		return c;
	}
	
	public void reset()
	{
		for (ArrayList l : updateLists)
		{
			l.clear();
		}
	}
	
	public void addChunkUpdate(ChunkUpdate c)
	{
		chunkUpdates.add(c);
	}
	
	public void addAnimationUpdate(AnimationUpdate a)
	{
		animUpdates.add(a);
	}
	
	public void addEntityUpdate(EntityUpdate e)
	{
		entityUpdates.add(e);
	}
	
	public void addPlayerNameUpdate(PlayerNameUpdate p)
	{
		playerNameUpdates.add(p);
	}
	
	public void addInventoryUpdate(InventoryUpdate i)
	{
		inventoryUpdates.add(i);
	}
	
	public void addInteractionUpdates(InteractionUpdate i)
	{
		interactionUpdates.add(i);
	}
	
	public ArrayList<ChunkUpdate> getChunkUpdates()
	{
		return chunkUpdates;
	}
	
	public ArrayList<AnimationUpdate> getAnimUpdates()
	{
		return animUpdates;
	}
	
	public ArrayList<EntityUpdate> getEntityUpdates()
	{
		return entityUpdates;
	}
	
	public ArrayList<PlayerNameUpdate> getPlayerNameUpdates()
	{
		return playerNameUpdates;
	}
	
	public ArrayList<InventoryUpdate> getInventoryUpdates()
	{
		return inventoryUpdates;
	}
	
	public ArrayList<InteractionUpdate> getInteractionUpdates()
	{
		return interactionUpdates;
	}
}