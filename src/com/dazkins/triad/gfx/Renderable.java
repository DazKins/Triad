package com.dazkins.triad.gfx;

import com.dazkins.triad.math.Matrix3;

public class Renderable
{
	private BufferObject bufferObject;
	private Matrix3 matrix;
	
	public Renderable(BufferObject bo, Matrix3 m)
	{
		this.bufferObject = bo;
		this.matrix = m;
	}
	
	public void render()
	{
//		float[] tmpData = bufferObject.getData().data;
//		for (float f : tmpData)
//			System.out.println(f);
//		System.out.println();
		ShaderProgram.getCurrentShader().setUniform("mat", matrix);
		bufferObject.render();
	}
}