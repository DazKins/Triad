package com.dazkins.triad;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.ByteBuffer;

import javax.imageio.ImageIO;

import org.lwjgl.BufferUtils;
import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.PixelFormat;

import com.dazkins.triad.game.GameState;
import com.dazkins.triad.game.GameStatePlaying;
import com.dazkins.triad.game.world.World;
import com.dazkins.triad.game.world.tile.Tile;
import com.dazkins.triad.gfx.BufferObject;
import com.dazkins.triad.gfx.Font;
import com.dazkins.triad.gfx.Image;
import com.dazkins.triad.gfx.WindowInfo;
import com.dazkins.triad.gfx.model.Model;
import com.dazkins.triad.util.TriadProfiler;

public class Triad {
	private boolean running;
	private final String title = "Triad Pre-Alpha";
	public WindowInfo winInfo = new WindowInfo(800, 450);
	
	private boolean rescaled;

	private GameState currentState;
	
	public static void main(String args[]) {
		Triad triad = new Triad();
		triad.start();
	}
	
	public Triad() {
		try {
			Display.setResizable(true);
			Display.setDisplayMode(new DisplayMode(winInfo.getW(), winInfo.getH()));
			Display.setTitle(title);
			Display.setIcon(loadIcon("/art/icon.png"));
			Display.create();
		} catch (LWJGLException e) {
			e.printStackTrace();
		}
		
		initOpenGL();
		
		initProg();
	}
	
	private void initProg() {
		BufferObject.init();
		
		Tile.initDatabase();
		
		World.init();
		
		if(!Image.init())
			System.out.println("Failed to initialize art!");
		
		Model.loadModels();
		
		currentState = new GameStatePlaying();
		currentState.init(this);
		
		Font.initializeFont();
	}
	
	private void initOpenGL() {
		GL11.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
		GL11.glMatrixMode(GL11.GL_PROJECTION);
		GL11.glLoadIdentity();
		GL11.glOrtho(0, winInfo.getW(), 0, winInfo.getH(), -100.0f, 100.0f);
		GL11.glEnable(GL11.GL_TEXTURE_2D);
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		GL11.glEnable(GL11.GL_DEPTH_TEST);
		GL11.glEnable(GL11.GL_CULL_FACE);
		GL11.glCullFace(GL11.GL_BACK);
	}
	
	public boolean wasRescaled() {
		return rescaled;
	}
	
	private void stop() {
		running = false;
		Display.destroy();
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

		mainLoop : while (running) {
			long now = System.nanoTime();
			delta += (now - lastTime) / nsPerTick;
			lastTime = now;
			while (delta >= 1) {
				ticks++;
				TriadProfiler.instance.startStamp("game_tick");
				tick();
				TriadProfiler.instance.endStamp();
				if(running == false)
					break mainLoop;
				delta -= 1;
			}
			frames++;
			TriadProfiler.instance.startStamp("game_render");
			render();
			TriadProfiler.instance.endStamp();
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
		if (Display.wasResized()) {
			resyncOpenGL();
			rescaled = true;
		}
	}
	
	private void render() {
		GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
		
		currentState.render();
		
		Display.update();
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
		winInfo.setW(Display.getWidth());
		winInfo.setH(Display.getHeight());
		GL11.glLoadIdentity();
		GL11.glMatrixMode(GL11.GL_PROJECTION);
		GL11.glOrtho(0, winInfo.getW(), 0, winInfo.getH(), -1000.0f, 1000.0f);
		GL11.glViewport(0, 0, winInfo.getW(), winInfo.getH());
	}

	private void tick() {
		currentState.tick();
		
		rescaled = false;
		
		if(Display.isCloseRequested()) {
			stop();
		}
	}
}