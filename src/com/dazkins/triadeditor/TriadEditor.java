package com.dazkins.triadeditor;

import org.lwjgl.Sys;
import org.lwjgl.opengl.GL11;

import com.dazkins.triad.game.world.World;
import com.dazkins.triad.game.world.tile.Tile;
import com.dazkins.triad.gfx.BufferObject;
import com.dazkins.triad.gfx.Image;
import com.dazkins.triad.gfx.Window;
import com.dazkins.triad.input.InputHandler;

public class TriadEditor implements Runnable {
	private static int WIDTH = 1280;
	private static int HEIGHT = 720;
	
	private boolean running;
	
	private Application app;
	private ControlPanel controlPanel;
	
	private InputHandler input;
	private Window OGLWindow;
	
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
		Sys.touch();
		
		OGLWindow = new Window(WIDTH, HEIGHT);
		OGLWindow.setup();

		BufferObject.init();
		
		Image.init();
		
		input = new InputHandler(OGLWindow);
		
		app = new Application(input, OGLWindow);
		
		controlPanel = new ControlPanel(WIDTH, 0);
		
		app.initControlPanel(controlPanel);
		
		initOpenGL();
	}
	
	private void initOpenGL() {
		GL11.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
		GL11.glMatrixMode(GL11.GL_PROJECTION);
		GL11.glLoadIdentity();
		GL11.glOrtho(0, WIDTH, 0, HEIGHT, -1000.0f, 1000.0f);
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
		OGLWindow.update();
	}
	
	private void tick() {
		if (OGLWindow.wasCloseRequested())
			running = false;
		
		input.tick();
		app.tick();
	}
}