package com.dazkins.triad.game.entity;

import com.dazkins.triad.game.world.World;
import com.dazkins.triad.gfx.Camera;
import com.dazkins.triad.gfx.model.Model;
import com.dazkins.triad.math.AABB;

public class EntitySandGrass extends Entity
{

	public EntitySandGrass(World w, float x, float y)
	{
		super(w, StorageEntityID.SANDGRASS, x, y, "sandGrass");
	}

	public AABB getAABB()
	{
		return null;
	}
}