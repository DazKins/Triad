package com.dazkins.triad.networking.packet;
//Tells a client when it enters an interaction with another entity
public class Packet014InteractionUpdate extends Packet
{
	private boolean start;
	
	private int interactingEntityID;
	private int entityInteractedWithID;
	
	public int getInteractingEntityID()
	{
		return interactingEntityID;
	}
	
	public void setInteractingEntityID(int interactingEntityID)
	{
		this.interactingEntityID = interactingEntityID;
	}
	
	public int getEntityInteractedWithID()
	{
		return entityInteractedWithID;
	}
	
	public void setEntityInteractedWithID(int entityInteractedWithID)
	{
		this.entityInteractedWithID = entityInteractedWithID;
	}

	public boolean isStart()
	{
		return start;
	}

	public void setStart(boolean start)
	{
		this.start = start;
	}
}