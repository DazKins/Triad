package com.dazkins.triad.math;

public class NoiseMap {
	private float rGrads[][];
	
	public NoiseMap() {
		rGrads = new float[256 * 256][];
		
		for (int i = 0; i < rGrads.length; i++) {
			rGrads[i] = new float[2];
			
			rGrads[i] = (float) Math.random();
		}
	}
	
	public float sample(float x, float y) {
		int gx0 = (int) x & 255;
		int gy0 = (int) y & 255;
		int gx1 = gx0 + 1;
		int gy1 = gy0 + 1;
		
		
	}
}