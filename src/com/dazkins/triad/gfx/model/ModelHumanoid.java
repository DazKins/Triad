package com.dazkins.triad.gfx.model;

import com.dazkins.triad.game.entity.Facing;
import com.dazkins.triad.game.entity.mob.Mob;
import com.dazkins.triad.gfx.Image;

public class ModelHumanoid extends Model {
	private Quad head[] = new Quad[4];
	private Quad rightArm[] = new Quad[4];
	private Quad leftArm[] = new Quad[4];
	private Quad body[] = new Quad[4];
	private Quad leftLeg[] = new Quad[4];
	private Quad rightLeg[] = new Quad[4];
	
	public ModelHumanoid(Image i) {
		super(i);

		int up = Facing.UP.ordinal();
		int down = Facing.DOWN.ordinal();
		int	left = Facing.LEFT.ordinal();
		int right = Facing.RIGHT.ordinal();
		
		head[up] = new Quad(-9, 32, 18, 16, 27, 0, 9, 8);
		head[down] = new Quad(-9, 32, 18, 16, 0, 0, 9, 8);
		head[left] = new Quad(-9, 32, 18, 16, 18, 0, 9, 8);
		head[right] = new Quad(-9, 32, 18, 16, 9, 0, 9, 8);
		addQuads(head);

		rightArm[up] = new Quad(4, 16, 10, 18, 0, 8, 5, 9);
		rightArm[down] = new Quad(-14, 16, 10, 18, 5, 8, 5, 9);
		rightArm[left] = new Quad(-4, 16, 10, 18, 10, 8, 5, 9);
		rightArm[right] = new Quad(-4, 16, 10, 18, 15, 8, 5, 9);
		addQuads(rightArm);
		
		rightLeg[up] = new Quad(-1, 0, 10, 18, 20, 8, 5, 9);
		rightLeg[down] = new Quad(-9, 0, 10, 18, 25, 8, 5, 9);
		rightLeg[left] = new Quad(-4, 0, 10, 18, 30, 8, 5, 9);
		rightLeg[right] = new Quad(-4, 0, 10, 18, 35, 8, 5, 9);
		addQuads(rightLeg);
		
		leftArm[up] = new Quad(-14, 16, 10, 18, 0, 17, 5, 9);
		leftArm[down] = new Quad(4, 16, 10, 18, 5, 17, 5, 9);
		leftArm[left] = new Quad(-4, 16, 10, 18, 10, 17, 5, 9);
		leftArm[right] = new Quad(-4, 16, 10, 18, 15, 17, 5, 9);
		addQuads(leftArm);
		
		leftLeg[up] = new Quad(-9, 0, 10, 18, 20, 17, 5, 9);
		leftLeg[down] = new Quad(-1, 0, 10, 18, 25, 17, 5, 9);
		leftLeg[left] = new Quad(-4, 0, 10, 18, 30, 17, 5, 9);
		leftLeg[right] = new Quad(-4, 0, 10, 18, 35, 17, 5, 9);
		addQuads(leftLeg);
		
		body[up] = new Quad(-9, 16, 18, 18, 0, 26, 9, 9);
		body[down] = new Quad(-9, 16, 18, 18, 9, 26, 9, 9);
		body[left] = new Quad(-4, 16, 10, 18, 18, 26, 5, 9);
		body[right] = new Quad(-4, 16, 10, 18, 23, 26, 5, 9);
		addQuads(body);
	}
		
	public void render(Mob m) {
		setOffset(m.getX(), m.getY());
		
		int ordinal = m.getFacing().ordinal();
		
		enableSelectiveRendering();
		
		renderQuad(head[ordinal]);
		renderQuad(rightArm[ordinal]);
		renderQuad(leftArm[ordinal]);
		renderQuad(rightLeg[ordinal]);
		renderQuad(leftLeg[ordinal]);
		renderQuad(body[ordinal]);
		
		super.render();
	}
}