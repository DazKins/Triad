package com.dazkins.triad.gfx;

import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

import com.dazkins.triad.gfx.bitmap.Bitmap;

public class SpriteSheet {
	private Bitmap[][] sprites;
	
	public SpriteSheet(String path, int tw, int th) throws IOException {
		loadSpriteSheet(path, tw, th);
	}
	
	private void loadSpriteSheet(String path, int tw, int th) throws IOException {
		BufferedImage img;
			
		img = ImageIO.read(SpriteSheet.class.getResource(path));
		
		int width = img.getWidth();
		int height = img.getHeight();
		
		int xTiles = width / tw;
		int yTiles = height / th;

		Bitmap[][] rValue = new Bitmap[xTiles][yTiles];
		
		for (int x = 0; x < xTiles; x++) {
			for (int y = 0; y < yTiles; y++) {
				rValue[x][y] = new Bitmap(tw, th, img.getRGB(x * tw, y * th, tw, th, null, 0, tw));
			}
		}
		
		sprites = rValue;
	}
	
	public void renderSprite(int sx ,int sy, Bitmap b, int xp, int yp) {
		b.blit(sprites[sx][sy], xp, yp);
	}
}