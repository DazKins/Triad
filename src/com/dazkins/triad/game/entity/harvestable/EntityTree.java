package com.dazkins.triad.game.entity.harvestable;

import com.dazkins.triad.game.entity.EntityIDStorage;
import com.dazkins.triad.game.inventory.item.Item;
import com.dazkins.triad.game.world.World;
import com.dazkins.triad.gfx.Camera;
import com.dazkins.triad.gfx.model.Model;
import com.dazkins.triad.gfx.model.ModelTree;
import com.dazkins.triad.gfx.model.animation.AnimationTreeShake;
import com.dazkins.triad.math.AABB;

public class EntityTree extends EntityHarvestable
{
	public EntityTree(World w, float x, float y)
	{
		super(w, EntityIDStorage.TREE, x, y, "tree", 30);
	}

	public void dropLoot()
	{
		Item.dropItemStack(world, x, y, Item.log, (int) (Math.random() * 3) + 1);
	}

	public void tick()
	{
	}

	public void harvest(int d)
	{
		super.harvest(d);
	}

	public AABB getAABB()
	{
		return new AABB(x - 16, y - 5, x + 16, y + 10);
	}
}