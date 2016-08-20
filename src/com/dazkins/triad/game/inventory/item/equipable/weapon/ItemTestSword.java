package com.dazkins.triad.game.inventory.item.equipable.weapon;

public class ItemTestSword extends ItemWeapon
{
	public ItemTestSword(int id)
	{
		super(id, "testSword");
	}

	public int getDamage()
	{
		return 15;
	}

	public int getKnockback()
	{
		return 18;
	}

	public int getAttackCooldown()
	{
		return 30;
	}

	public int getAttackRange()
	{
		return 100;
	}
}