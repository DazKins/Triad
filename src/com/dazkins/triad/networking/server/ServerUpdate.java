package com.dazkins.triad.networking.server;

import java.util.ArrayList;

import com.dazkins.triad.game.world.Chunk;
import com.dazkins.triad.networking.client.AnimationUpdate;
import com.dazkins.triad.networking.client.InteractionUpdate;

@SuppressWarnings("unchecked")
public class ServerUpdate
{
	private ArrayList<ArrayList> updateLists = new ArrayList<ArrayList>();
	
	private ArrayList<PlayerVelocityUpdate> playerVelocityUpdates;
	private ArrayList<ServerChunkRequest> chunkRequests;
	private ArrayList<AnimationUpdate> animUpdates;
	
	public ServerUpdate()
	{
		playerVelocityUpdates = new ArrayList<PlayerVelocityUpdate>();
		updateLists.add(playerVelocityUpdates);
		chunkRequests = new ArrayList<ServerChunkRequest>();
		updateLists.add(chunkRequests);
		animUpdates = new ArrayList<AnimationUpdate>();
		updateLists.add(animUpdates);
	}
	
	public ServerUpdate clone()
	{
		ServerUpdate s = new ServerUpdate();
		s.playerVelocityUpdates = (ArrayList<PlayerVelocityUpdate>) playerVelocityUpdates.clone();
		s.chunkRequests = (ArrayList<ServerChunkRequest>) chunkRequests.clone();
		s.animUpdates = (ArrayList<AnimationUpdate>) animUpdates.clone();
		
		s.updateLists = (ArrayList<ArrayList>) updateLists.clone();
		return s;
	}
	
	public void reset()
	{
		for (ArrayList l : updateLists)
		{
			l.clear();
		}
	}
	
	public void addPlayerVelocityUpdate(PlayerVelocityUpdate p)
	{
		playerVelocityUpdates.add(p);
	}
	
	public ArrayList<PlayerVelocityUpdate> getPlayerVelocityUpdates()
	{
		return playerVelocityUpdates;
	}
	
	public void addChunkRequest(ServerChunkRequest c)
	{
		chunkRequests.add(c);
	}
	
	public ArrayList<ServerChunkRequest> getChunkRequests()
	{
		return chunkRequests;
	}
	
	public void addAnimationUpdate(AnimationUpdate a)
	{
		animUpdates.add(a);
	}
	
	public ArrayList<AnimationUpdate> getAnimationUpdates()
	{
		return animUpdates;
	}
}