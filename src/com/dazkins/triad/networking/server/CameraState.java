package com.dazkins.triad.networking.server;

import com.dazkins.triad.math.AABB;

public class CameraState
{
	private AABB aabb;
	
	public CameraState(AABB b)
	{
		aabb = b;
	}
	
	public AABB getAABB()
	{
		return aabb;
	}
}