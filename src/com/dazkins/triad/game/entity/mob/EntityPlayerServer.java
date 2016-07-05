package com.dazkins.triad.game.entity.mob;

import java.util.ArrayList;

import com.dazkins.triad.game.entity.Entity;
import com.dazkins.triad.game.entity.Interactable;
import com.dazkins.triad.game.entity.StorageEntityID;
import com.dazkins.triad.game.inventory.Inventory;
import com.dazkins.triad.game.world.World;
import com.dazkins.triad.gfx.model.animation.StorageAnimationID;
import com.dazkins.triad.math.AABB;

public class EntityPlayerServer extends Mob
{
	public EntityPlayerServer(World w, float x, float y, String name)
	{
		super(w, StorageEntityID.PLAYER, x, y, name, 1000);
		this.inv = new Inventory(9, 5);
	}

	public int getMaxHealth()
	{
		return 1000;
	}
	
	public void attemptInteract()
	{
		AABB b = getFacingAttackArea(getFacing());
		ArrayList<Interactable> ints = world.getInteractablesInAABB(b);
		for (Interactable i : ints)
		{
			setInteractingObject(i);
			i.onInteract(this);
		}
	}

	public boolean mayPass(Entity e)
	{
		return true;
	}
	
	protected int getBaseDamage()
	{
		return 5;
	}

	protected int getBaseKnockback()
	{
		return 5;
	}

	protected int getBaseAttackCooldown()
	{
		return 40;
	}

	protected int getBaseAttackRange()
	{
		return 50;
	}

	public AABB getAABB()
	{
		return new AABB(x - 8, y, x + 8, y + 10);
	}

	public float getMovementSpeed()
	{
		return 1f;
	}
	
	public void tick()
	{
		super.tick();
		super.move();
		
		if (Math.abs(xa) >= 0.2f || Math.abs(ya) >= 0.2f)
			addNewAnimation(StorageAnimationID.HUMANOID_WALKING, 1, false, 1);
		else
			addNewAnimation(StorageAnimationID.HUMANOID_IDLE, 1, false, 1);

		setFacingBasedOnVelocities(xa, ya);
		
		xa *= 0.75f;
		ya *= 0.75f;
	}
}