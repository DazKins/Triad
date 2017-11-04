package com.dazkins.triad.gfx.model;

import java.util.ArrayList;

import org.lwjgl.opengl.GL11;

import com.dazkins.triad.gfx.BufferObject;
import com.dazkins.triad.gfx.Image;
import com.dazkins.triad.gfx.RenderContext;
import com.dazkins.triad.math.AABB;
import com.dazkins.triad.math.Matrix3;

public class Quad
{
	private BufferObject bufferObject;

	private float rot;
	private float cRotX, cRotY;

	private float offsetX, offsetY;
	
	private float scaleX, scaleY;

	private Image img;

	private float x, y;
	private float w, h;
	private int tx, ty;
	private int tw, th;

	private ArrayList<Quad> childQuads;
	private ArrayList<Quad> temporaryChildQuads;

	private Quad parentQuad;

	public Quad(float x, float y, float w, float h, int tx, int ty, int tw, int th)
	{
		this.x = x;
		this.y = y;
		this.w = w;
		this.h = h;
		this.tx = tx;
		this.ty = ty;
		this.tw = tw;
		this.th = th;
		
		scaleX = 1.0f;
		scaleY = 1.0f;

		childQuads = new ArrayList<Quad>();
		temporaryChildQuads = new ArrayList<Quad>();
	}

	public void init(Image i)
	{
		this.img = i;
	}

	public void addChildQuad(Quad q)
	{
		q.initParentQuad(this);
		childQuads.add(q);
	}

	public void addTemporaryChildQuad(Quad q)
	{
		q.initParentQuad(this);
		temporaryChildQuads.add(q);
	}

	public void addTemporaryChildQuads(Quad[] q)
	{
		for (int i = 0; i < q.length; i++)
		{
			q[i].initParentQuad(this);
			temporaryChildQuads.add(q[i]);
		}
	}

	private void initParentQuad(Quad q)
	{
		parentQuad = q;
	}

	public void generate()
	{
		bufferObject = new BufferObject(16);
		bufferObject.resetData();
		img.loadSpriteBufferObject(bufferObject, x, y, w, h, tx, ty, tw, th, 0.0f, 0.0f);
		bufferObject.compile();
	}

	public void setCenterOfRotation(float x, float y)
	{
		cRotX = x;
		cRotY = y;
	}

	public void setRotation(float f)
	{
		rot = f;
	}

	public float getRotation()
	{
		return rot;
	}

	public float getOffsetX()
	{
		return offsetX;
	}

	public float getOffsetY()
	{
		return offsetY;
	}

	public void setOffset(float x, float y)
	{
		offsetX = x;
		offsetY = y;
	}

	//TODO maybe work towards removing this now the new matrix system is in place
	private void attachTransformation(RenderContext rc)
	{
		if ((cRotX != 0 || cRotY != 0) && rot != 0)
			rc.getMatrixStack().transform(Matrix3.translate(cRotX, cRotY));
//			GL11.glTranslatef(cRotX, cRotY, 0);
		
		if (rot != 0)
			rc.getMatrixStack().transform(Matrix3.rotate(rot));
//			GL11.glRotatef(rot, 0, 0, 1);
		
		if ((cRotX != 0 || cRotY != 0) && rot != 0)
			rc.getMatrixStack().transform(Matrix3.translate(-cRotX, -cRotY));
//			GL11.glTranslatef(-cRotX, -cRotY, 0);

		//TODO more render layering to be cleared up
//		float z = renderLayer * 0.001f;
		
		if (scaleX != 1 || scaleY != 1)
			rc.getMatrixStack().transform(Matrix3.scale(scaleX, scaleY));
//			GL11.glScalef(scaleX, scaleY, 1.0f);
		
		if (offsetX != 0 || offsetY != 0)
			rc.getMatrixStack().transform(Matrix3.translate(offsetX, offsetY));
//			GL11.glTranslatef(offsetX, offsetY, z);
	}
	
	public void stretch(float nH, float nW)
	{
		scaleX = nH / h;
		scaleY = nW / w;
	}

	public void render(RenderContext rc)
	{
		temporaryChildQuads.sort(Model.rSort);
		childQuads.sort(Model.rSort);

//		GL11.glPushMatrix();
		rc.getMatrixStack().push();

		if (parentQuad != null)
		{
			parentQuad.attachTransformation(rc);
		}

		attachTransformation(rc);

//		bufferObject.render();
		rc.addToRender(bufferObject);

//		GL11.glPopMatrix();
		rc.getMatrixStack().pop();

		for (Quad q : childQuads)
		{
			q.render(rc);
		}
		for (int i = 0; i < temporaryChildQuads.size(); i++)
		{
			Quad q = temporaryChildQuads.get(i);
			if (q != null)
			{
				q.render(rc);
			}
		}

		temporaryChildQuads.clear();
	}

	public AABB getRenderAABB()
	{
		return new AABB(x, y, x + w, y + h);
	}
}