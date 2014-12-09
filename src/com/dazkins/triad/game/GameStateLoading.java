package com.dazkins.triad.game;

import com.dazkins.triad.Triad;
import com.dazkins.triad.game.entity.particle.ParticlePoolLoader;
import com.dazkins.triad.game.gui.GuiLoading;
import com.dazkins.triad.game.world.World;
import com.dazkins.triad.util.Loader;

public class GameStateLoading implements GameState {
	private Triad triad;
	private Loader loader;
	
	private GuiLoading gui;
	
	private boolean started;
	
	public void init(Triad triad) {
		this.triad = triad;
		loader = new Loader();
		loader.addLoad(new ParticlePoolLoader());
		loader.addLoad(World.testWorld);
		
		gui = new GuiLoading(triad, loader);
	}

	public void render() {
		gui.render(null);
	}

	public void tick() {
		if (!started) {
			new Thread(loader).start();
			started = true;
		}
		if (loader.getPercent() >= 1.0f)
			triad.setGameState(new GameStatePlaying());
	}
}