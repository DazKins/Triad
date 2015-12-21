package com.dazkins.triad.networking.client;

import java.util.ArrayList;

public class ClientUpdate
{
	private ArrayList<ChunkData> chunkUpdates;
	private ArrayList<AnimationUpdate> animUpdates;
	private ArrayList<EntityUpdate> entityUpdates;
	private ArrayList<PlayerNameUpdate> playerNameUpdates;
	
	public ClientUpdate()
	{
		chunkUpdates = new ArrayList<ChunkData>();
		animUpdates = new ArrayList<AnimationUpdate>();
		entityUpdates = new ArrayList<EntityUpdate>();
		playerNameUpdates = new ArrayList<PlayerNameUpdate>();
	}
	
	@SuppressWarnings("unchecked")
	public ClientUpdate clone()
	{
		ClientUpdate c = new ClientUpdate();
		c.chunkUpdates = (ArrayList<ChunkData>) chunkUpdates.clone();
		c.animUpdates = (ArrayList<AnimationUpdate>) animUpdates.clone();
		c.entityUpdates = (ArrayList<EntityUpdate>) entityUpdates.clone();
		c.playerNameUpdates = (ArrayList<PlayerNameUpdate>) playerNameUpdates.clone();
		return c;
	}
	
	public void reset()
	{
		chunkUpdates.clear();
		animUpdates.clear();
		entityUpdates.clear();
		playerNameUpdates.clear();
	}
	
	public void addChunkUpdate(ChunkData c)
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
	
	public ArrayList<ChunkData> getChunkUpdates()
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
}