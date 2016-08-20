package com.dazkins.triad.game.entity;

import com.dazkins.triad.game.ability.AbilityBar;

public interface IEntityWithAbilityBar
{
	public AbilityBar getAbilityBar();
	public void setAbilityBar(AbilityBar a);
}