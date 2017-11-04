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
import com.dazkins.triad.util.StringUtil;
import com.dazkins.triad.util.TriadLogger;

public class TTF
{
	private static final int LETTER_HEIGHT = 32;
	private static final float LETTER_SPACING = 0.5f;
	private static final float SPACE_SIZE = 2.0f;
	
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
	
	public static float getLetterHeight()
	{
		float size = RenderFormatManager.TEXT.getSize();
		return (float) LETTER_HEIGHT * size;
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
	
	public static String getReducedString(String p)
	{
		String red = "";
		
		p = p.replace(" ", " " + StringUtil.RANDOM_SPACE_SEPERATOR);
		String[] split = p.split(StringUtil.RANDOM_SPACE_SEPERATOR);
		for (String s : split)
		{
			if (!s.equals(""))
			{
				if (s.toCharArray()[0] != '#')
				{
					red += s;
				}
			}
		}
		
		return red;
	}
	
	public float getStringLength(String p)
	{
		String red = getReducedString(p);
		
		float s = RenderFormatManager.TEXT.getSize();
		
		int r = 0;
		for (char c : red.toCharArray())
		{
			if (c == ' ')
				r += s * SPACE_SIZE;
			r += s * charWidth.get(c) + LETTER_SPACING;
		}
		return r;
	}
	
	//TODO update text
	private void renderTTFString(String s, float x, float y, float z)
	{
		TextRenderFormat format = RenderFormatManager.TEXT;
		
		float r = format.getColor().getDR();
		float g = format.getColor().getDG();
		float b = format.getColor().getDB();
		
		GL11.glColor4f(r, g, b, 1.0f);
		
		float scale = format.getSize();
		
		float i = 0;
		for (char c : s.toCharArray())
		{
			if (c == ' ')
			{
				i += SPACE_SIZE * scale;
				continue;
			}
			BufferObject bo = charMap.get(c);
			if (bo != null)
			{
				GL11.glPushMatrix();
				GL11.glTranslatef(x + i, y, z + i * 0.0001f);
				GL11.glScalef(scale, scale, 1.0f);
				bo.render();
				GL11.glPopMatrix();
				i += (charWidth.get(c) + LETTER_SPACING) * scale;
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
			imgObj.loadSpriteBufferObject(bo, -scalePad / 2.0f, 0, scaleWidth + scalePad, LETTER_HEIGHT, 0, 0, cWidth + pad * 2, cHeight, 0.0f, 0.0f);
			bo.compile();
			
			charMap.put(c, bo);
			charWidth.put(c, scaleWidth);
		}
	}
	
	public static void renderStringWithFormating(String s, float x, float y, float z)
	{
		TTF font = RenderFormatManager.TEXT.getFont();
		
		if (font == null)
		{
			TriadLogger.log("No font assigned!", true);
			return;
		}
		
		s = s.replace(" ", " " + StringUtil.RANDOM_SPACE_SEPERATOR);
		String[] split = s.split(StringUtil.RANDOM_SPACE_SEPERATOR);
		
		int renderOffset = 0;
		int layerCount = 0;
		
		for (int i = 0; i < split.length; i++)
		{
			String cur = split[i];
			if (cur.length() > 0)
			{
				char[] chars = cur.toCharArray();
				char start = chars[0];
				if (start == '#')
				{
					String r = (chars[1] + "") + (chars[2] + "");
					String g = (chars[3] + "") + (chars[4] + "");
					String b = (chars[5] + "") + (chars[6] + "");
					
					int ir = Integer.parseInt(r, 16);
					int ig = Integer.parseInt(g, 16);
					int ib = Integer.parseInt(b, 16);
					
					RenderFormatManager.TEXT.setColour(new Color(ir, ig, ib));
				} else 
				{
					renderString(cur, x + renderOffset, y, z + layerCount * 0.01f);
					renderOffset += font.getStringLength(cur);
					layerCount++;
				}
			}
		}
	}
	
	public static void renderString(String s, float x, float y, float z)
	{
		TTF font = RenderFormatManager.TEXT.getFont();
		
		if (font != null)
			font.renderTTFString(s, x, y, z);
		else
			TriadLogger.log("No font assigned!", true);
	}
}