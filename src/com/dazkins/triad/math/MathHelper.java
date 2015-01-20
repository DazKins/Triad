package com.dazkins.triad.math;

import com.dazkins.triad.game.world.Chunk;

public class MathHelper {
	//Treats negatives better
	public static int betterMod(int x, int m) {
		int r = 0;
		
		if (x < 0)
			r = (m - (Math.abs(x) % m));
		else
			r = x % m;
		
		if (r < 0)
			r = m - 1;
		if (r >= m)
			r = 0;
		
		return r;
	}
}