package com.dazkins.triad.gfx.model;

import com.dazkins.triad.game.entity.mob.EntityPlayer;

public class ModelPlayer extends Model {
	private Quad head;
	private Quad body;
	private Quad rightArm;
	private Quad leftArm;
	
	public ModelPlayer() {
		super(10);
		head = new Quad(18, 14, 2 * 16, 0, 9, 7, 1.0f, 0);
		quads.add(head);
		body = new Quad(18, 18, 2 * 16, 7, 9, 9, 1.0f, 0);
		body.setOffset(0, -18);
		quads.add(body);
		rightArm = new Quad(10, 16, 2 * 16 + 9, 0, 5, 8, 1.0f, 0);
		rightArm.setOffset(-5, -13);
		rightArm.setCenterOfRotation(-1, 0);
		quads.add(rightArm);
		leftArm = new Quad(10, 16, 2 * 16 + 9, 0, 5, 8, 1.0f, 0);
		leftArm.setOffset(15, -13);
		leftArm.setCenterOfRotation(19, 0);
		quads.add(leftArm);
	}
	
	public void render(EntityPlayer p) {
		body.setDepth(-0.0001f);
		rightArm.setDepth(-0.0002f);
		setOffset(p.getX(), p.getY());
		if (Math.abs(p.getXA()) >= 1.5 || Math.abs(p.getYA()) >= 1.5) {
			leftArm.setRotation((float)Math.sin(p.lifeTicks / 5.0f) * 10.0f);
			rightArm.setRotation((float)Math.cos(-p.lifeTicks / 5.0f) * 10.0f);
		} else {
			leftArm.setRotation(0);
			rightArm.setRotation(0);
		}
		super.render();
	}
}