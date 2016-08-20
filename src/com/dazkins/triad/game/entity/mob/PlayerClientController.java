package com.dazkins.triad.game.entity.mob;

import org.lwjgl.system.glfw.GLFW;

import com.dazkins.triad.game.entity.Entity;
import com.dazkins.triad.game.entity.Facing;
import com.dazkins.triad.game.entity.shell.EntityShell;
import com.dazkins.triad.game.inventory.Inventory;
import com.dazkins.triad.game.inventory.item.Item;
import com.dazkins.triad.input.InputHandler;
import com.dazkins.triad.math.AABB;
import com.dazkins.triad.math.MathHelper;
import com.dazkins.triad.networking.client.TriadClient;

public class PlayerClientController
{
	private TriadClient client;
	
	private float x;
	private float y;
	
	private float xa;
	private float ya;
	
	private InputHandler input;

	private EntityShell interactingObject;

	private String name;

	public PlayerClientController(String n, float x, float y, InputHandler input)
	{
		this.x = x;
		this.y = y;
		this.input = input;

		name = n;
	}
	
	public void setClient(TriadClient c)
	{
		this.client = c;
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
				ya += getMovementSpeed() * moveModifier;
			} else if (input.isKeyDown(GLFW.GLFW_KEY_S))
			{
				ya += -getMovementSpeed() * moveModifier;
			}
			if (input.isKeyDown(GLFW.GLFW_KEY_A))
			{
				xa += -getMovementSpeed() * moveModifier;
			} else if (input.isKeyDown(GLFW.GLFW_KEY_D))
			{
				xa += getMovementSpeed() * moveModifier;
			}
			
			if (input.isKeyJustDown(GLFW.GLFW_KEY_1))
			{
				client.useAbility(0);
			}
			
			if (input.isKeyJustDown(GLFW.GLFW_KEY_2))
			{
				client.useAbility(1);
			}
			
			if (input.isKeyJustDown(GLFW.GLFW_KEY_3))
			{
				client.useAbility(2);
			}
			
			if (input.isKeyJustDown(GLFW.GLFW_KEY_4))
			{
				client.useAbility(3);
			}
			
			if (input.isKeyJustDown(GLFW.GLFW_KEY_5))
			{
				client.useAbility(4);
			}
			
			if (input.isKeyJustDown(GLFW.GLFW_KEY_6))
			{
				client.useAbility(5);
			}
			
			if (input.isKeyJustDown(GLFW.GLFW_KEY_7))
			{
				client.useAbility(6);
			}
			
			if (input.isKeyJustDown(GLFW.GLFW_KEY_8))
			{
				client.useAbility(7);
			}
			
			if (input.isKeyJustDown(GLFW.GLFW_KEY_9))
			{
				client.useAbility(8);
			}
			
			if (input.isKeyJustDown(GLFW.GLFW_KEY_0))
			{
				client.useAbility(9);
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

		client.updatePlayerVelocity(xa, ya);

//		x += xa;
//		y += ya;

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
	
//	public int getFacing()
//	{
//		return facing;
//	}

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
		return 0.2f + MathHelper.getRandomWithNegatives() * 0.001f;
	}
}