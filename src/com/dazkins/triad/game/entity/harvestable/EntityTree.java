package com.dazkins.triad.game.entity.harvestable;

import com.dazkins.triad.game.inventory.item.Item;
import com.dazkins.triad.game.world.World;
import com.dazkins.triad.gfx.Camera;
import com.dazkins.triad.gfx.model.Model;
import com.dazkins.triad.gfx.model.ModelTree;
import com.dazkins.triad.gfx.model.animation.AnimationTreeShake;
import com.dazkins.triad.math.AABB;

public class EntityTree extends EntityHarvestable {
	public EntityTree(World w, float x, float y) {
		super(w, x, y, "tree", 30);
	}

	public void render(Camera cam) {
		renderShadow(cam, x - 15, y - 5, 30, 10);
		super.render(cam);
	}
	
	public void dropLoot() {
		Item.dropItemStack(world, x, y, Item.log, (int) (Math.random() * 3) + 1);
	}

	public void tick() {
		if (model != null)
			model.updateAnimationState(this);
	}

	public void harvest(int d) {
		super.harvest(d);
		model.addAnimation(new AnimationTreeShake(this), 1, true);
	}

	public void initModel() {
		model = new ModelTree();
	}

	public AABB getAABB() {
		return new AABB(x, y, x + 32, y + 32);
	}
}