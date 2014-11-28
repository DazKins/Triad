package com.dazkins.triad.game.entity.mob;

import org.lwjgl.system.glfw.GLFW;

import com.dazkins.triad.game.entity.EntityTorch;
import com.dazkins.triad.game.entity.Facing;
import com.dazkins.triad.game.inventory.Inventory;
import com.dazkins.triad.game.inventory.item.Item;
import com.dazkins.triad.game.inventory.item.equipable.weapon.ItemWeapon;
import com.dazkins.triad.game.world.World;
import com.dazkins.triad.gfx.Image;
import com.dazkins.triad.gfx.model.Model;
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
		this.inv = new Inventory(10, 10);
		inv.addItem(Item.testHelmet);
		inv.addItem(Item.testChest);
		inv.addItem(Item.testLegs);
		inv.addItem(Item.testSword);
	}
	
	public int getMaxHealth() {
		return 1000;
	}

	public void tick() {
		super.tick();
		model.addAnimation(new AnimationHumanoidIdle(this), 0, false);
		if (input.isKeyDown(GLFW.GLFW_KEY_W)) {
			addYAMod(getMovementSpeed());
			model.addAnimation(new AnimationHumanoidWalking(this), 1, false);
		}
		if (input.isKeyDown(GLFW.GLFW_KEY_A)) {
			addXAMod(-getMovementSpeed());
			model.addAnimation(new AnimationHumanoidWalking(this), 1, false);
		}
		if (input.isKeyDown(GLFW.GLFW_KEY_S)) {
			addYAMod(-getMovementSpeed());
			model.addAnimation(new AnimationHumanoidWalking(this), 1, false);
		}
		if (input.isKeyDown(GLFW.GLFW_KEY_D)) {
			addXAMod(getMovementSpeed());
			model.addAnimation(new AnimationHumanoidWalking(this), 1, false);
		}
		
		if (input.isKeyJustDown(GLFW.GLFW_KEY_Q))
			world.addEntity(new EntityTorch(world, x, y));
		
		if (input.isKeyDown(GLFW.GLFW_KEY_SPACE)) {
			ItemWeapon wep = eInv.getWeaponItem();
			if (attemptAttack(getFacingAttackArea(getFacing()))) {
				if (wep != null)
					model.addAnimation(new AnimationHumanoidSlashing(this, eInv.getWeaponItem().getAttackCooldown()), 5, true);
				else
					model.addAnimation(new AnimationHumanoidSlashing(this, getBaseAttackCooldown()), 5, true);
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
		return 0.5f;
	}
}