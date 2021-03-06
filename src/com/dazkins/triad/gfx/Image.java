package com.dazkins.triad.gfx;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;

import com.dazkins.triad.util.TriadLogger;

public class Image
{
	private static Map<String, Image> nameToImage;

	private BufferedImage img;
	private int width, height;
	private int texID;

	public BufferedImage getRawImage()
	{
		return img;
	}

	public int getWidth()
	{
		return width;
	}

	public int getHeight()
	{
		return height;
	}

	public static void processAndLoadFile(File f)
	{
		try
		{
			if (f.isFile())
			{
				if (f.getAbsolutePath().endsWith(".png"))
				{
					Image im = new Image(f);
					nameToImage.put(f.getName().replace(".png", ""), im);
				}
			} else if (f.isDirectory())
			{
				File[] files = f.listFiles();
				for (int i = 0; i < files.length; i++)
					processAndLoadFile(files[i]);
			}
		} catch (Exception e)
		{
			TriadLogger.log(e.getMessage(), true);
		}
	}

	public static boolean init()
	{
		nameToImage = new HashMap<String, Image>();
		try
		{
			String imageFolder = "/art";
			File imageDir = new File(Image.class.getResource(imageFolder).toURI());

			processAndLoadFile(imageDir);
		} catch (Exception e)
		{
			TriadLogger.log(e.getMessage(), true);
		}
		return true;
	}

	public static Image getImageFromName(String name)
	{
		Image i = nameToImage.get(name);
		if (i == null)
			i = getImageFromName("missing_texture");
		return i;
	}

	public void bindGLTexture()
	{
		if (GL11.glGetInteger(GL11.GL_TEXTURE_BINDING_2D) != texID)
		{
			GL11.glBindTexture(GL11.GL_TEXTURE_2D, texID);
		}
	}
	
	public Image(BufferedImage img)
	{
		loadImage(img);
	}

	public Image(File f) throws IOException 
	{
		loadImage(ImageIO.read(f));
	}

	private void loadImage(BufferedImage img)
	{
		this.img = img;
		
		width = img.getWidth();
		height = img.getHeight();
		int[] pixels = img.getRGB(0, 0, width, height, null, 0, width);

		ByteBuffer b = BufferUtils.createByteBuffer((width * height) * 4);
		texID = GL11.glGenTextures();

		for (int i = 0; i < pixels.length; i++)
		{
			if (pixels[i] != 0xFFFF00FF)
			{
				byte aa = (byte) ((pixels[i] >> 24) & 0xFF);
				byte rr = (byte) ((pixels[i] >> 16) & 0xFF);
				byte gg = (byte) ((pixels[i] >> 8) & 0xFF);
				byte bb = (byte) ((pixels[i]) & 0xFF);

				b.put(rr);
				b.put(gg);
				b.put(bb);
				b.put(aa);
			} else
			{
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
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_S, GL11.GL_CLAMP);
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_T, GL11.GL_CLAMP);
		GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL11.GL_RGBA, width, height, 0, GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, b);

		GL11.glBindTexture(GL11.GL_TEXTURE_2D, 0);
	}

	//TODO change parameters
	public void loadSpriteBufferObject(BufferObject bo, float x, float y, float w, float h, int tx, int ty, int tw, int th, float z, float b)
	{
		float offset = 0.0001f;

		float tx0 = (tx / (float) width) + offset;
		float ty0 = (ty / (float) height) + offset;
		float tx1 = ((tx + tw) / (float) width) - offset;
		float ty1 = ((ty + th) / (float) height) - offset;

		bo.getData().bindImage(this);
		
		//TODO get rid of this properly
//		bo.getData().setDepth(z);
//		if (b != 0)
//			bo.getData().setRGB(b, b, b);
		
		bo.getData().setUV(tx1, ty0).setXY(x + w, y + h).pushVertex();
		bo.getData().setUV(tx0, ty0).setXY(x, y + h).pushVertex();
		bo.getData().setUV(tx0, ty1).setXY(x, y).pushVertex();
		bo.getData().setUV(tx1, ty1).setXY(x + w, y).pushVertex();
		
//		bo.getData().setUV(tx0, ty0).setXY(x + w, y + h).pushVertex();
//		bo.getData().setUV(tx0, ty1).setXY(x, y + h).pushVertex();
//		bo.getData().setUV(tx1, ty0).setXY(x, y).pushVertex();
//		bo.getData().setUV(tx1, ty1).setXY(x + w, y).pushVertex();
	}
}