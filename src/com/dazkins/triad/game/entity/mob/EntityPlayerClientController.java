package com.dazkins.triad.game.entity.mob;


import org.lwjgl.system.glfw.GLFW;

import com.dazkins.triad.game.entity.Entity;
import com.dazkins.triad.game.entity.Facing;
import com.dazkins.triad.game.entity.StorageEntityID;
import com.dazkins.triad.game.entity.shell.EntityShell;
import com.dazkins.triad.game.inventory.Inventory;
import com.dazkins.triad.game.inventory.item.Item;
import com.dazkins.triad.input.InputHandler;
import com.dazkins.triad.math.AABB;

public class EntityPlayerClientController
{
	private float x;
	private float y;
	
	private float xa;
	private float ya;
	
	private int facing;
	
	private Inventory inv;
	
	private InputHandler input;

	private EntityShell interactingObject;

	private String name;

	public EntityPlayerClientController(String n, float x, float y, InputHandler input)
	{
		this.x = x;
		this.y = y;
		this.input = input;
		this.inv = new Inventory(9, 5);
		inv.addItem(Item.testHelmet);
		inv.addItem(Item.testChest);
		inv.addItem(Item.testLegs);
		inv.addItems(Item.testSword, 4);
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

	public EntityShell getInteractingObject()
	{
		return interactingObject;
	}

	public void setInteractingObject(EntityShell i)
	{
		interactingObject = i;
	}

	public void tick()
	{
		float moveModifier = 1.0f;

		if (input != null)
		{
			if (input.isKeyDown(GLFW.GLFW_KEY_LEFT_SHIFT))
			{
				moveModifier = 10.0f;
			}

			if (input.isKeyDown(GLFW.GLFW_KEY_W))
			{
				facing = Facing.UP;
				ya += getMovementSpeed() * moveModifier;
			} else if (input.isKeyDown(GLFW.GLFW_KEY_S))
			{
				facing = Facing.DOWN;
				ya += -getMovementSpeed() * moveModifier;
			}
			if (input.isKeyDown(GLFW.GLFW_KEY_A))
			{
				facing = Facing.LEFT;
				xa += -getMovementSpeed() * moveModifier;
			} else if (input.isKeyDown(GLFW.GLFW_KEY_D))
			{
				facing = Facing.RIGHT;
				xa += getMovementSpeed() * moveModifier;
			}

			//TODO reimplement
//			if (input.isKeyDown(GLFW.GLFW_KEY_SPACE))
//			{
//				if (attemptAttack(getFacingAttackArea(getFacing())))
//				{
//					tryHarvest();
//				}
//			}
		}

		x += xa;
		y += ya;

		xa *= 0.75f;
		ya *= 0.75f;
	}
	
	public float getXA()
	{
		return xa;
	}
	
	public float getYA()
	{
		return ya;
	}
	
	public float getX()
	{
		return x;
	}
	
	public float getY()
	{
		return y;
	}
	
	public void setX(float x)
	{
		this.x = x;
	}
	
	public void setY(float y)
	{
		this.y = y;
	}
	
	public int getFacing()
	{
		return facing;
	}
	
	public Inventory getInventory()
	{
		return inv;
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
	
	public void setInventory(Inventory i)
	{
		inv = i;
	}
}