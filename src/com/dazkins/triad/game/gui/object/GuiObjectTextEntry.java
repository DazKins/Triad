package com.dazkins.triad.game.gui.object;

import com.dazkins.triad.game.gui.Gui;
import com.dazkins.triad.game.gui.renderformat.RenderFormatManager;
import com.dazkins.triad.input.InputHandler;

public class GuiObjectTextEntry extends GuiObjectTextBox
{
	private InputHandler input;
	
	public GuiObjectTextEntry(Gui g)
	{
		super(g);
		input = this.gui.getInputHandler();
		setContent("");
	}
	
	public void tick()
	{
		if (input.isTypedQueueOpen())
		{
			setShowCursor(true);
			String cur = this.getContent();
			
			while (!input.isTypedQueueEmpty())
			{
				cur += input.popTypedQueue();
			}
			
			int b = input.getAndPurgeBackspaceCount();
			if (cur.length() > 0)
				cur = cur.substring(0, cur.length() - b);
			
			if (!this.getContent().equals(cur))
			{
				RenderFormatManager.TEXT.setSize(0.7f);
				setContent(cur);
				setupGraphics();
				RenderFormatManager.TEXT.reset();
			}
		} else 
		{
			setShowCursor(false);	
		}
	}
}