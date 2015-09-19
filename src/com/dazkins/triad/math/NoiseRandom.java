package com.dazkins.triad.math;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class NoiseRandom
{
	private Random r = new Random();

	private Map<Integer, Map<Integer, Float[]>> map;

	public NoiseRandom()
	{
		map = new HashMap<Integer, Map<Integer, Float[]>>();
	}

	public synchronized Float[] generateValue(int x, int y)
	{
		if (!map.containsKey(x))
		{
			map.put(x, new HashMap<Integer, Float[]>());
		}

		Map<Integer, Float[]> m = map.get(x);

		if (m == null)
			map.put(x, new HashMap<Integer, Float[]>());

		m = map.get(x);

		if (!m.containsKey(y))
		{
			float xx = r.nextFloat() * 2 - 1;
			float yy = r.nextFloat() * 2 - 1;
			float mag = (float) Math.sqrt(xx * xx + yy * yy);
			xx /= mag;
			yy /= mag;
			Float v[] = new Float[2];
			v[0] = xx;
			v[1] = yy;
			m.put(y, v);
			return v;
		}

		return map.get(x).get(y);
	}
}