package com.dazkins.triad.game.entity;

import com.dazkins.triad.game.world.World;
import com.dazkins.triad.gfx.Camera;
import com.dazkins.triad.gfx.model.Model;
import com.dazkins.triad.gfx.model.ModelFlower;
import com.dazkins.triad.math.AABB;

public class EntityFlower extends Entity
{
	public EntityFlower(World w, float x, float y)
	{
		super(w, EntityIDStorage.FLOWER, x, y, "flower");
	}

	public AABB getAABB()
	{
		return null;
	}
}