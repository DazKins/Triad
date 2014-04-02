package com.dazkins.triad.game.entity.mob;

import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;

import com.dazkins.triad.game.entity.Facing;
import com.dazkins.triad.game.inventory.EquipmentInventory;
import com.dazkins.triad.game.inventory.Inventory;
import com.dazkins.triad.game.inventory.item.ItemRegisterer;
import com.dazkins.triad.game.inventory.item.ItemStack;
import com.dazkins.triad.game.world.World;
import com.dazkins.triad.gfx.Font;
import com.dazkins.triad.gfx.model.ModelHumanoid;
import com.dazkins.triad.input.InputHandler;
import com.dazkins.triad.math.AABB;

public class EntityPlayer extends Mob {
	private InputHandler input;
	
	private int attackCooldown = 30;
	private int attackCooldownCounter;
	
	public EntityPlayer(World w, float x, float y, InputHandler input) {
		
		super(w, x, y, "player", 1000);
		this.input = input;
		this.inv = new Inventory(10, 10);
//		for (int i = 0; i < 100; i++)
			inv.addItemStack(new ItemStack(ItemRegisterer.getItemByName("testsword"), 1));
			inv.addItemStack(new ItemStack(ItemRegisterer.getItemByName("testdagger"), 1));
		eInv.addItemStack(new ItemStack(ItemRegisterer.getItemByName("testsword"), 1), EquipmentInventory.Type.HEAD.ordinal());
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
		
		if(attackCooldownCounter > 0) {
			attackCooldownCounter--;
		}
		
		if (input.isKeyDown(Keyboard.KEY_SPACE) && attackCooldownCounter == 0) {
			int xx = (int) x >> 5;
			int yy = (int) y >> 5;
			if (this.getFacing() == Facing.UP)
				world.sendAttackCommand(10, xx, yy + 1);
			if (this.getFacing() == Facing.DOWN)
				world.sendAttackCommand(10, xx, yy - 1);
			if (this.getFacing() == Facing.LEFT)
				world.sendAttackCommand(10, xx - 1, yy);
			if (this.getFacing() == Facing.RIGHT)
				world.sendAttackCommand(10, xx + 1, yy);
			attackCooldownCounter = attackCooldown;
		}
		
		xa *= 0.85;
		ya *= 0.85;
		
		move(xa, ya);
	}

	public AABB getAABB() {
		return new AABB(x - 8, y, x + 8, y + 48);
	}
	
	public void render() {
		GL11.glColor3f(1.0f, 1.0f, 1.0f);
		super.render(false);
		((ModelHumanoid)this.getModel()).updateAnimationState(this);
		((ModelHumanoid)this.getModel()).render(this);
	}

	public float getMovementSpeed() {
		return 2;
	}

	public void renderToPlayerGui() {
//		Font.drawStringWithShadow("X: " + ((int) x >> 5) + " Y: " + ((int) y >> 5), x - (name.length() * 16) / 2, y + 52);
	}
}