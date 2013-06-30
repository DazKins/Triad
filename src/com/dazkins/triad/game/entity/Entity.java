package com.dazkins.triad.game.entity;

import com.dazkins.triad.gfx.bitmap.Bitmap;
import com.dazkins.triad.gfx.Camera;

public abstract class Entity {
	protected float x, y;
	protected long lifeTicks;
	
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

	public Entity(float x, float y) {
		this.x = x;
		this.y = y;
	}
	
	public void move(float xa, float ya) {
		x += xa;
		y += ya;
	}
	
	public void tick() {
		lifeTicks++;
	}
	
	public abstract void render(Bitmap b, Camera cam);
}