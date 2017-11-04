package com.dazkins.triad.game;

import com.dazkins.triad.Triad;
import com.dazkins.triad.game.gui.GuiMainMenu;
import com.dazkins.triad.game.gui.renderformat.RenderFormatManager;
import com.dazkins.triad.gfx.BufferObject;
import com.dazkins.triad.gfx.Color;
import com.dazkins.triad.gfx.Image;
import com.dazkins.triad.gfx.RenderContext;
import com.dazkins.triad.gfx.Window;
import com.dazkins.triad.input.InputHandler;
import com.dazkins.triad.math.Matrix3;

public class GameStateMainMenu implements GameState
{
	private Triad triad;
	
	private GuiMainMenu mainMenu;

	private InputHandler input;
	
	//TODO DELETE ALL THIS
	private BufferObject testBO;

	public void init(Triad triad, InputHandler inp)
	{
		this.triad = triad;
		Window win = triad.win;
		input = inp;
		
		mainMenu = new GuiMainMenu(triad, input, this);
		
		testBO = new BufferObject(4 * 4);
		
		testBO.resetData();
		
//		testBO.getData().setUV(0, 0).setXY(0, 0).pushVertex();
//		testBO.getData().setUV(1, 0).setXY(100, 0).pushVertex();
//		testBO.getData().setUV(1, 1).setXY(100, 100).pushVertex();
//		testBO.getData().setUV(0, 1).setXY(0, 100).pushVertex();
//		
//		testBO.getData().bindImage(Image.getImageFromName("missing_texture"));
//		
//		testBO.compile();
		
//		Image.getImageFromName("missing_texture").loadSpriteBufferObject(testBO, 0, 0, 100, 100, 0, 0, 16, 16, 0, 0);
		
//		testBO.compile();
	}
	
	public void onPlayButtonPress()
	{
		triad.setGameState(new GameStatePlaying());
	}

	public void render(RenderContext rc)
	{
		RenderFormatManager.TEXT.setColour(new Color(0));
		mainMenu.render(rc, null);
		
//		rc.getMatrixStack().push();
//		rc.getMatrixStack().transform(Matrix3.scale(3.0f));
//		rc.getMatrixStack().transform(Matrix3.translate(100.0f, 100.0f));
//		rc.addToRender(testBO);
//		rc.getMatrixStack().pop();
//		rc.addToRender(testBO);
	}

	public void tick()
	{
		mainMenu.tick();
		input.tick();
	}
}