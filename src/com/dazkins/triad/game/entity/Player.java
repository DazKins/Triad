package com.dazkins.triad.game.entity;

import java.awt.event.KeyEvent;

import com.dazkins.triad.gfx.Art;
import com.dazkins.triad.gfx.Bitmap;
import com.dazkins.triad.input.InputHandler;

public class Player extends Entity {
	private InputHandler input;
	
	public Player(float x, float y, InputHandler input) {
		super(x, y);
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
		
		move(xa, ya);
	}
	
	public void render(Bitmap b) {
		Art.mainSpriteSheet.renderSprite(3, 0, b, (int) x - 8, (int) y - 16);
	}
}