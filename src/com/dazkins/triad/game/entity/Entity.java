package com.dazkins.triad.game.entity;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import com.dazkins.triad.file.DatabaseFile;
import com.dazkins.triad.game.world.World;
import com.dazkins.triad.gfx.Camera;
import com.dazkins.triad.gfx.GLRenderer;
import com.dazkins.triad.math.AABB;

public abstract class Entity {
	private static Random rand = new Random();
	private static Map<Integer, Class> globalIDEntityMap = new HashMap<Integer, Class>();
	
	protected float x, y;
	protected float xa, ya;
	protected long lifeTicks;
	protected String name;
	protected World world;
	
	protected int globalID; // TODO: Implement working solution to generating globalID
	protected int individualID;
	
	protected DatabaseFile database;

	public Entity(World w, float x, float y, String s) {
		this.x = x;
		this.y = y;
		this.world = w;
		individualID = rand.nextInt();
		try {
			database = new DatabaseFile("res/data/entities/entity_" + s + ".db");
		} catch (IOException e) {
			e.printStackTrace();
		}
		name = (String) database.tags.get(0).get("NAME");
		globalID = (Integer) database.tags.get(0).get("ID");
		if (!globalIDEntityMap.containsKey(globalID)) {
			globalIDEntityMap.put(globalID, getClass());
		}
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
	
	public void move(float xa, float ya) {
		x += xa;
		y += ya;
	}
	
	public void tick() {
		lifeTicks++;
	}
	
	public abstract void render(GLRenderer t, Camera cam);
	
	public abstract AABB getAABB();
}