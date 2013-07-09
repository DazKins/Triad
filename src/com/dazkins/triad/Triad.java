package com.dazkins.triad;

import java.awt.Canvas;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;

import javax.swing.JFrame;

import com.dazkins.triad.game.GameState;
import com.dazkins.triad.game.GameStatePlaying;
import com.dazkins.triad.gfx.Art;
import com.dazkins.triad.gfx.bitmap.Bitmap;

public class Triad extends Canvas implements Runnable {
	private boolean running;
	private final String title = "Triad Pre-Alpha";
	public final int WIDTH = 424;
	public final int HEIGHT = 240;
	private final int SCALE = 3;
	
	private BufferedImage screenImage;
	private Bitmap screenBitmap;

	private GameState currentState;
	
	public static void main(String args[]) {
		Triad mc = new Triad();
		JFrame frame = new JFrame(mc.title);
		
		frame.setSize(new Dimension(mc.WIDTH * mc.SCALE, mc.HEIGHT * mc.SCALE));
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.add(mc);
		frame.setResizable(false);
		frame.setVisible(true);
		frame.pack();
		
		mc.start();
	}
	
	public Triad() {
		Dimension size = new Dimension(WIDTH * SCALE, HEIGHT * SCALE);
		setPreferredSize(size);
		setMinimumSize(size);
		setMaximumSize(size);
		
		if(!Art.init())
			System.out.println("Failed to initialize art!");
		currentState = new GameStatePlaying();
		currentState.init(this);
		
		screenImage = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
		screenBitmap = new Bitmap(screenImage);
	}
	
	private void stop() {
		running = false;
	}

	private void start() {
		running = true;
		new Thread(this).start();
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
		BufferStrategy bs = getBufferStrategy();
		
		if (bs == null) {
			createBufferStrategy(3);
			return;
		}
		
		Graphics g = bs.getDrawGraphics();
		
		currentState.render(screenBitmap);
		
		g.drawImage(screenImage, 0, 0, WIDTH * SCALE, HEIGHT * SCALE, null);
		
		g.dispose();
		bs.show();
	}

	private void tick() {
		currentState.tick();
	}
}