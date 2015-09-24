package com.dazkins.triad.networking.packet;

public class Packet007EntityAnimationStart extends Packet
{
	private int entityGID;
	private int animID;
	private int index;
	private boolean overwrite;
	
	public int getEntityGID()
	{
		return entityGID;
	}
	
	public void setEntityGID(int entityGID)
	{
		this.entityGID = entityGID;
	}
	
	public int getAnimID()
	{
		return animID;
	}
	
	public void setAnimID(int animID)
	{
		this.animID = animID;
	}
	
	public int getIndex()
	{
		return index;
	}
	
	public void setIndex(int index)
	{
		this.index = index;
	}
	
	public boolean getOverwrite()
	{
		return overwrite;
	}
	
	public void setOverwrite(boolean overwrite)
	{
		this.overwrite = overwrite;
	}
}