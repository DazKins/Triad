package com.dazkins.triad.gfx.model;

import com.dazkins.triad.game.entity.Facing;
import com.dazkins.triad.gfx.Image;

public class ModelDoor extends Model
{
	private Quad[] quads;

	public ModelDoor()
	{
		super(Image.getImageFromName("door"));

		quads = new Quad[4];

		quads[Facing.UP] = new Quad(0, 0, 32, 64, 30, 0, 16, 32);
		quads[Facing.DOWN] = new Quad(0, 0, 32, 64, 0, 0, 16, 32);
		quads[Facing.LEFT] = new Quad(-6, -16, 14, 80, 16, 0, 7, 40);
		quads[Facing.RIGHT] = new Quad(0, -16, 14, 80, 23, 0, 7, 40);
		addQuads(quads);
	}
	
	public void render(int f)
	{
		addQuadToRenderQueue(quads[f]);

		super.render();
	}
}