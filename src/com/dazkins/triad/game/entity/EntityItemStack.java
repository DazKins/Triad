package com.dazkins.triad.game.entity;

import org.lwjgl.opengl.GL11;

import com.dazkins.triad.game.entity.mob.PlayerClientController;
import com.dazkins.triad.game.inventory.item.ItemStack;
import com.dazkins.triad.game.world.World;
import com.dazkins.triad.game.world.tile.Tile;
import com.dazkins.triad.gfx.BufferObject;
import com.dazkins.triad.gfx.Camera;
import com.dazkins.triad.gfx.Color;
import com.dazkins.triad.math.AABB;

public class EntityItemStack extends Entity
{

	static
	{
	}

	private ItemStack is;

	private float yBounce;

	public EntityItemStack(World w, float x, float y, ItemStack is)
	{
		super(w, StorageEntityID.ITEM_STACK, x, y, null);
		this.is = is;
	}

	public void renderToPlayerGui(Camera c)
	{
	}

	public void tick()
	{
		super.tick();

		xa *= 0.89;
		ya *= 0.89;

		super.move();

		yBounce = (float) (Math.sin((float) lifeTicks / 10.0f) * 4.0f) + 6;
	}

	public boolean mayPass(Entity e)
	{
		return true;
	}

	protected void onCollide(Entity e)
	{
		//TODO Reimplement
//		if (e instanceof EntityPlayerClientController)
//		{
//			if (getSpeed() < 0.2f && lifeTicks > 30)
//			{
//				EntityPlayerClientController ep = (EntityPlayerClientController) e;
//				if (ep.getInventory().addItemStack(is))
//					this.remove();
//			}
//		}
	}

	public AABB getBoundsForRendering()
	{
		return new AABB(x, y + yBounce, x + 32, y + 32 + yBounce);
	}

	public AABB getAABB()
	{
		return new AABB(x, y, x + 32, y + 5 + yBounce);
	}
}