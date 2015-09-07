package com.dazkins.triad.game.entity;

import java.util.ArrayList;
import java.util.Comparator;

import org.lwjgl.opengl.GL11;

import com.dazkins.triad.game.entity.mob.Mob;
import com.dazkins.triad.game.world.World;
import com.dazkins.triad.game.world.tile.Tile;
import com.dazkins.triad.gfx.BufferObject;
import com.dazkins.triad.gfx.Camera;
import com.dazkins.triad.gfx.Color;
import com.dazkins.triad.gfx.model.Model;
import com.dazkins.triad.math.AABB;

public abstract class Entity {
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
		this.y = y - (float) Math.random() / 100.0f;
		this.world = w;
		this.name = s;
		xvm = new ArrayList<Float>();
		yvm = new ArrayList<Float>();
	}
	
	protected boolean isModelReady() {
		return model != null;
	}
	
	public void push(float xa, float ya) {
		addXAMod(xa);
		addYAMod(ya);
	}
	
	public boolean mayPass(Entity e) {
		return false;
	}
	
	public boolean mayBePushedBy(Entity e) {
		return false;
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
	
	public AABB getBoundsForRendering() {
		if (model != null)
			return model.getRenderAABB().shifted(x, y);
		return null;
	}
	
	//Stores the last known direction the entity was facing
	private int facing;
	
	public int getFacing() {
		return facing;
	}
	
	protected void setFacingBasedOnVelocities(float xa, float ya) {
		if (Math.abs(xa) > Math.abs(ya)) {
			if (xa < 0)
				facing = Facing.LEFT;
			else
				facing = Facing.RIGHT;
		} else if (Math.abs(xa) < Math.abs(ya)) {
			if (ya < 0)
				facing = Facing.DOWN;
			else
				facing = Facing.UP;	
		}
	}
	
	public void setFacing(int f) {
		facing = f;
	}
	
	public void renderToPlayerGui(Camera c) { }
	
	public void render(Camera cam) {
		if (!isModelReady())
			initModel();
		if (model != null) {
			int xx = (int) x >> 5;
			int yy = (int) y >> 5;
			Color c = world.getTileColor(xx, yy);
			GL11.glColor4f(c.getDR(), c.getDG(), c.getDB(), 1.0f);
			model.render(cam, this);
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
	
	public void move() {
		for (Float i : xvm) {
			this.xa += i;
		}
		for (Float i : yvm) {
			this.ya += i;
		}
		
		if (this.getAABB() != null) {
			for (Entity e : world.getEntitiesInAABB(this.getAABB())) {
				if (e != this) {
					this.onCollide(e);
				}
			}
			for (Entity e : world.getEntitiesInAABB(this.getAABB().shifted(xa, 0))) {
				if (e != this && (!this.mayPass(e) || !e.mayPass(this))) {
					if (e.mayBePushedBy(this)) {
						e.push(xa, 0);
					} else {
					}
					xa = 0;
				}
			}
			for (Entity e : world.getEntitiesInAABB(this.getAABB().shifted(0, ya))) {
				if (e != this && (!this.mayPass(e) || !e.mayPass(this))) {
					if (e.mayBePushedBy(this)) {
						e.push(0, ya);
					} else {
					}
					ya = 0;
				}
			}
		}
		
		x += xa;
		y += ya;
		
		xvm.clear();
		yvm.clear();
	}
	
	protected void onCollide(Entity e) { }

	public void tick() {
		if (!isModelReady())
			initModel();
		
		lifeTicks++;
		world.registerEntityTick(this);
	}
	
	public String getName() {
		return name;
	}
	
	public abstract AABB getAABB();
	
	public static SortByY ySorter = new SortByY();
	
	public static class SortByY implements Comparator<Entity> {
		public int compare(Entity o1, Entity o2) {
			return (o1.getY() > o2.getY()) ? -1 : (o1.getY() < o2.getY()) ? 1 : 0;
		}
	}
	
	protected void renderShadow(Camera c, float x, float y, float w, float h) {
		GL11.glPushMatrix();
		GL11.glTranslatef(x, y, Tile.yPosToDepthRelativeToCamera(c, this.y) - 0.005f);
		GL11.glScalef(w / 32.0f, h / 32.0f, 1.0f);
		BufferObject.shadow.render();
		GL11.glPopMatrix();
	}
}