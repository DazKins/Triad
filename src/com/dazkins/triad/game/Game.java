package com.dazkins.triad.game;

import com.dazkins.triad.game.entity.Player;
import com.dazkins.triad.game.world.World;
import com.dazkins.triad.gfx.Bitmap;
import com.dazkins.triad.input.InputHandler;

public class Game {
	private World world;
	private InputHandler input;
	private Player player;
	
	public Game(InputHandler i) {
		world = new World();
		input = i;
		player = new Player(0, 16, input);
		world.addEntity(player);
	}
	
	public void tick() {
		world.tick();
	}
	
	public void render(Bitmap b) {
		int hi;
		int hello;
		world.render(b);
	}
}