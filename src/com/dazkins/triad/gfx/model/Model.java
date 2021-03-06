package com.dazkins.triad.gfx.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

import com.dazkins.triad.gfx.RenderContext;
import com.dazkins.triad.math.Matrix3;
import org.lwjgl.opengl.GL11;

import com.dazkins.triad.game.entity.Entity;
import com.dazkins.triad.gfx.Camera;
import com.dazkins.triad.gfx.Image;
import com.dazkins.triad.gfx.model.animation.Animation;
import com.dazkins.triad.math.AABB;
import com.dazkins.triad.util.TriadLogger;

public abstract class Model
{
	protected List<Quad> quads;
	protected List<Integer> quadRenders;
	protected List<Quad> tempQuads;

	private Image img;

	protected float offsetX, offsetY;

	private boolean selectiveRendering;

	private Animation anims[];

	private float xMin, yMin;
	private float xMax, yMax;

	public static ModelFlower flower = new ModelFlower();
	public static ModelSandGrass sandGrass = new ModelSandGrass();

	public AABB getRenderAABB()
	{
		return new AABB(xMin, yMin, xMax, yMax);
	}

	public Quad getQuad(int i)
	{
		return quads.get(i);
	}

	public void setOffset(float x, float y)
	{
		offsetX = x;
		offsetY = y;
	}
	
	public abstract void render(RenderContext rc, int facing);

	protected void addQuads(Quad[] q)
	{
		for (int i = 0; i < q.length; i++)
		{
			if (q[i] != null)
				addQuad(q[i]);
		}
	}

	protected void addQuad(Quad q)
	{
		q.init(img);
		q.generate();
		getBoundsAndRevalidateRender(q);
		quads.add(q);
	}

	private void getBoundsAndRevalidateRender(Quad q)
	{
		AABB b = q.getRenderAABB();
		if (b.getX0() < xMin)
			xMin = b.getX0();
		if (b.getY0() < yMin)
			yMin = b.getY0();
		if (b.getX1() > xMax)
			xMax = b.getX1();
		if (b.getY1() > yMax)
			yMax = b.getY1();
	}

	protected void enableSelectiveRendering()
	{
		selectiveRendering = true;
	}

	protected void disableSelectiveRendering()
	{
		selectiveRendering = false;
	}

	public void addQuadToRenderQueue(Quad quad)
	{
		if (!selectiveRendering)
		{
			TriadLogger.log("Warning! Selective rendering is not enabled!", true);
		}
		quadRenders.add(quads.indexOf(quad));
	}

	public Model(Image i)
	{
		anims = new Animation[50];
		quads = new ArrayList<Quad>();
		quadRenders = new ArrayList<Integer>();
		tempQuads = new ArrayList<Quad>();
		img = i;
	}

	public void updateAnimationState()
	{
		for (int i = 0; i < anims.length; i++)
		{
			if (anims[i] != null)
				anims[i].updateState();
		}
	}

	public boolean hasAnimation()
	{
		for (int i = 0; i < anims.length; i++)
		{
			if (anims[i] != null)
				return false;
		}
		return true;
	}

	public static final RenderSorter rSort = new RenderSorter();

	private static class RenderSorter implements Comparator<Quad>
	{
		public int compare(Quad q0, Quad q1)
		{
			return q1.getRenderLayer() < q0.getRenderLayer() ? 1 : -1;
		}
	}

	public void render(RenderContext rc)
	{
		ArrayList<Quad> quadsToRender = new ArrayList<>();
		if (selectiveRendering)
		{
			for (int i = 0; i < quadRenders.size(); i++)
			{
				int index = quadRenders.get(i);
				if (index >= 0)
				{
					Quad q = quads.get(index);
					quadsToRender.add(q);
					quadsToRender.addAll(q.getChildQuads());
					quadsToRender.addAll(q.getTemporaryChildQuads());
				}
			}
		} else
		{
			for (int i = 0; i < quads.size(); i++)
			{
				Quad q = quads.get(i);
				quadsToRender.add(q);
				quadsToRender.addAll(q.getChildQuads());
				quadsToRender.addAll(q.getTemporaryChildQuads());
			}
		}
		for (Quad q : tempQuads)
		{
			quadsToRender.add(q);
		}
		quadsToRender.sort(rSort);

		resetRenderBounds();
		loadRenderBounds(quadsToRender);

		rc.getMatrixStack().push();

		rc.getMatrixStack().transform(Matrix3.translate(offsetX, offsetY));
		for (int i = 0; i < quadsToRender.size(); i++)
		{
			quadsToRender.get(i).render(rc);
		}
		rc.getMatrixStack().pop();

		quadRenders.clear();
		tempQuads.clear();
	}

	public void loadRenderBounds(ArrayList<Quad> quads)
	{
		for (Quad q : quads)
		{
			getBoundsAndRevalidateRender(q);
		}
	}

	public void resetRenderBounds()
	{
		xMin = 0;
		yMin = 0;
		xMax = 0;
		yMax = 0;
	}

	public void removeAnimations()
	{
		Arrays.fill(anims, null);
	}

	public void addTemporaryQuad(Quad q)
	{
		if (q != null)
		{
			tempQuads.add(q);
			getBoundsAndRevalidateRender(q);
		}
	}

	public boolean hasInstanceOfAnim(Animation a)
	{
		for (int i = 0; i < anims.length; i++)
		{
			if (anims[i] != null)
			{
				if (anims[i].getID() == a.getID())
					return true;
			}
		}
		return false;
	}

	public void addAnimation(Animation a, int i, boolean overwrite)
	{
		if (overwrite)
		{
			a.init(this);
			anims[i] = a;
		} else
		{
			if (!hasInstanceOfAnim(a))
			{
				a.init(this);
				anims[i] = a;
			}
		}
	}

	private void renderQuad(RenderContext rc, Quad q)
	{
		q.render(rc);
	}

	public void setImage(Image img)
	{
		this.img = img;
	}

	public void animationStop(Animation a)
	{
		for (int i = 0; i < anims.length; i++)
		{
			if (anims[i] == a)
			{
				anims[i] = null;
			}
		}
	}
}