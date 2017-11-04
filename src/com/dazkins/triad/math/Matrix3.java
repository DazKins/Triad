package com.dazkins.triad.math;

import java.nio.FloatBuffer;

import org.lwjgl.BufferUtils;
import org.lwjgl.util.vector.Matrix;

public class Matrix3
{
	private float vals[];
	
	private Matrix3()
	{
		vals = new float[9];
	}
	
	public static Matrix3 identity()
	{
		Matrix3 m = new Matrix3();
		
		m.setVal(0, 0, 1.0f);
		m.setVal(1, 1, 1.0f);
		m.setVal(2, 2, 1.0f);
		
		return m;
	}
	
	public static Matrix3 scale(float x, float y)
	{
		Matrix3 m = identity();
		
		m.setVal(0, 0, x);
		m.setVal(1, 1, y);
		
		return m;
	}
	
	public static Matrix3 translate(float x, float y)
	{
		Matrix3 m = identity();
		
		m.setVal(0, 2, x);
		m.setVal(1, 2, y);
		
		return m;
	}
	
	public static Matrix3 rotate(float r)
	{
		Matrix3 m = identity();
		
		float cos = (float) Math.cos(r);
		float sin = (float) Math.sin(r);
		
		m.setVal(0, 0, cos);
		m.setVal(0, 1, -sin);
		
		m.setVal(1, 0, sin);
		m.setVal(1, 1, cos);
		
		return m;
	}
	
	//TODO maybe this is deprecated
	public static Matrix3 screenSpace(float w, float h)
	{
		Matrix3 trans = Matrix3.translate(-1.0f, -1.0f);
		Matrix3 scale = Matrix3.scale(2.0f / w, 2.0f / h);

//		return Matrix3.mult(scale, trans);
		return Matrix3.mult(trans, scale);
	}
	
	public static Matrix3 scale(float s)
	{
		return scale(s, s);
	}
	
	public static Matrix3 mult(Matrix3 m0, Matrix3 m1)
	{
		Matrix3 m = identity();
		
		for (int i = 0; i < 3; i++)
		{
			for (int u = 0; u < 3; u++)
			{
				float dot = 0.0f;
				
				for (int j = 0; j < 3; j++)
				{
					dot += m0.getVal(i, j) * m1.getVal(j, u);
				}
				
				m.setVal(i, u, dot);
			}
		}
		
		return m;
	}
	
	private void setVal(int x, int y, float v)
	{
		vals[x + y * 3] = v;
	}
	
	private float getVal(int x, int y)
	{
		return vals[x + y * 3];
	}
	
	public Matrix3 clone()
	{
		Matrix3 m = new Matrix3();
		
		for (int i = 0; i < 9; i++)
		{
			m.vals[i] = vals[i];
		}
		
		return m;
	}
	
	public FloatBuffer dropToBuffer()
	{
		FloatBuffer b = BufferUtils.createFloatBuffer(9);
		b.put(vals);
		b.flip();
		return b;
	}
	
	public String toString()
	{
		String s = "";
		for (int y = 0; y < 3; y++)
		{
			for (int x = 0; x < 3; x++)
			{
				s += getVal(x, y) + ",";
			}
			s += "\n";
		}
		return s;
	}
}