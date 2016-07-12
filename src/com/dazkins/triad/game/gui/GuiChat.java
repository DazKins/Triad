package com.dazkins.triad.game.gui;

import java.util.ArrayList;

import org.lwjgl.system.glfw.GLFW;

import com.dazkins.triad.Triad;
import com.dazkins.triad.game.chat.Chat;
import com.dazkins.triad.game.chat.ChatMessage;
import com.dazkins.triad.game.gui.object.GuiObjectTextBox;
import com.dazkins.triad.game.gui.object.GuiObjectTextEntry;
import com.dazkins.triad.game.gui.renderformat.RenderFormatManager;
import com.dazkins.triad.gfx.TTF;
import com.dazkins.triad.input.InputHandler;

public class GuiChat extends Gui
{
	private GuiObjectTextBox mainChat;
	private GuiObjectTextEntry textEntry;
	
	private Chat chat;
	
	private static final float TEXT_SIZE = 0.7f;
	
	public GuiChat(Triad t, InputHandler i, Chat c)
	{
		super(t, i);
		this.chat = c;
		mainChat = new GuiObjectTextBox(this);
		mainChat.setWidth(600);
		
		textEntry = new GuiObjectTextEntry(this);
		textEntry.setWidth(600);

		RenderFormatManager.TEXT.setSize(TEXT_SIZE);
		mainChat.setY(TTF.getLetterHeight());
		RenderFormatManager.TEXT.reset();
		
		setupGraphics();
	}

	public void onExit()
	{
		
	}

	public void setupGraphics()
	{
		RenderFormatManager.TEXT.setSize(TEXT_SIZE);
		RenderFormatManager.BOX.setRenderStyle(3);
		
		ArrayList<ChatMessage> msgs = chat.getChatMessages();
		String content = "";
		for (ChatMessage s : msgs)
		{
			String sender = s.getSender();
			String msg = s.getMessage();
			content += "#ff006a " + sender + ": #ffffff " + msg + " \\n ";
		}
		
		mainChat.setContent(content);
		
		super.setupGraphics();

		RenderFormatManager.BOX.reset();
		RenderFormatManager.TEXT.reset();
	}
	
	public void render()
	{
		
		if (chat.getAndPurgeHasChatChanged())
		{
			setupGraphics();
		}
		
		RenderFormatManager.TEXT.setSize(TEXT_SIZE);
		
		RenderFormatManager.BOX.setRenderStyle(3);
		mainChat.render();
		
		if (input.isTypedQueueOpen())
			RenderFormatManager.BOX.setRenderStyle(4);
		textEntry.render();
		
		
		RenderFormatManager.BOX.reset();
		RenderFormatManager.TEXT.reset();
	}
	
	public void tick()
	{
		super.tick();
		
		if (input.isKeyJustDownIgnoreTypedQueue(GLFW.GLFW_KEY_T))
		{
			input.openTypedQueue();
		}
		
		if (input.isKeyJustDownIgnoreTypedQueue(GLFW.GLFW_KEY_ESCAPE))
		{
			input.closeTypedQueue();
		}
		
		if (input.isKeyJustDownIgnoreTypedQueue(GLFW.GLFW_KEY_ENTER))
		{
			String cont = textEntry.getContent();
			textEntry.setContent("");
			textEntry.setupGraphics();
			chat.sendMessage(cont);
			input.closeTypedQueue();
		}
	}
}