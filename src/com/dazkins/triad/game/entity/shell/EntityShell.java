package com.dazkins.triad.game.entity.shell;

import com.dazkins.triad.game.ability.AbilityBar;
import com.dazkins.triad.game.entity.renderer.EntityRenderer;
import com.dazkins.triad.game.inventory.EquipmentInventory;
import com.dazkins.triad.game.inventory.Inventory;
import com.dazkins.triad.gfx.Camera;
import com.dazkins.triad.networking.client.ClientEntityManager;

//Stores entity renderer with other info that may be needed by the client, such as inventory, equipment etc.
public class EntityShell
{
	private ClientEntityManager clientEntityManager;
	
	private EntityRenderer entityRenderer;
	
	private EntityShell interactingEntity;
	private int interactingEntityID;
	
	private float x;
	private float y;
	
	private int facing;
	
	private String name;
	
	private Inventory inventory;
	private EquipmentInventory equipmentInventory;
	private AbilityBar abilityBar;
	
	private int globalID;
	
	private int health;
	private int maxHealth;
	
	public EntityShell(ClientEntityManager cem, int gID)
	{
		clientEntityManager = cem;
		globalID = gID;
	}
	
	public void render(Camera cam)
	{
		if (entityRenderer != null)
			entityRenderer.render(cam);
	}
	
	public int getGlobalID()
	{
		return globalID;
	}

	public Inventory getInventory()
	{
		return inventory;
	}
	
	public void setInteractingEntityID(int id)
	{
		interactingEntityID = id;
		if (id >= 0)
			interactingEntity = clientEntityManager.getEntityShell(id);
		else
			interactingEntity = null;
	}
	
	public EntityShell getInteractingEntity()
	{
		if (interactingEntity == null)
		{
			if (interactingEntityID > 0)
				setInteractingEntityID(interactingEntityID);
		}
		return interactingEntity;
	}

	public void setInventory(Inventory inventory)
	{
		this.inventory = inventory;
	}
	
	public void setAbilityBar(AbilityBar a)
	{
		this.abilityBar = a;
	}
	
	public AbilityBar getAbilityBar()
	{
		return this.abilityBar;
	}
	
	public void tick()
	{
		if (entityRenderer != null)
			entityRenderer.tick();
	}
	
	public void setRenderer(EntityRenderer e)
	{
		if (e != null)
		{
			entityRenderer = e;
			entityRenderer.initEntityShell(this);
		}
	}
	
	public EntityRenderer getRenderer()
	{
		return entityRenderer;
	}

	public float getX()
	{
		return x;
	}

	public void setX(float x)
	{
		this.x = x;
		if (entityRenderer != null)
			entityRenderer.setX(x);
	}

	public float getY()
	{
		return y;
	}

	public void setY(float y)
	{
		this.y = y;
		if (entityRenderer != null)
			entityRenderer.setY(y);
	}
	
	public int getFacing()
	{
		return facing;
	}
	
	public void setFacing(int f)
	{
		facing = f;
		if (entityRenderer != null)
			entityRenderer.setFacing(f);
	}

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
		if (entityRenderer != null)
			entityRenderer.setName(name);
	}

	public void setEquipmentInventory(EquipmentInventory i)
	{
		equipmentInventory = i;
	}
	
	public EquipmentInventory getEquipmentInventory()
	{
		return equipmentInventory;
	}
	
	public int getHealth()
	{
		return health;
	}
	
	public void setHealth(int h)
	{
		this.health = h;
	}
	
	public int getMaxHealth()
	{
		return this.maxHealth;
	}
	
	public void setMaxHealth(int mh)
	{
		this.maxHealth = mh;
	}
}