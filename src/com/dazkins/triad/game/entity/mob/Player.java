package com.dazkins.triad.game.entity.mob;

import java.awt.event.KeyEvent;

import com.dazkins.triad.game.world.World;
import com.dazkins.triad.gfx.Art;
import com.dazkins.triad.gfx.Camera;
import com.dazkins.triad.gfx.bitmap.Bitmap;
import com.dazkins.triad.input.InputHandler;
import com.dazkins.triad.math.AABB;

public class Player extends Mob {
	private InputHandler input;
	
	public Player(World w, float x, float y, InputHandler input) {
		super(w, x, y, "player", 20);
		this.input = input;
	}
	
	public int getMaxHealth() {
		return 20;
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
		Art.spriteSheet.renderSprite(((int) (lifeTicks / 10) % 2) + 3, 0, b, ((int) x - 8 - (int) cam.getX()), ((int) y - 16 - (int)cam.getY()));
	}

	public AABB getAABB() {
		return new AABB(x -8, y - 3, x + 8, y);
	}
}