package com.dazkins.triad.networking.client;

public class AnimationUpdate
{
	private int entityGID;
	private int animID;
	private int index;
	private boolean overwrite;
	
	public AnimationUpdate(int gID, int aID, int ind, boolean over)
	{
		entityGID = gID;
		animID = aID;
		index = ind;
		overwrite = over;
	}
	
	public int getEntityGID()
	{
		return entityGID;
	}
	
	public int getAnimID()
	{
		return animID;
	}
	
	public int getIndex()
	{
		return index;
	}
	
	public boolean getOverwrite()
	{
		return overwrite;
	}
}