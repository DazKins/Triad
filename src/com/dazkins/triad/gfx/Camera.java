package com.dazkins.triad.gfx;

import org.lwjgl.opengl.GL11;

import com.dazkins.triad.game.entity.Entity;
import com.dazkins.triad.input.InputHandler;
import com.dazkins.triad.math.AABB;

public class Camera {
	private WindowInfo winInfo;
	private float x, y;
	
	private float minX, minY;
	private float maxX, maxY;
	
	private float zoom = 1.0f;
	
	private InputHandler input;
	
	private float minZoom, maxZoom;
	
	public Camera(InputHandler i, WindowInfo w, int x, int y) {
		this.winInfo = w;
		this.input = i;
		this.x = x;
		this.y = y;
	}
	
	public void attachTranslation() {
		GL11.glTranslatef(-x, -y, 0);
		GL11.glScalef(zoom, zoom, 1.0f);
	}
	
	public AABB getViewportBounds() {
		return new AABB(getX(), getY(), getX() + getW(), getY() + getH());
	}

	public void setBounds(float minX, float minY, float maxX, float maxY) {
		this.maxX = maxX;
		this.maxY = maxY;
		this.minX = minX;
		this.minY = minY;
	}
	
	public void moveWithKeys(float speed, int upKey, int downKey, int rightKey, int leftKey) {
		float xa = 0;
		float ya = 0;
		
		if (input.isKeyDown(upKey)) {
			ya += speed;
		}
		if (input.isKeyDown(downKey)) {
			ya -= speed;
		}
		if (input.isKeyDown(rightKey)) {
			xa += speed;
		}if (input.isKeyDown(leftKey)) {
			xa -= speed;
		}
		
		x += xa;
		y += ya;
	}
	
	public void lockCameraToEntity(Entity e) {
		x = (e.getX() - getW() / 2.0f) * zoom;
		y = (e.getY() - getH() / 2.0f) * zoom;
		
		if (getX() + getW() > maxX)
			x = (maxX - getW()) * zoom;
		if (getY() + getH() > maxY)
			y = (maxY - getH()) * zoom;
		if (getX() < minX)
			x = (minX) * zoom;
		if (getY() < minY)
			y = minY * zoom;
	}
	
	public void tick() {
		if(input.mWheel != 0) {
			float zoa = (input.mWheel / 2400.0f) / 2.0f;
			boolean canChange = ((zoa < 0) && zoom != minZoom) || ((zoa > 0) && zoom != maxZoom);
			if (canChange) {
				zoom += zoa;
			}
		}
		
		if (zoom < minZoom)
			zoom = minZoom;
		if (zoom > maxZoom)
			zoom = maxZoom;
	}
	
	public void lockZoom(float min, float max) {
		this.minZoom = min;
		this.maxZoom = max;
	}

	public float getX() {
		return x / zoom;
	}

	public void setX(float x) {
		this.x = x;
	}

	public float getY() {
		return y / zoom;
	}

	public void setY(float y) {
		this.y = y;
	}
	
	public float getW() {
		return winInfo.getW() / zoom;
	}

	public float getH() {
		return winInfo.getH() / zoom;
	}
	
	public float getZoom() {
		return zoom;
	}
}