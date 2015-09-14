package com.dazkins.triad.game.entity.renderer;

import com.dazkins.triad.game.entity.EntityIDStorage;

@SuppressWarnings("unchecked")

public class StorageEntityRenderer {
	private static Class<? extends EntityRenderer>[] renderers = new Class[10];
	
	static {
		renderers[EntityIDStorage.PLAYER] = EntityRendererPlayer.class;
		renderers[EntityIDStorage.TREE] = EntityRendererTree.class;
		renderers[EntityIDStorage.PIG] = EntityRendererPig.class;
	}
	
	public static EntityRenderer recieveRenderer(int id) {
		try {
			return (EntityRenderer) renderers[id].newInstance();
		} catch (Exception e) {
			return null;
		}
	}
}