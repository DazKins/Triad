package com.dazkins.triad.game.entity.mob;

import java.util.ArrayList;

import org.lwjgl.system.glfw.GLFW;

import com.dazkins.triad.game.entity.Activeatable;
import com.dazkins.triad.game.entity.Facing;
import com.dazkins.triad.game.inventory.Inventory;
import com.dazkins.triad.game.inventory.item.Item;
import com.dazkins.triad.game.world.World;
import com.dazkins.triad.gfx.Image;
import com.dazkins.triad.gfx.model.ModelHumanoid;
import com.dazkins.triad.gfx.model.animation.AnimationHumanoidIdle;
import com.dazkins.triad.gfx.model.animation.AnimationHumanoidSlashing;
import com.dazkins.triad.gfx.model.animation.AnimationHumanoidWalking;
import com.dazkins.triad.input.InputHandler;
import com.dazkins.triad.math.AABB;

public class EntityPlayer extends Mob {
	private InputHandler input;
	
	public EntityPlayer(World w, float x, float y, InputHandler input) {
		super(w, x, y, "player", 1000);
		this.input = input;
		this.inv = new Inventory(9, 5);
		inv.addItem(Item.testHelmet);
		inv.addItem(Item.testChest);
		inv.addItem(Item.testLegs);
		inv.addItem(Item.testSword);
		inv.addItem(Item.axe);
	}
	
	public int getMaxHealth() {
		return 1000;
	}

	public void tick() {
		super.tick();
		model.addAnimation(new AnimationHumanoidIdle(this), 0, false);
		
		float moveModifier = 1.0f;
		
		if (input.isKeyDown(GLFW.GLFW_KEY_LEFT_SHIFT)) {
			moveModifier = 10.0f;
		}
		
		if (input.isKeyDown(GLFW.GLFW_KEY_W)) {
			setFacing(Facing.UP);
			addYAMod(getMovementSpeed() * moveModifier);
			model.addAnimation(new AnimationHumanoidWalking(this), 1, false);
		} else if (input.isKeyDown(GLFW.GLFW_KEY_S)) {
			setFacing(Facing.DOWN);
			addYAMod(-getMovementSpeed() * moveModifier);
			model.addAnimation(new AnimationHumanoidWalking(this), 1, false);
		}
		if (input.isKeyDown(GLFW.GLFW_KEY_A)) {
			setFacing(Facing.LEFT);
			addXAMod(-getMovementSpeed() * moveModifier);
			model.addAnimation(new AnimationHumanoidWalking(this), 1, false);
		} else if (input.isKeyDown(GLFW.GLFW_KEY_D)) {
			setFacing(Facing.RIGHT);
			addXAMod(getMovementSpeed() * moveModifier);
			model.addAnimation(new AnimationHumanoidWalking(this), 1, false);
		}
		
		if (input.isKeyDown(GLFW.GLFW_KEY_SPACE)) {
			if (attemptAttack(getFacingAttackArea(getFacing()))) {
				model.addAnimation(new AnimationHumanoidSlashing(this, this.getAttackCooldown()), 5, true);
				tryHarvest();
			}
		}
		
		if (input.isKeyJustDown(GLFW.GLFW_KEY_Z)) {
			ArrayList<Activeatable> as = world.getActivatablesInAABB(getAABB());
			for (Activeatable a : as) {
				a.onActivate(this);
			}
		}
		
		move();

		model.updateAnimationState(this);
		
		xa *= 0.75;
		ya *= 0.75;
	}
	
	protected int getBaseDamage() {
		return 5;
	}
	
	protected int getBaseKnockback() {
		return 5;
	}
	
	protected int getBaseAttackCooldown() {
		return 40;
	}
	
	protected int getBaseAttackRange() {
		return 50;
	}
	
	protected void initModel() {
		model = new ModelHumanoid(Image.getImageFromName("player"));
	}

	public AABB getAABB() {
		return new AABB(x - 8, y, x + 8, y + 10);
	}

	public float getMovementSpeed() {
		return 1f;
	}
}