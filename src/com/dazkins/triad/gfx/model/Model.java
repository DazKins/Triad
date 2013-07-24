package com.dazkins.triad.gfx.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.lwjgl.opengl.GL11;

import com.dazkins.triad.game.entity.Entity;
import com.dazkins.triad.game.entity.mob.EntityPlayer;

public class Model {
	public static Map<Class<? extends Entity>, Model> entityModelMap = new HashMap<Class<? extends Entity>, Model>();
	
	protected List quads;
	
	protected List quadSkips;
	
	private float offsetX, offsetY; 
	
	public static void loadModels() {
		entityModelMap.put(EntityPlayer.class, new ModelPlayer());
	}
	
	protected void setOffset(float x, float y) {
		offsetX = x;
		offsetY = y;
	}
	
	public void skipRender(Quad quad) {
		quadSkips.add(quads.indexOf(quad));
	}
	
	public Model() {
		quads = new ArrayList();
		quadSkips = new ArrayList();
	}
	
	public void render() {
		GL11.glPushMatrix();
		GL11.glTranslatef(offsetX, offsetY, 0);
		for (int i = 0; i < quads.size(); i++) {
			if (!quadSkips.contains(i))
				((Quad) quads.get(i)).render();
		}
		GL11.glPopMatrix();
	}
}