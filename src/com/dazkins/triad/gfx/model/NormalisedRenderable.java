package com.dazkins.triad.gfx.model;

import org.lwjgl.opengl.GL11;

import com.dazkins.triad.gfx.BufferObject;

//Used to store a renderable with an already predefined buffer object
public class NormalisedRenderable
{
	private BufferObject bo;

	private float offsetX, offsetY;

	private float renderLayer;

	private float cRotX, cRotY;
	private float rot;

	public float getOffsetX()
	{

		return offsetX;
	}

	public void setOffsetX(float offsetX)
	{
		this.offsetX = offsetX;
	}

	public float getOffsetY()
	{
		return offsetY;
	}

	public void setOffsetY(float offsetY)
	{
		this.offsetY = offsetY;
	}

	public float getRenderLayer()
	{
		return renderLayer;
	}

	public void setRenderLayer(float renderLayer)
	{
		this.renderLayer = renderLayer;
	}

	public float getcRotX()
	{
		return cRotX;
	}

	public void setcRotX(float cRotX)
	{
		this.cRotX = cRotX;
	}

	public float getcRotY()
	{
		return cRotY;
	}

	public void setcRotY(float cRotY)
	{
		this.cRotY = cRotY;
	}

	public float getRot()
	{
		return rot;
	}

	public void setRot(float rot)
	{
		this.rot = rot;
	}

	public NormalisedRenderable(BufferObject b)
	{
		bo = b;
	}

	public void render()
	{
		GL11.glPushMatrix();

		if ((cRotX != 0 || cRotY != 0) && rot != 0)
		{
			GL11.glTranslatef(cRotX, cRotY, 0);
			GL11.glRotatef(rot, 0, 0, 1);
			GL11.glTranslatef(-cRotX, -cRotY, 0);
		}

		GL11.glTranslatef(offsetX, offsetY, renderLayer * 0.001f);

		bo.render();

		GL11.glPopMatrix();
	}
}