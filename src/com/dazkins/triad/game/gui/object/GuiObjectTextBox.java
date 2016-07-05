package com.dazkins.triad.game.gui.object;

import java.util.ArrayList;

import com.dazkins.triad.game.gui.Gui;
import com.dazkins.triad.game.gui.renderformat.RenderFormatManager;
import com.dazkins.triad.gfx.OpenGLHelper;
import com.dazkins.triad.gfx.TTF;
import com.dazkins.triad.math.AABB;

public class GuiObjectTextBox extends GuiObject
{
	private GuiObjectBox box;
	
	private String rawMsg;
	private ArrayList<String> msg;
	
	private float x;
	private float y;
	
	private float width;
	
	public GuiObjectTextBox(Gui g, String m, float x, float y, float maxWidth, int layer) 
	{
		super(g, layer);
		
		this.x = x;
		this.y = y;
		
		rawMsg = m;
		
		this.width = maxWidth;

		setupBox();
		oldFont = RenderFormatManager.TEXT.getFont();
	}
	
	public AABB getBounds()
	{
		return box.getBounds();
	}
	
	private TTF oldFont;
	
	public void render()
	{
		if (RenderFormatManager.TEXT.getFont() != oldFont)
			setupBox();
		
		box.render();
		for (int i = 0; i < msg.size(); i++)
		{
//			FontRenderer.drawString(msg.get(i), x + 10, (y + i * 16) - 5, layer * 0.001f + 0.0001f, 1.0f);
			float strLen = RenderFormatManager.TEXT.getFont().getStringLength(msg.get(i));
			float move = (width - strLen) / 2.0f;
			TTF.renderString(msg.get(i), x + 5 + move, y + 2 + i * TTF.LETTER_HEIGHT, layer * 0.001f + 0.0001f, 1.0f);
		}
		
		oldFont = RenderFormatManager.TEXT.getFont();
		
		OpenGLHelper.renderReferencePoint(x, y);
	}
	
	private void setupBox()
	{
		msg = new ArrayList<String>();
		
		String[] ms = rawMsg.split(" ");
		
		String curString = "";
		
		for (int i = 0; i < ms.length; i++)
		{
			String oldLine = curString;
			curString += ms[i] + " ";
			int curLineLength = RenderFormatManager.TEXT.getFont().getStringLength(curString);
			if (curLineLength > width)
			{
				msg.add(oldLine);
				System.out.println(oldLine);
				curString = ms[i] + " ";
			}
		}
		
		if (!curString.equals(""))
			msg.add(curString);
		
		int height = msg.size() * TTF.LETTER_HEIGHT;
		
		box = new GuiObjectBox(super.gui, x, y, width, height, layer);
	}
}