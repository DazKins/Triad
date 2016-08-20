//package com.dazkins.triad.game.entity.particle;
//
//public class RainParticleBehaviourController extends ParticleBehaviourController
//{
//	public void tick()
//	{
//		float x = operatingParticle.getX();
//		float y = operatingParticle.getY();
//		float a = operatingParticle.getA();
//		long l = operatingParticle.getLifeTicks();
//
//		y -= Math.random() * 4 + 2;
//		if (l > 60)
//		{
//			a -= 0.01f;
//
//			if (a <= 0.01f)
//			{
//				operatingParticle.markForDestruction();
//				operatingParticle.remove();
//			}
//		} else
//		{
//			if (a < 0.5f)
//				a += 0.01f;
//		}
//
//		operatingParticle.lifeTicks = l;
//		operatingParticle.setA(a);
//		operatingParticle.setX(x);
//		operatingParticle.setY(y);
//	}
//}