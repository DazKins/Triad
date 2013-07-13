package com.dazkins.triad;

import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL11;

import com.dazkins.triad.game.GameState;
import com.dazkins.triad.game.GameStatePlaying;
import com.dazkins.triad.gfx.Image;
import com.dazkins.triad.gfx.GLRenderer;

public class Triad {
	private boolean running;
	private final String title = "Triad Pre-Alpha";
	public final int WIDTH = 1280;
	public final int HEIGHT = 720;

	private GameState currentState;
	
	public static void main(String args[]) {
		Triad mc = new Triad();
		mc.start();
	}
	
	public Triad() {
		
		currentState = new GameStatePlaying();
		currentState.init(this);
		
		try {
			Display.setDisplayMode(new DisplayMode(WIDTH, HEIGHT));
			Display.setResizable(false);
			Display.create();
		} catch (LWJGLException e) {
			e.printStackTrace();
		}
		
		GL11.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
		GL11.glMatrixMode(GL11.GL_PROJECTION);
		GL11.glLoadIdentity();
		GL11.glOrtho(0, WIDTH, 0, HEIGHT, -1.0f, 1.0f);
		GL11.glEnable(GL11.GL_TEXTURE_2D);
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		
		if(!Image.init())
			System.out.println("Failed to initialize art!");
//		GL11.glBindTexture(GL11.GL_TEXTURE_2D, Image.spriteSheet.texID);
	}
	
	private void stop() {
		running = false;
	}

	private void start() {
		running = true;
		this.run();
	}

	public void run() {
		long lastTime = System.nanoTime();
		double nsPerTick = 1000000000D / 60D;
		int frames = 0;
		int ticks = 0;
		long lastTimer = System.currentTimeMillis();
		double delta = 0;

		while (running) {
			long now = System.nanoTime();
			delta += (now - lastTime) / nsPerTick;
			lastTime = now;
			while (delta >= 1) {
				ticks++;
				tick();
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
	}

	private void render() {
		GLRenderer t = GLRenderer.instance;
		t.open();
		currentState.render(t);
		t.draw();
		Display.update();
	}

	private void tick() {
		currentState.tick();
		
		if(Display.isCloseRequested())
			stop();
	}
}