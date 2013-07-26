package com.dazkins.triad.gfx.model;

import com.dazkins.triad.game.entity.Facing;
import com.dazkins.triad.game.entity.mob.EntityPlayer;

public class ModelPlayer extends Model {
	private Quad frontFacingHead;
	
	public ModelPlayer() {
		frontFacingHead = new Quad(32, 32, 2 * 16, 0, 9, 8, 1.0f, 0);
		quads.add(frontFacingHead);
	}
		
	public void render(EntityPlayer p) {
		setOffset(p.getX(), p.getY());
		super.render();
	}
}