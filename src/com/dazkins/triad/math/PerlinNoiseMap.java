package com.dazkins.triad.math;

import java.awt.image.BufferedImage;

import javax.swing.ImageIcon;
import javax.swing.JOptionPane;

import org.lwjgl.system.linux.SysIOctl;

public class PerlinNoiseMap {
	private PerlinNoise perlin;
	
	private int octaves;
	private float persistence;
	private float scale;
	
	private double nCorrection;
	private double pCorrection;
	
	public PerlinNoiseMap(float persistence, int octaves, float scale) {
		this.octaves = octaves;
		this.persistence = persistence;
		this.scale = scale;
		
		perlin = new PerlinNoise();
		
		nCorrection = 1;
		pCorrection = 1;
	}
	
	//Brute force method, can take time
	public void checkForConsistencyCorrection() {
		int width = 512;
		int height = 512;
		
		double max = -1;
		double min = 1;
		
		for (int x = -width; x < width; x++) {
			for (int y = -width; y < height; y++) {
				float s = sample(x, y);
				if (s > max)
					max = s;
				if (s < min)
					min = s;
			}
		}
		
		nCorrection = 1 / -min;
		pCorrection = 1 / max;
	}
	
	public float sample(float x, float y) {
		float total = 0;
		float freq = scale;
		float maxVal = 0;
		float amp = 1;
		
		for (int i = 0; i < octaves; i++) {
			float xx = x * freq;
			float yy = y * freq;
			
			float val = perlin.sample(xx, yy) * amp;
			
			total += val;
			
			maxVal += amp;
			
			amp *= persistence;
			freq *= 2;
		}
		
		total /= maxVal;
		
		if (total < 0)
			total *= nCorrection;
		else
			total *= pCorrection;
		
		if (total > 1.0f)
			total = 1.0f;
		if (total < -1.0f)
			total = -1.0f;
		
		return total;
	}
	
	//DEBUGGING PURPOSES ONLY
	public void displaySample(float zx, float zy) {
		int width = 1024;
		int height = 1024;
		
		BufferedImage img = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		
		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {
				float s = (sample(x / zx, y / zy) + 1.0f) / 2.0f;
				int col = (int) (s * 255) << 16 | (int) (s * 255) << 8 | (int) (s * 255);
				img.setRGB(x, y, col);
			}
		}
		
		JOptionPane.showMessageDialog(null, null, "Noise Sample", JOptionPane.YES_NO_OPTION, new ImageIcon(img.getScaledInstance(width, height, BufferedImage.SCALE_AREA_AVERAGING)));
	}
}