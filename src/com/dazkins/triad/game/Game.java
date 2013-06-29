package com.dazkins.triad.game;

import com.dazkins.triad.game.entity.Player;
import com.dazkins.triad.game.world.World;
import com.dazkins.triad.gfx.Bitmap;
import com.dazkins.triad.gfx.Camera;
import com.dazkins.triad.input.InputHandler;

public class Game {
	private World world;
	private InputHandler input;
	private Player player;
	private Camera cam;
	
	public Game(InputHandler i) {
		world = new World();
		input = i;
		player = new Player(0, 16, input);
		world.addEntity(player);
		cam = new Camera(input, 0, 0);
	}
	
	public void tick() {
		world.tick();
		cam.tick();
	}
	
	public void render(Bitmap b) {
		world.render(b, cam);
	}
}