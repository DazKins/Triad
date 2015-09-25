package com.dazkins.triad.gfx.model.animation;

import com.dazkins.triad.game.entity.renderer.EntityRenderer;
import com.dazkins.triad.util.TriadLogger;

@SuppressWarnings("unchecked")
public class StorageAnimation
{
	private static Class<? extends Animation>[] animations = new Class[100];
	@SuppressWarnings("rawtypes")
	private static Class[] args = new Class[]{EntityRenderer.class, float.class};

	static
	{
		animations[StorageAnimationID.HUMANOID_WALKING] = AnimationHumanoidWalking.class;
		animations[StorageAnimationID.QUADRUPED_WALKING] = AnimationQuadrupedWalking.class;
	}

	public static Animation getAndInstantiateAnimation(int id, EntityRenderer er, float speed)
	{
		try
		{
			Class<? extends Animation> c = animations[id];
			return c.getDeclaredConstructor(args).newInstance(er, speed);
		} catch (Exception e)
		{
			TriadLogger.log(e.getMessage(), true);
			return null;
		}
	}
}