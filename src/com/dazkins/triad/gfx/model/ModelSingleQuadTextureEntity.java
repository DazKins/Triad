package com.dazkins.triad.gfx.model;

import com.dazkins.triad.gfx.Image;
import com.dazkins.triad.gfx.RenderContext;

public class ModelSingleQuadTextureEntity extends Model
{
	public ModelSingleQuadTextureEntity(Image i)
	{
		super(i);
		addQuad(new Quad(-16, 0, 32, 32, 0, 0, 16, 16));
	}

	public ModelSingleQuadTextureEntity(Image i, int tx, int ty, int tw, int th)
	{
		super(i);
		addQuad(new Quad(-tw, 0, tw * 2, th * 2, tx, ty, tw, th));
	}

	public Quad getQuad()
	{
		return quads.get(0);
	}

	//TODO maybe change this so it doesn't require a facing parameter
	@Override
	public void render(RenderContext rc, int facing)
	{
		super.render(rc);
	}
}