package com.dazkins.triad.gfx.model;

import com.dazkins.triad.game.entity.Facing;
import com.dazkins.triad.game.entity.mob.EntityPlayer;

public class ModelPlayer extends Model {
	private Quad frontFacingHead;
	
	public ModelPlayer() {
//		frontFacingHead = new Quad();
	}
		
	public void render(EntityPlayer p) {
		setOffset(p.getX(), p.getY());
		if (Math.abs(p.getXA()) >= p.getMovementSpeed() - 0.5f || Math.abs(p.getYA()) >= p.getMovementSpeed() - 0.5f) {
			leftArm.setRotation((float)Math.sin(p.lifeTicks / 5.0f) * 10.0f);
			rightArm.setRotation((float)Math.cos(-p.lifeTicks / 5.0f) * 10.0f);
		} else {
			leftArm.setRotation(0);
			rightArm.setRotation(0);
		if (p.getFacing() == Facing.LEFT) {
				
		}
		super.render();
	}
}