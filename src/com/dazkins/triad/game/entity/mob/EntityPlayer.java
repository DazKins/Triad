package com.dazkins.triad.game.entity.mob;

import org.lwjgl.system.glfw.GLFW;

import com.dazkins.triad.game.entity.EntityTorch;
import com.dazkins.triad.game.entity.Facing;
import com.dazkins.triad.game.inventory.Inventory;
import com.dazkins.triad.game.inventory.item.Item;
import com.dazkins.triad.game.inventory.item.equipable.weapon.ItemWeapon;
import com.dazkins.triad.game.world.World;
import com.dazkins.triad.gfx.model.Model;
import com.dazkins.triad.gfx.model.animation.AnimationHumanoidIdle;
import com.dazkins.triad.gfx.model.animation.AnimationHumanoidSlashing;
import com.dazkins.triad.gfx.model.animation.AnimationHumanoidWalking;
import com.dazkins.triad.input.InputHandler;
import com.dazkins.triad.math.AABB;

public class EntityPlayer extends Mob {
	private InputHandler input;
	
	private int attackCooldown = 30;
	private int attackCooldownCounter;
	private int attackWindow;
	
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
		if (input.isKeyDown(GLFW.GLFW_KEY_W)) {
			ya = getMovementSpeed();
			getModel().addAnimation(new AnimationHumanoidWalking(this), 1);
		}
		if (input.isKeyDown(GLFW.GLFW_KEY_A)) {
			xa = -getMovementSpeed();
			getModel().addAnimation(new AnimationHumanoidWalking(this), 1);
		}
		if (input.isKeyDown(GLFW.GLFW_KEY_S)) {
			ya = -getMovementSpeed();
			getModel().addAnimation(new AnimationHumanoidWalking(this), 1);
		}
		if (input.isKeyDown(GLFW.GLFW_KEY_D)) {
			xa = getMovementSpeed();
			getModel().addAnimation(new AnimationHumanoidWalking(this), 1);
		}
		getModel().addAnimation(new AnimationHumanoidIdle(this), 0);
		
		
		if(attackCooldownCounter > 0) {
			attackCooldownCounter--;
		}
		
		if (input.isKeyJustDown(GLFW.GLFW_KEY_Q))
			world.addEntity(new EntityTorch(world, x, y));
		
		if (input.isKeyDown(GLFW.GLFW_KEY_SPACE) && attackCooldownCounter == 0) {
			int xx = (int) x >> 5;
			int yy = (int) y >> 5;
			ItemWeapon weapon = eInv.getWeaponItem();
			int damage = 0;
			if (weapon != null)
				damage = weapon.getDamage();
			else
				damage = 5;

			if (this.getFacing() == Facing.UP) {
				AABB hit = new AABB(x - 22, y + 20, x + 22, y + 70);
				world.sendAttackCommand(damage, hit, this);
			}
			if (this.getFacing() == Facing.DOWN) {
				AABB hit = new AABB(x - 22, y - 30, x + 22, y + 20);
				world.sendAttackCommand(damage, hit, this);
			}
			if (this.getFacing() == Facing.LEFT) {
				AABB hit = new AABB(x - 40, y + 10, x, y + 50);
				world.sendAttackCommand(damage, hit, this);
			}
			if (this.getFacing() == Facing.RIGHT) {
				AABB hit = new AABB(x, y + 10, x + 40, y + 50);
				world.sendAttackCommand(damage, hit, this);
			}
			attackCooldownCounter = attackCooldown;

			getModel().addAnimation(new AnimationHumanoidSlashing(this), 5);
		}
		
		xa *= 0.75;
		ya *= 0.75;
		
		move(xa, ya);

		Model m = getModel();
		m.updateAnimationState(this);
		
//		if (!getModel().hasAnimation())
	}

	public AABB getAABB() {
		return new AABB(x - 8, y, x + 8, y + 10);
	}

	public float getMovementSpeed() {
		return 2;
	}
}