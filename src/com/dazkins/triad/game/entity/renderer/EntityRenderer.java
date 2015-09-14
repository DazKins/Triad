package com.dazkins.triad.game.entity.renderer;

import org.lwjgl.opengl.GL11;

import com.dazkins.triad.game.world.IWorldAccess;
import com.dazkins.triad.game.world.tile.Tile;
import com.dazkins.triad.gfx.Camera;
import com.dazkins.triad.gfx.Color;
import com.dazkins.triad.gfx.model.Model;

public abstract class EntityRenderer {
	//ID of the entity the rendering instance is refering to
	private int id;
	
	private float x;
	private float y;
	
	private boolean isModelReady;
	
	private Model model;
	
	private boolean idAssigned;
	
	protected IWorldAccess world;
	
	public abstract void initModel();
	
	public void setID(int id) {
		if (!idAssigned) {
			this.id = id;
			idAssigned = true;
		} else {
			System.err.println("ID has already been assigned to this entity");
		}
	}
	
	public void setWorld(IWorldAccess wo) {
		world = wo;
	}
	
	public int getID() {
		return id;
	}
		
	public void setX(float x) {
		this.x = x;
	}
	
	public void setY(float y) {
		this.y = y;
	}
	
	protected void setModel(Model m) {
		model = m;
		isModelReady = true;
	}
	
	public float getX() {
		return x;
	}
	
	public float getY() {
		return y;
	}
	
	public void render(Camera cam) {
		if (isModelReady) {
			model.setOffset(x, y);
			model.setDepth(Tile.yPosToDepthRelativeToCamera(cam, y));
			Color t = world.getTileColor((int) (x / Tile.tileSize), (int) (y / Tile.tileSize));
			if (t != null)
				GL11.glColor3f(t.getDR(), t.getDG(), t.getDB());
			model.render();
		}
		else
			initModel();
	}
	
	//Old method from Entity class for reference
//	public void render(Camera cam) {
//		if (!isModelReady())
//			initModel();
//		if (model != null) {
//			int xx = (int) x >> 5;
//			int yy = (int) y >> 5;
//			if (world != null) {
//				Color c = world.getTileColor(xx, yy);
//				GL11.glColor4f(c.getDR(), c.getDG(), c.getDB(), 1.0f);
//			}
//			model.render(cam, this);
//		}
//	}
}