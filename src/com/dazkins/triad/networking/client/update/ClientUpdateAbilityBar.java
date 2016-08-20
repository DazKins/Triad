package com.dazkins.triad.networking.client.update;

import com.dazkins.triad.game.ability.AbilityBar;

public class ClientUpdateAbilityBar extends ClientUpdate
{
	private AbilityBar abilityBar;
	private int gID;
	
	public ClientUpdateAbilityBar(AbilityBar a, int i)
	{
		this.gID = i;
		this.abilityBar = a;
	}
	
	public AbilityBar getAbilityBar()
	{
		return abilityBar;
	}
	
	public int getGID()
	{
		return gID;
	}
}