package com.dazkins.triad.networking.client;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

import com.dazkins.triad.game.entity.StorageEntityID;
import com.dazkins.triad.game.entity.renderer.EntityRenderer;
import com.dazkins.triad.game.entity.renderer.StorageEntityRenderer;
import com.dazkins.triad.game.entity.shell.EntityShell;
import com.dazkins.triad.game.inventory.Inventory;
import com.dazkins.triad.game.inventory.item.ItemStack;
import com.dazkins.triad.game.world.IWorldAccess;
import com.dazkins.triad.gfx.Camera;
import com.dazkins.triad.networking.client.update.ClientUpdateAnimation;
import com.dazkins.triad.util.TriadLogger;

public class ClientEntityManager
{
	private ClientWorldManager clientWorldManager;
	
	private Map<Integer, EntityShell> entityShells = new HashMap<Integer, EntityShell>();
	
	private Map<Integer, String> storedNameUpdates = new HashMap<Integer, String>();
	private Map<Integer, Inventory> storedInventoryUpdates = new HashMap<Integer, Inventory>();
	
	private ArrayList<Integer> loadedEntities = new ArrayList<Integer>();

	private IWorldAccess world;

	public ClientEntityManager(ClientWorldManager c)
	{
		clientWorldManager = c;
	}
	
	public void updatePlayerEntity(float x, float y, int f)
	{
		updateEntity(x, y, clientWorldManager.getMyPlayerID(), StorageEntityID.PLAYER, f);
	}
	
	public void initEntity(IWorldAccess wo, int gID, int tID)
	{
		if (entityShells.containsKey(gID))
		{
			TriadLogger.log("Entity renderer has already been registered", true);
			return;
		}

		world = wo;

		EntityShell s = new EntityShell(this, gID);
		
		EntityRenderer r = StorageEntityRenderer.recieveRenderer(tID);

		if (r != null)
			r.setWorld(world);
		
		s.setRenderer(r);

		entityShells.put(gID, s);

		loadedEntities.add(gID);
	}
	
	public ArrayList<EntityShell> getLoadedEntities()
	{
		ArrayList<EntityShell> es = new ArrayList<EntityShell>();
		
		for (int i : loadedEntities)
		{
			EntityShell e = entityShells.get(i);
			es.add(e);
		}
		
		return es;
	}
	
	public EntityShell getEntityShell(int id)
	{
		return entityShells.get(id);
	}
	
	public EntityShell getMyPlayerShell()
	{
		return entityShells.get(clientWorldManager.getMyPlayerID());
	}

	public void updateEntity(float x, float y, int gID, int tID, int facing)
	{
		EntityShell s = entityShells.get(gID);
		if (s != null)
		{
			s.setX(x);
			s.setY(y);
			s.setFacing(facing);
		}
	}
	
	public void removeEntity(int gID)
	{
		loadedEntities.remove((Integer) gID);
		entityShells.remove(gID);
	}

	public static SortByY ySorter = new SortByY();

	public static class SortByY implements Comparator<EntityRenderer>
	{
		public int compare(EntityRenderer o1, EntityRenderer o2)
		{
			return (o1.getY() > o2.getY()) ? -1 : (o1.getY() < o2.getY()) ? 1 : 0;
		}
	}
	
	public void handleAnimationUpdate(ClientUpdateAnimation a)
	{
		int eID = a.getEntityGID();
		int aID = a.getAnimID();
		int index = a.getIndex();
		boolean overwrite = a.getOverwrite();
		float speed = a.getAnimSpeed();
		EntityShell e = entityShells.get(eID);
		if (e != null)
			e.getRenderer().addAnimation(aID, index, overwrite, speed);
	}
	
	public void handleInteractionUpdate(int gID, int iID, boolean start)
	{
		EntityShell e = entityShells.get(gID);
		if (start)
		{
			e.setInteractingEntityID(iID);
		} else 
		{
			e.setInteractingEntityID(-1);
		}
	}
	
	public void handlePlayerNameUpdate(int gID, String n)
	{
		EntityShell es = entityShells.get(gID);
		if (es != null)
		{
			es.setName(n);
		} else 
		{
			storedNameUpdates.put(gID, n);
		}
	}
	
	public void handleInventoryUpdate(int gID, Inventory i)
	{
		EntityShell es = entityShells.get(gID);
		if (es != null)
		{
			es.setInventory(i);
			
			if (storedInventoryUpdates.containsKey(gID))
				storedInventoryUpdates.remove(gID);
		} else
		{
			if (!storedInventoryUpdates.containsKey(gID))
				storedInventoryUpdates.put(gID, i);
			else
				storedInventoryUpdates.replace(gID, i);
		}
	}
	
	public void tick()
	{
		for (int i : loadedEntities)
		{
			EntityShell e = entityShells.get(i);
			//TODO investigate null instances of this
			if (e != null)
				e.tick();
		}

		for (Map.Entry<Integer, String> mapE : storedNameUpdates.entrySet())
		{
			EntityShell es = entityShells.get(mapE.getKey());
			if (es != null)
			{
				es.setName(mapE.getValue());
			}
		}


		for (Map.Entry<Integer, Inventory> mapE : storedInventoryUpdates.entrySet())
		{
			int gID = mapE.getKey();
			EntityShell es = entityShells.get(gID);
			if (es != null)
			{
				handleInventoryUpdate(gID, mapE.getValue());
			}
		}
	}

	public void render(Camera cam)
	{
		ArrayList<EntityRenderer> render = new ArrayList<EntityRenderer>();
		for (int i : loadedEntities)
		{
			EntityShell s = entityShells.get(i);
			if (s != null)
			{
				EntityRenderer er = s.getRenderer();
				if (er != null)
					render.add(er);
			}
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