package com.dazkins.triad.game.entity;

import java.util.ArrayList;
import java.util.List;

import com.dazkins.triad.game.world.World;

public abstract class TriggerableEntity extends Entity {
	protected List<ActiveateableEntity> affectedEntities;
	
	public TriggerableEntity(World w, float x, float y, String s) {
		super(w, x, y, s);
		affectedEntities = new ArrayList<ActiveateableEntity>();
	}
	
	public void addAffectedEntity(ActiveateableEntity ae) {
		affectedEntities.add(ae);
	}
	
	public void onTrigger() {
		for (int i = 0; i < affectedEntities.size(); i++) {
			affectedEntities.get(i).onActivate();
		}
	}
}