package com.dazkins.triad.networking.server;

import com.dazkins.triad.game.entity.mob.EntityPlayerServer;

public class PlayerVelocityUpdate
{
	private EntityPlayerServer ent;
	
	private float xa;
	private float ya;
	
	public PlayerVelocityUpdate(EntityPlayerServer e, float x, float y)
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