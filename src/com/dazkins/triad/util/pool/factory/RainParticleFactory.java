//package com.dazkins.triad.util.pool.factory;
//
//import com.dazkins.triad.game.entity.particle.Particle;
//import com.dazkins.triad.game.entity.particle.RainParticleBehaviourController;
//import com.dazkins.triad.game.world.tile.Tile;
//import com.dazkins.triad.gfx.Camera;
//
//public class RainParticleFactory extends ParticleFactory
//{
//	public Particle create(Particle p, Camera c, float x, float y, float w, float h, float r, float g, float b)
//	{
//		p.create(x, y, w, h, r, g, b, 0.0f, Tile.yPosToDepthRelativeToCamera(c, y - 512));
//		p.assignPBC(new RainParticleBehaviourController());
//		return p;
//	}
//}