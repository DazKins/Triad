package com.dazkins.triad.gfx.model.animation;

import com.dazkins.triad.game.entity.renderer.EntityRenderer;

@SuppressWarnings("unchecked")
public class StorageAnimation
{
	private static Class<? extends Animation>[] animations = new Class[100];
	@SuppressWarnings("rawtypes")
	private static Class[] args = new Class[]{EntityRenderer.class};

	static
	{
		animations[StorageAnimationID.HUMANOID_WALKING] = AnimationHumanoidWalking.class;
		animations[StorageAnimationID.QUADRUPED_WALKING] = AnimationQuadrupedWalking.class;
	}

	public static Animation getAndInstantiateAnimation(int id, EntityRenderer er)
	{
		try
		{
			Class<? extends Animation> c = animations[id];
			return c.getDeclaredConstructor(args).newInstance(er);
		} catch (Exception e)
		{
			e.printStackTrace();
			return null;
		}
	}
}