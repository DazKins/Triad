package com.dazkins.triad.gfx;

import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.util.Random;

public class Bitmap {
	private int width, height;
	private int[] pixels;

	public Bitmap(int w, int h, int c) {
		width = w;
		height = h;
		pixels = new int[w * h];
		for (int i = 0; i < pixels.length; i++) {
			pixels[i] = c;
		}
	}

	public Bitmap(BufferedImage img) {
		width = img.getWidth();
		height = img.getHeight();
		pixels = ((DataBufferInt) img.getRaster().getDataBuffer()).getData();
	}

	public void randomize() {
		Random r = new Random();
		for (int i = 0; i < pixels.length; i++) {
			pixels[i] = r.nextInt(0xFFFFFF);
		}
	}

	public void blit(Bitmap b, int xp, int yp) {
		int x0 = xp;
		int y0 = yp;
		int x1 = xp + b.width;
		int y1 = yp + b.height;

		if (x0 < 0)
			x0 = 0;
		if (y0 < 0)
			y0 = 0;
		if (x1 > width)
			x1 = width;
		if (y1 > height)
			y1 = height;
		
		for (int x = x0; x < x1; x++) {
			int xPixel = x - xp;
			for (int y = y0; y < y1; y++) {
				int yPixel = y - yp;
				pixels[x + y * width] = b.pixels[xPixel + yPixel * b.width];
			}
		}
	}
 }