package com.dazkins.triad.game.entity.renderer;

import org.lwjgl.opengl.GL11;

import com.dazkins.triad.game.world.IWorldAccess;
import com.dazkins.triad.game.world.tile.Tile;
import com.dazkins.triad.gfx.Camera;
import com.dazkins.triad.gfx.Color;
import com.dazkins.triad.gfx.model.Model;
import com.dazkins.triad.gfx.model.animation.StorageAnimation;
import com.dazkins.triad.util.TriadLogger;

public abstract class EntityRenderer
{
	// ID of the entity the rendering instance is refering to
	private int id;

	protected float x;
	protected float y;
	
	private int facing;

	private boolean isModelReady;

	private Model model;

	private boolean idAssigned;

	protected IWorldAccess world;

	public abstract void initModel();

	public void setID(int id)
	{
		if (!idAssigned)
		{
			this.id = id;
			idAssigned = true;
		} else
		{
			TriadLogger.log("ID has already been assigned to this entity", true);
		}
	}
	
	public void addAnimation(int aID, int i, boolean o, float s)
	{
		if (isModelReady)
		{
			model.addAnimation(StorageAnimation.getAndInstantiateAnimation(aID, this, s), i, o);
		}
	}

	public void setWorld(IWorldAccess wo)
	{
		world = wo;
	}

	public int getID()
	{
		return id;
	}
	
	public void setFacing(int f)
	{
		facing = f;
	}

	public void setX(float x)
	{
		this.x = x;
	}

	public void setY(float y)
	{
		this.y = y;
	}

	protected void setModel(Model m)
	{
		model = m;
		isModelReady = true;
	}

	public float getX()
	{
		return x;
	}

	public float getY()
	{
		return y;
	}

	public void render(Camera cam)
	{
		if (isModelReady)
		{
			model.setOffset(x, y);
			model.setDepth(Tile.yPosToDepthRelativeToCamera(cam, y));
			Color t = world.getTileColor((int) (x / Tile.TILESIZE), (int) (y / Tile.TILESIZE));
			if (t != null)
				GL11.glColor3f(t.getDR(), t.getDG(), t.getDB());
			model.render(facing);
		} else
			initModel();
	}
	
	public void tick()
	{
		if (isModelReady)
			model.updateAnimationState();
	}

	public int getFacing()
	{
		return facing;
	}
}