package com.dazkins.triad.math;

import java.awt.image.BufferedImage;

import javax.swing.ImageIcon;
import javax.swing.JOptionPane;

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
	
	//DEBUGGING PURPOSES ONLY
	public void displaySample() {
		int width = 256;
		int height = 256;
		
		BufferedImage img = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		
		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {
				float s = sample(x, y) * 2.0f;
				int col = 0;
				if (s > 0.3f)
					col = 0xFF0000;
				else if (s > 0.05f)
					col = 0x00FF00;
				else if (s > -0.05f)
					col = 0x0000FF;
				else if (s > -0.3f)
					col = 0x00FFFF;
				img.setRGB(x, y, col);
			}
		}
		
		JOptionPane.showMessageDialog(null, null, "Noise Sample", JOptionPane.YES_NO_OPTION, new ImageIcon(img.getScaledInstance(width * 2, height * 2, BufferedImage.SCALE_AREA_AVERAGING)));
	}
}