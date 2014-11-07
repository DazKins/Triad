package com.dazkins.triad.gfx.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.lwjgl.opengl.GL11;

import com.dazkins.triad.game.entity.Entity;
import com.dazkins.triad.game.entity.EntityButton;
import com.dazkins.triad.game.entity.mob.EntityPlayer;
import com.dazkins.triad.game.entity.mob.EntityZombie;
import com.dazkins.triad.gfx.Image;
import com.dazkins.triad.gfx.model.animation.Animation;

public abstract class Model {
	public static Map<Class<? extends Entity>, Model> entityModelMap = new HashMap<Class<? extends Entity>, Model>();
	
	protected List<Quad> quads;
	protected List<Integer> quadRenders;
	
	private Image img;
	
	private float offsetX, offsetY;
	private float depth;

	private boolean selectiveRendering;
	
	private Animation anim;
	
	public static void loadModels() {
		entityModelMap.put(EntityPlayer.class, new ModelHumanoid(Image.getImageFromName("player")));
		entityModelMap.put(EntityZombie.class, new ModelZombie(Image.getImageFromName("zombie")));
		entityModelMap.put(EntityButton.class, new ModelButton(Image.getImageFromName("button")));
	}
	
	public Quad getQuad(int i) {
		return quads.get(i);
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
	
	public void updateAnimationState(Entity e) {
		if (anim != null)
			anim.updateState(e);
	}
	
	public boolean hasAnimation() {
		return anim != null;
	}
	
	public abstract void render(Entity e);
	 
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
	
	public void setCurrentAnimation(Animation a) {
		a.init(this);
		anim = a;
	}
	
	private void renderQuad(Quad q) {
		q.render();
	}
	
	public void animationStop() {
		anim = null;
	}
}