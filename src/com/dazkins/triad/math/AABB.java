package com.dazkins.triad.math;

public class AABB {
	private float x0, y0;
	private float x1, y1;
	
	public AABB(float x0, float y0, float x1, float y1) {
		this.x0 = x0;
		this.y0 = y0;
		this.x1 = x1;
		this.y1 = y1;
	}
	
	public void move(float xa, float ya) {
		x0 += xa;
		x1 += xa;
		y0 += ya;
		y1 += ya;
	}
	
	public AABB shifted(float xa, float ya) {
		return new AABB(x0 + xa, y0 + ya, x1 + xa, y1 + ya);
	}
}