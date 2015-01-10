package com.dazkins.triad.math;

public class NoiseMap {
	private PerlinNoise octs[];
	
	private int octaves;
	private float persistence;
	private float scale;
	
	public NoiseMap(float persistence, int octaves, float scale) {
		this.octaves = octaves;
		this.persistence = persistence;
		this.scale = scale;
		
		octs = new PerlinNoise[octaves];
		
		for (int i = 0; i < octaves; i++) {
			octs[i] = new PerlinNoise();
		}
	}
	
	public float sample(float x, float y) {
		float r = 0;
		
		for (int i = 0; i < octaves; i++) {
			int index = i + 1;
			float xx = x * index * index * scale;
			float yy = y * index * index * scale;
			float val = octs[i].sample(xx, yy);
			float m = (float) Math.pow(persistence, i);
			r += val * m;
		}
		
		float d = 1 + (float) (1 - Math.pow(persistence, octaves) / (1 - persistence));
		r /= d;
		
		return r;
	}
}