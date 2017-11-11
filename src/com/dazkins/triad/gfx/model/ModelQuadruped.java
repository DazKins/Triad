package com.dazkins.triad.gfx.model;

import com.dazkins.triad.game.entity.Entity;
import com.dazkins.triad.game.entity.Facing;
import com.dazkins.triad.game.world.tile.Tile;
import com.dazkins.triad.gfx.Camera;
import com.dazkins.triad.gfx.Image;
import com.dazkins.triad.gfx.RenderContext;

public class ModelQuadruped extends Model
{
	private Quad head[] = new Quad[4];
	private Quad frontRightLeg[] = new Quad[4];
	private Quad frontLeftLeg[] = new Quad[4];
	private Quad backRightLeg[] = new Quad[4];
	private Quad backLeftLeg[] = new Quad[4];
	private Quad body[] = new Quad[4];

	public ModelQuadruped(Image i)
	{
		super(i);

		int up = Facing.UP;
		int down = Facing.DOWN;
		int right = Facing.RIGHT;
		int left = Facing.LEFT;

		head[up] = new Quad(0, 0, 16, 14, 24, 0, 8, 7);
		head[up].setRenderLayer(1);
		head[down] = new Quad(0, 0, 16, 14, 0, 0, 8, 7);
		head[down].setRenderLayer(7);
		head[right] = new Quad(0, 0, 16, 14, 8, 0, 8, 7);
		head[right].setRenderLayer(3);
		head[left] = new Quad(0, 0, 16, 14, 16, 0, 8, 7);
		head[left].setRenderLayer(3);
		addQuads(head);

		body[up] = new Quad(-2, -7, 20, 12, 22, 25, 10, 6);
		body[up].setRenderLayer(2);
		body[down] = new Quad(-2, -4, 20, 16, 32, 25, 10, 8);
		body[down].setRenderLayer(6);
		body[right] = new Quad(-17, -4, 22, 14, 11, 25, 11, 7);
		body[right].setRenderLayer(2);
		body[left] = new Quad(11, -4, 22, 14, 0, 25, 11, 7);
		body[left].setRenderLayer(2);
		addQuads(body);

		frontRightLeg[right] = new Quad(-17, -10, 10, 12, 25, 7, 5, 6);
		frontRightLeg[right].setCenterOfRotation(-12, 2);
		frontRightLeg[right].setRenderLayer(3);
		frontRightLeg[left] = new Quad(11, -10, 10, 12, 20, 7, 5, 6);
		frontRightLeg[left].setCenterOfRotation(16, 2);
		frontRightLeg[left].setRenderLayer(1);
		frontRightLeg[down] = new Quad(-2, -10, 10, 12, 35, 7, 5, 6);
		frontRightLeg[down].setRenderLayer(1);
		addQuads(frontRightLeg);

		frontLeftLeg[left] = new Quad(11, -10, 10, 12, 0, 7, 5, 6);
		frontLeftLeg[left].setRenderLayer(3);
		frontLeftLeg[left].setCenterOfRotation(16, 2);
		frontLeftLeg[right] = new Quad(-17, -10, 10, 12, 5, 7, 5, 6);
		frontLeftLeg[right].setCenterOfRotation(-12, 2);
		frontLeftLeg[right].setRenderLayer(1);
		frontLeftLeg[down] = new Quad(8, -10, 10, 12, 15, 7, 5, 6);
		frontLeftLeg[down].setRenderLayer(1);
		addQuads(frontLeftLeg);

		backRightLeg[right] = new Quad(-5, -10, 10, 12, 25, 16, 5, 6);
		backRightLeg[right].setCenterOfRotation(0, 2);
		backRightLeg[right].setRenderLayer(3);
		backRightLeg[left] = new Quad(23, -10, 10, 12, 20, 16, 5, 6);
		backRightLeg[left].setCenterOfRotation(28, 2);
		backRightLeg[left].setRenderLayer(1);
		backRightLeg[up] = new Quad(8, -13, 10, 12, 30, 16, 5, 6);
		backRightLeg[up].setRenderLayer(1);
		addQuads(backRightLeg);

		backLeftLeg[left] = new Quad(23, -10, 10, 12, 0, 16, 5, 6);
		backLeftLeg[left].setCenterOfRotation(28, 2);
		backLeftLeg[left].setRenderLayer(3);
		backLeftLeg[right] = new Quad(-5, -10, 10, 12, 5, 16, 5, 6);
		backLeftLeg[right].setCenterOfRotation(0, 2);
		backLeftLeg[right].setRenderLayer(1);
		backLeftLeg[up] = new Quad(-2, -13, 10, 12, 10, 16, 5, 6);
		backLeftLeg[up].setRenderLayer(1);
		addQuads(backLeftLeg);
	}

	public Quad[] getFrontLeftLeg()
	{
		return frontLeftLeg;
	}

	public Quad[] getBackLeftLeg()
	{
		return backLeftLeg;
	}

	public Quad[] getFrontRightLeg()
	{
		return frontRightLeg;
	}

	public Quad[] getBackRightLeg()
	{
		return backRightLeg;
	}

	public void render(RenderContext rc, int f)
	{
		super.enableSelectiveRendering();

		addQuadToRenderQueue(head[f]);
		addQuadToRenderQueue(body[f]);
		addQuadToRenderQueue(frontRightLeg[f]);
		addQuadToRenderQueue(frontLeftLeg[f]);
		addQuadToRenderQueue(backRightLeg[f]);
		addQuadToRenderQueue(backLeftLeg[f]);

		super.render(rc);
	}
}