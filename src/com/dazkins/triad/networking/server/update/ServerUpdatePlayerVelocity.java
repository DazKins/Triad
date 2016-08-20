package com.dazkins.triad.networking.server.update;

import com.dazkins.triad.game.entity.mob.EntityPlayer;

public class ServerUpdatePlayerVelocity
{
	private EntityPlayer ent;
	
	private float xa;
	private float ya;
	
	public ServerUpdatePlayerVelocity(EntityPlayer e, float x, float y)
	{
		xa = x;
		ya = y;
		ent = e;
	}
	
	public EntityPlayer getEnt()
	{
		return ent;
	}
	
	public float getXA()
	{
		return xa;
	}
	
	public float getYA()
	{
		return ya;
	}
}