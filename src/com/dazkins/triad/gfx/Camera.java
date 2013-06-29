package com.dazkins.triad.gfx;

import java.awt.event.KeyEvent;

import com.dazkins.triad.input.InputHandler;

public class Camera {
	private float x, y;
	private InputHandler input;
	
	public Camera(InputHandler input, float x, float y) {
		this.x = x;
		this.y = y;
		this.input = input;
	}
	
	public void tick() {
		float xa = 0, ya = 0;
		
		if (input.isKeyDown(KeyEvent.VK_W))
			ya -= 1;
		if (input.isKeyDown(KeyEvent.VK_A))
			xa -= 1;
		if (input.isKeyDown(KeyEvent.VK_S))
			ya += 1;
		if (input.isKeyDown(KeyEvent.VK_D))
			xa += 1;
		
		x += xa;
		y += ya;
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