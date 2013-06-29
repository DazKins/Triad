package com.dazkins.triad.game;

import com.dazkins.triad.game.world.World;
import com.dazkins.triad.gfx.Bitmap;
import com.dazkins.triad.input.InputHandler;

public class Game {
	private World world;
	private InputHandler input;
	
	public Game(InputHandler i) {
		world = new World();
		input = i;
	}
	
	public void tick() {
		world.tick();
	}
	
	public void render(Bitmap b) {
		world.render(b);
	}
}