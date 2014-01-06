package com.dazkins.triad.gfx.model;

import com.dazkins.triad.game.entity.Entity;
import com.dazkins.triad.game.entity.Facing;
import com.dazkins.triad.game.entity.mob.Mob;
import com.dazkins.triad.game.entity.mob.MovementState;
import com.dazkins.triad.game.world.tile.Tile;
import com.dazkins.triad.gfx.Font;
import com.dazkins.triad.gfx.Image;

public class ModelZombie extends ModelHumanoid {
	public ModelZombie(Image i) {
		super(i);
	}
	
	public void updateAnimationState(Entity e) {
		super.updateAnimationState(e);
		
		Facing f = e.getFacing();
		int ordinal = f.ordinal();
		
		if (f == Facing.LEFT) {
			rightArm[ordinal].setRotation(-90);
			leftArm[ordinal].setRotation(-90);
		} else if (f == Facing.RIGHT) {
			rightArm[ordinal].setRotation(90);
			leftArm[ordinal].setRotation(90);
		}
	}
}