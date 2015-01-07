package com.dazkins.triad.math;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.util.Random;

import javax.swing.ImageIcon;
import javax.swing.JOptionPane;

public class NoiseMap {
	private float rGrads[][];
	
	public static void main(String args[]) {
		while (true) {
			NoiseMap n = new NoiseMap();
			JOptionPane.showMessageDialog(null, null, "Noise", JOptionPane.YES_NO_OPTION, new ImageIcon(n.toImg().getScaledInstance(256 * 2, 256 * 2, Image.SCALE_AREA_AVERAGING)));
		}
	}
	
	public BufferedImage toImg() {
		int pixels[] = new int[256 * 256];
		for (int x = 0; x < 256; x++) {
			for (int y = 0; y < 256; y++) {
				float r = sample(x / 64.0f, y / 64.0f);
				
				if (r < 0)
					pixels[x + y * 256] = 0x0000FF;
				else
					pixels[x + y * 256] = 0x00FF00;
			}
		}
		
		BufferedImage img = new BufferedImage(256, 256, BufferedImage.TYPE_INT_RGB);
		img.setRGB(0, 0, 256, 256, pixels, 0, 256);
		return img;
	}
	
	public NoiseMap() {
		rGrads = new float[256 * 256][];
		Random r = new Random();
		
		for (int i = 0; i < rGrads.length; i++) {
			rGrads[i] = new float[2];
			float xx = r.nextFloat() * 2 - 1;
			float yy = r.nextFloat() * 2 - 1;
			float mag = (float) Math.sqrt(xx * xx + yy * yy);
			xx /= mag;
			yy /= mag;
			rGrads[i][0] = xx;
			rGrads[i][1] = yy;
//			int rand = r.nextInt(4);
//			if (rand == 0) {
//				rGrads[i][0] = 0.0f;
//				rGrads[i][1] = 1.0f;
//			} else if (rand == 1) {
//				rGrads[i][0] = 0.0f;
//				rGrads[i][1] = -1.0f;
//			} else if (rand == 2) {
//				rGrads[i][0] = 1.0f;
//				rGrads[i][1] = 0.0f;
//			} else {
//				rGrads[i][0] = -1.0f;
//				rGrads[i][1] = 0.0f;
//			}
		}
	}
	
	private float dot(float x0, float y0, float x1, float y1) {
		return (x0 * x1) + (y0 * y1);
	}
	
	private float getVX(int x, int y) {
		if (x < 0 || y < 0 || x >= 256 || y >= 256)
			return 0;
		return rGrads[x + y * 256][0];
	}
	
	private float getVY(int x, int y) {	
		if (x < 0 || y < 0 || x >= 256 || y >= 256)
			return 0;
		return rGrads[x + y * 256][1];
	}
	
	public float sample(float x, float y) {
		int gx0 = (int) Math.floor(x);
		int gy0 = (int) Math.floor(y);
		int gx1 = gx0 + 1;
		int gy1 = gy0 + 1;
		
		float dx0 = x - gx0;
		float dy0 = y - gy0;
		
		float dx1 = x - gx1;
		float dy1 = y - gy1;
		
		float w0 = dot(dx0, dy0, getVX(gx0, gy0), getVY(gx0, gy0));
		float w1 = dot(dx0, dy1, getVX(gx0, gy1), getVY(gx0, gy1));
		float w2 = dot(dx1, dy1, getVX(gx1, gy1), getVY(gx1, gy1));
		float w3 = dot(dx1, dy0, getVX(gx1, gy0), getVY(gx1, gy0));
		
		float sx = weigh(dx0);
		float sy = weigh(dy0);
		
		float a = lerp(sy, w0, w1);
		float b = lerp(sy, w3, w2);
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