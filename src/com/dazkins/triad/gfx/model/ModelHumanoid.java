package com.dazkins.triad.gfx.model;

import com.dazkins.triad.game.entity.Facing;
import com.dazkins.triad.game.entity.mob.Mob;
import com.dazkins.triad.game.world.tile.Tile;
import com.dazkins.triad.gfx.Image;

public class ModelHumanoid extends Model {
	private Quad upFacingHead;
	private Quad leftFacingHead;
	private Quad rightFacingHead;
	private Quad downFacingHead;
	
	private Quad rightArm;
	private Quad leftArm;
	
	private Quad body;
	private Quad sideBody;
	
	private Quad leftLeg;
	private Quad rightLeg;
	
	public ModelHumanoid(Image i) {
		super(i);
		
		upFacingHead = new Quad(0, 0, 18, 16, 24, 0, 9, 8);
		downFacingHead = new Quad(0, 0, 18, 16, 0, 0, 9, 8);
		leftFacingHead = new Quad(0, 0, 18, 16, 16, 0, 9, 8);
		rightFacingHead = new Quad(0, 0, 18, 16, 8, 0, 9, 8);
		addQuad(upFacingHead);
		addQuad(downFacingHead);
		addQuad(leftFacingHead);
		addQuad(rightFacingHead);
		
		rightArm = new Quad(-3, -16, 10, 16, 0, 8, 5, 8);
		leftArm = new Quad(12, -16, 10, 16, 0, 8, 5, 8);
		rightArm.setCenterOfRotation(3, 0);
		leftArm.setCenterOfRotation(12, 0);
		addQuad(rightArm);
		addQuad(leftArm);
		
		body = new Quad(0, -16, 18, 18, 5, 8, 9, 9);
		sideBody = new Quad(4, -16, 10, 16, 0, 8, 5, 8);
		addQuad(body);
		addQuad(sideBody);
		
		leftLeg = new Quad(0, -32, 10, 16, 0, 8, 5, 8);
		rightLeg = new Quad(8, -32, 10, 16, 0, 8, 5, 8);
		addQuad(leftLeg);
		addQuad(rightLeg);
	}
		
	public void render(Mob m) {
		setOffset(m.getX(), m.getY());
		setDepth(Tile.yPosToDepth(m.getY()));
		
		Facing f = m.getFacing();
		if (f != Facing.UP)
			skipRender(upFacingHead);
		if (f != Facing.DOWN)
			skipRender(downFacingHead);
		if (f != Facing.LEFT)
			skipRender(leftFacingHead);
		if (f != Facing.RIGHT)
			skipRender(rightFacingHead);
		
		if (f == Facing.LEFT || f == Facing.RIGHT) {
			leftArm.addOffset(-8, 0);
			rightArm.addOffset(8, 0);
			leftLeg.addOffset(4, 0);
			rightLeg.addOffset(-4, 0);
			skipRender(body);
		} else {
			rightArm.setDepth(-0.0003f);
		}
		
		if (Math.abs(m.getXA()) >= m.getMovementSpeed() - 0.5 || Math.abs(m.getYA()) >= m.getMovementSpeed() - 0.5) {
			if (f == Facing.LEFT || f == Facing.RIGHT) {
				rightArm.addRotation((float) Math.sin(m.lifeTicks / 5.0f) * 20.0f);
				leftArm.addRotation((float) Math.sin(-m.lifeTicks / 5.0f) * 20.0f);
				rightLeg.addRotation((float) Math.sin((m.lifeTicks + 60) / 5.0f) * 50.0f);
				leftLeg.addRotation((float) Math.sin(-(m.lifeTicks + 60) / 5.0f) * 50.0f);
				rightLeg.setCenterOfRotation(8, -16);
				leftLeg.setCenterOfRotation(8, -16);
			} else {
				rightArm.addRotation((float) Math.sin(m.lifeTicks / 5.0f) * 13.0f);
				leftArm.addRotation((float) Math.sin(-m.lifeTicks / 5.0f) * 13.0f);
				rightLeg.addOffset(0, (float) Math.abs((Math.cos(-m.lifeTicks / 10.0f) * 8.0f)));
				leftLeg.addOffset(0, (float) Math.abs((Math.sin(m.lifeTicks / 10.0f) * 8.0f)));
			}
		} else {
			float rr = rightArm.getRotation();
			if (rr < 0)
				rightArm.addRotation(rr + 0.8f);
			if (rr > 0)
				rightArm.addRotation(rr - 0.8f);
			
			float lr = leftArm.getRotation();
			if (lr < 0)
				leftArm.addRotation(lr + 0.8f);
			if (lr > 0)
				leftArm.addRotation(lr - 0.8f);
		}
		
		super.render();
	}
}