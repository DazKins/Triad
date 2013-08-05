package com.dazkins.triad.gfx.model;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.lwjgl.opengl.GL11;

import com.dazkins.triad.game.entity.Entity;
import com.dazkins.triad.game.entity.mob.EntityPlayer;
import com.dazkins.triad.gfx.Image;

public class Model {
	public static Map<Class<? extends Entity>, Model> entityModelMap = new HashMap<Class<? extends Entity>, Model>();
	
	protected List quads;
	protected List quadSkips;
	
	private Image img;
	
	private float offsetX, offsetY;
	private float depth;
	
	public static void loadModels() {
		try {
			entityModelMap.put(EntityPlayer.class, new ModelHumanoid(new Image("/art/entities/player.png")));
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
	
	protected void addQuad(Quad q) {
		q.init(img);
		q.generate();
		quads.add(q);
	}
	
	public void skipRender(Quad quad) {
		quadSkips.add(quads.indexOf(quad));
	}
	
	public Model(Image i) {
		quads = new ArrayList();
		quadSkips = new ArrayList();
		img = i;
	}
	
	public void render() {
		GL11.glPushMatrix();
		GL11.glTranslatef(offsetX, offsetY, depth);
		for (int i = 0; i < quads.size(); i++) {
			if (!quadSkips.contains(i)) {
				Quad q = (Quad) quads.get(i);
				q.render();
				q.resetProperties();
			}
		}
		quadSkips.clear();
		GL11.glPopMatrix();
	}
}