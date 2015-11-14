package com.dazkins.triad.game.gui.object;

import com.dazkins.triad.game.gui.Gui;
import com.dazkins.triad.math.AABB;

public class GuiObject {
	protected int layer;
	protected Gui gui;
	
	public GuiObject(Gui g, int l)
	{
		this.gui = g;
		layer = l;
		gui.addObject(this);
	}
	
	public AABB getBounds()
	{
		return null;
	}
	
	public void tick() { }
}