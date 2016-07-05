package com.dazkins.triad.gfx;

import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.util.HashMap;

import org.lwjgl.opengl.GL11;

import com.dazkins.triad.game.gui.renderformat.RenderFormatManager;
import com.dazkins.triad.game.gui.renderformat.TextRenderFormat;
import com.dazkins.triad.util.TriadLogger;

public class TTF
{
	public static final int LETTER_HEIGHT = 32;
	
	public static TTF main;
//	public static TTF mainBold;
//	public static TTF mainItalic;
	
	private Font font;
	
	private HashMap<Character, BufferObject> charMap = new HashMap<Character, BufferObject>();
	private HashMap<Character, Integer> charWidth = new HashMap<Character, Integer>();
	
	private float loadSize = 128f;
	
	public static void init()
	{
		String mainFont = "Gabriela-Regular";
		main = new TTF("/font/" + mainFont + ".ttf", Font.PLAIN);
//		mainBold = new TTF("/font/" + mainFont + ".ttf", Font.BOLD);
//		mainItalic = new TTF("/font/" + mainFont + ".ttf", Font.ITALIC);
	}
	
	public TTF(String path, int style)
	{
		try
		{
			font = Font.createFont(Font.TRUETYPE_FONT, TTF.class.getResourceAsStream(path)).deriveFont(style, loadSize);
		} catch (Exception e) 
		{
			TriadLogger.log("Could not load font: " + path, true);
		}
		
		for (int i = 0; i < 256; i++)
		{
			generateCharacterBO((char) i);
		}
	}
	
	public int getStringLength(String p)
	{
		int r = 0;
		for (char c : p.toCharArray())
		{
			r += charWidth.get(c);
		}
		return r;
	}
	
	private void renderTTFString(String s, float x, float y, float z, float scale)
	{
		TextRenderFormat format = RenderFormatManager.TEXT;
		
		float r = format.getColor().getDR();
		float g = format.getColor().getDG();
		float b = format.getColor().getDB();
		
		GL11.glColor4f(r, g, b, 1.0f);
		
		int i = 0;
		for (char c : s.toCharArray())
		{
			BufferObject bo = charMap.get(c);
			if (bo != null)
			{
				GL11.glPushMatrix();
				GL11.glTranslatef(x + i, y, z + i * 0.0001f);
				GL11.glScalef(scale, scale, 1.0f);
				bo.render();
				GL11.glPopMatrix();
				i += charWidth.get(c) * scale;
			} else 
			{
				TriadLogger.log("No character registered for: " + c, true);
			}
		}
		
		GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
	}
	
	private void generateCharacterBO(char c)
	{
		//Temp image to get height and width values
		BufferedImage tmpImage = new BufferedImage(1, 1, BufferedImage.TYPE_INT_RGB);
		Graphics2D gTmp = (Graphics2D) tmpImage.getGraphics();
		gTmp.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		gTmp.setFont(font);
		FontMetrics metrics = gTmp.getFontMetrics();
		
		int cWidth = metrics.stringWidth(c + "");
		int cHeight = metrics.getHeight();
		
		int pad = 20;
		
		if (cWidth > 0)
		{
			BufferedImage img = new BufferedImage(cWidth + pad * 2, cHeight, BufferedImage.TYPE_INT_ARGB);
			Graphics2D g = (Graphics2D) img.getGraphics();
			
			g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
			g.setFont(font);
			
			g.setColor(java.awt.Color.WHITE);
			g.drawString(c + "", pad, metrics.getAscent());
			
			Image imgObj = new Image(img);
			
			float ratio = (float) cWidth / (float) cHeight;
			int scaleWidth = (int) (ratio * LETTER_HEIGHT);
			
			float padRatio = (float) (pad * 2) / (float) cHeight;
			float scalePad = padRatio * LETTER_HEIGHT;
			
			BufferObject bo = new BufferObject(36);
			bo.resetData();
			imgObj.renderSprite(bo, -scalePad / 2.0f, 0, scaleWidth + scalePad, LETTER_HEIGHT, 0, 0, cWidth + pad * 2, cHeight, 0.0f, 0.0f);
			bo.compile();
			
			charMap.put(c, bo);
			charWidth.put(c, scaleWidth);
		}
	}
	
	public static void renderString(String s, float x, float y, float z, float scale)
	{
		TTF font = RenderFormatManager.TEXT.getFont();
		
		if (font != null)
			font.renderTTFString(s, x, y, z, scale);
		else
			TriadLogger.log("No font assigned!", true);
	}
}