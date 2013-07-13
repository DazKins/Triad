package com.dazkins.triad.game.entity.mob;

import org.lwjgl.input.Keyboard;

import com.dazkins.triad.game.world.World;
import com.dazkins.triad.gfx.Image;
import com.dazkins.triad.gfx.Camera;
import com.dazkins.triad.gfx.Font;
import com.dazkins.triad.gfx.GLRenderer;
import com.dazkins.triad.input.InputHandler;
import com.dazkins.triad.math.AABB;

public class Player extends Mob {
	private InputHandler input;
	
	public Player(World w, float x, float y, InputHandler input) {
		super(w, x, y, "player", 1000);
		this.input = input;
	}
	
	public int getMaxHealth() {
		return 1000;
	}

	public void tick() {
		super.tick();
		
		if (input.isKeyDown(Keyboard.KEY_W))
			ya = 2;
		if (input.isKeyDown(Keyboard.KEY_A))
			xa = -2;
		if (input.isKeyDown(Keyboard.KEY_S))
			ya = -2;
		if (input.isKeyDown(Keyboard.KEY_D))
			xa = 2;
		
		xa *= 0.85;
		ya *= 0.85;
		
		move(xa, ya);
	}
	
	public void render(GLRenderer g, Camera cam) {
		Image.spriteSheet.renderSprite(g, (((int) (lifeTicks / 10) % 2) + 3) * 16, 0, 16, 16, (int) (x - 16 - cam.getX()), (int) (y - 32 - cam.getY()), 32, 32);
		Font.drawString(g, name, (int) (x - ((name.length() * 8) / 2) - cam.getX()), (int) (y - cam.getY()));
	}

	public AABB getAABB() {
		return new AABB(x - 8, y - 32, x + 8, y - 26);
	}
}