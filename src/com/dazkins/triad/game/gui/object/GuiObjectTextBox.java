package com.dazkins.triad.game.gui.object;

import java.util.ArrayList;

import org.omg.Messaging.SyncScopeHelper;

import com.dazkins.triad.game.gui.Gui;
import com.dazkins.triad.game.gui.renderformat.RenderFormatManager;
import com.dazkins.triad.game.gui.renderformat.TextAlign;
import com.dazkins.triad.gfx.OpenGLHelper;
import com.dazkins.triad.gfx.TTF;
import com.dazkins.triad.math.AABB;
import com.dazkins.triad.util.StringUtil;

public class GuiObjectTextBox extends GuiObjectBox
{
	private String rawMsg;
	private ArrayList<String> msg;
	private boolean showCursor;
	
	private int maxLineCount;
	
	public GuiObjectTextBox(Gui g) 
	{
		super(g);
	}
	
	public void setShowCursor(boolean b)
	{
		this.showCursor = b;
	}
	
	public void setMaxLineCount(int m)
	{
		maxLineCount = m;
	}
	
	public void setContent(String c)
	{
		this.rawMsg = c;
	}
	
	private TTF oldFont;
	
	public void render()
	{
		if (RenderFormatManager.TEXT.getFont() != oldFont)
			setupGraphics();
		
		super.render();
		for (int i = 0; i < msg.size(); i++)
		{
			String curMsg = msg.get(i);
			
			float strLen = RenderFormatManager.TEXT.getFont().getStringLength(curMsg);
			
			float move = 0;
			
			if (maxLineCount == 1)
			{
				if (strLen > super.width)
					move = super.width - strLen;
			}
			
			TextAlign a = RenderFormatManager.TEXT.getAlign();
			
			if (a == TextAlign.CENTRE)
				move = (super.width - strLen) / 2.0f;
			
			TTF.renderStringWithFormating(curMsg, x + move + 3, y + height + 3 - (i + 1) * TTF.getLetterHeight(), layer * 0.001f + 0.0001f);

			if (i == msg.size() - 1 && showCursor)
				TTF.renderStringWithFormating("#ff006a |", x + move + 3 + strLen, y + height + 3 - (i + 1) * TTF.getLetterHeight(), layer * 0.001f + 0.0001f);
		}
		
		oldFont = RenderFormatManager.TEXT.getFont();
		
//		OpenGLHelper.renderReferencePoint(x, y);
	}
	
	public String getContent()
	{
		return rawMsg;
	}
	
	public void setupGraphics()
	{
		if (maxLineCount == 1)
		{
			msg = new ArrayList<String>();
			msg.add(rawMsg);
			height = TTF.getLetterHeight();
		} else
		{
			msg = new ArrayList<String>();
			
			String rep = rawMsg.replace(" ", " " + StringUtil.RANDOM_SPACE_SEPERATOR);
			String[] ms = rep.split(StringUtil.RANDOM_SPACE_SEPERATOR);
			
			String curString = "";
			
			for (int i = 0; i < ms.length; i++)
			{
				if (maxLineCount > 0)
				{
					if (msg.size() >= maxLineCount)
					{
						break;
					}
				}
				
				String oldLine = curString;
				curString += ms[i];
				float curLineLength = RenderFormatManager.TEXT.getFont().getStringLength(curString);
				
				if (ms[i].replace(" ", "").equals("\\n"))
				{
					msg.add(oldLine);
					curString = "";
					continue;
				}
				if (curLineLength > width)
				{
					msg.add(oldLine);
					curString = ms[i];
				}
			}
			
			if (!curString.equals(""))
			{
				if (maxLineCount > 0)
				{
					if (msg.size() < maxLineCount)
						msg.add(curString);
				}
				else
					msg.add(curString);
			}
			
			height = msg.size() * TTF.getLetterHeight();
		}
	}
}