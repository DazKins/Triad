package com.dazkins.triad.game;

import com.dazkins.triad.Triad;
import com.dazkins.triad.game.gui.GuiMainMenu;
import com.dazkins.triad.game.gui.renderformat.BoxRenderFormat;
import com.dazkins.triad.game.gui.renderformat.RenderFormatManager;
import com.dazkins.triad.gfx.BufferObject;
import com.dazkins.triad.gfx.Color;
import com.dazkins.triad.gfx.Image;
import com.dazkins.triad.gfx.RenderContext;
import com.dazkins.triad.gfx.Window;
import com.dazkins.triad.gfx.model.Quad;
import com.dazkins.triad.input.InputHandler;
import com.dazkins.triad.math.MathHelper;
import com.dazkins.triad.math.Matrix3;

public class GameStateMainMenu implements GameState
{
	private Triad triad;
	
	private GuiMainMenu mainMenu;

	private InputHandler input;
	
	//TODO DELETE ALL THIS
//	private BufferObject testBO;
//	private BufferObject testB02;

	private Quad testQuad;
	private Quad testQuad2;

	public void init(Triad triad, InputHandler inp)
	{
		this.triad = triad;
		Window win = triad.win;
		input = inp;
		
		mainMenu = new GuiMainMenu(triad, input, this);
		
//		testBO = new BufferObject(4 * 4);
//
//		testBO.resetData();

//		testBO.getData().setUV(0, 0).setXY(0, 0).pushVertex();
//		testBO.getData().setUV(1, 0).setXY(100, 0).pushVertex();
//		testBO.getData().setUV(1, 1).setXY(100, 100).pushVertex();
//		testBO.getData().setUV(0, 1).setXY(0, 100).pushVertex();

//		testBO.getData().bindImage(Image.getImageFromName("missing_texture"));

//		testBO.compile();
		
//		Image.getImageFromName("iconSheet").loadSpriteBufferObject(testBO, 100, 100, 100, 100, 24, 0, 32, 32, 0, 0);
//
//		testBO.compile();
//
//		testB02 = new BufferObject(4 * 4);
//		testB02.resetData();
//		Image.getImageFromName("iconSheet").loadSpriteBufferObject(testB02, 0, 0, 100, 100, 0, 0, 32, 32, 0, 0);
//		testB02.compile();

		testQuad = new Quad(200, 200, 256, 256, 32, 0, 8, 8);
		testQuad.init(Image.getImageFromName("iconSheet"));
		testQuad.generate();
//
		testQuad2 = new Quad(0, 0, 256, 256, 24, 0, 8, 8);
		testQuad2.init(Image.getImageFromName("iconSheet"));
		testQuad2.generate();
	}
	
	public void onPlayButtonPress()
	{
		triad.setGameState(new GameStatePlaying());
	}

	float t = 0.0f;

	public void render(RenderContext rc)
	{
		RenderFormatManager.TEXT.setColour(new Color(0));
		mainMenu.render(rc, null);
		
//		rc.getMatrixStack().push();
//		rc.getMatrixStack().transform(Matrix3.scale(3.0f));
//		rc.getMatrixStack().transform(Matrix3.translate(100.0f, 100.0f));
//		rc.addToRender(testBO);
//		rc.getMatrixStack().pop();
//		rc.addToRender(testB02);

//		testQuad.render(rc);
//		rc.getMatrixStack().push();
//		rc.getMatrixStack().transform(Matrix3.translate(-128, -128));
//		rc.getMatrixStack().transform(Matrix3.rotate(t += 0.01f));
//		rc.getMatrixStack().transform(Matrix3.translate(128, 128.0f));
//		testQuad2.render(rc);
//		rc.getMatrixStack().pop();
	}

	public void tick()
	{
		mainMenu.tick();
		input.tick();
	}
}