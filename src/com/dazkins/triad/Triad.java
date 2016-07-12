package com.dazkins.triad;

import org.lwjgl.Sys;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GLContext;

import com.dazkins.triad.audio.SoundManager;
import com.dazkins.triad.game.GameState;
import com.dazkins.triad.game.GameStateMainMenu;
import com.dazkins.triad.game.GameStatePlaying;
import com.dazkins.triad.game.gui.object.GuiObjectBox;
import com.dazkins.triad.gfx.BufferObject;
import com.dazkins.triad.gfx.Image;
import com.dazkins.triad.gfx.TTF;
import com.dazkins.triad.gfx.Window;
import com.dazkins.triad.input.InputHandler;
import com.dazkins.triad.networking.client.TriadClient;
import com.dazkins.triad.util.TriadLogger;
import com.dazkins.triad.util.debugmonitor.DebugMonitor;

public class Triad implements Runnable
{
	private boolean running;
	private final String title = "Triad Pre-Alpha";
	public Window win;

	private GameState currentState;

	private InputHandler input;

	public static float zMin = -10f;
	public static float zMax = 70f;

	private TriadClient client;

	public static void main(String args[])
	{
		TriadLogger.initClientLog();
		Triad triad = new Triad();
		new Thread(triad).run();
	}

	public Triad()
	{
		client = new TriadClient("Player" + (int) (Math.random() * 10000));

		SoundManager.registerSound("/audio/music/triad_theme.wav", "theme");
//		 SoundManager.getAudio("theme").play();

//		win = new Window(1920, 1080, true);
		win = new Window(1280, 720, false);

		Sys.touch();

		win.setup();

		input = new InputHandler(win);

		initOpenGL();

		initProg();
	}

	private void initProg()
	{
		Image.init();

		BufferObject.init();

		TTF.init();
		
		GuiObjectBox.init();

//		setGameState(new GameStateLoading());
		setGameState(new GameStateMainMenu());
		
		DebugMonitor.setWindow(win);
	}

	public void setGameState(GameState g)
	{
		currentState = g;
		currentState.init(this, input);

		if (currentState instanceof GameStatePlaying)
		{
			GameStatePlaying gsp = (GameStatePlaying) currentState;
			gsp.initClient(client);
		}
	}

	private void initOpenGL()
	{
		GLContext.createFromCurrent();
		GL11.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
		GL11.glMatrixMode(GL11.GL_PROJECTION);
		GL11.glLoadIdentity();
		GL11.glOrtho(0, win.getW(), 0, win.getH(), zMin, zMax);
		GL11.glEnable(GL11.GL_TEXTURE_2D);
		GL11.glEnable(GL11.GL_DEPTH_TEST);
		GL11.glEnable(GL11.GL_CULL_FACE);
		GL11.glCullFace(GL11.GL_BACK);
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);

		TriadLogger.log("OpenGL Context Succefully Created!", false);
	}

	private void stop()
	{
		running = false;
		win.destroy();
		client.stop();
	}

	public void run()
	{
		running = true;
		this.runLoop();
	}

	private void runLoop()
	{
		long lastTime = System.nanoTime();
		double nsPerTick = 1000000000D / 60D;
		int frames = 0;
		int ticks = 0;
		long lastTimer = System.currentTimeMillis();
		double delta = 0;

		mainLoop: while (running)
		{
			DebugMonitor.enable();
			long now = System.nanoTime();
			delta += (now - lastTime) / nsPerTick;
			lastTime = now;
			while (delta >= 1)
			{
				ticks++;
				tick();
				checkWindow();
				if (running == false)
					break mainLoop;
				delta -= 1;
			}
			frames++;
			render();

			if (System.currentTimeMillis() - lastTimer > 1000)
			{
				lastTimer += 1000;
				DebugMonitor.setVariableValue("FPS", frames);
				DebugMonitor.setVariableValue("UPS", ticks);
				frames = 0;
				ticks = 0;
			}
		}

		System.exit(0);
	}

	private void checkWindow()
	{
		if (win.wasResized())
		{
			resyncOpenGL();
		}
	}

	private void render()
	{
		GL11.glClear(/** GL11.GL_COLOR_BUFFER_BIT | **/ GL11.GL_DEPTH_BUFFER_BIT);

		currentState.render();

		DebugMonitor.render();
		
		win.update();
	}

	// TODO Reimplement icon loading

	// private ByteBuffer[] loadIcon(String p) {
	// ByteBuffer[] rValue;
	//
	// BufferedImage img = null;
	//
	// try {
	// img = ImageIO.read(Triad.class.getResource(p));
	// } catch (IOException e) {
	// e.printStackTrace();
	// }
	//
	// int pixels[] = img.getRGB(0, 0, img.getWidth(), img.getHeight(), null, 0,
	// img.getWidth());
	//
	// rValue = new ByteBuffer[1];
	// rValue[0] = BufferUtils.createByteBuffer(pixels.length * 4);
	//
	// for (int i = 0; i < pixels.length; i++) {
	// byte aa = (byte) ((pixels[i] >> 24) & 0xFF);
	// byte rr = (byte) ((pixels[i] >> 16) & 0xFF);
	// byte gg = (byte) ((pixels[i] >> 8) & 0xFF);
	// byte bb = (byte) (pixels[i] & 0xFF);
	//
	// rValue[0].put(rr);
	// rValue[0].put(gg);
	// rValue[0].put(bb);
	// rValue[0].put(aa);
	// }
	// rValue[0].flip();
	//
	// return rValue;
	// }

	private void resyncOpenGL()
	{
		GL11.glLoadIdentity();
		GL11.glMatrixMode(GL11.GL_PROJECTION);
		GL11.glOrtho(0, win.getW(), 0, win.getH(), zMin, zMax);
		GL11.glViewport(0, 0, win.getW(), win.getH());
	}

	private void tick()
	{
		currentState.tick();
		
		input.tick();

		if (win.wasCloseRequested())
		{
			stop();
		}

		win.tickState();
	}
}