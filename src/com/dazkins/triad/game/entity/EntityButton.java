package com.dazkins.triad.game.entity;

import java.util.ArrayList;

import com.dazkins.triad.game.world.World;
import com.dazkins.triad.game.world.tile.Tile;
import com.dazkins.triad.gfx.Camera;
import com.dazkins.triad.gfx.OpenGLHelper;
import com.dazkins.triad.gfx.model.ModelButton;
import com.dazkins.triad.math.AABB;

public class EntityButton extends Entity implements Activeatable
{
	private ArrayList<Activeatable> triggers;

	public EntityButton(World w, float x, float y)
	{
		super(w, StorageEntityID.BUTTON, x, y, "button");
		triggers = new ArrayList<Activeatable>();
	}

	public EntityButton(World w, float x, float y, Activeatable a)
	{
		super(w, StorageEntityID.BUTTON, x, y, "button");
		triggers = new ArrayList<Activeatable>();
		triggers.add(a);
	}

	public EntityButton(World w, float x, float y, ArrayList<Activeatable> a)
	{
		super(w, StorageEntityID.BUTTON, x, y, "button");
		triggers = a;
	}

	public void tick()
	{

	}

	public AABB getAABB()
	{
		return new AABB(x - 16, y, x + 16, y + 32);
	}

	public void onActivate(Entity e)
	{
		System.out.println(this + " is triggered by " + e + "!");
		for (int i = 0; i < triggers.size(); i++)
		{
			triggers.get(i).onActivate(e);
		}
	}
}