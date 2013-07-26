package com.dazkins.triad.gfx;

import org.lwjgl.opengl.GL11;

import com.dazkins.triad.Triad;
import com.dazkins.triad.game.entity.Entity;
import com.dazkins.triad.input.InputHandler;
import com.dazkins.triad.math.AABB;

public class Camera {
	private Triad triad;
	private InputHandler input;
	private float x, y;
	
	private float minX, minY;
	private float maxX, maxY;
	
	public Camera(InputHandler i, Triad t, int x, int y) {
		this.triad = t;
		this.input = input;
		this.x = x;
		this.y = y;
	}
	
	public void attachTranslation() {
		GL11.glTranslatef(-x, -y, 0);
	}
	
	public AABB getViewportBounds() {
		return new AABB(x, y, x + triad.WIDTH, y + triad.HEIGHT);
	}

	public void setBounds(float minX, float minY, float maxX, float maxY) {
		this.maxX = maxX;
		this.maxY = maxY;
		this.minX = minX;
		this.minY = minY;
	}
	
	public void lockCameraToEntity(Entity e) {
		if (!triad.wasRescaled()) { 
			if (x != e.getX() - triad.WIDTH / 2 || y != e.getY() - triad.HEIGHT / 2) {
				float xa, ya;
				float opp = e.getX() - (x + triad.WIDTH / 2);
				float adj = e.getY() - (y + triad.HEIGHT / 2);
				float len = (float) Math.sqrt(opp * opp + adj * adj);
				xa = (int) opp / len;
				ya = (int) adj / len;
				
				x += xa * len * 0.05f;
				y += ya * len * 0.05f;
			}  
		} else {
			x = e.getX() - triad.WIDTH / 2;
			y = e.getY() - triad.HEIGHT / 2;
		}
		
		if (x < minX)
			x = minX;
		if (y < minY)
			y = minY;
		if (x + triad.WIDTH > maxX)
			x = maxX - triad.WIDTH;
		if (y + triad.HEIGHT > maxY)
			y = maxY - triad.HEIGHT;
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
	
	public float getW() {
		return triad.WIDTH;
	}

	public float getH() {
		return triad.HEIGHT;
	}
}