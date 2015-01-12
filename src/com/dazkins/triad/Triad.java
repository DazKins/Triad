package com.dazkins.triad;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.Random;

import javax.imageio.ImageIO;

import org.lwjgl.BufferUtils;
import org.lwjgl.Sys;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GLContext;

import com.dazkins.triad.audio.SoundManager;
import com.dazkins.triad.game.GameState;
import com.dazkins.triad.game.GameStateLoading;
import com.dazkins.triad.gfx.BufferObject;
import com.dazkins.triad.gfx.Font;
import com.dazkins.triad.gfx.Image;
import com.dazkins.triad.gfx.Window;
import com.dazkins.triad.input.InputHandler;
import com.dazkins.triad.math.NoiseRandom;

public class Triad implements Runnable {
	private boolean running;
	private final String title = "Triad Pre-Alpha";
	public Window win = new Window(1280, 720);

	private GameState currentState;
	
	private InputHandler input;
	
	public static void main(String args[]) {
		Triad triad = new Triad();
		new Thread(triad).run();
	}
	
	public Triad() {
		SoundManager.registerSound("/audio/music/triad_theme.wav", "theme");
//		SoundManager.getAudio("theme").play();

		Sys.touch();
		
		win.setup();
		
		input = new InputHandler(win);
		
		initOpenGL();
		
		initProg();
	}
	
	private void initProg() {
		Image.init();
		
		BufferObject.init();
		
		Font.initializeFont();
		
		setGameState(new GameStateLoading());
	}
	
	public void setGameState(GameState g) {
		currentState = g;
		currentState.init(this);
	}
	
	private void initOpenGL() {
        GLContext.createFromCurrent();
		GL11.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
		GL11.glMatrixMode(GL11.GL_PROJECTION);
		GL11.glLoadIdentity();
		GL11.glOrtho(0, win.getW(), 0, win.getH(), -1000f, 1000f);
		GL11.glEnable(GL11.GL_TEXTURE_2D);
		GL11.glEnable(GL11.GL_DEPTH_TEST);
		GL11.glEnable(GL11.GL_CULL_FACE);
		GL11.glCullFace(GL11.GL_BACK);
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        
        System.err.println("OpenGL Context Succefully Created!");
	}
	
	private void stop() {
		running = false;
        win.destroy();
	}

	public void run() {
		running = true;
		this.runLoop();
	}

	private void runLoop() {
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
				long l = System.currentTimeMillis();
				tick();
				if(running == false)
					break mainLoop;
				delta -= 1;
			}
			frames++;
			long l = System.currentTimeMillis();
			render();
			checkWindow();

			if (System.currentTimeMillis() - lastTimer > 1000) {
				lastTimer += 1000;
				System.out.println("FPS: " + frames + " UPS: " + ticks);
				frames = 0;
				ticks = 0;
			}
		}
	}
	
	private void checkWindow() {
		if (win.wasResized()) {
			resyncOpenGL();
		}
	}
	
	private void render() {
		GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
		
		currentState.render();
		
		win.update();
	}
	
	private ByteBuffer[] loadIcon(String p) {
		ByteBuffer[] rValue;
		
		BufferedImage img = null;
		
		try {
			img = ImageIO.read(Triad.class.getResource(p));
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		int pixels[] = img.getRGB(0, 0, img.getWidth(), img.getHeight(), null, 0, img.getWidth());
		
		rValue = new ByteBuffer[1];
		rValue[0] = BufferUtils.createByteBuffer(pixels.length * 4);
		
		for (int i = 0; i < pixels.length; i++) {
			byte aa = (byte) ((pixels[i] >> 24) & 0xFF);
			byte rr = (byte) ((pixels[i] >> 16) & 0xFF);
			byte gg = (byte) ((pixels[i] >> 8) & 0xFF);
			byte bb = (byte) (pixels[i] & 0xFF);
			
			rValue[0].put(rr);
			rValue[0].put(gg);
			rValue[0].put(bb);
			rValue[0].put(aa);
		}
		rValue[0].flip();
		
		return rValue;
	}

	private void resyncOpenGL() {
		win.setW(win.getW());
		win.setH(win.getH());
		GL11.glLoadIdentity();
		GL11.glMatrixMode(GL11.GL_PROJECTION);
		GL11.glOrtho(0, win.getW(), 0, win.getH(), -1000.0f, 1000.0f);
		GL11.glViewport(0, 0, win.getW(), win.getH());
	}

	private void tick() {
		input.tick();
		
		win.tickState();
		
		currentState.tick();
		
		if(win.wasCloseRequested()) {
			stop();
		}
	}
}