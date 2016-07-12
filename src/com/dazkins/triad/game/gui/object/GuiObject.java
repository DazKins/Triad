package com.dazkins.triad.game.gui.object;

import com.dazkins.triad.game.gui.Gui;
import com.dazkins.triad.math.AABB;

public abstract class GuiObject
{
	protected int layer;
	protected Gui gui;
	
	protected float x;
	protected float y;
	
	protected float width;
	protected float height;

	public GuiObject(Gui g)
	{
		this.gui = g;
		gui.addObject(this);
	}
	
	public void setLayer(int l)
	{
		layer = l;
	}
	
	public void setX(float x)
	{
		this.x = x;
	}
	
	public void setY(float y)
	{
		this.y = y;
	}

	public void setWidth(float w)
	{
		this.width = w;
	}
	
	public void setHeight(float h)
	{
		this.height = h;
	}

	public AABB getBounds()
	{
		return null;
	}

	public void tick()
	{
	}

	public abstract void setupGraphics();
}