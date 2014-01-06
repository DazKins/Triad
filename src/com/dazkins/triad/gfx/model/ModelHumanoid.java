package com.dazkins.triad.gfx.model;

import com.dazkins.triad.game.entity.Entity;
import com.dazkins.triad.game.entity.Facing;
import com.dazkins.triad.game.entity.mob.Mob;
import com.dazkins.triad.game.entity.mob.MovementState;
import com.dazkins.triad.game.world.tile.Tile;
import com.dazkins.triad.gfx.Image;

public class ModelHumanoid extends Model {
	protected Quad head[] = new Quad[4];
	protected Quad rightArm[] = new Quad[4];
	protected Quad leftArm[] = new Quad[4];
	protected Quad body[] = new Quad[4];
	protected Quad leftLeg[] = new Quad[4];
	protected Quad rightLeg[] = new Quad[4];
	
	public ModelHumanoid(Image i) {
		super(i);

		int up = Facing.UP.ordinal();
		int down = Facing.DOWN.ordinal();
		int	left = Facing.LEFT.ordinal();
		int right = Facing.RIGHT.ordinal();
		
		head[up] = new Quad(-9, 32, 18, 16, 27, 0, 9, 8);
		head[up].setRenderLayer(5);
		head[down] = new Quad(-9, 32, 18, 16, 0, 0, 9, 8);
		head[down].setRenderLayer(5);
		head[left] = new Quad(-9, 32, 18, 16, 18, 0, 9, 8);
		head[left].setRenderLayer(5);
		head[right] = new Quad(-9, 32, 18, 16, 9, 0, 9, 8);
		head[right].setRenderLayer(5);
		addQuads(head);

		rightArm[up] = new Quad(4, 16, 10, 18, 0, 8, 5, 9);
		rightArm[up].setCenterOfRotation(9, 34);
		rightArm[up].setRenderLayer(4);
		rightArm[down] = new Quad(-14, 16, 10, 18, 5, 8, 5, 9);
		rightArm[down].setCenterOfRotation(-9, 34);
		rightArm[down].setRenderLayer(2);
		rightArm[left] = new Quad(-4, 16, 10, 18, 10, 8, 5, 9);
		rightArm[left].setCenterOfRotation(1, 34);
		rightArm[left].setRenderLayer(4);
		rightArm[right] = new Quad(-4, 16, 10, 18, 15, 8, 5, 9);
		rightArm[right].setCenterOfRotation(1, 34);
		rightArm[right].setRenderLayer(2);
		addQuads(rightArm);
		
		
		rightLeg[up] = new Quad(-1, 0, 10, 18, 20, 8, 5, 9);
		rightLeg[up].setCenterOfRotation(4, 18);
		rightLeg[up].setRenderLayer(1);
		rightLeg[down] = new Quad(-9, 0, 10, 18, 25, 8, 5, 9);
		rightLeg[down].setCenterOfRotation(-4, 18);
		rightLeg[down].setRenderLayer(1);
		rightLeg[left] = new Quad(-4, 0, 10, 18, 30, 8, 5, 9);
		rightLeg[left].setCenterOfRotation(1, 18);
		rightLeg[left].setRenderLayer(1);
		rightLeg[right] = new Quad(-4, 0, 10, 18, 35, 8, 5, 9);
		rightLeg[right].setCenterOfRotation(1, 18);
		rightLeg[right].setRenderLayer(3);
		addQuads(rightLeg);
		
		
		leftArm[up] = new Quad(-14, 16, 10, 18, 0, 17, 5, 9);
		leftArm[up].setCenterOfRotation(-9, 34);
		leftArm[up].setRenderLayer(2);
		leftArm[down] = new Quad(4, 16, 10, 18, 5, 17, 5, 9);
		leftArm[down].setCenterOfRotation(9, 34);
		leftArm[down].setRenderLayer(4);
		leftArm[left] = new Quad(-4, 16, 10, 18, 10, 17, 5, 9);
		leftArm[left].setCenterOfRotation(1, 34);
		leftArm[left].setRenderLayer(2);
		leftArm[right] = new Quad(-4, 16, 10, 18, 15, 17, 5, 9);
		leftArm[right].setCenterOfRotation(1, 34);
		leftArm[right].setRenderLayer(4);
		addQuads(leftArm);
		
		
		leftLeg[up] = new Quad(-9, 0, 10, 18, 20, 17, 5, 9);
		leftLeg[up].setCenterOfRotation(-4, 18);
		leftLeg[up].setRenderLayer(1);
		leftLeg[down] = new Quad(-1, 0, 10, 18, 25, 17, 5, 9);
		leftLeg[down].setCenterOfRotation(4, 18);
		leftLeg[down].setRenderLayer(1);
		leftLeg[left] = new Quad(-4, 0, 10, 18, 30, 17, 5, 9);
		leftLeg[left].setCenterOfRotation(1, 18);
		leftLeg[left].setRenderLayer(3);
		leftLeg[right] = new Quad(-4, 0, 10, 18, 35, 17, 5, 9);
		leftLeg[right].setCenterOfRotation(1, 18);
		leftLeg[right].setRenderLayer(1);
		addQuads(leftLeg);
		
		
		body[up] = new Quad(-9, 16, 18, 18, 0, 26, 9, 9);
		body[up].setRenderLayer(3);
		body[down] = new Quad(-9, 16, 18, 18, 9, 26, 9, 9);
		body[down].setRenderLayer(3);
		body[left] = new Quad(-4, 16, 10, 18, 18, 26, 5, 9);
		body[left].setRenderLayer(3);
		body[right] = new Quad(-4, 16, 10, 18, 23, 26, 5, 9);
		body[right].setRenderLayer(3);
		addQuads(body);
	}
		
	public void render(Entity e) {
		setOffset(e.getX(), e.getY());
		setDepth(Tile.yPosToDepth(e.getY()));
		
		Facing f = e.getFacing();
		int ordinal = f.ordinal();
		
		enableSelectiveRendering();
		
		addQuadToRenderQeue(head[ordinal]);
		addQuadToRenderQeue(rightArm[ordinal]);
		addQuadToRenderQeue(leftArm[ordinal]);
		addQuadToRenderQeue(rightLeg[ordinal]);
		addQuadToRenderQeue(leftLeg[ordinal]);
		addQuadToRenderQeue(body[ordinal]);
		
		super.render();
	}

	public void updateAnimationState(Entity e) {
		Mob m = (Mob) e;
		Facing f = m.getFacing();
		int ordinal = f.ordinal();
		
		Quad cHead = head[ordinal];
		Quad cRightArm = rightArm[ordinal];
		Quad cLeftArm = leftArm[ordinal];
		Quad cRightLeg = rightLeg[ordinal];
		Quad cLeftLeg = leftLeg[ordinal];
		Quad cBody = body[ordinal];
		
		if (m.getMovementState() == MovementState.MOVING) {
			if (f == Facing.LEFT || f == Facing.RIGHT) {
				cRightArm.setRotation((float) Math.cos(m.lifeTicks / 8.0f) * 50.0f);
				cLeftArm.setRotation((float) -Math.cos(m.lifeTicks / 8.0f) * 50.0f);
				cRightLeg.setRotation((float) Math.sin(m.lifeTicks / 10.0f) * 50.0f);
				cLeftLeg.setRotation((float) -Math.sin(m.lifeTicks / 10.0f) * 50.0f);
			} else {
				cRightArm.setRotation((float) Math.cos(m.lifeTicks / 10.0f) * 15.0f);
				cLeftArm.setRotation((float) -Math.sin(m.lifeTicks / 10.0f) * 15.0f);
				cRightLeg.setOffset(0, (float) (Math.sin(m.lifeTicks / 5.0f) + 1.0f) * 3.0f);
				cLeftLeg.setOffset(0, (float) (-Math.sin(m.lifeTicks / 5.0f) + 1.0f) * 3.0f);
			}
		} else {
			float crar = cRightArm.getRotation();
			if (crar != 0) {
				if (crar > 0)
					cRightArm.setRotation(crar - 0.05f);
				if (crar < 0)
					cRightArm.setRotation(crar + 0.05f);
			}
			float clar = cLeftArm.getRotation();
			if (clar != 0) {
				if (clar > 0)
					cLeftArm.setRotation(clar - 0.05f);
				if (clar < 0)
					cLeftArm.setRotation(clar + 0.05f);
			}
			float crlr = cRightLeg.getRotation();
			if (crlr != 0) {
				if (crlr > 0)
					cRightLeg.setRotation(crlr - 0.05f);
				if (crlr < 0)
					cRightLeg.setRotation(crlr + 0.05f);
			}
			float cllr = cLeftLeg.getRotation();
			if (cllr != 0) {
				if (cllr > 0)
					cLeftLeg.setRotation(cllr - 0.05f);
				if (cllr < 0)
					cLeftLeg.setRotation(cllr + 0.05f);
			}
			float crlo = cRightLeg.getOffsetY();
			if (crlo != 0) {
				if (crlo > 0)
					cRightLeg.setOffset(0, crlo - 0.005f);
				if (crlo < 0)
					cRightLeg.setOffset(0, crlo + 0.005f);
			}
			float cllo = cLeftLeg.getOffsetY();
			if (cllo != 0) {
				if (cllo > 0)
					cLeftLeg.setOffset(0, cllo - 0.005f);
				if (cllo < 0)
					cLeftLeg.setOffset(0, cllo + 0.005f);
			}
		}
	}
}