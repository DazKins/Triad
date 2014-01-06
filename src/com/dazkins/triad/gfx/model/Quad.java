package com.dazkins.triad.gfx.model;

import org.lwjgl.opengl.GL11;

import com.dazkins.triad.gfx.BufferObject;
import com.dazkins.triad.gfx.Image;

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
	
	private int renderLayer;

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
		this.img = i;
	}
	
	public void generate() {
		bufferObject = new BufferObject(32);
		bufferObject.start();
		img.renderSprite(bufferObject, x, y, w, h, tx, ty, tw, th, 0.0f, 1.0f);
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
	
	public void setRenderLayer(int l) {
		renderLayer = l;
	}
	
	public int getRenderLayer() {
		return renderLayer;
	}

	public void render() {
		GL11.glPushMatrix();
		
		if ((cRotX != 0 || cRotY != 0) && rot != 0) {
			GL11.glTranslatef(cRotX, cRotY, 0);
			GL11.glRotatef(rot, 0, 0, 1);
			GL11.glTranslatef(-cRotX, -cRotY, 0);
		}
		
		GL11.glTranslatef(offsetX, offsetY, renderLayer * 0.001f);

		bufferObject.render();
		GL11.glPopMatrix();
	}
}