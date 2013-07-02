package com.dazkins.triad.game;

import com.dazkins.triad.Triad;
import com.dazkins.triad.game.entity.mob.Player;
import com.dazkins.triad.game.gui.Gui;
import com.dazkins.triad.game.gui.PlayerGui;
import com.dazkins.triad.game.world.World;
import com.dazkins.triad.gfx.Camera;
import com.dazkins.triad.gfx.bitmap.Bitmap;
import com.dazkins.triad.input.InputHandler;

public class Game {
	private World world;
	private InputHandler input;
	private Player player;
	private Camera cam;
	private Triad triad;
	private Gui gui;
	
	public Game(Triad triad, InputHandler i) {
		world = new World();
		input = i;
		player = new Player(world, 0, 16, input);
		world.addEntity(player);
		this.triad = triad;
		cam = new Camera(input, 0, 0, triad.WIDTH, triad.HEIGHT);
		cam.setBounds(0, 0, world.MWIDTH << 4, world.MHEIGHT << 4);
		gui = new PlayerGui(triad, input, player);
	}
	
	public void tick() {
		world.tick();
		cam.lockCameraToEntity(player);
		gui.tick();
	}
	
	public void render(Bitmap b) {
		world.render(b, cam);
		gui.render(b);
	}
}