package com.dazkins.triad.game.entity.mob;

import org.lwjgl.input.Keyboard;

import com.dazkins.triad.game.world.World;
import com.dazkins.triad.gfx.model.ModelHumanoid;
import com.dazkins.triad.input.InputHandler;
import com.dazkins.triad.math.AABB;

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
			ya = getMovementSpeed();
		if (input.isKeyDown(Keyboard.KEY_A))
			xa = -getMovementSpeed();
		if (input.isKeyDown(Keyboard.KEY_S))
			ya = -getMovementSpeed();
		if (input.isKeyDown(Keyboard.KEY_D))
			xa = getMovementSpeed();
		
		xa *= 0.85;
		ya *= 0.85;
		
		move(xa, ya);
	}

	public AABB getAABB() {
		return new AABB(x - 8, y, x + 8, y + 48);
	}
	
	public void render() {
		((ModelHumanoid)this.getModel()).render(this);
//		OpenGLHelper.renderReferencePoint(x, y);
//		getAABB().renderBounds(2);
	}

	public float getMovementSpeed() {
		return 2;
	}
}