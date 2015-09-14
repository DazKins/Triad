package com.dazkins.triad.networking.client;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

import com.dazkins.triad.game.entity.renderer.EntityRenderer;
import com.dazkins.triad.game.entity.renderer.EntityRendererPlayer;
import com.dazkins.triad.game.entity.renderer.StorageEntityRenderer;
import com.dazkins.triad.game.world.IWorldAccess;
import com.dazkins.triad.gfx.Camera;

public class EntityRendererManager {
	private Map<Integer, EntityRenderer> renderers = new HashMap<Integer, EntityRenderer>();
	private ArrayList<Integer> entitiesToRender = new ArrayList<Integer>();
	
	private EntityRendererPlayer player;
	
	private IWorldAccess world;
	
	public void initRenderer(IWorldAccess wo, int gID, int tID) {
		if (renderers.containsKey(gID)) {
			System.err.println("Entity renderer has already been registered");
			return;
		}
		
		world = wo;
		
		EntityRenderer r = StorageEntityRenderer.recieveRenderer(tID);
		
		if (r != null)
			r.setWorld(world);
		
		renderers.put(gID, r);
		
		entitiesToRender.add(gID);
	}
	
	public void setPlayerEntityRenderer(EntityRendererPlayer p) {
		player = p;
	}
	
	public void updateRenderer(float x, float y, int gID, int tID) {
		EntityRenderer r = renderers.get(gID);
		if (r != null) {
			r.setX(x);
			r.setY(y);
		}
	}
	
	public static SortByY ySorter = new SortByY();
	
	public static class SortByY implements Comparator<EntityRenderer> {
		public int compare(EntityRenderer o1, EntityRenderer o2) {
			return (o1.getY() > o2.getY()) ? -1 : (o1.getY() < o2.getY()) ? 1 : 0;
		}
	}
	
	public void render(Camera cam) {
		ArrayList<EntityRenderer> render = new ArrayList<EntityRenderer>();
		for (int i : entitiesToRender) {
			EntityRenderer r = renderers.get(i);
			if (r != null)
				render.add(r);
		}
		render.add(player);
		render.sort(ySorter);
		for (EntityRenderer r : render) {
			r.render(cam);
		}
	}
	
	public boolean hasAlreadyEntity(int gID) {
		return entitiesToRender.contains(gID);
	}
}