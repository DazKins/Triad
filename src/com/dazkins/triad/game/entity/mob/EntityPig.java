package com.dazkins.triad.game.entity.mob;

import com.dazkins.triad.game.entity.Entity;
import com.dazkins.triad.game.entity.EntityIDStorage;
import com.dazkins.triad.game.entity.Facing;
import com.dazkins.triad.game.world.World;
import com.dazkins.triad.gfx.Camera;
import com.dazkins.triad.gfx.model.ModelPig;
import com.dazkins.triad.gfx.model.animation.AnimationQuadrupedWalking;
import com.dazkins.triad.math.AABB;

public class EntityPig extends Mob
{
	private boolean moving;
	private float dir;

	public EntityPig(World w, float x, float y, int h)
	{
		super(w, EntityIDStorage.PIG, x, y, "pig", h);
		moving = true;
		dir = (float) (Math.random() * Math.PI * 2);
	}

	public float getMovementSpeed()
	{
		return 0.25f;
	}

	public int getMaxHealth()
	{
		return 10;
	}

	public AABB getAABB()
	{
		if (getFacing() == Facing.RIGHT)
			return new AABB(x - 24, y, x, y + 10);
		if (getFacing() == Facing.LEFT)
			return new AABB(x, y, x + 24, y + 10);
		if (getFacing() == Facing.UP)
			return new AABB(x - 8, y, x + 8, y + 18);
		else
			return new AABB(x - 8, y, x + 8, y + 18);
	}

	public boolean mayBePushedBy(Entity e)
	{
		return true;
	}

	public void tick()
	{
		super.tick();
		int r = (int) (Math.random() * (moving ? 600 : 1000));
		if (r == 1)
		{
			moving = !moving;
			dir = (float) (Math.random() * Math.PI * 2);
		}

		if (moving)
		{
			float xa = (float) Math.cos(dir) * getMovementSpeed();
			float ya = (float) Math.sin(dir) * getMovementSpeed();

			addXAMod(xa);
			addYAMod(ya);

			setFacingBasedOnVelocities(xa, ya);
		}

		xa *= 0.75;
		ya *= 0.75;

		move();
	}
}