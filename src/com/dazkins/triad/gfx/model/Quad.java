package com.dazkins.triad.gfx.model;

import org.lwjgl.opengl.GL11;

import com.dazkins.triad.gfx.BufferObject;
import com.dazkins.triad.gfx.Image;

public class Quad {
	private BufferObject model;

	private float rot;
	private float cRotX, cRotY;

	private float offsetX, offsetY;

	private float depth;
	
	private Image img;
	
	private float x, y;
	private float w, h;
	private int tx, ty;
	private int tw, th;

	public Quad(float x, float y, float w, float h, int tx, int ty, int tw, int th) {
		this.x = x;
		this.y = y;
		this.w = w;
		this.h = h;
		this.tx = tx;
		this.ty = ty;
		this.tw = tw;
		this.th = th;
	}
	
	public void init(Image i) {
		img = i;
	}
	
	public void generate() {
		model = new BufferObject(20);
		model.start();
		img.renderSprite(model, x, y, w, h, tx, ty, tw, th, 0.0f);
		model.stop();
	}

	public void setCenterOfRotation(float x, float y) {
		cRotX = x;
		cRotY = y;
	}

	public void setDepth(float z) {
		depth = z;
	}

	public void addRotation(float f) {
		rot += f;
	}
	
	public float getRotation() {
		return rot;
	}
	
	public void addOffset(float x, float y) {
		offsetX += x;
		offsetY += y;
	}
	
	public void resetProperties() {
		offsetX = 0;
		offsetY = 0;
		rot = 0;
	}

	public void render() {
		GL11.glPushMatrix();
		
		if (cRotX != 0 || cRotY != 0)
			GL11.glTranslatef(cRotX, cRotY, 0);

		GL11.glRotatef(rot, 0, 0, 1);

		if (cRotX != 0 || cRotY != 0)
			GL11.glTranslatef(-cRotX, -cRotY, 0);
		
		GL11.glTranslatef(offsetX, offsetY, depth);

		model.render();
		GL11.glPopMatrix();
	}
}