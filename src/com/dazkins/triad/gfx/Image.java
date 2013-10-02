package com.dazkins.triad.gfx;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.ByteBuffer;

import javax.imageio.ImageIO;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;

public class Image {
	public static Image spriteSheet;
	public static Image iconSheet;
	public static Image fontSheet;

	private BufferedImage img;
	private int width, height;
	public int texID;
	
	public BufferedImage getRawImage() {
		return img;
	}

	public int getWidth() {
		return width;
	}
	
	public int getHeight() {
		return height;
	}
	
	public static boolean init() {
		try {
			spriteSheet = new Image("/art/spriteSheet.png");
			iconSheet = new Image("/art/iconSheet.png");
			fontSheet = new Image("/art/font.png");
		} catch (IOException e) {
			return false;
		}
		return true;
	}
	
	public void bindGLTexture() {
		if (GL11.glGetInteger(GL11.GL_TEXTURE_BINDING_2D) != texID) {
			GL11.glBindTexture(GL11.GL_TEXTURE_2D, texID);
		}
	}

	public Image(String path) throws IOException {
		loadSpriteSheet(path);
	}
	
	public void renderSprite(BufferObject bo, float x, float y, float w, float h, int tx, int ty, int tw, int th, float z, float b) {
		float offset = 0.01f;
		
		float tx0 = ((tx + offset) / (float) width);
		float ty0 = ((ty + offset) / (float) height);
		float tx1 = ((tx + tw - offset) / (float) width);
		float ty1 = ((ty + th - offset) / (float) height);
		
		bo.bindImage(this);
		bo.setDepth(z);
		bo.setRGB(b, b, b);
		bo.setUV(tx1, ty0);
		bo.addVertex(x + w, y + h);
		bo.setUV(tx0, ty0);
		bo.addVertex(x, y + h);
		bo.setUV(tx0, ty1);
		bo.addVertex(x, y);
		bo.setUV(tx1, ty1);
		bo.addVertex(x + w, y);
	}

	private void loadSpriteSheet(String path) throws IOException {
		img = ImageIO.read(Image.class.getResource(path));

		width = img.getWidth();
		height = img.getHeight();
		int[] pixels = img.getRGB(0, 0, width, height, null, 0, width);

		ByteBuffer b = BufferUtils.createByteBuffer((width * height) * 4);
		texID = GL11.glGenTextures();

		for (int i = 0; i < pixels.length; i++) {
			if (pixels[i] != 0xFFFF00FF) {
				byte aa = (byte) ((pixels[i] >> 24) & 0xFF);
				byte rr = (byte) ((pixels[i] >> 16) & 0xFF);
				byte gg = (byte) ((pixels[i] >> 8) & 0xFF);
				byte bb = (byte) ((pixels[i]) & 0xFF);

				b.put(rr);
				b.put(gg);
				b.put(bb);
				b.put(aa);
			} else {
				b.put((byte) 0);
				b.put((byte) 0);
				b.put((byte) 0);
				b.put((byte) 0);
			}
		}

		b.flip();
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, texID);

		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_NEAREST);
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_NEAREST);
		GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL11.GL_RGBA, width, height, 0, GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, b);

		GL11.glBindTexture(GL11.GL_TEXTURE_2D, 0);
	}
}