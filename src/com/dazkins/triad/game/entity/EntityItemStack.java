package com.dazkins.triad.game.entity;

import com.dazkins.triad.game.inventory.item.ItemStack;
import com.dazkins.triad.game.world.World;
import com.dazkins.triad.game.world.tile.Tile;
import com.dazkins.triad.gfx.Camera;
import com.dazkins.triad.math.AABB;

public class EntityItemStack extends Entity {
	private ItemStack is;
	
	public EntityItemStack(World w, float x, float y, ItemStack is) {
		super(w, x, y, null);
		this.is = is;
	}

	public void renderToPlayerGui(Camera c) {
		
	}

	public void render() {
		is.getItemType().renderIcon(x, y, Tile.yPosToDepth(y), 2);
	}

	public AABB getAABB() {
		return null;
	}
}