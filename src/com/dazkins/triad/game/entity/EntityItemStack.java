package com.dazkins.triad.game.entity;

import org.lwjgl.opengl.GL11;

import com.dazkins.triad.game.entity.mob.EntityPlayer;
import com.dazkins.triad.game.inventory.item.ItemStack;
import com.dazkins.triad.game.world.World;
import com.dazkins.triad.game.world.tile.Tile;
import com.dazkins.triad.gfx.Camera;
import com.dazkins.triad.math.AABB;

public class EntityItemStack extends Entity {
	private ItemStack is;
	
	private float yBounce;
	
	public EntityItemStack(World w, float x, float y, ItemStack is) {
		super(w, x, y, null);
		this.is = is;
	}

	public void renderToPlayerGui(Camera c) { }
	
	public void tick() {
		super.tick();
		
		xa *= 0.89;
		ya *= 0.89;
		
		super.move(false);
		
		yBounce = (float) (Math.sin((float)lifeTicks / 10.0f) * 4.0f) + 6;
	}

	public void render() {
		//Shadow
		GL11.glDisable(GL11.GL_TEXTURE_2D);
		float z = Tile.yPosToDepth(y);
		float b = world.getTileBrightness((int) x >> 5, (int) y >> 5);
		float bm = b / 14.0f;
		GL11.glBegin(GL11.GL_QUADS);
			GL11.glColor4f(0.0f, 0.0f, 0.0f, 0.2f);
			GL11.glVertex3f(x, y, z);
			GL11.glVertex3f(x + 32, y, z);
			GL11.glVertex3f(x + 32, y + 5, z);
			GL11.glVertex3f(x, y + 5, z);
			GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
		GL11.glEnd();
		GL11.glEnable(GL11.GL_TEXTURE_2D);
		GL11.glColor3f(bm, bm, bm);
		is.getItemType().renderIcon(x, y + yBounce, Tile.yPosToDepth(y) + 0.001f, 1);
		GL11.glColor3f(1.0f, 1.0f, 1.0f);
	}
	
	protected void onCollide(Entity e) {
		if (e instanceof EntityPlayer) {
			if (getSpeed() < 0.2f && lifeTicks > 30) {
				EntityPlayer ep = (EntityPlayer) e;
				if (ep.getInventory().addItemStack(is))
					this.remove();
			}
		}
	}
	
	public AABB getAABB() {
		return new AABB(x, y, x + 32, y + 32);
	}
}