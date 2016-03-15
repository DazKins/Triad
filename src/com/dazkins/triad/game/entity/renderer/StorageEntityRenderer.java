package com.dazkins.triad.game.entity.renderer;

import com.dazkins.triad.game.entity.StorageEntityID;
import com.dazkins.triad.util.TriadLogger;

@SuppressWarnings("unchecked")
public class StorageEntityRenderer
{
	private static Class<? extends EntityRenderer>[] renderers = new Class[20];

	static
	{
		renderers[StorageEntityID.PLAYER] = EntityRendererPlayer.class;
		renderers[StorageEntityID.TREE] = EntityRendererTree.class;
		renderers[StorageEntityID.PIG] = EntityRendererPig.class;
		renderers[StorageEntityID.FLOWER] = EntityRendererFlower.class;
		renderers[StorageEntityID.SAND_GRASS] = EntityRendererSandGrass.class;
		renderers[StorageEntityID.ZOMBIE] = EntityRendererZombie.class;
		renderers[StorageEntityID.CHEST] = EntityRendererChest.class;
	}

	public static EntityRenderer recieveRenderer(int id)
	{
		try
		{
			return (EntityRenderer) renderers[id].newInstance();
		} catch (Exception e)
		{
			TriadLogger.log("No entity renderer for id : " + id, true);
			return null;
		}
	}
}