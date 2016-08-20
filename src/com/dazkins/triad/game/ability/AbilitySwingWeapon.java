package com.dazkins.triad.game.ability;

import com.dazkins.triad.game.entity.mob.Mob;
import com.dazkins.triad.game.world.World;
import com.dazkins.triad.math.AABB;

public class AbilitySwingWeapon extends Ability
{
	private AABB attackHitbox = new AABB(-50, -50, 50, 50);
	
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
		
		float x = m.getX();
		float y = m.getY();
		
		AABB b = attackHitbox.shifted(x, y);
		
		//AABB, Mob, Damage, Knockback
		w.sendAttackCommand(b, m, 10, 1);
	}
}