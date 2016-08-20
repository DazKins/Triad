package com.dazkins.triad.game.entity.mob;

import java.util.ArrayList;

import com.dazkins.triad.game.ability.Ability;
import com.dazkins.triad.game.ability.AbilityBar;
import com.dazkins.triad.game.ability.AbilitySwingWeapon;
import com.dazkins.triad.game.entity.Entity;
import com.dazkins.triad.game.entity.Interactable;
import com.dazkins.triad.game.entity.StorageEntityID;
import com.dazkins.triad.game.inventory.EquipmentInventory;
import com.dazkins.triad.game.inventory.Inventory;
import com.dazkins.triad.game.inventory.item.Item;
import com.dazkins.triad.game.inventory.item.ItemStack;
import com.dazkins.triad.game.world.World;
import com.dazkins.triad.gfx.model.animation.StorageAnimationID;
import com.dazkins.triad.math.AABB;

public class EntityPlayer extends Mob
{
	public EntityPlayer(World w, float x, float y, String name)
	{
		super(w, StorageEntityID.PLAYER, x, y, name, 100);
		this.inv = new Inventory(9, 5);
		abilityBar = new AbilityBar(this, 10);
		abilityBar.setAbility(0, Ability.swingWeapon);
		inv.addItem(Item.log);
		this.eInv = new EquipmentInventory();
		eInv.addItemStack(new ItemStack(Item.testHelmet, 1));
		eInv.addItemStack(new ItemStack(Item.testChest, 1));
//		eInv.addItemStack(new ItemStack(Item.testFeet, 1));
		eInv.addItemStack(new ItemStack(Item.testLegs, 1));
		eInv.addItemStack(new ItemStack(Item.testSword, 1));
	}

	public int getMaxHealth()
	{
		return 100;
	}
	
	public void onUseAbility(Ability a)
	{
		if (a instanceof AbilitySwingWeapon)
		{
			addNewAnimation(StorageAnimationID.HUMANOID_SLASHING, 1, false, 0.3f);
		}
	}
	
	public void attemptInteract()
	{
		AABB b = getFacingAttackArea(getFacing());
		ArrayList<Interactable> ints = world.getInteractablesInAABB(b);
		for (Interactable i : ints)
		{
			setInteractingObject(i);
			i.onInteract(this);
		}
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
	
	public void tick()
	{
		super.tick();
		super.move();
		
		if (Math.abs(xa) >= 0.2f || Math.abs(ya) >= 0.2f)
			addNewAnimation(StorageAnimationID.HUMANOID_WALKING, 2, false, 1);
		else
			addNewAnimation(StorageAnimationID.HUMANOID_IDLE, 2, false, 1);

		setFacingBasedOnVelocities(xa, ya);
		
		xa *= 0.75f;
		ya *= 0.75f;
	}
}