package com.dazkins.triad.game.entity.mob;

import java.util.ArrayList;

import com.dazkins.triad.game.ability.Ability;
import com.dazkins.triad.game.ability.AbilityBar;
import com.dazkins.triad.game.entity.Entity;
import com.dazkins.triad.game.entity.Facing;
import com.dazkins.triad.game.entity.IEntityWithAbilityBar;
import com.dazkins.triad.game.entity.IEntityWithEquipmentInventory;
import com.dazkins.triad.game.entity.IEntityWithInventory;
import com.dazkins.triad.game.entity.Interactable;
import com.dazkins.triad.game.inventory.EquipmentInventory;
import com.dazkins.triad.game.inventory.Inventory;
import com.dazkins.triad.game.inventory.item.Item;
import com.dazkins.triad.game.inventory.item.ItemStack;
import com.dazkins.triad.game.inventory.item.equipable.weapon.ItemWeapon;
import com.dazkins.triad.game.inventory.item.equipable.weapon.harvestTool.ItemHarvestTool;
import com.dazkins.triad.game.world.World;
import com.dazkins.triad.math.AABB;

public abstract class Mob extends Entity implements IEntityWithInventory, IEntityWithAbilityBar, IEntityWithEquipmentInventory
{
	protected int health;
	protected Inventory inv;
	protected EquipmentInventory eInv;

	private int attackCooldownCounter;

	protected Mob target;
	
	protected Interactable interactingObject;
	
	protected AbilityBar abilityBar;

	public Mob(World w, int id, float x, float y, String s, int h)
	{
		super(w, id, x, y, s);
		health = h;
		eInv = new EquipmentInventory();
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

	public Interactable getInteractingObject()
	{
		return interactingObject;
	}

	public void setInteractingObject(Interactable i)
	{
		interactingObject = i;
		world.getServerWorldManager().onInteractUpdate(this);
	}
	
	public void useAbility(int n)
	{
		abilityBar.useAbility(n);
	}
	
	public void onUseAbility(Ability a)
	{
		
	}

	protected void moveUp()
	{
		setFacing(Facing.UP);
		addYAMod(getMovementSpeed());
	}

	protected void moveDown()
	{
		setFacing(Facing.DOWN);
		addYAMod(-getMovementSpeed());
	}

	protected void moveLeft()
	{
		setFacing(Facing.LEFT);
		addXAMod(-getMovementSpeed());
	}

	protected void moveRight()
	{
		setFacing(Facing.RIGHT);
		addXAMod(getMovementSpeed());
	}

	public int getMovementState()
	{
		float speedLen = 0.2f;
		if (Math.abs(xa) > speedLen || Math.abs(ya) > speedLen)
			return MovementState.MOVING;
		else
			return MovementState.STATIONARY;
	}

	public abstract float getMovementSpeed();

	public abstract int getMaxHealth();

	public int getHealth()
	{
		return health;
	}

	protected int getBaseDamage()
	{
		return 0;
	}

	public int getDamage()
	{
		ItemWeapon wep = eInv.getWeaponItem();
		if (wep != null)
		{
			return wep.getDamage();
		} else
		{
			return getBaseDamage();
		}
	}

	protected int getBaseKnockback()
	{
		return 0;
	}

	public int getKnockbackValue()
	{
		ItemWeapon wep = eInv.getWeaponItem();
		if (wep != null)
		{
			return wep.getKnockback();
		} else
		{
			return getBaseKnockback();
		}
	}

	protected int getBaseAttackCooldown()
	{
		return 0;
	}

	protected int getAttackCooldown()
	{
		ItemWeapon wep = eInv.getWeaponItem();
		if (wep != null)
		{
			return wep.getAttackCooldown();
		} else
		{
			return getBaseAttackCooldown();
		}
	}

	protected int getBaseAttackRange()
	{
		return 0;
	}

	protected int getAttackRange()
	{
		ItemWeapon wep = eInv.getWeaponItem();
		if (wep != null)
		{
			return wep.getAttackRange();
		} else
		{
			return getBaseAttackRange();
		}
	}

	public void tryHarvest()
	{
		AABB b = getFacingAttackArea(getFacing());
		Item i = eInv.getWeaponItem();
		int d = 0;
		if (i != null)
		{
			if (i instanceof ItemHarvestTool)
			{
				ItemHarvestTool h = (ItemHarvestTool) i;
				d = h.getHarvestDamage();
				world.sendHarvestCommand(b, this, d);
			}
		}
	}

	protected boolean attemptAttack(AABB a)
	{
		if (attackCooldownCounter == 0)
		{
			attackArea(a);
			attackCooldownCounter = getAttackCooldown();
			return true;
		} else
		{
			return false;
		}
	}

	private void attackArea(AABB a)
	{
		world.sendAttackCommand(a, this, getDamage(), getKnockbackValue());
	}

	public Class<? extends Mob>[] getHostileMobs()
	{
		return null;
	}

	public AABB getEnemyScanArea()
	{
		return null;
	}

	public void hurt(Mob m, int d, int k)
	{
		float x0 = this.getX() - m.getX();
		float y0 = this.getY() - m.getY();
		float mag = (float) Math.sqrt(x0 * x0 + y0 * y0);
		x0 /= mag;
		y0 /= mag;
		push(x0 * k, y0 * k);
		health -= d;
	}
	
	private int prevHealth = -1;

	public void tick()
	{
		super.tick();

		ArrayList<Entity> ents = new ArrayList<Entity>();

		AABB eb = this.getEnemyScanArea();

		if (eb != null)
		{
			ArrayList<Mob> hostileMobs = new ArrayList<Mob>();
			ents = world.getEntitiesInAABB(eb);
			for (Entity e : ents)
			{
				if (e instanceof Mob)
				{
					if (this.isHostileTo(e))
						hostileMobs.add((Mob) e);
				}
			}

			if (hostileMobs.size() > 1)
			{
				int r = (int) (Math.random() * (hostileMobs.size() - 1));
				this.target = hostileMobs.get(r);
			} else if (hostileMobs.size() == 1)
			{
				this.target = hostileMobs.get(0);
			} else
			{
				target = null;
			}
		}

		if (attackCooldownCounter > 0)
		{
			attackCooldownCounter--;
		}

		if (health <= 0)
		{
			kill();
		}
		
		if (health != prevHealth)
			world.getServerWorldManager().handleHealthUpdate(this);
			
		prevHealth = health;
		
		if (abilityBar != null)
			world.getServerWorldManager().handleCooldownUpdate(this, abilityBar.getCooldownUpdates());
	}

	public ItemStack[] getItemsToDrop()
	{
		return null;
	}

	public void onDeath()
	{
		ItemStack[] stacks = getItemsToDrop();
		if (stacks != null)
		{
			for (int i = 0; i < stacks.length; i++)
			{
				Item.dropItemStack(world, x, y, stacks[i]);
			}
		}
	}

	public void kill()
	{
		onDeath();
		remove();
	}

	public boolean mayPass(Entity e)
	{
		if (e instanceof Mob)
		{
			return false;
		}
		return true;
	}

	public boolean isHostileTo(Entity e)
	{
		Class<? extends Entity>[] c = getHostileMobs();
		for (int i = 0; i < c.length; i++)
		{
			if (e.getClass() == c[i])
				return true;
		}
		return false;
	}

	public Inventory getInventory()
	{
		return inv;
	}
	
	public void setInventory(Inventory i)
	{
		inv = i;
	}
	
	public AbilityBar getAbilityBar()
	{
		return abilityBar;
	}
	
	public void setAbilityBar(AbilityBar b)
	{
		abilityBar = b;
	}

	protected AABB[] getInteractAreas()
	{
		AABB[] r = new AABB[4];
		int ir = 40;
		r[Facing.DOWN] = new AABB(x - 20, y - ir + 5, x + 20, y + 5);
		r[Facing.UP] = new AABB(x - 20, y + 5, x + 20, y + 5 + ir);
		r[Facing.LEFT] = new AABB(x - ir, y - 10, x, y + 20);
		r[Facing.RIGHT] = new AABB(x, y - 10, x + ir, y + 20);
		return r;
	}

	public AABB[] getAttackAreas()
	{
		AABB[] r = new AABB[4];
		int ar = getAttackRange();
		r[Facing.DOWN] = new AABB(x - 20, y - ar + 5, x + 20, y + 5);
		r[Facing.UP] = new AABB(x - 20, y + 5, x + 20, y + 5 + ar);
		r[Facing.LEFT] = new AABB(x - ar, y - 10, x, y + 20);
		r[Facing.RIGHT] = new AABB(x, y - 10, x + ar, y + 20);
		return r;
	}

	protected AABB getFacingAttackArea(int i)
	{
		return getAttackAreas()[i];
	}

	public EquipmentInventory getEquipmentInventory()
	{
		return eInv;
	}
	
	public void setEquipmentInventory(EquipmentInventory i)
	{
		eInv = i;
	}
}