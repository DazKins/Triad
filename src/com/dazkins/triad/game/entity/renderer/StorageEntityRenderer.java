package com.dazkins.triad.game.entity.renderer;

import com.dazkins.triad.game.entity.StorageEntityID;

@SuppressWarnings("unchecked")
public class StorageEntityRenderer
{
	private static Class<? extends EntityRenderer>[] renderers = new Class[10];

	static
	{
		renderers[StorageEntityID.PLAYER] = EntityRendererPlayer.class;
		renderers[StorageEntityID.TREE] = EntityRendererTree.class;
		renderers[StorageEntityID.PIG] = EntityRendererPig.class;
	}

	public static EntityRenderer recieveRenderer(int id)
	{
		try
		{
			return (EntityRenderer) renderers[id].newInstance();
		} catch (Exception e)
		{
			return null;
		}
	}
}