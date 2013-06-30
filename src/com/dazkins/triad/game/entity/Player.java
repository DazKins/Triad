package com.dazkins.triad.game.entity;

import java.awt.event.KeyEvent;

import com.dazkins.triad.gfx.Art;
import com.dazkins.triad.gfx.bitmap.Bitmap;
import com.dazkins.triad.gfx.Camera;
import com.dazkins.triad.input.InputHandler;

public class Player extends Entity {
	private InputHandler input;
	private int health;
	
	public Player(float x, float y, InputHandler input) {
		super(x, y);
		this.input = input;
		health = 20;
	}
	
	public int getMaxHealth() {
		return 200;
	}
	
	public int getHealth() {
		return (int) ((lifeTicks / 3) % getMaxHealth());
	}

	public void tick() {
		super.tick();
		
		float xa = 0, ya = 0;
		
		if (input.isKeyDown(KeyEvent.VK_W))
			ya -= 1;
		if (input.isKeyDown(KeyEvent.VK_A))
			xa -= 1;
		if (input.isKeyDown(KeyEvent.VK_S))
			ya += 1;
		if (input.isKeyDown(KeyEvent.VK_D))
			xa += 1;
		
		move(xa, ya);
	}
	
	public void render(Bitmap b, Camera cam) {
		Art.spriteSheet.renderSprite(((int) (lifeTicks / 10) % 3) + 2, 0, b, ((int) x - 8 - (int) cam.getX()), ((int) y - 16 - (int)cam.getY()));
	}
}