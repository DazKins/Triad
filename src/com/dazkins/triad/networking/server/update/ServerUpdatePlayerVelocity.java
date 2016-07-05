package com.dazkins.triad.networking.server.update;

import com.dazkins.triad.game.entity.mob.EntityPlayerServer;

public class ServerUpdatePlayerVelocity
{
	private EntityPlayerServer ent;
	
	private float xa;
	private float ya;
	
	public ServerUpdatePlayerVelocity(EntityPlayerServer e, float x, float y)
	{
		xa = x;
		ya = y;
		ent = e;
	}
	
	public EntityPlayerServer getEnt()
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