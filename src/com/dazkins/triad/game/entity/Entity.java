package com.dazkins.triad.game.entity;

import java.sql.NClob;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import org.lwjgl.opengl.GL11;

import com.dazkins.triad.game.entity.mob.EntityPlayer;
import com.dazkins.triad.game.entity.mob.Mob;
import com.dazkins.triad.game.world.Chunk;
import com.dazkins.triad.game.world.World;
import com.dazkins.triad.game.world.tile.Tile;
import com.dazkins.triad.gfx.Camera;
import com.dazkins.triad.gfx.model.Model;
import com.dazkins.triad.math.AABB;

public abstract class Entity {
	private static Random rand = new Random();
	
	protected float x, y;
	protected float xa, ya;
	public long lifeTicks;
	protected String name;
	protected World world;
	
	protected int globalID; // TODO Implement working solution to generating globalID
	
	protected boolean toBeRemoved;
	
	protected ArrayList<Float> xvm;
	protected ArrayList<Float> yvm;
	
	protected Model model;

	public Entity(World w, float x, float y, String s) {
		this.x = x;
		this.y = y;
		this.world = w;
		this.name = s;
		xvm = new ArrayList<Float>();
		yvm = new ArrayList<Float>();
		initModel();
	}
	
	public void push(float xa, float ya) {
		addXAMod(xa);
		addYAMod(ya);
	}
	
	public boolean needsToBeRemoved() {
		return toBeRemoved;
	}
	
	public void remove() {
		toBeRemoved = true;
	}
	
	public void initWorld(World w) {
		world = w;
	}
	
	//Stores the last known direction the entity was facing
	private int lFacing;
	
	public int getFacing() {
		float absXA = Math.abs(xa);
		float absYA = Math.abs(ya);
		
		if (absXA > absYA) {
			if (xa < 0)
				lFacing = Facing.LEFT;
			else
				lFacing = Facing.RIGHT;
		} else if(absXA < absYA) {
			if (ya < 0)
				lFacing = Facing.DOWN;
			else
				lFacing = Facing.UP;
		}
		return lFacing;
	}
	
	public abstract void renderToPlayerGui(Camera c);
	
	public void render() {
		if (model != null) {
			int xx = (int) x >> 5;
			int yy = (int) y >> 5;
			float b = world.getTileBrightness(xx, yy);
			float bm = b / 14.0f;
			GL11.glColor3f(bm, bm, bm);
			model.render(this);
		}
	}
	
	protected void initModel() { }
	
	public float getX() {
		return x;
	}

	public void setX(float x) {
		this.x = x;
	}

	public float getY() {
		return y;
	}

	public void setY(float y) {
		this.y = y;
	}
	
	public float getXA() {
		return xa;
	}
	
	public void addXAMod(float a) {
		xvm.add(a);
	}
	
	public float getYA() {
		return ya;
	}
	
	public void addYAMod(float a) {
		yvm.add(a);
	}
	
	public float getSpeed() {
		float x0 = Math.abs(getXA());
		float y0 = Math.abs(getYA());
		float mag = (float) Math.sqrt(x0 * x0 + y0 * y0);
		return mag;
	}
	
	public void move(boolean a) {
		if (!a) {
			for (Float i : xvm) {
				this.xa += i;
			}
			for (Float i : yvm) {
				this.ya += i;
			}
		}
		
		if (this.getAABB() != null) {
			for (Entity e : world.getEntitiesInAABB(this.getAABB())) {
				if (e != this) {
					this.onCollide(e);
				}
			}
		}
		
		x += xa;
		y += ya;
		
		AABB b = getAABB();
		if (b != null) {
			if (b.getX0() < 0)
				x -= b.getX0();
			if (b.getY0() < 0)
				y -= b.getY0();
			if (b.getX1() > world.getW())
				x += world.getW() - b.getX1();
			if (b.getY1() > world.getH())
				y += world.getH() - b.getY1();
		}
		
		xvm.clear();
		yvm.clear();
	}
	
	protected void onCollide(Entity e) { }

	public void tick() {
		lifeTicks++;
	}
	
	public String getName() {
		return name;
	}
	
	public abstract AABB getAABB();
	
	public static SortByY ySorter = new SortByY();
	
	public static class SortByY implements Comparator<Entity> {
		public int compare(Entity o1, Entity o2) {
			return (o1.getY() > o2.getY()) ? -1 : 1;
		}
	}
}