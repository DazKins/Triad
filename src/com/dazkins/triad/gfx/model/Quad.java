package com.dazkins.triad.gfx.model;

import java.util.ArrayList;

import org.lwjgl.opengl.GL11;

import com.dazkins.triad.gfx.BufferObject;
import com.dazkins.triad.gfx.Image;
import com.dazkins.triad.math.AABB;

public class Quad {
	private BufferObject bufferObject;

	private float rot;
	private float cRotX, cRotY;

	private float offsetX, offsetY;
	
	private Image img;
	
	private float x, y;
	private float w, h;
	private int tx, ty;
	private int tw, th;
	
	private float renderLayer;
	
	private ArrayList<Quad> childQuads;
	private ArrayList<Quad> temporaryChildQuads;
	
	private Quad parentQuad;

	public Quad(float x, float y, float w, float h, int tx, int ty, int tw, int th) {
		this.x = x;
		this.y = y;
		this.w = w;
		this.h = h;
		this.tx = tx;
		this.ty = ty;
		this.tw = tw;
		this.th = th;
		
		childQuads = new ArrayList<Quad>();
		temporaryChildQuads = new ArrayList<Quad>();
	}
	
	public void init(Image i) {
		this.img = i;
	}
	
	public void addChildQuad(Quad q) {
		q.initParentQuad(this);
		childQuads.add(q);
	}
	
	public void addTemporaryChildQuad(Quad q) {
		q.initParentQuad(this);
		temporaryChildQuads.add(q);
	}
	
	public void addTemporaryChildQuads(Quad[] q) {
		for (int i = 0; i < q.length; i++) {
			q[i].initParentQuad(this);
			temporaryChildQuads.add(q[i]);
		}
	}
	
	private void initParentQuad(Quad q) {
		parentQuad = q;
	}
	
	public void generate() {
		bufferObject = new BufferObject(36);
		bufferObject.start();
		img.renderSprite(bufferObject, 0, 0, w, h, tx, ty, tw, th, 0.0f, 0.0f);
		bufferObject.stop();
	}

	public void setCenterOfRotation(float x, float y) {
		cRotX = x;
		cRotY = y;
	}

	public void setRotation(float f) {
		rot = f;
	}
	
	public float getRotation() {
		return rot;
	}
	
	public float getOffsetX() {
		return offsetX;
	}
	
	public float getOffsetY() {
		return offsetY;
	}
	
	public void setOffset(float x, float y) {
		offsetX = x;
		offsetY = y;
	}
	
	public void setRenderLayer(float l) {
		renderLayer = l;
	}
	
	public float getRenderLayer() {
		return renderLayer;
	}
	
	private void attachTransformation() {
		if ((cRotX != 0 || cRotY != 0) && rot != 0) {
			GL11.glTranslatef(cRotX, cRotY, 0);
			GL11.glRotatef(rot, 0, 0, 1);
			GL11.glTranslatef(-cRotX, -cRotY, 0);
		}
		
		GL11.glTranslatef(x + offsetX,  y + offsetY, renderLayer * 0.002f);
	}

	public void render() {
		temporaryChildQuads.sort(Model.rSort);
		childQuads.sort(Model.rSort);
		
		GL11.glPushMatrix();
		
		if (parentQuad != null) {
			parentQuad.attachTransformation();
		}
		
		attachTransformation();

		bufferObject.render();
		
		GL11.glPopMatrix();
		
		for (Quad q : childQuads) {
			q.render();
		}
		for (int i = 0; i < temporaryChildQuads.size(); i++) {
			Quad q = temporaryChildQuads.get(i);
			if (q != null) {
				q.render();
			}
		}
		
		temporaryChildQuads.clear();
	}
	
	public AABB getRenderAABB() {
		return new AABB(x, y, x + w, y + h);
	}
}