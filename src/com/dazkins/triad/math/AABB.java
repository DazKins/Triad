package com.dazkins.triad.math;

import org.lwjgl.opengl.GL11;

public class AABB
{
	private float x0, y0;
	private float x1, y1;

	public float getCX()
	{
		return (x1 + x0) / 2.0f;
	}

	public float getCY()
	{
		return (y1 + y0) / 2.0f;
	}

	public float getHalfDiagonal()
	{
		float x = x0 - getCX();
		float y = y0 - getCY();
		return (float) Math.sqrt(x * x + y * y);
	}

	public float getX0()
	{
		return x0;
	}

	public float getY0()
	{
		return y0;
	}

	public float getX1()
	{
		return x1;
	}

	public float getY1()
	{
		return y1;
	}

	public float getW()
	{
		return Math.abs(x1 - x0);
	}

	public float getH()
	{
		return Math.abs(y1 - y0);
	}

	public AABB shiftX0(float v)
	{
		return new AABB(x0 + v, y0, x1, y1);
	}

	public AABB shiftY0(float v)
	{
		return new AABB(x0, y0 + v, x1, y1);
	}

	public AABB shiftX1(float v)
	{
		return new AABB(x0, y0, x1 + v, y1);
	}

	public AABB shiftY1(float v)
	{
		return new AABB(x0, y0, x1, y1 + v);
	}
	
	public AABB expand(float v)
	{
		return new AABB(x0 - v, y0 - v, x1 + v, y1 + v);
	}

	public AABB(float x0, float y0, float x1, float y1)
	{
		this.x0 = x0;
		this.y0 = y0;
		this.x1 = x1;
		this.y1 = y1;
	}

	public void move(float xa, float ya)
	{
		x0 += xa;
		x1 += xa;
		y0 += ya;
		y1 += ya;
	}

	public AABB shifted(float xa, float ya)
	{
		return new AABB(x0 + xa, y0 + ya, x1 + xa, y1 + ya);
	}

	public boolean intersects(AABB a)
	{
		if (x1 < a.x0)
			return false;
		if (a.x1 < x0)
			return false;
		if (y1 < a.y0)
			return false;
		if (a.y1 < y0)
			return false;
		return true;
	}
	
	public boolean intersects(float x, float y)
	{
		if (x > x1)
			return false;
		if (x < x0)
			return false;
		if (y < y0)
			return false;
		if (y > y1)
			return false;
		return true;
	}

	public void renderBounds(float width)
	{
		GL11.glDisable(GL11.GL_TEXTURE_2D);

		GL11.glLineWidth(width);
		GL11.glColor4f(1.0f, 0.0f, 0.0f, 1.0f);
		GL11.glBegin(GL11.GL_LINES);
		GL11.glVertex3f(x0, y0, 5.0f);
		GL11.glVertex3f(x1, y0, 5.0f);

		GL11.glVertex3f(x1, y0, 5.0f);
		GL11.glVertex3f(x1, y1, 5.0f);

		GL11.glVertex3f(x1, y1, 5.0f);
		GL11.glVertex3f(x0, y1, 5.0f);

		GL11.glVertex3f(x0, y1, 5.0f);
		GL11.glVertex3f(x0, y0, 5.0f);
		GL11.glEnd();
		GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
		GL11.glLineWidth(1);

		GL11.glEnable(GL11.GL_TEXTURE_2D);
	}

	public String toString()
	{
		return x0 + " " + y0 + " " + x1 + " " + y1;
	}
}