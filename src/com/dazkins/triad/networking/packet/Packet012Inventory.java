package com.dazkins.triad.networking.packet;

public class Packet012Inventory extends Packet 
{
	private int entityGID;
	private int type;
	
	private int width;
	private int height;
	
	private int[] items;
	private int[] stackCounts;
	
	public int getEntityGID() 
	{
		return entityGID;
	}
	
	public void setEntityGID(int entityGID) 
	{
		this.entityGID = entityGID;
	}
	
	public int getWidth()
	{
		return width;
	}

	public void setWidth(int width)
	{
		this.width = width;
	}

	public int getHeight()
	{
		return height;
	}

	public void setHeight(int height)
	{
		this.height = height;
	}

	public int[] getItems() 
	{
		return items;
	}
	
	public void setItems(int[] items) 
	{
		this.items = items;
	}
	
	public int[] getStackCounts() 
	{
		return stackCounts;
	}
	
	public void setStackCounts(int[] stackCounts) 
	{
		this.stackCounts = stackCounts;
	}
	
	public int getType()
	{
		return type;
	}
	
	public void setType(int t)
	{
		type = t;
	}
}