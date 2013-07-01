package com.dazkins.triad.game.entity;

import java.io.IOException;
import java.util.Random;

import com.dazkins.triad.file.DatabaseFile;
import com.dazkins.triad.gfx.Camera;
import com.dazkins.triad.gfx.bitmap.Bitmap;

public abstract class Entity {
	private static Random rand;
	
	protected float x, y;
	protected long lifeTicks;
	protected String name;
	
	protected int globalID;
	protected int individualID;
	protected int databaseFileReference;
	
	protected static DatabaseFile entityDatabase;
	
	static {
		rand = new Random();
		
		try {
			entityDatabase = new DatabaseFile("res/data/entities.db");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public Entity(float x, float y, int dfr) {
		this.x = x;
		this.y = y;
		individualID = rand.nextInt();
		loadEntityData(dfr);
	}
	
	protected void loadEntityData(int dfr) {
		if (dfr == -1)
			return;
		name = (String) entityDatabase.tags.get(dfr).get("NAME");
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
	
	public abstract void render(Bitmap b, Camera cam);
}