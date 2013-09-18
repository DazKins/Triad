package com.dazkins.triadeditor;

import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL11;

import com.dazkins.triad.game.world.World;
import com.dazkins.triad.game.world.tile.Tile;
import com.dazkins.triad.gfx.BufferObject;
import com.dazkins.triad.gfx.Image;
import com.dazkins.triad.gfx.ViewportInfo;
import com.dazkins.triad.input.InputHandler;

public class TriadEditor implements Runnable {
	private static String TITLE = "Triad Editor";
	private static int WIDTH = 1280;
	private static int HEIGHT = 720;
	
	private boolean running;
	
	private Application app;
	private ControlPanel controlPanel;
	
	private InputHandler input;
	private ViewportInfo OGLViewport;
	
	public static void main(String args[]) {
		TriadEditor te = new TriadEditor();
		te.start();
	}
	
	private void stop() {
		running = false;
	}
	
	private void start() {
		running = true;
		new Thread(this).start();
	}
	
	public void init() {
		try {
			Display.setDisplayMode(new DisplayMode(WIDTH, HEIGHT));
			Display.setLocation(0, 0);
			Display.create();
		} catch (LWJGLException e) {
			e.printStackTrace();
		}

		BufferObject.init();
		
		Image.init();
		
		Tile.initDatabase();
		
		World.init();
		
		input = new InputHandler();
		OGLViewport = new ViewportInfo(0, 0, WIDTH, HEIGHT);
		
		app = new Application(input, OGLViewport);
		
		controlPanel = new ControlPanel(WIDTH, 0);
		
		app.initControlPanel(controlPanel);
		
		initOpenGL();
	}
	
	private void initOpenGL() {
		GL11.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
		GL11.glMatrixMode(GL11.GL_PROJECTION);
		GL11.glLoadIdentity();
		GL11.glOrtho(0, WIDTH, 0, HEIGHT, -10.0f, 10.0f);
		GL11.glEnable(GL11.GL_TEXTURE_2D);
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		GL11.glEnable(GL11.GL_DEPTH_TEST);
		GL11.glEnable(GL11.GL_CULL_FACE);
		GL11.glCullFace(GL11.GL_BACK);
	}

	public void run() {
		init();
		long lastTime = System.nanoTime();
		double nsPerTick = 1000000000D / 60D;
		int frames = 0;
		int ticks = 0;
		long lastTimer = System.currentTimeMillis();
		double delta = 0;

		mainLoop : while (running) {
			long now = System.nanoTime();
			delta += (now - lastTime) / nsPerTick;
			lastTime = now;
			while (delta >= 1) {
				ticks++;
				tick();
				if(running == false)
					break mainLoop;
				delta -= 1;
			}
			frames++;
			render();

			if (System.currentTimeMillis() - lastTimer > 1000) {
				lastTimer += 1000;
				System.out.println("FPS: " + frames + " UPS: " + ticks);
				frames = 0;
				ticks = 0;
			}
		}
		
		System.exit(0);
	}
	
	private void render() {
		GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
		app.render();
		Display.update();
	}
	
	private void tick() {
		if (Display.isCloseRequested())
			running = false;
		
		input.tick();
		app.tick();
	}
}