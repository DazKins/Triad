package com.dazkins.triad.math;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.util.Random;

import javax.swing.ImageIcon;
import javax.swing.JOptionPane;

public class PerlinNoise {
	private NoiseRandom rand;
	
	public PerlinNoise() {
		rand = new NoiseRandom();
	}
	
	private int fastfloor(double x) {
		return x>0 ? (int)x : (int)x-1;
	}
	
	private float dot(float x0, float y0, float x1, float y1) {
		return (x0 * x1) + (y0 * y1);
	}
	
	public float sample(float x, float y) {
		int gx0 = fastfloor(x);
		int gy0 = fastfloor(y);
		int gx1 = gx0 + 1;
		int gy1 = gy0 + 1;
		
		float dx0 = x - gx0;
		float dy0 = y - gy0;
		float dx1 = x - gx1;
		float dy1 = y - gy1;
		
		Float[] vals1 = rand.generateValue(gx0, gy1);
		Float[] vals0 = rand.generateValue(gx0, gy0);
		Float[] vals2 = rand.generateValue(gx1, gy0);
		Float[] vals3 = rand.generateValue(gx1, gy1);
		
		float w0 = dot(dx0, dy0, vals0[0], vals0[1]);
		float w1 = dot(dx0, dy1, vals1[0], vals1[1]);
		float w2 = dot(dx1, dy0, vals2[0], vals2[1]);
		float w3 = dot(dx1, dy1, vals3[0], vals3[1]);
		
		float sx = weigh(dx0);
		float sy = weigh(dy0);
		
		float a = lerp(sy, w0, w1);
		float b = lerp(sy, w2, w3);
		float h = lerp(sx, a, b);
		
		return h;
	}
	
	private float weigh(float x) {
		return 3 * (x * x) - 2 * (x * x * x);
	}
	
	private float lerp(float w, float a, float b) {
	      return a + w * (b - a);
	}
}