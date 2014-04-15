package com.dazkins.triad.game.entity;

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

	public Entity(World w, float x, float y, String s) {
		this.x = x;
		this.y = y;
		this.world = w;
		individualID = rand.nextInt();
		try {
			database = new SingleLineDatabaseFile("res/data/entities/entity_" + s + ".db");
			name = database.getString("NAME");
			globalID = database.getInt("ID");
			if (!globalIDEntityMap.containsKey(globalID)) {
				globalIDEntityMap.put(globalID, getClass());
			}
		} catch (Exception e) {
		}
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
	
	public float getYA() {
		return ya;
	}
	
	public void move(float xa, float ya) {
		x += xa;
		y += ya;
	}
	
	public void tick() {
		lifeTicks++;
	}
	
	public String getName() {
		return name;
	}
	
	public abstract AABB getAABB();
}