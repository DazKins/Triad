package com.dazkins.triad.game.gui.renderformat;

public class BoxRenderFormat extends RenderFormat 
{
	private boolean simple = true;
	
	public boolean isSimple()
	{
		return simple;
	}
	
	public BoxRenderFormat setSimple(boolean s)
	{
		simple = s;
		return this;
	}
	
	public RenderFormat reset()
	{
		simple = true;
		return this;
	}
}