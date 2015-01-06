package com.dazkins.triad.math;

import java.awt.Image;
import java.awt.image.BufferedImage;

import javax.swing.ImageIcon;
import javax.swing.JOptionPane;

public class NoiseMap {
	private float rGrads[][];
	
	public static void main(String args[]) {
		NoiseMap n = new NoiseMap();
		JOptionPane.showMessageDialog(null, null, "Noise", JOptionPane.YES_NO_OPTION, new ImageIcon(n.toImg().getScaledInstance(256, 256, Image.SCALE_AREA_AVERAGING)));
	}
	
	public BufferedImage toImg() {
		int pixels[] = new int[256 * 256];
		for (int x = 0; x < 256; x++) {
			for (int y = 0; y < 256; y++) {
				pixels[x + y * 256] = (int) ((this.sample(x / 64.0f, y / 64.0f) + 1) / 2.0f * 100);
			}
		}
		
		BufferedImage img = new BufferedImage(256, 256, BufferedImage.TYPE_INT_RGB);
		img.setRGB(0, 0, 256, 256, pixels, 256, 0);
		return img;
	}
	
	public NoiseMap() {
		rGrads = new float[256 * 256][];
		
		for (int i = 0; i < rGrads.length; i++) {
			rGrads[i] = new float[2];
			float xx = (float) Math.random() * 2 - 1;
			float yy = (float) Math.random() * 2 - 1;
			float mag = (float) Math.sqrt(xx * xx + yy * yy);
			xx /= mag;
			yy /= mag;
			rGrads[i][0] = xx;
			rGrads[i][1] = yy;
		}
	}
	
	private float dot(float x0, float y0, float x1, float y1) {
		return x0 * x1 + y0 * y1;
	}
	
	private float getVX(int x, int y) {
		return rGrads[x + y * 256][0];
	}
	
	private float getVY(int x, int y) {
		return rGrads[x + y * 256][1];
	}
	
	public float sample(float x, float y) {
		int gx0 = (int) Math.floor(x) & 255;
		int gy0 = (int) Math.floor(y) & 255;
		int gx1 = gx0 + 1;
		int gy1 = gy0 + 1;
		
		//Bottom left
		float dx0 = gx0 - x;
		float dy0 = gy0 - y;
		
		//Top left
		float dx1 = gx0 - x;
		float dy1 = gy1 - y;
		
		//Top right
		float dx2 = gx1 - x;
		float dy2 = gy1 - y;
		
		//Botom right
		float dx3 = gx1 - x;
		float dy3 = gy0 - y;
		
		float w0 = dot(dx0, dy0, getVX(gx0, gy0), getVY(gx0, gy0));
		float w1 = dot(dx1, dy1, getVX(gx0, gy1), getVY(gx0, gy1));
		float w2 = dot(dx2, dy2, getVX(gx1, gy1), getVY(gx1, gy1));
		float w3 = dot(dx3, dy3, getVX(gx1, gy0), getVY(gx1, gy0));
		
//		System.out.println(w0 + " " + w1);
		
		float av0 = (w0 + w1) / 2.0f;
		float av1 = (w2 + w3) / 2.0f;
		float av = (av0 + av1) / 2.0f;
		
		return av;
	}
}