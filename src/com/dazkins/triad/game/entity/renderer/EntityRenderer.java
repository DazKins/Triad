package com.dazkins.triad.game.entity.renderer;

import org.lwjgl.opengl.GL11;

import com.dazkins.triad.game.entity.shell.EntityShell;
import com.dazkins.triad.game.world.IWorldAccess;
import com.dazkins.triad.game.world.tile.Tile;
import com.dazkins.triad.gfx.Camera;
import com.dazkins.triad.gfx.Color;
import com.dazkins.triad.gfx.OpenGLHelper;
import com.dazkins.triad.gfx.TTF;
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

	protected Model model;

	private boolean idAssigned;

	protected IWorldAccess world;

	public abstract void initModel();
	
	private String name = "";
	
	protected EntityShell shell;

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
	
	public void initEntityShell(EntityShell s)
	{
		this.shell = s;
	}
	
	public void setName(String s)
	{
		name = s;
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
			if (name != null && !name.equals(""))
				TTF.renderString(name, x - name.length() * 8, y + 64, Tile.yPosToDepthRelativeToCamera(cam, y) + 1.0f);
		} else
			initModel();
		
		float healthPercent = (float) shell.getHealth() / shell.getMaxHealth();
		
		GL11.glColor4f(0.0f, 1.0f, 0.0f, 1.0f);
		OpenGLHelper.immDrawQuad(x - 32.0f, y - 10, x + healthPercent * 64.0f - 32.0f, y - 2, Tile.yPosToDepthRelativeToCamera(cam, y - 20.0f) + 0.001f);
		GL11.glColor4f(1.0f, 0.0f, 0.0f, 1.0f);
		OpenGLHelper.immDrawQuad(x + healthPercent * 64.0f - 32.0f, y - 10, x + 32.0f, y - 2, Tile.yPosToDepthRelativeToCamera(cam, y - 20.0f) + 0.001f);
		GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
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