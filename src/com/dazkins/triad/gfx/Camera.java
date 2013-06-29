package com.dazkins.triad.gfx;

import java.awt.event.KeyEvent;

import com.dazkins.triad.game.entity.Entity;
import com.dazkins.triad.input.InputHandler;

public class Camera {
	private InputHandler input;
	private float x, y;
	private float w, h;
	
	private float minX, minY;
	private float maxX, maxY;
	
	public Camera(InputHandler input,float x, float y, float w, float h) {
		this.x = x;
		this.y = y;
		this.input = input;
		this.w = w;
		this.h = h;
	}
	
	public void setBounds(float minX, float minY, float maxX, float maxY) {
		this.maxX = maxX;
		this.maxY = maxY;
		this.minX = minX;
		this.minY = minY;
	}
	
	public void lockCameraToEntity(Entity e) {
		x = e.getX() - w / 2;
		y = e.getY() - h / 2;
		
		if (x < minX) 
			x = minX;
		if (y < minY)
			y = minY;
		if (x + w > maxX)
			x = maxX - w;
		if (y + h > maxY)
			y = maxY - h;
	}

	public float getX() {
		return x;
	}

	public void setX(float x) {
		this.x = x;
	}

	public float getY() {
		return y;
	}

	public void setY(float y) {
		this.y = y;
	}
}