package com.dazkins.triad.game.gui.renderformat;

public class BoxRenderFormat extends RenderFormat 
{
	private int renderStyle = 1;
	
	public int getRenderStyle()
	{
		return renderStyle;
	}
	
	public BoxRenderFormat setRenderStyle(int s)
	{
		renderStyle = s;
		return this;
	}
	
	public RenderFormat reset()
	{
		renderStyle = 1;
		return this;
	}
}