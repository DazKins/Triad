package com.dazkins.triad.game.entity.mob;

import java.util.ArrayList;

import org.lwjgl.system.glfw.GLFW;

import com.dazkins.triad.game.entity.Activeatable;
import com.dazkins.triad.game.entity.Entity;
import com.dazkins.triad.game.entity.EntityIDStorage;
import com.dazkins.triad.game.entity.Facing;
import com.dazkins.triad.game.entity.Interactable;
import com.dazkins.triad.game.inventory.Inventory;
import com.dazkins.triad.game.inventory.item.Item;
import com.dazkins.triad.game.world.World;
import com.dazkins.triad.gfx.Camera;
import com.dazkins.triad.gfx.Image;
import com.dazkins.triad.gfx.model.ModelHumanoid;
import com.dazkins.triad.gfx.model.animation.AnimationHumanoidIdle;
import com.dazkins.triad.gfx.model.animation.AnimationHumanoidSlashing;
import com.dazkins.triad.gfx.model.animation.AnimationHumanoidWalking;
import com.dazkins.triad.input.InputHandler;
import com.dazkins.triad.math.AABB;

public class EntityPlayerClient extends Mob
{
	private InputHandler input;

	private Interactable interactingObject;

	private String name;

	public EntityPlayerClient(String n, float x, float y, InputHandler input)
	{
		super(null, EntityIDStorage.PLAYER, x, y, "player", 1000);
		this.input = input;
		this.inv = new Inventory(9, 5);
		inv.addItem(Item.testHelmet);
		inv.addItem(Item.testChest);
		inv.addItem(Item.testLegs);
		inv.addItem(Item.testSword);
		inv.addItem(Item.axe);

		name = n;
	}

	public String getName()
	{
		return name;
	}

	public int getMaxHealth()
	{
		return 1000;
	}

	public Interactable getInteractingObject()
	{
		return interactingObject;
	}

	public void setInteractingObject(Interactable i)
	{
		interactingObject = i;
	}

	public void tick()
	{
		super.tick();

		float moveModifier = 1.0f;

		if (input != null)
		{
			if (input.isKeyDown(GLFW.GLFW_KEY_LEFT_SHIFT))
			{
				moveModifier = 10.0f;
			}

			if (input.isKeyDown(GLFW.GLFW_KEY_W))
			{
				setFacing(Facing.UP);
				addYAMod(getMovementSpeed() * moveModifier);
			} else if (input.isKeyDown(GLFW.GLFW_KEY_S))
			{
				setFacing(Facing.DOWN);
				addYAMod(-getMovementSpeed() * moveModifier);
			}
			if (input.isKeyDown(GLFW.GLFW_KEY_A))
			{
				setFacing(Facing.LEFT);
				addXAMod(-getMovementSpeed() * moveModifier);
			} else if (input.isKeyDown(GLFW.GLFW_KEY_D))
			{
				setFacing(Facing.RIGHT);
				addXAMod(getMovementSpeed() * moveModifier);
			}

			if (input.isKeyDown(GLFW.GLFW_KEY_SPACE))
			{
				if (attemptAttack(getFacingAttackArea(getFacing())))
				{
					tryHarvest();
				}
			}
		}

		// TODO Reimplement
		// if (input.isKeyJustDown(GLFW.GLFW_KEY_Q)) {
		// ArrayList<Interactable> is =
		// world.getInteractablesInAABB(getInteractAreas()[getFacing()]);
		// for (Interactable i : is) {
		// i.onInteract(this);
		// }
		// }
		//
		// if (input.isKeyJustDown(GLFW.GLFW_KEY_E)) {
		// ArrayList<Activeatable> as = world.getActivatablesInAABB(getAABB());
		// for (Activeatable a : as) {
		// a.onActivate(this);
		// }
		// }

		move();

		xa *= 0.75f;
		ya *= 0.75f;
	}

	public boolean mayPass(Entity e)
	{
		return true;
	}

	protected int getBaseDamage()
	{
		return 5;
	}

	protected int getBaseKnockback()
	{
		return 5;
	}

	protected int getBaseAttackCooldown()
	{
		return 40;
	}

	protected int getBaseAttackRange()
	{
		return 50;
	}

	public AABB getAABB()
	{
		return new AABB(x - 8, y, x + 8, y + 10);
	}

	public float getMovementSpeed()
	{
		return 1f;
	}
}