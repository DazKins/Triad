package com.dazkins.triad.game.gui.renderformat;

import com.dazkins.triad.gfx.Color;
import com.dazkins.triad.gfx.TTF;

public class TextRenderFormat extends RenderFormat
{
	private TTF font = TTF.main;
	private Color c = new Color(0);
	private float a = 1.0f;
	private boolean shadow = false;
	
	public TextRenderFormat setA(float a)
	{
		this.a = a;
		return this;
	}
	
	public TextRenderFormat setColour(Color c)
	{
		this.c = c;
		return this;
	}
	
	public TextRenderFormat setShadow(boolean s)
	{
		shadow = s;
		return this;
		
	}
	
	public TextRenderFormat setFont(TTF f)
	{
		this.font = f;
		return this;
	}

	public Color getColor()
	{
		return c;
	}

	public float getA()
	{
		return a;
	}
	
	public boolean hasShadow()
	{
		return shadow;
	}
	
	public TTF getFont()
	{
		return font;
	}

	public TextRenderFormat reset()
	{
		c = new Color(0);
		a = 1.0f;
		shadow = false;
		font = TTF.main;
		return this;
	}
}