package com.dazkins.triad.gfx.bitmap;

import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.util.Arrays;
import java.util.Random;

public class Bitmap {
	protected int width, height;
	protected int[] pixels;

	public Bitmap(int w, int h) {
		width = w;
		height = h;
		pixels = new int[w * h];
	}

	public Bitmap(int w, int h, int[] p) {
		width = w;
		height = h;
		pixels = Arrays.copyOf(p, p.length);
	}

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
	
	protected int getPixel(int x, int y) {
		return pixels[x + y * width];
	}
	
	protected void setPixel(int x, int y, int c) {
		pixels[x + y * width] = c;
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
				int c = b.getPixel(xPixel, yPixel);
				if (c != 0xFFFF00FF)
					setPixel(x, y, c);
			}
		}
	}
}