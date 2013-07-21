package com.dazkins.triad.game.entity.mob;

import org.lwjgl.input.Keyboard;

import com.dazkins.triad.game.world.World;
import com.dazkins.triad.input.InputHandler;
import com.dazkins.triad.math.AABB;
import com.dazkins.triad.gfx.model.ModelPlayer;;

public class EntityPlayer extends Mob {
	private InputHandler input;
	
	public EntityPlayer(World w, float x, float y, InputHandler input) {
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
		
		System.out.println(this.getFacing());
	}

	public AABB getAABB() {
		return new AABB(x, y, x + 32, y + 32);
	}
	
	public void render() {
		((ModelPlayer)this.getModel()).render(this);
	}
}