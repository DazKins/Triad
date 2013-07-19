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

	public Quad(float w, float h, int tx, int ty, int tw, int th, float alpha, float z) {
		model = new BufferObject(655360);
		model.start();
		Image.spriteSheet.renderSprite(model, tx, ty, tw, th, 0, 0, w, h, alpha, z);
		model.stop();
	}
	
	public float getRot() {
		return rot;
	}

	public void setCenterOfRotation(float x, float y) {
		cRotX = x;
		cRotY = y;
	}

	public void setDepth(float z) {
		depth = z;
	}

	public void setRotation(float f) {
		rot = f;
	}

	public void setOffset(float x, float y) {
		offsetX = x;
		offsetY = y;
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