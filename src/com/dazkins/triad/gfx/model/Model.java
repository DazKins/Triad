package com.dazkins.triad.gfx.model;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.lwjgl.opengl.GL11;

import com.dazkins.triad.game.entity.Entity;
import com.dazkins.triad.game.entity.mob.EntityPlayer;
import com.dazkins.triad.game.entity.mob.EntityZombie;
import com.dazkins.triad.gfx.Image;

public class Model {
	public static Map<Class<? extends Entity>, Model> entityModelMap = new HashMap<Class<? extends Entity>, Model>();
	
	protected List<Quad> quads;
	protected List<Integer> quadRenders;
	
	private Image img;
	
	private float offsetX, offsetY;
	private float depth;

	private boolean selectiveRendering;
	
	public static void loadModels() {
		try {
			entityModelMap.put(EntityPlayer.class, new ModelHumanoid(new Image("/art/entities/player.png")));
			entityModelMap.put(EntityZombie.class, new ModelHumanoid(new Image("/art/entities/zombie.png")));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	protected void setOffset(float x, float y) {
		offsetX = x;
		offsetY = y;
	}
	
	protected void setDepth(float d) {
		depth = d;
	}
	
	protected void addQuads(Quad[] q) {
		for (int i = 0; i < q.length; i++) {
			addQuad(q[i]);
		}
	}
	
	protected void addQuad(Quad q) {
		q.init(img);
		q.generate();
		quads.add(q);
	}
	
	protected void enableSelectiveRendering() {
		selectiveRendering = true;
	}
	
	protected void disableSelectiveRendering() {
		selectiveRendering = false;
	}
	
	public void addQuadToRenderQeue(Quad quad) {
		if (!selectiveRendering) {
			System.err.println("Warning! Selective rendering is not enabled!");
		}
		quadRenders.add(quads.indexOf(quad));
	}
	
	public Model(Image i) {
		quads = new ArrayList<Quad>();
		quadRenders = new ArrayList<Integer>();
		img = i;
	}
	 
	public void render() {
		GL11.glPushMatrix();
		GL11.glTranslatef(offsetX, offsetY, depth);
		if(selectiveRendering) {
			for (int i = 0; i < quadRenders.size(); i++) {
				Quad q = (Quad) quads.get(quadRenders.get(i));
				renderQuad(q);
			}
		} else {
			for (int i = 0; i < quads.size(); i++) {
				Quad q = (Quad) quads.get(i);
				renderQuad(q);
			}
		}
		quadRenders.clear();
		GL11.glPopMatrix();
	}
	
	private void renderQuad(Quad q) {
		q.render();
	}
}