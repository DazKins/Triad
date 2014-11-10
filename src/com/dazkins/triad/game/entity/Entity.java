package com.dazkins.triad.game.entity;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import com.dazkins.triad.file.SingleLineDatabaseFile;
import com.dazkins.triad.game.world.World;
import com.dazkins.triad.gfx.Camera;
import com.dazkins.triad.gfx.model.Model;
import com.dazkins.triad.math.AABB;

public abstract class Entity {
	private static Random rand = new Random();
	private static Map<Integer, Class<? extends Entity>> globalIDEntityMap = new HashMap<Integer, Class<? extends Entity>>();
	
	protected float x, y;
	protected float xa, ya;
	public long lifeTicks;
	protected String name;
	protected World world;
	
	protected int globalID; // TODO Implement working solution to generating globalID
	protected int individualID;
	
	protected SingleLineDatabaseFile database;
	
	protected boolean toBeRemoved;

	public Entity(World w, float x, float y, String s) {
		this.x = x;
		this.y = y;
		this.world = w;
		individualID = rand.nextInt();
		if (s != null) {
			try {
				database = new SingleLineDatabaseFile("res/data/entities/entity_" + s + ".db");
				name = database.getString("NAME");
				globalID = database.getInt("ID");
				if (!globalIDEntityMap.containsKey(globalID)) {
					globalIDEntityMap.put(globalID, getClass());
				}
			} catch (Exception e) {
				System.err.println("Unable to load database for entity: " + s);
			}
		}
	}
	
	public boolean needsToBeRemoved() {
		return toBeRemoved;
	}
	
	public void remove() {
		toBeRemoved = true;
	}
	
	public Facing getFacing() {
		float absXA = Math.abs(xa);
		float absYA = Math.abs(ya);
		
		if (absXA > absYA) {
			if (xa < 0)
				return Facing.LEFT;
			else
				return Facing.RIGHT;
		} else {
			if (ya < 0)
				return Facing.DOWN;
			else
				return Facing.UP;
		}
	}
	
	public abstract void renderToPlayerGui(Camera c);
	
	public abstract void render();
	
	protected Model getModel() {
		return Model.entityModelMap.get(this.getClass());
	}
	
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
	
	public void setXA(float a) {
		xa = a;
	}
	
	public float getYA() {
		return ya;
	}
	
	public void setYA(float a) {
		ya = a;
	}
	
	public void move(float xa, float ya) {
		if (this.getAABB() != null) {
			for (Entity e : world.getEntitiesInAABB(this.getAABB())) {
				if (e != this) {
					this.onCollide(e);
				}
			}
		}
		
		x += xa;
		y += ya;
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