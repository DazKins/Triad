package com.dazkins.triad.networking.client;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

import com.dazkins.triad.game.entity.StorageEntityID;
import com.dazkins.triad.game.entity.renderer.EntityRenderer;
import com.dazkins.triad.game.entity.renderer.EntityRendererPlayer;
import com.dazkins.triad.game.entity.renderer.StorageEntityRenderer;
import com.dazkins.triad.game.world.IWorldAccess;
import com.dazkins.triad.gfx.Camera;
import com.dazkins.triad.util.TriadLogger;

public class ClientEntityManager
{
	private ClientWorldManager clientWorldManager;
	
	private Map<Integer, EntityRenderer> renderers = new HashMap<Integer, EntityRenderer>();
	private Map<Integer, String> storedNameUpdates = new HashMap<Integer, String>();
	private ArrayList<Integer> loadedEntities = new ArrayList<Integer>();

	private IWorldAccess world;

	public ClientEntityManager(ClientWorldManager c)
	{
		clientWorldManager = c;
	}
	
	public void updatePlayerRenderer(float x, float y, int f)
	{
		updateRenderer(x, y, clientWorldManager.getMyPlayerID(), StorageEntityID.PLAYER, f);
	}
	
	public void initRenderer(IWorldAccess wo, int gID, int tID)
	{
		if (renderers.containsKey(gID))
		{
			TriadLogger.log("Entity renderer has already been registered", true);
			return;
		}

		world = wo;

		EntityRenderer r = StorageEntityRenderer.recieveRenderer(tID);

		if (r != null)
			r.setWorld(world);

		renderers.put(gID, r);

		loadedEntities.add(gID);
	}

	public void updateRenderer(float x, float y, int gID, int tID, int facing)
	{
		EntityRenderer r = renderers.get(gID);
		if (r != null)
		{
			r.setX(x);
			r.setY(y);
			r.setFacing(facing);
		}
	}
	
	public void removeRenderer(int gID)
	{
		loadedEntities.remove((Integer) gID);
		renderers.remove(gID);
	}

	public static SortByY ySorter = new SortByY();

	public static class SortByY implements Comparator<EntityRenderer>
	{
		public int compare(EntityRenderer o1, EntityRenderer o2)
		{
			return (o1.getY() > o2.getY()) ? -1 : (o1.getY() < o2.getY()) ? 1 : 0;
		}
	}
	
	public void handleAnimationUpdate(AnimationUpdate a)
	{
		int eID = a.getEntityGID();
		int aID = a.getAnimID();
		int index = a.getIndex();
		boolean overwrite = a.getOverwrite();
		float speed = a.getAnimSpeed();
		EntityRenderer e = renderers.get(eID);
		if (e != null)
			e.addAnimation(aID, index, overwrite, speed);
	}
	
	public void handlePlayerNameUpdate(int gID, String n)
	{
		EntityRenderer er = renderers.get(gID);
		if (er != null && er instanceof EntityRendererPlayer)
		{
			EntityRendererPlayer ep = (EntityRendererPlayer) er;
			ep.setName(n);
		} else 
		{
			storedNameUpdates.put(gID, n);
		}
	}
	
	public void tick()
	{
		for (int i : loadedEntities)
		{
			EntityRenderer e = renderers.get(i);
			//TODO investigate null instances of this
			if (e != null)
				e.tick();
		}

		for (Map.Entry<Integer, String> mapE : storedNameUpdates.entrySet())
		{
			EntityRenderer er = renderers.get(mapE.getKey());
			if (er != null)
			{
				EntityRendererPlayer ep = (EntityRendererPlayer) er;
				ep.setName(mapE.getValue());
			}
		}
	}

	public void render(Camera cam)
	{
		ArrayList<EntityRenderer> render = new ArrayList<EntityRenderer>();
		for (int i : loadedEntities)
		{
			EntityRenderer r = renderers.get(i);
			if (r != null)
				render.add(r);
		}
		render.sort(ySorter);
		for (EntityRenderer r : render)
		{
			r.render(cam);
		}
	}

	public boolean hasAlreadyEntity(int gID)
	{
		return loadedEntities.contains(gID);
	}
}