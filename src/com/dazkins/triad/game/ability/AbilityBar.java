package com.dazkins.triad.game.ability;

import com.dazkins.triad.game.entity.mob.Mob;
import com.dazkins.triad.game.world.World;
import com.dazkins.triad.networking.packet.Packet021AbilityBar;

public class AbilityBar
{
	private World world;
	private Mob mob;
	
	private int size;
	
	private Ability[] abilities;
	private long[] lastUse;
	
	private boolean hasChangedFlag;
	
	public AbilityBar(Mob m, int size)
	{
		if (m != null)
			this.world = m.getWorld();
		this.mob = m;
		this.hasChangedFlag = true;
		this.abilities = new Ability[size];
		this.lastUse = new long[size];
		this.size = size;
	}
	
	public void setAbility(int i, Ability a)
	{
		abilities[i] = a;
		
		if (mob != null)
			lastUse[i] = mob.getLifeTicks() - a.getCooldown();
		
		hasChangedFlag = true;
	}
	
	public Ability getAbility(int i)
	{
		return abilities[i];
	}
	
	public void useAbility(int i)
	{
		Ability a = abilities[i];
		
		if (mob.getLifeTicks() - lastUse[i] >= a.getCooldown())
		{
			a.onUse(world, mob);
			lastUse[i] = mob.getLifeTicks();
		}
	}
	
	public boolean getAndPurgeHasChangedFlag()
	{
		if (hasChangedFlag)
		{
			hasChangedFlag = false;
			return true;
		}
		return false;
	}
	
	public int getSize()
	{
		return size;
	}
	
	public static Packet021AbilityBar compressToPacket(AbilityBar a, int gID)
	{
		Packet021AbilityBar p0 = new Packet021AbilityBar();
		int[] as = new int[a.size];
		for (int i = 0; i < a.size; i++)
		{
			Ability ab = a.abilities[i];
			if (ab != null)
				as[i] = ab.getID();
			else
				as[i] = -1;
		}
		p0.setAbilities(as);
		p0.setGID(gID);
		return p0;
	}
	
	public static AbilityBar getFromPacket(Packet021AbilityBar p)
	{
		int[] as = p.getAbilities();
		int size = as.length;
		AbilityBar a = new AbilityBar(null, size);
		for (int i = 0; i < size; i++)
		{
			int ind = as[i];
			if (ind != -1)
				a.setAbility(i, Ability.abilities[ind]);
		}
		return a;
	}
}