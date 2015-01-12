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
		loader = new Loader("startup_load");
		loader.addLoad(new ParticlePoolLoader());
		
		gui = new GuiLoading(triad, loader);
	}

	public void render() {
		gui.render(null);
	}
	
	public void tick() {
		if (!started) {
			Loader.startThreadLoad(loader);
			started = true;
		}
		if (loader.getPercent() >= 1.0f) {
			triad.setGameState(new GameStatePlaying());
			loader.stop();
		}
	}
}