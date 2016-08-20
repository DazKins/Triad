package com.dazkins.triad.game.ability;

import com.dazkins.triad.game.entity.mob.Mob;
import com.dazkins.triad.game.world.World;
import com.dazkins.triad.math.AABB;

public class AbilitySwingWeapon extends Ability
{
	public AbilitySwingWeapon(int id)
	{
		super(id, "swingweapon");
	}
	
	public int getCooldown()
	{
		return 30;
	}

	public void onUse(World w, Mob m)
	{
		m.onUseAbility(this);
		
		AABB attackHitboxes[] = m.getAttackAreas();
		
		AABB b = attackHitboxes[m.getFacing()];
		
		w.sendAttackCommand(b, m, m.getDamage(), m.getKnockbackValue());
	}
}